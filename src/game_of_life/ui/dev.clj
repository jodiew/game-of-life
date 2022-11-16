(ns game-of-life.ui.dev
  (:require [game-of-life.core :as gol]
            
            [io.github.humbleui.ui :as ui]
            [io.github.humbleui.debug :as debug]
            [io.github.humbleui.window :as window]
            [io.github.humbleui.paint :as paint]
            
            [portal.api :as p]))

;; portal stuff
(def p (p/open))
(add-tap #'p/submit)

;; app stuff

(defonce *window (atom nil))

;; (def *active-cells (atom #{[2 2]}))

(defn cell [[x y]] 
  (ui/dynamic _ [living @gol/world
                 win @*window]
              (ui/clickable
               {:on-click (fn [_]
                            (tap> [x y])
                            (dosync
                             (if (living [x y])
                               (alter gol/world disj [x y])
                               (alter gol/world conj [x y]))
                             (tap> @gol/world))
                            (some-> win window/request-frame))}
               (ui/rect
                (if (living [x y])
                  (paint/fill 0xFF79fc72)
                  (paint/fill 0xFF444444))
                (ui/gap 50 50)))))

(def game-grid
  (ui/column
   (interpose
    (ui/gap 0 10)
    (for [col (range 5)]
      (ui/halign 0.5
                 (ui/row
                  (interpose
                   (ui/gap 10 0)
                   (for [row (range 5)]
                     (cell [row col])))))))))

(def app
  (ui/default-theme
   (ui/halign 0.5
              (ui/valign 0.5
                         game-grid))))

(ui/start-app!
 (reset! *window
         (ui/window
          {:title "Humble 🐝 UI"}
          #'app)))
(reset! debug/*enabled? true)
(send-off gol/evolver gol/evolution)

(some-> @*window window/request-frame)
