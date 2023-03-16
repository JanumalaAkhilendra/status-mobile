(ns status-im2.common.bottom-sheet-screen.style
  (:require
    [quo2.foundations.colors :as colors]
    [react-native.reanimated :as reanimated]))

(defn background
  [opacity]
  (reanimated/apply-animations-to-style
   {:opacity opacity}
   {:background-color colors/neutral-100-opa-70
    :position         :absolute
    :top              0
    :bottom           0
    :left             0
    :right            0}))

(defn main-view
  [translate-y]
  (reanimated/apply-animations-to-style
<<<<<<< HEAD
   {:transform [{:translate-y translate-y}]}
   {:background-color        (colors/theme-colors colors/white colors/neutral-95)
=======
   {:transform [{:translateY translate-y}]}
   {:margin-top              0
    :background-color        (colors/theme-colors colors/white colors/neutral-100)
>>>>>>> 52b8d487a (feat: bottom sheet screen)
    :border-top-left-radius  20
    :border-top-right-radius 20
    :flex                    1
    :overflow                :hidden}))

(def handle-container
  {:left            0
   :right           0
   :top             0
   :height          20
   :z-index         1
   :position        :absolute
   :justify-content :center
   :align-items     :center})

<<<<<<< HEAD
(defn handle
  []
  {:width            32
   :height           4
   :border-radius    100
   :background-color (colors/theme-colors colors/neutral-100-opa-30 colors/white-opa-30)})
=======
(def handle
  {:width            32
   :height           4
   :border-radius    100
   :background-color (colors/theme-colors colors/neutral-100-opa-10 colors/white-opa-10)})
>>>>>>> 52b8d487a (feat: bottom sheet screen)
