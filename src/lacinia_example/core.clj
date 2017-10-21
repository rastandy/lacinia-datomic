(ns lacinia-example.core
  (:require [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [ring.util.request :refer [body-string]]
            [compojure.core :refer [POST defroutes]]
            [com.walmartlabs.lacinia :as lacinia]
            [lacinia-example.schema :as schema]
            [ring.adapter.jetty :as jetty]))

(defn handler [request]
  (response
    (lacinia/execute
      schema/star-wars-schema
      (body-string request) nil nil)))

(defroutes my-routes
  (POST "/graphql" request (handler request)))

(def app
  (-> my-routes
      wrap-json-response))

(defn start-server []
  (jetty/run-jetty #'app {:port 3000
                          :join? false}))
