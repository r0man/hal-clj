(defproject hal-clj "0.1.8"
  :description "Clojure library for the Hypertext Application Language"
  :url "https://github.com/r0man/hal-clj"
  :license {:name "Eclipse Public License"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[noencore "0.1.17"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371" :scope "provided"]]
  :cljsbuild {:builds []}
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
  :deploy-repositories [["releases" :clojars]]
  :prep-tasks [["cljx" "once"]]
  :profiles {:dev {:plugins [[com.cemerick/austin "0.1.5"]
                             [com.keminglabs/cljx "0.4.0" :exclusions [org.clojure/clojure]]
                             [com.cemerick/clojurescript.test "0.3.1"]
                             [lein-cljsbuild "1.0.3"]]
                   :hooks [leiningen.cljsbuild]
                   :cljsbuild {:test-commands {"node" ["node" :node-runner "target/testable.js"]
                                               "phantom" ["phantomjs" :runner "target/testable.js"]}
                               :builds [{:source-paths ["target/classes" "target/test-classes"]
                                         :compiler {:output-to "target/testable.js"
                                                    :optimizations :advanced
                                                    :pretty-print true}}]}
                   :prep-tasks [["cljx" "once"] ["cljsbuild" "once"]]
                   :repl-options {:nrepl-middleware [cljx.repl-middleware/wrap-cljx]}
                   :test-paths ["target/test-classes"]}})
