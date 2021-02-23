package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;

public class Princess extends Decor{
    public boolean canWalkOn(Game game){
        game.getPlayer().setWinner(true);
        return true;
    }
    @Override
    public String toString() {
        return "Princess";
    }
}
