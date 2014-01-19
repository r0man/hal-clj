(ns hal.core-test
  #+cljs (:require-macros [cemerick.cljs.test :refer [are is deftest testing]])
  (:require [hal.core :as hal]
            #+clj [clojure.test :refer :all]
            #+clj [clojure.pprint :refer [pprint]]
            #+cljs [cemerick.cljs.test :as t]))

(def europe
  (hal/with-hrefs {:name "Europe"}
    :self "http://example.com/continents/1"))

(def germany
  (hal/with-hrefs {:name "Germany"}
    :self "http://example.com/countries/1"))

(def request
  {:scheme :http
   :server-name "example.com"
   :uri "/continents"})

(deftest test-with-hrefs
  (is (= (hal/with-hrefs {}
           :continent nil)
         {}))
  (is (= (hal/with-hrefs {}
           :continent "http://example.com/continents/1")
         {:_links {:continent {:href "http://example.com/continents/1"}}}))
  (is (= (hal/with-hrefs {}
           :continent "http://example.com/continents/1"
           :country "http://example.com/country/1")
         {:_links {:continent {:href "http://example.com/continents/1"}
                   :country {:href "http://example.com/country/1"}}})))

(deftest test-with-embedded
  (is (= (hal/with-embedded {}
           :continent nil)
         {}))
  (is (= (hal/with-embedded {}
           :continent europe)
         {:_links {:continent {:href "http://example.com/continents/1"}}
          :_embedded {:continent europe}})))

(deftest test-href
  (is (nil? (hal/href europe nil)))
  (is (nil? (hal/href europe :not-existing)))
  (is (= (hal/href europe :self) (-> europe :_links :self :href))))

(deftest test-keys
  (is (empty? (hal/keys nil)))
  (is (= [:name] (hal/keys europe))))

(deftest test-vals
  (is (empty? (hal/vals nil)))
  (is (= ["Europe"] (hal/vals europe))))

(deftest test-next-url
  (are [req expected]
    (is (= expected (hal/next-url req)))
    nil nil
    {} nil
    request "http://example.com/continents?page=2"
    (assoc-in request [:query-params :page] 2) "http://example.com/continents?page=3"
    (assoc-in request [:query-params :page] "2") "http://example.com/continents?page=3"))

(deftest test-prev-url
  (are [req expected]
    (is (= expected (hal/prev-url req)))
    nil nil
    {} nil
    request nil
    (assoc-in request [:query-params :page] 1) nil
    (assoc-in request [:query-params :page] 2) "http://example.com/continents?page=1"
    (assoc-in request [:query-params :page] "2") "http://example.com/continents?page=1"))

(deftest test-resources
  (let [res (hal/resources request :continents [europe])]
    (is (= [europe] (hal/embedded res :continents)))
    (is (= "http://example.com/continents" (hal/href res :self)))
    (is (= "http://example.com/continents?page=2" (hal/href res :next)))
    (is (nil? (hal/href res :prev))))
  (let [res (hal/resources (assoc-in request [:query-params :page] 2) :continents [europe])]
    (is (= [europe] (hal/embedded res :continents)))
    (is (= "http://example.com/continents?page=2" (hal/href res :self)))
    (is (= "http://example.com/continents?page=3" (hal/href res :next)))
    (is (= "http://example.com/continents?page=1" (hal/href res :prev)))))
