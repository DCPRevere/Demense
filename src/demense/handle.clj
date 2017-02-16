(ns demense.handle
  (:require [demense.repository :as repo]
            [demense.domain :as dom]))

;; TODO: correlation and causation ids!
;; TODO: use maps instead of records for commands
;; Neither multimethods nor protocol functions can be spec'd.
;; TODO: seperate pure and io parts of the handler

(defmulti handle-pure
  (fn [agg command]
    (:demense.event/type command)))

(defmethod handle-pure
  :demense.event.type/create-item
  [agg command]
  (let [{:keys [:demense.item/id
                :demense.item/name]} command]
    (if (nil? agg)
      (dom/create agg id name)
      agg)))

(defmethod handle-pure
  :demense.event.type/deactivate-item
  [agg command]
  (dom/deactivate agg))

(defmethod handle-pure
  :demense.event.type/remove-items
  [agg command]
  (let [{:keys [:demense.item/count]} command]
    (dom/remove agg count)))

(defmethod handle-pure
  :demense.event.type/check-in-items
  [agg command]
  (let [{:keys [:demense.item/count]} command]
    (dom/check-in agg count)))

(defmethod handle-pure
  :demense.event.type/rename-item
  [agg command]
  (let [{:keys [:demense.item/name]} command]
    (dom/rename agg name)))

(defn handle-io
  [handle-pure command]
  (let [{:keys [:demense.item/id]} command
        agg (repo/get-by-id id)]
    (-> agg
        (io! (handle-pure command))
        repo/save)))

(defn handle
  [command]
  (handle-io handle-pure command))
