package cn.edu.neu.zhangph.method;

import java.util.EnumMap;

import cn.edu.neu.zhangph.util.GlobalHeap;
import cn.edu.neu.zhangph.util.IndividualHeap;
import cn.edu.neu.zhangph.util.Pair;


public interface Service {

    enum Candidate {
    	CANDIDATERES,INDIVIDUAL,GLOBALSET
    }
    public EnumMap<Candidate,Object> candidateGeneration(IndividualHeap[] individualHeap, GlobalHeap globalHeap, Pair[][] candidateRes, 
			int k, int queryNum);
}
