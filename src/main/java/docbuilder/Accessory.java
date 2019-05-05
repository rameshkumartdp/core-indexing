package docbuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by rames on 05-05-2019.
 */
public class Accessory {
    private String type;
    private String description;
    @JsonIgnore
    private String measurements;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeasurements() {
        return measurements;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }
}
