package jeske;

import java.util.regex.Matcher;

public interface StringReplacerCallback {
  public String replace(Matcher match);
}
