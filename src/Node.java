import java.util.ArrayList;

public class Node {

	//Attributes
	private static int POSINF = (int) Double.POSITIVE_INFINITY;
	private static int NEGINF = (int) Double.NEGATIVE_INFINITY;
	private int max;
	private int min;
	private static int count; //number of created nodes
	private static int leafNo; //number of leafs belonging to this node
	private ArrayList<Integer> leafValues;
	
	// Getter
	public int getMax() {
		return max;
	}
	public int getMin() {
		return min;
	}
	public static int getCount() {
		return count;
	}
	public static int getLeafNo() {
		return leafNo;
	}
	public ArrayList<Integer> getLeafValues() {
		return leafValues;
	}
	
	// Setter
	public void setMax(int max) {
		this.max = max;
	}
	public void setMin(int min) {
		this.min = min;
	}
	
	// Constructor
	public Node() {
		this.max = POSINF;
		this.min = NEGINF;
		count++;
	}
	
	// Build Leaf
	public void buildLeaf(int value) {
		Leaf l1 = new Leaf();
		l1.setValue(value);
		leafNo++;
		leafValues.add(value);	
	}
}
