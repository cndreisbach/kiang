(ns kiang.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(import '[java.io File])

(defn words [code]
  (->> (-> (str/lower-case code)
           (str/replace #"[^\w\s-]" "")
           (str/split #"\s+"))
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


;; Everything below here needs to be pulled into separate namespaces.

(def shootout-ext-map
  {:bigloo :scheme
   :chicken :scheme
   :csharp :csharp
   :fpascal :pascal
   :gawk :awk
   :gcc :c
   :gforth :forth
   :ghc :haskell
   :gprolog :prolog
   :gpp :c++
   :guile :scheme
   :jruby :ruby
   :mawk :awk
   :mzscheme :scheme
   :psyco :python
   :swiprolog :prolog})

(def shootout-exts
  [:bash
   :bigloo
   :chicken
   :clojure
   :csharp
   :erlang
   :fpascal
   :gawk
   :gcc
   :gforth
   :ghc
   :gpp
   :gprolog
   :groovy
   :guile
   :io
   :java
   :javascript
   :jruby
   :lua
   :mawk
   :mercury
   :mzscheme
   :newlisp
   :objc
   :ocaml
   :ocaml
   :ooc
   :perl
   :php
   :pike
   :poplisp
   :psyco
   :python
   :rebol
   :ruby
   :scala
   :swiprolog
   :tcl])

(defn get-file-ext
  [filename]
  (keyword (last (str/split filename #"\."))))

(defn get-lang
  [filename]
  (let [ext (get-file-ext filename)]
    (shootout-ext-map ext ext)))

(defn shootout-files []
  (->> (-> "shootout/bench" io/resource io/as-file file-seq)
       (filter #(.isFile %))
       (map #(.toString %))
       (filter #(some #{(get-file-ext %)} shootout-exts))))

(defn load-corpus-from-shootout
  []
  (reduce
   (fn [corpus file]
     (train corpus (words (slurp file)) (get-lang file)))
   {}
   (shootout-files)))

(import '[java.io FileWriter])

(defn write-to-file [file-name obj]
  (with-open [w (FileWriter. (File. file-name))]
    (binding [*out* w *print-dup* true] (write obj :pretty true :stream *out*))))