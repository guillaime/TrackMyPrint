package com.example.tmp.trackmyprint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<production_proccess> names = new ArrayList<>();
        names.add(new production_proccess("Order Received"));
        names.add(new production_proccess("Printing Pending"));
        names.add(new production_proccess("Cutting not started"));
        names.add(new production_proccess("Quality Control not started"));
        names.add(new production_proccess("Packaging"));
        names.add(new production_proccess("Shipping"));

        production_proccess_list_adapter a = new production_proccess_list_adapter(this, names);
        ListView lv = (ListView) findViewById(R.id.production_proccess);
        lv.setAdapter(a);

        ImageView checkIn = (ImageView) findViewById(R.id.check_in_status);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, nfcActivity.class);
                startActivity(intent);
            }
        });

        // Deze is temporary om naar de userList te gaan.
        ImageView checkIn2 = (ImageView) findViewById(R.id.profile_image);
        checkIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, userProcessListActivity.class);
                startActivity(intent);
            }
        });


    }
}
