/*
 * (C) Copyright 2007-2017, by France Telecom and Contributors.
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
package org.jgrapht.alg;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

/**
 * The algorithm determines the k shortest simple paths in increasing order of weight. Weights can
 * be negative (but no negative cycle is allowed), and paths can be constrained by a maximum number
 * of edges. Multigraphs are allowed.
 *
 * <p>
 * The algorithm is a variant of the Bellman-Ford algorithm but instead of only storing the best
 * path it stores the "k" best paths at each pass, yielding a complexity of O(k*n*(m^2)) where m is
 * the number of edges and n is the number of vertices.
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 *
 * @author Guillaume Boulmier
 * @since July 5, 2007
 * @deprecated Use {@link org.jgrapht.alg.shortestpath.KShortestPaths} instead.
 */
@Deprecated
public class KShortestPaths<V, E>
{
    /**
     * Graph on which shortest paths are searched.
     */
    private Graph<V, E> graph;

    private int nMaxHops;

    private int nPaths;

    private V startVertex;

    private PathValidator<V, E> pathValidator;

    /**
     * Creates an object to compute ranking shortest paths between the start vertex and others
     * vertices.
     *
     * @param graph graph on which shortest paths are searched.
     * @param startVertex start vertex of the calculated paths.
     * @param k number of paths to be computed.
     */
    public KShortestPaths(Graph<V, E> graph, V startVertex, int k)
    {
        this(graph, startVertex, k, null);
    }

    /**
     * Creates an object to compute ranking shortest paths between the start vertex and others
     * vertices. Providing non-null path validator may be used to accept/deny paths according to
     * some external logic. These validations will be used in addition to the basic path validations
     * - that the path is from start to target with no loops.
     *
     * @param graph graph on which shortest paths are searched.
     * @param startVertex start vertex of the calculated paths.
     * @param k number of paths to be computed.
     * @param pathValidator the path validator to use
     */
    public KShortestPaths(
        Graph<V, E> graph, V startVertex, int k, PathValidator<V, E> pathValidator)
    {
        this(graph, startVertex, k, graph.vertexSet().size() - 1, pathValidator);
    }

    /**
     * Creates an object to calculate ranking shortest paths between the start vertex and others
     * vertices.
     *
     * @param graph graph on which shortest paths are searched.
     * @param startVertex start vertex of the calculated paths.
     * @param nPaths number of ranking paths between the start vertex and an end vertex.
     * @param nMaxHops maximum number of edges of the calculated paths.
     *
     * @throws NullPointerException if the specified graph or startVertex is <code>null</code>.
     * @throws IllegalArgumentException if nPaths is negative or 0.
     * @throws IllegalArgumentException if nMaxHops is negative or 0.
     */
    public KShortestPaths(Graph<V, E> graph, V startVertex, int nPaths, int nMaxHops)
    {
        this(graph, startVertex, nPaths, nMaxHops, null);
    }

    /**
     * Creates an object to calculate ranking shortest paths between the start vertex and others
     * vertices. Providing non-null path validator may be used to accept/deny paths according to
     * some external logic. These validations will be used in addition to the basic path validations
     * - that the path is from start to target with no loops.
     *
     * @param graph graph on which shortest paths are searched.
     * @param startVertex start vertex of the calculated paths.
     * @param nPaths number of ranking paths between the start vertex and an end vertex.
     * @param nMaxHops maximum number of edges of the calculated paths.
     * @param pathValidator the path validator to use
     *
     * @throws NullPointerException if the specified graph or startVertex is <code>null</code>.
     * @throws IllegalArgumentException if nPaths is negative or 0.
     * @throws IllegalArgumentException if nMaxHops is negative or 0.
     */
    public KShortestPaths(
        Graph<V, E> graph, V startVertex, int nPaths, int nMaxHops,
        PathValidator<V, E> pathValidator)
    {
        assertKShortestPathsFinder(graph, startVertex, nPaths, nMaxHops);

        this.graph = graph;
        this.startVertex = startVertex;
        this.nPaths = nPaths;
        this.nMaxHops = nMaxHops;
        this.pathValidator = pathValidator;
    }

    /**
     * Returns the k shortest simple paths in increasing order of weight.
     *
     * @param endVertex target vertex of the calculated paths.
     *
     * @return list of paths, or <code>null</code> if no path exists between the start vertex and
     *         the end vertex.
     */
    public List<GraphPath<V, E>> getPaths(V endVertex)
    {
        assertGetPaths(endVertex);

        KShortestPathsIterator<V, E> iter = new KShortestPathsIterator<>(
            this.graph, this.startVertex, endVertex, this.nPaths, this.pathValidator);

        // at the i-th pass the shortest paths with less (or equal) than i edges
        // are calculated.
        for (int passNumber = 1; (passNumber <= this.nMaxHops) && iter.hasNext(); passNumber++) {
            iter.next();
        }

        List<RankingPathElement<V, E>> list = iter.getPathElements(endVertex);

        if (list == null) {
            return null;
        }

        List<GraphPath<V, E>> pathList = new ArrayList<>();

        for (RankingPathElement<V, E> element : list) {
            pathList.add(new PathWrapper(element));
        }

        return pathList;
    }

    private void assertGetPaths(V endVertex)
    {
        if (endVertex == null) {
            throw new NullPointerException("endVertex is null");
        }
        if (endVertex.equals(this.startVertex)) {
            throw new IllegalArgumentException("The end vertex is the same as the start vertex!");
        }
        if (!this.graph.vertexSet().contains(endVertex)) {
            throw new IllegalArgumentException("Graph must contain the end vertex!");
        }
    }

    private void assertKShortestPathsFinder(
        Graph<V, E> graph, V startVertex, int nPaths, int nMaxHops)
    {
        if (graph == null) {
            throw new NullPointerException("graph is null");
        }
        if (startVertex == null) {
            throw new NullPointerException("startVertex is null");
        }
        if (nPaths <= 0) {
            throw new NullPointerException("nPaths is negative or 0");
        }
        if (nMaxHops <= 0) {
            throw new NullPointerException("nMaxHops is negative or 0");
        }
    }

    private class PathWrapper
        implements GraphPath<V, E>
    {
        private RankingPathElement<V, E> rankingPathElement;

        private List<E> edgeList;

        PathWrapper(RankingPathElement<V, E> rankingPathElement)
        {
            this.rankingPathElement = rankingPathElement;
        }

        // implement GraphPath
        @Override
        public Graph<V, E> getGraph()
        {
            return graph;
        }

        // implement GraphPath
        @Override
        public V getStartVertex()
        {
            return startVertex;
        }

        // implement GraphPath
        @Override
        public V getEndVertex()
        {
            return rankingPathElement.getVertex();
        }

        // implement GraphPath
        @Override
        public List<E> getEdgeList()
        {
            if (edgeList == null) {
                edgeList = rankingPathElement.createEdgeListPath();
            }
            return edgeList;
        }

        // implement GraphPath
        @Override
        public double getWeight()
        {
            return rankingPathElement.getWeight();
        }

        // override Object
        @Override
        public String toString()
        {
            return getEdgeList().toString();
        }
    }
}

// End KShortestPaths.java
