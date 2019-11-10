package io.github.marwlod.kd_trees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No point specified!");
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No point specified!");
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("No rectangle specified!");
        List<Point2D> pointsInside = new ArrayList<>();
        for (Point2D currPoint : pointSet) {
            if (rect.contains(currPoint)) {
                pointsInside.add(currPoint);
            }
        }
        return pointsInside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No point specified!");
        if (pointSet.isEmpty()) return null;
        Point2D nearest = pointSet.first();
        double sqDistToNearest = nearest == null ? Double.POSITIVE_INFINITY : p.distanceSquaredTo(nearest);
        for (Point2D currPoint : pointSet) {
            if (p.distanceSquaredTo(currPoint) < sqDistToNearest) {
                nearest = currPoint;
                sqDistToNearest = p.distanceSquaredTo(currPoint);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        StdDraw.clear();

        // drawing all the points from the tree
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        kdTree.draw();

        // drawing rectangle
        RectHV rectangle = new RectHV(0.15, 0.15, 0.71, 0.45);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        rectangle.draw();

        // checking what points are inside the rectangle
        StringBuilder sb = new StringBuilder();
        Iterable<Point2D> pointsInRect = kdTree.range(rectangle);
        for (Point2D point : pointsInRect) {
            sb.append(point).append(" ");
        }
        System.out.println("Inside rectangle: " + sb.toString());

        // checking which point is the nearest to query point
        Point2D pointQueried = new Point2D(1, 0);
        pointQueried.draw();
        System.out.println("Nearest to " + pointQueried + " is " + kdTree.nearest(pointQueried));
        StdDraw.show();
    }
}
