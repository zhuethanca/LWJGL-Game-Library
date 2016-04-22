package com.base.game;

import com.base.engine.core.CoreEngine;

public class Main {
	public static void main(String[] args){
		TestGame game;
		CoreEngine engine = new CoreEngine(game = new TestGame());
		game.setMain(engine);
		engine.start();
	}
}
