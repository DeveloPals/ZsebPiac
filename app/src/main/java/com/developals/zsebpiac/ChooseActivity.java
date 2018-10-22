package com.developals.zsebpiac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        // Lekérdezis es megkapja a loginolt ember adatait 7 adat van eddig,
        // php-t editelni kell, hogy adhassunk meg hozza

        String username =  LoginActivity.username.toString();
        String typee = "getCustomer";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(typee,username);


    }

    public void ChooseCustomer(View view){
        Intent producersIntent = new Intent(ChooseActivity.this, ProducersListActivity.class);
        startActivity(producersIntent);
    }

    public void ChooseProducers(View view){
        Intent producersIntent = new Intent(ChooseActivity.this, MyProductListActivity.class);
        startActivity(producersIntent);
    }

}
