package bfst20.data;

import bfst20.logic.misc.OSMType;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.entities.LinePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinePathData {

    private static boolean isLoaded = false;
    private static LinePathData linePathData;
    private Map<OSMType, List<LinePath>> linePaths;
    private List<LinePath> highWays;
    private Map<Node, Way> nodeToCoastline;
    private Map<Node, Way> nodeToForest;
    private Map<Node, Way> nodeToFarmland;

    private LinePathData() {
        linePaths = new HashMap<>();
        nodeToCoastline = new HashMap<>();
        nodeToForest = new HashMap<>();
        nodeToFarmland = new HashMap<>();
        highWays = new ArrayList<>();
    }

    public static LinePathData getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            linePathData = new LinePathData();
        }
        return linePathData;
    }

    public void saveHighways(List<LinePath> highways) {
        this.highWays = highways;
    }

    public List<LinePath> getHighWays() {
        return this.highWays;
    }

    public Map<OSMType, List<LinePath>> getLinePaths() {
        return linePaths;
    }

    public void addLinePath(OSMType OSMType, LinePath linePath) {
        if (linePaths.get(OSMType) == null) linePaths.put(OSMType, new ArrayList<>());
        linePaths.get(OSMType).add(linePath);
    }

    public void setLinePaths(Map<OSMType, List<LinePath>> linePaths) {
        this.linePaths = linePaths;
    }


    public void addType(OSMType OSMType) {
        linePaths.put(OSMType, new ArrayList<>());
    }


    public Map<Node, Way> getNodeToCoastline() {
        return nodeToCoastline;

    }

    public void addNodeToForest(Node node, Way way) {
        nodeToForest.put(node, way);
    }

    public void addToNodeToFarmland(Node node, Way way) {
        nodeToFarmland.put(node, way);
    }

    public void addToNodeToCoastline(Node node, Way way) {
        nodeToCoastline.put(node, way);

    }

    public Map<Node, Way> getNodeToForest() {
        return nodeToForest;
    }

    public Map<Node, Way> getNodeToFarmland() {
        return nodeToFarmland;
    }

    public Way removeWayFromNodeToForest(Node node) {
        return nodeToForest.remove(node);
    }

    public Way removeWayFromNodeToFarmland(Node node) {
        return nodeToFarmland.remove(node);
    }

    public Way removeWayFromNodeToCoastline(Node node) {
        return nodeToCoastline.remove(node);
    }


    public void clearData() {
        linePaths = new HashMap<>();
        nodeToCoastline = new HashMap<>();
        nodeToForest = new HashMap<>();
        nodeToFarmland = new HashMap<>();
        System.gc();
    }
}
