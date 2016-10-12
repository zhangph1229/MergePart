package cn.edu.neu.zhangph.composite;

import java.util.*;
import java.util.Map.Entry;

import cn.edu.neu.zhangph.method.Service;
import cn.edu.neu.zhangph.util.GlobalHeap;
import cn.edu.neu.zhangph.util.IndividualHeap;
import cn.edu.neu.zhangph.util.Pair;

/**
 * 改进的KNNT算法
 * 
 * @author zhangph
 *
 */
public class MyCandidateGeneration implements Service {

	/**
	 * 
	 * Create the candidate set for query and num trajectories.
	 * 
	 * @param query   Query point set
	 * @param k       Recommended trajectory bar number
	 * @param num     Total number of trajectory
	 * @return EnumMap<Candidate, Object>, Candidate is an enumeration class.
	 *         return three parameters [CandidateRes, Individual[], GlobalHeap]
	 */
	@Override
	public EnumMap<Candidate, Object> candidateGeneration(
			IndividualHeap[] individualHeap, GlobalHeap globalHeap,
			Pair[][] candidateRes, int k, int queryNum) {
		//内存测试
		Runtime run = Runtime.getRuntime(); // Runtime is singletom object
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		} // 暂停程序执行
		run.gc();
		// 获取开始时内存使用量
		long startMem = run.totalMemory() - run.freeMemory();
		/*
		 * 4. main algorithm
		 */
		Map<Integer, Integer> isFullMap = new HashMap<Integer, Integer>();
		boolean tmp = true;
		long count = 0;
		while (!isFullMatching(k, queryNum, isFullMap) && tmp == true) {
			Pair pair = globalHeap.pop();
			count++;
			int x = pair.getGeometry().getId();
			int y = pair.getFlag();
			if (candidateRes[x][y] == null) {
				candidateRes[x][y] = pair;
				if (isFullMap.containsKey(x))
					isFullMap.put(x, isFullMap.get(x) + 1);
				else
					isFullMap.put(x, 1);
			}
			/*
			 * 将global heap中属于轨迹x的也加入到候选集中
			 */
			Set<Integer> globalOutFlag = new HashSet<Integer>();
			globalOutFlag.add(y);
			Iterator iterator = globalHeap.getPairs().entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, Pair> map = (Map.Entry<Integer, Pair>) iterator.next();
				if (map != null && map.getValue().getGeometry().getId() == x
						&& candidateRes[x][map.getKey()] == null) {
					globalOutFlag.add(map.getKey());
					candidateRes[x][map.getKey()] = map.getValue();
					if (isFullMap.containsKey(x))
						isFullMap.put(x, isFullMap.get(x) + 1);
					else
						isFullMap.put(x, 1);
				}
			}
			Iterator<Integer> flag = globalOutFlag.iterator();
			while (flag.hasNext()) {
				int nextFlag = flag.next();
				List<Pair> pairs = individualHeap[nextFlag].getPairs();
				if (pairs == null || pairs.size() == 0) {
					tmp = false;
					break;
				} else {
					/*
					 * 接下来有两类操作，一类：如果该私有堆的第一个元素在候选项集中存在结果，将直接加入到候选集或者被淘汰掉
					 * 另一类：如果该私有堆的第一个元素在候选项集中不存在结果，还是要按部就班的弹入全局堆中
					 */
					int pairId = pairs.get(0).getGeometry().getId();
					if (isFullMap.containsKey(pairId)
							&& candidateRes[pairId][nextFlag] == null) {
						candidateRes[pairId][nextFlag] = individualHeap[nextFlag].pop();
						if (isFullMap.containsKey(pairId))
							isFullMap.put(pairId, isFullMap.get(pairId) + 1);
						else
							isFullMap.put(pairId, 1);
					}
					// 按部就班的弹入全局堆中
					if (!individualHeap[nextFlag].getPairs().isEmpty()) {
						globalHeap.push(nextFlag, 	individualHeap[nextFlag].pop());
					} else	break;
				}
			}
		}
		
		// global heap pop number and println
//		System.out.println(count);
		// The CompareCandidateGeneration memory statistics
//		long endMem = run.totalMemory() - run.freeMemory();
//		System.out.println((endMem - startMem)/1000000);
		
		// use EnumMap return more than one result
		EnumMap<Candidate, Object> retMap = new EnumMap<Candidate, Object>(Candidate.class);
		retMap.put(Candidate.CANDIDATERES, candidateRes);
		retMap.put(Candidate.INDIVIDUAL, individualHeap);
		retMap.put(Candidate.GLOBALSET, globalHeap);
		
		return retMap;
	}
	/**
	 * 判断是否存在k条全匹配轨迹
	 * @param num
	 * @param qNum
	 * @param isFull
	 * @return
	 */
	public static boolean isFullMatching(int num, int qNum,
			Map<Integer, Integer> isFull) {
		@SuppressWarnings("rawtypes")
		Iterator isfull = isFull.entrySet().iterator();
		while (isfull.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<Integer, Integer> entry =  (Entry<Integer, Integer>) isfull.next();
			if (entry.getValue() == qNum)
				num--;
		}
		if (num <= 0)
			return true;
		else
			return false;
	}
}
