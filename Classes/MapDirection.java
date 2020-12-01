package Classes;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public String toString(){
        return switch(this) {
            case NORTH -> "NORTH";
            case NORTHEAST -> "NORTH-EAST";
            case EAST -> "EAST";
            case SOUTHEAST -> "SOUTH-EAST";
            case SOUTH -> "SOUTH";
            case SOUTHWEST -> "SOUTH-WEST" ;
            case WEST -> "WEST";
            case NORTHWEST -> "NORTH-WEST";
        };
    }

    public Vector2d toUnitVector(){
        return switch(this) {
            case NORTH -> new Vector2d(0,1);
            case NORTHEAST -> new Vector2d(1,1);
            case EAST -> new Vector2d(1,0);
            case SOUTHEAST -> new Vector2d(1,-1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }

    public int getValueOfDirection(){
        return switch(this) {
            case NORTH ->  0;
            case NORTHEAST -> 1;
            case EAST -> 2;
            case SOUTHEAST -> 3;
            case SOUTHWEST -> 4 ;
            case SOUTH -> 5;
            case WEST -> 6;
            case NORTHWEST -> 7;
        };
    }

    public static MapDirection getDirectionFromValue(int value) {
        return switch(value){
            case 0 -> NORTH;
            case 1 -> NORTHEAST;
            case 2 -> EAST;
            case 3 -> SOUTHEAST;
            case 4 -> SOUTHWEST;
            case 5 -> SOUTH;
            case 6 -> WEST;
            case 7 -> NORTHWEST;
            default -> throw new IllegalArgumentException("Value not in range 0-7!");
        };
    };




}
