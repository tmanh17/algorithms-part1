import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private final LineSegment[] segments;

    public BruteCollinearPoints(Point[] originPoints) {  // finds all line segments containing 4 points
        if (originPoints == null) {
            throw new IllegalArgumentException();
        }
        for (Point p : originPoints) {
            if (p == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] points = Arrays.copyOf(originPoints, originPoints.length);
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        List<LineSegment> segments = new ArrayList<>();

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k]) && points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])) {
                            segments.add(new LineSegment(points[i], points[l]));
                        }
                    }
                }
            }
        }

        this.segments = segments.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {                // the number of line segments
        return this.segments.length;
    }

    public LineSegment[] segments() {              // the line segments
        return this.segments.clone();
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);

//            StdOut.println(points[i]);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
//        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            segment.draw();
        }
        StdDraw.show();
    }
}