package io.github.marwlod.percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int side;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final WeightedQuickUnionUF weightedQuickUnionUFPercol;
    private final int topIndex;
    private final int botIndex;
    private int openSites;
    private boolean[] isOpen;


    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("number cannot be less than 1");
        side = n;
        weightedQuickUnionUF = new WeightedQuickUnionUF(side * side + 2);
        weightedQuickUnionUFPercol = new WeightedQuickUnionUF(side * side + 2);
        topIndex = side * side;
        botIndex = side * side + 1;
        openSites = 0;
        isOpen = new boolean[side * side];
    }

    public void open(int row, int col) {
        if (row <= 0 || row > side || col <= 0 || col > side) throw new IllegalArgumentException("row and/or column out of bounds");
        int index = calcIndex(row, col);
        if (!isOpen[index]) {
            isOpen[index] = true;
            openSites++;

            unionWithNeighbor(index, row, col - 1);
            unionWithNeighbor(index, row, col + 1);
            unionWithNeighbor(index, row - 1, col);
            unionWithNeighbor(index, row + 1, col);
        }
    }

    private void unionWithNeighbor(int index, int row, int col) {
        int neighborIndex = calcIndex(row, col);
        if (neighborIndex != -1 && isOpen[neighborIndex]) {
            weightedQuickUnionUF.union(index, neighborIndex);
            weightedQuickUnionUFPercol.union(index, neighborIndex);
        } else if (neighborIndex == -1) {
            if (row == 0) {
                weightedQuickUnionUF.union(index, topIndex);
                weightedQuickUnionUFPercol.union(index, topIndex);
            } else if (row == side + 1) {
                weightedQuickUnionUFPercol.union(index, botIndex);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > side || col <= 0 || col > side) throw new IllegalArgumentException("row and/or column out of bounds");
        return isOpen[calcIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        if (row <= 0 || row > side || col <= 0 || col > side) throw new IllegalArgumentException("row and/or column out of bounds");
        return weightedQuickUnionUF.connected(calcIndex(row, col), topIndex);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return weightedQuickUnionUFPercol.connected(topIndex, botIndex);
    }

    private int calcIndex(int row, int col) {
        if (row <= 0 || row > side || col <= 0 || col > side) return -1;
        return (row - 1) * side + col - 1;
    }
}
