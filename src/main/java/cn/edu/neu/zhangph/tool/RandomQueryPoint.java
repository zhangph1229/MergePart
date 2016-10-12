package cn.edu.neu.zhangph.tool;

import java.util.Random;

public class RandomQueryPoint {
	public static void main(String[] args) {
		Random random = new Random();
		
		for(int i = 0 ; i < 20; i++){
			System.out.print(random.nextInt(2000) + "," + random.nextInt(2000));
			System.out.print("\t");
		}
		
	}
}