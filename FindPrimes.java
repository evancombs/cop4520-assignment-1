import java.util.concurrent.atomic.AtomicInteger;
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
    int numThreads = 8;
    Thread[] threads = new Thread[numThreads];
    ThreadRunner threadRunner = new ThreadRunner((int) 10e8);
    for (int i = 0; i < numThreads; i++)
    {
      threads[i] = new Thread(threadRunner);
      threads[i].start();
    }
  }

  // Returns all the prime numbers up to n
  public static void FindPrimesSequential(int n)
  {
    for (int i = 0; i <= n; i++)
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

    for (int i = 2; i < Math.sqrt(n); i++)
    {
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
  boolean endFlag;
  public AtomicInteger primesFound;
  public AtomicInteger sumOfPrimes;
  public int n;

  public ThreadRunner(int numberUpTo)
  {
    n = numberUpTo;
    primesFound = new AtomicInteger(0);
    sumOfPrimes = new AtomicInteger(0);
    endFlag = false;
  }
  public void run()
  {
    int current = 0;
    while (current <= n)
    {
      current = counter.addAndGet(1);
      if (FindPrimes.isPrime(current))
      {
        //System.out.println(current);
        primesFound.getAndIncrement();
        sumOfPrimes.addAndGet(current);
      }
    }
    if (!endFlag)
      PrintSummary();
    endFlag = true;
  }

  private PrintSummary()
  {

  }
}
