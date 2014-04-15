package com.loyola.ezcards.ezcardsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        Contact contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linecard, arg2, false);

        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        //fix this when save object part is complete! by then, we dont need the textView on the right side!
        String txtValue = contactListItems.getFullName() + "\n" + contactListItems.getPhoneCell() + "\n" + contactListItems.getCompany1();
        tvName.setText(txtValue);
        //tvName.setText(contactListItems.getFirstName());
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
        tvPhone.setText(contactListItems.getPhoneMain());


        return convertView;
    }

}
