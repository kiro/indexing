package indexing.server.handlers;

import com.google.common.collect.Lists;
import indexing.common.Pair;
import indexing.index.IndexedFileMap;
import indexing.index.Key;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Handler that responds with json object with suggestions for a query.
 */
public class SuggestHandler extends AbstractHandler {
  private IndexedFileMap<String, List<Pair<String, Integer>>> index;

  public SuggestHandler(IndexedFileMap<String, List<Pair<String, Integer>>> index) {
    this.index = index;
  }

  public void handle(URI uri, OutputStream outputStream) throws IOException {
    String prefix = urlParams(uri).get("query");
    List<String> suggestions = getSuggestions(prefix);

    SuggestResults suggestResults = new SuggestResults();
    suggestResults.results = suggestions;
    outputStream.write(toResultsJson(suggestResults));
  }

  /**
   * Gets the suggestions for a given prefix, by doing a binary search to find
   * the position of the prefix in the sorted list of keys and then returns the
   * next 8 values.
   */
  private List<String> getSuggestions(String prefix) {
    List<Key<String>> keys = index.keys();
    int index = Collections.binarySearch(keys, new Key(prefix), new IgnoreCaseComparator());
    index = Math.abs(index) - 1;

    List<String> result = Lists.newArrayList();
    for (int i = index; i < Math.min(index + 8, keys.size()); i++) {
      result.add(keys.get(i).key());
    }

    return result;
  }

  /**
   * Used for representing the results as json.
   */
  public class SuggestResults {
    List<String> results;
  }

  /**
   * Compares string by ignoring the case.
   */
  private class IgnoreCaseComparator implements Comparator<Key<String>> {
    public int compare(Key<String> key1, Key<String> key2) {
      return key1.key().toLowerCase().compareTo(key2.key().toLowerCase());
    }
  }  
}