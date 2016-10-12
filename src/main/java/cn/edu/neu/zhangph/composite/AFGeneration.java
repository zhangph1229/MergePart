package cn.edu.neu.zhangph.composite;

import java.util.*;

import cn.edu.neu.zhangph.method.ActiveService;
import cn.edu.neu.zhangph.util.*;
/**
 * Active Function candidate Generation
 * @author zhangph
 *
 */
public class AFGeneration implements ActiveService{
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
		 * 全局堆弹出匹配数的测试
		 */
		long count = 0;
		
		/*
		 * 在改进的算法中，前两部分跟之前一样，只需要修改第三部分即可。
		 * 4. initialize candidate set and repeat
		 */
		Map<Integer, Integer> isFullMap = new HashMap<Integer, Integer>();
		Set<Integer> isExistCandidate = new HashSet<Integer>();
		boolean tmp = true;
		while (!isFullMatching(k, queryNum, isFullMap) && tmp == true) {
			count++;
			Pair pair = globalHeap.pop();
			int x = pair.getGeometry().getId();
			int y = pair.getFlag();
			if (candidateRes[x][y] == null) {
				candidateRes[x][y] = pair;
				if (isFullMap.containsKey(x))
					isFullMap.put(x, isFullMap.get(x) + 1);
				else
					isFullMap.put(x, 1);
			}
			isExistCandidate.add(x);
			// 添加部分
			/*
			 * 将global heap中属于轨迹x的也加入到候选集中
			 */
			List<Integer> globalOutFlag = new ArrayList<Integer>();
			globalOutFlag.add(y);
			Map<Integer, Pair> otherValue = globalHeap.getPairs();
			Iterator iterator = otherValue.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, Pair> map = (java.util.Map.Entry<Integer, Pair>) iterator.next();
				if (map != null && map.getValue().getGeometry().getId() == x && candidateRes[x][map.getKey()] == null) {
					globalOutFlag.add(map.getKey());
					candidateRes[x][map.getKey()] = map.getValue();
					isFullMap.put(x, isFullMap.get(x) + 1);
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
					if (isExistCandidate.contains(pairId) && candidateRes[pairId][nextFlag] == null) {
						candidateRes[pairId][nextFlag] = individualHeap[nextFlag].pop();
						isFullMap.put(pairId, isFullMap.get(pairId) + 1);
					}
					// 按部就班的弹入全局堆中
					if (!individualHeap[nextFlag].getPairs().isEmpty()
							&& individualHeap[nextFlag].getPairs().size() != 0) {
						globalHeap.push(nextFlag, individualHeap[nextFlag].pop());
					} else	break;
				}
			}
			//------------------------------------------------------------------
			//(Lines 1-10 are the same as Algorithm 1) ......
			/**
			 * add qualifier expectation-absed generation
			 */
			double c = isExistCandidate.size();
			while(isFullNum(isFullMap, queryNum)/c < miu){
				// compute partial-matching candidate's expectation
				// retrieve the candidate R with highest expectation
				int r = max(computeExpectation(candidateRes, globalHeap, isFullMap, queryNum));
				// make up the matching pairs for R
				for(int j = 0;  j < candidateRes[r].length; j++){
					if(candidateRes[r][j] == null){
						if(globalHeap.getPairs().containsKey(j) && globalHeap.getPairs().get(j).getGeometry().getId() == r){
							candidateRes[r][j] = globalHeap.pop(j);
							globalHeap.push(j, individualHeap[j].pop());
						}else {
							Iterator<Pair> it = individualHeap[j].getPairs().iterator();
							while(it.hasNext()){
								Pair p = it.next();
								if(p.getGeometry().getId() == r){
									candidateRes[r][j] = p;
									break;
								}
							}
						}
						isFullMap.put(r, isFullMap.get(r) + 1);
					}else {
						individualHeap[j].pop();
					}
				}
			}
			//------------------------------------------------------------------
		}
		/**
		 * 使用上面的while循环快速的生成k条全匹配的轨迹
		 * 下面添加部分为判断有k条全匹配的轨迹的聚合距离小于全局堆的聚合距离
		 * <br>尽管使用这种low的方式降低了代码的可读性,产生了代码冗余,从效率上可以提高
		 */
		while (!isFullMatching(k, queryNum, candidateRes, globalHeap) && tmp == true) {
			count++;
			Pair pair = globalHeap.pop();
			int x = pair.getGeometry().getId();
			int y = pair.getFlag();
			if (candidateRes[x][y] == null) {
				candidateRes[x][y] = pair;
				if (isFullMap.containsKey(x))
					isFullMap.put(x, isFullMap.get(x) + 1);
				else
					isFullMap.put(x, 1);
			}
			isExistCandidate.add(x);
			// 添加部分
			/*
			 * 将global heap中属于轨迹x的也加入到候选集中
			 */
			List<Integer> globalOutFlag = new ArrayList<Integer>();
			globalOutFlag.add(y);
			Map<Integer, Pair> otherValue = globalHeap.getPairs();
			Iterator iterator = otherValue.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, Pair> map = (java.util.Map.Entry<Integer, Pair>) iterator.next();
				if (map != null && map.getValue().getGeometry().getId() == x && candidateRes[x][map.getKey()] == null) {
					globalOutFlag.add(map.getKey());
					candidateRes[x][map.getKey()] = map.getValue();
					isFullMap.put(x, isFullMap.get(x) + 1);
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
					if (isExistCandidate.contains(pairId) && candidateRes[pairId][nextFlag] == null) {
						candidateRes[pairId][nextFlag] = individualHeap[nextFlag].pop();
						isFullMap.put(pairId, isFullMap.get(pairId) + 1);
					}
					// 按部就班的弹入全局堆中
					if (!individualHeap[nextFlag].getPairs().isEmpty()
							&& individualHeap[nextFlag].getPairs().size() != 0) {
						globalHeap.push(nextFlag, individualHeap[nextFlag].pop());
					} else{
						tmp = false;
						break;
					}
				}
			}
			//------------------------------------------------------------------
			//(Lines 1-10 are the same as Algorithm 1) ......
			/**
			 * add qualifier expectation-absed generation
			 */
			double c = isExistCandidate.size();
			while(isFullNum(isFullMap, queryNum)/c < miu){
				// compute partial-matching candidate's expectation
				// retrieve the candidate R with highest expectation
				int r = max(computeExpectation(candidateRes, globalHeap, isFullMap, queryNum));
				// make up the matching pairs for R
				for(int j = 0;  j < candidateRes[r].length; j++){
					if(candidateRes[r][j] == null){
						if(globalHeap.getPairs().containsKey(j) && globalHeap.getPairs().get(j).getGeometry().getId() == r){
							candidateRes[r][j] = globalHeap.pop(j);
							globalHeap.push(j, individualHeap[j].pop());
						}else {
							Iterator<Pair> it = individualHeap[j].getPairs().iterator();
							while(it.hasNext()){
								Pair p = it.next();
								if(p.getGeometry().getId() == r){
									candidateRes[r][j] = p;
									break;
								}
							}
						}
						isFullMap.put(r, isFullMap.get(r) + 1);
					}else {
						individualHeap[j].pop();
					}
				}
			}
			//------------------------------------------------------------------
		}
		
		// global heap pop number and println
//		System.out.println(count);
		// The CompareCandidateGeneration memory statistics
//		long endMem = run.totalMemory() - run.freeMemory();
//		System.out.println((endMem - startMem)/1000000);

		EnumMap<Candidate, Object> retMap = new EnumMap<Candidate, Object>(Candidate.class);
		retMap.put(Candidate.CANDIDATERES, candidateRes);
		retMap.put(Candidate.INDIVIDUAL, individualHeap);
		retMap.put(Candidate.GLOBALSET, globalHeap);
		return retMap;
	}
	/**
	 * 判断一条轨迹是否为全匹配轨迹
	 * @param isFullMap
	 * @param len
	 * @return 匹配的数量
	 */
	private double isFullNum(Map<Integer, Integer> isFullMap, int len) {
		int count = 0;
		Iterator it = isFullMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Integer, Integer> entery = (java.util.Map.Entry<Integer, Integer>) it.next();
			if(entery.getValue() >= len )count++;
		}
		return count;
	}
	/**
	 * 返回期望值最大的轨迹
	 * @param af 经过激活函数后的各个轨迹的期望值
	 * @return 返回期望值最大的轨迹
	 */
	private int max(Map<Integer, Double> af) {
		double max = 0;
		int r = 0;
		Iterator it = af.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Integer, Double> entry = (java.util.Map.Entry<Integer, Double>) it.next();
			if(entry.getValue() > max){
				max = entry.getValue();
				r = entry.getKey();
			}
		}
		return r;
	}
	/**
	 * 计算部分匹配轨迹的期望值
	 * @return 经过激活函数后的各个轨迹的期望值
	 */
	private Map<Integer, Double> computeExpectation(Pair[][] candidateRes,
			GlobalHeap globalHeap, Map<Integer, Integer> isFullMap, int queryNum) {
		Map<Integer, Double> af = new HashMap<Integer, Double>();
		for(int i = 0; i < candidateRes.length; i++){
			double t1 = 0;
			double t2 = 0;
			int count1 = 0;
			int count2 = 0;
			for(int j = 0; j < candidateRes[i].length; j++){
				if(candidateRes[i][j] != null){
					count1++;
					t1 += candidateRes[i][j].getScore(); 
				}else {
					count2++;
					t2 += globalHeap.getPairs().get(j).getScore();
				}
			}
			if(isFullMap.containsKey(i) && isFullMap.get(i) < queryNum ) af.put(i, 1/(1 + Math.exp(-(t1/count1 + t2/count2))));
		}
		return af;
	}
	/**
	 * 是否有k条全匹配的轨迹
	 * @param num --> k
	 * @param qNum 查询点的数量
	 * @param isFull --> hashmap 判断一条轨迹是否全匹配
	 * @return <tt>ture</tt> 如果有k条全匹配的轨迹
	 */
	public static boolean isFullMatching(int num, int qNum, Map<Integer, Integer> isFull) {
		Iterator isfull = isFull.entrySet().iterator();
		while (isfull.hasNext()) {
			Map.Entry<Integer, Integer> entry = (java.util.Map.Entry<Integer, Integer>) isfull.next();
			if (entry.getValue() == qNum)
				num--;
		}
		if (num <= 0)
			return true;
		else
			return false;
	}
	/**
	 * 判断是否有k条全匹配,并且小于全局堆聚合距离的轨迹
	 * @param num --> k
	 * @param qNum 查询点数
	 * @param result --> 候选项集
	 * @param globalHeap --> 当前全局堆
	 * @return <tt>true</tt> 有k条全匹配,并且小于全局堆聚合距离的轨迹
	 */
	public static boolean isFullMatching(int num, int qNum, Pair[][] result,
			GlobalHeap globalHeap) {
		Iterator it = globalHeap.getPairs().entrySet().iterator();
		double sum = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Pair> entry = (java.util.Map.Entry<Integer, Pair>) it
					.next();
			if (entry != null) {
				sum += entry.getValue().getScore();
			}
		}
		int tra = 0;
		for (int i = 0; i < result.length; i++) {
			int count = 0;
			double score = 0;
			for (int j = 0; j < result[i].length; j++) {
				if (result[i][j] != null) {
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
}
