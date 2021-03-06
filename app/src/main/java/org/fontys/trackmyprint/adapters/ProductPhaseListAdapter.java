package org.fontys.trackmyprint.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.fontys.trackmyprint.MainActivity;
import org.fontys.trackmyprint.R;
import org.fontys.trackmyprint.database.entities.Phase;

import java.util.List;

/**
 * Created by fhict on 08/12/2016.
 */

public class ProductPhaseListAdapter extends ArrayAdapter<Phase> {

    public ProductPhaseListAdapter(Context context, List<Phase> phases) {
        super(context, R.layout.production_proccess_item, phases);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater phaseInflater = LayoutInflater.from(getContext());
        View cv = phaseInflater.inflate(R.layout.production_proccess_item, parent, false);
        final Phase pp = getItem(position);

        TextView title = (TextView) cv.findViewById(R.id.production_proccess_title);
        title.setText(pp.getName());

        final ImageButton checkIn = (ImageButton) cv.findViewById(R.id.imageButtonCheck);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.getInstance().getCurrentPhase() != pp) {
                    MainActivity.getInstance().setCurrentPhase(pp);
                    checkIn.setImageResource(R.drawable.productphase_checked);
                } else {
                    MainActivity.getInstance().setCurrentPhase(null);
                    checkIn.setImageResource(R.drawable.checkin_btn);
                }
            }

        });
        if (MainActivity.getInstance().getCurrentPhase() != pp) {
            checkIn.setImageResource(R.drawable.checkin_btn);
        } else {
            checkIn.setImageResource(R.drawable.productphase_checked);
        }
        return cv;
    }

}
