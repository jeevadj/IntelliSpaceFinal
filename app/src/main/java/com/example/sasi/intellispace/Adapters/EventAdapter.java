package com.example.sasi.intellispace.Adapters;

/**
 * Created by Nasa on 06-Mar-18.
 */

public class EventAdapter {
    public EventAdapter() {
    }
    String Building;
    String Floor;

    public EventAdapter(String building, String floor, String date, String startT, String endT, String roomName) {
        Building = building;
        Floor = floor;
        Date = date;
        StartT = startT;
        EndT = endT;
        RoomName = roomName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    String Date;

    String StartT;
    String EndT;
    String RoomName;

    public String getBuilding() {
        return Building;
    }

    public void setBuilding(String building) {
        Building = building;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getStartT() {
        return StartT;
    }

    public void setStartT(String startT) {
        StartT = startT;
    }

    public String getEndT() {
        return EndT;
    }

    public void setEndT(String endT) {
        EndT = endT;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }
}
