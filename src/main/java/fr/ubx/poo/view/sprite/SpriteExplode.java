/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteExplode extends Sprite {
    private Position position;

    public SpriteExplode(Pane layer, Position position) {
        super(layer, null);
        this.position = position;
        updateImage();
    }

    @Override
    public void updateImage() {
        setImage(ImageFactory.getInstance().getExplode());
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
