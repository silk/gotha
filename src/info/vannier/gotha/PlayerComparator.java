package info.vannier.gotha;

import java.io.Serializable;
import java.util.Comparator;

public class PlayerComparator implements Comparator<Player>, Serializable{
    public final static int NO_ORDER = 0;
    public final static int NUMBER_ORDER = 1;
    public final static int NAME_ORDER = 2;
    public final static int RANK_ORDER = 3;
    public final static int RATING_ORDER = 4;
    public final static int AGAID_ORDER = 5;
    
    int playerOrderType = PlayerComparator.NO_ORDER;
    public PlayerComparator(int playerOrderType){
        this.playerOrderType = playerOrderType;
    }

    @Override
    public int compare(Player p1, Player p2){
        switch (playerOrderType){
            case NAME_ORDER :
                int c = p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
                if (c != 0) return c;
                else return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
            case RANK_ORDER :
                if (p1.getRank() < p2.getRank()) return 1;
                if (p1.getRank() > p2.getRank()) return -1;
                c = p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
                if (c != 0) return c;
                else return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
            case RATING_ORDER :
                if (p1.getRating() < p2.getRating()) return 1;
                if (p1.getRating() > p2.getRating()) return -1;
                c = p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase());
                if (c != 0) return c;
                else return p1.getFirstName().toLowerCase().compareTo(p2.getFirstName().toLowerCase());
            case AGAID_ORDER :
                int agaId1 = 0;
                try{
                    agaId1 = Integer.parseInt(p1.getAgaId());
                }
                catch(NumberFormatException e){
                    agaId1 = 0;
                }
                int agaId2 = 0;
                try{
                    agaId2 = Integer.parseInt(p2.getAgaId());
                }
                catch(NumberFormatException e){
                    agaId2 = 0;
                }
                if (agaId1 > agaId2) return 1;
                if (agaId1 < agaId1) return -1;
                return 0;
            default :
                return 0;
        }
    }
}

