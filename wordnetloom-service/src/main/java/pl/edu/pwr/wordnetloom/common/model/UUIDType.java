package pl.edu.pwr.wordnetloom.common.model;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;

import java.util.UUID;

public class UUIDType extends AbstractSingleColumnStandardBasicType<UUID>{

    static final long serialVersionUID = 1L;

    public static UUIDType INSTANCE = new UUIDType();

    public UUIDType(){
        super(BinaryTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "uuid-binary";
    }

    public boolean registerUnderJavaType() {
        return true;
    }
}
