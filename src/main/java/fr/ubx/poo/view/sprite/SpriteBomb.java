/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.bombs.Bomb;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteBomb extends SpriteGameObject {


    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb b = (Bomb) go;
        int sec = b.getTimer().getSec();
        if(sec > 0) setImage(ImageFactory.getInstance().getBomb(sec));
    }
}
