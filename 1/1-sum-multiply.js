const readline = require('readline')
	.createInterface({
		input: process.stdin,
		output: process.stdout,
		terminal: true,
	});

const lines = [];

readline.on('line', function (line) {
    lines.push(line);
});

readline.on('close', function (line) {
    // console.log('All done:');
    console.log('Lines: ', lines);
	main(lines)
});

function findMatch(numbers)
{
	for (let i = 0; i < numbers.length; i++) 
	{
		const n1 = numbers[i];
		for (let j = i; j < numbers.length; j++) 
		{
			const n2 = numbers[j];
			for (let k = j; k < numbers.length; k++)
			{
				const n3 = numbers[k]
				if (n1 + n2 + n3 === 2020)
				{
					return n1 * n2 * n3;
				}
			}
		}
	}
}

function main(lines)
{
	console.log('main');
	const numbers = lines.map(l => parseInt(l));
	console.log('numbers', numbers);
	console.log(findMatch(numbers));
}

