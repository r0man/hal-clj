(ns hal.core
  (:refer-clojure :exclude [keys vals])
  (:require [clojure.string :refer [blank?]]
            [no.en.core :refer [format-url parse-integer]]))

(def ^:dynamic *defaults*
  {:page :page
   :per-page :per-page})

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
           (with-hrefs k (href embedded :self)))
       res))
   res (partition 2 kvs)))

(defn keys [res]
  (let [ks (clojure.core/keys res)]
    (remove #{:_embedded :_links} ks)))

(defn vals [res]
  (map res (keys res)))

(defn- extract-page [req & [opts]]
  (let [k (:page (merge *defaults* opts))]
    [k (parse-integer (get-in req [:query-params k]))]))

(defn next-url
  "Build the URL for the next HAL resources from the Ring `request`."
  [req & [opts]]
  (if-not (blank? (:uri req))
    (let [[k v] (extract-page req opts)]
      (-> (assoc-in req [:query-params k] (inc (or v 1)))
          (format-url)))))

(defn prev-url
  "Build the URL for the previous HAL resources from the Ring `request`."
  [req & [opts]]
  (if-not (blank? (:uri req))
    (let [[k v] (extract-page req opts)]
      (if (and v (> v 1))
        (-> (assoc-in req [:query-params k] (dec v))
            (format-url))))))

(defn resource
  "Make a HAL resource for the Ring request `req` and the
  resource `res`."
  [req res & [opts]]
  (-> (with-hrefs res :self (format-url req))))

(defn resources
  "Make a HAL resource for the Ring request `req` and the resources
  `coll`."
  [req name coll & [opts]]
  (-> (with-hrefs {}
        :self (format-url req)
        :next (next-url req opts)
        :prev (prev-url req opts))
      (with-embedded name coll)))
