package driver;

import static java.lang.Math.*;


public class GraphMath {

    /*
        Generates the the control vertices by making a (45 , 45 , 90)triangle
        using a simplified version of the Intersection of Two Circles method.

     */
    public static double[] getControlVertices(double xSource , double ySource , double xDestination , double yDestination){
        double height = 30;
        double controlX , controlY;
        /*
            The midpoints,and the edge distance are initialized for easy reading.
            Distance formula gets the distance between the source and destination.
         */
        double edgeDistance =sqrt(pow((xSource - xDestination), 2) + pow((ySource - yDestination) , 2));
        double baseX = (xSource + xDestination) / 2;
        double baseY = (ySource + yDestination) / 2;

        controlX = baseX + height * (yDestination - ySource) / edgeDistance;
        controlY = baseY + height * (xDestination - xSource) / edgeDistance;

        return new double[] {controlX , controlY};
    }
}
