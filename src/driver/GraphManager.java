package driver;

import graphing.*;
import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Shape;

import java.io.File;


public class GraphManager {
    private static Graph graph = new Graph();
    private static Shape currentReference;
    public static final Media SOUND_ACCEPT = new Media(new File("res/snd_accept.mp3").toURI().toString());
    public static final Media SOUND_DECLINE = new Media(new File("res/snd_decline.mp3").toURI().toString());
    public static boolean newEdge = false;

    public static Shape getSelectedReference(){
        return currentReference;
    }

    /*
        Creates a new vertex and sets up the listeners which
        respond to drag and drop user actions.
     */
    public static void addVertex(double x , double y){
        currentReference = graph.addVertex(x , y);
        vertexInit();
    }

    /*
        The reference is updated when the user clicks an existing node
        or when the user creates a new node.
     */
    public static void setReference(Shape shape){
        currentReference = shape;
    }

    /*
        Checks (x , y) location for the presence of an edge
        or a vertex. The currentReference is updated to the
        node found.
     */
    public static boolean checkLocations(double x , double y){
        if(!graph.getVertices().isEmpty()){
            for(Vertex vertex : graph.getVertices()){
                if(vertex.getBoundsInParent().contains(x , y)){
                    currentReference = vertex;
                    return false;
                }
            }
            for(Edge edge : graph.getEdges()){
                if(edge.getBoundsInParent().contains(x , y)){
                    currentReference = edge;
                    return false;
                }
            }
        }
        return true;
    }

    /*
        Initializes all the listener events for the vertex
        that was just created
     */
    private static void vertexInit(){
        /*
            Sets up the source for the drag event so other vertices can
            communicate.
         */
        currentReference.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setReference((Shape)mouseEvent.getSource());
                Dragboard dragboard = currentReference.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(" ");     //garbage content just to fill clipboard
                dragboard.setContent(content);
                mouseEvent.consume();
            }
        });

        /*
            The target accepts the source only if they are not equal. This
            opens the other drag events to call handle.
         */
        currentReference.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getGestureSource() != dragEvent.getTarget()){
                    dragEvent.acceptTransferModes(TransferMode.ANY);
                }
                dragEvent.consume();
            }
        });

        /*
            Highlights the vertices to indicate to the user a
            link is possible between the two.
         */
        currentReference.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getGestureSource() != dragEvent.getTarget()){
                    Vertex vertex = (Vertex) dragEvent.getGestureSource();
                    vertex.setFill(Vertex.LINK_COLOR);
                    vertex = (Vertex) dragEvent.getTarget();
                    vertex.setFill(Vertex.LINK_COLOR);
                }
                dragEvent.consume();
            }

        });

        /*
            Reverts colors of vertices when the mouse leaves
            a potential linking vertex.
         */
        currentReference.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                Vertex vertex = (Vertex) dragEvent.getGestureSource();
                vertex.setFill(Vertex.DEFAULT_COLOR);
                vertex = (Vertex) dragEvent.getTarget();
                vertex.setFill(Vertex.DEFAULT_COLOR);
            }
        });

        /*
            Creates an edge when the mouse drop vertex differs from the sources.
            If the edge is added, it is updated to the currenent reference and
            flags the animation loop to display it
         */
        currentReference.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                MediaPlayer mediaPlayer;
                if(graph.addEdge((Vertex)dragEvent.getGestureSource() , (Vertex)dragEvent.getGestureTarget())){
                    newEdge = true;
                    setReference(graph.getEdges().get(graph.getEdges().size() - 1));
                    mediaPlayer = new MediaPlayer(SOUND_ACCEPT);
                }
                else{
                    mediaPlayer = new MediaPlayer(SOUND_DECLINE);
                }
                mediaPlayer.play();
            }
        });
    }

}
