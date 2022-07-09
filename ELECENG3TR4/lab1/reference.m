h = @(s) 1/((s+1)*(s^2+s+1));
mag  = @(s) abs(h(s));

freq = 16518;
a = mag(10000/freq*1i)
b = mag(30000/freq*1i)
b/a