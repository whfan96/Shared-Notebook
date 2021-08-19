package com.example.lungteng.finalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class PublicNotes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener  {
    Intent intent = new Intent();
    ListView listview;
    EditText editTitle, editText;
    TextView txtTime;
    DatabaseReference drf;
    String[] aMemo, aTitle, aTime, uid_key, key_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findViews();

        Intent intent=this.getIntent();
        Bundle b=intent.getExtras();
        aMemo=b.getStringArray("Memo");
        aTitle=b.getStringArray("Title");
        aTime=b.getStringArray("Time");
        uid_key=b.getStringArray("Uid");
        key_num=b.getStringArray("Key");

        ArrayList<HashMap<String, Object>> listData=new ArrayList<HashMap<String, Object>>();
        for(int i=0; i<aTitle.length; i++) {
            int cont = i+1;
            HashMap<String, Object> myHasMap;
            myHasMap = new HashMap<String, Object>();
            myHasMap.put("TITLE", aTitle[i]);
            myHasMap.put("DATA", aMemo[i]);
            myHasMap.put("TIME",aTime[i]);
//            myHasMap.put("AREA1", cont+".");
            listData.add(myHasMap);

            SimpleAdapter listItemAdapter = new SimpleAdapter(this, listData, R.layout.item_note,
                    new String[]{"TITLE", "DATA", "TIME"}, new int[]{R.id.txtTitle, R.id.txtData, R.id.txtTime});

            listview.setAdapter(listItemAdapter);
            listview.setFastScrollEnabled(true);
        }

        listview.setOnItemClickListener(this);
    }
    void findViews() {
        drf= FirebaseDatabase.getInstance().getReference();
        listview = (ListView) findViewById(R.id.listview);
        editTitle = (EditText)findViewById(R.id.editTitle);
        editText = (EditText)findViewById(R.id.editText);
        txtTime= (TextView)findViewById(R.id.txtTime);
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        intent.setClass(PublicNotes.this, ModifyPublic.class);
        Bundle b=new Bundle();
        b.putString("Uid",uid_key[i]);
        b.putString("Key",key_num[i]);
        intent.putExtras(b);
        startActivity(intent);
        PublicNotes.this.finish();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_note) {
        } else if (id == R.id.nav_notebook) {
            Intent i=new Intent();
            i.setClass(PublicNotes.this, DataPersonalNotes.class);
            startActivity(i);
            PublicNotes.this.finish();
        } else if (id == R.id.nav_signout) {
            Intent i=new Intent();
            i.setClass(PublicNotes.this, Login.class);
            startActivity(i);
            PublicNotes.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
