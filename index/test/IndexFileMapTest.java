package indexing.index.test;

import indexing.common.Pair;
import indexing.common.Serializers;
import indexing.index.IndexedFileMap;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;

import static indexing.common.Pair.pair;
import static indexing.common.Serializers.*;
import static java.util.Arrays.asList;

/**
 * IndexFileMapTest
 */
public class IndexFileMapTest extends TestCase {
  private static final String INDEX_FILE = "test";

  public static void testIndexFileMap() throws Exception {
    List<Pair<String, Integer>> value1 = asList(pair("value", 1), pair("zzzz", 2), pair("eeee", 3));
    List<Pair<String, Integer>> value2 = asList(pair("value1", 1));
    List<Pair<String, Integer>> value3 = asList(pair("xxx", 1), pair("tttt", 2));
    List<Pair<String, Integer>> value4 = asList();

    IndexedFileMap.builder(INDEX_FILE, serializer(string()), serializer(list(Serializers.pair(string(), integer()))))
        .add("aa", value4)
        .add("key", value1)
        .add("u", value3)
        .add("xxxx", value2)                
        .build();

    IndexedFileMap<String, List<Pair<String, Integer>>> map = IndexedFileMap.newInstance(INDEX_FILE,
        serializer(string()), serializer(list(Serializers.pair(string(), integer()))));

    assertEquals(map.get("key"), value1);
    assertEquals(map.get("xxxx"), value2);
    assertEquals(map.get("u"), value3);
    assertEquals(map.get("aa"), value4);
    assertEquals(map.get("ccc"), null);
  }

  @Override
  public void setUp() {
    new File(INDEX_FILE).delete();
    new File(INDEX_FILE + ".index").delete();
  }
}