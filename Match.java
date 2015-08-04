// Match.java
// Authors: Shirley Lu & Sojung Lee
// Purpose: The Match class stores information about a match. This includes the 2 teams in the match, the scores of the match, the snitch snatches of the match, whether the match went into overtime, and who won the match.
// Information: sets are not included because all information should be changed in the text file databse, not via the program
// Edits:
// 12.2.12 Document created, skeletons added (Shirley Lu)
// 12.2.12 Javadoc added (Sojung Lee)
// 12.2.12 Code completed (Shirley Lu, Sojung Lee)
 
/** 
 * Defines a class, Match, to store information about a single quidditch match.
 * It keeps track of the score of the match, the teams playing, which team catches
 * the snitch (if ever), if the match went into overtime (overtime1 and overtime2), 
 * and finally, the team that won the match.
 */ 
public class Match {
    private Team [] teams; //the teams that are playing in the match
    private int [][] scores;  // the score of the match, for each time period if needed 
    private boolean [] snatches; //if true, teams[0] caught snitch; if false, teams[1] caught snitch
    private boolean [] overtime; //did the match go into overtime 1 (overtime[1]=true)? did the match go into overtime 2 (overtime[2]=true)?
    private Team whoWon;
		
    /**
     * Constructs a Match, given the teams playing, the score of the match, 
     * a boolean array representing which team caught the snitch, and a boolean
     * array representing if the game went into overtime. 
     */ 
    public Match (Team[]nTeams, int[][]nScores, boolean[]nSnatches, boolean[]nOvertime){
	teams = nTeams;
	scores = nScores;
	snatches = nSnatches;
	overtime = nOvertime;		
	analyze();
    }
		
    /**
     * Analyzes the match information and changes whoWon accordingly
     * Invokes updateTeams() to update the stats of the teams. 
     */
    //determines who won the match and calls the update teams method to update their stats
    public void analyze (){
	if (overtime[1] && overtime[2]){
	    if (!analyzeHelper(2))
		System.out.println ("Score input error. Overtime2 true; scores are tied or empty");	
	}
	else if (overtime[1] && !overtime[2]){
	    if (!analyzeHelper(1))
		System.out.println ("Score input error. Overtime2 true; Overtime1 true; scores are tied or empty");
	}
	else if (!overtime[1] && !overtime[2]){
	    if (!analyzeHelper(0))
		System.out.println ("Score input error. Overtime2 false; Overtime1 false; scores are tied or empty");
	}
	else
	    System.out.println("Score input error.");	
				
	updateTeams();
		
    }

    /**
     * A helper method that analyzes the match information and changes whoWon accordingly
     * @param An integer that represents the first array index of the 2D scores[][]
     */
    private boolean analyzeHelper(int n){
	if (scores[n][0]>scores[n][1])
	    whoWon = teams[0];
	else if (scores[n][1]>scores[n][0])
	    whoWon = teams[1];
	else 
	    return false;
	return true;
    }
		
    /** 
     * Updates teams[0] and teams[1]'s statistics - matchesPlayed, matchesWon, teamsBeat, etc.
     */
    //updates team stats (teamsBeat, teamsPlayed, matchesWon,win%, apd, qAgasint, qFor)
    public void updateTeams(){
	updateTeam(0);
	updateTeam(1);
	updateWonBeat();			
	updateQuafflePoints();
	updateAPD();
    }
	
    /** 
     * Adds a Match to the teams array, adds the team played, as well as updates the winning percentage
     * @param An integer representing the index of the Team[] teams
     */
    private void updateTeam(int n){
	teams[n].addMatch(this); 
	teams[n].addTeamPlayed(teams[1-n]);
	teams[n].updateWP();
    }

    /**
     * Updates the stats of the team that won. 
     */
    private void updateWonBeat(){
	//updating matchesWon int and teamsbeat hashtable
	if (teams[0].equals(whoWon)){
	    teams[0].setMatchesWon(teams[0].getMatchesWon()+1);
	    teams[0].addTeamBeat(teams[1]);
	}
	else if (teams[1].equals(whoWon)){
	    teams[1].setMatchesWon(teams[1].getMatchesWon()+1);
	    teams[1].addTeamBeat(teams[0]);
	}
	else
	    System.out.println("updating teamBeat & matchesWon - whoWon error");
    }

    /** 
     * Updates the quaffle points (for and against) for both teams 
     */
    private void updateQuafflePoints(){
	int forA = 0;
	int forB = 0;
	if (overtime[2]){
	    analyzeHelper(2);
	}
	if (overtime[1]){
	    if (!analyzeHelper(1))
		System.out.println("pdating qFor and qAgainst - snatch array error");
	}
	else if (!analyzeHelper(0)){
	    System.out.println("here updating qFor and qAgainst - snatch array error");
	}
	teams[0].setQFor(teams[0].getQFor()+forA);
	teams[1].setQFor(teams[1].getQFor()+forB);
	teams[0].setQAgainst(teams[0].getQAgainst()+forB);
	teams[1].setQAgainst(teams[1].getQAgainst()+forA);
    }
	
    /** 
     * Updates the adjusted point differential of both teams 
     */
    private void updateAPD(){
	double APD = 0.0;
	if (overtime[2] && overtime[1])
	    APD = (double) Math.abs(scores[2][0]-scores[2][1]);
	else if (!overtime[2] && overtime[1])
	    APD = (double) Math.abs(scores[1][0]-scores[1][1]);
	else if (!overtime[2] && !overtime[1])
	    APD = (double) Math.abs(scores[0][0]-scores[0][1]);
	if (APD > 80.0)
	    APD = 80.0+Math.sqrt(APD-80.0);
	if (teams[0].equals(whoWon)){
	    teams[0].setAPD(teams[0].getAPD()+APD);
	    teams[1].setAPD(teams[1].getAPD()-APD);
	}
	else if (teams[1].equals(whoWon)){
	    teams[0].setAPD(teams[0].getAPD()-APD);
	    teams[1].setAPD(teams[1].getAPD()+APD);
	}
	else
	    System.out.println ("updating APD - whoWon error");
    }
	
    /** 
     * A helper method used in 
     */
    private boolean updateHelper(int n){
	int forA = 0, forB=0;
	if (overtime[n]){
	    if (snatches[n]){
		forA+=(scores[n][0]-30)/10;
		forB+=(scores[n][1])/10;
	    }
	    else if (!snatches[n]) {
		forA+=(scores[n][0])/10;
		forB+=(scores[n][1]-30)/10;
	    }
	    else if (n==2){
		forA+=(scores[n][0])/10;
		forB+=(scores[n][1])/10;
	    }
	    else 
		return false;
	}
	return true;
    }
		
    /** 
     * Returns a string representation of this Match object. 
     */
    public String toString (){
	StringBuilder s = new StringBuilder (teams[0].getName()+","+teams[1].getName()+",");
	if (snatches[0])
	    s.append(scores[0][0]+"*,"+scores[0][1]);
	else 
	    s.append(scores[0][0]+","+scores[0][1]+"*");
	if (overtime[1]){
	    if (snatches[1])
		s.append(","+scores[1][0]+"*,"+scores[1][1]);
	    else 
		s.append(","+scores[1][0]+","+scores[1][1]+"*");
	}
	if (overtime[2]){
	    if (snatches[2])
		s.append(","+scores[2][0]+"*,"+scores[2][1]);
	    else 
		s.append(","+scores[2][0]+","+scores[2][1]+"*");
	}
    	return s.toString();
    }
		
    /** 
     * Access and returns a Team array, representing the two Teams that are 
     * involved in a Quidditch match with each other. 
     * @return A Team[] of two Team objects.
     */
    public Team[] getTeams(){
    	return teams;
    }
		
    /**
     * Access and returns a 2D integer array, representing the scores of the 
     * Quidditch Match.
     * @return An int[][] of two integers. 
     */
    public int[][] getScores(){
    	return scores;
    }
		
    /** 
     * Access and returns a boolean array. If snatches[0] is true, that means that 
     * Team 1 caught the snitch. If snatches[1] is true, that means that Team 2 
     * caught the snitch. If both snatches[0] and snatches[1] are false, that means
     * that neight team caught the snitch.
     * @return A boolean array of two slots, with each slot representing a team. 
     */
    public boolean[] getSnatches(){
    	return snatches;
    }
		
    /**
     * Access and returns a boolean array. If overtime[0] is true, that means that 
     * the match was tied, and the teams went into the the first section of overtime for 5 minutes. 
     * If overtime[1] is true, that means that the match was tied after the first overtime,
     * and the teams went into the second and final section of overtime, with the first 
     * team to score winning the match.
     * @return A boolean array of two slots, with the first slot representing the first overtime, and 
     * with the second slot representing the second overtime.
     */
    public boolean[] getOvertime (){
    	return overtime;
    }
		
    /**
     * Access and returns the Team object that won the match. 
     * @return The winning team.
     */
    public Team getWinner (){
    	return whoWon;
    }
}
