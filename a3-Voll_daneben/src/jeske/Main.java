package jeske;

import com.esotericsoftware.minlog.Log;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import jeske.GUI.Draw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

  private static List<Integer> numberChumber;

  public static void main(String[] args) {
    BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(
                    Main.class.getResourceAsStream("/beispieldaten/beispiel3.txt")
            )
    );


    List<Integer> numbers = new ArrayList<>();
    bufferedReader.lines().forEach(l -> numbers.add(Integer.parseInt(l)));

    Set<Integer> numbersWithoutDuplicatesSet = new HashSet<>(numbers);
    List<Integer> numbersWithoutDuplicates = new ArrayList<>(numbersWithoutDuplicatesSet);
    Collections.sort(numbersWithoutDuplicates);

    int gotMoney = 25 * numbers.size();
    Collections.sort(numbers);
    Draw draw = new Draw(numbers);
    numberChumber = numbers;
    //fkingShitSolution(numbers);
    /*
    List<Point> kartoffel = realSolution(numbers, 5);
    int[] ai = new int[kartoffel.size()];
    for (int i = 0; i < kartoffel.size(); i++) {
      ai[i] = kartoffel.get(i).getElement();
    }
    Arrays.sort(ai);

    Log.info("Found solution: " + Arrays.toString(ai) + " Worth: " + calcExpenses(numbers, Ints.asList(ai)) + " Win: " + gotMoney);*/


    //int[] numbersArray = new int[]{1,3,8,14,24,34,40,50};
    int[] numbersArray = new int[]{3, 6, 9, 11, 15, 20, 23};
    /*int[] numbersArray = new int[]{3
            ,6
            ,9
            ,11
            ,15
            ,20
            ,23
            ,35
            ,42
            ,47
            ,50};*/

    //allPos(numbersArray,3);
    //brute2(numbers,5);
    //brute2(numbers,10);
    int[] aiN = new int[]{1, 7, 12};
    System.out.println(calcExpenses(numbers, Ints.asList(aiN)));
    int[] a = new int[numbers.size()];
    for (int i = 0; i < numbers.size(); i++) {
      a[i] = numbers.get(i);
    }
    System.out.println("Nice " + largestMinDist(a, a.length, 10));
    System.out.println();

    int size = (int) Math.ceil(numbers.size() / 5.0);
    //Maybe bisection
    for (int i = 1; i < 100; i++) {
      //System.out.println("i: " + i + " listSize: " + Lists.partition(numbers, i).size());
    }

    List<List<Integer>> list = orderedSplit(numbers, 10);

    System.out.println();
    List<Integer> aiList = new ArrayList<>();
    for (List<Integer> lis : list) {
      double avg = avg(lis);
      int po9int = nearestElement(lis, avg);
      aiList.add(po9int);
      System.out.println(lis);
    }

    draw.setShit(list);
    draw.setOldSoulution(aiList);
    draw.repaint();


    Log.info(aiList + " -> " + calcExpenses(numbers, aiList) + " / " + gotMoney);
    if (calcExpenses(numbers, aiList) < gotMoney) {
      // Log.info("FUCKING SUCCESS!!! \n" +
      //       " gotMoney: " + gotMoney);
    }

    Log.info("__________________________");

    List<List<Integer>> well = new ArrayList<>();
    for (int i = 0; i < aiList.size(); i++) {
      List<Integer> currList = new ArrayList<>();
      int ai = aiList.get(i);
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

      well.add(currList);
    }

    List<List<Integer>> lel = Lists.cartesianProduct(well);

    int lowest = 9999999;
    List<Integer> best = null;
    for (List<Integer> li : lel) {
      int calced = calcExpenses(numbers, li);
      if (calced < gotMoney) {
        //Log.info(aiList + " -> " + calced + " / " + gotMoney);
        // Log.info("FUCKING SUCCESS!!! \n" +
        //         " gotMoney: " + gotMoney);
        if (calced < lowest) {
          lowest = calced;
          best = li;
        }
      }
    }
    draw.setSolution(best);
    draw.repaint();
    Log.info("BEST LIST: " + best + " with " + lowest + " / " + gotMoney);
    //List<List<Integer>> list = Lists.partition(numbers, 40);
    //List<List<Integer>> list = chunks(numbers, 5);
    //List<List<List<Integer>>> list2= split(numbers, 10);
    // https://stackoverflow.com/questions/16616560/is-there-a-simple-way-to-split-one-list-into-x-sublists
  }

  public static <T> List<List<T>> orderedSplit(List<T> list, int lists) throws NullPointerException, IllegalArgumentException {
    if (list == null) {
      throw new NullPointerException("La lista es nula.");
    }

    if (lists <= 0) {
      throw new IllegalArgumentException("La lista debe divirse en una cantidad mayor a 0.");
    }

    if (list.size() < lists) {
      throw new IllegalArgumentException("El tamaño de la lista no es suficiente para esa distribución.");
    }

    List<List<T>> result = new ArrayList<List<T>>(lists);

    int listsSize = list.size() / lists;
    int remainder = list.size() % lists;

    int index = 0;
    int remainderAccess = 0;
    int from = index * listsSize + remainderAccess;
    int to = (index + 1) * listsSize + remainderAccess;

    while (lists > index) {

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


  public static <T> List<List<T>> splits(List<T> list, int size)
          throws NullPointerException, IllegalArgumentException {
    if (list == null) {
      throw new NullPointerException("The list parameter is null.");
    }

    if (size <= 0) {
      throw new IllegalArgumentException(
              "The size parameter must be more than 0.");
    }

    List<List<T>> result = new ArrayList<List<T>>(size);

    for (int i = 0; i < size; i++) {
      result.add(new ArrayList<T>());
    }

    int index = 0;

    for (T t : list) {
      result.get(index).add(t);
      index = (index + 1) % size;
    }

    return result;
  }

  public static <T> List<List<Integer>> chunks(List<Integer> originalList, int partitionSize) {
    List<List<Integer>> partitions = new LinkedList<List<Integer>>();
    for (int i = 0; i < originalList.size(); i += partitionSize) {
      partitions.add(originalList.subList(i,
              Math.min(i + partitionSize, originalList.size())));
    }
    return partitions;
  }

  public static <T> List<List<T>> splitListToSubLists(List<T> parentList, int subListSize) {
    List<List<T>> subLists = new ArrayList<List<T>>();
    if (subListSize > parentList.size()) {
      subLists.add(parentList);
    } else {
      int remainingElements = parentList.size();
      int startIndex = 0;
      int endIndex = subListSize;
      do {
        List<T> subList = parentList.subList(startIndex, endIndex);
        subLists.add(subList);
        startIndex = endIndex;
        if (remainingElements - subListSize >= subListSize) {
          endIndex = startIndex + subListSize;
        } else {
          endIndex = startIndex + remainingElements - subList.size();
        }
        remainingElements -= subList.size();
      } while (remainingElements > 0);

    }
    return subLists;
  }

  @SuppressWarnings("unchecked")
  private static List<List<List<Integer>>> split(List<Integer> list, int groups) {
    if (groups <= 0 || groups > list.size())
      throw new IllegalArgumentException("Invalid number of groups: " + groups +
              " (list size: " + list.size() + ")");
    List<List<List<Integer>>> result = new ArrayList<>();
    split(list, 0, new List[groups], 0, result);
    return result;
  }

  private static void split(List<Integer> list, int listIdx,
                            List<Integer>[] combo, int comboIdx,
                            List<List<List<Integer>>> result) {
    if (combo.length - comboIdx == 1) {
      combo[comboIdx] = list.subList(listIdx, list.size());
      result.add(new ArrayList<>(Arrays.asList(combo)));
    } else {
      for (int i = 0; i <= (list.size() - listIdx) - (combo.length - comboIdx); i++) {
        combo[comboIdx] = list.subList(listIdx, listIdx + 1 + i);
        split(list, listIdx + 1 + i, combo, comboIdx + 1, result);
      }
    }
  }

  private static List<Point> realSolution(List<Integer> numbers, int k) {
    List<Point> ret = new ArrayList<>();
    if (k == 1) {
      double avg = avg(numbers);
      int element = nearestElement(numbers, avg);
      Log.info("First point avg -> " + avg + " Point -> " + element);
      ret.add(new Point(element));
      return ret;
    }


    List<Point> points = realSolution(numbers, k - 1);
    initializePoints(points, numbers);

    ret.addAll(points);

    Point pointToSplit = new Point(-1);
    Point pointAfter = null;
    Point pointBefore = null;
    pointToSplit.setDistanceSum(Integer.MIN_VALUE, 0);

    for (Point point : points) {
      if (point.getDistanceSum() > pointToSplit.getDistanceSum()) {
        pointToSplit = point;
        pointBefore = point.getLastPoint();
        pointAfter = point.getNextPoint();
      }
    }

    Log.info("Point to split: " + pointToSplit + " with distance " + pointToSplit.getDistanceSum());

    ret.remove(pointToSplit);
    Point newPointLeft;
    Point newPointRight;

    if (pointToSplit.getBeforeNumbers().isEmpty()) {
      newPointLeft = pointToSplit;
    } else {
      newPointLeft = generatePoint(pointToSplit.getBeforeNumbers(), numbers);
    }

    if (pointToSplit.getAfterNumbers().isEmpty()) {
      newPointRight = pointToSplit;
    } else {
      newPointRight = generatePoint(pointToSplit.getAfterNumbers(), numbers);
    }

    Log.info("Splitted into: \n" +
            " left : " + newPointLeft + "\n" +
            " right: " + newPointRight);

    newPointLeft.setLeftPoint(pointBefore);
    newPointLeft.setRightPoint(newPointRight);
    newPointRight.setLeftPoint(newPointLeft);
    newPointRight.setRightPoint(pointAfter);

    ret.add(newPointLeft);
    ret.add(newPointRight);

    return ret;
  }


  public static void initializePoints(List<Point> points, List<Integer> numbers) {
    for (Point currPoint : points) {
      int pointIndex = numbers.indexOf(currPoint.getElement());

      //Before
      if (currPoint.getLastPoint() == null) {
        //+1 für mit
        currPoint.setBeforeNumbers(numbers.subList(0, pointIndex));
      } else {
        Point beforePoint = currPoint.getLastPoint();
        int diff = Math.abs(beforePoint.getElement() - currPoint.getElement()) / 2;
        int nearestElement = nearestElement(numbers, currPoint.getElement() - diff);
        int indexStart;
        //Ist im Bereich
        if (nearestElement > currPoint.getElement() - diff) {
          indexStart = numbers.indexOf(nearestElement);
        } else {
          indexStart = numbers.indexOf(nearestElement) + 1;
        }

        //+1 für mit
        currPoint.setBeforeNumbers(numbers.subList(indexStart, pointIndex));
      }
      if (currPoint.getNextPoint() == null) {
        //+1 für ohne
        currPoint.setAfterNumbers(numbers.subList(pointIndex + 1, numbers.size()));
      } else {
        Point nextPoint = currPoint.getNextPoint();
        int diff = Math.abs(nextPoint.getElement() - currPoint.getElement()) / 2;
        currPoint.setRightDiff(diff);
        int nearestElement = nearestElement(numbers, currPoint.getElement() + diff);
        int indexEnd;
        //Ist im Bereich
        if (nearestElement < currPoint.getElement() + diff) {
          indexEnd = numbers.indexOf(nearestElement);
        } else {
          indexEnd = numbers.indexOf(nearestElement) - 1;
        }
        //+1 für ohne
        currPoint.setAfterNumbers(numbers.subList(pointIndex + 1, indexEnd + 1));
      }

      int leftDistance = calcSumOfDistances(currPoint.getBeforeNumbers(), currPoint.getElement());
      int rightDistance = calcSumOfDistances(currPoint.getAfterNumbers(), currPoint.getElement());
      Log.info("Point = " + currPoint + "\n" +
              " leftDistance  = " + leftDistance + "\n" +
              " rightDistance = " + rightDistance);
      currPoint.setDistanceSum(leftDistance, rightDistance);
    }
  }

  private static int calcSumOfDistances(List<Integer> subList, int point) {
    int sum = 0;
    for (int number : subList) {
      sum += Math.abs(point - number);
    }
    return sum;
  }

  public static Point generatePoint(List<Integer> subList, List<Integer> numbers) {
    double avg = avg(subList);
    int element = nearestElement(numbers, avg);
    return new Point(element);
  }

  public static int nearestElement(List<Integer> numbers, double avg) {
    if (numbers.size() == 1)
      return numbers.get(0);

    for (int i = 0; i < numbers.size() - 1; i++) {
      int aiNumber = numbers.get(i);
      int nextNumber = numbers.get(i + 1);

      if (aiNumber == Math.round(avg))
        return aiNumber;

      if (nextNumber == Math.round(avg))
        return nextNumber;


      //aiNumber <= choosenNumber <=nextNumber
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

    throw new UnsupportedOperationException();
  }

  public static double avg(List<Integer> numbers) {
    double sum = 0;
    double numberOfElements = numbers.size();
    for (int number : numbers) {
      sum += number;
    }
    return sum / numberOfElements;
  }

  private static void fkingShitSolution(List<Integer> numbers) {
    int k = 0;
    int D = numbers.get(numbers.size() - 1) - numbers.get(0);
    while (k != 3) {
      List<Integer> test = new ArrayList<>();

      int curr = numbers.get(0);
      test.add(curr);
      for (int n : numbers) {
        if (n - curr >= D) {
          test.add(n);
          curr = n;
        }
      }
      Log.info("D=" + D + " " + test);
      Log.info("Expenses=" + calcExpenses(numbers, test));
      //Log.info("Participation cost=" + gotMoney);
      //Log.info("Win: " + (gotMoney-calcExpenses(numbers, test)));
      k = test.size();
      if (test.size() > 10) {
        D += 1;
      } else if (test.size() < 10) {
        D -= 1;
      }
    }
  }

  private static void brute2(List<Integer> numbers, int k) {
    int min = 9999999;
    int[] bestAi = new int[0];
    Set<Integer> numberSet = new HashSet<>();
    numberSet.addAll(numbers);
    List<Integer> leckMich = new ArrayList<>();
    numberSet.forEach(leckMich::add);
    Collections.sort(leckMich);

    //List<Integer> numbers = Ints.asList(numbersArray);
    Map<Integer, List<int[]>> map = new HashMap<>();

    switch (k) {
      case 3:
        for (int n1 : numbers) {
          for (int n2 : numbers) {
            for (int n3 : numbers) {
              int[] ai = new int[]{n1, n2, n3};

              int expenses = calcExpenses(numbers, Ints.asList(ai));
              if (!map.containsKey(expenses)) {
                map.put(expenses, new ArrayList<>());
              } else {
                map.get(expenses).add(ai);
              }
              if (expenses < min) {
                min = expenses;
                bestAi = ai;
              }
            }
          }
        }
        break;
      case 10:
        for (int n1 : leckMich) {
          Log.info("N1 " + numbers.indexOf(n1) / (double) numbers.get(numbers.size() - 1));
          for (int n2 : leckMich) {
            Log.info(" N2 " + numbers.indexOf(n2) / (double) numbers.get(numbers.size() - 1));
            for (int n3 : leckMich) {
              Log.info("  N3 " + numbers.indexOf(n3) / (double) numbers.get(numbers.size() - 1));
              for (int n4 : leckMich) {
                Log.info("   N4 " + numbers.indexOf(n4) / (double) numbers.get(numbers.size() - 1));
                for (int n5 : leckMich) {
                  Log.info("    N5 " + numbers.indexOf(n5) / (double) numbers.get(numbers.size() - 1));
                  for (int n6 : leckMich) {
                    for (int n7 : leckMich) {
                      int[] test2 = new int[]{n1, n2, n3, n4, n5, n6, n7};
                      Log.info(Arrays.toString(test2));
                      for (int n8 : leckMich) {
                        for (int n9 : leckMich) {
                          for (int n10 : leckMich) {
                            int[] ai = new int[]{n1, n2, n3, n4, n5, n6, n7, n8, n9, n10};

                            int expenses = calcExpenses(numbers, Ints.asList(ai));
                            if (!map.containsKey(expenses)) {
                              map.put(expenses, new ArrayList<>());
                            } else {
                              map.get(expenses).add(ai);
                              if (expenses < min) {
                                min = expenses;
                                bestAi = ai;
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        break;
      case 5:
        for (int n1 : numbers) {
          for (int n2 : numbers) {
            for (int n3 : numbers) {
              for (int n4 : numbers) {
                for (int n5 : numbers) {
                  int[] ai = new int[]{n1, n2, n3, n4, n5};

                  int expenses = calcExpenses(numbers, Ints.asList(ai));
                  if (!map.containsKey(expenses)) {
                    map.put(expenses, new ArrayList<>());
                  } else {
                    map.get(expenses).add(ai);
                    if (expenses < min) {
                      min = expenses;
                      bestAi = ai;
                    }
                  }
                }
              }
            }
          }
        }
        break;
    }

    Log.info("BRUTEFORCING: " + numbers);
    Log.info("Result: AI: " + Arrays.toString(bestAi) + " expenses: " + min);
  }

  private static void allPos(int[] numbersArray, int kSize) {
    int to = numbersArray[numbersArray.length - 1];
    switch (kSize) {
      case 5:
        to += 4;
        break;
      case 3:
        to += 2;
        break;
    }
    int min = 9999999;
    int[] bestAi = new int[0];
    List<Integer> numbers = Ints.asList(numbersArray);
    Map<Integer, List<int[]>> map = new HashMap<>();

    switch (kSize) {

      case 5:
        for (int i = 1; i < to - 1; i++) {
          for (int j = i + 1; j < to - 2; j++) {
            for (int k = j + 1; k < to - 3; k++) {
              for (int l = k + 1; l < to - 4; l++) {
                for (int m = l + 1; m < to - 5; m++) {
                  int[] ai = new int[]{i, j, k, l, m};

                  int expenses = calcExpenses(numbers, Ints.asList(ai));
                  //map.put(expenses, ai);
                  if (expenses < min) {
                    min = expenses;
                    bestAi = ai;
                  }
                }
              }
            }
          }
        }
        break;
      case 3:
        for (int i = 1; i < to - 1; i++) {
          for (int j = 1; j < to - 2; j++) {
            for (int k = 1; k < to - 3; k++) {
              int[] ai = new int[]{i, j, k};

              int expenses = calcExpenses(numbers, Ints.asList(ai));
              if (!map.containsKey(expenses)) {
                map.put(expenses, new ArrayList<>());
              } else {
                map.get(expenses).add(ai);
              }
              if (expenses < min) {
                min = expenses;
                bestAi = ai;
              }
            }
          }
        }

    }

    Log.info("BRUTEFORCING: " + Arrays.toString(numbersArray));
    Log.info("Result: AI: " + Arrays.toString(bestAi) + " expenses: " + min);

  }


  public static int calcExpenses(List<Integer> choosenNumbers, List<Integer> aiNumbers) {
    AtomicInteger payments = new AtomicInteger(0);
    //Log.info("Choosen numbers -> " + choosenNumbers);
    //Log.info("ai numbers      -> " + aiNumbers);
    for (int choosenNumber : choosenNumbers) {
      //Log.info("Choosen number: " + choosenNumber);

      for (int i = 0; i < aiNumbers.size() - 1; i++) {
        int aiNumber = aiNumbers.get(i);
        int nextNumber = aiNumbers.get(i + 1);

        //aiNumber <= choosenNumber <=nextNumber
        if (aiNumber <= choosenNumber && choosenNumber < nextNumber) {
          //  Log.info("Down number   : " + aiNumber);
          //Log.info("Upper number  : " + nextNumber);
          int distDown = Math.abs(choosenNumber - aiNumber);
          int distUp = Math.abs(choosenNumber - nextNumber);
          //System.out.println("distUp = " + distUp);
          //System.out.println("distDown = " + distDown);
          //Log.info("Dist="+Math.min(distDown, distUp));
          payments.addAndGet(Math.min(distDown, distUp));
          break;
        }
        //choosen > highest AI number
        else if (choosenNumber > aiNumbers.get(aiNumbers.size() - 1)) {
          payments.addAndGet(Math.abs(choosenNumber - aiNumbers.get(aiNumbers.size() - 1)));
          // Log.info("Dist="+Math.abs(choosenNumber-aiNumbers.get(aiNumbers.size()-1)));
          break;
        } else if (choosenNumber < aiNumbers.get(0)) {
          //Log.info("Dist=" + (aiNumbers.get(0)-choosenNumber));
          payments.addAndGet(aiNumbers.get(0) - choosenNumber);
          break;
        }
      }
    }
    return payments.get();
  }


  // Returns true if it is possible to
// arrange k elements of arr[0..n-1]
// with minimum distance given as mid.
  static boolean isFeasible(int mid, int arr[],
                            int n, int k) {
    // Place first element at arr[0] position
    int pos = arr[0];
    List<Integer> poss = new ArrayList<>();
    poss.add(pos);
    // Initialize count of elements placed.
    int elements = 1;

    // Try placing k elements with minimum
    // distance mid.
    for (int i = 1; i < n; i++) {
      if (arr[i] - pos >= mid) {
        // Place next element if its
        // distance from the previously
        // placed element is greater
        // than current mid
        pos = arr[i];
        elements++;
        poss.add(pos);
        // Return if all elements are
        // placed successfully
        if (elements == k) {
          System.out.println(poss + " -> " + calcExpenses(numberChumber, poss));
          return true;
        }
      }
    }
    return false;
  }

  // Returns largest minimum distance for
// k elements in arr[0..n-1]. If elements
// can't be placed, returns -1.
  static int largestMinDist(int arr[], int n,
                            int k) {
    // Sort the positions
    Arrays.sort(arr);

    // Initialize result.
    int res = -1;

    // Consider the maximum possible distance
    int left = arr[0], right = arr[n - 1];

    // Do binary search for largest
    // minimum distance
    while (left < right) {
      int mid = (left + right) / 2;

      // If it is possible to place k
      // elements with minimum distance mid,
      // search for higher distance.
      if (isFeasible(mid, arr, n, k)) {
        // Change value of variable max to
        // mid if all elements can be
        // successfully placed
        res = Math.max(res, mid);
        left = mid + 1;
      }

      // If not possible to place k elements,
      // search for lower distance
      else
        right = mid;
    }

    return res;
  }


}

