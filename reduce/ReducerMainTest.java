package indexing.reduce;

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
 * ReducerMainTest
 */
public class ReducerMainTest extends TestCase {
  public void testReducer() throws Exception {
    TestReducerOutput output = new TestReducerOutput();
    String data = "a b\n" +
                  "x y\nx y\nx y\nx a\nx a\nx a\nx b\nx b\nx c\n" +
                  "y x\ny x\ny z";

    new ReducerMain().run(new BufferedReader(new StringReader(data)), output);

    assertEquals(output.result, asList(
        pair("a", asList(pair("b", 1))),
        pair("x", asList(pair("a", 3), pair("y", 3), pair("b", 2), pair("c", 1))),
        pair("y", asList(pair("x", 2), pair("z", 1)))
    ));
  }

  public void testReducerOutput() throws IOException {
    ReducerOutput.indexedFileOutput("index");

  }

  private class TestReducerOutput extends ReducerOutput {
    boolean closeCalled = false;
    List<Pair<String, List<Pair<String, Integer>>>> result =
        new ArrayList<Pair<String, List<Pair<String, Integer>>>>();

    @Override
    public void write(String key, List<Pair<String, Integer>> values) throws IOException {
      result.add(pair(key, values));
    }

    @Override
    public void close() throws IOException {
      closeCalled = true;
    }
  }
}
