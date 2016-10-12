package cn.edu.neu.trajectory.construct;

import java.util.*;

public class Table {
    private List<Point> segment = new ArrayList<>();
    private double error;

    public Table() {
        // TODO Auto-generated constructor stub
    }

    public Table(List<Point> segment, double error) {
        this.segment = segment;
        this.error = error;
    }

    public List<Point> getSegment() {
        return segment;
    }

    public void setSegment(List<Point> segment) {
        this.segment = segment;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }


}
