package pl.edu.pwr.wordnetloom.common.model;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.util.UUID;

import static java.lang.System.arraycopy;
import static org.hibernate.internal.util.BytesHelper.asLong;
import static org.hibernate.internal.util.BytesHelper.fromLong;


public class UUIDTypeDescriptor extends AbstractTypeDescriptor<UUID>{

    static final long serialVersionUID = 1L;

    public static UUIDTypeDescriptor INSTANCE = new UUIDTypeDescriptor();

    public UUIDTypeDescriptor() {
        super(UUID.class);
    }

    @Override
    public String toString(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID fromString(final String value){
        return UUID.fromString(value);
    }

    @Override
    public <X> X unwrap(final UUID value, final Class<X> type, final WrapperOptions options){
        if(value == null) return null;

        if(UUID.class.isAssignableFrom(type)){
            return type.cast(value);
        }

        if(String.class.isAssignableFrom(type)){
            return type.cast(value.toString());
        }

        if(byte[].class.isAssignableFrom(type)){
            return type.cast(ToBytesTransformer.INSTANCE.transform(value));
        }

        throw unknownUnwrap(type);
    }

    @Override
    public <X> UUID wrap(final X value, final WrapperOptions options) {
        if (value == null) return null;

        if (UUID.class.isInstance(value)) {
            return UUID.class.cast(value);
        }

        if (String.class.isInstance(value)) {
            return UUID.fromString(String.class.cast(value));
        }

        if (byte[].class.isInstance(value)) {
            return ToBytesTransformer.INSTANCE.parse(value);
        }

        throw unknownWrap(value.getClass());
    }

    public static class ToBytesTransformer implements org.hibernate.type.descriptor.java.UUIDTypeDescriptor.ValueTransformer {
        public static final ToBytesTransformer INSTANCE = new ToBytesTransformer();

        @Override
        public UUID parse(final Object value) {
            if(((byte[])value).length < 16){
                System.out.println("Randomowy UUID");
                return UUID.randomUUID();
            }
            byte[] msb = new byte[8];
            byte[] lsb = new byte[8];

            arraycopy(value, 4, msb, 0, 4);
            arraycopy(value, 2, msb, 4, 2);
            arraycopy(value, 0, msb, 6, 2);
            arraycopy(value, 8, lsb, 0, 8);

            return new UUID(asLong(msb), asLong(lsb));
        }

        @Override
        public byte[] transform(final UUID uuid) {
            final byte[] out = new byte[16];
            final byte[] msbIn = fromLong(uuid.getMostSignificantBits());

            arraycopy(msbIn, 6, out, 0, 2);
            arraycopy(msbIn, 4, out, 2, 2);
            arraycopy(msbIn, 0, out, 4, 4);
            arraycopy(fromLong(uuid.getLeastSignificantBits()), 0, out, 8, 8);

            return out;
        }
    }
}
