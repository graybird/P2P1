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

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * Log Interface<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   4/May 2000    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 * @seealso BasicFileLog
 * @seealso Logger
 * @seealso java.text.SimpleDateFormat
 */



public interface Log
{
  /**
   * A log needs to be able to write a log message and that's about it really. Any
   * rollover / timestamp or general bells and whistles can be added by the implementor
   * of the log.
   *
   * @param p_msg          The message to log
   */
  public void logMessage( Object p_msg);
  
  /**
   * A log also needs to flush itself to disk properly so we can buffer it in memory
   * whilst the logger thread commits a whole bunch of changes.
   */
  public void flush();

}