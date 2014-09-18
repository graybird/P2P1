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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.simulation.root.Document;
import com.neurogrid.simulation.root.Keyword;
import com.neurogrid.simulation.root.MessageHandler;
import com.neurogrid.simulation.root.Network;
import com.neurogrid.simulation.root.ResourceLimitedNode;
 
/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class is an adversarial node which misleads querants about the
 * contents it has to offer <br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.1   25/Mar/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class AdversarialNode extends ResourceLimitedNode
{
  public static final String cvsInfo = "$Id: AdversarialNode.java,v 1.3 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  private static Category o_cat = Category.getInstance(AdversarialNode.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("AdversarialNode logging Initialized");
  }
 
  // private variables

  private static DecimalFormat o_df = new DecimalFormat("######");

  static { o_df.setMinimumIntegerDigits(6); }

  public static String getNewNodeID()
  {
    StringBuffer x_buf = new StringBuffer(10);
    x_buf.append("ADV_NOD_");
    x_buf.append(o_df.format(++o_global_node_id));
    return x_buf.toString();
  }

  /**
   * Node Constructor
   *
   * create a node according to all the settings in the private variables, such as creating a new
   * unique ID of the form NOD_###### and creating hashtables to store the nodes connections.
   * Multihashtables for the nodes knowledge and contents, a hashset for the seen GUIDs and a 
   * vector for the inbox.
   *
   * The node object itself is put in the o_nodes static hashtable referenced via its unique id
   */
  public AdversarialNode(Network p_network, MessageHandler p_message)
  {
    super(4,2,12);
    o_node_ID = getNewNodeID();	
    o_network = p_network;
    o_cat.debug("new node: "+ o_global_node_id + "::"+ o_node_ID);
    p_network.addNode(this);
    o_message_handler = p_message;
  }


  /**
   * Node Constructor
   *
   * create a node according to all the settings in the private variables, such as creating a new
   * unique ID of the form ADV_NOD_###### and creating hashtables to store the nodes connections.
   * Multihashtables for the nodes knowledge and contents, a hashset for the seen GUIDs and a 
   * vector for the inbox.
   *
   * The node object itself is put in the o_nodes static hashtable referenced via its unique id
   *
   * @param p_network            the network this node will be a part of
   * @param p_handler            the message handler this node will use
   * @param p_max_connections    the maximum number of connections for this node
   * @param p_min_connections    the minimum number of connections for this node
   * @param p_max_knowledge      the maximum knowledge for this node
   */
  public AdversarialNode(Network p_network, 
                         MessageHandler p_message, 
                         int p_max_connections, 
                         int p_min_connections, 
                         int p_max_knowledge)
  {
    super(p_max_connections,p_min_connections,p_max_knowledge);
    o_node_ID = getNewNodeID();	
    o_network = p_network;
    o_cat.debug("new node: "+ o_global_node_id + "::"+ o_node_ID);
    p_network.addNode(this);
    o_message_handler = p_message;
  }

  /**
   * Get string representation
   *
   * Representation includes NODE_ID and complete list of IDs of connected nodes
   * as well as all the associations in the content and knowledge multihashtables
   *
   * @return String
   */
  public String toString()
  {
    StringBuffer x_buf = new StringBuffer(100);

    x_buf.append("ADVERSARIAL_NODE_ID: ");
    x_buf.append(o_node_ID);
    x_buf.append("\n");
    if(o_conn_list != null)
    {
      x_buf.append("CONN_LIST: ");
      Iterator x_iter = o_conn_list.keySet().iterator();
      String x_conn = null;
      while(x_iter.hasNext())
      {
        x_conn = (String)(x_iter.next());
        x_buf.append(x_conn);
        x_buf.append("::");
      }
      x_buf.append("\n");
    }

    if(o_contents != null)
    {
      Iterator x_iter = o_contents.entrySet().iterator();
      Keyword x_keyword = null;
      Vector x_documents = null;
      Map.Entry x_entry = null;
      x_buf.append("CONTENT: ");
      while(x_iter.hasNext())
      {
      	x_entry = (Map.Entry)(x_iter.next());
        x_keyword = (Keyword)(x_entry.getKey());
        x_buf.append(x_keyword);
        x_documents = (Vector)(x_entry.getValue());
        x_buf.append("--");
        for(int j=0;j<x_documents.size();j++)
        {
          x_buf.append(((Document)(x_documents.elementAt(j))).getDocumentID());
          x_buf.append("::");
        }
      }
      x_buf.append("\n");
    }

    x_buf.append("\n\n");

    return x_buf.toString();
  }
  
  // these adversarial versions overide the methods in the Node class
  

  /**
   * get all the documents in this node independently of whether they 
   * are associated with the query keywords or not ...
   *
   * @param p_keywords    the keywords to check for
   * @return Set          the documents associated with these keywords at this node
   */
  public Set matchingDocuments(Keyword[] p_keywords)
    throws Exception
  {
    if(p_keywords == null) throw new Exception(o_node_ID + " matchingDocuments() argument is null");
  
    HashSet x_set = new HashSet();
  
    Vector x_docs = null;

    Iterator x_iter = o_contents.values().iterator();

    while(x_iter.hasNext())
    {
      x_docs = (Vector)(x_iter.next());
      if(x_docs != null)
      {
        x_set.addAll(x_docs);
      }
    }
    return x_set;
    //return null;
  }
  
  /**
   * check if this node has a particular document, but always claims
   * to have the document, whether or not you actually do
   *
   * @param p_document     the document to add to this node
   * @return boolean       whether this node contains this document or not
   */
  public boolean hasContent(Document p_document)
    throws Exception
  {
    if(p_document == null) throw new Exception(o_node_ID + " hasContent() argument is null");
    return true;
  }

}

