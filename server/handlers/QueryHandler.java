package indexing.server.handlers;

import indexing.common.Pair;
import indexing.index.IndexedFileMap;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handler that responds with paginated results for a query.
 */
public class QueryHandler extends AbstractHandler {
  private static final int RESULTS_PER_PAGE = 15;

  private IndexedFileMap<String, List<Pair<String, Integer>>> index;

  public QueryHandler(IndexedFileMap<String, List<Pair<String, Integer>>> index) {
    this.index = index;
  }

  /**
   * Responds with all results in a page for a query. 
   */
  public void handle(URI uri, OutputStream outputStream) throws IOException {
    Map<String, String> params = urlParams(uri);

    List<Pair<String, Integer>> allResults = index.get(params.get("query"));
    if (allResults == null) {
      allResults = new ArrayList<Pair<String, Integer>>();
    }
    int page = (params.containsKey("page")) ? Integer.parseInt(params.get("page")) : 1;

    QueryResults results = new QueryResults(allResults, page);
    outputStream.write(toResultsJson(results));    
  }

  /**
   * Represented paginated results. Used for json serialization of the results.
   */
  public class QueryResults {
    List<Pair<String, Integer>> results;
    int totalResults;
    int perPage;
    int currentPage;
    
    public QueryResults(List<Pair<String, Integer>> allResults, int page) {    
      perPage = RESULTS_PER_PAGE;
      totalResults = allResults.size();
      currentPage = page;

      int from = Math.min((page - 1) * RESULTS_PER_PAGE, allResults.size());
      int to = Math.min(page * RESULTS_PER_PAGE, allResults.size());

      results = allResults.subList(from, to);
    }
  }
}