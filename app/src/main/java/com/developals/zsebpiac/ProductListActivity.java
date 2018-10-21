package com.developals.zsebpiac;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setTitle("Termékek");
        Toolbar mToolbar =  findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);

        ListView listView = findViewById(R.id.list_view);

        CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);

        Intent intent = getIntent();
        int pid = intent.getIntExtra(ProducersListActivity.PRODUCERS_ID, 0);

        View prodcuersView = findViewById(R.id.producer_data);

        TextView line1 = prodcuersView.findViewById(R.id.text_view1);
        TextView line2 = prodcuersView.findViewById(R.id.text_view2);
        TextView line3 = prodcuersView.findViewById(R.id.text_view3);

        line1.setText(ProducersListActivity.LINE1[pid]);
        line2.setText(ProducersListActivity.LINE2[pid]);
        line3.setText(ProducersListActivity.LINE3[pid]);

    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.layout_listitem, null);



            return convertView;
        }
    }
}
