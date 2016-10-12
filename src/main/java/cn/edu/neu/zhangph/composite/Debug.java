package cn.edu.neu.zhangph.composite;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.davidmoten.rtree.geometry.Point;

import cn.edu.neu.zhangph.util.GlobalHeap;
import cn.edu.neu.zhangph.util.IndividualHeap;
import cn.edu.neu.zhangph.util.Pair;
/**
 * 调试/测试
 * @author zhangph
 *
 */
public class Debug {
	public static void debugIndividualHeap(IndividualHeap[] individualHeap) {
		for (int i = 0; i < individualHeap.length; i++) {
			List<Pair> pairs = individualHeap[i].getPairs();
			System.out.println(pairs.size());
			Iterator<Pair> it = pairs.iterator();
			while (it.hasNext()) {
				Pair entry = it.next();
				System.out.println(entry.getFlag() + ", " + ((Point) entry.getGeometry()).getId() + ", "
						+ entry.getGeometry() + ", " + entry.getScore());
			}
		}
	}
	public static void debugGlobalHeap(GlobalHeap globalHeap) {
		@SuppressWarnings("rawtypes")
		Map map = globalHeap.getPairs();
		@SuppressWarnings("rawtypes")
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<Integer, Pair> entry = (java.util.Map.Entry<Integer, Pair>) iterator.next();
			System.out.println(entry.getKey() + " = " + ((Point) entry.getValue().getGeometry()).getId() + " "
					+ entry.getValue().getGeometry() + " " + entry.getValue().getScore());
		}
	}

	public static void debugCandidateSet(Pair[][] candidateRes) {
		for (int i = 0; i < candidateRes.length; i++) {
			for (int j = 0; j < candidateRes[i].length; j++) {
				Pair pair = candidateRes[i][j];
				if (pair != null)
					System.out.print(" " + ((Point) pair.getGeometry()).getId() + " ");
				else
					System.out.print(" + ");
			}
			System.out.println();
		}
	}

	public static void debugPair(Pair pair) {
		if (pair == null)
			return;
		System.out.println("[" + pair.getFlag() + "," + ((Point) pair.getGeometry()).getId() + "] = "
				+ pair.getGeometry() + ", " + pair.getScore());
	}
	

	public static void debugCandidateVerification(Map<Integer, Double> resultSet) {
		Iterator iterator = resultSet.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Double> map = (java.util.Map.Entry<Integer, Double>) iterator
					.next();
			System.out.println(map.getKey() + " = " + map.getValue());
		}
	}

}
