package main.java.io.github.marwlod.percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double confidenceFactor;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("both arguments must be above 0");

        double[] fractions = new double[trials];
        int first, second;
        double opened;
        Percolation percolation;
        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            opened = 0;
            while (!percolation.percolates()) {
                first = StdRandom.uniform(1, n + 1);
                second = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(first, second)) {
                    percolation.open(first, second);
                    opened++;
                }
            }
            fractions[i] = opened / (n * n);
        }
        this.mean = StdStats.mean(fractions);
        this.stddev = StdStats.stddev(fractions);
        this.confidenceFactor = CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - confidenceFactor;
    }

    public double confidenceHi() {
        return mean + confidenceFactor;
    }

    public static void main(String[] args) {
        if (args.length != 2) return;
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean = " + percolationStats.mean());
        System.out.println("stddev = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}
