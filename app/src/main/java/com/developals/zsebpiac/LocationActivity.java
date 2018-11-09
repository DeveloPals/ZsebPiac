package com.developals.zsebpiac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developals.zsebpiac.Dao.User;

public class LocationActivity extends AppCompatActivity {

    private User user = User.getCurrent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Button button = findViewById(R.id.button_add_location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etZipCode = findViewById(R.id.text_zip_code);
                EditText etCity = findViewById(R.id.text_city);
                EditText etStreet = findViewById(R.id.text_street_address);

                User user = User.getCurrent();
                String[] locations = new String[3];
                locations[0] = etZipCode.getText().toString();
                locations[1] = etCity.getText().toString();
                locations[2] = etStreet.getText().toString();

                etZipCode.setText("");
                etCity.setText("");
                etStreet.setText("");

                user.addLocation(locations);

                addItem(user.getLocations().size() - 1);
            }
        });

        fillLayout();
    }

    private void addItem(final int position) {
        final LinearLayout layout = findViewById(R.id.layout_list_locations);

        View view = getLayoutInflater().inflate(R.layout.deletable_list_item, layout, false);
        TextView tvText = view.findViewById(R.id.text);
        tvText.setText(user.getLocation(position));

        Button button = view.findViewById(R.id.button_clear);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user.removeLocation(position);
                layout.removeViewAt(position);
            }
        });
        layout.addView(view);
    }

    private void fillLayout() {
        for (int i = 0; i < user.getLocations().size(); i++) {
            addItem(i);
        }
    }

}
