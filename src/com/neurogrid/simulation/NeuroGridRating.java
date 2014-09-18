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

import com.neurogrid.simulation.root.Node;

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

public class NeuroGridRating
{
  private static final String cvsInfo = "$Id: NeuroGridRating.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  // private variables

  private Node o_node = null;
  private int o_rating = 0;

 /**
  * Compares its two arguments for order.
  *
  * @param p_node
  * @param p_rating
  */
  public NeuroGridRating(Node p_node, int p_rating)//long p_key)
  {
    o_node = p_node;
    o_rating = p_rating;
  }

 /**
  * Returns the rating
  *
  * @return int
  */
  public int intValue()
  {
    return o_rating;
  }

 /**
  * Increments the rating and hands back the same object
  *
  * @return NeuroGridRating
  */
  public NeuroGridRating increment()
  {
    o_rating++;
    return this;
  }

 /**
  * Increments the rating by a certain amount and hands back the same object
  *
  * @return NeuroGridRating
  */
  public NeuroGridRating add(int p_increment)
  {
    o_rating = o_rating + p_increment;
    return this;
  }


 /**
  * Returns the node
  *
  * @return Node
  */
  public Node getNode()
  {
    return o_node;
  }



}

