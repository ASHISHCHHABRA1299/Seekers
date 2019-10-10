package com.example.texttospeech;

import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   EditText mEditText;
   Button btn;
   TextToSpeech mTextToSpeech;
   TextView mTextView;
   DatabaseReference reff;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=(TextView)findViewById(R.id.edittext);


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
       reff= FirebaseDatabase.getInstance().getReference();
       reff.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String text=dataSnapshot.child("text").getValue().toString();
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
