package jeske;

import jeske.GUI.Draw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnclosingRectangle {
  boolean[][] occupation;
  List<Rectangle> rectangles;
  //TODO extra data structur
  List<Rectangle> bestRectangles;
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

    for (int col = 0; col < occupation.length; col++) {
      boolean[] rows = occupation[col];
      for (int row = 0; row < rows.length; row++) {

      }
    }

    for (int row = 0; row < occupation[0].length; row++) {
      for (int col = 0; col < occupation.length; col++) {
        boolean occupied = occupation[col][row];
        if (col == 15 && row == 25)
          System.out.println(col + " " + row);
        if (!occupied && fits(width, heigth, row, col)) {
          place(rectangle, row, col);
          return;
        }
      }
    }
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
    printArray(occupation);

    draw.addRectangle(rect);
    try {
      Thread.sleep(20);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println();

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
    printArray(occupation);
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
