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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPersonalNotes extends AppCompatActivity {
    Intent intent = new Intent();
    EditText title, content;
    DatabaseReference drf;
    String userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    String permissions="false", date;
    Button btnPermission;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_personal);
        findViews();
        Intent i = this.getIntent();
        Bundle b = i.getExtras();
        num = b.getInt("num");
        num++;

    }
    void findViews() {
        drf = FirebaseDatabase.getInstance().getReference();
        title = (EditText)findViewById(R.id.editTitle);
        content = (EditText)findViewById(R.id.editText);
        btnPermission = (Button)findViewById(R.id.btnPermission);
        Date();
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
        Toast.makeText(EditPersonalNotes.this,date,Toast.LENGTH_LONG).show();
    }

    public void onCancel(View v) {
        intent.setClass(EditPersonalNotes.this, DataPersonalNotes.class);
        startActivity(intent);
        EditPersonalNotes.this.finish();
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

    public void onSave(View v) {
        drf.child("notes").child("uid").child(userUID).child(""+num).child("title").setValue(title.getText().toString());
        drf.child("notes").child("uid").child(userUID).child(""+num).child("content").setValue(content.getText().toString());
        drf.child("notes").child("uid").child(userUID).child(""+num).child("create_date").setValue(date);
        drf.child("notes").child("uid").child(userUID).child(""+num).child("permissions").setValue(permissions);
        drf.child("notes").child("uid").child(userUID).child(""+num).child("modify_date").setValue(date);
        drf.child("notes").child("uid").child(userUID).child(""+num).child("modify_people").setValue(userUID);

        intent.setClass(EditPersonalNotes.this, DataPersonalNotes.class);
        startActivity(intent);
        EditPersonalNotes.this.finish();
    }

}
