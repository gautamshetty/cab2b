package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.util.Constants.TYPE_DERIVED;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.util.global.Constants;

public class TestUtil {
    public static EntityInterface getEntity(String entityName, String attrName, EntityInterface parent) {
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        AttributeInterface a = deFactory.createStringAttribute();
        a.setName(attrName);

        EntityInterface e1 = deFactory.createEntity();
        e1.setName(entityName);
        if (parent != null) {
            e1.setParentEntity(parent);
            markInherited(a);
        }
        e1.addAttribute(a);
        return e1;
    }

    public static EntityInterface getEntity(String entityName, String attrName) {
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        AttributeInterface a = deFactory.createStringAttribute();
        a.setName(attrName);

        EntityInterface e1 = getEntity(entityName);
        e1.addAttribute(a);
        a.setEntity(e1);
        return e1;
    }

    public static EntityInterface getEntity(String entityName) {
        DomainObjectFactory deFactory = DomainObjectFactory.getInstance();
        EntityInterface e1 = deFactory.createEntity();
        e1.setName(entityName);
        return e1;
    }

    public static void markInherited(AbstractMetadataInterface owner) {
        TaggedValueInterface taggedValue = DomainObjectFactory.getInstance().createTaggedValue();
        taggedValue.setKey(TYPE_DERIVED);
        taggedValue.setValue(TYPE_DERIVED);
        owner.addTaggedValue(taggedValue);
    }

    public static AssociationInterface getAssociation(String srcEntityName, String targetEntityName) {
        EntityInterface src = getEntity(srcEntityName);
        EntityInterface target = getEntity(targetEntityName);

        AssociationInterface association = DomainObjectFactory.getInstance().createAssociation();
        association.setName("AssociationName_" + src.getAssociationCollection().size() + 1);
        association.setEntity(src);
        src.addAssociation(association);
        association.setSourceRole(getRole("srcRole"));
        association.setTargetEntity(target);
        association.setTargetRole(getRole("tgtRole"));
        association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
        return association;
    }

    private static RoleInterface getRole(String roleName) {
        RoleInterface role = DomainObjectFactory.getInstance().createRole();
        role.setAssociationsType(Constants.AssociationType.ASSOCIATION);
        role.setName(roleName);
        role.setMaximumCardinality(Constants.Cardinality.MANY);
        role.setMinimumCardinality(Constants.Cardinality.MANY);
        return role;
    }
}