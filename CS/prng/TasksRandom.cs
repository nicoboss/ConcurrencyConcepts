using System;
using System.Collections.Concurrent;
using System.Threading.Tasks;

namespace TasksRandom
{
    class TasksRandom
    {
        public static void Main()
        {
            ConcurrentQueue<ulong> result = new ConcurrentQueue<ulong>();

            Task.Run(() =>
              {
                  ulong seed = 0;
                  while (true)
                  {
                      //Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
                      while (result.Count > 10) System.Threading.Thread.Yield();
                      seed += 0x9E3779B97F4A7C15;
                      ulong z = seed;
                      z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9;
                      z = (z ^ (z >> 27)) * 0x94D049BB133111EB;
                      Console.WriteLine("[TASK] Generated!");
                      result.Enqueue((z ^ (z >> 31)) >> 31);
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
