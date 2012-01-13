# The Computer Language Benchmarks Game
# http://shootout.alioth.debian.org/
#
# contributed by Ersun Warncke
#
# Based on regex-dna Perl #7 by Danny Bauer, Mirco Wahab, Jake Berner,
# Andrew Rodland, and Daniel Green.
#
# use re::engine::RE2 - which handles large grouped alternative matches
# very efficiently.  Do all counting in a single pass (2X as fast on
# a single core as forking version on 2 cores).
#
# Do substitutions one at a time.  Even with 11 subs this is still faster.

use strict;
use warnings;

my $l_file  = -s STDIN;
my $content; read STDIN, $content, $l_file;
# this is significantly faster than using <> in this case

$content =~ s/^>.*//mg;
$content =~ tr/\n//d;

my $l_code = length $content;

my @seq = (
    'agggtaaa|tttaccct',
    '[cgt]gggtaaa|tttaccc[acg]',
    'a[act]ggtaaa|tttacc[agt]t',
    'ag[act]gtaaa|tttac[agt]ct',
    'agg[act]taaa|ttta[agt]cct',
    'aggg[acg]aaa|ttt[cgt]ccct',
    'agggt[cgt]aa|tt[acg]accct',
    'agggta[cgt]a|t[acg]taccct',
    'agggtaa[cgt]|[acg]ttaccct'
);

my %seq = (
    aaggtaaa => 0,
    acggtaaa => 0,
    agagtaaa => 0,
    agcgtaaa => 0,
    aggataaa => 0,
    aggctaaa => 0,
    agggaaaa => 0,
    agggcaaa => 0,
    aggggaaa => 0,
    agggtaaa => 0,
    agggtaac => 0,
    agggtaag => 0,
    agggtaat => 0,
    agggtaca => 0,
    agggtaga => 0,
    agggtata => 0,
    agggtcaa => 0,
    agggtgaa => 0,
    agggttaa => 0,
    aggttaaa => 0,
    agtgtaaa => 0,
    atggtaaa => 0,
    attaccct => 0,
    cgggtaaa => 0,
    cttaccct => 0,
    ggggtaaa => 0,
    gttaccct => 0,
    tataccct => 0,
    tctaccct => 0,
    tgggtaaa => 0,
    tgtaccct => 0,
    ttaaccct => 0,
    ttcaccct => 0,
    ttgaccct => 0,
    tttaacct => 0,
    tttacact => 0,
    tttaccat => 0,
    tttaccca => 0,
    tttacccc => 0,
    tttacccg => 0,
    tttaccct => 0,
    tttaccgt => 0,
    tttacctt => 0,
    tttacgct => 0,
    tttactct => 0,
    tttagcct => 0,
    tttatcct => 0,
    tttcccct => 0,
    tttgccct => 0,
    ttttccct => 0,
);

my %map = (
    'agg[act]taaa|ttta[agt]cct' => [
        'aggataaa',
        'aggctaaa',
        'aggttaaa',
        'tttaacct',
        'tttagcct',
        'tttatcct'
    ],
    'aggg[acg]aaa|ttt[cgt]ccct' => [
        'agggaaaa',
        'agggcaaa',
        'aggggaaa',
        'tttcccct',
        'tttgccct',
        'ttttccct'
    ],
    '[cgt]gggtaaa|tttaccc[acg]' => [
        'cgggtaaa',
        'ggggtaaa',
        'tgggtaaa',
        'tttaccca',
        'tttacccc',
        'tttacccg'
    ],
    'ag[act]gtaaa|tttac[agt]ct' => [
        'agagtaaa',
        'agcgtaaa',
        'agtgtaaa',
        'tttacact',
        'tttacgct',
        'tttactct'
    ],
    'agggtaa[cgt]|[acg]ttaccct' => [
        'agggtaac',
        'agggtaag',
        'agggtaat',
        'attaccct',
        'cttaccct',
        'gttaccct'
    ],
    'agggtaaa|tttaccct' => [
        'agggtaaa',
        'tttaccct'
    ],
    'agggt[cgt]aa|tt[acg]accct' => [
        'agggtcaa',
        'agggtgaa',
        'agggttaa',
        'ttaaccct',
        'ttcaccct',
        'ttgaccct'
    ],
    'agggta[cgt]a|t[acg]taccct' => [
        'agggtaca',
        'agggtaga',
        'agggtata',
        'tataccct',
        'tctaccct',
        'tgtaccct'
    ],
    'a[act]ggtaaa|tttacc[agt]t' => [
        'aaggtaaa',
        'acggtaaa',
        'atggtaaa',
        'tttaccat',
        'tttaccgt',
        'tttacctt'
    ]
);

my $rx = '('. join('|', @seq) . ')';

{
    use re::engine::RE2;

    while ($content =~ /$rx/igo) {
        $seq{$1}++;
    }
}

for my $m (@seq) {
    my $i = 0;
    $i += $seq{$_} for @{$map{$m}};
    print "$m $i\n";
}

$content =~ s/B/(c|g|t)/g;
$content =~ s/D/(a|g|t)/g;
$content =~ s/H/(a|c|t)/g;
$content =~ s/K/(g|t)/g;
$content =~ s/M/(a|c)/g;
$content =~ s/N/(a|c|g|t)/g;
$content =~ s/R/(a|g)/g;
$content =~ s/S/(c|g)/g;
$content =~ s/V/(a|c|g)/g;
$content =~ s/W/(a|t)/g;
$content =~ s/Y/(c|t)/g;

printf "\n%d\n%d\n%d\n", $l_file, $l_code, length $content;

