/**
 *  @author : Aakshaye M Gaikar
 *  @version: 1.0
 *  @revision: 1
 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * Percolation class implementing methods as per API design
 */
public class Percolation {
    private final int n; // grid size
    private boolean[][] grid; // n x n grid
    private final WeightedQuickUnionUF uf; // weighted union find object
    private final int numSites; // total sites incl. virtual top & bottom
    private int openSites; // open site count

    /**
     * Initialize grid to be blocked and other instance variables
     * @param n grid size
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        grid = new boolean[n+1][n+1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grid[i][j] = false;
            }
        }
        numSites = (n * n) + 2;
        uf = new WeightedQuickUnionUF(numSites); // virtual top and bottom sites
        openSites = 0;
        connectTopBottomLayer();
    }

    /**
     * Open a site in the grid and increment their count
     * @param row   row number
     * @param col   col number
     */
    public void open(int row, int col) {
        validateIndex(row, col);
        if (!grid[row][col]) {
            grid[row][col] = true;
            openSites++;
        } else {
            return;
        }
        // top
        if ((row - 1) > 0 && isOpen(row - 1, col) &&
                !uf.connected(rowcolTo1D(row, col), rowcolTo1D(row - 1, col))) {
            uf.union(rowcolTo1D(row, col), rowcolTo1D(row -1, col));
        }
        // right
        if ((col + 1) <= n && isOpen(row, col + 1) &&
                !uf.connected(rowcolTo1D(row, col), rowcolTo1D(row, col +1))) {
            uf.union(rowcolTo1D(row, col), rowcolTo1D(row, col + 1));
        }
        // bottom
        if ((row + 1) <= n && isOpen(row + 1, col) &&
                !uf.connected(rowcolTo1D(row, col), rowcolTo1D(row + 1, col))) {
            uf.union(rowcolTo1D(row, col), rowcolTo1D(row + 1, col));
        }
        // left
        if ((col - 1) > 0 && isOpen(row, col - 1) &&
                !uf.connected(rowcolTo1D(row, col), rowcolTo1D(row, col - 1))) {
            uf.union(rowcolTo1D(row, col), rowcolTo1D(row, col - 1));
        }
    }

    /**
     * Check if given site is open
     * @param row   row number
     * @param col   col number
     * @return
     */
    public boolean isOpen(int row, int col) {
        validateIndex(row, col);
        if (grid[row][col]) return true;
            return false;
    }

    /**
     * Check if given site is filled
     * @param row   row number
     * @param col   col number
     * @return
     */
    public boolean isFull(int row, int col) {
        validateIndex(row, col);
        // root is virtual top
        if (isOpen(row, col) && uf.connected(rowcolTo1D(row, col), 0))
            return true;
        return false;
    }

    /**
     * Return count of open sites
     * @return  number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }

    /**
     * Return whether system percolates or not
     * @return  boolean
     */
    public boolean percolates() {
        if (uf.connected(0, numSites -1) && numberOfOpenSites() > 0)
            return true;
        return false;
    }

    /**
     * Give 1 dimensional value of 2 dimensional grid
     * @param row   row number
     * @param col   col number
     * @return  site number for uf object
     */
    private int rowcolTo1D(int row, int col) {
        validateIndex(row, col);
        return col + (row - 1) * n;
    }

    /**
     * Connect top row to virtual top site and bottom row to virtual bottom
     */
    private void connectTopBottomLayer() {
        // Connect top layer to virtual top
        for (int i = 1; i <= n; i++) {
            uf.union(0, i);
        }
        // Connect bottom layer to virtual bottom
        for (int i = this.numSites - 2; i >= numSites - n - 1; i--) {
            uf.union(numSites -1, i);
        }
    }

    /**
     * Row and column indices should be between 1 and n both inclusive
     * @param row   row number
     * @param col   col number
     */
    private void validateIndex(int row, int col) {
        if (row <= 0 || row > n) {
            throw new IllegalArgumentException("row Index out of bounds" + row);
        }
        if (col <= 0 || col > n) {
            throw new IllegalArgumentException("column Index out of bounds" + col);
        }
    }

    /**
     * Main method for testing above methods
     * @param args  0 - input file path
     */
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system

        // repeatedly read in sites to open and draw resulting system
        Percolation perc = new Percolation(n);
         while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            StdOut.println(i + " " + j);
            perc.open(i, j);
        }
    }
}
