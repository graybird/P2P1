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

import java.util.Comparator;

 /**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This class is the comparison operator required for NeuroGrid search.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   13/Feb/2002    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class NeuroGridComparator implements Comparator
{
  private static final String cvsInfo = "$Id: NeuroGridComparator.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  // private variables

  //private long o_key;

 /**
  * Compares its two arguments for order.
  *
  * @param p_key
  */
  public NeuroGridComparator()//long p_key)
  {
      //o_key = p_key;
  }

 /**
  * Compares its two arguments for order.
  *
  * @param p_o1
  * @param p_o2
  *
  * @return int
  */
  public int compare(Object p_o1, Object p_o2)
  {
    int x_key1 = ((NeuroGridRating)(p_o1)).intValue();
    int x_key2 = ((NeuroGridRating)(p_o2)).intValue();

    if(x_key1 == x_key2)
      return 1;
    else if(x_key1 < x_key2)
      return 1;
    else
      return -1;


  }

 /**
  * Indicates whether some other object is "equal to" this Comparator
  *
  * @param p_obj
  *
  * @return boolean
  */
  public boolean equals(Object p_obj)
  {
    return (p_obj == this);
  }


}

