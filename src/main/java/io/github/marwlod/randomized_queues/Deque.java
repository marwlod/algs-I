package main.java.io.github.marwlod.randomized_queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node prev;
        Node next;
    }

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("object can't be null");
        Node toAdd = new Node();
        toAdd.item = item;
        toAdd.prev = null;
        toAdd.next = first;
        if (first != null) first.prev = toAdd;
        else last = toAdd;

        first = toAdd;
        size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("object can't be null");
        Node toAdd = new Node();
        toAdd.item = item;
        toAdd.prev = last;
        toAdd.next = null;
        if (last != null) last.next = toAdd;
        else first = toAdd;

        last = toAdd;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        Item item = first.item;
        first = first.next;
        if (first != null) first.prev = null;
        else last = null;
        size--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty");
        Item item = last.item;
        last = last.prev;
        if (last != null) last.next = null;
        else first = null;
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove operation not supported");
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) throw new NoSuchElementException("no more items in the deque");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addLast("d");
        deque.addLast("e");
        deque.addFirst("c");
        deque.addFirst("b");
        deque.addFirst("a");
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeFirst());
        System.out.println();
//        Iterator<String> iter = deque.iterator();
        for (String s : deque) {
            System.out.println(s);
        }
    }
}
