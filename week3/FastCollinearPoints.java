import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] originPoints) {  // finds all line segments containing 4 or more points
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

        if (points.length >= 4){
            for (Point start : points) {
                Point[] ps = Arrays.copyOf(points, points.length);
                Arrays.sort(ps, start.slopeOrder());

                int accumulated = 2;
                double slope = start.slopeTo(ps[1]);;
                double banSlope = -1.2345678987654321;
                if (start.compareTo(ps[1]) > 0){
                    banSlope = start.slopeTo(ps[1]);
                }
                for (int i = 2; i < ps.length; i++){
                    if (start.slopeTo(ps[i]) == slope){
                        accumulated++;
                    } else {
                        if (accumulated >= 4 && slope != banSlope){
                            segments.add(new LineSegment(start, ps[i-1]));
                        }
                        accumulated = 2;
                        slope = start.slopeTo(ps[i]);
                        if (start.compareTo(ps[i]) > 0){
                            banSlope = start.slopeTo(ps[i]);
                        }
                    }
                }
                if (accumulated >= 4 && slope != banSlope){
                    segments.add(new LineSegment(start, ps[ps.length-1]));
                }
            }
        }

        this.segments = segments.toArray(new LineSegment[0]);
    }

    public int numberOfSegments() {               // the number of line segments
        return this.segments.length;
    }

    public LineSegment[] segments() {             // the line segments
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
//        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
//            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}