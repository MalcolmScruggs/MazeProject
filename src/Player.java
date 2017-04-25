import java.awt.Color;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

public class Player {
  int x;
  int y;

  Player(int x, int y) {
    this.x = x;
    this.y = y;
  }

  WorldImage makeImage() {
    return new RectangleImage(MazeWorld.CELL_SIZE, MazeWorld.CELL_SIZE, OutlineMode.SOLID,
        Color.PINK);
  }
}