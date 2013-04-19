package indexing.index.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * InputStream wrapper around ByteBuffer.
 */
public class ByteBufferInputStream extends InputStream {
  private ByteBuffer byteBuffer;

  public ByteBufferInputStream(ByteBuffer byteBuffer) {
    this.byteBuffer = byteBuffer;
  }

  @Override
  public int read() throws IOException {
    return byteBuffer.hasRemaining() ? byteBuffer.get() & 0xFF : -1;    
  }
}
