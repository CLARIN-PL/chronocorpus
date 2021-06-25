package pl.clarin.chronocorpus;

import org.ini4j.Ini;

public class Configuration {

    public static String DATA_STORE_FILE;
    public static String STATISTICS_FILE;
    public static String METADATA_ZIP_FILE;
    public static String DATA_ZIP_FILE;

    public static String TOOL_NAME;
    public static String HOST;
    public static String RABBIT_USER;
    public static String REQUEST_QUEUE_NAME = "nlp_dummy";
    public static String RABBIT_PASSWORD;

    public static void init(Ini ini){
        DATA_STORE_FILE = ini.get("configuration", "data_store_file");
        STATISTICS_FILE = ini.get("configuration", "statistics_file");
        METADATA_ZIP_FILE = ini.get("configuration", "metadata_zip_file");
        DATA_ZIP_FILE = ini.get("configuration","data_zip_file");

        TOOL_NAME= ini.get("service", "tool");
        HOST = ini.get("service", "rabbit_host");
        RABBIT_USER = ini.get("service", "rabbit_user");
        RABBIT_PASSWORD = ini.get("service", "rabbit_password");
        REQUEST_QUEUE_NAME = ini.get("service", "queue_prefix") + TOOL_NAME;
    }

}
