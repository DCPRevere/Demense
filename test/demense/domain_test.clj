(ns demense.domain-test
  (:require [demense.domain :as sut]
            [clojure.test :as t]
            [clojure.spec :as s]
            [demense.utils :as utils]))

(t/deftest apply-event
  (let [original {:demense.item/id 345
                  :demense.item/activated? false}
        created-event {:demense.event/type :demense.event.type/item-created
                       :demense.item/id 556}
        deactivated-event {:demense.event/type
                           :demense.event.type/item-deactivated}
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

;; This could just be specd. There is no need for this to be unit tested as well.
(t/deftest append-event
  (let [event (utils/gen-event :demense.event.type/test 3 "coffee" nil)
        another-event (utils/gen-event :demense.event.type/test 5 "milk" nil)
        no-changes (utils/gen-item 345 true)
        with-changes (assoc no-changes :demense.item/changes [event])
        with-more-changes
        (assoc no-changes :demense.item/changes [event another-event])]
    (t/testing "Aggregate with no changes has a change appended."
      (t/is (= (sut/append-event no-changes event)
               with-changes)))
    (t/testing "Multiple events are added to the end of the list of changes"
      (t/is (= (sut/append-event with-changes another-event)
               with-more-changes)))))
