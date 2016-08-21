(ns ridinoti.ridibooks
  (:require [clj-http.client :as http]
            [ridinoti.config :as config]
            [clojure.data.json :as json]))

(defn login [cs] (http/get "https://ridibooks.com/account/action/login"
                           {:query-params {:user_id (config/get :ridi :user-id),
                                           :password (config/get :ridi :password)}
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
