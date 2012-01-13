/* The Computer Language Benchmarks Game
 * http://shootout.alioth.debian.org/
 *
 * contributed by The Go Authors.
 * based on C program by Kevin Carson
 * flag.Arg hack by Isaac Gouy
 * goroutines by Atom
 */

package main

import (
   "flag"
   "fmt"
   "runtime"
   "strconv"
)

const LOG_N_CPU = 2
const N_CPU = (1 << LOG_N_CPU)

type Node struct {
   item        int
   left, right *Node
}

func bottomUpTree(item, depth int) *Node {
   if depth <= 0 {
      return &Node{item: item}
   }
   return &Node{item, bottomUpTree(2*item-1, depth-1), bottomUpTree(2*item, depth-1)}
}

func (n *Node) itemCheck() int {
   if n.left == nil {
      return n.item
   }
   return n.item + n.left.itemCheck() - n.right.itemCheck()
}

const minDepth = 4

func main() {
   runtime.GOMAXPROCS(N_CPU)

   n := 0
   flag.Parse()
   if flag.NArg() > 0 {
      n, _ = strconv.Atoi(flag.Arg(0))
   }

   maxDepth := n
   if minDepth+2 > n {
      maxDepth = minDepth + 2
   }
   stretchDepth := maxDepth + 1

   {
      check := bottomUpTree(0, stretchDepth).itemCheck()
      fmt.Printf("stretch tree of depth %d\t check: %d\n", stretchDepth, check)
   }

   longLivedTree := bottomUpTree(0, maxDepth)

   outputs := make(map[int]chan string)
   control := make(chan byte, N_CPU)
   for _depth := minDepth; _depth <= maxDepth; _depth += 2 {
      outputs[_depth] = make(chan string, 1)
      go func(depth int) {
         control <- 0

         iterations := 1 << uint(maxDepth-depth+minDepth)
         check := 0

         for i := 1; i <= iterations; i++ {
            check += bottomUpTree(i, depth).itemCheck()
            check += bottomUpTree(-i, depth).itemCheck()
         }
         outputs[depth] <- fmt.Sprintf("%d\t trees of depth %d\t check: %d\n",
            iterations*2, depth, check)

         <-control
      }(_depth)
   }
   for depth := minDepth; depth <= maxDepth; depth += 2 {
      fmt.Print(<-outputs[depth])
   }

   fmt.Printf("long lived tree of depth %d\t check: %d\n", maxDepth, longLivedTree.itemCheck())
}
