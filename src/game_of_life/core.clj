(ns game-of-life.core)

(def running (atom true))
(def evolve-sleep-ms 1000)
(def osc-start
  #{[3 4] [4 4] [5 4]})
(def big-osc-start
  #{#_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ]
    #_[ ] #_[ ] #_[ ] [3 1] [4 1] [5 1] #_[ ] #_[ ] #_[ ]
    #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ]
    #_[ ] [1 3] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] [7 3] #_[ ]
    #_[ ] [1 4] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] [7 4] #_[ ]
    #_[ ] [1 5] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] [7 5] #_[ ]
    #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ]
    #_[ ] #_[ ] #_[ ] [3 7] [4 7] [5 7] #_[ ] #_[ ] #_[ ]
    #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ] #_[ ]})
(def glider-start
  #{            [3 1]
    [1 2]       [3 2]
          [2 3] [3 3]})
(def stable-glider-start
  #{[0 0] [1 0]
    [0 1] [1 1]
    

                [2 4]
    [0 5]       [2 5]
          [1 6] [2 6]})

(def world (ref osc-start))

(defn neighbours-for [[x y]]
  (for [Y (range (dec y) (+ 2 y))
        X (range (dec x) (+ 2 x))
        :when (not= [x y] [X Y])]
    [X Y]))

(defn active-neighbours [board cell]
  (filter board (neighbours-for cell)))

(defn update-cell [board cell]
  (let [alive (count (active-neighbours board cell))]
    (cond
      (and (board cell)
           (<= 2 alive 3))     cell
      (and (not (board cell))
           (= 3 alive))       cell)))

(defn all-cells [board]
  (set (mapcat neighbours-for board)))

(defn evolve []
  (dosync
   (let [live-cells @world]
     (->> live-cells
          all-cells
          (map #(update-cell live-cells %))
          (remove nil?)
          set
          (ref-set world)))))

(def evolver (agent nil))

(defn evolution [_]
  (when @running
    (send-off *agent* #'evolution))
  (evolve)
  (. Thread (sleep evolve-sleep-ms))
  nil)