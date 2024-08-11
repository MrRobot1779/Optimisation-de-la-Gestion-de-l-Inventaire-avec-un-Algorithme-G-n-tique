package affichage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import initialisation.Model;


public class AlgoGen  {

	// ATTRIBUTS

	private final int MIN_CAPACITY_CAMION = 100;
	private final int MIN_VOLUME_CAMION = 100;
	private final int MIN_NB_ITER = 4;
    private final JFrame frame;
    private final Model model;
    private final JTextArea textArea;
    private JComboBox<Integer> productsNbBox;
    private final JCheckBox[] conditionsAllower;
    private JTextField[] camionTextFields;
    private JTextField[] conditionsTextFields;
    private final JButton start;

    // CONSTRUCTEURS

    public AlgoGen() {
        // MODELE
    	model = new Model();
        
        // VUE
        frame = createFrame();
        textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        productsNbBox = new JComboBox<Integer>();
        start = new JButton("Lancer");
        Integer[] product = new Integer[model.getTotalNbProduct()];
    	for(int i = 1; i < product.length; i++) {
    		product[i] = i;
    	} 
    	DefaultComboBoxModel<Integer> comboBoxModel = new DefaultComboBoxModel<Integer>(product);
    	productsNbBox.setModel(comboBoxModel);	
    	
        String[] conditions = {"Nombre d'itérations", "Temps d'éxecution (ms)", "Score minimum"};
        
        conditionsAllower = new JCheckBox[3];
        conditionsTextFields = new JTextField[3];
        
        for (int i = 0; i < conditions.length; i++) {
            conditionsTextFields[i] = new JTextField();
            conditionsAllower[i] = new JCheckBox(conditions[i]);
        }
        
        camionTextFields = new JTextField[2];
        camionTextFields[0] = new JTextField(20);
        camionTextFields[1] = new JTextField(20);
        
        placeComponents();
        // CONTROLEUR
        connectControllers();
    }

    // COMMANDES

    public void display() {  
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // OUTILS
    
    private JFrame createFrame() {
        JFrame f = new JFrame("Algorithme génétique");
        f.setPreferredSize(null);
        return f;
    }
    
    private void placeComponents() {
    	JPanel p = new JPanel();
        { //--
        	p.add(new JLabel("Veuillez remplir les champs (*) avant de lancer l'application"));
        } //--
        frame.add(p, BorderLayout.NORTH);
    	
        //new FlowLayout(FlowLayout.LEFT)
    	p = new JPanel(new GridLayout(0, 1));
        { //--
        	
        	JPanel q = new JPanel(new GridLayout(3, 0));
        	{ //--
        		q.add(new JLabel("Carctéristiques du camion (*)"));
        		
        		JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
        		{ //--
					r.add(new JLabel("Poids"));
		    		r.add(camionTextFields[0]);
                    r.add(new JLabel("Kg"));
        		} //--
        		q.add(r);
        		
        		r = new JPanel(new FlowLayout(FlowLayout.LEFT));
        		{ //--
		    		r.add(new JLabel("Volume"));
		    		r.add(camionTextFields[1]);
                    r.add(new JLabel("L"));
        		} //--
        		q.add(r);
        		
        		q.setBorder(BorderFactory.createEtchedBorder());
        		
        	} //--
    		p.add(q);
    		
    		
    		
    		q = new JPanel(new GridLayout(0, 1));
    		{ //--
    			q.add(new JLabel("Conditions à prendre en compte"));
    			
    			for(int i = 0; i < 3; i++) {
    					q.add(conditionsAllower[i]);
    					q.add(conditionsTextFields[i]);
	    		}
    		
    			
        		q.setBorder(BorderFactory.createEtchedBorder());
	        	
        	} //--
    		
    		p.add(q);
    		
    		q = new JPanel();
    		{ //--
    			q.add(new JLabel("Nombre de produits à utiliser (*)"));
        		q.add(productsNbBox);
	        	
        	} //--
    		p.add(q);
        } //--
        frame.add(p, BorderLayout.WEST);

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        p = new JPanel();
        { //--
        	p.add(start);
        } //--
        frame.add(p, BorderLayout.SOUTH);
    }
    

    private void connectControllers() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add observer to the model
        ((Observable) model).addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("\ndebut de start");
                String capacityText = camionTextFields[0].getText();
                double capacity;

                String volumeText = camionTextFields[1].getText();
                double volume;

                if (capacityText.isEmpty() || (capacity =Double.parseDouble(capacityText)) < MIN_CAPACITY_CAMION)
                    JOptionPane.showMessageDialog(null, "Entrez une capacité supérieure à " + MIN_CAPACITY_CAMION
                            , "Erreur", JOptionPane.ERROR_MESSAGE);
                else
                    model.setCapcity(capacity);

                if (volumeText.isEmpty() || (volume = Double.parseDouble(volumeText)) < MIN_CAPACITY_CAMION)
                    JOptionPane.showMessageDialog(null, "Entrez un volume supérieur à " + MIN_VOLUME_CAMION
                            , "Erreur", JOptionPane.ERROR_MESSAGE);
                else
                    model.setVolume(volume);

                if (conditionsAllower[0].isSelected()) {
                	
                	
                    String nbrIterrationText = conditionsTextFields[0].getText();
                    int nbrIterration = Integer.parseInt(nbrIterrationText);

                    if (nbrIterrationText.isEmpty() || (nbrIterration) < MIN_NB_ITER)
                        JOptionPane.showMessageDialog(null, "Entrez un nombre d'itérations supérieur à " + MIN_NB_ITER
                                , "Erreur", JOptionPane.ERROR_MESSAGE);
                    else {
                   
                        model.setNbIteration(nbrIterration);
                        model.setNbIterationAllower(true);
                    }
                } else {
                    model.setNbIterationAllower(false);
                }

                if (conditionsAllower[1].isSelected()) {
                    int value = Integer.parseInt(conditionsTextFields[1].getText());
                    model.setTempsExecution(value);
                    model.setTempsExecutionAllower(true);
                } else {
                	model.setTempsExecution(0);
                    model.setTempsExecutionAllower(false);
                }

                if (conditionsAllower[2].isSelected()) {
                    int value = Integer.parseInt(conditionsTextFields[2].getText());
                    model.setMinScore(value);
                    model.setMinScoreAllower(true);
                } else {
                    model.setMinScoreAllower(false);
                }
                            
                model.lancerAlgoGen();

                textArea.setText("");
                textArea.append(model.getResult());
            }
            
            
        });

        productsNbBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = (Integer) productsNbBox.getSelectedItem();
                System.out.print("\nnombre de produits selectionnée");
                if (n == 0) {
                    JOptionPane.showMessageDialog(null, "Le nombre de produits ne doit pas être égal à zéro"
                            , "Erreur", JOptionPane.ERROR_MESSAGE);
                } else {
                    model.setNbProduct(n);
                    model.setProducts(n);
                }
            }
        });
    }
	

    
    // POINT D'ENTREE
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AlgoGen().display();
            }
        });
    }
 
}

