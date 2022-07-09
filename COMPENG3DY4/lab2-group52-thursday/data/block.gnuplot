# example.gnuplut : configuration for plotting (change as needed)

reset                                   # reset
set size ratio 0.2                      # set relative size of plots
set xlabel 'Sample #'                   # set x-axis label for all plots
set grid xtics ytics                    # grid: enable both x and y lines
set grid lt 1 lc rgb '#cccccc' lw 1     # grid: thin gray lines
set multiplot layout 3,1 scale 1.0,1.0  # set three plots for this figure


# filter in / filter out
set ylabel 'Filter in/out'               # set y-axis label
set yrange [-1600:1600]                      # set y plot range
set xrange [0:1000]                       # set x plot range
plot '../data/block.dat' using 1:2 with lines lt 1 lw 2 lc rgb '#888888' notitle,\
     '../data/block.dat' using 1:3 with lines lt 1 lw 2 lc rgb '#DD00DD' notitle

# spectrum
set ylabel 'Spectrum'                    # set y-axis label
set xlabel 'Frequency bin'               # set x-axis label for this plot
set yrange [0:800]                         # set y plot range
set xrange [0:30000]                       # set x plot range (change range as you see fit)
plot '../data/block.dat' using 1:4 with lines lt 1 lw 3 lc rgb '#888888' notitle


# spectrum
set ylabel 'Spectrum'                    # set y-axis label
set xlabel 'Frequency bin'               # set x-axis label for this plot
set yrange [0:800]                         # set y plot range
set xrange [0:30000]                       # set x plot range (change range as you see fit)
plot '../data/block.dat' using 1:5 with lines lt 1 lw 3 lc rgb '#DD00DD' notitle

unset multiplot
