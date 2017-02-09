(ns demense.specs.eventstore
  (:require [clojure.spec :as s]
            [demense.eventstore :as evst]))

;; (s/fdef update-current
;;         :args (s/cat :evst :demense.event/eventstore
;;                      :id :demense.item/id
;;                      :events (s/coll-of :demense.event/event)
;;                      :version int?)
;;         :ret :demense.event/eventstore)
