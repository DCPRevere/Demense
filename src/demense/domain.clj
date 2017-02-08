(ns demense.domain)

(defmulti apply-event
  (fn [agg event]
    (:demense.event/type event)))

(defmethod apply-event
  :demense.event.type/item-created
  [agg event]
  (let [{:keys [:demense.item/id]} event]
    (assoc agg
           :demense.item/id id
           :demense.item/activated? true)))

(defmethod apply-event
  :demense.event.type/item-deactivated
  [agg event]
  (assoc agg :demense.item/activated? false))

(defmethod apply-event
  :default
  [agg event]
  agg)

(defn append-event
  [agg event]
  (update agg :demense.item/changes
          #(concat % [event])))

(defn raise
  [agg event]
  (-> agg
      (append-event event)
      (apply-event event)))

;; TODO: automatically assign :demense.item/id to the event.

(defn create
  [agg id name]
  (raise agg {:demense.event/type :demense.event.type/item-created
              :demense.item/id id
              :demense.item/name name}))

(defn deactivate
  [agg]
  (let [{:keys [:demense.item/id :demense.item/activated?]} agg]
    (if activated?
      (raise agg {:demense.event/type :demense.event.type/item-deactivated
                  :demense.item/id id})
      (throw (Exception. "Cannot deactivate inactive.")))))

(defn rename
  [agg name]
  (let [{:keys [:demense.item/id]} agg]
    (if (empty? name)
      (throw (Exception. "New name is null or empty."))
      (raise agg {:demense.event/type :demense.event.type/item-renamed
                  :demense.item/id id
                  :demense.item/name name}))))

(defn check-in
  [agg count]
  (let [{:keys [:demense.item/id]} agg]
    (if (pos? count)
      (raise agg {:demense.event/type :demense.event.type/items-checked-in
                  :demense.item/id id
                  :demense.item/count count})
      (throw (Exception. "Check in count isn't positive.")))))

(defn remove
  [agg count]
  (let [{:keys [:demense.item/id]} agg]
    (if (pos? count)
      (raise agg {:demense.event/type :demense.event.type/items-removed
                  :demense.item/id id
                  :demense.item/count count})
      (throw (Exception. "Remove count isn't positive.")))))

(defn load-from-history
  [events]
  (reduce apply-event nil events))

