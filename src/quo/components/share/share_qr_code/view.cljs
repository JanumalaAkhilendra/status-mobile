(ns quo.components.share.share-qr-code.view
  (:require [oops.core :as oops]
            [quo.components.buttons.button.view :as button]
            [quo.components.icon :as icon]
            [quo.components.list-items.preview-list.view :as preview-list]
            [quo.components.markdown.text :as text]
            [quo.components.share.qr-code.view :as qr-code]
            [quo.components.share.share-qr-code.style :as style]
            [quo.components.tabs.tab.view :as tab]
            [quo.foundations.colors :as colors]
            [quo.foundations.resources :as quo.resources]
            [quo.theme]
            [react-native.blur :as blur]
            [react-native.core :as rn]
            [reagent.core :as reagent]))

(defn url-title [share-qr-code-type]
  ;; TODO: get translated strings
  (case share-qr-code-type
    :profile "Link to profile"
    (:wallet-legacy
     :wallet-multichain) "Wallet address"
    ""))

(defn- dashed-line [component-width]
  (into [rn/view {:style style/dashed-line}]
        (take (inc (int (* (/ (- component-width 16 16) 6) 2)))
              (interleave (repeat [rn/view {:style style/line}])
                          (repeat [rn/view {:style style/line-space}])))))

(defn profile-variant-section [{:keys [share-qr-code-type component-width data]}]
  [:<>
   [rn/view {:style {:background-color :green}}
    [text/text
     {:size   :paragraph-2
      :weight :medium
      :style  style/title}
     (str (url-title share-qr-code-type) " -- " component-width)]

    [rn/view {:style {:width (- component-width 12 12 32 16)}}
     [text/text
      {:size            :paragraph-1
       :weight          :monospace
       :ellipsize-mode  :middle
       :number-of-lines 1}
      data]]]

   [rn/view {:style {:justify-content :center
                     :margin-left     16}}
    [button/button
     {:icon-only?          true
      :type                :grey
      :background          :blur
      :size                32
      :accessibility-label :share-profile
      ;:on-press            share-on-press
      }
     :i/share]]])

;; TODO: solve translations here
;; TODO: add callbacks dor those tabs
(defn- header [wallet-type]
  [rn/view {:style style/header-container}
   [tab/view
    {:id                          :wallet-legacy-tab
     :active-item-container-style style/header-tab-active
     :item-container-style        style/header-tab-inactive
     :size                        24
     :active                      (= :wallet-legacy wallet-type)
     :on-press                    (fn [id] (prn id))}
    "Legacy"]
   [rn/view {:style style/space-between-tabs}]
   [tab/view
    {:id                          :wallet-multichain-tab
     :active-item-container-style style/header-tab-active
     :item-container-style        style/header-tab-inactive
     :size                        24
     :active                      (= :wallet-multichain wallet-type)
     :on-press                    (fn [id] (prn id))}
    "Multichain"]
   [icon/icon :i/info
    {:size            20
     :container-style style/info-icon
     :color           style/info-icon-color}]])

(defn- title-text [share-qr-code-type]
  [text/text {:size :paragraph-2 :weight :medium :style style/title}
   (if (= share-qr-code-type :profile)
     "Link to profile"
     "Wallet address")])

(defn- data-text
  ([component-width qr-data]
   (data-text component-width qr-data false))
  ([component-width qr-data ellipsize?]
   (let [text-styles (cond-> {:size            :paragraph-1
                              :weight          :monospace
                              :number-of-lines 2}
                       ellipsize? (assoc :ellipsize-mode :middle
                                         :number-of-lines 1))]
     [rn/view {:style (style/data-text component-width)}
      [text/text text-styles
       qr-data]])))

;; TODO: add on-press callback
(defn- share-button [{:keys [alignment]}]
  [rn/view {:style (style/share-button-container alignment)}
   [button/button
    {:icon-only?          true
     :type                :grey
     :background          :blur
     :size                style/share-button-size
     :accessibility-label :share-profile}
    :i/share]])

(defn view*
  [{:keys              [qr-image-uri qr-data component-width] ;; TODO: maybe rename `component-width`
    share-qr-code-type :type}]
  [rn/view {:style style/content-container}
   (when-let [wallet-type (#{:wallet-legacy :wallet-multichain} share-qr-code-type)]
     [header wallet-type])

   [qr-code/view {:qr-image-uri qr-image-uri
                  :size         (style/qr-code-size component-width)}]

   [rn/view {:style style/bottom-container}
    (case share-qr-code-type
      :profile
      [:<>
       [rn/view
        [title-text share-qr-code-type]
        [data-text component-width qr-data :ellipsize]]
       [share-button {:alignment :center}]]

      :wallet-legacy
      [rn/view {:style style/wallet-legacy-container}
       [title-text share-qr-code-type]
       [rn/view {:style style/wallet-data-and-share-container}
        [data-text component-width qr-data]
        [share-button {:alignment :top}]]]

      :wallet-multichain
      [rn/view {:style style/wallet-multichain-container}

       [rn/view {:style style/wallet-multichain-networks}
        [preview-list/view {:type :network :size :size-32}
         [{:source (quo.resources/get-network :ethereum)}
          {:source (quo.resources/get-network :optimism)}
          {:source (quo.resources/get-network :arbitrum)}
          #_{:source (quo.resources/get-network :zksync)}
          #_{:source (quo.resources/get-network :polygon)}]]

        [button/button ;;TODO: on press callback
         {:icon-only?          true
          :type                :grey
          :background          :blur
          :size                32
          :accessibility-label :qr-network-settings}
         :i/advanced]]

       [rn/view {:style style/divider-container}
        [dashed-line component-width]]
       ;;
       [rn/view {:style style/wallet-multichain-data-container}
        [title-text share-qr-code-type]

        [rn/view {:style style/wallet-data-and-share-container}
         [data-text component-width
          [:<>
           [text/text {:style {:color (colors/resolve-color :ethereum nil)}}
            "eth:"]
           [text/text {:style {:color (colors/resolve-color :optimism nil)}}
            "opt:"]
           [text/text {:style {:color (colors/resolve-color :arbitrum nil)}}
            "arb1:"]
           "0x39cf6E0Ba4C4530735616e1Ee7ff5FbCB726fBd2"
           #_data]]
         ;;
         [share-button {:alignment :top}]]]]
      nil)]])

(defn view [props]
  (reagent/with-let [component-width (reagent/atom nil)]
    [quo.theme/provider {:theme :dark}
     [rn/view {:style     style/outer-container
               :on-layout #(reset! component-width (oops/oget % "nativeEvent.layout.width"))}
      [blur/view {:blur-radius   20
                  ;:blur-amount 20 ;; TODO: set it on iOS
                  :overlay-color colors/white-opa-5}
       (when @component-width
         [view* (assoc props :component-width @component-width)])]]]))
