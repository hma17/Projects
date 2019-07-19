package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {

    private static Random r = new Random(1000);
    @Test
    public void tesNearestDemoSlides() {

        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5); // constructs a Point with x = 1.1, y = 2.2
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);
        Point p2 = new Point(1, 5);



        KDTree nn = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        Point expected = new Point(1, 5);
        Point nearest = nn.nearest(0, 7);
        assertEquals(expected, nearest);
    }
    private Point randomPoint() {
        double x = r.nextDouble();
        double y = r.nextDouble();
        return new Point(x, y);
    }
    private List<Point> randomPoints(int n) {
        List<Point> list = new ArrayList<>();
        for (int i = 0; i < n; i += 1) {
            list.add(randomPoint());
        }
        return list;
    }

    private void testTime(int query, int poo) {
        int queryCount = query;
        int pointCount = poo;

        List<Point> points = randomPoints(pointCount);
        NaivePointSet sR = new NaivePointSet(points);
        KDTree cR = new KDTree(points);

        List<Point> testSample = randomPoints(queryCount);

        Stopwatch sw = new Stopwatch();
        for (Point p : testSample) {
            Point expected = sR.nearest(p.getX(), p.getY());
        }
        System.out.println("time elapsed for " + queryCount
                + " naive implementation " + "on " + pointCount + " points " + sw.elapsedTime());
        Stopwatch sw2 = new Stopwatch();
        for (Point p : testSample) {
            Point nearest = cR.nearest(p.getX(), p.getY());
        }
        System.out.println("time elapsed for " + queryCount
               + " better implementation " + "on " + pointCount + " points " + sw2.elapsedTime());
    }

    private void testRandom(int query, int longer) {

        List<Point> points = randomPoints(longer);
        NaivePointSet sR = new NaivePointSet(points);
        KDTree cR = new KDTree(points);

        List<Point> testSample = randomPoints(query);

        for (Point p : testSample) {
            Point expected = sR.nearest(p.getX(), p.getY());
            Point actual = cR.nearest(p.getX(), p.getY());
            assertEquals(actual, expected);
        }
    }
    //at the given instance test if there is a possible point closer to the given point
    //space. For horizontal check x distance with the best distance vice versa
    //corners are made by the last four predecessors.
    @Test
    public void testWith1000() {
        int queryCount = 1000;
        int pointsCount = 1000;
        testRandom(queryCount, pointsCount);
    }
    @Test
    public void testTestimeWithN() {
        List<Integer> timeScale = new ArrayList<>();
        timeScale.add(10);
        timeScale.add(1004444);
        timeScale.add(1500);
        timeScale.add(200000);
        timeScale.add(1000000);
        timeScale.add(400000);
        timeScale.add(50000);
        for (int N : timeScale) {
            testTime(N, 1000);
        }
    }
    @Test
    public void compareTimingLikeSpec() {
        List<Point> randomPoints = randomPoints(10000);
        KDTree kd = new KDTree(randomPoints);
        NaivePointSet nps = new NaivePointSet(randomPoints);
        List<Point> queryPoints = randomPoints(10000);
        Stopwatch sw = new Stopwatch();
        for (Point p : queryPoints) {
            nps.nearest(p.getX(), p.getY());
        }
        double time = sw.elapsedTime();
        System.out.println("Naive 10000 on 100000 points: " + time);

        Stopwatch sw2 = new Stopwatch();
        for (Point p : queryPoints) {
            kd.nearest(p.getX(), p.getY());
        }
        double time2 = sw2.elapsedTime();
        System.out.println("Better 10000 on 100000 points: " + time2);
    }
}
