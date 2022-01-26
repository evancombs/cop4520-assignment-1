import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
// The handler class for our multithreaded prime-finding solution
class FindPrimes
{
  public static void main(String[] args)
  {
    /*
    for (int i = 0; i <= 32; i++)
    {
      System.out.println("Checking " + i + "... " + (isPrime(i) ? "Prime!" : "Not prime!"));
    }*/
    // FindPrimesSequential((int) 10e2);
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
    System.out.println("Preparing to dispatch threads...");

    // Thread[] threads = new Thread[numThreads];
    ArrayList<Thread> threads = new ArrayList<>();

    // Create a new threadRunner with the desire number to find primes up to
    ThreadRunner threadRunner = new ThreadRunner((int) max);
    for (int i = 0; i < numThreads; i++)
    {
      Thread thread = new Thread(threadRunner);
      threads.add(thread);
      thread.start();
      // threads.get(i)
      // threads[i] = new Thread(threadRunner);
      // threads[i].start();
    }

    System.out.println("Dispatched threads!");
    System.out.println("Preparing to join threads...");
    try
    {
      for (Thread thread : threads)
      {
        thread.join();
        ;
      }
    }
    catch (Exception e)
    {
      System.out.println("Caught an exception!");
    }
    System.out.println("Joined threads!");
    /*
    for (int i = 0; i < numThreads; i++)
    {
      threads.get(i).join();
    }*/
    System.out.println("Reached end of main!");

    System.out.println("Primes found: " + threadRunner.GetPrimesFound());
    System.out.println("Sum of primes: " + threadRunner.GetSumOfPrimes());
  }

  // Returns all the prime numbers up to n
  public static void FindPrimesSequential(int n)
  {
    for (int i = 0; i < n; i++)
    {
      if (isPrime(i))
        System.out.println(i);
    }
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
      // System.out.println("Checking factor " + i + " on number " + n);
      if (n % i == 0)
        return false;
    }
    return true;
  }
}

// PrimeThread is a single thread that will attempt to find a prime.
class ThreadRunner implements Runnable
{
  static AtomicInteger counter = new AtomicInteger(1);
  AtomicBoolean endFlag;
  public AtomicInteger primesFound;
  public AtomicInteger sumOfPrimes;
  public int n;

  public ThreadRunner(int numberUpTo)
  {
    n = numberUpTo;
    primesFound = new AtomicInteger(0);
    sumOfPrimes = new AtomicInteger(0);
    endFlag = new AtomicBoolean(false);
  }
  public void run()
  {
    int current = 0;

    while (endFlag.get() && (current = counter.getAndIncrement()) <= n)
    {
      //current = counter.addAndGet(1);
      if (FindPrimes.isPrime(current))
      {
        // System.out.println(current);
        primesFound.getAndIncrement();
        sumOfPrimes.addAndGet(current);
      }
    }
    /*
    for (int current = counter.getAndIncrement(); current <= n; )
    {
      if (FindPrimes.isPrime(current))
      {
        System.out.println(current);
        primesFound.getAndIncrement();
        sumOfPrimes.addAndGet(current);
      }
    }*/

    if (!endFlag.get())
      ;// PrintSummary();
    endFlag.set(true);
  }
  public int GetPrimesFound()
  {
    return primesFound.get();
  }

  public int GetSumOfPrimes()
  {
    return sumOfPrimes.get();
  }
}
