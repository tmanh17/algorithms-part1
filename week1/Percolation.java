//package manhdt.week1;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int[] X = {-1, 0, 0, 1};
    private static final int[] Y = {0, -1, 1, 0};
    private final WeightedQuickUnionUF dsu;
    private final WeightedQuickUnionUF dsuBackwash; // handle backwash in isFull method
    private final boolean[][] grids;
    private final int n;
    private int numberOfOpenSites;
    private final int virtualTopId;
    private final int virtualBottomId;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.virtualTopId = 0;
        this.virtualBottomId = n * n + 1;
        this.grids = new boolean[n + 1][n + 1];
        this.dsu = new WeightedQuickUnionUF(n * n + 2); // extra 2 virtual nodes
        this.dsuBackwash = new WeightedQuickUnionUF(n * n + 1); // extra 1 virtual nodes

        for (int i = 1; i <= n; i++) {
            this.dsu.union(virtualTopId, getId(1,i)); // connect virtual top node
            this.dsu.union(virtualBottomId, getId(n, i)); // connect virtual bottom node

            this.dsuBackwash.union(virtualTopId, getId(1,i)); // connect virtual top node
        }
    }

    private int getId(int row, int col) {
        return this.n * (row - 1) + col;  // range Id 1 -> n * n
    }

    public void open(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException();
        if (isOpen(row, col)) {
            return;
        }

        this.numberOfOpenSites++;
        grids[row][col] = true;

        for (int i = 0; i < 4; i++) {
            int x = row + X[i];
            int y = col + Y[i];

            if (isValid(x, y) && isOpen(x, y)) {
                this.dsu.union(getId(row, col), getId(x, y));
                this.dsuBackwash.union(getId(row, col), getId(x, y));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException();
        return grids[row][col];
    }

    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) throw new IllegalArgumentException();
        return isOpen(row, col) && (dsuBackwash.find(this.virtualTopId) == dsuBackwash.find(getId(row, col)));
    }

    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    public boolean percolates() {
        if (this.n == 1 && this.numberOfOpenSites == 0) {
            return false;
        }
        return dsu.find(this.virtualTopId) == dsu.find(this.virtualBottomId);
    }

    private boolean isValid(int row, int col) {
        return (row >= 1 && row <= n && col >= 1 && col <= n);
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(1);

        System.out.println(percolation.isFull(1,1));
        System.out.println(percolation.percolates());
    }
}

