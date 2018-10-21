package com.developals.zsebpiac;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListActivity extends AppCompatActivity {

    public static String [] LINE1 = {"Kecskesajt", "Tehéntej"};
    public static String [] LINE2 = {"1200Ft/kg", "290Ft/l"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setTitle("Termékek");
        Toolbar mToolbar =  findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View view = findViewById(R.id.search_and_list);
        ListView listView = view.findViewById(R.id.list_view);

        CustomAdapter customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);

        Intent intent = getIntent();
        int pid = intent.getIntExtra(ProducersListActivity.PRODUCERS_ID, 0);

        View producersView = findViewById(R.id.producer_data);

        TextView line1 = producersView.findViewById(R.id.text_view1);
        TextView line2 = producersView.findViewById(R.id.text_view2);
        TextView line3 = producersView.findViewById(R.id.text_view3);

        line1.setText(ProducersListActivity.LINE1[pid]);
        line2.setText(ProducersListActivity.LINE2[pid]);
        line3.setText(ProducersListActivity.LINE3[pid]);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    class CustomAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 2;
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

            ImageView imageView = convertView.findViewById(R.id.image_view);
            TextView textView1 = convertView.findViewById(R.id.text_view1);
            TextView textView2 = convertView.findViewById(R.id.text_view2);
            TextView textView3 = convertView.findViewById(R.id.text_view3);

            imageView.setImageResource(R.drawable.logo);
            textView1.setText(LINE1[position]);
            textView2.setText(LINE2[position]);
            textView3.setText("");

            return convertView;
        }
    }
}
