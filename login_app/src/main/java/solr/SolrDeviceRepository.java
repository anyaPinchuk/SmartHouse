package solr;

import entities.House;
import entities.solr.SolrDevice;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrDeviceRepository extends SolrCrudRepository<SolrDevice, String> {
    @Query("(name:*?0* OR model:*?0*) && house_id:?1")
    List<SolrDevice> find(String searchItem, String houseId);

    @Query("(name:*?0* OR model:*?0*) && house_id:?1")
    List<SolrDevice> find(String searchItem, String houseId, PageRequest pageRequest);

    Long countByHouseId(String houseId);

    List<SolrDevice> findByHouseId(String houseId, PageRequest pageRequest);
}
