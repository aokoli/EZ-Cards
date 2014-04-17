package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by newbuyer on 4/17/14.
 */
public class DeleteContactActivity extends Activity{
    private  DatabaseHandler db;
    ContactListAdapterCheck adapter;
    ArrayList<Contact> contacts;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycardcheck);

       db = new DatabaseHandler(this);
        final ListView lvCustomList = (ListView) findViewById(R.id.lv_custom_list);

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        contacts = db.getAllContacts();
        adapter = new ContactListAdapterCheck(DeleteContactActivity.this, contacts);
        lvCustomList.setAdapter(adapter);

        lvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent1 = null;
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                //Object listItem = lvCustomList.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Clicked position: " + position + " and ContactId : " + arg, Toast.LENGTH_LONG).show();
                intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent1);
            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_delete,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete1:
                for(int i = 0; i < contacts.size(); i++)

                {
                    if (adapter.mCheckStates.get(i) == true) {
                        db.deleteContact(contacts.get(i));

                    } else {

                    }
                }
                startActivity(new Intent("com.loyola.ezcards.ezcardsapp.Mycards"));
        }
        return false;
    }
}
