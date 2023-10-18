(ns quo2.components.calendar.calendar-year.view
  (:require
    [quo2.components.calendar.calendar-year.style :as style]
    [quo2.components.markdown.text :as text]
    [quo2.theme :as theme]
    [react-native.core :as rn]))

(defn- view-internal
  [{:keys [selected? disabled? on-press theme]} year]
  [rn/touchable-opacity
   {:on-press on-press
    :style    (style/container
               {:selected? selected?
                :disabled? disabled?
                :theme     theme})
    :disabled disabled?}
   [text/text
    {:weight :medium
     :size   :paragraph-2
     :style  (style/text
              {:selected? selected?
               :theme     theme})}
    year]])

(def view (theme/with-theme view-internal))