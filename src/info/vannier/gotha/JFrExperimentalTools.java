/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JFrExperimentalTools.java
 */
package info.vannier.gotha;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Luc
 */
public class JFrExperimentalTools extends javax.swing.JFrame {
    private static final long REFRESH_DELAY = 2000;
    private long lastComponentsUpdateTime = 0;
    
    private TournamentInterface tournament;

    /** Creates new form JFrVariousTools */
    public JFrExperimentalTools(TournamentInterface tournament) throws RemoteException{
        this.tournament = tournament;
                
        initComponents();
        customInitComponents();
        setupRefreshTimer();
    }    
    
    private void setupRefreshTimer() {
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (tournament.getLastTournamentModificationTime() > lastComponentsUpdateTime) {
                        updateAllViews();
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        new javax.swing.Timer((int) REFRESH_DELAY, taskPerformer).start();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnShiftRatings = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnShiftNONEGFRatings = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        pnlForceASCII = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txfOrıgınal = new javax.swing.JTextField();
        btnTestconversıon = new javax.swing.JButton();
        txfConverted = new javax.swing.JTextField();
        btnForceConversion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        btnShiftRatings.setText("Shift ratings by +2050");
        btnShiftRatings.setEnabled(false);
        btnShiftRatings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShiftRatingsActionPerformed(evt);
            }
        });
        getContentPane().add(btnShiftRatings);
        btnShiftRatings.setBounds(10, 560, 330, 23);

        jLabel1.setForeground(new java.awt.Color(255, 0, 51));
        jLabel1.setText("Experimental tools");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(40, 30, 360, 14);

        btnShiftNONEGFRatings.setText("Shift non-EGF ratings by +2050");
        btnShiftNONEGFRatings.setEnabled(false);
        btnShiftNONEGFRatings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShiftNONEGFRatingsActionPerformed(evt);
            }
        });
        getContentPane().add(btnShiftNONEGFRatings);
        btnShiftNONEGFRatings.setBounds(10, 590, 330, 23);

        jLabel2.setForeground(new java.awt.Color(255, 0, 51));
        jLabel2.setText("Be cautious for use in actual tournaments");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(40, 50, 360, 14);

        pnlForceASCII.setBorder(javax.swing.BorderFactory.createTitledBorder("Force ASCII conversion"));
        pnlForceASCII.setLayout(null);

        jLabel3.setText("This converts Latin 9  and Turkish characters into theır usual ASCII equivalent");
        pnlForceASCII.add(jLabel3);
        jLabel3.setBounds(30, 30, 480, 14);

        txfOrıgınal.setText("IĞÜŞİÖÇ ığüşiöç ");
        pnlForceASCII.add(txfOrıgınal);
        txfOrıgınal.setBounds(20, 70, 370, 20);

        btnTestconversıon.setText("Test conversıon");
        btnTestconversıon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestconversıonActionPerformed(evt);
            }
        });
        pnlForceASCII.add(btnTestconversıon);
        btnTestconversıon.setBounds(20, 100, 370, 23);

        txfConverted.setBackground(new java.awt.Color(204, 255, 255));
        pnlForceASCII.add(txfConverted);
        txfConverted.setBounds(20, 130, 370, 20);

        btnForceConversion.setText("Force ASCII conversion for all players");
        btnForceConversion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForceConversionActionPerformed(evt);
            }
        });
        pnlForceASCII.add(btnForceConversion);
        btnForceConversion.setBounds(20, 200, 370, 23);

        getContentPane().add(pnlForceASCII);
        pnlForceASCII.setBounds(30, 90, 530, 280);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnShiftRatingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShiftRatingsActionPerformed
        ArrayList<Player> alPlayers = null;
        try {
            alPlayers = tournament.playersList();
        } catch (RemoteException ex) {
            Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Player p : alPlayers){
            p.setRating(p.getRating() + 2050);
            try {
                tournament.modifyPlayer(p, p);
            } catch (TournamentException ex) {
                Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tournamentChanged();
    }//GEN-LAST:event_btnShiftRatingsActionPerformed

    private void btnShiftNONEGFRatingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShiftNONEGFRatingsActionPerformed
                ArrayList<Player> alPlayers = null;
        try {
            alPlayers = tournament.playersList();
        } catch (RemoteException ex) {
            Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Player p : alPlayers){
            if (p.getRatingOrigin().equals("EGF")) continue;
            p.setRating(p.getRating() + 2050);
            try {
                tournament.modifyPlayer(p, p);
            } catch (TournamentException ex) {
                Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tournamentChanged();
    }//GEN-LAST:event_btnShiftNONEGFRatingsActionPerformed

    private void btnTestconversıonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestconversıonActionPerformed
        String strOrıgınal = this.txfOrıgınal.getText();
        String strConverted = Gotha.forceToASCII(strOrıgınal);
        this.txfConverted.setText(strConverted);
    }//GEN-LAST:event_btnTestconversıonActionPerformed

    private void btnForceConversionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForceConversionActionPerformed
        System.out.println("\nbtnForceConversionActionPerformed");
        ArrayList<Player> alPlayers = null;
        try {
            alPlayers = tournament.playersList();
        } catch (RemoteException ex) {
            Logger.getLogger(JFrPlayersMMG.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        int nbModifiedPlayers = 0;
        for (Player p : alPlayers){
            String newName = Gotha.forceToASCII(p.getName());
            String newFirstName = Gotha.forceToASCII(p.getFirstName());
            if (!newName.equals(p.getName()) || !newFirstName.equals(p.getFirstName())){
                Player newPlayer = new Player(p);
                newPlayer.setName(newName);
                newPlayer.setFirstName(newFirstName);
                try {
                    tournament.modifyPlayer(p, newPlayer);
                } catch (RemoteException ex) {
                    Logger.getLogger(JFrPlayersMMG.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                } catch (TournamentException ex) {
                    Logger.getLogger(JFrPlayersMMG.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                nbModifiedPlayers++;
            }
        }
        
        String strMessage = " players have been modified";
        if (nbModifiedPlayers <= 1) strMessage = " player has been modified";
        JOptionPane.showMessageDialog(this, "" + nbModifiedPlayers + strMessage, "Message", JOptionPane.INFORMATION_MESSAGE);

        this.tournamentChanged();        
        
    }//GEN-LAST:event_btnForceConversionActionPerformed

        /** This method is called from within the constructor to
     * initialize the form.
     * Unlike initComponents, customInitComponents is editable
     */
    private void customInitComponents() throws RemoteException {
        int w = JFrGotha.MEDIUM_FRAME_WIDTH;
        int h = JFrGotha.MEDIUM_FRAME_HEIGHT;
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((dim.width - w) / 2, (dim.height - h) / 2, w, h);

        setIconImage(Gotha.getIconImage());
 
        updateComponents();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnForceConversion;
    private javax.swing.JButton btnShiftNONEGFRatings;
    private javax.swing.JButton btnShiftRatings;
    private javax.swing.JButton btnTestconversıon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel pnlForceASCII;
    private javax.swing.JTextField txfConverted;
    private javax.swing.JTextField txfOrıgınal;
    // End of variables declaration//GEN-END:variables


    private void updateAllViews() {
        try {
            if (!tournament.isOpen()) dispose();
            this.lastComponentsUpdateTime = tournament.getCurrentTournamentTime();
            setTitle("Experimental tools");
        } catch (RemoteException ex) {
            Logger.getLogger(JFrExperimentalTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateComponents();
    }
    
    private void updateComponents(){

    }
    
    private void tournamentChanged(){
        try {
            tournament.setLastTournamentModificationTime(tournament.getCurrentTournamentTime());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

}
