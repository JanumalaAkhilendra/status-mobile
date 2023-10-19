(ns status-im2.contexts.profile.utils
  (:require [clojure.string :as string]))

(defn displayed-name
  [{:keys [name display-name preferred-name ens-verified primary-name]}]
  ;; `preferred-name` is our own name
  ;; otherwise we make sure the `name` is verified and use it
  (let [display-name   (when-not (string/blank? display-name)
                         display-name)
        preferred-name (when-not (string/blank? preferred-name)
                         preferred-name)
        ens-name       (or preferred-name
                           (when (and ens-verified name) name))]
    (or ens-name
        display-name
        primary-name)))

(defn photo
  [{:keys [images]}]
  (or (:large images)
      (:thumbnail images)
      (first images)))
