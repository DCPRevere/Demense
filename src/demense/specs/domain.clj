(ns demense.specs.domain
  (:require [clojure.spec :as s]
            [demense.domain :as dom]
            [clojure.spec.test :as s.t]
            [clojure.spec.gen :as g]))

(s/fdef dom/append-event
        :args (s/cat :agg :demense.item/item
                     :event :demense.event/event)
        :ret :demense.item/item
        :fn (fn [{:keys [args ret]}]
              (= (+ 1 (count (:demense.item/changes (:agg args))))
                 (count (:demense.item/changes ret)))))

(s.t/check `dom/append-event)

(s/fdef dom/raise
        :args (s/cat :agg :demense.item/item
                     :event :demense.event/event)
        :ret :demense.item/item
        ;; the raising of an event should not change the id of the aggregate.
        ;; the testing of actualy apending theevent should hapen in a test of the application, not the test of the raising.
        )

(s.t/check `dom/raise)

(s/fdef dom/create
        :args (s/cat :agg :demense.item/item
                     :id :demense.item/id
                     :name :demense.item/name)
        :ret :demense.item/item
        :fn (fn [{:keys [args ret]}]
              (and (= (:id args) (:demense.item/id ret))
                   (:demense.item/activated? ret))))

(s.t/check `dom/create)

(s/fdef dom/deactivate
        :args (s/cat :agg :demense.item/item)
        :ret :demense.item/item
        :fn #(not (:demense.item/activated? (:ret %))))

(s.t/check `dom/create)

(s/fdef dom/rename
        :args (s/cat :agg :demense.item/item
                     :name :demense.item/name)
        :ret :demense.item/item)

(s.t/check `dom/create)

(s/fdef dom/check-in
        :args (s/cat :agg :demense.item/item
                     :count :demense.item/count)
        :ret :demense.item/item)

(s.t/check `dom/check-in)

(s/fdef dom/remove
        :args (s/cat :agg :demense.item/item
                     :count :demense.item/count)
        :ret :demense.item/item)

(g/generate (s/gen (s/cat :event (s/* int?))))

(s.t/check `dom/remove)

(s/fdef dom/load-from-history
        :args (s/coll-of (s/cat :create :demense.event.type/item-created
                                :others (s/* :demense.event/event))
                         :count 1)
        :ret :demense.item/item)

(s.t/check `dom/load-from-history)
