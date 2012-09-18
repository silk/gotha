package info.vannier.gotha;

/**
 *
 * @author Luc
 */
public class Team implements java.io.Serializable{
    private static final long serialVersionUID = Gotha.GOTHA_DATA_VERSION;
    private int teamNumber;
    private String teamName;
    private Player[][] teamMembers = new Player[Gotha.MAX_NUMBER_OF_ROUNDS][Gotha.MAX_NUMBER_OF_MEMBERS_BY_TEAM];;

    public Team(int teamNumber, String name, int teamSize){
        this.teamNumber = teamNumber;
        this.teamName = name;
    }

    public Team(String name, int teamSize){
        teamNumber = -1;    // Because it is not possible, here to give a team
        this.teamName = name;
    }
    public Team(Team team){
        deepCopy(team);
    }
    
    private void deepCopy(Team t){
        this.teamNumber = t.getTeamNumber();
        this.teamName = t.getTeamName();
        for (int ir = 0; ir < Gotha.MAX_NUMBER_OF_ROUNDS; ir++){
            for (int ib = 0; ib < Gotha.MAX_NUMBER_OF_MEMBERS_BY_TEAM; ib++){
                Player p = t.getTeamMember(ir, ib);
                this.setTeamMember(p, ir, ib);
            }
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

    public boolean setTeamMember(Player p, int roundNumber, int boardNumber){
        if (roundNumber >= teamMembers.length || roundNumber < 0) return false;
        if (boardNumber >= teamMembers[0].length || boardNumber < 0) return false;
        else{
            teamMembers[roundNumber][boardNumber] = p;
            return true;
        }
    }

    public Player getTeamMember(int roundNumber, int boardNumber){
        if (roundNumber >= teamMembers.length || roundNumber < 0) return null;
        if (boardNumber >= teamMembers[0].length || boardNumber < 0) return null;
       if (boardNumber >= teamMembers.length || boardNumber < 0) return null;
        else{
            return teamMembers[roundNumber][boardNumber];
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
        Player[][] newTeamMembers = new Player[teamMembers.length][newTeamSize];
        int copySize = Math.min(newTeamSize, teamMembers[0].length);
        for (int ir = 0; ir < teamMembers.length; ir++){
            System.arraycopy(teamMembers[ir], 0, newTeamMembers[ir], 0, copySize);
        }
        
        if (newTeamSize > copySize){
            for (int ir = 0; ir < teamMembers.length; ir++){
                for (int ip = copySize; ip < newTeamSize; ip++){
                    newTeamMembers[ir][ip] = null;
                }
            }
        }        
        teamMembers = newTeamMembers;
    }

    /**
     * Starting from V3.28.04 where team members may be variable, meanRating means mean rating at first round
     * @return 
     */
    public int meanRating(){
        int sum = 0;
        for (int ip = 0; ip < this.teamMembers[0].length; ip++){
            Player p = teamMembers[0][ip];
            if (p != null) sum += p.getRating();
        }
        int mean = sum/teamMembers.length;
        return mean;
    }

       /**
     * Starting from V3.28.04 where team members may be variable, medianRating means mean rating at first round
     * @return 
     */

    public int medianRating(){
        int size = teamMembers[0].length;
        if (size % 2 == 1){
            Player p = teamMembers[0][size / 2];
            int r = 0;
            if (p != null) r = p.getRating();
            return r;
        }
        else{
        Player p1 = teamMembers[0][(size - 1) / 2];
        Player p2 = teamMembers[0][(size - 1) / 2 + 1];
        int r1 = 0;
        int r2 = 0;
        if (p1 != null) r1 = p1.getRating();
        if (p2 != null) r2 = p2.getRating();
        int median = (r1 + r2) / 2;
        return median;
        }
    }

}











