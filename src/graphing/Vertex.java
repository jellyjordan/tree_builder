package graphing;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
    Used in the Graph object to provide a node. GUI is also
    stored here
 */
public class Vertex extends Circle implements Comparable<Vertex>{
    public static final Color DEFAULT_COLOR = Color.LIGHTGRAY;
    public static final Color LINK_COLOR = Color.DODGERBLUE;
    public static final Color NO_LINK_COLOR = Color.RED;
    public static final Color STROKE_COLOR = Color.BLACK;
    private final int vertexID;

    /*
        Creates the node at the x and y coordinates and
        assigns an id to it provided by the Graph.
     */
    public Vertex(double x , double y , int vertexID){
        super(x , y , 15 , DEFAULT_COLOR);
        super.setStroke(STROKE_COLOR);
        super.setStrokeWidth(3);
        this.vertexID = vertexID;
    }

    public int getVertexID(){
        return vertexID;
    }

    /*
        Sorting is used when vertices are added and deleted
        in order to minimize ID generating times and also the
        generation of the adjacency matrix. ID's should never
        be equal!
     */
    @Override
    public int compareTo(Vertex vertex) {
        if(this.vertexID > vertex.getVertexID()){
            return 1;
        }
        else{
            return -1;
        }
    }
}
