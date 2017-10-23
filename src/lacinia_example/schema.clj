(ns lacinia-example.schema
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as schema]
            [lacinia-example.resolvers :as r]))

(def star-wars-schema
  (-> (io/resource "schema.edn")
      slurp
      edn/read-string
      (attach-resolvers {:resolve-hero           r/resolve-hero
                         :resolve-human          r/resolve-human
                         :resolve-humans         r/resolve-humans
                         :resolve-droid          r/resolve-droid
                         :resolve-droids         r/resolve-droids
                         :resolve-friends        r/resolve-friends
                         :resolve-mutate-human   r/resolve-mutate-human})
      schema/compile))
