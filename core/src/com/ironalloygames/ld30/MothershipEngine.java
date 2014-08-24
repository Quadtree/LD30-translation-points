package com.ironalloygames.ld30;

public class MothershipEngine extends Actor {

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public void render() {
		super.render();

		this.drawDefault("mothership2");
	}

}
