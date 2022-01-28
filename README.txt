Compile with "javac FindPrimes.java"
Compilation generates FindPrimes.class and ThreadRunner.class

Input
	Run with "java FindPrimes <number of threads> <number to find primes up to>"
	
Output
	Outputs to file "output.txt" with following format:
	<
Experimental Evaluation
	Empirical analysis is performed using the System.currentTimeMillis() method. Threads are joined to ensure that program
	main execution pauses until all concurrent threads have completed their tasks. Execution time is compared between a
	prime-finding algorithm built sequentially of a comparible design and the concurrent prime finding algorithm.
		
		n		sequential time 		concurrent time 
		10^2	0ms						5ms
		10^4	4ms						6ms
		10^5	20ms					18ms
		10^6	399ms					124ms
		10^7	10,578ms				1,063ms
		10^8	293,861ms				11,186ms
		
		n		sequential time 		concurrent time 		sequential / n			concurrent / n
		10^2	0ms						4ms						0
		10^4	1ms						5ms						0.001
		10^5	5ms						17ms					0.0005
		10^6	17ms					93ms					0.000017				0.000093
		10^7	124ms					103ms					0.0000124				0.0000103
		10^8	1514ms					6921ms					0.00001514				0.00006921
		
	While a sequential solution is preferable for small values of n(likely due to an increased overhead and complexity
	of the concurrent solution), it quickly becomes inefficient for large n.
	
Proof / Explanation
	My solution implements the sieve of eratosthenes in a concurrent context. An array of atomic booleans
	replace the typical sieve boolean array, and each number is assigned to a thread from an atomic counter.
	
	It may be expected that ocassionally, threads will wait on one another; for instance, if numbers 3 and 5 were
	being handled by separate threads, one would have to wait for the other to set 15 as false, as both would need to.
	This may be considered repeated computation, and there is likely a maximum number of threads before the overhead
	exceed the benefits.
	
	The solution benefits from concurrency as each thread can handle marking the sieve for a different prime number.
	Aside from cases such as mentioned above, more threads allow more primes to be marked from the sieve concurrently.