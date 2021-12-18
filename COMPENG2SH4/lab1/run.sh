#!/bin/bash
out=`(./Lab1)`
echo $out
if [[ $out == *"!!!FAILURES!!!"* ]]; then
  exit 1;
fi
