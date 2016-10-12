package cn.edu.neu.zhangph.util;
import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Point;
public class Pair {
	private Point p;
	private Point q;
	private int flag; //p id
	private double score;
	private Geometry geometry;
	public Pair() {	}
	public Pair(Point p, Point q){
		this.p = p;
		this.q = q;
	}
	public Pair(Point p, Point q, int flag){
		this.p = p;
		this.q = q;
		this.flag = flag;
	}
	public Pair(Point p, Point q, int flag, double score){
		this.p = p;
		this.q = q;
		this.flag = flag;
		this.score = score;
	}
	public Pair(int i, Geometry geometry, double score2) {
		this.geometry = geometry;
		this.flag = i;
		this.score = score2;
	}
	public Point getP() {
		return p;
	}
	public void setP(Point p) {
		this.p = p;
	}
	public Point getQ() {
		return q;
	}
	public void setQ(Point q) {
		this.q = q;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public Geometry getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}
