(ns hal.core
  (:require [clojure.string :refer [blank?]]))

(defn embedded
  ([res]
     (:_embedded res))
  ([res k]
     (get (embedded res) k)))

(defn links
  ([res]
     (:_links res))
  ([res k]
     (get (links res) k)))

(defn href [res k]
  (:href (get (links res) k)))

(defn self-href [res]
  (-> res :_links :self :href))

(defn with-hrefs
  [res & kvs]
  (reduce
   (fn [res [k href]]
     (if-not (blank? href)
       (assoc-in res [:_links k :href] href)
       res))
   res (partition 2 kvs)))

(defn with-embedded
  [res & kvs]
  (reduce
   (fn [res [k embedded]]
     (if embedded
       (-> (assoc-in res [:_embedded k] embedded)
           (with-hrefs k (self-href embedded))
           identity)
       res))
   res (partition 2 kvs)))
