(ns kiang.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(import '[java.io File])

(defn words [code]
  (-> (str/lower-case code)
      (str/replace #"[^\w\s-]" "")
      (str/split #"\s+")))

(defn train
  [corpus words lang]
  (reduce
   (fn [corpus word]
     (update-in corpus [lang word] (fnil inc 1.0)))
   corpus
   words))

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

(def shootout-ext-map
  {'bigloo' :scheme
   'chicken' :scheme
   'csharp' :csharp
   'fpascal' :pascal
   'gawk' :awk
   'gcc' :c
   'gforth' :forth
   'ghc' :haskell
   'gprolog' :prolog
   'gpp' :c++
   'guile' :scheme
   'mawk' :awk
   'mzscheme' :scheme
   'psyco' :python
   'swiprolog' :prolog})

(defn shootout-files []
  (map #(.toString %)
       (filter #(.isFile %)
               (-> "shootout/bench" io/resource io/as-file file-seq))))

(defn get-lang
  [filename]
  (let [ext (last (str/split filename #"\."))]
    (shootout-ext-map ext (keyword ext))))

(defn load-corpus-from-shootout
  []
  (reduce
   (fn [corpus file]
     (train corpus (words (slurp file)) (get-lang file)))
   {}
   (shootout-files)))

