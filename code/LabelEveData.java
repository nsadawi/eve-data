/**
 - To label Eve data resulting from AverageEveData.java
 - input files are csv
 - We are assuming input files will always have the same structure!
 - input files are result of averaging eve data
 - We read the file into a list of list of strings
   - each list of strings represents a line in the csv file
 - We apply the rules 
*/
  import java.io.*;
  import java.util.*;
  import java.util.regex.Matcher;
  import java.util.regex.Pattern;

import java.util.*;
import java.math.*;

import java.util.UUID;

class LabelEveData
{


final static private Pattern splitSearchPattern = Pattern.compile("[\",]");

/**
 * Splits a csv line into a list of strings, 
 * avoids splitting if the comma is in double quotes
 * @param  s a csv text line as a string
 * @return   A list of strings containing all text between commas in the text line
 */ 
private static List<String> splitByCommasNotInQuotes(String s) {
	if (s == null)
		return Collections.emptyList();

	List<String> list = new ArrayList<String>();
	Matcher m = splitSearchPattern.matcher(s);
	int pos = 0;
	boolean quoteMode = false;
	while (m.find())
	{
		String sep = m.group();
		if ("\"".equals(sep))
		{
			quoteMode = !quoteMode;
		}
		else if (!quoteMode && ",".equals(sep))
		{
			int toPos = m.start();
			list.add((s.substring(pos, toPos)).trim());
			pos = m.end();
		}
	}
	if (pos < s.length())
		list.add((s.substring(pos)).trim());
	return list;
}



/**
 * reads column names from csv file (1st line of file) and return as list of strings
 * @param  inFile a file name with full path
 * @return  a list of strings containing file names
 */ 
public static List<String> readColumnNames(String inFile){
	List<String> csvList = new ArrayList<String>(); // to store line after it's split
   	try
		{
			BufferedReader infile = new BufferedReader( new FileReader( inFile ) ); // input1.txt

			String line = infile.readLine(); // 1st line - it's the headers - column names!
			csvList = splitByCommasNotInQuotes(line);
			
			infile.close();			
		}
		catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			System.out.println("EXCEPTION CAUGHT: " + sw.toString() );
			System.exit( 0 );
		}
		return csvList;
   }
   
   
   
   
/**
 * read csv file line by line and return as list of list of strings
 * we can skip 1st line in case it has column names
 * @param  inFile a file name with full path
 * @param  skipFstLine true or not
 * @return  a list of list of strings containing file contents
 */ 
   public static List<List<String>> readFile(String inFile, boolean skipFstLine){
	List<List<String>> csvList = new ArrayList<List<String>>(); // to store lines after they're split
   	try
		{
			BufferedReader infile = new BufferedReader( new FileReader( inFile ) ); // input1.txt

			String line;
			if(skipFstLine)
			   infile.readLine(); // skip 1st line - it's the headers - column names!
			//reading in the infile to the ArrayList
			boolean b = true;
			FileWriter fileWritter;
			BufferedWriter bufferWritter;
			while((line = infile.readLine()) != null)
			{

				List<String> csvPieces = splitByCommasNotInQuotes(line);
				/*
				String[] linePieces = line.split(",");
		    		List<String> csvPieces = new ArrayList<String>(linePieces.length);
		    		for(String piece : linePieces)
		    		{
		    			csvPieces.add(piece);
		    		}
		    		*/
		    		csvList.add(csvPieces);

			}
			infile.close();
			// iterate through the key-value bindings, printing them out

			//for (Map.Entry<String, Integer> kv : freq.entrySet()) {
				//System.out.println("Word: " + kv.getKey() + " - Freq: " + kv.getValue().intValue());
			//}


		}
		catch (Exception e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			System.out.println("EXCEPTION CAUGHT: " + sw.toString() );
			System.exit( 0 );
		}
		return csvList;
   }




public static void saveCSVListToTextFile(List<List<String>> csvList, String outFile) throws IOException {
        PrintWriter writer = new PrintWriter(outFile, "UTF-8"); 
        for(List<String> csv : csvList)
    	{   
    	   writer.print(csv.get(0));
    	   for(int i = 1; i < csv.size(); i ++)
    	   {  		
    	      writer.print(", "+csv.get(i));
    	   }
    	   writer.println("");
   	}   	                   
        writer.close();        
}

/**
 * given a list of list of strings (representing csv text lines), this function returns 
 * values in a certain column as a list of strings
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  id index of column to be retrieved as list of strings
 * @return  a list of strings
 */ 
 public static List<String> getAllIDChemicals(List<List<String>> csvList, int id){
        List<String> values = new ArrayList<String>();
    	for(List<String> csv : csvList)
    	{    		
    	   String s = csv.get(id);
    	   values.add(s);
   	}
    	//System.out.println("Size " + i );
    	return values;
}

/**
 * given a list of list of strings (representing csv text lines), and a list of strings
 * (representing id_chemicals)
 * this method removes entries which have id_chemicals from the main list
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  toRemove list of id_chemicals to be removed
 * @param  id index of id_chemical column
 */ 
 public static void removeElements(List<List<String>> csvList, List<String> toRemove, int chemID){
 	List<List<String>> tmp = new ArrayList<List<String>>();
    	for(List<String> csv : csvList)
    	{    		
    	   String s = csv.get(chemID);
    	   for(String s1 : toRemove){
    	     if(s.equals(s1))
    	        tmp.add(csv);
    	   }    	   
   	}
    	for(List<String> csv : tmp)    	     
   	   csvList.remove(csv);   	   
}


/**
 * given a list of list of strings (representing csv text lines), this function returns 
 * values in a certain column as an array of doubles (assuming that col has double values)
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  id index of column to be retrieved as array of doubles
 * @return  an array of doubles
 */ 
 public static double[] findColValues(List<List<String>> csvList, int id){
        double[] values = new double[csvList.size()];
        int i = 0;
    	for(List<String> csv : csvList)
    	{    		
    	   String s = csv.get(id);
    	   values[i] = Double.parseDouble(s);
    	   i++;
   	}
    	//System.out.println("Size " + i );
    	return values;
}


/**
 * given a list of list of strings (representing csv text lines), this function finds and saves 
 * autofluorescent Compounds
 * it returns a list of autofluorescent compound id's
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  i index of F-Cherry column
 * @param  j index of F-Sapphire column
 * @param  k index of F-Venus column
 * @param  id index of chemical-id column
 * @return a list of autofluorescent compound id's
 */ 
 public static List<String> findAutofluorescentCompounds(List<List<String>> csvList,int chemID, int i, int j, int k){ 
 List<String> autofluorescentCompounds = new ArrayList<String>();               
    	for(List<String> csv : csvList)
    	{    		
    	   double c = Double.parseDouble(csv.get(i));
    	   double s = Double.parseDouble(csv.get(j));
    	   double v = Double.parseDouble(csv.get(k)); 
    	   if(c > 0.08 || s > 0.08 || v > 0.08){    	        
    	        autofluorescentCompounds.add(csv.get(chemID));
    	        //System.out.println("autofluorescent: " +csv.get(chemID));
    	   }
   	}   	
   	return autofluorescentCompounds;   	
}

/**
 * given a list of list of strings (representing csv text lines), this function finds and saves 
 * Potential Hit Compounds (for MCherry, Sapphire or Venus channels)
 * it returns a list of compound id's
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  id index of id_chemical column
 * @param miy index of MIY-Channel column (Channel should be MCherry, Sapphire or Venus)
 * @param dt index of DT-Channel column (Channel should be MCherry, Sapphire or Venus)
 * @return a list of Possibly Active compound ids for that channel
 */ 
 public static List<String> findPossiblyActiveCompounds(List<List<String>> csvList, int chemID, int miy, int dt){
        List<String> possiblyActiveCompounds = new ArrayList<String>();
    	for(List<String> csv : csvList)
    	{    	
    	   double miyc = Double.parseDouble(csv.get(miy));    	       	   
    	   double dtc = Double.parseDouble(csv.get(dt));    	   
    	   
    	   if(miyc > 4.0 || dtc > 1.5){
    	        possiblyActiveCompounds.add(csv.get(chemID));
    	   }    	       	   
   	}   	
   	return possiblyActiveCompounds;   	    	
}

/**
 * given a list of list of strings (representing csv text lines), this function finds and saves 
 * possibly active Compounds (compounds with MIY > 4 OR DT > 1.5)
 * it removes them from the input list and saves them to a csv file
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param miyCherry index of MIY-Cherry column
 * @param miySapphire index of MIY-Sapphire column
 * @param miyVenus index of MIY-Venus column
 * @param dtCherry index of DT-Cherry column
 * @param dtSapphire index of DT-Sapphire column
 * @param dtVenus index of DT-Venus column
 */ 
 /*
 public static void findPossiblyActive(List<List<String>> csvList, int miyCherry, int miySapphire, int miyVenus, int dtCherry, int dtSapphire, int dtVenus){        
        //int i = 0;
        List<List<String>> toRemove = new ArrayList<List<String>>();
    	for(List<String> csv : csvList)
    	{    		
    	   double miyc = Double.parseDouble(csv.get(miyCherry));    	       	   
    	   double dtc = Double.parseDouble(csv.get(dtCherry));    	   
    	   
    	   if(miyc > 4.0 || dtc > 1.5){
    	        //printList(csv);
    	        toRemove.add(csv);
    	   }
   	}
   	for(List<String> csv : toRemove)
    	{    		
    	   csvList.remove(csv);
   	}
   	try{ 	saveCSVListToTextFile(toRemove, "Cherry_possibly_active.csv"); } catch(IOException e){System.out.println("Cannot write to Cherry_possibly_active file!");}
   	toRemove.clear();
   	
   	for(List<String> csv : csvList)
    	{    		
    	   double miys = Double.parseDouble(csv.get(miySapphire));    	  
    	   double dts = Double.parseDouble(csv.get(dtSapphire));
    	   
    	   if(miys > 4.0 || dts > 1.5){
    	        //printList(csv);
    	        toRemove.add(csv);
    	   }
   	}
   	for(List<String> csv : toRemove)
    	{    		
    	   csvList.remove(csv);
   	}
   	try{ 	saveCSVListToTextFile(toRemove, "Sapphire_possibly_active.csv"); } catch(IOException e){System.out.println("Cannot write to Sapphire_possibly_active file!");}
   	toRemove.clear();
   	
   	for(List<String> csv : csvList)
    	{    		    	   
    	   double miyv = Double.parseDouble(csv.get(miyVenus)); 
    	   double dtv = Double.parseDouble(csv.get(dtVenus)); 
    	   
    	   if(miyv > 4.0 || dtv > 1.5){
    	        //printList(csv);
    	        toRemove.add(csv);
    	   }
   	}
   	for(List<String> csv : toRemove)
    	{    		
    	   csvList.remove(csv);
   	}
   	try{ 	saveCSVListToTextFile(toRemove, "Venus_possibly_active.csv"); } catch(IOException e){System.out.println("Cannot write to Venus_possibly_active file!");}
   	toRemove.clear();
    	//System.out.println("Size " + i );
}
*/

/**
 * given a list of list of strings (representing csv text lines), this function finds and saves 
 * Potential Hit Compounds (for MCherry, Sapphire or Venus channels)
 * it returns a list of compound id's
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  id index of id_chemical column
 * @param  i index of HitRatio-Channel column (Channel should be MCherry, Sapphire or Venus)
 * @return a list of Potential Hit compound ids for that channel
 */ 
 public static List<String> findPotentialHitCompounds(List<List<String>> csvList, int chemID, int i){        

        List<String> pHitCompounds = new ArrayList<String>();
    	for(List<String> csv : csvList)
    	{    		
    	   double c = Double.parseDouble(csv.get(i));    	   
    	   if(c < 0.8){    	        
    	        pHitCompounds.add(csv.get(chemID));
    	   }
   	}   	
   	return pHitCompounds;   	    	
}

/**
 * given lists of chem id's of potential hit compounds, this function finds and saves def toxic compounds
 * Potential Hit Compounds (for MCherry, Sapphire or Venus channels)
 * it returns a list of toxic compound id's
 * @param mcherryPHitCompounds List of chem id's of mcherry PHit Compounds 
 * @param sapphirePHitCompounds List of chem id's of sapphire PHit Compounds 
 * @ param venusPHitCompounds List of chem id's of venus PHit Compounds 
 * @return a list of Definitely Toxic compound ids
 */ 
public static List<String> findDefinitelyToxicCompounds(List<String> mcherryPHitCompounds, List<String> sapphirePHitCompounds, List<String> venusPHitCompounds){        
        //find compounds that exist in the three lists
        //loop thru one list and find its elements in the other two!
        List<String> defToxicCompounds = new ArrayList<String>();
        //cherry
    	for(String element : mcherryPHitCompounds)
    	{    		    	      
    	   if(sapphirePHitCompounds.contains(element) && venusPHitCompounds.contains(element)){
    	        
    	        defToxicCompounds.add(element);
    	        //System.out.println("toxic: " +element);
    	   }
   	}   	
   	return defToxicCompounds;   	    	
}

/**
 * given lists of chem id's of potential hit compounds and possibly active, this function finds and saves possibly toxic compounds
 * Potential Hit & Possibly Active Compounds (for MCherry, Sapphire or Venus channels)
 * compounds found in potential hit list2 are given a score of 2 (for each list they're in)
 * compounds found in possibly active lists are given a score of 1 (for each list they're in)
 * it returns a list of possibly toxic compound id's
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  id index of id_chemical column
 * @param mcherryPHitCompounds List of chem id's of mcherry PHit Compounds 
 * @param sapphirePHitCompounds List of chem id's of sapphire PHit Compounds 
 * @param venusPHitCompounds List of chem id's of venus PHit Compounds 
 * @param mcherryPActiveCompounds List of chem id's of mcherry PossiblyActive Compounds 
 * @param sapphirePActiveCompounds List of chem id's of sapphire PossiblyActive Compounds 
 * @param venusPActiveCompounds List of chem id's of venus PossiblyActive Compounds 
 * @return a list of Definitely Toxic compound ids
 */ 
public static List<String> findPossiblyToxicCompounds(List<List<String>> csvList,int chemID,List<String> mcherryPHitCompounds,List<String> sapphirePHitCompounds,List<String> venusPHitCompounds,List<String> mcherryPossiblyActiveCompounds,List<String> sapphirePossiblyActiveCompounds,List<String> venusPossiblyActiveCompounds){

        //loop thru all compounds and find their id's in all lists and give them scores accordingly
        List<String> possiblyToxicCompounds = new ArrayList<String>();
        //cherry
    	for(List<String> csv : csvList)
    	{    		
    	   String element = csv.get(chemID);
    	   int mcPHitScore = 0; if(mcherryPHitCompounds.contains(element))  mcPHitScore = 2;
    	   int sPHitScore  = 0; if(sapphirePHitCompounds.contains(element)) sPHitScore  = 2;
    	   int vPHitScore  = 0; if(venusPHitCompounds.contains(element))    vPHitScore  = 2;
    	   
    	   int mcPActScore = 0; if(mcherryPossiblyActiveCompounds.contains(element))  mcPActScore = 1;
    	   int sPActScore  = 0; if(sapphirePossiblyActiveCompounds.contains(element)) sPActScore = 1;
    	   int vPActScore  = 0; if(venusPossiblyActiveCompounds.contains(element))    vPActScore = 1;
    	   
    	   //now find the overall score
    	   int score = mcPHitScore + sPHitScore + vPHitScore + mcPActScore + sPActScore + vPActScore;
    	   
    	   if(score == 5 || score == 6){    	        
    	        possiblyToxicCompounds.add(element);
    	        //System.out.println("possibly toxic: " +element);
    	   }
   	}   	
   	return possiblyToxicCompounds;   	    	
}


/**
 * given lists of chem id's of possibly active compounds, this function finds and saves inactive compounds
 * possibly active compounds (for MCherry, Sapphire or Venus channels)
 * it returns a list of inactive compound id's (id's non existing in any of the 3 poss active lists)
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  id index of id_chemical column
 * @param mcherryPActiveCompounds List of chem id's of mcherry PossiblyActive Compounds 
 * @param sapphirePActiveCompounds List of chem id's of sapphire PossiblyActive Compounds 
 * @param venusPActiveCompounds List of chem id's of venus PossiblyActive Compounds 
 * @return a list of Inactive compound ids
 */ 
public static List<String> findInactiveCompounds(List<List<String>> csvList, int chemID, List<String> mcPActiveCompounds, List<String> sapPActiveCompounds, List<String> venPActiveCompounds){        
        //find compounds that exist in none of the three lists
        //loop thru the main list and check chem id in the other three!
        List<String> inactiveCompounds = new ArrayList<String>();
        
        for(List<String> csv : csvList)
    	{    		
    	   String element = csv.get(chemID);    	   
    	   if(!(mcPActiveCompounds.contains(element) && sapPActiveCompounds.contains(element) && venPActiveCompounds.contains(element))){
    	        
    	        inactiveCompounds.add(element);
    	        //System.out.println("inactive: " +element);
    	   }
   	}   	    		
   	return inactiveCompounds;   	    	
}

/**
 * Returns the index of a certain column in a list of column names
 * Indices start at 0 
 *
 * @param  columnNames a List<String> of column names
 * @param  columnName the query column name
 * @return      the index of this column
 */  
public static int findColumnID(List<String> columnNames, String columnName) {            
	int i = 0;
        for (String cName : columnNames) {
        	//System.out.println(cName);
        	if(cName.equals(columnName)) return i;
        	i++;
        }	    
        return -1;     
  }
  
//prints all column names
//they're in a list of string
public static void printListOfColNames(List<String> csvList){
   
    if(!csvList.isEmpty())
    {
	System.out.print(csvList.get(0));	
	for(int i=1; i < csvList.size(); i++)
 	   System.out.print("," + csvList.get(i)); 	    	
 	   
	System.out.println();
    }	    		
}


/**
 * given lists of all averaged data, autoF, def toxic, possibly toxic and inactive compounds
 * print out all data records and their corresponding class
 * we use chem_id to look up a chemical in corresponding lists
 * if a compound is not in any list then it's Active
 * possibly active compounds (for MCherry, Sapphire or Venus channels)
 * it returns a list of inactive compound id's (id's non existing in any of the 3 poss active lists)
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  autoFComps autoFComps compounds
 * @param defToxComps defToxComps Compounds 
 * @param possToxComps possToxComps Compounds 
 * @param inactComps inActive Compounds 
 */ 
public static void printLabelledData(List<List<String>> csvList, List<String> autoFComps, List<String> defToxComps, List<String> inactComps, List<String> possToxComps){

    	for(List<String> csv : csvList)
    	{
    		if(!csv.isEmpty())
    		{
    			//get id of compound
    			String id_chem = csv.get(0);
    			System.out.print(id_chem+",");

    			for(int i=1; i < csv.size(); i++)
    			{
    				System.out.print(csv.get(i)+",");
    			}
    			
    			if (autoFComps.contains(id_chem)) {
			    System.out.print("Autofluorescent");
			} else {
			    if (defToxComps.contains(id_chem)) {
			       System.out.print("Definitely Toxic");
			    } else
			       if (inactComps.contains(id_chem)) {
			         System.out.print("Inactive");
			       } else
			          if (possToxComps.contains(id_chem)) {
			            System.out.print("Possibly Toxic");
			          } else			
			             System.out.print("Active");
			}
    		  System.out.println();
    		}    		
    	}
}

/**
 * given lists of all averaged data, autoF, def toxic, possibly toxic and inactive compounds
 * print out data records for compounds which are either active or inactive and their corresponding class
 * we use chem_id to look up a chemical in corresponding lists
 * if a compound is not in any list then it's Active
 * possibly active compounds (for MCherry, Sapphire or Venus channels)
 * it returns a list of inactive compound id's (id's non existing in any of the 3 poss active lists)
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  autoFComps autoFComps compounds
 * @param defToxComps defToxComps Compounds 
 * @param possToxComps possToxComps Compounds 
 * @param inactComps inActive Compounds 
 */ 
public static void printLabelledActiveInactiveData(List<List<String>> csvList, List<String> autoFComps, List<String> defToxComps, List<String> inactComps, List<String> possToxComps){

    	for(List<String> csv : csvList)
    	{
    		if(!csv.isEmpty())
    		{
    			//get id of compound
    			String id_chem = csv.get(0);
    			    			
    			if (inactComps.contains(id_chem)) {
    			       System.out.print(id_chem+",");
    			       for(int i=1; i < csv.size(); i++)
    			       {
    				 System.out.print(csv.get(i)+",");
    			       }
			       System.out.print("Inactive\n");
			} else
			   if (!autoFComps.contains(id_chem) && !defToxComps.contains(id_chem) && !possToxComps.contains(id_chem)){
			       System.out.print(id_chem+",");
    			       for(int i=1; i < csv.size(); i++)
    			       {
    				 System.out.print(csv.get(i)+",");
    			       }
			      System.out.print("Active\n");
			   }			           			    		    			
			
    		  //System.out.println();
    		}    		
    	}
}


public static void printList(List<String> csvList){
       
			    //id's for values we want from csv file (venus, sapphire ..etc)
			    // int e[] = {9,10,11,12,15,16,17,18,21,22,23,24};
	    			   	     
	  	//int e[] = {0,1,2,3,4,9,10,11,12,15,16,17,18,21,22,23,24};
	  
	    		if(!csvList.isEmpty())
	    		{
	    			//System.out.print(csvList.get(0));
	    			//if(csvList.get(0).equals("1")) ij++;
	    			for(int i=0; i < csvList.size(); i++)
	    			{
	    				System.out.println(csvList.get(i));
	    			}
	    		}
	    		//System.out.println();

}
	
	
	
 //receives a double array
 public static void printDoubleArray(double[] values){	
   System.out.print(String.format("%.5f", values[0] ) );   	   
   for(int i = 1; i < values.length ; i++){
      System.out.print(", " + String.format( "%.5f", values[i] ) );
   }
   System.out.println();
 }
	
		

  public static void main(String args[]){             
    //String fileName = "values_with_mean_of_control.csv";
    //String fileName = "AvgMerged.csv";
    
    String fileName = "";
    
   if (args.length > 0 ) {
     fileName = args[0];
   } else {
     System.out.println("Please provide a csv file name!");
     System.err.println("Invalid arguments count:" + args.length);
     System.exit(-1);
   }
   
    try{  	
       List<String> colNames = readColumnNames(fileName);
       colNames.add("Label");
  	printListOfColNames(colNames);
  	int chemID = findColumnID(colNames, "id_chemical"); //System.out.println(chemID);
  	
	//F-Cherry,F-Sapphire,F-Venus,HitRatio-Cherry,HitRatio-Sapphire,HitRatio-Venus,MIY-Cherry,MIY-Sapphire,MIY-Venus,DT-Cherry,DT-Sapphire,DT-Venus
  	int fCherry = findColumnID(colNames, "F-Cherry");
  	int fSapphire = findColumnID(colNames, "F-Sapphire");
  	int fVenus = findColumnID(colNames, "F-Venus");
  	//System.out.println(fCherry);System.out.println(fSapphire);System.out.println(fVenus);
  	
  	int hrCherry = findColumnID(colNames, "HitRatio-Cherry");
  	int hrSapphire = findColumnID(colNames, "HitRatio-Sapphire");
  	int hrVenus = findColumnID(colNames, "HitRatio-Venus");
  	//System.out.println(hrCherry);System.out.println(hrSapphire);System.out.println(hrVenus);
  	
  	int miyCherry = findColumnID(colNames, "MIY-Cherry");
  	int miySapphire = findColumnID(colNames, "MIY-Sapphire");
  	int miyVenus = findColumnID(colNames, "MIY-Venus");
  	//System.out.println(miyCherry);System.out.println(miySapphire);System.out.println(miyVenus);
  	
  	int dtCherry = findColumnID(colNames, "DT-Cherry");
  	int dtSapphire = findColumnID(colNames, "DT-Sapphire");
  	int dtVenus = findColumnID(colNames, "DT-Venus");
  	//System.out.println(dtCherry);System.out.println(dtSapphire);System.out.println(dtVenus);
  	
  	
  	List<List<String>> csvList = readFile(fileName,/*skip 1st line?*/ true);
  	//printCSVList(csvList);
  	
  	
  	//not used at the moment!
  	//List<String> csvIDChemicals = getAllIDChemicals(csvList, chemID);
  	
  	/*for(String e : csvIDChemicals)
    	{    		
    	   System.out.println("chem id " + e );
   	}*/
   	
    	
  	/*
  	double[] vfCherry = findColValues(csvList, fCherry);
  	double[] vfSapphire = findColValues(csvList, fSapphire);
  	double[] vfVenus = findColValues(csvList, fVenus);
  	
  	double[] vhrCherry = findColValues(csvList, hrCherry);
  	double[] vhrSapphire = findColValues(csvList, hrSapphire);
  	double[] vhrVenus = findColValues(csvList, hrVenus);
  	
  	double[] vmiyCherry = findColValues(csvList, miyCherry);
  	double[] vmiySapphire = findColValues(csvList, miySapphire);
  	double[] vmiyVenus = findColValues(csvList, miyVenus);
  	
  	double[] vdtCherry = findColValues(csvList, dtCherry);
  	double[] vdtSapphire = findColValues(csvList, dtSapphire);
  	double[] vdtVenus = findColValues(csvList, dtVenus);
  	*/
  	//System.out.println(csvList.size());
  	
  	List<String> autoFluorescentCompounds = findAutofluorescentCompounds(csvList,chemID, fCherry,fSapphire, fVenus);
  	//remove Autofluorescent Compounds
  	//removeElements(csvList, autoFluorescentCompounds, chemID);   	
  	//System.out.println("Autofluorescent Compounds: "+autoFluorescentCompounds.size());
  	
  	
  	List<String> mcherryPHitCompounds  = findPotentialHitCompounds(csvList, chemID, hrCherry);
  	List<String> sapphirePHitCompounds = findPotentialHitCompounds(csvList, chemID, hrSapphire);
	List<String> venusPHitCompounds    = findPotentialHitCompounds(csvList, chemID, hrVenus);
	
	List<String> definitelyToxicCompounds = findDefinitelyToxicCompounds(mcherryPHitCompounds, sapphirePHitCompounds, venusPHitCompounds);
	//remove definitely Toxic Compounds
  	//removeElements(csvList, definitelyToxicCompounds, chemID);
  	//System.out.println("definitely Toxic Compounds Compounds: "+definitelyToxicCompounds.size()); 
  	
	List<String> mcherryPossiblyActiveCompounds = findPossiblyActiveCompounds(csvList, chemID, miyCherry, dtCherry);
	List<String> sapphirePossiblyActiveCompounds = findPossiblyActiveCompounds(csvList, chemID, miySapphire, dtSapphire);
	List<String> venusPossiblyActiveCompounds = findPossiblyActiveCompounds(csvList, chemID, miyVenus, dtVenus);
	
	List<String> inactiveCompounds = findInactiveCompounds(csvList, chemID, mcherryPossiblyActiveCompounds, sapphirePossiblyActiveCompounds, venusPossiblyActiveCompounds);
	//remove inactive Compounds
  	//removeElements(csvList, inactiveCompounds, chemID);  	
  	//System.out.println("Inactive Compounds: "+inactiveCompounds.size());
  	
  	
	List<String> possiblyToxicCompounds = findPossiblyToxicCompounds(csvList, chemID,mcherryPHitCompounds, sapphirePHitCompounds, venusPHitCompounds,mcherryPossiblyActiveCompounds, sapphirePossiblyActiveCompounds, venusPossiblyActiveCompounds);
	//System.out.println("possibly Toxic Compounds Compounds: "+possiblyToxicCompounds.size()); 
	//remove possibly Toxic Compounds
  	//removeElements(csvList, possiblyToxicCompounds, chemID);
  	
  	//if a compound is none of the above, then it's active
  	//System.out.println("Active Compounds: "+csvList.size());
	
	
	//print out the whole labelled dataset
	//printLabelledData( csvList, autoFluorescentCompounds, definitelyToxicCompounds, inactiveCompounds, possiblyToxicCompounds);
	
	//print out the whole labelled active and inactive compounds
	printLabelledActiveInactiveData( csvList, autoFluorescentCompounds, definitelyToxicCompounds, inactiveCompounds, possiblyToxicCompounds);
	
	/*
	for (int idx=0; idx<10; ++idx){
              UUID idOne = UUID.randomUUID();
              System.out.println("User Id: " + String.valueOf(idOne));
        }
	 */
	 
  	//findPotentialHitCompounds(csvList, hrCherry, hrSapphire, hrVenus);
  	//findPossiblyActive(csvList, miyCherry, miySapphire, miyVenus, dtCherry, dtSapphire, dtVenus);
  	//System.out.println(csvList.size());
  	//System.out.println(v.length);
  	//printDoubleArray(v);
    }//close try
    catch(Exception aioobe){System.out.println("Error!");}		    	     	 
  }//end main  	       
}

























