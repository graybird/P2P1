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


import java.util.Properties;

import com.neurogrid.log.Logger;


public class NetworkParameters
{
  public static final String cvsInfo = "$Id: NetworkParameters.java,v 1.6 2003/06/25 12:16:20 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }	
  
  public String o_sim_type = null;

  public int o_no_keywords;
  public int o_no_documents;
  public int o_no_keywords_per_document;
  public int o_no_nodes;
  public int o_no_honest_nodes;
  public int o_no_documents_per_node;
  public int o_max_knowledge_per_node;
  public int o_no_connections_per_node;
  public int o_min_connections_per_node;
  public int o_max_connections_per_node;
  public int o_no_search_keywords;
  
  public int o_start_TTL;
  public int o_max_forwarding_degree;
  public int o_min_forwarding_degree;
  public int o_internal_loop;
  public int o_probe_loop;
  public int o_no_probes;
  public int o_growth_loop;
  public int o_stats_loop;
  

  public boolean o_ring_topology;
  public boolean o_reciprocal_connections;
  public boolean o_zipf_distribution;
  public boolean o_node_doc_zipf_distribution;
  public boolean o_random_searches;
  public boolean o_random_forwarding;
  public boolean o_fuzzy_searches;

  public boolean o_applet;
  
  public static final String SIMULATION_TYPE = "SIMULATION_TYPE";
  public static final String NO_KEYWORDS = "NO_KEYWORDS";
  public static final String NO_DOCUMENTS = "NO_DOCUMENTS";
  public static final String NO_KEYWORDS_PER_DOCUMENT = "NO_KEYWORDS_PER_DOCUMENT";
  public static final String NO_NODES = "NO_NODES";
  public static final String NO_HONEST_NODES = "NO_HONEST_NODES";
  public static final String NO_DOCUMENTS_PER_NODE = "NO_DOCUMENTS_PER_NODE";
  public static final String MAX_KNOWLEDGE_PER_NODE = "MAX_KNOWLEDGE_PER_NODE";
  public static final String NO_CONNECTIONS_PER_NODE = "NO_CONNECTIONS_PER_NODE";
  public static final String MIN_CONNECTIONS_PER_NODE = "MIN_CONNECTIONS_PER_NODE";
  public static final String MAX_CONNECTIONS_PER_NODE = "MAX_CONNECTIONS_PER_NODE";
  public static final String NO_SEARCH_KEYWORDS = "NO_SEARCH_KEYWORDS";
  public static final String START_TTL = "START_TTL";
  public static final String MAX_FORWARDING_DEGREE = "MAX_FORWARDING_DEGREE";
  public static final String MIN_FORWARDING_DEGREE = "MIN_FORWARDING_DEGREE";
  public static final String INTERNAL_LOOP = "INTERNAL_LOOP";
  public static final String PROBE_LOOP = "PROBE_LOOP";
  public static final String NO_PROBES = "NO_PROBES";
  public static final String GROWTH_LOOP = "GROWTH_LOOP";
  public static final String STATS_LOOP = "STATS_LOOP";
  public static final String RING_TOPOLOGY = "RING_TOPOLOGY";
  public static final String RECIPROCAL_CONNECTIONS = "RECIPROCAL_CONNECTIONS";
  public static final String DOC_KEYWORD_ZIPF_DISTRIBUTION = "DOC_KEYWORD_ZIPF_DISTRIBUTION";
  public static final String NODE_DOC_ZIPF_DISTRIBUTION = "NODE_DOC_ZIPF_DISTRIBUTION";
  public static final String RANDOM_SEARCHES = "RANDOM_SEARCHES";
  public static final String RANDOM_FORWARDING = "RANDOM_FORWARDING";
  public static final String FUZZY_SEARCHES = "FUZZY_SEARCHES";
  public static final String APPLET = "APPLET";

  public NetworkParameters()
  {
  	
  }
  
  public void writeToLog(Logger p_logger)
  {
      p_logger.logMessage("% "+o_sim_type);
      p_logger.logMessage("% o_no_keywords = " + o_no_keywords);
      p_logger.logMessage("% o_no_documents = " + o_no_documents);
      p_logger.logMessage("% o_no_keywords_per_document = " + o_no_keywords_per_document);
      p_logger.logMessage("% o_no_nodes = " + o_no_nodes);
      p_logger.logMessage("% o_no_honest_nodes = " + o_no_honest_nodes);
      p_logger.logMessage("% o_no_documents_per_node = " + o_no_documents_per_node);
      p_logger.logMessage("% o_max_knowledge_per_node = " + o_max_knowledge_per_node);
      p_logger.logMessage("% o_no_connections_per_node = "+ o_no_connections_per_node);
      p_logger.logMessage("% o_min_connections_per_node = "+ o_min_connections_per_node);
      p_logger.logMessage("% o_max_connections_per_node = "+ o_max_connections_per_node);
      p_logger.logMessage("% o_max_forwarding_degree = " + o_max_forwarding_degree);
      p_logger.logMessage("% o_min_forwarding_degree = " + o_min_forwarding_degree);
      p_logger.logMessage("% o_no_search_keywords = " + o_no_search_keywords);
      p_logger.logMessage("% o_internal_loop = " + o_internal_loop);
      p_logger.logMessage("% o_start_TTL =" + o_start_TTL);  	
    
      p_logger.logMessage("% o_reciprocal_connections =  " + o_reciprocal_connections);
      p_logger.logMessage("% o_ring =  " + o_ring_topology);
      p_logger.logMessage("% o_zipf =  " + o_zipf_distribution);
      p_logger.logMessage("% o_node_doc_zipf =  " + o_node_doc_zipf_distribution);
      p_logger.logMessage("% o_random_search =  " + o_random_searches);
      p_logger.logMessage("% o_random_forwarding =  " + o_random_forwarding);
      p_logger.logMessage("% o_fuzzy_searches =  " + o_fuzzy_searches);
  }
  
  /**
   * @param p_properties  a set of @properties to parse
   */
  public void parse(Properties p_properties)
  {
      try{o_sim_type = p_properties.getProperty(SIMULATION_TYPE);}
      catch(Exception e){o_sim_type="none";}
      
      try{o_no_keywords = Integer.parseInt(p_properties.getProperty(NO_KEYWORDS));}
      catch(Exception e){o_no_keywords=1000;} //1000;
      
      try{o_no_documents = Integer.parseInt(p_properties.getProperty(NO_DOCUMENTS));}
      catch(Exception e){o_no_documents=2000;} //2000;
      
      try{o_no_keywords_per_document = Integer.parseInt(p_properties.getProperty(NO_KEYWORDS_PER_DOCUMENT));}
      catch(Exception e){o_no_keywords_per_document=2;} //2;
      
      try{o_no_nodes = Integer.parseInt(p_properties.getProperty(NO_NODES));}
      catch(Exception e){o_no_nodes=20;} //20;
      
      try{o_no_honest_nodes = Integer.parseInt(p_properties.getProperty(NO_HONEST_NODES));}
      catch(Exception e){o_no_honest_nodes=20;} //20;
      
      try{o_no_documents_per_node = Integer.parseInt(p_properties.getProperty(NO_DOCUMENTS_PER_NODE));}
      catch(Exception e){o_no_documents_per_node=3;} //3;
      
      try{o_max_knowledge_per_node = Integer.parseInt(p_properties.getProperty(MAX_KNOWLEDGE_PER_NODE));}
      catch(Exception e){o_max_knowledge_per_node=3;} //3;
      
      try{o_no_connections_per_node = Integer.parseInt(p_properties.getProperty(NO_CONNECTIONS_PER_NODE));}
      catch(Exception e){o_no_connections_per_node=3;} //3;
      
      try{o_max_connections_per_node = Integer.parseInt(p_properties.getProperty(MAX_CONNECTIONS_PER_NODE));}
      catch(Exception e){o_max_connections_per_node=3;} //3;
      
      try{o_min_connections_per_node = Integer.parseInt(p_properties.getProperty(MIN_CONNECTIONS_PER_NODE));}
      catch(Exception e){o_min_connections_per_node=2;} //3;
      
      try{o_no_search_keywords = Integer.parseInt(p_properties.getProperty(NO_SEARCH_KEYWORDS));}
      catch(Exception e){o_no_search_keywords=3;} //3;
      

      try{o_start_TTL = Integer.parseInt(p_properties.getProperty(START_TTL));}
      catch(Exception e){o_start_TTL=7;} //7;
      
      try{o_max_forwarding_degree = Integer.parseInt(p_properties.getProperty(MAX_FORWARDING_DEGREE));}
      catch(Exception e){o_max_forwarding_degree=1;} //1; 
 
 
      try{o_min_forwarding_degree = Integer.parseInt(p_properties.getProperty(MIN_FORWARDING_DEGREE));}
      catch(Exception e){o_min_forwarding_degree=2;} //2;
      
      
      try{o_internal_loop = Integer.parseInt(p_properties.getProperty(INTERNAL_LOOP));}
      catch(Exception e){o_internal_loop=1;} //1000;
      
      try{o_probe_loop = Integer.parseInt(p_properties.getProperty(PROBE_LOOP));}
      catch(Exception e){o_probe_loop=1;} //1000;
      
      try{o_no_probes = Integer.parseInt(p_properties.getProperty(NO_PROBES));}
      catch(Exception e){o_no_probes=1;} //1000;
      
      try{o_growth_loop = Integer.parseInt(p_properties.getProperty(GROWTH_LOOP));}
      catch(Exception e){o_growth_loop=1;} //1000;
      
      try{o_stats_loop = Integer.parseInt(p_properties.getProperty(STATS_LOOP));}
      catch(Exception e){o_stats_loop=1;} //1000;
            
      
      try{o_ring_topology = (p_properties.getProperty(RING_TOPOLOGY).equals("true")?true:false);}
      catch(Exception e){o_ring_topology=true;} //true;
      
      try{o_reciprocal_connections = (p_properties.getProperty(RECIPROCAL_CONNECTIONS).equals("true")?true:false);
      
      //System.out.println("A:"+RECIPROCAL_CONNECTIONS);
      //System.out.println("B:"+p_properties.getProperty(RECIPROCAL_CONNECTIONS));
      //System.out.println("C:"+o_reciprocal_connections);
      
      }
      catch(Exception e){o_reciprocal_connections=true;} //true;

      //System.out.println("A:"+RECIPROCAL_CONNECTIONS);
      //System.out.println("B:"+p_properties.getProperty(RECIPROCAL_CONNECTIONS));
      //System.out.println("C:"+o_reciprocal_connections);
      
      
      try{o_zipf_distribution = (p_properties.getProperty(DOC_KEYWORD_ZIPF_DISTRIBUTION).equals("true")?true:false);}
      catch(Exception e){o_zipf_distribution=true;} //true;
      
      try{o_node_doc_zipf_distribution = (p_properties.getProperty(NODE_DOC_ZIPF_DISTRIBUTION).equals("true")?true:false);}
      catch(Exception e){o_node_doc_zipf_distribution=true;} //true;
      
      try{o_random_searches = (p_properties.getProperty(RANDOM_SEARCHES).equals("true")?true:false);}
      catch(Exception e){o_random_searches=true;} //true;
     
      try{o_random_forwarding = (p_properties.getProperty(RANDOM_FORWARDING).equals("true")?true:false);}
      catch(Exception e){o_random_forwarding=true;} //true;
      
      try{o_fuzzy_searches = (p_properties.getProperty(FUZZY_SEARCHES).equals("true")?true:false);}
      catch(Exception e){o_fuzzy_searches=true;} //true;
      

      try{o_applet = (p_properties.getProperty(APPLET).equals("true")?true:false);}
      catch(Exception e){o_applet=true;} //false;
  }

}
