Compile with "javac FindPrimes.java"
Compilation generates FindPrimes.class and ThreadRunner.class

Input
	Compile with "java FindPrimes.java"
	Run with "java FindPrimes <number of threads> <number to find primes up to>"
	
	Example:
	"java FindPrimes 8 1e8"
	This will find all primes up to 10^8 using 8 threads.
	
Output
	Outputs to file "output.txt" with following format:
	Execution time: <time>
	Number of primes found: <primes found
	Sum of all primes: <sum of primes found>

	Top ten maximum primes
	<prime 10>
	<prime 10>
	<prime 10>
	<prime 10>
	<prime 10>
	<prime 10>
	<prime 10>
	<prime 10>
	<prime 10>
	<prime 10>
	
Experimental Evaluation / Efficiency
	Empirical analysis is performed using the System.currentTimeMillis() method. Threads are joined to ensure that program
	main execution pauses until all concurrent threads have completed their tasks. 
	
		n		sequential time 		concurrent time 
		10^2	0ms						4ms
		10^3	0ms						4ms
		10^4	2ms						5ms
		10^5	18ms					18ms
		10^6	392ms					88ms
		10^7	10,578ms				676ms
		10^8	293,861ms				11,186ms
			
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