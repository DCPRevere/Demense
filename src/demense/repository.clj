(ns demense.repository
  (:require [demense.domain :as dom]
            [demense.eventstore :as evst]
            [clojure.string :as str]))

(defprotocol Repository
  (get-by-id [this id])
  (save [this agg]))

(def stream-prefix "item-")

(defn remove-prefix
  "Removes prefix if possible, returns the original string if not."
  [str prefix]
  (str/replace
   str (re-pattern (str "^" prefix)) ""))

(defn s->a [s-id]
  (let [id (remove-prefix s-id stream-prefix)]
    (if (= id s-id)
      (throw (Exception. "Invalid stream id."))
      id)))

(defn a->s [id]
  (str stream-prefix id))

(defrecord EventStoreRepo
    []
  Repository
  (get-by-id [this id]
    (dom/load-from-history (evst/get-events id)))

  (save [this agg]
    (evst/save-events (:item/events agg)
                      )))
