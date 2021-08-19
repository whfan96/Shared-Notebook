package com.example.lungteng.finalapp;

import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyPublic extends AppCompatActivity {
    Intent intent = new Intent();
    EditText title, content;
    DatabaseReference drf;
    String user_uid, key, date, userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_public);
        findViews();
        Intent i=this.getIntent();
        Bundle b=i.getExtras();
        user_uid=b.getString("Uid");
        key=b.getString("Key");
        data_show();
    }
    void findViews() {
        drf= FirebaseDatabase.getInstance().getReference();
        title = (EditText)findViewById(R.id.editTitle);
        content = (EditText)findViewById(R.id.editText);
        Date();
    }
    void data_show(){
        drf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title.setText(dataSnapshot.child("notes").child("uid").child(user_uid).child(key).child("title").getValue().toString());
                content.setText(dataSnapshot.child("notes").child("uid").child(user_uid).child(key).child("content").getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}});
    }
    public void onCancel(View v) {
        intent.setClass(ModifyPublic.this, DataPublicNotes.class);
        startActivity(intent);
        ModifyPublic.this.finish();
    }
    void Date(){
        final Calendar c =Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        int mYear,mMonth,mDay,mTime,aMin;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mTime=c.get(Calendar.HOUR_OF_DAY);
        aMin=c.get(Calendar.MINUTE);
        date=mYear+"/"+mMonth+"/"+mDay+" "+mTime+":"+aMin;
        Toast.makeText(ModifyPublic.this,date,Toast.LENGTH_LONG).show();
    }
    public void onSave(View v) {
        drf.child("notes").child("uid").child(user_uid).child(key).child("title").setValue(title.getText().toString());
        drf.child("notes").child("uid").child(user_uid).child(key).child("content").setValue(content.getText().toString());
        drf.child("notes").child("uid").child(user_uid).child(key).child("modify_date").setValue(date);
        drf.child("notes").child("uid").child(user_uid).child(key).child("modify_people").setValue(userUID);


        intent.setClass(ModifyPublic.this, PublicNotes.class);
        startActivity(intent);
        ModifyPublic.this.finish();
    }
}
