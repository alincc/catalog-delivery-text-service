package no.nb.microservices.catalogdeliverytext.core.dsm;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DsmStream implements StreamingOutput {
    private InputStream data;

    public DsmStream(InputStream data) {
        this.data = data;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        byte[] buffer = new byte[1024 * 64];
        int length = data.read(buffer);
        while (length != -1) {
            outputStream.write(buffer, 0, length);
            length = data.read(buffer);
        }
        data.close();
        this.data = null;
    }
}
