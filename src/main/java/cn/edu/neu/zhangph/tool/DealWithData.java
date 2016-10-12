package cn.edu.neu.zhangph.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DealWithData {
	public static void main(String[] args) throws IOException {
//		generateData("/home/zhangph/workspace/data/little10000.txt", "");
		String[] files = {"/home/zhangph/workspace/knnt-final/jar/time.txt", "/home/zhangph/workspace/knnt-final/jar/count.txt", "/home/zhangph/workspace/knnt-final/jar/memory.txt"};
		processRes("/home/zhangph/workspace/knnt-final/jar/test.txt",files);
		
	}
	public static void generateData(String inputFile, String outputFile) throws IOException{
		BufferedReader bfr = null;
//		FileWriter writer = null;
		try {
			bfr = new BufferedReader(new FileReader(inputFile) );
//			writer = new FileWriter(outputFile);
			String regex = ",";
			String line = null;
			for(int i = 0; i < 6; i++) bfr.readLine();
			while((line = bfr.readLine()) != null){
				String[] split = line.split(regex);
				int x = Integer.parseInt(split[0]);
				double p1 = Double.parseDouble(split[1]);
				double p2 = Double.parseDouble(split[2]);
				System.out.println(x + "," + p1 + "," + p2);
//				writer.append(p1 + " " + p2);
//				writer.append(System.getProperty("line.separator"));
			}
//			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bfr.close();
//			writer.close();
		}
	}
	
	public static void processRes(String input, String[] output) throws IOException{
		BufferedReader bfr = null;
		FileWriter[] writer = new FileWriter[3];
		try {
			bfr = new BufferedReader(new FileReader(input) );
			writer[0] = new FileWriter(output[0]);
			writer[1] = new FileWriter(output[1]);
			writer[2] = new FileWriter(output[2]);
			String regex = " ";
			String line = null;
			for(int i = 0; i < 6; i++) bfr.readLine();
			while((line = bfr.readLine()) != null){
				String[] split = line.split(regex);
				if(line.endsWith("2016")){
					System.out.println(line + "---------");
					writer[0].append("---------------------"+"\n");
					writer[1].append("---------------------"+"\n");
					writer[2].append("---------------------"+"\n");
				}
				
				if(split[0].equals("time")){
					writer[0].append(split[1] + "\n");
				}else if(split[0].equals("count")){
					writer[1].append(split[1] + "\n");
				}else if(split[0].equals("memory")){
					writer[2].append(split[1] + "\n");
				}
			}
			writer[0].flush();
			writer[1].flush();
			writer[2].flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bfr.close();
			writer[0].close();
			writer[1].close();
			writer[2].close();
		}
	}
}
