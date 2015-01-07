package algorithm;

import graphing.*;
import java.util.ArrayList;
import java.util.Collections;

/*
    Kruskal's algorithm uses a greedy approach to create a minimum
    spanning tree from a graph. The steps are, sort edges by weight
    than select the edge with the least weight. If it does not create
    a cycle, keep it.
 */
public class Kruskal {

    public static void kruskal(Graph graph){
        ArrayList<Edge> edgesMarked = new ArrayList<Edge>();              // Tracks edges in tree, to update GUI
        int rootX , rootY;

        // Creates array size of the largest vertex ID in array which is the largest
        int[] vertices = new int[graph.getVertices().get(graph.getVertices().size() - 1).getVertexID() + 1];

        // Initialize the vertex array of disjoint sets
        for(int i = 0; i < vertices.length; i++){
            vertices[i] = -1;
        }

        // Sorts the graph's edges in non-decreasing order
        Collections.sort(graph.getEdges());

        // Repeat until there are enough edges to create a tree from all vertices
        Edge edge;
        for(int i = 0; i < graph.getEdges().size(); i++){
            edge = graph.getEdges().get(i);
            // Gets the set's root node for the edge vertices
            rootX = find(edge.getParentNode().getVertexID() , vertices);
            rootY = find(edge.getDestinationNode().getVertexID() , vertices);

            // Checks if the edge will create a cycle
            if(rootX != rootY){
                // Adds edge to the set
                union(rootX, rootY, vertices);
                // Solution check
                if(graph.getEdges().indexOf(edge) == graph.getVertices().size() - 1){
                    // When the solution is found, mark the remaining edges for deletion
                    for(i = i + 1; i < graph.getEdges().size(); i++){
                        edge = graph.getEdges().get(i);
                        edgesMarked.add(edge);
                    }
                    break;
                }
            }
            // Marks the edge for deletion if it creates a cycle
            else{
                edgesMarked.add(edge);
            }
        }
        updateGraphs(edgesMarked , graph.getEdges());
    }
    // Finds the root of any subset
    private static int find(int root , int[] vertices){
        // Node contains path to a parent node
        if(vertices[root] > -1){
            root = find(vertices[root] , vertices);
        }
        return root;
    }

    // Joins a smaller disjoint set to a larger disjoint set
    private static void union(int xIndex , int yIndex ,int[] vertices){
        // Y in the larger set
        if(vertices[xIndex] > vertices[yIndex]){
            vertices[yIndex] += vertices[xIndex];
            vertices[xIndex] = yIndex;
        }
        // X is the larger set or equal size
        else{
            vertices[xIndex] += vertices[yIndex];
            vertices[yIndex] = xIndex;
        }
    }

    /*
        Sets the edges marked for deletion invisible
        and removed their reference from the graph
     */
    private static void updateGraphs(ArrayList<Edge> delList , ArrayList<Edge> edges){
        for(Edge edge : delList){
            edge.setVisible(false);
            edges.remove(edge);
        }
    }
}
