package cn.edu.neu.trajectory.tree;

import java.io.*;
import java.util.*;

import cn.edu.neu.basic.error.SegmentError;
import cn.edu.neu.trajectory.construct.*;

/**
 * @author zhangph
 * @version v2.0 2015-12-10
 */
public class SortTree {

    private final static double PI = 3.14159265;


	private static double r = 0.995;
    private static int n = 0;
    private static Node root = new Node(null, 0);
    public static int treeHigh;
    public static double threshold = 10000;
    public static List<Node> listResult = new ArrayList<Node>();

    
    public static double getR() {
		return r;
	}

	public static void setR(double r) {
		SortTree.r = r;
	}

	public static int getN() {
		return n;
	}

	public static void setN(int n) {
		SortTree.n = n;
	}

	public static int getTreeHigh() {
		return treeHigh;
	}

	public static void setTreeHigh(int treeHigh) {
		SortTree.treeHigh = treeHigh;
	}
    
    
    public static void main(String[] args) {

        SortTree st = new SortTree();
        List<Point> points = new ArrayList<>();

        String file = "./data/1277_part.txt";
        points = input_file(file, 1);
//		String file = args[0];
//		r= Double.parseDouble(args[1]);
//		int flag = Integer.parseInt(args[2]);
// 		points = input_file(file, flag);

        long start = System.currentTimeMillis();

        Table[][] seg_table = new Table[n][n];
        seg_table = constructSegTable(points);
//		st.showSegTable(seg_table);
        System.out.println("seg_table over");

        List<Stack<Table>> stackList = new ArrayList<>();
        stackList = constructStack(seg_table, points);
        System.out.println("stack list over ");

        treeHigh = (int) (r * n);

        Node target = st.createTree(root, stackList, points, seg_table);

//		long end = System.currentTimeMillis();
        st.showTrajectory(target, r + file);

//		System.out.println((end - start));

        String file1 = "F:/program/gnuplot/st/first/test5/raw.plt";
        String file2 = "F:/program/gnuplot/st/first/test5/simp.plt";
        st.output_file(file1, points);
        st.output_file(file2, target);
    }

    private static void showStackList(List<Stack<Table>> stackList) {
        for (int i = 0; i < stackList.size(); i++) {
            @SuppressWarnings("unchecked")
            Stack<Table> stack = stackList.get(i);
            while (!stack.isEmpty()) {
                Table table = stack.pop();
                if (table != null) {
                    Point p1 = table.getSegment().get(0);
                    Point p2 = table.getSegment().get(1);
                    double error = table.getError();
                    System.out.printf("(%d, %d): %2.4f  ", p1.getIndex(), p2.getIndex(), error);
                }
            }
            System.out.println();
        }
    }

    private void showSegTable(Table[][] seg_table) {
        for (int i = 0; i < seg_table.length; i++) {
            for (int j = 0; j < seg_table[i].length; j++) {
                Table table = seg_table[i][j];
                if (table != null) {
                    List<Point> seg = table.getSegment();
                    double error = table.getError();
                    Point p1 = seg.get(0);
                    Point p2 = seg.get(1);
                    System.out.printf("(%d,%d):%2.4f    ", p1.getIndex(), p2.getIndex(), error);
                }
            }
            System.out.println();
        }
    }

    public static Table[][] constructSegTable(List<Point> points) {
        Table[][] seg_table = new Table[n][n];
        int num = points.size();
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                seg_table[i][j] = null;
            }
        }
        for (int i = 0; i < num - 1; i++) {
            Point p1 = points.get(i);
//			for (int j = i + 1; j < ((num * r + i + 1) < num ? (num *  r + i) : num ); j++) {
            for (int j = i + 1; j < ((num * (1 - r) + i + 1) < num ? (num * (1 - r) + i) : num); j++) {
//			for (int j = i + 1; j < num; j++) {
                Point p2 = points.get(j);
                if (j == i + 1) {
                    seg_table[i][j] = new Table(Arrays.asList(p1, p2), 2 * PI);
                } else {
                    seg_table[i][j] = new Table(Arrays.asList(p1, p2), SegmentError.computeError(p1, p2, points));
                }
            }
        }
        return seg_table;
    }

    public static List<Stack<Table>> constructStack(Table[][] seg_table, List<Point> points) {
        Runtime run = Runtime.getRuntime();
        long total1 = run.totalMemory();
        List<Stack<Table>> stackList = new ArrayList<>();
        int num = points.size();
        for (int i = 0; i < num - 1; i++) {
            Stack<Table> stack = new Stack<>();
            List<Table> list_table = new ArrayList<Table>();
//			for (int j = i + 1; j < ((num * r + i + 1) < num ? (num *  r + i) : num ); j++) {
            for (int j = i + 1; j < ((num * (1 - r) + i + 1) < num ? (num * (1 - r) + i) : num); j++) {
//			for (int j = i + 1; j < num; j++) {
                list_table.add(seg_table[i][j]);
            }
            Collections.sort(list_table, new Comparator<Table>() {
                public int compare(Table arg0, Table arg1) {
                    return Double.compare(arg1.getError(), arg0.getError());
                }
            });
            Iterator<Table> it = list_table.iterator();
            while (it.hasNext()) {
                stack.add(it.next());
            }
            stackList.add(stack);
        }
        long total = run.totalMemory();
//		System.out.println("total " + total/1024/1024);

        return stackList;
    }

    /**
     * sortTree method --> create the tree
     *
     * @param root
     * @return leaf node
     */
    public static Node createTree(Node root, List<Stack<Table>> stackList, List<Point> points, Table[][] seg_table) {
        System.out.println("tree high is " + treeHigh);
        Node target = null;
        double sum_error = 0;
        int high = 0;
        root.setStack(stackList.get(0));
        while (true) {
            if (!root.getStack().isEmpty() && threshold > sum_error) {
                Table node = root.getStack().peek();
                List<Point> seg = node.getSegment();
                double value = node.getError();
                int index = seg.get(1).getIndex();
                if (!seg.isEmpty()) {
                    Node child = null;
                    if (index < stackList.size()) {
                        child = new Node(root.getStack().pop().getSegment(), value, root, stackList.get(index));
                    } else {
                        child = new Node(root.getStack().pop().getSegment(), value, root);
                    }
                    high++;
                    sum_error += child.getError();

                    if (high < treeHigh && !isLastPoint(child.getSegment().get(1), points)) {
                        root = child;
                    } else {
                        if (high <= treeHigh && isLastPoint(child.getSegment().get(1), points)) {
                            threshold = sum_error;
                            target = child;
                        }
                        high--;
                        sum_error -= child.getError();
                    }
                }
            } else {
                if (root.getParent() == null) {
                    return target;
                } else {
                    Node child = root;
                    root = root.getParent();
                    child.setParent(root);
                    sum_error -= child.getError();
                    high--;
                }
            }
        }
    }


    /**
     * p is (not) last point
     *
     * @param Point p
     * @return boolean
     */
    public static boolean isLastPoint(Point p, List<Point> points) {
        if (p.getIndex() == points.get(n - 1).getIndex()) {
            return true;
        }
        return false;
    }


    /**
     * show a trajectory that is target trajectory
     *
     * @param child
     */
    public void showTrajectory(Node child, String info) {
        double error = 0;
        int count = 0;
        for (int i = 0; i < treeHigh; i++) {
            List<Point> list = new ArrayList<>();
            if (child != null && child.getSegment() != null) {
                count++;
                list = child.getSegment();
                //System.out.println(list.size());
                Point p1 = list.get(0);
                Point p2 = list.get(1);
                double value = child.getError();
                error += value;
//				System.out.println("[(" + p1.getIndex()+","+p2.getIndex() + "," + value +")] ");
                child = child.getParent();
            }
        }
        System.out.println(info + " count = " + (count + 1));
        System.out.println("error = " + error);

    }

    /**
     * @param file
     */
    public static List<Point> input_file(String file, int flag) {
        int index = 0;
        List<Point> points = new ArrayList<>();
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
                n = points.size();
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
                n = points.size();
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
        return points;
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

    /**
     * output the result into file
     *
     * @param file   is output file
     * @param result is the result trajectory
     */
    public static void output_file(String file, List<Point> result) {
        BufferedWriter bfw = null;
        try {
            bfw = new BufferedWriter(new FileWriter(new File(file)));
            for (int i = 0; i < result.size(); i++) {
                Point point = result.get(i);
                bfw.write(point.getX() + " " + point.getY() + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bfw.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void output_file(String file, Node child) {
        BufferedWriter bfw = null;
        try {
            bfw = new BufferedWriter(new FileWriter(new File(file)));
            double error = 0;
            List<Point> reverse = new ArrayList<>();
            for (int i = 0; i < treeHigh; i++) {
                List<Point> list = new ArrayList<>();
                if (child != null && child.getSegment() != null) {
                    list = child.getSegment();
                    Point p1 = list.get(0);
                    Point p2 = list.get(1);
                    double value = child.getError();
                    error += value;
                    reverse.add(p2);
                    reverse.add(p1);
                    child = child.getParent().getParent();
                }
            }
            for (int i = reverse.size() - 1; i > -1; i--) {
                Point p1 = reverse.get(i);
                String line1 = p1.getX() + " " + p1.getY();
                bfw.write(line1 + "\r\n");
            }


            bfw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bfw.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * print the result
     *
     * @param points is the trajectory's points
     */
    public static void print_point(List<Point> points) {
        Iterator<Point> it = points.iterator();
        while (it.hasNext()) {
            Point point = it.next();
            System.out.println("index = " + point.getIndex() + " , x = " + point.getX() + " , y = " + point.getY());
        }
    }
}
