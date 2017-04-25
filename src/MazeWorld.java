import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.*;
import javalib.worldimages.*;


public class MazeWorld extends World {
  static final int WIDTH = 100;
  static final int HEIGHT = 40;
  static final int CELL_SIZE = 18; // must be even number
  static final int FONT_SIZE = 20;
  static final double ANIMATION_SPEED = 0.00000000000000000000000001;

  Player p;
  boolean gameActive;
  boolean showBreadthSearch;
  boolean showDepthSearch;

  ArrayList<ArrayList<Vertex>> maze;
  ArrayList<Edge> edges;
  ArrayList<Vertex> breadthSearchPath;
  ArrayList<Vertex> depthSearchPath;
  ArrayList<Vertex> finalPath;
  int tickCount;
  int breadthSearchIndex;
  int depthSearchIndex;

  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
    renderVerticies(scene);

    // render breadth first search path
    renderBreadthFirstSearch(scene);

    // render depth first search path
    renderDepthFirstSearch(scene);

    // render player
    scene.placeImageXY(p.makeImage(), p.x * CELL_SIZE + (CELL_SIZE / 2),
        p.y * CELL_SIZE + (CELL_SIZE / 2));

    // render reset text
    if (!gameActive) {
      WorldImage img = new TextImage("press R to reset", FONT_SIZE, Color.red);
      scene.placeImageXY(img, (WIDTH / 2) * CELL_SIZE, (HEIGHT / 2) * CELL_SIZE + CELL_SIZE);
      for (Vertex v : finalPath) {
        WorldImage rect1 = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
            new Color(0, 155, 255, 100));

        scene.placeImageXY(rect1, v.x * CELL_SIZE + CELL_SIZE / 2, v.y * CELL_SIZE + CELL_SIZE / 2);
      }
    }

    return scene;
  }

  /**
   * Creates the image of the maze by using the verticies and their list of edges.
   * The images created are placed onto the give scene.
   * 
   * @param scene - the world scene that the created images are placed onto.
   */
  private void renderVerticies(WorldScene scene) {
    // render all vertexes
    for (ArrayList<Vertex> row : maze) {
      for (Vertex v : row) {
        if (v.x == 0 && v.y == 0) {
          WorldImage rect1 = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
              Color.green);
          scene.placeImageXY(rect1, v.x * CELL_SIZE + CELL_SIZE / 2,
              v.y * CELL_SIZE + CELL_SIZE / 2);

        }
        else if (v.x == WIDTH - 1 && v.y == HEIGHT - 1) {
          WorldImage rect2 = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
              Color.magenta);
          scene.placeImageXY(rect2, v.x * CELL_SIZE + CELL_SIZE / 2,
              v.y * CELL_SIZE + CELL_SIZE / 2);
        }
        else {
          WorldImage rect = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, Color.gray);
          scene.placeImageXY(rect, v.x * CELL_SIZE + CELL_SIZE / 2,
              v.y * CELL_SIZE + CELL_SIZE / 2);
        }
        // set walls on verticies
        boolean wallTop = true;
        boolean wallLeft = true;
        boolean wallBottom = true;
        boolean wallRight = true;
        for (Edge e : v.outEdges) {
          // check top
          if (e.to.y + 1 == v.y) {
            wallTop = false;
          }
          // check left
          if (e.to.x + 1 == v.x) {
            wallLeft = false;
          }
          // check bottom
          if (e.to.y - 1 == v.y) {
            wallBottom = false;
          }
          // check right
          if (e.to.x - 1 == v.x) {
            wallRight = false;
          }
        }
        if (wallTop) {
          WorldImage img = new LineImage(new Posn(CELL_SIZE, 0), Color.black);
          scene.placeImageXY(img, (v.x * CELL_SIZE + (CELL_SIZE / 2)), v.y * CELL_SIZE);
        }
        if (wallLeft) {
          WorldImage img = new LineImage(new Posn(0, CELL_SIZE), Color.black);
          scene.placeImageXY(img, v.x * CELL_SIZE, v.y * CELL_SIZE + (CELL_SIZE / 2));
        }
        if (wallBottom) {
          WorldImage img = new LineImage(new Posn(CELL_SIZE, 0), Color.black);
          scene.placeImageXY(img, (v.x * CELL_SIZE) + (CELL_SIZE / 2), v.y * CELL_SIZE + CELL_SIZE);
        }
        if (wallRight) {
          WorldImage img = new LineImage(new Posn(0, CELL_SIZE), Color.black);
          scene.placeImageXY(img, v.x * CELL_SIZE + CELL_SIZE, v.y * CELL_SIZE + (CELL_SIZE / 2));
        }
      }
    }
  }

  /**
   * Creates the image of the current progress of the breadth first search
   * The images created are placed onto the give scene.
   * 
   * @param scene - the world scene that the created images are placed onto.
   */
  private void renderBreadthFirstSearch(WorldScene scene) {
    for (int i = 0; i < breadthSearchIndex; i++) {
      WorldImage rect = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
          new Color(0, 0, 155, 100));
      scene.placeImageXY(rect, breadthSearchPath.get(i).x * CELL_SIZE + CELL_SIZE / 2,
          breadthSearchPath.get(i).y * CELL_SIZE + CELL_SIZE / 2);
    }
    if (breadthSearchIndex >= breadthSearchPath.size() - 2) {
      for (Vertex v : finalPath) {
        WorldImage rect1 = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
            new Color(0, 155, 255, 100));

        scene.placeImageXY(rect1, v.x * CELL_SIZE + CELL_SIZE / 2, v.y * CELL_SIZE + CELL_SIZE / 2);
      }
      WorldImage img = new TextImage("BFS took " + breadthSearchPath.size() + " moves ("
          + (breadthSearchPath.size() - finalPath.size()) + " wrong)", FONT_SIZE, Color.red);

      scene.placeImageXY(img, (WIDTH / 2) * CELL_SIZE, (HEIGHT / 2) * CELL_SIZE - CELL_SIZE);
    }
  }

  /**
   * Creates the image of the current progress of the breadth first search
   * The images created are placed onto the give scene.
   * 
   * @param scene - the world scene that the created images are placed onto.
   */
  private void renderDepthFirstSearch(WorldScene scene) {
    for (int i = 0; i < depthSearchIndex; i++) {
      WorldImage rect = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
          new Color(0, 255, 155, 100));
      scene.placeImageXY(rect, depthSearchPath.get(i).x * CELL_SIZE + CELL_SIZE / 2,
          depthSearchPath.get(i).y * CELL_SIZE + CELL_SIZE / 2);
    }
    if (depthSearchIndex >= depthSearchPath.size() - 2) {
      for (Vertex v : finalPath) {
        WorldImage rect1 = new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID,
            new Color(0, 155, 255, 100));

        scene.placeImageXY(rect1, v.x * CELL_SIZE + CELL_SIZE / 2, v.y * CELL_SIZE + CELL_SIZE / 2);
      }
      WorldImage img = new TextImage("DFS took " + depthSearchPath.size() + " moves ("
          + (depthSearchPath.size() - finalPath.size()) + " wrong)", FONT_SIZE, Color.red);

      scene.placeImageXY(img, (WIDTH / 2) * CELL_SIZE, (HEIGHT / 2) * CELL_SIZE);
    }
  }

  @Override
  public void onTick() {
    if (this.showBreadthSearch) {

      tickCount++;
      if (tickCount >= 0 && breadthSearchIndex < breadthSearchPath.size() - 2) {
        breadthSearchIndex++;
      }
    }

    if (this.showDepthSearch) {
      tickCount++;
      if (tickCount >= 0 && depthSearchIndex < depthSearchPath.size() - 2) {
        depthSearchIndex++;
      }
    }
  }


  @Override
  public void onKeyEvent(String ke) {
    if (ke.equals("right") && this.validMoveFrom(p.x, p.y, p.x + 1, p.y)) {
      p.x++;
    }
    else if (ke.equals("left") && this.validMoveFrom(p.x, p.y, p.x - 1, p.y)) {
      p.x--;
    }
    else if (ke.equals("up") && this.validMoveFrom(p.x, p.y, p.x, p.y - 1)) {
      p.y--;
    }
    else if (ke.equals("down") && this.validMoveFrom(p.x, p.y, p.x, p.y + 1)) {
      p.y++;
    }
    else if (ke.equals("r")) {
      createMaze();
    }
    else if (ke.equals("b")) {
      this.showBreadthSearch = !this.showBreadthSearch;
    }
    else if (ke.equals("d")) {
      this.showDepthSearch = !this.showDepthSearch;
    }
    checkEnd();
  }

  boolean validMoveFrom(int fromX, int fromY, int destX, int destY) {
    if (destX < 0 || destX >= WIDTH || destY < 0 || destY >= HEIGHT) {
      return false;
    }
    ArrayList<Edge> fromOutEdges = maze.get(fromY).get(fromX).outEdges;
    for (Edge e : fromOutEdges) {
      if (e.to.x == destX && e.to.y == destY) {
        return true;
      }
    }
    return false;
  }

  void checkEnd() {
    if (p.x == WIDTH - 1 && p.y == HEIGHT - 1) {
      this.gameActive = false;
    }
  }

  void breadthFirstSearch() {
    GraphUtils g = new GraphUtils();
    this.breadthSearchPath = g.breadthFirstCheckAll(this.maze.get(0).get(0),
        this.maze.get(HEIGHT - 1).get(WIDTH - 1));
    this.finalPath = g.shortestPath(this.maze.get(0).get(0),
        this.maze.get(HEIGHT - 1).get(WIDTH - 1));
  }

  void depthFirstSearch() {
    GraphUtils g = new GraphUtils();
    this.depthSearchPath = g.depthFirstCheckAll(this.maze.get(0).get(0),
        this.maze.get(HEIGHT - 1).get(WIDTH - 1));
    this.finalPath = g.shortestPath(this.maze.get(0).get(0),
        this.maze.get(HEIGHT - 1).get(WIDTH - 1));
  }

  // resets the values in the maze
  void createMaze() {
    MakeMaze m = new MakeMaze();
    m.createGrid();
    m.setEdges();
    this.edges = m.kruskals();
    m.setValidEdges(edges);
    this.maze = m.verticies;
    this.p = new Player(0, 0);
    this.gameActive = true;
    this.showBreadthSearch = false;
    this.breadthSearchIndex = 0;
    this.showDepthSearch = false;
    this.depthSearchIndex = 0;
    this.tickCount = 0;
    this.breadthFirstSearch();
    this.depthFirstSearch();
  }
}
