package jeske;

import com.google.common.graph.Graph;
import de.graphml.writer.GraphWriter;
import de.graphml.writer.model.BaseGraph;
import de.graphml.writer.yed.YedEdge;
import de.graphml.writer.yed.YedKeys;
import de.graphml.writer.yed.YedNode;
import de.graphml.writer.yed.graphics.PolyLineEdge;
import de.graphml.writer.yed.graphics.ShapeNode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

public class Utility {
  public static void writeGraphML(Graph<Person> graph) throws FileNotFoundException {
    GraphWriter graphWriter = new GraphWriter(new FileOutputStream("graph.graphml"));
    graphWriter.startDocument();
    graphWriter.writeKeys(Arrays.asList(YedKeys.values()));

    graphWriter.startGraph(BaseGraph.DIRECTED);
    YedNode<ShapeNode> node = new YedNode<>(ShapeNode.asRectangle(0, 0, 150, 30, "Data"));

    graph.nodes().forEach(graphNode -> {
      node.nodeGraphics.firstLabel().text = graphNode.getName();
      graphWriter.node(node, graphNode.getName());
    });

    PolyLineEdge edgeGraphics = new PolyLineEdge();
    YedEdge<PolyLineEdge> edge = new YedEdge<>(edgeGraphics);

    graph.edges().forEach(graphEdge -> {
      graphWriter.edge(edge, graphWriter.getNextId(), graphEdge.source().getName(), graphEdge.target().getName());
    });

    graphWriter.endGraph(BaseGraph.DIRECTED);
    graphWriter.endDocument();
  }
}
