package com.loyola.ezcards.ezcardsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Contact> contactList;

    public ContactListAdapter(Context context, ArrayList<Contact> list) {

        this.context = context;
        contactList = list;
    }

    @Override
    public int getCount() {

        return contactList.size();
    }

    @Override
    public Object getItem(int position) {

        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Contact contact = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linecard, arg2, false);

        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(contact.getImageLocation(), options);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_contactImage);
        imageView.setImageBitmap(bitmap);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        //fix this when save object part is complete! by then, we dont need the textView on the right side!
        String txtValue = contact.getFullName() + "\n" + contact.getPhoneCell() + "\n" + contact.getCompany1();
        tvName.setText(txtValue);
        //tvName.setText(contact.getFirstName());
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
        tvPhone.setText(contact.getPhoneMain());


        return convertView;
    }

}
