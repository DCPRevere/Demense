(ns demense.handle
  (:require [demense.repository :as repo]
            [demense.item :as item]))

;; TODO: correlation and causation ids!
;; Neither multimethods nor protocol functions can be spec'd.

(defmulti handle-pure
  (fn [_ command]
    (:demense.event/type command)))

(defmethod handle-pure
  :demense.event.type/create-item
  [item command]
  (let [{:keys [:demense.item/id
                :demense.item/name]} command]
    (if (nil? item)
      (item/create item id name)
      item)))

(defmethod handle-pure
  :demense.event.type/deactivate-item
  [item command]
  (item/deactivate item))

(defmethod handle-pure
  :demense.event.type/remove-items
  [item command]
  (let [{:keys [:demense.item/count]} command]
    (item/remove item count)))

(defmethod handle-pure
  :demense.event.type/check-in-items
  [item command]
  (let [{:keys [:demense.item/count]} command]
    (item/check-in item count)))

(defmethod handle-pure
  :demense.event.type/rename-item
  [item command]
  (let [{:keys [:demense.item/name]} command]
    (item/rename item name)))

(defn handle
  [command]
  (let [{:keys [:demense.item/id]} command
        item (repo/get-by-id id)]
    (-> item
        (handle-pure command)
        repo/save)))
