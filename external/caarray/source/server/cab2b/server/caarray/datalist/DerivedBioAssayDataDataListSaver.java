package cab2b.server.caarray.datalist;

import static edu.wustl.cab2b.server.datalist.DataListUtil.getAttributeByName;
import static edu.wustl.cab2b.server.datalist.DataListUtil.markVirtual;

import java.util.Map;

import cab2b.common.caarray.IDerivedBioAssayDataRecord;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.server.datalist.AbstractDataListSaver;
import edu.wustl.cab2b.server.datalist.DataListUtil;

public class DerivedBioAssayDataDataListSaver extends AbstractDataListSaver<IDerivedBioAssayDataRecord> {
    static final String CUBE_ATTRIBUTE_NAME = "cube";

    static final String DIM1LABELS_ATTRIBUTE_NAME = "dim1Labels";

    static final String DIM2LABELS_ATTRIBUTE_NAME = "dim2Labels";

    static final String DIM3LABELS_ATTRIBUTE_NAME = "dim3Labels";

    public DerivedBioAssayDataDataListSaver(EntityInterface oldEntity) {
        super(oldEntity);
    }

    @Override
    protected void populateNewEntity(EntityInterface oldEntity) {
        DataListUtil.copyAttributes(newEntity, oldEntity);
        newEntity.addAttribute(createObjectAttribute(CUBE_ATTRIBUTE_NAME));
        newEntity.addAttribute(createObjectAttribute(DIM1LABELS_ATTRIBUTE_NAME));
        newEntity.addAttribute(createObjectAttribute(DIM2LABELS_ATTRIBUTE_NAME));
        newEntity.addAttribute(createObjectAttribute(DIM3LABELS_ATTRIBUTE_NAME));
    }

    @Override
    public Map<AbstractAttributeInterface, Object> transformToMap(IDerivedBioAssayDataRecord record) {
        Map<AbstractAttributeInterface, Object> recordsMap = super.getRecordAsMap(record);
        recordsMap.put(getAttributeByName(newEntity, CUBE_ATTRIBUTE_NAME), record.getCube());
        recordsMap.put(getAttributeByName(newEntity, DIM1LABELS_ATTRIBUTE_NAME), record.getDim1Labels());
        recordsMap.put(getAttributeByName(newEntity, DIM2LABELS_ATTRIBUTE_NAME), record.getDim2Labels());
        recordsMap.put(getAttributeByName(newEntity, DIM3LABELS_ATTRIBUTE_NAME), record.getDim3Labels());
        return recordsMap;
    }

    private AttributeInterface createObjectAttribute(String name) {
        // TODO DE
        AttributeInterface attribute = null; // DomainObjectFactory.getInstance().createObjectAttribute();
        attribute.setName(name);
        markVirtual(attribute);
        return attribute;
    }
}
