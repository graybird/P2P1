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

package com.neurogrid.parser;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * This is a variant of a hashtable that works with objects and stores collisions in
 * an arrays within the hashtable, which also places limits on the number of
 * entries stored.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   13/Jul/2000    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 *
 * see also Referenced classes of package java.util:
 *            Dictionary
 */

public class MultiHashtable implements Serializable, Map
{
  public static final String cvsInfo = "$Id: MultiHashtable.java,v 1.2 2003/06/25 11:50:47 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  /*
   * Instance variables
   */

  private Map  o_hashtable = null;
  
  
  
  /**
   * Creates a new multihashtable based on a LinkedHashMap instance with the specified 
   * initial capacity, load factor and ordering mode.
   *
   * @param p_initialCapacity
   * @param p_loadFactor
   * @param p_accessOrder
   */
   public MultiHashtable(int p_initialCapacity, float p_loadFactor, boolean p_accessOrder) 
   {
     o_hashtable = new LinkedHashMap(p_initialCapacity,p_loadFactor,p_accessOrder);
    //super(p_initialCapacity,p_loadFactor);
   } 
 
   private int MAX_ENTRIES = 10;

   
   public class LimitedLinkedHashMap extends LinkedHashMap
   {

     public LimitedLinkedHashMap(int p_initialCapacity, float p_loadFactor, boolean p_accessOrder)
     {
     	super(p_initialCapacity,p_loadFactor,p_accessOrder);   
     }

     protected boolean removeEldestEntry(Map.Entry eldest) 
     {
       boolean x_remove = this.totalSize() > MAX_ENTRIES;
       //System.out.println("Checking whether to remove eldest entry: " + x_remove);
       return x_remove;
     }
                         
     /**
      * get the sum of the sizes of all the vectors
      *
      * @return int        the size
      */
     public synchronized int totalSize()
     {
       Iterator x_iter = this.entrySet().iterator();
       
       // guess I will have to keep track of the size myself ...
       // would be better to update each time I add rather
       // than calculating each time ...

       Map.Entry x_entry = null;
       Vector x_vector = null;
       int sum = 0;
       while(x_iter.hasNext())
       {
         x_entry = (Map.Entry)(x_iter.next());
         x_vector = (Vector)(x_entry.getValue());
         sum += x_vector.size();
       }
       //System.out.println("Checking total size: " + sum);
       return sum;
     }   
     
   }	         
   
  /**
   * Creates a new multihashtable based on a LimitedLinkedHashMap instance with the specified 
   * initial capacity, load factor and ordering mode.  The max capacity is set in this class
   * and refered to by the inner class
   *
   * @param p_initialCapacity
   * @param p_loadFactor
   * @param p_accessOrder
   */
   public MultiHashtable(int p_initialCapacity, float p_loadFactor, boolean p_accessOrder,
                         int p_max_capacity) 
   {
   	
     MAX_ENTRIES = p_max_capacity;	
     //System.out.println("MAX_ENTRIES: " +MAX_ENTRIES);	
     o_hashtable = new LimitedLinkedHashMap(p_initialCapacity,p_loadFactor,p_accessOrder);   
                 
   }          
 

  /**
   * Create a new multihashtable
   *
   * @param p_initialCapacity
   * @param p_loadFactor
   */
  public MultiHashtable(int p_initialCapacity, float p_loadFactor)
  {
    o_hashtable = new Hashtable(p_initialCapacity,p_loadFactor);
    //super(p_initialCapacity,p_loadFactor);
  }

  /**
   * Create a new default size multihashtable
   *
   */
  public MultiHashtable(Hashtable p_hashtable)
  {
    o_hashtable = p_hashtable;
  }

  /**
   * Create a new default size multihashtable
   *
   */
  public MultiHashtable()
  {
    o_hashtable = new Hashtable();
  }

  /**
   * Create a specified size multihashtable with default expansion
   *
   */
  public MultiHashtable(int p_initialCapacity)
  {
    o_hashtable = new Hashtable(p_initialCapacity);
  }

  /**
   * Associates the specified value with the specified key in this map
   * in addition to any existing association. 
   *
   * @param p_key          the key
   * @param p_value        the value to associate it with
   */
  public Object put(Object p_key, Object p_value)
  {
    Vector x_value_vector = null;
    if(o_hashtable.containsKey(p_key) == false)
    {
      x_value_vector = new Vector();
      x_value_vector.addElement(p_value);
      //System.out.println("Adding first entry for key " + p_key);
      o_hashtable.put(p_key,x_value_vector);
    }
    else
    {
      x_value_vector = (Vector)(o_hashtable.get(p_key));
      x_value_vector.addElement(p_value);
      //System.out.println("Adding subsequent entry for key " + p_key);
      // this is necessary in order to make sure that the size is checked ...
      o_hashtable.remove(p_key);
      o_hashtable.put(p_key,x_value_vector);
    }

    return null;
  }
  
  /**
   * Associates the specified value with the specified key in this map
   * replacing any existing association, unless the p_replace flag
   * is set to true, in which case it will remove an existing association
   * if there is only one .... FIXXXXXXXXXXXXXXXXXXXXx
   *
   * @param p_key          the key
   * @param p_value        the value to associate it with
   */
  public Object put(Object p_key, Object p_value, boolean p_replace)
  {
    if(p_replace == false)
      put(p_key,p_value);
    else
    {
      Vector x_value_vector = null;
      if(o_hashtable.containsKey(p_key) == false)
      {
        x_value_vector = new Vector();
        x_value_vector.addElement(p_value);
        o_hashtable.put(p_key,x_value_vector);
      }
      else
      {
        x_value_vector = (Vector)(o_hashtable.get(p_key));
        if(x_value_vector.size() == 1)
        {
          // this will probably do strange things
          // FIXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
          x_value_vector.remove(0);
          x_value_vector.addElement(p_value);
        }
        else
          x_value_vector.addElement(p_value);
      }
    }
    return null;
  }

  /**
   * replace a value-key pair into the hashtable
   * and defaults to adding the new association
   * if the old one doesn't exist
   *
   * @param p_key              the key
   * @param p_new_value        the value to associate it with
   * @param p_old_value        the value to remove
   */
  public Object replace(Object p_key, Object p_new_value, Object p_old_value)
  {
    Vector x_value_vector = null;
    if(o_hashtable.containsKey(p_key) == false)
    {
      x_value_vector = new Vector();
      x_value_vector.addElement(p_new_value);
      o_hashtable.put(p_key,x_value_vector);
    }
    else
    {
      x_value_vector = (Vector)(o_hashtable.get(p_key));
      if(x_value_vector.contains(p_old_value))
      {
      	// this operation could be slow for a large vector
      	// FIXXXXXXXXXXXXXXXXXXXXXXXXXXX
        x_value_vector.remove(p_old_value);
        x_value_vector.addElement(p_new_value);
      }
      else
      x_value_vector.addElement(p_new_value);
    }
    return null;
  }

  /**
   * Returns the vector to which this map maps the specified key. 
   *
   * @param p_key          the key
   * @return Object        associated Vector of objects
   */
  public Object get(Object p_key)
  {
    return o_hashtable.get(p_key);
  }

  /**
   * Returns a set view of the keys contained in this map.
   *
   * @return Set        the keys
   */
  public Set keySet()
  {
    return o_hashtable.keySet();
  }

 /**
   * Returns the number of key-vector mappings in this map.
   *
   * @return int        the size
   */
  public int size()
  {
    return o_hashtable.size();
  }
  
 /**
   * get the sum of the sizes of all the vectors
   *
   * @return int        the size
   */
  public int totalSize()
  {
    Iterator x_iter = o_hashtable.keySet().iterator();

    Object x_key = null;
    Vector x_vector = null;
    int sum = 0;
    while(x_iter.hasNext())
    {
      x_key = x_iter.next();
      x_vector = (Vector)(o_hashtable.get(x_key));
      sum += x_vector.size();
    }
    return sum;
  }


 /**
   * checks if there is a key matching this object in the hashtable
   *
   * @param p_object
   *
   * @return boolean
   */
  public boolean containsKey(Object p_object)
  {
    return o_hashtable.containsKey(p_object);
  }

 /**
   * checks if there is something matching this object in the hashtable vectors
   *
   * @param p_object
   *
   * @return boolean
   */
  public boolean containsValue(Object p_object)
  {
    boolean x_contains = false;
    Iterator x_iter = o_hashtable.keySet().iterator();
    Vector x_vector = null;
    Object x_key = null;
    while(x_iter.hasNext())
    {
      x_key = (Object)(x_iter.next());
      x_vector = (Vector)(o_hashtable.get(x_key));

      if(x_vector.contains(p_object))
      {
        x_contains = true;
        return x_contains;
      }
    }

    return x_contains;
  }

 /**
   * get a clone
   *
   * @return Object        a cloned version of the multi-hashtable
   */
  public Object clone()
  {
    MultiHashtable x_mht = new MultiHashtable(o_hashtable.size() * 2);

    Iterator x_iter = o_hashtable.keySet().iterator();
    Vector x_vector = null;
    String x_key = null;
    while(x_iter.hasNext())
    {
      x_key = (String)(x_iter.next());
      x_vector = (Vector)(o_hashtable.get(x_key));

      for(int i=0;i<x_vector.size();i++)
        x_mht.put(x_key,((String)(x_vector.elementAt(i))));
    }

    return x_mht;
  }




 /**
   * return a string representation
   *
   * @return String
   */
  public String toString()
  {
    Iterator x_iter = o_hashtable.entrySet().iterator();
    Vector x_vector = null;
    Object x_key = null;
    Map.Entry x_entry = null;
    StringBuffer x_buf = new StringBuffer(100);
    while(x_iter.hasNext())
    {
      x_entry = (Map.Entry)(x_iter.next());
      x_key = (Object)(x_entry.getKey());
      x_vector = (Vector)(x_entry.getValue());
      x_buf.append(x_key.toString());
      x_buf.append(": ");
      for(int i=0;i<x_vector.size();i++)
      {
        x_buf.append(x_vector.elementAt(i));
        x_buf.append(";");
      }
      x_buf.append("\n");
    }

    return x_buf.toString();
  }

 /**
   * write to a print writer
   */
  public void displayContents(PrintWriter p_out, String p_separator, String p_new_line)
  {
    Iterator x_iter = o_hashtable.keySet().iterator();

    String x_key = null;
    Vector x_vector = null;
    while(x_iter.hasNext())
    {
      x_key = (String)(x_iter.next());
      x_vector = (Vector)(o_hashtable.get(x_key));
      for(int i=0;i<x_vector.size();i++)
      {
        p_out.println(x_key + p_separator + (String)(x_vector.elementAt(i)));
        p_out.println(p_new_line);
      }
    }
  }

 /**
   *  Removes all mappings from this map 
   */
  public void clear()
  {
    o_hashtable.clear();
  }

 /**
   * Returns true if this map contains no key-value mappings
   */
  public boolean isEmpty()
  {
    return o_hashtable.isEmpty();
  }

 /**
   * Removes the mapping for this key from this map if it is present 
   * (and thus the vector associated with it)
   */
  public Object remove(Object p_key)
  {
    return o_hashtable.remove(p_key);
  }

 /**
   * remove a particular value from the vector associated with a particular key
   */
  public Object remove(Object p_key,Object p_value)
  {
    Object x_object = null;
    Vector x_vector = (Vector)o_hashtable.get(p_key);
    if(x_vector != null)
    {
      if(x_vector.contains(p_value))
        x_vector.remove(p_value);
      if(x_vector.size() == 0)
        return o_hashtable.remove(p_key);
    }
    return x_object;
  }

 /**
   * Returns a set view of the mappings contained in this map.
   *
   * @return Set
   */
  public Set entrySet() 
  {
    return o_hashtable.entrySet();	
  }        
           
 /**
   * Compares the specified object with this map for equality.
   *
   * @param p_object
   *
   * @return boolean
   */
  public boolean equals(Object p_object) 
  {
    return o_hashtable.equals(p_object);	
  }           

 /**
   * Returns the hash code value for this map. 
   *
   * @return int
   */
  public int hashCode() 
  {
    return o_hashtable.hashCode();	
  }          


 /**
   * Copies all of the mappings from the specified map to this map (optional operation). 
   *
   * @param p_object
   */
  public void putAll(Map p_object) 
  {
    o_hashtable.putAll(p_object);	
  }          


           
 /**
   * Returns a collection view of the values contained in this map. 
   *
   * @return Collection
   */
  public Collection values() 
  {
    return o_hashtable.values();	
  }          


  public static void main(String[] args)
  {
    try
    {
      System.out.println("\nfalse setting ....");	
    	
      MultiHashtable o_knowledge = new MultiHashtable(100,0.75F,false,10);

      o_knowledge.put("FOURTH","FOURTH");
      o_knowledge.put("THIRD","THIRD");	
      o_knowledge.put("SECOND","SECOND");	
      o_knowledge.put("FIRST","FIRST");
       
      System.out.println(o_knowledge);
      
      o_knowledge.get("THIRD");
      
      System.out.println("after access ....");
    	
      System.out.println(o_knowledge);
      
      System.out.println("\ntrue setting ....");
      
      o_knowledge = new MultiHashtable(100,0.75F,true,10);
      
      o_knowledge.put("FIRST","FIRST");
      o_knowledge.put("SECOND","SECOND");	
      o_knowledge.put("THIRD","THIRD");	
      o_knowledge.put("FOURTH","FOURTH");
      
      System.out.println(o_knowledge);
      
      o_knowledge.get("THIRD");
      
      System.out.println("after access ....");
    	
      System.out.println(o_knowledge);
    	
    }
    catch(Exception e)	
    {
      e.printStackTrace();
    }  	
  }


}
