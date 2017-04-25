import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class MakeMaze {
  ArrayList<ArrayList<Vertex>> verticies;

  void createGrid() {
    this.verticies = new ArrayList<ArrayList<Vertex>>();
    for (int y = 0; y < MazeWorld.HEIGHT; y++) {
      ArrayList<Vertex> row = new ArrayList<Vertex>();
      verticies.add(row);
      for (int x = 0; x < MazeWorld.WIDTH; x++) {
        row.add(new Vertex("(" + x + ", " + y + ")", x, y));
      }
    }
  }

  void setEdges() {
    for (int y = 0; y < verticies.size(); y++) {
      for (int x = 0; x < verticies.get(y).size(); x++) {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        int weightRange = 100000;
        Random rand = new Random();
        Vertex curr = verticies.get(y).get(x);
        if (y > 0) {
          edges.add(new Edge(curr, verticies.get(y - 1).get(x), rand.nextInt(weightRange)));
        }
        if (y < verticies.size() - 1) {
          edges.add(new Edge(curr, verticies.get(y + 1).get(x), rand.nextInt(weightRange)));
        }
        if (x > 0) {
          edges.add(new Edge(curr, verticies.get(y).get(x - 1), rand.nextInt(weightRange)));
        }
        if (x < verticies.get(y).size() - 1) {
          edges.add(new Edge(curr, verticies.get(y).get(x + 1), rand.nextInt(weightRange)));
        }
        curr.setOutEdges(edges);
      }
    }
  }

  void clearEdges() {
    for (int y = 0; y < verticies.size(); y++) {
      for (int x = 0; x < verticies.get(y).size(); x++) {
        verticies.get(y).get(x).outEdges = new ArrayList<Edge>();
      }
    }
  }

  ArrayList<Edge> kruskals() {
    ArrayList<Edge> edges = new ArrayList<Edge>();
    ArrayList<Edge> tree = new ArrayList<Edge>();
    HashMap<String, String> representatives = new HashMap<String, String>();
    // add every edge to edges & all nodes represent themselves
    for (ArrayList<Vertex> row : verticies) {
      for (Vertex v : row) {
        representatives.put(v.name, v.name);
        for (Edge e : v.outEdges) {
          edges.add(e);
        }
      }
    }

    // sort edges
    Collections.sort(edges, new EdgeComparator());

    // remove shortest and add to tree if it doesn't introduce a cycle
    while (tree.size() < MazeWorld.WIDTH * MazeWorld.HEIGHT - 1) {
      if (edges.size() == 0) {
        throw new RuntimeException("Can't make tree when no edges");
      }
      String toName = edges.get(0).to.name;
      String fromName = edges.get(0).from.name;
      if (find(representatives, toName).equals(find(representatives, fromName))) {
        edges.remove(0);
      }
      else {
        representatives.put(find(representatives, toName), find(representatives, fromName));
        tree.add(edges.remove(0));
      }
    }
    return tree;
  }

  String find(HashMap<String, String> representatives, String s) {
    if (representatives.get(s).equals(s)) {
      return s;
    }
    else {
      return find(representatives, representatives.get(s));
    }
  }

  void setValidEdges(ArrayList<Edge> validEdges) {
    this.clearEdges();
    for (Edge e : validEdges) {
      int weight = e.weight;
      Vertex from = e.from;
      int fromX = from.x;
      int fromY = from.y;

      Vertex to = e.to;
      int toX = to.x;
      int toY = to.y;

      verticies.get(fromY).get(fromX).outEdges.add(e);
      verticies.get(toY).get(toX).outEdges.add(new Edge(to, from, weight));
    }
  }

  ArrayList<ArrayList<Vertex>> generateMaze() {
    createGrid();
    setEdges();
    setValidEdges(kruskals());
    return this.verticies;
  }
}
