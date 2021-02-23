/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game.World;

import fr.ubx.poo.game.Dimension;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.PositionNotFoundException;
import fr.ubx.poo.game.World.Level.LevelEntities;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.model.go.monster.Monster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class World {
    private final List<Map<Position, Decor>> grid = new ArrayList<>();
    public final List<Dimension> dimension = new ArrayList<>();
    public boolean hasChanged = false;
    public int currentLevel = 0;
    public boolean levelChanged = false;
    private int precedentLevel = -1;

    public List<LevelEntities> levelEntities = new ArrayList<>();

    /**
     * Create a new instance of World with one level
     * @param raw A 2d Array of WorldEntity for the only level
     */
    public World(WorldEntity[][] raw) {
        dimension.add(new Dimension(raw.length-1, raw[0].length-1));
        levelEntities.add(new LevelEntities(0, raw, dimension.get(0)));
        grid.add(WorldBuilder.build(raw, dimension.get(0)));
    }

    /**
     * Create a new instance of World with multiple levels
     * @param raw A list of 2d Array of WorldEntity for every levels
     */
    public World(List<WorldEntity[][]> raw) {
        for (WorldEntity[][] rawLevel : raw) {
            dimension.add(new Dimension(rawLevel.length-1, rawLevel[0].length-1));
            grid.add(WorldBuilder.build(rawLevel, dimension.get(dimension.size()-1)));
            levelEntities.add(new LevelEntities(raw.indexOf(rawLevel), rawLevel, dimension.get(dimension.size()-1)));
        }
    }

    /**
     * Find player position in the current level
     * @return Player position
     */
    public Position findPlayer() {
        Position playerPosition = null;
        try {
            //First Load
            if (currentLevel == 0 && precedentLevel == -1)
                playerPosition = levelEntities.get(currentLevel).findRawEntity(WorldEntity.Player);

                //We came from a upper level
            else if (precedentLevel > currentLevel)
                playerPosition = levelEntities.get(currentLevel).findNextDoor();

                //We came from a lower level
            else
                playerPosition = levelEntities.get(currentLevel).findRawEntity(WorldEntity.DoorPrevOpened);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return playerPosition;
    }

    /**
     * Initialize entities in every levels
     * @param game
     */
    public void initializeLevels(Game game) {
        levelEntities.forEach((level -> {
            try {
                level.loadMonsters(game);
            } catch (PositionNotFoundException e) {
                System.err.println("Position not found : " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }));
    }

    /**
     * Add a bomb to the current level
     * @param game
     * @return True if the bomb could be create, false otherwise
     */
    public Boolean addBomb(Game game) {
        return levelEntities.get(currentLevel).addBomb(game);
    }

    /**
     * Get a list of every monsters in the current level
     * @return
     */
    public List<Monster> getMonsters() {
        return levelEntities.get(currentLevel).getMonsters();
    }

    /**
     * Get a list of every monsters in the specified level
     * @param levelId
     * @return
     */
    public List<Monster> getMonsters(int levelId) {
        return levelEntities.get(levelId).getMonsters();
    }

    /**
     * Get a list of every bombs in the current level
     * @return
     */
    public List<Bomb> getBombs() {
        return levelEntities.get(currentLevel).getBombs();
    }

    /**
     * Get a list of every bombs in the specified level
     * @param levelId Id of the level
     * @return
     */
    public List<Bomb> getBombs(int levelId) {
        return levelEntities.get(levelId).getBombs();
    }

    /**
     * Get the last bomb created in the current level
     * @return Bomb reference
     */
    public Bomb getLastBomb() {
        return levelEntities.get(currentLevel).getLastBomb();
    }

    /**
     * Check if there is a GameObject (bomb, monster) at the specified pos in the current level
     * @param pos Position where the check need to be done
     * @return True if there is an entity, false otherwise
     */
    public boolean isGameObject(Position pos) {
        return levelEntities.get(currentLevel).isGameObject(pos);
    }

    /**
     * Update all entities in every levels, (except monsters)
     */
    public void updateEntities() {
        for (LevelEntities level: levelEntities) {
            if (level.updateBombs() && currentLevel == level.getLevelId())
                hasChanged = true;
            if (currentLevel == level.getLevelId() && level.updateMonsters(false))
                hasChanged = true;
            else if (currentLevel != level.getLevelId())
                level.updateMonsters(true);
        }
    }

    public Decor get(Position position) {
        return grid.get(currentLevel).get(position);
    }

    public Decor get(Position position, int levelId) {
        return grid.get(levelId).get(position);
    }

    public void set(Position position, Decor decor) {
        hasChanged = true;
        grid.get(currentLevel).put(position, decor);
    }

    public void clear(Position position) {
        hasChanged = true;
        grid.get(currentLevel).remove(position);
    }

    public void clear(Position position, int levelId) {
        if (currentLevel == levelId)
            hasChanged = true;
        grid.get(levelId).remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.get(currentLevel).forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.get(currentLevel).values();
    }

    public boolean isInside(Position position) {
        return position.inside(this.dimension.get(currentLevel));
    }

    public boolean isInside(Position position, int levelId) {
        return position.inside(this.dimension.get(levelId));
    }

    public boolean isEmpty(Position position) {
        return grid.get(currentLevel).get(position) == null;
    }

    public boolean isEmpty(Position position, int levelId) {
        return grid.get(levelId).get(position) == null;
    }

    /**
     * Request to go to the next level
     */
    public void nextLevel(){
        precedentLevel = currentLevel;
        currentLevel++;
        levelChanged = true;
    }

    /**
     * Request to go to the previous level
     */
    public void prevLevel(){
        precedentLevel = currentLevel;
        currentLevel--;
        levelChanged = true;
    }

    public Dimension getDimension(){
        return dimension.get(currentLevel);
    }
}
