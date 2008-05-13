package edu.wustl.cab2b.server.util;

import java.util.Properties;

import edu.wustl.cab2b.common.util.Utility;

/**
 * This class is wrapper around property file server.properties.
 * @author Chandrakant Talele
 */
public final class ServerProperties {
    private static Properties props = Utility.getPropertiesFromFile("server.properties");

    /**
     * @return The database IP
     */
    public static String getDatabaseIp() {
        return props.getProperty("database.server.ip");
    }

    /**
     * @return The database port
     */
    public static String getDatabasePort() {
        return props.getProperty("database.server.port");
    }

    /**
     * @return the name of the database
     */
    public static String getDatabaseName() {
        return props.getProperty("database.name");
    }

    /**
     * @return the user to be used for connecting to the database
     */
    public static String getDatabaseUser() {
        return props.getProperty("database.username");
    }

    /**
     * @return the password of database user
     */
    public static String getDatabasePassword() {
        return props.getProperty("database.password");
    }

    /**
     * @return The database loader to be used
     */
    public static String getDatabaseLoader() {
        return props.getProperty("database.loader");
    }

    /**
     * @return The data-source name
     */
    public static String getDatasourceName() {
        return props.getProperty("datasource.name");
    }

}