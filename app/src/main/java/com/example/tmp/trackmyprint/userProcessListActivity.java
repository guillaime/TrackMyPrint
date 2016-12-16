package com.example.tmp.trackmyprint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class userProcessListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_process_list);

        List<production_proccess> names = new ArrayList<>();
        names.add(new production_proccess("Order Received"));
        names.add(new production_proccess("Printing Pending"));
        names.add(new production_proccess("Cutting not started"));
        names.add(new production_proccess("Quality Control not started"));
        names.add(new production_proccess("Packaging"));
        names.add(new production_proccess("Shipping"));

        user_process_list_adapter b = new user_process_list_adapter(this, names);
        ListView lvuser = (ListView) findViewById(R.id.production_proccess);
        lvuser.setAdapter(b);

        ImageView checkIn = (ImageView) findViewById(R.id.check_in_status);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userProcessListActivity.this, nfcActivity.class);
                startActivity(intent);
            }
        });

    }
}