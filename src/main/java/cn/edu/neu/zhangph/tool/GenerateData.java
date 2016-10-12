package cn.edu.neu.zhangph.tool;

import java.io.*;
import java.util.StringTokenizer;
/**
 * Generate data from document
 */
public class GenerateData {
	public static void main(String[] args) {
		String path = "/home/zhangph/workspace/data/t-drive/data";
//		String path2 = System.getProperty("user.dir")+"/src/main/resources/data.txt";
//		String path3 =  System.getProperty("user.dir")+"/src/main/resources/changedata.txt";
//		String path3 = "/home/zhangph/workspace/data/changedata.txt";
		try {
			traverseFolder(path);
//			changeData("./t-drive.txt");
//			lettleDataTest("./t-drive-all.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void lettleDataTest(String path3) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path3));
		FileWriter fw = new FileWriter("t-drive-6000.txt");
		String regex = ",";
//		String tmp = "";
		int count  = 0;
		int flag = 0;
		while(count < 6000){
			String line= bufferedReader.readLine();
			StringTokenizer stk = new StringTokenizer(line, regex);
			int id = Integer.parseInt(stk.nextToken());
			if(flag != id) count++;
			flag = id;
			String x = stk.nextToken();
			String y = stk.nextToken();
			fw.write(line + "\n");
//			System.out.println(count);
		}
		
		fw.flush();
		fw.close();
		bufferedReader.close();
	}

	@SuppressWarnings("unused")
	private static void changeData(String path2) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path2));
		FileWriter fw = new FileWriter("./t-drive-all.txt");
		String line = null;
		String regex = ",";
		String tmp = "";
		int count = -1;
		while((line= bufferedReader.readLine()) != null){
			StringTokenizer stk = new StringTokenizer(line, regex);
			String id = stk.nextToken();
			if(!tmp.equals(id)) count++;
			tmp = id;
			String x = stk.nextToken();
			String y = stk.nextToken();
			fw.write(count + "," + x + "," + y + "\n");
			System.out.println(count);
		}
		
		fw.flush();
		fw.close();
		bufferedReader.close();
	}

	public static void traverseFolder(String path) throws IOException{
		File file = new File(path);
		File[] tempList = file.listFiles();
		System.out.println("该目录下对象个数：" + tempList.length);
		FileWriter fw = new FileWriter("./t-drive.txt", true);
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile() && tempList[i].getAbsolutePath().endsWith(".txt")) {
				System.out.println("文     件：" + tempList[i]);
				String string = tempList[i].getAbsolutePath();
				String id = string.substring(string.lastIndexOf("\\") + 1, string.lastIndexOf(".") );
				try {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(tempList[i]));
					String line = null;
					String regex = ",";
//					for(int j = 0; j < 6; j++){
//						bufferedReader.readLine();
//					}
					while((line = bufferedReader.readLine()) != null){
//						StringTokenizer stk = new StringTokenizer(line, regex);
//						stk.nextToken();
//						stk.nextToken();
//						fw.write(id + "," + stk.nextToken() + "," + stk.nextToken() + "\n");
						String[] split = line.split(regex);
						if(split.length >3){
							fw.write(split[0]+ "," + split[2] + "," + split[3] + "\n");
						}
					}
					fw.flush();
//					fw.close();
					bufferedReader.close();
					if(i == tempList.length - 1)
						fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹：" + tempList[i]);
				traverseFolder(tempList[i].getAbsolutePath());
			}
		}
	}
}
