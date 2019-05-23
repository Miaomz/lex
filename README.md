# Lex

## Lex program
1. the part of manifest constants should only and must be in the beginning of a file
2. the comments could only be surrounded by "/\*" and "*/"
3. the %% line that separates the transform rules should contain nothing more

## Regular Expression
1. my lex supports {}, (), |, *, ·. These operators are NOT ordered by priority.
2. empty string denotes ε in the implementation
3. The tag and definition of regex must be separated by ONE blank. And this line should be immediately ended by a lineSeparator.