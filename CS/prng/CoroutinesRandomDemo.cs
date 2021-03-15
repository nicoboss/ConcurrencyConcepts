using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    class Program
    {

        //Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
        private static IEnumerable<ulong> prng(ulong seed)
        {
            while (true)
            {
                seed += 0x9E3779B97F4A7C15;
                ulong z = seed;
                z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9;
                z = (z ^ (z >> 27)) * 0x94D049BB133111EB;
                Console.WriteLine("Generated!");
                yield return (z ^ (z >> 31)) >> 31;
            }
        }

        public static void Main()
        {
            ConcurrentQueue<ulong> result = new ConcurrentQueue<ulong>();

            Task.Run(() =>
              {
                  foreach (var randomNumber in prng(0))
                  {
                      while (result.Count > 10) System.Threading.Thread.Yield();
                      result.Enqueue(randomNumber);
                  }
              });
            ulong randomNumber;
            for (int i = 0; i < 100; ++i)
            {
                while (!result.TryDequeue(out randomNumber)) System.Threading.Thread.Yield();
                Console.WriteLine(randomNumber);
            }
        }
    }
}
