package cn.edu.neu.zhangph.composite;

import java.util.StringTokenizer;

import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

import cn.edu.neu.zhangph.method.MyProperty;
/**
 * 通过配置文件创建查询点集合
 * @author zhangph
 *
 */
public class CommonCreateQuery {
	/**
	 * @param len 查询点的数量
	 * @return 查询点集合
	 */
	public static Point[] createQuery(int len){
		String queries = MyProperty.props.getProperty("query");
		String[] res = queries.split("\t");
		int length;
		if(len < res.length) length = len;
		else  length = res.length;
		Point[] query = new Point[length];
		for (int i = 0; i < res.length && i < len ; i++) {
			StringTokenizer stringTokenizer = new StringTokenizer(res[i], ",");
			query[i] = Geometries.point(Float.parseFloat(stringTokenizer.nextToken().trim()),Float.parseFloat(stringTokenizer.nextToken().trim()));
		}
		return query;
	}
}
