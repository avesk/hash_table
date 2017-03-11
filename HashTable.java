/* HashTable.java
   CSC 225 - Spring 2017
   Template for string hashing
   
   =================
   
   Modify the code below to use quadratic probing to resolve collisions.
   
   Your task is implement the hash, insert, find, remove, and resize methods for the hash table.
   
   The load factor should always remain in the range [0.25,0.75] and the tableSize should always be prime.
   
   =================
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java HashTable
	
   Input data should consist of a list of strings to insert into the hash table, one per line,
   followed by the token "###" on a line by itself, followed by a list of strings to search for,
   one per line, followed by the token "###" on a line by itself, followed by a list of strings to remove,
   one per line.
	
   To conveniently test the algorithm with a large input, create
   a text file containing the input data and run the program with
	java HashTable file.txt
   where file.txt is replaced by the name of the text file.

   B. Bird - 07/04/2015
   M. Simpson - 21/02/2016
*/

import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.lang.Math;

public class HashTable{

     //The size of the hash table.
     int TableSize;
     
     //The current number of elements in the hash table.
     int NumElements;
	
	//The variable T represents the array used for the table.
	// DECLARE YOUR TABLE DATA STRUCTURE HERE
    String T[][];
	
	public HashTable(){
     	NumElements = 0;
     	TableSize = 33301;
     	// INITIALZE YOUR TABLE DATA STRUCTURE HERE

     	//Table[i][0] is the status code
     	//Table[i][1] is the stored string
     	T = new String [TableSize][2];
	}
	
	/* hash(s) = ((3^0)*s[0] + (3^1)*s[1] + (3^2)*s[2] + ... + (3^(k-1))*s[k-1]) mod TableSize
	   (where s is a string, k is the length of s and s[i] is the i^th character of s)
	   Return the hash code for the provided string.
	   The returned value is in the range [0,TableSize-1].

	   NOTE: do NOT use Java's built in pow function to compute powers of 3. To avoid integer
	   overflows, write a method that iteratively computes powers and uses modular arithmetic
	   at each step.
	*/
	public int hash(String s){
		int hash = 0;
		int val;
		for(int i = 0; i<s.length(); i++){
			val = s.charAt(i);
			hash += val*power(3,i) % TableSize;

			if(hash < 0){
				System.out.println(s + " Caused an overflow");
				System.out.println("Hash is  " + hash + " on the " + i + "th iteration");
				System.exit(0);
			}
		}
		// System.out.println("This is the HASH: " + hash);
		return(hash % TableSize);
	}

	public static long power(long n, long e){
		long b = 1;
		for(int i = 0; i<e; i++){
			b*=n;
		}

		// System.out.println("This is the result of power: " + result);
		return(b);

	}
	
	/* insert(s)
	   Insert the value s into the hash table and return the index at 
	   which it was stored.
	*/
	public int insert(String s){
		double loadFactor = ((double)NumElements/(double)TableSize);
		//check if the load factor threshold is reached

		if( loadFactor >= .75 ){
			System.out.println("I SHOULD NEVER BE IN HERE");
			int prime = getPrime(NumElements);
			resize(prime);
		}

		//get hash:
		int i = hash(s);
		int j = 0;
		
		while(T[i][1] != null && T[i][0]!="x"){
			
			//Quad probe
			i = i + j*j;
			j++;

			//Lin Probe:
			// i+=1;

			if (i >= TableSize)
				i = i%TableSize;

			if(i<0) System.out.println("This is the i in INSERT: " + i + " and this is the j: " + j);

		}

		T[i][1] = s;
		T[i][0] = "o";
		NumElements++;
		// System.out.println("Inserted " + s + " successfully");
		return i;
	}

	public int resizeInsert(String s){
		// System.out.println("Im in the resizeInsert the string is " + s);

		// NumElements++;
		//get hash:
		int i = hash(s);
		int j = 1;
		
		while(T[i][1] != null && T[i][0]!="x"){
			
			//Quad probe
			i+=j*j % TableSize;
			j++;

			// //Lin Probe:
			// i+=1;

			if (i >= TableSize)
				i = i%TableSize;

			if(i<0) System.out.println("This is the i in INSERT: " + i + " and this is the j: " + j);

		}

		T[i][1] = s;
		T[i][0] = "o";
		return i;
	}


	
	/* find(s)
	   Search for the string s in the hash table. If s is found, return
	   the index at which it was found. If s is not found, return -1.
	*/
	public int find(String s){
     	
     	//Get the hash value of the string and start the search at that index.
		int i = hash(s);
		
		//Use linear probing to find the string.
		while (true){
			String element = T[i][1];
			//If the slot is empty, the provided string was not found.
			if (element == null || T[i][0]=="x" )
				return -1;
			//If the slot contains the desired string, return its index.
			//Note that to test whether strings are equal in Java,
			//the '==' operator is not correct.
			if (s.equals(element) && T[i][0]!="x")
				return i;
			//If the string was not at this index, continue looking.
			i++;
			if (i >= TableSize)
				i = i%TableSize;
		}
	}
	
	/* remove(s)
	   Remove the value s from the hash table if it is present. If s was removed, 
	   return the index at which it was removed from. If s was not removed, return -1.
	*/
	public int remove(String s){
		int index = find(s);
		if(find(s)!=-1 && T[index][0] == "o"){
			T[index][0] = "x";
		}
		else{
			return(-1);
		}
     	return index;
	}
	
	/* resize()
	   Resize the hash table to be a prime within the load factor requirements.
	*/
	public void resize(int newSize){
		System.out.println("Needed a resize, this is the new Size: " + newSize);

		int oldTableSize = TableSize;
		String OldT[][] = T;

		TableSize = newSize;
		T = new String[TableSize][2];

		for(int j=0; j<oldTableSize; j++){

			if(OldT[j][1]!=null){
				resizeInsert(OldT[j][1]);
				// System.out.println("Re-Inserted " + OldT[j][1] + " successfully");

			}		
		}	   	
	}

	public static int getPrime(int numEl){
		numEl*=2;

		//find a new prime
		for(int i = numEl; ; i++){
			if(isPrime(i)){
				return i;
			}
		}

	}

	public static boolean isPrime(int num) {
        if (num < 2) return false;
        if (num == 2) return true;
        if (num % 2 == 0) return false;
        for (int i = 3; i * i <= num; i += 2)
            if (num % i == 0) return false;
        return true;
}
	
	/* **************************************************** */
	
	/* main()
	   Contains code to test the hash table methods. 
	*/
	public static void main(String[] args){
		Scanner s;
		boolean interactiveMode = false;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			interactiveMode = true;
			s = new Scanner(System.in);
		}
		s.useDelimiter("\n");
		if (interactiveMode){
			System.out.printf("Enter a list of strings to store in the hash table, one per line.\n");
			System.out.printf("To end the list, enter '###'.\n");
		}else{
			System.out.printf("Reading table values from %s.\n",args[0]);
		}
		
		Vector<String> tableValues = new Vector<String>();
		Vector<String> searchValues = new Vector<String>();
		Vector<String> removeValues = new Vector<String>();
		
		String nextWord;
		
		while(s.hasNext() && !(nextWord = s.next().trim()).equals("###"))
			tableValues.add(nextWord);
		System.out.printf("Read %d strings.\n",tableValues.size());
		
		if (interactiveMode){
			System.out.printf("Enter a list of strings to search for in the hash table, one per line.\n");
			System.out.printf("To end the list, enter '###'.\n");
		}else{
			System.out.printf("Reading search values from %s.\n",args[0]);
		}	
		
		while(s.hasNext() && !(nextWord = s.next().trim()).equals("###"))
			searchValues.add(nextWord);
		System.out.printf("Read %d strings.\n",searchValues.size());
		
		if (interactiveMode){
			System.out.printf("Enter a list of strings to remove from the hash table, one per line.\n");
			System.out.printf("To end the list, enter '###'.\n");
		}else{
			System.out.printf("Reading remove values from %s.\n",args[0]);
		}
		
		while(s.hasNext() && !(nextWord = s.next().trim()).equals("###"))
			removeValues.add(nextWord);
		System.out.printf("Read %d strings.\n",removeValues.size());
		
		HashTable H = new HashTable();
		long startTime, endTime;
		double totalTimeSeconds;
		
		startTime = System.currentTimeMillis();

		for(int i = 0; i < tableValues.size(); i++){
			String tableElement = tableValues.get(i);
			long index = H.insert(tableElement);
		}
		endTime = System.currentTimeMillis();
		totalTimeSeconds = (endTime-startTime)/1000.0;
		
		System.out.printf("Inserted %d elements.\n Total Time (seconds): %.2f\n",tableValues.size(),totalTimeSeconds);
		
		int removedCount = 0;
		int notRemovedCount = 0;
		startTime = System.currentTimeMillis();

		for(int i = 0; i < removeValues.size(); i++){
			String removeElement = removeValues.get(i);
			long index = H.remove(removeElement);
			if (index == -1)
				notRemovedCount++;
			else
				removedCount++;
		}
		endTime = System.currentTimeMillis();
		totalTimeSeconds = (endTime-startTime)/1000.0;
		
		System.out.printf("Tried to remove %d items (%d removed, %d not removed).\n Total Time (seconds): %.2f\n",
							removeValues.size(),removedCount,notRemovedCount,totalTimeSeconds);

		int foundCount = 0;
		int notFoundCount = 0;
		startTime = System.currentTimeMillis();

		for(int i = 0; i < searchValues.size(); i++){
			String searchElement = searchValues.get(i);
			long index = H.find(searchElement);
			if (index == -1)
				notFoundCount++;
			else
				foundCount++;
		}
		endTime = System.currentTimeMillis();
		totalTimeSeconds = (endTime-startTime)/1000.0;
		
		System.out.printf("Searched for %d items (%d found, %d not found).\n Total Time (seconds): %.2f\n",
							searchValues.size(),foundCount,notFoundCount,totalTimeSeconds);
							
		
	}
}
