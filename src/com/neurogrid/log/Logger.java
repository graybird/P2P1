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

package com.neurogrid.log;

import java.util.Vector;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * Logger class<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   4/May 2000    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 * @seealso Log
 * @seealso BasicFileLog
 * @seealso java.text.SimpleDateFormat
 */

public final class Logger
             implements Runnable
{
  public static final String cvsInfo = "$Id: Logger.java,v 1.1 2003/03/29 10:57:43 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  /*
   * Instance variables
   */
  private Vector			o_messages;     // buffer for messages
  private Thread			o_thread;       // thread for excecution
  private Log				o_log;          // log to write to
  private boolean			o_running;      // run state
  private Semaphore			o_sem;		// semaphore
  
  public static int o_verbosity = 10;
  public static boolean o_no_thread = false;
  
  /**
   * Create a new Logger by passing it an instance of Log.
   *
   * @param p_log        The log to log messages to
   */
  public Logger( Log p_log)
  {
    if(o_verbosity > 0) System.out.println("creating new logger");
    o_messages = new Vector(100);
    o_log = p_log;
    o_thread = new Thread( this);
    o_running = false;
    o_sem = new Semaphore();
    o_thread.start();
    if(o_verbosity > 0) System.out.println("starting logger thread");
  }


  /**
   * Log a message String to the Logger. This queues up under load and batches
   * the log writes off to wherever using a Thread.
   *
   * @param p_msg          The message to log
   */
  public synchronized void logMessage( Object p_msg)
  {

    /*
     * Write the message to our in memory staging area
     */
    o_messages.addElement( p_msg);
    if(o_verbosity > 0) System.out.println("add element to message list: " + p_msg.toString());
    /*
     * If the thread is not currently in process then start it off
     */
    o_sem.semSignal();
    if(o_no_thread == true)
      forceLog();
  }

  public void forceLog()
  {
      while (o_messages.size() > 0)
      {
      	if(o_verbosity > 0) System.out.println("logger writing message to log ...");
        o_log.logMessage( (Object) o_messages.elementAt(0));
        o_messages.removeElementAt(0);
        o_log.flush();
      }
  }
  
  /**
   * Implement the run() method required by the Runnable interface so that we are
   * able to be called by a Thread. This method goes through the buffer and attempts
   * to write all elements out to the log file.
   */
  public void run()
  {
    if(o_verbosity > 0) System.out.println("logger thread running ...");
    o_running = true;

    while (o_running)
    {
      while (o_messages.size() > 0)
      {
      	if(o_verbosity > 0) System.out.println("logger writing message to log ...");
        o_log.logMessage( (Object) o_messages.elementAt(0));
        o_messages.removeElementAt(0);
        o_log.flush();
      }

      try
      {
        if(o_verbosity > 0) System.out.println("logger thread waiting ...");
      	o_sem.semWait();
      }
      catch (InterruptedException intex)
      {
      	o_running=false;
      }
    }

    o_running = false;
    System.out.println("logger thread exiting");
  }


  /**
   * Method to halt the execution of the logging thread. This unblocks the thread and
   * then sets the run state to false
   */
  public void halt()
  {
    System.out.println("logger halt");
    o_running = false;
    o_sem.semSignal();
  }
}


