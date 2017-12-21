#!/bin/bash

source /etc/profile

artifactId="dep-transfer"
version="0.0.1"

echo "Begin to deploy $artifactId..."

echo "Kill the old process:"
A=`ps -ef|grep $artifactId-$version|grep -v grep|awk '{print $2}'`
if [ -n "$A" ];then
  echo $A
  kill -9 $A
else
  echo "no process!"
fi

nohup java -jar $artifactId-$version.jar &

echo "$artifactId-$version is working!"
