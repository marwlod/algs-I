package io.github.marwlod.randomized_queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) return;
        int num = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int i = 0;
        for (; i < num; i++) {
            queue.enqueue(StdIn.readString());
        }
        while (!StdIn.isEmpty()) {
            if (StdRandom.uniform(i + 1) < num) {
                queue.dequeue();
                queue.enqueue(StdIn.readString());
            } else {
                StdIn.readString();
            }
            i++;
        }
        for (i = 0; i < num; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
