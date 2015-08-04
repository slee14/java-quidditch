//Go.java
//Authors: Shirley Lu & Sojung Lee
//Purpose: Defines a class, Go, to store information about all the Teams, Matches, and Pools that will be used in a Quidditch Tournament. It keeps track of all the Teams, Matches and Pools using three separate Hashtables. 
//Edits:
//12.2.12 Document created, skeletons added(Shirley Lu)
//12.2.12 Javadoc added (Sojung Lee)
//12.2.12 use Vector instead of Hashtables? maybe just use Vector for teams, keep Hashtable for the rest

import java.util.*;
import java.io.*;
import javax.swing.*; 
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*; 

public class Go { 
 
    //---------Go Class variables and methods-----------
 
    private Vector <Team> allTeams;
    private Hashtable <Match,Match> allMatches;
    private Hashtable <Integer,Pool> allPools;
 
    /**
     * Constructs a Go object, and initializes allTeams, allMatches and allPools
     */
    public Go (){
	allTeams = new Vector <Team> ();
	allMatches = new Hashtable <Match,Match> (500);
	allPools = new Hashtable <Integer,Pool> (20);
    }
    
    /**
     * Adds a Team object using its name and pool integer  of String s, and adds it to the 
     * allTeams Vector, as well as to the corresponding Pool in the Hashtable allPools. 
     * @param A String value s representing information needed to make the Team object. 
     */
    public void addTeam (String s){
	int nPool = Integer.parseInt(s.substring(0,1)); //Gets the pool number from s
	String tName = s.substring(2); //Gets the name from s
	Team temp = new Team(tName,nPool); //creation of a temporary Team 
	allTeams.add(temp); //add the Team object to allTeams Vector 
	allPools.get(nPool).addTeam(temp); //add the Team object to a Pool in allPools
    }
	
    /**
     * Adds a Match object using its String representation, and adds it to the allMatches 
     * Hashtable. When using String s to create a Match object, the length of the string and 
     * the position of "*" must be considered. 
     * @param A String value s representing information needed to make the Match object.
     */
    public void addMatch (String s){
	String[] splitLine = s.split(","); //Splits by comma 
		
	Team[] twoTeams = inputTeams(splitLine); //Fills in twoTeams with two Team objects		
	boolean[] snatches = new boolean[3];  //Creation of a boolean[] for the 3 match period
	int [][] scores = new int [2][3];  //Creation of an int[][] for the 3 match periods 
	boolean [] overtime = new boolean[3]; //Creation of a boolean[] for whether went into overtime
	addMatchHelper(splitLine,2,0,snatches,scores,overtime);
	addMatchHelper(splitLine,4,1,snatches,scores,overtime);
	addMatchHelper(splitLine,6,2,snatches,scores,overtime);
			
	Match m = new Match(twoTeams,scores,snatches,overtime);
	allMatches.put(m,m); //add the match to the allMatches Hashtable
    }
 
    private void addMatchHelper (String[] line, int index, int n, boolean[] snatches, int[][] scores, boolean[] overtime) {
	if (line.length>index) { //went into overtime 1
	    boolean a = line[index].contains("*"); 
		
	    if (a) {
		snatches[n] = true; 
		scores[n][0] = Integer.parseInt(line[index].replace("*","")); 
		scores[n][1] = Integer.parseInt(line[index+1]); 
	    } else {
		snatches[n] = false; 
		scores[n][0] = Integer.parseInt(line[index]); 
		scores[n][1] = Integer.parseInt(line[index+1].replace("*","")); 
	    }
	    overtime[n] = true; 
	}
    }
    /**
     * Reads in from a text file and puts them in them the allTeams Vector 
     * @param A String for the input text file. 
     */
    public void readTeams (String fileAt){
  	//reads teams in from a text file
	try {
	    Scanner sc = new Scanner (new File(fileAt));
	    while (sc.hasNext()){
		String line = sc.nextLine();
		addTeam(line);
	    } 
	    sc.close();
	}
	catch (FileNotFoundException ex){
	    ex.printStackTrace();
	}
	catch (IOException ex){
	    ex.printStackTrace();
	}	
    }
 
    /**
     * Reads in from a text file and puts them in the allMatches Hashtable
     * @param A String for the input text file. 
     */
    public void readMatches (String inFileName){
	//reads matches in from a text file
	try { 
	    Scanner reader = new Scanner (new File(inFileName)); //Read in the inFileName file
	    while (reader.hasNext()) {
		String line = reader.nextLine();
		addMatch(line);	 //each line is a string representing match information	
	    }
	}
	catch (Exception ex){
	    System.out.println("here");
	    System.err.println (ex);
	}
    }

    /**
     * Creates a Team array object representing two teams playing each other
     * @param A String[] splitLine, containing information about both Team objects
     * @return A Team[] of two Team objects that play each other 
     */
    public Team[] inputTeams (String[] splitLine) {	
	Team[] bothTeams = new Team[2]; 

	for (int i = 0; i < allTeams.size(); i++) {
	    Team current = allTeams.get(i); 
	    String currentName = current.getName(); 
	    if (currentName.equals(splitLine[0])) bothTeams[0] = current; 
	    if (currentName.equals(splitLine[1])) bothTeams[1] = current; 
	}
	return bothTeams; 
    }

    /**
     * Creates nPools number of empty Pools, then puts them in the allPools Hashtable. The pools are referenced by an Integer.
     * @param An integer representing the number of pools we want in the tournament 
     */
    public void createPools (int nPools){
	for (int i=nPools;i>0;i--){ //loops for every pool we want to create
	    Pool temp = new Pool (); //creation of an empty Pool object 
	    allPools.put(i,temp); //add the Pool object to the allPools Hashtable 
	}
    }
 
    /**
     * Returns in pool ranking as a String array
     * The string will contain the teams in a each pool in sorted order.
     * @return A String representing the rankings within a single Pool
     */
    public String[] getInPoolRankings (){
	String[] temp = new String[allPools.size()]; 
	for (int i=1;i<allPools.size();i++){
	    Pool current = allPools.get(i);
	    if (current==null) return null;
	    current.rankPool(); //ranks all the teams in a pool
	    temp[i] = "Pool "+i+"\n"+current.toString()+"\n"; 
	}
	return temp; 
    }
	
    /**
     * Writes every pool's information to a file after each pool is been ranked. 
     * The files will contain the teams in a each pool in sorted order.
     */
    public void writeInPoolRankings (){
	try {	
	    boolean b = new File("Output Files/Pools/").mkdirs();
	    String[] temp = getInPoolRankings();
	    for (int a = 1; a < temp.length; a++) {
		File file = new File("Output Files"+File.separator+"Pools"+File.separator+"Pool"+a+".txt");
		FileWriter writer = new FileWriter(file); 
		BufferedWriter out = new BufferedWriter(writer); 
		out.write(temp[a] + "\n"); 
		out.close();
	    }
	}
	catch (Exception ex){
	    System.err.println(ex);
	}
    }
 
    /** 
     * Returns the overall rankings in a String. 
     * @return A String representing all of the teams in the tournament ordered by rank 
     */
    public String getOverallRankings (){
	for (int i=1;i<=allPools.size();i++)
	    allPools.get(i).rankPool();
	StringBuilder sb = new StringBuilder ();
	int tInEachP = (int) Math.ceil((double)allTeams.size()/(double)allPools.size());
	Pool temp = new Pool ();
	Vector <Team> tempAllTeams = new Vector <Team> ();
	int tInVector = 0;
	for (int i=0;i<=tInEachP;i++){
	    for (int j=1;j<allPools.size();j++){
		if (i != allPools.get(j).getNumTeams()){
		    Team curr = allPools.get(j).getTeams().get(i);
		    temp.addTeam(curr);
		}
	    }
	    temp.rankPool();
	    for (int k=0;k<temp.getNumTeams();k++){
		tempAllTeams.add(tInVector,temp.getTeams().get(k));
		tInVector++;
	    }
	    temp = new Pool();
	}
		
	for (int m = 0; m < tempAllTeams.size(); m++) {
	    Team current = tempAllTeams.get(m); 
	    sb.append((m+1)+" " + current.getName()+" (Pool "+current.getPool()+")" + "\n");
	}
	return sb.toString();
    }

    /** 
     * Returns all the teams in each Pool as a String, for every pool. Includes 
     * the team names.
     * @return A String representing information about a Pool object 
     */
    public String getPoolInfo(){
		
	StringBuilder sb = new StringBuilder();
	for (int i=1;i<allPools.size();i++){
	    Pool temp = allPools.get(i);
	    sb.append("Pool "+i+"\n"+temp.toString2()+"\n");
	}
	return sb.toString();
    }		
		
    /** 
     * Returns less specific team information as a String, for every team. 
     * @return A String representing information about a Team object
     */
    public String[] getTeamInfo() { 
	String[] temp = new String[allTeams.size()];
	for (int i = 0; i < allTeams.size(); i++) {
	    Team current = allTeams.get(i);  
	    temp[i] = current.toString()+"\n"; 
	} 
	return temp; 
    } 		
	
    /** 
     * Returns more specific team information as a String, for every team. 
     * @return A String representing more specific information about a Team object
     */
    public String[] getTeamInfo2() { 
	String[] temp = new String[allTeams.size()]; 
	for (int i = 0; i < allTeams.size(); i++) {
	    Team current = allTeams.get(i);      
	    temp[i] = current.toString2(); 
	} 
	return temp; 
    } 		
		
    /** 
     * Writes team information to a text file, for every team. In the end, there 
     * will be a text file for each team containing its own information. 
     */
    public void writeTeamInfo() { 
	try {
	    boolean b = new File("Output Files/Teams/").mkdirs();	
	    for (int i = 0; i < allTeams.size(); i++) {
	    	Team current = allTeams.get(i); 
	    	String name = current.getName();  	
		File file = new File("Output Files"+File.separator+"Teams"+File.separator+name+".txt"); 
		FileWriter writer = new FileWriter(file); 
		BufferedWriter out = new BufferedWriter(writer); 
		out.write(current.toString2());
		out.close();
	    }
	} catch (Exception ex) {
	    System.err.println(ex); 
	} 
    }  
    
    /**
     * Returns match information as a String, for all matches.
     * @return A String representing all of the match information in a tournament
     */
    public String getMatches() {
	StringBuilder sb = new StringBuilder();
	Enumeration <Match> matches = allMatches.keys();
	while(matches.hasMoreElements()) {
	    Match current = allMatches.get(matches.nextElement());
	    sb.append(allMatches.get(matches.nextElement()).toString() + "\n"); 
	}
	return sb.toString();
    }
	
}

