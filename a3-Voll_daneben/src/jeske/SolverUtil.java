package jeske;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides testable utility functions for the Solver
 */
public class SolverUtil {
  /**
   * Finds the element in the list that is closest to the given number.
   *
   * @param numbers list of numbers
   * @param avg     number
   * @return closest element in the list
   */
  static int nearestElement(List<Integer> numbers, double avg) {
    if (numbers.size() == 1)
      return numbers.get(0);

    for (int i = 0; i < numbers.size() - 1; i++) {
      int aiNumber = numbers.get(i);
      int nextNumber = numbers.get(i + 1);

      if (aiNumber == Math.round(avg))
        return aiNumber;

      if (nextNumber == Math.round(avg))
        return nextNumber;

      if (aiNumber <= avg && avg < nextNumber) {
        double distDown = Math.abs(avg - aiNumber);
        double distUp = Math.abs(avg - nextNumber);

        if (distDown == distUp)
          return nextNumber;

        if (Math.min(distDown, distUp) == distDown) {
          return aiNumber;
        } else {
          return nextNumber;
        }
      }

    }

    //The code should not reach this point
    throw new UnsupportedOperationException();
  }

  /**
   * Calculates the average of all numbers in the list.
   *
   * @param numbers list
   * @return average
   */
  static double avg(List<Integer> numbers) {
    double sum = 0;
    double numberOfElements = numbers.size();
    for (int number : numbers) {
      sum += number;
    }
    return sum / numberOfElements;
  }

  /**
   * For a given AI selection, the cost of this will be calculated
   *
   * @param numbers   numbers
   * @param aiNumbers ai numbers
   * @return cost
   */
  public static int calcExpenses(List<Integer> numbers, List<Integer> aiNumbers) {
    AtomicInteger payments = new AtomicInteger(0);

    for (int number : numbers) {

      for (int i = 0; i < aiNumbers.size() - 1; i++) {
        int aiNumber = aiNumbers.get(i);
        int nextNumber = aiNumbers.get(i + 1);

        if (aiNumber <= number && number < nextNumber) {
          int distDown = Math.abs(number - aiNumber);
          int distUp = Math.abs(number - nextNumber);
          payments.addAndGet(Math.min(distDown, distUp));
          break;
        }
        //choosen > highest AI number
        else if (number > aiNumbers.get(aiNumbers.size() - 1)) {
          payments.addAndGet(Math.abs(number - aiNumbers.get(aiNumbers.size() - 1)));
          break;
        } else if (number < aiNumbers.get(0)) {
          payments.addAndGet(aiNumbers.get(0) - number);
          break;
        }
      }
    }
    return payments.get();
  }
}
