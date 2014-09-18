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
 

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.log.Logger;
import com.neurogrid.parser.MultiHashtable;
import com.neurogrid.simulation.FuzzyContentMessage;
import com.neurogrid.simulation.SimpleContentMessage;
import com.neurogrid.simulation.SimpleDocument;
import com.neurogrid.simulation.SimpleKeyword;
import com.neurogrid.simulation.statistics.SearchStatistics;
 
/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class represents the network itself.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   8/Oct/2001    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public abstract class Network
{
  // private variables
  private static final String cvsInfo = "$Id: Network.java,v 1.8 2003/06/25 11:50:46 samjoseph Exp $";

  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  protected String o_network_ID = null; 
  
    private static Category o_cat = Category.getInstance(Keyword.class.getName());  

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
 

  public static int o_message_transfers = 0;
  public static int o_no_matches = 0;
  public static int o_no_false_matches = 0;
  public static int o_TTL_of_first_match = -1;
  
  // will need to separate the above stats into something non-static at some point
  // FIXXXXXXXXXXXXXXXXXXXXXX

  public static int o_verbosity = 3;
  public boolean o_ring_topology = false;
  public boolean o_reciprocal_connections = false;
  public boolean o_zipf_distribution = false;
  public boolean o_node_doc_zipf_distribution = false;
  public boolean o_random_searches = false;  
  public boolean o_random_forwarding = false;  
  public int o_min_connections_per_node = 2; 
  public int o_max_connections_per_node = 3; 
  public int o_max_knowledge_per_node = 6; 
 
  public int o_max_forwarding_degree = 2;
  public int o_min_forwarding_degree = 1;  
  
  //public Hashtable o_sending_message = new Hashtable();
  
  public static boolean o_applet = true; //ie applets hate HashSets and vector.remove()
  
  public static boolean o_thread_flag = true;

  public static int o_sleep_duration = 750;
  
  public static Logger o_search_logger = null;

  private static long o_time = 0L;
  private static long o_new_time = 0L;
  
  protected HashMap o_nodes = new HashMap(2000);  // a hashtable of all nodes indexed via ID  
  protected HashSet o_activated_node = new HashSet();  // nodes that have been activated during current search

  /**
   * constructor to ensure resets
   */
  public Network()
  {
    o_message_transfers = 0;
    o_no_matches = 0;
    o_no_false_matches = 0;
    o_TTL_of_first_match = -1;

    o_verbosity = 3;
    o_ring_topology = false;
    o_reciprocal_connections = false;
    o_zipf_distribution = false;
    o_node_doc_zipf_distribution = false;
  
    o_thread_flag = true;
    o_random_searches = false;  
    o_random_forwarding = false;  
    o_sleep_duration = 750;
  
    Logger o_search_logger = null;

    o_time = 0L;
    o_new_time = 0L;
  
    o_nodes = new HashMap(2000);  // a hashtable of all nodes indexed via ID  
    o_activated_node = new HashSet();  // nodes that have been activated during current search
	
  }

  /**
   * get a mapping of node ids to nodes
   * this method returns a clone so that the 
   * underlying network node structure will not change
   *
   * @return Map
   */
  public Map getNodes()
  {
    return (Map)(o_nodes.clone());	
  }

  /**
   * get the number of nodes in this network
   *
   * @return int
   */
  public int getNoNodes()
  {
    return o_nodes.size();	
  }
  
  /**
   * get the number of nodes in this network
   *
   * @return int
   */
  public int getNoHonestNodes()
  {
    Iterator x_iter = o_nodes.values().iterator();
    
    int x_sum = 0;
    Node x_node = null;
    String x_class = null;
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());
      x_class = x_node.getClass().getName();
      //o_cat.debug(x_class);
      if(!x_class.equals("com.neurogrid.simulation.AdversarialNode"))
      {
        x_sum++;
      }
    } 	
  	
    return x_sum;	
  }
  
  /**
   * check if any of the nodes in this network have a 
   * document with a particular keyword
   *
   * @param p_keyword    the keyword to check for
   * @return int         the number of documents associated with this keyword in the network
   */
  public int hasKeyword(Keyword p_keyword)
    throws Exception
  {
    if(p_keyword == null) throw new Exception(o_network_ID + " hasKeyword() argument is null");
    
    Iterator x_iter = o_nodes.values().iterator();
    
    int x_sum = 0;
    Node x_node = null;
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());
      x_sum += x_node.hasKeyword(p_keyword);	
    }

    return x_sum;
    //return 1;
  }
  
  /**
   * check if any of the nodes in this network have a 
   * document with any of the keywords
   *
   * @param p_keywords   the keywords to check for
   * @return int         the number of documents associated with this keyword in the network
   */
  public int hasKeywords(Keyword[] p_keywords)
    throws Exception
  {
    if(p_keywords == null) throw new Exception(o_network_ID + " hasKeyword() argument is null");
    
    Iterator x_iter = o_nodes.values().iterator();

    Document x_doc = null;
    int x_sum = 0;
    int x_key_sum = 0;
    Node x_node = null;
    HashSet x_docs = null;
    Iterator x_doc_iter = null;

    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());

      x_docs = (HashSet)(x_node.matchingDocuments(p_keywords));
      // this breaks down when an adversarial node overrides the matchingDocuments method
      // need to check each individual doc for keywords
      x_doc_iter = x_docs.iterator();
      
      while(x_doc_iter.hasNext())
      {
      	x_doc = (Document)(x_doc_iter.next());
      	x_key_sum = 0;
      	for(int i=0;i<p_keywords.length;i++)
      	{
      	  if(x_doc.hasKeyword(p_keywords[i]))
      	  {
      	    x_key_sum++;
          }
        }
        if(x_key_sum > 0)
        {
          x_sum++;
        }
      }
    }
    
    return x_sum;
    //return 1;
  }
  
  /**
   * check if any of the nodes in this network have a 
   * document with any of the keywords
   *
   * @param p_keywords   the keywords to check for
   * @return int         the number of unique documents associated with this keyword in the network
   */
  public int hasKeywordsUnique(Keyword[] p_keywords)
    throws Exception
  {
    if(p_keywords == null) throw new Exception(o_network_ID + " hasKeyword() argument is null");
    
    Iterator x_iter = o_nodes.values().iterator();

    Node x_node = null;
    HashSet x_docs = null;
    HashSet x_set = new HashSet();
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());

      x_docs = (HashSet)(x_node.matchingDocuments(p_keywords));
      if(x_docs != null)
      {
        x_set.addAll(x_docs);
      }
    }
    
    return x_set.size();
    //return 1;
  }
  
  /**
   * check if any of the nodes in this network have a 
   * document with any of the keywords
   *
   * @param p_keywords   the keywords to check for
   * @return int         the number of documents associated with this keyword in the network
   */
  public int countMatches(Keyword[] p_keywords)
    throws Exception
  {
    if(p_keywords == null) throw new Exception(o_network_ID + " hasKeyword() argument is null");
    
    Iterator x_iter = o_nodes.values().iterator();

    int x_sum = 0;
    Node x_node = null;

    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());

      x_sum += x_node.matchingKeywords(p_keywords);
    }
    
    return x_sum;
    //return 1;
  }
  

  /**
   * get the number of active nodes in this network
   *
   * @return int
   */
  public int getNoActiveNodes()
  {
    return o_activated_node.size();	
  }
  
  /**
   * check if a particlar node is active in this network
   *
   * @return boolean
   */
  public boolean activeNodeContains(Node p_node)
  {
    return o_activated_node.contains(p_node);	
  }
  
  /**
   * remove a node from the active list
   *
   * @param p_node
   */
  protected void removeActiveNode(Node p_node)
  {
    o_activated_node.remove(p_node);	
  }
  
  /**
   * add a node to the active list
   *
   * @param p_node
   */  
  protected void addActiveNode(Node p_node)
  {
    o_activated_node.add(p_node);	
  }
  

  /**
   * print time and message since last check
   *
   * @param p_message
   */
  public static void timeCheck(String p_message)
    throws Exception
  {
    if(p_message == null) throw new Exception("timeCheck() argument is null");
    o_new_time = System.currentTimeMillis();
    System.out.println(p_message + " :" + (o_new_time - o_time) + "ms");
    o_time = o_new_time;
  }

  /**
   * Clear the network, removing the list of connections, the contents and the 
   * knowledge of all nodes.  This serves to set things up so we can start over from
   * completely afresh ...
   *
   * Also empty the document and keyword hashtables so that there are no documents referenced
   * by id, or by keyword, and that there are no keywords referenced by id, or keyword id referenced to
   * the keywords themselves.
   *
   * Clear all the message tables as well.
   *
   * Also empty the public static o_nodes hashtable of nodes indexed via their ids
   */
  public void clearNetwork()
  {
    Iterator x_iter = o_nodes.values().iterator();
    Node x_node = null;
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());
      x_node.clear();
    }
    
    o_activated_node.clear();

    Document.o_documents.clear();
    Document.o_documents_by_keyword.clear();
    Document.o_document_locations.clear();
    Keyword.o_keywords.clear();
    Keyword.o_keyword_ids.clear();
    Keyword.resetGlobalKeywordID();
    Message.o_messages.clear();
    Message.o_messages_by_TTL.clear();
    
    o_nodes.clear();
    
    if(o_verbosity > -5) System.out.println("cleared network; #nodes = " + o_nodes.size());
  }

  /**
   * Refresh the network, resetting the statistics
   *
   * Clear the hashtable telling us which nodes have been activated?
   *
   * Clear the inbox and GUID stores of each node
   *
   * Clear all the message tables
   */
  public void refresh()
  {
    o_message_transfers = 0;
    o_no_matches = 0;
    o_no_false_matches = 0;
    o_TTL_of_first_match = -1;
    o_activated_node.clear();
    Iterator x_iter = o_nodes.values().iterator();
    Node x_node = null;
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());
      x_node.refresh();
    }

    Message.o_messages_by_TTL.clear();
    Message.o_messages.clear();
  }
  
  /**
   * Get Node ID
   *
   * @return String
   */
  public String getNetworkID()
  {
    return o_network_ID;
  }

  /**
   * Get string representation
   *
   * Representation includes NODE_ID and complete list of IDs of connected nodes
   * as well as all the associations in the content and knowledge multihashtables
   *
   * @return String
   */
  public abstract String toString();


  /**
   * add a node to this network
   *
   * @param p_node
   */
  public void addNode(Node p_node)
  {
    o_nodes.put(p_node.getNodeID(),p_node);	
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
  public abstract void addNodes(int p_no_keywords, 
                                int p_no_documents, 
                                int p_no_keywords_per_document,
                                int p_no_nodes, 
                                int p_no_documents_per_node,
                                int p_no_connections_per_node, 
                                boolean p_knowledge,
                                Random p_random)
                            
    throws Exception;
    
  /**
   * create some nodes 
   *
   * @param p_no_nodes         the number of nodes to create
   * @return HashMap           map of the created node ids --> nodes
   */
  public abstract HashMap createNodes(int p_no_nodes, 
                                      int p_no_honest_nodes,
                                      Random p_random)
    throws Exception;
 


  public void setSendingMessageFalse()
    throws Exception
  {
    //Set x_set = Node.o_nodes.keySet();
    //Vector x_vector = new Vector(x_set);
    Vector x_vector = Node.getVectorFromMap(o_nodes);
    int x_size = x_vector.size();
    Node x_node = null;
    for(int i=0;i<x_size;i++)  	
    {
      x_node = (Node)(o_nodes.get(x_vector.elementAt(i) ) );
      x_node.clearSendingMessage(); 
    }
  }
  
 
  /**
   * create some nodes 
   *
   * @param p_no_keywords      the number of keywords to create
   * @return HashMap           map of the created keyword ids --> keywords
   */
  public HashMap createKeywords(int p_no_keywords)
    throws Exception
  {  
    if(p_no_keywords < 1) throw new Exception(o_network_ID + " createKeywords() less than one keyword not permitted");      

    HashMap x_new_keywords = new HashMap(p_no_keywords*2);
    Keyword x_temp_keyword = null;

    for(int i=0;i<p_no_keywords;i++)
    {
      x_temp_keyword = new SimpleKeyword();
      x_new_keywords.put(x_temp_keyword.getKeywordID(),x_temp_keyword);
    }
     
    if(o_cat.isDebugEnabled()) o_cat.debug("Created Keywords");
    
    return x_new_keywords;
  }
  
  /**
   * create the network itself
   *
   * @param p_network_parameters         the parameters for the network
   *
   */
  public void createNetwork(NetworkParameters p_network_parameters, Random p_random)
    throws Exception
  {  
    int p_no_keywords = p_network_parameters.o_no_keywords;
    int p_no_documents = p_network_parameters.o_no_documents;
    int p_no_keywords_per_document = p_network_parameters.o_no_keywords_per_document;
    int p_no_nodes = p_network_parameters.o_no_nodes;
    int p_no_honest_nodes = p_network_parameters.o_no_honest_nodes;
    int p_no_documents_per_node = p_network_parameters.o_no_documents_per_node;
    int p_no_connections_per_node = p_network_parameters.o_no_connections_per_node;
      
    o_ring_topology = p_network_parameters.o_ring_topology;
    o_reciprocal_connections = p_network_parameters.o_reciprocal_connections;
    o_zipf_distribution = p_network_parameters.o_zipf_distribution;
    o_node_doc_zipf_distribution = p_network_parameters.o_node_doc_zipf_distribution;
    o_random_searches = p_network_parameters.o_random_searches; 
    o_random_forwarding = p_network_parameters.o_random_forwarding; 
    o_min_connections_per_node = p_network_parameters.o_min_connections_per_node; 
    o_max_connections_per_node = p_network_parameters.o_max_connections_per_node; 
    o_max_knowledge_per_node = p_network_parameters.o_max_knowledge_per_node; 
    o_max_forwarding_degree = p_network_parameters.o_max_forwarding_degree; 
    o_min_forwarding_degree = p_network_parameters.o_min_forwarding_degree; 
      
    if(p_no_keywords < 1) throw new Exception(o_network_ID + " createNetwork() less than one keyword not permitted");      
    if(p_no_documents < 1) throw new Exception(o_network_ID + " createNetwork() less than one document not permitted");      
    if(p_no_keywords_per_document < 1) throw new Exception(o_network_ID + " createNetwork() less than one keyword per document not permitted");      
    if(p_no_nodes < 1) throw new Exception(o_network_ID + " createNetwork() less than one node not permitted");      
    if(p_no_documents_per_node < 1) throw new Exception(o_network_ID + " createNetwork() less than one document per node not permitted");      
    if(p_no_connections_per_node < 1) throw new Exception(o_network_ID + " createNetwork() less than one connection per node not permitted");      

    // if we wanted to draw the initial network I guess we could do it here
    // ideally we would want to place nodes closer to those they were already connected to
    // which might be difficult to do, but we could create a toroidal space, perhaps
    // square is better and start off by placing a node at random and then placing the
    // nodes that are connected to it at some distance away ... will probably look messy
    // but who cares ...
    
    // 1. making some nodes

    if(o_cat.isDebugEnabled()) o_cat.debug("Begin");
    // Generate N no. of nodes
    
    //NGSimulatorApplet.restartButton.setLabel("Point 7");
 
    createNodes(p_no_nodes,p_no_honest_nodes,p_random);
    
    // 2. making some keywords and documents

    //System.out.println(Node.o_nodes.toString());
    //System.out.println("");

    // Generate some keywords
    // this subsection could be replaced by loading from file
    // ideally the document/keywords generated here should be placed directly into the 
    // generateContent function
    //FIXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    if(o_applet == false)
    {
      try
      {
        loadSerializedDocuments(p_no_documents, 
                                p_no_keywords_per_document,
                                p_no_keywords,
                                o_zipf_distribution);
      }
      catch(FileNotFoundException e)
      {
        if(o_cat.isDebugEnabled()) o_cat.debug("No file matching that distribution");
        createKeywordsAndDocuments(p_no_keywords,
                                   p_no_documents,
                                   p_no_keywords_per_document,
                                   p_random);

        try{
      
        saveSerializedDocuments(p_no_documents, 
                                p_no_keywords_per_document,
                                p_no_keywords,
                                o_zipf_distribution);
         }catch(Exception x_se){if(o_cat.isDebugEnabled())x_se.printStackTrace();}
      }
    }
    else
    {
      //NGSimulatorApplet.restartButton.setLabel("Point 7");
      createKeywordsAndDocuments(p_no_keywords,
                                 p_no_documents,
                                 p_no_keywords_per_document,
                                 p_random);
    }
     
    {
      // could serialize the static tables directly

    }
    // could make creation of this lead to automatic storage ...
    // then when we request a distribution with similar parameters
    // affective parameters are: No documents, No keywords, keywords per doc
    // type of distribution
    

    //Randomly generate C connections between nodes - naturally have to think of
    //small world networks at some point (leave for now)

    // also put some content in the nodes

    // distribute knowledge to nearest neighbours if applicable

    // This is content creation phase

    //Enumeration x_enum = Node.o_nodes.keys();
    Vector x_vector = Node.getVectorFromMap(o_nodes);
    //new Vector(Node.o_nodes.size());
    //while(x_enum.hasMoreElements()){x_vector.addElement(x_enum.nextElement());}
    //Set x_set = Node.o_nodes.keySet(); // applets don't like sets?
    //Vector x_vector = new Vector(x_set);
    //Hashtable x_clone = null;
    
    // 3. placing some of this content in the nodes


    generateContent(o_nodes,
                    Document.o_documents,
                    Document.o_document_ids,
                    p_no_documents_per_node,
                    o_node_doc_zipf_distribution,
                    p_random);

    // this is connection creation phase
    // 4. generating connections
 
    //generateTwoWayConnections(x_vector,p_knowledge,p_no_connections_per_node,p_degree_of_correlation);
    
    if(o_ring_topology)
    {
      generateRingTopology(x_vector,p_no_connections_per_node);
    }
    else
    {
      generateRandomTopology(x_vector,x_vector,p_no_connections_per_node,o_reciprocal_connections);
    }
 
    //(optional) distribute known keywords and ID to nearest neighbors
    
    //generateTwoWayKnowledge(x_vector,p_knowledge);
    
    if(o_cat.isDebugEnabled()) o_cat.debug("created network: #nodes = " + o_nodes.size());
	
  	
  	
  }
    
    
  public void createKeywordsAndDocuments(int p_no_keywords, 
                                         int p_no_documents,
                                         int p_no_keywords_per_document,
                                         Random p_random)
    throws Exception
  {
    if(p_no_documents < 1) throw new Exception(o_network_ID + " createDocuments() less than one document not permitted");      
    if(p_no_keywords < 1) throw new Exception(o_network_ID + " createDocuments() less than one keyword not permitted");      
    if(p_no_keywords_per_document < 1) throw new Exception(o_network_ID + " createDocuments() less than one keyword per document not permitted");      

    createKeywords(p_no_keywords);

    if(o_cat.isDebugEnabled())
    {
      o_cat.debug(Keyword.o_keywords.toString());
      o_cat.debug("");
    }

    //Give each some X documents that each have 3 (?) assoc. keywords

    createDocuments(p_no_documents, 
                    p_no_keywords_per_document, 
                    o_zipf_distribution,
                    p_random);
  }
    


  /**
   * search the network for a particular document
   *
   * @param p_start_TTL      the starting TTL of the message, limiting the number of times it is forwarded
   * @param p_keywords       the keywords we are searching for
   * @param p_search_target  the search target - if there is one
   * @param p_node           the node we will start the search at
   *
   * @return SearchStatistics        some stats
   */
  public SearchStatistics searchNetwork(ContentMessage p_message)
  //int p_start_TTL, Keyword[] p_keywords, Document p_search_target, Node p_node)
    throws Exception
  {  
    Keyword[] x_keywords = p_message.getKeywords();
    Node x_node = p_message.getStart();
    int x_start_TTL  = p_message.getTTL();
    Document x_search_target= p_message.getDocument(); // this may not exist in a fuzzy search ...
    
    // want to log the search 
    if(o_cat.isDebugEnabled())
    {  	
      o_cat.debug(x_node.getNodeID()+" "+ x_search_target);
    }
    
    
    p_message.setPreviousLocation(x_node);

    //x_node.indicateSendingMessage(x_node); // this doesn't really make any sense ...
    x_node.addMessageToInbox(p_message);
    x_node.injectMessage(p_message);

    Vector x_messages = null;
    Message x_message = null;

    for(int x=x_start_TTL-1;x>0;x--) // grab all the highest TTL messages
    {
      //if(o_cat.isDebugEnabled()) Network.timeCheck("Processing TTL: " + x);
      x_messages = (Vector)(Message.o_messages_by_TTL.remove(Integer.toString(x)));
      // so actually we never use the sequence order as placed in the inbox ....
      if(x_messages != null)
      {

          
          // would need to write graphics method based on updating the state of the current
          // node, i.e. if it has been activated or not	

            if(o_cat.isDebugEnabled())
               o_cat.debug(x_messages.size() + " messages at TTL " + x);

          for(int y=0;y<x_messages.size();y++)
          {
            x_message = (Message)(x_messages.elementAt(y));
            x_node = x_message.getLocation();
            if(o_cat.isDebugEnabled())
               o_cat.debug("Going to handle message at: " + x_node.getNodeID());
            
            x_node.handleMessage(x_message);

          }
        }

      }

  

    SearchStatistics x_search_stats = new SearchStatistics();
    x_search_stats.setNoMatches(o_no_matches);
    x_search_stats.setNoFalseMatches(o_no_false_matches);
    x_search_stats.setTTLOfFirstMatch(o_TTL_of_first_match);
    x_search_stats.setNoMessageTransfers(o_message_transfers);
    x_search_stats.setNoActivatedNodes(getNoActiveNodes());
    x_search_stats.setNoPossibleTargets(p_message.getPossibleMatches(this));


    if(o_cat.isDebugEnabled())
    {
      o_cat.debug("No. matches: " + x_search_stats.getNoMatches());
      o_cat.debug("No. false matches: " + x_search_stats.getNoFalseMatches());
      o_cat.debug("TTL of first match: " + x_search_stats.getTTLOfFirstMatch());
      o_cat.debug("No. of message transfers: " + x_search_stats.getNoMessageTransfers());
      o_cat.debug("Nodes reached: " + x_search_stats.getNoActivatedNodes());
      
      // want number of possible matches ...

      // actually now we want to know how in how many locations our document is in

      o_cat.debug("No. of possible matches: " + x_search_stats.getNoPossibleTargets());
    }  

    Vector x_vector = null;

    if(o_cat.isInfoEnabled())
    {
      //x_set = Node.o_nodes.keySet();
      //x_vector = new Vector(x_set);
      x_vector = Node.getVectorFromMap(o_nodes);
      for(int x=0;x<x_vector.size();x++)
      {
        x_node = (Node)(o_nodes.get(x_vector.elementAt(x)));

        // could print out node data to check what's going on
        o_cat.info(x_node.toString());
      }
    }

    return x_search_stats;

  }

  /**
   * search the network for a particular document
   *
   * @param p_start_TTL      the starting TTL of the message, limiting the number of times it is forwarded
   * @param p_random         randomness for selecting start location and target
   * 
   * @param SearchStatistics results from the search
   */
  public SearchStatistics searchNetwork(int p_start_TTL, Random p_random)
    throws Exception
  {
    if(p_start_TTL < 1) throw new Exception(o_network_ID + " searchNetwork() TTL below 1 not permitted");      
    if(o_cat.isDebugEnabled()) o_cat.debug("Starting search");
      
    o_thread_flag = true;

    double x_random;
    int x_pick;
    //pick 3(?) random keywords (or perhaps a document and take it's keywords) and start a
    //search in some location

    // select a node and update -> well could follow the updates from the
    // start location, placing messages in the inBox of connected nodes and then updating
    // those nodes, forwarding, responding

    Set x_set = null;
    Vector x_vector = null;
    Enumeration x_doc_enum = null;
    Document x_temp_doc = null;
    Document x_search_target = null;
    Vector x_target_locations = null;

    Node x_node = null;
    
    //simple selection of a random document

    if(o_random_searches == true)
    {
      Vector x_available_docs = Node.getVectorFromMultiHashtable(Document.o_document_locations);
      //double x_rnd = Math.random();
      double x_rnd = p_random.nextDouble();
      x_pick = (int)(x_rnd * (double)x_available_docs.size());
    
      x_search_target = (Document)(x_available_docs.elementAt(x_pick));
      
      x_node = getRandomSearchStart(x_search_target,p_random);
    }
    else
    {
      // so the idea is that we just pick a search location at random
      x_vector = (Vector)(Node.getVectorFromMap(o_nodes)).clone();
      HashSet x_excludes = new HashSet();

      while(x_search_target == null)
      {
      	/*
        //x_random = Math.random();
        x_random = p_random.nextDouble();
        x_pick = (int)(x_random * x_vector.size());

        if(x_vector.size() == 0)
	{
	  o_cat.error("Couldn't find any nodes that don't contain all the documents related to a particular keyword");
          System.exit(0);
        }

        // x_node is the start location for the search
        x_node = (Node)(o_nodes.get(x_vector.elementAt(x_pick)));
        x_vector.removeElementAt(x_pick);
        */
        // I need to control this from the outside ...
        // or really some consistent framework for dealing with stats related to 
        // different node types
        //FIXXXXXXXXXXXXXXXXXXXXXX

        x_node = (Node)getRandomSearchStart(x_excludes,
                                            "com.neurogrid.simulation.NeuroGridNode",
                                            p_random);


        x_excludes.add(x_node);

        x_search_target = getAssociatedSearchTarget(x_node,p_random);
      }
    
      if(o_cat.isDebugEnabled()) o_cat.debug("Non-random search target is: " + x_search_target.toString());
       
    }

    x_target_locations = x_search_target.getLocation();

    // this is the document we are going to search for - should try and find one with
    // some fixed number of copies in the network

    

    Keyword[] x_keywords = x_search_target.getKeywords();
    if(o_cat.isDebugEnabled())
    {
      o_cat.debug("search keywords::");
      for(int j=0;j<x_keywords.length;j++)
        o_cat.debug(x_keywords[j]);
    }
    
    ContentMessage x_message = new SimpleContentMessage(p_start_TTL,x_keywords,x_search_target,x_node);

    return searchNetwork(x_message);
  }
    
  /**
   * search the network for a particular document
   *
   * @param p_start_TTL      the starting TTL of the message, limiting the number of times it is forwarded
   * @param p_random         randomness for selecting start location and target
   * 
   * @param SearchStatistics results from the search
   */
  public SearchStatistics fuzzySearchNetwork(int p_start_TTL, int p_no_search_keywords, Random p_random)
    throws Exception
  {
    if(p_start_TTL < 1) throw new Exception(o_network_ID + " fuzzySearchNetwork() TTL below 1 not permitted");      
    if(o_cat.isDebugEnabled()) o_cat.debug("Starting search");
  
    // this fuzzy searching needs to take into account whether the keywords should
    // be associated with the node we start searching from ...

    // need a random search start location --> we could ensure it was of a particular type  ....
    // actually it would be more desirable to identity what type of node was doing the search ...

    Node x_node = getRandomSearchStart(p_random);  
    Keyword[] x_keywords = null;

    if(o_random_searches == true)
    {  
      //select some random keywords	
    	
      x_keywords = getRandomSearchKeywords(p_no_search_keywords,p_random);
    }
    else
    {
      x_keywords = getAssociatedSearchKeywords(p_no_search_keywords,x_node,p_random);
    }
    
    if(o_cat.isDebugEnabled())
    {
      o_cat.debug("search keywords::");
      for(int j=0;j<x_keywords.length;j++)
        o_cat.debug(x_keywords[j]);
    }
        
    ContentMessage x_message = new FuzzyContentMessage(p_start_TTL,x_keywords,x_node);

    return searchNetwork(x_message);
  }
    
  /**
   * create some additional documents using existing keywords
   *
   * @param p_no_documents
   * @param p_no_keywords_per_document
   *
   * @return HashMap
   */
  public HashMap createDocuments(int p_no_documents, 
                                 int p_no_keywords_per_document, 
                                 boolean p_zipf, 
                                 Random p_random)
    throws Exception
  {  
    //return new Document[p_no_documents];

    if(p_no_documents < 1) throw new Exception(o_network_ID + " createDocuments() less than one document not permitted");      
    if(p_no_keywords_per_document < 1) throw new Exception(o_network_ID + " createDocuments() less than one keyword per document not permitted");      

    o_cat.debug("Creating documents");
    
    HashMap x_new_documents = new HashMap(p_no_documents*2);
    
    Keyword[] x_keywords = null;
    Document x_temp_doc = null;

    if(p_zipf == false)
    {       
      Object[][] x_dist = getUniformDistribution(p_no_documents,
                                                 p_no_keywords_per_document,
                                                 Keyword.o_keywords,
                                                 Keyword.o_keywords,
                                                 p_random);         
      for(int i=0;i<p_no_documents;i++)
      {
      	x_keywords = new Keyword[p_no_keywords_per_document];
      	for(int j=0;j<p_no_keywords_per_document;j++)
      	{
          x_keywords[j] = (Keyword)(x_dist[i][j]);
        }
        x_temp_doc = new SimpleDocument(x_keywords);
        x_new_documents.put(x_temp_doc.getDocumentID(),x_temp_doc);

        if(o_cat.isDebugEnabled()) o_cat.debug("New Document: "+ i);
      }
    }   
    else // create a zipf distribution of keywords over documents
    {

      Vector x_vec = getZipfDistribution(p_no_documents,
                                         p_no_keywords_per_document,
                                         Keyword.o_keywords,
                                         Keyword.o_keyword_ids,
                                         3,
                                         true,
                                         p_random);
      Vector x_keyword_vec = null;
      // now all the document vectors should be filled up with Zipf distributed keywords

      // create documents out of the vectors ...
       
      for(int i=0;i<p_no_documents;i++)
      {
        x_keyword_vec = (Vector)(x_vec.elementAt(i));
        x_temp_doc = new SimpleDocument(x_keyword_vec);
        x_new_documents.put(x_temp_doc.getDocumentID(),x_temp_doc);

        if(o_cat.isDebugEnabled()) o_cat.debug("New Document: "+ i);
      }
      
    }

    // debugging stuff to print out all docs and keywords

    if(o_cat.isDebugEnabled())
    {
      Iterator x_iter = Document.o_documents.keySet().iterator();
      String x_doc_id = null;
      Keyword[] x_doc_words = null;
      Document x_doc = null;
      while(x_iter.hasNext())
      {
        x_doc_id = (String)(x_iter.next());
        o_cat.debug(x_doc_id + ": ");
        x_doc = (Document)(Document.o_documents.get(x_doc_id));
        x_doc_words = x_doc.getKeywords();

        for(int z= 0;z<x_doc_words.length;z++)
          o_cat.debug(x_doc_words[z].toString() + ":");
        o_cat.debug("");
      }
    }
    
    return x_new_documents;
  }

    
  /**
   * places content in the specified set of nodes, drawing from all documents
   * available in the system
   *
   * @param p_nodes
   * @param p_no_documents_per_node
   * @param p_zipf
   */
  public void generateContent(HashMap p_nodes, 
                              HashMap p_documents, 
                              HashMap p_document_ids, 
                              int p_no_documents_per_node, 
                              boolean p_zipf,
                              Random p_random)
    throws Exception
  {
    if(p_nodes == null) throw new Exception(o_network_ID + " generateContent() argument is null");      
    if(p_no_documents_per_node < 1) throw new Exception(o_network_ID + " generateContent less than one document per node not permitted");      

    Iterator x_iter = p_nodes.values().iterator();
    Hashtable x_clone = null;
    double x_random;
    int x_pick;

    Node x_node = null;
    Node x_connected_node = null;
    String x_id = null;
    String x_doc_id = null;
    //Document x_document = null;
    
    
    //if(o_node_doc_zipf_distribution == false)
    if(p_zipf == false)
    {

      Object[][] x_dist = getUniformDistribution(p_nodes.size(),
                                                 p_no_documents_per_node,
                                                 p_documents,
                                                 p_documents,
                                                 p_random);  
      int i=0;                                                         
      while(x_iter.hasNext())
      {
      	x_node = (Node)(x_iter.next());
      	for(int j=0;j<p_no_documents_per_node;j++)
      	{	
          x_node.addContent((Document)(x_dist[i][j]));
        }
        i++;
        // if(o_cat.isDebugEnabled()) o_cat.debug("New Document: "+ i);
      }

    }
    else
    {
      Vector x_vec = getZipfDistribution(p_nodes.size(),
                                         p_no_documents_per_node,
                                         p_documents,
                                         p_document_ids,
                                         3,
                                         false,
                                         p_random);
      Vector x_docs = null;
      // now all the document vectors should be filled up with Zipf distributed keywords


      // create documents out of the vectors ...
      int i=0;
      while(x_iter.hasNext())
      {
        // ideally I would like to run through the documents and grab their keywords
        // and then find similar documents and their locations and then connect nodes on that basis
        x_node = (Node)(x_iter.next());
        
        x_docs = (Vector)(x_vec.elementAt(i));
        for(int j=0;j<x_docs.size();j++)
        {
          x_doc_id = (String)(x_docs.elementAt(j));
          //System.out.println();
          x_node.addContent((Document)(Document.o_documents.get(x_doc_id)));
        }
        //if(o_cat.isDebugEnabled()) o_cat.debug("Added documents to node: "+ x_id);
        i++;
      }    	
    }
    
    // debugging stuff to check doc/node distribution
    
    if(o_cat.isDebugEnabled())
    {
      x_iter = p_nodes.values().iterator();
      Iterator x_iter2 = null;
      String x_node_id = null;
      Map x_node_docs = null;
      x_node = null;
      Document x_doc = null;
      Long x_l_doc_id = null;
      while(x_iter.hasNext())
      {
        x_node = (Node)(x_iter.next());
        o_cat.debug(x_node.getNodeID() + ": ");
        x_node_docs = x_node.getContentsByDocID();
        x_iter2 = x_node_docs.keySet().iterator();

        while(x_iter2.hasNext())
        {
         // x_l_doc_id = (Long)(x_enum2.nextElement());
          //System.out.print((String)(x_node_docs.get(x_l_doc_id)) + ":");
          o_cat.debug((String)(x_iter2.next()) + ":");
        }
        o_cat.debug("");
      }
    }
  }

  
  /**
   * creates connections between nodes in the system
   *
   * @param p_vector
   * @param p_knowledge
   * @param p_no_connections_per_node
   * @param p_degree_of_correlation
   *
  public abstract void generateConnections(Vector p_vector, boolean p_knowledge, int p_no_connections_per_node,
                                         int p_degree_of_correlation)
    throws Exception;
  */
  /**
   * creates connections between nodes in the system
   *
   * @param p_nodes
   * @param p_no_documents_per_node
   *
  public abstract void generateTwoWayConnections(Vector p_vector, boolean p_knowledge, 
                                               int p_no_connections_per_node,
                                               int p_degree_of_correlation)
    throws Exception;
  */
  /**
   * generate knowledge between connected nodes
   *
   * @param p_vector               the nodes which are going to share knowledge with each other
   * @param p_knowledge            boolean indicator of whether knowledge of node contents is being shared
   *
  public abstract void generateTwoWayKnowledge(Vector p_vector, boolean p_knowledge)
    throws Exception;
  */
      
  /**
   * generate knowledge between connected nodes
   *
   * @param p_vector               the nodes which are going to share knowledge with each other
   * @param p_knowledge            boolean indicator of whether knowledge of node contents is being shared
   *
  public abstract void generateKnowledge(Vector p_vector, boolean p_knowledge)
    throws Exception;


  
*/

  protected static void saveSerializedDocuments(int p_no_documents, 
                                                int p_no_keywords_per_document,
                                                int p_no_keywords, 
                                                boolean p_zipf_distribution)
  {    
    if(Network.o_verbosity > 0)System.out.println("Attempting to save documents via serialization");
    long time = System.currentTimeMillis();

    StringBuffer x_buf = new StringBuffer(50);
    x_buf.append("ng-docs-D").append(p_no_documents);
    x_buf.append("-KD").append(p_no_keywords_per_document);
    x_buf.append("-K").append(p_no_keywords);
    x_buf.append("-Z").append(p_zipf_distribution);
    x_buf.append(".ser");
    String x_documents_file = x_buf.toString();
    if(Network.o_verbosity > 0)System.out.println("File name: " + x_documents_file);
    try
    {
      FileOutputStream out = new FileOutputStream(x_documents_file);
      ObjectOutputStream s = new ObjectOutputStream(out);
      s.writeObject("Stored:");
      s.writeObject(new Date());
      s.writeObject(Keyword.o_keywords);	
      s.writeObject(Keyword.o_keyword_ids);	
      s.writeObject(Document.o_documents_by_keyword);	
      s.writeObject(Document.o_document_ids);	
      s.writeObject(Document.o_documents);	
      s.flush();
    }    
    catch(FileNotFoundException e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    catch(java.io.IOException e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();    
    } 
    if(Network.o_verbosity > 0)
    {
    	System.out.println("Serialization took: " +
    	  (System.currentTimeMillis() - time) + "ms");
    }
    // should do garbage collection here
    Runtime rt = Runtime.getRuntime();
    if(Network.o_verbosity > 0)System.out.println("Ser:Free Memory " + rt.freeMemory());
    rt.gc();
    if(Network.o_verbosity > 0)System.out.println("after gc: Free Memory " + rt.freeMemory());
    if(Network.o_verbosity > 0)System.out.println("Saved: " + x_documents_file);
  }


  public static void loadSerializedDocuments(int p_no_documents, 
                                             int p_no_keywords_per_document,
                                             int p_no_keywords, 
                                             boolean p_zipf_distribution)
    throws FileNotFoundException
  {
    if(o_applet == true) throw new FileNotFoundException("Applet");
    if(Network.o_verbosity > 0)System.out.println("Attempting to load documents via serialization");
    StringBuffer x_buf = new StringBuffer(50);
    x_buf.append("ng-docs-D").append(p_no_documents);
    x_buf.append("-KD").append(p_no_keywords_per_document);
    x_buf.append("-K").append(p_no_keywords);
    x_buf.append("-Z").append(p_zipf_distribution);
    x_buf.append(".ser");
    String x_documents_file = x_buf.toString();
    if(Network.o_verbosity > 0)System.out.println("File name: " + x_documents_file);

    long time = System.currentTimeMillis();
    try
    {
      FileInputStream in = new FileInputStream(x_documents_file);
      ObjectInputStream s = new ObjectInputStream(in);
      String temp = (String)s.readObject();
      Date date = (Date)s.readObject();
      Keyword.o_keywords = (HashMap)s.readObject();	
      Keyword.o_keyword_ids = (HashMap)s.readObject();	
      Document.o_documents_by_keyword = (MultiHashtable)s.readObject();	
      Document.o_document_ids = (HashMap)s.readObject();	
      Document.o_documents = (HashMap)s.readObject();	
    }  
    catch(java.io.FileNotFoundException e)
    {
      throw e;
    }  
    catch(java.io.IOException e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    catch(ClassNotFoundException e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    if(Network.o_verbosity > 0)
    {
      System.out.println("Deserialization took: " +
                       (System.currentTimeMillis() - time) + "ms");
    }
    // should do garbage collection here
    Runtime rt = Runtime.getRuntime();
    if(Network.o_verbosity > 0)System.out.println("DeSer:Free Memory " + rt.freeMemory());
    rt.gc();
    if(Network.o_verbosity > 0)System.out.println("after gc: Free Memory " + rt.freeMemory());
  }
  
    /**
   * create a zipf distribution 
   *
   * @param p_no_bins
   * @param p_no_items_per_bin
   * @param p_item_table                    hashtable of items and item ids
   * @param p_item_id_table                 hashtable of item ids and longs
   *
   * @return Object[][]
   */
  public Object[][] getUniformDistribution(int p_no_bins, 
                                           int p_no_items_per_bin, 
                                           HashMap p_item_table,
                                           HashMap p_item_id_table,
                                           Random p_random)
    throws Exception                                     
  {  
    o_cat.debug("Trying to create Uniform distribution");

    Vector x_vector = null;
    HashMap x_clone = null;
    HashMap x_id_clone = null;
    Object[][] x_items = new Object[p_no_bins][p_no_items_per_bin];
    double x_rnd = 0.0D;
    int x_pick = -1;
    String x_item_id = null;
    
    for(int i=0;i<p_no_bins;i++)
    {
      if(i%1000 == 0 && o_cat.isDebugEnabled()) o_cat.debug("reached bin: " + i);
      //x_clone = (HashMap)(Keyword.o_keywords.clone());
      x_clone = (HashMap)(p_item_id_table.clone());
      
        
        //x_vector = new Vector(x_clone.keySet());
      x_vector = Node.getVectorFromMap(x_clone);

        // need to grab three keywords at random
        
      for(int j=0;j<p_no_items_per_bin;j++)
      {
        //x_random = Math.random();
        x_rnd = p_random.nextDouble();
        //x_random = 0.3D;
        
        x_pick = (int)(x_rnd * x_vector.size());
        // orig x_vector.remove(x_vector.elementAt(x_pick));
        x_item_id = (String)(x_vector.elementAt(x_pick)); // this is replacement for MVM
        x_vector.removeElementAt(x_pick); // this is replacement for MVM
          
        x_items[i][j] = p_item_id_table.get(x_item_id);
        if(o_cat.isDebugEnabled()) o_cat.debug("x_items["+i+"]["+j+"]:" + x_items[i][j]);
          
      } 
    }

    return x_items;
  }

    /**
   * create a zipf distribution 
   *
   * @param p_no_bins
   * @param p_no_items_per_bin
   * @param p_item_table                    hashtable of items and item ids
   * @param p_item_id_table                 hashtable of item ids and longs
   * @param p_main_item_proportion          no of bins divided by this number is the no of bins with 1st item
   * @param p_creation                      whether new items should be created to fill distribution
   *
   * @return Vector
   */
  public Vector getZipfDistribution(int p_no_bins, 
                                    int p_no_items_per_bin, 
                                    HashMap p_item_table,
                                    HashMap p_item_id_table,
                                    int p_main_item_proportion, 
                                    boolean p_creation,
                                    Random p_random)
    throws Exception                                     
  {  
    if(p_no_bins < 1) throw new Exception(o_network_ID + " getZipfDistribution() less than one bin not permitted");      
    if(p_no_items_per_bin < 1) throw new Exception(o_network_ID + " getZipfDistribution() less than one item per bin not permitted");      

    // create a vector large enough to contain all bins	
    // fill it with vectors large enough to handle items per bin
    Vector x_vec = new Vector(p_no_bins);
    for(int i=0;i<p_no_bins;i++)
    {
      x_vec.addElement(new Vector(p_no_items_per_bin));
    }
      
    // not sure if I can just put an int[] x_doc = new int[]
    // in a hashtable, maybe I can just use Vectors
            
    int x_current_id = -1;
      
    // take a clone of the vector of vectors
    // I don't remember why we need to clone and re-clone these vectors
    // I guess it is so we can remove bins from consideration once we have
    // added a particular item ..
    Vector x_orig_clone = (Vector)(x_vec.clone());
    Vector x_vec_clone = (Vector)(x_vec.clone());
    int x_clone_length = x_vec_clone.size();
    int x_orig_clone_length = x_orig_clone.size();
    double x_rnd = 0.0D;
    int x_choice = -1;
    Vector x_bin = null;
      
    // Get first item
    long x_current_item_id = 1;
    //String x_current = (String)(Keyword.o_keyword_ids.get(new Long(x_current_keyword_id )));
    String x_current = (String)(p_item_id_table.get(new Long(x_current_item_id)));
    String x_previous = null;
    String x_mou = null;
      
    int x_switch = (int)(p_no_bins/p_main_item_proportion); // this setting makes a lot of difference
                                               // since it sets the proportion  of
                                               // bins having the main item
    //if(x_switch < 10) x_switch = 10;
    int x_subset = x_switch;
    int x_current_block = x_switch;
    int x_bin_id = 0;


    ZipfItem x_item = null;
    String x_item_id = null;
      
    // start loop large enough to fill all vectors in set
    int x_size = p_no_bins * p_no_items_per_bin;	
    //System.out.println("x_size: " + x_size);
    for(int i=1;i<=x_size;i++)
    {
      if(o_verbosity > 0) System.out.println("i: "+i);
      if(o_verbosity > 0) System.out.println("x_size: "+x_size);
      	
      // need some function of i that translates into the current item
      // this should be the inverse zipf function I guess ...
      	
      // should generate some number of items 1 i.e. #bins/3
      // then generate half as many of next item and so on until we fill up
      	
      if(i>x_switch)
      {
        // this works out how many of the current item we will generate	
        x_current_block = (int)(x_current_block * ((double)(x_current_item_id))/
                                             ((double)(x_current_item_id+1)));
        // so we start with X of item 1 and then x/2 of item 2 and then x/3 of item 3 and so on?
        //System.out.println("x_current_block: " + x_current_block);
        x_current_item_id++;
        // create additional items if necessary
        // this will fuck up if we try and create new documents FIXXXXXXXXXXXXXXXXXXXX
        x_current = (String)(p_item_id_table.get(new Long(x_current_item_id )));
        //System.out.println("x_current_item_id: " + x_current_item_id);
        //System.out.println("x_current = " + (x_current==null?"null":"not null"));
        if(x_current == null)
        {
          //System.out.println("no more items - creating new or stick");	
          if(p_creation == true)
          {	
            //System.out.println("Creating new keyword");
            new SimpleKeyword();
            //System.out.println("Zipf keyword creation: " + x_keyword.getKeywordID());
       	  }
       	  else
       	  {
       	    // it seems difficult to go back and create more documents
       	    // so the other alternative would be to reset?
       	    // adding proportionally less of each item ...?
       	    p_main_item_proportion *= 2;
       	    x_switch = (int)(p_no_bins/p_main_item_proportion);
       	    x_current_item_id = 1;
       	    //x_current_item_id--;
       	    i=1;	
       	    // the alternative would presumably be to restart having increased the proportion ...
       	  }  
          x_current = (String)(p_item_id_table.get(new Long(x_current_item_id )));
        }
        //x_keyword = (Keyword)(Keyword.o_keywords.get(x_current));
        //x_keyword.setRarity((int)(x_current_keyword_id));
        //System.out.println(p_item_table);
        x_item = (ZipfItem)(p_item_table.get(x_current));
        //System.out.println("x_item = " + (x_item==null?"null":"not null"));
        x_item.setRarity((int)(x_current_item_id));
        //System.out.println("Switching to new keyword");
        x_vec_clone = (Vector)(x_orig_clone.clone());
        
        //System.out.println("a: " + ((double)(x_current_keyword_id)));
        //System.out.println("b: " + ((double)(x_current_keyword_id+1)));
        //System.out.println("c: " + ((double)(x_current_keyword_id))/
 	                            // ((double)(x_current_keyword_id+1)));

        //System.out.println("x_subset: " + x_subset);
        //System.out.println("x_current_block: " + x_current_block);
        if(x_current_block == 0) x_current_block = 1;
        x_switch = x_switch + x_current_block;
      }
      //x_current = Keyword.getZipfKeywordID(1.0D - (double)(i)/(double)(x_size));
      //System.out.println("i/x_size: " + (1.0D - (double)(i)/(double)(x_size)));
      //System.out.println("x_current: " + x_current);
      	
      //System.out.println("x_vec_clone.size(): "+x_vec_clone.size());
      //System.out.println("x_orig_clone.size(): "+x_orig_clone.size());
      x_clone_length = x_vec_clone.size();  	
      x_orig_clone_length = x_orig_clone.size();  	
      if(x_orig_clone_length == 0) break;
      if(x_clone_length == 0) 
      {i=x_switch; continue;}

      	      	
      //x_rnd = Math.random();
      x_rnd = p_random.nextDouble();
      //System.out.println("x_rnd: " + x_rnd);
      //System.out.println("x_clone_length: " + x_clone_length);
      x_choice = (int) (x_rnd * x_clone_length);  
      //System.out.println("x_choice: " + x_choice);
      x_bin = (Vector)(x_vec_clone.elementAt(x_choice));    	
      	
      // having picked a document vector at random
      // see if it has any keywords
      for(int j=0;j<p_no_items_per_bin;j++)
      {
      	// could loop here until we find a doc with space - not really needed
      	//System.out.println("x_bin.size(): "+x_bin.size());
      	if(x_bin.size() == p_no_items_per_bin)
        {
	  //System.out.println("skipping full doc");
          // this bin has already been filled up with items, so don't consider it
          // for future addition of items
          //x_vec_clone.remove(x_bin);
          x_bin_id = x_vec_clone.indexOf(x_bin);  //mvm
          if(x_bin_id != -1) x_vec_clone.removeElementAt(x_bin_id); // this is replacement for MVM
          //x_orig_clone.remove(x_bin);  // don't want to be looking at this again
          x_bin_id = x_orig_clone.indexOf(x_bin);  //mvm
          if(x_bin_id != -1) x_orig_clone.removeElementAt(x_bin_id);  // this is replacement for MVM
          i--;   //if this bin was full redo 
          break;
        }      	  
        try{x_mou = (String)(x_bin.elementAt(j));}catch(Exception e){x_mou = null;}
      	if(x_mou == null) // if no item add this one
      	{
      	  //System.out.println("adding item");
      	  x_bin.addElement(x_current);
      	  //x_vec_clone.remove(x_doc);
      	  x_bin_id = x_vec_clone.indexOf(x_bin);  //mvm
      	  if(x_bin_id != -1) x_vec_clone.removeElementAt(x_bin_id);// this is replacement for MVM
      	  break;
      	}
      	else if(x_mou.equals(x_current)) // if item already present skip
      	{
      	  //x_vec_clone.remove(x_doc);
      	  x_bin_id = x_vec_clone.indexOf(x_bin);  //mvm
      	  if(x_bin_id != -1) x_vec_clone.removeElementAt(x_bin_id);// this is replacement for MVM
      	  i--;
      	  x_size++;
      	  //System.out.println("Shouldn't occur - found item already in bin");
      	  break;
      	}      	  
      	else // different item already here - try next slot
      	{
      	  //System.out.println("Different item here - try new slot");
          if(j == p_no_items_per_bin - 1)
          {
	    //System.out.println("AAskipping full bin");
            // if bin is now full remove it from consideration
            //x_vec_clone.remove(x_bin);
            //x_orig_clone.remove(x_bin);
            x_bin_id = x_vec_clone.indexOf(x_bin);  //mvm
            if(x_bin_id != -1) x_vec_clone.removeElementAt(x_bin_id);// this is replacement for MVM
            //x_bin_id = x_orig_clone.indexOf(x_bin);  //mvm
            //if(x_bin_id != -1) x_orig_clone.removeElementAt(x_bin_id);// this is replacement for MVM
            i--;   //if this bin was full redo 
          }
      	  continue;
      	} 
  
      }  	  
      	
    }
      
    // now all the bin vectors should be filled up with Zipf distributed items
    return x_vec;
  }

// could do with a get random search start that allowed
// us to exclude some nodes, and/or specify the type of node ...



   /**
   * grab a random node
   *
   * @param p_random        the randomness generator
   *
   * @return Node      a set of keywords
   */
  public Node getRandomNode(Set p_excludes, Random p_random)
    throws Exception
  {
    double x_random;
    int x_pick; 	
    Map x_clone = getNodes();
    Iterator x_iter = p_excludes.iterator();
    Node x_node = null;
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());	
      x_clone.remove(x_node.getNodeID());
    }
    Vector x_vector = Node.getVectorFromMap(x_clone);  	
    x_random = p_random.nextDouble();
    if(x_vector.size() > 0)
    {
      x_pick = (int)(x_random * x_vector.size());
      //System.out.println("x_pick = " + x_pick);
      x_node = (Node)(o_nodes.get(x_vector.elementAt(x_pick))); 
    }
    
    return x_node;    	  	
  }	


   /**
   * grab some keywords at random that appear in the
   * network
   *
   * @param p_random        the randomness generator
   *
   * @return Keyword[]      a set of keywords
   */
  public Node getRandomSearchStart(Set p_excludes, String p_type, Random p_random)
    throws Exception
  {
    double x_random;
    int x_pick; 	
    Map x_clone = getNodes();
    Iterator x_iter = p_excludes.iterator();
    Node x_node = null;
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());	
      x_clone.remove(x_node.getNodeID());
    }
    Vector x_vector = Node.getVectorFromMap(x_clone);  	
    String x_class = "null";
    
    do
    {
      if(x_vector.size() == 0) throw new Exception("Node of type " + 
                                                   p_type +
                                                   " not available given exclude list");
      x_random = p_random.nextDouble();
      x_pick = (int)(x_random * x_vector.size());
     //System.out.println("x_pick = " + x_pick);
      x_node = (Node)(o_nodes.get(x_vector.elementAt(x_pick))); 
      x_vector.removeElementAt(x_pick);
      x_class = x_node.getClass().getName();
    // x_node is the start location for the search
    }while(!x_class.equals(p_type));

    
    return x_node;    	  	
  }	

   /**
   * grab some keywords at random that appear in the
   * network
   *
   * @param p_random        the randomness generator
   *
   * @return Keyword[]      a set of keywords
   */
  public Keyword[] getRandomSearchKeywords(int p_no_keywords, Random p_random)
  {
    // we need a vector of all keywords in the network ... 	
   
    Iterator x_iter = o_nodes.values().iterator();
    
    HashSet x_set = new HashSet();
    Node x_node = null;
    while(x_iter.hasNext())
    {
      x_node = (Node)(x_iter.next());
      x_set.addAll(x_node.getKeywords());	
    }

    Vector x_vector = new Vector(x_set);

    double x_random;
    int x_pick;  	
    Keyword[] x_keywords = new Keyword[p_no_keywords];
    
    for(int i=0;i<p_no_keywords;i++)
    {
      x_random = p_random.nextDouble();
      x_pick = (int)(x_random * x_vector.size());
    //System.out.println("x_pick = " + x_pick);

    // x_node is the start location for the search
      x_keywords[i] = (Keyword)(x_vector.elementAt(x_pick));
      x_vector.removeElementAt(x_pick);
    }
    
    return x_keywords;	
  }

   /**
   * grab some keywords that are associated with a particular
   * node, i.e. those keywords appear at least once in that
   * node
   *
   * @param p_node          the node to base the associations on
   * @param p_random        the randomness generator
   *
   * @return Keyword[]      a set of keywords
   */
  public Keyword[] getAssociatedSearchKeywords(int p_no_keywords, Node p_node, Random p_random)
    throws Exception
  {
    Vector x_vector = new Vector(p_node.getKeywords()); 	
    if(p_no_keywords > x_vector.size()) throw new Exception("Less than " +p_no_keywords+ " in node.");

    double x_random;
    int x_pick;  	
    Keyword[] x_keywords = new Keyword[p_no_keywords];
    
    for(int i=0;i<p_no_keywords;i++)
    {
      x_random = p_random.nextDouble();
      x_pick = (int)(x_random * x_vector.size());
    //System.out.println("x_pick = " + x_pick);

    // x_node is the start location for the search
      x_keywords[i] = (Keyword)(x_vector.elementAt(x_pick));
      x_vector.removeElementAt(x_pick);
    }
    
    return x_keywords;
  }
  
  
  /**
   * get a random search location to start from
   *
   * @param p_random        the randomness generator
   *
   * @return Node           a randomly chosen node to search from
   */
  public Node getRandomSearchStart(Random p_random)
    throws Exception
  {
    double x_random;
    int x_pick; 	
    Vector x_vector = Node.getVectorFromMap(o_nodes);  	
    
    // having removed all the locations of this document from the list of nodes, pick
    // another node at random
    //x_random = Math.random();
    x_random = p_random.nextDouble();
    x_pick = (int)(x_random * x_vector.size());
    //System.out.println("x_pick = " + x_pick);

    // x_node is the start location for the search
    Node x_node = (Node)(o_nodes.get(x_vector.elementAt(x_pick)));
    
    return x_node;    	  
  }
  
  /**
   * given a document to search for, find a search location to start from
   * that doesn't have that document in local content.
   *
   * @param p_document      the document to search for
   * @param p_random        the randomness generator
   *
   * @return Node           a randomly chosen node to search from
   */
  public Node getRandomSearchStart(Document p_document, Random p_random)
    throws Exception
  {  
    double x_random;
    int x_pick; 	
    Vector x_vector = Node.getVectorFromMap(o_nodes);
    Vector x_target_locations = p_document.getLocation();

    Enumeration x_enum = x_target_locations.elements();
    Node x_loc = null;
    while(x_enum.hasMoreElements())
    {
      x_loc = (Node)(x_enum.nextElement());	
      //System.out.println("LOCATION ID: " + x_loc.getNodeID());
      x_vector.removeElement(x_loc.getNodeID());
    }

    // having removed all the locations of this document from the list of nodes, pick
    // another node at random
    //x_random = Math.random();
    x_random = p_random.nextDouble();
    x_pick = (int)(x_random * x_vector.size());
    //System.out.println("x_pick = " + x_pick);

    // x_node is the start location for the search
    Node x_node = (Node)(o_nodes.get(x_vector.elementAt(x_pick)));
    
    if(x_node.hasContent(p_document))
    {
      o_cat.error("Error start node contains target");
    }
    return x_node;  	
  }
   
  /**
   * given a search start location, work out a target document that is associated
   * via a keyword with the contents of the start location and is not present in the local
   * content for the node in question.
   *
   * @param p_node          the search start
   * @param p_random        the randomness generator
   *
   * @return Document       the search target
   */
  public Document getAssociatedSearchTarget(Node p_node, Random p_random)
    throws Exception
  {  
    if(o_cat.isDebugEnabled()) o_cat.debug("selected node: " + p_node.getNodeID());

    // so now we need to go through the contents of this node
    // choose a keyword at random

    double x_random;
    int x_pick; 
    Document x_document = null;
    Keyword x_keyword = null;
    Document x_search_target = null;

    Hashtable x_seen_docs = new Hashtable();
    Hashtable x_seen_keywords = new Hashtable();

        // make sure that we exhaust all possible options at this node before moving on ....

    while(x_search_target == null)
    {
      x_document = p_node.getRandomContent(x_seen_docs);
      if(x_document == null)
	break;
      x_seen_docs.put(x_document,x_document);
      while(x_search_target == null)
      {
        x_keyword = x_document.getRandomKeyword(x_seen_keywords);
        if(x_keyword == null)
	  break;
        x_seen_keywords.put(x_keyword,x_keyword);

        if(o_cat.isDebugEnabled()) o_cat.debug("selected keyword: " + x_keyword);

        // get a clone of all the documents via keyword - problem is that some of these aren't in the network

        Vector x_documents = (Vector)(Document.o_documents_by_keyword.get(x_keyword));
        Vector x_vec_clone = (Vector)(x_documents.clone());

        int x_doc_id = -1;
        Document x_doc = null;
        //String x_dog_tag = null;
        Map x_docs = p_node.getContentsByDocID();
        Iterator x_iter = x_docs.values().iterator();
        // remove the documents located here
        while(x_iter.hasNext())
        {
          x_doc = (Document)x_iter.next();
          if(o_cat.isDebugEnabled()) o_cat.debug("removing doc: " + x_doc.getDocumentID());
          x_doc_id = x_vec_clone.indexOf(x_doc);  //mvm
          if(x_doc_id != -1) x_vec_clone.removeElementAt(x_doc_id);// this is replacement for MVM
        }

        // remove any documents that don't actually exist in the network
        int x_size = x_vec_clone.size();
        Vector x_possibles = new Vector(x_size);

        for(int i=0;i<x_size;i++)
 	{
	  x_doc = (Document)(x_vec_clone.elementAt(i));
          //System.out.println("Checking doc: " + x_doc.getDocumentID());
          //System.out.println("doc location size: " + (x_doc.getLocation()).size());
          if((x_doc.getLocation()).size() > 0)
	  {
	    //System.out.println("adding doc to possibles");
	    x_possibles.addElement(x_doc);
          }
        }
          

            // select one of the remaining documents at random

        if(x_possibles.size() == 0)
	  continue;
   	  
	
        //x_random = Math.random();
        x_random = p_random.nextDouble();
        x_pick = (int)(x_random * x_possibles.size());
	
	if(o_cat.isDebugEnabled()) o_cat.debug("x_pick = " + x_pick);
        if(o_cat.isDebugEnabled()) o_cat.debug("x_possibles.size() = " + x_possibles.size());

        x_search_target = (Document)(x_possibles.elementAt(x_pick));

        // problem may be that documents of this keyword may all be in this location - if so then we
        // should pick another node at random ...
      }
    }
		
    return x_search_target;
  }
  
  
  
  /**
   * creates random topology
   *
   * maybe we should be specifying ingroup and outgroup ....
   *
   * @param p_projecting_nodes          the nodes that will project connections
   * @param p_receiving_nodes           the nodes that will receive connections
   * @param p_no_connections_per_node   the number of outgoing connections per node
   * @param p_reciprocal                if all connections should be in both directions
   */
  public void generateRandomTopology(Vector p_projecting_nodes,
                                     Vector p_receiving_nodes,
                                     int p_no_connections_per_node,
                                     boolean p_reciprocal)
    throws Exception
  {
    int x_no_nodes = p_projecting_nodes.size();

    Node x_node = null;
    Node x_connection = null;
    Vector x_clone_connections = null;
    double x_random = -0.0D;
    int x_pick = -1;
    String x_node_id = null;

    
    for(int i=0;i<x_no_nodes;i++)
    {
      x_clone_connections = (Vector)(p_receiving_nodes.clone());
      x_node = (Node)(o_nodes.get(p_projecting_nodes.elementAt(i)));  
      // select the nodes one at a time

      if(o_cat.isDebugEnabled())
      {
        o_cat.debug("x_clone_connections is " +(x_clone_connections == null?"null":"not null"));
        o_cat.debug("x_node is " +(x_node == null?"null":"not null"));
      }
      
      x_clone_connections.removeElement(x_node.getNodeID()); 
      // don't connect to yourself

      if(o_cat.isDebugEnabled()) o_cat.debug("x_clone_connections == " + x_clone_connections.toString());

      // add connections 

      for(int j=0;j<p_no_connections_per_node;j++)
      {
         x_random = Math.random();
         x_pick = (int)(x_random * x_clone_connections.size());
         x_node_id = (String)(x_clone_connections.elementAt(x_pick));
         x_clone_connections.removeElementAt(x_pick);
         x_connection = (Node)(o_nodes.get(x_node_id));
         x_node.addConnection(x_connection);
         // if these are reciprocal connections add one back as well
         if(p_reciprocal) x_connection.addConnection(x_node);
      }
    }
  	
  }
  
  
  /**
   * creates ring topology
   *
   * @param p_no_connections_per_node
   */
  public void generateRingTopology(Vector p_nodes, int p_no_connections_per_node)
    throws Exception
  {
    int x_no_nodes = p_nodes.size();
    Node x_previous_node = null;
    Node x_node = null;
    Node x_next_node = null;
    
    for(int i=0;i<x_no_nodes;i++)
    {
      x_node = (Node)(o_nodes.get(p_nodes.elementAt(i)));  // select the nodes one at a time  	
    
      if(o_cat.isDebugEnabled())
      {
        o_cat.debug("no nodes: " + x_no_nodes);
        o_cat.debug("previous node: " + ((i-1+x_no_nodes)%x_no_nodes));
        o_cat.debug("next node: " + (i+1)%x_no_nodes);
      }

      if(p_no_connections_per_node < (x_no_nodes/2))
      {
        for(int c=1;c<=p_no_connections_per_node/2;c++)
        {
          x_previous_node = (Node)(o_nodes.get(p_nodes.elementAt((i-c+x_no_nodes)%x_no_nodes)));  // select the nodes one at a time
          x_next_node = (Node)(o_nodes.get(p_nodes.elementAt((i+c)%x_no_nodes)));  // select the nodes one at a time
          x_node.addConnection(x_previous_node);
          x_node.addConnection(x_next_node);
        }
      }
      else
      {
        x_previous_node = (Node)(o_nodes.get(p_nodes.elementAt((i-1+x_no_nodes)%x_no_nodes)));  // select the nodes one at a time
        x_next_node = (Node)(o_nodes.get(p_nodes.elementAt((i+1)%x_no_nodes)));  // select the nodes one at a time
        x_node.addConnection(x_previous_node);
        x_node.addConnection(x_next_node);
      }
    }
  	
  }
  
  
  /**
   * creates power law topology
   *
   * @param p_nodes           the nodes that we will give a power law topology to
   * @param p_constant_c      the linear constant
   * @param p_constant_power  the power factor
   */
  public void generatePowerLawTopology(Vector p_nodes, 
                                       double p_constant_c, 
                                       double p_constant_power,
                                       Random p_random)
    throws Exception
  {
    int x_no_nodes = p_nodes.size();
    
    // so for a real power law topology we need to generate a set of numbers of 
    // vertices - so take x_no_nodes values of k from distribution
    
    int[] x_k = new int[x_no_nodes];
    int x_sum = 0;
    Node[] x_nodes = new Node[x_no_nodes];
    for(int i=0;i<x_no_nodes;i++)
    {
      x_k[i] =  (int)(Math.exp((Math.log(p_random.nextDouble()/p_constant_c))/(-1.0*p_constant_power)));
      x_sum += x_k[i];
      x_nodes[i] = (Node)(o_nodes.get(p_nodes.elementAt(i)));  
      x_nodes[i].setInitialNoConnections(x_k[i]);
    }

    o_cat.info("x_sum " +x_sum); // if sum is odd we should re-calculate

    Node x_node = null;
    Node x_connection = null;
    Vector x_clone_connections = null;
    double x_random = -0.0D;
    int x_pick = -1;
    String x_node_id = null;

    
    for(int i=0;i<x_no_nodes;i++)
    {
      x_clone_connections = (Vector)(p_nodes.clone());
      //x_node = (Node)(o_nodes.get(p_nodes.elementAt(i)));  
      // select the nodes one at a time

      if(o_cat.isDebugEnabled())
      {
        o_cat.debug("x_clone_connections is " +(x_clone_connections == null?"null":"not null"));
        o_cat.debug("x_nodes["+i+"] is " +(x_nodes[i] == null?"null":"not null"));
      }
      
      x_clone_connections.removeElement(x_nodes[i].getNodeID()); 
      // don't connect to yourself

      if(o_cat.isDebugEnabled()) o_cat.debug("x_clone_connections == " + x_clone_connections.toString());

      // add connections 

      int x_required_conns = x_k[i] - x_nodes[i].getNoConnections();  
      for(int j=0;j<x_required_conns;j++)
      {
         x_random = Math.random();
         if(x_clone_connections.size() == 0)
         {
           break;
         }
         x_pick = (int)(x_random * x_clone_connections.size());
         if(o_cat.isDebugEnabled()) o_cat.debug("x_pick == " + x_pick);
         x_node_id = (String)(x_clone_connections.elementAt(x_pick));
         if(o_cat.isDebugEnabled()) o_cat.debug("x_node_id == " + x_node_id);
         x_connection = (Node)(o_nodes.get(x_node_id));
         if(o_cat.isDebugEnabled()) o_cat.debug("x_connection is " +(x_connection == null?"null":"not null"));
         if(x_connection.getNoConnections() < x_connection.getInitialNoConnections())
         {
           x_nodes[i].addConnection(x_connection);
           // if these are reciprocal connections add one back as well
           x_connection.addConnection(x_nodes[i]);
         }
         else
         {
           j--; // we still need to made an additional connection
         }
         x_clone_connections.removeElementAt(x_pick);
      }
    }
  }

  
}

