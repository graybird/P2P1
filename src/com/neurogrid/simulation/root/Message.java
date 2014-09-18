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
 
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.parser.MultiHashtable;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class represents messages in the network.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   6/Oct/2001    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public abstract class Message
{
  // private variables
  public static final String cvsInfo = "$Id: Message.java,v 1.3 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  private static Category o_cat = Category.getInstance(Message.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("Message logging Initialized");
  }
  
  
  protected static long o_global_message_id = 0;

  protected String o_message_ID = null;
  protected int o_TTL = 0;

  protected Node o_where = null;
  protected Node o_previous = null;
  protected Node o_start = null;

  public static Hashtable o_messages = new Hashtable(500);
  public static MultiHashtable o_messages_by_TTL = new MultiHashtable(500);

  /**
   * Get Message ID
   *
   * @return String
   */
  public String getMessageID()
  {
    return o_message_ID;
  }

  /**
   * Get Start - the node which generated the query
   *
   * @return Node
   */
  public Node getStart()
  {
    return o_start;
  }

  /**
   * Get String representation 
   *
   * @return String
   */
  public abstract String toString();


  /**
   * Get Location - get the current location of this message
   *
   * @return Node
   */
  public Node getLocation()
  {
    return o_where;
  }

  /**
   * Set Location - set the location of the message to a particular Node
   *
   * @param p_here
   */
  public void setLocation(Node p_here)
    throws Exception
  {
    if(p_here == null) throw new Exception(o_message_ID + " setLocation() argument is null");
    o_where = p_here;
  }

  /**
   * Get Previous Location - get the previous location of this message
   *
   * @return Node
   */
  public Node getPreviousLocation()
  {
    return o_previous;
  }

  /**
   * Set Previous Location - set the previous location of the message to a particular Node
   *
   * @param p_previous
   */
  public void setPreviousLocation(Node p_previous)
  {
    o_previous = p_previous;
  }

  /**
   * Get TTL
   *
   * @return int
   */
  public int getTTL()
  {
    return o_TTL;
  }

  /**
   * decrement TTL
   */
  public void decrementTTL()
  {
    o_TTL--;
    if(o_cat.isDebugEnabled())
      o_cat.debug("adding "+o_TTL+" ttl message id "+ o_message_ID +" to ttl table");
    o_messages_by_TTL.put(Integer.toString(o_TTL),this);
    if(o_cat.isDebugEnabled())
      o_cat.debug("ttl "+o_TTL+" table size = "+ ((Vector)(o_messages_by_TTL.get(Integer.toString(o_TTL)))).size());
  }


}

