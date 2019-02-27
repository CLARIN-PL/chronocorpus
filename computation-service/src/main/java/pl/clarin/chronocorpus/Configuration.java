package pl.clarin.chronocorpus;

import org.ini4j.Ini;

public class Configuration {

    public static  String DATA_STORE_FILE;
    public static  String METADATA_ZIP_FILE;
    public static  String DATA_ZIP_FILE;

    public static void init(Ini ini){
        DATA_STORE_FILE = ini.get("configuration", "data_store_file");
        METADATA_ZIP_FILE = ini.get("configuration", "metadata_zip_file");
        DATA_ZIP_FILE = ini.get("configuration","data_zip_file");
    }

}
