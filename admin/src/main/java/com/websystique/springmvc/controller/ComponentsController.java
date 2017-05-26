package com.websystique.springmvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ComponentsController {

    @RequestMapping("/components")
    public Map<String, List<String>> aboutApps() {
        return collectAllDeployedApps();
    }

    private static Map<String, List<String>> collectAllDeployedApps() {

        final Map<String, List<String>> result = new HashMap<>();
        try {
            final List<String> apps = new ArrayList<>();

            // get MBean Server
            MBeanServer mBeanServer = findServer();
            if (mBeanServer == null) {
                result.put("error-message", Arrays.asList("Tomcat MBean Server is not available"));
                return result;
            }

            // get apps
            final Set<ObjectName> instances = mBeanServer
                    .queryNames(new ObjectName("Catalina:j2eeType=WebModule,*"), null);
            for (ObjectName each : instances) {
                apps.add(each.getKeyProperty("name")); //it will be in format like //localhost/appname
            }

            // transformation like this: "//localhost/admin##1.0.1" -> "admin 1.0.1"
            List<String> components = apps.stream()
                    .map(s -> s.split("/"))
                    .map(array -> array[array.length - 1])
                    .filter(s -> s.contains("##"))
                    .map(s -> s.split("##"))
                    .map(array -> String.join(" ", array))
                    .collect(Collectors.toList());

            if (components.size() > 0) {
                result.put("components", components);
            }


        } catch (MalformedObjectNameException e) {
            result.put("error-message", Arrays.asList(e.getMessage()));
        }
        return result;
    }

    private static MBeanServer findServer() {
        ArrayList<MBeanServer> servers = MBeanServerFactory.findMBeanServer(null);
        for (MBeanServer eachServer : servers) {
            for (String domain : eachServer.getDomains()) {
                if (domain.equals("Catalina")) {
                    return eachServer;
                }
            }
        }
        return null;
    }
}
