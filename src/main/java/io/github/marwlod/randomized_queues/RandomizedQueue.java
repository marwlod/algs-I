package main.java.io.github.marwlod.randomized_queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size;

    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("item can't be null");
        if (size == queue.length) resize(2 * queue.length);
        queue[size++] = item;
        exchange(queue, size - 1, StdRandom.uniform(size));
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        if (size < queue.length / 4) resize(queue.length / 2);
        Item item = queue[--size];
        queue[size] = null;
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("queue is empty");
        return queue[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private void resize(int cap) {
        Item[] copy = (Item[]) new Object[cap];
        System.arraycopy(queue, 0, copy, 0, size);
        queue = copy;
    }

    private void exchange(Item[] q, int i, int j) {
        Item temp = q[i];
        q[i] = q[j];
        q[j] = temp;
    }

    private class RandomIterator implements Iterator<Item> {
        private int current;
        private int[] indexes;

        RandomIterator() {
            indexes = StdRandom.permutation(size);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove operation not supported");
        }

        @Override
        public boolean hasNext() {
            return current != size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("queue is empty");
            return queue[indexes[current++]];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        queue.enqueue("d");
        queue.enqueue("e");
        for (String s : queue) {
            System.out.println(s);
        }
        System.out.println();
        for (String s : queue) {
            System.out.println(s);
        }
        System.out.println();
        for (String s : queue) {
            System.out.println(s);
        }
    }
}