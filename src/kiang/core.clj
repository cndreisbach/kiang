(ns kiang.core
  (:use [clojure.pprint :only [pprint]]
        [clojure.set :only [intersection]])
  (:require [clojure.string :as str]))

(defn- split-and-strip [code]
  (-> (str/lower-case code)
           (str/replace #"[^\w\s-]" "")
           (str/split #"\s+")))

(defn words [code]
  (->> (split-and-strip code)
       (filter (fn [word] (and
                           (< 0 (count word) 15)
                           (re-find #"[a-z]" word))))))

(defn train
  [corpus words lang]
  (reduce
   (fn [corpus word]
     (update-in corpus [lang word] (fnil inc 1.0)))
   corpus
   words))

(defn find-common-words
  [corpus]
  (apply
   intersection
   (map
    (fn [words]
      (set (keys words)))
    (vals corpus))))

(defn clean
  [corpus]
  (let [common-words (find-common-words corpus)]
    (into {}
          (map
           (fn [lang]
             [lang (apply dissoc (corpus lang) common-words)])
           (keys corpus)))))

(defn totals
  [corpus]
  (reduce
   (fn [totals word-counts]
     (merge-with + totals word-counts))
   {}
   (vals corpus)))

(defn language-probability
  [corpus words lang]
  (let [word-counts (corpus lang)
        totals (totals corpus)]
    (reduce
     (fn [score word]
       (+ score (Math/log (/ (word-counts word 1.0) (totals word 1.0)))))
     0.0
     words)))

(defn score-by-language
  [corpus words]
  (into {}
        (map
         (fn [lang]
           [lang (language-probability corpus words lang)])
         (keys corpus))))

(defn guess-language
  [corpus words]
  (let [score (score-by-language corpus words)]
    (first
     (sort-by
      (fn [lang]
        (- (score lang)))
      (keys corpus)))))

(defn write-corpus
  [corpus filename]
  (spit filename (with-out-str (pprint corpus))))

(defn read-corpus
  [filename]
  (read-string (slurp filename)))