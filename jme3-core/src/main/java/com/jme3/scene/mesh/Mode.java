package com.jme3.scene.mesh;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.ConcreteMesh;

/**
 * The mode of the Mesh specifies both the type of primitive represented
 * by the mesh and how the data should be interpreted.
 */
public enum Mode {
    /**
     * A primitive is a single point in space. The size of the points
     * can be specified with {@link ConcreteMesh#setPointSize(float) }.
     */
    Points(true),

    /**
     * A primitive is a line segment. Every two vertices specify
     * a single line. {@link Material#getAdditionalRenderState()} and {@link RenderState#setLineWidth(float)} can be used
     * to set the width of the lines.
     */
    Lines(true),

    /**
     * A primitive is a line segment. The first two vertices specify
     * a single line, while subsequent vertices are combined with the
     * previous vertex to make a line. {@link Material#getAdditionalRenderState()} and {@link RenderState#setLineWidth(float)} can
     * be used to set the width of the lines.
     */
    LineStrip(false),

    /**
     * Identical to {@link #LineStrip} except that at the end
     * the last vertex is connected with the first to form a line.
     * {@link Material#getAdditionalRenderState()} and {@link RenderState#setLineWidth(float)} can be used
     * to set the width of the lines.
     */
    LineLoop(false),

    /**
     * A primitive is a triangle. Each 3 vertices specify a single
     * triangle.
     */
    Triangles(true),

    /**
     * Similar to {@link #Triangles}, the first 3 vertices
     * specify a triangle, while subsequent vertices are combined with
     * the previous two to form a triangle.
     */
    TriangleStrip(false),

    /**
     * Similar to {@link #Triangles}, the first 3 vertices
     * specify a triangle, each 2 subsequent vertices are combined
     * with the very first vertex to make a triangle.
     */
    TriangleFan(false),

    /**
     * A combination of various triangle modes. It is best to avoid
     * using this mode as it may not be supported by all renderers.
     * The {@link ConcreteMesh#setModeStart(int[]) mode start points} and
     * {@link ConcreteMesh#setElementLengths(int[]) element lengths} must
     * be specified for this mode.
     */
    Hybrid(false),
    /**
     * Used for Tesselation only. Requires to set the number of vertices
     * for each patch (default is 3 for triangle tesselation)
     */
    Patch(true);
    private boolean listMode = false;

    private Mode(boolean listMode){
        this.listMode = listMode;
    }

    /**
     * Returns true if the specified mode is a list mode (meaning
     * ,it specifies the indices as a linear list and not some special
     * format).
     * Will return true for the types {@link #Points}, {@link #Lines} and
     * {@link #Triangles}.
     *
     * @return true if the mode is a list type mode
     */
    public boolean isListMode(){
        return listMode;
    }
}
