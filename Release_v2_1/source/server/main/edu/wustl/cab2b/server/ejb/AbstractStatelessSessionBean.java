package edu.wustl.cab2b.server.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import edu.wustl.cab2b.server.cache.DatalistCache;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.util.ConnectionUtil;
import edu.wustl.cab2b.server.util.TimerUtill;
import edu.wustl.common.util.logger.Logger;

/**
 * Abstract class which represents a Statless Session Bean.
 * Each Statless Session Bean must extend this class.
 * It avoids need of each bean to implement methods from {@link javax.ejb.SessionBean} class.
 * @author Chandrakant Talele
 */
public abstract class AbstractStatelessSessionBean implements SessionBean {
    SessionContext sessionContext;

    /**
     * @throws CreateException
     */
    public void ejbCreate() throws CreateException {

    }

    /**
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException, RemoteException {
        throw new IllegalStateException("ejbActivate() must not be called on Stateless Bean");
    }

    /**
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException, RemoteException {
        throw new IllegalStateException("ejbPassivate() must not be called on Stateless Bean");
    }

    /**
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException, RemoteException {
    }

    /**
     * (non-Javadoc)
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        this.sessionContext = sessionContext;
        Logger.configure("caB2B.logger");
        EntityCache.getInstance();
        Connection connection = ConnectionUtil.getConnection();
        try {
            PathFinder.getInstance(connection);
            CategoryCache.getInstance(connection);
        } finally {
            ConnectionUtil.close(connection);
        }
        DatalistCache.getInstance();

        TimerUtill.initilizeSync();
    }

}