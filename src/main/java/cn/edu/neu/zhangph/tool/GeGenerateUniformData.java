package cn.edu.neu.zhangph.tool;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class GeGenerateUniformData {
    public static void main( String[] args ) throws IOException    {
    	int tra = 4000;
    	generateUniformData(tra);
//    	randomQuery(20, tra);
//    	uniformQuery(20,tra);
    }
    
    
    private static void uniformQuery(int i, int tra) {
    	String file = "./uniform-1.properties";
    	FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.append("query=");
			int seg = tra/i;
			int x = 0;
			int y = tra * 2 - 1;
			for (int j = 0; j < i; j++) {
				y-=seg;x+=seg;
				fw.append(tra + "," + (y) + "\t");
				fw.append(tra + "," + (x) + "\t");
			}
	    	fw.flush();
	    	fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static void randomQuery(int i, int tra) {
    	Random random = new Random();
    	String file = "./uniform.properties";
    	FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.append("query=");
	    	for(;i > 0; i--){
	    		double x = random.nextInt(tra) + random.nextDouble();
	    		if(x > tra) x -= random.nextInt(tra);
	    		double y = random.nextInt(tra) + random.nextDouble();
	    		if(y > tra) y -= random.nextInt(tra);
	    		fw.append(x + "," + y + "\t");
	    	}
	    	fw.flush();
	    	fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static void generateUniformData(int tra){
    	String file = "./uniform-rand.csv";
    	FileWriter fw;
    	Random rand = new Random();
		try {
			fw = new FileWriter(file);
	    	for (int i = 0; i < tra; i++) {
	    		int line = tra;
				for (int j = 0; j < tra; j++) {
					fw.append(i + "," + rand.nextInt() + "," + rand.nextInt());
					fw.append(System.getProperty("line.separator"));
//					fw.append(line++ + "," + i + "," + j);
//					fw.append(System.getProperty("line.separator"));
				}
			}
	    	fw.flush();
	    	fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }
}
