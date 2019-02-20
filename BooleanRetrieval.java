package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public class BooleanRetrieval {
	
	HashMap<String, Set<Integer>> invIndex;
	int [][] docs;
	HashSet<String> vocab;
	HashMap<Integer, String> map;  // int -> word
	HashMap<String, Integer> i_map; // inv word -> int map
	private static String query_type;
	private static String query_string;
	private static String output_file_path;

	public BooleanRetrieval() throws Exception{
		// Initialize variables and Format the data using a pre-processing class and set up variables
		invIndex = new HashMap<String, Set<Integer>>();
		DatasetFormatter formater = new DatasetFormatter();
		formater.textCorpusFormatter("./all.txt");
		docs = formater.getDocs();
		//System.out.println(docs.length);
		vocab = formater.getVocab();
		map = formater.getVocabMap();
		i_map = formater.getInvMap();
	}

	void createPostingList(){
		//Initialze the inverted index with a SortedSet (so that the later additions become easy!)
		for(String s:vocab){
			invIndex.put(s, new TreeSet<Integer>());
		}
		//for each doc
		for(int i=0; i<docs.length; i++){
			//for each word of that doc
			for(int j=0; j<docs[i].length; j++){
				//Get the actual word in position j of doc i
				Set<Integer> w = invIndex.get(map.get(docs[i][j]));
				w.add(i+1);
				invIndex.put(map.get(docs[i][j]), w);
				/* TO-DO:
				Get the existing posting list for this word w and add the new doc in the list. 
				Keep in mind doc indices start from 1, we need to add 1 to the doc index , i
				 */
			}

		}
	}


	Set<Integer> intersection(Set<Integer> a, Set<Integer> b){
		/*
		First convert the posting lists from sorted set to something we 
		can iterate easily using an index. I choose to use ArrayList<Integer>.
		Once can also use other enumerable.
		 */
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
		ArrayList<Integer> PostingList_b = new ArrayList<Integer>(b);
		Set<Integer> result = new TreeSet<Integer>();


		
		int m=PostingList_a.size();int n=PostingList_b.size();
		
	
	        if (m > n) 
	        {
	            ArrayList<Integer> tempp = PostingList_a;
	            PostingList_a = PostingList_b;
	            PostingList_b = tempp;
	 
	            int temp = m;
	            m = n;
	            n = temp;
	        }
	 
	      
	        //Collections.sort(PostingList_a);
	 
	    
	        for (int i1 = 0; i1 < n; i1++) 
	        {
	            if (binarySearch(PostingList_a, 0, m - 1, PostingList_b.get(i1) )!= -1) 
	                result.add(PostingList_b.get(i1));
	        }
	    
	
	     
		
		
		
		return result;
	}

	
int binarySearch(ArrayList<Integer> arr, int l, int r, int x) 
{
    if (r >= l) 
    {
        int mid = l + (r - l) / 2;

        // If the element is present at the middle itself
        if (arr.get(mid) == x)
            return mid;

        // If element is smaller than mid, then it can only 
        // be present in left subarray
        if (arr.get(mid) > x)
            return binarySearch(arr, l, mid - 1, x);

    
        return binarySearch(arr, mid + 1, r, x);
    }

    return -1;
}	
	
	
	
	
	
	Set <Integer> evaluateANDQuery(String a, String b){
		return intersection(invIndex.get(a), invIndex.get(b));
	}

	Set<Integer> union(Set<Integer> a, Set<Integer> b){
		/*
		 * IMP note: you are required to implement OR and cannot use Java Collections methods directly, e.g., .addAll whcih solves union in 1 line!
		 * TO-DO: Figure out how to perform union extending the posting list intersection method discussed in class?
		 */
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
		ArrayList<Integer> PostingList_b = new ArrayList<Integer>(b);
		Set<Integer> result = new TreeSet<Integer>();


		
		
		int m=PostingList_a.size();int n=PostingList_b.size();
		
	
	        if (m > n) 
	        {
	            ArrayList<Integer> tempp = PostingList_a;
	            PostingList_a = PostingList_b;
	            PostingList_b = tempp;
	 
	            int temp = m;
	            m = n;
	            n = temp;
	        }
	 
	   
	       // Collections.sort(PostingList_a);
	 
	   
	        for (int i1 = 0; i1 < n; i1++) 
	        {
	            if (binarySearch(PostingList_a, 0, m - 1, PostingList_b.get(i1) )== -1) 
	                result.add(PostingList_b.get(i1));
	        }
	        for (int i1 = 0; i1 < m; i1++) 
	        {
	        result.add(PostingList_a.get(i1));
	        
	        }
		// Implement Union here
		return result;
	}

	Set <Integer> evaluateORQuery(String a, String b){
		return union(invIndex.get(a), invIndex.get(b));
	}
	
	Set<Integer> not(Set<Integer> a){
		Set<Integer> result = new TreeSet<Integer>();
		/*
		 Hint:
		 NOT is very simple. I traverse the sorted posting list between i and i+1 index
		 and add the other (NOT) terms in this posting list between these two pointers
		 First convert the posting lists from sorted set to something we 
		 can iterate easily using an index. I choose to use ArrayList<Integer>.
		 Once can also use other enumerable.
		 
		 
		 */
		
		ArrayList<Integer> PostingList_a = new ArrayList<Integer>(a);
	
		// TO-DO: Implement the not method using above idea or anything you find better!
	
	    ArrayList<Integer> newList = new ArrayList<Integer>(map.keySet());
		for (int i1 = 0; i1 < newList.size(); i1++) 
        {
            if (binarySearch(PostingList_a, 0, PostingList_a.size() - 1, newList.get(i1) )== -1) 
                result.add(newList.get(i1));
        }	
			
		return result;
	}

	Set <Integer> evaluateNOTQuery(String a){
		return not(invIndex.get(a));
	}
	
	Set <Integer> evaluateAND_NOTQuery(String a, String b){
		return intersection(invIndex.get(a), not(invIndex.get(b)));
	}
	
	public static void fileWrite(Set<Integer> result) {
		try {
		    PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(output_file_path)));
		
		               output.print(query_string+ " -> "+result);
		    		    output.close();
		} catch (IOException e) {System.out.println("Error: "+e.getMessage()+";Output File Path Error");
		}
	}
	
	public static void main(String[] args) throws Exception {
try{  
		//Initialize parameters
		BooleanRetrieval model = new BooleanRetrieval();

		//Generate posting lists
		model.createPostingList();
		query_type = args[0];
		query_string = args[1];
	
		String[] str=query_string.toLowerCase().split(" ");
			
			
		
		output_file_path = args[2];
	
		Set<Integer> result=new TreeSet<Integer>();
		switch(query_type.toLowerCase()){
		case "plist":		
		
			result=model.invIndex.get(query_string.toLowerCase());
			break;
		case "and":
			result=model.evaluateANDQuery(str[0], str[2]);
			break;
		case "or":
			result=model.evaluateORQuery(str[0], str[2]);
			break;
		case "and-not":

			result=model.evaluateAND_NOTQuery(str[0], str[3].substring(0, str[3].length()-1));
			break;
		default :
			System.out.println("Error: Invalid Query Type");	
		
		}
		
		if(result==null || output_file_path==null){throw new NullPointerException();}else if(output_file_path!=null){
			fileWrite(result);
		}


	}catch(NullPointerException e){System.out.println("Error: Invalid Query String argument;Kindly check the query string");}
     catch(Exception e){System.out.println("Error: Invalid Query String argument;Kindly check the query string");
		
	}}

}