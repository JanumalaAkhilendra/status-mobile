(ns status-im2.contexts.quo-preview.share.share-qr-code
  (:require
   [quo.core :as quo]
   [react-native.core :as rn]
   [reagent.core :as reagent]
   [status-im2.common.resources :as resources]
   [status-im2.contexts.quo-preview.preview :as preview]
   [utils.image-server :as image-server]
   [utils.re-frame :as rf]))

(def descriptor
  [{:key :url :type :text}
   {:key     :type
    :type    :select
    :options [{:key :profile}
              {:key :wallet-legacy}
              {:key :wallet-multichain}]}
   {:key :link-title :type :text}])

(defn view
  []
  (let [state (reagent/atom {:type       :wallet-legacy
                             :link-title "Link to profile"
                             :data
                             "status.app/u/zQ34Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Suspendisse ut metus. Proin venenatis turpis sit amet ante consequat semper. Aenean nunc. Duis iaculis odio id lectus. Integer dapibus justo vitae elit."
                             ;"status.app/u/zQ34easjbasas12adjie8"
                             })]
    (fn []
      (let [qr-media-server-uri (image-server/get-qr-image-uri-for-any-url
                                 {:url         (:data @state)
                                  :port        (rf/sub [:mediaserver/port])
                                  :qr-size     700
                                  :error-level :highest})]
        [preview/preview-container
         {:state                     state
          :descriptor                descriptor
          :full-component-layout?    true
          :component-container-style {:padding-horizontal 0}}

         [rn/view {:style {:flex             1
                           :justify-content  :center
                           :align-items      :center
                           :padding-vertical 20
                           :padding-horizontal 20}}
          [rn/view {:style {:position :absolute
                            :top      0
                            :bottom   0
                            :left     0
                            :right    0}}
           [rn/image
            {:style {:flex 1}
             :source (resources/get-mock-image :dark-blur-bg)}]]

          [quo/share-qr-code
           {:qr-image-uri       qr-media-server-uri
            :type              (:type @state)
            :link-title        (:link-title @state)
            :url-on-press      #(js/alert "url pressed")
            :url-on-long-press #(js/alert "url long pressed")
            :share-on-press    #(js/alert "share pressed")
            :data               (:data @state)}]]]))))
