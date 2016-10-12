package cn.edu.neu.zhangph.composite;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import cn.edu.neu.zhangph.method.CreateTree;
import cn.edu.neu.zhangph.util.GlobalHeap;
import cn.edu.neu.zhangph.util.IndividualHeap;
import cn.edu.neu.zhangph.util.Pair;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Point;

/**
 * 该类主要测试运行时间,在测试运行时间之前应该关闭count测试
 * @author zhangph
 *
 */

public class Test {
	public static void main(String[] args) {
		if (args.length < 0) {
			System.err.println("Please Enter Args: <file> <traNum>");
			System.exit(0);
		}

//		String file = args[0];
//		int traNum = Integer.parseInt(args[1]);
		String file = "/home/zhangph/workspace/data/little.txt";
		int traNum = 6001;
		System.out.println("file : " + file);
		/*
		 * 0. Create r-tree for index
		 */
		@SuppressWarnings("unchecked")
		RTree<Integer, Geometry> tree = CreateTree.createTree(file);
		for(int algorithm = 2; algorithm < 4; algorithm++){
			System.out.println("algorithm : " + algorithm);
			callMethodForK(tree, algorithm, file, traNum);
			callMethodForQ(tree, algorithm, file, traNum);
			if(algorithm > 1)callMethodForMiu(tree, algorithm, file, traNum);
		}
	}
	
	public static void callMethodForK(RTree<Integer, Geometry> tree, int algorithm, String file, int traNum) {
		System.out.println("callMethodForK Start time is : " + GregorianCalendar.getInstance().getTime());
		for (int k = 2; k < 21; k+=2) {
			/// 参数修改
			int q = 10;
			double miu = 0.3;
			/*
			 * 1. Create query points in query[]
			 */
			Point[] query = CommonCreateQuery.createQuery(q);
			long start = System.currentTimeMillis(); // 1
			/*
			 * 2. Construct the individual heap for each query point
			 */
			IndividualHeap[] individualHeap = createIndividual(query, tree, k);

			/*
			 * 3. initialize the global heap and for each individual heap pop a
			 * matching pair and push it to global
			 */
			GlobalHeap globalHeap = new GlobalHeap();
			for (int i = 0; i < individualHeap.length; i++) {
				globalHeap.push(i, individualHeap[i].pop());
			}
			Pair[][] candidateRes = new Pair[traNum][query.length];
			callMethod(individualHeap, globalHeap, candidateRes, k, query, miu, algorithm);
			long end = System.currentTimeMillis(); // 2
//			System.out.println( "time " + (end - start));
		}
		System.out.println("callMethodForK End time is : " + GregorianCalendar.getInstance().getTime());
	}
	
	public static void callMethodForQ(RTree<Integer, Geometry> tree, int algorithm, String file, int traNum){
		System.out.println("callMethodForQ Start time is : " + GregorianCalendar.getInstance().getTime());
		 for (int q = 2; q < 17; q+=2) {
			/// 参数修改
			int k = 10;
			double miu = 0.3;
			/*
			 * 1. Create query points in query[]
			 */
			Point[] query = CommonCreateQuery.createQuery(q);
			long start = System.currentTimeMillis(); // 1
			/*
			 * 2. Construct the individual heap for each query point
			 */
			IndividualHeap[] individualHeap = createIndividual(query, tree, k);
			/*
			 * 3. initialize the global heap and for each individual heap pop a
			 * matching pair and push it to global
			 */
			GlobalHeap globalHeap = new GlobalHeap();
			for (int i = 0; i < individualHeap.length; i++) {
				globalHeap.push(i, individualHeap[i].pop());
			}
			Pair[][] candidateRes = new Pair[traNum][query.length];
			callMethod(individualHeap, globalHeap, candidateRes, k, query, miu, algorithm);
			long end = System.currentTimeMillis(); // 2
//			System.out.println("time " + (end - start));
		}
		System.out.println("callMethodForQ End time is : " + GregorianCalendar.getInstance().getTime());
	}
	
	public static void callMethodForMiu(RTree<Integer, Geometry> tree,
			int algorithm, String file, int traNum) {
		System.out.println("callMethodForMiu Start time is : "
				+ GregorianCalendar.getInstance().getTime());
		for (double miu = 0; miu < 1; miu += 0.2) {
			// / 参数修改
			int q = 10;
			int k = 10;
			/*
			 * 1. Create query points in query[]
			 */
			Point[] query = CommonCreateQuery.createQuery(q);

			long start = System.currentTimeMillis(); // 1
			/*
			 * 2. Construct the individual heap for each query point
			 */
			IndividualHeap[] individualHeap = createIndividual(query, tree, k);

			/*
			 * 3. initialize the global heap and for each individual heap pop a
			 * matching pair and push it to global
			 */
			GlobalHeap globalHeap = new GlobalHeap();
			for (int i = 0; i < individualHeap.length; i++) {
				globalHeap.push(i, individualHeap[i].pop());
			}
			Pair[][] candidateRes = new Pair[traNum][query.length];
			callMethod(individualHeap, globalHeap, candidateRes, k, query, miu,
					algorithm);
			long end = System.currentTimeMillis(); // 2
//			System.out.println("time " + (end - start));
		}
		System.out.println("callMethodForMiu callMethodForMiu End time is : "
				+ GregorianCalendar.getInstance().getTime());
	}
	
	private static void callMethod(IndividualHeap[] individualHeap, GlobalHeap globalHeap, Pair[][] candidateRes, int k, Point[] query, double miu, int algorithm ){
		Method method = new Method();
		switch (algorithm) {
		case 0:
			method.callCompare(individualHeap, globalHeap, candidateRes, k, query);
			break;
		case 1:
			method.callSelf(individualHeap, globalHeap, candidateRes, k, query);
			break;
		case 2:
			method.callQE(individualHeap, globalHeap, candidateRes, k, query, miu);
			break;
		case 3:
			method.callAF(individualHeap, globalHeap, candidateRes, k, query, miu);
			break;
		case 4:
			method.callAFQH(individualHeap, globalHeap, candidateRes, k, query, miu);
			break;
		}
	}
	
	public static IndividualHeap[] createIndividual(Point[] query, RTree<Integer, Geometry> tree, int k) {
		IndividualHeap[] individualHeap = new IndividualHeap[query.length];
		for (int i = 0; i < query.length; i++) {
			List<Entry<Integer, Geometry>> queryNearest = tree.nearest(query[i], 500000000, 100000).toList()
					.toBlocking().single();
			List<Pair> pairs = tranPair(queryNearest, query[i], i);
			individualHeap[i] = new IndividualHeap(pairs);
		}
		return individualHeap;
	}

	/**
	 * 
	 * @param list
	 * @param q
	 * @param i
	 * @return
	 */
	private static List<Pair> tranPair(List<Entry<Integer, Geometry>> list, Point q, int i) {
		List<Pair> pairs = new ArrayList<Pair>();
		Iterator<Entry<Integer, Geometry>> it = list.iterator();
		while (it.hasNext()) {
			Entry<Integer, Geometry> entry = it.next();
			double score = entry.geometry().distance(q.mbr());
			pairs.add(new Pair(i, entry.geometry(), score));
		}
		return pairs;
	}

}
