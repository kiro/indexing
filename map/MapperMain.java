package indexing.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point for the mapper.
 */
public class MapperMain {
  /**
   * Runs the mapper for each line in the input.
   */
  public void run(BufferedReader input, MapperOutput output) throws IOException {
    Mapper mapper = new Mapper(output);

    String line;
    while ((line = input.readLine()) != null) {
      mapper.map(line);
    }
    output.close();
  }

  public static void main(String [] args) throws Exception {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    new MapperMain().run(input, MapperOutput.toStdOut());
  }
}
