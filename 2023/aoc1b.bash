#/bin/bash
total=0
sed -r \
	-e 's/(one/1/g' \
	-e 's/two/2/g' \
	-e 's/three/3/g' \
	-e 's/four/4/g' \
	-e 's/five/5/g' \
	-e 's/six/6/g' \
	-e 's/seven/7/g' \
	-e 's/eight/8/g' \
	-e 's/nine/9/g' \
	-e 's/[^0-9]+//g'
exit
while read n; do ((total += n)); done < <(
	sed -r \
		-e 's/one/1/g' \
		-e 's/two/2/g' \
		-e 's/three/3/g' \
		-e 's/four/4/g' \
		-e 's/five/5/g' \
		-e 's/six/6/g' \
		-e 's/seven/7/g' \
		-e 's/eight/8/g' \
		-e 's/nine/9/g' \
		-e 's/[^0-9]+//g' | while read a; do echo "${a:0:1}${a: -1:1}"; done
)
echo $total
