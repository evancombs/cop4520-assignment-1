Compile with "javac FindPrimes.java"
Compilation generates FindPrimes.class and ThreadRunner.class

Input
	Run with "java FindPrimes <number of threads> <number to find primes up to>
	
Experimental Evaluation
	Empirical analysis is performed using the System.currentTimeMillis() method. Threads are joined to ensure that program
	main execution pauses until all concurrent threads have completed their tasks. Execution time is compared between a
	prime-finding algorithm built sequentially of a comparible design and the concurrent prime finding algorithm.
		
		n		sequential time 		concurrent time 
		10^2	0ms						5ms
		10^4	4ms						6ms
		10^5	20ms					18ms
		10^6	399ms					124ms
		10^7	10,578ms				2388ms
		10^8	293,861ms				6,1255ms
		
	While a sequential solution is preferable for small values of n(likely due to an increased overhead and complexity
	of the concurrent solution), it quickly becomes inefficient for large n.
	
Proof / Explanation
	My solution uses