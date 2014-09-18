package com.neurogrid.simulation.root;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * You may find further details about this software at
 * http://www.neurogrid.net/
 *
 */
 
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;


/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * Message Test class <br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   23/May/2003   sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */


public class MessageTest
    extends TestCase
{	
  private static final String cvsInfo = "$Id: MessageTest.java,v 1.2 2003/06/25 11:50:45 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  private static Category o_cat = Category.getInstance(MessageTest.class.getName());  
  
  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
	BasicConfigurator.configure(); 
	PropertyConfigurator.configure(p_conf);  	
	o_cat.info("MessageTest logging Initialized");
  }
     
  
  private Message o_message = null;

  public Message createInstance() 
    throws Exception 
  {
    return new Message()
        {
          /**
           * Get String representation 
           *
           * @return String
           */
          public String toString()
          {
            StringBuffer x_buf = new StringBuffer(100);
            x_buf.append(o_message_ID);
            x_buf.append(": TTL-");
            x_buf.append(o_TTL);

            return x_buf.toString();
          }
  
          /**
           * Get possible matches for this query in this network
           *
           * @param p_network
           *
           * @return int
           */
          public int getPossibleMatches(Network p_network)
            throws Exception
          {
            return -1;
          }                  
        };
  }
 
  /**
   * Subclasses must invoke this from their constructor.
   */
  public MessageTest(String p_name) 
    throws Exception
  {
    super(p_name);
    
  }



  protected void setUp() 
    throws Exception 
  {
    super.setUp();
    o_message = createInstance();
    MessageTest.init(System.getProperty( "Log4jConfig" ));

  }
  
  protected void tearDown() throws Exception 
  {
    o_message = null;
    super.tearDown(); 

  }

/*
String  getMessageID () 
Node  getStart () 
abstract String  toString () 
Node  getLocation () 
void  setLocation (Node p_here) throws Exception  
Node  getPreviousLocation () 
void  setPreviousLocation (Node p_previous) 
int  getTTL () 
void  decrementTTL () 
abstract int  getPossibleMatches (Network p_network) throws Exception 


*/
  /**
   * test that we can delete a UriTriple ....
   * 
   * @throws Exception      a general exception
   */
  public void testGetMessageID() 
    throws Exception
  {
    String x_message_id = o_message.getMessageID();
    assertTrue("id not null",x_message_id == null);
  }
 
  /**
   * main method that supports individual running
   * of different test methods
   *
   * @param args   array of strings that are incoming arguments
   */  
  public static void main(String[] args) 
  {
    try
    {
      String x_method = System.getProperty("test.method");
      System.out.println("test.method="+x_method);
      if(x_method == null || x_method.equals(""))
      {
        System.out.println("testing all methods");
    
        junit.textui.TestRunner.run(MessageTest.class);
      }
      else
      {
        System.out.println("testing single method");

        TestSuite suite = new TestSuite();
        suite.addTest(new MessageTest(x_method));
        junit.textui.TestRunner.run(suite);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

}