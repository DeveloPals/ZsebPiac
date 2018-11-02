package com.developals.zsebpiac.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developals.zsebpiac.Dao.DBPhoneNumber;
import com.developals.zsebpiac.ProducersDataActivity;
import com.developals.zsebpiac.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneNumberAdapter extends BaseAdapter {

    public List<String> numbers;
    private Context context;

    public PhoneNumberAdapter(Context context){
        this.context = context;
        numbers = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return numbers.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.layout_listitem_editable, null);
        TextView textView = convertView.findViewById(R.id.text_listitem_display);
        textView.setText(numbers.get(position));

        ImageButton button = convertView.findViewById(R.id.button_listitem_edit);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DBPhoneNumber.Delete(context, "", numbers.get(position));
                numbers.remove(position);
                notifyDataSetChanged();
                //UpdateListHeight(mListViewPhones, mPhoneAdapter);
            }
        });
        return convertView;
    }
}
