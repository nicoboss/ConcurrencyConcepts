using System;
using System.Collections.Concurrent;
using System.Threading;
using System.Threading.Tasks;
using System.Net.Http;
using System.IO;
using System.Text;

namespace AsyncLargeFileDownload
{
    class AsyncLargeFileDownload
    {
        static async Task Main(string[] args)
        {
            ConcurrentQueue<byte[]> result = new ConcurrentQueue<byte[]>();
            var task = Task.Run(async () =>
            {
                Console.WriteLine($"[AsyncLargeFileDownload] async start: {Thread.CurrentThread.ManagedThreadId}");
                using (HttpClient client = new HttpClient())
                {
                    const string url = "http://www.nicobosshard.ch/Hi.html";
                    using (HttpResponseMessage response = await client.GetAsync(url, HttpCompletionOption.ResponseHeadersRead))
                    using (Stream streamToReadFrom = await response.Content.ReadAsStreamAsync())
                    {
                        int count = 0;
                        do
                        {
                            var buffer = new byte[10];
                            count = await streamToReadFrom.ReadAsync(buffer, 0, 10);
                            Console.WriteLine($"[AsyncLargeFileDownload] async enqueue: {Thread.CurrentThread.ManagedThreadId}");
                            result.Enqueue(buffer[0..count]);
                        } while (count > 0);
                    }
                }
                Console.WriteLine($"[AsyncLargeFileDownload] async end: {Thread.CurrentThread.ManagedThreadId}");
            });
            Console.WriteLine($"[AsyncLargeFileDownload] Main vor Task: {Thread.CurrentThread.ManagedThreadId}");
            System.Threading.Thread.Sleep(10);
            Console.WriteLine($"[AsyncLargeFileDownload] Main nach Task: {Thread.CurrentThread.ManagedThreadId}");
            while (true)
            {
                if (result.TryDequeue(out var chunk))
                {
                    Console.WriteLine($"[AsyncLargeFileDownload] Main dequeue: {Thread.CurrentThread.ManagedThreadId}");
                    if (chunk.Length == 0) break;
                    Console.WriteLine(Encoding.UTF8.GetString(chunk));
                }
                else
                {
                    System.Threading.Thread.Yield();
                }
            }
        }
    }
}
