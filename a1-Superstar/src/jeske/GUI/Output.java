package jeske.GUI;

//Imports

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

/**
 * Graphische Ausgabe der Konsole durch Umleitung
 * der Printstreams.
 */
public class Output extends JFrame {
  private JPanel panel = new JPanel();
  private JTextArea log = new JTextArea();

  /**
   * setzt Designspezifische Eigenschaften des Fensters und leitet
   * die Printstreams um.
   *
   * @param guiSignal CountDownLatch als Methode, um im main-Thread darauf zu warten, dass der
   *                  Outputstream in die GUI umgeleitet wurde.
   */
  private Output(CountDownLatch guiSignal) throws FileNotFoundException {
    super("Ausgabe");

    log.setFocusable(true);
    log.setBackground(Color.darkGray);
    log.setForeground(Color.LIGHT_GRAY);

    log.setEditable(false);

    //Erstellen des Printsreams zum umleiten
    PrintStream printStream = new PrintStream(new CustomOutputStream(log));

    //Streams neu setzen
    System.setOut(printStream);
    System.setErr(printStream);

    add(new JScrollPane(log));

    //Weitere Einstellungen
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(1280, 720);
    setLocationRelativeTo(null);

    guiSignal.countDown();
  }


  /**
   * Erstellt die Graphische Ausgabe der Konsole.
   *
   * @param guiSignal CountDownLatch als Methode, um im main-Thread darauf zu warten, dass der
   *                  Outputstream in die GUI umgeleitet wurde.
   */
  public static void main(CountDownLatch guiSignal) {
    SwingUtilities.invokeLater(() -> {
      try {
        new Output(guiSignal).setVisible(true);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    });
  }
}
