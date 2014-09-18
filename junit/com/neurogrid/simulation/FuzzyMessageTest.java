package com.neurogrid.simulation;

import junit.framework.TestCase;
// JUnitDoclet begin import
import com.neurogrid.simulation.FuzzyMessage;
import com.neurogrid.simulation.root.Message;
import com.neurogrid.simulation.root.Keyword;
import com.neurogrid.simulation.root.Document;
import com.neurogrid.simulation.root.Node;
import java.util.Vector;
import java.util.Random;
// JUnitDoclet end import

/**
* Generated by JUnitDoclet, a tool provided by
* ObjectFab GmbH under LGPL.
* Please see www.junitdoclet.org, www.gnu.org
* and www.objectfab.de for informations about
* the tool, the licence and the authors.
*/


public class FuzzyMessageTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  com.neurogrid.simulation.FuzzyMessage fuzzymessage = null;
  public static final int START_TTL = 4;
  // JUnitDoclet end class
  
  public FuzzyMessageTest(String name) {
    // JUnitDoclet begin method FuzzyMessageTest
    super(name);
    // JUnitDoclet end method FuzzyMessageTest
  }
  
  public com.neurogrid.simulation.FuzzyMessage createInstance() throws Exception {
    // JUnitDoclet begin method testcase.createInstance
    
    
    Keyword[] x_keywords = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
    }	
     
    Document x_doc = new SimpleDocument(x_keywords);
    Node x_node = new GnutellaNode(new GnutellaNetwork(),new Random(888));
    return new com.neurogrid.simulation.FuzzyMessage(START_TTL,x_doc.getKeywords(),x_node);
    // JUnitDoclet end method testcase.createInstance
  }
  
  protected void setUp() throws Exception {
    // JUnitDoclet begin method testcase.setUp
    super.setUp();
    fuzzymessage = createInstance();
    // JUnitDoclet end method testcase.setUp
  }
  
  protected void tearDown() throws Exception {
    // JUnitDoclet begin method testcase.tearDown
    fuzzymessage = null;
    super.tearDown();
    // JUnitDoclet end method testcase.tearDown
  }
  
  public void testGetCvsInfo() throws Exception {
    // JUnitDoclet begin method getCvsInfo
    // JUnitDoclet end method getCvsInfo
  }
  
  public void testDecrementTTL() throws Exception {
    // JUnitDoclet begin method toString

    Keyword[] x_keywords = new SimpleKeyword[3];
    
    for(int i=0;i<3;i++)
    {
      x_keywords[i] = new SimpleKeyword();	
    }	
     
    Document x_doc = new SimpleDocument(x_keywords);
    Node x_node = new GnutellaNode(new GnutellaNetwork(),new Random(888));
    FuzzyMessage x_message = new FuzzyMessage(START_TTL-1,x_doc.getKeywords(),x_node);
    
    assertTrue("message TTL not correct",fuzzymessage.getTTL()==START_TTL);
    Vector x_messages = (Vector)(Message.o_messages_by_TTL.remove(Integer.toString(START_TTL)));
    assertTrue("message not in correct place in TTL table",x_messages.contains(fuzzymessage));
    fuzzymessage.decrementTTL();
    assertTrue("message TTL not correct",fuzzymessage.getTTL()==START_TTL-1);
    x_messages = (Vector)(Message.o_messages_by_TTL.remove(Integer.toString(START_TTL-1)));
    assertTrue("message not in correct place in TTL table",x_messages.contains(fuzzymessage));
    
    assertTrue("incorrect number of messages  in TTL table: "+x_messages.size(),x_messages.size() == 2);
   
    // JUnitDoclet end method toString
  }
  
  public void testToString() throws Exception {
    // JUnitDoclet begin method toString
    // JUnitDoclet end method toString
  }
  
  public void testMain() throws Exception {
    // JUnitDoclet begin method main
    // JUnitDoclet end method main
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
    junit.textui.TestRunner.run(FuzzyMessageTest.class);
    // JUnitDoclet end method testcase.main
  }
}