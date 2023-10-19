(ns quo.components.share.share-qr-code.style
  (:require [quo.foundations.colors :as colors]))

(def outer-container
  {:border-radius 16
   :width         "100%"
   :overflow      :hidden})

(def ^:private padding-horizontal 12)

(def content-container
  {:z-index            1
   :padding-horizontal padding-horizontal
   :padding-top        12
   :padding-bottom     8})

;; Header
(def header-container
  {:flex-direction :row
   :margin-bottom  12})

(def header-tab-active {:background-color colors/white-opa-20})
(def header-tab-inactive {:background-color colors/white-opa-5})
(def space-between-tabs {:width 8})

(def info-icon
  {:margin-left :auto
   :align-self  :center})

(def info-icon-color colors/white-opa-40)

;; QR code
(defn qr-code-size [total-width]
  (- total-width (* 2 padding-horizontal)))

;; Bottom part
(def bottom-container
  {:margin-top      8
   :flex-direction  :row
   :justify-content :space-between})

(def title {:color colors/white-opa-40})

(def share-button-size 32)
(def ^:private share-button-gap 16)

(defn share-button-container [alignment]
  {:justify-content (if (= alignment :top) :flex-start :center)
   :margin-left     share-button-gap})

(defn data-text [total-width]
  {:width (- total-width (* 2 padding-horizontal) share-button-size share-button-gap)})

;; Wallet variants
(def wallet-data-and-share-container
  {:margin-top      2
   :flex-direction  :row
   :justify-content :space-between})

(def wallet-legacy-container {:flex 1})

(def wallet-multichain-container {:flex 1 :margin-top 4})

(def wallet-multichain-networks
  {:flex-direction  :row
   :justify-content :space-between
   :margin-bottom   8})

(def wallet-multichain-data-container {:margin-top 4})


(def divider-container
  {:height            8
   :margin-horizontal 4
   :justify-content   :center
   :overflow          :hidden})

(def dashed-line
  {:flex-direction :row
   :margin-left    -1})

(def line
  {:background-color colors/white-opa-20
   :width            2
   :height           1})

(def line-space
  {:width  4
   :height 1})

