package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MyCardsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycards);

        DatabaseHandler db = new DatabaseHandler(this);
        final ListView lvCustomList = (ListView) findViewById(R.id.lv_custom_list);

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        final ArrayList<Contact> contacts = db.getAllContacts();
        ContactListAdapter adapter = new ContactListAdapter(MyCardsActivity.this, contacts);
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
        inf.inflate(R.menu.menu_contact,menu);
        return true;
    }
}