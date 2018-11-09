package com.developals.zsebpiac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.developals.zsebpiac.Dao.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProducersData2Activity extends AppCompatActivity implements ListView.OnItemClickListener {

    private final int NAME = 0;
    private final int DISPLAYED_NAME = 1;
    private final int PRODUCER_ID = 5;
    private final int EMAILS = 2;
    private final int PHONES = 3;
    private final int LOCATIONS = 4;
    private final int PASSWORD = 6;

    private String [] dataTitle = {"Név", "Megjelenő név", "Email címek", "Telefonszámok", "Átvételi pontok", "Őstermelő szám", "Jelszó módosítás"};
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producers_data2);

        setTitle("Beállítások");
        Toolbar mToolbar =  findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //TextView displayedName = findViewById(R.id.text_displayed_name);
        //displayedName.setText(User.getCurrent().getDisplayedName());

        User user = User.getCurrent();

        List<HashMap<String, String>> dataList = new ArrayList<>();
        for(int i=0;i < dataTitle.length;i++){
            HashMap<String, String> map = new HashMap<>();
            map.put("line1", dataTitle[i]);
            String line2 = "";
            switch (i){
                case NAME:
                    line2 = user.getLastName() + " " + user.getFirstName();
                    break;
                case DISPLAYED_NAME:
                    line2 = user.getDisplayedName();
                    break;
                case EMAILS:
                    line2 = user.getEmail();
                    break;
                case PHONES:
                    for(int j = 0; j < user.getPhones().size(); j++){
                        String num = PhoneNumberUtils.formatNumber(user.getPhones().get(j), "HU");
                        line2 = line2.concat(num.concat("\n"));
                    }
                    break;
                case LOCATIONS:
                    for(int j = 0; j < user.getLocations().size(); j++){
                        line2 = line2.concat(user.getLocation(j).concat("\n"));
                    }
                    break;
            }
            map.put("line2", line2);
            dataList.add(map);
        }

        adapter = new SimpleAdapter(this, dataList, R.layout.simple_list_item,
                new String [] {"line1", "line2"}, new int [] {R.id.text1, R.id.text2});

        ListView listView = findViewById(R.id.list_producerdata);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch (position){
            case PHONES:
                intent = new Intent(this, PhoneActivity.class);
                break;
            case NAME: case DISPLAYED_NAME:
                intent = new Intent(this, NameActivity.class);
                break;
            case LOCATIONS:
                intent = new Intent(this, LocationActivity.class);
                break;
            case PASSWORD:
                intent = new Intent(this, ModifyPasswActivity.class);
                break;
        }
        if(intent != null)
            startActivity(intent);
    }
}
