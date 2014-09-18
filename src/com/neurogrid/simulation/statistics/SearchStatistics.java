package com.neurogrid.simulation.statistics;

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
 * Statistics<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   13/Jul/2000    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 *
 */

public class SearchStatistics
{
	public static final String cvsInfo =
		"$Id: SearchStatistics.java,v 1.2 2003/06/25 11:50:44 samjoseph Exp $";
	/**
	* @return the cvs string
	*/
	private static String getCvsInfo()
	{
		return cvsInfo;
	}

	private int o_no_matches = -1;
	private int o_no_false_matches = -1;
	private int o_TTL_of_first_match = -10;
	private int o_message_transfers = -1;
	private int o_no_active_nodes = -1;
	private int o_possible_targets = -1;

	/**
	* Constructor  
	*/
	public SearchStatistics()
	{
	}

	/**
	* Set the no of matches on this search run
	*
	* @param p_no_matches  the number of matches
	*/
	public void setNoMatches(int p_no_matches)
	{
		o_no_matches = p_no_matches;
	}

	/**
	* Set the no of false matches on this search run
	*
	* @param p_no_false_matches  the no of false matches
	*/
	public void setNoFalseMatches(int p_no_false_matches)
	{
		o_no_false_matches = p_no_false_matches;
	}

	/**
	* Set the TTL of the first match on this search run
	*
	* @param p_TTL_of_first_match   the TTL of the first match
	*/
	public void setTTLOfFirstMatch(int p_TTL_of_first_match)
	{
		o_TTL_of_first_match = p_TTL_of_first_match;
	}

	/**
	* Set the no of message transfers on this search run
	*
	* @param p_message_transfers  the number of message transfers
	*/
	public void setNoMessageTransfers(int p_message_transfers)
	{
		o_message_transfers = p_message_transfers;
	}

	/**
	* Set the no of activated nodes on this search run
	*
	* @param p_no_active_nodes  the number of active nodes
	*/
	public void setNoActivatedNodes(int p_no_active_nodes)
	{
		o_no_active_nodes = p_no_active_nodes;
	}

	/**
	* Set the no of possible targets on this search run
	*
	* @param p_possible_targets   the number of possible targets
	*/
	public void setNoPossibleTargets(int p_possible_targets)
	{
		o_possible_targets = p_possible_targets;
	}

	/**
	* Get the no of matches on this search run
	*
	* @return int   the number of matches
	*/
	public int getNoMatches()
	{
		return o_no_matches;
	}

	/**
	* Get the no of false matches on this search run
	*
	* @return  int   the number of false matches
	*/
	public int getNoFalseMatches()
	{
		return o_no_false_matches;
	}

	/**
	* Get the TTL of the first match on this search run
	*
	* @return int   the ttl of the first match
	*/
	public int getTTLOfFirstMatch()
	{
		return o_TTL_of_first_match;
	}

	/**
	* Get the no of message transfers on this search run
	*
	* @return  int    the number of message transfers
	*/
	public int getNoMessageTransfers()
	{
		return o_message_transfers;
	}

	/**
	* Get the no of activated nodes on this search run
	*
	* @return  int  the number of activated nodes 
	*/
	public int getNoActivatedNodes()
	{
		return o_no_active_nodes;
	}

	/**
	* Get the no of possible targets on this search run
	*
	* @return int   the number of possible targets
	*/
	public int getNoPossibleTargets()
	{
		return o_possible_targets;
	}

}
