(ns ridinoti.config
  (:require [clojure.edn :as edn]))

(def config (edn/read-string (slurp "config.edl")))

(defn- private-get [config keys]
  (if (seq keys)
    (private-get ((first keys) config) (rest keys))
    config))

(defn get [& keys]
  (private-get config keys))
