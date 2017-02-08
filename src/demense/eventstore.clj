(ns demense.eventstore
  (:require [clojure.spec :as s]))

(def current
  "current is a map of ids to a list of their event descriptors."
  (atom {}))

(defn ->descriptors
  [id events exp-version]
  (map-indexed (fn [idx event]
                 {:agg/id id
                  :event/data event
                  :event/version (+ 1 exp-version idx)})
               events))

(defn save-events
  [id events exp-version]
  (swap! current
         (fn [current]
           (let [event-descs (get current id)
                 version (:event/version (last event-descs))]
             (if (or (= -1 exp-version)
                     (= version exp-version)
                     (and (nil? version) (= 0 exp-version)))
               (update current id
                       concat (->descriptors id events exp-version))
               (throw (Exception. "Concurrency exception.")))))))

(defn get-events
  [id]
  (map :event/data (get @current id)))

