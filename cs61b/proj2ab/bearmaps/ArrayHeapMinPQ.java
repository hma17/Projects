package bearmaps;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>, Comparator<T> {
    private ArrayList<PriorityNode<T>> minHeap; //array representation of the data structure
    private int size; // size of the array
    private int nextIndex; //index to place the next item in
    private double loadFactor;
    private HashMap<T, Integer> itemsMap;


    private class PriorityNode<T> implements Comparable<PriorityNode> {
        private T item;
        private double priority;
        private int index;

        PriorityNode() {
            item = null;
            priority = 0.0;
        }
        private Double getPriority(PriorityNode node) {
            return node.priority;
        }
        @Override
        public boolean equals(Object o) {
            if (o == null || o.getClass() != this.getClass()) {
                return false;
            } else {
                return ((PriorityNode) o).getItem().equals(getItem());
            }
        }
        @Override
        public int hashCode() {
            return this.hashCode();
        }

        PriorityNode(T t, double e) {
            item = t;
            priority = e;
            index = 0;
        }
        @Override
        public int compareTo(PriorityNode p) {
            if (this.priority > p.priority) {
                return 1;
            } else if (this.priority < p.priority) {
                return -1;
            }
            return 0;
        }
        public T getItem() {
            return item;
        }
    }
    public ArrayHeapMinPQ() {
        size = 20;
        minHeap = new ArrayList<PriorityNode<T>>();
        minHeap.add(null);
        nextIndex = 1;
        loadFactor = 0.75;
        itemsMap = new HashMap<>(size);
    }
    public double[] getPriorityList(int s) {
        double[] priorityList = new double[s];
        for (int i = 0; i < s; i++) {
            priorityList[i] = getPriority(getNode(i + 1));
        }
        return priorityList;
    }


    @Override
    public void add(T item, double priority) {
        if (nextIndex == 1) {
            PriorityNode next = new PriorityNode(item, priority);
            this.minHeap.add(next);
            itemsMap.put(item, nextIndex);
            next.index = nextIndex;
            nextIndex++;
            return;
        }
        if (!contains(item)) {
            itemsMap.put(item, nextIndex);
            PriorityNode next = new PriorityNode(item, priority);
            next.index = nextIndex;
            this.minHeap.add(nextIndex, next);
            swimUp(nextIndex);
            nextIndex++;
        }
        return;
    }
    private PriorityNode getNode(int i) {
        if (i <= nextIndex) {
            if (this.minHeap.get(i) != null) {
                return minHeap.get(i);
            }
        }
        return null;
    }
    private double getPriority(PriorityNode node) {
        if (node != null) {
            return node.priority;
        }
        throw new NullPointerException("null input in the getpriority func");
    }


    private int getParent(int k) {
        if (k == 1) {
            return 1;
        }
        return k / 2;
    }
    private void swimUp(int k) {
        if (k == 1) {
            return;
        }
        PriorityNode parent = getNode(getParent(k));
        PriorityNode currNode = getNode(k);
        if (getPriority(parent) > getPriority(currNode)) {
            swap(k, getParent(k));
            swimUp(getParent(k));
        }
    }
    private void swimDown(int i) {
        if (i >= nextIndex) {
            return;
        }
        int currindex = i;
        int leftIndex = getLeft(currindex);
        int rightIndex = getRight(currindex);

        PriorityNode node = getNode(i);
        PriorityNode rightNode = getNode(rightIndex);
        PriorityNode leftNode = getNode(leftIndex);
        if (rightNode == null && leftNode == null) {
            return;
        }
        if (rightNode == null && leftNode !=  null && getPriority(leftNode) < getPriority(node)) {
            swap(leftIndex, currindex);
            return;
        }
        if (rightNode == null && leftNode !=  null && getPriority(leftNode) > getPriority(node)) {
            return;
        }

        if (leftNode == null && rightNode !=  null && getPriority(rightNode) < getPriority(node)) {
            swap(rightIndex, currindex);
            return;
        }
        if (leftNode == null && rightNode !=  null && getPriority(rightNode) > getPriority(node)) {
            return;
        }
        if (getPriority(leftNode) > getPriority(node)
                && getPriority(rightNode) > getPriority(node)) {
            return;
        }
        if (getPriority(leftNode) < getPriority(node)) {
            swap(leftIndex, currindex);
            swimDown(currindex);
            swimDown(leftIndex);
            return;
        }
        if (getPriority(rightNode) < getPriority(node)) {
            swap(rightIndex, currindex);
            swimDown(currindex);
            swimDown(rightIndex);
            return;
        }

    }
    private void swap(int nint, int pNint) {
        if (!(nint == pNint)) {
            PriorityNode node = getNode(nint);
            PriorityNode parentNode = getNode(pNint);
            itemsMap.put((T) node.getItem(), pNint);
            itemsMap.put((T) parentNode.getItem(), nint);
            Collections.swap(minHeap, nint, pNint);
        }
    }

    public int getLeft(int i) {
        return (i * 2);
    }
    public int  getRight(int i) {
        return  (i * 2) + 1;
    }

    @Override
    public boolean contains(T item) {
        return itemsMap.containsKey(item);
    }


    @Override
    public T getSmallest() {
        if (nextIndex > 1) {
            return (T) getNode(1).getItem();
        }
        throw new NullPointerException("no item to return");
    }

    @Override
    public T removeSmallest() {
        if (nextIndex == 1) {
            throw new NoSuchElementException("no item to remove");
        }
        T item = (T) getNode(1).getItem();
        swap(1, nextIndex - 1);
        minHeap.add(nextIndex - 1, null);
        nextIndex = nextIndex - 1;
        swimDown(1);
        return item;
    }

    @Override
    public int size() {
        return nextIndex - 1;
    }
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("cant dooo that");
        }
        PriorityNode node = minHeap.get(itemsMap.get(item));
        node.priority = priority;
        swimUp(itemsMap.get(item));
        swimDown(itemsMap.get(item));
    }
    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }
}
