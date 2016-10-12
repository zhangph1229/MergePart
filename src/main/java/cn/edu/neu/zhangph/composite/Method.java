package cn.edu.neu.zhangph.composite;

import java.util.EnumMap;
import java.util.Map;

import cn.edu.neu.zhangph.method.Service;
import cn.edu.neu.zhangph.util.GlobalHeap;
import cn.edu.neu.zhangph.util.IndividualHeap;
import cn.edu.neu.zhangph.util.Pair;
import cn.edu.neu.zhangph.method.ActiveService;
import com.github.davidmoten.rtree.geometry.Point;
/**
 * 主要方法的调用
 * @author zhangph
 *
 */
public class Method {
	/**
	 * 调用GH方法
	 * @return 
	 */
	public Map<Integer, Double> callCompare(IndividualHeap[] individualHeap,
			GlobalHeap globalHeap, Pair[][] candidateRes, int k, Point[] query) {
		CompareCandidateGeneration cg = new CompareCandidateGeneration();
		CommonCandidateVerification cv = new CommonCandidateVerification();
		EnumMap<cn.edu.neu.zhangph.method.Service.Candidate, Object> candidate = cg
				.candidateGeneration(individualHeap, globalHeap, candidateRes,
						k, query.length);
		candidate.entrySet().iterator();
		Map<Integer, Double> res = cv.candidateVerification(
				(Pair[][]) candidate.get(Service.Candidate.CANDIDATERES),
				query,
				(IndividualHeap[]) candidate.get(Service.Candidate.INDIVIDUAL),
				(GlobalHeap) candidate.get(Service.Candidate.GLOBALSET), k);
		return res;
	}

	public Map<Integer, Double> callQE(IndividualHeap[] individualHeap,
			GlobalHeap globalHeap, Pair[][] candidateRes, int k, Point[] query,
			double miu) {
		QEGeneration cg = new QEGeneration();
		CommonCandidateVerification cv = new CommonCandidateVerification();

		EnumMap<cn.edu.neu.zhangph.method.ActiveService.Candidate, Object> candidate = cg
				.candidateGeneration(individualHeap, globalHeap, candidateRes,
						k, query.length, miu);
		candidate.entrySet().iterator();
		Map<Integer, Double> res = cv.candidateVerification(
				(Pair[][]) candidate.get(ActiveService.Candidate.CANDIDATERES),
				query, (IndividualHeap[]) candidate
						.get(ActiveService.Candidate.INDIVIDUAL),
				(GlobalHeap) candidate.get(ActiveService.Candidate.GLOBALSET),
				k);
		return res;
	}

	public Map<Integer, Double> callSelf(IndividualHeap[] individualHeap,
			GlobalHeap globalHeap, Pair[][] candidateRes, int k, Point[] query) {
		MyCandidateGeneration cg = new MyCandidateGeneration();
		CommonCandidateVerification cv = new CommonCandidateVerification();
		EnumMap<cn.edu.neu.zhangph.method.Service.Candidate, Object> candidate = cg
				.candidateGeneration(individualHeap, globalHeap, candidateRes,
						k, query.length);
		// candidate.entrySet().iterator();
		// System.out.println("-----------	MyCandidateGeneration----------");
		Map<Integer, Double> res = cv.candidateVerification(
				(Pair[][]) candidate.get(Service.Candidate.CANDIDATERES),
				query,
				(IndividualHeap[]) candidate.get(Service.Candidate.INDIVIDUAL),
				(GlobalHeap) candidate.get(Service.Candidate.GLOBALSET), k);
		return res;
	}

	public Map<Integer, Double> callAF(IndividualHeap[] individualHeap,
			GlobalHeap globalHeap, Pair[][] candidateRes, int k, Point[] query,
			double miu) {
		AFGeneration cg = new AFGeneration();
		CommonCandidateVerification cv = new CommonCandidateVerification();
		EnumMap<cn.edu.neu.zhangph.method.ActiveService.Candidate, Object> candidate = cg
				.candidateGeneration(individualHeap, globalHeap, candidateRes,
						k, query.length, miu);
		// System.out.println("-----------AFGeneration----------");
//		candidate.entrySet().iterator();
		Map<Integer, Double> res = cv.candidateVerification(
				(Pair[][]) candidate.get(ActiveService.Candidate.CANDIDATERES),
				query, (IndividualHeap[]) candidate
						.get(ActiveService.Candidate.INDIVIDUAL),
				(GlobalHeap) candidate.get(ActiveService.Candidate.GLOBALSET),
				k);
		return res;
	}

	public Map<Integer, Double> callAFQH(IndividualHeap[] individualHeap,
			GlobalHeap globalHeap, Pair[][] candidateRes, int k, Point[] query,
			double miu) {
		AFQHGeneration cg = new AFQHGeneration();
		CommonCandidateVerification cv = new CommonCandidateVerification();
		EnumMap<cn.edu.neu.zhangph.method.ActiveService.Candidate, Object> candidate = cg
				.candidateGeneration(individualHeap, globalHeap, candidateRes,
						k, query.length, miu);
		// System.out.println("-------AFQHGeneration------");
		candidate.entrySet().iterator();
		Map<Integer, Double> res = cv.candidateVerification(
				(Pair[][]) candidate.get(ActiveService.Candidate.CANDIDATERES),
				query, (IndividualHeap[]) candidate
						.get(ActiveService.Candidate.INDIVIDUAL),
				(GlobalHeap) candidate.get(ActiveService.Candidate.GLOBALSET),
				k);
		return res;
	}
	
}
