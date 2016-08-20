(ns ridinoti.core
  (:require [clojure.edn :as edn])
  (:require [ridinoti.ridibooks :as ridi])
  (:require [ridinoti.telegram :as telegram])
  (:gen-class))

(defn includes? [xs x] (boolean (some #{x} xs)))

(defn side-effect [f] (fn [x] (f x) x))

(defn read [name]
  (try (edn/read-string (slurp name))
       (catch Exception e ())))

(defn cache [name ids] (spit name (pr-str ids)))

(defn -main []
  (let [pushed (read "/tmp/pushed.edn")]
    (->> (ridi/notifications 20)
         (filter #((complement includes?) pushed (:itemId %)))
         (map (side-effect telegram/push))
         (map :itemId)
         (concat pushed)
         (cache "/tmp/pushed.edn"))))
