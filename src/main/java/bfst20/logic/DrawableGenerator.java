package bfst20.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.presentation.LinePath;
import javafx.scene.paint.Color;

public class DrawableGenerator {

    private Map<Type, List<LinePath>> drawables = new HashMap<>();
    private Map<Node, Way> nodeToCoastline = new HashMap<>();
    private Map<Node, Way> nodeToForest = new HashMap<>();
    private Map<Node, Way> nodeToFarmland = new HashMap<>();
    private List<Way> OSMWays;
    private Map<Long, Node> OSMNodes;
    private List<Relation> OSMRelations;
    private static boolean loaded = false;
    private static DrawableGenerator drawableGenerator;
    private AppController appController;

    private DrawableGenerator() {
        appController = new AppController();
        OSMWays = appController.getOSMWaysFromModel();
        OSMNodes = appController.getOSMNodesFromModel();
        OSMRelations = appController.getOSMRelationsFromModel();
    }

    public static DrawableGenerator getInstance() {
        if (!loaded) {
            drawableGenerator = new DrawableGenerator();
        }

        return drawableGenerator;
    }

    public Map<Type, List<LinePath>> createDrawables() {

        createWays();

        createRelations();

        return drawables;
    }

    private void createWays() {
        for (Way way : OSMWays) {
            if (way.getTagValue("natural") != null && way.getTagValue("natural").equals("coastline")) continue;

            LinePath linePath = createLinePath(way);

            Type type = linePath.getType();
            if (drawables.get(type) == null) {

                drawables.put(type, new ArrayList<>());
            }

            if (type != Type.PLACE) {
                drawables.get(type).add(linePath);
            }
        }
    }


    private void createRelations() {
        for (Relation relation : OSMRelations) {

            if (relation.getTag("landuse") != null && relation.getTag("landuse").contains("forest")) {

                connectWays(relation, nodeToForest);

            } else if (relation.getTag("landuse") != null && relation.getTag("landuse").contains("farmland")) {

                connectWays(relation, nodeToFarmland);

            } else if (relation.getTag("name") != null && relation.getTag("name").contains("Region")) {
                if (!drawables.containsKey(Type.COASTLINE)) {
                    drawables.put(Type.COASTLINE, new ArrayList<>());
                }

                connectWays(relation, nodeToCoastline);

            }
        }

        addRelation(Type.FOREST, nodeToForest);
        addRelation(Type.FARMLAND, nodeToFarmland);
        addRelation(Type.COASTLINE, nodeToCoastline);
    }

    private void connectWays(Relation relation, Map<Node, Way> nodeTo) {
        Collections.sort(relation.getMembers());
        for (long entry : relation.getMembers()) {
            Way way = (binarySearch(OSMWays, entry));

            if (way == null) continue;

            Way before = removeWayBefore(way, nodeTo);
            Way after = removeWayAfter(way, nodeTo);

            way = merge(merge(before, way), after);

            nodeTo.put(OSMNodes.get(way.getFirstNodeId()), way);
            nodeTo.put(OSMNodes.get(way.getLastNodeId()), way);
        }
    }


    private void addRelation(Type type, Map<Node, Way> nodeTo) {
        for (Map.Entry<Node, Way> entry : nodeTo.entrySet()) {
            if (entry.getKey() == OSMNodes.get(entry.getValue().getLastNodeId())) {
                drawables.get(type).add(
                        new LinePath(entry.getValue(), type, OSMNodes, Type.getColor(type), true));
            }
        }
    }


    private LinePath createLinePath(Way way) {
        Type type = Type.UNKNOWN;

        try {

            if (way.containsKey("landuse") || way.containsKey("natural")) {
                type = getSubType(way);
            } else {
                type = Type.valueOf(way.getFirstTag()[0].toUpperCase());
            }
        } catch (Exception err) {

        }

        Color color = Type.getColor(type);
        Boolean fill = Type.getFill(type);

        return new LinePath(way, type, OSMNodes, color, fill);
    }

    private Way removeWayAfter(Way way, Map<Node, Way> nodeTo) {
        Way after = nodeToCoastline.remove(OSMNodes.get(way.getLastNodeId()));
        if (after != null) {
            nodeToCoastline.remove(OSMNodes.get(after.getFirstNodeId()));
            nodeToCoastline.remove(OSMNodes.get(after.getLastNodeId()));
        }
        return after;
    }

    private Way removeWayBefore(Way way, Map<Node, Way> nodeTo) {
        Way before = nodeToCoastline.remove(OSMNodes.get(way.getFirstNodeId()));
        if (before != null) {
            nodeToCoastline.remove(OSMNodes.get(before.getFirstNodeId()));
            nodeToCoastline.remove(OSMNodes.get(before.getLastNodeId()));
        }
        return before;
    }

    private Way merge(Way before, Way after) {
        if (before == null) return after;
        if (after == null) return before;

        Way way = new Way();
        // Why do we need this? Seems to do the same without it
        if (before.getFirstNodeId() == after.getFirstNodeId()) {
            way.addAllNodeIds(before);

            Collections.reverse(way.getNodeIds());
            way.getNodeIds().remove(way.getNodeIds().size() - 1);
            way.addAllNodeIds(after);
        } else if (before.getFirstNodeId() == after.getLastNodeId()) {

            addWayToMerge(way, after, before);

        } else if (before.getLastNodeId() == after.getFirstNodeId()) {

            addWayToMerge(way, before, after);
        }

        // Why do we need this? Seems to do the same without it
        else if (before.getLastNodeId() == after.getLastNodeId()) {
            Way tmp = new Way(after);

            Collections.reverse(tmp.getNodeIds());
            way.addAllNodeIds(before);
            way.getNodeIds().remove(way.getNodeIds().size() - 1);
            way.addAllNodeIds(tmp);
        } else {
            throw new IllegalArgumentException("Cannot merge unconnected OSMWays");
        }

        return way;
    }

    private Type getSubType(Way way) {
        Type newType;

        if (way.containsKey("natural")) {
            newType = Type.valueOf(way.getTagValue("natural").toUpperCase());
        } else {
            newType = Type.valueOf(way.getTagValue("landuse").toUpperCase());
        }

        return newType;
    }

    //Order of before and after depends on the context
    private void addWayToMerge(Way way, Way before, Way after) {
        way.addAllNodeIds(before);
        way.getNodeIds().remove(way.getNodeIds().size() - 1);
        way.addAllNodeIds(after);
    }


    private Way binarySearch(List<Way> list, long id) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Way midElement = list.get(mid);
            long midId = midElement.getId();

            if (midId < id) {
                low = mid + 1;
            } else if (midId > id) {
                high = mid - 1;
            } else {
                return midElement;
            }
        }
        return null;
    }

}