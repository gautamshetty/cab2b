package edu.wustl.cab2b.server.datalist;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.queryengine.result.IRecord;

public class DefaultDataListSaver extends AbstractDataListSaver<IRecord> {

    public DefaultDataListSaver(EntityInterface oldEntity) {
        super(oldEntity);
    }

    @Override
    protected void populateNewEntity(EntityInterface oldEntity) {
        DataListUtil.copyAttributes(newEntity, oldEntity);
    }

}
