-module(actors_app).

-behaviour(application).

-export([run/0, start/2, stop/1]).

send_msgs(_, 0) -> true; % Rekursionsabbruch
send_msgs(Counter,
          Count) -> % Rekursion
    Counter ! {inc, 1}, % Increment message senden
    send_msgs(Counter, Count - 1). % Rekursiever Aufruff

run() ->
    io:fwrite("Start counting...\n"),
    Counter = spawn(counter, counter, [0]),
    send_msgs(Counter, 1000000), % Eine Million Messages
    Counter.

start(_StartType, _StartArgs) ->
    Result = run(),
    Result ! value,
    actors_sup:start_link().

stop(_State) -> ok.
