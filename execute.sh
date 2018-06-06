#!/bin/bash

gradle build

if [ $? -eq 0 ]
then
	gradle run --console=plain
fi
