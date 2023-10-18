(ns status-im2.contexts.wallet.events
  (:require [re-frame.core :as re-frame]
            [utils.re-frame :as rf]))

(def ens-local-suggestion-saved-address-mock
  {:name     "Pedro"
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
<<<<<<< HEAD
  {:db (dissoc db :wallet/scanned-address)})
=======
  {:db (dissoc db :wallet-2/scanned-address)})

(defn fetch-address-suggestions
  [{:keys [db]} _]
  {:db (assoc db
              :wallet-2/searching-local-suggestions? false
              :wallet-2/local-suggestions            []
              :wallet-2/valid-ens-or-address?        true)})

(re-frame/reg-event-fx :wallet-2/fetch-address-suggestions fetch-address-suggestions)

(defn fetch-ens-suggestions
  [{:keys [db]} ens]
  (println "fetch ens success")
  {:db (assoc db
              :wallet-2/searching-local-suggestions? false
              :wallet-2/local-suggestions            (if (= ens "pedro.eth")
                                                       [ens-local-suggestion-saved-address-mock]
                                                       [])
              :wallet-2/valid-ens-or-address?        true)})

(re-frame/reg-event-fx :wallet-2/fetch-ens-suggestions fetch-ens-suggestions)

(defn search-address-local-suggestions
  [{:keys [db]} address]
  {:db (assoc db
              :wallet-2/searching-local-suggestions? true
              :wallet-2/local-suggestions            []
              :wallet-2/valid-ens-or-address?        false)
   :fx [[:dispatch [:wallet-2/fetch-address-suggestions address]]]})

(re-frame/reg-event-fx :wallet-2/search-address-local-suggestions search-address-local-suggestions)

(defn search-ens-local-suggestions
  [{:keys [db]} ens]
  (println "search ens local sug")
  {:db                   (assoc db
                                :wallet-2/searching-local-suggestions? true
                                :wallet-2/local-suggestions            []
                                :wallet-2/valid-ens-or-address?        false)
   :utils/dispatch-later [{:dispatch [:wallet-2/fetch-ens-suggestions ens]
                           :ms       2000}]})

(re-frame/reg-event-fx :wallet-2/search-ens-local-suggestions search-ens-local-suggestions)

(defn clean-local-suggestions
  [{:keys [db]}]
  {:db (assoc db :wallet-2/local-suggestions [] :wallet-2/valid-ens-or-address? false)})

(re-frame/reg-event-fx :wallet-2/clean-local-suggestions clean-local-suggestions)
>>>>>>> 239dfdfeb (progress on ens and address mocked suggestions)
