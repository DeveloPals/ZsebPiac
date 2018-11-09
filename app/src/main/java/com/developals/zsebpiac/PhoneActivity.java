package com.developals.zsebpiac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.telephony.PhoneNumberUtils;

import com.developals.zsebpiac.Dao.User;


public class PhoneActivity extends AppCompatActivity {

    private User user = User.getCurrent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Button button = findViewById(R.id.button_add_phone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNumber = findViewById(R.id.text_phone);
                String number = etNumber.getText().toString();
                etNumber.setText("");
                user.addPhone(number);
                addItem(user.getPhones().size() - 1);
            }
        });

        fillLayout();
    }

    private void addItem(final int position){
        final LinearLayout layout = findViewById(R.id.layout_list_phones);

        String number = user.getPhones().get(position);
        number = PhoneNumberUtils.formatNumber(number, "HU");

        View view = getLayoutInflater().inflate(R.layout.deletable_list_item, layout, false);
        TextView tvText =  view.findViewById(R.id.text);
        tvText.setText(number);

        Button button = view.findViewById(R.id.button_clear);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                user.removePhone(position);
                layout.removeViewAt(position);
            }
        });
        layout.addView(view);
    }

    private void fillLayout(){
        for(int i=0; i < user.getPhones().size(); i++){
            addItem(i);
        }
    }
}
