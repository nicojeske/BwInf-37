package jeske;

import com.google.common.base.Strings;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import jeske.GUI.Output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static com.esotericsoftware.minlog.Log.*;


public class Main {

  private static Map<String, Person> people = new HashMap<>();

  public static void main(String[] args) throws IOException, InterruptedException {
    set(LEVEL_DEBUG);

    if (INFO && !DEBUG) {
      CountDownLatch guiSignal = new CountDownLatch(1);
      Output.main(guiSignal);
      guiSignal.await();
    }





    BufferedReader br = new BufferedReader(new InputStreamReader(
            Main.class.getResourceAsStream("/beispieldaten/superstar4.txt")
    ));


    //CreateGraph
    MutableGraph<Person> relationGraph = initialize(br);

    if (DEBUG) {
      Utility.writeGraphML(relationGraph);
    }

    solve(relationGraph);

  }

  private static void solve(MutableGraph<Person> relationGraph) {
    int querys = 0;
    int lastQuerryCount = 0;
    //Allready checked relationships.
    Map<Person, List<Person>> checkedRelations = new HashMap<>();
    relationGraph.nodes().forEach(person -> checkedRelations.put(person, new ArrayList<>()));

    List<Person> candidates = new ArrayList<>();
    candidates.addAll(people.values());

    MutableGraph<Person> askedGraph = GraphBuilder.directed().build();
    //Copy over the persons
    relationGraph.nodes().forEach(askedGraph::addNode);

    Person person = candidates.get(0);

    debug("Finding candidate");
    while (candidates.size() > 1) {
      for (int i = 0; i < candidates.size(); i++) {
        Person testedPerson = candidates.get(i);
        if (testedPerson == person)
          continue;

        if (checkedRelations.get(person).contains(testedPerson))
          continue;

        //debug("Test relation between " + person + " and " + testedPerson);
        querys++;
        checkedRelations.get(person).add(testedPerson);
        if (relationGraph.hasEdgeConnecting(person, testedPerson)) {
          candidates.remove(person);
          askedGraph.putEdge(person, testedPerson);
          person = testedPerson;
          break;
        } else {
          candidates.remove(testedPerson);
        }
      }
    }

    Person lastCandidate = candidates.get(0);
    debug("Found candidate " + lastCandidate + " with " + querys + " querys.");
    info("Letzter Kandidat für den Superstar ist " + lastCandidate);

    lastQuerryCount = querys;
    debug("Check that the candidate doesnt follow anyone.");
    //Testing if the last candidate follows no one
    for (Person p : people.values()) {
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
    debug("Completed check with " + (querys - lastQuerryCount) + " querys");

    lastQuerryCount = querys;
    debug("Check that really everyone follows the candidate");

    for (Person p : people.values()) {
      if (p == lastCandidate)
        continue;

      if (checkedRelations.get(p).contains(lastCandidate))
        continue;


      //debug("Test relation between " + p + " and " + lastCandidate);
      querys++;
      checkedRelations.get(p).add(lastCandidate);
      if (!relationGraph.hasEdgeConnecting(p, lastCandidate)) {
        error(lastCandidate + " ist kein Superstar, da ihm nicht alle folgen.");
        return;
      } else {
        askedGraph.putEdge(p, lastCandidate);
      }
    }
    debug("Completed check with " + (querys - lastQuerryCount) + " querys");

    //Last check for safety
    int connnectionsToCandidate = askedGraph.inDegree(lastCandidate);
    if (connnectionsToCandidate != askedGraph.nodes().size() - 1) {
      error(lastCandidate + " ist kein Superstar, da ihm nicht alle folgen? (Unexpected Error)");
    }

    info(lastCandidate + " ist der SUPERSTAR der Gruppe.");
    info("Es wurden " + querys + " Anfragen benötigt. Kosten: " + querys + "€");
  }

  private static MutableGraph<Person> initialize(BufferedReader br) throws IOException {
    info("Loading persons...");
    StringTokenizer stringTokenizer = new StringTokenizer(br.readLine());
    int numberOfPeople = stringTokenizer.countTokens();

    info("Number of people=" + numberOfPeople);
    info("Creating graph...");
    MutableGraph<Person> relationGraph = GraphBuilder.directed().expectedNodeCount(numberOfPeople).build();

    while (stringTokenizer.hasMoreTokens()) {
      String name = stringTokenizer.nextToken();
      Person newPerson = new Person(name);
      people.put(name, newPerson);
      relationGraph.addNode(newPerson);
      debug("Created person '" + name + "'");
    }


    String currentLine;
    while (!Strings.isNullOrEmpty(currentLine = br.readLine())) {
      StringTokenizer relationTokens = new StringTokenizer(currentLine);
      String relationFromString = relationTokens.nextToken();
      String relationToString = relationTokens.nextToken();
      Person relationFromPerson = people.get(relationFromString);
      Person relationToPerson = people.get(relationToString);

      relationGraph.putEdge(relationFromPerson, relationToPerson);
    }

    info("Created graph with " + relationGraph.nodes().size() + " persons and " +
            relationGraph.edges().size() + " relationships");
    return relationGraph;
  }
}
