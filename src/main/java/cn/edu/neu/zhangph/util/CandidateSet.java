package cn.edu.neu.zhangph.util;

public class CandidateSet {

	public boolean isFullMatching(int num, int qNum, Pair[][] result) {
		int count = 0;
		int tra = 0;
		for (int i = 0; i < result.length; i++) {
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

	public boolean contains(int row, int flag, Pair[][] result) {
		if (result[row][flag] == null)
			return false;
		else
			return true;
	}

//	public void add(int row, int flag, Pair pair) {
//		result[row][flag] = pair;
//	}

}
