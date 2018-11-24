package jeske;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.Lists;
import jeske.GUI.Draw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Solver for the task
 */
public class Solver {
  private List<Integer> numbers;
  private Draw draw;

  /**
   * Creates a new Instance of the Solver
   *
   * @param numbers chosen numbers
   * @param draw    GUI for rendering
   */
  Solver(List<Integer> numbers, Draw draw) {
    this.numbers = numbers;
    this.draw = draw;
  }

  /**
   * Splits an list into n mostly equal parts
   *
   * @param list list to split
   * @param n    number of result lists
   * @param <T>  type
   * @return list, split into n parts
   * @throws NullPointerException     input list null
   * @throws IllegalArgumentException n <= 0 OR n < list.size
   */
  private static <T> List<List<T>> orderedSplit(List<T> list, int n) throws NullPointerException, IllegalArgumentException {
    if (list == null) {
      throw new NullPointerException("list is null.");
    }

    if (n <= 0) {
      throw new IllegalArgumentException("division with 0");
    }

    if (list.size() < n) {
      throw new IllegalArgumentException("less elements than asked parts.");
    }

    List<List<T>> result = new ArrayList<>(n);

    int listsSize = list.size() / n;
    int remainder = list.size() % n;

    int index = 0;
    int remainderAccess = 0;
    int from = index * listsSize + remainderAccess;
    int to = (index + 1) * listsSize + remainderAccess;

    while (n > index) {

      if (remainder != 0) {
        result.add(list.subList(from, to + 1));
        remainder--;
        remainderAccess++;
      } else {
        result.add(list.subList(from, to));
      }

      index++;
      from = index * listsSize + remainderAccess;
      to = (index + 1) * listsSize + remainderAccess;
    }

    return result;
  }

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
   * For each number in the aiList, take the AI number itself, as well as the number before and after it.
   * e.g. from aiList [5,10,20] -> e.g. [[3,5,7], [7,10,13], [17,20]]
   *
   * @param numbersWithoutDuplicates chosen numbers
   * @param aiList                   ai numbers
   * @return list of possible variances
   */
  private List<List<Integer>> getPossibleVariances(List<Integer> numbersWithoutDuplicates, List<Integer> aiList) {
    List<List<Integer>> variances = new ArrayList<>();
    for (Integer anAiList : aiList) {
      List<Integer> currList = new ArrayList<>();
      int ai = anAiList;
      int index = numbersWithoutDuplicates.indexOf(ai);

      if (index >= 1) {
        int beforeNumber = numbersWithoutDuplicates.get(index - 1);
        currList.add(beforeNumber);
      }

      currList.add(ai);

      if (index < numbersWithoutDuplicates.size() - 1) {
        int afterNumber = numbersWithoutDuplicates.get(index + 1);
        currList.add(afterNumber);
      }

      variances.add(currList);
    }
    return variances;
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

  /**
   * Solves the task
   */
  void solve() {
    //Sort the numbers
    Collections.sort(numbers);

    //Removing duplicate numbers
    List<Integer> numbersWithoutDuplicates = new ArrayList<>(new HashSet<>(numbers));
    //Sort numbers in ascending order
    Collections.sort(numbersWithoutDuplicates);

    //The entire stake
    int paidMoney = 25 * numbers.size();

    //Column the numbers into ten parts
    List<List<Integer>> list = orderedSplit(numbers, 10);

    //Finding a first solution. For each of the ten parts,
    //an number z is searched for which the needed payment is minimal.
    //The nearest number to z is then saved for the solution.
    List<Integer> aiList = new ArrayList<>();
    for (List<Integer> lis : list) {
      int small = lis.get(0);
      int height = lis.get(lis.size() - 1);

      int min = 999999;
      int minZ = -1;

      for (int z = small; z <= height; z++) {
        int sum = 0;
        for (int i = 0; i < lis.size() - 1; i++) {
          int currNumber = lis.get(i);
          sum += Math.abs(currNumber - z);
        }

        if (sum <= min) {
          min = sum;
          minZ = z;
          //System.out.printf("Min: %s Z: %s \n", min, z);
        }
      }

      aiList.add(nearestElement(lis, minZ));
    }

    draw.setAreas(list);
    draw.setOldSoulution(aiList);
    draw.repaint();

    Log.info("First Solution: " + aiList + " -> " + (paidMoney - calcExpenses(numbers, aiList)) + " gain.");


    //For each AI number, take the AI number itself, as well as the number before and after it.
    //e.g. from aiList [5,10,20] -> e.g. [[3,5,7], [7,10,13], [17,20]]
    List<List<Integer>> variances = getPossibleVariances(numbersWithoutDuplicates, aiList);
    List<List<Integer>> allPossibilitys = Lists.cartesianProduct(variances);

    //Test all possible solutions and choose the best one.
    int lowest = Integer.MAX_VALUE;
    List<Integer> best = null;
    for (List<Integer> possibility : allPossibilitys) {
      int calced = calcExpenses(numbers, possibility);
      if (calced < paidMoney) {
        if (calced < lowest) {
          lowest = calced;
          best = possibility;
        }
      }
    }

    draw.setSolution(best);
    draw.repaint();
    Log.info("Final solution: " + best + " with " + (paidMoney - lowest) + " gain");
  }
}
