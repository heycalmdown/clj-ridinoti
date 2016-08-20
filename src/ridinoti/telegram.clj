(ns ridinoti.telegram
  (:require [clojure.string :as s]
            [clojure.edn :as edn]
            [morse.api :as telegram]))

(def config (edn/read-string (slurp "config.edl")))

(defn strip [item] (s/replace item #"<p>|</p>" ""))

(defn base [item]
  (if-not (s/starts-with? item "http") (str "https://ridibooks.com" item)))

(defn push [m]
  (let [message (strip (:message m))
        url (base (:landingUrl m))
        text (str message "\n" url)
        config (:telegram config)]
    (telegram/send-text (:token config)
                        (:chat-id config)
                        {:parse_mode "html"}
                        text)))

