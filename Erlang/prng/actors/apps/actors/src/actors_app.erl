%%%-------------------------------------------------------------------
%% @doc actors public API
%% @end
%%%-------------------------------------------------------------------

-module(actors_app).

-behaviour(application).

-export([start/2, stop/1]).

start(_StartType, _StartArgs) ->
    actors_sup:start_link().

stop(_State) ->
    ok.

%% internal functions
