package io.github.marwlod.kd_trees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No point specified!");
        if (root == null) {
            root = new Node(p, null, null, Point2D.X_ORDER);
            size++;
        }
        Node trav = root;
        while (true) {
            // point to insert has equal/higher x or y coordinate
            if (trav.comparator.compare(p, trav.point) >= 0 && p.compareTo(trav.point) != 0) {
                if (trav.right == null) {
                    // alternating between comparing by x coordinate and y coordinate
                    trav.right = new Node(p, null, null,
                            trav.comparator == Point2D.X_ORDER ? Point2D.Y_ORDER : Point2D.X_ORDER);
                    size++;
                    break;
                }
                trav = trav.right;
            } else if (trav.comparator.compare(p, trav.point) < 0) {
                if (trav.left == null) {
                    trav.left = new Node(p, null, null,
                            trav.comparator == Point2D.X_ORDER ? Point2D.Y_ORDER : Point2D.X_ORDER);
                    size++;
                    break;
                }
                trav = trav.left;
            }
            // point with the same x and y coordinates is already present in the tree
            else {
                break;
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No point specified!");
        Node trav = root;
        while (trav != null) {
            // point to insert has higher x or y coordinate
            if (p.compareTo(trav.point) == 0) {
                return true;
            } else if (trav.comparator.compare(p, trav.point) >= 0) {
                trav = trav.right;
            } else if (trav.comparator.compare(p, trav.point) < 0) {
                trav = trav.left;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(root, null, false);
    }

    private void drawNode(Node curr, Node prev, boolean isCurrLeft) {
        if (curr != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.02);
            curr.point.draw();
            StdDraw.setPenRadius(0.005);
            // comparing by x -> vertical line ending at previous y coordinate
            if (prev == null && curr.comparator == Point2D.X_ORDER) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(curr.point.x(), 0, curr.point.x(), 1);
            } else if (curr.comparator == Point2D.X_ORDER) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(curr.point.x(), isCurrLeft ? 0 : 1, curr.point.x(), prev.point.y());
            }
            // comparing by y -> horizontal line ending at previous x coordinate
            else if (prev == null && curr.comparator == Point2D.Y_ORDER) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(0, curr.point.y(), 1, curr.point.y());
            } else if (curr.comparator == Point2D.Y_ORDER) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(isCurrLeft ? 0 : 1, curr.point.y(), prev.point.x(), curr.point.y());
            } else {
                throw new IllegalStateException("Unknown comparator used for comparing tree nodes");
            }
            drawNode(curr.left, curr, true);
            drawNode(curr.right, curr, false);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("No rectangle specified!");
        List<Point2D> inside = new ArrayList<>();
        Node trav = root;
        range(inside, trav, null, rect, false, new RectHV(0, 0, 1, 1));
        return inside;
    }

    private void range(final List<Point2D> list, final Node curr, final Node prev, final RectHV queryRect, final boolean isCurrLeft, final RectHV currRect) {
        if (curr != null) {
            if (queryRect.contains(curr.point)) {
                list.add(curr.point);
            }
            final SplitRect splitRect = splitRectangle(curr, prev, isCurrLeft, currRect);

            if (queryRect.intersects(splitRect.leftRect)) {
                range(list, curr.left, curr, queryRect, true, splitRect.leftRect);
            }
            if (queryRect.intersects(splitRect.rightRect)) {
                range(list, curr.right, curr, queryRect, false, splitRect.rightRect);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("No point specified!");
        if (root == null) return null;
        return nearest(p, root.point, root, null, false, new RectHV(0, 0, 1, 1));
    }

    private Point2D nearest(final Point2D p, Point2D nearestSoFar, final Node curr, final Node prev, final boolean isCurrLeft, final RectHV currRect) {
        if (curr != null) {
            if (p.distanceSquaredTo(curr.point) < p.distanceSquaredTo(nearestSoFar)) {
                nearestSoFar = curr.point;
            }
            final SplitRect splitRect = splitRectangle(curr, prev, isCurrLeft, currRect);
            final double nearestDist = nearestSoFar.distanceSquaredTo(p);
            final double distToLeftRect = splitRect.leftRect.distanceSquaredTo(p);
            final double distToRightRect = splitRect.rightRect.distanceSquaredTo(p);
            if (nearestDist > distToLeftRect && nearestDist > distToRightRect) {
                if (curr.comparator.compare(p, curr.point) >= 0) {
                    nearestSoFar = nearest(p, nearestSoFar, curr.right, curr, false, splitRect.rightRect);
                    if (nearestSoFar.distanceSquaredTo(p) > distToLeftRect) {
                        return nearest(p, nearestSoFar, curr.left, curr, true, splitRect.leftRect);
                    }
                } else {
                    nearestSoFar = nearest(p, nearestSoFar, curr.left, curr, true, splitRect.leftRect);
                    if (nearestSoFar.distanceSquaredTo(p) > distToRightRect) {
                        return nearest(p, nearestSoFar, curr.right, curr, false, splitRect.rightRect);
                    }
                }
            } else if (nearestDist > distToLeftRect) {
                return nearest(p, nearestSoFar, curr.left, curr, true, splitRect.leftRect);
            } else if (nearestDist > distToRightRect) {
                return nearest(p, nearestSoFar, curr.right, curr, false, splitRect.rightRect);
            }
        }
        return nearestSoFar;
    }

    private SplitRect splitRectangle(Node curr, Node prev, boolean isCurrLeft, RectHV currRect) {
        final RectHV leftRect;
        final RectHV rightRect;
        if (curr.comparator == Point2D.X_ORDER && isCurrLeft) {
            final double currPointX = curr.point.x();
            final double prevPointY = prev == null ? 1 : prev.point.y();
            leftRect = new RectHV(currRect.xmin(), currRect.ymin(), currPointX, prevPointY);
            rightRect = new RectHV(currPointX, currRect.ymin(), currRect.xmax(), prevPointY);
        } else if (curr.comparator == Point2D.X_ORDER) {
            final double currPointX = curr.point.x();
            final double prevPointY = prev == null ? 0 : prev.point.y();
            leftRect = new RectHV(currRect.xmin(), prevPointY, currPointX, currRect.ymax());
            rightRect = new RectHV(currPointX, prevPointY, currRect.xmax(), currRect.ymax());
        } else if (curr.comparator == Point2D.Y_ORDER && isCurrLeft) {
            final double currPointY = curr.point.y();
            final double prevPointX = prev == null ? 1 : prev.point.x();
            leftRect = new RectHV(currRect.xmin(), currRect.ymin(), prevPointX, currPointY);
            rightRect = new RectHV(currRect.xmin(), currPointY, prevPointX, currRect.ymax());
        } else if (curr.comparator == Point2D.Y_ORDER) {
            final double currPointY = curr.point.y();
            final double prevPointX = prev == null ? 0 : prev.point.x();
            leftRect = new RectHV(prevPointX, currRect.ymin(), currRect.xmax(), currPointY);
            rightRect = new RectHV(prevPointX, currPointY, currRect.xmax(), currRect.ymax());
        } else {
            throw new IllegalStateException("Unknown comparator used for comparing points inside tree nodes");
        }
        return new SplitRect(leftRect, rightRect);
    }

    private static class SplitRect {
        private final RectHV leftRect;
        private final RectHV rightRect;

        private SplitRect(RectHV leftRect, RectHV rightRect) {
            this.leftRect = leftRect;
            this.rightRect = rightRect;
        }
    }

    private static class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private final Comparator<Point2D> comparator;

        private Node(Point2D point, Node left, Node right, Comparator<Point2D> comparator) {
            this.point = point;
            this.left = left;
            this.right = right;
            this.comparator = comparator;
        }
    }

    public static void main(String[] args) {
        StdDraw.clear();

        // drawing all the points from the tree
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.372, 0.497));
        kdTree.insert(new Point2D(0.564, 0.413));
        kdTree.insert(new Point2D(0.226, 0.577));
        kdTree.insert(new Point2D(0.144, 0.179));
        kdTree.insert(new Point2D(0.083, 0.51));
        kdTree.insert(new Point2D(0.32, 0.708));
        kdTree.insert(new Point2D(0.417, 0.362));
        kdTree.insert(new Point2D(0.862, 0.825));
        kdTree.insert(new Point2D(0.785, 0.725));
        kdTree.insert(new Point2D(0.499, 0.208));
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
        Point2D pointQueried = new Point2D(0.5, 0.5);
        pointQueried.draw();
        System.out.println("Nearest to " + pointQueried + " is " + kdTree.nearest(pointQueried));
        StdDraw.show();
    }
}
