package fr.ubx.poo.model.go.monster;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.utils.Timer;

import java.util.ArrayList;
import java.util.List;

public class Monster extends GameObject implements Movable{
    private Direction direction;
    private double mooveCD;
    private Timer TimerCD;
    private boolean alive = true;
    private int levelId;

    public Monster(Game game, Position pos, int levelId){
        super(game, pos);
        direction = Direction.S;
        this.mooveCD = 1;
        this.TimerCD = new Timer(this.mooveCD);
        this.levelId = levelId;
    }

    public Direction getDirection(){ return direction; }

    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());

        if(! this.game.getWorld().isInside(nextPos, levelId)) return false;
        if (! this.game.getWorld().isEmpty(nextPos, levelId)){
            if(! this.game.getWorld().get(nextPos, levelId).isABonus()) return false;
        }
        for(Bomb b : this.game.getWorld().getBombs(levelId)){
            if(b.getPosition().equals(nextPos)) return false;
        }
        for(Monster m : game.getWorld().getMonsters(levelId)){
            if(m.getPosition().equals(nextPos)) return false;
        }
        return true;
    }

    public void doMove(Direction direction) {
        this.setPosition(direction.nextPosition(getPosition()));
    }

    public Timer getTimerCD() {
        return TimerCD;
    }

    public double getMooveCD() {
        return mooveCD;
    }

    public void setMooveCD(double mooveCD) {
        this.mooveCD = mooveCD;
    }

    public void kill(){
        this.alive = false;
        game.getWorld().clear(getPosition());
    }

    public boolean isAlive(){
        return this.alive;
    }

    /**
     * change the variable "direction" with a new random direction.
     */
    public void randomMoove(){
        direction = Direction.random();
        if(! canMove(Direction.N) && ! canMove(Direction.E) && ! canMove(Direction.S) && ! canMove(Direction.W)) return;
        while(! canMove(direction)) direction = Direction.random();
    }

    /**
     * Find the best way to acces the player and put in the variable "direction"
     * the direction of the 1st moovement.
     * If there is no way between the monster and the player, call randomMoove().
     */
    public void bestMoove(){
        Position IniPos = getPosition();
        if(IniPos.equals(this.game.getPlayer().getPosition())){
            TimerCD = new Timer(this.mooveCD);
            return;
        }

        Flag flag = new Flag(20); // 20 is the lenght max of a way (more can create latency).

        recbestMoove(IniPos, Direction.N, 0, new ArrayList<>(), this.game, flag, this.levelId);
        int up = flag.getValue();

        recbestMoove(IniPos, Direction.E, 0, new ArrayList<>(), this.game, flag, this.levelId);
        int right = flag.getValue();

        recbestMoove(IniPos, Direction.S, 0, new ArrayList<>(), this.game, flag, this.levelId);
        int down = flag.getValue();

        recbestMoove(IniPos, Direction.W, 0, new ArrayList<>(), this.game, flag, this.levelId);
        int left = flag.getValue();

        if(flag.getValue() == 20) randomMoove(); // No way found.

        else if(up <= right && up <= down && up <= left) direction = Direction.N;

        else if(right <= up && right <= down && right <= left) direction = Direction.E;

        else if(down <= right && down <= up && down <= left) direction = Direction.S;

        else if(left <= right && left <= down && left <= up) direction = Direction.W;

        if(canMove(direction)) doMove(direction);
        TimerCD = new Timer(this.mooveCD);
    }

    /**
     * Recursive function used to find the best way
     * @param dist increment with each moovement
     * @param listPos list of position already visited
     */
    private static void recbestMoove(Position pos, Direction dir, int dist, List <Position> listPos, Game g, Flag flag, int levelId){
        if(dist >= flag.getValue()) return; // Shortest way already found.
        if(pos.equals(g.getPlayer().getPosition())){ // Way found.
            flag.setValue(dist);
            return;
        }

        if(! canMoveStatic(dir, pos, listPos, g, levelId)) return;

        pos = dir.nextPosition(pos);
        dist++;
        listPos.add(pos);

        recbestMoove(pos, Direction.N, dist, new ArrayList<>(listPos), g, flag, levelId); // up

        recbestMoove(pos, Direction.E, dist, new ArrayList<>(listPos), g, flag, levelId); // right

        recbestMoove(pos, Direction.S, dist, new ArrayList<>(listPos), g, flag, levelId); // down

        recbestMoove(pos, Direction.W, dist, new ArrayList<>(listPos), g, flag, levelId); // left
    }

    /**
     * static version of canMoove to use it in recbestMoove
     * @param listPos return false if the next position is in
     */
    private static boolean canMoveStatic(Direction dir, Position pos, List <Position> listPos, Game g, int levelId) {
        Position nextPos = dir.nextPosition(pos);

        if(! g.getWorld().isInside(nextPos, levelId)) return false;
        if (! g.getWorld().isEmpty(nextPos, levelId)){
            if(! g.getWorld().get(nextPos, levelId).isABonus()) return false;
        }
        for(Bomb b : g.getWorld().getBombs(levelId)){
            if(b.getPosition().equals(nextPos)) return false;
        }
        for(Monster m : g.getWorld().getMonsters(levelId)){
            if(m.getPosition().equals(nextPos)) return false;
        }

        for(Position p : listPos){
            if(p.equals(nextPos)) return false;
        }
        return true;
    }
}
