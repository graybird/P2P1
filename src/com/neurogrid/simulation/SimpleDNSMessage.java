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

import com.neurogrid.simulation.root.Node;
import com.neurogrid.util.IPAddress;

 /**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class represents simple DNS message.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   22/May/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class SimpleDNSMessage extends com.neurogrid.simulation.root.Message
{
  private static final String cvsInfo = "$Id: SimpleDNSMessage.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  private static Category o_cat = Category.getInstance(SimpleDNSMessage.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("SimpleDNSMessage logging Initialized");
  }

  // private variables
  private static DecimalFormat o_df = new DecimalFormat("######");
  // should think about possible clocking of message ids in large simulations

  static
  {
    o_df.setMinimumIntegerDigits(6);
  }

  private static String o_domain_name = null;  //e.g."www.paolo.net"
  private static IPAddress o_ip = null; //e.g. 127.0.0.1  

  private static String getNewMessageID()
  {
    StringBuffer x_buf = new StringBuffer(10);
    x_buf.append("SIM_DNS_MES_");
    x_buf.append(o_df.format(++o_global_message_id));
    return x_buf.toString();
  }

  /**
   * SimpleDNSMessage Constructor - used when initially sending the message
   *
   * @param p_TTL              the time to live (TTL) of the message
   * @param p_domain_name      the domain name if we are a query response
   * @param p_ip               the associated ip address
   * @param p_start            the node at which the message originated       
   *
   * @throws Exception         general exception
   */
  public SimpleDNSMessage(int p_TTL,
                          String p_domain_name, 
                          IPAddress p_ip,
                          Node p_start)
    throws Exception
  {
    if(p_TTL < 1 ) throw new Exception("SimpleDNSMessage Constructor: TTL must be 1 or more");
    if(p_ip == null) throw new Exception("SimpleDNSMessage Constructor: IPAddress is null");
    if(p_start == null) throw new Exception("SimpleDNSMessage Constructor: Node is null");
    
    o_message_ID = getNewMessageID();
    o_TTL = p_TTL;
    o_domain_name = p_domain_name;  
    o_ip = p_ip; 
    o_start = p_start;
    
    // maybe this functionality below should go into the parent class - and we pass in the 
    // message identifier ??? or just the prefix ??? FIXXXXXXXXXXXXXXXXXXX

    if(o_cat.isDebugEnabled())
      o_cat.debug("adding "+o_TTL+" ttl message id "+ o_message_ID +" to ttl table");
    o_messages_by_TTL.put(Integer.toString(o_TTL),this);
    if(o_cat.isDebugEnabled())
      o_cat.debug("ttl "+o_TTL+" table size = "+ ((Vector)(o_messages_by_TTL.get(Integer.toString(o_TTL)))).size());
  
  }

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
    x_buf.append(", domain name: ");
    x_buf.append(o_domain_name);
    x_buf.append(", ip address: ");
    x_buf.append(o_ip);
    
    return x_buf.toString();
  }
  

}

