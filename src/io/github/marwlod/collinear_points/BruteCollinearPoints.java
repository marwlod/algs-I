package io.github.marwlod.collinear_points;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BruteCollinearPoints {
    private int numberOfSegments = 0;
    private List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Array cannot be null");
        segments = new ArrayList<>();
        for (int p1 = 0; p1 < points.length; p1++) {
            if (points[p1] == null) throw new IllegalArgumentException("Point is null");
            for (int p2 = p1 + 1; p2 < points.length; p2++) {
                if (points[p2] == null || points[p2].compareTo(points[p1]) == 0) throw new IllegalArgumentException("Point null or duplicate");
                for (int p3 = p2 + 1; p3 < points.length; p3++) {
                    if (points[p3] == null || points[p3].compareTo(points[p1]) == 0 || points[p3].compareTo(points[p2]) == 0) {
                        throw new IllegalArgumentException("Point null or duplicate");
                    }
                    for (int p4 = p3 + 1; p4 < points.length; p4++) {
                        if (points[p4] == null || points[p4].compareTo(points[p3]) == 0 || points[p4].compareTo(points[p2]) == 0 || points[p4].compareTo(points[p1]) == 0) {
                            throw new IllegalArgumentException("Point null or duplicate");
                        }

                        double slope = points[p1].slopeTo(points[p2]);
                        if (slope == points[p1].slopeTo(points[p3]) && slope == points[p1].slopeTo(points[p4])) {
                            segments.add(segmentize(points[p1], points[p2], points[p3], points[p4]));
                            numberOfSegments++;
                        }
                    }
                }
            }
        }
    }

    private LineSegment segmentize(Point p1, Point p2, Point p3, Point p4) {
        List<Point> points = Stream.of(p1, p2, p3, p4)
                .sorted(Point::compareTo)
                .collect(Collectors.toList());
        return new LineSegment(points.get(0), points.get(3));
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[segments.size()];
        return segments.toArray(lineSegments);
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
        int i = 0;
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(i++ + " " + segment);
            segment.draw();
        }
        StdDraw.show();
    }
}