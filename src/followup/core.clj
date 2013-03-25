(ns followup.core
  (:gen-class)
  (:use [clojure.pprint]
        [compojure.core :only (GET POST defroutes)]
        [hiccup.core]
        [hiccup.page])
  (:require [followup.db :as db]
            [clojure.java.jdbc :as jdbc]
            [ring.util.response :as resp]
            [compojure.handler]
            [compojure.route]))

;; FIXME could I init something here?
(def myname "evan")
(db/setup)

;; helpers
#_(defmacro partition-by-key
  [k coll]
  `(partition-by #(~k %) ~coll))

;; data
(defn get-all-tasks
  []
  (jdbc/with-connection db/spec
    (jdbc/with-query-results res 
      ["select id, desc, owner, created_at, started_at, finished_at from task"]
      (doall res))))

;; the pages
(defn htodo
  [title tasks]
  [:div
   [:h1 title]
   [:ul
    (for [x tasks]
      [:li (with-out-str (pprint x))])]])

(defn homepage
  []
  (let [[todo-tasks followup-tasks] 
        (partition-by #(= (:owner %) myname) (get-all-tasks))]
  (html5 {:lang "en"}
         [:body
          (htodo "todo" todo-tasks)
          (htodo "follow-up" followup-tasks)])))

;; the actions
(defn update-task
  [id k v]
  (jdbc/with-connection db/spec
    (jdbc/transaction
      (jdbc/update-values :task ["id=?" id] {k v}))))

(defn datetime
  []
  (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss") (java.util.Date.)))

(defn start-task
  [id]
  (update-task id :started_at (datetime)))

(defn finish-task
  [id]
  (update-task id :finished_at (datetime)))

;; the handlers
(defroutes handler
  (compojure.route/resources "/")
  (GET "/" request (homepage))
  (POST "/:id/start" [id] (start-task id))
  (POST "/:id/finish" [id] (finish-task id))
  ;; default route
  (GET "*" request
       {:status 200 :body (with-out-str (pprint request))}))

;; the web app
(def app
  (-> handler
      (compojure.handler/site)))

