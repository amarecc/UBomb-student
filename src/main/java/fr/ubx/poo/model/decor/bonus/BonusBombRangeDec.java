package fr.ubx.poo.model.decor.bonus;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.Decor;

public class BonusBombRangeDec extends Decor {

    public boolean canWalkOn(Game game){
        return true;
    }

    public boolean isABonus(){
        return true;
    }

    public boolean canExplode() {
        return true;
    }

    public void take(Game game){
        if(game.getPlayer().getRangeBomb() > 1) game.getPlayer().decreaseRange();
    }

    @Override
    public String toString() {
        return "BonusBombRangeDec";
    }
}
