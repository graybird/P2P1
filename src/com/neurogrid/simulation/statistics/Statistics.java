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

package com.neurogrid.simulation.statistics;

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

public class Statistics
{
	public static final String cvsInfo =
		"$Id: Statistics.java,v 1.2 2003/06/25 11:50:44 samjoseph Exp $";
	/**
	 * @return the cvs string
	 */
	public static String getCvsInfo()
	{
		return cvsInfo;
	}

	/**
	* The target to calculate the statistics.
	*/
	protected double[] o_target;

	/**
	* The number of data.
	*/
	protected int o_data_count = 0;

	/**
	* The minimum value;
	*/
	protected double o_minimum = 0.0D;

	/**
	* The maximum value;
	*/
	protected double o_maximum = 0.0D;

	/**
	* The average;
	*/
	protected double o_average = 0.0D;

	/**
	* The standard deviation.
	*/
	protected double o_deviation = 0.0D;

	/**
	* The minimum value to use in calculation.
	*/
	protected Double o_minimum_limit;

	/**
	* The maximum value to use in calculation.
	*/
	protected Double o_maximum_limit;

	/**
	 * Constructs a <code>Statistics</code> with a 
	 * <code>Statisticable</code>.
	 * 
	 * @param p_target the target to calculate the statistics.
	 */
	public Statistics(double[] p_target)
	{
		o_target = p_target;
	}

	/**
	 * Sets the minimum limit of value to use in calculation.
	 * 
	 * @param p_value the minimum limit.
	 */
	public void setMinimumLimit(double p_value)
	{
		o_minimum_limit = new Double(p_value);
	}

	/**
	 * Sets the maximum limit of value to use in calculation.
	 * 
	 * @param p_value the maximum limit.
	 */
	public void setMaximumLimit(double p_value)
	{
		o_maximum_limit = new Double(p_value);
	}

	/**
	 * Calculates the statistics of the target.
	 */
	public void calculate()
	{
		o_data_count = 0;
		o_minimum = 0.0;
		o_maximum = 0.0;
		o_average = 0.0;
		o_deviation = 0.0;

		if (o_target != null)
		{
			try
			{
				o_data_count = 0;

				double x_amount = 0.0;
				double x_amount2 = 0.0;

				for (int i = 0; i < o_target.length; i++)
				{
					double x_value = o_target[i];

					boolean x_valid = true;
					if (o_minimum_limit != null
						&& o_minimum_limit.doubleValue() > x_value)
					{
						x_valid = false;
					}

					if (o_maximum_limit != null
						&& o_maximum_limit.doubleValue() < x_value)
					{
						x_valid = false;
					}

					if (x_valid)
					{
						if (o_data_count == 0)
						{
							o_minimum = x_value;
							o_maximum = x_value;
						}
						else
						{
							if (o_minimum > x_value)
							{
								o_minimum = x_value;
							}

							if (o_maximum < x_value)
							{
								o_maximum = x_value;
							}

						}
						x_amount += x_value;
						x_amount2 += x_value * x_value;

						o_data_count++;
					}
				}

				if (o_data_count > 0)
				{
					o_average = x_amount / (double) o_data_count;
					o_deviation =
						Math.sqrt(
							x_amount2 / (double) o_data_count
								- o_average * o_average);
				}
			}
			catch (IndexOutOfBoundsException x_exception)
			{
				x_exception.printStackTrace();
			}
		}
	}

	/**
	 * Gets the minimum value.
	 * @return the minimum value.
	 */
	public double getMin()
	{
		return o_minimum;
	}

	/**
	 * Gets the maximum value.
	 * @return the maximum value.
	 */
	public double getMax()
	{
		return o_maximum;
	}

	/**
	 * Gets the average value.
	 * @return the average value.
	 */
	public double getAverage()
	{
		return o_average;
	}

	/**
	 * Gets the standard deviation value.
	 * @return the standard deviation value.
	 */
	public double getDeviation()
	{
		return o_deviation;
	}

	/**
	 * Gets the variance value.
	 * @return the variance value.
	 */
	public double getVariance()
	{
		return o_deviation * o_deviation;
	}

	/**
	 * Returns a string representation of the state of this object.
	 * @return a string representation of the state of this object.
	 */
	public String getOutputString()
	{
		return "data_count="
			+ o_data_count
			+ ",minimum="
			+ o_minimum
			+ ",maximum="
			+ o_maximum
			+ ",average="
			+ o_average
			+ ",deviation="
			+ o_deviation;
	}

	/**
	 * Returns a raw string representation of the state of this object,
	 * for debugging use. It should be invoked from <code>toString</code>
	 * method of the subclasses.
	 * @return a string representation of the state of this object.
	 */
	protected String paramString()
	{
		return getOutputString();
	}

	/**
	 * Returns a string representation of the state of this object,
	 * for debugging use.
	 * @return a string representation of the state of this object.
	 */
	public String toString()
	{
		return getClass().getName() + "[" + paramString() + "]";
	}
}
