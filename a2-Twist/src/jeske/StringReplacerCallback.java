package jeske;

import java.util.regex.Matcher;

/**
 * Interface for a method that replaces a RegEx match.
 */
public interface StringReplacerCallback {
  String replace(Matcher match);
}
