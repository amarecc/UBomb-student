/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpritePlayer extends SpriteGameObject {

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        updateImage();
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;

        if (player.isInvicible())
            effect.setBrightness(0.8);
        else
            effect.setBrightness(0.0);
        setImage(ImageFactory.getInstance().getPlayer(player.getDirection()));
    }
}
