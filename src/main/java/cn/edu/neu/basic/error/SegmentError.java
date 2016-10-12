package cn.edu.neu.basic.error;


import java.io.*;
import java.util.*;

import cn.edu.neu.basic.angular.*;
import cn.edu.neu.trajectory.construct.*;

/**
 * computer the segments' error for every point pair
 *
 * @author zhangph
 */
public abstract class SegmentError {
    //segment error
    /**
     * @param p1
     * @param p2
     * @param temp
     * @return
     */
    private final static double PI = 3.14159265;

    public static double compError(Point p1, Point p2, List<Point> temp) {
        if (p1.getIndex() == p2.getIndex()) {
            return 0;
        } else if (p1.getIndex() + 1 == p2.getIndex()) {
            return 2 * PI;
        }
        double error = 0;
        List<Double> list = new ArrayList<>();
        double arg0 = Angular.compAngular(p1, p2);
        //System.out.println(p1.getIndex()+","+p2.getIndex()+":"+arg0);
        //System.out.println("temp.size = "+temp.size());
        for (int i = 0; i < temp.size() - 1; i++) {
            double arg1 = Angular.compAngular(temp.get(i), temp.get(i + 1));
            //System.out.println(p1.getIndex()+","+p2.getIndex()+":"+arg1);
            list.add(AngularDifference.angularDifference(arg0, arg1));
        }

//		error = list.get(0).doubleValue();
        for (int i = 0; i < list.size(); i++) {
            error += list.get(i).doubleValue();
        }
        return error / list.size();
    }

    public static double computeError(Point p1, Point p2, List<Point> temp) {
        double error = 0;
        List<Double> list = new ArrayList<>();
        double ang1 = Angular.compAngular(p1, p2);
        int[] index = new int[2];
        index[0] = p1.getIndex();
        index[1] = p2.getIndex();
        double ang2 = 0;
        for (int i = index[0]; i < index[1]; i++) {
            Point point1 = temp.get(i);
            int j = i + 1;
            Point point2 = temp.get(j);
            ang2 = Angular.compAngular(point1, point2);
            list.add(AngularDifference.angularDifference(ang1, ang2));
        }

        for (int i = 0; i < list.size(); i++) {
            error += list.get(i);
        }

//		return error;
        return error / (p2.getIndex() - p1.getIndex());
    }

    public static double computeError(int k1, int k2, List<Point> temp) {
        double error = 0;
        List<Double> list = new ArrayList<>();
        Point p1 = temp.get(k1);
        Point p2 = temp.get(k2);
        double ang1 = Angular.compAngular(p1, p2);
        int[] index = new int[2];
        index[0] = p1.getIndex();
        index[1] = p2.getIndex();
        double ang2 = 0;
        for (int i = index[0] + 1; i < index[1]; i++) {
            Point point1 = temp.get(i);
            int j = i + 1;
            Point point2 = temp.get(j);
            ang2 = Angular.compAngular(point1, point2);
            list.add(AngularDifference.angularDifference(ang1, ang2));
        }

        if (list.size() > 0) {
            error = list.get(0).doubleValue();
            for (int i = 1; i < list.size(); i++) {
                if (error < list.get(i)) {
                    error = list.get(i);
                }
            }
        }

        return error;

    }

    public static double computeErrorAdvance(Point p1, Point p2, List<Point> temp) {
        double error = 0;
        List<Double> list = new ArrayList<>();
        double ang1 = Angular.compAngular(p1, p2);
        int[] index = new int[2];
        index[0] = p1.getIndex();
        index[1] = p2.getIndex();
        double ang2 = 0;
        for (int i = index[0]; i < index[1]; i++) {
            Point point1 = temp.get(i);
            int j = i + 1;
            Point point2 = temp.get(j);

            int flag = 0;
            if (i == index[0]) flag = 1;
            ang2 = Angular.compAngular(point1, point2);
            list.add(AngularDifference.angularDifference(ang1, ang2));
        }

        if (list.size() > 0) {
            error = list.get(0).doubleValue();
            for (int i = 1; i < list.size(); i++) {
                if (error < list.get(i)) {
                    error = list.get(i);
                }
            }
        }

        return error;

    }


    public static void main(String[] args) {
        List<Point> points = new ArrayList<Point>();
        BufferedReader bfr = null;
        String path = "./data/sample.txt";
        String line = null;
        String regex = ",";
        Map<List<Point>, Double> map = new HashMap<List<Point>, Double>();
        try {
            int i = 0;
            bfr = new BufferedReader(new FileReader(path));
            while ((line = bfr.readLine()) != null) {
                String[] data = line.split(regex);
                Point point = new Point(Double.parseDouble(data[0]), Double.parseDouble(data[1]), i++);
                points.add(point);
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Point p1 = new Point(0, 1, 0);
        Point p2 = new Point(2, 1, 2);
        double error = computeError(p1, p2, points);

        System.out.println(error);


    }
}
