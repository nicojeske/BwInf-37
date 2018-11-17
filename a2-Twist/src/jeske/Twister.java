package jeske;

import com.esotericsoftware.minlog.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

class Twister {

  /**
   * Dictionary.
   * Works according to the following system:
   * The keys are the letters of a word in ascending order. (e.g. for Auto -> acr)
   * The value then represents all the words in a list that can be formed with these letters.
   */
  private static Map<String, List<String>> dict = new HashMap<>();

  Twister() throws IOException {
    Log.set(Log.LEVEL_INFO);

    BufferedReader directoryReader = new BufferedReader(
            new InputStreamReader(
                    this.getClass().getResourceAsStream("/beispieldaten/woerterliste.txt")
            )
    );

    initializeDictionary(directoryReader);
  }

  /**
   * Twists a String
   * @param string String to twist
   * @return twisted string
   */
  String twist(String string) {
    //Takes each word of the string and twists it with the function twistWord
    return Util.replace(string, Pattern.compile("([A-zöäüÖÄÜ])+"), match -> twistWord(match.group()));
  }

  /**
   * Initializes the directory
   * @param directoryReader BufferedReader from the worterliste.txt
   * @throws IOException Error while reading the file
   */
  private static void initializeDictionary(BufferedReader directoryReader) throws IOException {
    String currLine;
    //For every word in the dictionary
    while((currLine = directoryReader.readLine()) != null){
      //Remove leading and trailing whitespaces and convert string to lowercase letters
      String charsString = convertWordToDictionaryForm(currLine);
      if(dict.containsKey(charsString)) {
        dict.get(charsString).add(currLine);
      } else {
        dict.put(charsString, new ArrayList<>());
        dict.get(charsString).add(currLine);
      }
    }
    System.out.println();
  }

  /**
   * Try's to untwist a String.
   * @param encoded string
   * @return The best possible decoded string
   */
  String untwist(String encoded) {
    //Takes each word of the string and try's to untwist it with the function untwistWord
    return Util.replace(encoded, Pattern.compile("([A-zöäüÖÄÜ])+"), match -> untwistWord(match.group()));
  }

  /**
   * Try's to untwist a given word
   * @param word word
   * @return With one possibility: The possibility in plain text.
   *         For several possibilities: Option 1 | Option 2 | ...]
   *         If no solution has been found: [?Original Word?]
   */
  private static String untwistWord(String word) {

    //If the word has between 1 and 3 letters it can't be twisted.
    if(word.length()<=3)
      return word;

    //Saves the first letter to keep upper and lower case later on
    char firstChar = word.charAt(0);
    //Convert word in dictionary form
    String charsString = convertWordToDictionaryForm(word);
    Log.debug("Finding Dict entry for -> " + word);
    //If the word is in the dictionary, find fitting words.
    if(dict.containsKey(charsString)){
      Log.debug("Possibilities -> " + dict.get(charsString));
      String ret;
      //Try's to get a fitting Word from the possible words.
      if(dict.get(charsString).size() > 1) {
        ret = getFittingWord(dict.get(charsString), word.toCharArray());
        if(ret.charAt(0) != '[') {
          ret = firstChar + ret.substring(1);
        }
      } else {
        ret = dict.get(charsString).get(0);
        ret = firstChar + ret.substring(1);
      }

      Log.debug("Recognized " + word + " -> " + ret);
      return ret;

    //If the word is not in the dictionary
    } else {
      Log.info(word + " not found...");
      return "[?" + firstChar + word.substring(1) + "?]";
    }
  }

  /**
   * Converts a word into the dictionary format: lowercase, sorted in ascending order.
   * @param word word
   * @return dictionary format
   */
  private static String convertWordToDictionaryForm(String word) {
    word = word.trim();
    word = word.toLowerCase();
    char[] chars = word.toCharArray();
    Arrays.sort(chars);
    return new String(chars);
  }

  /**
   * Gets the possible words from the dictionary and returns them if possible.
   * Given is only that the word from the dictionary contains all the same chars.
   * Now must be checked, if the start and the end char are the same.
   * @param words Word list from the dictionary fitting to the word you are trying to decode
   * @param chars char[] from the word you are trying to decode
   * @return With one possibility: The possibility in plain text.
   *         For several possibilities: [Option 1 | Option 2 | ...]
   *         If no solution has been found: [?Original Word?]
   */
  private static String getFittingWord(List<String> words, char[] chars) {
    char firstLetter = chars[0];
    char lastLetter = chars[chars.length-1];
    ArrayList<String> ret = new ArrayList<>();
    for(String word : words){
      //Beginning and ending letter match -> the word is a solution.
      if(word.charAt(0) == firstLetter && word.charAt(word.length()-1) == lastLetter) {
        ret.add(word);
      }
    }

    //Exactly one solution -> return solution
    if(ret.size() == 1) {
      return ret.get(0);
    //More then one solution -> return List representation from the solutions.
    } else if(ret.size() > 1) {
      return ret.toString();
    //No solution -> [?word?]
    } else {
      Log.warn("No fitting word found for " + new String(chars));
      return  "[?"+ new String(chars)+"?]";
    }
  }

  /**
   * Twists a given word
   * @param word word
   * @return twisted word
   */
  private static String twistWord(String word) {
    //If word is between 1 and 3 chars long, it can't be twisted.
    if(word.length() <= 3)
      return word;

    //Twisting the word.

    List<Character> charsToTwist = new ArrayList<>();
    for (char c : word.substring(1, word.length() - 1).toCharArray()) {
      charsToTwist.add(c);
    }

    Collections.shuffle(charsToTwist);

    StringBuilder twistedPartBuilder = new StringBuilder();
    charsToTwist.forEach(twistedPartBuilder::append);


    //If the twisted Word is the same as the original, twist it again.
    if (twistedPartBuilder.toString().equals(word.substring(1, word.length() - 1))) {
      boolean possible = false;
      char firstChar = charsToTwist.get(0);
      for (Character character : charsToTwist) {
        if (character != firstChar) {
          possible = true;
          break;
        }
      }

      if (possible) {
        twistWord(word);
      }
    }


    return ""+word.charAt(0) + twistedPartBuilder.toString() + word.charAt(word.length()-1);
  }
}
