/**
 *  @author : Aakshaye M Gaikar
 *  @version: 1.0
 *  @revision: 1
 */
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
/**
 * PercolationStats class implementing methods as per API design
 */
public class PercolationStats {
    private static final double CONFCONS = 1.96; // Confidence constant
    private final int t; // number of trials
    private final double[] threshold; // values where percolation is reached
    private final double mean; // mean of threshold
    private final double stddev; // std. deviation of threshold

    /**
     * Perform trials independent experiments on an n-by-n grid
      */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.t = trials;
        threshold = new double[trials];

        Percolation p;
        int trialNum = 0;
        while (trialNum < t) {
            p = new Percolation(n);
            while (!p.percolates()) {
                int row = genRandomNum(1, n+1);
                int col = genRandomNum(1, n+1);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                }
            }
            threshold[trialNum] = (double) p.numberOfOpenSites()/(n*n);
            trialNum++;
        }
        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
    }

    /**
     * sample mean of percolation threshold
     * @return mean value
      */
    public double mean() {
        return mean;
    }

    /** sample standard deviation of percolation threshold
     * @return standard deviation
     */
    public double stddev() {
        return stddev;
    }

    /** low  endpoint of 95% confidence interval
     * @return formula result
     */
    public double confidenceLo() {
        return mean - (CONFCONS*stddev/Math.sqrt(t));
    }

    /** high endpoint of 95% confidence interval
     * @return formula result
     */
    public double confidenceHi() {
        return mean + (CONFCONS*stddev/Math.sqrt(t));
    }

    /**
     * Random number for site opening
     * @param start integer
     * @param end   integer
     * @return  random number
     */
    private static int genRandomNum(int start, int end) {
        return StdRandom.uniform(start, end);
    }

    /**
     * Main method for testing above methods
     * @param args  0 - grid size n
     *              1 - number of trials
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
