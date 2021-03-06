package fr.ubx.poo.game.World;

import fr.ubx.poo.game.Dimension;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.bonus.*;

import java.util.Hashtable;
import java.util.Map;

public class WorldBuilder {
    private final Map<Position, Decor> grid = new Hashtable<>();

    private WorldBuilder() {
    }

    public static Map<Position, Decor> build(WorldEntity[][] raw, Dimension dimension) {
        WorldBuilder builder = new WorldBuilder();
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                Position pos = new Position(x, y);
                Decor decor = processEntity(raw[y][x]);
                if (decor != null)
                    builder.grid.put(pos, decor);
            }
        }
        return builder.grid;
    }

    private static Decor processEntity(WorldEntity entity) {
        switch (entity) {
            case BombNumberDec:
                return new BonusBombNbDec();
            case BombNumberInc:
                return new BonusBombNbInc();
            case BombRangeDec:
                return new BonusBombRangeDec();
            case BombRangeInc:
                return new BonusBombRangeInc();
            case Heart:
                return new Heart();
            case Princess:
                return new Princess();
            case Key:
                return new Key();
            case Box:
                return new Box();
            case Stone:
                return new Stone();
            case Tree:
                return new Tree();
            case DoorNextClosed:
                return new Door(false, true);
            case DoorNextOpened:
                return new Door(true, true);
            case DoorPrevOpened:
                return new Door(true, false);
            default:
                return null;
        }
    }
}
