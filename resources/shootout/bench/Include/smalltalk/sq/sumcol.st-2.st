"* The Computer Language Shootout
    http://shootout.alioth.debian.org/
    contributed by Isaac Gouy *"!

!AttachableFileStream methodsFor: 'platform'!
   "* atEnd and atEndOfFile dont seem to find eof 
      for files redirected to stdin, but next does
      answer nil *"
   | ch |
   ^(ch := self next) isNil
      ifTrue: [nil]
      ifFalse: [(String with: ch), (self upTo: Character lf)]! !


      sum := sum + line asNumber.
   ].


