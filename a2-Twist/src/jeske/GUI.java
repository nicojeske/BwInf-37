package jeske;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

/**
 * GUI
 */
public class GUI {
  private JButton twistButton;
  private JPanel mainPanel;
  private JTextArea textAreaToTwist;
  private JTextArea textAreaToDecode;
  private JButton openFileTwistButton;
  private JButton openFileUntwistButton;
  private JButton enttwistenButton;
  private JTextArea textAreaResult;


  /**
   * Creates the GUI
   */
  private GUI() {
    initialize();
  }

  /**
   * Initializes the Twister instance and the action listeners.
   */
  private void initialize() {
    URL woerterliste = this.getClass().getResource("/beispieldaten/woerterliste.txt");
    File dictionaryFile = new File(String.valueOf(woerterliste));

    //If the system cannot find the dictionary file, the user must specify it himself.
    if(!dictionaryFile.exists()) {
      File userFile = Util.getUserFile("Wörterbuch", "txt");
      //No choice -> show error
      if(userFile == null) {
        JOptionPane.showMessageDialog(null,
                "Fehler bei der Dateiauswahl", "Fehler", JOptionPane.ERROR_MESSAGE);
      } else {
        dictionaryFile = userFile;
      }
    }

    //Try's to create a Twister. If there is a problem with the dictionary file, ask the user
    //if he wants to try again. If yes call initialize again. If no -> exit,
    Twister twister = null;
    try {
      twister = new Twister(dictionaryFile);
    } catch (IOException e) {
      int userChoice = JOptionPane.showConfirmDialog(null, "Ungültiges Wörterbuch. Erneut versuchen?",
              "Fehler", JOptionPane.YES_NO_OPTION);
      if (userChoice == JOptionPane.YES_OPTION) {
        initialize();
        return;
      } else {
        System.exit(1);
      }
    }

    //Twist given text, when twistButton is pressed.
    Twister finalTwister = twister;
    twistButton.addActionListener(e -> {
      String text = textAreaToTwist.getText();
      String twistedText = finalTwister.twist(text);
      textAreaResult.setText(twistedText);
    });

    //Untwist given text, when untwistButton is pressed.
    enttwistenButton.addActionListener(e -> {
      String text = textAreaToDecode.getText();
      String decodedText = finalTwister.untwist(text);
      textAreaResult.setText(decodedText);
    });


    //Enables the user, to load a text file to twist it.
    openFileTwistButton.addActionListener(e -> {
      try {
        File file = Util.getUserFile("Text zum twisten (.txt)", "txt");
        assert file != null;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        textAreaToTwist.setText("");
        reader.lines().forEach(textAreaToTwist::append);
      } catch (FileNotFoundException e1) {
        JOptionPane.showMessageDialog(null, "Fehler beim einlesen der Datei..");
      }
    });

    //Enables the user, to load a text file to untwist it.
    openFileUntwistButton.addActionListener(e -> {
      try {
        File file = Util.getUserFile("Text zum enttwisten (.txt)", "txt");
        Objects.requireNonNull(file);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        textAreaToDecode.setText("");
        reader.lines().forEach(textAreaToDecode::append);
      } catch (FileNotFoundException | NullPointerException el) {
        JOptionPane.showMessageDialog(null, "Fehler beim einlesen der Datei..");
      }
    });
  }

  /**
   * Starts the programm, creates the GUI
   * @param args null
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignored){

    }
    JFrame frame = new JFrame("GUI");
    frame.setContentPane(new GUI().mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
