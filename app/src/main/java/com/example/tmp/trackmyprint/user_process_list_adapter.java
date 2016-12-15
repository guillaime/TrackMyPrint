package com.example.tmp.trackmyprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Farshad on 15-12-2016.
 */

public class user_process_list_adapter {
    private Context context;
    private List<production_proccess> production_proccesses;

    public user_process_list_adapter(Context context, List<production_proccess> proccesses){
        super(context, R.layout.production_proccess_item, proccesses);
        this.context = context;
        this.production_proccesses = proccesses;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        production_proccess pp = production_proccesses.get(position);
        View cv = convertView;

        if(cv == null) {
            LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cv = i.inflate(R.layout.production_proccess_item, null);
        }

        TextView title = (TextView) cv.findViewById(R.id.production_proccess_title);
        title.setText(pp.name);
        return cv;

    }

}

