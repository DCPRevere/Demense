(ns demense.specs
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as g]))

(s/def ::id int?)

(s/def :demense.item/id ::id)
(s/def :demense.item/name (s/and string? seq))
(s/def :demense.item/count pos-int?)
(s/def :demense.item/active? boolean?)

(s/def :demense.event/type keyword?)

(defmulti event-type :demense.event/type)

(defmethod event-type
  :demense.event.type/item-created [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/name]))

(defmethod event-type
  :demense.event.type/item-deactivated [_]
  (s/keys :req [:demense.event/type :demense.item/id]))

(defmethod event-type
  :demense.event.type/item-renamed [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/name]))

(defmethod event-type
  :demense.event.type/items-checked-in [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/count]))

(defmethod event-type
  :demense.event.type/items-removed [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/count]))

(s/def :demense.event/event (s/multi-spec event-type :demense.event/type))

(s/def :demense.event.type/item-created
  (s/and :demense.event/event
         #(= :demense.event.type/item-created
             (:demense.event/type %))))

(s/def :demense.item/changes (s/coll-of :demense.event/event))

(s/def :demense.item/item
  (s/keys :req [:demense.item/id
                :demense.item/active?]
          :opt [:demense.item/changes]))

(defmulti command-type :demense.event/type)

(defmethod command-type
  :demense.event.type/create-item [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/name]))

(defmethod command-type
  :demense.event.type/deactivate-item [_]
  (s/keys :req [:demense.event/type :demense.item/id]))

(defmethod command-type
  :demense.event.type/remove-items [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/count]))

(defmethod command-type
  :demense.event.type/check-in-items [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/count]))

(defmethod command-type
  :demense.event.type/rename-item [_]
  (s/keys :req [:demense.event/type :demense.item/id :demense.item/name]))

(s/def :demense.event/command (s/multi-spec command-type :demense.event/type))
