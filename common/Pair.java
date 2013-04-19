package indexing.common;

/**
 * Key/Value pair.
 */
public class Pair<K, V> {
  public final K key;
  public final V value;

  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public static <K, V> Pair<K, V> pair(K key, V value) {
    return new Pair<K, V>(key, value);
  }
  
  @Override
  public boolean equals(Object o) {
    Pair<K, V> p = (Pair<K, V>)o;
    return p.key.equals(this.key) && p.value.equals(this.value);
  }

  @Override
  public String toString() {
    return this.key + " " + this.value; 
  }
}
