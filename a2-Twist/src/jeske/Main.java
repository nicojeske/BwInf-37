package jeske;


import com.esotericsoftware.minlog.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);

       // Test.main(null);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Main.class.getResourceAsStream("/beispieldaten/twist1.txt")
                )
        );


        StringBuilder stringBuilder = new StringBuilder();
        String line = reader.readLine();
        String[] words = line.split("([A-zöäüÖÄÜ])+");
        line = Util.replace(line, Pattern.compile("([A-zöäüÖÄÜ])+"), match -> twistWord(match.group()));

       // Log.info("TWISTED TEXT: " + stringBuilder.toString());
    }

  private static String twistWord(String nextToken) {
    char[] chars = new char[nextToken.length()];
    Random rng = new Random();
    chars[0] = nextToken.charAt(0);
    chars[nextToken.length()-1] = nextToken.charAt(nextToken.length()-1);

    ArrayList<Character> remainingChars = new ArrayList<>();
    for (int i = 1; i < nextToken.length()-1; i++) {
      remainingChars.add(nextToken.charAt(i));
    }

    int numberOfRenamingChars = remainingChars.size();
    for (int i = 0; i < numberOfRenamingChars; i++) {
      int randomIndex = rng.nextInt(remainingChars.size());
      char currChar = remainingChars.get(randomIndex);
      remainingChars.remove(remainingChars.indexOf(currChar));
      chars[i+1] = currChar;
    }

    Log.debug("TWISTED WORD: " + nextToken + " TO " + new String(chars));
    return new String(chars);
  }
}
