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
 
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class represents content messages in the network.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   23/May/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public abstract class ContentMessage extends Message
{
  // private variables
  public static final String cvsInfo = "$Id: ContentMessage.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  private static Category o_cat = Category.getInstance(ContentMessage.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("ContentMessage logging Initialized");
  }
  
  
  protected Keyword[] o_keywords = null;
  protected Document o_document = null;

  /**
   * Get Document - the document for which this message is a query
   *
   * @return Document
   */
  public Document getDocument()
  {
    return o_document;
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
   * Get possible matches for this query in this network
   *
   * @param p_network
   *
   * @return int
   */
  public abstract int getPossibleMatches(Network p_network)
    throws Exception;  

}

