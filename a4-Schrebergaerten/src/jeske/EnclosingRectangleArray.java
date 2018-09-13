package jeske;

import java.awt.*;

public class EnclosingRectangleArray {
  private LineDimension[] columns;
  private LineDimension[] rows;
  private Rectangle[][] data;
  private int nbrClolums;
  private int nbrRows;

  public void initialize(int capacityX, int capacityY, int firstColumnWidth, int firstRowHeight, Rectangle firstCellValue) {
    if (columns == null || columns.length < capacityX) {
      columns = new LineDimension[capacityX];
    }

    if (rows == null || rows.length < capacityX) {
      rows = new LineDimension[capacityY];
    }


  }

  private class LineDimension {
    public int size;
    public int index;
  }
}
