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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

/**
* Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
*
* This abstract class represents keywords in the network.<br><br>
*
* Change History<br>
* ------------------------------------------------------------------------------------<br>
* 0.0   6/Oct/2001    sam       Created file<br>
*
* @author Sam Joseph (sam@neurogrid.com)
*/

public abstract class Keyword implements Serializable, ZipfItem
{
	private static final String cvsInfo =
		"$Id: Keyword.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
		
	/**
	* @return the cvs string
	*/
	public static String getCvsInfo()
	{
		return cvsInfo;
	}

	private static Category o_cat =
		Category.getInstance(Keyword.class.getName());

	/**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
	public static void init(String p_conf)
	{
		BasicConfigurator.configure();
		PropertyConfigurator.configure(p_conf);
		o_cat.info("Keyword logging Initialized");
	}

	public static final String DEFAULT_ID = "NULL";
	public static final long DEFAULT_KEY = 0L;
	public static final int DEFAULT_RARITY = 1;

	protected static long o_global_keyword_id = 0;
	protected long o_key = DEFAULT_KEY;
	protected int o_rarity = DEFAULT_RARITY;
	protected String o_keyword_ID = DEFAULT_ID;
	


	/**
	 * reset the overall static global keyword ids, which we need when starting the network from
	 * scratch
	 */
	public static void resetGlobalKeywordID()
	{
		o_global_keyword_id = 0;
	}

	/**
	 * @return int the global keyword id
	 */
	public static long getGlobalKeywordID()
	{
		return o_global_keyword_id;
	}

	// perhaps these should be protected and then access methods defined in extended classes
	// if they were defined as lists or maps then we would have a choice about how they were
	// implemented ....

	public static final int KEYWORD_ID_HASH_MAP_STARTING_SIZE = 500;
	public static final int KEYWORD_HASH_MAP_STARTING_SIZE = 500;
	
	public static HashMap o_keyword_ids = new HashMap(KEYWORD_ID_HASH_MAP_STARTING_SIZE);
	public static HashMap o_keywords = new HashMap(KEYWORD_HASH_MAP_STARTING_SIZE);
	// could we replace these with LinkedLists? 
	// how efficient is it to grab an element from a linked list?
	// do we have to run along it? guess we do ....
	// most efficient approach would be to index all items in arrays
	// resizing the arrays when necessary ....

	// not sure why I wanted linked lists -- ..?

	/**
	 * Get Keyword ID
	 *
	 * @return String
	 */
	public String getKeywordID()
	{
		return o_keyword_ID;
	}

	/**
	 * Get Key
	 *
	 * @return long
	 */
	public long getKey()
	{
		return o_key;
	}

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
	 * @param p_rarity the rarity of this keyword
	 */
	public void setRarity(int p_rarity)
	{
		o_rarity = p_rarity;
	}

	/**
	 * Get String representation
	 *
	 * @return String
	 */
	public abstract String toString();
}
