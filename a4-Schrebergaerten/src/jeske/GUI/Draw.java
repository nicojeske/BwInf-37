package jeske.GUI;

import jeske.EnclosingRectangle;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Draw extends JPanel {
  private List<Rectangle> rectangleList;
  private Map<Rectangle, Color> rectangleColorMap = new HashMap<>();
  private int boundingWidth;
  private int boundingHeight;
  private Random rng = ThreadLocalRandom.current();
  private int factor = 10;

  public Draw() {
    JFrame frame = new JFrame("Draw");
    frame.setContentPane(this);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 500);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public void reset(int height, int width) {
    rectangleList = new ArrayList<>();
    rectangleColorMap = new HashMap<>();
    boundingHeight = height;
    boundingWidth = width;
  }

  public void addRectangle(Rectangle rectangle) {
    rectangleList.add(rectangle);
    rectangleColorMap.put(rectangle,
            new Color(
                    rng.nextInt(255),
                    rng.nextInt(255),
                    rng.nextInt(255)));
    repaint();


  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int factorHeight = 1;
    int factorWidth = 1;
    if (boundingHeight != 0 && boundingWidth != 0) {
      factorHeight = (this.getHeight() - 100) / boundingHeight;
      factorWidth = (this.getWidth() - 100) / boundingWidth;
    }
    factor = Math.min(factorHeight, factorWidth);


    //TODO factor dynamically

    if (rectangleList != null) {
      g2d.drawString("" + boundingWidth, 48 + boundingWidth * factor / 2, 80 - 3);
      g2d.drawString("" + boundingHeight, 30, 82 + boundingHeight * factor / 2);

      g2d.drawString("Total area           : " + EnclosingRectangle.area(boundingWidth, boundingHeight) + "m^2", 10, 10);
      g2d.drawString("Rectangle area  : " + EnclosingRectangle.totalArea(rectangleList) + "m^2", 10, 25);
      g2d.drawString("Free area            : " + (EnclosingRectangle.area(boundingWidth, boundingHeight) - EnclosingRectangle.totalArea(rectangleList)) + "m^2", 10, 40);

      g2d.setStroke(new BasicStroke(3));
      g2d.drawRect(48, 80, boundingWidth * factor + 2, boundingHeight * factor + 2);
      g2d.setStroke(new BasicStroke(1));
      for (Rectangle rect : rectangleList) {
        g2d.setColor(rectangleColorMap.get(rect));
        g2d.fillRect(rect.x * factor + 50, rect.y * factor + 82, rect.width * factor, rect.height * factor);
      }
    }
  }
}
