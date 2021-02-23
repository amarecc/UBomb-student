package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Box extends Decor {

    public boolean canWalkOn(Game game){
        Position boxPos = game.getPlayer().getDirection().nextPosition(game.getPlayer().getPosition());
        Position nextBoxPos = game.getPlayer().getDirection().nextPosition(boxPos);

        if (nextBoxPos.inside(game.getWorld().getDimension())){
            return game.getWorld().isEmpty(nextBoxPos) && !game.getWorld().isGameObject(nextBoxPos);
        }

        return false;
    }

    public void take(Game game){
        Position boxPos = game.getPlayer().getDirection().nextPosition(game.getPlayer().getPosition());
        Position nextBoxPos = game.getPlayer().getDirection().nextPosition(boxPos);

        game.getWorld().clear(boxPos);
        game.getWorld().set(nextBoxPos, this);
    }

    public boolean canExplode() {
        return true;
    }

    @Override
    public String toString() {
        return "Box";
    }
}
