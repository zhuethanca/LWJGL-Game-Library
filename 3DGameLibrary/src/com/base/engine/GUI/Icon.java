//package com.base.engine.GUI;
//
//import com.base.engine.core.Vector2f;
//import com.base.engine.core.Vector3f;
//import com.base.engine.rendering.Texture;
//
//public class Icon implements Comparable<Icon>{
//	private Vector3f pos;
//	private Vector2f size;
//	private Texture texture;
//	public Icon(Vector3f pos, Vector2f size, Texture texture) {
//		this.pos = pos;
//		this.size = size;
//		this.texture = texture;
//	}
//	public Vector3f getPos() {
//		return pos;
//	}
//	public Vector2f getSize() {
//		return size;
//	}
//	public Texture getTexture() {
//		return texture;
//	}
//	@Override
//	public int compareTo(Icon o) {
//		return (int) (o.pos.getZ() - pos.getZ());
//	}
//}
