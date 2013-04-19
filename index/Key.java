package indexing.index;

/**
 * Represents a key and it's position in the file with values. This
 * class exists mostly for memory efficiency, so that the long is kept as
 * primitive and if needed the key can be represented as an array of bytes
 * and not string. Also these can be stored sorted in an array sorted by key
 * and binary search can be used to find the position for a key, so that we
 * don't have to use map.
 */
public class Key<K extends Comparable<K>> implements Comparable<Key<K>> {
  private K key;
  private long position;

  public Key(K key) {
    this(key, 0);
  }

  public Key(K key, long position) {
    this.key = key;
    this.position = position;
  }

  long position() {
    return position;
  }

  public K key() {
    return key;
  }

  public int compareTo(Key<K> keyPosition) {
    return key.compareTo(keyPosition.key);
  }
}
