(ns kiang.shootout
  (:use [kiang.core])
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

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

