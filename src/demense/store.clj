(ns demense.store
  (:require [clojure.tools.logging :as log]
            [okku.core :as okku])
  (:import akka.actor.UntypedActor
           eventstore.j.EventDataBuilder
           eventstore.j.SettingsBuilder
           eventstore.j.ReadEventBuilder
           eventstore.j.WriteEventsBuilder
           eventstore.tcp.ConnectionActor
           eventstore.Settings
           java.net.InetSocketAddress))

(defonce as (okku/actor-system "evst"))

(def settings
  (.build
   (doto (SettingsBuilder.)
     (.address (InetSocketAddress. "127.0.0.1" 1113))
     (.defaultCredentials "admin" "changeit"))))

(def connection
  (okku/spawn (ConnectionActor/getProps settings) :in as))

(def read-result
  (okku/spawn
   (okku/actor
    (onReceive
     [message]
     (case (class message)
       ReadEventCompleted (log/info "Read event completed.")
       Failure (log/error "Read failure.")
       (log/error "Unhandled message."))))
   :in as))

(def write-result
  (okku/spawn
   (okku/actor
    (onReceive
     [message]
     (case (class message)
       WriteEventsCompleted (log/info "Event written.")
       Failure (log/error "Write failure.")
       (log/error "Unhandled message."))))
   :in as))

(def read-event
  (.build
   (doto (ReadEventBuilder. "my-stream")
     (.first)
     (.resolveLinkTos false)
     (.requireMaster true))))

(def event
  (.build
   (doto (EventDataBuilder. "my-event")
     (.data "my data")
     (.metadata "first event"))))

(def write-events
  (.build
   (doto (WriteEventsBuilder. "my-stream")
     (.addEvent event)
     (.expectAnyVersion))))

(.tell connection read-event read-result)

(.tell connection write-events write-result)

(defprotocol Store
  (get-events [id])
  (save-events [events stream])
  (publish [event stream]))

(defrecord EventStore
    [host port]
  Store
  (get-events [id])

  (save-events [events stream])

  (publish [event stream]))
