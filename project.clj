(defproject hal-clj "0.1.0"
  :description "Clojure library for the Hypertext Application Language"
  :url "https://github.com/r0man/hal-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :lein-release {:deploy-via :clojars}
  :cljsbuild {:builds []}
  :dependencies [[noencore "0.1.11"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.5.1"]
                                  [org.clojure/clojurescript "0.0-2138"]]
                   :plugins [[com.keminglabs/cljx "0.3.2"] ;; Must be before Austin: https://github.com/cemerick/austin/issues/37
                             [com.cemerick/austin "0.1.3"]
                             [com.cemerick/clojurescript.test "0.2.1"]
                             [lein-cljsbuild "1.0.1"]]
                   :hooks [cljx.hooks leiningen.cljsbuild]
                   :cljx {:builds [{:source-paths ["src"]
                                    :output-path "target/classes"
                                    :rules :clj}
                                   {:source-paths ["src"]
                                    :output-path "target/classes"
                                    :rules :cljs}
                                   {:source-paths ["test"]
                                    :output-path "target/test-classes"
                                    :rules :clj}
                                   {:source-paths ["test"]
                                    :output-path "target/test-classes"
                                    :rules :cljs}]}
                   :cljsbuild {:builds [{:id "test"
                                         :source-paths ["test" "target/classes" "target/test-classes"]
                                         :compiler {:output-to "target/testable.js"
                                                    :output-dir "target/test"
                                                    :optimizations :advanced
                                                    :pretty-print true}}]
                               :test-commands {"phantom" ["phantomjs" :runner "target/testable.js"]}}
                   :repl-options {:nrepl-middleware [cljx.repl-middleware/wrap-cljx]}
                   :resource-paths ["test-resources"]
                   :source-paths ["target/classes"]
                   :test-paths ["test" "target/test-classes"]}})
