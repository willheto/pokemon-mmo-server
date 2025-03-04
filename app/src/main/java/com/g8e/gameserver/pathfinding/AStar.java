package com.g8e.gameserver.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.g8e.gameserver.World;
import com.g8e.gameserver.tile.TilePosition;

public class AStar {

    private final World world;

    public AStar(World world) {
        this.world = world;
    }

    public List<PathNode> findPath(int startX, int startY, int targetX, int targetY) {
        PriorityQueue<PathNode> openList = new PriorityQueue<>(Comparator.comparingInt(a -> a.f));
        Set<PathNode> closedList = new HashSet<>();

        PathNode startPathNode = new PathNode(startX, startY, null);
        PathNode targetPathNode = new PathNode(targetX, targetY, null);

        if (world.tileManager.getCollisionByXandY(targetX, targetY)) {
            // find closest walkable tile
            TilePosition closestWalkableTile = world.tileManager.getClosestWalkableTile(targetX, targetY);

            if (closestWalkableTile == null) {
                return new ArrayList<>(); // Early exit if target is unreachable
            }

            targetPathNode = new PathNode(closestWalkableTile.x, closestWalkableTile.y, null);

        }

        openList.add(startPathNode);

        while (!openList.isEmpty()) {
            PathNode currentPathNode = openList.poll();

            // Check if we've reached the target
            if (currentPathNode.equals(targetPathNode)) {
                return constructPath(currentPathNode);
            }

            closedList.add(currentPathNode);

            // Get the neighbors (up, down, left, right)
            List<PathNode> neighbors = getNeighbors(currentPathNode);

            for (PathNode neighbor : neighbors) {
                if (closedList.contains(neighbor))
                    continue;

                int gCost = currentPathNode.g + getDistance(currentPathNode, neighbor);
                boolean isInOpenList = openList.contains(neighbor);

                // If the neighbor is not in the open list, or we found a shorter path to it
                if (!isInOpenList || gCost < neighbor.g) {
                    neighbor.g = gCost;
                    neighbor.h = getDistance(neighbor, targetPathNode); // Heuristic cost
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = currentPathNode;

                    if (!isInOpenList) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        // If no path is found, return an empty list
        return new ArrayList<>();
    }

    private List<PathNode> getNeighbors(PathNode current) {
        List<PathNode> neighbors = new ArrayList<>();

        int[][] directions = {
                { 0, 1 }, // down
                { 1, 0 }, // right
                { 0, -1 }, // up
                { -1, 0 } // left
        };

        for (int[] direction : directions) {
            int newX = current.x + direction[0];
            int newY = current.y + direction[1];

            // Skip adding the tile if there's a collision
            if (world.tileManager.getCollisionByXandY(newX, newY)) {
                continue;
            }

            // Add only walkable, collision-free tiles
            neighbors.add(new PathNode(newX, newY, current));
        }

        return neighbors;
    }

    private int getDistance(PathNode a, PathNode b) {
        // Using Manhattan distance as the heuristic
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private List<PathNode> constructPath(PathNode currentPathNode) {
        List<PathNode> path = new ArrayList<>();
        while (currentPathNode != null) {
            path.add(currentPathNode);
            currentPathNode = currentPathNode.parent;
        }
        Collections.reverse(path); // Reverse to get the path from start to target
        return path;
    }

}