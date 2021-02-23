/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.Properties;

import fr.ubx.poo.game.World.World;
import fr.ubx.poo.game.World.WorldFromFile;


import fr.ubx.poo.model.go.character.Player;

public class Game {
    private final World world;
    private final Player player;
    private final String worldPath;
    private String prefix;
    private int nbrLevel;
    public int initPlayerLives;

    public Game(String worldPath){
        this.worldPath = worldPath;
        loadConfig(worldPath);

        world = new WorldFromFile(nbrLevel, this.worldPath, prefix).loadAllLevels();
        Position positionPlayer = null;

        positionPlayer = world.findPlayer();
        player = new Player(this, positionPlayer);

        world.initializeLevels(this);
    }

    /**
     * Update the player position according to the current level
     */
    public void updatePlayerPosition() {
        Position positionPlayer = null;

        positionPlayer = world.findPlayer();
        player.setPosition(positionPlayer);
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));

            prefix = prop.getProperty("prefix");
            nbrLevel = Integer.parseInt(prop.getProperty("levels"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }
}
