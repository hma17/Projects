package bearmaps;


import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class TestPointSet {
    @Test
    public void testNearest() {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0);
        assertTrue(ret.getX() == 3.3);

    }
    @Test
    public void tesNearestInKd() {

        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(1, 5);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5); // constructs a Point with x = 1.1, y = 2.2
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);



        KDTree nn = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
    }

}
