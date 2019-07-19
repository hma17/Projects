package bearmaps.proj2c;


import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;

import java.util.List;

public class KDTree implements PointSet {
    private Node root;
    private static final boolean HORIZONTAL = false;
    private static final boolean VERTICAL = true;

    private class Node {
        Node leftChild;
        Node rightChild;
        Point value;
        Boolean orientation;

        Node(Node left, Node right, Point p, Boolean tf) {
            leftChild = left;
            rightChild = right;
            value = p;
            orientation = tf;
        }
        private double eucladianDistance(Point p) {
            double yCoord = value.getY();
            double xCoord = value.getX();
            double x = Math.abs(xCoord - p.getX());
            double y = Math.abs(yCoord - p.getY());
            return Math.sqrt(x * x + y * y);
        }
        private double horizontalDistance(Point p) {
            double xCoord = value.getX();
            return xCoord - p.getX();
        }
        private double verticalDistance(Point p) {
            double yCoord = value.getY();
            return yCoord - p.getY();

        }

    }

    private Node insert(Point p, Node n, Boolean orientation) {
        if (n == null) {
            return new Node(null, null, p, orientation);
        }
        if (p.equals(n.value)) {
            return n;
        }
        if (comparePoints(p, n.value, orientation) == 0) {
            n.leftChild = insert(p, n.leftChild, !orientation);
        } else if (comparePoints(p, n.value, orientation) < 0) {
            n.leftChild = insert(p, n.leftChild, !orientation);
        } else if (comparePoints(p, n.value, orientation) > 0) {
            n.rightChild = insert(p, n.rightChild, !orientation);
        }
        return n;

    }


    public KDTree(List<Point> points) {
        for (Point p : points) {
            root = insert(p, root, HORIZONTAL);
        }
    }

    private int comparePoints(Point P, Point q, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return Double.compare(P.getX(), q.getX());
        }
        return Double.compare(P.getY(), q.getY());
    }

    @Override
    public Point nearest(double x, double y) {
        Point sample = new Point(x, y);
        Point xrando = new Point(99999999999.99, 999999999999.9);
        Node rando = new Node(null, null, xrando, false);
        Node best = nearestHelper(root, sample, rando);
        return best.value;

    }



    private Node nearestHelper(Node n, Point p, Node best) {
        Node goodSide = new Node(null, null, null, false);
        Node badSide = new Node(null, null, null, false);

        if (n == null) {
            return best;
        }
        if (Point.distance(n.value, p) < Point.distance(best.value, p)) {
            best = n;
        }
        if (!n.orientation) {
            if (n.horizontalDistance(p) > 0) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
                best = nearestHelper(goodSide, p, best);
                if (Math.abs(n.horizontalDistance(p)) < best.eucladianDistance(p)) {
                    best = nearestHelper(badSide, p, best);
                }
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
                best = nearestHelper(goodSide, p, best);
                if (Math.abs(n.horizontalDistance(p)) < best.eucladianDistance(p)) {
                    best = nearestHelper(badSide, p, best);
                }
            }
            return best;
        } else if (n.orientation) {
            if (n.verticalDistance(p) > 0) {
                goodSide = n.leftChild;
                badSide = n.rightChild;
                best = nearestHelper(goodSide, p, best);
                if (Math.abs(n.verticalDistance(p)) < best.eucladianDistance(p)) {
                    best = nearestHelper(badSide, p, best);
                }
            } else {
                goodSide = n.rightChild;
                badSide = n.leftChild;
                best = nearestHelper(goodSide, p, best);
                if (Math.abs(n.verticalDistance(p)) < best.eucladianDistance(p)) {
                    best = nearestHelper(badSide, p, best);
                }
            }
            return best;
        }
        return best;
    }
}
