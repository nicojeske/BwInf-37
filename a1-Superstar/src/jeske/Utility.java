package jeske;

import com.google.common.graph.Graph;
import de.graphml.writer.GraphWriter;
import de.graphml.writer.model.BaseGraph;
import de.graphml.writer.yed.YedEdge;
import de.graphml.writer.yed.YedKeys;
import de.graphml.writer.yed.YedNode;
import de.graphml.writer.yed.graphics.PolyLineEdge;
import de.graphml.writer.yed.graphics.ShapeNode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

class Utility {
  static void writeGraphML(Graph<Person> graph) throws FileNotFoundException {
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

    graph.edges().forEach(graphEdge -> graphWriter.edge(edge,
            graphWriter.getNextId(),
            graphEdge.source().getName(),
            graphEdge.target().getName())
    );

    graphWriter.endGraph(BaseGraph.DIRECTED);
    graphWriter.endDocument();
  }

  /**
   * Lässt den Anwender eine Eingabedatei auswählen und gibt diese Datei zurück.
   *
   * @param fileDescription Beschreibung der Datei
   * @param fileExtension   Gewünschte Dateiendung
   * @return vom User ausgwählte Datei
   * @throws IllegalArgumentException Der User hat keine Datei ausgewählt
   */
  static File getUserFile(String fileDescription, String fileExtension) {
    //Erstellung eines neuen Filechoosers
    JFileChooser chooser = new JFileChooser();

    //Limitiert Filechooser auf txt
    chooser.setFileFilter(new FileNameExtensionFilter(fileDescription, fileExtension));

    //Setzt den Filechooser auf den Ordner, aus dem die jar ausgeführt wurde
    chooser.setCurrentDirectory(new File("."));

    File file;

    int fileChooser = chooser.showOpenDialog(null); //Rückgabewert des Filechoosers

    if (fileChooser == 0) { //Eine Datei wurde im Filechooser ausgewählt
      file = chooser.getSelectedFile();
      return file; //Rückgabe des ausgewählten Verkehrsnetzes
    }

    return null;
  }
}
