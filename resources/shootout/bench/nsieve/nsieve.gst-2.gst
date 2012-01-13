"* The Computer Language Benchmarks Game    http://shootout.alioth.debian.org/    contributed by Isaac Gouy    modified by Eliot Miranda    then by Nicolas Cellier *"!!Tests class methodsFor: 'benchmark scripts'!nsieve2   | n |   n := self arg.   (n < 2) ifTrue: [n := 2].   self primeBenchmark2For: n to: self stdout using: Array.   ^''! !!Tests class methodsFor: 'benchmarking'!nsieve2: n using: arrayClass    | count isPrime k |   count := 0.   isPrime := arrayClass new: n withAll: true.   2 to: n do:      [:i |       (isPrime at: i) ifTrue:          [k := i.         [(k := k + i) <= n] whileTrue: [isPrime at: k put: false].         count := count + 1]].   ^count! !!Tests class methodsFor: 'benchmarking'!primeBenchmark2For: v to: output using: arrayClass   v to: v - 2 by: -1 do:      [:n| | m |      m := (2 raisedTo: n) * 10000.      output         nextPutAll: 'Primes up to ';         print: m paddedTo: 8;         print: (self nsieve2: m using: arrayClass) paddedTo: 9; nl      ]! !

Tests nsieve2!
