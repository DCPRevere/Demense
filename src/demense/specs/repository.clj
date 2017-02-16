(ns demense.specs.repository
  (:require [clojure.spec :as s]
            [clojure.spec.test :as s.t]
            [demense.repository :as repo]))

(s/fdef repo/remove-prefix
        :args (s/cat :str string? :prefix string?)
        :ret string?
        :fn (fn [{:keys [args ret]}]
              (or (= ret (:str args))
                  (= (:str args) (str (:prefix args) ret)))))

(s.t/check `repo/remove-prefix)
