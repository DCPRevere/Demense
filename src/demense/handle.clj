(ns demense.handle
  (:require [demense.repository :as repo]
            [demense.domain :as dom]))

(def r (repo/->EventStoreRepo))

(defn get-by-id [id]
  (partial repo/get-by-id r))

(defn save [agg]
  (partial repo/save r))

(defprotocol Command
  (handle [this]))

(defrecord CreateInventoryItem
    [id name]
  Command
  (handle [this]
    (let [{:keys [id name]} this
          agg (get-by-id id)]
      (if (nil? agg)
        (-> agg
            (dom/create id name)
            save)))))

(defrecord
    DeactivateInventoryItem
    [id]
  Command
  (handle [this]
    (let [{:keys [id]} this
          agg (get-by-id id)]
      (-> agg
          dom/deactivate
          save))))

(defrecord
    RemoveItemsFromInventory
    [id count]
  Command
  (handle [this]
    (let [{:keys [id count]} this
          agg (get-by-id id)]
      (-> agg
          (dom/remove count)
          save))))

(defrecord
    CheckInItemsToInventory
    [id count]
  Command
  (handle [this]
    (let [{:keys [id count]} this
          agg (get-by-id id)]
      (-> agg
          (dom/check-in count)
          save))))

(defrecord
    RenameInventoryItem
    [id name]
  Command
  (handle [this]
    (let [{:keys [id name]} this
          agg (get-by-id id)]
      (-> agg
          (dom/rename name)
          save))))
