package indexing.server;

import com.sun.net.httpserver.HttpServer;
import indexing.common.Pair;
import indexing.index.IndexedFileMap;
import indexing.server.handlers.FileHandler;
import indexing.server.handlers.QueryHandler;
import indexing.server.handlers.SuggestHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import static indexing.common.Serializers.*;

/**
 * Simple HTTP server, that serves search requests from the index. It has urls that return
 * suggestions for a prefix and the results for a search query.
 */
public class Server {
  /**
   * Runs the server. 
   *
   * @throws IOException
   */
  public void run(String indexFile, String rootPath, int port) throws IOException {
    IndexedFileMap<String, List<Pair<String, Integer>>> index = IndexedFileMap
        .newInstance(indexFile, serializer(string()), serializer(list(pair(string(), integer()))));

    HttpServer server = HttpServer.create(new InetSocketAddress(port), -1);
    server.createContext("/", new FileHandler(rootPath));
    server.createContext("/suggest", new SuggestHandler(index));
    server.createContext("/query", new QueryHandler(index));
    server.setExecutor(null);
    System.out.println("Server started. Please go to http://localhost:" + port + "/index.html");
    server.start();
  }
}
