package com.developals.zsebpiac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.developals.zsebpiac.Dao.User;

public class NameActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etDisplayedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        etFirstName = findViewById(R.id.text_first_name);
        etLastName = findViewById(R.id.text_last_name);
        etDisplayedName = findViewById(R.id.text_displayed_name);

        etFirstName.setText(User.getCurrent().getFirstName());
        etLastName.setText(User.getCurrent().getLastName());
        etDisplayedName.setText(User.getCurrent().getDisplayedName());

        CustomTextWatcher textWatcher = new CustomTextWatcher();
        etFirstName.addTextChangedListener(textWatcher);
        etLastName.addTextChangedListener(textWatcher);

        etDisplayedName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && etDisplayedName.getText().toString().equals("")) {
                    RefreshDisplayedName();
                }
            }
        });

        Button btnSave = findViewById(R.id.button_save_name);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getCurrent();
                user.setFirstName(etFirstName.getText().toString());
                user.setLastName(etLastName.getText().toString());
                user.setDisplayedName(etDisplayedName.getText().toString());
                user.updateName();
            }
        });
    }

    private void RefreshDisplayedName() {
        String name = etLastName.getText().toString() + " " + etFirstName.getText().toString();
        etDisplayedName.setHint(name);
    }

    private String getWholeName() {
        return etLastName.getText().toString() + " " + etFirstName.getText().toString();
    }

    class CustomTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           String name = etDisplayedName.getText().toString();
           name = new StringBuilder(name).insert(start, s).toString();
           RefreshDisplayedName();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
