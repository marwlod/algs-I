package io.github.marwlod.percolation;

public class MyWeightedQuickUnionUF {
    private int[] id;
    private int[] sizes;

    public MyWeightedQuickUnionUF(int n) {
        id = new int[n];
        sizes = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
            sizes[i] = 1;
        }
    }

    public int root(int p) {
        while (id[p] != p) {
            id[p] = id[id[p]];
            p = id[p];
        }
        return p;
    }

    public void union(int p, int q) {
        int rp = root(p);
        int rq = root(q);
        if (rp == rq) return;
        if (sizes[rp] < sizes[rq]) {
            id[rp] = rq;
            sizes[rq] += sizes[rp];
        } else {
            id[rq] = rp;
            sizes[rp] += sizes[rq];
        }
    }

    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }
}
