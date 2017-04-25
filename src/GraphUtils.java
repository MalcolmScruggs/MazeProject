import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GraphUtils {
  ArrayList<Vertex> depthFirstCheckAll(Vertex source, Vertex target) {
    ArrayList<Vertex> visited = new ArrayList<Vertex>();
    ArrayList<Vertex> worklist = new ArrayList<Vertex>();
    worklist.add(source);

    while (worklist.size() > 0) {
      Vertex next = worklist.get(0);
      if (visited.contains(next)) {
        worklist.remove(0);
      }
      else if (next.equals(target)) {
        return visited;
      }
      else {
        for (Edge e : next.outEdges) {
          worklist.add(0, e.to);
        }
        visited.add(next);
      }
    }
    return null;
  }

  ArrayList<Vertex> breadthFirstCheckAll(Vertex source, Vertex target) {
    ArrayList<Vertex> visited = new ArrayList<Vertex>();
    ArrayList<Vertex> worklist = new ArrayList<Vertex>();
    worklist.add(source);

    while (worklist.size() > 0) {
      Vertex next = worklist.get(0);
      if (visited.contains(next)) {
        worklist.remove(0);
      }
      else if (next.equals(target)) {
        return visited;
      }
      else {
        for (Edge e : next.outEdges) {
          worklist.add(e.to);
        }
        visited.add(next);
      }
    }
    return null;
  }

  ArrayList<Vertex> shortestPath(Vertex source, Vertex target) {
    HashSet<Vertex> unvisited = new HashSet<Vertex>();
    HashMap<Vertex, Integer> distance = new HashMap<Vertex, Integer>();
    HashMap<Vertex, Vertex> predecessors = new HashMap<Vertex, Vertex>();
    distance.put(source, 0);
    unvisited.add(source);
    while (unvisited.size() > 0) {
      Vertex v = this.getMin(unvisited, distance);
      unvisited.remove(v);
      for (Edge e : v.outEdges) {
        if (distance.get(e.to) == null || distance.get(e.to) > distance.get(v) + e.weight) {
          distance.put(e.to, distance.get(v) + e.weight);
          unvisited.add(e.to);
          predecessors.put(e.to, v);
        }
      }
    }
    // get path to target
    ArrayList<Vertex> path = new ArrayList<Vertex>();
    Vertex step = target;
    if (predecessors.get(step) == null) {
      return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(0, step);
    }
    return path;
  }

  Vertex getMin(HashSet<Vertex> unvisited, HashMap<Vertex, Integer> distance) {
    Vertex min = null;
    for (Vertex v : unvisited) {
      if (min == null) {
        min = v;
      }
      else {
        if (distance.get(v) != null && distance.get(min) != null
            && distance.get(v) < distance.get(min)) {
          min = v;
        }
      }
    }
    return min;
  }
}
