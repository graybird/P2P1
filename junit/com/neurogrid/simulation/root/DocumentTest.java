/*
 * Created on 2003/06/25
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.neurogrid.simulation.root;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:sam@neurogrid.com">Sam Joseph</a>
 *
 * test the base document class
 */
public class DocumentTest extends TestCase
{

	private static Category o_cat =
		Category.getInstance(DocumentTest.class.getName());

	/**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
	public static void init(String p_conf)
	{
		BasicConfigurator.configure();
		PropertyConfigurator.configure(p_conf);
		o_cat.info("DocumentTest logging Initialized");
	}

	private Document o_document = null;

	/**
	 * Constructor for DocumentTest.
	 * @param arg0 the test name
	 */
	public DocumentTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * @return Document - a Document instance
	 * @throws Exception a general exception
	 */
	public static Document createInstance() throws Exception
	{
		return new Document()
		{
			/**
			 * Get String representation 
			 *
			 * @return String
			 */
			public String toString()
			{
				return o_document_ID;
			}
		};
	}

	/**
	 * @param args incoming arguments
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(DocumentTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		o_document = createInstance();
		//init(System.getProperty("Log4JConfig"));
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
		o_document = null;
	}

	/**
	 *  test getting documents by keyword
	 */
	public void testGetDocumentsByKeyword()
	{
		Keyword x_keyword = null;
		try
		{
			x_keyword = KeywordTest.createInstance();
		}
		catch (Exception e)
		{
			o_cat.debug(e);
		}
		Vector x_docs = Document.getDocumentsByKeyword(x_keyword);
		assertTrue(
			"empty documents by keyword table returned something",
			x_docs == null);
	}

	/**
	 * test getting document id
	 */
	public void testGetDocumentID()
	{
		assertTrue(
			"failed to get Document ID",
			o_document.getDocumentID() == Document.DEFAULT_ID);
	}

	/**
	 *  test getting location
	 */
	public void testGetLocation()
	{
		assertTrue(
			"locations vector not initialized",
			o_document.getLocation() != null);
		assertTrue(
			"locations vector not empty",
			o_document.getLocation().size() == 0);
	}

	/**
	 * test setting the location
	 */
	public void testSetLocation()
	{
		Node o_node = null;
		try
		{
			o_node = NodeTest.createInstance();
			o_document.setLocation(o_node);
		}
		catch (Exception e)
		{
			o_cat.debug(e);
		}
		Vector x_vector = o_document.getLocation();
		assertTrue("locations vector not of length 1", x_vector.size() == 1);
		assertTrue(
			"locations vector not of length 1",
			x_vector.contains(o_node));
		assertTrue(
			"locations vector not of length 1",
			Document.o_document_locations.containsValue(o_node));
		// seems like the locations vector would be better off a hashtable or something
	}

	/**
	 *  test getting the rarity
	 */
	public void testGetRarity()
	{
		assertTrue("failed to get rarity",o_document.getRarity()==Document.DEFAULT_RARITY);
	}

	/**
	 *  test setting the rarity
	 */
	public void testSetRarity()
	{
		final int x_new_rarity = 10;
		o_document.setRarity(x_new_rarity);	
		assertTrue("failed to set rarity",o_document.getRarity()==x_new_rarity);
	}

	/**
	 * 
	 */
	public void testGetKeywords()
	{
		assertTrue("keywords not null",o_document.getKeywords()==null);
	}

	/**
	 * test the has keyword function
	 */
	public void testHasKeyword()
	{
		try
		{
			o_document.hasKeyword(KeywordTest.createInstance());
			fail("should have been exception since keywords are not initialized");
		}
		catch(Exception e)
		{
			o_cat.debug(e);
		}
	}

	/**
	 * Test for Keyword getRandomKeyword()
	 */
	public void testGetRandomKeyword()
	{
		try
		{
			o_document.getRandomKeyword();
			fail("should have been exception since keywords are not initialized");
		}
		catch(Exception e)
		{
			o_cat.debug(e);
		}	
    }

	/**
	 * Test for Keyword getRandomKeyword(Hashtable)
	 */
	public void testGetRandomKeywordHashtable()
	{
		try
		{
			Hashtable x_exclude = new Hashtable();
			Keyword x_keyword = KeywordTest.createInstance();
			x_exclude.put(x_keyword,x_keyword);
			o_document.getRandomKeyword(x_exclude);
			fail("should have been exception since keywords are not initialized");
		}
		catch(Exception e)
		{
			o_cat.debug(e);
		}		}

	/**
	 * test the checking function
	 */
	public void testCheck()
	{
		String x_test = "test";
		try
		{
			Document.check(null, x_test);
			fail("no exception");
		}
		catch(Exception e)
        {
           assertTrue("message incorrect", e.getMessage().equals(x_test + " is null"));	
        }

	}

}
