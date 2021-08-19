package com.example.lungteng.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataPersonalNotes extends AppCompatActivity {
    String userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference drf;
    int num;
    String[] aMemo, aTitle, aTime, akey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        drf = FirebaseDatabase.getInstance().getReference();
        read_notes();
    }
    public void read_notes(){

        drf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    int i = 0;
                    int sum = Integer.parseInt("" + dataSnapshot.child("notes").child("uid").child(userUID).getChildrenCount());
                    aMemo = new String[sum];
                    aTitle = new String[sum];
                    aTime = new String[sum];
                    akey = new String[sum];
                    for (DataSnapshot ds : dataSnapshot.child("notes").child("uid").child(userUID).getChildren()) {
                        akey[i] = ds.getKey().toString();
                        aTitle[i] = ds.child("title").getValue().toString();
                        aMemo[i] = ds.child("content").getValue().toString();
                        aTime[i] = ds.child("create_date").getValue().toString();
                        i++;
                        num = Integer.parseInt(ds.getKey().toString());
                    }
                    change();
                }catch (Exception e){
                    e.getMessage();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    void change(){
        Intent i = new Intent();
        i.setClass(DataPersonalNotes.this, PersonalNotes.class);
        Bundle b = new Bundle();
        b.putStringArray("Title",aTitle);
        b.putStringArray("Memo",aMemo);
        b.putStringArray("Time",aTime);
        b.putStringArray("key",akey);
        b.putInt("num",num);
        i.putExtras(b);
        startActivity(i);
        DataPersonalNotes.this.finish();
    }
}
