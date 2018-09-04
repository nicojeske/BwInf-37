package jeske;

import com.esotericsoftware.minlog.Log;
import com.google.common.base.Strings;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class Main {

  private static final String TAG = "Main";
  private static Map<String, Person> people = new HashMap<>();

  public static void main(String[] args) throws IOException {
    Log.set(Log.LEVEL_DEBUG);

    BufferedReader br = new BufferedReader(new InputStreamReader(
            Main.class.getResourceAsStream("/beispieldaten/superstar3.txt")
    ));

    Log.info(TAG, "Loaded people");
    StringTokenizer stringTokenizer = new StringTokenizer(br.readLine());
    int numberOfPeople = stringTokenizer.countTokens();

    Log.info(TAG, "Number of People=" + numberOfPeople);
    Log.info(TAG, "Creating graph");
    MutableGraph<Person> relationGraph = GraphBuilder.directed().expectedNodeCount(numberOfPeople).build();

    while (stringTokenizer.hasMoreTokens()) {
      String name = stringTokenizer.nextToken();
      Person newPerson = new Person(name);
      people.put(name, newPerson);
      relationGraph.addNode(newPerson);
      Log.debug(TAG, "Created person '" + name + "'");
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

    Utility.writeGraphML(relationGraph);


    }
}
