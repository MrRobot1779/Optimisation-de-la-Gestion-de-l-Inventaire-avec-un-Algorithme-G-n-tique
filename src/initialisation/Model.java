package initialisation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

import dataReader.StdDataReader;
import dataReader.StdProduct;
import entitees.Generation;
import entitees.Populations;
import entitees.StdGeneration;
import entitees.StdIndividu;
import entitees.StdPopulations;

public class Model extends Observable {
    
	// ATTRIBUTS
	private Populations populations;
	private StdGeneration lastGeneration ;
	private List<Integer> quantities;
    private int tailleGeneration;
    private int productsNumber;
    private int nbParents;
    
    private double maxCapacity;
    private double maxVolume;
    
    private StdDataReader dataReader;
    
    private String result;
    private int tempsExecution;
    private boolean tempsExecutionAllower;
    private int minScore;
    private boolean minScoreAllower;
    private int nbIterations;
    private boolean nbIterationAllower;
	
    // CONSTRUCTEUR
    
    public Model() {
    	getDataFromProperties();
    	populations = new StdPopulations();
    	// Lecture des données à partir du fichier CSV
		
        dataReader = new StdDataReader("src/data.csv");
        
        tempsExecutionAllower = false;
        minScoreAllower = false;
        nbIterationAllower = false;

        // Récupération des quantités de produits et lancer l'algo genetique

    }
    
    // Requete
    
    public int getTotalNbProduct() {
    	return dataReader.countLines("src/data.csv");
    }
    
    public Populations getPopulations(){
    	return populations;
    }
    
    public Generation getLastGenaration() {
    	return lastGeneration;
    }
    
    public String getResult() {
    	return result;
    }
    
    // COMMANDES
    
    public void setNbProduct(int nb) {
    	productsNumber = nb ;
    	setChanged();
        notifyObservers();
    }
    
    public void setProducts(int nb) {
    	dataReader.setProducts(nb);
    }
    
    public void setCapcity(double maxCapacity) {
    	this.maxCapacity = maxCapacity;
    	setChanged();
        notifyObservers();
    }
    
    public void setVolume(double maxVolume) {
    	this.maxVolume = maxVolume ;
    	setChanged();
        notifyObservers();
    }
    
    public void setNbIteration(int nbIterations) {
    	this.nbIterations =  nbIterations ;
    	setChanged();
        notifyObservers();
    }
    
    public void setNbIterationAllower(boolean b) {
    	this.nbIterationAllower =  b;
    	setChanged();
        notifyObservers();
    }
    
    public void setTempsExecution(int tempsExecution) {
    	this.tempsExecution =  tempsExecution ;
    	setChanged();
        notifyObservers();
    }
    
    public void setTempsExecutionAllower(boolean b) {
    	this.tempsExecutionAllower = b;
    	setChanged();
        notifyObservers();
    }
    
    public void setMinScore(int minScore) {
    	this.minScore =  minScore;
    	setChanged();
        notifyObservers();
    }
    
    public void setMinScoreAllower(boolean b) {
    	this.minScoreAllower =  b;
    	setChanged();
        notifyObservers();
    }
    
    
    
    public void lancerAlgoGen() {
    	
    	long startTime = System.currentTimeMillis();
    	
        // Création de la première génération
    	quantities = dataReader.getQuantities();
        StdGeneration gen = StdGeneration.createInitialGeneration(tailleGeneration, productsNumber, quantities);
        populations.clear();
        populations.addGeneration(gen);
        
        // Initialiser les individus de la 1ere generation d'apres les produits de data.csv
        
        for (StdIndividu individu : gen.getGeneration()) {
            
        	individu.setValue(dataReader.getPrices());
            
            individu.setWeight(dataReader.getWeights());
            
            individu.setVolume(dataReader.getVolumes());
            
            individu.setFitness(maxCapacity, maxVolume);
        }
        
        StringBuilder result1 = new StringBuilder();
        lastGeneration = gen;
        double score = 0;
        int j = 0;
        
        while(!(nbIterationAllower || minScoreAllower || tempsExecutionAllower)? (System.currentTimeMillis() - startTime < 100) : 
    		(nbIterationAllower? (j < nbIterations) : true) && (minScoreAllower ? (score < minScore) : true) &&
    		(tempsExecutionAllower ? (System.currentTimeMillis() - startTime < tempsExecution) : true)) {

            if(j > 0)
        		populations.addGeneration(lastGeneration);

        	score = 0;
        	score = lastGeneration.getBestIndividu().getfitnessValue();
        	 result1.append("le meilleur individu de la génération numéro " + lastGeneration.getNumber() + "\n");
             result1.append(lastGeneration.getBestIndividu().affichageCaraIndividu());
             result1.append("Score : " + score + "\n");

        	
            // Sélection des parents pour la prochaine génération
            List<StdIndividu> parents = lastGeneration.selectParents(nbParents);
        
            
            // Croisement des parents pour générer des enfants
            List<StdIndividu> childs = lastGeneration.croisementParents(parents);
            
            
            // Mutation des parents et des enfants
            List<StdIndividu> mutParents = lastGeneration.mutationIndividus(parents);
            List<StdIndividu> mutchilds = lastGeneration.mutationIndividus(childs);
            
            // Évaluation des nouveaux individus après mutation
            for(StdIndividu child : mutchilds) {
            	
				child.setValue(dataReader.getPrices());
                child.setWeight(dataReader.getWeights());
                child.setVolume(dataReader.getVolumes());
                child.setFitness(maxCapacity,maxVolume);
            }
            
            for(StdIndividu parent : mutParents) {
            	parent.setValue(dataReader.getPrices());
            	parent.setWeight(dataReader.getWeights());
                parent.setVolume(dataReader.getVolumes());
                parent.setFitness(maxCapacity,maxVolume);
            }
      
            // Création de la nouvelle génération
            lastGeneration = new StdGeneration(++j);
            lastGeneration.addIndividus(mutParents);
            lastGeneration.addIndividus(mutchilds);

            result1.append("--------------------------------------------------------------\n");

        } 
        
        result1.append("--------------------------------------------------------------\n");
        result1.append("--------------------------------------------------------------\n");
        
        // Affichage de la meilleure solution trouvée
       int n  =  populations.getBestGenerationNumber();
        result1.append("Meilleure solution trouvée à la génération numero : " + n + "\n"
        		+ "avec ces caractéristiques : \n" + populations.getBestSolution().affichageCaraIndividu());
        score = populations.getBestSolution().getfitnessValue();
        result1.append("Score : " + score + "\n");
        
        result1.append("\nLa liste de ses produits :\n");
        
        List<Integer> produits = populations.getBestSolution().getIndividu();
        List<StdProduct> products = dataReader.getProducts();

        for (int i = 0; i < produits.size(); i++) {
        	if(produits.get(i) > 0)
        		result1.append(products.get(i).getName() + ", quantité = " + produits.get(i) + "\n");
        }
        
        long t = System.currentTimeMillis() - startTime;
        long tEnS = t / 1000;
        
        result1.append("\n\n\n\nTemps d'exécution : " + t + " ms / " + tEnS +  "," + (t - tEnS * 1000) + " s" );
        
        result = result1.toString();
        
        System.out.print("\nfin de lancerAlgoGen model");
        
        setChanged();
        notifyObservers();
    }
      
    
    
    // OUTILS
    
    private void getDataFromProperties() {
    	// Lecture des préférences depuis le fichier properties
        
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/preferences.properties"));
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier entré n'existe pas ou n'est pas accessible en lecture : " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Une erreur s'est produite lors de la lecture du fichier : " + e.getMessage());
        }
        
        // Extraction des paramètres
        
        tailleGeneration = Integer.parseInt(properties.getProperty("nbIndividualsPerGeneration"));
        nbParents = Integer.parseInt(properties.getProperty("nbGenerationPrents"));
        
    }
	
	
    
}

