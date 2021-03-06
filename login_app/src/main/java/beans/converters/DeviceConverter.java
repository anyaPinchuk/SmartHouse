package beans.converters;

import dto.DeviceDTO;
import entities.Device;
import entities.solr.SolrDevice;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class DeviceConverter {
    public Optional<Device> toEntity(DeviceDTO dto) {
        if (dto == null)
            return Optional.empty();
        return Optional.of(new Device(dto.getId(), dto.getName(), dto.getModel(), dto.getState(), dto.getPower()));
    }

    public Optional<DeviceDTO> toDTO(Device entity) {
        if (entity == null)
            return Optional.empty();
        return Optional.of(new DeviceDTO(entity.getId(), entity.getName(), entity.getModel(), entity.getState(),
                entity.getPower()));
    }

    public Optional<DeviceDTO> toDTO(SolrDevice entity) {
        if (entity == null)
            return Optional.empty();
        return Optional.of(new DeviceDTO(Long.valueOf(entity.getId()), entity.getName(), entity.getModel(), entity.getState(),
                entity.getPower()));
    }
}
