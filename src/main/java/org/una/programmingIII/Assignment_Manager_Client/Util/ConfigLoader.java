package org.una.programmingIII.Assignment_Manager_Client.Util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static boolean loaded = false;

    private static void load() {
        if (loaded) return;
        try (InputStream input = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                loaded = true;
            }
        } catch (Exception e) {
            Logger.getLogger("ConfigLoader").severe("Could not load config.properties: " + e.getMessage());
        }
    }

    public static String getBackendUrl() {
        load();
        return properties.getProperty("backend.url", "http://localhost:8080");
    }
}
