import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;
    private final int Y_COORDINATE = 1;
    private final int X_COORDINATE = 0;

    private static class Node {
        Point2D point;
        int aligned;
        Node left;
        Node right;
        RectHV rect;

        private Node(Point2D p, RectHV rect, int aligned, Node left, Node right) {
            this.point = p;
            this.rect = rect;
            this.aligned = aligned;
            this.left = left;
            this.right = right;
        }
    }

    public KdTree() {                               // construct an empty set of points
        size = 0;
        root = null;
    }

    public boolean isEmpty() {                      // is the set empty?
        return size == 0;
    }

    public int size() {                             // number of points in the set
        return size;
    }

    public void insert(Point2D p) {                 // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        size++;
        if (size == 1) {
            root = new Node(p, new RectHV(0, 0, 1, 1), Y_COORDINATE, null, null);
        } else {
            Node c = root;
            while (c != null) {
                if (c.point.compareTo(p) == 0) {
                    size--;
                    return;
                }
                if (c.aligned == Y_COORDINATE) {
                    if (p.x() > c.point.x()) {
                        if (c.right == null) { // rect
                            c.right = new Node(p, new RectHV(c.point.x(), c.rect.ymin(), c.rect.xmax(), c.rect.ymax()), X_COORDINATE, null, null);
                            return;
                        }
                        c = c.right;
                    } else {
                        if (c.left == null) {
                            c.left = new Node(p, new RectHV(c.rect.xmin(), c.rect.ymin(), c.point.x(), c.rect.ymax()), X_COORDINATE, null, null);
                            return;
                        }
                        c = c.left;
                    }
                } else {
                    if (p.y() > c.point.y()) {
                        if (c.right == null) {
                            c.right = new Node(p, new RectHV(c.rect.xmin(), c.point.y(), c.rect.xmax(), c.rect.ymax()), Y_COORDINATE, null, null);
                            return;
                        }
                        c = c.right;
                    } else {
                        if (c.left == null) {
                            c.left = new Node(p, new RectHV(c.rect.xmin(), c.rect.ymin(), c.rect.xmax(), c.point.y()), Y_COORDINATE, null, null);
                            return;
                        }
                        c = c.left;
                    }
                }
            }
        }
    }

    public boolean contains(Point2D p) {            // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node curr = root;
        while (curr != null) {
            if (curr.point.compareTo(p) == 0) {
                return true;
            }
            if (curr.aligned == Y_COORDINATE) {
                if (p.x() > curr.point.x()) {
                    curr = curr.right;
                } else {
                    curr = curr.left;
                }
            } else {
                if (p.y() > curr.point.y()) {
                    curr = curr.right;
                } else {
                    curr = curr.left;
                }
            }
        }
        return false;
    }

    public void draw() {                            // draw all points to standard draw
        Node curr = root;
        Stack<Node> st = new Stack<>();
        st.push(curr);
        while (!st.isEmpty()) {
            Node p = st.pop();
            p.point.draw();
            if (p.left != null) {
                st.push(p.left);
            }
            if (p.right != null) {
                st.push(p.right);
            }
        }
    }


    private List<Point2D> rangeSearch(Node curr, RectHV rect) {
        if (curr == null) return new ArrayList<>();
        List<Point2D> ret = new ArrayList<>();
        if (rect.contains(curr.point)) {
            ret.add(curr.point);
        }
        if (curr.left != null && curr.left.rect.intersects(rect)) {
            ret.addAll(rangeSearch(curr.left, rect));
        }
        if (curr.right != null && curr.right.rect.intersects(rect)) {
            ret.addAll(rangeSearch(curr.right, rect));
        }
        return ret;
    }

    public Iterable<Point2D> range(RectHV rect) {   // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> ret = rangeSearch(root, rect);

        return () -> new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i <= ret.size() - 1;
            }

            @Override
            public Point2D next() {
                return ret.get(i++);
            }
        };
    }

    private Point2D nearestSearch(Node curr, Point2D p, Point2D nearest) {
        if (curr == null || curr.rect.distanceSquaredTo(p) >= p.distanceSquaredTo(nearest)) {
            return nearest;
        }
        Point2D c = curr.point;
        if (p.distanceSquaredTo(c) < p.distanceSquaredTo(nearest)) {
            nearest = c;
        }
        if ((curr.aligned == Y_COORDINATE && p.x() > c.x()) || (curr.aligned == X_COORDINATE && p.y() > c.y())) { // go right first (up, right)
            nearest = nearestSearch(curr.right, p, nearest);
            nearest = nearestSearch(curr.left, p, nearest);
        } else { // go left first
            nearest = nearestSearch(curr.left, p, nearest);
            nearest = nearestSearch(curr.right, p, nearest);
        }
        return nearest;
    }

    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        Point2D nearest = root.point;
        return nearestSearch(root, p, nearest);
    }

    public static void main(String[] args) {        // unit testing of the methods (optional)
        Point2D a = new Point2D(0.372, 0.497);
        Point2D b = new Point2D(0.564, 0.413);
        Point2D c = new Point2D(0.226, 0.577);
        Point2D d = new Point2D(0.144, 0.179);
        Point2D e = new Point2D(0.083, 0.51);
        Point2D f = new Point2D(0.32, 0.708);
        Point2D g = new Point2D(0.417, 0.362);
        Point2D h = new Point2D(0.862, 0.825);
        Point2D i = new Point2D(0.785, 0.725);
        Point2D j = new Point2D(0.499, 0.208);

        RectHV rect = new RectHV(0.132, 0.401, 0.134, 0.448);
        KdTree kd = new KdTree();

        kd.insert(a);
        kd.insert(b);
        kd.insert(c);
        kd.insert(d);
        kd.insert(e);
        kd.insert(f);
        kd.insert(g);
        kd.insert(h);
        kd.insert(i);
        kd.insert(j);

        StdOut.println(kd.size());
        for (Point2D p : kd.range(rect)) {
            StdOut.println(p);
        }
        StdOut.println();
        StdOut.println(kd.contains(new Point2D(0.9, 0.6)));
        StdOut.println(kd.nearest(new Point2D(0.49, 0.52)));
    }
}
