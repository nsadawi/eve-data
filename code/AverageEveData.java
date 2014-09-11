/**
 - To Average Eve Data & Compute values needed for labelling
 - files are tsv
 - We are assuming input files will always have the same structure!
 - values needed for labelling are computed according to formulas provided in the notes
 - formulas are also available in kevin's thesis!
 - several new columns will be introduced as a result of these computations
 - notice that id_chemical can repeat because the same chemical might have several experiments
 - obselete: We attach a random string to id_chemical to distinguish between things
 - This application will print results to std out
 - we can use:    java AverageEveData > out.csv
*/
  import java.io.*;
  import java.util.*;
  import java.util.regex.Matcher;
  import java.util.regex.Pattern;
  import java.util.UUID;

import java.math.*;

class AverageEveData
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

			String line = infile.readLine(); // skip 1st line - it's the headers - column names!
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


/**
 * given a list of list of strings (representing csv text lines), this function clusters 
 * various lines by a certain element given this element's index (place in the csv line)
 * @param  csvList a list of list of strings (representing csv text lines)
 * @param  id2ClusterBy index of element to be used for grouping (place in the csv line)
 * @return  a list of list of of list of strings containing clusters
 */ 
 public static List<List<List<String>>> findCSVClusters(List<List<String>> csvList, int id2ClusterBy){
 	List<List<String>> csvCluster = new ArrayList<List<String>>();
 	List<List<List<String>>> plateNoClusters = new ArrayList<List<List<String>>>();
	List<List<String>> csvList1 = new ArrayList<List<String>>(csvList);
	//int i = 0;

    	for(List<String> csv : csvList)
    	{
    		//if(csv.get(0).equals("1")) i++;
    		csvList1.remove(csv);
    		csvCluster.add(csv);

    		for(List<String> csv1 : csvList1)
    		{
    			if(!csv.isEmpty() && !csv1.isEmpty() && csv.get(id2ClusterBy).equals(csv1.get(id2ClusterBy)) && !csvCluster.contains(csv1)){
    			     //csvList.remove(csv);
    			     csvCluster.add(csv1);
    			}
    		}
    		boolean done = false;
    		for(List<List<String>> csvCls : plateNoClusters)
    		{
    		    if(csvCls.contains(csv))
    		      done = true;
    		}
    		if(!done)
	  	   plateNoClusters.add(new ArrayList<List<String>>(csvCluster));

    		csvCluster.clear();
    	}
    	//System.out.println("Size " + i );
    	return plateNoClusters;
}



//prints selected column names
//they're in a list of string
public static void printListOfColNames(List<String> csvList){
   String[] s = {"F-Cherry","F-Sapphire","F-Venus","HitRatio-Cherry","HitRatio-Sapphire","HitRatio-Venus", "MIY-Cherry","MIY-Sapphire","MIY-Venus","DT-Cherry","DT-Sapphire","DT-Venus" };
    int e[] = {4,9,10,11,12,15,16,17,18,21,22,23,24};
    if(!csvList.isEmpty() && (e.length <= csvList.size()))
    {
		System.out.print(csvList.get(e[0]));	
	for(int i=1; i < e.length; i++)
 	   System.out.print("," + csvList.get(e[i]));
 	   
 	for(int i=0; i < s.length; i++)
 	   System.out.print("," + s[i]);
 	   
	System.out.println();
    }	    		
}


public static void printCSVList(List<List<String>> csvList){
       //int ij = 0;
    	for(List<String> csv : csvList)
    	{
    		if(!csv.isEmpty())
    		{
    			System.out.print(csv.get(0));
    			//if(csv.get(0).equals("1")) ij++;
    			for(int i=1; i < csv.size(); i++)
    			{
    				System.out.print("," + csv.get(i));
    			}
    		}
    		//System.out.print(" - "+ij+"\n");
    	}
}


public static void printList(List<String> csvList){
	      //id's for values we want from csv file (venus, sapphire ..etc)
		int e[] = {9,10,11,12,15,16,17,18,21,22,23,24};	    			   	     
    		if(!csvList.isEmpty() && (e.length <= csvList.size()))
    		{
    			System.out.print(csvList.get(e[0]));
    			//if(csvList.get(0).equals("1")) ij++;
    			for(int i=1; i < e.length; i++)
    			{
    				System.out.print("," + csvList.get(e[i]));
    			}
    		}
    		System.out.println();

}

//converts string to double and returns them in an array
//reads required fields only (in e[])
public static double[] listToDoubleArray(List<String> csvList){
	   //id's for values we want from csv file (venus, sapphire ..etc)
	    int e[] = {9,10,11,12,15,16,17,18,21,22,23,24};
	   
	   double[] values = new double[12];
	   
	   for(int i = 0; i < values.length ; i++){
	      values[i] = Double.parseDouble(csvList.get(e[i]));
	   }
	   
	   return values;             
}

	//receives a double array
	public static void printDoubleArray(double[] values){	
	   System.out.print(String.format("%.5f", values[0] ) );   	   
	   for(int i = 1; i < values.length ; i++){
	      System.out.print(", " + String.format( "%.5f", values[i] ) );
	   }
	   System.out.println();
	}
	
	
	//receives a chem id and double array
	public static void printChemIDAndDoubleArray(int id, double[] values){
		System.out.print(id+", ");
		printDoubleArray(values);
	}
	
	//receives a double matrix
	public static void printDoubleMatrix(double[][] values){
	  for(int i = 0; i < values.length; i++)	   	   
	    printDoubleArray(values[i]);	  
	}
	
	//receives a chem id and double array
	public static void printChemIDAndDoubleMatrix(int id, double[][] values){
	   for(int i = 0; i < values.length; i++){
	      //id_chemicals can repeat in mass screen data, so we attach a random string
	      //UUID rnd = UUID.randomUUID();
	      //System.out.print(id+"_"+String.valueOf(rnd)+", ");
	      System.out.print(id+", ");		   	   
	      printDoubleArray(values[i]);
	   }	
	}
	
	// the array double[] m MUST BE SORTED
	public static double medianOfArray(double[] m) {
	    //System.out.println("Median of:");
	    //printDoubleArray(m);
	    double median = 0.;
	    int middle = m.length/2;
	    if (m.length%2 == 1) {
		median = m[middle];
	    } else {
		median = (m[middle-1] + m[middle]) / 2.0;
	    }
	    //System.out.println("is: "+median);
	    return median;
	}

     public static double[] columnMedians(double[][] row) {
        double medians[] = new double[row[0].length];
        //double total = 0;
        for (int i = 0; i < row[0].length; i++){
            double c[] = new double[row.length];
            for (int j = 0; j < row.length; j++){
                c[j] = row[j][i];
            }
            Arrays.sort(c);
            medians[i] = medianOfArray(c);
            //total = 0;
        }
        return medians;
    }
    
    // the array double[] m MUST BE SORTED
	public static double meanOfArray(double[] m) {
	    double sum = 0.0;
	    for (int j = 0; j < m.length; j++){
                sum += m[j];
            }
	    return sum/m.length;
	}
	
	
    public static double[] columnMeans(double[][] row) {
        double means[] = new double[row[0].length];
        //double total = 0;
        for (int i = 0; i < row[0].length; i++){
            double c[] = new double[row.length];
            for (int j = 0; j < row.length; j++){
                c[j] = row[j][i];
            }
            Arrays.sort(c);
            means[i] = meanOfArray(c);
            //total = 0;
        }
        return means;
    }


    public static double[] columnAverage(double[][] row) {
        double average[] = new double[row[0].length];
        double total = 0;
        for (int i = 0; i < row[0].length; i++){
            for (int j = 0; j < row.length; j++){
                total = total + row[j][i];
            }
            average[i] = total / row.length;
            total = 0;
        }
        return average;
    }
    
    
    public static double[] chemCtrlColumnAverage(double[] ctrl, double[][] chem, int w) {
        double average[] = new double[ctrl.length];
        for(int j = 0; j < chem[0].length; j++){	
           double sum = 0.;   	   
	    for(int i = 0; i < chem.length ; i++){
	        //System.out.print(String.format( "%.5f", values[i][j] ) +", ");
	        sum += Math.abs(chem[i][j] - ctrl[i]);
	    }
	    average[j] = sum / w;
	    //System.out.println();
	  }
        
        return average;
    }
    
 
 //receives two arrays of double and concatenates them   
public static double[] combineTwoDoubleArrays(double[] a, double[] b){
        double[] c = new double[a.length + b.length];
	System.arraycopy(a, 0, c, 0, a.length);
	System.arraycopy(b, 0, c, a.length, b.length);
	return c;
 }
 
 //do the computations explained in kevin's thesis and return them in an array
 //we end up with 12 new values
 //these values are used later for labelling
 //cherry-initial-fluorescence,cherry-final-fluorescence,cherry-lagtime2,cherry-doubletime,sapphire-initial-fluorescence,sapphire-final-fluorescence,sapphire-lagtime2,sapphire-doubletime,venus-initial-fluorescence,venus-final-fluorescence,venus-lagtime2,venus-doubletime
    public static double[]  compute_F_HR_MIY_DT(double[] ctrl, double[] compound) {
        double fCherry = (compound[0] - ctrl[0]) / (ctrl[1] - ctrl[0]);        
        double fSapphire = (compound[4] - ctrl[4]) / (ctrl[5] - ctrl[4]);        
        double fVenus = (compound[8] - ctrl[8]) / (ctrl[9] - ctrl[8]);
        
        double hrCherry = (compound[1] - compound[0]) / (ctrl[1] - ctrl[0]);                
        double hrSapphire = (compound[5] - compound[4]) / (ctrl[5] - ctrl[4]);                
        double hrVenus = (compound[9] - compound[8]) / (ctrl[9] - ctrl[8]); 
        
        double miyCherry = compound[2] - ctrl[2];
        double miySapphire = compound[6] - ctrl[6];              
        double miyVenus = compound[10] - ctrl[10];   
        
        double dtCherry = compound[3] / ctrl[3];
        double dtSapphire = compound[7] / ctrl[7];              
        double dtVenus = compound[11] / ctrl[11];  
        
        double values[] = {fCherry,fSapphire,fVenus,hrCherry,hrSapphire,hrVenus, miyCherry,miySapphire,miyVenus,dtCherry,dtSapphire,dtVenus};             
        return values;
    }
        
        
//this method removes controls from a list of chemicals and creates a pair
//(list of chem's, their control)
public static void removeControls(List<Chem> chemList, LinkedHashMap chemRLE) {
    List<Chem> tmpChemList = new ArrayList<Chem>(chemList);
    //System.out.println(tmpChemList.size());
    
    //remove control from list
     Iterator<Chem> iter = tmpChemList.iterator();
     Chem chm = null;
     while (iter.hasNext()) {
           chm = (Chem) iter.next();
	    if (chm.chemID == 1313) {	        
		iter.remove();
		break;
	    }
     }
     chemRLE.put(tmpChemList, chm);    
}

 public static void main(String args[])
  {
          
   //to save list of chemicals in a plate with their control
   //(list of chem's in a plate, ctrl)
   LinkedHashMap chemRLE = new LinkedHashMap();

  int ctrlChemID = 1313;//id_chemical for control

  //String fileName = "/home/likewise-open/ACADEMIC/csstnns/Desktop/CherryPick.csv";
  //String fileName = "/home/likewise-open/ACADEMIC/csstnns/Downloads/MassScreen.csv";
   String fileName = "";
    
   if (args.length > 0 ) {
     fileName = args[0];
   } else {
     System.out.println("Please provide a csv file name!");
     System.err.println("Invalid arguments count:" + args.length);
     System.exit(-1);
   }


  try{
	//get a list of column names (fst line of file)
  	List<String> colNames = readColumnNames(fileName);
  	//print them out
  	printListOfColNames(colNames);
  	//read file into a list of lists
  	List<List<String>> csvList = readFile(fileName,/*skip 1st line?*/ true);
  	
  	//create clusters (groups) by plate #
  	//ie rows of each plate will be in the same sub-list
  	//(index of plate # is 0 in the header list) 
  	List<List<List<String>>> plateNoClusters = findCSVClusters(csvList, 0);
  	
	List<Plate> plateList = new ArrayList<Plate>();
	//loop thru all plate groups
    	for(List<List<String>> csvCls : plateNoClusters) //csvCls is cluster by plate #
    	{
    		//get plate number of current plate cluster
    		int plateNumber = Integer.parseInt(csvCls.get(0).get(0));
    		//System.out.println("Number of rows for Plate # "+plateNumber+" is: " + csvCls.size() );
   		
    		//cluster by id_chemical (index is 4 in the header list)    		
    		List<List<List<String>>> chemIDClusters = findCSVClusters(csvCls, 4);
    		//a list of Chemical Compounds
    		List<Chem> chemList = new ArrayList<Chem>();
    		//loop thru all same chemical groups
    		for(List<List<String>> csvCls1 : chemIDClusters)  //csvCls1 is clusters by id_chemical
    		{
    		  	//now cluster by well id (index is 1 in the header list)
	    		List<List<List<String>>> wellIDClusters = findCSVClusters(csvCls1, 1);
	    			
			List<List<String>> csvCls2 = wellIDClusters.get(0);
    			//check the chemID to model a chemical
    			List<String> csvList2 = csvCls2.get(0);
    			int chemID = Integer.parseInt(csvList2.get(4));
    			
			//a list of Wells	  
			List<Well> wellList = new ArrayList<Well>();
			//loop thru all wells and add to wellList
			//we have several wells for each chem compound
			for(List<List<String>> csvCls3 : wellIDClusters){  //csvCls3 is clusters by well id
	    		      List<String> csvList3 = csvCls3.get(0);
	    		      char wellID = csvList3.get(1).charAt(0);
	    		      int sizeWellIDx = csvCls3.size();
	    		      //System.out.println("\tNumber of rows for well "+wellID+" is: " + sizeWellIDx );
	    		      wellList.add (new Well(wellID,csvCls3));
			}
			//add current chemial compound to chemList
			//chem comp has one chemID and several wells
			chemList.add(new Chem(chemID, wellList));
  		}
  		//add current plate to plateList
  		//plate has one plateNo and several chemicals
    		plateList.add(new Plate(plateNumber, chemList));    		    		
    	} // end for(List<List<String>> csvCls : plateNoClusters)
    	    	
    	
    	
    	for(Plate plate : plateList){ 
    	  //fill the hashmap chemRLE with lists of chems & their ctrl
    	  removeControls(plate.list, chemRLE);     	      	   	       	        	        	        	        	    
	}
    	
    	    	
    	// Get a set of the entries
      Set set1 = chemRLE.entrySet();
      // Get an iterator
      Iterator plateNums1 = set1.iterator();
           //here we llop thru all list of chemicals and their corresponding controls 
           //and we do the necerssary computations to carry out the averaging
        while(plateNums1.hasNext()) {
      
		 Map.Entry me = (Map.Entry) plateNums1.next();
		 //System.out.print(me.getKey() + ": ");
		 //System.out.println(me.getValue());
		    
		 //get list of chemicals in this plate         
		 List<Chem> chmLs = (List<Chem>) me.getKey();
		 //get ctrl in this plate
		 Chem chmCtrl = (Chem) me.getValue();
		 
		 //System.out.println("Plate:");		 
		 //System.out.println("Ctrl: "+chmCtrl.chemID);
	    	 //System.out.println("  Ctrl ID: "+chmCtrl.chemID+" -> exists in: "+chmCtrl.list.size()+" Well Rows");  
	    	 //compute total number of well rows for this ctrl
	    	 int totNoOfCtrlWellRows = 0;
	    	 
	    	 //now averaging for the ctrl
	    	 for(Well well : chmCtrl.list)
	      	        totNoOfCtrlWellRows += well.list.size();      	      
	      	   int k = 0;  
	    	   double[][] ctrlValues = new double [totNoOfCtrlWellRows] [12];
	    	   for(Well well : chmCtrl.list){
	      	        //System.out.println("     Has Well: "+well.wellRow+" -> "+well.list.size()+" Columns");	      	        	      	        
		    	  for(List<String> ls : well.list){
	    	            //printList(ls);
	    	            ctrlValues[k] = listToDoubleArray(ls); 
	    	            k++;
	    	          }
	    	     }
		     
		     //uncomment of you want to use column means
		     //double[] ctrlColMeans = columnMeans(ctrlValues);
		     
		     //uncomment of you want to use column medians
		     double[] ctrlColMeans = columnMedians(ctrlValues);
		 
		 //now averaging & calculations for all chemicals that have this ctrl    
		 for(Chem chm : chmLs)
	    	 {
	    	    try{
	    		//System.out.print(chm.chemID+", ");
	    		int noOfWellRows = chm.list.size();  	    				    				    				    					
			//12 columns for original values & 12 columns for new computed values
			//original values are readings from input data
			double[][] mediansAndNewValues = new double[chm.list.get(0).list.size()][12+12];			
			for(int xx = 0; xx < chm.list.get(0).list.size(); xx++){
			   double[][] compValsAtSameConc = new double[chm.list.size()][12];     
			   for(int yy = 0; yy < chm.list.size(); yy++){
			      //printList(chm.list.get(yy).list.get(xx));
			      //printDoubleArray(listToDoubleArray(chm.list.get(yy).list.get(xx)));
			      compValsAtSameConc[yy] = listToDoubleArray(chm.list.get(yy).list.get(xx));
			   }
			   //column medians for chemical
			   double[] chemColMedians = columnMedians(compValsAtSameConc);
			   //double[] chemColMedians = columnMeans(compValsAtSameConc);
			   double[] fhrmiydtValues   = compute_F_HR_MIY_DT(ctrlColMeans, chemColMedians);
			   
			   //mediansOfCompValsAtSameConc[xx] = combineTwoDoubleArrays(chemColMedians,fhrmiydtValues);
			   
			   mediansAndNewValues[xx] = combineTwoDoubleArrays(chemColMedians,fhrmiydtValues);
			   //mediansOfCompValsAtSameConc[xx] = chemColMedians;			   
			}
			//printDoubleMatrix(mediansOfCompValsAtSameConc);
			//System.out.println("-----------");
		
			printChemIDAndDoubleMatrix(chm.chemID, mediansAndNewValues);						
			//System.out.println(">> Averages <<:");
			//printDoubleArray(chemCtrlColumnAverage(ctrlColMedians, mediansOfCompValsAtSameConc, noOfWellRows));							
	            }//close try
		    catch(IndexOutOfBoundsException aioobe){}	
	    	 }// end for(Chem chm : chmLs)    	     	 
        }      // end  while(plateNums1.hasNext()) {                          

     }
     catch (Exception e){//Catch exception if any
        e.printStackTrace();
  	System.err.println("Error: " + e.getMessage());
     }



  }


  
  //simple class to model a well
  static  class Well {
	char wellRow;
	List<List<String>> list;
	
	public Well(char wellRow, List<List<String>> list){
	    this.wellRow = wellRow;
	    this.list = new ArrayList<List<String>>(list);	    		    
       }
  }
  //simple class to model a chemical compound
  static  class Chem {
	int chemID;
	List<Well> list;
	
	public Chem(int chemID, List<Well> list){
	    this.chemID = chemID;
	    this.list = new ArrayList<Well>(list);	    		    
       }
  }
  //simple class to model a plate
  static class Plate {
	int plateNo;
	List<Chem> list;
	
	public Plate(int plateNo, List<Chem> list){
	    this.plateNo = plateNo;
	    this.list = new ArrayList<Chem>(list);	    		    
       }
  }

}
