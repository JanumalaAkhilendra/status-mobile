(ns quo.components.share.share-qr-code.style
  (:require [quo.foundations.colors :as colors]))

(def qr-code-container
  {:width              "100%"
   :padding-top        12
   :padding-horizontal 12
   :padding-bottom     8
   :border-radius      16
   :overflow           :hidden})

(def blur-layer
  {:position         :absolute
   :top              0
   :bottom           0
   :left             0
   :right            0})

(def title {:color colors/white-opa-40})


