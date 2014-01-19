(ns hal.core-test
  #+cljs (:require-macros [cemerick.cljs.test :refer [are is deftest testing]])
  (:require [hal.core :as hal]
            #+clj [clojure.test :refer :all]
            #+cljs [cemerick.cljs.test :as t]))

(def europe
  (hal/with-hrefs {:name "Europe"}
    :self "http://example.com/continents/1"))

(def germany
  (hal/with-hrefs {:name "Germany"}
    :self "http://example.com/countries/1"))

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
