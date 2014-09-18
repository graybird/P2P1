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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * Basic File Logging class<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   4/May 2000    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 * @seealso Log
 * @seealso Logger
 * @seealso java.text.SimpleDateFormat
 */

public class BasicFileLog extends Object implements Log
{
  public static final String cvsInfo = "$Id: BasicFileLog.java,v 1.2 2003/06/25 11:50:44 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  /*
   * Instance variables
   */
  protected SimpleDateFormat		o_format;         // date formatter
  protected boolean			o_timeStamp;      // time stamp set?
  protected PrintWriter			o_writer;         // output print writer
  private String			o_logFile;        // log file name



  /**
   * Create a BasicFileLog.
   *
   * @param p_logFile         The log file name
   * @param p_timeStamp       true means timestamp entries or false to not
   * @param p_timeFormat      The time formatting string
   *
   * @throws IOException    If there was a problem opening the file
   */
  public BasicFileLog( String p_logFile, boolean p_timeStamp, String p_timeFormat)
    throws IOException
  {

    if(Logger.o_verbosity > 0)
    {
      System.out.println("p_logFile: " + p_logFile);
      System.out.println("p_timeStamp: " + p_timeStamp);
      System.out.println("p_timeFormat: " + p_timeFormat);
    }
    /*
     * Setup the timestamp object. Only create this once to save garbage collection and
     * because it is one of the heavier weight objects in the JDK
     */
    o_timeStamp = p_timeStamp;

    if (p_timeStamp)
    {
      o_format = new SimpleDateFormat( p_timeFormat);
    }

    /*
     * Attempt to create the log file or seek until the end of it
     */
    o_logFile = p_logFile;
    createWriter();
  }


  /**
   * Create a BasicFileLog. Using this constructor means that no timestamping will be done.
   *
   * @param p_logFile          The log file name.
   *
   * @throws IOException     If there was a problem opening the file
   */
  public BasicFileLog( String p_logFile) throws IOException
  {
    this( p_logFile, false, "");
  }


  /**
   * Internal method for this object and its descendants to create the writer. Note
   * that if the writer exists (in the cases where we are going to do rollovers then it
   * is fairly sociable to close it first. This method also seeks to the end of any
   * existing log files
   *
   * @throws IOException    Reports any problems in opening the file
   */
  protected void createWriter() throws IOException
  {

    /*
     * Close the writer if it already exists
     */
    if (o_writer != null)
    {
      o_writer.close();
    }

    /*
     * Seek until the end of the file and create the writer object
     */
    RandomAccessFile raf = new RandomAccessFile( o_logFile, "rw");
    o_writer = new PrintWriter( new FileWriter( raf.getFD()));
    raf.seek( raf.length());
  }


  /**
   * Log a message to the log file we have honouring any timestampting along the way.
   *
   * @param p_msg         The log message to write
   */
  public synchronized void logMessage( Object p_msg)
  {
    try
    {
    

      if (o_timeStamp)
      {
        Date now = new Date();
        o_writer.print( o_format.format( now));
        o_writer.print( ": ");
      }


      if (! (p_msg instanceof Exception) )
      {
        if(Logger.o_verbosity > 0) System.out.println("printing message: " + p_msg);
        o_writer.println( p_msg.toString());

      }
      else
      {
        if(Logger.o_verbosity > 0) System.out.println("printing exception: " + p_msg);
        o_writer.println( p_msg.toString());
        ((Exception) p_msg).printStackTrace( o_writer);
      }
    }
    catch(Exception e)
    {e.printStackTrace();}

  }


  /**
   * Flush the output stream so that log entries are written to disk properly.
   */
  public void close()
  {
    o_writer.close();
  }

  /**
   * Flush the output stream so that log entries are written to disk properly.
   */
  public void flush()
  {
    o_writer.flush();
  }
  
  public static void main(String[] args)
  {
  	try{
    BasicFileLog   o_log = new BasicFileLog("test.log",false,"");              // file log
    Logger         o_logger = new Logger(o_log);      // logger object for writing to file log
    Logger.o_no_thread = true;
    o_logger.logMessage("Start Logging");

	}catch(Exception e){e.printStackTrace();}
  }
}