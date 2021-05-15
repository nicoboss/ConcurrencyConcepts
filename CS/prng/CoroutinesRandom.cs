using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Channels;
using System.Threading.Tasks;

namespace CoroutinesRandom {
    class CoroutinesRandom {
        //Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
        private static IEnumerable<ulong> prng(ulong seed) {
            Console.WriteLine($"[CoroutinesRandom] [Coroutine] start: {Thread.CurrentThread.ManagedThreadId}");
            while (true) {
                seed += 0x9E3779B97F4A7C15;
                ulong z = seed;
                z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9;
                z = (z ^ (z >> 27)) * 0x94D049BB133111EB;
                Console.WriteLine($"[CoroutinesRandom] [Coroutine] generated: {Thread.CurrentThread.ManagedThreadId}");
                yield return (z ^ (z >> 31)) >> 31;
            }
        }

        public static async Task Main() {
            var result = Channel.CreateBounded<ulong>(20);
            _ = Task.Run(async () => {
                  Console.WriteLine($"[CoroutinesRandom] async start: {Thread.CurrentThread.ManagedThreadId}");
                  foreach (var randomNumber in prng(0)) {
                      Console.WriteLine($"[CoroutinesRandom] async enqueue: {Thread.CurrentThread.ManagedThreadId}");
                      await result.Writer.WriteAsync(randomNumber);
                  }
                  Console.WriteLine($"[CoroutinesRandom] async end: {Thread.CurrentThread.ManagedThreadId}");
              });
            Console.WriteLine($"[CoroutinesRandom] Main vor Task: {Thread.CurrentThread.ManagedThreadId}");
            System.Threading.Thread.Sleep(100);
            Console.WriteLine($"[CoroutinesRandom] Main nach Task: {Thread.CurrentThread.ManagedThreadId}");
            for (int i = 0; i < 100; ++i) {
                var randomNumber = await result.Reader.ReadAsync();
                Console.WriteLine($"[CoroutinesRandom] main dequeue {randomNumber}: {Thread.CurrentThread.ManagedThreadId}");
            }
        }
    }
}
