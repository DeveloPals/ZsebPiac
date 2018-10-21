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
