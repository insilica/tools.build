(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]))

(def lib 'co.insilica/tools.build)
(def version "0.8.3")
(defn get-version [opts]
  (str version (when (:snapshot opts) "-SNAPSHOT")))

(defn test [opts]
  (bb/clean opts)
  (b/process {:command-args ["clojure" "-X:test"]})
  opts)

(defn jar [opts]
  (-> opts
      (assoc :lib lib
             :src-dirs ["src/main/clojure" "src/main/resources"]
             :version (get-version opts))
      bb/clean
      (assoc :src-pom "template/pom.xml")
      bb/jar))

(defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
  (-> opts test jar))

(defn deploy "Deploy the JAR to Clojars." [opts]
  (-> opts
      (assoc :lib lib :version (get-version opts))
      bb/deploy))
