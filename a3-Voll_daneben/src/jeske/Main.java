package jeske;

import com.esotericsoftware.minlog.Log;
import jeske.GUI.Draw;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entry point
 */
public class Main {

  /**
   * Entry point
   *
   * @param args null
   */
  public static void main(String[] args) {
    Log.set(Log.LEVEL_INFO);

    //Get input file
    BufferedReader bufferedReader = getInputFile();

    //Get the numbers from the input file.
    List<Integer> numbers;
    try {
      numbers = getNumbersFromFile(bufferedReader);
    } catch (IllegalArgumentException e) {
      Util.askUserYesNo(e.getMessage() + " Erneut versuchen?",
              "Fehler",
              JOptionPane.ERROR_MESSAGE,
              () -> main(null),
              () -> System.exit(1));
      return;
    }

    Draw draw = new Draw(numbers);
    Solver solver = new Solver(numbers, draw);
    solver.solve();
  }

  /**
   * From a given input file, try to read all number choices
   *
   * @param bufferedReader Input file
   * @return choosen numbers
   * @throws NumberFormatException Input file is not valid e.g. contains text
   */
  private static List<Integer> getNumbersFromFile(BufferedReader bufferedReader) throws NumberFormatException {
    List<Integer> ret = new ArrayList<>();
    bufferedReader.lines().forEach(line -> {
      try {
        int number = Integer.parseInt(line);
        ret.add(number);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Ungültige Eingabedatei.");
      }
    });
    return ret;
  }

  /**
   * IF Log.DEBUG
   * get an input file from classpath
   * ELSE
   * let the user choose an input file
   * if the user chooses nothing, he is asked, if he wants to retry or not.
   * IF he does'nt want to retry, the program exits with -1
   *
   * @return BufferedReader for the input file
   */
  private static BufferedReader getInputFile() {
    BufferedReader bufferedReader = null;
    //In Debug mode, use resource files
    if (Log.DEBUG) {
      bufferedReader = new BufferedReader(
              new InputStreamReader(
                      Main.class.getResourceAsStream("/beispieldaten/beispiel3.txt")
              )
      );

      //In release, let the user choose an input file. If no file is selected, the user can decide
      //to try again or close the program.
    } else {
      File file = Util.getUserFile("Eingabe", "txt");
      try {
        bufferedReader = new BufferedReader(new FileReader(Objects.requireNonNull(file)));
      } catch (FileNotFoundException | NullPointerException e) {
        int answer = JOptionPane.showConfirmDialog(null,
                "Fehler bei der Dateiauswahl. Erneut versuchen?",
                "Fehler",
                JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
          main(null);
          return null;
        } else {
          System.exit(1);
        }
      }
    }

    return bufferedReader;
  }


}

