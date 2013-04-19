package indexing.map;

import java.io.IOException;

/**
 * Represents an output of the mapper.
 */
public abstract class MapperOutput {
  public abstract void write(String key, String value) throws IOException;

  /**
   * Close method, it can be overridden in case some resource needs
   * to be closed at the end.
   */
  public void close() throws IOException {
  }

  /**
   * Mapper output to std out.
   */
  public static MapperOutput toStdOut() throws IOException {
    return new MapperOutput() {
      public void write(String key, String value) {
        System.out.println(key + " " + value);
      }
    };
  }
}
