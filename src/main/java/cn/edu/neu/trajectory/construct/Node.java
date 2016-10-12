package cn.edu.neu.trajectory.construct;

import java.util.*;

/**
 * 锟节碉拷锟斤拷锟斤拷萁峁�
 *
 * @author zhangph
 *         |segment| error | parent | stack |
 *         | 锟竭讹拷       |  锟斤拷锟�    |  锟斤拷锟节碉拷    |   栈      |
 *         |segment| double|segment | ...   |
 */
public class Node {
    private List<Point> segment;
    private double error;
    private Node parent;
    private Stack<Table> stack;

    public Node() {
        // TODO Auto-generated constructor stub
    }

    public Node(List<Point> segment, double error) {
        this.segment = segment;
        this.error = error;
    }

    public Node(List<Point> segment, double error, Node parent) {
        this.parent = parent;
        this.segment = segment;
        this.error = error;
    }

    public Node(List<Point> segment, double error, Node parent, Stack<Table> stack) {
        this.stack = stack;
        this.parent = parent;
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

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Stack<Table> getStack() {
        return stack;
    }

    public void setStack(Stack<Table> stack) {
        this.stack = stack;
    }
}
