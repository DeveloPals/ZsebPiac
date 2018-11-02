package com.developals.zsebpiac;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.developals.zsebpiac.Dao.DBPhoneNumber;
import com.developals.zsebpiac.Dao.DBUser;

import java.util.ArrayList;
import java.util.List;

public class ProducersDataActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener,
        DialogInterface.OnClickListener {

    private final String TAG = "ProducersDataActivity";

    private AlertDialog mPhoneDialog;
    private AlertDialog mAddressDialog;
    private PhonesAdapter mPhoneAdapter;
    private AddressAdapter mAddressAdapter;
    private ListView mListViewPhones;
    private ListView mListViewAddress;
    private EditText mPhoneNumber;
    private EditText mZipCode;
    private EditText mCity;
    private EditText mAddress;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etProducerId;
    private List<String> mTempPhones;
    private List<String []> mTempAddresses;
    private int alterIndex;

    private String emailStored;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_data);
        setTitle("Adataim");
        Toolbar mToolbar =  findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etFirstName = findViewById(R.id.text_firstname);
        etLastName = findViewById(R.id.text_lastname);
        etProducerId = findViewById(R.id.text_producerid);

        mListViewPhones = findViewById(R.id.list_phones);
        mListViewAddress = findViewById(R.id.list_address);
        mListViewPhones.setOnItemClickListener(this);
        mListViewAddress.setOnItemClickListener(this);

        mTempPhones = new ArrayList<String>();
        mTempAddresses = new ArrayList<String []>();

        mPhoneAdapter = new PhonesAdapter();
        mListViewPhones.setAdapter(mPhoneAdapter);

        mAddressAdapter = new AddressAdapter();
        mListViewAddress.setAdapter(mAddressAdapter);

        ImageButton btnAddPhone = findViewById(R.id.button_add_phone);
        ImageButton btnAddAddress = findViewById(R.id.button_add_address);
        btnAddPhone.setOnClickListener(this);
        btnAddAddress.setOnClickListener(this);


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProducersDataActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_phonenumber, null);
        mPhoneNumber = mView.findViewById(R.id.text_phone);
        mBuilder.setView(mView);
        mBuilder.setPositiveButton("Hozzáadás", this);
        mBuilder.setNegativeButton("Mégse", this);
        mPhoneDialog = mBuilder.create();


        mView = getLayoutInflater().inflate(R.layout.dialog_address, null);
        mZipCode = mView.findViewById(R.id.text_zip_code);
        mCity = mView.findViewById(R.id.text_city);
        mAddress = mView.findViewById(R.id.text_address);
        mBuilder.setView(mView);
        mAddressDialog = mBuilder.create();

        SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
        emailStored = pref.getString("email", null);

        alterIndex = -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_datas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                DBUser.Update(this, emailStored, etFirstName.getText().toString(),
                        etLastName.getText().toString(), etProducerId.getText().toString());
                break;
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_add_phone:
                mPhoneDialog.show();
                mPhoneDialog.getButton(Dialog.BUTTON_POSITIVE).setText("Hozzáadás");
                mPhoneNumber.requestFocus();
                break;
            case R.id.button_add_address:
                mAddressDialog.show();
                mAddressDialog.getButton(Dialog.BUTTON_POSITIVE).setText("Hozzáadás");
                mZipCode.requestFocus();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        alterIndex = position;
        switch (parent.getId()){
            case R.id.list_phones:
                mPhoneNumber.setText(mTempPhones.get(position));
                mPhoneDialog.getButton(Dialog.BUTTON_POSITIVE).setText("Módosítás");
                mPhoneDialog.show();
                break;
            case R.id.list_address:
                mZipCode.setText(mTempAddresses.get(position)[0]);
                mCity.setText(mTempAddresses.get(position)[1]);
                mAddress.setText(mTempAddresses.get(position)[2]);
                mAddressDialog.getButton(Dialog.BUTTON_POSITIVE).setText("Módosítás");
                mAddressDialog.show();
                break;
        }
    }



    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(dialog==mAddressDialog){
            if(which==Dialog.BUTTON_POSITIVE) {
                String[] item = {mZipCode.getText().toString(), mCity.getText().toString(), mAddress.getText().toString()};
                if (alterIndex == -1)
                {
                    mTempAddresses.add(item);

                }
                else
                {

                    mTempAddresses.set(alterIndex, item);

                }
                mAddressAdapter.notifyDataSetChanged();
                UpdateListHeight(mListViewAddress, mAddressAdapter);
            }
            mZipCode.setText("");
            mCity.setText("");
            mAddress.setText("");
        }
        else if(dialog==mPhoneDialog){
            if(which==Dialog.BUTTON_POSITIVE){
                if(alterIndex == -1) {
                    mTempPhones.add(mPhoneNumber.getText().toString());
                    DBPhoneNumber.InsertInto(this, emailStored, mPhoneNumber.getText().toString());
                }
                else {
                    DBPhoneNumber.Update(this, emailStored, mTempPhones.get(alterIndex), mPhoneNumber.getText().toString());
                    mTempPhones.set(alterIndex, mPhoneNumber.getText().toString());
                }
                mPhoneAdapter.notifyDataSetChanged();
                UpdateListHeight(mListViewPhones, mPhoneAdapter);
            }
            mPhoneNumber.setText("");
        }
        alterIndex = -1;
    }


    class PhonesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTempPhones.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.layout_listitem_editable, null);
            TextView textView = convertView.findViewById(R.id.text_listitem_display);
            textView.setText(mTempPhones.get(position));

            ImageButton button = convertView.findViewById(R.id.button_listitem_edit);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    DBPhoneNumber.Delete(ProducersDataActivity.this, emailStored, mTempPhones.get(position));
                    mTempPhones.remove(position);
                    mPhoneAdapter.notifyDataSetChanged();
                    UpdateListHeight(mListViewPhones, mPhoneAdapter);
                }
            });
            return convertView;
        }
    }

    public class AddressAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTempAddresses.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.layout_listitem_editable, null);
            TextView textView = convertView.findViewById(R.id.text_listitem_display);

            String text = mTempAddresses.get(position)[0] + " " + mTempAddresses.get(position)[1] + " " + mTempAddresses.get(position)[2];
            textView.setText(text);

            ImageButton button = convertView.findViewById(R.id.button_listitem_edit);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mTempAddresses.remove(position);
                    mAddressAdapter.notifyDataSetChanged();
                    UpdateListHeight(mListViewAddress, mAddressAdapter);
                }
            });
            return convertView;
        }
    }

    private void UpdateListHeight(ListView listView, BaseAdapter adapter){

        int numberOfItems = adapter.getCount();

        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = adapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() *
                (numberOfItems - 1);

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }




}


