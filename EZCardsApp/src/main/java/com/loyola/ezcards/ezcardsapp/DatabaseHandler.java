package com.loyola.ezcards.ezcardsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper{

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Initial Create:
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FNAME + " TEXT,"
                + KEY_LNAME + " TEXT,"
                + KEY_PH_MAIN + " TEXT,"
                + KEY_PH_CELL + " TEXT,"
                + KEY_FAX + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_COMPANY1 + " TEXT,"
                + KEY_COMPANY2 + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_ADDRESS1 + " TEXT,"
                + KEY_ADDRESS2 + " TEXT,"
                + KEY_IMG_LOCN + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    /**
     * addContact()
     * return: void
     * @param contact
     */
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
            values.put(KEY_FNAME, contact.getFirstName());
            values.put(KEY_LNAME, contact.getLastName());
            values.put(KEY_PH_MAIN, contact.getPhoneMain());
            values.put(KEY_PH_CELL, contact.getPhoneCell());
            values.put(KEY_FAX, contact.getFax());
            values.put(KEY_EMAIL, contact.getEmail());
            values.put(KEY_COMPANY1, contact.getCompany1());
            values.put(KEY_COMPANY2, contact.getCompany2());
            values.put(KEY_TITLE, contact.getTitle());
            values.put(KEY_ADDRESS1, contact.getAddress1());
            values.put(KEY_ADDRESS2, contact.getAddress2());
            values.put(KEY_IMG_LOCN, contact.getImageLocation());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    /**
     * getAllContacts()
     * returns: ArrayList<Contact>
     * parameters: none
     */
    public ArrayList<Contact> getAllContacts() {

        ArrayList<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setFirstName(cursor.getString(1));
                contact.setLastName(cursor.getString(2));
                contact.setPhoneMain(cursor.getString(3));
                contact.setPhoneCell(cursor.getString(4));
                contact.setFax(cursor.getString(5));
                contact.setEmail(cursor.getString(6));
                contact.setCompany1(cursor.getString(7));
                contact.setCompany2(cursor.getString(8));
                contact.setTitle(cursor.getString(9));
                contact.setAddress1(cursor.getString(10));
                contact.setAddress2(cursor.getString(11));
                contact.setImageLocation(cursor.getString(12));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_FNAME, KEY_PH_MAIN}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }


    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, contact.getFirstName());
        values.put(KEY_PH_MAIN, contact.getPhoneMain());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EZContactsManager";
    private static final String TABLE_CONTACTS = "EZContacts";

    //Contacts Table Columns
    private static final String KEY_ID = "Id";
    private static final String KEY_FNAME = "FirstName";
    private static final String KEY_LNAME = "LastName";
    private static final String KEY_PH_MAIN = "PhoneMain";
    private static final String KEY_PH_CELL = "PhoneCell";
    private static final String KEY_FAX = "Fax";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_COMPANY1 = "Company1";
    private static final String KEY_COMPANY2 = "Company2";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_ADDRESS1 = "Address1";
    private static final String KEY_ADDRESS2 = "Address2";
    private static final String KEY_IMG_LOCN = "ImageLocation";

}
