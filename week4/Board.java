import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private final int[][] tiles;
    private final int n;


    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, n);
        }
    }


    private int location2Value(int x, int y) {
        if (x == n - 1 && y == n - 1) return 0;
        return x * n + y + 1;
    }

    // string representation of this board
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(this.n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ret.append(tiles[i][j]).append(" ");
            }
            ret.append("\n");
        }

        return ret.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int ret = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) continue;
                if (tiles[i][j] != location2Value(i, j)) {
                    ret++;
                }
            }
        }

        return ret;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int ret = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) continue;
                int xVal = (int) (Math.ceil(tiles[i][j] * 1.0 / n) - 1);
                int yVal = (int) (Math.ceil(tiles[i][j] % n) - 1);
                if (yVal == -1) {
                    yVal = n - 1;
                }
                ret += Math.abs(xVal - i) + Math.abs(yVal - j);
            }
        }

        return ret;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != location2Value(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board)) {
            return false;
        }
        Board board = (Board) y;
        if (this.n != board.n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != board.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] X = {-1, 0, 0, 1};
        int[] Y = {0, 1, -1, 0};
        List<Board> boards = new ArrayList<>();
        int x0 = -1, y0 = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    x0 = i;
                    y0 = j;
                    break;
                }
            }
        }

        for (int k = 0; k < 4; k++) {
            int x = x0 + X[k];
            int y = y0 + Y[k];

            if (x >= 0 && x < n && y >= 0 && y < n) {
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    System.arraycopy(this.tiles[i], 0, tiles[i], 0, n);
                }
                tiles[x0][y0] = tiles[x][y];
                tiles[x][y] = 0;
                Board board = new Board(tiles);
                boards.add(board);
            }
        }


        return () -> new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < boards.size();
            }

            @Override
            public Board next() {
                return boards.get(i++);
            }
        };
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board board = new Board(tiles);
        int x0 = -1, x1 = -1, y0 = -1, y1 = -1;
        if (board.tiles[0][0] != 0) {
            x0 = 0;
            y0 = 0;
        }
        if (board.tiles[0][1] != 0) {
            if (x0 == -1) {
                x0 = 0;
                y0 = 1;
            } else {
                x1 = 0;
                y1 = 1;
            }
        }
        if (board.tiles[1][1] != 0) {
            x1 = 1;
            y1 = 1;
        }

        int tmp = board.tiles[x0][y0];
        board.tiles[x0][y0] = board.tiles[x1][y1];
        board.tiles[x1][y1] = tmp;

        return board;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int tiles[][] = new int[][]{
                {3, 2},
                {0, 1}
        };

        Board board1 = new Board(tiles);
        Board board2 = new Board(tiles);

        StdOut.println(board1.manhattan());
        StdOut.println(board1.isGoal());
        StdOut.println(board1.equals(board2));
    }

}