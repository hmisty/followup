(ns followup.db
  (:gen-class)
  (:require [clojure.java.jdbc :as jdbc]))

(def file "followup.db")

(def spec {:classname "org.sqlite.JDBC"
           :subprotocol "sqlite"
           :subname file})

(defmacro create-table-if-not-exists
  " support sqlite if not exists statement when creating tables
  "
  [name & specs]
  `(jdbc/create-table (str "if not exists " (jdbc/as-identifier ~name)) ~@specs))

(defn- create-table-task
  " DDL to create table task if not exists
  "
  []
  (create-table-if-not-exists :task 
                              ;; implicit alias to ROWID which is autoincrement
                              [:id "integer primary key"]
                              ;; a short description
                              [:desc "text"]
                              ;; owner or owners, use text instead of another table
                              ;; e.g. julien, tracy
                              ;; e.g. julien/tracy
                              [:owner "text"]
                              ;; auto update when created
                              [:created_at "datetime"]
                              ;; auto update when clicking "start"
                              [:started_at "datetime"]
                              ;; auto update when ticking [x]
                              [:finished_at "datetime"]
                              ;; it's good to analyze multi-tasking later on...
                              ))

(defn setup
  " initialize database
  "
  []
  (jdbc/with-connection spec
    (create-table-task)))

