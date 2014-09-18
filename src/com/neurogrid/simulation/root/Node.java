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

package com.neurogrid.simulation.root;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.parser.MultiHashtable;
import com.neurogrid.simulation.NeuroGridComparator;
import com.neurogrid.simulation.NeuroGridRating;

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

public abstract class Node
{
	// private variables
	private static final String cvsInfo =
		"$Id: Node.java,v 1.6 2003/06/25 12:16:20 samjoseph Exp $";
	/**
	 * @return the cvs string
	 */
	public static String getCvsInfo()
	{
		return cvsInfo;
	}

	private static Category o_cat = Category.getInstance(Node.class.getName());

	/**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
	public static void init(String p_conf)
	{
		BasicConfigurator.configure();
		PropertyConfigurator.configure(p_conf);
		o_cat.info("Node logging Initialized");
	}

	protected static long o_global_node_id = 0;

	protected int o_initial_no_conns = 0;

	protected boolean o_activated = false;
	protected String o_node_ID = null;
	protected Network o_network = null;

	//private String o_node_ID = getNewNodeID(); // this will need to go into constructor

	// should the connection list even be a hashtable
	// I mean the node id itself is contained inside the node
	// we could just have a hash set - the string node id is just really
	// to help me when I'm looking at debug output ...
	// any time we want to display we can grab it from the node itself, right?
	// I guess there is a question about what we use to specify a node in order to be able to grab
	// hold of it ..., but perhaps that should always be the reference to the object itself ...?
	// since within java that can always be turned into the object and thus the String id ...

	// and rather than getConnList we should be handing out a cloned set of 
	// connections(i.e. node refs)? or something ...
	// if we had a HashSet we could pass out an iterator like this:
	//  set.clone.iterator() --> actually a clone itself would be most flexible ..
	// one thing that would prevent us from switching to set generally is that the LinkedHashSet
	// doesn't support the removeEldestEntry method ...

	// if we stayed with hashmap then we could do something like
	// map.clone.values() and we would get back a collection object
	// should the initialization be moved to classes with constructor ...?

	protected Map o_conn_list = new HashMap();
	// list of connections to other nodes
	protected MultiHashtable o_contents = new MultiHashtable();
	// stored docs indexed via keyword?
	protected Hashtable o_contents_by_doc_id = new Hashtable();
	// stored docs indexed via doc id

	// I guess knowledge and associated methods should go into another base abstract class
	// because it is not needed by the gnutella nodes
	protected MultiHashtable o_knowledge = new MultiHashtable();
	// stores keyword-node relations
	private static final int SEEN_GUID_CACHE_START_SIZE = 10;
	protected Hashtable o_seenGUIDs = new Hashtable(SEEN_GUID_CACHE_START_SIZE);
	//private Hashtable o_query_from_which_node = null;
	protected LinkedHashMap o_in_box = new LinkedHashMap();
	// does insertion order mean most recent out first?
	// actually given the way we process messages the inbox is not really used since the order the messages
	// are processed is pretty much fixed ... although not perfectly perhaps ...
	// however it would be used to resolve conflicts in an event based system ..? or maybe not ...
	// really the location is set within the message ... 
	//private static Vector o_search_match = new Vector(500);

	protected HashSet o_sending_message = new HashSet();
	// nodes that we are sending a message to ...

	protected MessageHandler o_message_handler = null;

	/**
	 * get the network this node is a part off
	 *
	 * @return Network
	 */
	public Network getNetwork()
	{
		return o_network;
	}

	/**
	 * indicate that this node is sending a message
	 *
	 * @param p_node  ???
	 */
	public void indicateSendingMessage(Node p_node)
	{
		o_sending_message.add(p_node);
	}

	/**
	 * get the size of the sending message list
	 *
	 * @return int the size of the sending message list
	 */
	public int getNoSendingMessage()
	{
		return o_sending_message.size();
	}

	/**
	 * set this node inactive
	 */
	public void setInactive()
	{
		o_network.removeActiveNode(this);
		o_activated = false;
	}

	/**
	 * set this node active
	 */
	public void setActive()
	{
		o_network.addActiveNode(this);
		o_activated = true;
	}

	/**
	 * @return boolean whether this node is active or not
	 */
	public boolean getActive()
	{
		return o_activated;
	}

	/**
	 * Get Node ID
	 *
	 * @return String the node ID
	 */
	public String getNodeID()
	{
		return o_node_ID;
	}

	/**
	 * Get number of connections
	 *
	 * @return int the number of connections
	 */
	public int getNoConnections()
	{
		return o_conn_list.size();
	}

	/**
	 * Get the number of contents
	 *
	 * @return int
	 */
	public int getNoContents()
	{
		return o_contents_by_doc_id.size();
	}

	/**
	 * Get size of knowledge base
	 *
	 * @return int
	 */
	public int getNoKnowledge()
	{
		return o_knowledge.size();
	}

	/**
	 * Get total size of knowledge base
	 *
	 * get the sum total of the sizes of the vectors in the knowledge multihashtable, i.e how many
	 * keyword-node associations are being stored
	 *
	 * @return int
	 */
	public int getTotalNoKnowledge()
	{
		Iterator x_iter = o_knowledge.keySet().iterator();
		int x_sum = 0;
		Vector x_vector = null;
		Keyword x_key = null;

		while (x_iter.hasNext())
		{
			x_key = (Keyword) (x_iter.next());
			x_vector = (Vector) (o_knowledge.get(x_key));
			x_sum += x_vector.size();
		}

		return x_sum;
	}

	/**
	 * Check InBox
	 *
	 * @return boolean
	 */
	public boolean checkInbox()
	{
		boolean x_full = true;
		if (o_in_box.size() == 0)
		{
			x_full = false;
		}

		return x_full;
	}

	/**
	 * clear clear this node
	 */
	public void refresh()
	{
		clearInbox();
		clearGUIDs();
		setInactive();
		clearSendingMessage();
	}

	/**
	 * clear clear this node
	 */
	public void clear()
	{
		clearConnList();
		clearContents();
		clearInbox();
		clearGUIDs();
		setInactive();
		clearSendingMessage();
	}

	/**
	 * clear Sending message
	 */
	public void clearSendingMessage()
	{
		o_sending_message.clear();
	}

	/**
	  * clear InBox
	  */
	public void clearInbox()
	{
		o_in_box.clear();
	}

	/**
	  * clear GUIDS
	  */
	public void clearGUIDs()
	{
		o_seenGUIDs.clear();
	}

	/**
	  * clear connections
	  */
	public void clearConnList()
	{
		o_conn_list.clear();
	}

	/**
	  * clear contents
	  */
	public void clearContents()
	{
		o_contents.clear();
		o_contents_by_doc_id.clear();
	}

	/**
	  * clear knowledge
	  */
	public void clearKnowledge()
	{
		o_knowledge.clear();
	}

	/**
	 * Get string representation
	 *
	 * @return String
	 */
	public abstract String toString();

	/**
	 * Get Node connection list - pass out a clone 
	 * so that the conn list can only be modified 
	 * internally to the object
	 *
	 * @return Hashtable
	 */
	public Map getConnList()
	{
		return (Map) (((HashMap) (o_conn_list)).clone());
	}

	/**
	 * Get all keywords stored in this node independently of how many times they are 
	 * associated with different documents in this node
	 *
	 * @return Set
	 */
	public Set getKeywords()
	{
		return o_contents.keySet();
	}

	/**
	 * Get all keywords stored in this node independently of how many times they are 
	 * associated with different documents in this node
	 *
	 * @return Keyword[]
	 */
	public Keyword[] allKeywords()
	{
		int x_no_keys = o_contents.size();
		Keyword[] x_keywords = new Keyword[x_no_keys];

		Iterator x_iter = o_contents.keySet().iterator();
		int i = 0;
		while (x_iter.hasNext())
		{
			x_keywords[i++] = (Keyword) (x_iter.next());
		}

		return x_keywords;
	}

	/**
	 * Add connection between this node and another
	 *
	 * @param p_node     the node to connect to 
	 * 
	 * @throws Exception general exception
	 */
	public void addConnection(Node p_node) throws Exception
	{
		if (p_node == null)
		{
			throw new Exception(
				o_node_ID + " addConnection() argument is null");
		}

		o_conn_list.put(p_node.getNodeID(), p_node);
	}

	/**
	 * Remove connection between this node and another
	 *
	 * @param p_node     the node to remove the connection to 
	 * 
	 * @throws Exception general exception
	 */
	public void removeConnection(Node p_node) throws Exception
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
	 * @throws Exception general exception
	 */
	public void removeConnection(Node p_node, Random p_random) throws Exception
	{
		if (p_node == null)
		{
			throw new Exception(
				o_node_ID + " removeConnection() argument is null");
		}
		o_conn_list.remove(p_node.getNodeID());
	}

	/**
	 * @param p_node  the node we are checking for a connection with
	 * @return  true if there is a connection to this node
	 * @throws Exception general exception
	 */
	public boolean hasConnection(Node p_node) throws Exception
	{
		if (p_node == null)
		{
			throw new Exception(
				o_node_ID + " hasConnection() argument is null");
		}

		return o_conn_list.containsKey(p_node.getNodeID());
	}

	/**
	 * Add some content to this node
	 *
	 * @param p_document     the document to add to this node
	 * @throws Exception general exception
	 */
	public void addContent(Document p_document) throws Exception
	{
		if (p_document == null)
		{
			throw new Exception(o_node_ID + " addContent() argument is null");
		}
		Keyword[] x_keywords = p_document.getKeywords();
		for (int i = 0; i < x_keywords.length; i++)
		{
			o_contents.put(x_keywords[i], p_document);
			o_contents_by_doc_id.put(p_document.getDocumentID(), p_document);
		}
		p_document.setLocation(this);
	}

	/**
	 * check if this node has a particular document
	 *
	 * @param p_document     the document to add to this node
	 * @return boolean       whether this node contains this document or not
	 * @throws Exception general exception
	 */
	public boolean hasContent(Document p_document) throws Exception
	{
		if (p_document == null)
		{
			throw new Exception(o_node_ID + " hasContent() argument is null");
		}
		return o_contents_by_doc_id.containsKey(p_document.getDocumentID());
	}

	/**
	 * check if this node has a document with a particular kewyord
	 *
	 * @param p_keyword    the keyword to check for
	 * @return int         the number of documents associated with this keyword at this node
	 * @throws Exception general exception
	 */
	public int hasKeyword(Keyword p_keyword) throws Exception
	{
		if (p_keyword == null)
		{
			throw new Exception(o_node_ID + " hasKeyword() argument is null");
		}

		Vector x_docs = (Vector) (o_contents.get(p_keyword));
		int x_size = 0;
		if (x_docs != null)
		{
			x_size = x_docs.size();
		}
		return x_size;
		//return 1;
	}

	/**
	 * get all the documents in this node that are associated with these keywords
	 * (not sure about whether this should be an AND or an OR operation ...)
	 *
	 * @param p_keywords    the keywords to check for
	 * @return Set          the documents associated with these keywords at this node
	 * @throws Exception general exception
	 */
	public Set matchingDocuments(Keyword[] p_keywords) throws Exception
	{
		if (p_keywords == null)
		{
			throw new Exception(
				o_node_ID + " matchingDocuments() argument is null");
		}

		HashSet x_set = new HashSet();

		Vector x_docs = null;
		for (int i = 0; i < p_keywords.length; i++)
		{
			if (p_keywords[i] == null)
			{
				throw new Exception(
					o_node_ID
						+ " matchingDocuments() p_keywords["
						+ i
						+ "] is null");
			}

			x_docs = (Vector) (o_contents.get(p_keywords[i]));
			if (x_docs != null)
			{
				x_set.addAll(x_docs);
			}
		}
		return x_set;
		//return null;
	}

	/**
	 * checks how many keywords present in the node contents match the keywords
	 * passed to the methods
	 *
	 * this could be adjusted to take into account the number of documents that are related
	 * to a particular keyword,  i.e. a match against a node that has 20 documents related to keyword 7
	 * is much more useful than against a node that has only 1?????  Although we could argue that
	 * we want to match as specifically as possible, i.e. having 2 out of 3 keywords is more useful than
	 * having 1 out of 3 even if there are hundreds of documents
	 *
	 * @param p_keywords           the keywords to check against
	 *
	 * @return int                 the number of matches
	 * @throws Exception general exception
	 */
	public int matchingKeywords(Keyword[] p_keywords) throws Exception
	{
		if (p_keywords == null)
		{
			throw new Exception(
				o_node_ID + " matchingKeywords() argument is null");
		}

		//HashSet x_keys = new HashSet(o_contents.keySet());
		int x_overlap = 0;
		if (o_contents != null)
		{
			for (int i = 0; i < p_keywords.length; i++)
			{
				if (o_contents.get(p_keywords[i]) != null)
				{
					x_overlap++;
				}
			}
		}
		return x_overlap;
	}

	/**
	 * set the number of initial connections
	 *
	 * @param p_initial_no_conns    the number of initial connections for this node 
	 */
	public void setInitialNoConnections(int p_initial_no_conns)
	{
		o_initial_no_conns = p_initial_no_conns;
	}

	/**
	 * get the number of initial connections
	 *
	 * @return int         the number of initial connections for this node 
	 */
	public int getInitialNoConnections()
	{
		return o_initial_no_conns;
	}

	/**
	  * get the node content
	  *
	  * @return Iterator
	  */
	public Iterator getContent()
	{
		return o_contents.keySet().iterator();
	}

	/**
	 * get the node content in the form of a hashtable relating doc id to document
	 *
	 * @return Map
	 */
	public Map getContentsByDocID()
	{
		return (Map) (o_contents_by_doc_id.clone());
	}

	/**
	 * get a random document from the node content
	 *
	 * @return Document
	 */
	public Document getRandomContent()
	{
		Vector x_vector = (Vector) (o_contents.get(getRandomKeyword()));

		double x_random = Math.random();
		int x_pick = (int) (x_random * x_vector.size());

		return (Document) (x_vector.elementAt(x_pick));
	}

	/**
	 * get a random document from the node content, excluding any in the hashtable provided
	 *
	 * @param p_exclude  keywords to exclude from the random selection
	 *
	 * @return Document a randomly selected document
	 * @throws Exception general exception
	 */
	public Document getRandomContent(Hashtable p_exclude) throws Exception
	{
		Hashtable x_clone = (Hashtable) (o_contents_by_doc_id.clone());

		Document x_doc = null;

		Enumeration x_enum = p_exclude.keys();
		while (x_enum.hasMoreElements())
		{
			x_doc = (Document) (x_enum.nextElement());
			x_clone.remove(x_doc.getDocumentID());
		}

		Vector x_vector = getVectorFromHashtable(x_clone);

		if (x_vector.size() == 0)
		{
			return null;
		}

		double x_random = Math.random();
		int x_pick = (int) (x_random * x_vector.size());

		return (Document)
			(Document.o_documents.get(x_vector.elementAt(x_pick)));
	}

	/**
	 * get a random keyword from the node content
	 *
	 * @return Keyword
	 */
	public Keyword getRandomKeyword()
	{
		//Set x_set = o_contents.keySet();
		//Vector x_vector = new Vector(x_set);
		Iterator x_iter = o_contents.keySet().iterator();
		Vector x_vector = new Vector(o_contents.size());
		while (x_iter.hasNext())
		{
			x_vector.addElement(x_iter.next());
		}

		double x_random = Math.random();
		int x_pick = (int) (x_random * x_vector.size());

		return (Keyword) (x_vector.elementAt(x_pick));
	}

	/**
	 * Add knowledge about another node, specifically what keywords are represented in the content
	 *
	 * @param p_node            the node about which we are receiving data
	 * @param p_keywords        the keywords stored in the node
	 * @throws Exception        a general exception
	 */
	
	//一组关键词加入知识库
	public void addKnowledge(Node p_node, Keyword[] p_keywords)
		throws Exception
	{
		if (p_node == null)
		{
			throw new Exception(
				o_node_ID + " addKnowledge() p_node argument is null");
		}

		if (p_keywords == null)
		{
			throw new Exception(
				o_node_ID + " addKnowledge() p_keywords argument is null");
		}

		Vector x_vector = null;
		for (int i = 0; i < p_keywords.length; i++)
		{
			addKnowledge(p_node, p_keywords[i]);
		}

	}

	/**
	 * Add knowledge about another node, specifically which keyword is represented in the content
	 *
	 * @param p_node            the node about which we are receiving data
	 * @param p_keyword         the keyword stored in the node
	 * @throws Exception        a general exception
	 */
	
	//加入一个关键词
	public void addKnowledge(Node p_node, Keyword p_keyword) throws Exception
	{
		if (p_node == null)
		{
			throw new Exception(
				o_node_ID + " addKnowledge() p_node argument is null");
		}

		if (p_keyword == null)
		{
			throw new Exception(
				o_node_ID + " addKnowledge() p_keyword argument is null");
		}

		// looks like we need to be careful here so that the same node does not appear more than
		// once in the Vector associated with each keyword

		Vector x_vector = (Vector) (o_knowledge.get(p_keyword));
		if (x_vector == null || (x_vector.contains(p_node)) == false)
		{
			o_knowledge.put(p_keyword, p_node);
			if (o_cat.isDebugEnabled())
			{
				o_cat.debug(
					p_node.getNodeID() + " nodes associated with " + p_keyword);
			}

			//if(o_cat.isDebugEnabled()) o_cat.debug(((Vector)o_knowledge.get(p_keyword)).getNodeID().first +" retrieved");
		}
	}

	/**
	/**
	 * remove knowledge about another node, specifically what keywords it has
	 * provided incorrect hits against
	 *
	 * @param p_node            the node about which we are receiving data
	 * @param p_keywords        the keywords the node has provided an incorrect response to
	 * @throws Exception        a general exception
	 */
	public void removeKnowledge(Node p_node, Keyword[] p_keywords)
		throws Exception
	{
		if (p_node == null)
		{
			throw new Exception(
				o_node_ID + " removeKnowledge() p_node argument is null");
		}

		if (p_keywords == null)
		{
			throw new Exception(
				o_node_ID + " removeKnowledge() p_keywords argument is null");
		}

		// looks like we need to be careful here so that the same node does not appear more than
		// once in the Vector associated with each keyword

		Vector x_vector = null;
		for (int i = 0; i < p_keywords.length; i++)
		{
			removeKnowledge(p_node, p_keywords[i]);
		}

	}

	/**
	 * Remove knowledge about another node, specifically which keyword it has
	 * failed to respond correctly to
	 *
	 * @param p_node            the node about which we are receiving data
	 * @param p_keyword         the keyword it provided an incorrect response to
	 * @throws Exception        a general exception
	 */
	public void removeKnowledge(Node p_node, Keyword p_keyword)
		throws Exception
	{
		if (p_node == null)
		{
			throw new Exception(
				o_node_ID + " removeKnowledge() p_node argument is null");
		}
		if (p_keyword == null)
		{
			throw new Exception(
				o_node_ID + " removeKnowledge() p_keyword argument is null");
		}
		Vector x_vector = (Vector) (o_knowledge.get(p_keyword));
		if (x_vector != null && (x_vector.contains(p_node)) == true)
		{
			o_knowledge.remove(p_keyword, p_node);
			if (o_cat.isDebugEnabled())
			{
				o_cat.debug(
					p_node.getNodeID()
						+ " node association with "
						+ p_keyword
						+ " removed");
			}

			//if(o_cat.isDebugEnabled()) o_cat.debug(((Vector)o_knowledge.get(p_keyword)).getNodeID().first +" retrieved");
		}
	}

	/**
	 * Get a recommendation about which nodes should we ask about a keyword
	 *
	 * @param p_keyword             the keyword we are searching against
	 *
	 * @return Vector               the nodes we recommend searching
	 * @throws Exception        a general exception
	 */
	
	//推荐关键词
	public Vector getRecommendation(Keyword p_keyword) throws Exception
	{
		if (p_keyword == null)
		{
			throw new Exception(
				o_node_ID + " getRecommendation() p_keyword argument is null");
		}

		return (Vector) (o_knowledge.get(p_keyword));
	}

	private static NeuroGridComparator o_comparator = new NeuroGridComparator();

	/**
	 * Get a sorted recommendation about which nodes we should ask about 
	 * a keyword query
	 *
	 * @param p_keywords            the keywords we are searching against
	 *
	 * @return TreeSet               the nodes we recommend searching
	 * @throws Exception        a general exception
	 */
	
	//排好序的推荐列表
	public TreeSet getSortedRecommendation(Keyword[] p_keywords)
		throws Exception
	{
		if (p_keywords == null)
		{
			throw new Exception(
				o_node_ID + " getRecommendation() p_keywords argument is null");
		}

		int x_pick;
		double x_random;
		
		//推荐列表20
		final int x_recommendations_map_start_size = 20;
		HashMap x_recommendations =
			new HashMap(x_recommendations_map_start_size);
		NeuroGridRating x_rating = null;

		Vector x_temp = null;
		Node x_node = null;
		for (int i = 0;
			i < p_keywords.length;
			i++) // loop through the keywords from the query
		{
			if (o_cat.isDebugEnabled())
			{
				o_cat.debug("Sorting recommendations for " + p_keywords[i]);
			}

			x_temp = getRecommendation(p_keywords[i]);
			// get nodes that we know are related to keyword

			if (x_temp != null)
			{
				if (o_cat.isDebugEnabled())
				{
					o_cat.debug(
						x_temp.size() + " nodes related to " + p_keywords[i]);
				}

				for (int j = 0; j < x_temp.size(); j++)
				{
					x_node = (Node) (x_temp.elementAt(j));
					// if(x_node == p_message.getPreviousLocation())  // avoid querying previous node
					//   continue;
					x_rating =
						(NeuroGridRating) (x_recommendations.get(x_node));
					if (x_rating != null)
					{
						x_recommendations.put(
							x_node,
							x_rating.add(p_keywords[i].getRarity()));
					} // could modify so that keyword id is added (for zipf)
					else //  (* )
						{
						x_recommendations.put(
							x_node,
							new NeuroGridRating(
								x_node,
								p_keywords[i].getRarity()));
					}
					if (o_cat.isDebugEnabled())
					{
						o_cat.debug("Recommended node - " + x_node.getNodeID());
					}
				}
			}
		}

		// could remove node we recieved from recommendation list - now doing that above ....
		int x_suggested_nodes = x_recommendations.size();
		if (o_cat.isDebugEnabled())
		{
			o_cat.debug(
				"x_recommendations.size(): " + x_recommendations.size());
		}

		//x_recommendations.remove(p_message.getPreviousLocation());

		TreeSet x_tree_set = null;
		Iterator x_iterator = null;

		x_tree_set = new TreeSet(o_comparator);
		Iterator x_iter = x_recommendations.keySet().iterator();

		while (x_iter.hasNext())
		{
			x_node = (Node) (x_iter.next());
			x_rating = (NeuroGridRating) (x_recommendations.get(x_node));
			o_cat.debug(x_rating.getClass());
			x_tree_set.add(x_rating);
		}

		return x_tree_set;
	}

	/**
	 * check if the inbox contains this message
	 *
	 * @param p_message         the message we are checking for
	 * @return String           the message ID
	 * @throws Exception        a general exception
	 */
	public String inboxContainsMessage(Message p_message) throws Exception
	{
		Message x_message = (Message) (o_in_box.get(p_message.getMessageID()));

		if (x_message == null)
		{
			return null;
		}

		return x_message.getClass().getName();
	}

	/**
	 * remove a message from the inbox
	 *
	 * @param p_message         the message to remove 
	 * @return Object           the message we have removed
	 * @throws Exception        a general exception
	 */
	public Object removeFromInbox(Message p_message) throws Exception
	{
		return o_in_box.remove(p_message.getMessageID());
	}

	/**
	 * Add message to inbox, making sure that the message knows where it now is
	 *
	 * @param p_message         the message to add
	 * @return boolean          no meaning in this context ????
	 * @throws Exception        a general exception
	 */
	public boolean addMessageToInbox(Message p_message) throws Exception
	{
		if (p_message == null)
		{
			throw new Exception("addMessageToInbox() argument is null");
		}
		//if(o_graphics_simulator != null) doGraphics(); 	// will need to add this back at some point
		p_message.setLocation(this);
		if (o_cat.isDebugEnabled())
		{
			o_cat.debug("message added to inbox of node " + this.getNodeID());
		}
		o_in_box.put(p_message.getMessageID(), p_message);

		return false;
	}

	/**
	 * do graphics
	 */
	public void doGraphics()
	{

		if (o_cat.isDebugEnabled())
		{
			o_cat.debug("doGraphics");
		}

		//try{Thread.sleep((int)((double)Network.o_sleep_duration/2D));}catch(Exception e){}	
		try
		{
			Thread.sleep(Network.o_sleep_duration);
		}
		catch (Exception e)
		{
			o_cat.debug(e);
		}
	}

	/**
	 * do graphics
	 */
	public void doGraphics2()
	{
		if (o_cat.isDebugEnabled())
		{
			o_cat.debug("doGraphics");
		}

		//try{Thread.sleep((int)((double)Network.o_sleep_duration/2D));}catch(Exception e){}	
		try
		{
			Thread.sleep(Network.o_sleep_duration);
		}
		catch (Exception e)
		{
			o_cat.debug(e);
		}
	}
	/**
	 * check if this message has been seen by this node
	 *
	 * @param p_message the message we are checking
	 * @return String   the message id of the seen message
	 * 
	 * @throws Exception a general exception
	 */
	public String checkSeen(Message p_message) throws Exception
	{
		return (String) (o_seenGUIDs.get(p_message.getMessageID()));
	}

	/**
	 * check how many messages seen so far
	 *
	 * @return int the number of seen messages
	 * 
	 * @throws Exception a general exception
	 */
	public int getNoSeenMessages() throws Exception
	{
		return o_seenGUIDs.size();
	}

	/**
	 * update this node to indicate it has seen this message 
	 *
	 * @param p_message  the message to be added to seen
	 * 
	 * @throws Exception a general exception
	 */
	public void addToSeen(Message p_message) throws Exception
	{
		o_seenGUIDs.put(p_message.getMessageID(), p_message.getMessageID());
	}

	/**
	 * handle message through the handler
	 *
	 * @param p_message  the message to handle
	 * @return boolean   ???
	 * 
	 * @throws Exception a general exception
	 */
	public boolean handleMessage(Message p_message) throws Exception
	{
		return o_message_handler.handleMessage(p_message, this);
	}

	/**
	 * inject message through the handler
	 *
	 * @param p_message  the message to inject
	 * @return boolean   ???
	 * 
	 * @throws Exception a general exception
	 */
	public boolean injectMessage(Message p_message) throws Exception
	{
		return o_message_handler.injectMessage(p_message, this);
	}

	/**
	 * turn a Hashtable into an independent vector
	 * 
	 * @param p_hashtable incoming hashtable
	 * @return a vector of keys in the hashtable
	 * @throws Exception a general exception
	 */
	public static Vector getVectorFromHashtable(Hashtable p_hashtable)
		throws Exception
	{
		if (p_hashtable == null)
		{
			throw new Exception("getVectorFromHashtable() argument is null");
		}
		Iterator x_iter = p_hashtable.keySet().iterator();
		Vector x_vector = new Vector(p_hashtable.size());
		while (x_iter.hasNext())
		{
			x_vector.addElement(x_iter.next());
		}
		return x_vector;
	}

	/**
	 * turn a map into a vector
	 * 
	 * @param p_map  the map we want to transform
	 * @return       an independent vector of the map keys
	 * @throws Exception a general exception
	 */
	public static Vector getVectorFromMap(Map p_map) throws Exception
	{
		if (p_map == null)
		{
			throw new Exception("getVectorFromMap() argument is null");
		}
		Iterator x_iter = p_map.keySet().iterator();
		Vector x_vector = new Vector(p_map.size());
		while (x_iter.hasNext())
		{
			x_vector.addElement(x_iter.next());
		}
		return x_vector;
	}

	/**
	 * turn a MultiHashtable into an independent vector
	 * 
	 * @param p_hashtable incoming multihashtable
	 * @return a vector of keys in the multihashtable
	 * @throws Exception a general exception
	 */
	public static Vector getVectorFromMultiHashtable(MultiHashtable p_hashtable)
		throws Exception
	{
		if (p_hashtable == null)
		{
			throw new Exception("getVectorFromMultiHashtable() argument is null");
		}
		Iterator x_iter = p_hashtable.keySet().iterator();
		Vector x_vector = new Vector(p_hashtable.size());
		while (x_iter.hasNext())
		{
			x_vector.addElement(x_iter.next());
		}
		return x_vector;
	}
}
