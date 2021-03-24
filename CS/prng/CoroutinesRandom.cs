using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace CoroutinesRandom
{
    class CoroutinesRandom
    {
        //Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
        private static IEnumerable<ulong> prng(ulong seed)
        {
            Console.WriteLine($"[CoroutinesRandom] [Coroutine] start: {Thread.CurrentThread.ManagedThreadId}");
            while (true)
            {
                seed += 0x9E3779B97F4A7C15;
                ulong z = seed;
                z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9;
                z = (z ^ (z >> 27)) * 0x94D049BB133111EB;
                Console.WriteLine($"[CoroutinesRandom] [Coroutine] generated: {Thread.CurrentThread.ManagedThreadId}");
                yield return (z ^ (z >> 31)) >> 31;
            }
        }

        public static void Main()
        {
            ConcurrentQueue<ulong> result = new ConcurrentQueue<ulong>();

            Task.Run(() =>
              {
                  Console.WriteLine($"[CoroutinesRandom] async start: {Thread.CurrentThread.ManagedThreadId}");
                  foreach (var randomNumber in prng(0))
                  {
                      while (result.Count > 10) System.Threading.Thread.Yield();
                      Console.WriteLine($"[CoroutinesRandom] async enqueue: {Thread.CurrentThread.ManagedThreadId}");
                      result.Enqueue(randomNumber);
                  }
                  Console.WriteLine($"[CoroutinesRandom] async end: {Thread.CurrentThread.ManagedThreadId}");
              });

            Console.WriteLine($"[CoroutinesRandom] Main vor Task: {Thread.CurrentThread.ManagedThreadId}");
            System.Threading.Thread.Sleep(10);
            Console.WriteLine($"[CoroutinesRandom] Main nach Task: {Thread.CurrentThread.ManagedThreadId}");
            ulong randomNumber;
            for (int i = 0; i < 100; ++i)
            {
                while (!result.TryDequeue(out randomNumber)) System.Threading.Thread.Yield();
                Console.WriteLine($"[CoroutinesRandom] main dequeue {randomNumber}: {Thread.CurrentThread.ManagedThreadId}");
            }
        }
    }
}
