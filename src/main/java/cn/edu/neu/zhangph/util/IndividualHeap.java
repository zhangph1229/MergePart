package cn.edu.neu.zhangph.util;

import java.util.ArrayList;
import java.util.List;

public class IndividualHeap {
	private Pair pair;
	private List<Pair> pairs = new ArrayList<Pair>();
	public IndividualHeap() {}

	public IndividualHeap(List<Pair> pairs) {
		this.pairs = pairs;
	}
	public Pair getPair() {
		return pair;
	}
	public void setPair(Pair pair) {
		this.pair = pair;
	}
	public List<Pair> getPairs() {
		return pairs;
	}

	public void setPairs(List<Pair> pairs) {
		this.pairs = pairs;
	}

	public Pair pop(){
		if(pairs.isEmpty()) return null;
		Pair pair = (Pair) pairs.get(0);
		pairs.remove(0);
		return pair;
	}
}
