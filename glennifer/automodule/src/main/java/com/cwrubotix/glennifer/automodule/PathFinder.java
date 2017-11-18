package com.cwrubotix.glennifer.automodule;

import com.cwrubotix.glennifer.automodule.PathFindingAlgorithm;
import com.cwrubotix.glennifer.automodule.Path;
import com.cwrubotix.glennifer.automodule.Position;
import com.cwrubotix.glennifer.automodule.Obstacle;

import java.util.ArrayList;


/**
 * @author Robbie Dozier
 *
 * Unfinished
 * @param <T> Path finding algorithm
 */
public class PathFinder<T extends PathFindingAlgorithm> {
    private T pathFindingAlgorithm;
    private Path path;
    private Position currentPos;
    private Position startPos;
    private Position targetPos;

    public PathFinder(T pathFindingAlgorithm, Position startPos, Position targetPos) {
        this.pathFindingAlgorithm = pathFindingAlgorithm;
        this.startPos = startPos;
        this.targetPos = targetPos;
        path = pathFindingAlgorithm.computePath(this.startPos, this.targetPos);
        this.currentPos = path.getStart();
    }

    public Path getPath() {
        return path;
    }

    public Position getCurrentPos() {
        return currentPos;
    }

    public Position getStartPos() {
        return startPos;
    }

    public Position getTargetPos() {
        return targetPos;
    }

    public void registerObstacle(Obstacle obstacle) {
        path = pathFindingAlgorithm.computePath(currentPos, obstacle);
    }
}
