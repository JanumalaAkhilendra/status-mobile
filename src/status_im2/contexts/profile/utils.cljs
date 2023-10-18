(ns status-im2.contexts.profile.utils)

(defn displayed-name
  [{:keys [name display-name preferred-name ens-verified primary-name]}]
  ;; `preferred-name` is our own name
  ;; otherwise we make sure the `name` is verified and use it
  (let [ens-name (or preferred-name
                     (when (and ens-verified name) name))]
    (or ens-name
        display-name
        primary-name)))

(defn photo
  [{:keys [images]}]
  (or (:large images)
      (:thumbnail images)
      (first images)))
