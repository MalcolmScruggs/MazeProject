import java.util.ArrayList;

public class Vertex {
  String name;
  int x;
  int y;
  ArrayList<Edge> outEdges;

  Vertex(String name, int x, int y) {
    this.name = name;
    this.x = x;
    this.y = y;
    this.outEdges = new ArrayList<Edge>();
  }

  void setOutEdges(ArrayList<Edge> outEdges) {
    this.outEdges = outEdges;
  }
}