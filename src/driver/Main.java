/****************************************************************
 * Created by Jordan La Croix                                   *
 * Github: https://github.com/jellyjordan                       *
 * App's main purpose is to allow users to construct weighted   *
 * directed graphs to test out algorithms used in graph theory  *
 ****************************************************************/
package driver;

import algorithm.Kruskal;
import graphing.Edge;
import graphing.Graph;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        final Group edges = new Group();                    //edge layer
        final Group sliderGroup = new Group(edges);         //slider layer
        final Group vertices = new Group(sliderGroup);      //vertex layer, given priority over edges
        Scene viewPort = new Scene(vertices ,800 , 600);
        final Slider weightSlider = new Slider(1 , 10 , 5);
        weightSlider.setVisible(false);
        weightSlider.setMajorTickUnit(1);
        weightSlider.setSnapToTicks(true);
        weightSlider.setMinorTickCount(0);
        weightSlider.setShowTickMarks(true);

        /*
            Edge weight and thickness will update as the slider is moved.
            The slider will disapear when the mouse is released.
         */
        weightSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                Edge selectedEdge = (Edge) GraphManager.getSelectedReference();
                selectedEdge.setWeight((int) weightSlider.getValue());
            }
        });
        weightSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                weightSlider.setVisible(false);
                mouseEvent.consume();
            }
        });
        sliderGroup.getChildren().add(weightSlider);
        /*
            The scenes listener creates vertices where the user clicks if the area is clear,
            otherwise it will get the selected edge and allow weight adjustmusts or will
            produce error sound if area is occupied by a vertex
         */
        viewPort.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                switch(GraphManager.checkLocations(mouseEvent.getX() , mouseEvent.getY())){
                    case 0: //empty area
                            GraphManager.addVertex(mouseEvent.getX() , mouseEvent.getY());
                            vertices.getChildren().add(GraphManager.getSelectedReference());
                            break;
                    case 1: //vertex clicked
                            MediaPlayer mediaPlayer = new MediaPlayer(GraphManager.SOUND_DECLINE);
                            mediaPlayer.play();
                            break;
                    case 2: //edge clicked
                            Edge selectedEdge = (Edge) GraphManager.getSelectedReference();
                            weightSlider.setValue(selectedEdge.getWeight());
                            weightSlider.setVisible(true);
                            weightSlider.setTranslateX(mouseEvent.getX() - 60);
                            weightSlider.setTranslateY(mouseEvent.getY());
                            break;
                }
                    mouseEvent.consume();

            }
        });

        // Button initialization
        Button runButton = new Button("Create Tree");
        runButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Kruskal.kruskal(GraphManager.getGraph());
            }
        });

        vertices.getChildren().add(runButton);
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

}
