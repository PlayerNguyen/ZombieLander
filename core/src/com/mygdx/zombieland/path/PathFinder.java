package com.mygdx.zombieland.path;

import com.mygdx.zombieland.World;
import com.mygdx.zombieland.location.Location;
import com.mygdx.zombieland.utils.FastMatrix;
import com.mygdx.zombieland.utils.Pair;

import java.util.*;

public class PathFinder {

    private Location source;
    private Location target;

    private final FastMatrix<Boolean> restrictedLocations;

    public PathFinder(Location source, Location target) {
        this.source = source;
        this.target = target;
        this.restrictedLocations = new FastMatrix<>();
    }

    public void addRestricted(FastMatrix<?> matrix) {
        for (Map.Entry<FastMatrix.Key, ?> keyEntry : matrix.entrySet()) {
            int x = keyEntry.getKey().x;
            int y = keyEntry.getKey().y;

            this.restrictedLocations.set(x, y, true);
        }
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public FastMatrix<Double> getPath(int offsetBond) {
        FastMatrix<Double> visited = new FastMatrix<>();

        Queue<Location> locations = new PriorityQueue<>();
        locations.add(new Location(source));

        int[] xDirection = new int[]{1, -1, 0, 0};
        int[] yDirection = new int[]{0, 0, 1, -1};

        while (!locations.isEmpty()) {

            Location curLocation = locations.poll();
            if (curLocation.x == target.x && curLocation.y == target.y) {
                // Success reach destination
                break;
            }

            Queue<PathMovement> distanceLocations = new PriorityQueue<>();
            for (int i = 0; i < xDirection.length; i++) {
                int nextX = (int) (curLocation.x + xDirection[i]);
                int nextY = (int) (curLocation.y + yDirection[i]);

                if (nextX < 0 || nextY < 0) continue;
                if (nextX > World.WINDOW_WIDTH || nextY > World.WINDOW_HEIGHT) continue;

                boolean isBlocked = false;
                for (int offsetX = -offsetBond; offsetX < offsetBond; offsetX++) {
                    for (int offsetY = -offsetBond; offsetY < offsetBond; offsetY++) {
                        if (this.restrictedLocations.get(nextX + offsetX, nextY + offsetY) != null) {
                            isBlocked = true;
                        }
                    }
                }

                if (isBlocked) {
                    continue;
                }

                if (visited.get(nextX, nextY) != null) {
                    continue;
                }
                Location nextLocation = new Location(nextX, nextY);
                distanceLocations.add(new PathMovement(nextLocation, nextLocation.distance(target)));
            }

            // Find minimum. This step require O(E) with E is neighbor that we have not explored
            PathMovement minLocationToTarget = distanceLocations.poll();
            if (minLocationToTarget == null) {
//                throw new RuntimeException("Invalid search location");
                return new FastMatrix<>();
            }
//            System.out.println("Takes minLocationToTarget at " + minLocationToTarget.getFirst() + " with minDistance " + minLocationToTarget.getSecond());

            visited.set((int) minLocationToTarget.getFirst().x, (int) minLocationToTarget.getFirst().y, minLocationToTarget.getSecond());
            locations.add(minLocationToTarget.getFirst());
        }

        return visited;
    }

    private static class PathMovement extends Pair<Location, Double> implements Comparator<Pair<Location, Double>>, Comparable<PathMovement> {

        public PathMovement(Location first, Double second) {
            super(first, second);
        }

        @Override
        public int compare(Pair<Location, Double> o1, Pair<Location, Double> o2) {
            return Double.compare(o1.getSecond(), o2.getSecond());
        }

        @Override
        public int compareTo(PathMovement o) {
            return Double.compare(this.getSecond(), o.getSecond());
        }
    }
}
