(ns demense.rest
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [demense.handle :as h]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :refer [response]]
            [ring.middleware.json
             :refer [wrap-json-body
                     wrap-json-params
                     wrap-json-response]]))

(defroutes bare-handler
  ;; (GET "/index" []
  ;;      (response nil))
  ;; (GET "/details" [id]
  ;;      (response nil))
  (POST "/create" [id name]
        (response (h/handle (h/->CreateInventoryItem id name))))
  (POST "/deactivate" [id]
         (response (h/handle (h/->DeactivateInventoryItem id))))
  (POST "/remove" [id count]
        (response (h/handle (h/->RemoveItemsFromInventory id count))))
  (POST "/checkin" [id count]
        (response (h/handle (h/->CheckInItemsToInventory id count))))
  (POST "/rename" [id name]
         (response (h/handle (h/->RenameInventoryItem id name)))))

(def handler
  (-> bare-handler
      wrap-json-body
      wrap-json-params
      wrap-json-response))


(defn start
  []
  (jetty/run-jetty handler ))


