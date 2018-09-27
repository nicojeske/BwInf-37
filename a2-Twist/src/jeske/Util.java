package jeske;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Util {

  /**
   * Given a string and a RegEx pattern, apply a given method to each hit and replace it with the result of the method.
   * @param input input string
   * @param regex regex pattern
   * @param callback replace method
   * @return replaced string
   */
  static String replace(String input, Pattern regex, StringReplacerCallback callback) {
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
   * @return vom User ausgwählte Datei. Null wenn keine Datei ausgewählt wurde.
   * @param fileDescription Beschreibung der Datei
   * @param fileExtension Dateiformat (txt, png, jpg, usw.)
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
}
