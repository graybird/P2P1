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
 
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import com.neurogrid.log.BasicFileLog;
import com.neurogrid.log.Logger;
import com.neurogrid.simulation.root.Network;
import com.neurogrid.simulation.root.NetworkParameters;
import com.neurogrid.simulation.root.Node;
import com.neurogrid.simulation.statistics.SearchStatistics;
import com.neurogrid.simulation.statistics.Statistics;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com>
 *
 * -----------------------------------------------------------
 * This class handles the overall simulation and 
 * a more advanced battery of stats than the basic
 * simulation class <br><br>
 *
 * @author Sam Joseph (sam@valuecommerce.ne.jp)
 *
 * Change History
 * ------------------------------------------------------------------------------------
 * 0.1   12/Mar/2001    sam       Created file
 */

public class AdvancedSimulation extends Simulation
{
  public static final String cvsInfo = "$Id: AdvancedSimulation.java,v 1.6 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  
  // private variables

  public static BasicFileLog   o_log = null;               // file log
  public static Logger         o_logger = null;            // logger object for writing to file log
  public static BasicFileLog   o_sum_log = null;               // file log
  public static Logger         o_sum_logger = null;            // logger object for writing to file log
  public static BasicFileLog   o_arch_log = null;               // file log
  public static Logger         o_arch_logger = null;            // logger object for writing to file log
  public static BasicFileLog   o_probe_log = null;               // file log
  public static Logger         o_probe_logger = null;            // logger object for writing to file log
  public static BasicFileLog   o_search_log = null;               // file log
  public static Logger         o_search_logger = null;            // logger object for writing to file log

  /**
   * Simulation Constructor
   */
  public AdvancedSimulation()
  {


  }


  public static void main(String[] args)
  {
    try
    {
      //String x_file = "./conf/ADVANCED_PARAMETERS.properties";
      String x_file = "conf/ADVANCED_PARAMETERS.properties";
      Simulation x_sim = new Simulation();
      if(args != null && args.length == 1)
      {
         x_file = args[0];
      }

      x_sim.getProperties(x_file);
     
      
      Date x_date = new Date(System.currentTimeMillis());
      String x_timestamp = x_date.toString().replace(' ','_').replace(':','-');
      
      String x_log_file = x_timestamp+ "_"+o_properties.getProperty("MAIN_LOG_FILE");
      String x_sum_log_file = x_timestamp+ "_"+o_properties.getProperty("SUMMARY_LOG_FILE");
      String x_arch_log_file = x_timestamp+ "_"+o_properties.getProperty("ARCHITECTURE_LOG_FILE");
      String x_probe_log_file = x_timestamp+ "_"+o_properties.getProperty("PROBE_LOG_FILE");
      String x_search_log_file = null;
      String x_name = null;
      try
      {  
      	x_name = o_properties.getProperty("SEARCH_LOG_FILE");
      	if(x_name != null)
      	  x_search_log_file = x_timestamp+ "_"+ x_name;
      }catch(Exception e){}
      // could handle the above not being speced


      Statistics x_nm_statistics = null;
      Statistics x_nfm_statistics = null;
      Statistics x_ttl_statistics = null;
      Statistics x_probe_ttl_statistics = null;
      Statistics x_mt_statistics = null;
      Statistics x_nr_statistics = null;
      Statistics x_rc_statistics = null;
      Statistics x_pr_statistics = null;
      Statistics x_ref_statistics = null;
      Statistics x_pef_statistics = null;

      
      Logger.o_verbosity = -10;
      AdvancedSimulation.o_log = new BasicFileLog(x_log_file,false,"");
      AdvancedSimulation.o_logger = new Logger(o_log);
      AdvancedSimulation.o_logger.logMessage("% Start Logging");
      AdvancedSimulation.o_sum_log = new BasicFileLog(x_sum_log_file,false,"");
      AdvancedSimulation.o_sum_logger = new Logger(o_sum_log);
      AdvancedSimulation.o_sum_logger.logMessage("% Start Logging");
      AdvancedSimulation.o_arch_log = new BasicFileLog(x_arch_log_file,false,"");
      AdvancedSimulation.o_arch_logger = new Logger(o_arch_log);
      AdvancedSimulation.o_arch_logger.logMessage("% Start Logging");
      AdvancedSimulation.o_probe_log = new BasicFileLog(x_probe_log_file,false,"");
      AdvancedSimulation.o_probe_logger = new Logger(o_probe_log);
      AdvancedSimulation.o_probe_logger.logMessage("% Start Logging");
      try{
      if(x_search_log_file != null)
      {
        AdvancedSimulation.o_search_log = new BasicFileLog(x_search_log_file,false,"");
        AdvancedSimulation.o_search_logger = new Logger(o_search_log);
        AdvancedSimulation.o_search_logger.logMessage("% Start Logging");
      }
      }catch(Exception e){}


      NetworkParameters o_network_parameters = new NetworkParameters();
      o_network_parameters.parse(o_properties);


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
      boolean x_reciprocal_connections = o_network_parameters.o_reciprocal_connections;
      boolean x_zipf_distribution = o_network_parameters.o_zipf_distribution;
      boolean x_node_doc_zipf_distribution = o_network_parameters.o_node_doc_zipf_distribution;
      boolean x_random_searches = o_network_parameters.o_random_searches;
      boolean x_random_forwarding = o_network_parameters.o_random_forwarding;
      boolean x_fuzzy_searches = o_network_parameters.o_fuzzy_searches;
      
      boolean x_applet = o_network_parameters.o_applet;
        
      double[] x_no_matches = new double[x_stats_loop];
      double[] x_no_false_matches = new double[x_stats_loop];
      double[] x_ttl = new double[x_stats_loop];
      double[] x_probe_ttl = new double[x_no_probes];
      double[] x_message_trans = new double[x_stats_loop];
      double[] x_nodes_reached = new double[x_stats_loop];
      double[] x_recall = new double[x_stats_loop];
      double[] x_precision = new double[x_stats_loop];
      double[] x_recall_efficiency = new double[x_stats_loop];
      double[] x_precision_efficiency = new double[x_stats_loop];
      
      boolean x_temp_learning_state = false;
      // new network design cannot have learning switched on and off so simple
      // FIXXXXXXXXXXXXXXX?????????????

     

      Network.o_applet = x_applet;

      int x_conn_sum = 0;
      int x_know_sum = 0;
      int x_know_total_sum = 0;
      int x_contents_sum = 0;
      
      String x_end = null;

      Network.timeCheck("Start");




      o_network_parameters.writeToLog(AdvancedSimulation.o_logger);
      o_network_parameters.writeToLog(AdvancedSimulation.o_sum_logger);
      o_network_parameters.writeToLog(AdvancedSimulation.o_arch_logger);
      o_network_parameters.writeToLog(AdvancedSimulation.o_probe_logger);
      try
      {
      	o_network_parameters.writeToLog(AdvancedSimulation.o_search_logger);
      }
      catch(Exception e){}

      // should be passing network parameters into network at creation time
      // instead of the setting going on afterwards
      // FIXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
      // should then make all network params protected instead of public
        
      Network x_network = new NeuroGridNetwork();
      
      x_network.o_max_forwarding_degree = x_max_forwarding_degree;
      x_network.o_min_forwarding_degree = x_min_forwarding_degree;

	  Network.o_verbosity = -10;

      x_network.o_ring_topology = x_ring_topology;
      x_network.o_reciprocal_connections = x_reciprocal_connections;
      x_network.o_zipf_distribution = x_zipf_distribution;
      x_network.o_node_doc_zipf_distribution = x_node_doc_zipf_distribution;
      x_network.o_random_searches = x_random_searches;
      x_network.o_random_forwarding = x_random_forwarding;
	  Network.o_search_logger = AdvancedSimulation.o_search_logger;
       
      Random x_random = new Random(888);
      x_network.createNetwork(o_network_parameters,x_random);         
                 

                  
      Iterator x_iter = x_network.getNodes().values().iterator();

      Node x_node = null;
                  
      AdvancedSimulation.o_arch_logger.logMessage("\n% Start Network Status\n");
      AdvancedSimulation.o_arch_logger.logMessage("% conns, knowledge, knowledge links, contents");
 
      StringBuffer x_buf = new StringBuffer(256);
          
      AdvancedSimulation.o_arch_logger.logMessage("s=[");
        
      while(x_iter.hasNext())
      {
        x_node = (Node)(x_iter.next());
        if(x_iter.hasNext())
          x_end = ";";
        else
          x_end = "]";
            
        x_buf.delete(0,x_buf.length());
        x_buf.append(x_node.getNoConnections()).append(" ")
             .append(x_node.getNoKnowledge()).append(" ")
             .append(x_node.getTotalNoKnowledge()).append(" ")
             .append(x_node.getNoContents()).append(x_end); 
          
        AdvancedSimulation.o_arch_logger.logMessage(x_buf.toString()); 
            
         // AdvancedSimulation.o_arch_logger.logMessage(x_node.getNoConnections() + ", " + x_node.getNoKnowledge()+ ", " + x_node.getTotalNoKnowledge()+ ", " + x_node.getNoContents() +x_end);
      }
      AdvancedSimulation.o_arch_logger.logMessage("\n");
      

      System.out.println("loop, No matches, No false matches, TTL, message transfers, Nodes reached, possible matches");
      System.out.println("loop, total conns, total knowledge, total knowledge links, message transfers");
      AdvancedSimulation.o_logger.logMessage("% loop, No matches, No false matches, TTL, message transfers, Nodes reached, possible matches");
      AdvancedSimulation.o_sum_logger.logMessage("% loop, No matches (ave,stdev), No false matches (ave,stdev), TTL (ave,stdev), message transfers (ave,stdev), Nodes reached (ave,stdev), recall (ave,stdev), precision (ave,stdev), recall efficiency (ave,stdev), precision efficiency (ave,stdev)");
      AdvancedSimulation.o_arch_logger.logMessage("% loop, total nodes, total conns, total knowledge, total knowledge links, message transfers");
      AdvancedSimulation.o_probe_logger.logMessage("% loop, total nodes, ttl (ave,stdev,max,min)");
      AdvancedSimulation.o_logger.logMessage("w=[");
      AdvancedSimulation.o_sum_logger.logMessage("x=[");
      AdvancedSimulation.o_arch_logger.logMessage("y=[");
      AdvancedSimulation.o_probe_logger.logMessage("z=[");


      x_internal_loop++;
      
      SearchStatistics x_stats = null;
        
      for(int j=1;j<x_internal_loop;j++)
      {
        if(j==x_internal_loop-1)
          x_end = "]";
        else
          x_end = ";";	
      /*    {
            x_enum = Node.o_nodes.elements();
            x_conn_sum = 0;
            while(x_enum.hasMoreElements())
            {
              x_node = (Node)(x_enum.nextElement());
              x_conn_sum += x_node.getNoConnections();
            }
            x_buf.delete(0,x_buf.length());
            x_buf.append("-1").append("   ").append(Node.o_nodes.size()).append("   ").append(x_conn_sum);
            AdvancedSimulation.o_arch_logger.logMessage(x_buf.toString());
          }*/
          if(x_fuzzy_searches)
          { 	
            x_stats = x_network.fuzzySearchNetwork(x_start_TTL,x_no_search_keywords,x_random);
          }
          else
          {
            x_stats = x_network.searchNetwork(x_start_TTL,x_random);	
          }
          x_buf.delete(0,x_buf.length());
          x_buf.append(j)
               .append("   ")
               .append(x_stats.getNoMatches())
               .append(" ")
               .append(x_stats.getNoFalseMatches())
               .append(" ")
               .append(x_stats.getTTLOfFirstMatch())
               .append(" ")
               .append(x_stats.getNoMessageTransfers())
               .append(" ")
               .append(x_stats.getNoActivatedNodes())
               .append(" ")
               .append(x_stats.getNoPossibleTargets())
               .append(x_end);

               
        /*  {
            x_enum = Node.o_nodes.elements();
            x_conn_sum = 0;
            while(x_enum.hasMoreElements())
            {
              x_node = (Node)(x_enum.nextElement());
              x_conn_sum += x_node.getNoConnections();
            }
            x_buf.delete(0,x_buf.length());
            x_buf.append("-2").append("   ").append(Node.o_nodes.size()).append("   ").append(x_conn_sum);
            AdvancedSimulation.o_arch_logger.logMessage(x_buf.toString());      
          } */
               
          //System.out.println(j + ",   " + x_array[0] + ", " + x_array[1] + ", " + x_array[2] + ", " + x_array[3] + ", " + x_array[4]);
          AdvancedSimulation.o_logger.logMessage(x_buf.toString());
          //AdvancedSimulation.o_logger.logMessage( j + "   " + x_array[0] + " " + x_array[1] + " " + x_array[2] + " " + x_array[3] + " " + x_array[4]);

          x_no_matches[j%x_stats_loop] = (double)(x_stats.getNoMatches());
          x_no_false_matches[j%x_stats_loop] = (double)(x_stats.getNoFalseMatches());
          x_ttl[j%x_stats_loop] = Math.max((double)(x_stats.getTTLOfFirstMatch()),0.0D);
          x_message_trans[j%x_stats_loop] = (double)(x_stats.getNoMessageTransfers());
          x_nodes_reached[j%x_stats_loop] = (double)(x_stats.getNoActivatedNodes());
          x_recall[j%x_stats_loop] = (double)(x_stats.getNoMatches()-x_stats.getNoFalseMatches())/(double)(x_stats.getNoPossibleTargets());
          x_precision[j%x_stats_loop] = (x_stats.getNoMatches() == 0?1.0D:(((double)(x_stats.getNoMatches()-x_stats.getNoFalseMatches())/(double)(x_stats.getNoMatches()))) );
          x_recall_efficiency[j%x_stats_loop] = 
                      x_recall[j%x_stats_loop]/
                      x_message_trans[j%x_stats_loop];
          x_precision_efficiency[j%x_stats_loop] = 
                      x_precision[j%x_stats_loop]/
                      x_message_trans[j%x_stats_loop];
          
          if((j%x_growth_loop) == 0)
          {
            x_network.addNodes(x_no_keywords,x_no_documents,x_no_keywords_per_document,1,
                          x_no_documents_per_node,x_no_connections_per_node,true,
                          x_random);      	
          }
          if((j%x_probe_loop) == 0) // do probe of network  - could switch learning off ...
          {
            //x_temp_learning_state = Network.o_learning;
            // FIXXXXXXXXXXXXXXX
            // unable to switch learning off	
            //Network.o_learning = false;	
            for(int p=0;p<x_no_probes;p++)
            {
              x_network.refresh();	
              if(x_fuzzy_searches)
              {
                x_stats = x_network.fuzzySearchNetwork(500,x_no_search_keywords,x_random);
              }
              else
              {
                x_stats = x_network.searchNetwork(500,x_random);
              }
              x_probe_ttl[p] = Math.max((double)(x_stats.getTTLOfFirstMatch()),0.0D);
              //System.out.println("x_probe_ttl["+p+"]: "+x_probe_ttl[p] + " - nodes reached: " + x_array[3]);
            }
            x_probe_ttl_statistics = new Statistics(x_probe_ttl);
            x_probe_ttl_statistics.calculate();

            x_buf.delete(0,x_buf.length());
            x_buf.append( j).append("   ").append(x_network.getNoNodes())
                 .append("   ").append(x_probe_ttl_statistics.getAverage())
                 .append("   ").append(x_probe_ttl_statistics.getDeviation())
                 .append("   ").append(x_probe_ttl_statistics.getMax())
                 .append("   ").append(x_probe_ttl_statistics.getMin())
                 .append(x_end);
                 
            AdvancedSimulation.o_probe_logger.logMessage(x_buf.toString());
            
   /*         AdvancedSimulation.o_probe_logger.logMessage( j + "   " + Node.o_nodes.size()
                                                       + "   " + x_probe_ttl_statistics.getAverage()
                                                       + "   " + x_probe_ttl_statistics.getDeviation()
                                                       + "   " + x_probe_ttl_statistics.getMax()
                                                       + "   " + x_probe_ttl_statistics.getMin());*/
    
            
            //Network.o_learning = x_temp_learning_state;	
          }
          if((j%x_stats_loop) == 0)
          {
            // would like to include average data for the searching activity..?	

            x_nm_statistics = new Statistics(x_no_matches);
            x_nm_statistics.calculate();
            x_nfm_statistics = new Statistics(x_no_false_matches);
            x_nfm_statistics.calculate();
            x_ttl_statistics = new Statistics(x_ttl);
            x_ttl_statistics.calculate();
            x_mt_statistics = new Statistics(x_message_trans);
            x_mt_statistics.calculate();
            x_nr_statistics = new Statistics(x_nodes_reached);
            x_nr_statistics.calculate();
            x_rc_statistics = new Statistics(x_recall);
            x_rc_statistics.calculate();
            x_pr_statistics = new Statistics(x_precision);
            x_pr_statistics.calculate();
            x_ref_statistics = new Statistics(x_recall_efficiency);
            x_ref_statistics.calculate();
            x_pef_statistics = new Statistics(x_precision_efficiency);
            x_pef_statistics.calculate();


            x_buf.delete(0,x_buf.length());
            x_buf.append(j)
                 .append("   ")
                 .append(x_nm_statistics.getAverage())
                 .append("   ")
                 .append(x_nm_statistics.getDeviation())
                 .append(" ")
                 .append(x_nfm_statistics.getAverage())
                 .append("   ")
                 .append(x_nfm_statistics.getDeviation())
                 .append(" ")
                 .append(x_ttl_statistics.getAverage())
                 .append(" ")
                 .append(x_ttl_statistics.getDeviation())
                 .append(" ")
                 .append(x_mt_statistics.getAverage())
                 .append(" ")
                 .append(x_mt_statistics.getDeviation())
                 .append(" ")
                 .append(x_nr_statistics.getAverage())
                 .append(" ")
                 .append(x_nr_statistics.getDeviation())
                 .append(" ")
                 .append(x_rc_statistics.getAverage())
                 .append(" ")
                 .append(x_rc_statistics.getDeviation())
                 .append(" ")
                 .append(x_pr_statistics.getAverage())
                 .append(" ")
                 .append(x_pr_statistics.getDeviation())
                 .append(" ")
                 .append(x_ref_statistics.getAverage())
                 .append(" ")
                 .append(x_ref_statistics.getDeviation())
                 .append(" ")
                 .append(x_pef_statistics.getAverage())
                 .append(" ")
                 .append(x_pef_statistics.getDeviation())
                 .append(x_end); 
                                                       
            AdvancedSimulation.o_sum_logger.logMessage(x_buf.toString());
            /*
            AdvancedSimulation.o_sum_logger.logMessage(j + "   " + x_nm_statistics.getAverage() +
                                                       "   " + x_nm_statistics.getDeviation() + " " +
                                                       x_ttl_statistics.getAverage() + " " + 
                                                       x_ttl_statistics.getDeviation() + " " +
                                                       x_mt_statistics.getAverage() + " " + 
                                                       x_mt_statistics.getDeviation() + " " +
                                                       x_nr_statistics.getAverage() + " " + 
                                                       x_nr_statistics.getDeviation() + " " +
                                                       x_pm_statistics.getAverage() + " " + 
                                                       x_pm_statistics.getDeviation() + " " +
                                                       x_ef_statistics.getAverage() + " " + 
                                                       x_ef_statistics.getDeviation());

*/


            x_iter = x_network.getNodes().values().iterator();
            x_conn_sum = 0;
            x_know_sum = 0;
            x_know_total_sum = 0;

            while(x_iter.hasNext())
            {
              x_node = (Node)(x_iter.next());

              x_conn_sum += x_node.getNoConnections();
              x_know_sum += x_node.getNoKnowledge();
              x_know_total_sum +=x_node.getTotalNoKnowledge();
            }
            x_buf.delete(0,x_buf.length());
            x_buf.append(j).append("   ").append(x_network.getNodes().size()).append("   ")
                 .append(x_conn_sum).append(" ").append(x_know_sum).append(" ")
                 .append(x_know_total_sum).append(" ").append(x_stats.getNoMessageTransfers()).append(x_end);
                 
            AdvancedSimulation.o_arch_logger.logMessage(x_buf.toString());
            
          //  AdvancedSimulation.o_arch_logger.logMessage( j + "   " + Node.o_nodes.size() + "   " + x_conn_sum + " " + x_know_sum + " " + x_know_total_sum + " " + x_array[2]);
          }
          
          
          // gotta refresh the network
          x_network.refresh();



          
          // need to solve problems with node addition XXXXXXXXXXXXXXXXXXX  
          
          // maybe we would need limits on hashtable sizes -> or we could just run until we are out of
          // memory, catch the exception, garbage collect and then try and print out the sizes ...
          // if I could get up to 100,000 nodes that would be fun.
        }

        // might be nice to have a getIterator option to avoid the cloning
        // FIXXXXXXXXXXXXXXXXXXX
        x_iter = x_network.getNodes().values().iterator();
          
        AdvancedSimulation.o_arch_logger.logMessage("\n% End Network Status\n");
        AdvancedSimulation.o_arch_logger.logMessage("% conns, knowledge, knowledge links, contents");
          
        AdvancedSimulation.o_arch_logger.logMessage("e=[");
        
        while(x_iter.hasNext())
        {
          x_node = (Node)(x_iter.next());
          
          if(x_iter.hasNext())
            x_end = ";";
          else
            x_end = "]";


          x_buf.delete(0,x_buf.length());
          x_buf.append(x_node.getNoConnections()).append(" ")
               .append(x_node.getNoKnowledge()).append(" ")
               .append(x_node.getTotalNoKnowledge()).append(" ")
               .append(x_node.getNoContents()).append(x_end); 
          
          // there should be a relatively simple way to plot the distribution of connections
          // this loop could include making table holding no.conns --> no. nodes
          
          AdvancedSimulation.o_arch_logger.logMessage(x_buf.toString()); 
         // AdvancedSimulation.o_arch_logger.logMessage(x_node.getNoConnections() + " " + x_node.getNoKnowledge()+ " " + x_node.getTotalNoKnowledge()+ " " + x_node.getNoContents() +x_end);
          
        }


          AdvancedSimulation.o_arch_logger.logMessage("subplot(2,2,1)");
          AdvancedSimulation.o_arch_logger.logMessage("plot(y(:,1),y(:,2))");
          AdvancedSimulation.o_arch_logger.logMessage("title('No of Nodes')");
          AdvancedSimulation.o_arch_logger.logMessage("axis([0 "+x_internal_loop+" 0 "+x_no_nodes*10+"])");

          AdvancedSimulation.o_arch_logger.logMessage("subplot(2,2,2)");
          AdvancedSimulation.o_arch_logger.logMessage("plot(y(:,1),y(:,3))");
          AdvancedSimulation.o_arch_logger.logMessage("title('No of Conns')");
          AdvancedSimulation.o_arch_logger.logMessage("axis([0 "+x_internal_loop+" 0 "+x_no_nodes*x_max_connections_per_node+"])");

          AdvancedSimulation.o_arch_logger.logMessage("subplot(2,2,3)");
          AdvancedSimulation.o_arch_logger.logMessage("plot(y(:,1),y(:,4))");
          AdvancedSimulation.o_arch_logger.logMessage("title('No of Knowledge')");
          AdvancedSimulation.o_arch_logger.logMessage("axis([0 "+x_internal_loop+" 0 "+x_no_nodes*x_max_knowledge_per_node+"])");

          AdvancedSimulation.o_arch_logger.logMessage("subplot(2,2,4)");
          AdvancedSimulation.o_arch_logger.logMessage("plot(y(:,1),y(:,5))");
          AdvancedSimulation.o_arch_logger.logMessage("title('Total Knowledge')");
          AdvancedSimulation.o_arch_logger.logMessage("axis([0 "+x_internal_loop+" 0 "+x_no_nodes*x_max_knowledge_per_node+"])");

        ////AdvancedSimulation.o_logger.logMessage("\nEnd Network status\n");91
        //x_enum = Node.o_nodes.elements();
        //System.out.println("\n\nEnd state of network\n");
        ////System.out.println("x_enum = "+(x_enum==null?"null":"not null"));
        //while(x_enum.hasMoreElements())
       // {
         // x_node = (Node)(x_enum.nextElement());
          ////System.out.print(x_node.getNoConnections());
          ////System.out.print(", ");
          ////System.out.println(x_node.getNoKnowledge());
          //AdvancedSimulation.o_logger.logMessage(x_node.getNoConnections() + ", " + x_node.getNoKnowledge());
        //}
        AdvancedSimulation.o_sum_logger.logMessage("");        
        AdvancedSimulation.o_sum_logger.logMessage("subplot(3,2,1)");
        AdvancedSimulation.o_sum_logger.logMessage("errorbar(x(:,1),x(:,2),x(:,3))");
        AdvancedSimulation.o_sum_logger.logMessage("title('No of Matches')");
        AdvancedSimulation.o_sum_logger.logMessage("axis([0 "+x_internal_loop+" 0 160])");
        AdvancedSimulation.o_sum_logger.logMessage("");

        AdvancedSimulation.o_sum_logger.logMessage("subplot(3,2,2)");
        AdvancedSimulation.o_sum_logger.logMessage("errorbar(x(:,1),x(:,4),x(:,5))");
        AdvancedSimulation.o_sum_logger.logMessage("title('No of False Matches')");
        AdvancedSimulation.o_sum_logger.logMessage("axis([0 "+x_internal_loop+" 0 160])");
        AdvancedSimulation.o_sum_logger.logMessage("");

        AdvancedSimulation.o_sum_logger.logMessage("subplot(3,2,3)");
        AdvancedSimulation.o_sum_logger.logMessage("errorbar(x(:,1),"+x_start_TTL+"-x(:,6),x(:,7))");
        AdvancedSimulation.o_sum_logger.logMessage("title('Path Length')");
        AdvancedSimulation.o_sum_logger.logMessage("axis([0 "+x_internal_loop+" 0 "+x_start_TTL+"])");
        AdvancedSimulation.o_sum_logger.logMessage("");
        
        AdvancedSimulation.o_sum_logger.logMessage("subplot(3,2,4)");
        AdvancedSimulation.o_sum_logger.logMessage("errorbar(x(:,1),x(:,8),x(:,9))");
        AdvancedSimulation.o_sum_logger.logMessage("title('Message Transfers')");
        AdvancedSimulation.o_sum_logger.logMessage("axis([0 "+x_internal_loop+" 0 "+x_no_nodes+"])");
        AdvancedSimulation.o_sum_logger.logMessage("");
        
        AdvancedSimulation.o_sum_logger.logMessage("subplot(3,2,5)");
        AdvancedSimulation.o_sum_logger.logMessage("errorbar(x(:,1),x(:,12),x(:,13))");
        AdvancedSimulation.o_sum_logger.logMessage("title('Recall')");
        AdvancedSimulation.o_sum_logger.logMessage("axis([0 "+x_internal_loop+" 0 1])");
        AdvancedSimulation.o_sum_logger.logMessage("");  
        
        AdvancedSimulation.o_sum_logger.logMessage("subplot(3,2,6)");
        AdvancedSimulation.o_sum_logger.logMessage("errorbar(x(:,1),x(:,14),x(:,15))");
        AdvancedSimulation.o_sum_logger.logMessage("title('Precision')");
        AdvancedSimulation.o_sum_logger.logMessage("axis([0 "+x_internal_loop+" 0 1])");
        AdvancedSimulation.o_sum_logger.logMessage("");  



      Network.timeCheck("End");
      AdvancedSimulation.o_logger.halt();
      AdvancedSimulation.o_log.close();
      AdvancedSimulation.o_sum_logger.halt();
      AdvancedSimulation.o_sum_log.close();
      AdvancedSimulation.o_arch_logger.halt();
      AdvancedSimulation.o_arch_log.close();
      AdvancedSimulation.o_probe_logger.halt();
      AdvancedSimulation.o_probe_log.close();
      try
      {
      AdvancedSimulation.o_search_logger.halt();
      AdvancedSimulation.o_search_log.close();
      }catch(Exception e){}
    
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

}

