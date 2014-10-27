package graphing;

import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;

/*
    Holds the weight of the connection between a source vertex
    and a destination vertex for directed graphs.
 */
public class Edge extends QuadCurve {
    public final static Color DEFAULT_COLOR = Color.BLACK;
    private final Vertex parentNode;
    private final Vertex destinationNode;
    private int weight;

    /*
        Initializes the edge to connect the vertex it was dragged from to
        the vertex it was dropped on. Edges will be made in a counter-clockwise
        curve from the source to the destination.
     */
    public Edge(Vertex source, Vertex destination , double controlX , double controlY){
        super(source.getCenterX() , source.getCenterY() , controlX , controlY , destination.getCenterX() , destination.getCenterY());
        super.setFill(Color.TRANSPARENT);
        super.setStroke(DEFAULT_COLOR);
        super.setStrokeWidth(3);
        parentNode = source;
        destinationNode = destination;
        weight = 1;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight(){
        return weight;
    }

    public Vertex getParentNode(){
        return parentNode;
    }

    public Vertex getDestinationNode(){
        return destinationNode;
    }
}
