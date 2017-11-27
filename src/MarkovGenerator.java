import java.util.ArrayList;

public class MarkovGenerator<E> {
	private ArrayList<Float> alphabet = new ArrayList<Float>(); //arrayList for alphabet for Markov Chain
	private ArrayList<Integer> instances = new ArrayList<Integer>(); //number of times each thing in the alphabet occurs
	private ArrayList<Float> generatedMarkov = new ArrayList<Float>(); //whatever is generated from the Markov Chain

	//double prob = 0.0;
	private int size = 0;
	
	private float[][] probabilities;
	private int[][] transitionTable;
		
	MarkovGenerator() {}
	
	ArrayList<Float> getGeneratedMarkov() {
		return generatedMarkov;
	}
	
	void createAlphabet(ArrayList<Float> elements) {//create an alphabet with all possible notes
		for (int i = 0; i < elements.size(); i++) {
			if (alphabet.contains(elements.get(i))) { //if element is contained in the alphabet already, increment the instances
				//System.out.println("Element contained in alphabet");
				instances.set(alphabet.indexOf(elements.get(i)), (instances.get(alphabet.indexOf(elements.get(i))) + 1));
			} else { //if not, add it to the alphabet and set its instances to 1
				//System.out.println("Element added to alphabet");
				alphabet.add(elements.get(i));
				instances.add(1);
			}
		}
	}
	
	void printAlphabetInstances() { //unit tests for alphabet and instances
		System.out.println("alphabet: " + alphabet);
		System.out.println("instances: " + instances);
	}
	
	void createTransitionTable(ArrayList<Float> elements) { //creates the transition table (trains on what follows)
		//System.out.println(elements);
		transitionTable = new int[alphabet.size()][alphabet.size()];
		int number = 0; //counts how many times the a number comes after the other

		
		for (int i = 0; i < alphabet.size(); i++) {
			for (int j = 0;  j < alphabet.size(); j++) {
				for (int a = 0; a < elements.size() - 1; a++) { //searches through the whole input arrayList
					if ((float)alphabet.get(i) == (float)elements.get(a) && (float)alphabet.get(j) == (float)elements.get(a+1)) {
						//System.out.println("curr: " + elements.get(a) + " i: " + alphabet.get(i) + " next: " + elements.get(a+1) + " j: " + alphabet.get(j));
						number++; //increments depending on how many times a number comes after the other
						//System.out.println("number: " + number);
					} else {
					}
				}
				transitionTable[i][j] = number;
				number = 0;
			}
		}
	}
	
	void testTransitionTable() { //unit test for transition table
		for (int i = 0; i < alphabet.size(); i++) {
			for (int j = 0;  j < alphabet.size(); j++) {
				System.out.println("transition table " + i + " " + j + " : " + transitionTable[i][j]);
			}
		}
	}
	
	void calculateProbabilities() { //calculates probabilities to use in generate
		probabilities = new float[alphabet.size()][alphabet.size()];
		int lineTotal = 0; 
		
		for (int i = 0; i < alphabet.size(); i++) {
			for (int j = 0; j < alphabet.size(); j++) {
				lineTotal = lineTotal + transitionTable[i][j];
				//System.out.println("i = " + i + " j = " + j + " total = " + lineTotal);
			}
			for (int j = 0; j < alphabet.size(); j++) {
				if (lineTotal == 0) {
				} else {
					probabilities[i][j] = ((float)transitionTable[i][j] / lineTotal);
					//System.out.println("i = " + i + " j = " + j + " probabilities" + probabilities[i][j]);
				}
			}
			lineTotal = 0;
		}
	}
	
	void testProbabilities() { //unit test for probabilities
		for (int i = 0; i < alphabet.size(); i++) {
			for (int j = 0; j < alphabet.size(); j++) {
				System.out.println("probabilities " + i + " " + j + ": "+ probabilities[i][j]);
			}
		}
	}
	
	void train(ArrayList<Float> elements) {
		createAlphabet(elements);
		//printAlphabetInstances();
		createTransitionTable(elements);
		//testTransitionTable();
		calculateProbabilities();
		//testProbabilities();
	}
	
	float findSeed() {
		float mostCommonValue = 0;
		int commCount = 0;
		for (int i = 0; i < instances.size()-1; i++) {
			if (instances.get(i) < instances.get(i+1)) {
				commCount = instances.get(i + 1);
			}
		}
//		System.out.println(commCount);
		if (commCount == 0) { //if everything has the same instance, start from the first word
			mostCommonValue  = (float) alphabet.get(0);
		} else { //else, start from the word that is the most probable to occur
			mostCommonValue = (float) alphabet.get(instances.indexOf(commCount));
		}
		return mostCommonValue;
	}
	
	void generate(E seed) {
		double prob = 0.0;
		double numMarkov = Math.random(); //random number generator
		
		if (alphabet.contains(seed)) {
			for (int j = 0; j < alphabet.size(); j++) {
				if (prob < numMarkov && numMarkov <= (prob + probabilities[alphabet.indexOf(seed)][j])) {
					generatedMarkov.add(alphabet.get(j)); //adds to the generated arrayList based on probability
				} else {}
				prob = prob + probabilities[alphabet.indexOf(seed)][j];
			}
		}
	}
	
}
