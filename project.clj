(defproject hal-clj "0.1.14"
  :description "Clojure library for the Hypertext Application Language"
  :url "https://github.com/r0man/hal-clj"
  :author "r0man"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[noencore "0.1.20" :exclusions [org.clojure/clojure]]]
  :aliases {"phantom-test" ["with-profile" "+dev" "do" ["clean"] ["doo" "phantom" "test" "once"]]
            "node-test" ["with-profile" "+dev" "do" ["clean"] ["doo" "node" "node-test" "once"]]
            "cleantest" ["with-profile" "+dev" "do"
                         ["clean"]
                         ["doo" "phantom" "test" "once"]
                         ["doo" "node" "node-test" "once"]]
            "ci" ["with-profile" "+dev" "do" ["cleantest"] ["lint"]]
            "lint" ["with-profile" "+dev" "eastwood"]}
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "target/testable.js"
                                   :output-dir "target"
                                   :main hal.test-runner
                                   :optimizations :advanced
                                   :pretty-print true}}
                       {:id "node-test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "target/node-testable.js"
                                   :output-dir "target"
                                   :main hal.test-runner
                                   :target :nodejs}}]}
  :deploy-repositories [["releases" :clojars]]
  :profiles {:dev {:plugins [[jonase/eastwood "0.2.1"]
                             [lein-doo "0.1.10"]
                             [lein-cljsbuild "1.1.7"]
                             [lein-difftest "2.0.0"]]
                   :dependencies [[org.clojure/clojure "1.9.0"]
                                  [org.clojure/clojurescript "1.10.339"]
                                  [cider/piggieback "0.3.9" :exclusions [org.clojure/clojurescript]]]}})
