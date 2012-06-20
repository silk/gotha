/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.vannier.gotha;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * LogElements contains a set of LogElement
 * A LogElement 
 * @author Luc
 * 
 */
public class LogElements {
        
    public static void incrementElement(String name, String value){
        Preferences prefsRoot = Preferences.userRoot();
        Preferences logPrefs = prefsRoot.node(Gotha.strPreferences + "/log");
        
        String strK = name + ":" + value;
        String strNbOcc = logPrefs.get(strK, "0");
        int nbOcc = Integer.parseInt(strNbOcc);
        nbOcc++;
        logPrefs.put(strK, "" + nbOcc);
    }
    
    public static void removeElement(String name, String value){
        Preferences prefsRoot = Preferences.userRoot();
        Preferences logPrefs = prefsRoot.node(Gotha.strPreferences + "/log");
        
        String strK = name + ":" + value;
        logPrefs.remove(strK);
    }
    
    public static void sendLogElements(){
        Preferences prefsRoot = Preferences.userRoot();
        Preferences logPrefs = prefsRoot.node(Gotha.strPreferences + "/log");
        String[] keys = null;
        try {
            keys = logPrefs.keys();
        } catch (BackingStoreException ex) {
            Logger.getLogger(LogElements.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String key : keys){
            int pos = key.indexOf(":");
            String name = key.substring(0, pos);
            String value = key.substring (pos + 1, key.length());
            String strNbOcc = logPrefs.get(key, "0");
            int nbOcc = Integer.parseInt(strNbOcc);
//            System.out.println("name = " + name + " value = " + value + " nbOcc = " + nbOcc);

            String strLog = "name=" + name + "&value= " + value + "&nbocc=" + nbOcc;
            strLog = strLog.replaceAll(" ", "%20");
                     
            URI uri = null;
            try {
                uri = new URI("http://vannier.info/gotha/log.php?" + strLog);
            } catch (URISyntaxException ex) {
                Logger.getLogger(LogElements.class.getName()).log(Level.SEVERE, null, ex);
            }
            URL url = null;
            try {
                url = uri.toURL();
            } catch (MalformedURLException ex) {
                Logger.getLogger(LogElements.class.getName()).log(Level.SEVERE, null, ex);
            }

            BufferedInputStream bis = null;
           
            String strReceived = "";
            URLConnection urlc;
            try {
                urlc = url.openConnection();
                urlc.getInputStream();
                bis = new BufferedInputStream(urlc.getInputStream());
                int i;
                while((i = bis.read()) != -1){
                    char c = (char) i;
                    strReceived += c;
                }
                bis.close();
               LogElements.removeElement(name, value);
            } catch (IOException ex) {
                System.out.println("sendLogElements : IOException");
                return;
                // Logger.getLogger(LogElements.class.getName()).log(Level.SEVERE, null, ex);
            }
//             System.out.println("strReceived = " + strReceived);
        }

    }
    
}

/**
 * 
 * @author Luc
 */
class LogElement{
    private String name;
    private String value = "";
    private int nbOcc = 0;
    
    public LogElement(String name, String value){
        this.name = name;
        this.value = value;
        this.nbOcc = 0;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the nbOcc
     */
    public int getNbOcc() {
        return nbOcc;
    }

    /**
     * @param nbOcc the nbOcc to set
     */
    public void setNbOcc(int nbOcc) {
        this.nbOcc = nbOcc;
    }
    

}

// LogElements (open list)
// 1/ with value = ""

//tournament.import.h9              Vu
//tournament.import.tou             Vu
//tournament.import.wallist         Vu
//tournament.import.vbs             Vu
//tournament.export.egf             Vu
//tournament.export.ffg             Vu
//tournament.export.html            Vu
//tournament.export.teamhtml        Vu  
//tournament.export.csv             Vu
//tournament.build                  Vu
//players.manager
//players.manager.egfratinglist
//players.manager.ffgratinglist
//players.manager.levenshtein
//players.manager.register
//players.manager.welcomesheet
//players.qc
//players.ur
//players.mmg
//players.tm
//games.pair                        Vu
//games.results
//games.roundrobin
//games.teamspairing
//options.tournament
//options.games
//help.og
//help.about

// 2/ with an actual value
//gotha.os strOS                    Vu
//gotha.jre strJRE                  Vu
//gotha.version                     Vu
//gotha.runningmode runningMode     Vu  
//tournament.open fileName 


