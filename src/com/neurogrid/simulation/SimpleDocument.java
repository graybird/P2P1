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
 

import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;

import com.neurogrid.simulation.root.Keyword;


  
 /**
 * Copyright (C) 2000 NeuroGrid <sam@neurogrid.com><br><br>
 *
 * The SimpleDocument extends the base document class with a number of constructors
 * that allow the document to be created with a specified number of newly created 
 * keywords or a group of particular keywords.<br><br>
 *
 * Change History<br>
 * ------------------------------------------------------------------------------------<br>
 * 0.0   6/Oct/2001    sam       Created file<br>
 *
 * @author Sam Joseph (sam@neurogrid.com)
 */

public class SimpleDocument extends com.neurogrid.simulation.root.Document 
{
  public static final String cvsInfo = "$Id: SimpleDocument.java,v 1.2 2003/06/25 11:50:46 samjoseph Exp $";
  public static String getCvsInfo()
  {
    return cvsInfo;
  }

  private static Category o_cat = Category.getInstance(SimpleDocument.class.getName());  

  /**
   * initialize the logging system
   *
   * @param p_conf      configuration filename
   */
  public static void init(String p_conf)
  {
    BasicConfigurator.configure(); 
    PropertyConfigurator.configure(p_conf);  	
    o_cat.info("SimpleDocument logging Initialized");
  }
  
  // private variables

  public static DecimalFormat o_df = new DecimalFormat("######");

  static
  {
    o_df.setMinimumIntegerDigits(6);
  }

  // these can be static because we don't store the global document id as a long as 
  // part of the document object - although we might want to in order to create a zipf 
  // distribution of documents over nodes ...
  /*
  private static String getNewDocumentID()
  {
    StringBuffer x_buf = new StringBuffer(10);
    x_buf.append("DOC_");
    x_buf.append(o_df.format(++o_global_document_id));
    return x_buf.toString();
  }
*/
  /**
   * Get a new key, incrementing along the static global keyword id to make sure this
   * is a unique key
   *
   * @return long
   */
  private static long getNewKey()
  {
    return ++o_global_document_id;
  }

  /**
   * Get a new document id formatted according to the DOC_###### style
   *
   * @return String
   */
  private String getNewDocumentID()
  {
    o_key = getNewKey();
    return getFormattedDocumentID(o_key);
  }

  /*
  private static String getNewDocumentID(long p_key)
  {
    StringBuffer x_buf = new StringBuffer(10);
    x_buf.append("DOC_");
    x_buf.append(o_df.format(p_key));
    return x_buf.toString();
  }*/

  private static final String o_id_head = "DOC_";
  private static StringBuffer o_buf = new StringBuffer(10);
  static
  {
    o_buf.append(o_id_head);
  }

  
   /**
   * Get a document id formatted according to the DOC_###### style
   *
   * @return String
   */
  public static String getFormattedDocumentID(long p_key)
  {
    o_buf.delete(o_id_head.length(),o_buf.length());
    o_buf.append(o_df.format(p_key));
    return o_buf.toString();
  }

  /**
   * SimpleDocument Constructor - create document with specified keywords
   *
   * Creates a new document with a new document id of the format DOC_######
   * using the global document id to generate the ###### number in the document id string.
   * The global document id is a static variable which is incremented each time a new document is created 
   * making sure that each document had a unique id.
   * The document is given a set of keywords and the document is referenced against each keyword
   * via a multihashtable - i.e. a hashtable that will store multiple elements against each key,
   * the static variable o_documents_by_keyword.
   * The document id string is linked to the actual document object through another hashtable,
   * the static variable o_documents.
   *
   * @param p_keywords   the keywords to associate with this document
   */
  public SimpleDocument(Keyword[] p_keywords)
    throws Exception
  {
    if(p_keywords == null || p_keywords.length < 1) throw new Exception("SimpleDocument Constructor: must include at least one keyword");
    o_document_ID = getNewDocumentID();
    o_keywords = p_keywords;
    //check(o_document_ID,"o_document_ID");
    //check(p_keywords,"p_keywords");
    for(int i=0;i<p_keywords.length;i++)
    {
      if(o_keywords[i] == null) throw new Exception("SimpleDocument Constructor: keyword [" + i + "] null");
      //check(o_keywords[i],"o_keywords[i]");
      o_documents_by_keyword.put(o_keywords[i],this);
      //System.out.println(o_keywords[i].toString());
    }
    o_documents.put(o_document_ID,this);
    o_document_ids.put(new Long(o_global_document_id),o_document_ID);
  }

  /**
   * SimpleDocument Constructor - create document with specified keywords
   *
   * Creates a new document with a new document id of the format DOC_######
   * using the global document id to generate the ###### number in the document id string.
   * The global document id is a static variable which is incremented each time a new document is created 
   * making sure that each document had a unique id.
   * The document is given a vector of keywords and the document is referenced against each keyword
   * via a multihashtable - i.e. a hashtable that will store multiple elements against each key,
   * the static variable o_documents_by_keyword.
   * The document id string is linked to the actual document object through another hashtable,
   * the static variable o_documents.
   *
   * @param p_keywords   the vector of keywords to associate with this document
   */
  public SimpleDocument(Vector p_keywords)
    throws Exception
  {
    if(p_keywords == null || p_keywords.size() < 1) throw new Exception("SimpleDocument Constructor: must include at least one keyword");

    o_document_ID = getNewDocumentID();
    Keyword x_keyword = null;
    String x_tmp = null;
    o_keywords = new Keyword[p_keywords.size()];
    //check(o_document_ID,"Ao_document_ID");
    //check(p_keywords,"Ap_keywords");
    for(int i=0;i<p_keywords.size();i++)
    {
      x_tmp = (String)(p_keywords.elementAt(i));
      if(x_tmp == null) throw new Exception("SimpleDocument Constructor: keyword [" + i + "] null");
      x_keyword = (Keyword)(Keyword.o_keywords.get(x_tmp));
      o_keywords[i] = x_keyword;
      //check(x_keyword,"Ax_keyword");
      o_documents_by_keyword.put(x_keyword,this);
    }
    o_documents.put(o_document_ID,this);
    o_document_ids.put(new Long(o_global_document_id),o_document_ID);
  }

  /**
   * SimpleDocument Constructor - create document with specified number of keywords
   *
   * Creates a new document with a new document id of the format DOC_######
   * using the global document id to generate the ###### number in the document id string.
   * The global document id is a static variable which is incremented each time a new document is created 
   * making sure that each document had a unique id.
   * A number of keywords, equal to the number specified, are created and the document is referenced against 
   * each keyword via a multihashtable - i.e. a hashtable that will store multiple elements against each key,
   * the static variable o_documents_by_keyword.
   * The document id string is linked to the actual document object through another hashtable,
   * the static variable o_documents.
   *
   * @param p_no_keywords  the number of keywords to associate with this document
   */
  public SimpleDocument(int p_no_keywords)
    throws Exception
  {
    if(p_no_keywords < 1) throw new Exception("SimpleDocument Constructor: must include at least one keyword");
    o_document_ID = getNewDocumentID();
    check(o_document_ID,"o_document_ID");
    o_keywords = new Keyword[p_no_keywords];
    for(int i=0;i<p_no_keywords;i++)
    {
      o_keywords[i] = new SimpleKeyword();
      o_documents_by_keyword.put(o_keywords[i],this);
    }
    o_documents.put(o_document_ID,this);
    o_document_ids.put(new Long(o_global_document_id),o_document_ID);
  }

  /**
   * Get String representation
   *
   * @return String
   */
  public String toString()
  {
    return o_document_ID;
  }
  
  /**
   * test main 
   *
   */
   public static void main(String[] args)
   {
     try{
     SimpleKeyword[] x_keywords = new SimpleKeyword[10];
    
     for(int i=0;i<10;i++)
     {
       x_keywords[i] = new SimpleKeyword();	
     }	
     
     SimpleDocument x_doc = new SimpleDocument(x_keywords);
     System.out.println(x_doc.toString() + " : " + x_doc.getDocumentID());
     Keyword[] x_keys = x_doc.getKeywords();
     for(int i=0;i<x_keys.length;i++)
     {
       System.out.println(x_keys[i].toString() + " : " + x_keys[i].getKeywordID() + " : " + x_keys[i].getKey());
     }	

     System.out.println("Random Keyword: " + x_doc.getRandomKeyword());   

     Hashtable x_exclude = new Hashtable();
     x_exclude.put(x_keywords[1],x_keywords[1]);

     System.out.println("Randomg Keyword that is not " + x_keywords[1].getKeywordID() + ": " + x_doc.getRandomKeyword(x_exclude));    
     System.out.println("Randomg Keyword that is not " + x_keywords[1].getKeywordID() + ": " + x_doc.getRandomKeyword(x_exclude));    
     System.out.println("Randomg Keyword that is not " + x_keywords[1].getKeywordID() + ": " + x_doc.getRandomKeyword(x_exclude));    
     System.out.println("Randomg Keyword that is not " + x_keywords[1].getKeywordID() + ": " + x_doc.getRandomKeyword(x_exclude));    
     System.out.println("Randomg Keyword that is not " + x_keywords[1].getKeywordID() + ": " + x_doc.getRandomKeyword(x_exclude));    
     System.out.println("Randomg Keyword that is not " + x_keywords[1].getKeywordID() + ": " + x_doc.getRandomKeyword(x_exclude));    
 

     }catch(Exception e)
     {e.printStackTrace();}
   }
}

