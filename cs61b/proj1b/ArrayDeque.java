/*ArrayDeque is an array based data structure that mimics a double ended queue for any object*/

public class ArrayDeque<T> implements Deque<T> {
    /*Making a nested class called TNode that sets up sentinel
    * Invariants:
    * Size of Array
    * Sentinel tNode that contains pointer to the currLast and currFirst
    * nextFirst moves BACKWARDS
    * nextLast moves FORWARD
    * by INDEX NUMBER*/
    private class TNode {
        T[] items;
        int nextLast;
        int nextFirst;

    /*Constructor Class for TNode*/
        private TNode() {
            items = (T[]) new Object[8];
            nextFirst = 0;
            nextLast = 1;
        }
        private TNode(int j, int firsT, int lasT) {
            items = (T[]) new Object[j];
            nextFirst = firsT;
            nextLast = lasT;

        }
    }

    /*size: instance of the ArrayDeque class
     *Empty tNode data structure of ArrayDeque class
     *TNode data structure*/

    private int size;
    private TNode tNode;

    /* Constructor Method that takes in all the member of the current Deque
    * and makes it its own new thing*/
    public ArrayDeque(ArrayDeque other) {
        tNode = new TNode();
        size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((T) other.get(i));
        }
    }

    /*Basic constructor constructs the default TNode. The TNode
    * has last index and first index. When the size*/

    public ArrayDeque() {
        tNode = new TNode();
        size = 0;
    }
    /*isFull method keeps track of the items being added to
     * to List. When the items is equal to 0 then it returns true.
     * SIZE IS INDEXED AT ONE*/

    private boolean isFull() {
        if (size >= tNode.items.length) {
            return true;
        }
        return false;
    }
    /*Returns the next index for FIRST item after the
    * the a new first item is added.
    * Checks if the nextFirst is 0. to make the list
    * circular it reels back by returning the length of
    * array. Otherwise returns the PREVIOUS index simply
    * by subtracting 1.*/

    private int nextIndexForFirst() {
        int nextIn = 0;
        if (tNode.nextFirst == 0) {
            nextIn = tNode.items.length - 1;
            return nextIn;
        }
        nextIn = tNode.nextFirst - 1;
        return nextIn;
    }
    /*Written specifically for helping
    * add last method. Returns the NEXT index for
    * nextLast after the PREVIOUS nextLast points
    * at NEW last item.*/

    private int nextIndexForLast() {
        int nexIn = 0;
        if (tNode.nextLast == tNode.items.length - 1) {
            return nexIn;
        }
        nexIn = tNode.nextLast + 1;
        return nexIn;
    }
    /*Helper Function of Print function.
     *Helper function to the addFirst and addLast
     *Returns a NEW array ORDERED according to
     *its ARRANGEMENT in the ArrayDeque and SIZE
     * thereof.
     * For non-complete ArrayDeque it still prints
     * the items


     * IMPLEMENTATION
     * */
    private T[] anticlockwiseArray() {
        int numVar = size;
        T[] objArray = (T[]) new Object[numVar];
        int currFirstIndex = indexofFirstitem();
        for (int i = 0; i < numVar; i++) {
            if (currFirstIndex < tNode.items.length) {
                objArray[i] = tNode.items[currFirstIndex];
                currFirstIndex += 1;
            } else {
                currFirstIndex = 0;
                objArray[i] = tNode.items[currFirstIndex];
                currFirstIndex += 1;
            }
        }
        return objArray;
    }
    /* Used specifically to resizing items to twice
     * the size. Copy the old items into an arranged
     * array, reset the nextLast and nextFirst*/

    private void resetItems() {
        T[] objArray = anticlockwiseArray();
        T[] newItems = (T[]) new Object[size * 2];
        System.arraycopy(objArray, 0, newItems, 0, size);
        tNode.items = newItems;
        tNode.nextFirst = tNode.items.length - 1;
        tNode.nextLast = size;
    }
    /*AddFirst, AddLast uses Helper Methods:
    * resetItems for increased sized list
    * nextIndexForLast: next index for nextLast
    * nextIndexForFirst: next index for nextFirst.*/

    @Override
    public void addFirst(T item) {
        if (isFull()) {
            resetItems();
        }
        tNode.items[tNode.nextFirst] = item;
        tNode.nextFirst = nextIndexForFirst();
        size += 1;

    }
    @Override
    public void addLast(T item) {
        if (isFull()) {
            resetItems();
        }
        tNode.items[tNode.nextLast] = item;
        tNode.nextLast = nextIndexForLast();
        size += 1;


    }
    /*Boolean check on the size instance of the Deque*/
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /*Returns instance variable size if array*/
    @Override
    public int size() {
        return size;
    }
    /*Prints all the items in the ArrayDeque
    * in one row, spaces in between and a new
    * follows.*/
    @Override
    public void printDeque() {
        T[] objArray = anticlockwiseArray();
        for (int i = 0; i < objArray.length; i++) {
            System.out.print(objArray[i] + " ");
        }
        System.out.println(" ");
    }

    /*Removes the nextFirst of the array by
    * resetting the value at next first to
    * null and then resetting the next first
    * forward. Also reduce the size if the array
    * by 1*/
    /*Helper method gives the index of the first inhabited in the items node*/

    private int indexofFirstitem() {
        int beforeInt = 0;
        if (tNode.nextFirst == tNode.items.length - 1) {
            return beforeInt;
        }
        beforeInt = tNode.nextFirst + 1;
        return beforeInt;
    }


    private void reduceSize() {
        T[] objArray = anticlockwiseArray();
        T[] newItems = (T[]) new Object[tNode.items.length / 2];
        System.arraycopy(objArray, 0, newItems, 0, size);
        tNode.items = newItems;
        tNode.nextFirst = tNode.items.length - 1;
        tNode.nextLast = size;


    }
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = tNode.items[indexofFirstitem()];
        tNode.items[indexofFirstitem()] = null;
        tNode.nextFirst = indexofFirstitem();
        size -= 1;
        double ratio = (1.0 * (size + 1)) / tNode.items.length;
        if (ratio < 0.25) {
            reduceSize();
        }
        return item;
    }
    /*Helper for removelast gives the int back*/

    private int indexofLastitem() {
        int lastInt = 0;
        if (tNode.nextLast == 0) {
            lastInt = tNode.items.length - 1;
            return lastInt;
        }
        lastInt = tNode.nextLast - 1;
        return lastInt;
    }
    /*Resets the currLast value to null. Resets
    * the nextLast pointer to the index before
    * the next
    * Last. Also resets the size by 1 */

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = tNode.items[indexofLastitem()];
        tNode.items[indexofLastitem()] = null;
        tNode.nextLast = indexofLastitem();
        size -= 1;
        double ratio = (1.0 * (size + 1)) / tNode.items.length;
        if (ratio < 0.25) {
            reduceSize();
        }
        return item;
    }
    /*Returns null to an empty Deque. When the
     * difference between the currLast and the
     * currFirst is 1. Does not destroy the data
     * structure. Why would you ever want tot do that?
     * Implementation errors include index out of
     * error. Otherwise, just return the item at the
     * index*/
    @Override
    public T get(int index) {
        int p = index + indexofFirstitem();
        if (p > tNode.items.length - 1) {
            int q = p - tNode.items.length;
            return tNode.items[q];
        } else {
            return tNode.items[p];
        }
    }

}
