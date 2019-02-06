package index;

import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Test on 2/7/2019.
 */
public class JSONUpdateRequest extends AbstractUpdateRequest {
    private final InputStream jsonInputStream;

    public JSONUpdateRequest(InputStream jsonInputStream) {
        super(METHOD.POST, "/update/json/docs");
        this.jsonInputStream = jsonInputStream;
        this.setParam("json.command", "false");
    }

    public void addFieldMapping(String field, String jsonPath) {
        getParams().add("f", field + ":" + jsonPath);
    }

    public void setSplit(String jsonPath) {
        setParam("split", jsonPath);
    }

    @Override
    public Collection<ContentStream> getContentStreams() throws IOException {
        ContentStream jsonContentStream = new InputStreamContentStream(
                jsonInputStream, "application/json");
        return Collections.singletonList(jsonContentStream);
    }

    private class InputStreamContentStream extends ContentStreamBase {
        private final InputStream inputStream;

        public InputStreamContentStream(InputStream inputStream,
                                        String contentType) {
            this.inputStream = inputStream;
            this.setContentType(contentType);
        }

        @Override
        public InputStream getStream() throws IOException {
            return inputStream;
        }
    }
}
