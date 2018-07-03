package com.example.jonathan.moviedatabase_stage2.Data;

class sampleMovieData {

    private static final String[] titleArray = {"Jon", "Katie", "Amelia", "Perrin"
    ,"Jonathan","Kat","Millie","Pear Pear"};


    public static int Length() {
        return titleArray.length;
    }

    public static String getString(int index) {
        return titleArray[index];
    }

}
