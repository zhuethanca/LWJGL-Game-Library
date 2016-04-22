package com.base.engine.rendering.meshLoading.oobjloader.builder;

import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class FaceVertex {

    int index = -1;
    public Vector3f pos = null;
    public Vector2f tex = null;
    public Vector3f normal = null;

    public String toString() {
        return pos + "|" + normal + "|" + tex;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((normal == null) ? 0 : normal.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((tex == null) ? 0 : tex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FaceVertex other = (FaceVertex) obj;
		if (normal == null) {
			if (other.normal != null)
				return false;
		} else if (!normal.equals(other.normal))
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (tex == null) {
			if (other.tex != null)
				return false;
		} else if (!tex.equals(other.tex))
			return false;
		return true;
	}
}