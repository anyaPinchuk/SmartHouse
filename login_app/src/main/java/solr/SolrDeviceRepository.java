package solr;

import entities.solr.SolrDevice;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.List;

public interface SolrDeviceRepository extends SolrCrudRepository<SolrDevice, String> {
    @Query("(name:*?0* OR model:*?0*) && house_id:?1")
    List<SolrDevice> find(String searchItem, String houseId);
}
