package jeske;

import jeske.GUI.Draw;
import jeske.GUI.Rect;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnclosingRectangle2Test {

  @org.junit.jupiter.api.Test
  void TestPlace() {
    Rect[] rectsArray = new Rect[]{
            new Rect(0, 0, 5, 10)
    };

    EnclosingRectangle2 enclosingRectangle2 = new EnclosingRectangle2(new Draw());
    enclosingRectangle2.initialize(15, 20);
    enclosingRectangle2.setConfiguration(Arrays.asList(rectsArray));
  }

  @Test
  void getCCOAs() {
    Draw draw = new Draw();

    EnclosingRectangle2 enclosingRectangle2 = new EnclosingRectangle2(draw);
    enclosingRectangle2.initialize(15, 20);
    Rect rect = new Rect(0, 0, 3, 1);
    List<CCOA> ccoaList = enclosingRectangle2.getCCOAs(rect);
    assertEquals(ccoaList.size(), 8);

    enclosingRectangle2.initialize(7, 10);
    Rect[] rectsArray = new Rect[]{
            new Rect(0, 0, 3, 5),
            new Rect(9, 0, 10, 7)
    };
    enclosingRectangle2.setConfiguration(Arrays.asList(rectsArray));
    ccoaList = enclosingRectangle2.getCCOAs(rect);
    draw.setCCOA(ccoaList);

    assertEquals(11, ccoaList.size());

  }


  @Test
  void dMin() {
    Draw draw = new Draw();
    EnclosingRectangle2 enclosingRectangle2 = new EnclosingRectangle2(draw);
    Rect rect = new Rect(0, 6, 3, 7);


    enclosingRectangle2.initialize(7, 10);
    Rect[] rectsArray = new Rect[]{
            new Rect(0, 0, 3, 5),
            new Rect(7, 4, 10, 6)
    };
    enclosingRectangle2.setConfiguration(Arrays.asList(rectsArray));

    CCOA ccoa;

    rect.updatePosition(7, 6);
    ccoa = new CCOA(7, 6, rect, false);
    draw.setCCOA(Arrays.asList(ccoa));
    assertEquals(0, enclosingRectangle2.dMin(ccoa));

    rect.updatePosition(7, 0);
    ccoa = new CCOA(7, 0, rect, false);
    draw.setCCOA(Arrays.asList(ccoa));
    assertEquals(3, enclosingRectangle2.dMin(ccoa));

    rect.updatePosition(0, 6);
    ccoa = new CCOA(0, 6, rect, false);
    draw.setCCOA(Arrays.asList(ccoa));
    assertEquals(1, enclosingRectangle2.dMin(ccoa));

  }
}