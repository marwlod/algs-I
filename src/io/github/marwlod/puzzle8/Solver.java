package io.github.marwlod.puzzle8;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private boolean solvable;
    private int moves;
    private final Iterable<Board> solution;

    private static class SearchNode {
        private final Board board;
        private final int manhattan;
        private final int movesSoFar;
        private final SearchNode prev;

        private SearchNode(Board board, int movesSoFar, SearchNode prev) {
            this.board = board;
            this.manhattan = board.manhattan();
            this.movesSoFar = movesSoFar;
            this.prev = prev;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("No board to solve specified!");
        MinPQ<SearchNode> pq = new MinPQ<>(Comparator.comparingInt(node -> node.manhattan + node.movesSoFar));
        MinPQ<SearchNode> pqTwin = new MinPQ<>(Comparator.comparingInt(node -> node.manhattan + node.movesSoFar));
        SearchNode curr = new SearchNode(initial, 0, null);
        SearchNode currTwin = new SearchNode(initial.twin(), 0, null);
        while (!curr.board.isGoal() && !currTwin.board.isGoal()) {
            for (Board neighbor : curr.board.neighbors()) {
                if (!neighbor.equals(curr.prev == null ? null : curr.prev.board)) {
                    pq.insert(new SearchNode(neighbor, curr.movesSoFar + 1, curr));
                }
            }
            for (Board neighbor : currTwin.board.neighbors()) {
                if (!neighbor.equals(currTwin.prev == null ? null : currTwin.prev.board)) {
                    pqTwin.insert(new SearchNode(neighbor, curr.movesSoFar + 1, currTwin));
                }
            }
            curr = pq.delMin();
            currTwin = pqTwin.delMin();
        }
        if (currTwin.board.isGoal()) {
            this.solvable = false;
            this.moves = -1;
            this.solution = null;
            return;
        }
        Stack<Board> boardStack = new Stack<>();
        int moveCount = 0;
        boardStack.push(curr.board);
        curr = curr.prev;
        while (curr != null) {
            boardStack.push(curr.board);
            curr = curr.prev;
            moveCount++;
        }
        List<Board> boards = new ArrayList<>();
        while (!boardStack.isEmpty()) {
            boards.add(boardStack.pop());
        }
        this.solvable = true;
        this.moves = moveCount;
        this.solution = boards;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solvable ? solution : null;
    }

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
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
