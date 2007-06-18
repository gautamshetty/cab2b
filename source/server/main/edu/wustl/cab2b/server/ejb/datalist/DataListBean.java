package edu.wustl.cab2b.server.ejb.datalist;

import java.rmi.RemoteException;
import java.util.List;

import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.server.datalist.DataListMetadataOperations;
import edu.wustl.cab2b.server.datalist.DataListOperationsController;
import edu.wustl.cab2b.server.ejb.AbstractStatelessSessionBean;

/**
 * This class has methods to perform various oprations on data list, like save,
 * retrieve operations on data list and its metadata, etc.
 * 
 * @author chetan_bh
 */
public class DataListBean extends AbstractStatelessSessionBean implements DataListBusinessInterface {
    private static final long serialVersionUID = 1234567890L;

    /**
     * @see DataListBusinessInterface#retrieveAllDataListMetadata()
     */
    public List<DataListMetadata> retrieveAllDataListMetadata() throws RemoteException {
        return new DataListMetadataOperations().retrieveAllDataListMetadata();
    }

    /**
     * @see DataListBusinessInterface#retreiveDataList(Long)
     */
    public DataList retrieveDataList(Long dataListId) throws RemoteException {
        // TODO
        return null;
    }

    /**
     * @see DataListBusinessInterface#retrieveDataListMetadata(Long)
     */
    public DataListMetadata retrieveDataListMetadata(Long id) throws RemoteException {
        return new DataListMetadataOperations().retrieveDataListMetadata(id);
    }

    /**
     * @see DataListBusinessInterface#saveDataList(DataList)
     */
    public Long saveDataList(DataList dataList) throws RemoteException {
        return DataListOperationsController.save(dataList);
    }

    public List<IRecord> getEntityRecord(Long entityId) throws RemoteException {
        return DataListOperationsController.getEntityRecords(entityId);
    }

}
