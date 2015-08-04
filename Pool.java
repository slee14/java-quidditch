// Team.java
// Authors: Shirley Lu & Sojung Lee
// Purpose: The Pool Class stores the number of teams in a Pool and the Team objects representing the teams in the Pool. 
// Information: sets are not included because all information should be changed in the text file databse, not via the program; removeTeam is also not included - change in text file datbase, not via program
// Edits:
// 12.2.12 Document created, skeletons added (Shirley Lu)
// 12.2.12 Javadoc added (Sojung Lee)

import java.util.*;

/** 
 * Defines a class, Pool, to store information about a single pool within a 
 * Quidditch tournament. It keeps track of the number of teams in the pool, and
 * a Vector of all the Team objects in the pool. 
 */
public class Pool {
    private int numTeams =0; //keeps track of the number of teams in the pool 
    private Vector<Team> teams; //keeps track of all the teams in the pool 
    
    /**
     * Constructs a Pool given the number of teams participating. Initializes the
     * Vector of Teams, teams. 
     */
    public Pool () {
	teams = new Vector<Team>(); // initializing of an empty Vector of Team objects 
    }
    
    /**
     * Adds a new team to this pool. 
     * @param A Team object.
     */
    public void addTeam (Team t){
	numTeams++; //increment the number of teams in the pool 
  	teams.add(t); //add the pool to the Vector 
    }
    
    /**
     * Orders the Vector to be sorted in terms of rank. 
     */
    public void rankPool (){	
	Collections.sort(teams);
	//Implements the compareTo method
    }
    
    /** 
     * Returns a string representation of this Pool object; teams have more information.  
     */
    public String toString2(){
	String toReturn = ""; 
	if (teams.size()==0)return toReturn;
	for (int i = 0; i < teams.size(); i++) {
	    Team current = teams.get(i); 
	    toReturn += (i+1) +" "+ current.toString2() + "\n"; 
	}
	return toReturn; 
    }
	
	/** 
     * Returns a string representation of this Pool object; teams have less information.  
     */
    public String toString (){
	String toReturn = ""; 
	if (teams.size()==0)return toReturn;
	for (int i = 0; i < teams.size(); i++) {
	    Team current = teams.get(i); 
	    toReturn += (i+1) +" "+ current.getName() + "\n"; 
	}
	return toReturn; 
    }
    
    /**
     * Access and returns an integer representing the number of teams in this Pool. 
     * @return An integer representing the number of teams in a Quidditch pool.
     */
    public int getNumTeams (){
	return numTeams;
    }
    
    /**
     * Access and returns a Vector representing all the teams in this Pool. 
     * @return A Vector<Team> representing all of the teams that are a member of a Quidsditch pool. 
     */
    public Vector<Team> getTeams (){
	return teams;
    }
}
