/**
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

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * Semaphore class<br><br>
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


public class Semaphore
{

  public static final String cvsInfo = "$Id: Semaphore.java,v 1.1 2003/03/29 10:57:43 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }
  /**
   * Create a new Semaphote object
   */
  public Semaphore()
  {
  }


  /**
   * Wait on the semaphore being signalled
   *
   * @throws InterruptedException    if the wait gets interrupted by a stop() call
   */
  public synchronized void semWait() throws InterruptedException
  {
    wait();
  }


  /**
   * Notify the semaphore
   */
  public synchronized void semSignal()
  {
    notifyAll();
  }
}