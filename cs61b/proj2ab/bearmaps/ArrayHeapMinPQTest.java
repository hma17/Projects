package bearmaps;


import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest<T> {

    @Test
    public void testAdd() {
        ArrayHeapMinPQ<String> a = new ArrayHeapMinPQ<>();
        a.add("save7", 9.0);
        a.add("save8", 2.0);
        a.add("save9", 4.0);
        a.add("save10", 6.0);
        a.add("1231", 4.0);
        a.add("savereat", 3.0);
        a.add("sav3", 3.0);
        a.add("se8", 2.0);
        a.add("ve9", 79.0);
        a.add("ve10", 6.0);
        a.add("31", 4.0);
        a.add("sat", 3.0);
        a.add("se7", 3.0);
        a.add("yy", 2.0);
        a.add("yyve9", 4.0);
        a.add("saveyy10", 6.0);
        a.add("123yy1", 4.0);
        a.add("savyyerepeat", 3.0);
        a.add("saveyy7", 3.0);
        a.add("save8yy", 2.0);
        a.add("saveyyy9", 4.0);
        a.add("savyye10", 6.0);
        a.add("123yyyy1", 4.0);
        a.add("savereyypeat", 3.0);


        double[] expected = {1.0, 2.0, 3.0, 4.0};
        double[] actual = (a.getPriorityList(4));
        java.util.Arrays.equals(actual, expected);
    }

    @Test
    public void testContains() {
        ArrayHeapMinPQ<String> a = new ArrayHeapMinPQ<>();
        a.add("four", 4.0);
        a.add("one", 1.0);
        a.add("three", 3.0);
        a.add("two", 2.0);
        boolean test = a.contains("four");
        assertTrue(test);

    }

    @Test
    public void testGetSmallest() {
        ArrayHeapMinPQ<String> a = new ArrayHeapMinPQ<>();
        a.add("four", 4.0);
        a.add("one", 1.0);
        a.add("three", 3.0);
        a.add("two", 2.0);
        String jacquleine = "one";
        assertEquals(a.getSmallest(), jacquleine);
    }

    @Test
    public void testRemoveSmallest() {
        ArrayHeapMinPQ<String> a = new ArrayHeapMinPQ<>();
        a.add("this", 1.0);
        a.add("ar", 3.5);
        a.add("looks", 3.0);
        a.add("right", 9.0);
        a.add("what", 8.0);
        a.add("say", 4.0);
        a.add("bad", 4.0);
        a.removeSmallest();
        a.removeSmallest();
        a.removeSmallest();
        a.removeSmallest();
        a.removeSmallest();
        a.removeSmallest();
        a.removeSmallest();
        String jacquleine = "one";
        T item = (T) a.removeSmallest();
        System.out.println(item);
        assertEquals(item, jacquleine);
    }


    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<String> a = new ArrayHeapMinPQ<>();
        a.add("this", 1.0);
        a.add("ar", 3.5);
        a.add("looks", 3.0);
        a.add("right", 9.0);
        a.add("what", 8.0);
        a.add("say", 4.0);
        a.add("bad", 4.0);
        a.changePriority("this", 9.0);
        assertTrue(a.size() == 1);
    }
}
