package jeske;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Util {
  /**
   * Lässt den Anwender eine Eingabedatei auswählen und gibt diese Datei zurück.
   *
   * @param fileDescription Beschreibung der Datei
   * @param fileExtension   Dateiformat (txt, png, jpg, usw.)
   * @return vom User ausgwählte Datei. Null wenn keine Datei ausgewählt wurde.
   * @throws IllegalArgumentException Der User hat keine Datei ausgewählt
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
   * Provides the user with a Yes No selection. Depending on the selection, the corresponding runnable will be executed.
   *
   * @param message    message
   * @param title      title
   * @param optionType optionType
   * @param yesAction  Runnable for yes
   * @param noAction   Runnable for no
   */
  static void askUserYesNo(String message, String title, int optionType, Runnable yesAction, Runnable noAction) {
    int answer = JOptionPane.showConfirmDialog(null,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            optionType);

    if (answer == JOptionPane.YES_OPTION) {
      yesAction.run();
    } else {
      noAction.run();
    }
  }

  /**
   * Maps a number from Range A to Range B
   *
   * @param value       value
   * @param range1Start range A start
   * @param range1End   range A end
   * @param range2Start range B start
   * @param range2End   range B end
   * @return mapped number
   */
  public static double map(double value,
                           double range1Start, double range1End,
                           double range2Start, double range2End) {

    final double EPSILON = 1e-12;

    if (Math.abs(range1End - range1Start) < EPSILON) {
      throw new ArithmeticException("/ 0");
    }

    double ratio = (range2End - range2Start) / (range1End - range1Start);
    return ratio * (value - range1Start) + range2Start;
  }
}
