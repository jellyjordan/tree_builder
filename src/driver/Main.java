/****************************************************************
 * Created by Jordan La Croix                                   *
 * Github: https://github.com/jellyjordan                       *
 * App's main purpose is to allow users to construct weighted   *
 * directed graphs to test out algorithms used in graph theory  *
 ****************************************************************/
package driver;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        final Group edges = new Group();             //edge layer
        final Group vertices = new Group(edges);     //vertex layer, given priority over edges
        Scene viewPort = new Scene(vertices ,800 , 600);

        /*
            The scenes listener creates vertices where the user clicks
            as long as the location is not occupied by any other node bounds
         */
        viewPort.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(GraphManager.checkLocations(mouseEvent.getX() , mouseEvent.getY())){
                    GraphManager.addVertex(mouseEvent.getX() , mouseEvent.getY());
                    vertices.getChildren().add(GraphManager.getSelectedReference());
                }
                else{
                    MediaPlayer mediaPlayer = new MediaPlayer(GraphManager.SOUND_DECLINE);
                    mediaPlayer.play();
                }
                mouseEvent.consume();
            }
        });

        primaryStage.setTitle("Graph Builder");
        primaryStage.setScene(viewPort);
        primaryStage.show();

        /*
            This main loop is important. It updates the gui as the graph
            updates from the user's actions.
         */
        new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if(GraphManager.newEdge){
                    GraphManager.newEdge = false;
                    edges.getChildren().add(GraphManager.getSelectedReference());
                }
            }
        }.start();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void createEdgePrompt(){

    }
}
