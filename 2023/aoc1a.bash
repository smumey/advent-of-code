#/bin/bash
total=0
while read n; do ((total += n)); done < <(sed -r -e 's/[^0-9]+//g' | while read a; do echo "${a:0:1}${a: -1:1}"; done)
echo $total
