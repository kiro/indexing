package indexing.index;

import com.google.protobuf.CodedInputStream;
import indexing.common.Serializer;
import indexing.index.io.ByteBufferInputStream;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A map from objects of type K to objects of type V, saved on disk. It uses two
 * files, one contains the values. The second with .index extension contains
 * records for the position of each key in the file with values.
 */
public class IndexedFileMap<K extends Comparable<K>, V> {
  static final String INDEX_EXTENSION = ".index";

  private final Serializer<V> valueSerializer;
  private final String filename;
  private final FileChannel dataFile;

  private List<Key<K>> positionIndex = new ArrayList<Key<K>>();  

  /**
   * Constructs a new IndexedFileMap, that reads the values from filename. The index from key
   * to value position from filename.index. And uses keySerializer and valueSerializer to
   * serialize and deserialize the key and values.
   */
  public IndexedFileMap(String filename, Serializer<K> keySerializer, Serializer<V> valueSerializer)
      throws IOException {

    this.filename = filename;
    this.valueSerializer = new SizeSerializer<V>(valueSerializer);
    this.dataFile = new RandomAccessFile(this.filename, "rw").getChannel();

    RandomAccessFile indexFile = new RandomAccessFile(filename + INDEX_EXTENSION, "rw");
    ByteBuffer indexFileContent = indexFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, indexFile.length());

    CodedInputStream inputStream = CodedInputStream.newInstance(new ByteBufferInputStream(indexFileContent));

    // load the index
    while (!inputStream.isAtEnd()) {
      K key = keySerializer.read(inputStream);
      long position = inputStream.readRawVarint64();
      positionIndex.add(new Key<K>(key, position));
      inputStream.resetSizeCounter();
    }
  }

  /**
   * Reads the value for a key.
   */
  public V get(K key) throws IOException {
    int index = Collections.binarySearch(positionIndex, new Key<K>(key, 0));
    if (index < 0) {
      return null;
    }

    long position = positionIndex.get(index).position();
    byte [] sizeBytes = new byte[4];
    dataFile.read(ByteBuffer.wrap(sizeBytes), position);
    int size = CodedInputStream.newInstance(sizeBytes).readRawLittleEndian32();

    byte [] value = new byte[size + 4];
    dataFile.read(ByteBuffer.wrap(value), position);
            
    return valueSerializer.read(CodedInputStream.newInstance(value));
  }

  /**
   * Returns the keys in the map.
   */
  public List<Key<K>> keys() {
    return positionIndex;
  }

  /**
   * Constructs builder for IndexFileMap.
   */
  public static <K extends Comparable<K>, V> IndexedFileMapBuilder<K, V> builder(
      String filename, Serializer<K> keySerializer, Serializer<V> valueSerializer)
        throws IOException {
    return new IndexedFileMapBuilder<K, V>(filename, keySerializer, valueSerializer);
  }

  /**
   * Constructs indexed file map.
   */
  public static <K extends Comparable<K>, V> IndexedFileMap<K, V> newInstance(
      String filename, Serializer<K> keySerializer, Serializer<V> valueSerializer)
        throws IOException {
    return new IndexedFileMap<K, V>(filename, keySerializer, valueSerializer);
  }
}