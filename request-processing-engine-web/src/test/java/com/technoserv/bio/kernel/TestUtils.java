package com.technoserv.bio.kernel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Andrey on 23.11.2016.
 */
public class TestUtils {

    public static String readFile(String resourceName) throws Exception { //TODO ...

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(resourceName)))) {
            //return buffer.lines().collect(Collectors.joining("\n"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = buffer.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }
}
