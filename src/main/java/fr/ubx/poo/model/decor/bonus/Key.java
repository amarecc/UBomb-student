package fr.ubx.poo.model.decor.bonus;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.Decor;

public class Key extends Decor {

    public boolean canWalkOn(Game game){
        return true;
    }

    public boolean isABonus(){
        return true;
    }

    public boolean canInteractWith() {
        return true;
    }

    public void take(Game game){
        game.getPlayer().increaseKeys();
    }
    @Override
    public String toString() {
        return "Key";
    }
}
