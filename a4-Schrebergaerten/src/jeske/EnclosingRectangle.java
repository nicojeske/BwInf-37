package jeske;

import jeske.GUI.Draw;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EnclosingRectangle {
  boolean[][] occupation;
  List<Rectangle> rectangles;
  //TODO extra data structur
  List<Rectangle> bestRectangles;
  int rows;
  int cols;
  int bestWidth;
  int bestHeight;
  int bestArea = Integer.MAX_VALUE;
  Draw draw;

  public EnclosingRectangle(Draw draw) {
    this.draw = draw;
  }

  public static void printArray(boolean[][] occupation) {
    for (boolean[] booleans : occupation) {
      System.out.println(Arrays.toString(booleans).replace("true", "#").replace(" ", "").replace("false", " ").replace(",", ""));
    }
  }

  public static int totalArea(List<Rectangle> rects) {
    int area = 0;
    for (Rectangle rect : rects) {
      area += (rect.height * rect.width);
    }
    return area;
  }

  public static int area(int width, int height) {
    return width * height;
  }

  public void addRectangle(Rectangle rectangle) {
    int width = rectangle.width;
    int heigth = rectangle.height;

//    for (int col = 0; col < occupation.length; col++) {
//      boolean[] rows = occupation[col];
//      for (int row = 0; row < rows.length; row++) {
//
//      }
//    }

    for (int row = 0; row < occupation[0].length; row++) {
      for (int col = 0; col < occupation.length; col++) {
        boolean occupied = occupation[col][row];
        if (col == 15 && row == 25)
          System.out.println(col + " " + row);
        if (!occupied && fits(width, heigth, row, col)) {
          place(rectangle, row, col);
          return;
        }
//        if(!occupied && (fits(width, heigth, row, col) || fits(heigth, width, row, col))){
//          Rectangle rotatedRectangle = new Rectangle(rectangle.x, rectangle.y, rectangle.height, rectangle.width);
//          if(fits(width, heigth, row, col) && fits(heigth, width, row, col)){
//            double valRect = fitness(rectangle);
//            double valRotated = fitness(rotatedRectangle);
//            System.out.println("Rect -> " + valRect);
//            System.out.println("Rota -> " + valRotated);
//
//            if(Math.max(valRect, valRotated) == valRect) {
//              place(rectangle, row, col);
//            } else {
//              place(rotatedRectangle, row, col);
//            }
//          } else if(fits(width, heigth, row, col)){
//            place(rectangle, row, col);
//          } else {
//            place(rotatedRectangle, row, col);
//          }
//          return;
//        }
      }
    }
  }

  public double fitness(Rectangle rect) {
    double dMin = dMin(rect);
//    System.out.print(rect + " -> ");
//    System.out.println(dMin);
    return 1 - (dMin / ((rect.width + rect.height) / 2.0));
  }

  public double dMin(Rectangle rect) {
    //System.out.println();

    double min = Double.POSITIVE_INFINITY;

    int zeroer = 0;

    HashMap<Rectangle, Double> rectangleDoubleHashMap = new HashMap<>();

    double distDown = rows - rect.y + rect.height;
    double distUp = rect.y;
    double distRight = cols - rect.x + rect.width;
    double distLeft = rect.x;


    Set<Rectangle> adjescentRect = new HashSet<>();


    if (distDown > 0)
      min = Math.min(distDown, min);
    else
      zeroer++;

    if (distUp > 0)
      min = Math.min(distUp, min);
    else
      zeroer++;

    if (distRight > 0)
      min = Math.min(distRight, min);
    else
      zeroer++;

    if (distLeft > 0)
      min = Math.min(distLeft, min);
    else
      zeroer++;

    if (zeroer > 2)
      return 0.0;

    rectloop:
    for (Rectangle rectangle : rectangles) {

      rectangleDoubleHashMap.put(rectangle, Double.MAX_VALUE);

      for (int x1 = rect.x; x1 <= rect.x + rect.width; x1++) {
        for (int y1 = rect.y; y1 <= rect.y + rect.height; y1++) {
          for (int x2 = rectangle.x; x2 <= rectangle.x + rectangle.width; x2++) {
            for (int y2 = rectangle.y; y2 <= rectangle.y + rectangle.height; y2++) {
              double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
              if (d < rectangleDoubleHashMap.get(rectangle)) {
                if (d == 0.0) {
                  rectangleDoubleHashMap.put(rectangle, 0.0);
                  if (zeroer == 3)
                    return 0.0;
                  adjescentRect.add(rectangle);
                  zeroer++;
                  continue rectloop;
                }
                rectangleDoubleHashMap.put(rectangle, d);
                // min = d;
              }
            }
          }
        }
      }
    }

    for (Map.Entry<Rectangle, Double> rectDoubleEntry : rectangleDoubleHashMap.entrySet()) {
      if (!adjescentRect.contains(rectDoubleEntry.getKey())) {
        if (rectDoubleEntry.getValue() < min) {
          min = rectDoubleEntry.getValue();
        }
      }
    }

    return min;
  }

  private void place(Rectangle rectangle, int row, int col) {
    int width = rectangle.width;
    int heigth = rectangle.height;

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < heigth; y++) {
        occupation[col + y][row + x] = true;
      }
    }

    Rectangle rect = new Rectangle(row, col, width, heigth);
    rectangles.add(rect);
    // printArray(occupation);

//    draw.addRectangle(rect);
//    try {
//      Thread.sleep(5);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }

    // System.out.println();

  }

  private boolean fits(int width, int heigth, int row, int col) {


    for (int x = 0; x < width; x++) {
      for (int y = 0; y < heigth; y++) {
        try {
          if (occupation[col + y][row + x])
            return false;
        } catch (ArrayIndexOutOfBoundsException ex) {
          return false;
        }
      }
    }
    return true;
  }

  public void shrink() {
    int heighestWidth = 0;
    for (int col = 0; col < occupation.length; col++) {
      for (int row = 0; row < occupation[0].length; row++) {
        if (occupation[col][row] && row > heighestWidth) {
          heighestWidth = row;
        }
      }
    }
    occupation = resize(occupation, heighestWidth, occupation.length);
    // printArray(occupation);
  }

  private boolean[][] resize(boolean[][] matrix, int w, int h) {
    boolean[][] temp = new boolean[h][w];
    h = Math.min(h, matrix.length);
    w = Math.min(w, matrix[0].length);
    for (int i = 0; i < h; i++)
      System.arraycopy(matrix[i], 0, temp[i], 0, w);
    return temp;
  }

  private void initialize(int capacityHeight, int capacityWidth) {
    occupation = new boolean[capacityHeight][capacityWidth];
    rows = capacityHeight;
    cols = capacityWidth;
    rectangles = new ArrayList<>();
  }

  public Solution solve(List<Rectangle> rects, int height, int width) {
    rectangles = new ArrayList<>();
    int widestRectangle = 0;
    for (Rectangle rect : rects) {
      if (rect.width > widestRectangle)
        widestRectangle = rect.width;
    }

    draw.reset(height, width);
    System.out.println("--------------------------------------------------------------------");
    System.out.println(width + " " + height);

    this.initialize(height, width);

    if (totalArea(rects) > area(width, height)) {
      while (totalArea(rects) < area(width, height)) {
        height++;
      }

      if (area(width, height) > bestArea) {
        width--;
      }

      if (totalArea(rects) >= area(width, height)) {
        if (width < widestRectangle) {
          for (Rectangle rect : rects) {
            this.addRectangle(rect);
          }
          this.shrink();
          width = occupation[0].length;
          control(rects, rectangles, width, height);

          return new Solution(bestRectangles, height, width);
        }
      }
    }


    for (Rectangle rect : rects) {
      this.addRectangle(rect);
    }
    this.shrink();
    width = occupation[0].length + 1;

    //All rectangles placed
    if (control(rects, rectangles, width, height)) {
      solve(rects, occupation.length, occupation[0].length - 1);
    } else {
      solve(rects, occupation.length + 1, occupation[0].length);
    }


    //TODO Replace with Solution
    return new Solution(bestRectangles, bestHeight, bestWidth);
  }

  private boolean control(List<Rectangle> rects, List<Rectangle> rectangles, int width, int height) {
    if (rects.size() == rectangles.size()) {
      System.out.println("All Rectangles placed");
      int area = area(occupation.length, occupation[0].length);
      if (area < bestArea) {
        bestArea = area;
        bestRectangles = rectangles;
        bestHeight = height;
        bestWidth = width;
      }
      return true;
    }
    return false;
  }
}
