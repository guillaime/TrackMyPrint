package com.example.tmp.trackmyprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.fontys.trackmyprint.database.Database;
import org.fontys.trackmyprint.database.entities.Employee;
import org.fontys.trackmyprint.database.entities.Phase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Employee currentEmployee;

    private production_proccess_list_adapter adapter;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentEmployee = new Employee("1", "Luuk Hermans");

        try {
            Database.initializeInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<Phase> phases = new ArrayList<>();
        phases.add(new Phase("1", "Prepairing"));
        phases.add(new Phase("2", "Printing"));
        phases.add(new Phase("3", "Quality control"));

        adapter = new production_proccess_list_adapter(this, phases);
        ListView lv = (ListView) findViewById(R.id.production_proccess);
        lv.setAdapter(adapter);

        TextView employeeName = (TextView) findViewById(R.id.profile_name);
        employeeName.setText(currentEmployee.getName());

        ImageView checkIn = (ImageView) findViewById(R.id.check_in_status);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, printDetails.class);
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
        instance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            Database.deInitializeInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void setCurrentPhase(Phase p) {
        currentEmployee.setPhaseId(p.getId());

        ImageView status = (ImageView) findViewById(R.id.check_in_status);
        status.setImageResource(R.drawable.checkedinbtn);
    }
}
