package cn.edu.neu.trajectory.construct;

/**
 * Construction of the trajectory's points
 *
 * @author zhangph
 */
public class Point {
    private double x, y;
    private int index; // 锟矫碉拷锟斤拷锟斤拷锟斤拷锟斤拷锟�

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Point() {

    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
    }

    public Point(int index) {
        this.index = index;
    }
}


