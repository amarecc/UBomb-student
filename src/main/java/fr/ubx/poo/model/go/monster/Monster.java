package fr.ubx.poo.model.go.monster;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.GameObject;

public class Monster extends GameObject {
    private Direction direction;

    public Monster(Game game, Position pos){
        super(game, pos);
        direction = Direction.S;
    }

    public Direction getDirection(){ return direction; }
}
