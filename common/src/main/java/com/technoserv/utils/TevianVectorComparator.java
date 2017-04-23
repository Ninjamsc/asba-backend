package com.technoserv.utils;

public class TevianVectorComparator {

    static {
        
        System.out.println("java.library.path = "+ System.getProperty("java.library.path"));

        System.out.println("Loading TevianVectorComparator library...");
        System.loadLibrary("TevianVectorComparator");
        System.out.println("TevianVectorComparator library loaded");
    }

    public static native float calculateSimilarity(byte[] binVector1, byte[] binVector2, String version);
}
