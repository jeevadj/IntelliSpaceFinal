package com.example.sasi.intellispace.Adapters;

/**
 * Created by hp on 06-02-2018.
 */

public class CardAdapter {
    public CardAdapter() {
    }

    public String building;
    public String floor;

    public CardAdapter(String building, String floor, String room) {
        this.building = building;
        this.floor = floor;
        this.room = room;
    }

    public String room;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
