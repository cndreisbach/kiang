# The Computer Language Benchmarks Game
# http://shootout.alioth.debian.org/
# Contributed by Sokolov Yura
# Modified by Rick Branson
# Modified by YAGUCHI Yuya

INIT = 42
IM = 139968
IM_F = IM.to_f
IA = 3877
IC = 29573

alu =
  "GGCCGGGCGCGGTGGCTCACGCCTGTAATCCCAGCACTTTGG"+
  "GAGGCCGAGGCGGGCGGATCACCTGAGGTCAGGAGTTCGAGA"+
  "CCAGCCTGGCCAACATGGTGAAACCCCGTCTCTACTAAAAAT"+
  "ACAAAAATTAGCCGGGCGTGGTGGCGCGCGCCTGTAATCCCA"+
  "GCTACTCGGGAGGCTGAGGCAGGAGAATCGCTTGAACCCGGG"+
  "AGGCGGAGGTTGCAGTGAGCCGAGATCGCGCCACTGCACTCC"+
  "AGCCTGGGCGACAGAGCGAGACTCCGTCTCAAAAA"

iub = [
  ["a", 0.27],
  ["c", 0.12],
  ["g", 0.12],
  ["t", 0.27],

  ["B", 0.02],
  ["D", 0.02],
  ["H", 0.02],
  ["K", 0.02],
  ["M", 0.02],
  ["N", 0.02],
  ["R", 0.02],
  ["S", 0.02],
  ["V", 0.02],
  ["W", 0.02],
  ["Y", 0.02],
]

homosapiens = [
  ["a", 0.3029549426680],
  ["c", 0.1979883004921],
  ["g", 0.1975473066391],
  ["t", 0.3015094502008],
]

def make_repeat_fasta(src, n)
  v = nil
  width = 60
  l = src.length
  s = src * ((n / l) + 1)
  s.slice!(n, l)
  puts (s.scan(/.{1,#{width}}/).join("\n"))
end

$random_seq = []
$j = 0
def make_random_seq()
  last = INIT
  while true
    last = (last * IA + IC) % IM
    $random_seq << last
    return if last == INIT
  end
end

def make_lookup_table(table)
  prob = 0.0
  table.each{|v| v[1]= (prob += v[1])}
  bisector =  "(0...IM).collect do |i|\n"
  bisector << "ii = i / IM_F;\n"
  table.each do |va, vb|
    bisector << "next #{va.inspect} if #{vb.inspect} > ii\n"
  end
  bisector << "end"
  
  eval bisector
end

def make_random_fasta(table, n)
  width = 60
  lut = make_lookup_table(table)

  lustr = ''
  (0...IM).each do |j|
    lustr[j] = lut[$random_seq[j]]
  end
  lustr += lustr[0, 60]

  (1..(n/width)).each do |i|
    puts lustr[$j, width]
    $j = ($j + width) % IM
  end
  if n % width != 0
    k = ($j + (n % width)) % IM
    puts ($j < k ? lustr[$j...k] : lustr[$j..-1] + lustr[0, k])
    $j = k
  end
end

n = (ARGV[0] or 27).to_i

puts ">ONE Homo sapiens alu"
make_repeat_fasta(alu, n*2)

make_random_seq

puts ">TWO IUB ambiguity codes"
make_random_fasta(iub, n*3)

puts ">THREE Homo sapiens frequency"
make_random_fasta(homosapiens, n*5)

