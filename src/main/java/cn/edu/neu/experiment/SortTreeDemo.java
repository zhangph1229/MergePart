package cn.edu.neu.experiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.edu.neu.trajectory.construct.*;
import cn.edu.neu.trajectory.tree.SortTree;


public class SortTreeDemo {
//	private final static double PI = 3.14159265;
//	private static double r = 0.1;
//	private static int n = 0;
	private static Node root = new Node(null, 0);
//	public static int treeHigh;
//	public static double threshold = 10000;
	public static List<Node> listResult = new ArrayList<Node>();

	public static void main(String[] args) {
//		String file = args[0];
//		r= Double.parseDouble(args[1]);
		String file = "F:/program/java/MergePart/t-drive-10.txt";
		SortTree.treeHigh = (int) (SortTree.getR() * SortTree.getN());
		BufferedReader allData = null;
		String line = null;
		int flag = 0;
		int index = 0;
		List<Point> points = new ArrayList<>();
		int count = 0;
		try {
			allData = new BufferedReader(new FileReader(file));
			while ((line = allData.readLine()) != null) {
				String[] res = line.split(",");
				int tra = Integer.parseInt(res[0]);
				if (flag == tra) {
					double x = Double.parseDouble(res[1]);
					double y = Double.parseDouble(res[2]);
					points.add(new Point(x, y, index++));
					count++;
				} else {
					int n = count;
					Table[][] seg_table = new Table[n][n];
					List<Stack<Table>> stackList = new ArrayList<>();
					seg_table = SortTree.constructSegTable(points);
					stackList = SortTree.constructStack(seg_table, points);
					Node target = SortTree.createTree(root, stackList, points, seg_table);
					inputTargetToFile(target, flag);
					points = null;
					index = 0;
					count = 0;
					flag = tra;
					System.gc();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				allData.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void inputTargetToFile(Node target, int flag) {
		FileWriter fw = null;
		try {
			fw = new FileWriter("./resultSet.txt", true);
			double error = 0;
			List<Point> reverse = new ArrayList<>();
			for (int i = 0; i < SortTree.getTreeHigh(); i++) {
				List<Point> list = new ArrayList<>();
				if (target != null && target.getSegment() != null) {
					list = target.getSegment();
					Point p1 = list.get(0);
					Point p2 = list.get(1);
					double value = target.getError();
					error += value;
					reverse.add(p2);
					reverse.add(p1);
					target = target.getParent().getParent();
				}
			}
			for (int i = reverse.size() - 1; i > -1; i--) {
				Point p1 = reverse.get(i);
				fw.write(flag + "," + p1.getX() + " " + p1.getY() + "\r\n");
			}

			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
