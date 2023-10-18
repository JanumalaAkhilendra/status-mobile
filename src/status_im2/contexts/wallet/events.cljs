(ns status-im2.contexts.wallet.events
  (:require [re-frame.core :as re-frame]
            [status-im2.contexts.wallet.item-types :as types]
            [utils.re-frame :as rf]))

(def ens-local-suggestion-saved-address-mock
  {:type     types/saved-address
   :name     "Pedro"
   :ens      "pedro.eth"
   :address  "0x4732894732894738294783294723894723984"
   :networks [:ethereum :optimism]})

(def ens-local-suggestion-mock
  {:address  "0x4732894732894738294783294723894723984"
   :networks [:ethereum :optimism]})

(rf/defn scan-address-success
  {:events [:wallet/scan-address-success]}
  [{:keys [db]} address]
  {:db (assoc db :wallet/scanned-address address)})

(rf/defn clean-scanned-address
  {:events [:wallet/clean-scanned-address]}
  [{:keys [db]}]
  {:db (dissoc db :wallet/scanned-address)})

(defn fetch-address-suggestions
  [{:keys [db]} [_]]
  {:db (assoc db
              :wallet/searching-local-suggestions? false
              :wallet/local-suggestions            []
              :wallet/valid-ens-or-address?        true)})

(re-frame/reg-event-fx :wallet/fetch-address-suggestions fetch-address-suggestions)

(defn fetch-ens-suggestions
  [{:keys [db]} [ens]]
  {:db (assoc db
              :wallet/searching-local-suggestions? false
              :wallet/local-suggestions            (if (= ens "pedro.eth")
                                                       [ens-local-suggestion-saved-address-mock]
                                                       [])
              :wallet/valid-ens-or-address?        true)})

(re-frame/reg-event-fx :wallet/fetch-ens-suggestions fetch-ens-suggestions)

(defn search-address-local-suggestions
  [{:keys [db]} [address]]
  {:db (assoc db
              :wallet/searching-local-suggestions? true
              :wallet/local-suggestions            []
              :wallet/valid-ens-or-address?        false)
   :fx [[:dispatch [:wallet/fetch-address-suggestions address]]]})

(re-frame/reg-event-fx :wallet/search-address-local-suggestions search-address-local-suggestions)

(defn search-ens-local-suggestions
  [{:keys [db]} [ens]]
  {:db                   (assoc db
                                :wallet/searching-local-suggestions? true
                                :wallet/local-suggestions            []
                                :wallet/valid-ens-or-address?        false)
   :utils/dispatch-later [{:dispatch [:wallet/fetch-ens-suggestions ens]
                           :ms       2000}]})

(re-frame/reg-event-fx :wallet/search-ens-local-suggestions search-ens-local-suggestions)

(defn clean-local-suggestions
  [{:keys [db]}]
  {:db (assoc db :wallet/local-suggestions [] :wallet/valid-ens-or-address? false)})

(re-frame/reg-event-fx :wallet/clean-local-suggestions clean-local-suggestions)
