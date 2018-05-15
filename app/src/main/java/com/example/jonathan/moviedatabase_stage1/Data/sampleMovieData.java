package com.example.jonathan.moviedatabase_stage1.Data;

public class sampleMovieData {

    private static String[] titleArray = {"Jon", "Katie", "Amelia", "Perrin"
    ,"Jonathan","Kat","Millie","Pear Pear"};


    public static int Length() {
        return titleArray.length;
    }

    public static String getString(int index) {
        return titleArray[index];
    }

}
