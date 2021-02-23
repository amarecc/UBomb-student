/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.monster.Monster;
import fr.ubx.poo.utils.Timer;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private boolean interactionRequested = false;
    private int lives = 1;
    private Timer invincibility = new Timer(0);
    private int keys = 0;
    private int bombs = 1;
    private int rangeBomb = 1;
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

    public int getBombs() { return bombs; }

    public int getRangeBomb() { return rangeBomb; }

    public Direction getDirection() {
        return direction;
    }

    public boolean collisionMonster() {
        for (Monster monster :game.getWorld().getMonsters()){
            if (monster.getPosition().equals(this.getPosition())){
                return true;
            }
        }

        return false;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (this.game.getWorld().isInside(nextPos)){

            if (this.game.getWorld().isEmpty(nextPos)){
                for(Bomb b : game.getWorld().getBombs()){
                    if(b.getPosition().equals(nextPos)) return false;
                }

                return true;
            }

            return this.game.getWorld().get(nextPos).canWalkOn(game);
        }

        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if (!this.game.getWorld().isEmpty(nextPos))
            this.game.getWorld().get(nextPos).take(game);

        setPosition(nextPos);
        this.game.getWorld().clear(nextPos);
    }

    public void requestInteract(){
        this.interactionRequested = true;
    }

    public boolean canInteractWith(){
        Position nextPos = direction.nextPosition(getPosition());

        if (this.game.getWorld().isInside(nextPos)){
            if (!this.game.getWorld().isEmpty(nextPos))
                return this.game.getWorld().get(nextPos).canInteractWith();
        }

        return false;
    }

    public void doInteract() {
        Position nextPos = direction.nextPosition(getPosition());

        this.game.getWorld().get(nextPos).interact(game);
        this.game.getWorld().set(nextPos, this.game.getWorld().get(nextPos));
    }

    public void update(long now) {

        if(invincibility.getSec() != 0) invincibility.decreaseTimer(); // réduction du temps d'invincibilité

        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
            moveRequested = false;
        }

        if (interactionRequested) {
            if (canInteractWith()) {
                doInteract();
            }
            interactionRequested = false;
        }

        if (collisionMonster())
            decreaseLives();
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public Timer getInvincibility() {
        return invincibility;
    }

    public boolean isInvicible() {
        return invincibility.getSec() > 0;
    }

    public void decreaseLives() {
        if(invincibility.getSec() == 0){
            this.lives--;
            this.invincibility = new Timer(1);

            if (this.lives == 0)
                this.alive = false;
        }
    }

    public void decreaseBombs() {
        if (this.bombs > 0)
    	    this.bombs--;
    }

    public void increaseBombs() {
    	this.bombs++;
    }

    public void decreaseRange() {
    	this.rangeBomb--;
    }

    public void increaseRange() {
    	this.rangeBomb++;
    }

    public void increaseKeys(){
        this.keys++;
    }

    public void decreaseKeys() {
        this.keys--;
    }

    public void increaseLives(){
        this.lives++;
    }

    public void setWinner(boolean isWinning){
        this.winner = isWinning;
    }

}
