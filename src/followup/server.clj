;; package to a standalone jar
;; run as a standalone server

(ns followup.server 
  (:gen-class)
  (:use [followup.core :only (app)])
  (:require [ring.adapter.jetty :as jetty]))

(defn -main [& [port]]
  (let [port (if port (Integer/parseInt port) 8082)]
    (jetty/run-jetty #'app {:join? false :port port})))
