package Server;

import java.io.InputStream;
import java.io.OutputStream;

public interface ServerStrategy {

    public void HandleClient(InputStream IStream , OutputStream OStream);
}
