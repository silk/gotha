/*
 * TournamentPrinting.java
 */
package info.vannier.gotha;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * TournamentPrinting manages printing jobs.
 * @author LV
 */
public class TournamentPrinting implements Printable {

    static final int TYPE_DEFAULT = 0;
    static final int TYPE_PLAYERSLIST = 1;
    static final int TYPE_TEAMSLIST = 2;
    static final int TYPE_GAMESLIST = 11;
    static final int TYPE_NOTPLAYINGLIST = 12;    
    static final int TYPE_MATCHESLIST = 13;
    static final int TYPE_STANDINGS = 21;
    static final int TYPE_TEAMSSTANDINGS = 22;
    static final int TYPE_TOURNAMENT_PARAMETERS = 101;
    static final int SUBTYPE_DEFAULT = 0;
    static final int SUBTYPE_ST_CAT = 1;    // Standings by cat
    // PL = Players List
    static final int PL_NUMBER_BEG = 0;
    static final int PL_NUMBER_LEN = 4;
    static final int PL_PINLIC_BEG = PL_NUMBER_BEG + PL_NUMBER_LEN + 1;
    static final int PL_PINLIC_LEN = 8;
    static final int PL_NF_BEG = PL_PINLIC_BEG + PL_PINLIC_LEN + 1;
    static final int PL_NF_LEN = 25;
    static final int PL_COUNTRY_BEG = PL_NF_BEG + PL_NF_LEN + 1;
    static final int PL_COUNTRY_LEN = 2;
    static final int PL_CLUB_BEG = PL_COUNTRY_BEG + PL_COUNTRY_LEN + 1;
    static final int PL_CLUB_LEN = 4;
    static final int PL_RANK_BEG = PL_CLUB_BEG + PL_CLUB_LEN + 1;
    static final int PL_RANK_LEN = 3;
    static final int PL_RT_BEG = PL_RANK_BEG + PL_RANK_LEN + 1;
    static final int PL_RT_LEN = 4;
    static final int PL_MM_BEG = PL_RT_BEG + PL_RT_LEN + 1;
    static final int PL_MM_LEN = 3;
    static final int PL_PART_BEG = PL_MM_BEG + PL_MM_LEN + 1;
    static final int PL_PART_LEN = Gotha.MAX_NUMBER_OF_ROUNDS;
    static final int PL_PADDING = 0;
    static final int PL_NBCAR = PL_PART_BEG + PL_PART_LEN + PL_PADDING; 
    // TL = Teams List
    static final int TL_NUMBER_BEG = 0;
    static final int TL_NUMBER_LEN = 4;
    static final int TL_TEAMNAME_BEG = TL_NUMBER_BEG + TL_NUMBER_LEN + 1;
    static final int TL_TEAMNAME_LEN = 20;
    static final int TL_BOARD_BEG = TL_TEAMNAME_BEG + TL_TEAMNAME_LEN + 1;
    static final int TL_BOARD_LEN = 2;
    static final int TL_NF_BEG = TL_BOARD_BEG + TL_BOARD_LEN + 1;
    static final int TL_NF_LEN = 25;
    static final int TL_RATING_BEG = TL_NF_BEG + TL_NF_LEN + 1;
    static final int TL_RATING_LEN = 4;
    static final int TL_COUNTRY_BEG = TL_RATING_BEG + TL_RATING_LEN + 1;
    static final int TL_COUNTRY_LEN = 2;
    static final int TL_CLUB_BEG = TL_COUNTRY_BEG + TL_COUNTRY_LEN + 1;
    static final int TL_CLUB_LEN = 4;
    static final int TL_PADDING = 2;
    static final int TL_NBCAR = TL_CLUB_BEG + TL_CLUB_LEN + TL_PADDING; //  67
    // GL = Games List
    static final int GL_TN_BEG = 0; // Table Number
    static final int GL_TN_LEN = 4;
    static final int GL_WNF_BEG = GL_TN_BEG + GL_TN_LEN + 1;
    static final int GL_WNF_LEN = 33;       // 22 + 1 + 2 + 3 + 1 + 4
    static final int GL_BNF_BEG = GL_WNF_BEG + GL_WNF_LEN + 1;
    static final int GL_BNF_LEN = 32;
    static final int GL_HD_BEG = GL_BNF_BEG + GL_BNF_LEN + 1;
    static final int GL_HD_LEN = 1;
    static final int GL_RES_BEG = GL_HD_BEG + GL_HD_LEN + 1;
    static final int GL_RES_LEN = 3;
    static final int GL_PADDING = 2;
    static final int GL_NBCAR = GL_RES_BEG + GL_RES_LEN + GL_PADDING;
    // Not playing list
    static final int NPL_REASON_BEG = 0;
    static final int NPL_REASON_LEN = 20;
    static final int NPL_NF_BEG = NPL_REASON_BEG + NPL_REASON_LEN + 1;
    static final int NPL_NF_LEN = 25;
    static final int NPL_RANK_BEG = NPL_NF_BEG + NPL_NF_LEN + 1;
    static final int NPL_RANK_LEN = 3;
    static final int NPL_PADDING = 30;
    static final int NPL_NBCAR = NPL_RANK_BEG + NPL_RANK_LEN + NPL_PADDING; // 87
    // ML = Matches List
    static final int ML_TN_BEG = 5; // Table Number
    static final int ML_TN_LEN = 7;
    static final int ML_WTN_BEG = ML_TN_BEG + ML_TN_LEN + 1;
    static final int ML_WTN_LEN = 20;
    static final int ML_BTN_BEG = ML_WTN_BEG + ML_WTN_LEN + 1;
    static final int ML_BTN_LEN = 20;
    static final int ML_RES_BEG = ML_BTN_BEG + ML_BTN_LEN + 1;
    static final int ML_RES_LEN = 3;
    static final int ML_PADDING = 5;
    static final int ML_NBCAR = ML_RES_BEG + ML_RES_LEN + ML_PADDING;
    // ST = Standings
    static final int ST_NUM_BEG = 0;
    static final int ST_NUM_LEN = 4;
    static final int ST_PL_BEG = ST_NUM_BEG + ST_NUM_LEN + 1;
    static final int ST_PL_LEN = 4;
    static final int ST_NF_BEG = ST_PL_BEG + ST_PL_LEN + 1;
    static final int ST_NF_LEN = 22;
    static final int ST_RK_BEG = ST_NF_BEG + ST_NF_LEN + 1;
    static final int ST_RK_LEN = 3;
    static final int ST_NBW_BEG = ST_RK_BEG + ST_RK_LEN + 1;
    static final int ST_NBW_LEN = 3;
    static final int ST_ROUND0_BEG = ST_NBW_BEG + ST_NBW_LEN + 1;
    static final int ST_ROUND_LEN = 7;
    static final int ST_CRIT_LEN = 6;
    static final int ST_PADDING = 1;
    static final int ST_NBFXCAR = ST_ROUND0_BEG + ST_PADDING;  // at runtime, numberOfCharactersInALine will be computed by adding round and crit infos
    // TST = Team Standings
    static final int TST_NUM_BEG = 0;
    static final int TST_NUM_LEN = 4;
    static final int TST_PL_BEG = TST_NUM_BEG + TST_NUM_LEN + 1;
    static final int TST_PL_LEN = 4;
    static final int TST_TN_BEG = TST_PL_BEG + TST_PL_LEN + 1;
    static final int TST_TN_LEN = 22;
    static final int TST_ROUND0_BEG = TST_TN_BEG + TST_TN_LEN + 1;
    static final int TST_ROUND_LEN = 8;
    static final int TST_CRIT_LEN = 6;
    static final int TST_PADDING = 1;
    static final int TST_NBFXCAR = ST_ROUND0_BEG + ST_PADDING;  // at runtime, numberOfCharactersInALine will be computed by adding round and crit infos
    // TP = Tournament Parameters
    static final int TP_TAB1 = 6;
    static final int TP_TAB2 = 12;
    static final int TP_TAB3 = 18;
    static final int TP_TAB4 = 24;
    static final int TP_NBCAR = 80;
    static final int WH_RATIO = 50;          // Width/Height ratio (%)
    static final int LINEFILLING_RATIO = 90; // Line filling ratio (%)
    static final int LHFS_RATIO = 140;       // Line Height/Font Size ratio (%)
    TournamentInterface tournament;
    int printType;
    int printSubType;
    /** from 0 to ... */
    private int roundNumber = -1;
    // For PlayersList and NotPlayingList
    ArrayList<Player> alPlayersToPrint;
    // For TeamsList
    ArrayList<Team> alTeamsToPrint;
    // For Standings
    private ArrayList<ScoredPlayer> alOrderedScoredPlayers;
    private String[][] halfGamesStrings;
    private int[] printCriteria;
    String[] strPlace;
    // For TeamsStandings
    private ScoredTeamsSet scoredTeamsSet;
    PrinterJob printerJob;
    PageFormat pageFormat;
    // These variables are computed by print method at first call    
    // Upper_Left coordinates, width and height of the usable printing page area
    int usableX = -1;
    int usableY = -1;
    int usableWidth = -1;
    int usableHeight = -1;
    int fontSize;
    int lineHeight;
    int numberOfBodyLinesInAPage;
    int numberOfPages;
    int numberOfCharactersInALine;

    public TournamentPrinting(TournamentInterface tournament) {
         this.tournament = tournament;

        printerJob = PrinterJob.getPrinterJob();
        pageFormat = new PageFormat();

        Paper paper = new Paper();
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());  // forces Imageable to maximum
        pageFormat.setPaper(paper);
        printerJob.setPrintable(this, pageFormat);
    }

    public void makePrinting(int printType, int printSubType, boolean askForPrinter) {
        this.printType = printType;
        this.printSubType = printSubType;
        switch (printType) {
            case TournamentPrinting.TYPE_PLAYERSLIST:
                int playersSortType = printSubType;
                try {
                    alPlayersToPrint = new ArrayList<Player>(tournament.playersList());
                } catch (RemoteException ex) {
                    Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                }
                PlayerComparator playerComparator = new PlayerComparator(playersSortType);
                Collections.sort(alPlayersToPrint, playerComparator);
                break;
            case TournamentPrinting.TYPE_NOTPLAYINGLIST:
                playersSortType = printSubType;
                playerComparator = new PlayerComparator(playersSortType);
                alPlayersToPrint = new ArrayList<Player>();
                try {
                    // Bye player
                    Player bP = tournament.getByePlayer(roundNumber);
                    if (bP!= null) alPlayersToPrint.add(bP);
                    // Not paired players
                    ArrayList<Player> alNotPairedPlayers = tournament.alNotPairedPlayers(roundNumber);
                    if (alNotPairedPlayers != null){
                        Collections.sort(alNotPairedPlayers, playerComparator);
                        alPlayersToPrint.addAll(alNotPairedPlayers);                       
                    }
                    // Not participating players
                    ArrayList<Player> alNotParticipatingPlayers = tournament.alNotParticipantPlayers(roundNumber);
                    if (alNotParticipatingPlayers != null){
                        Collections.sort(alNotParticipatingPlayers, playerComparator);
                        alPlayersToPrint.addAll(alNotParticipatingPlayers);
                    }
                    // Not FIN Reg
                    ArrayList<Player> alNotFINRegisteredPlayers = tournament.alNotFINRegisteredPlayers();
                    if (alNotFINRegisteredPlayers != null){
                        Collections.sort(alNotFINRegisteredPlayers, playerComparator);
                        alPlayersToPrint.addAll(alNotFINRegisteredPlayers);                       
                    }                  

                } catch (RemoteException ex) {
                    Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case TournamentPrinting.TYPE_TEAMSLIST:
                int teamsSortType = printSubType;
                try {
                    alTeamsToPrint = new ArrayList<Team>(tournament.teamsList());
                } catch (RemoteException ex) {
                    Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                }
                TeamComparator teamComparator = new TeamComparator(teamsSortType);
                Collections.sort(alTeamsToPrint, teamComparator);
                break;
            case TournamentPrinting.TYPE_STANDINGS:
                int numberOfRoundsPrinted = roundNumber + 1;
                int numberOfCriteriaPrinted = printCriteria.length;
                numberOfCharactersInALine = ST_NBFXCAR + numberOfRoundsPrinted * ST_ROUND_LEN
                        + numberOfCriteriaPrinted * ST_CRIT_LEN;
                TournamentParameterSet tps = null;
                try {
                    tps = tournament.getTournamentParameterSet();
                } catch (RemoteException ex) {
                    Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                }
                TournamentParameterSet printTPS = new TournamentParameterSet(tps);
                PlacementParameterSet printPPS = printTPS.getPlacementParameterSet();
                printPPS.setPlaCriteria(this.printCriteria);

                strPlace = ScoredPlayer.catPositionStrings(alOrderedScoredPlayers, roundNumber, printTPS);
                halfGamesStrings = ScoredPlayer.halfGamesStrings(alOrderedScoredPlayers, roundNumber, printTPS);
                break;
            case TournamentPrinting.TYPE_TEAMSSTANDINGS:
                numberOfRoundsPrinted = roundNumber + 1;
                // Build printCriteria
                int nbCrit = 0;
                for (int ic = 0; ic < TeamPlacementParameterSet.TPL_MAX_NUMBER_OF_CRITERIA; ic++) {
                    int crit = scoredTeamsSet.getTeamPlacementCriterion(ic).uid;
                    if (crit == TeamPlacementParameterSet.TPL_CRIT_NUL) {
                        continue;
                    } else {
                        nbCrit++;
                    }
                }
                printCriteria = new int[nbCrit];
                int ipc = 0;
                for (int ic = 0; ic < TeamPlacementParameterSet.TPL_MAX_NUMBER_OF_CRITERIA; ic++) {
                    int crit = scoredTeamsSet.getTeamPlacementCriterion(ic).uid;
                    if (crit == TeamPlacementParameterSet.TPL_CRIT_NUL) {
                        continue;
                    } else {
                        printCriteria[ipc++] = crit;
                    }
                }

                numberOfCriteriaPrinted = printCriteria.length;
                numberOfCharactersInALine = TST_NBFXCAR + numberOfRoundsPrinted * TST_ROUND_LEN
                        + numberOfCriteriaPrinted * TST_CRIT_LEN;
        }
        

        if (!askForPrinter || printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pi) {
        TournamentParameterSet tps = null;
        try {
            tps = tournament.getTournamentParameterSet();
        } catch (RemoteException ex) {
            Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
        }
        GeneralParameterSet gps = tps.getGeneralParameterSet();

        if (usableX < 0) {          // if it is the first call to print
            usableX = (int) pf.getImageableX() + 1;
            usableY = (int) pf.getImageableY() + 1;
            usableWidth = (int) pf.getImageableWidth() - 2;
            usableHeight = (int) pf.getImageableHeight() - 2;

            switch (printType) {
                case TYPE_DEFAULT:
                    fontSize = 12;
                    lineHeight = fontSize * LHFS_RATIO;
                    break;
                case TYPE_PLAYERSLIST:
                    int nbCarRef = PL_NBCAR;
                    fontSize = usableWidth / nbCarRef * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
                    try {
                        int numberOfBodyLines = tournament.numberOfPlayers();
                        numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;
                        numberOfPages = (numberOfBodyLines + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
                    } catch (RemoteException ex) {
                        Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case TYPE_NOTPLAYINGLIST:
                    nbCarRef = NPL_NBCAR;
                    fontSize = usableWidth / nbCarRef * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
//                    try {
                    {    
                    int numberOfBodyLines = this.alPlayersToPrint.size();//tournament.gamesList(roundNumber).size();
                        numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;
                        numberOfPages = (numberOfBodyLines + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
//                    } catch (RemoteException ex) {
//                        Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    }
                    break;
                case TYPE_TEAMSLIST:
                    nbCarRef = TL_NBCAR;
                    fontSize = usableWidth / nbCarRef * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
                    try {
                        int teamSize = tournament.getTeamTournamentParameterSet().getTeamGeneralParameterSet().getTeamSize();
                        int numberOfBodyLines = tournament.numberOfTeams() * teamSize;

                        numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;
                        numberOfBodyLinesInAPage = (numberOfBodyLinesInAPage / teamSize) * teamSize;
                        numberOfPages = (numberOfBodyLines + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
                    } catch (RemoteException ex) {
                        Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case TYPE_GAMESLIST:
                    nbCarRef = GL_NBCAR;
                    fontSize = usableWidth / nbCarRef * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
                    try {
                        int numberOfBodyLines = tournament.gamesList(roundNumber).size();
                        numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;
                        numberOfPages = (numberOfBodyLines + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
                    } catch (RemoteException ex) {
                        Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case TYPE_MATCHESLIST:
                    nbCarRef = ML_NBCAR;
                    fontSize = usableWidth / nbCarRef * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
                    try {
                        int numberOfBodyLines = tournament.gamesList(roundNumber).size();
                        numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;
                        numberOfPages = (numberOfBodyLines + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
                    } catch (RemoteException ex) {
                        Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case TYPE_STANDINGS:
                    fontSize = usableWidth / this.numberOfCharactersInALine * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
                    numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;

                    int numberOfBodyLines = this.alOrderedScoredPlayers.size();
                    numberOfPages = (numberOfBodyLines + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
                    if (this.printSubType == TournamentPrinting.SUBTYPE_ST_CAT) {
                        numberOfPages = 0;
                        for (int numCat = 0; numCat < gps.getNumberOfCategories(); numCat++) {
                            int nbPl = 0;
                            try {
                                nbPl = tournament.numberOfPlayersInCategory(numCat, alOrderedScoredPlayers);
                            } catch (RemoteException ex) {
                                Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            numberOfPages += (nbPl + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
                        }
                    }
                    break;
                case TYPE_TEAMSSTANDINGS:
                    fontSize = usableWidth / this.numberOfCharactersInALine * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
                    numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;

                    numberOfBodyLines = scoredTeamsSet.getOrderedScoredTeamsList().size();
                    numberOfPages = (numberOfBodyLines + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
                    break;
                case TYPE_TOURNAMENT_PARAMETERS:
                    nbCarRef = TP_NBCAR;
                    fontSize = usableWidth / nbCarRef * 100 / WH_RATIO * LINEFILLING_RATIO / 100;
                    lineHeight = fontSize * LHFS_RATIO / 100;
                    numberOfBodyLinesInAPage = (usableHeight / lineHeight) - 5;
                    numberOfPages = 2;
                    break;
            }

        }

        try {
            switch (printType) {
                case TYPE_DEFAULT:
                    return printADefaultPage(g, pf, pi);
                case TYPE_PLAYERSLIST:
                    return printAPageOfPlayersList(g, pf, pi);
                case TYPE_NOTPLAYINGLIST:
                    return printAPageOfNotPlayingPlayersList(g, pf, pi);
                case TYPE_TEAMSLIST:
                    return printAPageOfTeamsList(g, pf, pi);
                case TYPE_GAMESLIST:
                    return printAPageOfGamesList(g, pf, pi);
                case TYPE_MATCHESLIST:
                    return printAPageOfMatchesList(g, pf, pi);
                case TYPE_STANDINGS:
                    return printAPageOfStandings(g, pf, pi);
                case TYPE_TEAMSSTANDINGS:
                    return printAPageOfTeamsStandings(g, pf, pi);
                case TYPE_TOURNAMENT_PARAMETERS:
                    return printAPageOfTournamentParameters(g, pf, pi);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
        }
        return PAGE_EXISTS;
    }

    private int printAPageOfPlayersList(Graphics g, PageFormat pf, int pi) throws RemoteException {
        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Players List";
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);

        // Header Line
        printPlayersListHeaderLine(g, pf, pi);

        // Body lines
        int ln = 0;
        for (ln = 0; ln < numberOfBodyLinesInAPage; ln++) {
            int abstractLineNumber = ln + pi * numberOfBodyLinesInAPage;
            int playerNumber = abstractLineNumber;
            if (playerNumber >= alPlayersToPrint.size()) {
                break;
            }
            Player player = alPlayersToPrint.get(playerNumber);
            y = usableY + (4 + ln) * lineHeight;
            if ((ln % 2) == 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(usableX, y - lineHeight + 4, usableWidth, lineHeight);  // + 4 to keep leading part unfilled
                g.setColor(Color.BLACK);
            }
            if (player.getRegisteringStatus().compareTo("FIN") == 0) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.RED);
            }
            String strNumber = "" + (playerNumber + 1);
            x = usableX + usableWidth * (PL_NUMBER_BEG + PL_NUMBER_LEN) / PL_NBCAR;
            drawRightAlignedString(g, strNumber, x, y);

            String strPinLic = player.getEgfPin();
            if (strPinLic.length() == 0) {
                strPinLic = player.getFfgLicence();
            }
            if (strPinLic.length() == 0){
                strPinLic = player.getAgaId();
            }
            if (strPinLic.length() == 0) {
                strPinLic = "--------";
            }

            x = usableX + usableWidth * PL_PINLIC_BEG / PL_NBCAR;
            Font fontCourier = new Font("Courier New", Font.BOLD, fontSize);
            g.setFont(fontCourier);
            g.drawString(strPinLic, x, y);
            g.setFont(font);    // back to default

            String strName = player.getName();
            String strFirstName = player.getFirstName();
            if (strName.length() > 20) {
                strName = strName.substring(0, 20);
            }
            String strNF = strName + " " + strFirstName;
            if (strNF.length() > 25) {
                strNF = strNF.substring(0, 25);
            }
            if (player.getRegisteringStatus().compareTo("PRE") == 0) {
                strNF += "(P)";
            }
            x = usableX + usableWidth * PL_NF_BEG / PL_NBCAR;
            g.drawString(strNF, x, y);

            String strRk = Player.convertIntToKD(player.getRank());
            x = usableX + usableWidth * (PL_RANK_BEG + PL_RANK_LEN) / PL_NBCAR;
            drawRightAlignedString(g, strRk, x, y);
            String strRt = "" + player.getRating();
            x = usableX + usableWidth * (PL_RT_BEG + PL_RT_LEN) / PL_NBCAR;
            drawRightAlignedString(g, strRt, x, y);

            String strMM = "  ";
            strMM = "" + player.smms(tournament.getTournamentParameterSet().getGeneralParameterSet());
            x = usableX + usableWidth * (PL_MM_BEG + PL_MM_LEN) / PL_NBCAR;
            drawRightAlignedString(g, strMM, x, y);

            String strCountry = player.getCountry();
            strCountry = Gotha.leftString(strCountry, 2);
            x = usableX + usableWidth * PL_COUNTRY_BEG / PL_NBCAR;
            g.drawString(strCountry, x, y);

            String strClub = player.getClub();
            strClub = Gotha.leftString(strClub, 4);
            x = usableX + usableWidth * PL_CLUB_BEG / PL_NBCAR;
            g.drawString(strClub, x, y);

            String strPart = Player.convertParticipationToString(player, tournament.getTournamentParameterSet().getGeneralParameterSet().getNumberOfRounds());
            x = usableX + usableWidth * PL_PART_BEG / PL_NBCAR;
            fontCourier = new Font("Courier New", Font.BOLD, fontSize);
            g.setFont(fontCourier);
            g.drawString(strPart, x, y);
            // Come back to default Color
            g.setFont(font);    // back to default            

            g.setColor(Color.BLACK);
        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

        if (ln == 0) {
            return NO_SUCH_PAGE;
        }
        return PAGE_EXISTS;
    }

    private int printAPageOfNotPlayingPlayersList(Graphics g, PageFormat pf, int pi) throws RemoteException {
        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Players not playing in round " + (roundNumber + 1);
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);

        // Header Line
        printNotPlayingPlayersListHeaderLine(g, pf, pi);

        // Body lines
        int ln;
        for (ln = 0; ln < numberOfBodyLinesInAPage; ln++) {
            int abstractLineNumber = ln + pi * numberOfBodyLinesInAPage;
            int playerNumber = abstractLineNumber;
            if (playerNumber >= alPlayersToPrint.size()) {
                break;
            }
            Player player = alPlayersToPrint.get(playerNumber);
            y = usableY + (4 + ln) * lineHeight;
            if ((ln % 2) == 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(usableX, y - lineHeight + 4, usableWidth, lineHeight);  // + 4 to keep leading part unfilled
                g.setColor(Color.BLACK);
            }

            String strReason;
            if (!player.getRegisteringStatus().equals("FIN")){
                strReason = "No Final Registration";
            }
            else if (player.hasSameKeyString(tournament.getByePlayer(roundNumber))){
                strReason = "Bye player";
            }
            else if (!player.getParticipating(roundNumber)){
                strReason = "Not participating";
            }
            else{
                strReason = "Not paired";
            }
            x = usableX + usableWidth * NPL_REASON_BEG / NPL_NBCAR;
            g.drawString(strReason, x, y);

            String strName = player.getName();
            String strFirstName = player.getFirstName();
            if (strName.length() > 20) {
                strName = strName.substring(0, 20);
            }
            String strNF = strName + " " + strFirstName;
            if (strNF.length() > 25) {
                strNF = strNF.substring(0, 25);
            }
            if (player.getRegisteringStatus().compareTo("PRE") == 0) {
                strNF += "(P)";
            }
            x = usableX + usableWidth * NPL_NF_BEG / NPL_NBCAR;
            g.drawString(strNF, x, y);

            String strRk = Player.convertIntToKD(player.getRank());
            x = usableX + usableWidth * (NPL_RANK_BEG + NPL_RANK_LEN) / NPL_NBCAR;
            drawRightAlignedString(g, strRk, x, y);
        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

        if (ln == 0) {
            return NO_SUCH_PAGE;
        }
        return PAGE_EXISTS;
    }    
    
    private int printAPageOfTeamsList(Graphics g, PageFormat pf, int pi) throws RemoteException {
        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Teams List";
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);

        // Header Line
        printTeamsListHeaderLine(g, pf, pi);

        // Body lines
        int ln = 0;
        int teamSize = tournament.getTeamTournamentParameterSet().getTeamGeneralParameterSet().getTeamSize();
        for (ln = 0; ln < numberOfBodyLinesInAPage; ln++) {
            int abstractLineNumber = ln + pi * numberOfBodyLinesInAPage;
            int teamNumber = abstractLineNumber / teamSize;
            if (teamNumber >= alTeamsToPrint.size()) {
                break;
            }
            Team team = alTeamsToPrint.get(teamNumber);
            y = usableY + (4 + ln) * lineHeight;
            if ((ln % 2) == 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(usableX, y - lineHeight + 4, usableWidth, lineHeight);  // + 4 to keep leading part unfilled
                g.setColor(Color.BLACK);
            }

            String strNumber = "" + (team.getTeamNumber() + 1);
            x = usableX + usableWidth * (TL_NUMBER_BEG + TL_NUMBER_LEN) / TL_NBCAR;
            if (abstractLineNumber % teamSize == 0) {
                drawRightAlignedString(g, strNumber, x, y);
            }

            String strTeamName = team.getTeamName();
            if (strTeamName.length() > TL_TEAMNAME_LEN) {
                strTeamName = strTeamName.substring(0, TL_TEAMNAME_LEN);
            }
            x = usableX + usableWidth * TL_TEAMNAME_BEG / TL_NBCAR;
            if (abstractLineNumber % teamSize == 0) {
                g.drawString(strTeamName, x, y);
            }

            int boardNumber = abstractLineNumber % teamSize;
            String strBoard = "" + (boardNumber + 1);
            x = usableX + usableWidth * (TL_BOARD_BEG + TL_BOARD_LEN) / TL_NBCAR;
            drawRightAlignedString(g, strBoard, x, y);

            Player player = team.getTeamMember(Gotha.DEFAULT_ROUND_NUMBER, boardNumber);
            if (player == null) {
                continue;
            }
            String strName = player.getName();
            String strFirstName = player.getFirstName();
            if (strName.length() > 20) {
                strName = strName.substring(0, 20);
            }
            String strNF = strName + " " + strFirstName;
            if (strNF.length() > TL_NF_LEN) {
                strNF = strNF.substring(0, TL_NF_LEN);
            }
            x = usableX + usableWidth * TL_NF_BEG / TL_NBCAR;
            g.drawString(strNF, x, y);

            String strRating = "" + player.getRating();
            x = usableX + usableWidth * (TL_RATING_BEG + TL_RATING_LEN) / TL_NBCAR;
            drawRightAlignedString(g, strRating, x, y);

            String strCountry = player.getCountry();
            strCountry = Gotha.leftString(strCountry, 2);
            x = usableX + usableWidth * TL_COUNTRY_BEG / TL_NBCAR;
            g.drawString(strCountry, x, y);

            String strClub = player.getClub();
            strClub = Gotha.leftString(strClub, 4);
            x = usableX + usableWidth * TL_CLUB_BEG / TL_NBCAR;
            g.drawString(strClub, x, y);

        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

        if (ln == 0) {
            return NO_SUCH_PAGE;
        }
        return PAGE_EXISTS;
    }

    private int printAPageOfGamesList(Graphics g, PageFormat pf, int pi) throws RemoteException {
        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Games List";
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);

        String strRound = "Round";
        strRound += " " + (roundNumber + 1);
        char[] cRound = strRound.toCharArray();
        int roundWidth = fm.charsWidth(cRound, 0, cRound.length);
        x = (usableWidth - roundWidth) / 2;
        y += lineHeight;
        g.drawString(strRound, x, y);

        printGamesListHeaderLine(g, pf, pi);

        ArrayList<Game> alGamesToPrint = new ArrayList<Game>(tournament.gamesList(roundNumber));

        int gamesSortType = GameComparator.TABLE_NUMBER_ORDER;
        GameComparator gameComparator = new GameComparator(gamesSortType);
        Collections.sort(alGamesToPrint, gameComparator);


        int ln = 0;
        // Body lines
        for (ln = 0; ln < numberOfBodyLinesInAPage; ln++) {
            int abstractLineNumber = ln + pi * numberOfBodyLinesInAPage;
            int gameNumber = abstractLineNumber;
            if (gameNumber >= alGamesToPrint.size()) {
                break;
            }
            Game game = alGamesToPrint.get(gameNumber);
            y = usableY + (4 + ln) * lineHeight;
            if ((ln % 2) == 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(usableX, y - lineHeight + 4, usableWidth, lineHeight);  // + 4 to keep leading part unfilled
                g.setColor(Color.BLACK);
            }

            String strTN = "" + (game.getTableNumber() + 1);
            x = usableX + usableWidth * (GL_TN_BEG + GL_TN_LEN) / GL_NBCAR;
            drawRightAlignedString(g, strTN, x, y);

            Player wP = game.getWhitePlayer();
            String strName = wP.getName();
            String strFirstName = wP.getFirstName();
            if (strName.length() > 18) {
                strName = strName.substring(0, 18);
            }
            String strNF = strName + " " + strFirstName;
            if (strNF.length() > 22) {
                strNF = strNF.substring(0, 22);
            }
            String strClub = wP.getClub();
            strClub = Gotha.leftString(strClub, 4);
            strNF += " (" + Player.convertIntToKD(wP.getRank()) + "," + strClub + ")";
            x = usableX + usableWidth * GL_WNF_BEG / GL_NBCAR;
            int result = game.getResult();
            if (result >= Game.RESULT_BYDEF) {
                result -= Game.RESULT_BYDEF;
            }
            if (result == Game.RESULT_BOTHLOSE || result == Game.RESULT_EQUAL || result == Game.RESULT_BLACKWINS) {
                g.setFont(new Font("Default", Font.PLAIN, fontSize));
            }
            g.drawString(strNF, x, y);
            g.setFont(font);

            Player bP = game.getBlackPlayer();
            strName = bP.getName();
            strFirstName = bP.getFirstName();
            if (strName.length() > 18) {
                strName = strName.substring(0, 18);
            }
            strNF = strName + " " + strFirstName;
            if (strNF.length() > 22) {
                strNF = strNF.substring(0, 22);
            }
            strClub = bP.getClub();
            strClub = Gotha.leftString(strClub, 4);
            strNF += " (" + Player.convertIntToKD(bP.getRank()) + "," + strClub + ")";
            x = usableX + usableWidth * GL_BNF_BEG / GL_NBCAR;
            if (result == Game.RESULT_BOTHLOSE || result == Game.RESULT_EQUAL || result == Game.RESULT_WHITEWINS) {
                g.setFont(new Font("Default", Font.PLAIN, fontSize));
            }
            g.drawString(strNF, x, y);
            g.setFont(font);

            String strHd = "" + game.getHandicap();
            x = usableX + usableWidth * (GL_HD_BEG + GL_HD_LEN) / GL_NBCAR;
            drawRightAlignedString(g, strHd, x, y);

            String strResult = game.resultAsString();

            x = usableX + usableWidth * (GL_RES_BEG + GL_RES_LEN) / GL_NBCAR;
            drawRightAlignedString(g, strResult, x, y);
        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

        if (ln == 0) {
            return NO_SUCH_PAGE;
        }
        return PAGE_EXISTS;
    }

    private int printAPageOfMatchesList(Graphics g, PageFormat pf, int pi) throws RemoteException {
        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Matches List";
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);

        String strRound = "Round";
        strRound += " " + (roundNumber + 1);
        char[] cRound = strRound.toCharArray();
        int roundWidth = fm.charsWidth(cRound, 0, cRound.length);
        x = (usableWidth - roundWidth) / 2;
        y += lineHeight;
        g.drawString(strRound, x, y);

        printMatchesListHeaderLine(g, pf, pi);

        ArrayList<Match> alM = tournament.matchesList(roundNumber);
        ArrayList<ComparableMatch> alComparableMatchesToPrint = ComparableMatch.buildComparableMatchesArray(alM, tournament, roundNumber);

        int matchesSortType = MatchComparator.BOARD0_TABLE_NUMBER_ORDER;
        MatchComparator matchComparator = new MatchComparator(matchesSortType);
        Collections.sort(alComparableMatchesToPrint, matchComparator);

        int ln = 0;
        // Body lines
        for (ln = 0; ln < numberOfBodyLinesInAPage; ln++) {
            int abstractLineNumber = ln + pi * numberOfBodyLinesInAPage;
            int matchNumber = abstractLineNumber;
            if (matchNumber >= alComparableMatchesToPrint.size()) {
                break;
            }
            ComparableMatch cm = alComparableMatchesToPrint.get(matchNumber);
//            Match match = tournament.getMatch(roundNumber, );
            y = usableY + (4 + ln) * lineHeight;
            if ((ln % 2) == 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(usableX, y - lineHeight + 4, usableWidth, lineHeight);  // + 4 to keep leading part unfilled
                g.setColor(Color.BLACK);
            }


            String strTN = "" + (cm.board0TableNumber + 1) + "---";
            x = usableX + usableWidth * (ML_TN_BEG + ML_TN_LEN) / ML_NBCAR;
            drawRightAlignedString(g, strTN, x, y);

            Match match = tournament.getMatch(roundNumber, cm.board0TableNumber);
            int wResult = match.getTeamScore(match.getWhiteTeam());
            int bResult = match.getTeamScore(match.getBlackTeam());

            String strWTN = cm.wst.getTeamName();
            if (strWTN.length() > ML_WTN_LEN) {
                strWTN = strWTN.substring(0, ML_WTN_LEN);
            }
            x = usableX + usableWidth * ML_WTN_BEG / ML_NBCAR;
            if (wResult <= 1) {
                g.setFont(new Font("Default", Font.PLAIN, fontSize));
            }
            g.drawString(strWTN, x, y);
            g.setFont(font);

            String strBTN = cm.bst.getTeamName();
            if (strBTN.length() > ML_BTN_LEN) {
                strBTN = strBTN.substring(0, ML_BTN_LEN);
            }
            x = usableX + usableWidth * ML_BTN_BEG / ML_NBCAR;
            if (bResult <= 1) {
                g.setFont(new Font("Default", Font.PLAIN, fontSize));
            }
            g.drawString(strBTN, x, y);
            g.setFont(font);

            String strWResult = "" + Gotha.formatFractNumber(wResult, 1);
            String strBResult = "" + Gotha.formatFractNumber(bResult, 1);
            String strResult = strWResult + "-" + strBResult;
            x = usableX + usableWidth * (ML_RES_BEG + ML_RES_LEN) / ML_NBCAR;
            drawRightAlignedString(g, strResult, x, y);


        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

        if (ln == 0) {
            return NO_SUCH_PAGE;
        }
        return PAGE_EXISTS;
    }

    private int printAPageOfStandings(Graphics g, PageFormat pf, int pi) throws RemoteException {
        TournamentParameterSet tps = null;
        try {
            tps = tournament.getTournamentParameterSet();
        } catch (RemoteException ex) {
            Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
        }
        GeneralParameterSet gps = tps.getGeneralParameterSet();

        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Standings after round" + " " + (roundNumber + 1);
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);
        y += lineHeight;

        // Knowing pi, what category are we working with ?
        int curCat = 0;
        int nbPlayersBeforeCurCat = 0;
        int nbPagesBeforeCurCat = 0;
        int nbPlayersOfCurCat = 0;
        try {
            nbPlayersOfCurCat = tournament.numberOfPlayersInCategory(curCat, alOrderedScoredPlayers);
        } catch (RemoteException ex) {
            Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
        }
        // If the first criterion is not CAT, then, do as if everybody was in category 1
        if (this.printSubType != TournamentPrinting.SUBTYPE_ST_CAT) {
            nbPlayersOfCurCat = tournament.numberOfPlayers();
        }

        int nbPagesForCurCat = (nbPlayersOfCurCat + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
        while (pi >= nbPagesBeforeCurCat + nbPagesForCurCat) {
            curCat++;
            if (curCat >= gps.getNumberOfCategories()) {
                return NO_SUCH_PAGE;
            }
            nbPlayersBeforeCurCat += nbPlayersOfCurCat;
            nbPagesBeforeCurCat += nbPagesForCurCat;
            try {
                nbPlayersOfCurCat = tournament.numberOfPlayersInCategory(curCat, alOrderedScoredPlayers);
            } catch (RemoteException ex) {
                Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
            }
            nbPagesForCurCat = (nbPlayersOfCurCat + numberOfBodyLinesInAPage - 1) / numberOfBodyLinesInAPage;
        }

        if (printCriteria[0] == PlacementParameterSet.PLA_CRIT_CAT) {
            String strCat = "Category" + " " + (curCat + 1);
            char[] cCat = strCat.toCharArray();
            int catWidth = fm.charsWidth(cCat, 0, cCat.length);
            x = (usableWidth - catWidth) / 2;
            g.drawString(strCat, x, y);
        }
        printStandingsHeaderLine(g, pf, pi);

        int ln = 0;

        // Body lines
        for (ln = 0; ln < numberOfBodyLinesInAPage; ln++) {
            y = usableY + (4 + ln) * lineHeight;
//            int abstractLineNumber = ln + pi * numberOfBodyLinesInAPage;
            int playerNumber = nbPlayersBeforeCurCat + ln + (pi - nbPagesBeforeCurCat) * numberOfBodyLinesInAPage;
            if (playerNumber >= this.alOrderedScoredPlayers.size()) {
                break;
            }
            ScoredPlayer sp = alOrderedScoredPlayers.get(playerNumber);

            if (this.printSubType == TournamentPrinting.SUBTYPE_ST_CAT && sp.category(gps) != curCat) {
                break;
            }

            if ((ln % 2) == 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(usableX, y - lineHeight + 4, usableWidth, lineHeight);  // + 4 to keep leading part unfilled
                g.setColor(Color.BLACK);
            }

            String strNum = "" + (playerNumber + 1);
            x = usableX + usableWidth * (ST_NUM_BEG + ST_NUM_LEN) / numberOfCharactersInALine;
            drawRightAlignedString(g, strNum, x, y);

            String strPL = strPlace[playerNumber];
            x = usableX + usableWidth * (ST_PL_BEG + ST_PL_LEN) / numberOfCharactersInALine;
            drawRightAlignedString(g, strPL, x, y);

            String strName = sp.getName();
            String strFirstName = sp.getFirstName();

            if (strName.length() > 18) {
                strName = strName.substring(0, 18);
            }
            String strNF = strName + " " + strFirstName;
            if (strNF.length() > 22) {
                strNF = strNF.substring(0, 22);
            }
            x = usableX + usableWidth * (ST_NF_BEG) / numberOfCharactersInALine;
            g.drawString(strNF, x, y);

            String strRk = Player.convertIntToKD(sp.getRank());
            x = usableX + usableWidth * (ST_RK_BEG + ST_RK_LEN) / numberOfCharactersInALine;
            drawRightAlignedString(g, strRk, x, y);

            x = usableX + usableWidth * (ST_NBW_BEG + ST_NBW_LEN) / numberOfCharactersInALine;
            String strNbW = sp.formatScore(PlacementParameterSet.PLA_CRIT_NBW, roundNumber);
            drawRightAlignedString(g, strNbW, x, y);

            int numberOfRoundsPrinted = roundNumber + 1;
            int rBeg = ST_ROUND0_BEG;
            for (int r = 0; r < numberOfRoundsPrinted; r++) {
                int xR = usableX + usableWidth * (rBeg + (r + 1) * ST_ROUND_LEN) / numberOfCharactersInALine;

                drawRightAlignedString(g, this.halfGamesStrings[r][playerNumber], xR, y);
            }

            int numberOfCriteriaPrinted = printCriteria.length;
            int cBeg = rBeg + numberOfRoundsPrinted * ST_ROUND_LEN;
            for (int iC = 0; iC < numberOfCriteriaPrinted; iC++) {
                int xC = usableX + usableWidth * (cBeg + (iC + 1) * ST_CRIT_LEN) / numberOfCharactersInALine;
                String strCritValue = sp.formatScore(printCriteria[iC], roundNumber);
                PlacementParameterSet.criterionShortName(printCriteria[iC]);
                drawRightAlignedString(g, strCritValue, xC, y);
            }
        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

        if (ln == 0) {
            return NO_SUCH_PAGE;
        }

        return PAGE_EXISTS;
    }

    private int printAPageOfTeamsStandings(Graphics g, PageFormat pf, int pi) throws RemoteException {
        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Teams Standings after round" + " " + (roundNumber + 1);
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);
        y += lineHeight;

        printTeamsStandingsHeaderLine(g, pf, pi);

        int ln = 0;

        // Body lines
        ArrayList<ScoredTeam> alOrderedScoredTeamsList = scoredTeamsSet.getOrderedScoredTeamsList();
        for (ln = 0; ln < numberOfBodyLinesInAPage; ln++) {
            y = usableY + (4 + ln) * lineHeight;
            int abstractLineNumber = ln + pi * numberOfBodyLinesInAPage;
            int teamNumber = abstractLineNumber;
            if (teamNumber >= alOrderedScoredTeamsList.size()) {
                break;
            }

            if ((ln % 2) == 0) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(usableX, y - lineHeight + 4, usableWidth, lineHeight);  // + 4 to keep leading part unfilled
                g.setColor(Color.BLACK);
            }

            String strNum = "" + (teamNumber + 1);
            x = usableX + usableWidth * (TST_NUM_BEG + TST_NUM_LEN) / numberOfCharactersInALine;
            drawRightAlignedString(g, strNum, x, y);

            ScoredTeam st = alOrderedScoredTeamsList.get(teamNumber);

            String strPL = scoredTeamsSet.getTeamPositionString(st);
            x = usableX + usableWidth * (TST_PL_BEG + TST_PL_LEN) / numberOfCharactersInALine;
            drawRightAlignedString(g, strPL, x, y);

            String strTN = st.getTeamName();

            if (strTN.length() > TST_TN_LEN) {
                strTN = strTN.substring(0, TST_TN_LEN);
            }
            x = usableX + usableWidth * (TST_TN_BEG) / numberOfCharactersInALine;
            g.drawString(strTN, x, y);

            int numberOfRoundsPrinted = roundNumber + 1;
            int rBeg = TST_ROUND0_BEG;
            for (int r = 0; r < numberOfRoundsPrinted; r++) {
                String strMatch = scoredTeamsSet.getHalfMatchString(st, r);
                int xR = usableX + usableWidth * (rBeg + (r + 1) * TST_ROUND_LEN) / numberOfCharactersInALine;
                drawRightAlignedString(g, strMatch, xR, y);
            }

            int numberOfCriteriaPrinted = printCriteria.length;
            int cBeg = rBeg + numberOfRoundsPrinted * TST_ROUND_LEN;
            for (int ic = 0; ic < numberOfCriteriaPrinted; ic++) {
                int crit = printCriteria[ic];
                int coef = TeamPlacementParameterSet.criterionCoef(crit);
                String strCritValue = Gotha.formatFractNumber(st.getCritValue(ic), coef);
                int xC = usableX + usableWidth * (cBeg + (ic + 1) * TST_CRIT_LEN) / numberOfCharactersInALine;
                drawRightAlignedString(g, strCritValue, xC, y);
            }
        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

        if (ln == 0) {
            return NO_SUCH_PAGE;
        }

        return PAGE_EXISTS;
    }

    private int printAPageOfTournamentParameters(Graphics g, PageFormat pf, int pi) throws RemoteException {
        // Page 1 (pi = 0) contains General, Handicap and Placement parameters
        // Page 2 (pi = 1) contains Pairing parameters
        if (pi > 1) {
            return NO_SUCH_PAGE;
        }
        Font font = new Font("Default", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics(font);

        String strTitle = "Tournament Parameters";
        char[] cTitle = strTitle.toCharArray();
        int titleWidth = fm.charsWidth(cTitle, 0, cTitle.length);
        int x = (usableWidth - titleWidth) / 2;
        int y = (usableY + lineHeight);
        g.drawString(strTitle, x, y);

        TournamentParameterSet tps = tournament.getTournamentParameterSet();
        GeneralParameterSet gps = tps.getGeneralParameterSet();
        HandicapParameterSet hps = tps.getHandicapParameterSet();
        PlacementParameterSet pps = tps.getPlacementParameterSet();
        PairingParameterSet paiPS = tps.getPairingParameterSet();

        if (pi == 0) {
            int ln = 0;

            x = usableX;
            ln = 0;
            y = usableY + (4 + ln) * lineHeight;
            Font title2Font = new Font("Default", Font.BOLD, fontSize + 4);
            g.setFont(title2Font);
            g.drawString("General Parameters", x, y);
            ln++;
            g.setFont(font);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString(gps.getName(), x, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            int tournamentType = tournament.tournamentType();
            String strType = "Undefined system";
            if (tournamentType == TournamentParameterSet.TYPE_MACMAHON) {
                strType = "Mac-Mahon system";
            }
            if (tournamentType == TournamentParameterSet.TYPE_SWISS) {
                strType = "Swiss system";
            }
            if (tournamentType == TournamentParameterSet.TYPE_SWISSCAT) {
                strType = "SwissCat system";
            }
            g.drawString(strType, x, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString(gps.getNumberOfRounds() + " " + "rounds", x, y);
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString("Special Results", x, y);
            int xNBW = usableX + usableWidth * TP_TAB3 / TP_NBCAR;
            drawRightAlignedString(g, "NBW", xNBW, y);
            int xMMS = usableX + usableWidth * TP_TAB4 / TP_NBCAR;
            TournamentPrinting.drawRightAlignedString(g, "MMS", xMMS, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString("Absent", usableX + usableWidth * TP_TAB1 / TP_NBCAR, y);
            String strNBW = "";
            switch (gps.getGenNBW2ValueAbsent()) {
                case 0:
                    strNBW = "0";
                    break;
                case 1:
                    strNBW = "½";
                    break;
                case 2:
                    strNBW = "1";
                    break;
            }
            String strMMS = "";
            switch (gps.getGenMMS2ValueAbsent()) {
                case 0:
                    strMMS = "0";
                    break;
                case 1:
                    strMMS = "½";
                    break;
                case 2:
                    strMMS = "1";
                    break;
            }
            drawRightAlignedString(g, strNBW, xNBW, y);
            drawRightAlignedString(g, strMMS, xMMS, y);
            
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString("Bye", usableX + usableWidth * TP_TAB1 / TP_NBCAR, y);
            strNBW = "";
            switch (gps.getGenNBW2ValueBye()) {
                case 0:
                    strNBW = "0";
                    break;
                case 1:
                    strNBW = "½";
                    break;
                case 2:
                    strNBW = "1";
                    break;
            }
            strMMS = "";
            switch (gps.getGenMMS2ValueBye()) {
                case 0:
                    strMMS = "0";
                    break;
                case 1:
                    strMMS = "½";
                    break;
                case 2:
                    strMMS = "1";
                    break;
            }
            drawRightAlignedString(g, strNBW, xNBW, y);
            drawRightAlignedString(g, strMMS, xMMS, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strRoundDown = "Round down NBW and MMS scores : " + gps.isGenRoundDownNBWMMS();
            g.drawString(strRoundDown, x, y);
  
            if (gps.getNumberOfCategories() > 1) {
                ln++;
                for (int c = 0; c < gps.getNumberOfCategories(); c++) {
                    ln++;
                    y = usableY + (4 + ln) * lineHeight;
                    int lowL = Gotha.MIN_RANK;
                    int highL = Gotha.MAX_RANK;
                    if (c <= gps.getNumberOfCategories() - 2) {
                        lowL = gps.getLowerCategoryLimits()[c];
                    }
                    if (c >= 1) {
                        highL = gps.getLowerCategoryLimits()[c - 1] - 1;
                    }
                    String strLow = Player.convertIntToKD(lowL);
                    String strHigh = Player.convertIntToKD(highL);
                    g.drawString("Category"
                            + (c + 1) + " : " + strHigh + " - " + strLow, x, y);
                }
            }
            ln++;

            x = usableX;
            ln++;
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            title2Font = new Font("Default", Font.BOLD, fontSize + 4);
            g.setFont(title2Font);
            g.drawString("Handicap Parameters", x, y);
            ln++;
            g.setFont(font);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strRankThreshold = Player.convertIntToKD(hps.getHdNoHdRankThreshold());
            g.drawString("No handicap for players above " + strRankThreshold, x, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strHdCorr = "";
            if (hps.getHdCorrection() == 0) {
                strHdCorr = "Handicap not decreased";
            } else if (hps.getHdCorrection() > 0){
                strHdCorr = "Handicap decreased by" + " " + hps.getHdCorrection();
            } else if (hps.getHdCorrection() < 0){
                strHdCorr = "Handicap increased by" + " " + Math.abs(hps.getHdCorrection());
            }
            g.drawString(strHdCorr, x, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strHdCeil = "";
            strHdCeil = "Handicap ceiling" + " : " + hps.getHdCeiling();
            g.drawString(strHdCeil, x, y);

            x = usableX;
            ln++;
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            title2Font = new Font("Default", Font.BOLD, fontSize + 4);
            g.setFont(title2Font);
            g.drawString("Placement Parameters", x, y);
            ln++;
            g.setFont(font);

            int[] plaC = pps.getPlaCriteria();
            // Get rid of useless criteria
            int nbCrit = plaC.length;
            for (int c = nbCrit - 1; c > 0; c--) {
                if (plaC[c] == PlacementParameterSet.PLA_CRIT_NUL) {
                    nbCrit--;
                } else {
                    break;
                }
            }
            int[] plaCrit = new int[nbCrit];
            System.arraycopy(plaC, 0, plaCrit, 0, nbCrit);

            for (int crit = 0; crit < plaCrit.length; crit++) {
                ln++;
                y = usableY + (4 + ln) * lineHeight;
                String strCrit = PlacementParameterSet.criterionLongName(plaCrit[crit]);
                g.drawString("Criterion" + (crit + 1) + " : " + strCrit, x, y);
            }
            // Criteria Descriptions
            for (int crit = 0; crit < plaCrit.length; crit++) {
                ln++;
                y = usableY + (4 + ln) * lineHeight;
                String strCrit = PlacementParameterSet.criterionLongName(plaCrit[crit]);
                String strDescr = PlacementParameterSet.criterionDescription(plaCrit[crit]);
                Font italFont = new Font("Default", Font.ITALIC, fontSize);
                g.setFont(italFont);
                g.drawString(strCrit + " = " + strDescr, x, y);
                g.setFont(font);
            }

            if (tournament.teamsList().size() > 0) {
                x = usableX;
                ln++;
                ln++;
                y = usableY + (4 + ln) * lineHeight;
                title2Font = new Font("Default", Font.BOLD, fontSize + 4);
                g.setFont(title2Font);
                g.drawString("Team Placement Parameters", x, y);
                ln++;
                g.setFont(font);

                TeamPlacementParameterSet tpps = tournament.getTeamTournamentParameterSet().getTeamPlacementParameterSet();
                plaC = tpps.getPlaCriteria();
                // Get rid of useless criteria
                nbCrit = plaC.length;
                for (int c = nbCrit - 1; c > 0; c--) {
                    if (plaC[c] == TeamPlacementParameterSet.TPL_CRIT_NUL) {
                        nbCrit--;
                    } else {
                        break;
                    }
                }
                plaCrit = new int[nbCrit];
                System.arraycopy(plaC, 0, plaCrit, 0, nbCrit);

                for (int crit = 0; crit < plaCrit.length; crit++) {
                    ln++;
                    y = usableY + (4 + ln) * lineHeight;
                    String strCrit = TeamPlacementParameterSet.criterionLongName(plaCrit[crit]);
                    g.drawString("Criterion" + (crit + 1) + " : " + strCrit, x, y);
                }
                // Criteria Descriptions
                for (int crit = 0; crit < plaCrit.length; crit++) {
                    ln++;
                    y = usableY + (4 + ln) * lineHeight;
                    String strCrit = TeamPlacementParameterSet.criterionLongName(plaCrit[crit]);
                    String strDescr = TeamPlacementParameterSet.criterionDescription(plaCrit[crit]);
                    Font italFont = new Font("Default", Font.ITALIC, fontSize);
                    g.setFont(italFont);
                    g.drawString(strCrit + " = " + strDescr, x, y);
                    g.setFont(font);
                }
            }

        }
        int ln = 0;

        if (pi == 1) {
            x = usableX;
            ln = 0;
            y = usableY + (4 + ln) * lineHeight;
            Font title2Font = new Font("Default", Font.BOLD, fontSize + 4);
            g.setFont(title2Font);
            g.drawString("Pairing Parameters", x, y);
            ln++;
            g.setFont(font);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strSS = "Seeding system";
            int lastRSS1 = paiPS.getPaiMaLastRoundForSeedSystem1();
            if (lastRSS1 < gps.getNumberOfRounds() - 1) {
                strSS += " " + "until Round" + (lastRSS1 + 1);
            }
            strSS += " : ";
            if (paiPS.getPaiMaSeedSystem1() == PairingParameterSet.PAIMA_SEED_SPLITANDRANDOM) {
                strSS += "Split and Random";
            } else if (paiPS.getPaiMaSeedSystem1() == PairingParameterSet.PAIMA_SEED_SPLITANDFOLD) {
                strSS += "Split and Fold";
            } else if (paiPS.getPaiMaSeedSystem1() == PairingParameterSet.PAIMA_SEED_SPLITANDSLIP) {
                strSS += "Split and Slip";
            }
            if (paiPS.getPaiMaAdditionalPlacementCritSystem1() != PlacementParameterSet.PLA_CRIT_NUL) {
                strSS += " " + "with additional criterion on" + " "
                        + PlacementParameterSet.criterionLongName(paiPS.getPaiMaAdditionalPlacementCritSystem1());
            }
            g.drawString(strSS, x, y);

            if (lastRSS1 < gps.getNumberOfRounds() - 1) {
                ln++;
                y = usableY + (4 + ln) * lineHeight;
                String strSS2 = "Seeding system starting from Round" + (lastRSS1 + 2) + " : ";
                if (paiPS.getPaiMaSeedSystem2() == PairingParameterSet.PAIMA_SEED_SPLITANDRANDOM) {
                    strSS2 += "Split and Random";
                } else if (paiPS.getPaiMaSeedSystem2() == PairingParameterSet.PAIMA_SEED_SPLITANDFOLD) {
                    strSS2 += "Split and Fold";
                } else if (paiPS.getPaiMaSeedSystem2() == PairingParameterSet.PAIMA_SEED_SPLITANDSLIP) {
                    strSS2 += "Split and Slip";
                }
                if (paiPS.getPaiMaAdditionalPlacementCritSystem2() != PlacementParameterSet.PLA_CRIT_NUL) {
                    strSS2 += "with_additional_criterion_on"
                            + PlacementParameterSet.criterionLongName(paiPS.getPaiMaAdditionalPlacementCritSystem2());
                }
                g.drawString(strSS2, x, y);
            }

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strDG = "When pairing players from different groups is necessary,";
            g.drawString(strDG, x, y);
            int x1 = usableX + usableWidth * TP_TAB1 / TP_NBCAR;
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strUG = "The player in the upper group is chosen";
            if (paiPS.getPaiMaDUDDUpperMode() == PairingParameterSet.PAIMA_DUDD_TOP) {
                strUG += " " + "in the top of the group";
            }
            if (paiPS.getPaiMaDUDDUpperMode() == PairingParameterSet.PAIMA_DUDD_MID) {
                strUG += " " + "in the middle of the group";
            }
            if (paiPS.getPaiMaDUDDUpperMode() == PairingParameterSet.PAIMA_DUDD_BOT) {
                strUG += " " + "in the bottom of the group";
            }
            g.drawString(strUG, x1, y);
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strLG = "The player in the lower group is chosen";
            if (paiPS.getPaiMaDUDDLowerMode() == PairingParameterSet.PAIMA_DUDD_TOP) {
                strLG += " " + "in the top of the group";
            }
            if (paiPS.getPaiMaDUDDLowerMode() == PairingParameterSet.PAIMA_DUDD_MID) {
                strLG += " " + "in the middle of the group";
            }
            if (paiPS.getPaiMaDUDDLowerMode() == PairingParameterSet.PAIMA_DUDD_BOT) {
                strLG += " " + "in the bottom of the group";
            }
            g.drawString(strLG, x1, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strRan = "";
            if (paiPS.getPaiBaRandom() == 0) {
                strRan = "No random";
            } else if (paiPS.isPaiBaDeterministic()) {
                strRan = "Some deterministic random";
            } else {
                strRan = "Some non-deterministic random";
            }
            g.drawString(strRan, x, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strBalWB = "";
            if (paiPS.getPaiBaBalanceWB() != 0) {
                strBalWB = "Balance White and Black";
            }
            g.drawString(strBalWB, x, y);

            ln++;
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString("Secondary criteria", x, y);

            ln++;
            y = usableY + (4 + ln) * lineHeight;
            String strRankThreshold = Player.convertIntToKD(paiPS.getPaiSeRankThreshold());
            g.drawString("Secondary criteria not applied for players with a rank equal or stronger than"
                    + strRankThreshold, usableX + usableWidth * TP_TAB1 / TP_NBCAR, y);
            if (paiPS.isPaiSeNbWinsThresholdActive()) {
                ln++;
                y = usableY + (4 + ln) * lineHeight;
                g.drawString("Secondary criteria not applied for players with at least nbRounds/2 wins",
                        usableX + usableWidth * TP_TAB1 / TP_NBCAR, y);
            }
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString("Intra-country pairing is avoided. A group gap of" + " " + paiPS.getPaiSePreferMMSDiffRatherThanSameCountry() + " "
                    + "is preferred", usableX + usableWidth * TP_TAB1 / TP_NBCAR, y);
            ln++;
            y = usableY + (4 + ln) * lineHeight;
            g.drawString("Intra-club pairing is avoided. A group gap of" + " " + paiPS.getPaiSePreferMMSDiffRatherThanSameClub() + " "
                    + "is preferred", usableX + usableWidth * TP_TAB1 / TP_NBCAR, y);
            if (paiPS.getPaiSeMinimizeHandicap() != 0) {
                ln++;
                y = usableY + (4 + ln) * lineHeight;
                g.drawString("Low handicap games are preferred", usableX + usableWidth * TP_TAB1 / TP_NBCAR, y);
            }
        }

        // Print Page Footer
        printPageFooter(g, pf, pi);

//        if (ln == 0) return NO_SUCH_PAGE;
        return PAGE_EXISTS;
    }

    private void printPlayersListHeaderLine(Graphics g, PageFormat pf, int pi) {
        int y = usableY + 3 * lineHeight;
        int x = usableX + usableWidth * PL_PINLIC_BEG / PL_NBCAR;

        g.drawString("Pin/Lic/Id", x, y);
        x = usableX + usableWidth * PL_NF_BEG / PL_NBCAR;
        g.drawString("Name", x, y);
        x = usableX + usableWidth * (PL_RANK_BEG + PL_RANK_LEN) / PL_NBCAR;
        drawRightAlignedString(g, "Rk", x, y);
        x = usableX + usableWidth * (TournamentPrinting.PL_RT_BEG + PL_RT_LEN) / PL_NBCAR;
        drawRightAlignedString(g, "Rt", x, y);
        x = usableX + usableWidth * (PL_MM_BEG + PL_MM_LEN) / PL_NBCAR;
        drawRightAlignedString(g, "MM", x, y);

        x = usableX + usableWidth * PL_COUNTRY_BEG / PL_NBCAR;
        g.drawString("Co", x, y);
        x = usableX + usableWidth * PL_CLUB_BEG / PL_NBCAR;
        g.drawString("Club", x, y);
        x = usableX + usableWidth * PL_PART_BEG / PL_NBCAR;
        g.drawString("Participation", x, y);
    }
    

    private void printNotPlayingPlayersListHeaderLine(Graphics g, PageFormat pf, int pi) {
        int y = usableY + 3 * lineHeight;
        int x = usableX + usableWidth * this.NPL_REASON_BEG / NPL_NBCAR;

        g.drawString("Reason", x, y);
        x = usableX + usableWidth * NPL_NF_BEG / NPL_NBCAR;
        g.drawString("Name" + "    " + "First name", x, y);
        x = usableX + usableWidth * (PL_RANK_BEG + PL_RANK_LEN) / PL_NBCAR;
        drawRightAlignedString(g, "Rk", x, y);
    }    

    private void printTeamsListHeaderLine(Graphics g, PageFormat pf, int pi) {
        int y = usableY + 3 * lineHeight;
        int x = usableX + usableWidth * TL_TEAMNAME_BEG / TL_NBCAR;
        g.drawString("Team name", x, y);

        x = usableX + usableWidth * (TL_BOARD_BEG + TL_BOARD_LEN) / TL_NBCAR;
        drawRightAlignedString(g, "Bd", x, y);

        x = usableX + usableWidth * TL_NF_BEG / TL_NBCAR;
        g.drawString("Player name", x, y);

        x = usableX + usableWidth * (TL_RATING_BEG + TL_RATING_LEN) / TL_NBCAR;
        drawRightAlignedString(g, "Rating", x, y);

        x = usableX + usableWidth * TL_COUNTRY_BEG / TL_NBCAR;
        g.drawString("Co", x, y);

        x = usableX + usableWidth * TL_CLUB_BEG / TL_NBCAR;
        g.drawString("Club", x, y);
    }

    private void printGamesListHeaderLine(Graphics g, PageFormat pf, int pi) {
        int y = usableY + 3 * lineHeight;
        int x = usableX + usableWidth * (GL_TN_BEG + GL_TN_LEN) / GL_NBCAR;
        drawRightAlignedString(g, "Tble", x, y);
        x = usableX + usableWidth * GL_WNF_BEG / GL_NBCAR;
        g.drawString("White", x, y);
        x = usableX + usableWidth * GL_BNF_BEG / GL_NBCAR;
        g.drawString("Black", x, y);

        x = usableX + usableWidth * (GL_HD_BEG + GL_HD_LEN) / GL_NBCAR;
        drawRightAlignedString(g, "Hd", x, y);
        x = usableX + usableWidth * (GL_RES_BEG + GL_RES_LEN) / GL_NBCAR;
        drawRightAlignedString(g, "Res", x, y);
    }

    private void printMatchesListHeaderLine(Graphics g, PageFormat pf, int pi) {
        int y = usableY + 3 * lineHeight;
        int x = usableX + usableWidth * (ML_TN_BEG + ML_TN_LEN) / ML_NBCAR;
        drawRightAlignedString(g, "Tbles", x, y);
        x = usableX + usableWidth * TournamentPrinting.ML_WTN_BEG / ML_NBCAR;
        g.drawString(" White (board 1)", x, y);
        x = usableX + usableWidth * ML_BTN_BEG / ML_NBCAR;
        g.drawString(" Black (board 1)", x, y);

//        x = usableX + usableWidth * (ML_HD_BEG + ML_HD_LEN)/ ML_NBCAR;
//        drawRightAlignedString(g, "Hd", x, y);
        x = usableX + usableWidth * (ML_RES_BEG + ML_RES_LEN) / ML_NBCAR;
        drawRightAlignedString(g, "Res", x, y);
    }

    private void printStandingsHeaderLine(Graphics g, PageFormat pf, int pi) {
        int y = usableY + 3 * lineHeight;
        int x = usableX + usableWidth * (ST_NUM_BEG + ST_NUM_LEN) / numberOfCharactersInALine;
        TournamentPrinting.drawRightAlignedString(g, "Num", x, y);
        x = usableX + usableWidth * (ST_PL_BEG + ST_PL_LEN) / numberOfCharactersInALine;
        TournamentPrinting.drawRightAlignedString(g, "Pl", x, y);
        x = usableX + usableWidth * TournamentPrinting.ST_NF_BEG / this.numberOfCharactersInALine;
        g.drawString("Name", x, y);
        x = usableX + usableWidth * (TournamentPrinting.ST_RK_BEG + TournamentPrinting.ST_RK_LEN) / this.numberOfCharactersInALine;
        TournamentPrinting.drawRightAlignedString(g, "Rk", x, y);
        x = usableX + usableWidth * (TournamentPrinting.ST_NBW_BEG + TournamentPrinting.ST_NBW_LEN) / this.numberOfCharactersInALine;
        TournamentPrinting.drawRightAlignedString(g, "NBW", x, y);

        int numberOfRoundsPrinted = roundNumber + 1;
        int rBeg = ST_ROUND0_BEG;
        for (int r = 0; r < numberOfRoundsPrinted; r++) {
            int xR = usableX + usableWidth * (rBeg + (r + 1) * ST_ROUND_LEN) / numberOfCharactersInALine;
            String strRound = "R" + (r + 1);
            TournamentPrinting.drawRightAlignedString(g, strRound, xR, y);
        }
        int numberOfCriteriaPrinted = printCriteria.length;
        int cBeg = rBeg + numberOfRoundsPrinted * ST_ROUND_LEN;
        for (int iC = 0; iC < numberOfCriteriaPrinted; iC++) {
            int xC = usableX + usableWidth * (cBeg + (iC + 1) * ST_CRIT_LEN) / numberOfCharactersInALine;
            String strCrit = PlacementParameterSet.criterionShortName(printCriteria[iC]);
            TournamentPrinting.drawRightAlignedString(g, strCrit, xC, y);
        }
    }

    private void printTeamsStandingsHeaderLine(Graphics g, PageFormat pf, int pi) {
        int y = usableY + 3 * lineHeight;
        int x = usableX + usableWidth * (TST_NUM_BEG + TST_NUM_LEN) / numberOfCharactersInALine;
        TournamentPrinting.drawRightAlignedString(g, "Num", x, y);
        x = usableX + usableWidth * (TST_PL_BEG + TST_PL_LEN) / numberOfCharactersInALine;
        TournamentPrinting.drawRightAlignedString(g, "Pl", x, y);
        x = usableX + usableWidth * TST_TN_BEG / this.numberOfCharactersInALine;
        g.drawString("Team name", x, y);

        int numberOfRoundsPrinted = roundNumber + 1;
        int rBeg = TST_ROUND0_BEG;
        for (int r = 0; r < numberOfRoundsPrinted; r++) {
            int xR = usableX + usableWidth * (rBeg + (r + 1) * TST_ROUND_LEN) / numberOfCharactersInALine;
            String strRound = "R" + (r + 1);
            TournamentPrinting.drawRightAlignedString(g, strRound, xR, y);
        }
        int numberOfCriteriaPrinted = printCriteria.length;
        int cBeg = rBeg + numberOfRoundsPrinted * TST_ROUND_LEN;
        for (int iC = 0; iC < numberOfCriteriaPrinted; iC++) {
            int xC = usableX + usableWidth * (cBeg + (iC + 1) * TST_CRIT_LEN) / numberOfCharactersInALine;
            String strCrit = TeamPlacementParameterSet.criterionShortName(printCriteria[iC]);
            TournamentPrinting.drawRightAlignedString(g, strCrit, xC, y);
        }
    }

    private void printPageFooter(Graphics g, PageFormat pf, int pi) {
        int footerFontSize = fontSize;
        if (footerFontSize > 12) {
            footerFontSize = 12;
        }
        Font f = new Font("Default", Font.BOLD, footerFontSize);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        String strName = "";
        try {
            strName = tournament.getTournamentParameterSet().getGeneralParameterSet().getName();
        } catch (RemoteException ex) {
            Logger.getLogger(TournamentPrinting.class.getName()).log(Level.SEVERE, null, ex);
        }
        String strLeft = Gotha.getGothaVersionnedName() + " : " + strName + "    ";

        // Center part of footer
        String strCenter = "Page" + " " + (pi + 1) + "/" + numberOfPages;

        char[] tcLeft = strLeft.toCharArray();
        char[] tcCenter = strCenter.toCharArray();
        int wLeft = fm.charsWidth(tcLeft, 0, tcLeft.length);
        int wCenter = fm.charsWidth(tcCenter, 0, tcCenter.length);
        while (wLeft + wCenter / 2 > usableWidth / 2) {
            if (strLeft.length() <= 2) {
                break;
            }
            strLeft = strLeft.substring(0, strLeft.length() - 2);
            tcLeft = strLeft.toCharArray();
            wLeft = fm.charsWidth(tcLeft, 0, tcLeft.length);
        }
        strLeft = strLeft.substring(0, strLeft.length() - 2);
        g.drawString(strLeft, usableX, usableY + usableHeight - fm.getDescent());
        int x = usableX + (usableWidth - wCenter) / 2;
        g.drawString(strCenter, x, usableY + usableHeight - fm.getDescent());

        // Right part of footer
        java.util.Date dh = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm  ");
        String strDH = sdf.format(dh);
        String strRight = strDH;
        x = usableX + usableWidth;
        drawRightAlignedString(g, strRight, x, usableY + usableHeight - fm.getDescent());
    }

    private int printADefaultPage(Graphics g, PageFormat pf, int pi) {
        if (pi > 0) {
            return NO_SUCH_PAGE;
        }
        g.drawString("This page is printed for test only", usableX, usableY + 20);

        g.drawString("uX = " + usableX, 10, 100);
        g.drawString("uY = " + usableY, 10, 120);
        g.drawString("uW = " + usableWidth, 10, 140);
        g.drawString("uH = " + usableHeight, 10, 160);

        g.drawRect(usableX, usableY, usableWidth - 1, usableHeight - 1);

        g.setFont(new Font("Default", Font.PLAIN, 40));
        g.drawString("Font size = 40", usableX, usableY + 260);
        g.setFont(new Font("Default", Font.PLAIN, 18));
        g.drawString("Font size = 18", usableX, usableY + 360);
        g.setFont(new Font("Default", Font.PLAIN, 16));
        g.drawString("Font size = 12", usableX, usableY + 420);
        g.setFont(new Font("Default", Font.PLAIN, 7));
        g.drawString("Font size =  7", usableX, usableY + 500);
        g.setFont(new Font("Default", Font.PLAIN, 6));
        g.drawString("Font size =  5", usableX, usableY + 540);
        g.setFont(new Font("Default", Font.PLAIN, 4));
        g.drawString("Font size =  3", usableX, usableY + 580);
        g.setFont(new Font("Default", Font.PLAIN, 2));

        Font f = new Font("Default", Font.PLAIN, 100);
        FontMetrics fm = g.getFontMetrics(f);
        g.setFont(new Font("Default", Font.PLAIN, 12));
        g.drawString("Font size = 100", usableX + 120, usableY + 380);
        g.drawString("Leading = " + fm.getLeading(), usableX + 120, usableY + 400);
        g.drawString("Ascent = " + fm.getAscent(), usableX + 120, usableY + 420);
        g.drawString("MaxAscent = " + fm.getMaxAscent(), usableX + 120, usableY + 440);
        g.drawString("Descent = " + fm.getDescent(), usableX + 120, usableY + 460);
        g.drawString("MaxDescent = " + fm.getMaxDescent(), usableX + 120, usableY + 480);
        g.drawString("Height = " + fm.getHeight(), usableX + 120, usableY + 500);
        char[] tci = {'i', 'i', 'i', 'i', 'i', 'i', 'i', 'i', 'i', 'i'};
        g.drawString("charsWidth(\"i\") = " + fm.charsWidth(tci, 0, 1), usableX + 120, usableY + 520);
        g.drawString("charsWidth(\"iiiiiiiiii\") = " + fm.charsWidth(tci, 0, 10), usableX + 120, usableY + 540);
        char[] tcw = {'Æ'};
        g.drawString("charsWidth(\"Æ\") = " + fm.charsWidth(tcw, 0, 1), usableX + 120, usableY + 560);

        g.drawLine(100, 100, 500, 100);
        g.drawLine(100, 100 + fm.getLeading(), 500, 100 + fm.getLeading());
        g.drawLine(100, 100 + fm.getLeading() + fm.getAscent(), 500, 100 + fm.getLeading() + fm.getAscent());
        g.drawLine(100, 100 + fm.getLeading() + fm.getAscent() + fm.getDescent(), 500, 100 + fm.getLeading() + fm.getAscent() + fm.getDescent());
        g.setFont(f);
        g.drawString("aç_ÀÆÑÔ", 50, 100 + fm.getLeading() + fm.getAscent());

        // A typical 100 points font gives a total height of 127 points :
        // leading = 4
        // ascent = 101
        // descent = 22
        // width = 22 for "i", 95 for "W", 100 for "Æ"

        return PAGE_EXISTS;
    }

    private static void drawRightAlignedString(Graphics g, String str, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        char[] tc = str.toCharArray();
        int w = fm.charsWidth(tc, 0, str.length());
        int xLeft = x - w;
        g.drawString(str, xLeft, y);

    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setAlOrderedScoredPlayers(ArrayList<ScoredPlayer> alOrderedScoredPlayers) {
        this.alOrderedScoredPlayers = alOrderedScoredPlayers;
    }

    public void setPrintCriteria(int[] printCriteria) {
        this.printCriteria = new int[printCriteria.length];
        System.arraycopy(printCriteria, 0, this.printCriteria, 0, printCriteria.length);
    }

    /**
     * @param sts the sts to set
     */
    public void setScoredTeamsSet(ScoredTeamsSet scoredTeamsSet) {
        this.scoredTeamsSet = scoredTeamsSet;
    }
}