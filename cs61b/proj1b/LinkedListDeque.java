public class LinkedListDeque<T> implements Deque<T> {
    /* Nested node with previous and next pointers to thingNodes
    Generic type for the items in the nodes
    * */
    private class TNode {
        T item;
        TNode next;
        TNode prev;

        /* Constructor class for thingNode
         * */
        TNode(T i, TNode n, TNode p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private TNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new TNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new TNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i));
        }
    }
    @Override
    public void addFirst(T i) {
        if (size == 0) {
            sentinel.next = new TNode(i, sentinel, sentinel);
            sentinel.prev = sentinel.next;
            size += 1;
            return;
        }
        TNode secondNode = sentinel.next;
        sentinel.next = new TNode(i, sentinel.next, sentinel);
        secondNode.prev = sentinel.next;
        size += 1;
    }

    @Override
    public void addLast(T i) {
        if (size == 0) {
            sentinel.next = new TNode(i, sentinel, sentinel);
            sentinel.prev = sentinel.next;
            size += 1;
            return;
        }
        TNode secondLast = sentinel.prev;
        sentinel.prev = new TNode(i, sentinel, sentinel.prev);
        secondLast.next = sentinel.prev;
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        TNode curr = sentinel.next;
        for (int j = 0; j < size; j++) {
            System.out.print(curr.item + " ");
            curr = curr.next;
        }
        System.out.println(" ");

    }
    /* First checks if next of sentinel is the sentinel itself. Removes first item.
    Changes the previous of the second node to sentinel. Saves the first items with a pointer
    to prevent loss to Garbage collector. DOT equals ensures that the object itself is compared
    not the address allocated to the .next and sentinel they will always be different.
    * */
    @Override
    public T removeFirst() {
        if (sentinel.next.equals(sentinel)) {
            return null;
        }
        TNode first = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return first.item;
    }
    /*Same idea as first. Establish pointer to last node, reestablish pointer
    from sentinel previous to sentinel.prev.prev. Sentinel.prev.next to sentinel */
    @Override
    public T removeLast() {
        if (sentinel.prev.equals(sentinel)) {
            return null;
        }
        TNode last = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        last.next = null;
        sentinel.prev.next = sentinel;
        size -= 1;
        return last.item;
    }

    @Override
    public T get(int index) {
        TNode curr = sentinel.next;
        if (curr.equals(sentinel)) {
            return null;
        }
        /*Starting point confusion*/
        int j = 0;
        while (j < index) {
            curr = curr.next;
            j += 1;
        }
        return curr.item;
    }
    private T helperGet(TNode g, int index) {
        if (index == 0) {
            return g.item;
        } else {
            return helperGet(g.next, index - 1);
        }
    }
    public T getRecursive(int index) {
        return helperGet(sentinel.next, index);
    }

}
