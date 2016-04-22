package com.base.engine.physics;

import com.base.engine.core.Vector3f;

public class BoundingSphere {
	private final Vector3f center;
	private final float radius;

	public BoundingSphere(Vector3f center, float radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public IntersectData intersectBoundingSphere(BoundingSphere other){
		float radiusDistance = this.radius + other.radius;
		float centerDistance = other.center.sub(this.center).length();
		float distance = centerDistance - radiusDistance;
	
		return new IntersectData(centerDistance < radiusDistance, distance);
	}

	public Vector3f getCenter() {
		return center;
	}

	public float getRadius() {
		return radius;
	}
}
