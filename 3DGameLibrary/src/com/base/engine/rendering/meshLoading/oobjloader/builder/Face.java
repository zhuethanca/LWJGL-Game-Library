package com.base.engine.rendering.meshLoading.oobjloader.builder;

import java.util.ArrayList;

import com.base.engine.core.Vector3f;

public class Face {

    public ArrayList<FaceVertex> vertices = new ArrayList<FaceVertex>();
    public Material material = null;
    public Material map = null;

    public Face() {
    }

    public void add(FaceVertex vertex) {
        vertices.add(vertex);
    }
    public Vector3f faceNormal = new Vector3f(0, 0, 0);

    // @TODO: This code assumes the face is a triangle.  
    public void calculateTriangleNormal() {
        float[] edge1 = new float[3];
        float[] edge2 = new float[3];
        float[] normal = new float[3];
        Vector3f v1 = vertices.get(0).pos;
        Vector3f v2 = vertices.get(1).pos;
        Vector3f v3 = vertices.get(2).pos;
        float[] p1 = {v1.getX(), v1.getY(), v1.getZ()};
        float[] p2 = {v2.getX(), v2.getY(), v2.getZ()};
        float[] p3 = {v3.getX(), v3.getY(), v3.getZ()};

        edge1[0] = p2[0] - p1[0];
        edge1[1] = p2[1] - p1[1];
        edge1[2] = p2[2] - p1[2];

        edge2[0] = p3[0] - p2[0];
        edge2[1] = p3[1] - p2[1];
        edge2[2] = p3[2] - p2[2];

        normal[0] = edge1[1] * edge2[2] - edge1[2] * edge2[1];
        normal[1] = edge1[2] * edge2[0] - edge1[0] * edge2[2];
        normal[2] = edge1[0] * edge2[1] - edge1[1] * edge2[0];

        faceNormal.setX(normal[0]);
        faceNormal.setY(normal[1]);
        faceNormal.setZ(normal[2]);
    }
    
    public String toString() { 
        String result = "\tvertices: "+vertices.size()+" :\n";
        for(FaceVertex f : vertices) {
            result += " \t\t( "+f.toString()+" )\n";
        }
        return result;
    }
}   