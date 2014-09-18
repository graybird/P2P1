package com.neurogrid.simulation;

import junit.framework.TestCase;
// JUnitDoclet begin import
import com.neurogrid.simulation.GnutellaNode;
import com.neurogrid.simulation.SimpleDocument;
import com.neurogrid.simulation.SimpleContentMessage;
import com.neurogrid.simulation.SimpleKeyword;
import com.neurogrid.simulation.root.ContentMessage;
import com.neurogrid.simulation.root.Node;
import com.neurogrid.simulation.root.Keyword;
import com.neurogrid.simulation.root.Document;
import junit.framework.TestSuite;
import java.util.Set;
import java.util.Random;
// JUnitDoclet end import

/**
* Generated by JUnitDoclet, a tool provided by
* ObjectFab GmbH under LGPL.
* Please see www.junitdoclet.org, www.gnu.org
* and www.objectfab.de for informations about
* the tool, the licence and the authors.
*/


public class GnutellaNodeTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  com.neurogrid.simulation.GnutellaNode gnutellanode = null;
  com.neurogrid.simulation.GnutellaNetwork gnutellanetwork = null;
  // JUnitDoclet end class
  
  public GnutellaNodeTest(String name) {
    // JUnitDoclet begin method GnutellaNodeTest
    super(name);
    GnutellaNode.init(System.getProperty("Log4jConfig"));
    // JUnitDoclet end method GnutellaNodeTest
  }
  
  public com.neurogrid.simulation.GnutellaNode createInstance() throws Exception {
    // JUnitDoclet begin method testcase.createInstance
    gnutellanetwork = new GnutellaNetwork();
    return new com.neurogrid.simulation.GnutellaNode(gnutellanetwork,new Random(888));
    // JUnitDoclet end method testcase.createInstance
  }
  
  protected void setUp() throws Exception {
    // JUnitDoclet begin method testcase.setUp
    super.setUp();
    gnutellanode = createInstance();
    // JUnitDoclet end method testcase.setUp
  }
  
  protected void tearDown() throws Exception {
    // JUnitDoclet begin method testcase.tearDown
    gnutellanode = null;
    super.tearDown();
    // JUnitDoclet end method testcase.tearDown
  }
  
  public void testGetCvsInfo() throws Exception {
    // JUnitDoclet begin method getCvsInfo
    // JUnitDoclet end method getCvsInfo
  }
  
  public void testInit() throws Exception {
    // JUnitDoclet begin method init
    // JUnitDoclet end method init
  }
  
  public void testGetNewNodeID() throws Exception {
    // JUnitDoclet begin method getNewNodeID
    // JUnitDoclet end method getNewNodeID
  }
  
  public void testToString() throws Exception {
    // JUnitDoclet begin method toString
    // JUnitDoclet end method toString
  }
  
  public void testAddMessageToInbox() throws Exception {
    // JUnitDoclet begin method addMessageToInbox
    
    Keyword[] x_keywords = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
    }	
     
    Document x_doc = new SimpleDocument(x_keywords);
    Node x_node = new GnutellaNode(gnutellanetwork, new Random(888));
    ContentMessage x_message = new SimpleContentMessage(3,x_doc.getKeywords(),x_doc,x_node);
    gnutellanode.addMessageToInbox(x_message);
    assertTrue("message location not updated",x_message.getLocation() == gnutellanode);
    assertTrue("message of correct type not in inbox",
               gnutellanode.inboxContainsMessage(x_message).equals("com.neurogrid.simulation.SimpleContentMessage"));
    // JUnitDoclet end method addMessageToInbox
  }
  
  public void testDoGraphics() throws Exception {
    // JUnitDoclet begin method doGraphics
    // JUnitDoclet end method doGraphics
  }
  
  public void testDoGraphics2() throws Exception {
    // JUnitDoclet begin method doGraphics2
    // JUnitDoclet end method doGraphics2
  }
  
  public void testProcessMessage() throws Exception {
    // JUnitDoclet begin method processMessage
    // JUnitDoclet end method processMessage
  }
  
  public void testSetInactive() throws Exception {
    // JUnitDoclet begin method setInactive
    assertTrue("node already active",!gnutellanode.getActive());
    
    gnutellanode.setActive();
    
    assertTrue("node not active",gnutellanode.getActive());
    assertTrue("node not added to active set",gnutellanetwork.activeNodeContains(gnutellanode));
    
    gnutellanode.setInactive();
    
    assertTrue("node not inactive",!gnutellanode.getActive());
    assertTrue("node still in active set",!gnutellanetwork.activeNodeContains(gnutellanode));
    // JUnitDoclet end method setInactive
  }
  
  public void testSetActive() throws Exception {
    // JUnitDoclet begin method setActive
    assertTrue("node already active",!gnutellanode.getActive());
    
    gnutellanode.setActive();
    
    assertTrue("node not active",gnutellanode.getActive());
    assertTrue("node not added to active set",gnutellanetwork.activeNodeContains(gnutellanode));
    // JUnitDoclet end method setActive
  }
  
  public void testGetActive() throws Exception {
    // JUnitDoclet begin method getActive
    // JUnitDoclet end method getActive
  }
  
  public void testGetNodeID() throws Exception {
    // JUnitDoclet begin method getNodeID
    // JUnitDoclet end method getNodeID
  }
  
  public void testGetNoConnections() throws Exception {
    // JUnitDoclet begin method getNoConnections
    // JUnitDoclet end method getNoConnections
  }
  
  public void testGetNoContents() throws Exception {
    // JUnitDoclet begin method getNoContents
    // JUnitDoclet end method getNoContents
  }
  
  public void testGetNoKnowledge() throws Exception {
    // JUnitDoclet begin method getNoKnowledge
    // JUnitDoclet end method getNoKnowledge
  }
  
  public void testGetTotalNoKnowledge() throws Exception {
    // JUnitDoclet begin method getTotalNoKnowledge
    // JUnitDoclet end method getTotalNoKnowledge
  }
  
  public void testCheckInbox() throws Exception {
    // JUnitDoclet begin method checkInbox
    // JUnitDoclet end method checkInbox
  }
  
  public void testClearInbox() throws Exception {
    // JUnitDoclet begin method clearInbox
    Keyword[] x_keywords = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
    }	
     
    Document x_doc = new SimpleDocument(x_keywords);
    Node x_node = new GnutellaNode(gnutellanetwork,new Random(888));
    ContentMessage x_message = new SimpleContentMessage(3,x_doc.getKeywords(),x_doc,x_node);
    gnutellanode.addMessageToInbox(x_message);
    assertTrue("message location not updated",x_message.getLocation() == gnutellanode);
    assertTrue("message of correct type not in inbox",
                gnutellanode.inboxContainsMessage(x_message).equals("com.neurogrid.simulation.SimpleContentMessage"));

    assertTrue("checkInbox indicates inbox empty",gnutellanode.checkInbox());
    
    gnutellanode.clearInbox();
    
    assertTrue("checkInbox indicated inbox full",!gnutellanode.checkInbox());
    
    // JUnitDoclet end method clearInbox
  }
  
    
  public void testCheckSeen() throws Exception {
    // JUnitDoclet begin method checkInbox
    Keyword[] x_keywords = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
    }	
     
    Document x_doc = new SimpleDocument(x_keywords);
    Node x_node = new GnutellaNode(gnutellanetwork,new Random(888));
    ContentMessage x_message = new SimpleContentMessage(3,x_doc.getKeywords(),x_doc,x_node);
    ContentMessage x_message2 = new SimpleContentMessage(3,x_doc.getKeywords(),x_doc,x_node);
    gnutellanode.addToSeen(x_message); 
    assertTrue("seen message not indicated as such",gnutellanode.checkSeen(x_message)!=null);
    assertTrue("unseen messageindicated as seen",gnutellanode.checkSeen(x_message2)==null);
    
    
    // JUnitDoclet end method checkInbox
  }
  
  public void testClearGUIDs() throws Exception {
    // JUnitDoclet begin method clearGUIDs
    
    Keyword[] x_keywords = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
    }	
     
    Document x_doc = new SimpleDocument(x_keywords);
    Node x_node = new GnutellaNode(gnutellanetwork,new Random(888));
    ContentMessage x_message = new SimpleContentMessage(3,x_doc.getKeywords(),x_doc,x_node);
    gnutellanode.addToSeen(x_message);    
    assertTrue("seen message not indicated as such",gnutellanode.checkSeen(x_message)!=null);
    gnutellanode.clearGUIDs();    
    assertTrue("message indicated as seen after cleared",gnutellanode.checkSeen(x_message)==null);
    assertTrue("not all messages cleared",gnutellanode.getNoSeenMessages() == 0);
    
    // JUnitDoclet end method clearGUIDs
  }
  
  public void testClearConnList() throws Exception {
    // JUnitDoclet begin method clearConnList
    
    GnutellaNode x_node = new GnutellaNode(gnutellanetwork,new Random(888));
    
    gnutellanode.addConnection(x_node);
    
    assertTrue("failed to add connection",gnutellanode.hasConnection(x_node));
    
    gnutellanode.clearConnList();
    
    assertTrue("clear failed to remove connection",!gnutellanode.hasConnection(x_node));
    assertTrue("no. conns not zero",gnutellanode.getNoConnections() == 0);
    
    
    // JUnitDoclet end method clearConnList
  }
  
  public void testClearContents() throws Exception {
    // JUnitDoclet begin method clearContents
    
    SimpleDocument x_doc = new SimpleDocument(3);
    
    gnutellanode.addContent(x_doc);
    
    assertTrue("content not added",gnutellanode.hasContent(x_doc));    
    
    gnutellanode.clearContents();
    
    assertTrue("clear failed to remove contents",!gnutellanode.hasContent(x_doc));
    assertTrue("no. content not zero",gnutellanode.getNoContents() == 0);
    
    // JUnitDoclet end method clearContents
  }

  public void testGetConnList() throws Exception {
    // JUnitDoclet begin method getConnList
    // JUnitDoclet end method getConnList
  }
  
  public void testAllKeywords() throws Exception {
    // JUnitDoclet begin method allKeywords
    // JUnitDoclet end method allKeywords
  }
  
  public void testAddConnection() throws Exception {
    // JUnitDoclet begin method addConnection
    
    try
    {
      gnutellanode.addConnection(null);
      fail("Failed to throw exception when adding null connection");	
    }
    catch(Exception e){}

    GnutellaNode x_node = new GnutellaNode(gnutellanetwork,new Random(888));
    
    gnutellanode.addConnection(x_node);
    
    assertTrue(gnutellanode.hasConnection(x_node));
    
    
    // JUnitDoclet end method addConnection
  }
  
  public void testHasConnection() throws Exception {
    // JUnitDoclet begin method hasConnection
    try
    {
      gnutellanode.hasConnection(null);
      fail("Failed to throw exception when checking for null connection");	
    }
    catch(Exception e){}
        
    GnutellaNode x_node = new GnutellaNode(gnutellanetwork,new Random(888));
    GnutellaNode x_node2 = new GnutellaNode(gnutellanetwork,new Random(888));
    
    gnutellanode.addConnection(x_node);
    
    assertTrue(gnutellanode.hasConnection(x_node));
    assertTrue(!gnutellanode.hasConnection(x_node2));
    
    // JUnitDoclet end method hasConnection
  }
  
  public void testAddContent() throws Exception {
    // JUnitDoclet begin method addContent
    
    try
    {
      gnutellanode.addContent(null);
      fail("Failed to throw exception when adding null content");	
    }
    catch(Exception e){}

    SimpleDocument x_doc = new SimpleDocument(3);
    
    gnutellanode.addContent(x_doc);
    
    assertTrue(gnutellanode.hasContent(x_doc));    
    // JUnitDoclet end method addContent
  }
  
  public void testHasContent() throws Exception {
    // JUnitDoclet begin method hasContent
    
    try
    {
      gnutellanode.hasContent(null);
      fail("Failed to throw exception when checking for null content");	
    }
    catch(Exception e){}

    SimpleDocument x_doc = new SimpleDocument(3);
    SimpleDocument x_doc2 = new SimpleDocument(3);
    
    gnutellanode.addContent(x_doc);
    
    assertTrue(gnutellanode.hasContent(x_doc));    
    assertTrue(!gnutellanode.hasContent(x_doc2));    
    // JUnitDoclet end method hasContent
  }
  
  public void testHasKeyword() throws Exception {
    // JUnitDoclet begin method hasContent
    
    try
    {
      gnutellanode.hasKeyword(null);
      fail("Failed to throw exception when checking for null content");	
    }
    catch(Exception e){}

    Keyword[] x_keywords = new SimpleKeyword[3];
    Keyword[] x_keywords2 = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
      x_keywords2[i] = new SimpleKeyword();	
    }	

    SimpleDocument x_doc = new SimpleDocument(x_keywords);
    SimpleDocument x_doc2 = new SimpleDocument(x_keywords2);
    
    gnutellanode.addContent(x_doc);

    for(int i=0;i<3;i++)
    {    
      assertTrue("local keyword not indicated",gnutellanode.hasKeyword(x_keywords[i]) == 1);    
      assertTrue("absent keyword identified as local",gnutellanode.hasKeyword(x_keywords2[i]) == 0); 
    }   
    // JUnitDoclet end method hasContent
  }
  
  public void testMatchingDocuments() throws Exception {
    // JUnitDoclet begin method matchingDocuments

    try
    {
      gnutellanode.matchingDocuments(null);
      fail("Failed to throw exception when checking for null content");	
    }
    catch(Exception e){}

    Keyword[] x_keywords = new SimpleKeyword[3];
    Keyword[] x_keywords2 = new SimpleKeyword[3];
    Keyword[] x_keywords3 = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
      x_keywords2[i] = new SimpleKeyword();	
      x_keywords3[i] = new SimpleKeyword();	
    }	

    SimpleDocument x_doc = new SimpleDocument(x_keywords);
    SimpleDocument x_doc2 = new SimpleDocument(x_keywords);
    SimpleDocument x_doc3 = new SimpleDocument(x_keywords2);
    
    gnutellanode.addContent(x_doc);
    gnutellanode.addContent(x_doc2);
    gnutellanode.addContent(x_doc3);

    Set x_set = gnutellanode.matchingDocuments(x_keywords);
    
    assertTrue("not all matching documents found",x_set.size()==2);
    assertTrue("x_doc not found",x_set.contains(x_doc));
    assertTrue("xdoc2 not found",x_set.contains(x_doc2));
    assertTrue("xdoc3 found incorrectly",!x_set.contains(x_doc3));

    // added the following in order to expose NullPointerException error
    // in Node.java - i.e. the following operation
    //      x_docs = (Vector)(o_contents.get(p_keywords[i]));
    //      x_set.addAll(x_docs);
    x_set = gnutellanode.matchingDocuments(x_keywords3);
    assertTrue("match from unrelated keywords",x_set.size()==0);


    // JUnitDoclet end method matchingDocuments
  }
  
  public void testMatchingKeywords() throws Exception {
    // JUnitDoclet begin method matchingKeywords
    // JUnitDoclet end method matchingKeywords
  }
  
  public void testGetContent() throws Exception {
    // JUnitDoclet begin method getContent
    // JUnitDoclet end method getContent
  }
  
  public void testGetContentsByDocID() throws Exception {
    // JUnitDoclet begin method getContentsByDocID
    // JUnitDoclet end method getContentsByDocID
  }
  
  public void testGetRandomContent() throws Exception {
    // JUnitDoclet begin method getRandomContent
    // JUnitDoclet end method getRandomContent
  }
  
  public void testGetRandomKeyword() throws Exception {
    // JUnitDoclet begin method getRandomKeyword
    // JUnitDoclet end method getRandomKeyword
  }
  
  public void testAddKnowledge() throws Exception {
    // JUnitDoclet begin method addKnowledge
    // JUnitDoclet end method addKnowledge
  }
  
  public void testGetRecommendation() throws Exception {
    // JUnitDoclet begin method getRecommendation
    // JUnitDoclet end method getRecommendation
  }
  
  public void testHandleMessage() throws Exception {
 // JUnitDoclet begin method handleMessage
    SimpleKeyword[][] x_keywords = new SimpleKeyword[3][3];
    
    for(int i=0;i<3;i++)
    {
      for(int j=0;j<3;j++)
      {
        x_keywords[i][j] = new SimpleKeyword();	
      }
    }	
     
    SimpleDocument[] x_doc = new SimpleDocument[3];
    ContentMessage x_message[] = new SimpleContentMessage[3];
    
    Node x_node[] = new GnutellaNode[4];
    for(int i=0;i<4;i++)
    {
      x_node[i] = new GnutellaNode(gnutellanetwork,new Random(888));
    }
    
    
    gnutellanode.addConnection(x_node[1]);
    gnutellanode.addConnection(x_node[2]);
    gnutellanode.addConnection(x_node[3]);
    
    for(int k=0;k<3;k++)
    {
      x_doc[k] = new SimpleDocument(x_keywords[k]);	
      x_message[k] = new SimpleContentMessage(3,x_doc[k].getKeywords(),x_doc[k],x_node[3]);
      x_message[k].setPreviousLocation(x_node[3]);
    }
    
    gnutellanode.addContent(x_doc[0]);  
    
    assertTrue("failed to successfully handle message",
               gnutellanode.handleMessage(x_message[1]));

    assertTrue("node not activated",gnutellanode.getActive());

    assertTrue("Connected Node failed to receive message of correct type",
               x_node[1].inboxContainsMessage(x_message[1]).equals("com.neurogrid.simulation.SimpleContentMessage"));
    assertTrue("Connected Node failed to receive message of correct type",
               x_node[2].inboxContainsMessage(x_message[1]).equals("com.neurogrid.simulation.SimpleContentMessage"));
    assertTrue("Connected Node that sent message re-received message",
               x_node[3].inboxContainsMessage(x_message[1]) == null);
    assertTrue("initial node still has message",
               gnutellanode.inboxContainsMessage(x_message[1]) == null);


// JUnitDoclet end method handleMessage 
  }
  
  /**
  * JUnitDoclet moves marker to this method, if there is not match
  * for them in the regenerated code and if the marker is not empty.
  * This way, no test gets lost when regenerating after renaming.
  * Method testVault is supposed to be empty.
  */
  public void testVault() throws Exception {
    // JUnitDoclet begin method testcase.testVault

    // JUnitDoclet end method testcase.testVault
  }
  
  public static void main(String[] args) {
    // JUnitDoclet begin method testcase.main
    String x_method = System.getProperty("test.method");
    System.out.println("test.method="+x_method);
    if(x_method == null || x_method.equals(""))
    {
      System.out.println("testing all methods");
    
      junit.textui.TestRunner.run(GnutellaNodeTest.class);
    }
    else
    {
      System.out.println("testing single method");

      TestSuite suite = new TestSuite();
      suite.addTest(new GnutellaNodeTest(x_method));
      junit.textui.TestRunner.run(suite);
    }    // JUnitDoclet end method testcase.main
  }
}
