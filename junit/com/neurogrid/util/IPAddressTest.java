package com.neurogrid.util;

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
 * IP Address class <br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   26/May/2003   sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */


public class IPAddressTest
    extends TestCase
{	
  private static final String cvsInfo = "$Id: IPAddressTest.java,v 1.2 2003/06/25 11:50:47 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  private static Category o_cat = Category.getInstance(IPAddressTest.class.getName());  
  
  private IPAddress o_ip_address = null;
  private String o_test_ip = "127.0.0.1";

  public IPAddress createInstance() 
    throws Exception 
  {
    return new IPAddress(o_test_ip);
  }
  
  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("IPAddressTest logging Initialized");
  }
      
  /**
   * Subclasses must invoke this from their constructor.
   */
  public IPAddressTest(String p_name) 
    throws Exception
  {
    super(p_name);
  }



  protected void setUp() 
    throws Exception 
  {
    super.setUp();
    o_ip_address = createInstance();
    IPAddressTest.init(System.getProperty( "Log4jConfig" ));

  }
  
  protected void tearDown() throws Exception 
  {
    o_ip_address = null;
    super.tearDown(); 

  }


  /**
   * test that we can create an IP Address object
   * 
   * @throws Exception      a general exception
   */
  public void testEquals() 
    throws Exception
  {
    IPAddress x_ip1 = new IPAddress((byte)127,(byte)0,(byte)0,(byte)1);
    
    assertTrue("two alternate creation methods fail to create identical ips",
               o_ip_address.equals(x_ip1));
    
  }
 
  /**
   * test that we can create an IP Address object
   * 
   * @throws Exception      a general exception
   */
  public void testToString() 
    throws Exception
  {
    IPAddress x_ip1 = new IPAddress((byte)127,(byte)0,(byte)0,(byte)1);

    o_cat.debug("x_ip1: "+x_ip1);
    o_cat.debug("o_ip_address: "+o_ip_address);
    o_cat.debug("o_test_ip: "+o_test_ip);
    
    assertTrue("strings don't match",
               o_ip_address.toString().equals(x_ip1.toString()));
    
    assertTrue("string creation method doesn't match test ip string",
               o_ip_address.toString().equals(o_test_ip));
    
    assertTrue("byte creation method doesn't match test ip string",
               o_test_ip.equals(x_ip1.toString()));
    
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
    
        junit.textui.TestRunner.run(IPAddressTest.class);
      }
      else
      {
        System.out.println("testing single method");

        TestSuite suite = new TestSuite();
        suite.addTest(new IPAddressTest(x_method));
        junit.textui.TestRunner.run(suite);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

}