package com.example.lungteng.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataPublicNotes extends AppCompatActivity {
    String[] aMemo, aTitle, aTime, uid_key, key_num;
    DatabaseReference drf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        drf= FirebaseDatabase.getInstance().getReference();
        read_notes();
    }
    public void read_notes(){

        drf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    String str="";
                    int sum=0,i=0;
                    for (DataSnapshot ds : dataSnapshot.child("notes").child("uid").getChildren()){
                        str=ds.getKey().toString();
                        for(DataSnapshot dsh : dataSnapshot.child("notes").child("uid").child(str).getChildren()){
                            if(dsh.child("permissions").getValue().toString().equals("true"))
                                sum++;}}

                    aMemo=new String[sum];
                    aTitle=new String[sum];
                    aTime=new String[sum];
                    uid_key=new String[sum];
                    key_num=new String[sum];
                    for (DataSnapshot ds : dataSnapshot.child("notes").child("uid").getChildren()){
                        str=ds.getKey().toString();
                        for(DataSnapshot dsh : dataSnapshot.child("notes").child("uid").child(str).getChildren()){
                            if(dsh.child("permissions").getValue().toString().equals("true")){
                                uid_key[i]=str;
                                key_num[i]=dsh.getKey().toString();
                                aTitle[i]=dsh.child("title").getValue().toString();
                                aMemo[i]=dsh.child("content").getValue().toString();
                                aTime[i]=dsh.child("modify_date").getValue().toString();
                                i++;
                            }}}
                    change();
                }
                catch (Exception ex){
                    ex.getMessage();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    void change(){
        Intent i=new Intent();
        i.setClass(DataPublicNotes.this, PublicNotes.class);
        Bundle b=new Bundle();
        b.putStringArray("Title",aTitle);
        b.putStringArray("Memo",aMemo);
        b.putStringArray("Time",aTime);
        b.putStringArray("Uid",uid_key);
        b.putStringArray("Key",key_num);
        i.putExtras(b);
        startActivity(i);
        DataPublicNotes.this.finish();
    }
}
