package com.example.tmp.trackmyprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    }
}
