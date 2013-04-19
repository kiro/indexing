package indexing.index;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import indexing.common.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Serializes the size of the object, before serializing the content.
 * Useful when you want to know how much are you going to read.
 */
class SizeSerializer<V> implements Serializer<V> {
  private Serializer<V> serializer;
  private ByteArrayOutputStream buf = new ByteArrayOutputStream();
  private CodedOutputStream stream = CodedOutputStream.newInstance(buf);
  
  public SizeSerializer(Serializer<V> serializer) {
    this.serializer = serializer;
  }

  public void write(CodedOutputStream codedOutputStream, V value) throws IOException {
    buf.reset();
    serializer.write(stream, value);
    stream.flush();

    codedOutputStream.writeRawLittleEndian32(buf.size());
    codedOutputStream.writeRawBytes(buf.toByteArray());
  }

  public V read(CodedInputStream codedInputStream) throws IOException {
    int size = codedInputStream.readRawLittleEndian32();
    return serializer.read(codedInputStream);
  }
}
