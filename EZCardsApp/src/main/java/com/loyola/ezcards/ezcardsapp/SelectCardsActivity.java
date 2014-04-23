package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectCardsActivity extends Activity {

    boolean isNewContact = false;
    private Contact contact;
    private Button btnSaveContact;
    private final static String tag = "SelectCardsActivity";

    protected ImageView _image;
    protected EditText _field;
    protected EditText nameField;
    protected EditText phoneField;
    protected EditText emailField;

    private String recognizedText = null;
    private String imagePath = null;
    private Bitmap bitmap;
    private static final String IMAGE_DIRECTORY_NAME = "EZ Cards";
    File mediaFile;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardcapture);


        Intent intent = getIntent();
        recognizedText = intent.getStringExtra("RecognizedText");
        imagePath = intent.getStringExtra("OCRImagePath");

        Log.v(tag, " imagePath : " + imagePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        bitmap = BitmapFactory.decodeFile(imagePath, options);


        if(recognizedText != null){
            fieldMap();
        }

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

    public void fieldMap(){

        _image = (ImageView) findViewById(R.id.profileImageView);
        nameField = (EditText) findViewById(R.id.FirstName);
        phoneField = (EditText) findViewById(R.id.PhoneMain);
        emailField = (EditText) findViewById(R.id.email);
        _field = (EditText) findViewById(R.id.addressT);
        _image.setImageBitmap(bitmap);

        // You now have the text in recognizedText var, you can do anything with it.
        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
        // so that garbage doesn't make it to the display.

        Log.v(tag, "OCRED TEXT: " + recognizedText);

        /*if ( lang.equalsIgnoreCase("eng") ) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }*/

        String allText = "";

        allText = allText + "\n" + recognizedText;
        //recognizedText = recognizedText.trim();

        if ( recognizedText.length() != 0 ) {
            _field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
            _field.setSelection(_field.getText().toString().length());
        }

        Pattern p;
        Matcher m;

        String DisplayName = null;
        String phone = null;
        String emailID = null;

	    /*
	     * Name-matching Expression - Matches: T.V. Raman Alan Viverette Charles L.
	     * Chen Julie Lythcott-Haimes - Does not match: Google Google User
	     * Experience Team 650-720-5555 cell
	     */
        p = Pattern.compile("^([A-Z]([a-z]*|\\.) *){1,2}([A-Z][a-z]+-?)+$", Pattern.MULTILINE);
        m = p.matcher(allText);

        if (m.find()) {
            DisplayName = m.group().toString();
        }

	    /*
	     * Email-matching Expression - Matches: email: raman@google.com
	     * spam@google.co.uk v0nn3gu7@ice9.org name @ host.com - Does not match:
	     * #@/.cJX Google c@t
	     */
        //p = Pattern.compile("([A-Za-z0-9]+ *@ *[A-Za-z0-9]+(\\.[A-Za-z]{2,4})+)$", Pattern.MULTILINE);
        //p = Pattern.compile("(.+ *@ *.+(\\..{2,4})+)$", Pattern.MULTILINE);
        p = Pattern.compile("([^ \n]+ *@ *.+(\\..{2,4})+)$", Pattern.MULTILINE);
        m = p.matcher(allText);

        if (m.find()) {
            emailID = m.group(1);
        }

	    /*
	     * Phone-matching Expression - Matches: 1234567890 (650) 720-5678
	     * 650-720-5678 650.720.5678 - Does not match: 12345 12345678901 720-5678
	     */
        p = Pattern.compile("(?:^|\\D)(\\d{3})[)\\-. ]*?(\\d{3})[\\-. ]*?(\\d{4})(?:$|\\D)");
        m = p.matcher(recognizedText);

        if (m.find()) {
            phone = "(" + m.group(1) + ") " + m.group(2) + "-" + m.group(3);
        }


        //displays results for testing
        String output = new String();
        output = "Name: " + DisplayName + "\n" + "Phone: " + phone + "\n" + "Email: " + emailID + "\n";

        nameField.setText(DisplayName);
        phoneField.setText(phone);
        emailField.setText(emailID);
        Log.d(tag, "Input: " + allText);
        Log.d(tag, output);

        // Cycle done.
    }

    public void saveImageToExtStorage(){
        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");

            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        Log.v(tag, "FilePAth " + mediaFile.getPath());
        //mediaFile.getPath();
        try {
            FileOutputStream out = new FileOutputStream(mediaFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onSaveContact(View view){

        saveImageToExtStorage();

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
        contact.setTitle(((EditText) findViewById(R.id.titlefield)).getText().toString());
        contact.setCompany1(((EditText) findViewById(R.id.Company1)).getText().toString());
        contact.setCompany2(((EditText) findViewById(R.id.Company2)).getText().toString());
        contact.setAddress1(((EditText) findViewById(R.id.addressT)).getText().toString());
        contact.setImageLocation(mediaFile.getPath());
        //address1 and address2 separate?
        //change the layout to contain title field.

        Log.v(tag, "Contact: " + contact.getFirstName() + " " + contact.getLastName()+" "+contact.getId());
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
