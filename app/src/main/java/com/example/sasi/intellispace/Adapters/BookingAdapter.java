package com.example.sasi.intellispace.Adapters;

import java.util.ArrayList;

/**
 * Created by HPCOE on 18-02-2018.
 */

public class BookingAdapter
{
    public static  String B;

    public static String getBookdate() {
        return bookdate;
    }

    public static void setBookdate(String bookdate) {
        BookingAdapter.bookdate = bookdate;
    }

    public static String bookdate;

    public static String getB() {
        return B;
    }

    public static void setB(String b) {
        B = b;
    }

    public static String getRT() {
        return RT;
    }

    public static void setRT(String RT) {
        BookingAdapter.RT = RT;
    }

    public static String getST() {
        return ST;
    }

    public static void setST(String ST) {
        BookingAdapter.ST = ST;
    }

    public static String getET() {
        return ET;
    }

    public static void setET(String ET) {
        BookingAdapter.ET = ET;
    }

    public static String RT;
    public static String ST;
    public static String ET;

    public static ArrayList<CardAdapter> getAl() {
        return al;
    }

    public static void setAl(ArrayList<CardAdapter> al) {
        BookingAdapter.al = al;
    }

    public static ArrayList<CardAdapter> al=new ArrayList<>();
}
