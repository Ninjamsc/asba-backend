package com.technoserv.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by VBasakov on 22.11.2016.
 */
public class UtilTest {

    static String json = "{\n" +
            "\"version\" :\"1\"\n," +
            "\"template\":[\n" +
            "\n" +
            "0.09220985, -0.0681314, -0.11580598, 0.07313379, -0.07345156,\n" +
            "0.02329779, 0.05814262, -0.05309539, 0.20792033, -0.12362318,\n" +
            "0.26324084, 0.03866995, 0.07608874, -0.10223258, 0.0657178,\n" +
            "-0.08177867, -0.08531126, -0.09971812, -0.04912016, 0.02949078,\n" +
            "-0.18137185, 0.17208834, 0.06115345, 0.04461022, -0.14159331,\n" +
            "-0.0271113, 0.02166164, 0.03833164, -0.06509953, -0.07349667,\n" +
            "0.02184311, 0.11115962, 0.08505372, 0.00255523, -0.06974252,\n" +
            "0.22377653, 0.01883085, 0.1751471, 0.08599104, 0.21865158,\n" +
            "0.07352214, -0.07516863, -0.14694889, 0.11252031, -0.03235815,\n" +
            "0.05181734, -0.08017924, -0.03208954, -0.03364349, -0.05319224,\n" +
            "-0.00792952, 0.02240941, -0.13211289, -0.09546722, -0.09668011,\n" +
            "0.14003041, -0.05520651, 0.09499975, 0.00394137, -0.14820081,\n" +
            "-0.03258764, 0.05555704, -0.04968469, -0.0220145, -0.02891627,\n" +
            "-0.00094346, -0.07328547, 0.01962685, -0.073447, 0.03910528,\n" +
            "0.05717066, 0.13144678, 0.08889023, -0.11668456, -0.04151125,\n" +
            "-0.03455345, 0.10325407, 0.01001143, 0.07667451, 0.08012081,\n" +
            "0.06651299, -0.11430524, 0.03788028, -0.12235609, -0.15135401,\n" +
            "-0.0307548, 0.05385722, -0.06491732, 0.1276798, 0.06597276,\n" +
            "-0.02364791, -0.07002126, 0.02779831, -0.04230236, 0.03175509,\n" +
            "0.11071441, -0.0420349, 0.01997653, -0.03115777, 0.0024299,\n" +
            "0.10206569, 0.08081728, 0.04906504, -0.05319322, 0.06197476,\n" +
            "-0.10122427, -0.10369578, -0.07127877, -0.05189025, 0.0649693,\n" +
            "-0.01395369, -0.03806365, 0.01544157, 0.03515985, 0.18239044,\n" +
            "0.09935737, -0.04691097, 0.10817883, 0.14009495, -0.00141741,\n" +
            "0.04819384, -0.10831979, 0.02511743, -0.02743166, 0.05107384,\n" +
            "-0.07011004, 0.05036785, -0.05941105\n" +
            "\n" +
            "]\n" +
            " \n" +
            "\n" +
            "}\n";

    static String jsonProblem = "{\"problem\":4}";

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Response obj = mapper.readValue(json, Response.class);
        System.out.println(obj);
        JsonNode node = mapper.readValue(jsonProblem, JsonNode.class);
        int problem = node.get("problem").asInt();
        System.out.println(problem);
    }

    private static class Response{
        public int version;
        public double[] template;
    }

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
