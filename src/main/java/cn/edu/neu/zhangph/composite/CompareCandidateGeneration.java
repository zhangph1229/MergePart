package cn.edu.neu.zhangph.composite;

import java.util.*;

import com.github.davidmoten.rtree.geometry.Point;

import cn.edu.neu.zhangph.method.Service;
import cn.edu.neu.zhangph.util.*;

/**
 * This class implementions of the <tt>Service</tt>. It implements the paper --
 * "Tang L A, Zheng Y, Xie X, et al. Retrieving k-Nearest Neighboring Trajectories by a Set of Point Locations[M]// Advances in Spatial and Temporal Databases. Springer Berlin Heidelberg, 2011:223-241."
 * GH algorithm.
 * 
 * <p>This algorithm main idea from FA and TA algorithm.
 * 
 * @param <E> undetermined
 * 
 * @author zhangph
 * @see MyCandidateGeneration
 * @since 0.1
 *
 */
public class CompareCandidateGeneration<E> implements Service {
	/**
	 * @param individualHeap
	 *            It is IndividualHeap's array to represent query--heap
	 * @param globalHeap
	 *            The global heap G consisits of m matching pairs, G =
	 *            {<p1,q1>,<p2,q2>,...,<pm,qm>}, where <pi,qi> is popped from
	 *            the individual heap hi.
	 * @param candidateRes
	 * 			This class generate finally result, including all k trajectories in it.
	 * @param k	k-NNT's k
	 * @param qNum query's numbers
	 */
	@Override
	public EnumMap<Candidate, Object> candidateGeneration(
			IndividualHeap[] individualHeap, GlobalHeap globalHeap,
			Pair[][] candidateRes, int k, int queryNum) {
		//内存测试
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
		// 全局堆弹出数量测试
		long count = 0; // Test from global heap pop numbers

		/**
		 * 4. initialize candidate set and repeat
		 */
		while (!isFullMatching(k, queryNum, candidateRes)) {
			count++;
			Pair pair = globalHeap.pop();
			int x = ((Point) pair.getGeometry()).getId();
			int y = pair.getFlag();
			if (candidateRes[x][y] == null && pair != null) {
				candidateRes[x][y] = pair;
			}
			if (!individualHeap[pair.getFlag()].getPairs().isEmpty()
					&& individualHeap[pair.getFlag()].getPairs().size() != 0) {
				globalHeap.push(pair.getFlag(),
						individualHeap[pair.getFlag()].pop());
			} else
				break;
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
	/**
	 * This method for judging algorithm is over.
	 * 
	 * @param k	k-NNT's k
	 * @param qNum query's numbers
	 * @param result candidate set include all result
	 * @return <tt>true</tt> if result's all matching pair >= k
	 */
	public static boolean isFullMatching(int k, int qNum, Pair[][] result) {
		int allMatchTra = 0;
		for (int i = 0; i < result.length; i++) {
			int count = 0; 
			for (int j = 0; j < result[i].length; j++) {
				if (result[i][j] != null)
					count++;
			}
			if (count >= qNum)
				allMatchTra++;
		}
		if (allMatchTra >= k)
			return true;
		else
			return false;
	}
}
