package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import docbuilder.SearchDoc;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

@Component
public class TransformJson {

    public TransformJson() { }

    public SearchDoc getObjectFromJson() {
        SearchDoc object = null;
        try {
            FileReader inJson = new FileReader("src/main/resources/product.json");
            object = new ObjectMapper().readValue(inJson, SearchDoc.class);
        } catch(IOException io) {
            io.printStackTrace();
        }
        return object;
    }

}
