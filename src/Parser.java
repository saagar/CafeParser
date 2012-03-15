import java.util.*;
import java.io.*;


public class Parser {
	
	File f;
	
	public Parser(File file){
		f = file;
	}
	
	//public static void main(String args[]){
	public int parseFile(){
		//Scanner inputReader = new Scanner(System.in);
		//System.out.println("Please enter the name of the file to use: ");
		
		//want to use an array list to hold all the productLines in the file
		ArrayList<String> productLine = new ArrayList<String>();
		int ret = 0;
		//try catch for IO exceptions
		try {
			
			
			/* START PARSING */
			
			// get text file and parse it
			//File f = new File("D:\\Dropbox\\Projects\\CafeParser\\ConsolidatedProductMix.120309-0115.rpt");
			
			String dir = f.getParent();
			String filename = f.getName();
			
			System.out.println(f.getParent());	
			System.out.println(f.getPath());
			
			FileInputStream fileIn = new FileInputStream(f);		
			DataInputStream data = new DataInputStream(fileIn);
			InputStreamReader inputString = new InputStreamReader(data);
			BufferedReader reader = new BufferedReader(inputString);
			
			String filedate = new String(); //want to store the file date
			String wordline = reader.readLine();
			
			while (wordline != null){
				
				//parse the line word by word and add each word into the ArrayList --- based on API: http://docs.oracle.com/javase/1.4.2/docs/api/java/util/StringTokenizer.html
				StringTokenizer st = new StringTokenizer(wordline);
				
				int lineStart = 0; //counts which word in the line we are at
				
				while(st.hasMoreTokens()){
					String x = st.nextToken();
					//catch the subtotal and total line cases
					if(x.equals("Subtotals") || x.equals("Totals"))
						productLine.add(wordline);
					
					//check if first word is a product number
					if(lineStart == 0){
						String num = x.substring(1);
						
						try  
						{  
							Integer.parseInt(num);
							//if we found a product, store the general line
							productLine.add(wordline);
						}  
						catch(NumberFormatException nfe)  
						{  
							// don't need to parse rest of the line
							//if((x.trim()).equals(" ") || (x.trim()).equals(""))
							if(x.matches("[0-9]{2}:[0-9]{2}")){
								filedate = wordline;
								System.out.println(wordline);
							}
							break;
						}  					
					}
					
					lineStart++; //increment word position
				}
				//get the next line of the file
				wordline = reader.readLine();
				
			}
			
			//System.out.println(productLine.toString()); //for testing, enable comment to print the lines we want to parse
			
			FileWriter writer = new FileWriter(dir + "\\fullparsed_" + filename + ".csv");
			
			writer.append(filedate.trim());
			writer.append(",");
			writer.append("\n");
			writer.append("product number ");
			writer.append(",");
			writer.append("description ");
			writer.append(",");
			writer.append("size ");
			writer.append(",");
			writer.append("quantity ");
			writer.append(",");
			writer.append("selling price ");
			writer.append(",");
			writer.append("sales ");
			writer.append(",");
			writer.append("discount amount ");
			writer.append(",");
			writer.append("adj. sales ");
			writer.append(",");
			writer.append("% period sales ");
			writer.append(",");
			writer.append("# of trans ");
			writer.append(",");
			writer.append("% period trans ");
			writer.append(",");
			writer.append("% report trans");
			writer.append("\n");
			
			//need to generate more text before we flush
			
			
			int dataPosition = 0;
			ArrayList<String> items = new ArrayList<String>();
			ArrayList<String> amount = new ArrayList<String>();
			
			//print all information into CSV
			for(int i = 0; i < productLine.size(); i++){
				StringTokenizer delim = new StringTokenizer(productLine.get(i), "|");
				while(delim.hasMoreTokens()){
					String x = delim.nextToken();
					//push item into list
					if(dataPosition == 1){
						items.add(x.trim());
					}
					//push amount sold into list
					if(dataPosition == 3){
						amount.add(x.trim());
					}
					writer.append(x.trim());
					writer.append(',');
					dataPosition++;
				}
				dataPosition = 0;
				writer.append("\n");
			}

			/* END PARSING */
			
			//close writing buffers.
		    writer.flush();
		    writer.close();
		    ret += 1;
		    //generate simple CSV
		    FileWriter simpleWriter = new FileWriter(dir + "\\micro_"+filename+".csv");
		    
		    //insert spot for date
			simpleWriter.append("");
	    	simpleWriter.append(",");
		    
		    for(int i = 0; i < items.size(); i++){
		    	simpleWriter.append(items.get(i));
		    	simpleWriter.append(",");
		    }
		    simpleWriter.append("\n");
		    simpleWriter.append(filedate.trim());
	    	simpleWriter.append(",");
		    for(int i = 0; i < amount.size(); i++){
		    	simpleWriter.append(amount.get(i));
		    	simpleWriter.append(",");
		    }
		    simpleWriter.flush();
		    simpleWriter.close();
		    ret += 2;
		    System.out.println("Parsing was successful.");
		    return ret;
		    
		} catch (IOException e) {
			// catch any IO exceptions. mostly for file handling, in case we don't have a file opened correctly
			System.out.println("Something went wrong! Did you input the wrong folder/file? Please close relevant files before rerunning.");
			
			e.printStackTrace();
			return ret;
		}
		
	
	}

}
