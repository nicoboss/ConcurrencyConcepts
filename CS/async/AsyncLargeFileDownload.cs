using System;
using System.Collections.Concurrent;
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
            await Task.Run(async () =>
            {
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
                            result.Enqueue(buffer[0..count]);
                        } while (count > 0);
                    }
                }
            });
            while (true)
            {
                if (result.TryDequeue(out var chunk))
                {
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
