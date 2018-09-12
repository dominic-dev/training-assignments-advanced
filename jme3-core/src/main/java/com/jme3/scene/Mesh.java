package com.jme3.scene;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.mesh.Mode;
import com.jme3.util.IntMap;
import com.jme3.util.SafeArrayList;
import com.jme3.util.clone.Cloner;

import java.nio.*;

public interface Mesh {
    /**
     * Create a shallow clone of this Mesh. The {@link VertexBuffer vertex
     * buffers} are shared between this and the clone mesh, the rest
     * of the data is cloned.
     *
     * @return A shallow clone of the mesh
     */
    Mesh clone();

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    void cloneFields(Cloner cloner, Object original);

    /**
     * @return The number of LOD levels set on this mesh, including the main
     * index buffer, returns zero if there are no lod levels.
     */
    int getNumLodLevels();

    /**
     * Returns the lod level at the given index.
     *
     * the main index buffer.
     * @return The LOD index buffer at the index
     *
     * @throws IndexOutOfBoundsException If the index is outside of the
     * range [0, {@link #getNumLodLevels()}].
     *
    VertexBuffer getLodLevel(int lod);

    /**
     * Returns the mesh mode
     *
     * @return the mesh mode
     *
     * @see #setMode(Mode)
     */
    Mode getMode();

    /**
     * Change the Mesh's mode. By default the mode is {@link Mode#Triangles}.
     *
     * @param mode The new mode to set
     *
     * @see Mode
     */
    void setMode(Mode mode);

    /**
     * Returns the maximum number of weights per vertex on this mesh.
     *
     * @return maximum number of weights per vertex
     *
     * @see #setMaxNumWeights(int)
     */
    int getMaxNumWeights();

    /**
     * Set the maximum number of weights per vertex on this mesh.
     * Only relevant if this mesh has bone index/weight buffers.
     * This value should be between 0 and 4.
     *
     * @param maxNumWeights
     */
    void setMaxNumWeights(int maxNumWeights);

    /**
     * Returns the line width for line meshes.
     *
     * @return the line width
     * @deprecated use {@link Material#getAdditionalRenderState()} and {@link RenderState#getLineWidth()}
     */
    @Deprecated
    float getLineWidth();

    /**
     * Specify the line width for meshes of the line modes, such
     * as {@link Mode#Lines}. The line width is specified as on-screen pixels,
     * the default value is 1.0.
     *
     * @param lineWidth The line width
     * @deprecated use {@link Material#getAdditionalRenderState()} and {@link RenderState#setLineWidth(float)}
     */
    @Deprecated
    void setLineWidth(float lineWidth);

    /**
     * Indicates to the GPU that this mesh will not be modified (a hint).
     * Sets the usage mode to {@link VertexBuffer.Usage#Static}
     * for all {@link VertexBuffer vertex buffers} on this Mesh.
     */
    void setStatic();

    /**
     * Update the {@link #getVertexCount() vertex} and
     * {@link #getTriangleCount() triangle} counts for this mesh
     * based on the current data. This method should be called
     * after the {@link Buffer#capacity() capacities} of the mesh's
     * {@link VertexBuffer vertex buffers} has been altered.
     *
     * @throws IllegalStateException If this mesh is in
     */
    void updateCounts();

    /**
     * Returns the triangle count for the given LOD level.
     *
     * @param lod The lod level to look up
     * @return The triangle count for that LOD level
     */
    int getTriangleCount(int lod);

    /**
     * Returns how many triangles or elements are on this Mesh.
     * This value is only updated when {@link #updateCounts() } is called.
     * If the mesh mode is not a triangle mode, then this returns the
     * number of elements/primitives, e.g. how many lines or how many points,
     * instead of how many triangles.
     *
     * @return how many triangles/elements are on this Mesh.
     */
    int getTriangleCount();

    /**
     * Returns the number of vertices on this mesh.
     * The value is computed based on the position buffer, which
     * must be set on all meshes.
     *
     * @return Number of vertices on the mesh
     */
    int getVertexCount();

    /**
     * Gets the triangle vertex positions at the given triangle index
     * and stores them into the v1, v2, v3 arguments.
     *
     * @param index The index of the triangle.
     * Should be between 0 and {@link #getTriangleCount()}.
     *
     * @param v1 Vector to contain first vertex position
     * @param v2 Vector to contain second vertex position
     * @param v3 Vector to contain third vertex position
     */
    void getTriangle(int index, Vector3f v1, Vector3f v2, Vector3f v3);

    /**
     * Gets the triangle vertex positions at the given triangle index
     * and stores them into the {@link Triangle} argument.
     * Also sets the triangle index to the <code>index</code> argument.
     *
     * @param index The index of the triangle.
     * Should be between 0 and {@link #getTriangleCount()}.
     *
     * @param tri The triangle to store the positions in
     */
    void getTriangle(int index, Triangle tri);

    /**
     * Gets the triangle vertex indices at the given triangle index
     * and stores them into the given int array.
     *
     * @param index The index of the triangle.
     * Should be between 0 and {@link #getTriangleCount()}.
     *
     * @param indices Indices of the triangle's vertices
     */
    void getTriangle(int index, int[] indices);

    /**
     * Returns the mesh's VAO ID. Internal use only.
     */
    int getId();

    /**
     * Sets the mesh's VAO ID. Internal use only.
     */
    void setId(int id);

    /**
     * Sets the {@link VertexBuffer} on the mesh.
     * This will update the vertex/triangle counts if needed.
     *
     * @param vb The buffer to set
     * @throws IllegalArgumentException If the buffer type is already set
     */
    void setBuffer(VertexBuffer vb);

    /**
     * Unsets the {@link VertexBuffer} set on this mesh
     * with the given type. Does nothing if the vertex buffer type is not set
     * initially.
     *
     * @param type The buffer type to remove
     */
    void clearBuffer(VertexBuffer.Type type);

    /**
     * Creates a {@link VertexBuffer} for the mesh or modifies
     * the existing one per the parameters given.
     *
     * @param type The type of the buffer
     * @param components Number of components
     * @param format Data format
     * @param buf The buffer data
     *
     * @throws UnsupportedOperationException If the buffer already set is
     * incompatible with the parameters given.
     */
    void setBuffer(VertexBuffer.Type type, int components, VertexBuffer.Format format, Buffer buf);

    /**
     * Set a floating point {@link VertexBuffer} on the mesh.
     *
     * @param type The type of {@link VertexBuffer},
     * e.g. {@link VertexBuffer.Type#Position}, {@link VertexBuffer.Type#Normal}, etc.
     *
     * @param components Number of components on the vertex buffer, should
     * be between 1 and 4.
     *
     * @param buf The floating point data to contain
     */
    void setBuffer(VertexBuffer.Type type, int components, FloatBuffer buf);

    void setBuffer(VertexBuffer.Type type, int components, float[] buf);

    void setBuffer(VertexBuffer.Type type, int components, IntBuffer buf);

    void setBuffer(VertexBuffer.Type type, int components, int[] buf);

    void setBuffer(VertexBuffer.Type type, int components, ShortBuffer buf);

    void setBuffer(VertexBuffer.Type type, int components, byte[] buf);

    void setBuffer(VertexBuffer.Type type, int components, ByteBuffer buf);

    void setBuffer(VertexBuffer.Type type, int components, short[] buf);

    /**
     * Get the {@link VertexBuffer} stored on this mesh with the given
     * type.
     *
     * @param type The type of VertexBuffer
     * @return the VertexBuffer data, or null if not set
     */
    VertexBuffer getBuffer(VertexBuffer.Type type);

    /**
     * Get the {@link VertexBuffer} data stored on this mesh in float
     * format.
     *
     * @param type The type of VertexBuffer
     * @return the VertexBuffer data, or null if not set
     */
    FloatBuffer getFloatBuffer(VertexBuffer.Type type);

    /**
     * Updates the bounding volume of this mesh.
     * The method does nothing if the mesh has no {@link VertexBuffer.Type#Position} buffer.
     * It is expected that the position buffer is a float buffer with 3 components.
     */
    void updateBound();

    /**
     * Returns the {@link BoundingVolume} of this Mesh.
     * By default the bounding volume is a {@link BoundingBox}.
     *
     * @return the bounding volume of this mesh
     */
    BoundingVolume getBound();

    /**
     * Sets the {@link BoundingVolume} for this Mesh.
     * The bounding volume is recomputed by calling {@link #updateBound() }.
     *
     * @param modelBound The model bound to set
     */
    void setBound(BoundingVolume modelBound);

    /**
     * Returns a map of all {@link VertexBuffer vertex buffers} on this Mesh.
     * The integer key for the map is the {@link Enum#ordinal() ordinal}
     * of the vertex buffer's {@link VertexBuffer.Type}.
     * Note that the returned map is a reference to the map used internally,
     * modifying it will cause undefined results.
     *
     * @return map of vertex buffers on this mesh.
     */
    IntMap<VertexBuffer> getBuffers();

    /**
     * Returns a list of all {@link VertexBuffer vertex buffers} on this Mesh.
     * Using a list instead an IntMap via the {@link #getBuffers() } method is
     * better for iteration as there's no need to create an iterator instance.
     * Note that the returned list is a reference to the list used internally,
     * modifying it will cause undefined results.
     *
     * @return list of vertex buffers on this mesh.
     */
    SafeArrayList<VertexBuffer> getBufferList();
}
