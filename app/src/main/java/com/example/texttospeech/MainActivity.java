package com.example.texttospeech;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   RelativeLayout mRelativeLayout ;
   TextToSpeech mTextToSpeech;
   TextView mTextView;
   DatabaseReference reff;
   Context mContext;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=(TextView)findViewById(R.id.edittext);
        mRelativeLayout = findViewById(R.id.relative) ;


       mTextToSpeech=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
           @Override
           public void onInit(int i) {
             //if there is no error
             if(i!=TextToSpeech.ERROR)
             {
               mTextToSpeech.setLanguage(Locale.ENGLISH);
             }
           }
       });
       mRelativeLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               reff= FirebaseDatabase.getInstance().getReference();
               reff.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       double val=Double.parseDouble(String.valueOf(dataSnapshot.child("val").getValue().toString()));
                       double mid=425.0;
                       double x1=Double.parseDouble(String.valueOf(dataSnapshot.child("x1").getValue().toString()));
                       double x2=Double.parseDouble(String.valueOf(dataSnapshot.child("x2").getValue().toString()));
                       String text;
                       if(x1<mid&&x2<mid)
                       {
                           text=dataSnapshot.child("text").getValue().toString()+" "+"Detected"+" "+"Left";
                       }else if(x1>mid&&x2>mid)
                       {
                           text=dataSnapshot.child("text").getValue().toString()+" "+"Detected"+" "+"Right";
                       }else
                       {
                           text=dataSnapshot.child("text").getValue().toString()+" "+"Detected"+" "+"Center";
                       }

                       val/=850;
                       if(val>0.7)
                       {
                           mTextToSpeech.setPitch((float)5.0);
                       }else
                       {
                           mTextToSpeech.setPitch((float)1.0);
                       }
                       mTextToSpeech.setSpeechRate(0.1f);
                       mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                       mTextView.setText(text);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                   }
               });

           }
       });

       reff= FirebaseDatabase.getInstance().getReference();
       reff.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               double val=Double.parseDouble(String.valueOf(dataSnapshot.child("val").getValue().toString()));
               double mid=425.0;
               double x1=Double.parseDouble(String.valueOf(dataSnapshot.child("x1").getValue().toString()));
               double x2=Double.parseDouble(String.valueOf(dataSnapshot.child("x2").getValue().toString()));
               String text;
               if(x1<mid&&x2<mid)
               {
                   text=dataSnapshot.child("text").getValue().toString()+" "+"Detected"+" "+"Left";
               }else if(x1>mid&&x2>mid)
               {
                   text=dataSnapshot.child("text").getValue().toString()+" "+"Detected"+" "+"Right";
               }else
               {
                   text=dataSnapshot.child("text").getValue().toString()+" "+"Detected"+" "+"Center";
               }

               val/=850;
               if(val>0.7)
               {
                    mTextToSpeech.setPitch((float)5.0);
               }else
               {
                   mTextToSpeech.setPitch((float)1.0);
               }
               mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
               mTextView.setText(text);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
           }
       });

    }

    @Override
    protected void onPause() {
        if(mTextToSpeech!=null)
        {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        super.onPause();
    }
}
