package fr.ubx.poo.model.decor.bonus;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.decor.Decor;

public class BonusBombNbInc extends Decor {
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
        game.getPlayer().increaseBombs();
    }
    @Override
    public String toString() {
        return "BonusBombNbInc";
    }
}
