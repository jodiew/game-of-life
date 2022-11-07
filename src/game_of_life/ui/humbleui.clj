(ns game-of-life.ui.humbleui
  (:require [game-of-life.core :as gol]

            [io.github.humbleui.ui :as ui]
            [io.github.humbleui.canvas :as canvas]
            [io.github.humbleui.paint :as paint]
            [io.github.humbleui.core :as core]
            [io.github.humbleui.window :as window])
  (:import [io.github.humbleui.skija Canvas Color PaintMode Path]))

(defonce *window (atom nil))
(def dim 80)

(defn paint [ctx canvas size]
  (let [field (min (:width size) (:height size))
        scale (/ field dim)]
    ;; center canvas
    (canvas/translate canvas
                      (-> (:width size) (- field) (/ 2))
                      (-> (:height size) (- field) (/ 2)))
    ;; scale to fit full width or height
    (canvas/scale canvas scale scale)
    ;; erase background
    (with-open [bg (paint/fill 0xFFFFFFFF)]
      (canvas/draw-rect canvas (core/rect-xywh 0 0 dim dim) bg))
    
    (let [live-cells @gol/world
          board      (gol/all-cells live-cells)
          min-x      (apply min (map first board))
          max-x      (inc (apply max (map first board)))
          min-y      (apply min (map second board))
          max-y      (inc (apply max (map second board)))
          cell-size  (quot dim (max (- max-x min-x) (- max-y min-y)))]
      (doseq [x (range min-x max-x)
              y (range min-y max-y)]
        (let [Y (+ (* (- min-y) cell-size) (* y cell-size))
              X (+ (* (- min-x) cell-size) (* x cell-size))]
          (if (live-cells [x y])
            (canvas/draw-rect canvas
                              (core/rect-xywh X Y cell-size cell-size)
                              (paint/fill 0xFFACFFAC))
            (canvas/draw-rect canvas
                              (core/rect-xywh X Y cell-size cell-size)
                              (paint/stroke 0xFFBFBFBF 0.1))))))
    
    ;; schedule redraw on next vsync
    (window/request-frame (:window ctx))))

(def app
  (ui/default-theme
   (ui/center
    (ui/canvas
     {:on-paint paint}))))

(defn -main [& args]
  (ui/start-app!
   (reset! *window
           (ui/window
            {:title "Game of Life"}
            #'app)))
  (send-off gol/evolver gol/evolution))

(-main)