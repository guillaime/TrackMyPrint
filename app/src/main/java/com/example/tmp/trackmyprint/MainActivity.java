package com.example.tmp.trackmyprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
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

    private ImageButton btnScan;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = (ImageButton) findViewById(R.id.btnScan);

        currentEmployee = new Employee("1", "Luuk Hermans");

        try {
            Database.initializeInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        List<Phase> phases = new ArrayList<>();

        phases.addAll(Database.getInstance().getPhases().values());

        adapter = new production_proccess_list_adapter(this, phases);
        ListView lv = (ListView) findViewById(R.id.production_proccess);
        lv.setAdapter(adapter);

        TextView employeeName = (TextView) findViewById(R.id.profile_name);
        employeeName.setText(currentEmployee.getName());

        ImageView checkIn = (ImageView) findViewById(R.id.check_in_status);

        // Deze is temporary om naar de userList te gaan.
        ImageView checkIn2 = (ImageView) findViewById(R.id.profile_image);

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
        currentEmployee.setPhase(p);
        ImageView status = (ImageView) findViewById(R.id.check_in_status);
        TextView lblScan = (TextView) findViewById(R.id.lblScan);

        if (p.getId() == "100") {
            status.setImageResource(R.drawable.checkin_status);
            lblScan.setText("Please check in to a sector");
            btnScan.setImageResource(R.color.colorProfileRectangle);
        } else {
            status.setImageResource(R.drawable.checkedinbtn);
            lblScan.setText("Scan a product");
            btnScan.setImageResource(R.color.colorScanButton);

            btnScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, nfcActivity.class);
                    startActivity(intent);
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    public Phase getCurrentPhase() {
        return currentEmployee.getPhase();
    }
}
