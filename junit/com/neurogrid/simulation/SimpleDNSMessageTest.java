package com.neurogrid.simulation;

import java.util.Random;
import java.util.Vector;

import junit.framework.TestCase;

import com.neurogrid.simulation.root.Message;
import com.neurogrid.simulation.root.Node;
import com.neurogrid.util.IPAddress;



public class SimpleDNSMessageTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
  // JUnitDoclet begin class
  com.neurogrid.simulation.SimpleDNSMessage dnsmessage = null;
  public static final int START_TTL = 4;
  // JUnitDoclet end class
  
  public SimpleDNSMessageTest(String name) {
    // JUnitDoclet begin method SimpleDNSMessageTest
    super(name);
    // JUnitDoclet end method SimpleDNSMessageTest
  }
  
  public com.neurogrid.simulation.SimpleDNSMessage createInstance() throws Exception {
    // JUnitDoclet begin method testcase.createInstance
    
    
    Node x_node = new GnutellaNode(new GnutellaNetwork(),new Random(888));
    return new com.neurogrid.simulation.SimpleDNSMessage(START_TTL,
                                                         "localhost",
                                                         new IPAddress("127.0.0.1"),
                                                         x_node);
    // JUnitDoclet end method testcase.createInstance
  }
  
  protected void setUp() throws Exception {
    // JUnitDoclet begin method testcase.setUp
    super.setUp();
    dnsmessage = createInstance();
    // JUnitDoclet end method testcase.setUp
  }
  
  protected void tearDown() throws Exception {
    // JUnitDoclet begin method testcase.tearDown
    dnsmessage = null;
    super.tearDown();
    // JUnitDoclet end method testcase.tearDown
  }
  
  public void testGetCvsInfo() throws Exception {
    // JUnitDoclet begin method getCvsInfo
    // JUnitDoclet end method getCvsInfo
  }
  
  public void testDecrementTTL() throws Exception {
    // JUnitDoclet begin method toString

    // quite possibly this should be a DNSNode and DNSNetwork ...
    // would imagine we need a DNSMessageHandler ...
    Node x_node = new GnutellaNode(new GnutellaNetwork(),new Random(888));
    SimpleDNSMessage x_message = new SimpleDNSMessage(START_TTL-1,
                                                      "localhost",
                                                      new IPAddress("127.0.0.1"),
                                                      x_node);
    
    assertTrue("message TTL not correct",dnsmessage.getTTL()==START_TTL);
    Vector x_messages = (Vector)(Message.o_messages_by_TTL.remove(Integer.toString(START_TTL)));
    assertTrue("message not in correct place in TTL table",x_messages.contains(dnsmessage));
    dnsmessage.decrementTTL();
    assertTrue("message TTL not correct",dnsmessage.getTTL()==START_TTL-1);
    x_messages = (Vector)(Message.o_messages_by_TTL.remove(Integer.toString(START_TTL-1)));
    assertTrue("message not in correct place in TTL table",x_messages.contains(dnsmessage));
    
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
    junit.textui.TestRunner.run(SimpleDNSMessageTest.class);
    // JUnitDoclet end method testcase.main
  }
}
