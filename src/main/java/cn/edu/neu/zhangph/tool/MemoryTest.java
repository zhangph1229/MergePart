package cn.edu.neu.zhangph.tool;

import java.util.Date;

public class MemoryTest{
	private static Runtime run = Runtime.getRuntime();
	public static double memoryTestPro() {
		try {
			Thread t = new Thread();
			t.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		} // 暂停程序执行
		System.out.println("memory> total:" + run.totalMemory() + " free:"
				+ run.freeMemory() + " used:"
				+ (run.totalMemory() - run.freeMemory()));
		run.gc();
		System.out.println("time: " + (new Date()));
		// 获取开始时内存使用量
		long startMem = run.totalMemory() - run.freeMemory();
		return startMem;
	}
	
	public static double memoryTestPost(double startMem){
		 System.out.println("time: " + (new Date()));
		 long endMem = run.totalMemory()-run.freeMemory();
		 System.out.println("memory> total:" + run.totalMemory() + " free:" +
		 run.freeMemory() + " used:" + endMem);
		 System.out.println("memory " + (endMem-startMem));
		return endMem;
	}
}
