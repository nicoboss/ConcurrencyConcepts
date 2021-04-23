module Main where

import Control.Monad (forM_)
import Control.Concurrent (forkIO, threadDelay)
import Control.Concurrent.QSem (newQSem, waitQSem, signalQSem)
import Control.Concurrent.Chan (newChan, writeChan, readChan)
import Data.Bits
import Data.Word

-- Basieren auf C++ splitmix PRNG von Arvid Gerstmann.
main = do
    channel <- newChan
    guard <- newQSem 10
    let seed = 0
    forkIO (forM_ [1..] $ \i -> do
            waitQSem guard
            let currentSeed = seed + (0x9E3779B97F4A7C15 * i) :: Word64
            let z1 = (currentSeed `xor` (currentSeed `shiftR` 30)) * 0xBF58476D1CE4E5B9
            let z2 = (z1 `xor` (z1 `shiftR` 27)) * 0x94D049BB133111EB
            let result = (z2 `xor` (z2 `shiftR` 31)) `shiftR` 31
            writeChan channel result
            putStrLn "Generated!"
        )
    forM_ [1..100] $ \_ -> do
        threadDelay (1000)
        signalQSem guard
        print =<< readChan channel
