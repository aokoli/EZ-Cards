package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class SelectCardsActivity extends Activity {

    boolean isNewContact = false;
    private Contact contact;
    private Button btnSaveContact;
    private final static String tag = "SelectCardsActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardcapture);

        btnSaveContact = (Button) findViewById(R.id.btnSave);

        btnSaveContact.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        onSaveContact(view);    //view is
                    }
                });
    }

    public void onSaveContact(View view){
        contact = new Contact();
        Log.v(tag, "view: " + view.findViewById(R.id.FirstName));
        //if(Integer.valueOf(contact.getId()) == null){
            //contact.setId() this must be autoincrement number
            contact.setFirstName(((EditText)findViewById(R.id.FirstName)).getText().toString());
            contact.setLastName(((EditText) findViewById(R.id.LastName)).getText().toString());
            contact.setPhoneMain(((EditText) findViewById(R.id.PhoneMain)).getText().toString());
            contact.setPhoneCell(((EditText) findViewById(R.id.PhoneCell)).getText().toString());
            contact.setFax(((EditText) findViewById(R.id.Fax)).getText().toString());
            contact.setEmail(((EditText) findViewById(R.id.email)).getText().toString());
            contact.setTitle(((EditText) findViewById(R.id.Company1)).getText().toString());
            contact.setCompany1(((EditText) findViewById(R.id.Company2)).getText().toString());
            contact.setCompany2(((EditText) findViewById(R.id.Company3)).getText().toString());
            contact.setAddress1(((EditText) findViewById(R.id.addressT)).getText().toString());
            //address1 and address2 separate?
            //change the layout to contain title field.

            Log.v(tag, "Contact: " + contact.getFirstName() + " " + contact.getLastName());
            DatabaseHandler db = new DatabaseHandler(this);
            db.addContact(contact);
            Intent intent1 = new Intent(this, MyCardsActivity.class);
            startActivity(intent1);
//        }
//        else{
//
//        }
    }

}
