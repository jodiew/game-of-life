# Game of Life

Conway's Game of Life in Clojure using [Humble UI](https://github.com/HumbleUI/HumbleUI) for graphics.

Based on:

- [Game of Life Kata in Clojure by Michael Whatcott](https://www.youtube.com/watch?v=15WJqtGbaH8)
- [Rich Hickey’s Ants demo in Humble UI](https://github.com/tonsky/humble-ants)

## Running

vscode & Calva:

1. **Start a Project REPL and Connect (aka Jack-In)**
1. Select **deps.edn** as project type
1. Run **Evaluate Current File** for core.clj and humbleui.clj

terminal:

```bash
clj -M -m game-of-life.ui.humbleui --interactive
```
