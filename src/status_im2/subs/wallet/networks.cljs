(ns status-im2.subs.wallet.networks
  (:require [quo.foundations.resources :as resources]
            [re-frame.core :as re-frame]
            [status-im2.constants :as constants]))

(re-frame/reg-sub
 :wallet/filtered-networks-by-mode
 :<- [:wallet/networks]
 (fn [networks [_ test?]]
   (get networks (if test? :test :prod))))

(def ^:const mainnet-network-details
  {:source       (resources/networks :ethereum)
   :short-name   "eth"
   :network-name :ethereum})

(def ^:const arbitrum-network-details
  {:source       (resources/networks :arbitrum)
   :short-name   "arb1"
   :network-name :arbitrum})

(def ^:const optimism-network-details
  {:source       (resources/networks :optimism)
   :short-name   "opt"
   :network-name :optimism})

(def ^:const network-list
  {constants/mainnet-chain-id  mainnet-network-details
   constants/arbitrum-chain-id arbitrum-network-details
   constants/optimism-chain-id optimism-network-details})

(re-frame/reg-sub
 :wallet/network-details
 :<- [:wallet/filtered-networks-by-mode false]
 (fn [networks]
   (keep
    (fn [{:keys [chain-id related-chain-id is-test]}]
      (let [network-details (get network-list (if is-test related-chain-id chain-id))]
        (assoc network-details
               :chain-id         chain-id
               :related-chain-id related-chain-id)))
    networks)))
