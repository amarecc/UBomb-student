package fr.ubx.poo.game.World.Level;

import fr.ubx.poo.game.Dimension;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.PositionNotFoundException;
import fr.ubx.poo.game.World.WorldEntity;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.monster.Monster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelEntities {
    private int levelId;
    private int difficulty;
    private double monsterSpeed;
    private WorldEntity[][] raw;
    private Dimension dimension;
    private List<Bomb> bombs = new ArrayList<>();
    private List<Monster> monsters = new ArrayList<>();

    /**
     * Create a new instance of LevelEntities with the specified levelId, raw and dimension
     * @param levelId Id of the level
     * @param raw 2d Array of WorldEntity
     * @param dimension Dimension of the level
     */
    public LevelEntities(int levelId, WorldEntity[][] raw, Dimension dimension) {
        this.levelId = levelId;
        this.difficulty = levelId;
        this.raw = raw;
        this.dimension = dimension;

        if (difficulty > 3)
            difficulty = 3;

        setMonsterSpeed();
    }

    /**
     * Add bomb instance in the level
     * @param game
     * @return True if the bomb could be created, false otherwise
     */
    public Boolean addBomb(Game game) {
        for(Bomb b : bombs) {
            if(b.getPosition().equals(game.getPlayer().getPosition())) return false; // Pas 2 bombes au même endroit
        }

        if(game.getPlayer().getBombs() == 0) return false; // Limiter le nombre de bombes
        Bomb b = new Bomb(game, game.getPlayer().getPosition());
        bombs.add(b);

        return true;
    }

    /**
     * Perform an update on every bombs
     * @return True if an update was performed, false otherwise
     */
    public boolean updateBombs() {
        boolean levelUpdated = false;
        Iterator<Bomb> it = bombs.iterator();

        while (it.hasNext()) {
            Bomb b = it.next();

            b.getTimer().decreaseTimer();
            if (b.getTimer().getSec() == 0) {
                b.setExplosionArea(levelId);
                levelUpdated = true;
            } else if (b.getTimer().getSec() <= -1) {
                levelUpdated = true;
                b.setBlowUp(false);
                it.remove();
            }

            if (b.getBlowUp()) {
                b.getExplosionArea().forEach(pos -> b.ExplodeAUX(pos, levelId));
            }
        }

        return levelUpdated;
    }

    /**
     * Perform an update on every monsters
     * @return True if an update was performed, false otherwise
     */
    public boolean updateMonsters(boolean randomMovement) {
        boolean levelUpdate = false;
        Iterator<Monster> it = monsters.iterator();

        while(it.hasNext()) {
            Monster m = it.next();
            if(! m.isAlive()) {
                it.remove();
                levelUpdate = true;
            }
            else{
                if(m.getTimerCD().getSec() == 0) {
                    if (randomMovement)
                        m.randomMoove();
                    else
                        m.bestMoove();
                    levelUpdate = true;
                }
                else m.getTimerCD().decreaseTimer();
            }
        }

        return levelUpdate;
    }

    /**
     * Load every monsters in the level, and store them into monsters
     * @param game
     * @throws PositionNotFoundException
     */
    public void loadMonsters(Game game) throws PositionNotFoundException  {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    Monster m = new Monster(game, new Position(x, y), levelId);
                    monsters.add(m);
                    m.setMooveCD(monsterSpeed);
                }
            }
        }

        if (monsters.size() == 0)
            throw new PositionNotFoundException("Monsters");
    }

    /**
     * Find the provided WorldEntity in the current level
     * @param entity The WorldEntity to search
     * @return entity Position
     * @throws PositionNotFoundException
     */
    public Position findRawEntity(WorldEntity entity) throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == entity) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException(entity.name());
    }

    /**
     * Check if there is a GameObject (bomb, monster) at the specified pos
     * @param pos Position where the check need to be done
     * @return True if there is an entity, false otherwise
     */
    public boolean isGameObject(Position pos) {
        for (Bomb bomb: bombs) {
            if (bomb.getPosition().equals(pos))
                return true;
        }

        for (Monster monster: monsters) {
            if (monster.getPosition().equals(pos))
                return true;
        }

        return false;
    }

    /**
     * Find the position of any NextDoor, closed or open
     * @return Position of the NextDoor
     * @throws PositionNotFoundException
     */
    public Position findNextDoor() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorNextOpened || raw[y][x] == WorldEntity.DoorNextClosed) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("DoorNext");
    }

    /**
     * Get a list of every monsters
     * @return
     */
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Get a list of every bombs
     * @return
     */
    public List<Bomb> getBombs() {
        return bombs;
    }

    /**
     * Get the last bomb created
     * @return
     */
    public Bomb getLastBomb() {
        return bombs.get(bombs.size()-1);
    }

    /**
     * Get id of the level
     * @return
     */
    public int getLevelId() {
        return levelId;
    }

    /**
     * Set the time between the movement of the monsters according to the difficulty
     */
    public void setMonsterSpeed() {
            monsterSpeed = 1 - (0.2 * difficulty);
    }
}
