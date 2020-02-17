package io;

import shared.User;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Configuration file handler.
 * It provides methods to get critical parameters from the configuration file, needed by the server to run.
 */
public final class ConfigReader {

    private static User identity;

    private static final String IDENTITY_PATH = "./identity.dat";
    private static final String CONFIG_PATH = "./config.ini";
    private static final String PARAM_THREADS  = "maxThreads";
    private static final String PARAM_ADDRESS = "serverAddress";
    private static final String PARAM_PORT = "serverPort";

    private static final int DEFAULT_MAX_THREADS = 6;
    private static final int DEFAULT_PORT_NO = 1035;

    /**
     * @return The port number specified on the config file if everything goes ok, DEFAULT_PORT_NO otherwise
     */
    public static int getPort() {
        return getNumericParameter(PARAM_PORT, DEFAULT_PORT_NO);
    }

    /**
     * @return The MAX_THREADS number specified on the config file if everything goes ok, DEFAULT_THREADS_NUMBER otherwise
     */
    public static int getThreadsNumber() {
        return getNumericParameter(PARAM_THREADS, DEFAULT_MAX_THREADS);
    }

    /**
     * @return The serverAddress specified on the config file if everything goes ok, localhost otherwise
     */
    public static String getAddress() {
        String defaultAddress = null;
        try{
            defaultAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_PATH)) {
            prop.load(in);
            String value =  prop.getProperty(PARAM_ADDRESS, defaultAddress);
            if(value.equals("localhost")) {
                return defaultAddress;
            } else {
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return defaultAddress;
        }
    }

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

    /**
     *
     * @return The identity of the client. It has to be specified in the identity.dat file
     */
    public static User getIdentity() {
        try {
            if(identity == null) {
                FileInputStream inStream = new FileInputStream(new File(IDENTITY_PATH));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String[] lines = reader.readLine().split("\\s*;\\s*");
                identity = new User(Integer.parseInt(lines[0]), lines[1], lines[2], lines[3]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return identity;
    }

}
