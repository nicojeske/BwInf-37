package jeske;

import jeske.GUI.Draw;
import jeske.GUI.Rect;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class EnclosingRectangle2 {

  boolean[][] occupation;
  List<Rect> rectangles;
  //TODO extra data structur
  List<Rect> bestRectangles;
  int bestWidth;
  int bestHeight;
  int bestArea = Integer.MAX_VALUE;
  int rows;
  int cols;
  Draw draw;

  public EnclosingRectangle2(Draw draw) {
    this.draw = draw;
  }

  public static void printArray(boolean[][] occupation) {
    for (boolean[] booleans : occupation) {
      System.out.println(Arrays.toString(booleans).replace("true", "#").replace(" ", "").replace("false", " ").replace(",", ""));
    }
  }

  public static int area(int width, int height) {
    return width * height;
  }

  public static int totalArea(List<Rect> rects) {
    int area = 0;
    for (Rectangle rect : rects) {
      area += (rect.height * rect.width);
    }
    return area;
  }

  public void setConfiguration(List<Rect> rects) {
    rects.forEach(this::place);
    printArray(occupation);
  }

  public void addRectangle(Rect rectangle) {
    int width = rectangle.width;
    int heigth = rectangle.height;

//    for (int row = 0; row < occupation[0].length; row++) {
//      for (int col = 0; col < occupation.length; col++) {
//        boolean occupied = occupation[col][row];
//        //Heuristik?
//        if (!occupied && fits(heigth, width, row, col)) {
//          Rectangle rotatedRect = new Rectangle(rectangle);
//          rotatedRect.setSize(rectangle.height, rectangle.width);
//          place(rotatedRect, row, col);
//          return;
//        } else if(!occupied && fits(width, heigth, row, col)){
//          place(rectangle, row, col);
//          return;
//        }
//      }
//    }

//    place(rectangle);
//    draw.addRectangle(rectangle.getRectangle());
//
//    Rect keck = new Rect(25, 0, 28, 5);
//    place(keck);
//    draw.addRectangle(keck.getRectangle());
//
//    keck = new Rect(13, 10, 30, 15);
//    place(keck);
//    draw.addRectangle(keck.getRectangle());
    // printArray(occupation);

    rectangle = new Rect(0, 0, 4, 2);


    List<CCOA> points = getCCOAs(rectangle);


    List<Point> pointus = new ArrayList<>();
    points.forEach(p -> pointus.add(new Point(p.x, p.y)));
    //draw.setPoints(pointus);
    draw.setCCOA(points);

  }

  public List<CCOA> getCCOAs(Rect rectangle) {
    List<CCOA> points = new ArrayList<>();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        boolean occupied = occupation[row][col];
        if (!occupied) {
          List<CCOA> point = calcNumberOfBorders(row, col, rectangle);
          if (point != null) {
            for (CCOA ccoa : point) {
              points.add(ccoa);
            }
          }
        }
      }
    }
    return points;
  }

  public double calcDegree(CCOA ccoa) {
    double dMin = dMin(ccoa);
//    System.out.print(ccoa + " -> ");
//    System.out.println(dMin);
    return 1 - (dMin / ((ccoa.i.width + ccoa.i.height) / 2.0));
  }

  public double dMin(CCOA ccoa) {
    //System.out.println();

    double min = Double.POSITIVE_INFINITY;
    Rect rect = ccoa.i;
    if (ccoa.rotated)
      rect = rect.getRoatated();

    int zeroer = 0;

    HashMap<Rect, Double> rectangleDoubleHashMap = new HashMap<>();

    double distDown = rows - rect.y2;
    double distUp = rect.y1;
    double distRight = cols - rect.x2;
    double distLeft = rect.x1;


    Set<Rect> adjescentRect = new HashSet<>();


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
    for (Rect rectangle : rectangles) {
      if (rect.equals(rectangle))
        continue;

      rectangleDoubleHashMap.put(rectangle, Double.MAX_VALUE);

      for (int x1 = rect.x1; x1 <= rect.x2; x1++) {
        for (int y1 = rect.y1; y1 <= rect.y2; y1++) {
          for (int x2 = rectangle.x1; x2 <= rectangle.x2; x2++) {
            for (int y2 = rectangle.y1; y2 <= rectangle.y2; y2++) {
              double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
              if (d < rectangleDoubleHashMap.get(rectangle)) {
                if (d == 0.0) {
                  rectangleDoubleHashMap.put(rectangle, 0.0);
                  if (zeroer == 2)
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

    for (Map.Entry<Rect, Double> rectDoubleEntry : rectangleDoubleHashMap.entrySet()) {
      if (!adjescentRect.contains(rectDoubleEntry.getKey())) {
        if (rectDoubleEntry.getValue() < min) {
          min = rectDoubleEntry.getValue();
        }
      }
    }

    return min;
  }

  private void place(Rect rectangle) {
    for (int x = 0; x < rectangle.width; x++) {
      for (int y = 0; y < rectangle.height; y++) {
        occupation[rectangle.y1 + y][rectangle.x1 + x] = true;
      }
    }

    this.rectangles.add(rectangle);
    draw.addRectangle(rectangle.getRectangle());

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private List<Point> checkCFOP(int row, int col, Rect rectangle) {


    return null;
  }

  public List<CCOA> calcNumberOfBorders(int row, int col, Rect rectangle) {
    int borders = 0;
    boolean north = false, west = false, east = false, south = false;

    //Upper border
    if (row == 0 || occupation[row - 1][col]) {
      borders += 1;
      north = true;
    }

    //left border
    if (col == 0 || occupation[row][col - 1]) {
      borders += 1;
      west = true;
    }

    //Lower border
    if (row == rows - 1 || occupation[row + 1][col]) {
      borders += 1;
      south = true;
    }

    //Right border
    if (col == cols - 1 || occupation[row][col + 1]) {
      borders += 1;
      east = true;
    }

    if (borders == 1) {
      if (north || south) {
        if (checkForCorner(row, col, Direction.EAST)) {
          borders += 1;
          east = true;
        }
        if (checkForCorner(row, col, Direction.WEST)) {
          borders += 1;
          west = true;
        }
      } else if (east || west) {
        if (checkForCorner(row, col, Direction.NORTH)) {
          borders += 1;
          north = true;
        }
        if (checkForCorner(row, col, Direction.SOUTH)) {
          borders += 1;
          south = true;
        }
      }
    }

    List<CCOA> res = new ArrayList<>();

    /*if(borders > 1){
      int pointCol = col;
      int pointRow = row;

      if(north && east) {
        pointCol++;
      }
      if(south&&east){
        pointCol++;
        pointRow++;
      }
      if(south&&west) {
        pointRow++;
      }


      System.out.println(new Point(pointCol, pointRow));
      res.add(new CCOA(pointCol, pointRow, rectangle, ))
    }*/

    if (borders > 1) {

      int pointCol = col;
      int pointRow = row;

      if (north && east) {
        pointCol++;
      }
      if (south && east) {
        pointCol++;
        pointRow++;
      }
      if (south && west) {
        pointRow++;
      }


      if (north && east) {
        pointCol -= rectangle.width;
        //System.out.print("NE -> ");
      }
      if (south && east) {
        pointCol -= rectangle.width;
        pointRow -= rectangle.height;
        // System.out.print("SE -> ");
      }
      if (south && west) {
        pointRow -= rectangle.height;
        // System.out.print("SW -> ");
      }


      // System.out.print(new Point(col, row ));

      if (fits(new Point(pointCol, pointRow), rectangle)) {
        //System.out.println(" -> " + new Point(pointCol, pointRow));
        res.add(new CCOA(pointCol, pointRow, rectangle, false));
      } //else
      //System.out.println();

      pointCol = col;
      pointRow = row;

      if (north && east) {
        pointCol++;
      }
      if (south && east) {
        pointCol++;
        pointRow++;
      }
      if (south && west) {
        pointRow++;
      }


      Rect rotadet = rectangle.getRoatated();


      if (north && east) {
        // System.out.print("NE -> ");
        pointCol -= rotadet.width;
      }
      if (south && east) {
        //System.out.print("SE -> ");
        pointCol -= rotadet.width;
        pointRow -= rotadet.height;
      }
      if (south && west) {
        // System.out.print("SW -> ");
        pointRow -= rotadet.height;
      }


      //System.out.print(new Point(col, row ));

      if (fits(new Point(pointCol, pointRow), rotadet)) {
        res.add(new CCOA(pointCol, pointRow, rectangle, true));
        //System.out.println("[ROTATED] -> " + new Point(pointCol, pointRow));
      } //else
      //System.out.println();

      return res;
    }


    return null;
  }

  private boolean fits(Point point, Rect rectangle) {
    for (int row = 0; row < rectangle.height; row++) {
      for (int col = 0; col < rectangle.width; col++) {
        try {
          // if(point.x == 11 && point.y == 11)
          //System.out.println();
          if (occupation[row + point.y][col + point.x])
            return false;
        } catch (Exception e) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean checkForCorner(int row, int col, Direction direction) {
    try {
      switch (direction) {
        case WEST:
          for (int rowI = 0; rowI < rows; rowI++) {
            if (occupation[rowI][col - 1] && !occupation[rowI][col]) {
              return true;
            }
          }
          break;
        case EAST:
          for (int rowI = 0; rowI < rows; rowI++) {
            if (occupation[rowI][col + 1] && !occupation[rowI][col]) {
              return true;
            }
          }
          break;
        case NORTH:
          for (int colI = 0; colI < cols; colI++) {
            if (occupation[row - 1][colI] && !occupation[row][colI]) {
              return true;
            }
          }
          break;
        case SOUTH:
          for (int colI = 0; colI < cols; colI++) {
            if (occupation[row + 1][colI] && !occupation[row][colI]) {
              return true;
            }
          }
          break;
      }
    } catch (Exception e) {
      return false;
    }

    return false;
  }

  public void initialize(int capacityHeight, int capacityWidth) {
    occupation = new boolean[capacityHeight][capacityWidth];
    rows = capacityHeight;
    cols = capacityWidth;
    rectangles = new ArrayList<Rect>();
    draw.reset(capacityHeight, capacityWidth);
  }

  public Solution solve(List<Rect> rects, int height, int width) {
    rectangles = new ArrayList<Rect>();
    int widestRectangle = rects.stream()
            .max(Comparator.comparingInt(Rect::getWidth))
            .get()
            .width;

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
          alg(rects);
          this.shrink();
          width = occupation[0].length;
          control(rects, rectangles, height, width);

          return new Solution(bestRectangles, height, width);
        }
      }
    }


    alg(rects);
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
    //printArray(occupation);
  }

  private boolean[][] resize(boolean[][] matrix, int w, int h) {
    boolean[][] temp = new boolean[h][w];
    h = Math.min(h, matrix.length);
    w = Math.min(w, matrix[0].length);
    for (int i = 0; i < h; i++)
      System.arraycopy(matrix[i], 0, temp[i], 0, w);
    return temp;
  }

  private void alg(List<Rect> rects) {
    List<Rect> rectCopy = new ArrayList<>(rects);

    List<CCOA> allCCOAs = new ArrayList<>();
    for (Rect rect : rectCopy) {
      List<CCOA> ccoas = getCCOAs(rect);
      allCCOAs.addAll(ccoas);
    }

    while (!allCCOAs.isEmpty()) {

      HashMap<CCOA, Double> degreeMap = new HashMap<>();
      for (CCOA ccoa : allCCOAs) {
        degreeMap.put(ccoa, this.calcDegree(ccoa));
      }

      Map.Entry heighest = degreeMap.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).get();
      List<Map.Entry<CCOA, Double>> bestCCOAS = degreeMap.entrySet().stream().filter(ccoaDoubleEntry -> ccoaDoubleEntry.getValue() == heighest.getValue()).collect(Collectors.toList());
      bestCCOAS.sort(Comparator.comparingInt(value -> value.getKey().x + value.getKey().y));
      CCOA bestCCOA = bestCCOAS.get(0).getKey();

      if (bestCCOA.rotated)
        this.place(bestCCOA.i.getRoatated());
      else
        this.place(bestCCOA.i);

      rectCopy.removeIf(rect -> rect.sameSize(bestCCOA.i));


      //allCCOAs.removeIf(ccoa -> ccoa.i.sameSize(bestCCOA.i));

      allCCOAs = new ArrayList<>();
      for (Rect rect : rectCopy) {
        List<CCOA> ccoas = getCCOAs(rect);
        allCCOAs.addAll(ccoas);
      }
    }
  }

  private boolean control(List<Rect> rects, List<Rect> rectangles, int width, int height) {
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
