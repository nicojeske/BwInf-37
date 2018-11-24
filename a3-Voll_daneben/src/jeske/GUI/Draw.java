package jeske.GUI;

import jeske.Solver;
import jeske.Util;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Draw extends JPanel {
  private List<Integer> numbers;
  private List<Integer> solution;
  private List<Integer> oldSoulution;
  private List<List<Integer>> areas;

  public Draw(List<Integer> numbers) {
    this.numbers = numbers;
    JFrame frame = new JFrame("Draw");
    frame.setContentPane(this);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 250);
    frame.setVisible(true);
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

    //Draw Areas
    if (areas != null) {
      AtomicInteger i = new AtomicInteger(0);
      areas.forEach(l -> {
        int low = l.get(0);
        int high = l.get(l.size() - 1);
        int xStart = (int) Util.map(low, lowestNum, highestNum, 50, screenWidth - 50);
        int xEnd = (int) Util.map(high, lowestNum, highestNum, 50, screenWidth - 50);

        if (i.get() % 2 == 0)
          g2d.setColor(Color.gray);
        else
          g2d.setColor(Color.pink);
        g2d.fillRect(xStart, 80, xEnd - xStart, 40);
        i.addAndGet(1);
      });
    }

    g2d.setColor(Color.black);


    g2d.drawLine(50, 100, screenWidth - 50, 100);

    //Draw numbers
    AtomicInteger i = new AtomicInteger(1);
    numbers.forEach(number -> {
      int mappedValue = (int) Util.map(number, lowestNum, highestNum, 50, screenWidth - 50);
      FontMetrics fm = g2d.getFontMetrics();
      g2d.drawString(number.toString(), mappedValue - fm.stringWidth(number.toString()) / 2, 70 - fm.getHeight() * i.get());
      i.addAndGet(1);
      g2d.drawLine(mappedValue, 100, mappedValue, 70 - fm.getHeight() * i.get() + 20);
      if (i.get() == 4) {
        i.set(1);
      }
    });

    if (oldSoulution != null) {
      //Draw a solution
      drawSolution(g2d, lowestNum, highestNum, screenWidth, Color.red, oldSoulution, 0);
    }

    if (solution != null) {
      //Draw a solution
      drawSolution(g2d, lowestNum, highestNum, screenWidth, Color.BLUE, solution, 20);
      int gain = (25 * numbers.size()) - Solver.calcExpenses(numbers, solution);
      g2d.drawString("Beste Auswahl: " + solution.toString() + " mit einem Gewinn von " + gain + "€", 50, 200);
    }

  }

  /**
   * Draws a solution
   *
   * @param g2d         Graphic context
   * @param lowestNum   lowest Number from all numbers
   * @param highestNum  highest Number from all numbers
   * @param screenWidth Screen width
   * @param color       Color
   * @param solution    Solution
   * @param yOffset     YOffset
   */
  private void drawSolution(Graphics2D g2d, int lowestNum, int highestNum, int screenWidth, Color color, List<Integer> solution, int yOffset) {
    solution.forEach(sol -> {
      int mappedValue = (int) Util.map(sol, lowestNum, highestNum, 50, screenWidth - 50);
      FontMetrics fm = g2d.getFontMetrics();
      g2d.setColor(color);
      g2d.drawLine(mappedValue, 100, mappedValue, 125 + yOffset);
      g2d.setColor(Color.black);
      g2d.drawString(String.valueOf(sol), mappedValue - fm.stringWidth(String.valueOf(sol)) / 2, 140 + yOffset);
    });
  }

  /**
   * Sets the areas
   * @param list areas
   */
  public void setAreas(List<List<Integer>> list) {
    this.areas = list;
  }

  /**
   * Sets the old solution
   * @param oldSoulution oldSolution
   */
  public void setOldSoulution(List<Integer> oldSoulution) {
    this.oldSoulution = oldSoulution;
  }
}
