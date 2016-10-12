package cn.edu.neu.zhangph.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class GlobalHeap {
	private Map<Integer,Pair> pairs = new HashMap<Integer, Pair>();
	public GlobalHeap() {	}
	
	public Map<Integer, Pair> getPairs() {
		return pairs;
	}

	public void setPairs(Map<Integer, Pair> pairs) {
		this.pairs = pairs;
	}

	public void push(int i, Pair pair) {
		pairs.put(i, pair);
	}
	public Pair pop(){
		double min = Integer.MAX_VALUE;
		int key = 0;
		@SuppressWarnings("rawtypes")
		Iterator iterator = pairs.entrySet().iterator();
		while(iterator.hasNext()){
			@SuppressWarnings("unchecked")
			Map.Entry<Integer, Pair> entry = (Entry<Integer, Pair>) iterator.next();
			if(min > entry.getValue().getScore()){
				min = entry.getValue().getScore();
				key = entry.getKey();
			}
		}
		return (Pair) pairs.get(key);
	}
	public Pair pop(int j){
		return (Pair) pairs.get(j);
	}
}
