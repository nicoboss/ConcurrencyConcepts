module Main where

import qualified Control.Concurrent.STM as T
import Control.Monad (forM)
import Control.Concurrent (forkFinally)
import Control.Concurrent.MVar

type Counter = T.TMVar Integer
    
newCounter :: Integer -> T.STM (Counter)
newCounter value = T.newTMVar value

getCounter :: Counter -> T.STM Integer
getCounter counter = do
    value <- T.takeTMVar counter
    return value

incrementCounter :: Counter -> T.STM ()
incrementCounter counter = do
    oldValue <- T.takeTMVar counter
    let newValue = (oldValue + 1)
    T.putTMVar counter newValue

forkThread :: IO () -> IO (MVar ())
forkThread proc = do
    handle <- newEmptyMVar
    _ <- forkFinally proc (\_ -> putMVar handle ())
    return handle

main :: IO ()
main = do
    counter <- T.atomically (newCounter 0)
    threads <- forM [1..1000000] $ \_ -> do
        forkThread $ T.atomically (incrementCounter counter)
    mapM_ takeMVar threads -- Wait for all threads to finish
    value <- T.atomically $ getCounter counter
    print $ value
