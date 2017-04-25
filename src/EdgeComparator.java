import java.util.Comparator;

public class EdgeComparator implements Comparator<Edge> {

  @Override
  public int compare(Edge arg0, Edge arg1) {
    return arg0.weight - arg1.weight;
  }
  
}
