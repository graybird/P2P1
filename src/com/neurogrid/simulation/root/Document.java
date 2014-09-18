package com.neurogrid.simulation.root;

/*
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
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.parser.MultiHashtable;

/**
* Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
*
* This abstract class represents documents in the network.<br><br>
*
* Change History<br>
* ------------------------------------------------------------------------------------<br>
* 0.0   6/Oct/2001    sam       Created file<br>
*
* @author Sam Joseph (sam@neurogrid.com)
*/

public abstract class Document implements Serializable, ZipfItem
{
	public static final String cvsInfo =
		"$Id: Document.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
	/**
	* @return the cvs string
	*/
	public static String getCvsInfo()
	{
		return cvsInfo;
	}

	private static Category o_cat =
		Category.getInstance(Document.class.getName());

	/**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
	public static void init(String p_conf)
	{
		BasicConfigurator.configure();
		PropertyConfigurator.configure(p_conf);
		o_cat.info("Document logging Initialized");
	}

	// variables
	
	public static final String DEFAULT_ID = "NULL";
	public static final long DEFAULT_KEY = 0L;
	public static final int DEFAULT_RARITY = 1;

	protected static long o_global_document_id = 0;
	protected long o_key = DEFAULT_KEY;
	protected String o_document_ID = DEFAULT_ID;
	protected Vector o_where = new Vector();
	protected Keyword[] o_keywords = null;
	protected int o_rarity = 1;


	public static final int DOCUMENT_HASH_MAP_STARTING_SIZE = 500;
	public static final int DOCUMENT_ID_HASH_MAP_STARTING_SIZE = 500;
	public static final int DOCUMENT_KEYWORD_HASH_MAP_STARTING_SIZE = 500;
	public static final int DOCUMENT_LOCATIONS_HASH_MAP_STARTING_SIZE = 500;

	public static HashMap o_documents =
		new HashMap(DOCUMENT_HASH_MAP_STARTING_SIZE);
	public static HashMap o_document_ids =
		new HashMap(DOCUMENT_ID_HASH_MAP_STARTING_SIZE);
	public static MultiHashtable o_documents_by_keyword =
		new MultiHashtable(DOCUMENT_KEYWORD_HASH_MAP_STARTING_SIZE);
	public static MultiHashtable o_document_locations =
		new MultiHashtable(DOCUMENT_LOCATIONS_HASH_MAP_STARTING_SIZE);
	// document objects against Node location elements

	/**
	 * get all the documents that contain a particular keyword
	 * 
	 * @param p_keyword - the keyword that the documents should contain
	 *
	 * @return Vector  a vector of documents
	 */
	public static Vector getDocumentsByKeyword(Keyword p_keyword)
	{
		return (Vector) (o_documents_by_keyword.get(p_keyword));
	}

	/**
	 * Get Document ID
	 *
	 * @return String the document ID
	 */
	public String getDocumentID()
	{
		return o_document_ID;
	}

	/**
	* Get Location
	* 
	* A method that gives us a list of all the nodes that contain this document.
	*
	* @return Vector  list of locations
	*/
	public Vector getLocation()
	{
		return o_where;
	}

	/**
	 * Set Location
	 *
	 * A method that allows us to specify which nodes contain a document.
	 *
	 * @param p_here    A Node in which the document is located
	 * 
	 * @throws Exception a general exception
	 */
	public void setLocation(Node p_here) throws Exception
	{
		if (p_here == null)
		{
			throw new Exception(
				o_document_ID + " setLocation() argument is null");
		}
		o_document_locations.put(this, p_here);
		o_where.addElement(p_here);
	}

	/**
	 * Get String representation
	 *
	 * @return String
	 */
	public abstract String toString();


	/**
	 * Get rarity
	 *
	 * @return int
	 */
	public int getRarity()
	{
		return o_rarity;
	}

	/**
	 * set rarity
	 *
	 * @param p_rarity the rarity of this document
	 */
	public void setRarity(int p_rarity)
	{
		o_rarity = p_rarity;
	}

	/**
	 * Get keywords
	 *
	 * @return Keyword[]
	 */
	public Keyword[] getKeywords()
	{
		return o_keywords;
	}

	/**
	 * check if the document contains this keyword
	 *
	 * @param p_keyword the keyword to check for
	 *
	 * @return boolean
	 */
	public boolean hasKeyword(Keyword p_keyword)
	{
		int x_sum = 0;
		for (int i = 0; i < o_keywords.length; i++)
		{
			if (o_keywords[i] == p_keyword)
			{
				x_sum++;
			}
		}

		if (x_sum > 0)
		{
			return true;
		}

		return false;
	}

	/**
	 * Get random keyword
	 *
	 * @return Keyword[]
	 */
	public Keyword getRandomKeyword()
	{
		double x_random = Math.random();
		int x_pick = (int) (x_random * o_keywords.length);

		return o_keywords[x_pick];
	}

	/**
	 * Get random keyword excluding those in the hashtable provided
	 *
	 * @param p_exclude keywords to exclude from random selection
	 *
	 * @return Keyword[]
	 */
	public Keyword getRandomKeyword(Hashtable p_exclude)
	{
		Vector x_vector = new Vector(o_keywords.length);
		for (int i = 0; i < o_keywords.length; i++)
		{
			if (p_exclude.get(o_keywords[i]) == null)
			{
				x_vector.addElement(o_keywords[i]);
			}
		}

		if (x_vector.size() == 0)
		{
			return null;
		}


		double x_random = Math.random();
		int x_pick = (int) (x_random * x_vector.size());

		return (Keyword) x_vector.elementAt(x_pick);
	}

	/**
	 * @param p_object    an object to heck to see if it is null
	 * @param p_item      a string to describe the object
	 * @throws Exception  implies the object is null
	 */
	public static void check(Object p_object, String p_item) throws Exception
	{
		if (p_object == null)
		{
			throw new Exception(p_item + " is null");
		}
		//System.out.println(p_item + " is " + (p_object == null?"null":"not null"));	
	}

}
