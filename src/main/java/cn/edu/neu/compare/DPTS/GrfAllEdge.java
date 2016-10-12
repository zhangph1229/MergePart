package cn.edu.neu.compare.DPTS;

import java.util.*;

import cn.edu.neu.basic.error.SegmentError;
import cn.edu.neu.trajectory.construct.*;
import cn.edu.neu.trajectory.tree.SortTree;

/**
 * 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋潈閿熺潾浼欐嫹鍥�br/>
 * 瀵婚敓鏂ゆ嫹閿熸枻鎷锋剷鏂ゆ嫹鐩忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹璺敓鏂ゆ嫹
 */
public class GrfAllEdge {

    // 鍥鹃敓渚ヨ鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
    private int total;
    // 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓杈冿拷
    private int[] nodes;
    // 鍥鹃敓鏂ゆ嫹閿熻妭鎺ユ拝鎷烽敓鏂ゆ嫹
    private int[][] matirx;
    private static int tid = 0;
    private static double max = 0.0;
    public static List<Point> list_points = new ArrayList<>();
    public static List<Trajectory> tra_list = new ArrayList<>();
    public static List<Stack<Integer>> list = new ArrayList<>();

    private static int W = 50;            //閿熸磥褰撻敓鏂ゆ嫹 1-r
    private double min_error = 0.05;    //閿熸枻鎷疯閿熸枻鎷烽敓鏂ゆ嫹

    public static double[][] table;

    public static void main(String[] args) {
        Runtime run = Runtime.getRuntime();
        String file = "./data/1000.plt";
        SortTree st = new SortTree();
        list_points = st.input_file(file, 0);


//		st.print_point(list_points);
        table = GrfAllEdge.CE(list_points);
//		showTable(table);

        int[] nodes = null;
        nodes = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            nodes[i] = i;
        }
        GrfAllEdge grf = new GrfAllEdge(table.length, nodes);
        grf.initGrf(table);
//		grf.printMatrix();
        long start = System.currentTimeMillis();
        System.out.print("\n------ 瀵婚敓鏂ゆ嫹閿熸枻鎷锋剷鏂ゆ嫹鐩忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹璺敓鏂ゆ嫹閿熸枻鎷峰 ------\n");
        grf.resetVisited();
        int origin = 0;
        int goal = list_points.size() - 1;
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(origin);
        grf.dfsStack(-1, goal, stack);
        System.out.println("\n------ 瀵婚敓鏂ゆ嫹閿熸枻鎷锋剷鏂ゆ嫹鐩忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹璺敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹 ------");
        System.out.println(list.size());

        long end = System.currentTimeMillis();
        System.out.println("running time is " + (end - start) + "ms");

        long total = run.totalMemory();
        System.out.println("runtime = " + total);

    }

    public static void showTable(double[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static double[][] CE(List<Point> points) {
        int n = points.size();
        double table[][] = new double[n][n];
        int num = points.size();
        for (int i = 0; i < num - 1; i++) {
            Point p1 = points.get(i);
            int k = 0;
            int len = 0;
            for (int j = 0; j <= i; j++) {
                table[i][k++] = 0;
            }
            for (int j = i + 1; j < n; j++) {
                Point p2 = points.get(j);
                List<Point> key = new ArrayList<>();
                key.add(p1);
                key.add(p2);
                double error_tmp = SegmentError.computeError(p1, p2, points);
                table[i][k++] = error_tmp;
            }
        }
        return table;
    }

    /**
     * 瀵婚敓鏂ゆ嫹閿熸枻鎷锋剷鏂ゆ嫹鐩忛敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹璺敓鏂ゆ嫹
     *
     * @param underTop
     * 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋爤閿熸枻鎷烽敓鏂ゆ嫹閿熼摪杈圭鎷峰厓閿熸枻鎷�
     * @param goal
     * 鐩敓鏂ゆ嫹
     * @param stack
     */
    int count = 0;

    private void dfsStack(int underTop, int goal, Stack<Integer> stack) {
        if (stack.isEmpty() || count > 10000) {
            return;
        }
        // 閿熸枻鎷烽敓鏂ゆ嫹鏍堥敓鏂ゆ嫹鍏冮敓鎴綇鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
        int k = stack.peek().intValue();
        if (k == goal) {
            System.out.print("\n閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷风洀鎮翠紮鎷烽敓鏂ゆ嫹閿熼叺锟");
            return;
        }
        // 閿熸枻鎷锋爤閿熸枻鎷烽敓鏂ゆ嫹閿熻妭鎺ョ鎷烽敓鏂ゆ嫹閿熻娇閫掔櫢鎷烽敓鏂ゆ嫹鑼敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熼ズ鎲嬫嫹閿熸枻鎷�
        for (int i = k + 1; i < this.total; i++) {
            // 閿熷彨杈癸綇鎷烽敓鏂ゆ嫹閿熸彮璇ф嫹閿熸枻鎷烽敓鏂ゆ嫹閿熻緝纰夋嫹閿熸枻鎷烽敓閾扮鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
            if (this.matirx[k][i] == 1) {
                if (min_error < table[k][i] || stack.size() > W - 1) {
                    break;
                }
                // 閿熸枻鎷峰嵃璺敓鏂ゆ嫹
                if (i == goal) {
                    stack.push(goal);
                    Iterator<Integer> it = stack.iterator();
                    while (it.hasNext()) {
                        System.out.println("test " + it.next());
                    }
                    System.out.println("--------trajectory " + count++ + "-----------");

//					list.add(stack);
                    stack.pop();
                    continue;
                }
                // 閿熸枻鎷风己閿熸枻鎷烽敓锟�                stack.push(i);
                dfsStack(k, goal, stack);
            }
        }
        stack.pop();
    }


    public GrfAllEdge(int total, int[] nodes) {
        this.total = total;
        this.nodes = nodes;
        this.matirx = new int[total][total];
    }


    public Trajectory calTrajectory(Stack<Integer> stack, int k, int tid) {

        List<Point> list_p = new ArrayList<>();

        for (Integer i : stack) {
            Point p1 = list_points.get(this.nodes[i]);
            Point p2 = list_points.get(this.nodes[i + 1]);

            list_p.add(p1);
            list_p.add(p2);
            /*if(list_p.size()>W-1){
				break;
			}*/

            double error = SegmentError.computeErrorAdvance(p1, p2, list_points);
            System.out.println(error);
            if (max < error) {
                max = error;
            }
        }

        Point p3 = list_points.get(this.nodes[stack.peek().intValue()]);
        Point p4 = list_points.get(this.nodes[k]);

        double err = SegmentError.compError(p3, p4, list_points);
        if (max < err) {
            max = err;
        }

        Trajectory tra = new Trajectory(tid, max, list_p);
		/*System.out.println(tra.getTid());
		System.out.println(tra.getLis());
		System.out.println(tra.getError());*/
        return tra;
    }


    private void printMatrix() {
        System.out.println("----------------- matrix -----------------");
        for (int i = 0; i < this.total; i++) {
            System.out.print(" " + this.nodes[i] + "|");
            for (int j = 0; j < this.total; j++) {
                System.out.print(this.matirx[i][j] + "-");
            }
            System.out.print("\n");
        }
        System.out.println("----------------- matrix -----------------");
    }

    // 閿熸枻鎷烽敓鏂ゆ嫹[i][i]浣嶉敓鐭揪鎷烽敓鏂ゆ嫹鍏冮敓鏂ゆ嫹鍊间负0閿熸枻鎷�閿熸枻鎷风ず鍥鹃敓鍙殑璁规嫹閿熸枻鎷穒鏈敓鏂ゆ嫹閿熸枻鎷烽敓缁烇綇鎷�閿熸枻鎷风ず鍥鹃敓鍙殑璁规嫹閿熸枻鎷穒閿熺獤鎲嬫嫹閿熸枻鎷烽敓鏂ゆ嫹
    private void resetVisited() {
        for (int i = 0; i < this.total; i++) {
            this.matirx[i][i] = 0;
        }
    }

    // 閿熸枻鎷峰閿熸枻鎷峰浘閿熸枻鎷烽敓鏂ゆ嫹
    private void initGrf(double[][] table) {
        int j = 1;
        for (int i = 0; i < table.length; i++) {
            for (j = i + 1; (j < table[i].length); j++) {
                if (min_error > SegmentError.computeError(i, j, list_points)) {
                    this.matirx[i][j] = 1;
                }
            }
        }
    }

}
