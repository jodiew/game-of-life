# Game of Life

Conway's Game of Life in Clojure using [Humble UI](https://github.com/HumbleUI/HumbleUI) for graphics.

![game-of-life-osc](https://user-images.githubusercontent.com/16859754/200668959-392d049a-1a67-4416-8e58-65ba7d336568.gif)
![game-of-life-glider](https://user-images.githubusercontent.com/16859754/200669870-7e9a4b01-7bc7-4b6f-9b37-76397f35cdc3.gif)

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
