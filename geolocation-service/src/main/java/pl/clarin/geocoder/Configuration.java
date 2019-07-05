package pl.clarin.geocoder;

import org.ini4j.Ini;

public class Configuration {

    public static String LOCATION_CACHE_STORE_FILE;

    public static String TOOL_NAME;
    public static String HOST;
    public static String RABBIT_USER;
    public static String REQUEST_QUEUE_NAME = "nlp_dummy";
    public static String RABBIT_PASSWORD;

    public static void init(Ini ini){
        LOCATION_CACHE_STORE_FILE= ini.get("configuration", "locations_cache_store_file");

        TOOL_NAME= ini.get("service", "tool");
        HOST = ini.get("service", "rabbit_host");
        RABBIT_USER = ini.get("service", "rabbit_user");
        RABBIT_PASSWORD = ini.get("service", "rabbit_password");
        REQUEST_QUEUE_NAME = ini.get("service", "queue_prefix") + TOOL_NAME;
    }

}
