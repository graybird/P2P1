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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.simulation.root.ContentMessage;
import com.neurogrid.simulation.root.ContentMessageHandler;
import com.neurogrid.simulation.root.Document;
import com.neurogrid.simulation.root.Message;
import com.neurogrid.simulation.root.Network;
import com.neurogrid.simulation.root.Node;
 
 /**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class implements a Gnutella Message Handler.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   17/Mar/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class GnutellaMessageHandler implements ContentMessageHandler
{

  private static Category o_cat = Category.getInstance(GnutellaMessageHandler.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("GnutellaMessageHandler logging Initialized");
  }
	
  protected Random o_random = null;
  
  /**
   * constructor
   *
   * @param p_random      configuration filename
   */
  public GnutellaMessageHandler(Random p_random)
  {
    o_random = p_random;
  }  

  /**
   * handle a message arriving at a particular node
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate a successful operation
   */
  public boolean handleMessage(Message p_message, Node p_node)
    throws Exception
  {
    return handleMessage((ContentMessage)p_message,p_node);	
  }
  
  /**
   * handle a message arriving at a particular node
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate a successful operation
   */
  public boolean handleMessage(ContentMessage p_message, Node p_node)
    throws Exception
  {
    if(p_message == null) throw new Exception("handleMessage() p_message argument is null");
    if(p_node == null) throw new Exception("handleMessage() p_node argument is null");


    if(o_cat.isDebugEnabled()) o_cat.debug(p_node.getNodeID() + " processing " + p_message.toString());	  	
    // 1. Update stats re message transfer
    Network.o_message_transfers++; 
    // 2. Check whether message has been seen
    if(seenMessage(p_message,p_node) == true)
    {
      return false;
    }
    if(o_cat.isDebugEnabled()) o_cat.debug("unseen message");	  	
    // 3. Update node activity stats 
    //Node.o_activated_node.put(p_node,p_node);
    p_node.setActive();
    // 4. remove message from previous nodes message box ??
    p_node.removeFromInbox(p_message);
    // 5. check for a local match
    if(p_message.getClass().getName().equals("com.neurogrid.simulation.SimpleContentMessage"))
    {
      checkLocalMatch(p_message,p_node); // real gnutella networks carry on forwarding
    }
    else if(p_message.getClass().getName().equals("com.neurogrid.simulation.FuzzyContentMessage"))
    {
      checkFuzzyLocalMatch(p_message,p_node); // real gnutella networks carry on forwarding
    }
    else
    {
      throw new Exception("Unable to handle message of type "+p_message.getClass().getName());	
    }
    
    if(o_cat.isDebugEnabled()) o_cat.debug("no local match");	  	
    // 6. check message TTL
    if(checkMessageTTL(p_message,p_node) == false)
      return false;
    if(o_cat.isDebugEnabled()) o_cat.debug("TTL fine");	  	
    // 7. add message to all connected nodes (except the one we received from )
    return forwardMessage(p_message,p_node);
  }

  /**
   * inject a message into a particular node
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate a successful operation
   */
  public boolean injectMessage(Message p_message, Node p_node)
    throws Exception
  {
    return injectMessage((ContentMessage)p_message,p_node);
  }
  
  /**
   * inject a message into a particular node
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate a successful operation
   */
  public boolean injectMessage(ContentMessage p_message, Node p_node)
    throws Exception
  {
    if(p_message == null) throw new Exception("handleMessage() p_message argument is null");
    if(p_node == null) throw new Exception("handleMessage() p_node argument is null");


    if(o_cat.isDebugEnabled()) o_cat.debug(p_node.getNodeID() + " processing " + p_message.toString());	  	
    // 1. Update stats re message transfer
    //Network.o_message_transfers++; 
    // 2. Check whether message has been seen
    if(seenMessage(p_message,p_node) == true)
      return false;
    if(o_cat.isDebugEnabled()) o_cat.debug("unseen message");	  	
    // 3. Update node activity stats --> this is just graphics ..?
    p_node.setActive();
    // 4. remove message from previous nodes message box ??
    p_node.removeFromInbox(p_message);
    // 5. don't check for a local match 	
    // 6. check message TTL
    if(checkMessageTTL(p_message,p_node) == false)
      return false;
    if(o_cat.isDebugEnabled()) o_cat.debug("TTL fine");	  	
    // 7. add message to all connected nodes (except the one we received from )
    return forwardMessage(p_message,p_node);
  }
  
  /**
   * check if a message has been seen, and add it to 
   * list of seen messages if it has not
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate if the message has been seen
   */
  public boolean seenMessage(ContentMessage p_message, Node p_node)
    throws Exception
  {

    String x_previous = p_node.checkSeen(p_message);
    
    if(o_cat.isDebugEnabled()) o_cat.debug(p_node.getNodeID() + " seen " + x_previous);

    if(x_previous != null)
    {
      //try{p_message.getPreviousLocation().o_sending_message.clear();}catch(Exception e){e.printStackTrace();}
      if(o_cat.isDebugEnabled()) o_cat.debug("Seen Already: stop processing");
      return true;
    }
    
    p_node.addToSeen(p_message);
 

    return false;
  }
  
  /**
   * given that we are going to process this message adjust the state
   * of the node that sent the message, and remove message from local
   * message box
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate success
   */
  public boolean processCommit(ContentMessage p_message, Node p_node)
    throws Exception
  {
    
    //o_in_box.removeElement(p_message);
    //p_node.removeFromInbox(p_message);
    // not sure how much we really need this method 
    // am thinking it needs testing at the node level
    
    return true;
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
      if(Network.o_TTL_of_first_match == -1)
      {
        Network.o_TTL_of_first_match = p_message.getTTL();  
      }  	
      if(o_cat.isDebugEnabled()) o_cat.debug("local match");
      
      if(!p_node.getContentsByDocID().containsKey(p_message.getDocument().getDocumentID()))
      {
        Network.o_no_false_matches++;
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
      // TTL of first match similar
      if(Network.o_TTL_of_first_match == -1)
        Network.o_TTL_of_first_match = p_message.getTTL();    	

      if(o_cat.isDebugEnabled()) o_cat.debug("local match");

      // no. false matches can be calculated based on whether these docs are 
      // associated with desired keyword or not ...
      Iterator x_iter = x_set.iterator();
      Document x_doc = null;
      int x_sum = 0;
      
      while(x_iter.hasNext())
      {
      	x_doc = (Document)(x_iter.next());
      	x_sum = 0;
      	for(int i=0;i<p_message.getKeywords().length;i++)
      	{
      	  if(x_doc.hasKeyword(p_message.getKeywords()[i]))
      	  {
      	    x_sum++;
          }
        }
        if(x_sum == 0)
        {
          Network.o_no_false_matches++;
        }
      }
      
      // above message handler should take into account message type???	
    	
      return true;
    }
    
    return false;
  }
  
  /**
   * check ttl of message
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate success
   */
  public boolean checkMessageTTL(Message p_message, Node p_node)
  {
    if(p_message.getTTL() <= 1) return false;
    else return true;
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
    Node x_temp_node = null;
    String x_node_id = null;
    Message x_new_message = null;
    Map.Entry x_entry = null;
    Map x_conns = p_node.getConnList();
    Node x_previous = p_message.getPreviousLocation();
    if(x_previous != null)
      x_conns.remove(x_previous.getNodeID());

    // do this first so that we don't get confused with the clones ..

    p_message.decrementTTL();
    p_message.setPreviousLocation(p_node);

    int i=0;
    Iterator x_iter = x_conns.entrySet().iterator();
    while(x_iter.hasNext())
    {
      x_entry = (Map.Entry)(x_iter.next());
      x_temp_node = (Node)(x_entry.getValue());
      if(o_cat.isDebugEnabled()) o_cat.debug(i+":Forwarding to " + x_temp_node.getNodeID());
      //x_new_message = new SimpleMessage(p_message);
      if(i>0)
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

      //o_sending_message.put(x_temp_node,x_temp_node); // this is really graphics support
      x_temp_node.addMessageToInbox(p_message);   
      i++;
    }

    return true;
  }
  
  


}

