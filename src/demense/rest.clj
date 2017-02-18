(ns demense.rest
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [demense.handle :as h]
            [demense.repository :as repo]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :refer [response]]
            [ring.middleware.json
             :refer [wrap-json-body
                     wrap-json-params
                     wrap-json-response]]
            [demense.repository :as repo]))

;; TODO: render an aggregate as a string.
;; TODO: helper functions for creating maps
;; TODO: ensure that handle is returning the aggregate.

(defroutes bare-handler
  (GET "/search/:id" [id] (response (repo/get-by-id id)))
  (GET "/create/:id" [id name]
       (response (h/handle
                  {:demense.event/type :demense.event.type/create-item
                   :demense.item/id id
                   :demense.item/name name})))
  (GET "/deactivate/:id" [id]
       (response (h/handle
                  {:demense.event/type :demense.event.type/deactivate-item
                   :demense.item/id id})))
  (GET "/remove/:id" [id count]
       (response (h/handle
                  {:demense.event/type :demense.event.type/remove-items
                   :demense.item/id id
                   :demense.item/count count})))
  (GET "/check-in/:id" [id count]
       (response (h/handle
                  {:demense.event/type :demense.event.type/check-in-items
                   :demense.item/id id
                   :demense.item/count count})))
  (GET "/rename/:id" [id name]
       (response (h/handle
                  {:demense.event/type :demense.event.type/rename-item
                   :demense.item/id id
                   :demense.item/name name})))
  )

(def handler
  (-> bare-handler
      wrap-json-body
      wrap-json-params
      wrap-json-response))

(defn start []
  (jetty/run-jetty handler {:port 8080}))


