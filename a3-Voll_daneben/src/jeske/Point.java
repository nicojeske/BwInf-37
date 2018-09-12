package jeske;

import java.util.List;

public class Point {
  private List<Integer> beforeNumbers;
  private List<Integer> afterNumbers;
  private Point nextPoint;
  private Point lastPoint;
  private int element;
  private int distanceSum;
  private int leftDistance;
  private int rightDistance;
  private int rightDiff;

  public Point(int element) {
    this.element = element;
  }

  public List<Integer> getBeforeNumbers() {
    return beforeNumbers;
  }

  public void setBeforeNumbers(List<Integer> beforeNumbers) {
    this.beforeNumbers = beforeNumbers;
  }

  public List<Integer> getAfterNumbers() {
    return afterNumbers;
  }

  public void setAfterNumbers(List<Integer> afterNumbers) {
    this.afterNumbers = afterNumbers;
  }

  public Point getNextPoint() {
    return nextPoint;
  }

  public void setRightPoint(Point nextPoint) {
    this.nextPoint = nextPoint;
  }

  public Point getLastPoint() {
    return lastPoint;
  }

  public void setLeftPoint(Point lastPoint) {
    this.lastPoint = lastPoint;
  }

  public int getElement() {
    return element;
  }

  public void setElement(int element) {
    this.element = element;
  }

  public int getDistanceSum() {
    return distanceSum;
  }

  public void setDistanceSum(int leftDistance, int rightDistance) {
    this.distanceSum = leftDistance + rightDistance;
    this.leftDistance = leftDistance;
    this.rightDistance = rightDistance;
  }

  public int getLeftDistance() {
    return leftDistance;
  }

  public int getRightDistance() {
    return rightDistance;
  }

  @Override
  public String toString() {
    return "Point{" +
            "element=" + element +
            '}';
  }

  public int getRightDiff() {
    return rightDiff;
  }

  public void setRightDiff(int rightDiff) {
    this.rightDiff = rightDiff;
  }
}
