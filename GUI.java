//GUI.java
//Authors: Shirley Lu & Sojung Lee
//Purpose: To run the graphical user interface and display tournament information. 

import javax.swing.*; 
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*; 
import java.util.*;
import java.io.*;

/**
 * Defines a class, GUI, to store information about the graphical user interface. It 
 * keeps track of all the labels, buttons, text areas, and other GUI components that allow
 * the user to effectively and efficiently run the Quidditch Ranking Program 
 */
public class GUI extends JApplet{ 
    private JLabel teamInfoLabel, matchInfoLabel, teamExample, matchExample, poolLabel, poolLabel2, errorLabel; 
    private JComboBox fileField1, fileField2, stats; 
    private JTextField textInput1, textInput2, poolInput; 
    private JTextArea output; 
    private JButton poolSubmitButton, teamSubmitButton, matchSubmitButton, writeRankButton, writeInfoButton, saveButton, closeButton; 
    private JPanel p1, p2, pButtons, poolPanel; 
    private Container contentPane;
    private SpringLayout layout;
    private JScrollPane scrollPane;
    
    private boolean poolSubmitted; 
    private Go go;
 
    /** 
     * Constructs a GUI object, and invokes all the methods to create the java graphics components. 
     * It also initializes a new Go object. 
     */
    public GUI () {
	makeLabels();
	makeComboBoxes();
	makeTextFields();
	makeButtons(); 
	makeTextAreas();
	makePanelButtons();
	makeContainer();
	
	poolSubmitted = false; 
	go = new Go();
    }
 
    /**
     * Creates all the JLabels required for the graphical user interface
     */
    public void makeLabels() {
	teamInfoLabel = new JLabel("Team Information "); 
	matchInfoLabel = new JLabel("Match Information"); 
	teamExample = new JLabel("Sample entry: \'IvesPondTeams.txt\'");
	matchExample = new JLabel("Sample entry: \'IvesPondMatches.txt\'");
	poolLabel = new JLabel("Number of Pools");
	poolLabel2 = new JLabel("**Required");
	errorLabel = new JLabel("");
	errorLabel.setForeground(Color.red);
    }
	
    private void makeComboBoxesHelper(final JComboBox jcb, final JLabel jl){	
	jcb.addItem("Text File");
	jcb.addItem("Text Field"); 
	jcb.addItem("Database");
	jcb.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    String which = (String)jcb.getSelectedItem();
		    //For different selections on the combo box 
		    if (which.equals("Text File")){
				if (jl.equals(teamExample))
					jl.setText("Sample entry: \'IvesPondTeams.txt\'");
				else
					jl.setText("Sample entry: \'IvesPondMatches.txt\'");
		    }
		    else if (which.equals("Text Field")){
				if (jl.equals(teamExample))			
					jl.setText("Sample entry: \'1,Time Lords\'");
				else
					jl.setText("Sample entry: \'Time Lords,Sherlock,60,60*,30,30*,0,30*\'");
		    }
		    else if (which.equals("Database")){
			jl.setText("\n");
		    }
		}});
    }
	
    /**
     * Creates all the JComboBoxes required for the graphical user interface
     * Contains a dedicated actionPerformed methods to perform a specific action
     * in the event of a certain change in state. 
     */
    public void makeComboBoxes () { 
	fileField1 = new JComboBox(); 
	makeComboBoxesHelper(fileField1,teamExample);

	fileField2 = new JComboBox();
	makeComboBoxesHelper(fileField2,matchExample);

	stats = new JComboBox();
	stats.addItem("Overall Rankings");
	stats.addItem("Rankings by Pool");
	stats.addItem("Pools Information");
	stats.addItem("Matches Information");
	stats.addItem("Teams Information"); 
	stats.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    //For different selections for what the output box should display 
		    if (poolSubmitted){
			String which = (String)stats.getSelectedItem();
			if (which.equals("Overall Rankings")){
			    output.setText(go.getOverallRankings());
			}
			else if (which.equals("Rankings by Pool")){
			    String [] sA = go.getInPoolRankings();
			    StringBuilder sb = new StringBuilder ();
			    for (int i=1;i<sA.length;i++)
				sb.append(sA[i]);
			    output.setText(sb.toString());
			}
			else if (which.equals("Pools Information")){
			    output.setText(go.getPoolInfo());
			}
			else if (which.equals("Matches Information")){
			    output.setText(go.getMatches());
			}
			else if (which.equals("Teams Information")){
			    String [] sA = go.getTeamInfo2();
			    StringBuilder sb = new StringBuilder ();
			    for (int i=1;i<sA.length;i++){
					sb.append(sA[i]+"\n");
				}
			    output.setText(sb.toString());
			}
			errorLabel.setText(which+" displayed");
		    }
		    else 
			errorLabel.setText("Please submit number of pools");
		}});
    }

    /**
     * Creates all the JButtons required for the graphical user interface
     * Contains a dedicated actionPerformed methods to perform a specific action
     * in the event of a button press in the GUI
     */
    public void makeButtons() {
	writeRankButton = new JButton("Write Ranking");
	writeRankButton.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    if (poolSubmitted){
			//write ranking to file
			try {
			    boolean b = new File("Output Files/").mkdirs();
			    FileWriter writer = new FileWriter("Output Files"+File.separator+"Overall Rankings.txt"); 
			    BufferedWriter out = new BufferedWriter(writer); 
			    out.write(go.getOverallRankings()); 
				writer = new FileWriter("Output Files"+File.separator+"In Pool Rankings.txt");
				out = new BufferedWriter(writer);
				String [] sA = go.getInPoolRankings();
			    StringBuilder sb = new StringBuilder ();
			    for (int i=1;i<sA.length;i++)
					sb.append(sA[i]);
				out.write(sb.toString());
			    out.close();
			}
			catch (Exception ex){
			    System.err.println(ex);
			}
			errorLabel.setText("Rankings wrote to file");
		    }
		    else
			errorLabel.setText("Please submit number of pools");
		}});
	
	writeInfoButton = new JButton("Write Infos");
	writeInfoButton.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    if (poolSubmitted){	
			go.writeTeamInfo();
			go.writeInPoolRankings();
			errorLabel.setText("Team, and Match informations wrote to file");
		    }
		    else
			errorLabel.setText("Please submit number of pools");			
		}});
	
	saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    //creates database files
		    if (poolSubmitted){
			try {
			    //creates user accessible editable files
			    boolean b = new File("Database/").mkdirs();
			    File file = new File("Database"+File.separator+"teams.txt"); 
			    FileWriter writer = new FileWriter(file); 
			    BufferedWriter out = new BufferedWriter(writer); 
			    String [] sA = go.getTeamInfo();
			    for (int i=1;i<sA.length;i++)
				out.write(sA[i]); 
			    out.close();
					
			    file = new File("Database"+File.separator+"matches.txt"); 
			    writer = new FileWriter(file); 
			    out = new BufferedWriter(writer); 
			    out.write(go.getMatches()); 
			    out.close ();
			}
			catch (Exception ex){
			    System.err.println(ex);
			}
			errorLabel.setText("Databases updated");
		    }
		    else
			errorLabel.setText("Please submit number of pools");			
		}});
	
	closeButton = new JButton("Close"); 
	closeButton.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    System.exit(0);
		}});
	
	poolSubmitButton = new JButton("Submit");
	poolSubmitButton.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    //creates pools
		    go.createPools(Integer.parseInt(poolInput.getText())+1);
		    poolSubmitted = true;
		    errorLabel.setText("Number of pools submitted");
		}});
	
	teamSubmitButton = new JButton("Submit");
	teamSubmitButton.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    if (poolSubmitted){
			//reads stuff in from team file/text line depending on dropdown
			String which = (String)fileField1.getSelectedItem();
			if (which.equals("Text File")){
			    File f = new File (textInput1.getText());
			    if (f.exists()){
				go.readTeams(textInput1.getText());
				errorLabel.setText("Teams read from Text File");
			    }
			    else
				errorLabel.setText("Tried to read teams from file - File does not exist");
			}
			else if (which.equals("Text Field")){
			    if(textInput1.getText().split(",").length!=2){
				errorLabel.setText("Text Field formatted incorrectly");
			    }
			    else 
				go.addTeam(textInput1.getText());
			    errorLabel.setText("Teams read from Text Field");
			}
			else if (which.equals("Database")){
			    go.readTeams("Database"+File.separator+"teams.txt");
			    errorLabel.setText("Teams read from Database");
			}
				
		    }
		    else
			errorLabel.setText("Please submit number of pools");
		}});
	
	matchSubmitButton = new JButton ("Submit");
	matchSubmitButton.addActionListener(new ActionListener (){
		public void actionPerformed(ActionEvent e){
		    if (poolSubmitted){
			//reads stuff in from match file/text line depending on dropdown
			String which = (String)fileField2.getSelectedItem();
			if (which.equals("Text File")){
			    File f = new File (textInput2.getText());
			    if (f.exists()){
				go.readMatches(textInput2.getText());
				errorLabel.setText("Matches read from Text File");
			    }
			    else
				errorLabel.setText("Tried to read matches from file - File does not exist");
			}
			else if (which.equals("Text Field")){
			    if(textInput2.getText().split(",").length>4){
				errorLabel.setText("Text Field formatted incorrectly");
			    }
			    else
				go.readMatches(textInput2.getText());
			    errorLabel.setText("Matches read from Text Field");
			}
			else if (which.equals("Database")){
			    go.readMatches("Database"+File.separator+"matches.txt");
			    errorLabel.setText("Matches read from Database");
			}
				
		    }
		    else
			errorLabel.setText("please submit number of pools");			
		}});
    } 

    /**
     * Creates all the JTextFields required for the graphical user interface
     */
    public void makeTextFields() {
 
	textInput1 = new JTextField("teams.txt", 20); 
	textInput2 = new JTextField("matches.txt", 20);  
	poolInput = new JTextField("5",2); 
    } 

    /**
     * Creates all the JTextAreas required for the graphical user interface
     */
    public void makeTextAreas() { 
	output = new JTextArea("Statistics displayed here",15,48); 
    } 

    /**
     * Creates a JPanel holding the save, close, write rank, and write info button
     * FlowLayout.CENTER layout
     */
    public void makePanelButtons() {
    	pButtons = new JPanel(); 
    	pButtons.setLayout(new FlowLayout(FlowLayout.CENTER,2,1)); 
    	pButtons.add(writeRankButton); 
    	pButtons.add(writeInfoButton);
    	pButtons.add(saveButton);
    	pButtons.add(closeButton); 
    }

    /**
     * Creates a JPanel holding all the labels, buttons and text fields regarding team
     * information submission. FlowLayout.CENTER layout 
     */
    public void makePanel1() {
	p1 = new JPanel(); 
	p1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2)); 
	p1.add(teamInfoLabel); 
	p1.add(fileField1);
	p1.add(textInput1);
	p1.add(teamSubmitButton);
    }
 
    /** 
     * Creates a JPanel holding all of the labels, buttons, and text fields regarding
     * match information submission. FlowLayout.CENTER layout
     */
    public void makePanel2() {
	p2 = new JPanel();
	p2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
	p2.add(matchInfoLabel);
	p2.add(fileField2);
	p2.add(textInput2); 
	p2.add(matchSubmitButton);
    }
	
    /**
     * Creates a JScrollPane to allow the JTextArea "output" to scroll
     */
    public void makeScrollPane(){
	scrollPane = new JScrollPane(output);
	setPreferredSize(new Dimension (600,450));
    }
	
    /** 
     * Creates a JPanel holding all of the labels, buttons, and text fields regarding
     * pool information submission. FlowLayout.CENTER layout 
     */
    public void makePoolPanel(){
	poolPanel = new JPanel();
	poolPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,2));
	poolPanel.add(poolLabel);
	poolPanel.add(poolInput);
	poolPanel.add(poolLabel2);
	poolPanel.add(poolSubmitButton);
    }

    /** 
     * Creates a Container holding all of the created JPanels and scroll panes. 
     * Sets the layout of the GUI. 
     */ 
    public void makeContainer(){
	contentPane = new Container();
	layout = new SpringLayout();
	contentPane.setLayout(layout);
		
	makePanel1();
	makePanel2();
	makeScrollPane();
	makeButtons();
	makePoolPanel();
		
	contentPane.add(poolPanel);
		
	contentPane.add(p1);
	layout.putConstraint(SpringLayout.NORTH,p1,15,SpringLayout.SOUTH,poolPanel);
	contentPane.add(teamExample);
	layout.putConstraint(SpringLayout.NORTH,teamExample,30,SpringLayout.NORTH,p1);
	layout.putConstraint(SpringLayout.WEST,teamExample,140,SpringLayout.WEST,p1);
		
	contentPane.add(p2);
	layout.putConstraint(SpringLayout.NORTH,p2,15,SpringLayout.SOUTH,teamExample);
	contentPane.add(matchExample);
	layout.putConstraint(SpringLayout.NORTH,matchExample,30,SpringLayout.NORTH,p2);
	layout.putConstraint(SpringLayout.WEST,matchExample,140,SpringLayout.WEST,p2);
		
	contentPane.add(stats);
	layout.putConstraint(SpringLayout.WEST,stats,5,SpringLayout.WEST,contentPane);
	layout.putConstraint(SpringLayout.NORTH,stats,30,SpringLayout.SOUTH,matchExample);
		
	contentPane.add(scrollPane);
	layout.putConstraint(SpringLayout.WEST,scrollPane,5,SpringLayout.WEST,contentPane);
	layout.putConstraint(SpringLayout.EAST,scrollPane,-5,SpringLayout.EAST,contentPane);
	layout.putConstraint(SpringLayout.NORTH,scrollPane,5,SpringLayout.SOUTH,stats);
	layout.putConstraint(SpringLayout.SOUTH,scrollPane,-65,SpringLayout.SOUTH,contentPane);
		
	contentPane.add(errorLabel);
	layout.putConstraint(SpringLayout.WEST,errorLabel,5,SpringLayout.WEST,contentPane);
	layout.putConstraint(SpringLayout.SOUTH,errorLabel,-42,SpringLayout.SOUTH,contentPane);
		
	contentPane.add(pButtons);
	layout.putConstraint(SpringLayout.EAST,pButtons,-5,SpringLayout.EAST,contentPane);
	layout.putConstraint(SpringLayout.SOUTH,pButtons,-5,SpringLayout.SOUTH,contentPane);
    }
    
    /**	
     * Initializes the GUI by adding the Container contentPane
     */ 
    public void init () {  
	add(contentPane);
    } 
    
    /** 
     * Initalizes the JFrame for the GUI, sets the size, creates the applet and runs
     * init(). It also makes is so that the GUI is visible to the user. 
     */
    private static void createAndShowGUI() { 
	JFrame.setDefaultLookAndFeelDecorated(true);
	JFrame frame = new JFrame ("Quidditch Ranking Program");
	frame.setSize(600,570);
 
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	GUI applet = new GUI();
	applet.init();
  
	frame.add(applet, BorderLayout.CENTER);
	frame.setVisible(true); 
	
    }
 
    /**
     * Invokes the GUI and starts the program
     */

    public static void main (String[] args) { 
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
		public void run() { createAndShowGUI(); }  //runs the GUI 
	    });
    }
 

}
 
