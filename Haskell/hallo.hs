module Main where

import qualified Control.Concurrent.STM as T
import Control.Monad (forM_)
import Control.Concurrent (forkIO, threadDelay)

type Counter = T.TMVar Integer
    
newCounter :: Integer -> T.STM (Counter)
newCounter value = T.newTMVar value

getCounter :: Counter -> T.STM Integer
getCounter counter = do
    value <- T.takeTMVar counter
    return value

setCounter :: Counter -> Integer -> T.STM ()
setCounter counter value = do
    T.putTMVar counter value

incrementCounter :: Counter -> T.STM ()
incrementCounter counter = do
    oldValue <- T.takeTMVar counter
    let newValue = (oldValue + 1)
    T.putTMVar counter newValue

plusCounter :: Counter -> Integer -> T.STM ()
plusCounter counter value = do
    oldValue <- T.takeTMVar counter
    let newValue = (oldValue + value)
    T.putTMVar counter newValue

sleepMs n = threadDelay (n * 1000)

main :: IO ()
main = do
    counter <- T.atomically (newCounter 0)
    threads <- forM_ [1..1000000] $ \_ -> do
        forkIO $ T.atomically (incrementCounter counter)
    sleepMs 1000
    value <- T.atomically $ getCounter counter
    print $ value
