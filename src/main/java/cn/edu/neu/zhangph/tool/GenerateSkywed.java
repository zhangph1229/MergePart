package cn.edu.neu.zhangph.tool;

import java.io.*;
import java.util.Random;

public class GenerateSkywed {
	public static void main(String[] args) throws IOException {
		int num = 2800;
		generationData(num);
//		generationProp();
	}
	public static void generationProp() throws IOException{
		String file = "./skywed.properties";
		FileWriter fw = new FileWriter(new File(file));
		fw.append("query=");
		for (int i = 0; i < 25; i++) {
			double x = Norm_rand(0,1);
			double y = Norm_rand(0,1);
			fw.append(x+","+y);
			fw.append("\t");
		}
		fw.flush();
		fw.close();
	}
	public static void generationData(int num) throws IOException{
		String file = "./skywed.csv";
		Random rand = new Random();
		FileWriter fw = new FileWriter(new File(file));
		for(int i = 0; i < num*num; i++){
			double x = Norm_rand(0,1);
			double y = Norm_rand(0,1);
			fw.append(rand.nextInt(num) + "," + x + "," + y);
			fw.append(System.getProperty("line.separator"));
		}
		fw.flush();
		fw.close();
	}
	private static double Norm_rand(double miu, double sigma2) {
		double N = 12;
		double x = 0, temp = N;
		do {
			x = 0;
			for (int i = 0; i < N; i++)
				x = x + (Math.random());
			x = (x - temp / 2) / (Math.sqrt(temp / 12));
			x = miu + x * Math.sqrt(sigma2);
		} while (x <= 0); // 在此我把小于0的数排除掉了
		return x;
	}
}
