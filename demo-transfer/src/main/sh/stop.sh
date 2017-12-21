#!/bin/bash

artifactId="dep-transfer"
version="0.0.1"
echo "Kill the old process:"
A=`ps -ef|grep $artifactId-$version|grep -v grep|awk '{print $2}'`
if [ -n "$A" ];then
  echo $A
  kill -9 $A
else
  echo "no process!"
fi

echo "$artifactId-$version stop!"
