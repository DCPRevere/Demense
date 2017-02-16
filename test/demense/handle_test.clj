(ns demense.handle-test
  (:require [demense.handle :as sut]
            [clojure.test :as t]
            [demense.utils :as utils]))

;; Won't testing the handler just replicate any test we perform
;; on the domain?

;; (t/deftest "Creating item returns correct")

(t/deftest handle-pure
  (t/testing "When creating an item that already exists, return the item."
    (let [original (utils/gen-item 3 true)
          create-command
          (utils/gen-event :demense.event.type/create-item 5 "bread" nil)]
      (t/is (= (sut/handle-pure original create-command)
               original)))))
