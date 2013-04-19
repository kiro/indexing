package indexing.map;

import indexing.common.Pair;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static indexing.common.Pair.pair;
import static java.util.Arrays.asList;

/**
 * MapperMainTest
 */
public class MapperMainTest extends TestCase {
  public void testMapper() throws IOException {
    TestMapperOutput output = new TestMapperOutput();

    new MapperMain().run(
        new BufferedReader(new StringReader(" x y z \na b \n c\n")),
        output);

    assertEquals(output.results,
        asList(
          pair("x", "y"),
          pair("x", "z"),
          pair("y", "x"),
          pair("y", "z"),
          pair("z", "x"),
          pair("z", "y"),
          pair("a", "b"),
          pair("b", "a")
        ));
  }

  private static class TestMapperOutput extends MapperOutput {
    public List<Pair<String, String>> results = new ArrayList<Pair<String, String>>();

    @Override
    public void write(String key, String value) {
      results.add(Pair.pair(key, value));
    }
  }
}
