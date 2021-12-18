
classdef SimpleFunctions

  properties
    n = 0;
  end

  methods

    %% Class constructor. Set n if the time vector is given when the
    %% object is created.
    function obj = SimpleFunctions(time_vector)
      if nargin == 1
	obj.n = time_vector;
      end
    end

    %% Discrete-time delta (impulse) function
    function d = delta(obj, n)
      if nargin == 1
	n = obj.n;
      end
      d = cast (n == 0, class (n));
    end

    %% Discrete-time unit step function (= 1 at n = 0)
    function f = unitstep(obj, n)
      if nargin == 1
	n = obj.n;
      end
      f = cast(n >= 0, class(n));
    end
    
    %% Unit ramp function.
    function f = unitramp(obj, n)
      if nargin == 1
	n = obj.n;
      end
      f = n .* obj.unitstep(n);
    end

    %% Discrete-time unit pulse function. q units on either side of 0.
    function f = unitpulse(obj, q, n)
      if nargin == 2
	n = obj.n;
      end
      f = obj.unitstep(n + q) - obj.unitstep(n - q - 1);
    end
    
    %% Gaussian function.
    function g = gaussian(obj, mean, stdev, n)
      if nargin == 3
	n = obj.n;
      end
      g = 1/stdev/sqrt(2*pi)*exp(-(n-mean).^2/(2*stdev^2));
    end

    %% Sinc function (sin(pi x)/pi x)
    function s = sinc(obj, n)
      if nargin == 1
	n = obj.n;
      end
      s = sin(pi*n)./(pi*n);
    end

  end
      
end

