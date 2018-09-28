package jeske;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.Lists;
import jeske.GUI.Draw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static jeske.SolverUtil.*;

/**
 * Solver for the task
 */
class Solver {
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
    //the average is calculated and then the ai-number is selected from the part closest to the average.
    List<Integer> aiList = new ArrayList<>();
    for (List<Integer> lis : list) {
      double avg = avg(lis);
      int point = nearestElement(lis, avg);
      aiList.add(point);
      //System.out.println(lis);
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
    int lowest = 9999999;
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
}
