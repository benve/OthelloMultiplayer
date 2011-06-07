#!/bin/bash

CP=target/classes

MAXP=3

xterm -e "java -cp $CP com.github.benve.othellomultiplayer.gMain 1234 $MAXP 1 "&

sleep 10

for port in `seq 1235 1236`;
do
    xterm -e "java -cp $CP com.github.benve.othellomultiplayer.gMain $port $MAXP 0 " &
done