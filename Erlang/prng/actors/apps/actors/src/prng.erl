-module(prng).
-export([prng/1]).
send_msgs(_, 0, _) -> true; % Rekursionsabbruch
send_msgs(Consumer,
          Count,
          Seed) -> % Rekursion
    SeedNew = Seed + 11400714819323198485,
	Z1 = (SeedNew bxor (SeedNew bsr 30)) * 13787848793156543929,
	Z2 = (Z1 bxor (Z1 bsr 27)) * 10723151780598845931,
	Result = (Z2 bxor (Z2 bsr 31)) bsr 31,
    Consumer ! {rand, Result}, % Message senden
    send_msgs(Consumer, Count - 1, SeedNew). % Rekursiever Aufruff
prng(Seed) ->
    io:fwrite("Start Generating...\n"),
    Consumer = spawn(consumer, consumer, []),
    send_msgs(Consumer, 100, Seed). % Eine Million Messages
