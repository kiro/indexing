package indexing.index.io;

import com.google.protobuf.CodedOutputStream;
import indexing.common.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Serializes objects of type T to file, using FileChannel for faster io.
 */
public class FileOutput<T> {
  private static final int BUFFER_SIZE = 64 * 1024 * 1024;

  private FileChannel outputFile;
  private Serializer<T> serializer;
  private ByteArrayOutputStream buffer = new ByteArrayOutputStream(BUFFER_SIZE);
  private CodedOutputStream codedStream = CodedOutputStream.newInstance(buffer);

  public FileOutput(String filename, Serializer<T> serializer) throws FileNotFoundException {
    this.outputFile = new RandomAccessFile(filename, "rw").getChannel();    
    this.serializer = serializer;
  }

  /**
   * Serializes value of type T to the file.
   */
  public void write(T value) throws IOException {    
    serializer.write(codedStream, value);
    codedStream.flush();

    if (buffer.size() > BUFFER_SIZE) {
      outputFile.write(ByteBuffer.wrap(buffer.toByteArray()));
      buffer.reset();
    }
  }

  /**
   * Returns the current position in the file.
   */
  public long position() throws IOException {
    return outputFile.position() + buffer.size();
  }

  /**
   * Closes the file.
   */
  public void close() throws IOException {
    codedStream.flush();
    outputFile.write(ByteBuffer.wrap(buffer.toByteArray()));    
    outputFile.close();
  }
}