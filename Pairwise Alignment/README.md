Pairwise Alignment
=
Pairwise Alignment is one of two avaliable projects for the University of Georgia's CSCI 4470/6470 Algorithms course in 
Fall 2019 by Dr. Liming Cai. 

Operating Instructions
=
1. The input files, `scheme.txt` and `sequences.txt` should be stored inside the `res` (resources) folder.
    1. `scheme.txt` should contain a scoring system for each character matching between the two input sequences.
    Its format should be as follows:
    
        |   	| A  	| C  	| G  	| T  	| _  	|
        |:---:	|:----:	|:----:	|:----:	|:----:	|:----:	|
        | A 	| 5  	| -2 	| -2 	| -2 	| -6 	|
        | C 	| -2 	| 5  	| -2 	| -2 	| -6 	|
        | G 	| -2 	| -2 	| 5  	| -2 	| -6 	|
        | T 	| -2 	| -2 	| -2 	| 5  	| -6 	|
        | _ 	| -6 	| -6 	| -6 	| -6 	| 0  	|
        
    Note that the `Symbols` on the left correspond to the first sequence and the `Symbols` on the top correspond to 
    the second sequence. Each number represents a matching of the two `Symbols`.
        
    2. `sequences.txt` should have the following format:
        ```
        >names of the sequences
        <Sequence X>
        <Sequence Y>
        ```
2. The `main` driver method is contained within `PairwiseAlignment.java`. This should be run when all the input files 
are correctly formatted and located.

3. After the program is complete, the output file `output.txt` should appear in the `rec` folder with the following 
information:
    ```
    >alignment
    <Optimal Alignment for X>
    <Optimal Alignment for Y>
    >score
    <Optimal Score>
    ```
Here, `_` denotes a gap `Symbol` in the alignment. For more information, refer to the report attached.
