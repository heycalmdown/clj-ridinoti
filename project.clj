(defproject ridinoti "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [clj-http "3.1.0"]
                 [morse "0.2.0"]
                 [com.amazonaws/aws-lambda-java-core "1.1.0"]
                 [clj-aws-s3 "0.3.10"]]
  :aot :all)