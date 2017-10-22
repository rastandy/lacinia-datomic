(ns lacinia-example.db
  (:require [io.rkn.conformity :as c]
            [datomic.api :as d]))

(def uri "datomic:mem://lacinia-example")

(d/create-database uri)
(def conn (d/connect uri))

(def norms-map (c/read-resource "datomic-schema.edn"))
(c/ensure-conforms conn norms-map [:lacinia-example/star-wars-schema])

(def data (c/read-resource "datomic-data.edn"))
(c/ensure-conforms conn data [:lacinia-example/star-wars-data])
