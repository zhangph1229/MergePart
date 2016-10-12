package cn.edu.neu.compare.douglas;

import java.util.Random;


public class Douglas_Peucker_Algorithm {
    public static void main(String args[]) {
        Random random = new Random();
        double[] points = new double[20];
        double[] fpoints;
        for (int i = 0; i < points.length; i++)
            points[i] = random.nextInt(10);
        RamerDouglasPeuckerFilter rdpf = new RamerDouglasPeuckerFilter(1);
        fpoints = rdpf.filter(points);

        System.out.println("Orginal points");
        for (int i = 0; i < points.length; i++)
            System.out.print(points[i] + " ");

        System.out.println("\nFiltered points");
        for (int i = 0; i < fpoints.length; i++)
            System.out.print(fpoints[i] + " ");
    }
}