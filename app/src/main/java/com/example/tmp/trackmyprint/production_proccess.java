package com.example.tmp.trackmyprint;

import org.fontys.trackmyprint.database.entities.Phase;

/**
 * Created by fhict on 08/12/2016.
 */

public class production_proccess {

    public String name;
    public String iconName;
    public Phase phase;

    public production_proccess(String name, String iconName, Phase phase) {
        this.phase = phase;
        this.name = name;
        this.name = iconName;
    }

}
