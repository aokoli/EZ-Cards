package com.loyola.ezcards.ezcardsapp;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import a_vcard.android.provider.Contacts;
import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.ContactStruct;
import a_vcard.android.syncml.pim.vcard.VCardComposer;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class EZVCard {

    private static final String FILE_DIRECTORY_NAME = "EZ VCARDS";
    private static final String tag = "EZVCARDS";
    public static final String EZ_PATH = Environment
            .getExternalStorageDirectory().toString();

    public File create(Contact ezContact) {

        File ezFile = new File(EZ_PATH, FILE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!ezFile.exists()) {
            if (!ezFile.mkdirs()) {
                Log.v(FILE_DIRECTORY_NAME, "Oops! Failed to create "
                        + FILE_DIRECTORY_NAME + " directory");

            }
        }

        File myFile = new File(ezFile.getPath() + File.separator
                + "VCard-" + ezContact.getId() + ezContact.getLastName() + ".vcf");

        try{

            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(myFile), "UTF-8");

            VCardComposer composer = new VCardComposer();

            //create a contact
            ContactStruct contact1 = new ContactStruct();
            contact1.name = ezContact.getFirstName() + " " + ezContact.getLastName();
            contact1.addPhone(Contacts.Phones.TYPE_WORK, ezContact.getPhoneMain(), null, true);
            contact1.addPhone(Contacts.Phones.TYPE_MOBILE, ezContact.getPhoneCell(), null, false);
            contact1.addPhone(Contacts.Phones.TYPE_FAX_HOME, ezContact.getFax(), null, false);
            contact1.addOrganization(Contacts.OrganizationColumns.TYPE_WORK, ezContact.getCompany1() + " " + ezContact.getCompany2(), ezContact.getTitle(), true);
            contact1.addContactmethod(Contacts.KIND_EMAIL, Contacts.ContactMethods.MOBILE_EMAIL_TYPE_INDEX, ezContact.getEmail(), null, true);

            //create vCard representation
            String vcardString = composer.createVCard(contact1, VCardComposer.VERSION_VCARD30_INT);

            //write vCard to the output stream
            writer.write(vcardString);
            writer.write("\n"); //add empty lines between contacts

            writer.close();
            //return myFile;
        }catch (Exception e){
           Log.v(tag, "Exception: " + e);
            return null;
        }

        return myFile;
    }

    public Contact read(File vcfFile){

        VCardParser parser = new VCardParser();
        VDataBuilder builder = new VDataBuilder();

        String file = "example.vcf";

        File readFile = vcfFile;

        try{

            FileInputStream stream = new FileInputStream(readFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream, "UTF-8"));

            String vcardString = "";
            String line;
            while ((line = reader.readLine()) != null) {
                vcardString += line + "\n";
                Log.v(tag, "VCardString: " + vcardString);
            }
            reader.close();

            //parse the string
            boolean parsed = parser.parse(vcardString, "UTF-8", builder);
            if (!parsed) {
                throw new VCardException("Could not parse vCard file: " + file);
            }
        }catch(Exception e){
            Log.v(tag, "Exception: " + e);
            return null;
        }


        //get all parsed contacts
        List<VNode> pimContacts = builder.vNodeList;

        //do something for all the contacts
        for (VNode contact : pimContacts) {
            ArrayList<PropertyNode> props = contact.propList;

            //contact name - FN property
            String name = null;
            for (PropertyNode prop : props) {
                if ("FN".equals(prop.propName)) {
                    name = prop.propValue;
                    //we have the name now
                    break;
                }
            }

            //similarly for other properties (N, ORG, TEL, etc)
            //...
            Log.v(tag, "name?? : " + name);
            System.out.println("Found contact: " + name);
        }
        //must return a contact
        return null;
    }
}

