(ns quo.components.colors.color.style
  (:require
    [quo.components.colors.color.constants :as constants]
    [quo.foundations.colors :as colors]))

(defn color-button-common
  [window-width]
  {:width         48
   :height        48
   :border-width  4
   :border-radius 24
   :margin-right  (-> window-width
                      (- constants/IPHONE_11_PRO_VIEWPORT_WIDTH)
                      (/ 6)
                      (+ 10.5))
   :transform     [{:rotate "45deg"}]
   :border-color  :transparent})

(defn color-button
  ([color selected?]
   (color-button color selected? nil nil))
  ([color selected? idx window-width]
   (merge (color-button-common window-width)
          (when selected?
            {:border-top-color    (colors/alpha color 0.4)
             :border-end-color    (colors/alpha color 0.4)
             :border-bottom-color (colors/alpha color 0.2)
             :border-start-color  (colors/alpha color 0.2)}
            (when (zero? idx)
              {:margin-left  -4})))))

(defn color-circle
  [color border?]
  {:width            40
   :height           40
   :transform        [{:rotate "-45deg"}]
   :background-color color
   :justify-content  :center
   :align-items      :center
   :border-color     color
   :border-width     (if border? 2 0)
   :overflow         :hidden
   :border-radius    100})

(defn feng-shui
  [theme]
  {:width            40
   :height           40
   :transform        [{:rotate "45deg"}]
   :overflow         :hidden
   :border-color     (colors/theme-colors colors/neutral-100 colors/white theme)
   :border-width     2
   :background-color (colors/theme-colors colors/neutral-100 colors/white theme)
   :border-radius    20})

(defn left-half
  [theme]
  {:flex             1
   :background-color (colors/theme-colors colors/white colors/neutral-100 theme)})

(defn right-half
  [theme]
  {:flex             1
   :background-color (colors/theme-colors colors/neutral-100 colors/white theme)})
