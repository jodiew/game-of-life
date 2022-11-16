(ns game-of-life.ui.hexagon 
  (:require [io.github.humbleui.debug :as debug]
            [io.github.humbleui.ui :as ui]
            [io.github.humbleui.window :as window]
            [io.github.humbleui.paint :as paint]
            [io.github.humbleui.core :as core]
            [io.github.humbleui.canvas :as canvas])
  (:import [io.github.humbleui.skija Canvas Paint]
           [io.github.humbleui.types Point]))

(defonce *window (atom nil))

;; (defn draw-hexagon [^Canvas canvas p ^Paint paint]
;;   (.drawPolygon canvas p paint))

;; (defn paint [ctx canvas size]
;;   (draw-hexagon canvas
;;                 [(Point. 0 0) (Point. 0 10) (Point. 10 10) (Point. 10 0)]
;;                 (paint/stroke 0xFF000000 0.2)))

(defn polygon-points [s]
  (mapcat #(list (Math/cos (+ % (/ 8 Math/PI))) (Math/sin (+ % (/ 8 Math/PI)))) (range 0 (+ (* 2 Math/PI) (/ (* 2 Math/PI) s)) (/ (* 2 Math/PI) s))))

(defn paint [ctx canvas size]
  (canvas/draw-line canvas
                    0 0 500 500
                    (paint/stroke 0xFFFFCC33 5))
  (.drawPoint canvas 550 550 (paint/stroke 0xFF000000 5))
  (.drawPolygon canvas (float-array (map #(+ 100 (* 100 %)) (polygon-points 8)))
                (paint/stroke 0xFF000000 5))
  (.drawPolygon canvas (float-array (map #(+ 500 (* 100 %)) (polygon-points 6)))
                (paint/stroke 0xFF000000 5))
;;   (.drawPolygon canvas (float-array
;;                         (map #(+ (* 20 %) 100) [1 2.41
;;                                                 2.41 1
;;                                                 2.41 -1
;;                                                 1 -2.41
;;                                                 -1 -2.41
;;                                                 -2.41 -1
;;                                                 -2.41 1
;;                                                 -1 2.41
;;                                                 1 2.41
;;                                                 ]))
;;                 (paint/stroke 0xFF000000 5))
  (window/request-frame (:window ctx)))

(def app
  (ui/default-theme
   (ui/center
    (ui/canvas
     {:on-paint paint}))))

(ui/start-app!
 (reset! *window
         (ui/window
          {:title "Humble 🐝 UI"}
          #'app)))
(reset! debug/*enabled? true)

(some-> @*window window/request-frame)
