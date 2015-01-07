package algorithm;

import graphing.Edge;
import graphing.Graph;
import graphing.Vertex;
import org.python.util.PythonInterpreter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

/*
    Traveling Salesman Problem constructrs
    an optimal tour.
 */
public class TSP {
    private static final String TSP_PYTHON_PATH = "src/algorithm/tsp.py";

    public static void tour(Graph graph){

        // Intrepeter used to execute python script
        PythonInterpreter interpreter = new PythonInterpreter();

        /*
            Sets the keyword javaGraph for the interpreter to represent the
            convereted Java graph object.
         */
        interpreter.set("javaGraph" , graph);

        /*
            Sets up the output stream to capture the result of
            the interpreter's execution
         */
        StringWriter output = new StringWriter();
        interpreter.setOut(output);
        interpreter.execfile(TSP_PYTHON_PATH);

        /*
            Uses the output from the interpreter to
            get the results in a String format which is
            parsed
         */
        ArrayList<Integer> path =parseResult(output.toString());
        updatePath(path , graph);
    }
    /*
        Removes the edges from the graph using the source vertex
        to check all edges with non matching destinations, because the tour
        will only produce 1 outgoing edge per vertex
     */
    private static void updatePath(ArrayList<Integer> path , Graph graph){
        for(int pathIndex = 0; pathIndex < path.size() - 1; pathIndex++){
            // Get the source of the vertex using the path value
            int sourceVertexID = path.get(pathIndex);
            Vertex source = graph.getVertices().get(sourceVertexID);

            // Get the destination of the vertex using the path value
            int destinationVertexID = path.get(pathIndex + 1);
            Vertex destination = graph.getVertices().get(destinationVertexID);

            Iterator<Edge> edgeIterator = source.getEdges().iterator();
            while (edgeIterator.hasNext()){
                Edge nextEdge = edgeIterator.next();
                if(!nextEdge.getDestinationNode().equals(destination)){
                    nextEdge.setVisible(false);
                    graph.getEdges().remove(nextEdge);
                    edgeIterator.remove();
                }
            }
        }
    }

    /*
        Parses the returned string into a series of nodes which represent the path
        of the tour.
        Expected Format: "[#, #, #,]"
     */
    private static ArrayList<Integer> parseResult(String pathString){
        // Ordered path of the tour, each index represents the vertexID
        ArrayList<Integer> path = new ArrayList<Integer>();

        String vertexID = new String();
        for(int i = 1; i < pathString.length(); i++){
            // Append the number characters
            if(pathString.charAt(i) >= '0' && pathString.charAt(i) <= '9'){
                vertexID += pathString.charAt(i);
            }
            // End of number , add and restart
            else{
                path.add(Integer.parseInt(vertexID));
                vertexID = new String();
                i++;
            }
        }
        return path;
    }
}
