package com.technoserv.bio.kernel;

import com.technoserv.bio.kernel.rest.client.CompareServiceRestClient;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by Adrey on 24.11.2016.
 */
@Service
@Profile("test")
public class MockCompareServiceClient extends CompareServiceRestClient {

    @Override
    public String compare(CompareServiceRequest request) {
        return "{\n" +
                "    \"blackLists\": {\n" +
                "        \"listId\": \"1\",\n" +
                "        \"listName\": \"List 1\",\n" +
                "        \"photo\": [\n" +
                "            {\n" +
                "                \"similarity\": \"13.169\",\n" +
                "                \"url\": \"url1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"similarity\": \"13.169\",\n" +
                "                \"url\": \"url2\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"rules\": {\n" +
                "        \"photo\": [\n" +
                "            {\n" +
                "                \"similarity\": \"13.169\",\n" +
                "                \"url\": \"url1\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"similarity\": \"13.169\",\n" +
                "                \"url\": \"url2\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"ruleId\": \"1\",\n" +
                "        \"ruleName\": \"Rule1\"\n" +
                "    }\n" +
                "}";
    }
}