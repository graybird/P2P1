/*
 * Created on 2003/06/25
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.neurogrid.simulation.root;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:sam@neurogrid.com">Sam Joseph</a>
 *
 * test the base node class
 */
public class NodeTest extends TestCase
{

	private static Category o_cat =
		Category.getInstance(NodeTest.class.getName());

	/**
	 * initialize the logging system
	 *
	 * @param p_conf      configuration filename
	 */
	public static void init(String p_conf)
	{
		BasicConfigurator.configure();
		PropertyConfigurator.configure(p_conf);
		o_cat.info("NodeTest logging Initialized");
	}

	private Node o_node = null;

	/**
	 * Constructor for NodeTest.
	 * @param arg0 incoming arguments
	 */
	public NodeTest(String arg0)
	{
		super(arg0);
	}

	/**
	 * @return Node - a Node instance
	 * @throws Exception a general exception
	 */
	public static Node createInstance() throws Exception
	{
		return new Node()
		{
			/**
			 * Get String representation 
			 *
			 * @return String
			 */
			public String toString()
			{
				return o_node_ID;
			}
		};
	}

	/**
	 * @param args incoming args
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(NodeTest.class);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testGetNetwork()
	{
		assertTrue("Starting network is not null", o_node.getNetwork() == null);
	}

	public void testIndicateSendingMessage()
	{
		Node x_node = null;
		try
		{
			x_node = createInstance();
		}
		catch (Exception e)
		{
			o_cat.debug(e);
		}

		o_node.indicateSendingMessage(x_node);
		assertTrue("not the single expected message",o_node.getNoSendingMessage() == 1);
	}

	public void testGetNoSendingMessage()
	{
		//TODO Implement getNoSendingMessage().
	}

	public void testSetInactive()
	{
		//TODO Implement setInactive().
	}

	public void testSetActive()
	{
		//TODO Implement setActive().
	}

	public void testGetActive()
	{
		//TODO Implement getActive().
	}

	public void testGetNodeID()
	{
		//TODO Implement getNodeID().
	}

	public void testGetNoConnections()
	{
		//TODO Implement getNoConnections().
	}

	public void testGetNoContents()
	{
		//TODO Implement getNoContents().
	}

	public void testGetNoKnowledge()
	{
		//TODO Implement getNoKnowledge().
	}

	public void testGetTotalNoKnowledge()
	{
		//TODO Implement getTotalNoKnowledge().
	}

	public void testCheckInbox()
	{
		//TODO Implement checkInbox().
	}

	public void testRefresh()
	{
		//TODO Implement refresh().
	}

	public void testClear()
	{
		//TODO Implement clear().
	}

	public void testClearSendingMessage()
	{
		//TODO Implement clearSendingMessage().
	}

	public void testClearInbox()
	{
		//TODO Implement clearInbox().
	}

	public void testClearGUIDs()
	{
		//TODO Implement clearGUIDs().
	}

	public void testClearConnList()
	{
		//TODO Implement clearConnList().
	}

	public void testClearContents()
	{
		//TODO Implement clearContents().
	}

	public void testClearKnowledge()
	{
		//TODO Implement clearKnowledge().
	}

	/*
	 * Test for String toString()
	 */
	public void testToString()
	{
		//TODO Implement toString().
	}

	public void testGetConnList()
	{
		//TODO Implement getConnList().
	}

	public void testGetKeywords()
	{
		//TODO Implement getKeywords().
	}

	public void testAllKeywords()
	{
		//TODO Implement allKeywords().
	}

	public void testAddConnection()
	{
		//TODO Implement addConnection().
	}

	/*
	 * Test for void removeConnection(Node)
	 */
	public void testRemoveConnectionNode()
	{
		//TODO Implement removeConnection().
	}

	/*
	 * Test for void removeConnection(Node, Random)
	 */
	public void testRemoveConnectionNodeRandom()
	{
		//TODO Implement removeConnection().
	}

	public void testHasConnection()
	{
		//TODO Implement hasConnection().
	}

	public void testAddContent()
	{
		//TODO Implement addContent().
	}

	public void testHasContent()
	{
		//TODO Implement hasContent().
	}

	public void testHasKeyword()
	{
		//TODO Implement hasKeyword().
	}

	public void testMatchingDocuments()
	{
		//TODO Implement matchingDocuments().
	}

	public void testMatchingKeywords()
	{
		//TODO Implement matchingKeywords().
	}

	public void testGetContent()
	{
		//TODO Implement getContent().
	}

	public void testGetContentsByDocID()
	{
		//TODO Implement getContentsByDocID().
	}

	/*
	 * Test for Document getRandomContent()
	 */
	public void testGetRandomContent()
	{
		//TODO Implement getRandomContent().
	}

	/*
	 * Test for Document getRandomContent(Hashtable)
	 */
	public void testGetRandomContentHashtable()
	{
		//TODO Implement getRandomContent().
	}

	public void testGetRandomKeyword()
	{
		//TODO Implement getRandomKeyword().
	}

	/*
	 * Test for void addKnowledge(Node, Keyword[])
	 */
	public void testAddKnowledgeNodeKeywordArray()
	{
		//TODO Implement addKnowledge().
	}

	/*
	 * Test for void addKnowledge(Node, Keyword)
	 */
	public void testAddKnowledgeNodeKeyword()
	{
		//TODO Implement addKnowledge().
	}

	/*
	 * Test for void removeKnowledge(Node, Keyword[])
	 */
	public void testRemoveKnowledgeNodeKeywordArray()
	{
		//TODO Implement removeKnowledge().
	}

	/*
	 * Test for void removeKnowledge(Node, Keyword)
	 */
	public void testRemoveKnowledgeNodeKeyword()
	{
		//TODO Implement removeKnowledge().
	}

	public void testGetRecommendation()
	{
		//TODO Implement getRecommendation().
	}

	public void testGetSortedRecommendation()
	{
		//TODO Implement getSortedRecommendation().
	}

	public void testInboxContainsMessage()
	{
		//TODO Implement inboxContainsMessage().
	}

	public void testRemoveFromInbox()
	{
		//TODO Implement removeFromInbox().
	}

	public void testAddMessageToInbox()
	{
		//TODO Implement addMessageToInbox().
	}

	public void testDoGraphics()
	{
		//TODO Implement doGraphics().
	}

	public void testDoGraphics2()
	{
		//TODO Implement doGraphics2().
	}

	public void testCheckSeen()
	{
		//TODO Implement checkSeen().
	}

	public void testGetNoSeenMessages()
	{
		//TODO Implement getNoSeenMessages().
	}

	public void testAddToSeen()
	{
		//TODO Implement addToSeen().
	}

	public void testHandleMessage()
	{
		//TODO Implement handleMessage().
	}

	public void testInjectMessage()
	{
		//TODO Implement injectMessage().
	}

	public void testGetVectorFromHashtable()
	{
		//TODO Implement getVectorFromHashtable().
	}

	public void testGetVectorFromMap()
	{
		//TODO Implement getVectorFromMap().
	}

	public void testGetVectorFromMultiHashtable()
	{
		//TODO Implement getVectorFromMultiHashtable().
	}

}
