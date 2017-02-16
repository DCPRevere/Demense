(ns demense.repository
  (:require [demense.domain :as dom]
            [demense.eventstore :as evst]
            [clojure.string :as str]))

(def stream-prefix "item-")

(defn remove-prefix
  "Removes prefix if possible, returns the original string if not."
  [string prefix]
  (str/replace string (re-pattern (str "^" prefix)) ""))

(remove-prefix "foo-bar" "foo-")

(defn s->a [s-id]
  (let [id (remove-prefix s-id stream-prefix)]
    (if (= id s-id)
      (throw (Exception. "Invalid stream id."))
      id)))

(defn a->s [id]
  (str stream-prefix id))

(defn get-by-id
  [this id]
  (dom/load-from-history (evst/get-events id)))

(defn save
  [this agg]
  (let [{:keys [:item/id :item/changes]} agg]
    (evst/save-events id changes -1))
  agg)
