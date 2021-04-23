-module(consumer).
-export([consumer/0]).
consumer() ->
    receive
        {rand, RandomNumber} -> io:fwrite("Recieving ~w!~n", [RandomNumber]), consumer()
    end.
