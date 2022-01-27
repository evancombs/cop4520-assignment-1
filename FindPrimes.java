import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collections;
import java.util.ArrayList;
import java.util.*;
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
    FindPrimesSequential(max);
    FindPrimesConcurrent(numThreads, max);
  }

  // Returns all the prime numbers up to n
  public static void FindPrimesSequential(int max)
  {
    System.out.println("Finding all primes up to " + max + " sequentially! This may take some time...");
    long startTime = System.currentTimeMillis();

    long sumOfPrimes = 0;
    int primesFound = 0;
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

  public static void FindPrimesConcurrent(int numThreads, int max)
  {
    System.out.println("Finding all primes up to " + max + " sequentially! This may take some time...");
    long startTime = System.currentTimeMillis();

    ArrayList<Thread> threads = new ArrayList<>();

    // Create a new threadRunner with the desire number to find primes up to
    ThreadRunner threadRunner = new ThreadRunner((int) max);
    for (int i = 0; i < numThreads; i++)
    {
      Thread thread = new Thread(threadRunner);
      threads.add(thread);
      thread.start();
    }
    // Join threads, so that the program waits until they have all completed
    try
    {
      for (Thread thread : threads)
        thread.join();
    }
    catch (Exception e)
    {
      System.out.println("Caught an exception!");
    }

    long endTime = System.currentTimeMillis();
    System.out.println("Execution time: " + (endTime - startTime));
    System.out.println("Primes found: " + threadRunner.GetPrimesFound());
    System.out.println("Sum of primes: " + threadRunner.GetSumOfPrimes());
    threadRunner.PrintTopTenPrimes();
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
}


class ThreadRunner implements Runnable
{
  // Thread control members
  static AtomicInteger counter = new AtomicInteger(1);
  AtomicBoolean endFlag;
  public int n;

  // Execution results
  public AtomicInteger primesFound;
  public AtomicLong sumOfPrimes;

  int[] topTenPrimes;

  public ThreadRunner(int numberUpTo)
  {
    n = numberUpTo;
    primesFound = new AtomicInteger(0);
    sumOfPrimes = new AtomicLong(0);
    endFlag = new AtomicBoolean(false);

    topTenPrimes = new int[10];
  }
  public void run()
  {
    int current = 0;

    while (endFlag.get() && (current = counter.getAndIncrement()) <= n)
    {
      if (FindPrimes.isPrime(current))
      {
        topTenPrimes[primesFound.get() % 10] = current;
        primesFound.getAndIncrement();
        sumOfPrimes.addAndGet(current);
      }
    }
    endFlag.set(true);
  }
  public int GetPrimesFound()
  {
    return primesFound.get();
  }

  public long GetSumOfPrimes()
  {
    return sumOfPrimes.get();
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
