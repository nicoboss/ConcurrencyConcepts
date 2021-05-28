# WIPRO: Nebenläufige Programmiermodelle

## Abstrakt
Mit der Zunahme der verfügbaren CPU-Cores gewinnt die parallele Programmierung immer mehr an Bedeutung. Das Ziel in dieser Arbeit ist es, eine Übersicht über die aktuellen nebenläufigen Programmiermodelle und nebenläufigen Kontrollkonzepte zu erstellen. Die Analyse und der Vergleich werden mit einer Auswahl verschiedener Programmiersprachen realisiert. Welche Sprachen welche parallelen Programmiermodelle und Nebenläufigkeitskon¬zepte unterstützen wird mit Codebeispielen analysiert, demonstriert, verglichen, kommentiert und zusammenfassend in tabellarischer Form festgehalten. Es werden die Programmiersprachen Java, Golang, C#, Erlang, Kotlin und Haskell untersucht. Für die neben¬läufigen Kontrollkonzepte werden Atomics, Mutex, Software transactional Memory, Channels und Messages angeschaut. Bei den nebenläufigen Programmiermodellen werden Thread Pools, Futures/Tasks, Asynchrone Programmierung, eventbasierte Parallelisierung / Reactive, Continuation, Coroutines, Fibers, Message passing Channels und Actor Concurrency analysiert und verglichen. Sämtliche Codebeispiele für alle Programmiersprachen können in einer einzigen Entwicklungsumgebung geöffnet und ausgeführt werden.

## Entwicklungsumgebung einrichten
1. Visual Studio Code installieren: https://code.visualstudio.com/
2. Settings Sync Extension herunterladen: https://marketplace.visualstudio.com/items?itemName=Shan.code-settings-sync 
3. cloudSettings Ordner auf persönliches GitHub Gist hochladen: https://gist.github.com/
4. Visual Studio Code Einstellungen mit Settings Sync anwenden (Windows: Shift + Alt + D, macOS: Shift + Option + D)
5. Externe Abhängigkeiten wie Java OpenJDK 17 mit Loom, Maven, Haskell, Erlang und .NET Core wie in den Extensions beschrieben installieren.
