(ns demense.domain
  (:require [demense.events :as ev])
  (:import [demense.events
            InventoryItemCreated
            InventoryItemDeactivated
            InventoryItemRenamed
            ItemsCheckedInToInventory
            ItemsRemovedFromInventory]))

(defmulti apply-event
  (fn [agg event]
    (type event)))

(defmethod apply-event
  InventoryItemCreated
  [agg event]
  (let [{:keys [id]} event]
    (assoc agg
           :item/id id
           :item/activated? true)))

(defmethod apply-event
  InventoryItemDeactivated
  [agg event]
  (assoc agg
         :item/activated? false))

(defmethod apply-event
  :default
  [agg event]
  agg)

(defn append-event
  [agg event]
  (update agg :item/changes
          #(concat % [event])))

(defn raise
  [agg event]
  (-> agg
      (append-event event)
      (apply-event event)))

(defn change-name
  [agg name]
  (let [{:keys [:item/id]} agg]
    (if (empty? name)
      (throw (Exception. "New name is null or empty."))
      (raise agg (ev/->InventoryItemRenamed id name)))))

(defn remove
  [agg count]
  (let [{:keys [:item/id]} agg]
    (if (pos? count)
      (raise (ev/->ItemsRemovedFromInventory id count))
      (throw (Exception. "Remove count isn't positive.")))))

(defn check-in
  [agg count]
  (let [{:keys [:item/id]} agg]
    (if (pos? count)
      (raise (ev/->ItemsCheckedInToInventory id count))
      (throw (Exception. "Check in count isn't positive.")))))

(defn deactivate
  [agg]
  (let [{:keys [:item/id :item/activated?]} agg]
    (if activated?
      (raise (ev/->InventoryItemDeactivated id))
      (throw (Exception. "Cannot deactivate inactive.")))))

(defn load-from-history
  [events]
  (reduce apply-event nil events))

