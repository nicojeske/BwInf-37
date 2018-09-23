package jeske;

import com.google.common.base.Objects;

public abstract class Rectangle {
  int width;
  int height;

  public Rectangle(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Rectangle rectangle = (Rectangle) o;
    return width == rectangle.width &&
            height == rectangle.height;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(width, height);
  }
}
