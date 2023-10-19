(ns quo.components.share.share-qr-code.view
  (:require [quo.components.buttons.button.view :as button]
            [quo.components.icon :as icon]
            [quo.components.markdown.text :as text]
            [quo.components.share.qr-code.view :as qr-code]
            [quo.components.share.share-qr-code.style :as style]
            [quo.components.tabs.tab.view :as tab]
            [quo.foundations.colors :as colors]
            [quo.foundations.resources :as quo.resources]
            [react-native.blur :as blur]
            [react-native.core :as rn]
            [quo.components.tabs.tabs.view :as tabs]
            [quo.components.list-items.preview-list.view :as preview-list
             ]
            [oops.core :as oops]
            [reagent.core :as reagent]))

(defn url-title [share-qr-code-type]
  ;; TODO: get translated strings
  (case share-qr-code-type
    :profile "Link to profile"
    (:wallet-legacy
     :wallet-multichain) "Wallet address"
    ""))

(defn dashed-line [component-width]
  (into [rn/view {:style style/dashed-line-container}]
        (take (inc (int (* (/ (- component-width 20 20) 6) 2)))
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
       :weight          :medium
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

(defn view*
  [{:keys              [qr-image-uri
                        data
                        ;;link-title url-on-press url-on-long-press qr-url share-on-press
                        component-width]
    share-qr-code-type :type}]
  [rn/view

   (when (#{:wallet-legacy :wallet-multichain} share-qr-code-type)
     [rn/view {:style {:flex-direction :row
                       :margin-bottom  12}}
      [tab/view
       {:id                          :wallet-legacy
        :active-item-container-style {:background-color colors/white-opa-20}
        :item-container-style        {:background-color colors/white-opa-5}
        ;:notification-dot?   false
        ;:customization-color customization-color
        ;:accessibility-label accessibility-label
        :size                        24
        :active                      (= :wallet-legacy share-qr-code-type)
        :on-press                    (fn [id]
                                       (prn id))}
       "Legacy"]

      [tab/view
       {:id                          :wallet-multichain
        :active-item-container-style {:background-color colors/white-opa-20
                                      :margin-left      8}
        :item-container-style        {:background-color colors/white-opa-5
                                      :margin-left      8
                                      }
        ;:notification-dot?   false
        ;:customization-color customization-color
        ;:accessibility-label accessibility-label
        :size                        24
        :active                      (= :wallet-multichain share-qr-code-type)
        :on-press                    (fn [id]
                                       (prn id))}
       "Multichain"]
      ;;
      [icon/icon :i/info
       {:size            20
        :color           colors/white-opa-40
        :container-style {:margin-left :auto
                          :align-self  :center}}]

      ]

     #_[tabs/view {:style       {:margin-bottom 12}
                   :scrollable? false
                   :size        24
                   :data        [{:id    "A"
                                  :label "Legacy"}
                                 {:id    "b"
                                  :label "Multichain"}]
                   }]
     )

   [qr-code/view {:qr-image-uri qr-image-uri :size (- component-width 12 12)}]

   [rn/view {:style {:margin-top      8
                     :flex-direction  :row
                     :justify-content :space-between}}

    (case share-qr-code-type
      :profile
      [profile-variant-section {:share-qr-code-type share-qr-code-type ;; TODO: same as props
                                :component-width    component-width
                                :data               data}]
      :wallet-legacy
      [rn/view {:style {:flex 1}}
       [text/text
        {:size   :paragraph-2
         :weight :medium
         :style  style/title}
        (url-title share-qr-code-type)]

       [rn/view {:style {:flex-direction  :row
                         :justify-content :space-between}}
        [rn/view {:style {:width (- component-width 12 12 32 16)}}
         [text/text
          {:size            :paragraph-1
           :weight          :medium
           :ellipsize-mode  :middle
           :number-of-lines 2}
          data]]
        ;;
        [rn/view {:style {:justify-content :center}}
         [button/button
          {:icon-only?          true
           :type                :grey
           :background          :blur
           :size                32
           :accessibility-label :share-profile
           ;:on-press            share-on-press
           }
          :i/share]]]]

      :wallet-multichain
      [rn/view {:style {:flex       1
                        :margin-top 4}}
       [rn/view {:style {:flex-direction  :row
                         :justify-content :space-between
                         :margin-bottom   8}}
        [preview-list/view {:type :network
                            :size :size-32}
         [{:source (quo.resources/get-network :ethereum)}
          {:source (quo.resources/get-network :optimism)}
          {:source (quo.resources/get-network :arbitrum)}
          #_{:source (quo.resources/get-network :zksync)}
          #_{:source (quo.resources/get-network :polygon)}]]

        [button/button
         {:icon-only?          true
          :type                :grey
          :background          :blur
          :size                32
          :accessibility-label :share-profile
          ;:on-press            share-on-press
          }
         :i/advanced]]
       ;;

       [rn/view {:style {:height            8
                         :margin-horizontal 8
                         :justify-content   :center
                         :overflow          :hidden}}
        [dashed-line component-width]]
       ;;

       ]

      nil)
    ]])

(defn view [_]
  (let [component-width (reagent/atom nil)]
    (fn [props]
      [quo.theme/provider {:theme :dark}
       [rn/view {:style     style/qr-code-container
                 :on-layout (fn [e]
                              (reset! component-width (oops/oget e "nativeEvent.layout.width")))}
        [blur/view {:style         style/blur-layer
                    :blur-type     :light ;; TODO: maybe depend on theme
                    ;:blur-amount 20 ;; TODO: set it on iOS
                    :blur-radius   20
                    :overlay-color colors/white-opa-5}]
        (when @component-width
          [view* (assoc props :component-width @component-width)])]])))
