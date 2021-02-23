package fr.ubx.poo.game.World;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WorldFromFile {
    private WorldEntity[][] mapEntities;
    private int nbrLevel;
    private String path;

    public WorldFromFile(int nbrLevel, String path, String prefix) {
        this.nbrLevel = nbrLevel;
        this.path = path + "/" + prefix;
    }

    /**
     * Load a level in a 2d Array of WorldEntity from a file
     * @param level Level to be load
     * @return The 2d Array of WorldEntity of the specified level
     */
    WorldEntity[][] loadLevel(int level){
        ArrayList<List<WorldEntity>> listEntities= new ArrayList<>();
        listEntities.add(new ArrayList<>());

        if (level > this.nbrLevel)
            return null;

        String pathLevel = path + level + ".txt";

        try {
            BufferedReader file = new BufferedReader(new FileReader(pathLevel));
            int r;
            int i = 0;
            ArrayList<WorldEntity> lineEntities = new ArrayList<>();

            while ((r = file.read()) != -1){
                char currentChar = (char) r;

                if (WorldEntity.fromCode(currentChar).isPresent()) {
                    WorldEntity code = WorldEntity.fromCode(currentChar).get();
                    lineEntities.add(code);

                    listEntities.get(i).add(code);
                }

                if (currentChar == '\n') {
                    i++;
                    listEntities.add(new ArrayList<>());
                }

            }

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapEntities = new WorldEntity[listEntities.size()][listEntities.get(0).size()];

        for (int i = 0; i < mapEntities.length; i++){
            mapEntities[i] = listEntities.get(i).toArray(new WorldEntity[listEntities.get(i).size()]);
        }

        return mapEntities;
    }

    /**
     * Load every levels
     * @return World instance containing every levels
     */
    public World loadAllLevels(){
        List<WorldEntity[][]> allLevels = new ArrayList<>();
        for (int i = 1; i <= nbrLevel; i++)
            allLevels.add(loadLevel(i));

        return new World(allLevels);
    }


}
