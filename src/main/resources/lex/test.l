%{
    /*definition of manifest constants*/
    WS, RESERVED, COLON, IF, ELSE, ID, NUMBER, RELOP
%}

/*regular definition*/
delim  |\t|\n
ws {delim}·{delim}*
letter a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z
digit 0|1|2|3|4|5|6|7|8|9
id {letter}·({letter}|{digit})*
number {digit}·{digit}*·(|.·{digit}*)|-·{digit}·{digit}*·(|.·{digit}*)


%%

{ws} {return WS;}
i·n·t|v·o·i·d|d·o·u·b·l·e|c·h·a·r|r·e·t·u·r·n|e·n·d {return RESERVED;}
: {return COLON;}
i·f {return IF;}
e·l·s·e {return ELSE;}
{id} {installID(); return ID;}
{number} {installNum(); return NUMBER;}
< {return RELOP;}
> {return RELOP;}
=·= {return RELOP;}
!·= {return RELOP;}

%%

static int installID(){return -1;}

static int installNum(){return -1;}