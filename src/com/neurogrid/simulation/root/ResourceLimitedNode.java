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
 
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.parser.MultiHashtable;
 
/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class represents nodes in the network.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   6/Oct/2001    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public abstract class ResourceLimitedNode extends Node
{
  // private variables
  private static final String cvsInfo = "$Id: ResourceLimitedNode.java,v 1.5 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  private static Category o_cat = Category.getInstance(ResourceLimitedNode.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("ResourceLimitedNode logging Initialized");
  }

   
  public class LimitedLinkedHashMap extends LinkedHashMap
  {

    public LimitedLinkedHashMap(int p_initialCapacity, float p_loadFactor, boolean p_accessOrder)
    {
      super(p_initialCapacity,p_loadFactor,p_accessOrder);   
    }

    protected boolean removeEldestEntry(Map.Entry eldest) 
    {
      boolean x_remove = this.size() > o_max_connections;
      //System.out.println("Checking whether to remove eldest entry: " + x_remove);
      return x_remove;
    }
     
  }

  //protected LimitedLinkedHashMap o_conn_list = null;          // list of connections to other nodes

  //protected LinkedList o_conn_ll = new LinkedList();                      // linked list that also stores
                                                              // connections but allows us to use FIFO
                                                              // to limit no of connections
  /*
  public MultiHashtable o_contents = new MultiHashtable(); // stored docs indexed via keyword?
  protected Hashtable o_contents_by_doc_id = new Hashtable();  // stored docs indexed via doc id
    */
  protected int o_max_connections = 6;
  protected int o_min_connections = 3;
  protected int o_max_knowledge = 10;
  
  // should make the conn_list a MultiHashtable ... FIXXXXXXXXXXX
  // think the above is outdated, although there could be an argument
  // to say connections should be named/weighted
  
  
  public ResourceLimitedNode(int p_max_connections, int p_min_connections, int p_max_knowledge)
//    throws Exception
  {  
    o_max_connections = p_max_connections;
    o_min_connections = p_min_connections;
    if(o_cat.isInfoEnabled()) o_cat.info("o_max_connections: " + o_max_connections);
    if(o_cat.isInfoEnabled()) o_cat.info("o_min_connections: " + o_min_connections);
    o_max_knowledge = p_max_knowledge;  	
    //o_knowledge = new MultiHashtable(100,0.75F,true,o_max_knowledge);// access order
    o_knowledge = new MultiHashtable(100,0.75F,false,o_max_knowledge);// insertion order
    o_conn_list = new LimitedLinkedHashMap(100,0.75F,false);// insertion order
    //o_node_ID = SimpleNode.getNewNodeID();	
    //o_nodes.put(o_node_ID, this);

  }

  /**
   * Add connection between this node and another
   *
   * @param p_node     the node to connect to 
   */
  public void addConnection(Node p_node)
    throws Exception
  {
    if(p_node == null) throw new Exception(o_node_ID + " addConnection() argument is null");
    
    // remove it in order to make sure we update the insertion order
    // have tested that we really do need this ...
    o_conn_list.remove(p_node.getNodeID());
    if(o_cat.isDebugEnabled()){o_cat.debug("removed " + p_node.getNodeID() + " from connections list");}
    o_conn_list.put(p_node.getNodeID(),p_node);
    if(o_cat.isInfoEnabled()){o_cat.info("added " + p_node.getNodeID() + " to connections list");}
    if(o_cat.isInfoEnabled()){o_cat.info("no conns=" + getNoConnections());}
    if(o_cat.isInfoEnabled()){o_cat.info("o_conn_list.size()=" + o_conn_list.size());}
    if(o_cat.isInfoEnabled()){o_cat.info("o_max_connections=" + o_max_connections);}
  }

  /**
   * Remove connection between this node and another
   *
   * @param p_node     the node to remove the connection to 
   * 
   * @throws Exception - a general exception
   */
  public void removeConnection(Node p_node)
    throws Exception
  {
    removeConnection(p_node, new Random(System.currentTimeMillis()));
  }
 
  /**
   * Remove connection between this node and another
   *
   * @param p_node     the node to remove the connection to 
   * @param p_random   random number generator in case we need to reconnect to maintain
   *                   minimum number of connections 
   * 
   * @throws Exception   a general exception
   */
  public void removeConnection(Node p_node, Random p_random)
    throws Exception
  {
    if(p_node == null) {throw new Exception(o_node_ID + " removeConnection() argument is null");} 
    
    //if we wanted to enforce a minimum number of connections we could either just
    //block removal of connections, or add another connection to a random node in the network ...
    o_conn_list.remove(p_node.getNodeID());
    if(o_conn_list.size() < o_min_connections)
    {
      HashSet x_set = new HashSet(o_conn_list.values());
      x_set.add(p_node);
      Node x_node = o_network.getRandomNode(x_set,p_random);	
      if(x_node != null)
      {
        o_conn_list.put(x_node.getNodeID(),x_node);
      }
    }
  }


}

