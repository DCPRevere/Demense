(ns demense.repository
  (:require [demense.domain :as dom]
            [demense.eventstore :as evst]))

(defprotocol Repository
  (get-by-id [this id])
  (save [this agg]))

(defrecord EventStoreRepo
    []
  Repository
  (get-by-id [this id]
    (dom/load-from-history (evst/get-events id)))

  (save [this agg]
    (evst/save-events (:item/events agg))))
