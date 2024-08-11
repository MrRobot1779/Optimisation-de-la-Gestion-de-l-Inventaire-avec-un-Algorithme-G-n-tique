package entitees;

import java.util.Map;
import java.util.TreeMap;

public class StdPopulations  implements Populations {
	
	// ATRRIBUTS
	
	private Map<Integer, StdGeneration> populations;
	private Individu bestSolution;
	private int bestGenerationNumber;

	// CONSTRUCTEUR
	
	public StdPopulations(){
		populations = new TreeMap<Integer, StdGeneration>();
	}
	
	// REQUETES
	
	public int nbGenerations() {
		return populations.size();
	}
	
	public Map<Integer, StdGeneration> getPopulations(){
		return populations ;
	}
	
	public Individu getBestSolution() {
		return  bestSolution;
	}
	
	public int getBestGenerationNumber(){
		return bestGenerationNumber ;
	}
	
	// COMMANDES
	
	public void addGeneration(StdGeneration generation) {
	    if (generation == null) {
	        throw new IllegalArgumentException("valueProducts ne doit pas être null");
	    }
	    populations.put(generation.getNumber(), generation);
	    for (StdIndividu individu : generation.getGeneration()) {
	        if (bestSolution == null || individu.getfitnessValue() > bestSolution.getfitnessValue()) {
	            bestSolution = individu;
	            bestGenerationNumber = generation.getNumber();
	        }
	    }
	}

	public void clear() {
		populations.clear();
		bestSolution = null;
		bestGenerationNumber = 0;
	}


}


