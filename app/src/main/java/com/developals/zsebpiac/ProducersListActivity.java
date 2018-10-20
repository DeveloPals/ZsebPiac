package com.developals.zsebpiac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProducersListActivity extends AppCompatActivity {

    //int[] IMAGES = {};
    String [] LINE1 = {"Szabó Péter", "Nagy Lajos", "Szőrős Géza", "Csinos Etelka", "Bódi Guszti"};
    String [] LINE2 = {"12km", "5km", "14km", "23km", "1000km"};
    String [] LINE3 = {"Méz", "Tejtemrék", "Hús", "Zöldség és gyümölcs", "Sültrumpli"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_list);

        setTitle("Termelők");
        Toolbar mToolbar =  findViewById(R.id.app_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView listView = (ListView)findViewById(R.id.list_view);

        CustomAdapter customAdapater = new CustomAdapter();

        listView.setAdapter(customAdapater);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return LINE1.length;
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
            textView3.setText(LINE3[position]);

            return convertView;
        }
    }


}
