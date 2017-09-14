(ns demense.item)

(defmulti apply-event
  (fn [_ event]
    (:demense.event/type event)))

(defmethod apply-event
  :demense.event.type/item-created
  [item event]
  (let [{:keys [:demense.item/id]} event]
    (assoc item
           :demense.item/id id
           :demense.item/active? true)))

(defmethod apply-event
  :demense.event.type/item-deactivated
  [item event]
  (assoc item :demense.item/active? false))

(defmethod apply-event
  :demense.event.type/item-renamed
  [item event]
  (let [m (select-keys event [:demense.item/name])]
    (merge item m)))

(defmethod apply-event
  :default
  [item event]
  item)

(defn append-event
  [item event]
  (update item :demense.event/changes
          #(concat % [event])))

(defn raise
  [item event]
  (-> item
      (append-event event)
      (apply-event event)))

;; TODO: automatically assign :demense.item/id to the event.

(defn name-exception []
  (throw (Exception. "New name is nil or empty.")))

(defn create
  [item id name]
  (if (empty? name)
    (name-exception)
    (raise item {:demense.event/type :demense.event.type/item-created
                 :demense.item/id id
                 :demense.item/name name})))

(defn deactivate
  [item]
  (let [{:keys [:demense.item/id :demense.item/active?]} item]
    (if active?
      (raise item {:demense.event/type :demense.event.type/item-deactivated
                   :demense.item/id id})
      (throw (Exception. "Cannot deactivate inactive.")))))

(defn rename
  [item name]
  (let [{:keys [:demense.item/id]} item]
    (if (empty? name)
      (name-exception)
      (raise item {:demense.event/type :demense.event.type/item-renamed
                   :demense.item/id id
                   :demense.item/name name}))))

(defn check-in
  [item count]
  (let [{:keys [:demense.item/id]} item]
    (if (pos? count)
      (raise item {:demense.event/type :demense.event.type/items-checked-in
                   :demense.item/id id
                   :demense.item/count count})
      (throw (Exception. "Check in count isn't positive.")))))

(defn remove
  [item count]
  (let [{:keys [:demense.item/id]} item]
    (if (pos? count)
      (raise item {:demense.event/type :demense.event.type/items-removed
                   :demense.item/id id
                   :demense.item/count count})
      (throw (Exception. "Remove count isn't positive.")))))

(defn load-from-history
  [events]
  (reduce apply-event nil events))

