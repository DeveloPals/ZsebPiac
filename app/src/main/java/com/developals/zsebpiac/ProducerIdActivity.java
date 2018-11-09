package com.developals.zsebpiac;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProducerIdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_id);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("userData", MODE_PRIVATE);
        pref.getString("producer_id", null);

        Button button = findViewById(R.id.button_save_producer_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etProducerId =findViewById(R.id.text_producer_id);
                String id = etProducerId.getText().toString();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("userData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("producer_id", id);
                editor.commit();
            }
        });


    }
}
