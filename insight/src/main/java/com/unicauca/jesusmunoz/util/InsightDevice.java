package com.unicauca.jesusmunoz.util;

/**
 * Created by jesuseduardomunoz on 9/3/15.
 */
public class InsightDevice {

    private int ID;
    private String name;

    public InsightDevice(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public InsightDevice() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
