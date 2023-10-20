(ns quo.components.share.share-qr-code.view
  (:require [clojure.string :as string]
            [oops.core :as oops]
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

(defn- line [] [rn/view {:style style/line}])
(defn- space [] [rn/view {:style style/line-space}])

(defn- dashed-line [width]
  (into [rn/view {:style style/dashed-line}]
        (take (style/number-lines-and-spaces-to-fill width))
        (cycle [[line] [space]])))

(defn url-title [share-qr-code-type]
  ;; TODO: get translated strings
  (if (= share-qr-code-type :profile)
    "Link to profile"
    "Wallet address"))

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

(defn- qr-text
  ([component-width qr-data-text]
   (qr-text component-width qr-data-text false))
  ([component-width qr-data-text ellipsize?]
   [rn/view {:style (style/data-text component-width)}
    [text/text (cond-> {:size            :paragraph-1
                        :weight          :monospace
                        :number-of-lines 2}
                 ellipsize? (assoc :ellipsize-mode  :middle
                                   :number-of-lines 1))
     qr-data-text]]))

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

(defn- network-colored-text [network-short-name]
  [text/text {:style (style/network-short-name-text network-short-name)}
   (str network-short-name ":")])

(defn- wallet-multichain-colored-address [full-address]
  (let [[networks address] (as-> full-address $
                             (string/split $ ":")
                             [(butlast $) (last $)])
        ->network-hiccup-xf (map #(vector network-colored-text %))]
    (as-> networks $
      (into [:<>] ->network-hiccup-xf $)
      (conj $ address))))

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
        [qr-text component-width qr-data :ellipsize]]
       [share-button {:alignment :center}]]

      :wallet-legacy
      [rn/view {:style style/wallet-legacy-container}
       [title-text share-qr-code-type]
       [rn/view {:style style/wallet-data-and-share-container}
        [qr-text component-width qr-data]
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
         [qr-text component-width
          [wallet-multichain-colored-address
           "eth:opt:arb1:0x39cf6E0Ba4C4530735616e1Ee7ff5FbCB726fBd2"]]
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
