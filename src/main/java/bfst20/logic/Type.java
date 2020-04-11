package bfst20.logic;

import javafx.scene.paint.Color;


public enum Type {
    UNKNOWN,
    BOUNDS,
    BUILDING,
    HIGHWAY,
    COASTLINE,
    WATER,
    GREEN,
    HEATH,
    FOREST,
    PARKING,
    NATURAL,
    FARMLAND,
    TREE_ROW,
    LANDUSE,
    RESIDENTIAL,
    WOOD,
    PLACE;

    public static double getZoomLevel(Type type){
        switch (type){
            case COASTLINE:
            case HIGHWAY:
                return 1;
            case FOREST:
            case HEATH:
            case WATER:
            case FARMLAND:
            case NATURAL:
            case WOOD:
            case TREE_ROW:
            case GREEN:
                return 1.3011416847239474E7;
            case RESIDENTIAL:
            case BUILDING:
                return 2.8945867784311756E7;
            default:
                return Double.POSITIVE_INFINITY;
        }
    }

    public static boolean getFill(Type type) {
        switch (type) {
            case NATURAL:
            case FARMLAND:
            case FOREST:
            case WATER:
            case COASTLINE:
            case BUILDING:
            case RESIDENTIAL:
            case WOOD:
            case TREE_ROW:
            case HEATH:
            case GREEN:
                return true;
            default: //TODO FIX
                return false;
        }
    }



    public static Color getColor(Type type, Boolean colorBlindMode) {
        if (!colorBlindMode) return getNormalColor(type);
        else return getColorBlindColor(type);
    }

    public  static Color getNormalColor(Type type){
        switch (type) {
            case PLACE:
            case COASTLINE:
            case GREEN:
                return Color.LIGHTGREEN;
            case WATER:
                return Color.BLUE;
            case BUILDING:
                return Color.BROWN;
            case HIGHWAY:
                return Color.BLACK;
            case HEATH:
                return Color.rgb(255, 178, 102, 0.6);
            case RESIDENTIAL:
                return Color.rgb(128, 128, 128, 0.6);
            case FARMLAND:
                return Color.rgb(238, 240, 213, 0.6);
            case WOOD:
            case FOREST:
            case TREE_ROW:
                return Color.rgb(0, 102, 0, 0.7);
            case PARKING:
                return Color.RED;
            case NATURAL:
                return Color.BLUEVIOLET;
            default: //TODO FIX
                return Color.TRANSPARENT;
        }

    }

    public static Color getColorBlindColor(Type type){
        switch (type) {
            case PLACE:
            case COASTLINE:
            case GREEN:
                return Color.PINK;
            case WATER:
                return Color.RED;
            case BUILDING:
                return Color.YELLOW;
            case HIGHWAY:
                return Color.BURLYWOOD;
            case HEATH:
                return Color.rgb(255, 178, 102, 0.6);
            case RESIDENTIAL:
                return Color.rgb(128, 128, 128, 0.6);
            case FARMLAND:
                return Color.rgb(238, 240, 213, 0.6);
            case WOOD:
            case FOREST:
            case TREE_ROW:
                return Color.rgb(0, 102, 0, 0.7);
            case PARKING:
                return Color.RED;
            case NATURAL:
                return Color.BLUEVIOLET;
            default: //TODO FIX
                return Color.TRANSPARENT;
        }

    }
}