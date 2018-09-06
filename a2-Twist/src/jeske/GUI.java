package jeske;

import javax.swing.*;

public class GUI {
  private JButton twistenButton;
  private JPanel mainPanel;
  private JTextArea textAreaToTwist;
  private JTextArea textAreaToDecode;
  private JButton openFileButton;
  private JButton openFileButton2;
  private JButton enttwistenButton;
  private JTextArea textAreaResult;


  public GUI() {

    Main main = new Main();

    /*
    textAreaToTwist.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        textUpdated();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        textUpdated();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        textUpdated();
      }

      private void textUpdated(){
        if(textAreaToTwist.getText().isEmpty()){
          enttwistenButton.setEnabled(false);
          twistenButton.setEnabled(false);
        } else{
          enttwistenButton.setEnabled(false);
          twistenButton.setEnabled(true);
        }
      }
    });
    textAreaToDecode.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        textUpdated();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        textUpdated();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        textUpdated();
      }

      private void textUpdated(){
        if(textAreaToDecode.getText().isEmpty()){
          enttwistenButton.setEnabled(false);
          twistenButton.setEnabled(false);
        } else{
          enttwistenButton.setEnabled(true);
          twistenButton.setEnabled(false);
        }
      }
    });
    */

    twistenButton.addActionListener(e -> {
      String text = textAreaToTwist.getText();
      String twistedText = main.twist(text);
      textAreaResult.setText(twistedText);
    });

    enttwistenButton.addActionListener(e -> {
      String text = textAreaToDecode.getText();
      String decodedText = main.decode(text);
      textAreaResult.setText(decodedText);
    });


  }

  public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    JFrame frame = new JFrame("GUI");
    frame.setContentPane(new GUI().mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
}
