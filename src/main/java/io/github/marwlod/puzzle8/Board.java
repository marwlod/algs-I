package main.java.io.github.marwlod.puzzle8;

import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int dimension;
    private final int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.dimension = tiles.length;
        final int[][] tilesCopy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(tiles[i], 0, tilesCopy[i], 0, dimension);
        }
        this.tiles = tilesCopy;
        int currTarget = 1, totalManhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int currTile = tiles[i][j];
                if (currTile != 0 && currTile != currTarget) {
                    int xDist = Math.abs(((currTile - 1) % dimension) - j);
                    int yDist = Math.abs(((currTile - 1) / dimension) - i);
                    totalManhattan += xDist + yDist;
                }
                currTarget++;
            }
        }
        this.manhattan = totalManhattan;
    }

    // string representation of this board
    public String toString() {
        final StringBuilder tilesBuilder = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                tilesBuilder.append("\t").append(tiles[i][j]);
            }
            if (i < dimension - 1) {
                tilesBuilder.append("\n");
            }
        }
        return dimension + "\n" + tilesBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0, currTarget = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != currTarget) {
                    hamming++;
                }
                if (i == dimension - 1 && j == dimension - 2) {
                    break;
                }
                currTarget++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int currTarget = 1;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != currTarget) {
                    return false;
                }
                if (currTarget == dimension * dimension - 1) {
                    currTarget = 0;
                } else {
                    currTarget++;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int x = 0, y = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        Iterable<Board> neighbors;
        int[][] n1tiles = new int[dimension][dimension];
        int[][] n2tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(tiles[i], 0, n1tiles[i], 0, dimension);
            System.arraycopy(tiles[i], 0, n2tiles[i], 0, dimension);
        }

        // every tile that's not on the perimeter -> 4 neighbours
        if (x > 0 && x < dimension-1 && y > 0 && y < dimension-1) {
            int[][] n3tiles = new int[dimension][dimension];
            int[][] n4tiles = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                System.arraycopy(tiles[i], 0, n3tiles[i], 0, dimension);
                System.arraycopy(tiles[i], 0, n4tiles[i], 0, dimension);
            }
            swap(n1tiles, x, y, x-1, y);
            swap(n2tiles, x, y, x+1, y);
            swap(n3tiles, x, y, x, y-1);
            swap(n4tiles, x, y, x, y+1);

            neighbors = Arrays.asList(new Board(n1tiles), new Board(n2tiles), new Board(n3tiles), new Board(n4tiles));
        }
        // corner tile -> 2 neighbours
        else if (x == 0 && y == 0) {
            swap(n1tiles, x, y, x+1, y);
            swap(n2tiles, x, y, x, y+1);
            neighbors = Arrays.asList(new Board(n1tiles), new Board(n2tiles));
        } else if (x == 0 && y == dimension-1) {
            swap(n1tiles, x, y, x+1, y);
            swap(n2tiles, x, y, x, y-1);
            neighbors = Arrays.asList(new Board(n1tiles), new Board(n2tiles));
        } else if (x == dimension-1 && y == 0) {
            swap(n1tiles, x, y, x-1, y);
            swap(n2tiles, x, y, x, y+1);
            neighbors = Arrays.asList(new Board(n1tiles), new Board(n2tiles));
        } else if (x == dimension-1 && y == dimension-1) {
            swap(n1tiles, x, y, x-1, y);
            swap(n2tiles, x, y, x, y-1);
            neighbors = Arrays.asList(new Board(n1tiles), new Board(n2tiles));
        }
        // perimeter except corner -> 3 neighbours
        else {
            int[][] n3tiles = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                System.arraycopy(tiles[i], 0, n3tiles[i], 0, dimension);
            }
            if (x == 0 && y < dimension-1) {
                swap(n1tiles, x, y, x+1, y);
                swap(n2tiles, x, y, x, y+1);
                swap(n3tiles, x, y, x, y-1);
            } else if (x == dimension-1 && y < dimension-1) {
                swap(n1tiles, x, y, x-1, y);
                swap(n2tiles, x, y, x, y+1);
                swap(n3tiles, x, y, x, y-1);
            } else if (x < dimension-1 && y == 0) {
                swap(n1tiles, x, y, x+1, y);
                swap(n2tiles, x, y, x-1, y);
                swap(n3tiles, x, y, x, y+1);
            } else if (x < dimension-1 && y == dimension-1) {
                swap(n1tiles, x, y, x+1, y);
                swap(n2tiles, x, y, x-1, y);
                swap(n3tiles, x, y, x, y-1);
            }

            neighbors = Arrays.asList(new Board(n1tiles), new Board(n2tiles), new Board(n3tiles));
        }
        return neighbors;
    }

    private void swap(int[][] arr, int x1, int y1, int x2, int y2) {
        int temp = arr[x1][y1];
        arr[x1][y1] = arr[x2][y2];
        arr[x2][y2] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        boolean exchanged = false;
        int[][] twin = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (!exchanged && j < dimension - 1 && tiles[i][j] != 0 && tiles[i][j+1] != 0) {
                    twin[i][j+1] = tiles[i][j];
                    twin[i][j] = tiles[i][j+1];
                    exchanged = true;
                    j++;
                } else {
                    twin[i][j] = tiles[i][j];
                }
            }
        }
        return new Board(twin);
    }

    // does this board equal y?
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Board board = (Board) other;
        return dimension == board.dimension &&
                Arrays.deepEquals(tiles, board.tiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        final int[][] tiles = {
                {0,  1},
                {2,  3}
        };

        final Board board = new Board(tiles);
        final Board board1 = new Board(tiles);
        System.out.println(board.equals(board1));
        System.out.println(board);
        System.out.println(board.hamming());
        System.out.println(board.isGoal());
        System.out.println(board.twin());
        System.out.println(board.manhattan());
        board.neighbors().forEach(System.out::println);
    }
}
