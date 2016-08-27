(ns ridinoti.core
  (:require [clojure.edn :as edn]
            [ridinoti.config :as config]
            [ridinoti.ridibooks :as ridi]
            [ridinoti.telegram :as telegram]
            [aws.sdk.s3 :as s3])
  (:gen-class
    :implements [com.amazonaws.services.lambda.runtime.RequestStreamHandler]))

(defn includes? [xs x]
  (boolean (some #{x} xs)))

(defn side-effect [f]
  (fn [x] (f x) x))

(defn read [name]
  (try (edn/read-string
         (slurp
           (:content (s3/get-object (config/get :s3) "clj-ridinoti" name))))
       (catch com.amazonaws.services.s3.model.AmazonS3Exception _ ())))

(defn cache [name ids]
  (s3/put-object (config/get :s3) "clj-ridinoti" name (pr-str ids)))

(defn -handleRequest [_ _ _ _]
  (let [pushed (read "pushed.edn")]
    (->> (ridi/notifications 20)
         (filter #((complement includes?) pushed (:itemId %)))
         (map (side-effect telegram/push))
         (map :itemId)
         (concat pushed)
         (cache "pushed.edn"))))
