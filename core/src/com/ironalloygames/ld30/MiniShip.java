package com.ironalloygames.ld30;

public class MiniShip extends Actor {

	@Override
	public void render() {
		super.render();

		LD30.batch.draw(LD30.a.getSprite("mini_ship"), body.getPosition().x,
				body.getPosition().y, .5f, .5f, 1, 1, 37f / LD30.METER_SCALE,
				37f / LD30.METER_SCALE, body.getAngle());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}

}
