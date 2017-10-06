#!/bin/bash
for j in 4 8 16 32 64 128 256 512 1024 2048
do
  echo "Iteracao com $j por $j" > time/concorrente/concorrente$j.txt
  echo "Iteracao com $j por $j"
  for t in 1 2 4 8 16 32 64
  do
    echo "Número de threads $t" >> time/concorrente/concorrente$j.txt
    echo "Número de threads $t"
    for i in {1..20}
    do
      nice -n 0 ./multimat_concorrente $j $t  >> time/concorrente/concorrente$j.txt
      wait
    done
  done
done
