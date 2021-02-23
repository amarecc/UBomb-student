/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;

public interface Movable {

    /**
     * @param direction The direction where the GameObject want to go
     * @return True if it's possible to moove at the next position, false otherwise
     */
    boolean canMove(Direction direction);

    /**
     * Change the position of the GameObject with the next position
     * @param direction The direction where the GameObject go
     */
    void doMove(Direction direction);
}
