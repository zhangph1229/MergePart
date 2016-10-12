package cn.edu.neu.basic.angular;


import java.math.*;

/**
 * compute the angular difference
 *
 * @author crane
 */
public class AngularDifference {
    final static double PI = 3.14159265;
    private double angular1, angular2;

    public double getAngular1() {
        return angular1;
    }

    public void setAngular1(double angular1) {
        this.angular1 = angular1;
    }

    public double getAngular2() {
        return angular2;
    }

    public void setAngular2(double angular2) {
        this.angular2 = angular2;
    }

    public AngularDifference() {
        // TODO Auto-generated constructor stub
    }

    public AngularDifference(double angular1, double angular2) {
        this.angular1 = angular1;
        this.angular2 = angular2;
    }

    /**
     * @param arg0 the first angular
     * @param arg1 the second angular
     * @return ad angular difference
     */
    public static double angularDifference(double arg0, double arg1) {
        double ad = arg1 - arg0;
        if (Math.abs(ad) < 2 * PI - Math.abs(ad)) {
            return Math.abs(ad);
        } else {
            return 2 * PI - Math.abs(ad);
        }

    }

    public static double angularDifference(double arg0, double arg1, int flag) {
        if (flag == 1) {
            return arg1;
        }
        double ad = arg1 - arg0;
        if (Math.abs(ad) < 2 * PI - Math.abs(ad)) {
            return Math.abs(ad);
        } else {
            return 2 * PI - Math.abs(ad);
        }

    }
}

