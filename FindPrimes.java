import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
// import java.util.concurrent.CopyOnWriteArrayList;
// import java.util.Collections;
import java.util.ArrayList;
import java.util.*;
import java.io.*;

// The handler class for our multithreaded prime-finding solution
class FindPrimes
{
  public static void main(String[] args)
  {
    int numThreads = 0;
    int max = 0;
    try
    {
      numThreads = Integer.parseInt(args[0]);
      max = (int) Double.valueOf(args[1]).doubleValue();
    }
    catch(Exception e)
    {
      System.out.println(e);
      System.out.println("Error -- Run with \"java FindPrimes.java <number of threads> <number to search to>\"");
      return;
    }
    // FindPrimesSequential(max);
    // FindPrimesSequentialSieve(max);
    FindPrimesConcurrent(numThreads, max);
  }

  // Returns all the prime numbers up to n in a bruteforce method
  public static void FindPrimesSequential(int max)
  {
    System.out.println("Finding all primes up to " + max + " sequentially! This may take some time...");
    long startTime = System.currentTimeMillis();

    long sumOfPrimes = 0;
    int primesFound = 0;

    // We simply check every possible number, to see if it is prime.
    for (int i = 0; i < max; i++)
    {
      if (isPrime(i))
      {
        sumOfPrimes += i;
        primesFound++;
      }
    }
    long endTime = System.currentTimeMillis();
    System.out.println("Execution time: " + (endTime - startTime));
    System.out.println("Primes found: " + primesFound);
    System.out.println("Sum of primes: " + sumOfPrimes);
  }

  // Returns all prime numbers up to max using a prime sieve
  public static void FindPrimesSequentialSieve(int max)
  {
    boolean[] isPrime = new boolean[max];
    long sumOfPrimes = 0;
    int primesFound = 0;

    System.out.println("Finding all primes up to " + max + " sequentially, with a SIEVE! This may take some time...");
    long startTime = System.currentTimeMillis();

    // Initialize prime array
    for (int i = 0; i < max; i++)
      isPrime[i] = true;

    for (int i = 2; i < max; i++)
    {
      if (isPrime[i])
      {
        sumOfPrimes += i;
        primesFound++;
        for (int j = i; j < max; j += i)
        {
            isPrime[j] = false;
        }
      }
    }

    long endTime = System.currentTimeMillis();
    System.out.println("Execution time: " + (endTime - startTime));
    System.out.println("Primes found: " + primesFound);
    System.out.println("Sum of primes: " + sumOfPrimes);
  }

  // Returns all prime numbers up to max, using numThreads threads.
  public static void FindPrimesConcurrent(int numThreads, int max)
  {
    System.out.println("Finding all primes up to " + max + " concurrently! This may take some time...");
    long startTime = System.currentTimeMillis();

    ArrayList<Thread> threads = new ArrayList<>();

    // Create a new threadRunner with the desired number to find primes up to
    ThreadRunner threadRunner = new ThreadRunner((int) max);
    for (int i = 0; i < numThreads; i++)
    {
      Thread thread = new Thread(threadRunner);
      threads.add(thread);
    }

    for (Thread thread : threads)
        thread.start();

    // Join threads, so that the execution waits until they have all completed
    try
    {
      for (Thread thread : threads)
        thread.join();
    }
    catch (Exception e)
    {
      System.out.println("Failed joining threads!");
    }

    long endTime = System.currentTimeMillis();
    threadRunner.countSieve();

    // System.out.println("Execution time: " + (endTime - startTime));
    // System.out.println("Primes found: " + threadRunner.GetPrimesFound());
    // System.out.println("Sum of primes: " + threadRunner.GetSumOfPrimes());
    // threadRunner.PrintTopTenPrimes();

    WriteResultsToFile("output.txt", endTime - startTime, threadRunner.GetSumOfPrimes(),
                        threadRunner.GetPrimesFound(), threadRunner.getTopTenPrimes());
  }
  // A naive but simple method to check if a number is prime, by checking all
  // numbers up to it.
  public static boolean isPrime(int n)
  {
    // 1 and 0 are explicitly not prime
    if (n == 1 || n == 0)
      return false;

    // A number n can't have a factor greater than sqrt(n)
    for (int i = 2; i <= Math.sqrt(n); i++)
    {
      if (n % i == 0)
        return false;
    }
    return true;
  }
  private static void WriteResultsToFile(String filename, long executionTime, long sumOfPrimes, int primesFound, int[] topTen)
  {
    try
    {
      File file = new File(filename);
      PrintWriter writer = new PrintWriter(file);
      // writer.println("Test");
      writer.println("Execution time: " + executionTime);
      writer.println("Number of primes found: " + primesFound);
      writer.println("Sum of all primes: " + sumOfPrimes);

      writer.println("\nTop ten maximum primes");

      Arrays.sort(topTen);
      //System.out.println("Top ten largest primes found:");
      for (int i = 0; i < topTen.length; i++)
      {
        //System.out.println(topTenPrimes[i]);
        writer.println(topTen[i]);
      }
      writer.close();
    }
    catch(Exception e)
    {
      System.out.println("Failed to write output file!");
    }
  }
}


class ThreadRunner implements Runnable
{
  // Thread control members
  static AtomicInteger counter = new AtomicInteger(2);
  AtomicBoolean endFlag;
  public int n;

  AtomicBoolean[] isPrime;

  // Execution results
  public AtomicInteger primesFound;
  public AtomicLong sumOfPrimes;

  // Holds the last 10
  int[] topTenPrimes;

  public ThreadRunner(int numberUpTo)
  {
    n = numberUpTo;
    primesFound = new AtomicInteger(0);
    sumOfPrimes = new AtomicLong(0);
    endFlag = new AtomicBoolean(false);

    isPrime = new AtomicBoolean[numberUpTo];

    // Initialize sieve array
    for (int i = 0; i < numberUpTo; i++)
    {
        isPrime[i] = new AtomicBoolean(true);
    }

    topTenPrimes = new int[10];
  }
  public void run()
  {
    int current = 0;


    while((current = counter.getAndIncrement()) < Math.sqrt(n))
    {
      if(isPrime[current].get())
      {
        // System.out.println(current + " is prime!");
        // Fill out the sieve for this value, starting at the next multiple
        // Ex: 5 is the current value. We want to count 5 later, so we start at
        // 25, and mark 25, 30, 35,...
        for (int i = current * current; i < n; i += current)
          isPrime[i].set(false);
      }
    }
  }

  public void sieve()
  {
    int current = 0;
    while((current = counter.getAndIncrement()) < Math.sqrt(n))
    {
      if(isPrime[current].get())
      {
        // primesFound.getAndIncrement();
        // sumOfPrimes.addAndGet(current);
        // topTenPrimes[primesFound.get() % 10] = current;

        // Fill out the sieve for this value, starting at the next multiple
        for (int i = current * current; i < n; i += current)
          isPrime[i].set(false);
        //for (int i = current * current)
      }
    }
  }

  public void countSieve()
  {
    for (int i = 2; i < n; i++)
    {
      if(isPrime[i].get())
      {
        primesFound.getAndIncrement();
        sumOfPrimes.addAndGet(i);
        topTenPrimes[primesFound.get() % 10] = i;
      }
    }
  }
  public int GetPrimesFound()
  {
    return primesFound.get();
  }

  public long GetSumOfPrimes()
  {
    return sumOfPrimes.get();
  }

  public int[] getTopTenPrimes()
  {
    return topTenPrimes;
  }

  public void PrintTopTenPrimes()
  {
    Arrays.sort(topTenPrimes);
    System.out.println("Top ten largest primes found:");
    for (int i = 0; i < topTenPrimes.length; i++)
    {
      System.out.println(topTenPrimes[i]);
    }
  }
}
