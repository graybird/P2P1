/*
 * Created on 2003/06/25
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.neurogrid.simulation.root;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:sam@neurogrid.com">Sam Joseph</a>
 *
 * test the basic keyword class
 */
public class KeywordTest extends TestCase
{
	private static Category o_cat = Category.getInstance(KeywordTest.class.getName());  
  
	/**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
	public static void init(String p_conf)
	{
	  BasicConfigurator.configure(); 
	  PropertyConfigurator.configure(p_conf);  	
	  o_cat.info("KeywordTest logging Initialized");
	}

    private Keyword o_keyword = null;

	/**
	 * Constructor for KeywordTest.
	 * @param arg0 the test name
	 */
	public KeywordTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * @return Keyword - a Keyword instance
	 * @throws Exception a general exception
	 */
	public static Keyword createInstance() 
	  throws Exception 
	{
	  return new Keyword()
		  {
			/**
			 * Get String representation 
			 *
			 * @return String
			 */
			public String toString()
			{
				return o_keyword_ID;
			}
		  };
	}

	/**
	 * @param args incoming arguments
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(KeywordTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		o_keyword = createInstance();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
		o_keyword = null;
	}

	/**
	 * test that the reset works
	 */
	public void testResetGlobalKeywordID()
	{
		Keyword.resetGlobalKeywordID();
		assertTrue("global keyword ID not reset",Keyword.getGlobalKeywordID()==0);
	}

	/**
	 *  test we can get the keyword ID
	 */
	public void testGetKeywordID()
	{
		assertTrue("failed to get Keyword ID",o_keyword.getKeywordID()==Keyword.DEFAULT_ID);
	}

	/**
	 *  test we can get the key
	 */
	public void testGetKey()
	{
		assertTrue("failed to get Key",o_keyword.getKey()==Keyword.DEFAULT_KEY);
	}

	/**
	 * test we can get the rarity
	 */
	public void testGetRarity()
	{
		assertTrue("failed to get rarity",o_keyword.getRarity()==Keyword.DEFAULT_RARITY);
	}

	/**
	 * test we can set the rarity
	 */
	public void testSetRarity()
	{
		final int x_new_rarity = 10;
		o_keyword.setRarity(x_new_rarity);	
		assertTrue("failed to set rarity",o_keyword.getRarity()==x_new_rarity);
	}


}
