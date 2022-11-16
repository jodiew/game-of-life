(ns game-of-life.ui.can-click 
  (:require [io.github.humbleui.ui :as ui]
            [io.github.humbleui.window :as window]
            [io.github.humbleui.debug :as debug]
            [io.github.humbleui.canvas :as canvas]
            [io.github.humbleui.core :as core]
            [io.github.humbleui.paint :as paint]
            
            [portal.api :as p])
  (:import [io.github.humbleui.types IPoint]
           [io.github.humbleui.skija Canvas]))

;; portal setup
(def p (p/open))
(add-tap #'p/submit)

;; state
(def *window (atom nil))
(def *app (atom nil))
(def world (ref #{[3 4] [4 4] [5 4]}))

;; app
(def dim 9)

(defn on-event [ctx e]
  (when (and (#{:mouse-button} (:event e))
             (:pressed? e))
    (dosync
     (let [win-rect (window/content-rect (:window ctx))
           width (.getWidth win-rect)
           height (.getHeight win-rect)
           size (min width height)
           square-size (/ size dim)
           live-cells @world
           x (int (quot (:x e) square-size))
           y (int (quot (:y e) square-size))]
       (tap> {:my-x x
              :my-y y
              :world live-cells})
       (if (live-cells [x y])
         (alter world disj [x y])
         (alter world conj [x y]))
    ;;   (tap> e)
       ))
    true))

(defn on-paint [ctx ^Canvas canvas ^IPoint size]
  (let [{:keys [scale]} ctx
        {:keys [width height]} size
        size (min width height)
        square-size (/ size dim)
        gap-size (/ square-size 50)
        live-cells @world]
    (tap> {:scale scale
           :width width
           :height height
           :size size
           :dim dim
           :square-size square-size
           :gap-size gap-size
           :world live-cells})
    (canvas/with-canvas canvas
      (doseq [x (range dim)
              y (range dim)]
        (canvas/draw-rect canvas
                          (core/rect-xywh (+ (* square-size x) (/ gap-size 2))
                                          (+ (* square-size y) (/ gap-size 2))
                                          (- square-size gap-size)
                                          (- square-size gap-size))
                          (if (live-cells [x y])
                            (paint/fill 0xFF79bf77)
                            (paint/fill 0xFF787878))))
      )))

(def app-ui
  (ui/center
   (ui/canvas {:on-paint on-paint
               :on-event on-event})))

;; window setup
(def app
  (ui/default-theme {}
                    app-ui))

(reset! *app app)

(defn redraw []
  (some-> @*window window/request-frame)
  :redraw)

(redraw)

(defn -main [& args]
  (ui/start-app!
   (reset! *window
           (ui/window
            {:title "Game of Life"
             :width 500
             :height 500
             :x :left
             :y :top}
            *app)))
  (reset! debug/*enabled? true)
  (redraw))

(comment
  (-main)
  )