package jeske;

import com.google.common.base.Strings;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import jeske.GUI.Output;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static com.esotericsoftware.minlog.Log.*;


public class Main {

  public static void main(String[] args) throws InterruptedException {
    set(LEVEL_INFO);

    //Opens GUI if not in debug mode
    if (INFO && !DEBUG) {
      CountDownLatch guiSignal = new CountDownLatch(1);
      Output.main(guiSignal);
      //wait that System.out was redirected to the GUI
      guiSignal.await();
    }

    BufferedReader br = null;
    //User chooses a file
    if (!DEBUG) {
      while (br == null) {
        try {
          br = new BufferedReader(new FileReader(
                  Objects.requireNonNull(Utility.getUserFile("Superstar", "txt"))
          ));
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, "Feher bei der Dateiauswahl.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
      }
      //predetermined file for debug
    } else {
      br = new BufferedReader(new InputStreamReader(
              Main.class.getClassLoader().getResourceAsStream("./beispieldaten/superstar4.txt/")
      ));
    }

    //Create the fitting graph with guava
    MutableGraph<Person> relationGraph;
    try {
      relationGraph = initialize(br);
    } catch (IOException | IllegalArgumentException e) {
      JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
      main(null);
      return;
    }


    if (DEBUG) {
      //Save the graph as GraphML for yEd
      try {
        Utility.writeGraphML(relationGraph);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

    //Find the superstar
    solve(relationGraph);
  }

  /**
   * Finds the superstar in a given relationship graph
   *
   * @param relationGraph relationship graph
   */
  private static void solve(MutableGraph<Person> relationGraph) {
    //query counter for the invoice
    int querys = 0;

    //DEBUG
    int lastQueryCount;


    //Allready checked relationships.
    Map<Person, List<Person>> checkedRelations = new HashMap<>();
    relationGraph.nodes().forEach(person -> checkedRelations.put(person, new ArrayList<>()));

    //All persons in the network are possible candidates
    List<Person> candidates = new ArrayList<>(relationGraph.nodes());
    //All persons in the network
    List<Person> people = new ArrayList<>(relationGraph.nodes());

    //Graph of the inquired relationships
    MutableGraph<Person> askedGraph = GraphBuilder.directed().build();
    //Copy over the persons
    people.forEach(askedGraph::addNode);

    //First Candidate
    Person person = candidates.get(0);

    debug("Finding candidate");

    //Sort out the candidates according to the following principle until there is only one candidate left:
    //1. Go through all the other candidates where the relationship status has not yet been checked.
    //2.1. If there is a relationship between the current candidate and another candidate,
    //     the current candidate cannot be a superstar and is removed while the algorithm
    //     is repeated for the other candidate.
    //2.2. Otherwise, if there is no relationship between the current candidate and another candidate,
    //     then the other candidate cannot be a superstar, since not all of them follow him and are
    //     therefore deleted from the candidate list.
    while (candidates.size() > 1) {
      for (int i = 0; i < candidates.size(); i++) {
        Person testedPerson = candidates.get(i);

        //Same person
        if (testedPerson == person)
          continue;

        //Already checked
        if (checkedRelations.get(person).contains(testedPerson))
          continue;

        //debug("Test relation between " + person + " and " + testedPerson);
        querys++;
        checkedRelations.get(person).add(testedPerson);
        //Has a relationship -> remove current candidate. new candidate = testedPerson
        if (relationGraph.hasEdgeConnecting(person, testedPerson)) {
          candidates.remove(person);
          askedGraph.putEdge(person, testedPerson);
          person = testedPerson;
          break;
        } else {
          //No relationship -> Remove tested Person
          candidates.remove(testedPerson);
        }
      }
    }

    //Getting the only possible candidate
    Person lastCandidate = candidates.get(0);
    debug("Found candidate " + lastCandidate + " with " + querys + " querys.");
    //info("Letzter Kandidat für den Superstar ist " + lastCandidate);

    lastQueryCount = querys;
    debug("Check that the candidate doesnt follow anyone.");

    //--------------------------------------------------------
    //     - Check if all conditions are met for candidate -
    //--------------------------------------------------------

    //Testing if the last candidate follows no one
    for (Person p : people) {
      if (p == lastCandidate)
        continue;

      if (checkedRelations.get(lastCandidate).contains(p))
        continue;

      //debug("Test relation between " + lastCandidate + " and " + p);
      querys++;
      checkedRelations.get(lastCandidate).add(p);
      if (relationGraph.hasEdgeConnecting(lastCandidate, p)) {
        error(lastCandidate + " ist kein Superstar, da er " + p + " folgt.");
        return;
      }
    }
    debug("Completed check with " + (querys - lastQueryCount) + " querys");


    lastQueryCount = querys;
    debug("Check that really everyone follows the candidate");
    for (Person p : people) {
      if (p == lastCandidate)
        continue;

      if (checkedRelations.get(p).contains(lastCandidate))
        continue;

      querys++;
      checkedRelations.get(p).add(lastCandidate);
      if (!relationGraph.hasEdgeConnecting(p, lastCandidate)) {
        error(lastCandidate + " ist kein Superstar, da ihm nicht alle folgen.");
        error("Es ist kein Superstar in dieser Gruppe vorhanden.");
        return;
      } else {
        askedGraph.putEdge(p, lastCandidate);
      }
    }
    debug("Completed check with " + (querys - lastQueryCount) + " querys");

    //Last check for safety
    int connnectionsToCandidate = askedGraph.inDegree(lastCandidate);
    if (connnectionsToCandidate != askedGraph.nodes().size() - 1) {
      error(lastCandidate + " ist kein Superstar, da ihm nicht alle folgen? (Unexpected Error)");
      error("Es ist kein Superstar in dieser Gruppe vorhanden.");
      return;
    }

    info(lastCandidate + " ist der SUPERSTAR der Gruppe.");
    info("Es wurden " + querys + " Anfragen benötigt. Kosten: " + querys + "€");
  }

  /**
   * Creates a relationship graph from the input file.
   * The persons are the nodes and their relationship the directed edges
   *
   * @param br the file with the relations
   * @return A relation-graph from the given relations
   * @throws IOException              Error while reading the file
   * @throws IllegalArgumentException Error while analyzing the file
   */
  private static MutableGraph<Person> initialize(@NotNull BufferedReader br) throws IllegalArgumentException, IOException {
    debug("Loading persons...");
    StringTokenizer stringTokenizer = new StringTokenizer(br.readLine());
    int numberOfPeople = stringTokenizer.countTokens();
    //Hashmap with name -> person
    Map<String, Person> people = new HashMap<>();

    if (numberOfPeople <= 1)
      throw new IllegalArgumentException("The network must consist of more than one person.");


    debug("Number of people=" + numberOfPeople);
    debug("Creating graph...");
    MutableGraph<Person> relationGraph = GraphBuilder.directed().expectedNodeCount(numberOfPeople).build();

    //Adds the persons to the graph
    while (stringTokenizer.hasMoreTokens()) {
      String name = stringTokenizer.nextToken();
      Person newPerson = new Person(name);
      people.put(name, newPerson);
      relationGraph.addNode(newPerson);
      debug("Created person '" + name + "'");
    }

    //Adds the relations to the graph
    String currentLine;
    while (!Strings.isNullOrEmpty(currentLine = br.readLine())) {
      StringTokenizer relationTokens = new StringTokenizer(currentLine);

      if (relationTokens.countTokens() != 2)
        throw new IllegalArgumentException("Invalid relationship specification in the input file");

      String relationFromString = relationTokens.nextToken();
      String relationToString = relationTokens.nextToken();
      Person relationFromPerson = people.get(relationFromString);
      Person relationToPerson = people.get(relationToString);

      //saves the newly created edge
      relationGraph.putEdge(relationFromPerson, relationToPerson);
    }

    debug("Created graph with " + relationGraph.nodes().size() + " persons and "
            + relationGraph.edges().size() + " relationships");
    return relationGraph;
  }
}
