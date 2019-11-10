package io.github.marwlod.collinear_points;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private int numberOfSegments;
    private List<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("array cannot be null");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("no point can be null");
        }
        segments = new ArrayList<>();
        double[] slopes;
        List<Point> pointsInSeg;
        Point[] pointsCopy;
        LineSegment ls;

        for (Point point : points) {
            pointsCopy = new Point[points.length];
            System.arraycopy(points, 0, pointsCopy, 0, points.length);
            Arrays.sort(pointsCopy, point.slopeOrder());
            slopes = Arrays.stream(pointsCopy)
                    .mapToDouble(point::slopeTo)
                    .toArray();

            pointsInSeg = new ArrayList<>();

            for (int j = 0; j < pointsCopy.length - 1; j++) {
                if (pointsCopy[j].compareTo(pointsCopy[j + 1]) == 0) {
                    throw new IllegalArgumentException("duplicate points");
                }
                if (slopes[j] == slopes[j + 1]) {
                    if (pointsInSeg.isEmpty()) {
                        pointsInSeg.add(pointsCopy[j]);
                    }
                    pointsInSeg.add(pointsCopy[j + 1]);
                } else {
                    if (pointsInSeg.size() >= 3) {
                        pointsInSeg.add(point);
                        ls = segmentize(point, pointsInSeg);
                        if (ls != null) {
                            segments.add(ls);
                            numberOfSegments++;
                        }
                    }
                    pointsInSeg = new ArrayList<>();
                }
            }
            if (pointsInSeg.size() >= 3) {
                pointsInSeg.add(point);
                ls = segmentize(point, pointsInSeg);
                if (ls != null) {
                    segments.add(ls);
                    numberOfSegments++;
                }
            }
        }
    }

    private LineSegment segmentize(Point p, List<Point> points) {
        Point min = points.stream()
                .min(Point::compareTo)
                .orElse(null);
        if (p.compareTo(min) == 0) {
            Point max = points.stream()
                    .max(Point::compareTo)
                    .orElse(null);
            return new LineSegment(min, max);
        }
        return null;
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[segments.size()];
        return segments.toArray(result);
    }
}
