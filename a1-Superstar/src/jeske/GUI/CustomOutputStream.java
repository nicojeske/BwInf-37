package jeske.GUI;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Custom Outputstream, der die Konsolenausgaben in ein GUI und eine Log-Datei weiterleitet.
 */
class CustomOutputStream extends OutputStream {
  private final JTextArea destination;
  private final PrintStream log = new PrintStream("log.txt");

  CustomOutputStream(JTextArea destination) throws FileNotFoundException {
    if (destination == null)
      throw new IllegalArgumentException("Destination is null");

    this.destination = destination;
  }

  @Override
  public void write(byte[] buffer, int offset, int length) {
    final String text = new String(buffer, offset, length);
    SwingUtilities.invokeLater(() -> destination.append(text));
    SwingUtilities.invokeLater(() -> log.append(text));
  }

  @Override
  public void write(int b) throws IOException {
    write(new byte[]{(byte) b}, 0, 1);
  }
}
