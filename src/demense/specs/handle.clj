(ns demense.specs.handle
  (:require [clojure.spec :as s]
            [clojure.spec.test :as s.t]
            [demense.handle :as han]))

;; Multimethods cannot be spec'd.
