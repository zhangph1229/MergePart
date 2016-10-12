package cn.edu.neu.trajectory.construct;

import java.util.*;

public class Trajectory {
    private static int tid;
    private static List<Node> list_node;
    private static List<Point> list_point;
    private static double error;

    public Trajectory() {
        // TODO Auto-generated constructor stub
    }

    public Trajectory(int tid) {
        this.tid = tid;
    }

    public Trajectory(int tid, double error, List<Point> list) {
        this.tid = tid;
        this.list_point = list;
        this.error = error;
    }

    public Trajectory(int tid, List<Node> list, double error) {
        this.tid = tid;
        this.list_node = list;
        this.error = error;
    }

    public static List<Node> getList() {
        return list_node;
    }

    public static void setList(List<Node> list) {
        Trajectory.list_node = list;
    }

    public static int getTid() {
        return tid;
    }

    public static void setTid(int tid) {
        Trajectory.tid = tid;
    }

    public static double getError() {
        return error;
    }

    public static void setError(double error) {
        Trajectory.error = error;
    }

    public static List<Point> getList_point() {
        return list_point;
    }

    public static void setList_point(List<Point> list_point) {
        Trajectory.list_point = list_point;
    }


}
