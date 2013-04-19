package indexing.map;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * Maps each word in a line to all other words.
 */
public class Mapper {
  private MapperOutput output;
  private CharMatcher matcher;

  public Mapper(MapperOutput output) {
    this.output = output;
    this.matcher = CharMatcher.JAVA_LETTER_OR_DIGIT.negate();
  }
  
  public void map(String line) throws IOException {
    List<String> words = Lists.newArrayList(Splitter.on(matcher)
        .trimResults()
        .omitEmptyStrings()
        .split(line));

    for (int i = 0; i < words.size(); i++) {
      for (int j = 0; j < words.size(); j++) {
        if (i != j) {
          output.write(words.get(i), words.get(j));
        }
      }
    }
  }
}
