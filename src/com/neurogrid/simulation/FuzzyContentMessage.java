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
 
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.simulation.root.ContentMessage;
import com.neurogrid.simulation.root.Keyword;
import com.neurogrid.simulation.root.Network;
import com.neurogrid.simulation.root.Node;

 /**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class represents messages in the network where the search target is specified
 * only in terms of keywords, not a document id, i.e. a fuzzy search.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   25/Mar/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class FuzzyContentMessage extends com.neurogrid.simulation.root.ContentMessage
{
  private static final String cvsInfo = "$Id: FuzzyContentMessage.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  private static Category o_cat = Category.getInstance(FuzzyContentMessage.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("FuzzyContentMessage logging Initialized");
  }

  // private variables

  private static DecimalFormat o_df = new DecimalFormat("######");
  // should think about possible clocking of message ids in large simulations

  static
  {
    o_df.setMinimumIntegerDigits(6);
  }

  private static String getNewMessageID()
  {
    StringBuffer x_buf = new StringBuffer(10);
    x_buf.append("FUZ_MES_");
    x_buf.append(o_df.format(++o_global_message_id));
    return x_buf.toString();
  }

  /**
   * FuzzyContentMessage Constructor - used when initially sending the message
   *
   * The message constructor creates a message with a unique ID of the form MES_######
   * incrementing a static message id counter that gives the long value of the ###### in the message
   * id string, ensuring that each message has a unique id.
   *
   * The TTL, keywords, and starting node are set as specified and the message is put into 
   * a hashtable indexed via its message ID string, and into a multihashtable indexed via its TTL value
   *
   * @param p_TTL              the time to live (TTL) of the message
   * @param p_keywords         the keywords that the message will use as search criteria
   * @param p_start            the node at which the message originated
   */
  public FuzzyContentMessage(int p_TTL, Keyword[] p_keywords, Node p_start)
    throws Exception
  {
    if(p_TTL < 1 ) throw new Exception("FuzzyContentMessage Constructor: TTL must be 1 or more");
    if(p_keywords == null || p_keywords.length < 1 ) throw new Exception("FuzzyContentMessage Constructor: must have at least one keyword");
    if(p_start == null) throw new Exception("FuzzyContentMessage Constructor: Node is null");
    o_message_ID = getNewMessageID();
    o_TTL = p_TTL;
    o_keywords = p_keywords;
    o_start = p_start;
    o_messages.put(o_message_ID,this);
    if(o_cat.isDebugEnabled())
      o_cat.debug("adding "+o_TTL+" ttl message id "+ o_message_ID +" to ttl table");
    o_messages_by_TTL.put(Integer.toString(o_TTL),this);
    if(o_cat.isDebugEnabled())
      o_cat.debug("ttl "+o_TTL+" table size = "+ ((Vector)(o_messages_by_TTL.get(Integer.toString(o_TTL)))).size());

  }

  /**
   * FuzzyContentMessage Constructor - used when forwarding the message
   *
   * This constructor allows a message to be constructed using the information contained in some other
   * message.  The idea is that when a message is forwarded a new message is created that is a copy of the
   * original message.  The constructor also adds this new message to the 
   * multihashtable that stores messages via TTL under a new TTL.
   *
   * The forwarding process was set up like this for simplicity, but a more efficient implementation would 
   * maintain the same message object and remove the message object from the TTL indexed multihashtable, and
   * re-enter it under the new TTL.  This should be on the todo list as soon as we have worries about memory
   * usage.
   *
   * @param p_message
   */
  public FuzzyContentMessage(ContentMessage p_message)
    throws Exception
  {
    if(p_message == null) throw new Exception("FuzzyContentMessage Constructor: Message is null");
    o_message_ID = p_message.getMessageID();
    o_keywords = p_message.getKeywords();
    o_TTL = p_message.getTTL();
    o_previous = p_message.getPreviousLocation();
    o_start = p_message.getStart();
    if(o_cat.isDebugEnabled())
      o_cat.debug("adding "+o_TTL+" ttl message id "+ o_message_ID +" to ttl table");
    o_messages_by_TTL.put(Integer.toString(o_TTL),this);
    if(o_cat.isDebugEnabled())
      o_cat.debug("ttl "+o_TTL+" table size = "+ ((Vector)(o_messages_by_TTL.get(Integer.toString(o_TTL)))).size());
  }

  /**
   * Get String representation - including the TTL, and keywords
   *
   * @return String
   */
  public String toString()
  {
    StringBuffer x_buf = new StringBuffer(100);
    x_buf.append(o_message_ID);
    x_buf.append(": TTL-");
    x_buf.append(o_TTL);
    x_buf.append(": Keywords-");
    for(int i=0;i<o_keywords.length;i++)
    {
      x_buf.append(o_keywords[i]);
      x_buf.append(": ");
    }

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
    return p_network.hasKeywords(o_keywords);  
  }

}

