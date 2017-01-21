(ns demense.events)

(defprotocol Event)

(defrecord InventoryItemCreated
    [id])

(defrecord InventoryItemDeactivated
    [id])

(defrecord InventoryItemRenamed
    [id name])

(defrecord ItemsCheckedInToInventory
    [id count])

(defrecord ItemsRemovedFromInventory
    [id count])
