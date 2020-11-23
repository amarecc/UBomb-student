/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Key;
import fr.ubx.poo.model.decor.Princess;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.monster.Monster;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player extends GameObject implements Movable {

    private final boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private int keys = 0;
    private boolean winner;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public int getKeys() { return keys; }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        return true;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        //Do move if there is no decor
        if (this.game.getWorld().isInside(nextPos)){
            if (this.game.getWorld().isEmpty(nextPos))
                setPosition(nextPos);

            else if (this.game.getWorld().get(nextPos) instanceof Key){
                this.game.getWorld().clear(nextPos);
                this.keys++;
                setPosition(nextPos);
            }

            else if (this.game.getWorld().get(nextPos) instanceof Princess){
                setPosition(nextPos);
                this.winner = true;
            }
        }
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;

        if (this.collisionMonster()){
            this.lives--;
        }
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return this.lives > 0;
    }

    public boolean collisionMonster() {
        for (Monster monster :this.game.getMonsters()){
            if (monster.getPosition().equals(this.getPosition())){
                return true;
            }
        }

        return false;
    }

}
