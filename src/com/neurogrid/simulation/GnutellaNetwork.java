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

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.simulation.root.Document;
import com.neurogrid.simulation.root.Network;
import com.neurogrid.simulation.root.Node;
 
/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class implements a Gnutella Network.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   19/Mar/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class GnutellaNetwork extends Network
{
  // private variables
  private static final String cvsInfo = "$Id: GnutellaNetwork.java,v 1.3 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  private static Category o_cat = Category.getInstance(GnutellaNetwork.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("GnutellaNetwork logging Initialized");
  }
  
  /**
   * Constructor
   *
   */
  public GnutellaNetwork()
  {
    super();	
  }
  
  /**
   * Get Node ID
   *
   * @return String
   */
  public String getNetworkID()
  {
    return super.getNetworkID();
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
    return null;	
  }


  /**
   * add some nodes to the network
   *
   * @param p_no_keywords                      the no of keywords in the keyword pool
   * @param p_no_documents                     the no of documents in the documents pool
   * @param p_no_keywords_per_document         the no of keywords used to represent each document
   * @param p_no_nodes                         the no of nodes in the network
   * @param p_no_documents_per_node            the no of documents stored in each node
   * @param p_no_connections_per_node          the no of outgoing connections of each node
   * @param p_knowledge                        boolean that specifies if each node is given knowledge about
   *                                           its neighbours
   * @param p_random                           randomness generator
   */
  public void addNodes(int p_no_keywords, 
                       int p_no_documents, 
                       int p_no_keywords_per_document,
                       int p_no_nodes, 
                       int p_no_documents_per_node,
                       int p_no_connections_per_node, 
                       boolean p_knowledge,
                       Random p_random)
                            
    throws Exception
  {
    if(p_no_keywords < 1) throw new Exception(o_network_ID + " addNodes() less than one keyword not permitted");      
    if(p_no_documents < 1) throw new Exception(o_network_ID + " addNodes() less than one document not permitted");      
    if(p_no_keywords_per_document < 1) throw new Exception(o_network_ID + " addNodes() less than one keyword per document not permitted");      
    if(p_no_nodes < 1) throw new Exception(o_network_ID + " addNodes() less than one node not permitted");      
    if(p_no_documents_per_node < 1) throw new Exception(o_network_ID + " addNodes() less than one document per node not permitted");      
    if(p_no_connections_per_node < 1) throw new Exception(o_network_ID + " addNodes() less than one connection per node not permitted");      

    if(o_cat.isDebugEnabled()) o_cat.debug("Begin adding nodes");
    // Generate N no. of nodes

    // want to store these so we can just add documents to these nodes
    HashMap x_new_nodes = new HashMap(p_no_nodes*2);
    Node x_temp_node = null;
    Vector x_new_node_vector = new Vector(p_no_nodes);
    for(int i=0;i<p_no_nodes;i++)
    {
      x_temp_node = new GnutellaNode(this,p_random);
      x_new_node_vector.addElement(x_temp_node.getNodeID());
      x_new_nodes.put(x_temp_node.getNodeID(),x_temp_node);
    }

    // create some more documents (if desired) to put in these nodes - do we generate new keywords as well?
    //if(p_no_documents != 0)
      //createDocuments(p_no_documents, p_no_keywords_per_document);
      
    // the generate content command will then fill up the new nodes with documents selected randomly from 
    // all the documents available, including the new documents that have been created - 
    // might be better to only insert documents that have just been created, otherwise the 
    // network will have a non-uniform representation over the set of possible documents as the network
    // increases in size???????????????????????  

    generateContent(x_new_nodes,
                    Document.o_documents,
                    Document.o_document_ids,
                    p_no_documents_per_node,
                    o_node_doc_zipf_distribution,
                    new Random());  
    // would be good to specify what docs????????
    
    // better to have all docs come from same overall pool ?????????????

    // so having generated some nodes with some content they need to be connected to
    // the existing network (should avoid connecting them to each other)

    // could select some number of nodes at random and just connect
    // or check to find nodes with similar content

    // either way will need to exchange knowledge of each others contents if that is desired
    
    // can start off  by connecting at random?? - that would involve selecting some number of nodes
    // randomly and then connecting to them, and sharing keys
    
    if(o_ring_topology)
    {
      generateRingTopology(x_new_node_vector,p_no_connections_per_node);
    }
    else
    {
      generateRandomTopology(x_new_node_vector,
                             Node.getVectorFromMap(o_nodes),
                             p_no_connections_per_node,
                             o_reciprocal_connections);
    }
    // another version of this would perform a special search to find right nodes to connect to
    
  }

    
  /**
   * create some nodes 
   *
   * @param p_no_nodes         the number of nodes to create
   * @return HashMap           map of the created node ids --> nodes
   */
  public HashMap createNodes(int p_no_nodes, int p_no_honest_nodes, Random p_random)
    throws Exception
  {  
    if(p_no_nodes < 1) throw new Exception(o_network_ID + " createNodes() less than one node not permitted");      

    HashMap x_new_nodes = new HashMap(p_no_nodes*2);
    Node x_temp_node = null;

    for(int i=0;i<p_no_honest_nodes;i++)
    {
      x_temp_node = new GnutellaNode(this,p_random);
      x_new_nodes.put(x_temp_node.getNodeID(),x_temp_node);
    }
    for(int i=p_no_honest_nodes;i<p_no_nodes;i++)
    {
      x_temp_node = new AdversarialNode(this,new GnutellaMessageHandler(p_random));
      x_new_nodes.put(x_temp_node.getNodeID(),x_temp_node);
    }
     
    if(o_cat.isDebugEnabled()) o_cat.debug("Created Nodes");
    
    return x_new_nodes;
  }
 


  
}

