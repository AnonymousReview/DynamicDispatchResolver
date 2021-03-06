/*
 * (C) Copyright 2015-2017, by Fabian Späh and Contributors.
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
package org.jgrapht.alg.isomorphism;

import java.util.Comparator;

import org.jgrapht.Graph;

/**
 * This class is used to iterate over all existing (subgraph isomorphic) mappings between two
 * graphs. It is used by the {@link VF2SubgraphIsomorphismInspector}.
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the edges
 */
class VF2SubgraphMappingIterator<V, E>
    extends VF2MappingIterator<V, E>
{
    public VF2SubgraphMappingIterator(
        GraphOrdering<V, E> ordering1, GraphOrdering<V, E> ordering2,
        Comparator<V> vertexComparator, Comparator<E> edgeComparator)
    {
        super(ordering1, ordering2, vertexComparator, edgeComparator);
    }

    @Override
    protected IsomorphicGraphMapping<V, E> match()
    {
        VF2State<V, E> s;

        if (stateStack.isEmpty()) {
            Graph<V, E> g1 = ordering1.getGraph(), g2 = ordering2.getGraph();

            if ((g1.vertexSet().size() < g2.vertexSet().size())
                || (g1.edgeSet().size() < g2.edgeSet().size()))
            {
                return null;
            }

            s = new VF2SubgraphIsomorphismState<>(
                ordering1, ordering2, vertexComparator, edgeComparator);

            if (g2.vertexSet().isEmpty()) {
                return (hadOneMapping != null) ? null : s.getCurrentMapping();
            }
        } else {
            stateStack.pop().backtrack();
            s = stateStack.pop();
        }

        while (true) {
            while (s.nextPair()) {
                if (s.isFeasiblePair()) {
                    stateStack.push(s);
                    s = new VF2SubgraphIsomorphismState<>(s);
                    s.addPair();

                    if (s.isGoal()) {
                        stateStack.push(s);
                        return s.getCurrentMapping();
                    }

                    s.resetAddVertexes();
                }
            }

            if (stateStack.isEmpty()) {
                return null;
            }

            s.backtrack();
            s = stateStack.pop();
        }
    }
}

// End VF2SubgraphMappingIterator.java
