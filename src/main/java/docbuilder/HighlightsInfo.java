package docbuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by rames on 05-05-2019.
 */
public class HighlightsInfo {
    private Highlights highlights;

    public Highlights getHighlights() {
        return highlights;
    }

    public void setHighlights(Highlights highlights) {
        this.highlights = highlights;
    }


}
class Highlights {
    @JsonIgnore
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


