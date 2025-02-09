package ChemBalancerCloud;

import java.util.*;

public class Main extends Thread {
	// holds the values of reactents/products after they are multiplyed by the coefs
	private static ArrayList<Integer> reactents = new ArrayList<Integer>();
	private static ArrayList<Integer> reactentsOld = new ArrayList<Integer>();
	private static ArrayList<Integer> products = new ArrayList<Integer>();
	// hold the reactents coefs
	private static ArrayList<Integer> reCoefs = new ArrayList<Integer>();
	// hold the products coefs
	private static ArrayList<Integer> prCoefs = new ArrayList<Integer>();
	// holds the coefs that balance the equation
	private static ArrayList<Integer> balanceCoefs = new ArrayList<Integer>();
	// used for a feature of sum4
	private static ArrayList<Integer> postValRe = new ArrayList<>();
	private static ArrayList<Integer> postValPr = new ArrayList<>();

	private static ArrayList<Integer> maxCoefs = new ArrayList<>(Arrays.asList(100, 100, 100, 100, 100, 100));

	public static void main(String[] args) {
		String answerBook = "";
		boolean Isproducts = false;

		ArrayList<String> argElem = new ArrayList<>();
		ArrayList<Integer> argCoef = new ArrayList<>();
		ArrayList<Molecule> recat = new ArrayList<Molecule>();
		ArrayList<Molecule> prod = new ArrayList<Molecule>();

		Scanner book = new Scanner(System.in);
		System.out.println("type m go to the next molucule, p to swich to products  and t to finsh ");

		while (true) {
			System.out.println(" 	type the element name ");
			argElem.add(book.next());
			System.out.println("	 type the ratio to molucule ");
			argCoef.add(book.nextInt());
			System.out.println("	next element(x) | add molucul (m) | products(p) | end(t) ");

			answerBook = book.next();
			if (answerBook.equals("x")) {
				Print(argElem, argCoef, recat, prod);

			} else if (answerBook.equals("m")) {
				if (Isproducts) {
					prod.add(new Molecule(argElem, argCoef, true));
				} else {
					recat.add(new Molecule(argElem, argCoef, false));
				}
				argElem.clear();
				argCoef.clear();
				Print(argElem, argCoef, recat, prod);
			} else if (answerBook.equals("p")) {
				prod.add(new Molecule(argElem, argCoef, false));

				argElem.clear();
				argCoef.clear();
				Print(argElem, argCoef, recat, prod);
				Isproducts = true;
			} else if (answerBook.equals("t")) {

				prod.add(new Molecule(argElem, argCoef, true));

				argElem.clear();
				argCoef.clear();
				Print(argElem, argCoef, recat, prod);
				break;
			}

		}

		for (int size = 0; size < Main.findElements(recat).size(); size++) {
			reactents.add(0);
			reactentsOld.add(0);
			postValRe.add(0);
		}
		for (int size = 0; size < recat.size(); size++) {
			reCoefs.add(1);
		}

		for (int size = 0; size < Main.findElements(prod).size(); size++) {
			products.add(0);
			postValPr.add(0);
		}
		for (int size = 0; size < prod.size(); size++) {
			prCoefs.add(1);
		}

		System.out.println(bust(recat, prod));
		System.out.println();

	}

// works/ prints the elements in argElem alongside the subsequent coefficient in argCoef 
//and labels them as reactent or product
	public static void Print(ArrayList<String> argElem, ArrayList<Integer> argCoef, ArrayList<Molecule> recat,
			ArrayList<Molecule> prod) {
		for (int d = 0; d < recat.size(); d++) {
			System.out.print("reactent # " + d + " ");
			for (String str : recat.get(d).getElements()) {
				System.out.print(str + " ");
			}
			System.out.println();
			System.out.print("	     ");
			for (int num : recat.get(d).getQuantity()) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		System.out.println("-->");

		for (int d = 0; d < prod.size(); d++) {
			System.out.print("product # " + d + " ");
			for (String str : prod.get(d).getElements()) {
				System.out.print(str + " ");
			}
			System.out.println();
			System.out.print("	    ");
			for (int num : prod.get(d).getQuantity()) {
				System.out.print(num + " ");
			}
			System.out.println();
		}
		for (int t = 0; t < argElem.size(); t++) {
			System.out.print("current: " + argElem.get(t) + " " + argCoef.get(t) + "  ");
		}
	}

// works/ returns all of the different elements in an arraylist of molocules- not
// in a esaly repeatable order
	public static ArrayList<String> findElements(ArrayList<Molecule> chemEqu) {
		ArrayList<String> Elements = new ArrayList<String>();
		boolean inEl = false;
		String str = chemEqu.get(0).getElements()[0];
		Elements.add(str);
		for (int x = 0; x < chemEqu.size(); x++) {

			for (int n = 0; n < chemEqu.get(x).getElements().length; n++) {
				str = chemEqu.get(x).getElements()[n];
				for (int t = 0; t < Elements.size(); t++) {
					if ((str.equals(Elements.get(t)))) {
						inEl = true;
					}
				}
				if (inEl) {
					inEl = false;
				} else {
					Elements.add(str);
				}
			}
		}
		return Elements;

	}

// works/ compairs two arrayLists of the same size and returns true if all of the corasponding valuse are equal;
//good
	public static boolean compair(ArrayList<Integer> re, ArrayList<Integer> pr) {
		boolean ans = true;
		for (int q = 0; q < re.size(); q++) {
			int one = re.get(q);
			int two = pr.get(q);
			if (!(one == two)) {
				ans = false;
			}
		}
		return ans;
	}

// works/ gives you the quantity of each element after multiplyed by each molecules respective coefficient 
// in the order findElements(compounts);	
//works
	public static ArrayList<Integer> computCoef(ArrayList<Molecule> compounts, ArrayList<Integer> coef) {
		// holds the end values of the elements quantity
		ArrayList<Integer> rePr = new ArrayList<Integer>();
		// holds the raito of a given element to the number of molecules/compounds
		// AlCl3 -> holds 1 for Al and 3 for Cl
		ArrayList<Integer> coefs = new ArrayList<Integer>();
		// holds the elements string used to match the
		ArrayList<String> elements = new ArrayList<String>();
		// holds the elements in a specific molecule to add their qualtity to the total.
		// e.i
//		for (String elem : compounts.get(c).getElements()) {
//			compElems.add(elem);
//		}
		ArrayList<String> compElems = new ArrayList<String>();
		elements = findElements(compounts);
		for (int elementsSize = 0; elementsSize < elements.size(); elementsSize++) {
			rePr.add(0);
		}

		for (int c = 0; c < compounts.size(); c++) {
			coefs = compounts.get(c).coefficent(coef.get(c));
			for (String elem : compounts.get(c).getElements()) {
				compElems.add(elem);
			}
			for (int addingCoef = 0; addingCoef < coefs.size(); addingCoef++) {

				for (int d = 0; d < elements.size(); d++) {
					if (compElems.get(addingCoef).equals(elements.get(d))) {

						rePr.set(d, (rePr.get(d) + coefs.get(addingCoef)));
					}
				}
			}

			compElems.clear();
		}
		return rePr;
	}

// works/ sets the instance variable reactents or products dependent on the boolean React
// sets them in the order findElements(Reactents);
	public static void addElements(ArrayList<Molecule> Reactents, ArrayList<Molecule> compounts,
			ArrayList<Integer> coefValues, boolean React) {
		// holds the location of each element determend by Main.findElements(Reactens);
		ArrayList<String> compountElements = new ArrayList<String>();
		// holds the location of each element in the arrayList
		ArrayList<String> compElems = new ArrayList<String>();
		compountElements = Main.findElements(Reactents);
		compElems = Main.findElements(compounts);
		if (React) {

			for (int addingCoef = 0; addingCoef < coefValues.size(); addingCoef++) {

				for (int d = 0; d < compountElements.size(); d++) {

					if (compElems.get(addingCoef).equals(compountElements.get(d))) {

						reactents.set(d, coefValues.get(addingCoef));
					}
				}
			}

		} else {
			for (int addingCoef = 0; addingCoef < coefValues.size(); addingCoef++) {

				for (int d = 0; d < compountElements.size(); d++) {

					if (compElems.get(addingCoef).equals(compountElements.get(d))) {

						products.set(d, coefValues.get(addingCoef));
					}
				}
			}

		}
		System.out.println("	" + compountElements.subList(0, compountElements.size()));

	}

	public static boolean isEqual(ArrayList<Molecule> recat, ArrayList<Molecule> prod) {
		System.out.println("isEqual-----------");
		Main.addElements(recat, recat, Main.computCoef(recat, reCoefs), true);
		System.out.println("Reactents :" + reactents.subList(0, reactents.size()));
		Main.addElements(recat, prod, Main.computCoef(prod, prCoefs), false);
		System.out.println("Products :" + products.subList(0, products.size()));
		System.out.println("isEqual-----------");
		return Main.compair(products, reactents);
	}

	// in progress
	public static ArrayList<Integer> bust(ArrayList<Molecule> recat, ArrayList<Molecule> prod) {

		for (int balanceCoefsSize = 0; balanceCoefsSize < (recat.size() + prod.size()); balanceCoefsSize++) {
			balanceCoefs.add(1);
		}
//		balanceCoefs.set(0,48);
//		balanceCoefs.set(1,5);
//		balanceCoefs.set(2,24);
//		balanceCoefs.set(3,36);
//		balanceCoefs.set(4,55);
//		balanceCoefs.set(5,24);
		System.out.println("first " + balanceCoefs.subList(0, balanceCoefs.size()));
		Main.updateBalanceAndRePr(0, 1, recat);
		if (Main.isEqual(recat, prod)) {
			return balanceCoefs;
		}

//		while (true) {
//			//Structure for sum3
//			Main.sum3(recat, prod, 0, maxCoefs);
//			reCoefs = Main.split(balanceCoefs, recat, true);
//			prCoefs = Main.split(balanceCoefs, recat, false);
//			if (Main.isEqual(recat, prod)) {
//				return balanceCoefs;
//			}
//
//		}
		while (true) {
			for (int all = 0; all < balanceCoefs.size(); all++) {
				Main.sum4(recat, prod, all, maxCoefs);
			}
			if (Main.isEqual(recat, prod)) {
				System.out.println("reached end #2");
				System.out.println("solution : " + balanceCoefs.subList(0, balanceCoefs.size()));
				return balanceCoefs;
			}
		}

	}

//works
	public static void sum3(ArrayList<Molecule> recat, ArrayList<Molecule> prod, int k, ArrayList<Integer> maxCoefs) {

		if (k > balanceCoefs.size()) {
			System.out.println("error soultion not in range- Rais the maxCoef||the equation might not be balencable");
		} else {
			if (k == 0) {
				for (int q = 0; q < maxCoefs.get(k); q++) {

					if (balanceCoefs.get(0) >= maxCoefs.get(k)) {
						balanceCoefs.set(0, 1);
						Main.sum3(recat, prod, k + 1, maxCoefs);
					}
					reCoefs = Main.split(balanceCoefs, recat, true);
					prCoefs = Main.split(balanceCoefs, recat, false);

					if (Main.isEqual(recat, prod)) {
						System.out.println("reached end #2");
						System.out.println(balanceCoefs.subList(0, balanceCoefs.size()));
						break;
					}
					balanceCoefs.set(0, balanceCoefs.get(0) + 1);
					System.out.println("current attempt			" + balanceCoefs.subList(0, balanceCoefs.size()));
				}

			} else {
				balanceCoefs.set(k, balanceCoefs.get(k) + 1);
				if (balanceCoefs.get(k) >= maxCoefs.get(k)) {
					balanceCoefs.set(k, 1);
					Main.sum3(recat, prod, k + 1, maxCoefs);
				}
				System.out.println("current attempt			" + balanceCoefs.subList(0, balanceCoefs.size()));

			}
		}
		// System.out.println(balanceCoefs.subList(0, balanceCoefs.size()));
	}

// in progress
	public static boolean sum4Helper(ArrayList<Molecule> recat, ArrayList<Molecule> prod, int k,
			ArrayList<Integer> maxCoefs, int SetVal) {
		if (k < SetVal) {

			for (int q = 0; q < maxCoefs.get(k); q++) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Main.updateBalanceAndRePr(k, balanceCoefs.get(k) + 1, recat);
				System.out.println("		current attempt(IN)" + balanceCoefs.subList(0, balanceCoefs.size()));
				if (Main.isEqual(recat, prod)) {

					System.out.println("solution : " + balanceCoefs.subList(0, balanceCoefs.size()));
					return true;
				}
				if (k < reCoefs.size() && balanceCoefs.get(k) != 1) {
					if (Main.hasElemGreater(recat, recat.get(k), true)) {
						Main.updateBalanceAndRePr(k, balanceCoefs.get(k) - 1, recat);
						Main.sum4Helper(recat, prod, k + 1, maxCoefs, SetVal);
						break;
					}
				} else if (balanceCoefs.get(k) != 1) {
//				ArrayList<Integer> holder = new ArrayList<Integer>();
//				for(int allBalVal : balanceCoefs) {
//					holder.add(allBalVal);
//				}
					if (Main.sum4Helper(recat, prod, 0, maxCoefs, SetVal-1)) {
						return true;
					}
//				System.out.println("Holder(IN) :"+holder.subList(0, holder.size()));
//				for(int allBalVal = 0; allBalVal<holder.size();allBalVal++) {
//					Main.updateBalanceAndRePr(allBalVal, holder.get(allBalVal), recat);
//				}
					if (Main.hasElemGreater(recat, prod.get(k - reCoefs.size()), false)) {
						if (k == balanceCoefs.size() - 1) {
							break;
						}
						Main.updateBalanceAndRePr(k, balanceCoefs.get(k) - 1, recat);
						Main.sum4Helper(recat, prod, k + 1, maxCoefs, SetVal);
						break;
					}
				}

			}
		}
//		else if(k==SetVal-1) {
//			
//		}
		return false;
	}

	public static void sum4(ArrayList<Molecule> recat, ArrayList<Molecule> prod, int k, ArrayList<Integer> maxCoefs) {

		for (int q = 0; q < maxCoefs.get(k); q++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Main.updateBalanceAndRePr(k, balanceCoefs.get(k) + 1, recat);
			System.out.println("		current attempt" + balanceCoefs.subList(0, balanceCoefs.size()));
			if (Main.isEqual(recat, prod)) {
				System.out.println("reached end #1");
				System.out.println("solution : " + balanceCoefs.subList(0, balanceCoefs.size()));
				break;
			}
			if (k < reCoefs.size() && balanceCoefs.get(k) != 1) {
				if (Main.hasElemGreater(recat, recat.get(k), true)) {
					Main.updateBalanceAndRePr(k, balanceCoefs.get(k) - 1, recat);
					break;
				}
			} else if (balanceCoefs.get(k) != 1) {

				ArrayList<Integer> holder = new ArrayList<Integer>();
				for (int allBalVal : balanceCoefs) {
					holder.add(allBalVal);
				}
				if (Main.sum4Helper(recat, prod, 0, maxCoefs, k)) {
					break;
				}
				System.out.println("Holder(ORI) :" + holder.subList(0, holder.size()));
				for (int allBalVal = 0; allBalVal < holder.size(); allBalVal++) {
					Main.updateBalanceAndRePr(allBalVal, holder.get(allBalVal), recat);
				}
				if (Main.hasElemGreater(recat, prod.get(k - reCoefs.size()), false)) {
					if (k == balanceCoefs.size() - 1) {
						break;
					}
					Main.updateBalanceAndRePr(k, balanceCoefs.get(k) - 1, recat);
					break;
				}
			}

		}
	}// 2 Al +6 HCl -> 2 AlCl3 + 3 H2

//in progress
	public static boolean hasElemGreater(ArrayList<Molecule> recat, Molecule compound, boolean isReact) {
		ArrayList<Boolean> hasElems = new ArrayList<>();
		boolean hasAllElemGreater = false;
		ArrayList<String> elementList = Main.findElements(recat);
		ArrayList<Molecule> Compound = new ArrayList<>(Arrays.asList(compound));
		ArrayList<String> elementMList = Main.findElements(Compound);

		if (isReact) {
			for (int allCompoundElem = 0; allCompoundElem < elementMList.size(); allCompoundElem++) {
				for (int elList = 0; elList < elementList.size(); elList++) {
					if (elementMList.get(allCompoundElem).equals(elementList.get(elList))) {
						if (reactents.get(elList) > products.get(elList)) {
							hasElems.add(true);
							// The position of a hasElems value corasponds to a element in the coumpound
							// and if that value is greater for the reactents than the products
							// to help tell if count sould jummp to the next coumpound
						} else {
							hasElems.add(false);
						}
					}
				}
			}
		} else {
			// about to leave left of here
			for (int allCompoundElem = 0; allCompoundElem < elementMList.size(); allCompoundElem++) {
				for (int elList = 0; elList < elementList.size(); elList++) {
					if (elementMList.get(allCompoundElem).equals(elementList.get(elList))) {
						if (reactents.get(elList) < products.get(elList)) {
							hasElems.add(true);
							// The position of a hasElems value corasponds to a element in the coumpound
							// and if that value is greater for the reactents than the products
							// to help tell if count sould jummp to the next coumpound
						} else {
							hasElems.add(false);
						}
					}
				}
			}
		}
		for (Boolean val : hasElems) {
			if (val) {
				hasAllElemGreater = true;
			}
		}
		System.out.println("compound elements: " + elementMList.subList(0, elementMList.size()));
		System.out.print("is greater than ");
		if (isReact) {
			System.out.print("products");
		} else {
			System.out.print("reactents");
		}
		System.out.println(" : " + hasElems.subList(0, hasElems.size()));

		return hasAllElemGreater;
	}

	public static void updateBalanceAndRePr(int k, int newValue, ArrayList<Molecule> recat) {
		balanceCoefs.set(k, newValue);
		reCoefs = Main.split(balanceCoefs, recat, true);
		prCoefs = Main.split(balanceCoefs, recat, false);
	}

//works
	public static ArrayList<Integer> split(ArrayList<Integer> toBeSpl, ArrayList<Molecule> recat, boolean front) {
		ArrayList<Integer> part = new ArrayList<Integer>();
		for (int halfish = 0; halfish < toBeSpl.size(); halfish++) {
			if (front) {
				if (halfish <= recat.size() - 1) {
					part.add(toBeSpl.get(halfish));
				}
			} else {
				if (halfish > recat.size() - 1) {
					part.add(toBeSpl.get(halfish));
				}
			}
		}
		return part;
	}

}
