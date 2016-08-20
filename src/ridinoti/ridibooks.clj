(ns ridinoti.ridibooks
  (:require [clojure.edn :as edn]
            [clj-http.client :as http]
            [clojure.data.json :as json]))

(def config (edn/read-string (slurp "config.edl")))

(defn login [cs] (http/get "https://ridibooks.com/account/action/login"
                           {:query-params {:user_id (-> config :ridi :user-id),
                                           :password (-> config :ridi :password)}
                            :cookie-store cs}))

(defn fetch [cs limit] (http/get "http://api.ridibooks.com/v0/notifications"
                                 {:query-params {"limit" limit}
                                  :as :json
                                  :cookie-store cs}))

(defn notifications [limit]
  (let [cs (clj-http.cookies/cookie-store)]
    (login cs)
    (http/get "https://ridibooks.com" {:cookie-store cs})
    (->> (fetch cs limit)
         :body
         :notifications
         (map #(json/read-str % :key-fn keyword)))))
