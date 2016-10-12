package cn.edu.neu.zhangph.method;

import java.io.IOException;
import java.util.Properties;

public class MyProperty{
	public final static Properties props = new Properties();
	static {
		try {
			props.load(MyProperty.class.getClassLoader().getResourceAsStream(
					"cn/edu/neu/zhangph/method/skywed-3.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getProperty(String key){
		return props.getProperty(key);
	}
}

