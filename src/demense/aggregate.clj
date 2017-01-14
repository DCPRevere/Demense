(ns demense.aggregate)

(defn append-event [agg event])

(defmulti apply-event :event/type)

(defmethod apply-event :event/item-created
  [agg event]
  (let [{id :event/id
         name :event/name}
        event]
   (-> agg
       (assoc :item/id id
              :item/name name))))

(defmethod apply-event :event/item-deactivated
  [agg event]
  (-> agg
      (assoc :item/deactivated? true)))

(defmethod apply-event :event/items-checked-in
  [agg event] nil)

(defmethod apply-event :event/item-renamed
  [agg event]
  (-> agg
      (assoc :item/name (:item/name event))))

;; What does it mean to deactivate an item?
;; Is our aggregate an item or an inventory?
(defmethod apply-event :event/item-removed
  [agg event]
  (-> agg ))

(defn raise [agg event]
  (-> agg
      (append-event event)
      (apply-event event)))

(defn create [agg event]
  nil)

(defn deactivate [agg event]
  nil)

(defn remove [agg event] nil)

(defn check-in [agg event] nil)

(defn rename [agg event] nil)

