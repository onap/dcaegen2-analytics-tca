# JSON Path Filter

## Description

Filters incoming JSON based on given filter mappings.

The mappings in the plugin will be:

    +================================================+
    |   JSON Path Expression    |   Expected Values  |
    |---------------------------|--------------------|
    |$.employee.name.first      |    rajiv;manjesh   |
    |$.employee.name.last       |   singla;gowda     |
    |$.employee.email           |   johndoe@xyz.com  |
    +================================================+
    
The above filter mappings will inside json path $.employee.name.first and check if value matches rajiv or manjesh. 
Only if path does exist and matches any of the expected values rajiv or manjesh then first filter mapping will be true.

If multiple mappings are provided all mapping must be true for result to be matched.

If incoming json is null or empty then output result will be null as no matching is possible.

### Expression

The "root member object" for parsing any JSON is referred to as ```$```, regardless of
whether it's an array or an object. It also uses either dot notation or bracket notation for
defining the levels of parsing. For example: ```$.employee.name``` or ```$[employee][name]```.

#### Supported Operators

These operators are supported:

    +========================================================================+
    | Operator          | Description                                        |
    |-------------------|----------------------------------------------------|
    | $                 | The root element of the query                      |
    | *                 | Wildcard                                           |
    | ..                | Deep scan                                          |
    | .<name>           | Dot notation representing child                    |
    | [?(<expression>)] | Filter expression, should be boolean result always |
    +========================================================================+
