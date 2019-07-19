package bearmaps;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static bearmaps.Point.distance;

public class NaivePointSet implements PointSet {
    Set<Point> setOfPoints;
    public NaivePointSet(List<Point> points) {
        setOfPoints = new HashSet<>();
        for (Point p : points) {
            setOfPoints.add(p);
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point curr = new Point(x, y);
        Point best = new Point(999999999.99, 999999999.99);
        for (Point p : setOfPoints) {
            if (distance(best, curr) > distance(p, curr)) {
                best = p;
            }
        }
        return best;
    }
}
