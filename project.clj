(defproject followup "0.1.0-SNAPSHOT"
  :description "followup: a new concept of todo"
  :url "http://github.com/hmisty/followup"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.0.1"]
                 [compojure "1.0.1"]
                 [hiccup "1.0.3"]
                 [org.clojure/java.jdbc "0.1.1"]
                 [org.xerial/sqlite-jdbc "3.7.2"]]
  :main followup.server
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler followup.core/app})
