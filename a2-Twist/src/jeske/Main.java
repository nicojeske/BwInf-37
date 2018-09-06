package jeske;


import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class Main {

  private static Map<String, List<String>> dict = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        Log.set(Log.LEVEL_INFO);

      BufferedReader directoryReader = new BufferedReader(
              new InputStreamReader(
                      Main.class.getResourceAsStream("/beispieldaten/woerterliste.txt")
              )
      );

      initializeDictionary(directoryReader);

      GUI.main(null);

      //TODO Groß und Kleinschreibung beim Decoden beibehalten
      //TODO GUI
    }

    public String twist(String sentence) {
      return Util.replace(sentence, Pattern.compile("([A-zöäüÖÄÜ])+"), match -> twistWord(match.group()));
    }

  private static void initializeDictionary(BufferedReader directoryReader) throws IOException {
    String currLine;
    while((currLine = directoryReader.readLine()) != null){
      currLine = currLine.trim();
      currLine = currLine.toLowerCase();
      char[] chars = currLine.toCharArray();
      Arrays.sort(chars);
      String charsString = new String(chars);
      if(dict.containsKey(charsString)) {
        dict.get(charsString).add(currLine);
      } else {
        dict.put(charsString, new ArrayList<>());
        dict.get(charsString).add(currLine);
      }
    }
  }

  public String decode(String encoded) {
    String decoded = Util.replace(encoded, Pattern.compile("([A-zöäüÖÄÜ])+"), match -> decodeWord(match.group()));
    return decoded;
  }

  private static String decodeWord(String group) {
    //TODO Respektieren der Groß und kleinschreibung (einfach vom eigentlichen Wort ersten Buchstaben übernehmen?)
      group = group.toLowerCase();
    char[] chars = group.toCharArray();
    Arrays.sort(chars);
    String charsString = new String(chars);
    Log.debug("Finding Dict entry for -> " + group);
    if(dict.containsKey(charsString)){
      Log.debug("Possibilities -> " + dict.get(charsString));
      String ret = "";
      if(dict.get(charsString).size() > 1) {
        ret = getFittingWord(dict.get(charsString), group.toCharArray());
      } else {
        ret = dict.get(charsString).get(0);
      }
      Log.debug("Recognized " + group + " -> " + ret);
      return ret;
    } else {
      Log.debug(group + " not found...");
      return group;
    }
  }

  private static String getFittingWord(List<String> words, char[] chars) {
    char firstLetter = chars[0];
    char lastLetter = chars[chars.length-1];
    ArrayList<String> ret = new ArrayList<>();
    for(String word : words){
      if(word.charAt(0) == firstLetter && word.charAt(word.length()-1) == lastLetter) {
        ret.add(word);
      }
    }

    if(ret.size() == 1) {
      return ret.get(0);
    } else if(ret.size() > 1) {
      return ret.toString();
    } else {
      Log.warn("No fitting word found for " + new String(chars));
      return new String(chars);
    }
  }

  private static String twistWord(String word) {

    if(word.length() <= 3)
      return word;

    List<Character> charsToTwist = new ArrayList<>();
    for (char c : word.substring(1, word.length() - 1).toCharArray()) {
      charsToTwist.add(c);
    }

    Collections.shuffle(charsToTwist);

    StringBuilder twistedPartBuilder = new StringBuilder();
    charsToTwist.forEach(twistedPartBuilder::append);

    String twistedWord = ""+word.charAt(0) + twistedPartBuilder.toString() + word.charAt(word.length()-1);

    //FIXME Crash if word like "jjjjjj"
    if(twistedWord.equals(word))
      return twistWord(word);

    return twistedWord;
    }
}
