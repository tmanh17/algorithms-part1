import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private final List<Board> trace;
    private boolean isSolvable = false;
    SearchNode a;
    private static class SearchNode {
        Board board;
        int moves;
        int manhattan;
        SearchNode prev;

        private SearchNode(Board board, int moves, int manhattan, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.manhattan = manhattan;
            this.prev = prev;
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        trace = new ArrayList<>();
        Comparator<SearchNode> comparator1 = (b1, b2) -> {
            if (b1.manhattan + b1.moves > b2.manhattan + b2.moves) {
                return 1;
            }
            return 0;
        };


        MinPQ<SearchNode> pq = new MinPQ<>(comparator1);
        MinPQ<SearchNode> pqTwin = new MinPQ<>(comparator1);
        pq.insert(new SearchNode(initial, 0, initial.manhattan(), null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, initial.manhattan(), null));

        while (!pq.isEmpty() && !pqTwin.isEmpty()) {
            SearchNode curr = pq.delMin();
            SearchNode currTwin = pqTwin.delMin();
            if (curr.board.isGoal()) {
                do {
                    trace.add(curr.board);
                    curr = curr.prev;
                } while (curr != null);
                isSolvable = true;
                return;
            }
            if (currTwin.board.isGoal()) {
                isSolvable = false;
                return;
            }
            for (Board neighbor : curr.board.neighbors()) {
                SearchNode searchNode = new SearchNode(neighbor, curr.moves + 1, neighbor.manhattan(), curr);
                if (curr.prev == null || !searchNode.board.equals(curr.prev.board)) {
                    pq.insert(searchNode);
                }
            }
            for (Board neighbor : currTwin.board.neighbors()) {
                SearchNode searchNode = new SearchNode(neighbor, currTwin.moves + 1, neighbor.manhattan(), currTwin);
                if (currTwin.prev == null || !searchNode.board.equals(currTwin.prev.board)) {
                    pqTwin.insert(searchNode);
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) {
            return -1;
        }
        return trace.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        }
        return () -> new Iterator<>() {
            int i = trace.size() - 1;

            @Override
            public boolean hasNext() {
                return i >= 0;
            }

            @Override
            public Board next() {
                return trace.get(i--);
            }
        };
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}