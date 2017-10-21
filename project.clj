(defproject lacinia-example "0.1.0-SNAPSHOT"
  :description "Example app using Lacinia GraphQL library"
  :url "http://github.com/bnoguchi/lacinia-example"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-beta2"]
                 [ring/ring-jetty-adapter "1.6.2"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.6.0"]
                 [com.walmartlabs/lacinia "0.21.0"]
                 [io.rkn/conformity "0.5.1"]]
  :plugins  [[lein-ring "0.12.1"]]
  :ring {:handler lacinia-example.core/app})
