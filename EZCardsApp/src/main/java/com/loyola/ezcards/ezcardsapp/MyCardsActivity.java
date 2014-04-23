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

import java.util.ArrayList;


public class MyCardsActivity extends Activity {
    DatabaseHandler db;
    private final  static String tag = "Mycardactivity";
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycards);

        db = new DatabaseHandler(this);
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
                Contact c = (Contact) lvCustomList.getItemAtPosition(position);
                intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                intent1.putExtra("contact", c);

                startActivity(intent1);

            }

        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_mycards,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.addContact:
                startActivity(new Intent("com.loyola.ezcards.ezcardsapp.SELECT"));
                return true;

            case R.id.SelectContacts:

                startActivity(new Intent("com.loyola.ezcards.ezcardsapp.DELACT"));
                return true;

            case R.id.mainMenu:
                startActivity(new Intent("android.intent.action.MAIN" ));
                return true;
        }
        return false;
    }
}