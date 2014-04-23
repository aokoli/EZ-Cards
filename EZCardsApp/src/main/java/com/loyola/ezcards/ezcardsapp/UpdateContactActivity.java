package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class UpdateContactActivity extends Activity{
    Contact contact;
    private static Context mContext;
    ActionMode mActionMode;
    String imgPath;
    public static final int TAKE_PHOTO_CODE = 1;
    public static final int PICK_FROM_GALLERY = 2;
    ImageView imageView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateprofile);

        contact = (Contact) getIntent().getSerializableExtra("contact");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(contact.getImageLocation(), options);

        imageView = (ImageView) findViewById(R.id.profileImageView);
        imageView.setImageBitmap(bitmap);
        EditText fnameView = (EditText) findViewById(R.id.FirstName);
        fnameView.setText(contact.getFirstName());
        EditText lnameView = (EditText) findViewById(R.id.LastName);
        lnameView.setText(contact.getLastName());
        EditText titleView = (EditText) findViewById(R.id.titlefield);
        titleView.setText(contact.getTitle());
        EditText companyView = (EditText) findViewById(R.id.Company1);
        companyView.setText(contact.getCompany1());
        EditText companyView2 = (EditText) findViewById(R.id.Company2);
        companyView2.setText(contact.getCompany1());
        EditText phoneView = (EditText) findViewById(R.id.PhoneMain);
        phoneView.setText(contact.getPhoneCell());
        EditText cphoneView = (EditText) findViewById(R.id.PhoneCell);
        cphoneView.setText(contact.getPhoneCell());
        EditText faxView = (EditText) findViewById(R.id.Fax);
        faxView.setText(contact.getFax());
        EditText emailView = (EditText) findViewById(R.id.email);
        emailView.setText(contact.getEmail());
        EditText addressView = (EditText) findViewById(R.id.addressT);
        addressView.setText(contact.getAddress1());

        //Button image = (Button) findViewById(R.id.button1);



        imageView.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        if (mActionMode != null) {

                        }

                        // Start the CAB using the ActionMode.Callback defined above
                        mActionMode = startActionMode(mActionModeCallback);
                        view.setSelected(true);

                        //getImagelocation and create an image for the selected Image

                    }
                });

    }

    public void onSaveContact() {

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
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateContact(contact);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu_updateoption,menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.done:
                onSaveContact();
                intent = new Intent("com.loyola.ezcards.ezcardsapp.PROF");
                intent.putExtra("contact", contact);
                startActivity(intent);
                return true;

            case R.id.cancel:
                intent = new Intent("com.loyola.ezcards.ezcardsapp.PROF");
                intent.putExtra("contact", contact);
                startActivity(intent);
                return true;
        }
        return false;

    }



    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {



        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_mycards, menu);
            updateMenuTitles(menu);
            return true;
        }

        private void updateMenuTitles(Menu menu) {
            MenuItem edit = menu.findItem(R.id.SelectContacts);
            MenuItem contacts = menu.findItem(R.id.mainMenu);
            edit.setTitle("Take a picture");
            contacts.setTitle("Browse your pictures");


        }
        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.SelectContacts:
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());

                    //Complete this


                    startActivityForResult(intent, TAKE_PHOTO_CODE);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.mainMenu:
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());


                    //pick from gallery.. get location.. etc
                    startActivityForResult(i, PICK_FROM_GALLERY);
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //mActionMode = null;
        }
    };



    public Uri setImageUri() {
        // Store image in dcim
        String counter = "reg";
        File file = new File(Environment.getExternalStorageDirectory() +"/android/data/spaj_foto/spaj_foto("+counter+").png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }
    public String getImagePath() {
        return imgPath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == TAKE_PHOTO_CODE) {
                String selectedImagePath = getImagePath();

                imageView.setImageBitmap(decodeFile(selectedImagePath));

                if (requestCode == PICK_FROM_GALLERY) {
                    selectedImagePath = getImagePath();
                    imageView.setImageBitmap(decodeFile(selectedImagePath));
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }
}
