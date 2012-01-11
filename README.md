# Kiang

Kiang is a Clojure library that can guess the programming language of a code
snippet. It uses naive Bayesian filtering to do this and must be trained
before usage.

It is real dumb! That is naive Bayesian filtering, though, and it works
pretty well.

## Usage

``` clojure
(ns kiang.core)

(def corpus {})

(def corpus 
  (train corpus
         (words "def foo; 'bar'; end")
         :ruby))
;; => {:ruby {"end" 2.0, "bar" 2.0, "foo" 2.0, "def" 2.0}}

(def corpus
  (train corpus
         (words "sub foo { return 'bar'; }")
         :perl))
;; => {:perl {"bar" 2.0, "return" 2.0, "foo" 2.0, "sub" 2.0}, :ruby {"end" 2.0, "bar" 2.0, "foo" 2.0, "def" 2.0}}

(def corpus
  (train corpus
         (words "(defn foo [] 'bar')")
         :clojure))
;; => {:clojure {"bar" 2.0, "foo" 2.0, "defn" 2.0}, :perl {"bar" 2.0, "return" 2.0, "foo" 2.0, "sub" 2.0}, :ruby {"end" 2.0, "bar" 2.0, "foo" 2.0, "def" 2.0}}

(guess-language corpus (words "def add(x, y); a + b; end"))
;; => :ruby

(guess-language corpus (words "(defn add [x y] (+ x y))"))
;; => :clojure

(guess-language corpus (words "(defn sub [return] (return))"))
;; => :perl
;; We do not have a great corpus and this is a ridiculous code snippet.
```

## License

Copyright (C) 2012 Clinton R. Nixon

Distributed under the MIT License.
