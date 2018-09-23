package jeske.GUI;

import jeske.CCOA;
import jeske.EnclosingRectangle2;

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
  private List<Point> points = new ArrayList<>();
  private List<jeske.CCOA> CCOA;

  public Draw() {
    SwingUtilities.invokeLater(this::createAndShowGUI);
  }

  private void createAndShowGUI() {
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


    if (rectangleList != null) {
      g2d.drawString("" + boundingWidth, 48 + boundingWidth * factor / 2, 80 - 3);
      g2d.drawString("" + boundingHeight, 0, 82 + boundingHeight * factor / 2);

      g2d.drawString("Total area           : " + EnclosingRectangle2.area(boundingWidth, boundingHeight) + "m^2", 10, 10);
//      g2d.drawString("Rectangle area  : " + EnclosingRectangle2.totalArea(rectangleList) + "m^2", 10, 25);
//      g2d.drawString("Free area            : " + (EnclosingRectangle2.area(boundingWidth, boundingHeight) - EnclosingRectangle.totalArea(rectangleList)) + "m^2", 10, 40);
      //System.out.println(EnclosingRectangle.area(boundingWidth, boundingHeight) - EnclosingRectangle.totalArea(rectangleList));


      g2d.setStroke(new BasicStroke(3));
      //g2d.drawRect(20, 90, boundingWidth * factor + 2, boundingHeight * factor + 2);
      g2d.setStroke(new BasicStroke(1));
      for (Rectangle rect : rectangleList) {
        g2d.setColor(rectangleColorMap.get(rect));
        g2d.fillRect((rect.x) * factor, (rect.y) * factor, rect.width * factor, rect.height * factor);
      }
    }

    g2d.setColor(Color.gray);

    for (int y = 0; y < boundingHeight; y++) {
      g2d.drawLine(0, y * factor, boundingWidth * factor, y * factor);
    }

    for (int x = 0; x < boundingWidth; x++) {
      g2d.drawLine(x * factor, 0, x * factor, boundingHeight * factor);
    }

    if (CCOA != null) {
      int i = 0;
      for (jeske.CCOA ccoa : CCOA) {
        Rect rect = ccoa.i;
        if (ccoa.rotated) {
          rect = rect.getRoatated();
        }
        g2d.fillOval((ccoa.x) * factor - 5, (ccoa.y) * factor - 5, 10, 10);

        g2d.setColor(new Color(i));
        g2d.setStroke(new BasicStroke(3));
        //g2d.drawRect(20, 90, boundingWidth * factor + 2, boundingHeight * factor + 2);
        g2d.drawRect(ccoa.x * factor, ccoa.y * factor, rect.width * factor, rect.height * factor);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.black);
        i += 100000;
      }
    }


    g2d.setColor(Color.red);
    if (!points.isEmpty()) {
      for (Point point : points) {
        //g2d.fillOval((point.x+1) * factor, (point.y+1) * factor + 70, 10, 10);
        g2d.fillOval((point.x) * factor - 10, (point.y) * factor - 10, 20, 20);
//        System.out.println(point);
//        System.out.println();
//        System.out.println(new Point(point.x * factor + 43, point.y * factor +75));
      }
    }
    g2d.setColor(Color.black);


  }

  public void setPoints(List<Point> points) {
    this.points = points;
    repaint();
  }

  public List<CCOA> getCCOA() {
    return CCOA;
  }

  public void setCCOA(List<CCOA> ccoa) {
    this.CCOA = ccoa;
  }
}
