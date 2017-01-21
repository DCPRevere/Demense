(ns demense.eventstore
  (:require [clojure.spec :as s]
            [demense.specs]))

(def event-state (atom {}))

(defn save-events
  [id events]
  false)

(defn get-events
  [id]
  (map :event/data (get @event-state id)))

