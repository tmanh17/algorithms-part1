import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    public PointSET() {                            // construct an empty set of points
        points = new TreeSet<>();
    }

    public boolean isEmpty() {                      // is the set empty?
        return points.isEmpty();
    }

    public int size() {                             // number of points in the set
        return points.size();
    }

    public void insert(Point2D p) {                 // add the point to the set (if it is not already in the set)

        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    public boolean contains(Point2D p) {            // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw() {                            // draw all points to standard draw
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {   // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> inside = new ArrayList<>();

        for (Point2D p : points) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
                inside.add(p);
            }
        }

        return () -> new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i <= inside.size() - 1;
            }

            @Override
            public Point2D next() {
                return inside.get(i++);
            }
        };
    }


    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < minDistance) {
                nearest = point;
                minDistance = point.distanceSquaredTo(p);
            }
        }

        return nearest;
    }

    public static void main(String[] args) {        // unit testing of the methods (optional)

    }
}