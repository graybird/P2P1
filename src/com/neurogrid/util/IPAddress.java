package com.neurogrid.util;

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

import java.util.StringTokenizer;

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

public class IPAddress
{
	private static final String cvsInfo =
		"$Id: IPAddress.java,v 1.2 2003/06/25 11:50:47 samjoseph Exp $";
	/**
	* @return the cvs string
	*/
	public static String getCvsInfo()
	{
		return cvsInfo;
	}

	// private variables

	public static final int NO_BYTES = 4;
	private byte[] o_bytes = new byte[NO_BYTES];

	/**
	 * Create an ip address from a string
	 *
	 * @param p_ip_address  the string to create from
	 *
	 * @throws Exception  a general exception
	 */
	public IPAddress(String p_ip_address) throws Exception
	{
		String[] x_chunk = new String[o_bytes.length];
		StringTokenizer x_tok = new StringTokenizer(p_ip_address, ".");
		for (int i = 0; i < o_bytes.length; i++)
		{
			x_chunk[i] = x_tok.nextToken();
			o_bytes[i] = Byte.parseByte(x_chunk[i]);
		}
	}

	public static final int ZERO = 0;
	public static final int ONE = 0;
	public static final int TWO = 0;
	public static final int THREE = 0;

	/**
	 * Create an ip address from four shorts
	 *
	 * @param p_s1  first byte
	 * @param p_s2  second byte
	 * @param p_s3  third byte
	 * @param p_s4  fourth byte
	 */
	public IPAddress(byte p_s1, byte p_s2, byte p_s3, byte p_s4)
	{
		o_bytes[ZERO] = p_s1;
		o_bytes[ONE] = p_s2;
		o_bytes[TWO] = p_s3;
		o_bytes[THREE] = p_s4;
	}

	/**
	 * test equality
	 * 
	 * @param p_ip_address the ip address we are comparing with
	 *
	 * @return int
	 */
	public boolean equals(IPAddress p_ip_address)
	{
		int x_sum = 0;
		for (int i = 0; i < o_bytes.length; i++)
		{
			if (this.o_bytes[i] == p_ip_address.o_bytes[i])
			{
				x_sum++;
			}
		}
		if (x_sum == o_bytes.length)
		{
			return true;
		}
		return false;
	}

	/**
	 * Increments the rating and hands back the same object
	 *
	 * @return IPAddress
	 */
	public String toString()
	{
		final int x_buffer_size = 32;
		StringBuffer x_buf = new StringBuffer(x_buffer_size);
		for (int i = 0; i < o_bytes.length - 1; i++)
		{
			x_buf.append(Byte.toString(o_bytes[i]));
			x_buf.append(".");
		}
		x_buf.append(Byte.toString(o_bytes[o_bytes.length - 1]));

		return x_buf.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return super.hashCode();
	}

}
