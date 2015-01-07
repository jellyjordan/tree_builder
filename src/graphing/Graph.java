package graphing;

import driver.GraphMath;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/*
    Graph object is used to store vertices and edges
    which are updated with user controls and manipulated
    using algorithms
 */
public class Graph {

    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;
    private int vertexGenID;         //is set to give an ID to newly created vertices

    public Graph(){
        vertexGenID = 0;
        edges = new ArrayList<Edge>();
        vertices = new ArrayList<Vertex>();
    }

    /*
        Creates a vertex at location which was clicked by the user
     */
    public Vertex addVertex(double xLoc , double yLoc){
        updateIdentifier();
        Vertex vertex = new Vertex(xLoc , yLoc , vertexGenID);
        vertices.add(vertex);
        return vertex;
    }
    /*
        Adds an edge from one vertex to another as long as
        they vertices do not already share an edge
     */
    public boolean addEdge(Vertex vertexSource , Vertex vertexDestination){
        //Check if edge exists
        for(Edge edge : edges){
            if(edge.getParentNode().equals(vertexSource) && edge.getDestinationNode().equals(vertexDestination)){
                return false;
            }
        }
        double[] controlPoints = GraphMath.getControlVertices(vertexSource.getCenterX() , vertexSource.getCenterY() ,
                                                              vertexDestination.getCenterX() , vertexDestination.getCenterY() );

        Edge edge = new Edge(vertexSource , vertexDestination , controlPoints[0] , controlPoints[1]);

        /*
            Adds edge references to the list in graph, but also to the
            list in the parent vertex in order look up edges based on the
            source node.
         */
        edges.add(edge);
        vertices.get(vertexSource.getVertexID()).getEdges().add(edge);
        return true;
    }

    /*
        Deletes a vertex along with edges which are connected
     */
    public void deleteVertex(Vertex vertex){
        vertices.remove(vertex);
    }

    /*
        Deletes an edge, does not change vertices it is
        connected to.
     */
    public void deleteEdge(Edge edge){
        edges.remove(edge);
    }

    public ArrayList<Vertex> getVertices(){
        return vertices;
    }

    public ArrayList<Edge> getEdges(){
        return edges;
    }

    /*
        Generates an adjacency matrix for algorithm usage using the edges
        and vertices created by the user
     */
    public double[][] getAdjacencyMatrix(){
        int matrixSize = vertices.size();
        double[][] matrix = new double[matrixSize][matrixSize];
        /*
            Sets all edges to infinity, unless the edge points to
            itself. After the initialization the array is updated with
            the weight of the edges which exist.
         */
        for(int i = 0; i < matrixSize; i++){
            for(int j = 0; j < matrixSize; j++){
                if(i == j){
                    matrix[i][j] = 0;
                }
                matrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for(Edge edge : edges){
            matrix[edge.getParentNode().getVertexID()]
                  [edge.getDestinationNode().getVertexID()] = edge.getWeight();

        }
        return matrix;
    }

    /*
        Checks each vertex with the expected value and prepares an
        ID which is to be assigned to the next vertex
     */
    private void updateIdentifier(){
        int expectedVal = 0;
        if(!vertices.isEmpty()){
            Collections.sort(vertices);
            for(Vertex vertex : vertices){
                if(vertex.getVertexID() != expectedVal){
                    vertexGenID = expectedVal;
                    return;
                }
                else{
                    expectedVal++;
                }
            }
        }
        vertexGenID = expectedVal;
        return;
    }
}
