//Team.java
//Authors: Shirley Lu & Sojung Lee
//Purpose: A Team class to store crucial information about a team, such as the team name, which pool the team belongs to, matches the teams has played in, teams the team has played, teams the team has beat, number of matches won, the win % of the team, the adjusted point differential of the team, quaffle points against and quaffle points for
//Information: setname and setpools are not included because a number of matches won, the win percentage,
//apd, quaffle defense score and quaffle offense score.
 

import java.util.*;

public class Team implements Comparable <Team>{
    //information about the team
    private String name;
    private int pool;
    private Hashtable <Match,Match> matches;
    private Hashtable <Team,Team> teamsBeat; 
    private Hashtable <Team,Team> teamsPlayed;
  
    //tiebreaker information
    private int matchesWon;
    private double winPercentage;
    private double APD; //adjusted point differential
    private int quaffleAgainst;
    private int quaffleFor;
  
    /** 
     * Constructs a Team, given its name (like "Boston University") and its pool
     * number. 
     */  
    public Team (String nName, int nPool){
	name = nName;
	pool = nPool;
	matches = new Hashtable <Match,Match>();
	teamsBeat = new Hashtable <Team,Team>();
	teamsPlayed = new Hashtable <Team,Team>();
    }
 
    /**
     * Accesses and returns the name of this Team object. 
     * @return A string representing the name of the team. 
     */
    public String getName(){
	return name;
    }
  
    /** 
     * Accesses and returns the pool number of this Team object. 
     * @return An integer representing the pool number of the team. 
     */
    public int getPool(){
	return pool;
    }
  
    /**
     * Accesses and returns the Hashtable of matches played of this Team object. 
     * @return A Hashtable containing all the match played by the team. 
     */
    public Hashtable <Match,Match> getMatches (){
	return matches;
    }
 
    /**
     * Accesses and returns the Hashtable of teams beat of this Team object. 
     * @return A Hashtable containing all the Teams beat by this team. 
     */  
    public Hashtable <Team,Team> getTeamsBeat (){
	return teamsBeat;
    }
 
    /** 
     * Adds a Team to this Team's teamsBeat hashtable. 
     * @param A Team object to be added to teamsBeat hashtable.
     */ 
    public void addTeamBeat (Team t){
	teamsBeat.put(t,t);
    }

    /**
     * Accesses and returns the Hashtable of teams played of this Team object. 
     * @return A Hashtable containing all the Teams played by this team. 
     */    
    public Hashtable <Team,Team> getTeamsPlayed (){
	return teamsPlayed;
    }
  
    /** 
     * Adds a Team to this Team's teamsPlayed hashtable. 
     * @param A Team object to be added to teamsPlayed hashtable.
     */ 
    public void addTeamPlayed (Team t){
	teamsPlayed.put(t,t);
    }

    /**
     * Accesses and returns the int representing the number of matches won by this Team object.
     * @return A int representing matches won by this team. 
     */    
    public int getMatchesWon (){
	return matchesWon;
    }
 
    /**
     * Sets the number of matches won by this Team object.
     * @param matchesWon is updated
     */ 
    public void setMatchesWon (int i){
	matchesWon =i;
    }
  
    /**
     * Accesses and returns the double representing the win Percentage this Team object.
     * @return A double representing win percentage of this team. 
     */    
    public double getWinPercentage (){
	return winPercentage;
    }

    /**
     * Sets the win percentage of this Team object.
     * @param winPercentage is updated
     */   
    public void setWinPercentage (double d){
	winPercentage =d;
    }
  
    /**
     * Accesses and returns the double representing the adjusted point differential of this Team object.
     * @return A double representing adjusted point differential of this team. 
     */    
    public double getAPD (){
	return APD;
    }

    /**
     * Sets the adjusted point differential of this Team object.
     * @param APD is updated
     */    
    public void setAPD (double d){
	APD = d;
    }
 
    /**
     * Accesses and returns the int representing the quaffle points against of this Team object.
     * Quaffle Points Against are used to rank and judge how well a team's defense is
     * @return A int representing quaffle points against of this team. 
     */     
    public int getQAgainst (){
	return quaffleAgainst;
    }

    /**
     * Sets the quaffle points against of this Team object.
     * @param qAgainst is updated
     */    
    public void setQAgainst (int i){
	quaffleAgainst = i;
    }
    /**
     * Accesses and returns the int representing the quaffle points for of this Team object.
     * Quaffle Points Against are used to rank and judge how well a team's offense is
     * @return A int representing quaffle points for of this team. 
     */  
    public int getQFor (){
	return quaffleFor;
    }

    /**
     * Sets the quaffle points for of this Team object.
     * @param qFor is updated
     */    
    public void setQFor (int i){
	quaffleFor = i;
    } 
  
    /** 
     * Adds a Match to this Team's matches hashtable. 
     * @param A Match object to be added to matches Hashtable.
     */
    public void addMatch (Match nMatch){
	matches.put(nMatch,nMatch);
    }
  
    /** 
     * Compares two teams - if both the team name and pool number is equal, returns true.
     * @return A boolean result, true if the teams are equal and false if otherwise.
     * @param A Team object to compare this Team to. 
     */
    public boolean equals(Team other){
	return (other.getName().equals(name) && (other.getPool()==pool));
    }
  
    /** 
     * Compares two teams - returns positive number if lower ranked. The comparison is based on the tiebreaker information
     * of the two compared teams. 
     * @return An integer, negative if other is ranked higher than this Team, 0 if they are ranked the same, and 1 if other is ranked lower. 
     * @param A Team object to compare this Team to. 
     */
    public int compareTo(Team other){
	updateWP();
	if (winPercentage > other.getWinPercentage())
	    return -1;
	else if (winPercentage < other.getWinPercentage())
	    return 1;
	else if (teamsBeat.get(other)!= null)
	    return -1;
	else if (other.getTeamsBeat().get(this) != null)
	    return 1;
	else if (APD != other.getAPD ())
	    return (int) Math.ceil(other.getAPD()-APD);
	else if (quaffleFor != other.getQFor())
	    return (other.getQFor()-quaffleFor);
	else if (quaffleAgainst != other.getQAgainst())
	    return (other.getQAgainst()-quaffleAgainst);
	else
	    return 0;
    }
  
    /**
     * Updates the win percentage of this Team object.
     */
    public void updateWP (){
	winPercentage = (double)matchesWon/(double)matches.size();
    }
  
    /** 
     * Returns a string representation of this Team object's name and pool number
     */
    public String toString(){
	//team file information
	return pool+","+name;
    }    
    /**
     * Returns a String with all of the team's information:
     * win percentage, apd, etc.
     */
    public String toString2(){
	StringBuilder sb = new StringBuilder ("Team Name: "+name+"\n");
	sb.append("Pool: "+pool+"\n");
	sb.append("Win %: "+winPercentage+"\n");
	sb.append("APD/#matches: "+APD/matches.size()+"\n");
	sb.append("Quaffle Points For/#matches: "+(double)quaffleFor/(double)matches.size()+"\n");
	sb.append("Quaffle Points Against/#matches: "+(double)quaffleAgainst/(double)matches.size()+"\n");
	return sb.toString();
    }
}
