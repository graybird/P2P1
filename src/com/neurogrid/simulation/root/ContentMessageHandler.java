package com.neurogrid.simulation.root;

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

 
 /**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This interface defines functionality for message handling objects.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   17/Mar/2003    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public interface ContentMessageHandler extends MessageHandler
{
	
  // currently assuming all messages are searches ... although
  // creating different types of message implementation would rise above that

  // if this was abstract we could include partial implementations that
  // pointed to abstract methods ...
  // something like  checkLocalContents
  //                 forwardMessage
  // ????
 
  /**
   * handle a message arriving at a particular node
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate a successful operation
   */
  public boolean handleMessage(ContentMessage p_message, Node p_node)
    throws Exception;

  /**
   * inject a message into a particular node
   *
   * @param p_message    the incoming message
   * @param p_node       the node doing the processing
   *
   * @return boolean     flag to indicate a successful operation
   */
  public boolean injectMessage(ContentMessage p_message, Node p_node)
    throws Exception;


}

