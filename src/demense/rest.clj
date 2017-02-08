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

(def r (repo/->EventStoreRepo))

(defroutes bare-handler
  ;; (GET "/search/:id" [id]
  ;;      (response (str "You asked for: " id))) ;; (:item/activated? (repo/get-by-id r id))))
  (GET "/search/:id" [id]
       (response (repo/get-by-id r id)))
  (GET "/create/:id" [id name]
       (response (h/handle (h/->CreateInventoryItem id name))))
  (GET "/deactivate/:id" [id]
       (response (h/handle (h/->DeactivateInventoryItem id))))
  (GET "/remove/:id" [id count]
       (response (h/handle (h/->RemoveItemsFromInventory id count))))
  (GET "/checkin/:id" [id count]
       (response (h/handle (h/->CheckInItemsToInventory id count))))
  (GET "/rename/:id" [id name]
       (response (h/handle (h/->RenameInventoryItem id name)))))

(repo/get-by-id (repo/->EventStoreRepo) 3)

(h/handle (h/->CreateInventoryItem 4 "tea"))

(def handler
  (-> bare-handler
      wrap-json-body
      wrap-json-params
      wrap-json-response))

(defn start
  []
  (jetty/run-jetty handler {:port 8080}))


