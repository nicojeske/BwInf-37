package jeske;

import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Various tests for the solverUtil
 */
class SolverTest {

  @Test
  void calcExpenses() {
    int[] numbersArray = new int[]{1, 15, 100, 200, 300};
    List<Integer> numbers = Ints.asList(numbersArray);

    int[] aiArray = new int[]{1, 35, 117, 321, 448, 500, 678, 780, 802, 999};
    List<Integer> ai = Ints.asList(aiArray);

    assertEquals(135, Solver.calcExpenses(numbers, ai));

    numbersArray = new int[]{1, 3, 8, 14, 24, 34, 40, 50};
    numbers = Ints.asList(numbersArray);

    aiArray = new int[]{1, 2, 3, 4, 5};
    ai = Ints.asList(aiArray);

    assertEquals(140, Solver.calcExpenses(numbers, ai));


    numbersArray = new int[]{3, 6, 9, 11, 15, 20, 23};
    numbers = Ints.asList(numbersArray);

    aiArray = new int[]{6, 9, 15, 21};
    ai = Ints.asList(aiArray);

    assertEquals(8, Solver.calcExpenses(numbers, ai));

    numbersArray = new int[]{3, 6, 9, 11, 15, 20, 23, 35, 42, 47, 50};
    numbers = Ints.asList(numbersArray);

    aiArray = new int[]{6, 20, 42};
    ai = Ints.asList(aiArray);

    assertEquals(39, Solver.calcExpenses(numbers, ai));
  }


  @Test
  void nearestElement() {
    int[] numbersArray = new int[]{0, 50};
    List<Integer> numbers = Ints.asList(numbersArray);

    assertEquals(50, Solver.nearestElement(numbers, 26));
    assertEquals(0, Solver.nearestElement(numbers, 24));
    assertEquals(50, Solver.nearestElement(numbers, 25));
    assertEquals(0, Solver.nearestElement(numbers, 1));
    assertEquals(50, Solver.nearestElement(numbers, 49));
  }

  @Test
  void avg() {
    int[] numbersArray = new int[]{0, 50};
    List<Integer> numbers = Ints.asList(numbersArray);

    assertEquals(25, Solver.avg(numbers));
  }
}