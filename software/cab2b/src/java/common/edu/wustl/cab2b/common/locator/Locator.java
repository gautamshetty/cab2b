/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.locator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.log4j.Logger;
import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.util.CommonPropertyLoader;

/**
 * This class is responsible for all bean look ups. <br>
 * This is a singleton class.Cloning is not supported for this class.
 * The server and JNDI used for lookup, can be configured by changing
 * values in the file "jndi.properties" 
 * @author Chandrakant Talele
 */
public class Locator {
    private static Locator locator;

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(Locator.class);

    /**
     * This is to enforce that Locator is a singleton class
     */
    protected Locator() {
        System.setProperty(Context.PROVIDER_URL, CommonPropertyLoader.getJndiUrl());
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.HttpNamingContextFactory");
        //System.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
    }

    /**
     * This method is thread safe
     * @return instance of Locator
     */
    public static synchronized Locator getInstance() {
        if (locator == null) {
            locator = new Locator();
        }
        return locator;
    }

    /**
     * @param ejbName
     *            EJB Name defined in "ejb-jar.xml".
     *            It is defined at <ejb-name>HelloHome</ejb-name>
     * @param homeClassForEJB
     *            This is home class for the EJB which caller want to locate.
     *            It is defined in "ejb-jar.xml" at <home>learn.ejb.HelloHome</home>
     * 
     * @return Returns the BusinessInterface
     * @throws LocatorException
     *             Any exception, occured during look up operation.
     *             The exception will be wrapped in LocatorException
     */
    public BusinessInterface locate(String ejbName, Class<? extends EJBHome> homeClassForEJB)
            throws LocatorException {
        Object obj = null;

        logger.debug("Finding Bean : " + ejbName + "\n Home Interface is : " + homeClassForEJB.getName());
        try {
            logger.debug("Contacting to :" + System.getProperty("java.naming.provider.url"));
            Context ctx = new InitialContext();
            obj = ctx.lookup(ejbName);

        } catch (Throwable e) {
            if(e instanceof Exception) {
                throw new LocatorException(e.getMessage(),(Exception) e, ErrorCodeConstants.SR_0001);
            } else {
                throw new RuntimeException(e.getMessage(),e, ErrorCodeConstants.SR_0001);
            }
        }

        EJBHome homeObject = (EJBHome) javax.rmi.PortableRemoteObject.narrow(obj, homeClassForEJB);

        Method method = null;
        try {
            method = homeObject.getClass().getDeclaredMethod("create", new Class[0]);
        } catch (SecurityException e) {
            throw new LocatorException(e.getMessage(), e, ErrorCodeConstants.SR_0001);
        } catch (NoSuchMethodException e) {
            throw new LocatorException(e.getMessage(), e, ErrorCodeConstants.SR_0001);
        }
        // invoke the create method
        BusinessInterface businessInterface = null;
        try {
            Object bean = method.invoke(homeObject, new Object[0]);
            businessInterface = (BusinessInterface) bean;
        } catch (IllegalArgumentException e) {
            throw new LocatorException(e.getMessage(), e, ErrorCodeConstants.SR_0001);
        } catch (IllegalAccessException e) {
            throw new LocatorException(e.getMessage(), e, ErrorCodeConstants.SR_0001);
        } catch (InvocationTargetException e) {
            throw new LocatorException(e.getMessage(), e, ErrorCodeConstants.SR_0001);
        } catch (ClassCastException e) {
            throw new LocatorException(
                    "Required beans remote interface does not extends from business interface OR its businessinterface does not extend edu.wustl.cab2b.common.BusinessInterface",
                    e, ErrorCodeConstants.UN_XXXX);
        }

        return businessInterface;
    }

    /**
     * Cloning of locator is invalid
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
