package indexing.reduce;

import indexing.common.Pair;
import indexing.index.IndexedFileMap;
import indexing.index.IndexedFileMapBuilder;

import java.io.IOException;
import java.util.List;

import static indexing.common.Serializers.*;

/**
 * An abstraction for reducer output, which has an implementation for writing it to index file.
 */
public abstract class ReducerOutput {
  public abstract void write(String key, List<Pair<String, Integer>> values) throws IOException;

  public abstract void close() throws IOException;

  /**
   * Reducer output to indexed file.
   */
  public static ReducerOutput indexedFileOutput(final String filename) throws IOException {
    final IndexedFileMapBuilder<String, List<Pair<String, Integer>>> fileMapBuilder = IndexedFileMap.builder(
          filename,
          serializer(string()),
          serializer(list(pair(string(), integer())))
    );

    return new ReducerOutput() {
      public void write(String key, List<Pair<String, Integer>> values) throws IOException {
        fileMapBuilder.add(key, values);
      }

      public void close() throws IOException {
        fileMapBuilder.build();
      }
    };
  }
}
