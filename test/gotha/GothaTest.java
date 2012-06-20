/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gotha;


import info.vannier.gotha.Gotha;
import junit.framework.TestCase;

/**
 *
 * @author Admin
 */
public class GothaTest extends TestCase {
    
    public GothaTest(String testName) {
        super(testName);
    }

    /**
     * Test of leftString method, of class Gotha.
     */
    public void testLeftString() {
        System.out.println("leftString. Test 1");
        assertEquals("abc", Gotha.leftString("abcdef", 3));
        System.out.println("leftString. Test 2");
        assertEquals("", (Gotha.leftString("abcdef", 0)));
        System.out.println("leftString. Test 3");
        assertEquals("abcdef", (Gotha.leftString("abcdef", 6)));
        System.out.println("leftString. Test 4");
        assertEquals("abcdef ", (Gotha.leftString("abcdef", 7)));
        System.out.println("leftString. Test 5");
        assertEquals(" abcdef", (Gotha.leftString(" abcdef", 7)));    }


}
