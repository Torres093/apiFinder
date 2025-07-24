package grupoExpo.API.Utils;

import jakarta.persistence.Converter;
import java.util.UUID;
import jakarta.persistence.AttributeConverter;
import java.nio.ByteBuffer;

@Converter(autoApply = true)
public class Convertidor implements AttributeConverter<UUID, byte[]>{

    @Override
    public byte[] convertToDatabaseColumn(UUID uuid) {
        if (uuid == null) return null;
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
    @Override
    public UUID convertToEntityAttribute(byte[] bytes) {
        if (bytes == null || bytes.length != 16) return null;
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }
}
