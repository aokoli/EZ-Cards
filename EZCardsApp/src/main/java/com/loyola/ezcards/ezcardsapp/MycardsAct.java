package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by alshaymaaalhazzaa on 4/4/14.
 */
public class MycardsAct extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycards);

        DatabaseHandler db = new DatabaseHandler(this);
        ListView lvCustomList = (ListView) findViewById(R.id.lv_custom_list);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact("Ravi", "91"));
        db.addContact(new Contact("Srinivas", "99"));
        db.addContact(new Contact("Tommy", "95"));
        db.addContact(new Contact("Karthik", "93"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        ArrayList<Contact> contacts = db.getAllContacts();
        ContactListAdapter adapter = new ContactListAdapter(MycardsAct.this, contacts);
        lvCustomList.setAdapter(adapter);

//        lvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}