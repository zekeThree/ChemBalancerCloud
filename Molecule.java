package ChemBalancerCloud;
import java.util.*;

public class Molecule {
	private String[] elements;
	private int[] quantity;

	private boolean isR;

	public Molecule(String[] elements, int[] quantity, boolean r) {
		this.elements = elements;
		this.quantity = quantity;
		setR(r);
	}

	public Molecule(ArrayList<String> elements, ArrayList<Integer> quantity, boolean r) {
		this.elements = new String[elements.size()];
		for (int strNum = 0; strNum < elements.size(); strNum++) {
			this.elements[strNum] = elements.get(strNum);
		}
		this.quantity = new int[quantity.size()];
		for (int quaNum = 0; quaNum < quantity.size(); quaNum++) {
			this.quantity[quaNum] = quantity.get(quaNum);
		}

		setR(r);
	}

	public String[] getElements() {
		return elements;
	}

	public int[] getQuantity() {
		return quantity;
	}

	public ArrayList<Integer> coefficent(int co) {
		ArrayList<Integer> ans = new ArrayList<Integer>();
		for (int x = 0; x < quantity.length; x++) {
			ans.add(quantity[x] * co);
		}
		return ans;

	}

	public boolean isR() {
		return isR;
	}

	public void setR(boolean isR) {
		this.isR = isR;
	}
}
