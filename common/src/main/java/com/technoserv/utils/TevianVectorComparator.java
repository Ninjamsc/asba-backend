package com.technoserv.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TevianVectorComparator {

    /*static {
        //TODO Оберннуть в try catch http://stackoverflow.com/questions/7325579/java-lang-noclassdeffounderror-could-not-initialize-class-xxx
        try {
            System.out.println("java.library.path = " + System.getProperty("java.library.path"));

            System.out.println("Loading TevianVectorComparator library...");
            //System.loadLibrary("TevianVectorComparator");
            System.out.println("TevianVectorComparator library loaded");
        } catch (Throwable t){
            System.out.println("+++++++++++CANT_LOAD_LIB"+t);
            t.printStackTrace();
        }
    }*/


    public static double calculateSimilarityCliWrapper(String base64Vector1, String base64Vector2, String version) {
        double result = 0;
        try {
            String command = String.format("/bin/bash /opt/biometrics/run-tevian.sh %s %s", base64Vector1, base64Vector2);
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                result = Double.valueOf(line);
            }
        } catch (IOException e) {
            System.out.print("++++++++++++Wrong format++++++++++++++");
            e.printStackTrace();
        }
        return result;
    }


    public static double calculateSimilarityWrapper(double[] vector1, double[] vector2) {
        byte[] vector1bytes = getByteArrayFromVector(vector1), vector2bytes = getByteArrayFromVector(vector2);
        return calculateSimilarity(vector1bytes, vector2bytes, "1");
    }

    public static native float calculateSimilarity(byte[] binVector1, byte[] binVector2, String version);

    public static byte[] getByteArrayFromVector(double[] vector) {
        byte[] result = new byte[vector.length * 4];
        for (int i = 0; i < vector.length; i++) {
            double d = vector[i];
            float f = (float) d;
            int bits = Float.floatToIntBits(f);
            byte[] bytes = new byte[4];
            bytes[0] = (byte) (bits & 0xff);
            bytes[1] = (byte) ((bits >> 8) & 0xff);
            bytes[2] = (byte) ((bits >> 16) & 0xff);
            bytes[3] = (byte) ((bits >> 24) & 0xff);
            result[i * 4] = bytes[0];
            result[i * 4 + 1] = bytes[1];
            result[i * 4 + 2] = bytes[2];
            result[i * 4 + 3] = bytes[3];
        }
        return result;
    }
}
