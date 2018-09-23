package jeske;

import jeske.GUI.Rect;

public class CCOA {
  public int x;
  public int y;
  public Rect i;
  public boolean rotated;

  public CCOA(int x, int y, Rect i, boolean rotated) {
    this.x = x;
    this.y = y;
    this.i = new Rect(i);
    this.i.updatePosition(x, y);
    this.rotated = rotated;
  }

  @Override
  public String toString() {
    return "CCOA{" +
            "x=" + x +
            ", y=" + y +
            ", i=" + i +
            ", rotated=" + rotated +
            '}';
  }
}
