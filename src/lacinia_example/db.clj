(ns lacinia-example.db
  (:require
    [com.walmartlabs.lacinia.schema :as schema]))

(def ^:private humans-data
  (atom   [{:id          "1000"
            :name        "Luke Skywalker"
            :friends     ["1002", "1003", "2000", "2001"]
            :appears-in  ["NEWHOPE" "EMPIRE" "JEDI"]
            :home-planet "Tatooine"
            :force-side  "3001"}
           {:id          "1001"
            :name        "Darth Vader"
            :friends     ["1004"]
            :appears-in  ["NEWHOPE" "EMPIRE" "JEDI"]
            :home-planet "Tatooine"
            :force-side  "3000"}
           {:id          "1003"
            :name        "Leia Organa"
            :friends     ["1000", "1002", "2000", "2001"]
            :appears-in  ["NEWHOPE" "EMPIRE" "JEDI"]
            :home-planet "Alderaan"
            :force-side  "3001"}
           {:id         "1002"
            :name       "Han Solo"
            :friends    ["1000", "1003", "2001"]
            :appears-in ["NEWHOPE" "EMPIRE" "JEDI"]
            :force-side "3001"}
           {:id         "1004"
            :name       "Wilhuff Tarkin"
            :friends    ["1001"]
            :appears-in ["NEWHOPE"]
            :force-side "3000"}]))

(def ^:private droids-data
  (atom   [{:id               "2001"
            :name             "R2-D2"
            :friends          ["1000", "1002", "1003"]
            :appears-in       ["NEWHOPE" "EMPIRE" "JEDI"]
            :primary-function "ASTROMECH"}
           {:id               "2000"
            :name             "C-3PO"
            :friends          ["1000", "1002", "1003", "2001"]
            :appears-in       ["NEWHOPE" "EMPIRE" "JEDI"]
            :primary-function "PROTOCOL"}]))

(def ^:private hero-data
  {"NEWHOPE" "2001"
   "EMPIRE"  "1002"
   "JEDI"    "1000"})

(defn- first-match [data key value]
  (first (filter #(= (get % key) value) data)))

(defn- find-by-id
  [data target-id]
  (->> data
       (map :id)
       (filter #{target-id})
       first))

(defn- get-humans
  []
  (map #(schema/tag-with-type % :human) @humans-data))

(defn- get-droids
  []
  (map #(schema/tag-with-type % :droid) @droids-data))

(defn resolve-hero
  [_ args _]
  (let [episode (get args :episode "NEWHOPE")
        characters (concat (get-humans) (get-droids))
        hero-id (get hero-data episode)]
    (find-by-id characters hero-id)))

(defn resolve-droid
  [_ {:keys [id] :as args} _]
  (find-by-id (get-droids) id))

(defn resolve-friends
  [_ _ value]
  (let [characters (concat (get-humans) (get-droids))]
    (map #(find-by-id characters %) (:friends value))))

(defn resolve-human
  [_ {:keys [id] :as args} _]
  (find-by-id (get-humans) id))

(defn resolve-humans
  [_ _ _]
  (get-humans))

(defn resolve-droids
  [_ _ _]
  (get-droids))

(defn resolve-mutate-human
  [_ {:keys [id name] :as args} _]
  (swap! humans-data conj {:id id :name name})
  (mapv #(schema/tag-with-type % :human) @humans-data))
