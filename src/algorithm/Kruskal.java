package algorithm;

import graphing.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jelly on 10/31/14.
 */
public class Kruskal {

    public static void kruskal(Graph graph){
        ArrayList<Edge> edgesUsed = new ArrayList<Edge>();              // Tracks edges in tree, to update GUI
        int rootX , rootY;

        // Creates array size of the largest vertex ID in array which is the largest
        int[] vertices = new int[graph.getVertices().get(graph.getVertices().size() - 1).getVertexID() + 1];

        // Initialize the vertex array of disjoint sets
        for(int i = 0; i <= vertices.length; i++){
            vertices[i] = -1;
        }

        // Sorts the graph's edges in non-decreasing order
        Collections.sort(graph.getEdges());

        // Repeat until there are enough edges to create a tree from all vertices
        for(Edge edge : graph.getEdges()){
            // Gets the set's root node for the edge vertices
            rootX = find(edge.getParentNode().getVertexID() , vertices);
            rootY = find(edge.getDestinationNode().getVertexID() , vertices);

            // Checks if the edge will create a cycle
            if(rootX != rootY){
                // Adds edge to the set
                edgesUsed.add(edge);
                union(rootX , rootY , vertices);
                // Solution check
                if(edgesUsed.size() == graph.getEdges().size() - 1){
                    break;
                }
            }
        }
    }
    // Finds the root of any subset, and updates the path
    private static int find(int x , int[] vertices){
        int root = x;
        // Not a root
        if(vertices[x] >= 0){
            root = find(vertices[x] , vertices);
        }
        return root;
    }

    // Joins a smaller disjoint set to a larger disjoint set
    private static void union(int rootX , int rootY ,int[] vertices){
        // Y in the larger set
        if(vertices[rootX] > vertices[rootY]){
            vertices[rootX] = rootY;
            vertices[rootY] += vertices[rootX];
        }
        // X is the larger set
        else{
            vertices[rootY] = rootX;
            vertices[rootX] += vertices[rootY];
        }
    }
}
