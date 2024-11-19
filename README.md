# Password Generator

Just a small java project for creating passwords of any length you request, only limit is what your computer will put up with while generating a password sequence.

Password by default will contain characters between [a-z][A-Z][0-9].
There is also support for select special characters, in particular: `~!@#$%^&*+-=_:.,\\/\[\]\(\)\{\}

For the special characters, none are enabled by default, they each are enabled via glorious JButtons.
Generator works fine for riduculously long sequences that probably would not fit within a lot of service restrictions, don't think anyone will need a 1,000,000 character password but the program spits those out fine, mileage may vary between devices running it.

Nothing more fancy than that beyond a copy button to copy the generated password to the clipboard, should that be desired.
