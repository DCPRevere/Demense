(ns demense.handle
  (:require [demense.repository :as repo]
            [demense.domain :as dom]))

(def r (repo/->EventStoreRepo))

(defprotocol Command
  (handle [this]))

(defrecord CreateInventoryItem
    [id name]
  Command
  (handle [this]
    (let [{:keys [id name]} this
          agg (repo/get-by-id r id)]
      (if (nil? agg)
        (-> agg
            (dom/create id name)
            repo/save)))))

(defrecord
    DeactivateInventoryItem
    [id]
  Command
  (handle [this]
    (let [{:keys [id]} this
          agg (repo/get-by-id r id)]
      (-> agg
          dom/deactivate
          repo/save))))

(defrecord
    RemoveItemsFromInventory
    [id count]
  Command
  (handle [this]
    (let [{:keys [id count]} this
          agg (repo/get-by-id r id)]
      (-> agg
          (dom/remove count)
          repo/save))))

(defrecord
    CheckInItemsToInventory
    [id count]
  Command
  (handle [this]
    (let [{:keys [id count]} this
          agg (repo/get-by-id r id)]
      (-> agg
          (dom/check-in count)
          repo/save))))

(defrecord
    RenameInventoryItem
    [id name]
  Command
  (handle [this]
    (let [{:keys [id name]} this
          agg (repo/get-by-id r id)]
      (-> agg
          (dom/rename name)
          repo/save))))
