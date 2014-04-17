package com.loyola.ezcards.ezcardsapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class ProfileActivity extends Activity{

    private Button btnCall;
    private Button btnMsg;
    private Button btmEmail;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

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
                callIntent.setData(Uri.parse("tel:2517764076"));
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
                    sendIntent.setData(Uri.parse("sms:2517764076"));
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

                String to = "cbijayananda@luc.edu";
                String subject = "EZCards Email";
                String message = "Niiiiice!!";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

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
}
