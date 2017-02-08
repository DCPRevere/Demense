(ns demense.core
  (:gen-class)
  (:require [demense.rest :as rest]))

(defn -main
  [& args]
  (rest/start))
