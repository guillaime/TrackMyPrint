package org.fontys.trackmyprint.lib;

import org.fontys.trackmyprint.database.entities.Phase;

/**
 * Created by fhict on 08/12/2016.
 */

public class ProductPhase {

    public String name;
    public String iconName;
    public Phase phase;

    public ProductPhase(String name, String iconName, Phase phase) {
        this.phase = phase;
        this.name = name;
        this.name = iconName;
    }

}
