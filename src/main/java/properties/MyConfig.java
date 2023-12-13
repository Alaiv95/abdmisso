package properties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public interface MyConfig {
    Config config = readConfig();
    static Config readConfig() {
        return ConfigFactory.load("env.conf");
    }

    String SHORT_ISSO_URL = config.getString("SHORT_ISSO_URL");
    String FULL_ISSO_URL = config.getString("FULL_ISSO_URL");
    String DB_URL = config.getString("DB_URL");
    String DB_USER = config.getString("DB_USER");
    String DB_PASS = config.getString("DB_PASS");
    String MAPPING_FILE = config.getString("MAPPING_FILE");
    String ALL_FULL_ISSO_URL = config.getString("ALL_FULL_ISSO_URL");
}
