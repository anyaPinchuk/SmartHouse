package entities.solr;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(solrCoreName = "device")
public class SolrDevice {

    @Id
    @Indexed(name = "id", type = "string")
    private String id;

    @Indexed(name = "name", type = "string")
    private String name;

    @Indexed(name = "model", type = "string")
    private String model;

    @Indexed(name = "state", type = "string")
    private String state;

    @Indexed(name = "power", type = "string")
    private String power;

    @Indexed(name = "house_id", type = "string")
    private String houseId;


    public SolrDevice() {}

    public SolrDevice(String id, String name, String model, String state, String power, String houseId) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.state = state;
        this.power = power;
        this.houseId = houseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }
}
