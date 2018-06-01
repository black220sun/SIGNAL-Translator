# SIGNAL-Translator
Lexer and parser for SIGNAL programming language (will work for any LL(1) grammar).  
Implemented some code optimizations. (Only for SIGNAL).  
  
## Project structure
_`grammar_test`_ - grammar used for unit tests.  
_`grammar.gr`_ - SIGNAL grammar file (reworked).  
_`res`_ - directory for test files. `*.txt` files use grammar from `grammar_test` file, 
`*.sig` files use grammar from `grammar.gr` file.  
_`src`_ - source files directory.  
_`src/test/kotlin/com/blacksun`_ - containes testcases. `GrammarTest.kt` - general tests for grammar generation, 
`SignalGrammarTest.kt` - to run all `*.sig` files from `res` directory and ensure that there is no errors, 
`OptimizerTest.kt` - tests for SIGNAL code optimizations.  
_`src/main/kotlin/com/blacksun`_ - main source directory.  
- _`gui`_ - containes GUI classes.  
- _`settings`_ - containes classes for program's state save/load and multi-language GUI support.  
- _`optimizer`_ - containes implementation for SIGNAL code optimizations. 
All oplimizations should implement `Optimization` interface and be names as `Optimize*` (for easy importing from GUI).  
- _`utils`_ - contains utility classes for tokens, nodes, pattern matching and grammar rules implementation.  
- _`GrammarGen.kt`_ - processes grammar, implements parser part.  
- _`Lexer.kt`_ - implements lexer part.  
- _`Main.kt`_ - starts GUI for program.  
  
## Grammar rules structure:
- Each rule ends with `;` character.  
- Empty rule alternative should be the last in rule. 
- `|` character separates rule alternatives.  
-  `<name>` rules are syntax rules.  
- `NAME` rules are "keywords" rules (expects token with the same name).  
- `1`..`255` rules are code-character rules (expects character with the same ASCII code).  
- `'A'` rules are character rules (expects character with the same name).  
- Rules starting with `skip` are whitespace rules (characters are delimiters but will not be present in any token).  
- Rules starting with `hide` marks tokens that start comments.  
- Rules starting with `show` marks tokens that end comments. (Currently supports only one token per `hide`/`show` rules).  
- Rules starting with `part` marks fragment rules (used to collect characters into tokens).  
- Rules starting with `lexer` marks lexer rules (creates token from collected characters).  
  
## Grammar changes
### Original grammar:  
```
<signal-program> --> <program>;
<program> -->
    PROGRAM <procedure-identifier> ; <block> . |
    PROCEDURE <procedure-identifier> <parameters-list> ; <block> ;;
<block> --> <declarations> BEGIN <statements-list> END;
<declarations> --> <label-declarations> <constant-declarations> <variable-declarations> <math-function-declarations> <procedure-declarations>;
<label-declarations> --> LABEL <unsigned-integer> <labels-list> ; | <empty>;
<labels-list> --> , <unsigned-integer> <labels-list> | <empty>;
<constant-declarations> --> CONST <constant-declarations-list> | <empty>;
<constant-declarations-list> --> <constant-declaration> <constant-declarations-list> | <empty>;
<constant-declaration> --> <constant-identifier> = <constant> ;;
<constant> --> <complex-constant> | <unsigned-constant> | - <unsigned-constant>;
<variable-declarations> --> VAR <declarations-list> | <empty>;
<declarations-list> --> <declaration> <declarations-list> | <empty>;
<declaration> --> <variable-identifier> <identifiers-list> : <attribute> <attributes-list> ;;
<identifiers-list> --> , <variable-identifier> <identifiers-list> | <empty>;
<attributes-list> --> <attribute> <attributes-list> | <empty>;
<attribute> --> SIGNAL | COMPLEX | INTEGER | FLOAT | BLOCKFLOAT | EXT | [ <range> <ranges-list> ];
<ranges-list> --> , <range> <ranges-list> | <empty>;
<range> --> <unsigned-integer> .. <unsigned-integer>;
<math-function-declarations> --> DEFFUNC <function-list> | <empty>;
<function-list> --> <function> <function-list> | <empty>;
<function> --> <function-identifier> = <expression> <function-characteristic> ;;
<function-characteristic> --> \ <unsigned-integer> , <unsigned-integer>;
<procedure-declarations> --> <procedure> <procedure-declarations> | <empty>;
<procedure> --> PROCEDURE <procedure-identifier> <parameters-list> ;;
<parameters-list> --> ( <declarations-list> ) | <empty>;
<statements-list> --> <statement> <statements-list> | <empty>;
<statement> -->
    <unsigned-integer> : <statement> |
    <variable> := <expression> ; |
    <procedure-identifier> <actual-arguments> ; |
    <condition-statement> ENDIF ; |
    WHILE <conditional-expression> DO <statements-list> ENDWHILE ; |
    LOOP <statements-list> ENDLOOP ; |
    FOR <variable-identifier> := <loop-declaration> ENDFOR ; |
    CASE <expression> OF <alternatives-list> ENDCASE ; |
    GOTO <unsigned-integer> ; |
    LINK <variable-identifier> , <unsigned-integer> ; |
    IN <unsigned-integer>; |
    OUT <unsigned-integer>; |
    RETURN ; |
    ; |
    ($ <assembly-insert-file-identifier> $);
<condition-statement> --> <incomplete-condition-statement> <alternative-part>;
<incomplete-condition-statement> --> IF <conditional-expression> THEN <statements-list>;
<alternative-part> --> ELSE <statements-list> | <empty>;
<loop-declaration> --> <expression> TO <expression> DO <statements-list>;
<actual-arguments> --> ( <expression> <actual-arguments-list> ) | <empty>;
<actual-arguments-list> --> ,<expression> <actual-arguments-list> | <empty>;
<alternatives-list> --> <alternative> <alternatives-list> | <empty>;
<alternative> --> <expression> : / <statements-list> \;
<conditional-expression> --> <logical-summand> <logical>;
<logical> --> OR <logical-summand> <logical> | <empty>;
<logical-summand> --> <logical-multiplier> <logical-multipliers-list>;
<logical-multipliers-list> --> AND <logical-multiplier> <logical-multipliers-list> | <empty>;
<logical-multiplier> -->
    <expression> <comparison-operator> <expression> |
    [ <conditional-expression> ] |
    NOT <logical-multiplier>;
<comparison-operator> --> < | <= | = | <> | >= | >;
<expression> --> <summand> <summands-list> | - <summand> <summands-list>;
<summands-list> --> <add-instruction> <summand> <summands-list> | <empty>;
<add-instruction> --> + | - | !;
<summand> --> <multiplier> <multipliers-list>;
<multipliers-list> --> <multiplication-instruction> <multiplier> <multipliers-list> | <empty>;
<multiplication-instruction> --> * | / | & | MOD;
<multiplier> -->
    <unsigned-constant> |
    <complex-constant> |
    <constant-identifier> |
    <variable> |
    <function-identifier> |
    <builtin-function-identifier> <actual-arguments> |
    ( <expression> ) |
    - <multiplier> |
    ^ <multiplier>;
<variable> --> <variable-identifier><dimension> | <complex-variable>;
<complex-variable> --> " <complex-number> ";
<dimension> --> [ <expression> <expressions-list> ] | <empty>;
<expressions-list> --> , <expression><expressions-list> | <empty>;
<empty> --> ;

lexer <complex-constant> --> 39 <complex-number> 39;
lexer <unsigned-constant> --> <unsigned-number> | <unsigned-integer>;
part <complex-number> --> <left-part> <right-part>;
part <left-part> --> <expression> | <empty>;
part <right-part> --> , <expression> | $EXP ( <expression> ) | ;
<constant-identifier> --> <identifier>;
<variable-identifier> --> <identifier>;
<procedure-identifier> --> <identifier>;
<function-identifier> --> <identifier>;
<builtin-function-identifier> --> <identifier>;
<assembly-insert-file-identifier> --> <identifier>;
part <unsigned-number> --> <integer-part> <fractional-part>;
part <integer-part> --> <unsigned-integer> | ;
part <fractional-part> --> # <sign> <unsigned-integer>;
part <sign> --> + | - | ;
lexer <delimiter> --> ';' | ',' | '=' | '+' | '-' | '[' | ']' |
    '\' | '!' | '*' | '/' | '&' | '^' | '"' | '#' | ')' | 39;
lexer <dot-dot?> --> '.' <dot?>;
part <dot?> --> '.' | ;
lexer <colon-equal?> --> ':' <equal?>;
part <equal?> --> '=' | ;
lexer <open-any?> --> '(' <any?>;
part <any?> --> '$' | '*' | ;
lexer <less-any2?> --> '<' <any2?>;
part <any2?> --> '=' | '>' | ;
lexer <greater-any3?> --> '>' <any3?>;
part <any3?> --> '=' | ;
lexer <exp> --> '$' 'E' 'X' 'P';
lexer <identifier> --> <letter> <string>;
lexer <unsigned-integer> --> <digit> <digit-string>;
part <digit-string> --> <digit> <digit-string> | ;
part <letter> --> 'A'..'Z';
part <string> --> <letter> <string> | <digit> <string> | ;
part <digit> --> '0'..'9';
skip --> 9 | 10 | 11 | 13 | 32;
hide --> (*;
show --> *);
```
### Changes:
- To make grammar for fractional numbers LL(1):  
```
<unsigned-constant> --> <unsigned-integer> <fractional> | <fractional-part>;
<fractional> --> <fractional-part> | <empty>
lexer <fractional-part> --> '#' <sign> <u-int>;
part <sign> --> '+' | '-' | ;
```
- To make grammar for complex numbers LL(1):  
```
<complex-variable> --> " <complex-number> ";
<complex-constant> --> <quote> <complex-number> <quote>;
<complex-number> --> <left-part> <right-part>;
<left-part> --> <expression> | ;
<right-part> --> , <expression> | <exp> ( <expression> ) | ;
lexer <quote> --> 39;
```
- To make grammar for variable assignment and procedure calls LL(1) 
(leads to changes in language: procedures without arguments are now called as `proc();`, not `proc;`):  
```
<statement> -->
    <unsigned-integer> : <statement> |
    <assign-or-procedure> ; |
    <condition-statement> ENDIF ; |
    WHILE <conditional-expression> DO <statements-list> ENDWHILE ; |
    LOOP <statements-list> ENDLOOP ; |
    FOR <variable-identifier> := <loop-declaration> ENDFOR ; |
    CASE <expression> OF <alternatives- list> ENDCASE ; |
    GOTO <unsigned-integer> ; |
    LINK <variable-identifier> , <unsigned-integer> ; |
    IN <unsigned-integer>; |
    OUT <unsigned-integer>; |
    RETURN ; |
    ; |
($ <assembly-insert-file-identifier> $);
<actual-arguments> --> ( <actual-arguments-list> );
<actual-arguments-list> --> <expression> <expressions-list> | <empty>;
<assign-or-procedure> --> <identifier> <aop-rest> | <complex-variable> := <expression>;
<aop-rest> --> <actual-arguments> | <dimension> := <expression>;
<variable> --> <variable-identifier> <dimension> | <complex-variable>;
```
- To parse empty file without errors:  
```
<signal-program> --> <program> |
    <empty>;
```

## Code optimizations
Class `Optimizer` is used for syntax tree rewrite and code optimizations. It contains set of used optimizations.  
Currently supported optimizations:  
- `OptimizeEmpty` - removes all `;` statements. Should preceed all `OptimizeEmpty*` optimizations.    
- `OptimizeEmptyWhile` - removes all `WHILE <conditional-expression> DO ENDWHILE;` statements.  
- `OptimizeEmptyLoop` -removes all `LOOP ENDLOOP;` statements.  
- `OptimizeEmptyAlternative` - removes all `<expression>: /\` case alternatives. Should preceed `OptimizeEmptyCase`.  
- `OptimizeEmptyCase` - removes all `CASE <expression> OF ENDCASE;` statements.  
- `OptimizeEmptyElse` - removes all empty `ELSE` branches for `IF`-statements. Should preceed `OptimizeEmptyThen`.  
- `OptimizeEmptyThen` - reverts condition for `IF` statement with empty `THEN` part, then removes empty branch. Should preceed `OptimizeEmptyIf`.  
- `OptimizeEmptyIf` - removes all `IF <conditional-expression> THEN ENDIF;` statements.  
