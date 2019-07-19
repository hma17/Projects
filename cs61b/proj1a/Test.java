public class Test {
    public static void main(String[] args) {
        LinkedListDeque deck = new LinkedListDeque();
        deck.isEmpty();
        //deck.addLast(0);
        deck.addFirst(0);
        deck.addFirst(1);
        deck.removeLast();
        deck.removeFirst();
        deck.addLast(4);
        deck.removeLast();
        deck.addLast(6);
        deck.addFirst(7);
        deck.removeLast();
        deck.removeFirst();
        deck.addFirst(10);
        deck.removeFirst();
        deck.addLast(12);
        deck.removeFirst();
        deck.addFirst(14);
        deck.get(0);
        deck.removeFirst();
        System.out.println((deck.get(1)));
        System.out.print(deck.removeFirst());
        System.out.print(deck.removeLast());

    }
}
