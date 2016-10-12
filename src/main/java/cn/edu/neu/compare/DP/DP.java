package cn.edu.neu.compare.DP;
/**
 * The DP algorithm for the Min-Error problem.
 *
 * @author zhangph
 */

import java.util.*;

import cn.edu.neu.basic.error.SegmentError;
import cn.edu.neu.trajectory.construct.*;
import cn.edu.neu.trajectory.tree.SortTree;

public class DP {
    private static double MAX_VALUE = 10000;
    public static Node[][] table;
    public static int n;
    private static double r = 0.5;

    public static void main(String[] args) {
        Runtime run = Runtime.getRuntime();
        long start = System.currentTimeMillis();
        SortTree st = new SortTree();

        String file = "./data/littleData.txt";
//		String file = args[0];
//		r = Double.parseDouble(args[1]);
//		int index = Integer.parseInt(args[2]);
        List<Point> points = SortTree.input_file(file, 0);
        n = points.size();
        long total1 = run.totalMemory();

        table = computerError(points);
        DP dp = new DP();
//		dp.showTable(table);
        int w = (int) (n * r);
//		System.out.println("w = " + w );
        List<Point> res = dp.DP_Error(points, w);
        double error = dp.error(points, res);

        System.out.println("error = " + error);

    }

    public double error(List<Point> points, List<Point> res) {
        double error = 0;
        Set<Point> set = new HashSet<>();

        for (Iterator iterator = res.iterator(); iterator.hasNext(); ) {
            Point point = (Point) iterator.next();
            set.add(point);
        }
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Point p1 = (Point) iterator.next();
            Point p2 = null;
            if (iterator.hasNext())
                p2 = (Point) iterator.next();
            if (p2 != null)
                error += SegmentError.compError(p1, p2, points);
        }
        System.out.println(set.size());
        return error;
    }

    public List<Point> DP_Error(List<Point> points, int W) {
        List<Point> result = new ArrayList<>();
        int n = points.size();
        double min_error = MAX_VALUE;
        double error = 0;
        if (W == 2) {
            result.add(points.get(0));
            result.add(points.get(points.size() - 1));
        } else if (W >= n) {
            for (int i = 0; i < n; i++) {
                result.add(points.get(i));
            }
        } else {
            for (int k = 3; k <= W; k++) {
                for (int i = 1; i <= n - k; i++) {
                    for (int h = i + 1; h < n; h++) {
                        error = table[i][h].getError() > errorComp(h, k - 1, points) ? table[i][h].getError() : errorComp(h, k - 1, points);
                        if (error != 0 && error < min_error) {
                            min_error = error;
                            result.add(table[i][h].getSegment().get(0));
                            if (table[i][h].getSegment().get(1).getIndex() == n - 1) {
                                result.add(table[i][h].getSegment().get(1));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public double errorComp(int h, int w, List<Point> points) {
        double value = 0;
        Point point = points.get(h - 1);
        for (int j = h - 1; j < h; j++) {
            double error = 0;
            for (int i = j; i < j + 1; i++) {
                error = error > table[j][i].getError() ? error : table[j][i].getError();
            }
            if (value > error) {
                value = error;
            }
        }
        return value;
    }


    public static Node[][] computerError(List<Point> points) {
        Node table[][] = new Node[n][n];
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            int k = 0;
            for (int j = 0; j < points.size(); j++) {
                Point p2 = points.get(j);
                List<Point> key = new ArrayList<>();
                key.add(p1);
                key.add(p2);
                double error_tmp;
                if (i + 1 == j) error_tmp = 0;
                else error_tmp = SegmentError.computeError(p1, p2, points);
                table[i][k++] = new Node(key, error_tmp);
            }
        }
        //sort
        for (int i = 0; i < table.length; i++) {
            Node tmp_node = null;
            for (int j = table[i].length - 1; j > 0; j--) {
                Node node1 = table[i][j];
                if (node1 != null) {
                    double error1 = node1.getError();
                    for (int k = 0; k < j; k++) {
                        Node node2 = table[i][k];
                        if (node2 != null) {
                            double error2 = node2.getError();
                            if (error1 < error2) {
                                tmp_node = table[i][j];
                                table[i][j] = table[i][k];
                                table[i][k] = tmp_node;
                            }
                        }
                    }
                }
            }
        }
        return table;
    }

    public void showTable(Node[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                Node node = table[i][j];
                if (node != null) {
                    double error = node.getError();
                    List<Point> key = node.getSegment();
                    Point p1 = key.get(0);
                    Point p2 = key.get(1);
                    Stack<Table> stack = node.getStack();
                    System.out.print("[(" + p1.getIndex() + "," + p2.getIndex() + ")---" + table[i][j].getError());

                    System.out.print("]");
                }
            }
            System.out.println();
        }
    }
}
