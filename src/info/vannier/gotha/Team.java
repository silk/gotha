/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.vannier.gotha;

/**
 *
 * @author Luc
 */
public class Team implements java.io.Serializable{
    private static final long serialVersionUID = Gotha.GOTHA_DATA_VERSION;
    private int teamNumber;
    private String teamName;
    private Player[] teamMembers = new Player[Gotha.MAX_NUMBER_OF_MEMBERS_BY_TEAM];

    public Team(int teamNumber, String name, int teamSize){
        this.teamNumber = teamNumber;
        this.teamName = name;
        teamMembers = new Player[teamSize];
    }

    public Team(String name, int teamSize){
        teamNumber = -1;    // Because it is not possible, here to give a team
        this.teamName = name;
        teamMembers = new Player[teamSize];
    }
    public Team(Team team){
        deepCopy(team);
    }
    
    public void deepCopy(Team t){
        this.teamNumber = t.getTeamNumber();
        this.teamName = t.getTeamName();
        for (int ib = 0; ib < Gotha.MAX_NUMBER_OF_MEMBERS_BY_TEAM; ib++){
            Player p = t.getTeamMember(ib);
            this.setTeamMember(p, ib);
        }
        
    }

    /**
     * @return the name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param name the name to set
     */
    public void setTeamName(String name) {
        this.teamName = name;
    }

    public boolean setTeamMember(Player p, int boardNumber){
        if (boardNumber >= teamMembers.length || boardNumber < 0) return false;
        else{
            teamMembers[boardNumber] = p;
            return true;
        }
    }

    public Player getTeamMember(int boardNumber){
        if (boardNumber >= teamMembers.length || boardNumber < 0) return null;
        else{
            return teamMembers[boardNumber];
        }
    }

    /**
     * @return the teamNumber
     */
    public int getTeamNumber() {
        return teamNumber;
    }

    /**
     * @param teamNumber the teamNumber to set
     */
    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void modifyTeamSize(int newTeamSize){
        Player[] newTeamMembers = new Player[newTeamSize];
        int copySize = Math.min(newTeamSize, teamMembers.length);
        System.arraycopy(teamMembers, 0, newTeamMembers, 0, copySize);
        if (newTeamSize > copySize){
            for (int ip = copySize; ip < newTeamSize; ip++){
                newTeamMembers[ip] = null;
            }
        }
        teamMembers = newTeamMembers;

    }
    public int meanRating(){
        int sum = 0;
        for (int ip = 0; ip < this.teamMembers.length; ip++){
            Player p = teamMembers[ip];
            if (p != null) sum += p.getRating();
        }
        int mean = sum/teamMembers.length;
        return mean;
    }
    public int medianRating(){
        int size = teamMembers.length;
        if (size % 2 == 1){
            Player p = teamMembers[size / 2];
            int r = 0;
            if (p != null) r = p.getRating();
            return r;
        }
        else{
        Player p1 = teamMembers[(size - 1) / 2];
        Player p2 = teamMembers[(size - 1) / 2 + 1];
        int r1 = 0;
        int r2 = 0;
        if (p1 != null) r1 = p1.getRating();
        if (p2 != null) r2 = p2.getRating();
        int median = (r1 + r2) / 2;
        return median;
        }
    }

}
