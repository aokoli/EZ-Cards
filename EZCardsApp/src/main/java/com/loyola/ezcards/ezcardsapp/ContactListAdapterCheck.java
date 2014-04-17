package com.loyola.ezcards.ezcardsapp;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by newbuyer on 4/17/14.
 */
public class ContactListAdapterCheck extends ContactListAdapter implements CompoundButton.OnCheckedChangeListener{

    SparseBooleanArray mCheckStates;
    CheckBox cb;
//    ArrayList<Contact> list;
//    Context context;
    public ContactListAdapterCheck(Context context, ArrayList<Contact> list) {
        super(context, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Contact contactListItems = contactList.get(position);
        mCheckStates = new SparseBooleanArray(super.getCount());

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linecardcheck, arg2, false);

        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name2);
        //fix this when save object part is complete! by then, we dont need the textView on the right side!
        String txtValue = contactListItems.getFullName() + "\n" + contactListItems.getPhoneCell() + "\n" + contactListItems.getCompany1();
        tvName.setText(txtValue);
        //tvName.setText(contactListItems.getFirstName());
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone2);
        tvPhone.setText(contactListItems.getPhoneMain());
        cb = (CheckBox) convertView.findViewById(R.id.che);

        cb.setTag(position);
        cb.setChecked(mCheckStates.get(position, false));
        cb.setOnCheckedChangeListener(this);
        return convertView;
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);
    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        // TODO Auto-generated method stub

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }

}
