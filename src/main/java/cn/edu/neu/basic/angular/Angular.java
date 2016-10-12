package cn.edu.neu.basic.angular;


import java.math.*;

import cn.edu.neu.trajectory.construct.*;


public class Angular {
    final private static double PI = 3.14159265;

    /**
     * to compute the angular of A,B
     *
     * @param point1,point2 --> Point
     */

    public static double compAngular(Point point1, Point point2) {
        if (point1.getIndex() == point2.getIndex()) {
            return 0;
        }
        double x1 = point1.getX();
        double y1 = point1.getY();
        double x2 = point2.getX();
        double y2 = point2.getY();
        double angular = 0;
        double tan = 0;
        if (y2 - y1 > 0 && x2 - x1 > 0) {
            tan = (y2 - y1) / (x2 - x1);
            angular = Math.atan(tan);
        } else if (y2 - y1 < 0 && x2 - x1 > 0) {
            tan = -(y2 - y1) / (x2 - x1);
            angular = 2 * PI - Math.atan(tan);
        } else if (y2 - y1 > 0 && x2 - x1 < 0) {
            tan = -(y2 - y1) / (x2 - x1);
            angular = PI - Math.atan(tan);
        } else if (y2 - y1 < 0 && x2 - x1 < 0) {
            tan = (y2 - y1) / (x2 - x1);
            angular = PI + Math.atan(tan);
        }
        return angular;
    }

    public static double compAngular(double x1, double y1, double x2, double y2) {
        double angular = 0;
        double tan = 0;
        if (y2 - y1 > 0 && x2 - x1 > 0) {
            tan = (y2 - y1) / (x2 - x1);
            angular = Math.atan(tan);
        } else if (y2 - y1 < 0 && x2 - x1 > 0) {
            tan = -(y2 - y1) / (x2 - x1);
            angular = 2 * PI - Math.atan(tan);
        } else if (y2 - y1 > 0 && x2 - x1 < 0) {
            tan = -(y2 - y1) / (x2 - x1);
            angular = PI - Math.atan(tan);
        } else if (y2 - y1 < 0 && x2 - x1 < 0) {
            tan = (y2 - y1) / (x2 - x1);
            angular = PI + Math.atan(tan);
        }
        return angular;
    }
}