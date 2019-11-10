package io.github.marwlod.percolation;

public class QuickFindUF {
    private int[] id;

    public QuickFindUF(int n) {
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
    }

    public void union(int a, int b) {
        for (int i = 0; i < id.length; i++) {
            if (id[i] == id[a]) {
                id[i] = id[b];
            }
        }
    }

    public boolean connected(int a, int b) {
        return id[a] == id[b];
    }
}
