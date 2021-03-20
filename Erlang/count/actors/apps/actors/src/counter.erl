-module(counter).
-export([counter/1]).

counter(Value) ->
    receive
        value -> io:fwrite("Done! Counter is ~w!~n", [Value]);
        {inc, Amount} -> counter(Value + Amount)
    end.
