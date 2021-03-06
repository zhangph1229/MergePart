package cn.edu.neu.trajectory.incremental;

import java.io.*;
import java.util.*;

import cn.edu.neu.basic.error.SegmentError;
import cn.edu.neu.trajectory.construct.*;


/**
 * �����Ĺ켣����ʵ��
 *
 * @author zhangph
 * @version v 1.0 2015-10-13
 */
public class Incremental {
    private static double rate = 0.3;  //rate
    public static List<Point> points = new ArrayList<>();  //trajectory
    public static List<Point> removePoints = new ArrayList<>(); // removed trajectory points
    public Map<List<Point>, Double> table = new HashMap<>(); // | segment | error |
    private static int index = 0;

    public static void main(String[] args) {
        Runtime run = Runtime.getRuntime();
        Incremental inc = new Incremental();
        long total1 = run.totalMemory();

        String file1 = "./data/1277_part.txt";
        inc.input_file(file1, 1);

//		String file1 = args[0];
//		rate = Double.parseDouble(args[1]);
//		int flag = Integer.parseInt(args[2]);
//		inc.input_file(file1, flag);
        //inc.print_point(points);

        List<Point> result = inc.incremental_method();

        System.out.println("size = " + result.size());

        if (result != null)
            inc.showError(result);
//		inc.print_point(result, points);

        String file3 = "F:/program/gnuplot/st/first/test5/raw.plt";
        String file2 = "F:/program/gnuplot/st/first/test5/simp.plt";
        inc.output_file(file3, points);
        inc.output_file(file2, result);

//		long total = run.totalMemory();
//		System.out.println("memory = " + (total - total1)/1024/1024);
    }


    public void showError(List<Point> list_point) {
        double tmp = 0;
        int count = 0;
        for (int i = 0; i < list_point.size() - 1; i++) {
            count++;
            Point p1 = list_point.get(i);
            Point p2 = list_point.get(i + 1);
            double error;
            if (p1.getIndex() + 1 == p2.getIndex()) {
                error = 2 * 3.14159;
            } else {
                error = SegmentError.compError(p1, p2, points);
//				System.out.println(error);
            }
            tmp += error;
        }
        System.out.println("error = " + tmp / count);
    }

    //incremental trajectory simplification method

    /**
     * @return List<Point> result
     */
    public List<Point> incremental_method() {
        List<Point> points_tmp = new ArrayList<>();
        int counter = 0;
        int numVisited = 0;
        int numKeep = 0;

        long start = System.currentTimeMillis();

        for (int i = 0; i < points.size(); i++) {
            points_tmp.add(points.get(i));
            //System.out.println("index = " + points.get(i).getIndex());
            counter++;
            numVisited++;
            if (points_tmp.size() > 2) {
                for (int j = i - 1; j < points_tmp.size() - 1; j++) {
                    Point p1 = points_tmp.get(j);
                    Point p2 = points_tmp.get(j + 1);
                    List<Point> key = new ArrayList<>();
                    key.add(p1);
                    key.add(p2);
                    double value = SegmentError.compError(p1, p2, points_tmp);
                    table.put(key, value);
                }
                numKeep = (int) (numVisited * rate);
                if (numKeep < counter) {
                    Point p = trajectoryError(table);
                    counter--;
                    removePoints.add(p);
                }
            }
        }
        long end = System.currentTimeMillis();
        long run_time = end - start;
        System.out.println("this porgram running time is " + run_time + "ms");

        Iterator it = points_tmp.iterator();
        List<Point> point_r = new ArrayList<>();
        while (it.hasNext()) {
            Point point = (Point) it.next();
            if (!(removePoints.contains(point))) {
                point_r.add(point);
            }
        }
        return point_r;
    }

    public List<Point> incremental_method_modify() {
        List<Point> points_tmp = new ArrayList<>();
        List<Point> points_tra = null;
        int counter = 0;
        int numVisited = 0;
        int numKeep = 0;

        for (int i = 0; i < points.size(); i++) {
            points_tmp.add(points.get(i));
            numVisited++;

            if (points_tmp.size() > 2) {
                numKeep = (int) ((1 - rate) * numVisited);
                if (points_tmp.size() < numKeep) {
                    points_tra = points_tmp;
                    Point p1 = points_tmp.get(0);
                    double tmp = 0;
                    int flag = -1;
                    for (int j = 1; j < points_tmp.size(); j++) {
                        Point p2 = points_tmp.get(j);
                        double error = SegmentError.computeError(p1, p2, points_tra);
                        if (tmp < error) {
                            tmp = error;
                            flag = j;
                        }
                    }

                    points_tra.remove(flag);
                    points_tmp = points_tra;
                }
            }

        }

        return points_tmp;
    }

    //compute the trajectory's error

    /**
     * @param table is | segment | error |
     * @return max error point
     */
    public Point trajectoryError(Map<List<Point>, Double> table) {
        Point maxError = null;
        Iterator it = table.entrySet().iterator();
        List<Point> maxPoint = new ArrayList<>();
        double max = 0;
        while (it.hasNext()) {
            Map.Entry<List<Point>, Double> m = (Map.Entry) it.next();
            List<Point> point = m.getKey();
            double value = m.getValue();
            if (value > max) {
                max = value;
                maxError = point.get(1);
                maxPoint = point;
            }
        }
        if (table.containsKey(maxPoint)) {
            //System.out.println("before remove the table " + table.size());
            table.remove(maxPoint);
            //System.out.println("table remove successful!"+table.size());
        }
        return maxError;
    }


    @Deprecated
    /**
     *
     * @param num
     * @param table
     * @return
     */
    public Point trajectoryError(int num, Map<List<Point>, Double> table) {
        Point point_max = null;
        Iterator<Point> it = points.iterator();
        double max = 0;
        while (it.hasNext()) {
            Point it_point = it.next();
            double value = recursive(it_point, table, 0);
            if (max < value) {
                max = value;
                point_max = it_point;
            }
        }
        return point_max;
    }

    @Deprecated
    /**
     *
     * @param point
     * @param table2
     * @param sum
     * @return
     */
    public static double recursive(Point point, Map<List<Point>, Double> table2, double sum) {
        Iterator it = table2.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<List<Point>, Double> map = (Map.Entry<List<Point>, Double>) it.next();
            List<Point> key = map.getKey();
            double value = map.getValue();
            if (point.getIndex() != points.size() - 1) {
                if (point.getIndex() == key.get(0).getIndex())
                    recursive(key.get(1), table2, sum + value);
            }
        }
        return sum;
    }
    //load the trajectory file

    /**
     * @param file the input trajectory
     */
    public void input_file(String file, int flag) {
        int index = 0;
        BufferedReader bfr = null;
        if (flag == 0) {
            try {

                //jump the head
                bfr = new BufferedReader(new FileReader(file));

                for (int i = 0; i < 6; i++) {
                    bfr.readLine();
                }

                String line = null;
                String regex = ",";
                while ((line = bfr.readLine()) != null) {
                    String[] data = line.split(regex);
                    double x = Double.parseDouble(data[0]);
                    double y = Double.parseDouble(data[1]);
                    Point point = new Point(x, y, index++);
                    points.add(point);
                }
//				n = points.size();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bfr.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } else if (flag == 1) {
            try {
                bfr = new BufferedReader(new FileReader(file));
                String line = null;
                String regex = ",";
                while ((line = bfr.readLine()) != null) {
                    String[] data = line.split(regex);
                    double x = Double.parseDouble(data[data.length - 2]);
                    double y = Double.parseDouble(data[data.length - 1]);
                    Point point = new Point(x, y, index++);
                    points.add(point);
                }
//				n = points.size();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bfr.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

    }

    public void output_file(String file1, String file2) {
        BufferedReader bfr = null;
        BufferedWriter bfw = null;
        Set<String> set = new HashSet<>();
        try {
            bfr = new BufferedReader(new FileReader(new File(file1)));
            bfw = new BufferedWriter(new FileWriter(new File(file2)));

            String line = null;
            String regex = ",";
            while ((line = bfr.readLine()) != null) {
                set.add(line);
            }
            for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
                String string = (String) iterator.next();
                StringTokenizer stk = new StringTokenizer(string, regex);
                for (int i = 0; i < stk.countTokens() - 1; i++) {
                    stk.nextToken();
                }
                bfw.write(stk.nextToken() + " " + stk.nextToken() + "\r\n");
            }
            bfw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bfr.close();
                bfw.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void output_file(String file, List<Point> points) {
        BufferedWriter bfw = null;
        try {
            bfw = new BufferedWriter(new FileWriter(file));

            for (Iterator iterator = points.iterator(); iterator.hasNext(); ) {
                Point point = (Point) iterator.next();
                bfw.write(point.getX() + " " + point.getY() + "\r\n");

            }
            bfw.flush();
            bfw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // print the result

    /**
     * @param points is the trajectory's points
     */
    public void print_point(List<Point> result, List<Point> points) {
//		Iterator<Point> it = result.iterator();
//		while(it.hasNext()){
//			Point point = it.next();
//			System.out.println("index = " + point.getIndex());
////			System.out.println("index = " + point.getIndex() + " , x = " + point.getX() + " , y = " + point.getY());
//		}

        for (int i = 0; i < result.size() - 1; i++) {
            Point p1 = result.get(i);
            Point p2 = result.get(i + 1);
            double value = SegmentError.compError(p1, p2, points);
            System.out.println("[" + p1.getIndex() + "," + p2.getIndex() + "] " + value);
        }

    }
}

