//package manhdt.week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }

            thresholds[i] = percolation.numberOfOpenSites() * 1.0 / (n * n);
        }
        this.mean = StdStats.mean(thresholds);
        this.stddev = StdStats.stddev(thresholds);
        this.confidenceLo = this.mean - (1.96 * this.stddev / Math.sqrt(trials));
        this.confidenceHi = this.mean + (1.96 * this.stddev / Math.sqrt(trials));
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.confidenceLo;
    }

    public double confidenceHi() {
        return this.confidenceHi;
    }

    public static void main(String[] args) {
        //PercolationStats ps = new PercolationStats(2, 100000);
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}