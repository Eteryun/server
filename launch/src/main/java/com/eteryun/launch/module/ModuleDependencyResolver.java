package com.eteryun.launch.module;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import com.eteryun.api.module.Module;

public class ModuleDependencyResolver {
    private static final Logger logger = LogManager.getLogger("Eteryun Engine");

    public static List<Module> resolveDependencies(List<Module> modules) {
        List<Module> sortedModules = new ArrayList<>(modules);
        sortedModules.sort(Comparator.comparing(Module::getName));

        ImmutableMap<String, Module> modulesMap = Maps.uniqueIndex(sortedModules, Module::getName);

        MutableGraph<Module> graph = GraphBuilder.directed()
                .allowsSelfLoops(false)
                .expectedNodeCount(sortedModules.size())
                .build();

        for (Module module : sortedModules) {
            List<String> dependencies = module.getDependencies();
            graph.addNode(module);

            if (dependencies != null) {
                for (String dependency : dependencies) {
                    Module dependencyModule = modulesMap.get(dependency);
                    if (dependencyModule != null) {
                        graph.putEdge(module, dependencyModule);
                    } else {
                        logger.error("Unable to resolve dependency " + dependency + " for module " + module.getName());
                        graph.removeNode(module);
                    }
                }
            }
        }

        final List<Module> sorted = new ArrayList<>();
        final Map<Module, Mark> marks = new HashMap<>();

        for (final Module node : graph.nodes()) {
            ModuleDependencyResolver.visitNode(graph, node, marks, sorted, new ArrayDeque<>());
        }

        return sorted;
    }

    private static void visitNode(final Graph<Module> dependencyGraph,
                                  final Module node,
                                  final Map<Module, Mark> marks,
                                  final List<Module> sorted,
                                  final Deque<Module> currentIteration) throws IllegalStateException {
        final Mark mark = marks.getOrDefault(node, Mark.NOT_VISITED);
        if (mark == Mark.PERMANENT) {
            return;
        } else if (mark == Mark.TEMPORARY) {
            // Circular dependency.
            currentIteration.addLast(node);

            final StringBuilder loopGraph = new StringBuilder();
            for (final Module container : currentIteration) {
                loopGraph.append(container.getName());
                loopGraph.append(" -> ");
            }

            loopGraph.setLength(loopGraph.length() - 4);
            throw new IllegalStateException("Circular dependency detected: " + loopGraph.toString());
        }

        currentIteration.addLast(node);
        marks.put(node, Mark.TEMPORARY);

        for (final Module edge : dependencyGraph.successors(node)) {
            ModuleDependencyResolver.visitNode(dependencyGraph, edge, marks, sorted, currentIteration);
        }

        marks.put(node, Mark.PERMANENT);
        currentIteration.removeLast();
        sorted.add(node);
    }

    private enum Mark {
        NOT_VISITED,
        TEMPORARY,
        PERMANENT
    }
}
