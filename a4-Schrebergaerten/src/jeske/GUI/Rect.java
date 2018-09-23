package jeske.GUI;

import com.google.common.base.Objects;

import java.awt.*;

public class Rect extends jeske.Rectangle {
  public int x1;
  public int y1;
  public int x2;
  public int y2;
  public int width;
  public int height;

  public Rect(int x1, int y1, int x2, int y2) {
    super(x2 - x1, y2 - y1);

    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.width = x2 - x1;
    this.height = y2 - y1;

  }

  public Rect(Rect i) {
    this(i.x1, i.y1, i.x2, i.y2);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Rect getRoatated() {
    return new Rect(x1, y1, x1 + height, y1 + width);
  }

  public Rectangle getRectangle() {
    return new Rectangle(x1, y1, width, height);
  }


  public void updatePosition(int x, int y) {
    x1 = x;
    y1 = y;
    x2 = x + width;
    y2 = y + height;
  }

  public boolean sameSize(Rect rect) {
    return width == rect.width &&
            height == rect.height;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rect rect = (Rect) o;
    return x1 == rect.x1 &&
            y1 == rect.y1 &&
            x2 == rect.x2 &&
            y2 == rect.y2 &&
            width == rect.width &&
            height == rect.height;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(x1, y1, x2, y2, width, height);
  }
}
