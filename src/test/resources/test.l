%{
    /*definition of manifest constants*/
    IF, ELSE, ID, NUMBER, RELOP, GE
%}

/*regular definition*/
delim  |\t|\n
ws {delim}·{delim}*
letter a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z
digit 0|1|2|3|4|5|6|7|8|9
id {letter}·({letter}|{digit})*
number {digit}·{digit}*·(|.·{digit}*)


%%

{ws} {/*no return*/}
i·f {return IF;}
e·l·s·e {return ELSE;}
{id} {return ID;}
{number} {return NUMBER;}
< {return RELOP;}
> {return RELOP;}
=·= {return RELOP;}
!·= {return RELOP;}

%%

int installID(){return -1;}

int installNum(){return -1;}