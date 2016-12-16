package com.example.tmp.trackmyprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private production_proccess_list_adapter adapter;

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

        adapter = new production_proccess_list_adapter(this, names);
        ListView lv = (ListView) findViewById(R.id.production_proccess);
        lv.setAdapter(adapter);

        ImageView checkIn = (ImageView) findViewById(R.id.check_in_status);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, nfcActivity.class);
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Test", Toast.LENGTH_SHORT);
            }
        });

    }
}
