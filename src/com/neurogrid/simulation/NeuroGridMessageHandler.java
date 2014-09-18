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

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.simulation.root.ContentMessage;
import com.neurogrid.simulation.root.Document;
import com.neurogrid.simulation.root.Network;
import com.neurogrid.simulation.root.Node;
 
 /**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class implements a NeuroGrid Message Handler, which is effectively
 * a Gnutella Message Handler, but uses a different forwarding mechanism and 
 * handles learning in response to local matches.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   17/Mar/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class NeuroGridMessageHandler extends GnutellaMessageHandler
{

  private static Category o_cat = Category.getInstance(NeuroGridMessageHandler.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("NeuroGridMessageHandler logging Initialized");
  }
  
  /**
   * constructor
   *
   * @param p_random      configuration filename
   */
  public NeuroGridMessageHandler(Random p_random)
  {
    super(p_random);
  }  
    
  /**
   * check for a local match against this message
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate success
   */
  public boolean checkLocalMatch(ContentMessage p_message, Node p_node)
    throws Exception
  {
    // stats wise increment no matches and check if we have got first match TTL
    boolean x_return = p_node.hasContent(p_message.getDocument());
    if(x_return == true)
    {
      Network.o_no_matches++;
 	
      if(o_cat.isDebugEnabled()) o_cat.debug("local match");
      Node x_start_node = p_message.getStart();
      
      // if this node is lying about its content we should disconnect from it
      if(!p_node.getContentsByDocID().containsKey(p_message.getDocument().getDocumentID()))
      {
        Network.o_no_false_matches++;
        x_start_node.removeConnection(p_node,o_random); // should this be reciprocal ...?
        x_start_node.removeKnowledge(p_node,p_message.getKeywords());
        // if this node is a neighbour should the origin node be disconnecting from it?
        // and removing any existing recommendations towards this node ...
      }
      else
      {
        if(Network.o_TTL_of_first_match == -1)
        {
          Network.o_TTL_of_first_match = p_message.getTTL();  
        }   
        x_start_node.addConnection(p_node); // should this be reciprocal ...?
        x_start_node.addKnowledge(p_node,p_message.getKeywords());
      }
        
    }
    return x_return;

  }
    
  /**
   * check for a local match against this message
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate success
   */
  public boolean checkFuzzyLocalMatch(ContentMessage p_message, Node p_node)
    throws Exception
  {
    // stats wise increment no matches and check if we have got first match TTL
    Set x_set = p_node.matchingDocuments(p_message.getKeywords());
    // a dishonest node would pass back inaccurate matching docs here
    
    if(x_set != null && x_set.size() > 0)
    {
      // need to update stats
      // no matches increment equal to set size
      Network.o_no_matches += x_set.size(); 
      // node is indicating a set of documents so we add each as a match 
	

      if(o_cat.isDebugEnabled()) o_cat.debug("local match");

      // no. false matches can be calculated based on whether these docs are 
      // associated with desired keyword or not ...
      Iterator x_iter = x_set.iterator();
      Document x_doc = null;
      int x_sum = 0;
      int x_no_matches = x_set.size();

      Node x_start_node = p_message.getStart();
      
      while(x_iter.hasNext())
      {
      	x_doc = (Document)(x_iter.next());
      	x_sum = 0;
      	for(int i=0;i<p_message.getKeywords().length;i++)
      	{
      	  if(x_doc.hasKeyword(p_message.getKeywords()[i]))
      	  {
      	    x_sum++;
            x_start_node.addConnection(p_node);// does this become a reciprocal connection?
            x_start_node.addKnowledge(p_node,p_message.getKeywords()[i]);

          }
        }
        if(x_sum == 0)
        {
          Network.o_no_false_matches++;
          x_no_matches--;
        }
      }
      
            // TTL of first match similar
      if(x_no_matches>0 && Network.o_TTL_of_first_match == -1)
      {
        Network.o_TTL_of_first_match = p_message.getTTL();   
      } 
      
      // above message handler should take into account message type???	
    	
      return true;
    }
    
    return false;
  }
  
	 
  /**
   * forward message
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate success
   */
  public boolean forwardMessage(ContentMessage p_message, Node p_node)
    throws Exception
  {
    // do we need to set a min or max number of forwardings ....?
    // seems like these should both be network parameters?
    //NeuroGridNetwork.o_max_forwarding_degree;
    //NeuroGridNetwork.o_min_forwarding_degree;	
  	
    Node x_temp_node = null;
    String x_node_id = null;
    ContentMessage x_new_message = null;
    NeuroGridRating x_rating = null;
    Map x_conns = p_node.getConnList();
    if(o_cat.isDebugEnabled()) o_cat.debug("connections no. = "+x_conns.size());
    Node x_previous = p_message.getPreviousLocation();
    if(x_previous != null)
    {
      x_conns.remove(x_previous.getNodeID());
    }
    // do this first so that we don't get confused with the clones ..

    p_message.decrementTTL();
    p_message.setPreviousLocation(p_node);

    Object x_object = null;
    int i=0;
    TreeSet x_set = p_node.getSortedRecommendation(p_message.getKeywords());
    Iterator x_iter = x_set.iterator();
    while(x_iter.hasNext())
    {
      x_object = x_iter.next();
      if(o_cat.isDebugEnabled()) o_cat.debug(x_object.getClass());
      //o_cat.debug(((java.util.HashMap.Entry)x_object).getValue());
      x_rating = (NeuroGridRating)(x_object);
      x_temp_node = (Node)(x_rating.getNode());
      
      // check that we are connected to this node
      // and make sure we are not forwarding back to node we received from
      x_temp_node = (Node)(x_conns.remove(x_temp_node.getNodeID()));
      if(o_cat.isDebugEnabled()) o_cat.debug("connections no. = "+x_conns.size());
      if(o_cat.isDebugEnabled()) o_cat.debug("connections no. = "+p_node.getNoConnections());
      
      if(x_temp_node != null)
      {
        forwardMessageToSpecificNode(p_message,x_temp_node,(i>0));  
        i++;
        if(i>=p_node.getNetwork().o_max_forwarding_degree)
        {
          break; // avoid sending more than the max
        }  
      }
    }
    // in some ways forwarding to all and reconnecting might be the most efficient
    // rather than trying to forward down a subset of connections --> depends on
    // overall network activity perhaps --> automated throttling seems funky
    // send minimum required ...
    if(i<p_node.getNetwork().o_min_forwarding_degree)
    {
      if(o_cat.isDebugEnabled()) o_cat.debug("forwarding to additional nodes to make up for recommendation shortfall");
      if(o_cat.isDebugEnabled()) o_cat.debug("remaining connections no. = "+x_conns.size());
      Map.Entry x_entry = null;
      Vector x_vector = Node.getVectorFromMap(x_conns);
      double x_random = 0.0D;
      int x_pick = -1;

      int x_no_remaining = x_vector.size();
       
      for(int j=0;j<x_no_remaining;j++) 
      {
        if(x_vector.size() == 0)
          break;
        // choose nodes at random and send
        x_random = o_random.nextDouble();
        x_pick = (int) (x_random * x_vector.size());
        x_node_id = (String)(x_vector.elementAt(x_pick));
        x_vector.removeElementAt(x_pick);
        x_temp_node = (Node)(x_conns.get(x_node_id));       

        if(x_temp_node != null)
        {       
          forwardMessageToSpecificNode(p_message,x_temp_node,(i>0));
          i++;
          if(o_cat.isDebugEnabled()) o_cat.debug("i: "+i);
          if(o_cat.isDebugEnabled()) o_cat.debug("i>=p_node.getNetwork().o_min_forwarding_degree: "+(i>=p_node.getNetwork().o_min_forwarding_degree));
          if(o_cat.isDebugEnabled()) o_cat.debug("p_node.getNetwork().o_min_forwarding_degree: "+(p_node.getNetwork().o_min_forwarding_degree));
          if(i>=p_node.getNetwork().o_min_forwarding_degree)
          {
            break; // avoid sending more than the max
          }
        }  
      }
    }


    return true;
  }
  
  
  /**
   * forward message
   *
   * @param p_message    the incoming message
   * @param p_node       the node to forward to
   *
   * @return boolean     flag to indicate success
   */
  public boolean forwardMessageToSpecificNode(ContentMessage p_message, Node p_node, boolean p_create)
    throws Exception
  {
    if(o_cat.isDebugEnabled()) o_cat.debug("Forwarding to " + p_node.getNodeID());

    if(p_create==true)
    {
      if(p_message.getClass().getName().equals("com.neurogrid.simulation.SimpleContentMessage"))
      {
        p_message = new SimpleContentMessage(p_message);
      }
      else if(p_message.getClass().getName().equals("com.neurogrid.simulation.FuzzyContentMessage"))
      {
        p_message = new FuzzyContentMessage(p_message);
      }
      else
      {
        throw new Exception("unable to forward message of type "+p_message.getClass().getName());
      }
    }
    p_node.addMessageToInbox(p_message); 
    
    return true;
  }

}

