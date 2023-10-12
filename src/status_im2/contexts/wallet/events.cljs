(ns status-im2.contexts.wallet.events
  (:require
   [native-module.core :as native-module]
   [taoensso.timbre :as log]
   [utils.re-frame :as rf]
   [utils.security.core :as security]))

(rf/defn scan-address-success
  {:events [:wallet-2/scan-address-success]}
  [{:keys [db]} address]
  {:db (assoc db :wallet-2/scanned-address address)})

(rf/defn clean-scanned-address
  {:events [:wallet-2/clean-scanned-address]}
  [{:keys [db]}]
  {:db (dissoc db :wallet-2/scanned-address)})

(rf/reg-fx :wallet-2/create-derived-addresses
           (fn [{:keys [db]} [password {:keys [path]} on-success]]
             (let [{:keys [wallet-root-address]} (:profile/profile db)
                   sha3-pwd                      (native-module/sha3 (str (security/safe-unmask-data password)))]
               {:fx [[:json-rpc/call
                      [{:method     "wallet_getDerivedAddresses"
                        :params     [sha3-pwd wallet-root-address [path]]
                        :on-success on-success
                        :on-error   #(log/info "failed to derive address " %)}]]]})))

(rf/reg-fx :wallet-2/add-account
           (fn [{:keys [db]} [password {:keys [emoji account-name color]} {:keys [public-key address path]}]]
             (let [key-uid        (get-in db [:profile/profile :key-uid])
                   sha3-pwd       (native-module/sha3 (security/safe-unmask-data password))
                   account-config {:key-uid    key-uid
                                   :wallet     false
                                   :chat       false
                                   :type       :generated
                                   :name       account-name
                                   :emoji      emoji
                                   :path       path
                                   :address    address
                                   :public-key public-key
                                   :colorID    color}]
               {:fx [[:json-rpc/call
                      [{:method     "accounts_addAccount"
                        :params     [sha3-pwd account-config]
                        :on-success #(rf/dispatch [:navigate-to :wallet-accounts])
                        :on-error   #(log/info "failed to create account " %)}]]]})))

(rf/reg-fx :wallet-2/derive-address-and-add-account
           (fn [_ [password account-details]]
             (let [on-success (fn [derived-adress-details]
                                (rf/dispatch [:wallet-2/add-account password account-details
                                              (first derived-adress-details)]))]
               {:fx [[:dispatch [:wallet-2/create-derived-addresses password account-details on-success]]]})))
