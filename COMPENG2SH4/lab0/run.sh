#!/bin/bash

out=`(./Lab0)`
echo $out
if [[ $out == *"!!!FAILURES!!!"* ]]; then
  exit 1;
fi


