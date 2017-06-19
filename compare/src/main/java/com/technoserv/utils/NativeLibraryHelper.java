package com.technoserv.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 */
public class NativeLibraryHelper {

    private static final Logger log = LoggerFactory.getLogger(NativeLibraryHelper.class);

    public static synchronized List<String> getLoadedLibraries(final ClassLoader loader) {
        java.lang.reflect.Field libraryList = null;

        try {
            libraryList = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            libraryList.setAccessible(true);
        } catch (NoSuchFieldException e) {
            log.error("Can't get an initial library list.", e);
            // do not throw
        }

        if (libraryList != null) {
            final Vector<String> libraries;
            try {
                libraries = (Vector<String>) libraryList.get(loader);
                return Lists.newArrayList(libraries);
            } catch (IllegalAccessException e) {
                log.error("Can't get libraries.", e);
            }
        }

        return new ArrayList<>();
    }

    public static boolean isLibraryLoaded(String libraryName, ClassLoader classLoader) {
        for (String name : getLoadedLibraries(classLoader)) {
            if (libraryName.equals(name)) {
                return true;
            }
        }
        return false;
    }

}
