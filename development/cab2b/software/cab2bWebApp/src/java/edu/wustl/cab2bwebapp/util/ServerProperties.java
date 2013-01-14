/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2bwebapp.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author gaurav_mehta
 *
 */
public class ServerProperties {

    /**
     * ResourceBundle for ServerProperties
     */
    protected static ResourceBundle bundle = ResourceBundle.getBundle("server");

    /**
     * @param originalName
     * @return display name of given original name of application
     */
    public static String getModelGroupName(String originalName) {
        String displayName = null;
        try {
            displayName = bundle.getString(originalName);
            return displayName;
        } catch (MissingResourceException missingResourceException) {
            displayName = originalName;
            return displayName;
        }
    }

    /**
     * Gives the default user mentioned in server.properties
     * @return String user name
     */
    public static String getDefaultUser() {
        return bundle.getString("default.user");
    }
}