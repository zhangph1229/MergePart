package cn.edu.neu.zhangph.composite;

import java.util.*;

import cn.edu.neu.zhangph.method.ActiveService;
import cn.edu.neu.zhangph.util.*;

import com.github.davidmoten.rtree.geometry.Point;

public class QEGeneration implements ActiveService {
	
	@Override
	public EnumMap<Candidate, Object> candidateGeneration(
			IndividualHeap[] individualHeap, GlobalHeap globalHeap,
			Pair[][] candidateRes, int k, int queryNum, double miu) {

		/**
		 * 内存测试
		 */
		Runtime run = Runtime.getRuntime(); // Runtime is singletom object
		try {
			Thread t = new Thread();
			t.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		} // 暂停程序执行
		run.gc();
		// 获取开始时内存使用量
		long startMem = run.totalMemory() - run.freeMemory();

		/**
		 * 全局堆弹出数量测试
		 */
		long count = 0;

		Map<Integer, Integer> isFullMap = new HashMap<Integer, Integer>();
		Set<Integer> isExistCandidate = new HashSet<Integer>();

		while (!isFullMatching(k, queryNum, candidateRes, globalHeap)) {
			count++;
			Pair pair = globalHeap.pop();
			int x = pair.getGeometry().getId();
			int y = pair.getFlag();
			if (candidateRes[x][y] == null && pair != null) {
				candidateRes[x][y] = pair;
				if (isFullMap.containsKey(x))
					isFullMap.put(x, isFullMap.get(x) + 1);
				else
					isFullMap.put(x, 1);
			}
			isExistCandidate.add(x);
			if (!individualHeap[pair.getFlag()].getPairs().isEmpty()) {
				globalHeap.push(pair.getFlag(),
						individualHeap[pair.getFlag()].pop());
			} else
				break;

			// ------------------------------------------------------------------
			// (Lines 1-10 are the same as Algorithm 1) ......
			/**
			 * add qualifier expectation-absed generation
			 */
			double c = isExistCandidate.size();
			while (isFullNum(isFullMap, queryNum) / c < miu) {
				// compute partial-matching candidate's expectation
				Map<Integer, Double> expectation = computeExpectation(
						candidateRes, globalHeap, isFullMap, queryNum);
				// retrieve the candidate R with highest expectation
				int max = max(expectation);
				// make up the matching pairs for R
				for (int j = 0; j < candidateRes[max].length; j++) {
					count++;
					if (candidateRes[max][j] == null) {
						if (globalHeap.getPairs().containsKey(j)
								&& globalHeap.getPairs().get(j).getGeometry()
										.getId() == max) {
							candidateRes[max][j] = globalHeap.pop(j);
							globalHeap.push(j, individualHeap[j].pop());
						} else {
							Iterator<Pair> it = individualHeap[j].getPairs()
									.iterator();
							while (it.hasNext()) {
								Pair p = it.next();
								if (p.getGeometry().getId() == max) {
									candidateRes[max][j] = p;
									break;
								}
							}
						}
						isFullMap.put(max, isFullMap.get(max) + 1);
					} else {
						individualHeap[j].pop();
					}
				}
			}
			// ------------------------------------------------------------------
		}
		EnumMap<Candidate, Object> retMap = new EnumMap<Candidate, Object>(
				Candidate.class);
		retMap.put(Candidate.CANDIDATERES, candidateRes);
		retMap.put(Candidate.INDIVIDUAL, individualHeap);
		retMap.put(Candidate.GLOBALSET, globalHeap);

		// global heap pop number and println
//		System.out.println(count);
		// The CompareCandidateGeneration memory statistics
//		long endMem = run.totalMemory() - run.freeMemory();
//		System.out.println((endMem - startMem)/1000000);

		return retMap;
	}
	private double isFullNum(Map<Integer, Integer> isFullMap, int len) {
		int count = 0;
		Iterator it = isFullMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Integer, Integer> entery = (java.util.Map.Entry<Integer, Integer>) it.next();
			if(entery.getValue() >= len )count++;
		}
		return count;
	}
	private int max(Map<Integer, Double> expectation) {
		double max = 0;
		int r = 0;
		Iterator it = expectation.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Double> entry = (java.util.Map.Entry<Integer, Double>) it.next();
			if (entry.getValue() > max) {
				max = entry.getValue();
				r = entry.getKey();
			}
		}
		return r;
	}
	private Map<Integer, Double> computeExpectation(Pair[][] candidateRes,
			GlobalHeap globalHeap, Map<Integer, Integer> isFullMap, int queryNum) {
		Map<Integer, Double> expectation = new HashMap<Integer, Double>();
		for (int i = 0; i < candidateRes.length; i++) {
			if(isFullMap.containsKey(candidateRes[i]) && isFullMap.get(candidateRes[i]) >= queryNum) continue;
			double value = 0;
			for (int j = 0; j < candidateRes[i].length; j++) {
				if (candidateRes[i][j] != null) {
					value += Math.abs(globalHeap.getPairs().get(j).getScore()
							- candidateRes[i][j].getScore());
				}
			}
			if(isFullMap.containsKey(i) && isFullMap.get(i) < queryNum )expectation.put(i, value+0.01);
		}
		return expectation;
	}
	
	public static boolean isFullMatching(int num, int qNum, Pair[][] result) {
		int tra = 0;
		for (int i = 0; i < result.length; i++) {
			int count = 0;
			for (int j = 0; j < result[i].length; j++) {
				if (result[i][j] != null)
					count++;
			}
			if (count >= qNum)
				tra++;
		}
		if (tra >= num)
			return true;
		else
			return false;
	}
	public static boolean isFullMatching(int num, int qNum, Pair[][] result, GlobalHeap globalHeap) {
		Iterator it = globalHeap.getPairs().entrySet().iterator();
		double sum = 0;
		while(it.hasNext()){
			Map.Entry<Integer, Pair> entry = (java.util.Map.Entry<Integer, Pair>) it.next();
			if(entry != null){
				sum += entry.getValue().getScore();
			}
		}
		int tra = 0;
		for (int i = 0; i < result.length; i++) {
			int count = 0;
			double score = 0;
			for (int j = 0; j < result[i].length; j++) {
				if (result[i][j] != null){
					count++;
					score += result[i][j].getScore();
				}
			}
			if (count >= qNum && score < sum)
				tra++;
		}
		if (tra >= num)
			return true;
		else
			return false;
	}
	
	public static void debugGlobalHeap(GlobalHeap globalHeap) {
		Map map = globalHeap.getPairs();
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Pair> entry = (java.util.Map.Entry<Integer, Pair>) iterator.next();
			System.out.println(entry.getKey() + " = " + ((Point) entry.getValue().getGeometry()).getId() + " "
					+ entry.getValue().getGeometry() + " " + entry.getValue().getScore());
		}
	}
}
