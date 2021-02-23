/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Entity;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {

    /**
     * Check if the decor could be walked on
     * @param game
     * @return True if the decor can be walked on, false otherwise
     */
    public boolean canWalkOn(Game game){
        return false;
    }

    /**
     * Check if an interaction (Key enter pressed) is possible
     * @return True if an interaction is possible, false otherwise
     */
    public boolean canInteractWith() {
        return false;
    }

    /**
     * Check if the decor is a bonus
     * @return True if the decor is a bonus, false otherwise
     */
    public boolean isABonus(){
        return false;
    }

    /**
     * Check if the decor could explode
     * @return True if the decor could explode, false otherwise
     */
    public boolean canExplode() {
        return false;
    }

    /**
     * Function to called if the decor could be walked on
     * @param game
     */
    public void take(Game game){
    }

    /**
     * Function to call if the decor accept an interaction
     * @param game
     */
    public void interact(Game game) {
    }
}
