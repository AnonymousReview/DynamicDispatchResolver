/*
 * (C) Copyright 2015-2017, by Joris Kinable and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.graph.specifics;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.alg.util.UnorderedPair;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.EdgeSetFactory;
import org.jgrapht.util.ArrayUnenforcedSet;

/**
 * Fast implementation of UndirectedSpecifics. This class uses additional data structures to improve
 * the performance of methods which depend on edge retrievals, e.g. getEdge(V u, V v),
 * containsEdge(V u, V v),addEdge(V u, V v). A disadvantage is an increase in memory consumption. If
 * memory utilization is an issue, use a {@link DirectedSpecifics} instead.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @author Joris Kinable
 */
public class FastLookupUndirectedSpecifics<V, E>
    extends UndirectedSpecifics<V, E>
{
    private static final long serialVersionUID = 225772727571597846L;

    /*
     * Maps a pair of vertices <u,v> to a set of edges {(u,v)}. In case of a multigraph, all edges
     * which touch both u,v are included in the set
     */
    protected Map<Pair<V, V>, ArrayUnenforcedSet<E>> touchingVerticesToEdgeMap;

    /**
     * Construct a new fast lookup undirected specifics.
     * 
     * @param abstractBaseGraph the graph for which these specifics are for
     */
    public FastLookupUndirectedSpecifics(AbstractBaseGraph<V, E> abstractBaseGraph)
    {
        this(abstractBaseGraph, new LinkedHashMap<>(), new ArrayUnenforcedSetEdgeSetFactory<>());
    }

    /**
     * Construct a new fast lookup undirected specifics.
     * 
     * @param abstractBaseGraph the graph for which these specifics are for
     * @param vertexMap map for the storage of vertex edge sets
     */
    public FastLookupUndirectedSpecifics(
        AbstractBaseGraph<V, E> abstractBaseGraph, Map<V, UndirectedEdgeContainer<V, E>> vertexMap)
    {
        this(abstractBaseGraph, vertexMap, new ArrayUnenforcedSetEdgeSetFactory<>());
    }

    /**
     * Construct a new fast lookup undirected specifics.
     * 
     * @param abstractBaseGraph the graph for which these specifics are for
     * @param vertexMap map for the storage of vertex edge sets
     * @param edgeSetFactory factory for the creation of vertex edge sets
     */
    public FastLookupUndirectedSpecifics(
        AbstractBaseGraph<V, E> abstractBaseGraph, Map<V, UndirectedEdgeContainer<V, E>> vertexMap,
        EdgeSetFactory<V, E> edgeSetFactory)
    {
        super(abstractBaseGraph, vertexMap, edgeSetFactory);
        this.touchingVerticesToEdgeMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<E> getAllEdges(V sourceVertex, V targetVertex)
    {
        if (abstractBaseGraph.containsVertex(sourceVertex)
            && abstractBaseGraph.containsVertex(targetVertex))
        {
            Set<E> edges =
                touchingVerticesToEdgeMap.get(new UnorderedPair<>(sourceVertex, targetVertex));
            return edges == null ? Collections.emptySet() : new ArrayUnenforcedSet<>(edges);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getEdge(V sourceVertex, V targetVertex)
    {
        List<E> edges =
            touchingVerticesToEdgeMap.get(new UnorderedPair<>(sourceVertex, targetVertex));
        if (edges == null || edges.isEmpty())
            return null;
        else
            return edges.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEdgeToTouchingVertices(E e)
    {
        V source = abstractBaseGraph.getEdgeSource(e);
        V target = abstractBaseGraph.getEdgeTarget(e);

        getEdgeContainer(source).addEdge(e);

        // Add edge to touchingVerticesToEdgeMap for the UnorderedPair {u,v}
        Pair<V, V> vertexPair = new UnorderedPair<>(source, target);
        if (!touchingVerticesToEdgeMap.containsKey(vertexPair)) {
            ArrayUnenforcedSet<E> edgeSet = new ArrayUnenforcedSet<>();
            edgeSet.add(e);
            touchingVerticesToEdgeMap.put(vertexPair, edgeSet);
        } else
            touchingVerticesToEdgeMap.get(vertexPair).add(e);

        if (!source.equals(target)) { // If not a self loop
            getEdgeContainer(target).addEdge(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEdgeFromTouchingVertices(E e)
    {
        V source = abstractBaseGraph.getEdgeSource(e);
        V target = abstractBaseGraph.getEdgeTarget(e);

        getEdgeContainer(source).removeEdge(e);

        if (!source.equals(target))
            getEdgeContainer(target).removeEdge(e);

        // Remove the edge from the touchingVerticesToEdgeMap. If there are no more remaining edges
        // for a pair
        // of touching vertices, remove the pair from the map.
        Pair<V, V> vertexPair = new UnorderedPair<>(source, target);
        if (touchingVerticesToEdgeMap.containsKey(vertexPair)) {
            ArrayUnenforcedSet<E> edgeSet = touchingVerticesToEdgeMap.get(vertexPair);
            edgeSet.remove(e);
            if (edgeSet.isEmpty())
                touchingVerticesToEdgeMap.remove(vertexPair);
        }
    }

}
