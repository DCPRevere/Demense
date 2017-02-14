(ns demense.test.utils
  (:require  [clojure.test :as t]))

(defn gen-item [id activated?]
  {:demense.item/id id
   :demense.item/activated? activated?})

(defn gen-event [type id name count]
  {:demense.event/type type
   :demense.item/id id
   :demense.item/name name
   :demense.item/count count})
