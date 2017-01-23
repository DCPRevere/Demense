(defproject demense "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [compojure "1.5.2"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [org.clojure.gaverhae/okku "0.1.5"]
                 [com.geteventstore/eventstore-client_2.11 "3.0.5"]
                 [org.clojure/tools.logging "0.3.1"]]
  :main ^:skip-aot demense.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
