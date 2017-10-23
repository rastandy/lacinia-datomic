(ns lacinia-example.resolvers
  (:require
   [com.walmartlabs.lacinia.schema :as schema]
   [lacinia-example.db :as db]
   [datomic.api :as d]
   [clojure.walk :as w]
   [clojure.set :refer [map-invert]]))

(def db->graphql-map {:db/id :id
                      :character/name :name
                      :human/home-planet :home_planet
                      :droid/primary-function :primary_function
                      :character/friends :friends
                      :character/appears-in :appears_in
                      :character/type :type
                      :episode/title :episode})

(def graphql->db-map (map-invert db->graphql-map))

(defn db->graphql [map]
  (w/prewalk-replace db->graphql-map map))

(defn graphql->db [map]
  (w/prewalk-replace graphql->db-map map))

(defn tag-concrete-type [m]
  (schema/tag-with-type m (keyword (:type m))))

(defn- get-humans
  []
  (d/q '[:find [(pull ?e [:db/id :character/name :human/home-planet
                          {:character/appears-in [:episode/title]}] ) ...]
         :where [?e :character/type "human"]]
       (d/db db/conn)))

(defn- get-droids
  []
  (d/q '[:find [(pull ?e [:db/id :character/name :droid/primary-function
                          {:character/appears-in [:episode/title]}]) ...]
         :where [?e :character/type "droid"]]
       (d/db db/conn)))

(defn resolve-droid
  [_ {:keys [id] :as args} _]
  (let [id (bigint id)
        character (d/pull (d/db db/conn)
                          '[* {:character/appears-in [:episode/title]}] id)]
    (if (= (:character/type character) "droid")
      (db->graphql character)
      {})))

(defn resolve-human
  [x {:keys [id] :as args} _]
  (let [id (bigint id)
        character (d/pull (d/db db/conn)
                          '[* {:character/appears-in [:episode/title]}] id)]
    (if (= (:character/type character) "human")
      (db->graphql character)
      {})))

(defn resolve-humans
  [_ _ _]
  (db->graphql (get-humans)))

(defn resolve-droids
  [_ _ _]
  (db->graphql (get-droids)))

(defn resolve-friends
  [_ args character]
  (->> (d/q '[:find [(pull ?e [*]) ...]
              :in $ ?character
              :where [?character :character/friends ?e]]
            (d/db db/conn) (:id character))
       db->graphql
       (map tag-concrete-type)))

(defn resolve-hero
  [_ {:keys [episode] :as args} _]
  (-> (d/q '[:find (pull ?e [:db/id :character/name :character/type
                             {:character/appears-in [:episode/title]}]).
             :in $ ?episode
             :where
             [?eid :episode/title ?episode]
             [?eid :episode/hero ?e]]
           (d/db db/conn) (name episode))
      db->graphql
      tag-concrete-type))

(defn resolve-mutate-human
  [_ {:keys [id name] :as args} _]
  (d/transact db/conn [{:character/name name, :character/type "human"}])
  (db->graphql (get-humans)))
