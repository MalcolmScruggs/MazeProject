
public class RunMaze {
  public static void main(String[] args) {
    MazeWorld m = new MazeWorld();
    m.createMaze();
    m.bigBang(MazeWorld.WIDTH * MazeWorld.CELL_SIZE, MazeWorld.HEIGHT * MazeWorld.CELL_SIZE,
        MazeWorld.ANIMATION_SPEED);
  }

}
