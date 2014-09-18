package com.neurogrid.simulation;

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


 // Import log4j classes.
import java.text.DecimalFormat;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
 
 /**
 * Copyright (c) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * SimpleKeyword is an example of the kind of keyword that can be created using the 
 * base abstract class.  SimpleKeyword creates an ID of the form KEY_####### and each new keyword
 * that is instantiated is unique and if the most recently created keyword was KEY_00001 then the
 * next keyword will be KEY_00002. The keywords and their ids are placed in the appropriate
 * reference hashtables as provided by the base Keyword class.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   12/Mar/2001    sam       Created file<br>
 * 0.1   13/Mar/2003    sam       Added logging support<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class SimpleKeyword extends com.neurogrid.simulation.root.Keyword
{
  private static final String cvsInfo = "$Id: SimpleKeyword.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo() { return cvsInfo; }

  private static Category o_cat = Category.getInstance(SimpleKeyword.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("SimpleKeyword logging Initialized");
  }

  // private variables

  public static final DecimalFormat o_df = new DecimalFormat("######");

  static { o_df.setMinimumIntegerDigits(6); }

  /**
   * Get a new key, incrementing along the static global keyword id to make sure this
   * is a unique key
   *
   * @return long
   */
  private static long getNewKey()
  {
    return ++o_global_keyword_id;
  }

  /**
   * Get a new keyword id formatted according to the KEY_###### style
   *
   * @return String
   */
  private String getNewKeywordID()
  {
    o_key = getNewKey();
    return getFormattedKeywordID(o_key);
  }

  public static final String o_id_head = "KEY_";
  private static StringBuffer o_buf = new StringBuffer(10);
  static { o_buf.append(o_id_head); }

  /**
   * Get a keyword id formatted according to the KEY_###### style
   *
   * @return String
   */
  public static String getFormattedKeywordID(long p_key)
  {
    o_buf.delete(o_id_head.length(),o_buf.length());
    o_buf.append(o_df.format(p_key));
    return o_buf.toString();
  }

  /**
   * Keyword Constructor
   *
   * Creates a new keyword with a new keyword id of the format KEY_######
   * using the global keyword id to generate the ###### number in the keyword id string.
   * The global keyword id is a static variable which is incremented each time a new keyword is created 
   * making sure that each keyword had a unique id.
   * The keyword id string and global keyword id for that keyword id are then stored in a hashtable, 
   * so that a keyword id string can be quickly retrieved from the global id.
   * Another hashtable is also set up linking the keyword id string to the actual keyword object
   *
   */
  public SimpleKeyword()
  {
    o_keyword_ID = getNewKeywordID();
    o_cat.debug("new keyword: "+ o_global_keyword_id + "::"+ o_keyword_ID);
    o_keyword_ids.put(new Long(o_global_keyword_id),o_keyword_ID);
    o_keywords.put(o_keyword_ID,this);
  }

  /**
   * Get String representation
   *
   * @return String
   */
  public String toString()
  {
    return o_keyword_ID;
  }
  
  /**
   * check if these Simple Keywords are equal
   *
   * @param SimpleKeyword
   *
   * @return boolean
   */
  public boolean equals(SimpleKeyword p_keyword)
  {
    return o_keyword_ID.equals(p_keyword.getKeywordID());
  }
  
  /**
   * main for testing purposes
   *
   */
   /*
  public static void main(String[] args)
  {
    SimpleKeyword x_keyword = null;
    
    for(int i=0;i<10;i++)
    {
      x_keyword = new SimpleKeyword();	
      System.out.println(x_keyword.toString() + " : " + x_keyword.getKeywordID() + " : " + x_keyword.getKey());
    }
  }
  */
}

