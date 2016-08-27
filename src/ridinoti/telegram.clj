(ns ridinoti.telegram
  (:require [clojure.string :as s]
            [morse.api :as telegram]
            [ridinoti.config :as config]))

(defn strip [item]
  (s/replace item #"<p>|</p>" ""))

(defn base [item]
  (if-not (s/starts-with? item "http") (str "https://ridibooks.com" item)))

(defn push [{:keys [message landingUrl]}]
  (telegram/send-text (config/get :telegram :token)
                      (config/get :telegram :chat-id)
                      {:parse_mode "html"}
                      (str "(clj) " (strip message) "\n" (base landingUrl))))
