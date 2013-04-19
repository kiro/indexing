package indexing.common;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;

/**
 * Defines an interface for reading and writing objects of type T
 * using CodedInputStream and CodedOutputStream. 
 */
public interface Serializer<T> {
  /**
   * Serializes value to dataOutput.
   */
  public void write(CodedOutputStream codedOutputStream, T value) throws IOException;

  /**
   * Reads an object pair type T from dataInput.
   * @param codedInputStream
   */
  public T read(CodedInputStream codedInputStream) throws IOException;
}
