package jeske;


import jeske.GUI.Draw;
import jeske.GUI.Rect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

  public static void main(String[] args) {
    BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                    Main.class.getResourceAsStream("/beispieldaten/beispiel1.txt")
            )
    );

    List<Rect> rects = new ArrayList<>();

    reader.lines().forEach(line -> {
      StringTokenizer st = new StringTokenizer(line);
      if (st.countTokens() == 2) {
        int height = Integer.parseInt(st.nextToken());
        int width = Integer.parseInt(st.nextToken());
        rects.add(new Rect(0, 0, width, height));
      } else {
        throw new RuntimeException("Input File must be formatted like \nx y");
      }
    });

    sol(rects);
  }

  private static void sol(List<Rect> rects) {
    rects.sort((o1, o2) -> Integer.compare(o2.getHeight(), o1.getHeight()));
    int height = (int) rects.get(0).getHeight();

    Draw draw = new Draw();
    EnclosingRectangle2 enclosingRectangle = new EnclosingRectangle2(draw);
    rects.sort((o1, o2) -> Integer.compare(o2.getHeight(), o1.getHeight()));
    int maxHeight = (int) rects.get(0).getHeight();

    enclosingRectangle.solve(rects, maxHeight, 100);

    //Solution bestSol = enclosingRectangle.solve(rects, maxHeight, 300);
    /*
    List<List<Rectangle>> allPos = Lists.cartesianProduct(poss);

    Solution bestSol = null;
    int bestArea = Integer.MAX_VALUE;
    for (List<Rectangle> rectChoice : allPos) {
      List<Rectangle> rectus = new ArrayList<>(rectChoice);
      rectus.sort((o1, o2) -> Integer.compare(o2.height, o1.height));
      int maxHeight2 = (int) rectus.get(0).getHeight();
      Solution sol = enclosingRectangle.solve(rectus, maxHeight2, 300);

      int solArea = EnclosingRectangle.area(sol.width, sol.height);
      if(solArea < bestArea) {
        bestArea = solArea;
        bestSol = sol;
      }
    }

*/
   /* int solHeight = bestSol.height;
    int solWidth  = bestSol.width;
    List<Rectangle> solRectangles = bestSol.solutionRectangles;

    draw.reset(solHeight, solWidth);
    for (Rectangle bestRectangle : solRectangles) {
      draw.addRectangle(bestRectangle);
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    */


//    List<Rectangle> solutionRectangles = (List<Rectangle>) solution[0];


    System.out.println();
  }
}
