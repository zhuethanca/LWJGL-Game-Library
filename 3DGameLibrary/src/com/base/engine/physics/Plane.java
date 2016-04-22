package com.base.engine.physics;

import com.base.engine.core.Vector3f;

public class Plane {
	private final Vector3f normal;
	private final float distance;

	public Plane(Vector3f normal, float distance) {
		this.normal = normal;
		this.distance = distance;
	}
	
	public IntersectData intersectBoundingSphere(BoundingSphere other){
		float distanceFromSphereCenter = Math.abs(normal.dot(other.getCenter()) + distance);
		float distanceFromSphere = distanceFromSphereCenter - other.getRadius();
		
		return new IntersectData(distanceFromSphere < 0, distanceFromSphere);
	}
	
	public Plane normalized(){
		float magnitude = normal.length();
		return new Plane(normal.div(magnitude), distance/magnitude);
	}

	public Vector3f getNormal() {
		return normal;
	}

	public float getDistance() {
		return distance;
	}
}
