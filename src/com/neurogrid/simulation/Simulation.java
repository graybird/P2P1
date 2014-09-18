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

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import com.neurogrid.log.BasicFileLog;
import com.neurogrid.log.Logger;
import com.neurogrid.simulation.root.Network;
import com.neurogrid.simulation.root.NetworkParameters;
import com.neurogrid.simulation.statistics.SearchStatistics;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class handles multiple search simulations and storage of results.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   12/Mar/2001    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class Simulation
{
  private static final String cvsInfo = "$Id: Simulation.java,v 1.3 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  /**
   * Method for retrieving a resource bundle
   *
   * @param p_resource    name of the resource bundle
   */
  protected void getProperties(String p_file)
  {
    try
    {
     // o_resources = ResourceBundle.getBundle(p_resource,Locale.getDefault());
      
      
      o_properties = new Properties();
      FileInputStream x_input = new FileInputStream(p_file);
      o_properties.load(x_input);
      x_input.close();

    }
    catch (Exception e)
    {
      System.err.println(p_file + " not found");
      e.printStackTrace();
    }
  }
  
  protected static Properties o_properties = null;
  //protected static ResourceBundle o_resources = null;
  
  // private variables

  public static BasicFileLog   o_log = null;               // file log
  public static Logger         o_logger = null;            // logger object for writing to file log

  /**
   * Simulation Constructor
   */
  public Simulation()
  {

  // it'll never be a free net until you can search it properly
  }


  public static void main(String[] args)
  {
    try
    {
      Simulation x_sim = new Simulation();
      String x_file = "./conf/PARAMETERS.properties";
      if(args != null && args.length == 1)
      {
         x_file = args[0];
      }

      x_sim.getProperties(x_file);
    
      Logger.o_verbosity = -10;
      
      Date x_date = new Date(System.currentTimeMillis());
      String x_timestamp = x_date.toString().replace(' ','_').replace(':','-');
      
      String x_log_file = x_timestamp+ "_"+o_properties.getProperty("LOG_FILE");
      
      Simulation.o_log = new BasicFileLog(x_log_file,false,"");

      Simulation.o_logger = new Logger(o_log);
      Simulation.o_logger.logMessage("Start Logging");

      NetworkParameters o_network_parameters = new NetworkParameters();
      o_network_parameters.parse(Simulation.o_properties);
      
      String x_sim_type = o_network_parameters.o_sim_type;

      int x_no_keywords = o_network_parameters.o_no_keywords;
      int x_no_documents = o_network_parameters.o_no_documents;
      int x_no_keywords_per_document = o_network_parameters.o_no_keywords_per_document;
      int x_no_nodes = o_network_parameters.o_no_nodes;
      int x_no_honest_nodes = o_network_parameters.o_no_honest_nodes;
      int x_no_documents_per_node = o_network_parameters.o_no_documents_per_node;
      int x_max_knowledge_per_node = o_network_parameters.o_max_knowledge_per_node;
      int x_no_connections_per_node = o_network_parameters.o_no_connections_per_node;
      int x_max_connections_per_node = o_network_parameters.o_max_connections_per_node;
      int x_no_search_keywords = o_network_parameters.o_no_search_keywords;
  
      int x_start_TTL = o_network_parameters.o_start_TTL;
      
      int x_max_forwarding_degree = o_network_parameters.o_max_forwarding_degree;
      int x_min_forwarding_degree = o_network_parameters.o_min_forwarding_degree;
      
      int x_internal_loop = o_network_parameters.o_internal_loop;
      int x_probe_loop = o_network_parameters.o_probe_loop;
      int x_no_probes = o_network_parameters.o_no_probes;
      int x_growth_loop = o_network_parameters.o_growth_loop;
      int x_stats_loop = o_network_parameters.o_stats_loop;
      
      boolean x_ring_topology = o_network_parameters.o_ring_topology;
      boolean x_zipf_distribution = o_network_parameters.o_zipf_distribution;

      boolean x_applet = o_network_parameters.o_applet;

      Network x_network = new NeuroGridNetwork();

	  Network.o_applet = x_applet;
     
      x_network.o_max_forwarding_degree = x_max_forwarding_degree;
      x_network.o_min_forwarding_degree = x_min_forwarding_degree;
      
      x_network.o_ring_topology = x_ring_topology;
      x_network.o_zipf_distribution = x_zipf_distribution;
      
	  Network.o_verbosity = -10;      

      o_network_parameters.writeToLog(Simulation.o_logger);

      Network.timeCheck("Start");
      

      Simulation.o_logger.logMessage("AD-NG, Connectivity, TTL, #matches, #false matches, TTL of 1st match, #message transfers, #nodes reached, #possible matches");
      System.out.println("AD-NG, Connectivity, TTL, #matches, #false matches, TTL of 1st match, #message transfers, #nodes reached, #possible matches");

      // would be nice to run through for different numbers of connections per node ....

      SearchStatistics x_stats = null;
      

      //for(int y=1;y<21;y++)
      int y=2;
      {
        x_network.clearNetwork();
        x_network.refresh();

        // lets try to get NG here

      //  x_network.createNetwork(x_no_keywords,x_no_documents,x_no_keywords_per_document,x_no_nodes,
      //            x_no_documents_per_node,y,x_knowledge,x_degree_of_correlation);

        o_network_parameters.o_no_connections_per_node = y;
        x_network.createNetwork(o_network_parameters, new Random(888));

        //for(int i=20;i>0;i--)
        int i=5;
        {
          Random x_random = new Random(888);	
          for(int j=0;j<x_internal_loop;j++)
          {
            x_stats = x_network.fuzzySearchNetwork(i,x_no_search_keywords,x_random);
            System.out.println(y + ", " + i + ", " + x_stats.getNoMatches() + 
                                              ", " + x_stats.getNoFalseMatches() + 
                                              ", " + x_stats.getTTLOfFirstMatch() + 
                                              ", " + x_stats.getNoMessageTransfers() + 
                                              ", " + x_stats.getNoPossibleTargets() + 
                                              ", " + x_stats.getNoActivatedNodes());
            Simulation.o_logger.logMessage("AD-NG, "+ y + ", " + i + ", " + x_stats.getNoMatches() + 
                                              ", " + x_stats.getNoFalseMatches() + 
                                              ", " + x_stats.getTTLOfFirstMatch() + 
                                              ", " + x_stats.getNoMessageTransfers() + 
                                              ", " + x_stats.getNoPossibleTargets() + 
                                              ", " + x_stats.getNoActivatedNodes());
            // gotta refresh the network
            x_network.refresh();
            //System.gc();
            //Simulation.outputStats("loop: " +j);
          }
        }

        x_network.clearNetwork();
        x_network.refresh();


        //System.gc();
      }

      Network.timeCheck("End");
      Simulation.o_logger.halt();
     
      Simulation.o_log.close();
    
    
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  public static StringBuffer o_buf = new StringBuffer(256);
  public static long o_previous_used = 0L;
  public static long o_time = System.currentTimeMillis();
  
  public static void outputStats(String p_message)
  {
    outputStats(p_message,System.currentTimeMillis(),o_time);   
  }

  public static void outputStats(String p_message, long p_new_time, long p_time)
  {
    o_buf.delete(0,o_buf.length());
    o_buf.append(p_message).append((p_new_time - p_time));
    o_buf.append(", Free: ").append(Runtime.getRuntime().freeMemory());    	
    o_buf.append(", Max: ").append(Runtime.getRuntime().maxMemory());    	
    o_buf.append(", Total: ").append(Runtime.getRuntime().totalMemory()); 
    long x_current_used =  (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());  	
    o_buf.append(", Used: ").append(x_current_used);    	
    o_buf.append(", Change: ").append((x_current_used - o_previous_used - 1760L));   
    System.out.println(o_buf.toString()); 
    Simulation.o_logger.logMessage(o_buf.toString());	
    o_previous_used = x_current_used;
  }

}

