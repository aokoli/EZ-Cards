package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see com.loyola.ezcards.ezcardsapp.util.SystemUiHider
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnCapture;
    private Button btnMyCards;
    private Button btnSelectCards;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnMyCards = (Button) findViewById(R.id.btnMycard);
        btnSelectCards = (Button) findViewById(R.id.btnSelect);

        btnCapture.setOnClickListener(this);
        btnMyCards.setOnClickListener(this);
        btnSelectCards.setOnClickListener(this);


        DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact("Ravi", "9100000000"));
        db.addContact(new Contact("Srinivas", "9199999999"));
        db.addContact(new Contact("Tommy", "9522222222"));
        db.addContact(new Contact("Karthik", "9533333333"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
//
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCapture:
                Intent intent = new Intent(this, OCRServiceActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMycard:
                Intent intent1 = new Intent(this,MycardsAct.class);
                startActivity(intent1);
                break;
            case R.id.btnSelect:
                Intent intent2 = new Intent(this,SelectcardsAct.class);
                startActivity(intent2);
                break;
        }

    }

}

