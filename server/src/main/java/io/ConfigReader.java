package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration file handler.
 * It provides methods to get critical parameters from the configuration file, needed by the server to run.
 */
public final class ConfigReader {

    private static final String CONFIG_PATH = "./config.ini";

    private static final String PARAM_PORT = "port";
    private static final String PARAM_N_THREAD = "threads_number";
    private static final String PARAM_REQ_TIMEOUT = "requests_term_timeout";

    private static final int DEFAULT_PORT_NO = 1035;
    private static final int DEFAULT_THREAD_NO = 6;
    private static final int DEFAULT_REQ_TIMEOUT = 10;

    /**
     * @return The port number specified on the config file if everything goes ok, DEFAULT_PORT_NO otherwise
     */
    public static int getPort() {
        return getNumericParameter(PARAM_PORT, DEFAULT_PORT_NO);
    }

    /**
     * @return The number of threads specified on the config file if everything goes ok, DEFAULT_THREAD_NO otherwise
     */
    public static int getThreadsNo() {
        return getNumericParameter(PARAM_N_THREAD, DEFAULT_THREAD_NO);
    }

    public static int getThreadPoolTimeout() {return getNumericParameter(PARAM_REQ_TIMEOUT, DEFAULT_REQ_TIMEOUT); }

    /**
     * Gets a generic integer parameter from the Config file
     * @param paramName Name of the parameter to get on the config file
     * @param defaultValue Default value that the parameter will have in case something goes wrong
     * @return The parameter value if successful, defaultValue otherwise
     */
    private static int getNumericParameter(String paramName, int defaultValue) {
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_PATH)) {
            prop.load(in);
            String property = prop.getProperty(paramName, String.valueOf(defaultValue));
            int retVal =  Integer.parseInt(property);
            if(retVal > 0)
                return retVal;
            throw new IOException("Invalid value inserted as " + paramName + " in " + CONFIG_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

}
