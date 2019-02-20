# Boolean-Search-Engine
Boolean search Engine implements the methods like  AND, OR, NOT and perform some queries. Corpus (document collection)  used  in this project  are the Amazon.com reviews on electronics. 

BooleanRetrieval.java is the main file which should be run.This File methods are also depend on the DatasetFormatter.java file .So compile all files before you run the main file. 
It accepts 3 arguments as inputs  which are as follows  Query Type, Query String and OutPut file respectively.
Input examples: 
1)"AND" "mouse and scrolling" "mouse_and_scrol_list.txt"
2)"AND-NOT" "lenovo and (not logitech)" "lenevo_notLogitech.txt"

Always give Query String within quotation marks,so that it is consider as one string and follow below format while execution:
Format of Query String as follows :
	For "OR" type :
 		"word1<space>OR<space>word2"
 	For "AND" type :
 		"word1<space>AND<space>word2"
 	For "AND-NOT" type :
 		"word1<space>AND<space>(NOT<space>word2)"
 
 Always mind that the space between two words should not be more than 1 word.
 
 
 Exception Handling :
 For any incorrect typed Arguments search ,exceptions are printed on console .
 example:
 For inputs :  "PLIST" "Satellite" "out_line.txt"
               (since "Satellite" word does not exist in vocab or all.txt file ,this incorrect search throws exception.)
                
                or
                
                "AND" "mouse" "out_line.txt"
                (since "AND" operation must have 2 query term but here in example it is given only 1 input)
 
 
