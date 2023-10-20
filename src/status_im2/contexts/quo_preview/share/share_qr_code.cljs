(ns status-im2.contexts.quo-preview.share.share-qr-code
  (:require [quo.core :as quo]
            [react-native.core :as rn]
            [reagent.core :as reagent]
            [status-im2.common.resources :as resources]
            [status-im2.contexts.quo-preview.preview :as preview]
            [utils.image-server :as image-server]
            [utils.re-frame :as rf]))

(def descriptor
  [{:key   :qr-data
    :label "QR data:"
    :type  :text}
   {:key     :type
    :type    :select
    :options [{:key :profile}
              {:key :wallet-legacy}
              {:key :wallet-multichain}]}])

(def profile-descriptor
  [{:key     :profile-picture
    :type    :select
    :options [{:key   (resources/get-mock-image :user-picture-female2)
               :value "User 1"}
              {:key   (resources/get-mock-image :user-picture-male4)
               :value "User 2"}
              {:key   nil
               :value "No picture"}]}

   {:key  :full-name
    :type :text}
   (preview/customization-color-option)])

(def wallet-legacy-descriptor
  [{:key     :emoji
    :type    :select
    :options [{:key "🐈"}
              {:key "👻"}
              {:key "🐧"}]}
   (preview/customization-color-option)])

(def possible-networks [:ethereum :optimism :arbitrum :my-network])

(def wallet-multichain-descriptor
  [{:key     :emoji
    :type    :select
    :options [{:key "🐈"}
              {:key "👻"}
              {:key "🐧"}]}
   (preview/customization-color-option)
   {:key     :networks
    :type    :select
    :options [{:key   (take 1 possible-networks)
               :value "Ethereum"}
              {:key   (take 2 possible-networks)
               :value "Ethereum and Optimism"}
              {:key   (take 3 possible-networks)
               :value "Ethereum, Optimism and Arbitrum"}
              {:key   (take 4 possible-networks)
               :value "Ethereum, Optimism, Arbitrum and unknown"}]}])

(defn- get-network-short-name-url [network-kw]
  (case network-kw
    :ethereum "eth:"
    :optimism "opt:"
    :arbitrum "arb1:"
    (str (name network-kw) ":")))

(def ^:private profile-link
  "https://join.status.im/u/zQ3shfozoVJF6oUBcgcaaaK3sipoZFPYXZ5FbHvMXGc5Wrqnu")

(def ^:private wallet-address "0x39cf6E0Ba4C4530735616e1Ee7ff5FbCB726fBd2")

(defn- reset-qr-data [share-qr-type atom]
  [:<>
   (rn/use-effect
    (fn []
      (swap! atom assoc :qr-data (if (= share-qr-type :profile)
                                   profile-link
                                   wallet-address))
      nil)
    [share-qr-type])])

(defn view
  []
  (let [state (reagent/atom {:type                :profile
                             :qr-data             profile-link
                             :on-share-press      #(js/alert "share pressed")
                             :on-text-press       #(js/alert "text pressed")
                             :on-text-long-press  #(js/alert "text long press")
                             :profile-picture     nil
                             :full-name           "Abcd User"
                             :customization-color :purple
                             :emoji               "🐈"
                             :on-info-press       #(js/alert "Info pressed")
                             :on-legacy-press     #(js/alert (str "Tab " % " pressed"))
                             :on-multichain-press #(js/alert (str "Tab " % " pressed"))
                             :networks            (take 2 possible-networks)
                             :on-settings-press   #(js/alert "Settings pressed")})]
    (fn []
      (let [qr-url              (if (= (:type @state) :wallet-multichain)
                                  (as-> (:networks @state) $
                                    (map get-network-short-name-url $)
                                    (apply str $)
                                    (str $ (:qr-data @state)))
                                  (:qr-data @state))
            qr-media-server-uri (image-server/get-qr-image-uri-for-any-url
                                 {:url         qr-url
                                  :port        (rf/sub [:mediaserver/port])
                                  :qr-size     500
                                  :error-level :highest})
            typed-descriptor    (concat descriptor
                                        (case (:type @state)
                                          :profile profile-descriptor
                                          :wallet-legacy wallet-legacy-descriptor
                                          :wallet-multichain wallet-multichain-descriptor
                                          nil))]
        [preview/preview-container
         {:state                     state
          :descriptor                typed-descriptor
          :component-container-style {:padding-horizontal 0}}
         [rn/view {:style {:flex               1
                           :justify-content    :center
                           :align-items        :center
                           :padding-horizontal 20
                           :padding-vertical   40}}
          ;; Hack to reset the `:qr-data` atom value when the `:type` changes
          [:f> reset-qr-data (:type @state) state]
          [rn/view {:style {:position :absolute
                            :top      0
                            :bottom   0
                            :left     0
                            :right    0}}
           [rn/image {:style  {:flex        1
                               :resize-mode :repeat}
                      :source (resources/get-mock-image :dark-blur-bg)}]]
          [quo/share-qr-code
           (assoc @state :qr-image-uri qr-media-server-uri
                         :qr-data qr-url)]]]))))
