package com.example.lungteng.finalapp;

import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyPersonal extends AppCompatActivity {
    Intent intent = new Intent();
    EditText title, content;
    DatabaseReference drf;
    Button btnPermission;
    String akey, permissions, date, userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_personal);
        findViews();
        Intent i = this.getIntent();
        Bundle b = i.getExtras();
        akey = b.getString("key");
        data_show();
    }
    void data_show(){
        drf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    title.setText(dataSnapshot.child("notes").child("uid").child(userUID).child(akey).child("title").getValue().toString());
                    content.setText(dataSnapshot.child("notes").child("uid").child(userUID).child(akey).child("content").getValue().toString());
                    permissions = dataSnapshot.child("notes").child("uid").child(userUID).child(akey).child("permissions").getValue().toString();
                    if (permissions.equals("true")) {
                        btnPermission.setText("非公開");
                    } else {
                        btnPermission.setText("公開");
                    }
                }catch (Exception e){
                    e.getMessage();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}});
    }
    void findViews() {
        drf= FirebaseDatabase.getInstance().getReference();
        title = (EditText)findViewById(R.id.editTitle);
        content = (EditText)findViewById(R.id.editText);
        btnPermission=(Button)findViewById(R.id.btnPermission);
        Date();
    }

    public void onCancel(View v) {
        intent.setClass(ModifyPersonal.this, DataPersonalNotes.class);
        startActivity(intent);
        ModifyPersonal.this.finish();
    }
    public void permiss(View v){
        String s=btnPermission.getText().toString();
        if(s.equals("公開")){
            permissions="true";
            btnPermission.setText("非公開");
        }else {
            permissions="false";
            btnPermission.setText("公開");
        }
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
        Toast.makeText(ModifyPersonal.this,date,Toast.LENGTH_LONG).show();
    }
    public void onSave(View v) {
        drf.child("notes").child("uid").child(userUID).child(akey).child("title").setValue(title.getText().toString());
        drf.child("notes").child("uid").child(userUID).child(akey).child("content").setValue(content.getText().toString());
        drf.child("notes").child("uid").child(userUID).child(akey).child("permissions").setValue(permissions);
        drf.child("notes").child("uid").child(userUID).child(akey).child("modify_date").setValue(date);
        intent.setClass(ModifyPersonal.this, DataPersonalNotes.class);
        startActivity(intent);
        ModifyPersonal.this.finish();
    }
}
