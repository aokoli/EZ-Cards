package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MyCardsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycards);

        DatabaseHandler db = new DatabaseHandler(this);
        final ListView lvCustomList = (ListView) findViewById(R.id.lv_custom_list);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
//        db.addContact(new Contact("Ravi", "91"));
//        db.addContact(new Contact("Srinivas", "99"));
//        db.addContact(new Contact("Tommy", "95"));
//        db.addContact(new Contact("Karthik", "93"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        final ArrayList<Contact> contacts = db.getAllContacts();
        ContactListAdapter adapter = new ContactListAdapter(MyCardsActivity.this, contacts);
        lvCustomList.setAdapter(adapter);

        lvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent1 = null;
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Object listItem = lvCustomList.getItemAtPosition(position);
                intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent1);
            }

        });

    }
}