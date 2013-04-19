package indexing.common;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Static constructors for common Serializers. They can be chained to construct
 * more complicated serializers, for example:
 *
 * serializer(list(pair(string(), string())), gives a serializer for List<Pair<String, Integer>>.
 */
public class Serializers {
  private Serializers() {
  }

  /**
   * String serializer.
   */
  public static Serializer<String> string() {
    return new Serializer<String>() {
      public void write(CodedOutputStream codedOutputStream, String value) throws IOException {
        byte [] bytes = value.getBytes();
        codedOutputStream.writeRawVarint32(bytes.length);
        codedOutputStream.writeRawBytes(bytes);
      }

      public String read(CodedInputStream codedInputStream) throws IOException {
        int length = codedInputStream.readRawVarint32();
        return new String(codedInputStream.readRawBytes(length));
      }
    };
  }

  /**
   * Integer serializer.
   */
  public static Serializer<Integer> integer() {
    return new Serializer<Integer>() {

      public void write(CodedOutputStream codedOutputStream, Integer value) throws IOException {
         codedOutputStream.writeRawVarint32(value);
      }

      public Integer read(CodedInputStream codedInputStream) throws IOException {
        return codedInputStream.readRawVarint32();
      }
    };
  }

  /**
   * Long serializer.
   */
  public static Serializer<Long> int64() {
    return new Serializer<Long>() {

      public void write(CodedOutputStream codedOutputStream, Long value) throws IOException {
         codedOutputStream.writeRawVarint64(value);
      }

      public Long read(CodedInputStream codedInputStream) throws IOException {
        return codedInputStream.readRawVarint64();
      }
    };
  }

  /**
   * Pair<K, V> serializer.
   */
  public static <K, V> Serializer<Pair<K, V>> pair(final Serializer<K> keySerializer,
                                                   final Serializer<V> valueSerializer) {
    return new Serializer<Pair<K, V>>() {

      public void write(CodedOutputStream codedOutputStream, Pair<K, V> pair) throws IOException {
        keySerializer.write(codedOutputStream, pair.key);
        valueSerializer.write(codedOutputStream, pair.value);
      }

      public Pair<K, V> read(CodedInputStream codedInputStream) throws IOException {
        return Pair.pair(keySerializer.read(codedInputStream), valueSerializer.read(codedInputStream));
      }
    };
  }

  /**
   * Collection<V> serializer.
   */
  public static <V> Serializer<List<V>> list(final Serializer<V> valueSerializer) {
    return new Serializer<List<V>>() {

      public void write(CodedOutputStream codedOutputStream, List<V> collection) throws IOException {
        codedOutputStream.writeRawVarint32(collection.size());
        for (V value : collection) {
          valueSerializer.write(codedOutputStream, value);
        }
      }

      public List<V> read(CodedInputStream codedInputStream) throws IOException {
        int elements = codedInputStream.readRawVarint32();
        List<V> result = new ArrayList<V>();
        for (int i = 0; i < elements; i++) {
          result.add(valueSerializer.read(codedInputStream));
        }

        return result;
      }
    };
  }

  /**
   * Dummy method for syntax sugar when chaining serializers.
   */
  public static <V> Serializer<V> serializer(Serializer<V> serializer) {
    return serializer;
  }
}
