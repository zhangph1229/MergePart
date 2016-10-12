package cn.edu.neu.zhangph.composite;

import java.util.*;
import java.util.Map.Entry;

import com.github.davidmoten.rtree.geometry.Point;

import cn.edu.neu.zhangph.util.GlobalHeap;
import cn.edu.neu.zhangph.util.IndividualHeap;
import cn.edu.neu.zhangph.util.Pair;

/**
 * 第二阶段 候选验证
 * @author zhangph
 *
 */
public class CommonCandidateVerification {
	/**
	 * candidate verification for candidate set
	 * 
	 * @param candidateRes
	 *            候选项集
	 */
	public Map<Integer, Double> candidateVerification(Pair[][] candidateRes,
			Point[] query, IndividualHeap[] individualHeap,
			GlobalHeap globalHeap, int k) {
		Map<Integer, Double> resultSet = new HashMap<Integer, Double>();
		// 判断每一条候选轨迹是否是完全匹配轨迹，并将完全匹配的k条轨迹存放到结果集中
		for (int i = 0; i < candidateRes.length; i++) {
			if (isAllNull(candidateRes[i]))
				continue; // 忽略都为空的情况
			double score = 0;
			Integer key = 0;
			boolean isFull = true;
			for (int j = 0; j < candidateRes[i].length; j++) {
				if (candidateRes[i][j] == null) {
					isFull = false;
				} else if (candidateRes[i][j].getGeometry() != null) {
					score += candidateRes[i][j].getScore();
					key = candidateRes[i][j].getGeometry().getId();
				}
			}
			if (!isFull) {
				score = complementTra(candidateRes[i], query,
						individualHeap, globalHeap, key) + score;
			}
			if (resultSet.size() < k
					|| (resultSet.size() >= k && minValue(resultSet, score))) {
				resultSet.put(key, score);
			}
		}
		return resultSet;
	}
	/**
	 * 求最小值
	 * @param resultSet
	 * @param score
	 * @return
	 */
	private boolean minValue(Map<Integer, Double> resultSet, double score) {
		double max = 0;
		int key = 0;
		Iterator iterator = resultSet.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Double> map = (Entry<Integer, Double>) iterator
					.next();
			if (map.getValue() > max) {
				max = map.getValue();
				key = map.getKey();
			}
		}

		if (max > score) {
			resultSet.remove(key);
			return true;
		} else
			return false;
	}

	private double complementTra(Pair[] pairs, Point[] query,
			IndividualHeap[] individualHeap, GlobalHeap globalHeap, Integer key) {
		double score = 0;
		for (int i = 0; i < pairs.length; i++) {
			// 如果等于空，分成两种情况，一种是在global heap中，另一种是在individual heap中
			if (pairs[i] == null) {
				if (key == globalHeap.getPairs().get(i).getGeometry().getId())
					score += globalHeap.getPairs().get(i).getScore();
				else {
					List<Pair> pair = individualHeap[i].getPairs();
					Iterator iterator = pair.iterator();
					while (iterator.hasNext()) {
						Pair p = (Pair) iterator.next();
						if (key == (p.getGeometry().getId())) {
							score += p.getScore();
							break;
						}
					}
				}
			}
		}
		return score;
	}
	/**
	 * 全为空的轨迹
	 * @param pairs
	 * @return
	 */
	private boolean isAllNull(Pair[] pairs) {
		for (int i = 0; i < pairs.length; i++) {
			if (pairs[i] != null)
				return false;
		}
		return true;
	}
}
