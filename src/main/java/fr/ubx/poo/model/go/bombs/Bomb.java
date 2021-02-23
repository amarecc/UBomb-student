package fr.ubx.poo.model.go.bombs;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.monster.Monster;
import fr.ubx.poo.utils.Timer;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends GameObject {
	private Timer timer;
	private boolean blowUp = false;
	private List<Position> explosionArea = new ArrayList<>();

	public Bomb(Game game, Position position) {
		super(game, position);
		this.timer = new Timer(4);
		game.getPlayer().decreaseBombs();
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	/**
	 * Apply the effect of the explosion.
	 * @param pos where the effect is applied.
	 * @return 0 if the explosion can't occure.
	 * 		   1 if the explosion is stopped after.
	 * 		   2 if the explosion can continue.
	 */
	public int ExplodeAUX(Position pos, int levelId){ // 0 = explose pas / 1 = explose mais continue pas / 2 = explose et continue
		if (this.game.getWorld().isInside(pos, levelId)) {
			if (this.game.getPlayer().getPosition().equals(pos) && levelId == this.game.getWorld().currentLevel) {
				this.game.getPlayer().decreaseLives();
				return 2;
			}
			for (Monster m : this.game.getWorld().getMonsters(levelId)) {
				if (m.getPosition().equals(pos)) {
					m.kill();
					return 2;
				}
			}

			if (this.game.getWorld().isEmpty(pos, levelId)) {
				return 2;
			}

			Decor d = this.game.getWorld().get(pos, levelId);
			if (d.canExplode()) {
				this.game.getWorld().clear(pos, levelId);
				if(d.isABonus()) return 2;
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Change the variable "explosionArea" with a new list of positions
	 * corresponding at the positions where the explosion occure.
	 * And call "ExplodeAUX" on all positions of "explosionArea".
	 */
	public void setExplosionArea(int levelId) {
		if (!this.blowUp) {
			this.blowUp = true;
			game.getPlayer().increaseBombs();
			int range = game.getPlayer().getRangeBomb();
			List<Position> listpos = new ArrayList<>();
			Position center = getPosition();

			listpos.add(center);
			ExplodeAUX(center, levelId);

			for (int i = 1; i <= range; i++) { // up
				Position pos = new Position(center.x, center.y - i);
				int e = ExplodeAUX(pos, levelId);
				if (e == 0) break;
				listpos.add(pos);
				if (e == 1) break;
			}

			for (int i = 1; i <= range; i++) { // right
				Position pos = new Position(center.x + i, center.y);
				int e = ExplodeAUX(pos, levelId);
				if (e == 0) break;
				listpos.add(pos);
				if (e == 1) break;
			}

			for (int i = 1; i <= range; i++) { // down
				Position pos = new Position(center.x, center.y + i);
				int e = ExplodeAUX(pos, levelId);
				if (e == 0) break;
				listpos.add(pos);
				if (e == 1) break;
			}

			for (int i = 1; i <= range; i++) { // left
				Position pos = new Position(center.x - i, center.y);
				int e = ExplodeAUX(pos, levelId);
				if (e == 0) break;
				listpos.add(pos);
				if (e == 1) break;
			}
			this.explosionArea = listpos;
		}
	}

	public List<Position> getExplosionArea() {
		return explosionArea;
	}

	public boolean getBlowUp() {
		return blowUp;
	}

	public void setBlowUp(boolean blowUp) {
		this.blowUp = blowUp;
	}
}
