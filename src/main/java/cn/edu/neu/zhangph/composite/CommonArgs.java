package cn.edu.neu.zhangph.composite;

import java.util.*;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Point;

import cn.edu.neu.zhangph.method.CreateTree;
import cn.edu.neu.zhangph.util.GlobalHeap;
import cn.edu.neu.zhangph.util.IndividualHeap;
import cn.edu.neu.zhangph.util.Pair;

public class CommonArgs {
	
	public static void main(String[] args) {
		if (args.length < 0) {
			System.err.println("Please Enter Args: <file> <traNum>");
			System.exit(0);
		}
		// String file = args[0];
		// int traNum = Integer.parseInt(args[1]);
		String file = "./data/skywed.csv";
		int traNum = 6001;
		System.out.println("file : " + file);
		/*
		 * 0. Create r-tree for index
		 */
		RTree<Integer, Geometry> tree = CreateTree.createTree(file);
		
		
		for(int algorithm = 0; algorithm < 5; algorithm++){
			System.out.println("algorithm : " + algorithm);
			callMethodForK(tree, algorithm, file, traNum);
			callMethodForQ(tree, algorithm, file, traNum);
			if(algorithm > 1)callMethodForMiu(tree, algorithm, file, traNum);
		}
	}
	
	
	/**
	 * query numbers
	 */
	public static final int QNUMS = 10;
	/**
	 * k-NNT's k
	 */
	public static final int KTRAS = 10;
	/**
	 * μ (all matching pair) / (candidateRes.size()) 
	 */
	public static final double MIU = 0.3;
	
	/**
	 * Testing k change for all algorithms
	 * @param tree  R-Tree
	 * @param algorithm 
	 * 			0 GH
	 * 			1 NS
	 * 			2 QE
	 * 			3 AF
	 * 			4 AFGH
	 * @param file	load testing data
	 * @param traNum	k
	 */
	public static void callMethodForK(RTree<Integer, Geometry> tree, int algorithm, String file, int traNum) {
		System.out.println("callMethodForK Start time is : " + GregorianCalendar.getInstance().getTime());
		for (int k = 2; k < 21; k+=2) {
			/*
			 * 1. Create query points in query[]
			 */
			Point[] query = CommonCreateQuery.createQuery(QNUMS);
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
			
			callMethod(individualHeap, globalHeap, candidateRes, k, query, MIU, algorithm);
			long end = System.currentTimeMillis(); // 2
//			System.out.println((end - start));
		}
		System.out.println("callMethodForK End time is : " + GregorianCalendar.getInstance().getTime());
	}
	/**
	 * Testing q change for all algorithms
	 * @param tree  R-Tree
	 * @param algorithm 
	 * 			0 GH
	 * 			1 NS
	 * 			2 QE
	 * 			3 AF
	 * 			4 AFGH
	 * @param file	load testing data
	 * @param traNum	k
	 */
	public static void callMethodForQ(RTree<Integer, Geometry> tree, int algorithm, String file, int traNum){
		System.out.println("callMethodForQ Start time is : " + GregorianCalendar.getInstance().getTime());
		 for (int q = 2; q < 17; q+=2) {
			/*
			 * 1. Create query points in query[]
			 */
			Point[] query = CommonCreateQuery.createQuery(q);
			long start = System.currentTimeMillis(); // 1
			/*
			 * 2. Construct the individual heap for each query point
			 */
			IndividualHeap[] individualHeap = createIndividual(query, tree, KTRAS);

			/*
			 * 3. initialize the global heap and for each individual heap pop a
			 * matching pair and push it to global
			 */
			GlobalHeap globalHeap = new GlobalHeap();
			for (int i = 0; i < individualHeap.length; i++) {
				globalHeap.push(i, individualHeap[i].pop());
			}
			Pair[][] candidateRes = new Pair[traNum][query.length];
			callMethod(individualHeap, globalHeap, candidateRes, KTRAS, query, MIU, algorithm);
			long end = System.currentTimeMillis(); // 2
//			System.out.println((end - start));
		}
		System.out.println("callMethodForQ End time is : " + GregorianCalendar.getInstance().getTime());
	}
	/**
	 * Testing μ change for all algorithms
	 * @param tree  R-Tree
	 * @param algorithm 
	 * 			0 GH
	 * 			1 NS
	 * 			2 QE
	 * 			3 AF
	 * 			4 AFGH
	 * @param file	load testing data
	 * @param traNum	k
	 */
	public static void callMethodForMiu(RTree<Integer, Geometry> tree, int algorithm, String file, int traNum){
		System.out.println("callMethodForMiu Start time is : " + GregorianCalendar.getInstance().getTime());

		for(double miu = 0; miu <= 1; miu += 0.2){
			/*
			 * 1. Create query points in query[]
			 */
			Point[] query = CommonCreateQuery.createQuery(QNUMS);
			long start = System.currentTimeMillis(); // 1
			/*
			 * 2. Construct the individual heap for each query point
			 */
			IndividualHeap[] individualHeap = createIndividual(query, tree, KTRAS);
			/*
			 * 3. initialize the global heap and for each individual heap pop a
			 * matching pair and push it to global
			 */
			GlobalHeap globalHeap = new GlobalHeap();
			for (int i = 0; i < individualHeap.length; i++) {
				globalHeap.push(i, individualHeap[i].pop());
			}
			Pair[][] candidateRes = new Pair[traNum][query.length];
			callMethod(individualHeap, globalHeap, candidateRes, KTRAS, query, miu, algorithm);
			long end = System.currentTimeMillis(); // 2
//			System.out.println((end - start));
		}
		System.out.println("callMethodForMiu callMethodForMiu End time is : " + GregorianCalendar.getInstance().getTime());
	}
	
	/**
	 * 调用k-NNT方法
	 * @param individualHeap
	 * @param globalHeap
	 * @param candidateRes
	 * @param k
	 * @param query
	 * @param miu
	 * @param algorithm
	 */
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
	/**
	 * 创建私有堆
	 * @param query 查询点集合
	 * @param tree 生成的空间索引树
	 * @param k 返回的轨迹数量
	 * @return 返回一个私有对数组,没一行记录一个查询点所对应的私有堆
	 */
	public static IndividualHeap[] createIndividual(Point[] query, RTree<Integer, Geometry> tree, int k) {
		IndividualHeap[] individualHeap = new IndividualHeap[query.length];
		for (int i = 0; i < query.length; i++) {
			List<Entry<Integer, Geometry>> queryNearest = tree.nearest(query[i], 500000000, 150000).toList()
					.toBlocking().single();
			List<Pair> pairs = tranPair(queryNearest, query[i], i);
			individualHeap[i] = new IndividualHeap(pairs);
		}
		for (int i = 0; i < individualHeap.length; i++) {
			 List<Pair> pairs = individualHeap[i].getPairs();
			 Iterator it = pairs.iterator();
			 List<Pair> tmp = new ArrayList<Pair>();
			 tmp.addAll(pairs);
			 Map<Integer, Pair> map = new HashMap<>();
			 while(it.hasNext()){
				 Pair pair = (Pair) it.next();
				 if(!map.containsKey(pair.getGeometry().getId())){
					 map.put(pair.getGeometry().getId(), pair);
				 }else tmp.remove(pair);
			 }
			 individualHeap[i] = new IndividualHeap(tmp);
		}
		return individualHeap;
	}
	/**
	 * 将原始的Entry<Integer, Geometry>--> Pair
	 * @param list 原始的数据
	 * @param q 查询点q
	 * @param i 添加一个id编号
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
