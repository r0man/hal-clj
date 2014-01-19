(ns hal.core-test
  (:refer-clojure :exclude [keys vals])
  (:require [clojure.test :refer :all]
            [hal.core :refer :all]))

(def europe
  {:name "Europe"
   :_links {:self {:href "http://example.com/continents/1"}}})

(def germany
  {:name "Germany"
   :_links {:self {:href "http://example.com/countries/1"}}})

(deftest test-with-hrefs
  (is (= (with-hrefs {}
           :continent nil)
         {}))
  (is (= (with-hrefs {}
           :continent "http://example.com/continents/1")
         {:_links {:continent {:href "http://example.com/continents/1"}}}))
  (is (= (with-hrefs {}
           :continent "http://example.com/continents/1"
           :country "http://example.com/country/1")
         {:_links {:continent {:href "http://example.com/continents/1"}
                   :country {:href "http://example.com/country/1"}}})))

(deftest test-with-embedded
  (is (= (with-embedded {}
           :continent nil)
         {}))
  (is (= (with-embedded {}
           :continent europe)
         {:_links {:continent {:href "http://example.com/continents/1"}}
          :_embedded {:continent europe}})))

(deftest test-href
  (is (nil? (href europe nil)))
  (is (nil? (href europe :not-existing)))
  (is (= (href europe :self) (-> europe :_links :self :href))))

(deftest test-keys
  (is (empty? (keys nil)))
  (is (= [:name] (keys europe))))

(deftest test-vals
  (is (empty? (vals nil)))
  (is (= ["Europe"] (vals europe))))
