package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;

public class Door extends Decor {
    private boolean isOpen;
    private boolean goNext;

    public Door(boolean isOpen, boolean goNext){
        this.isOpen = isOpen;
        this.goNext = goNext;
    }

    public boolean canWalkOn(Game game){
        return isOpen;
    }

    public boolean canInteractWith() {
        return !isOpen;
    }

    public boolean getIsOpen(){
        return isOpen;
    }

    public boolean getGoNext(){
        return goNext;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public void take(Game game) {
        if (goNext)
            game.getWorld().nextLevel();
        else
            game.getWorld().prevLevel();
    }

    @Override
    public void interact(Game game) {
        if (game.getPlayer().getKeys() > 0) {
            this.isOpen = true;
            game.getWorld().hasChanged = true;
            game.getPlayer().decreaseKeys();
        }
    }

    @Override
    public String toString() {
        return "Door";
    }
}
