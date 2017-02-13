(ns demense.domain-test
  (:require [demense.domain :as sut]
            [clojure.test :as t]
            [clojure.spec :as s]))

;; Perhaps I should create a function that allows easier creation of
;; events and aggregates.

(t/deftest apply-event
  (let [original {:demense.item/id 345
                  :demense.item/activated? false}
        created-event {:demense.event/type :demense.event.type/item-created
               :demense.item/id 556}
        deactivated-event {:demense.event/type :demense.event.type/item-deactivated}
        irrelevant-event {:demense.event/type :demense.event.type/foo}
        created {:demense.item/id 556
                 :demense.item/activated? true}
        deactivated {:demense.item/id 345
                     :demense.item/activated? false}]
    (t/testing (str "Aggregate is activated and its id"
                    " is that provided by the created event.")
      (t/is (= (sut/apply-event original created-event)
               created)))
    (t/testing (str "Aggregate is deactivated and its id"
                    " doesn't change by deactivated event.")
      (t/is (= (sut/apply-event original deactivated-event)
               deactivated)))
    (t/testing (str "Aggregate is returned if the event"
                    " is not recorgnised")
      (t/is (= (sut/apply-event original irrelevant-event)
               original)))))

(t/deftest append-event
  (t/testing "Aggregate with no changes has a change appended."))
