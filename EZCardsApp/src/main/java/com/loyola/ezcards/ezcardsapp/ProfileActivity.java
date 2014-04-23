package com.loyola.ezcards.ezcardsapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class ProfileActivity extends Activity{

    private Button btnCall;
    private Button btnMsg;
    private Button btmEmail;
    DatabaseHandler db;
    int value;
    Contact contact;
    private final String tag = "Profile activity";

    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(contact.getImageLocation(), options);

        ImageView imageView = (ImageView) findViewById(R.id.profileImageView);
        imageView.setImageBitmap(bitmap);
        contact.getFirstName();
        TextView nameView = (TextView) findViewById(R.id.profilename);
        nameView.setText(contact.getFullName());
        TextView titleView = (TextView) findViewById(R.id.profiletitlefield);
        titleView.setText(contact.getTitle());
        Log.v(tag, "title" + contact.getTitle());
        TextView companyView = (TextView) findViewById(R.id.profilecomapnyfield);
        companyView.setText(contact.getCompany1());
        TextView phoneView = (TextView) findViewById(R.id.profilephonefield);
        phoneView.setText(contact.getPhoneCell());
        TextView faxView = (TextView) findViewById(R.id.profileFaxfield);
        faxView.setText(contact.getFax());
        TextView emailView = (TextView) findViewById(R.id.profileEmailField);
        Log.v(tag, "email: " + contact.getEmail());
        emailView.setText(contact.getEmail());
        TextView addressView = (TextView) findViewById(R.id.profileAddressfield);
        addressView.setText(contact.getAddress1());

        //Phone Call:
        btnCall = (Button) findViewById(R.id.buttonCall);

        //PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);


        //btnCall listener
        btnCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+contact.getPhoneCell()));
                //callIntent.setData(Uri.parse("tel:2517764076"));
                startActivity(callIntent);

            }

        });

        //SMS Message:
        btnMsg = (Button) findViewById(R.id.buttonSend);

        btnMsg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms: "+ contact.getPhoneCell()));
                    sendIntent.putExtra("sms_body", "default content");
                    startActivity(sendIntent);

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        //Email:
        btmEmail = (Button) findViewById(R.id.buttonSendEmail);
        btmEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String to = contact.getEmail();
                String subject = "EZCards Email";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });


    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        private final String tag = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                Log.i(tag, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(tag, "OFFHOOK");
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(tag, "IDLE");

                if (isPhoneCalling) {

                    Log.i(tag, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_profile,menu);
       // updateMenuTitles(menu);
        return true;
    }

//    private void updateMenuTitles(Menu menu) {
//        MenuItem edit = menu.findItem(R.id.editProfile);
//        MenuItem contacts = menu.findItem(R.id.listContacts);
//        edit.setTitle("edit");
//        contacts.setTitle("All contacts");
//
//
//    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.editProfile:
                Intent intent = new Intent("com.loyola.ezcards.ezcardsapp.UPDATE");
                intent.putExtra("contact", contact);
                startActivity(intent);
                return true;

            case R.id.listContacts:
                startActivity(new Intent("com.loyola.ezcards.ezcardsapp.Mycards"));
                return true;

            case R.id.createVcard:
                createVCARD();
                return true;
        }
        return false;

    }

    public void createVCARD(){
        EZVCard ezvCard = new EZVCard();
        File vcardFile = ezvCard.create(contact);

        Toast.makeText(getApplicationContext(),
                "VCard Created for " + vcardFile.getName() + " at " + vcardFile.getPath(),
                Toast.LENGTH_LONG).show();

    }
}
