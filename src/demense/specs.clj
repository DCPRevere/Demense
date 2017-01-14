(ns demense.specs
  (:require [clojure.spec :as s]))

(defmulti event-type :event/type)

(defmethod event-type
  :event/item-created [_]
  (s/keys :req [:event/type :event/id :item/name]))

(defmethod event-type
  :event/item-deactivated [_]
  (s/keys :req [:event/type]))

(defmethod event-type
  :event/item-renamed [_]
  (s/keys :req [:event/type :item/name]))

(defmethod event-type
  :event/items-checked-in [_]
  (s/keys :req [:event/type]))

(defmethod event-type
  :event/items-removed [_]
  (s/keys :req [:event/type]))

(s/def :event/event
  (s/multi-spec event-type :event/type))

(s/valid? :event/event
          {:event/type :event/items-removed})
