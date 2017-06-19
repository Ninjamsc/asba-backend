#!/usr/bin/env bash

rm -rf ../asba-backend-wars/

mkdir ../asba-backend-wars/

for file in `find . -type f -and -iname "*.war"`; do cp "$file" "../asba-backend-wars/"; done


