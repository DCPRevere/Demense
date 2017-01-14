(ns demense.handle
  (:require [demense.repository :as repo]
            [demense.aggregate :as agg]))

(defmulti handle :event/type)

(defmethod handle :event/item-created
  [event]
  (let [{id :event/id
         name :item/name} event]
    (-> (agg/create id name)
        repo/save)))

(defmethod handle :event/item-deactivated
  [event]
  (let [{id :item/id} event
        agg (repo/get-by-id id)]
    (-> agg
        agg/deactivate
        repo/save)))

(defmethod handle :event/items-removed
  [event]
  (let [{id :item/id} event
        agg (repo/get-by-id id)]
    (-> agg
        (agg/remove :item/count)
        repo/save)))

(defmethod handle :event/items-checked-in
  [event]
  (let [{id :item/id} event
        agg (repo/get-by-id id)]
    (-> agg
        (agg/check-in :item/count)
        repo/save)))

(defmethod handle :event/item-renamed
  [event]
  (let [{id :item/id
         name :item/name} event
        agg (repo/get-by-id id)]
    (-> agg
        (agg/rename name)
        repo/save)))
