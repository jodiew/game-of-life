(ns game-of-life.ui.terminal
  (:require [game-of-life.core :refer [evolve board-range]]
            [clojure.string]))

(defn print-board [board]
  (let [{:keys [min-x min-y max-x max-y]} (board-range board)]
    (clojure.string/join
     "\n"
     (map #(clojure.string/join " " %)
          (for [y (range min-y max-y)]
            (for [x (range min-x max-x)]
              (if (board [x y])
                "X"
                "O")))))))

(println (print-board #{[-1 0] [0 0] [1 0]}))

(doall (map #(println (print-board %) "\n") (take 5 (iterate evolve #{[-1 0] [0 0] [1 0]}))))
