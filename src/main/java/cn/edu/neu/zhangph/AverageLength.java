package cn.edu.neu.zhangph;

import java.io.*;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
public class AverageLength {
	public static void main(String[] args) throws Exception {
//		String path = "/home/zhangph/workspace/data/t-drive/data";
//		String path2 = System.getProperty("user.dir")+"/src/main/resources/data.txt";
//		String path3 =  System.getProperty("user.dir")+"/src/main/resources/changedata.txt";
//		String path3 = "/home/zhangph/workspace/data/changedata.txt";
		try {
//			traverseFolder(path);
//			changeData("./t-drive-10-12.txt");
			lettleDataTest("./t-drive-all.txt");
			
			//read zip file
//			String file = "F:/program/java/data/t-drive";
//			traverseFolder(file);
			
			//average length of trajectory data set
//			String t_driver = "./t-drive-10-12.txt";
//			System.out.println("T-Drive average length of trajectory "  + averageLen(t_driver));
//			String geolife = "F:/program/java/knnt-final/knnt-singleton/data/geolife-data.txt";
//			System.out.println("Geolife average length of trajectory "  + averageLen(geolife));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//average length for each trajectory
	public static int averageLen(String file) throws IOException{
		if(file == null ) return 0;
		BufferedReader buf = new BufferedReader(new FileReader(file));
		//skip the head just for geolife
//		for (int i = 0; i < 6; i++) {
//			buf.readLine();
//		}
		
		String line = null;
		long flag = 0l;
		int count = 0;
		int cur = 0;
		int sum = 0;
		while((line = buf.readLine()) != null){
//			System.out.println(line);
			String[] res = line.split(",");
			if(flag == Long.parseLong(res[0])){
				count++;
			}else {
				flag = Long.parseLong(res[0]);
				sum += count;
				count = 0;
				cur++;
			}
		}
		System.out.println(sum);
		System.out.println(cur);
		return sum / (cur + 1);
	}
	
	
	public static void traverseFolder(String path) throws Exception{
		File file = new File(path);
		File[] tempList = file.listFiles();
		System.out.println("该目录下对象个数：" + tempList.length);
		FileWriter fw = new FileWriter("./t-drive.txt", true);
		
		for (int i = 0; i < tempList.length; i++) { //文件夹
//			if (tempList[i].isFile() && tempList[i].getAbsolutePath().endsWith(".txt")) {
//				System.out.println("文     件：" + tempList[i]);
//				String string = tempList[i].getAbsolutePath();
//				String id = string.substring(string.lastIndexOf("\\") + 1, string.lastIndexOf(".") );
//				try {
//					BufferedReader bufferedReader = new BufferedReader(new FileReader(tempList[i]));
//					String line = null;
//					String regex = ",";
//					for(int j = 0; j < 6; j++){
//						bufferedReader.readLine();
//					}
//					while((line = bufferedReader.readLine()) != null){
//						StringTokenizer stk = new StringTokenizer(line, regex);
//						stk.nextToken();
//						stk.nextToken();
//						fw.write(id + "," + stk.nextToken() + "," + stk.nextToken() + "\n");
//						String[] split = line.split(regex);
//						if(split.length >3){
//							fw.write(split[0]+ "," + split[2] + "," + split[3] + "\n");
//						}
//					}
//					fw.flush();
//					fw.close();
//					bufferedReader.close();
//					if(i == tempList.length - 1)
//						fw.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			readZipFile(tempList[i]);
			if (tempList[i].isDirectory()) {
				System.out.println("文件夹：" + tempList[i]);
				traverseFolder(tempList[i].getAbsolutePath());
			}
		}
	}
    public static void readZipFile(File file) throws Exception {  
        ZipFile zf = new ZipFile(file);  
        InputStream in = new BufferedInputStream(new FileInputStream(file));  
        ZipInputStream zin = new ZipInputStream(in);  
        ZipEntry ze;  
        while ((ze = zin.getNextEntry()) != null) {  
            if (ze.isDirectory()) {
            } else {  
                System.err.println("file - " + ze.getName() + " : "  
                        + ze.getSize() + " bytes");  
                long size = ze.getSize();  
                if (size > 0) {  
                    BufferedReader br = new BufferedReader(  
                            new InputStreamReader(zf.getInputStream(ze)));  
                    FileWriter fw = new FileWriter("./t-drive-10-12.txt", true);
                    String line;  
                    while ((line = br.readLine()) != null) {  
//                        System.out.println(line);  
                        fw.append(line + "\n");
                    }  
                    fw.flush();
                    fw.close();
                    br.close();  
                }  
                System.out.println();  
            }  
        }  
        zin.closeEntry();  
    }  
	
	private static void lettleDataTest(String path3) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path3));
		FileWriter fw = new FileWriter("t-drive-10.txt");
		String regex = ",";
//		String tmp = "";
		int count  = 0;
		int flag = 0;
		while(count < 1000){
			String line= bufferedReader.readLine();
			StringTokenizer stk = new StringTokenizer(line, regex);
			int id = Integer.parseInt(stk.nextToken());
			if(flag != id) count++;
			flag = id;
			String x = stk.nextToken();
			String y = stk.nextToken();
			if(count > 990)fw.write(line + "\n");
//			System.out.println(count);
		}
		
		fw.flush();
		fw.close();
		bufferedReader.close();
	}

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
			stk.nextToken();
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


}

