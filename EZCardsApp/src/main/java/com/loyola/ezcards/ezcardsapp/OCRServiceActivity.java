package com.loyola.ezcards.ezcardsapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see com.loyola.ezcards.ezcardsapp.util.SystemUiHider
 */
public class OCRServiceActivity extends Activity {

    public static final String PACKAGE_NAME = "com.loyola.ezcards.ezcardsapp";
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/SimpleAndroidOCR/";

    // You should have the trained data file in assets folder
    // You can get them at:
    // http://code.google.com/p/tesseract-ocr/downloads/list
    public static final String lang = "eng";

    private static final String TAG = "SimpleAndroidOCR.java";

    protected Button _button;
    protected ImageView _image;
    protected EditText _field;
    protected EditText phoneField;
    protected EditText emailField;
    protected String _path;
    protected boolean _taken;

    protected static final String PHOTO_TAKEN = "photo_taken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }

        // lang.traineddata file with the app (in assets folder)
        // You can get them at:
        // http://code.google.com/p/tesseract-ocr/downloads/list
        // This area needs work and optimization
        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
                //GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(DATA_PATH
                        + "tessdata/" + lang + ".traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                //while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                //gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }
        super.onCreate(savedInstanceState);

        _path = DATA_PATH + "/ocr.jpg";
        startCameraActivity();

//
    }

    // Simple android photo capture:
    // http://labs.makemachine.net/2010/03/simple-android-photo-capture/

    protected void startCameraActivity() {
        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "resultCode: " + resultCode);

        if (resultCode == -1) {
            onPhotoTaken();
        } else {
            Log.v(TAG, "User cancelled");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(OCRServiceActivity.PHOTO_TAKEN, _taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(TAG, "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(OCRServiceActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }


    protected void onPhotoTaken() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

        try {
            ExifInterface exif = new ExifInterface(_path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            Log.v(TAG, "Orient: " + exifOrientation);

            int rotate = 0;

            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }

            Log.v(TAG, "Rotation: " + rotate);

            if (rotate != 0) {

                // Getting width & height of the given image.
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                // Setting pre rotate
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);

                // Rotating Bitmap
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
            }

            // Convert to ARGB_8888, required by tess
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        } catch (IOException e) {
            Log.e(TAG, "Couldn't correct orientation: " + e.toString());
        }

        // _image.setImageBitmap( bitmap );

        Log.v(TAG, "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(DATA_PATH, lang);
        baseApi.setImage(bitmap);

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();
        setContentView(R.layout.capturecard);
        _image = (ImageView) findViewById(R.id.imgPreview);
        phoneField = (EditText) findViewById(R.id.editText3);
        emailField = (EditText) findViewById(R.id.editText8);
        _field = (EditText) findViewById(R.id.editText7);
        _image.setImageBitmap(bitmap);

        // You now have the text in recognizedText var, you can do anything with it.
        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
        // so that garbage doesn't make it to the display.

        Log.v(TAG, "OCRED TEXT: " + recognizedText);

        if ( lang.equalsIgnoreCase("eng") ) {
            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
        }

        recognizedText = recognizedText.trim();

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
        m = p.matcher(recognizedText);

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
        m = p.matcher(recognizedText);

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
        phoneField.setText(phone);
        emailField.setText(emailID);
        Log.d(TAG, "Input: " + recognizedText);
        Log.d(TAG, output);

        // Cycle done.
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

}
