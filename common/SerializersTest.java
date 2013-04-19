package indexing.common;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static indexing.common.Serializers.*;
import static java.util.Arrays.asList;

/**
 * SerializersTest
 */
public class SerializersTest extends TestCase {
  public void testIntegerSerializer() throws Exception {
    Serializer<Integer> integerSerializer = integer();

    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    CodedOutputStream stream = CodedOutputStream.newInstance(buf);
    integerSerializer.write(stream, 5);
    stream.flush();

    assertEquals(5, (int)integerSerializer.read(CodedInputStream.newInstance(buf.toByteArray())));
  }

  public void testSerializers() throws Exception {
    Serializer<List<Pair<String, Integer>>> serializer = serializer(list(pair(string(), integer())));

    ByteArrayOutputStream arr = new ByteArrayOutputStream();
    CodedOutputStream outputStream = CodedOutputStream.newInstance(arr);
    List<Pair<String, Integer>> list = asList(Pair.pair("a", 1), Pair.pair("b", 2), Pair.pair("xyz", 124));
    
    serializer.write(outputStream, list);
    outputStream.flush();
    CodedInputStream inputStream = CodedInputStream.newInstance(new ByteArrayInputStream(arr.toByteArray()));

    assertEquals(list, serializer.read(inputStream));    
  }
}
