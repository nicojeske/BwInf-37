package jeske;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
  public static String replace(String input, Pattern regex, StringReplacerCallback callback) {
    StringBuffer resultString = new StringBuffer();
    Matcher regexMatcher = regex.matcher(input);
    while (regexMatcher.find()) {
      regexMatcher.appendReplacement(resultString, callback.replace(regexMatcher));
    }
    regexMatcher.appendTail(resultString);

    return resultString.toString();
  }

  /**
   * Lässt den Anwender eine Eingabedatei auswählen und gibt diese Datei zurück.
   *
   * @throws IllegalArgumentException Der User hat keine Datei ausgewählt
   * @return vom User ausgwählte Datei
   * @param fileDescription
   * @param fileExtension
   */
  static File getUserFile(String fileDescription, String fileExtension) {
    //Erstellung eines neuen Filechoosers
    JFileChooser chooser = new JFileChooser();

    //Limitiert Filechooser auf txt
    chooser.setFileFilter(new FileNameExtensionFilter(fileDescription, fileExtension));

    //Setzt den Filechooser auf den Ordner, aus dem die jar ausgeführt wurde
    chooser.setCurrentDirectory(new File("."));

    File file;

    int fileChooser = chooser.showOpenDialog(null); //Rückgabewert des Filechoosers

    if (fileChooser == 0) { //Eine Datei wurde im Filechooser ausgewählt
      file = chooser.getSelectedFile();
      return file; //Rückgabe des ausgewählten Verkehrsnetzes
    }

    return null;
  }

  /**
   * Lässt den User zwischen den gegebenen Möglichkeiten auswählen
   *
   * @param message Nachricht
   * @param title   Titel
   * @param choice  Auswahlmöglichkeiten
   * @return Wahl des Users
   */
  static int askUser(String message, String title, Object[] choice) {
    return JOptionPane.showOptionDialog(null,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            choice,
            0);
  }
}
