#!/bin/bash

out=`(./Lab3)`
echo $out
if [[ $out == *"!!!FAILURES!!!"* ]]; then
  exit 1;
fi
