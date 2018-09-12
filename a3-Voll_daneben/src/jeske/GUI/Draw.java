package jeske.GUI;

import jeske.Point;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Draw extends JPanel {
  final static double EPSILON = 1e-12;
  private List<Integer> numbers;
  private List<Integer> solution;
  private List<Integer> oldSoulution;
  private List<List<Integer>> list;

  public Draw(List<Integer> numbers) {
    this.numbers = numbers;
    JFrame frame = new JFrame("Draw");
    frame.setContentPane(this);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 200);
    frame.setVisible(true);
  }

  public static double map(double valueCoord1,
                           double startCoord1, double endCoord1,
                           double startCoord2, double endCoord2) {

    if (Math.abs(endCoord1 - startCoord1) < EPSILON) {
      throw new ArithmeticException("/ 0");
    }

    double offset = startCoord2;
    double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
    return ratio * (valueCoord1 - startCoord1) + offset;
  }

  public void setSolution(List<Integer> solution) {
    this.solution = solution;
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    int lowestNum = numbers.get(0);
    int highestNum = numbers.get(numbers.size() - 1);
    int screenWidth = this.getWidth();

    if (list != null) {
      AtomicInteger i2 = new AtomicInteger(0);
      list.forEach(l -> {
        int low = l.get(0);
        int high = l.get(l.size() - 1);
        int xStart = (int) map(low, lowestNum, highestNum, 50, screenWidth - 50);
        int xEnd = (int) map(high, lowestNum, highestNum, 50, screenWidth - 50);

        if (i2.get() % 2 == 0)
          g2d.setColor(Color.gray);
        else
          g2d.setColor(Color.pink);
        g2d.fillRect(xStart, 80, xEnd - xStart, 40);
        i2.addAndGet(1);

      });
    }

    g2d.setColor(Color.black);


    g2d.drawLine(50, 100, screenWidth - 50, 100);
    AtomicInteger i = new AtomicInteger(1);
    numbers.forEach(number -> {
      int mappedValue = (int) map(number, lowestNum, highestNum, 50, screenWidth - 50);
      //System.out.println(mappedValue);
      FontMetrics fm = g2d.getFontMetrics();
      int x = (getWidth() - fm.stringWidth(number.toString())) / 2;
      g2d.drawString(number.toString(), mappedValue - fm.stringWidth(number.toString()) / 2, 70 - fm.getHeight() * i.get());
      i.addAndGet(1);
      g2d.drawLine(mappedValue, 100, mappedValue, 70 - fm.getHeight() * i.get() + 20);
      if (i.get() == 4) {
        i.set(1);
      }
    });


    if (oldSoulution != null) {
      drawSolution(g2d, lowestNum, highestNum, screenWidth, Color.red, oldSoulution);
    }

    if (solution != null) {
      drawSolution(g2d, lowestNum, highestNum, screenWidth, Color.BLUE, solution);
    }

  }

  private void drawSolution(Graphics2D g2d, int lowestNum, int highestNum, int screenWidth, Color color, List<Integer> solution) {
    List<Point> points = new ArrayList<>();
    solution.forEach(sol -> {
      points.add(new Point(sol));
      //Main.initializePoints(points, numbers);
    });
    // Main.initializePoints2(points, numbers);
    points.forEach(sol -> {
      int mappedValue = (int) map(sol.getElement(), lowestNum, highestNum, 50, screenWidth - 50);
      FontMetrics fm = g2d.getFontMetrics();
      g2d.setColor(color);
      g2d.drawLine(mappedValue, 100, mappedValue, 125);
      g2d.setColor(Color.black);
      g2d.drawString(String.valueOf(sol.getElement()), mappedValue - fm.stringWidth(String.valueOf(sol.getElement())) / 2, 140);
      if (sol.getRightDiff() != 0) {
        g2d.drawLine(mappedValue + sol.getRightDiff(), 75, mappedValue + sol.getRightDiff(), 125);
      }
      g2d.setColor(Color.BLACK);
    });
  }

  /**
   * Draw a String centered in the middle of a Rectangle.
   *
   * @param g    The Graphics instance.
   * @param text The String to draw.
   * @param rect The Rectangle to center the text in.
   */
  public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
    // Get the FontMetrics
    FontMetrics metrics = g.getFontMetrics(font);
    // Determine the X coordinate for the text
    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
    // Set the font
    g.setFont(font);
    // Draw the String
    g.drawString(text, x, y);
  }

  public void setShit(List<List<Integer>> list) {
    this.list = list;
  }

  public List<Integer> getOldSoulution() {
    return oldSoulution;
  }

  public void setOldSoulution(List<Integer> oldSoulution) {
    this.oldSoulution = oldSoulution;
  }
}
