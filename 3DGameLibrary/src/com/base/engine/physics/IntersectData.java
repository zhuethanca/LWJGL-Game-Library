package com.base.engine.physics;

public class IntersectData {
	private final boolean doesIntersect;
	private final float distance;

	public IntersectData(boolean doesIntersect, float distance) {
		this.doesIntersect = doesIntersect;
		this.distance = distance;
	}

	public boolean isDoesIntersect() {
		return doesIntersect;
	}

	public float getDistance() {
		return distance;
	}
}
