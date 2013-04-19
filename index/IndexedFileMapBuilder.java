package indexing.index;

import com.google.common.base.Preconditions;
import indexing.common.Pair;
import indexing.common.Serializer;
import indexing.index.io.FileOutput;

import java.io.IOException;

import static indexing.common.Serializers.*;

/**
 * A builder for IndexedFileMap.
 */
public class IndexedFileMapBuilder<K extends Comparable<K>, V> {
  private FileOutput<Pair<K, Long>> indexOutput;
  private FileOutput<V> valueOutput;
  private K lastKey;

  /**
   * Constructs IndexedMapBuilder that writes to filename.
   */
  IndexedFileMapBuilder(String filename,
                        Serializer<K> keySerializer,
                        Serializer<V> valueSerializer) throws IOException {

    this.indexOutput = new FileOutput<Pair<K, Long>>(filename + IndexedFileMap.INDEX_EXTENSION,
        serializer(pair(keySerializer, int64())));
    this.valueOutput = new FileOutput<V>(filename, new SizeSerializer<V>(valueSerializer));
  }

  /**
   * Adds a key, value to the indexed map.
   */
  public IndexedFileMapBuilder<K, V> add(K key, V value) throws IOException {
    Preconditions.checkArgument(lastKey == null || lastKey.compareTo(key) <= 0,
        "Keys must be added in sorted order");    
    lastKey = key;
     
    indexOutput.write(Pair.pair(key, valueOutput.position()));
    valueOutput.write(value);
    
    return this;
  }

  public void build() throws IOException {
    indexOutput.close();
    valueOutput.close();
  }
}