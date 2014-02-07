/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

grammar Meta;

options {
    language = Java;
}
 
@header {
    package com.stratio.sdh.meta.generated;    
    import com.stratio.sdh.meta.statements.Statement;
    import com.stratio.sdh.meta.statements.AlterKeyspaceStatement;
    import com.stratio.sdh.meta.statements.CreateKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropTableStatement;
    import com.stratio.sdh.meta.statements.ExplainPlanStatement;
    import com.stratio.sdh.meta.statements.InsertIntoStatement;
    import com.stratio.sdh.meta.statements.SelectStatement;
    import com.stratio.sdh.meta.statements.SetOptionsStatement;
    import com.stratio.sdh.meta.statements.StopProcessStatement;
    import com.stratio.sdh.meta.statements.DropTriggerStatement;
    import com.stratio.sdh.meta.statements.CreateTriggerStatement;
    import com.stratio.sdh.meta.statements.TruncateStatement;
    import com.stratio.sdh.meta.statements.UseStatement;
    import com.stratio.sdh.meta.structures.CollectionLiteral;
    import com.stratio.sdh.meta.structures.Consistency;
    import com.stratio.sdh.meta.structures.ConstantProperty;
    import com.stratio.sdh.meta.structures.IdentifierProperty;
    import com.stratio.sdh.meta.structures.MapLiteralProperty;
    import com.stratio.sdh.meta.structures.Option;
    import com.stratio.sdh.meta.structures.Term;
    import com.stratio.sdh.meta.structures.ValueCell;
    import com.stratio.sdh.meta.structures.ValueProperty;
    import java.util.HashMap;
    import java.util.Map;
}

@members {
    public void displayRecognitionError(String[] tokenNames, RecognitionException e){
        System.err.print("Error recognized: ");
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        System.err.print(hdr+": ");
        System.err.println(msg);
    }
}

@lexer::header {
    package com.stratio.sdh.meta.generated;
}

@lexer::members {
    
}

// Case-insensitive alpha characters
fragment A: ('a'|'A');
fragment B: ('b'|'B');
fragment C: ('c'|'C');
fragment D: ('d'|'D');
fragment E: ('e'|'E');
fragment F: ('f'|'F');
fragment G: ('g'|'G');
fragment H: ('h'|'H');
fragment I: ('i'|'I');
fragment J: ('j'|'J');
fragment K: ('k'|'K');
fragment L: ('l'|'L');
fragment M: ('m'|'M');
fragment N: ('n'|'N');
fragment O: ('o'|'O');
fragment P: ('p'|'P');
fragment Q: ('q'|'Q');
fragment R: ('r'|'R');
fragment S: ('s'|'S');
fragment T: ('t'|'T');
fragment U: ('u'|'U');
fragment V: ('v'|'V');
fragment W: ('w'|'W');
fragment X: ('x'|'X');
fragment Y: ('y'|'Y');
fragment Z: ('z'|'Z');

// Case-insensitive keywords
T_TRUNCATE: T R U N C A T E;
T_CREATE: C R E A T E;
T_ALTER: A L T E R;
T_KEYSPACE: K E Y S P A C E;
T_NOT: N O T;
T_WITH: W I T H;
T_DROP: D R O P;
T_TABLE: T A B L E;
T_IF: I F;
T_EXISTS: E X I S T S;
T_AND: A N D;
T_USE: U S E;
T_SET: S E T;
T_OPTIONS: O P T I O N S;
T_ANALYTICS: A N A L Y T I C S;
T_TRUE: T R U E;
T_FALSE: F A L S E;
T_CONSISTENCY: C O N S I S T E N C Y;
T_ALL: A L L;
T_ANY: A N Y;
T_QUORUM: Q U O R U M;
T_ONE: O N E;
T_TWO: T W O;
T_THREE: T H R E E;
T_EACH_QUORUM: E A C H '_' Q U O R U M;
T_LOCAL_ONE: L O C A L '_' O N E;
T_LOCAL_QUORUM: L O C A L '_' Q U O R U M;
T_EXPLAIN: E X P L A I N;
T_PLAN: P L A N;
T_FOR: F O R;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
T_STOP: S T O P;
T_PROCESS: P R O C E S S;
//cambiado por Antonio 6-2-2014
T_TRIGGER: T R I G G E R;
T_ON: O N;
//Cambiado por Antonio 7-2-2014
T_USING: U S I N G;
=======
T_INSERT: I N S E R T;
T_INTO: I N T O;
T_COMPACT: C O M P A C T;
T_STORAGE: S T O R A G E;
T_CLUSTERING: C L U S T E R I N G;
T_ORDER: O R D E R;
T_SELECT: S E L E C T;
T_USING: U S I N G;
T_VALUES: V A L U E S;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

T_SEMICOLON: ';';
T_EQUAL: '=';
T_POINT: '.';
T_START_SBRACKET: '{';
T_END_SBRACKET: '}';
T_COLON: ':';
T_COMMA: ',';
T_START_BRACKET: '(';
T_END_BRACKET: ')';

fragment LETTER: ('A'..'Z' | 'a'..'z');
fragment DIGIT: '0'..'9';

T_CONSTANT: (DIGIT)+;

T_IDENT: LETTER (LETTER | DIGIT | '_')*;

T_TERM: (LETTER | DIGIT | '_' | '.')+;


//STATEMENTS

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
stopProcessStatement returns [StopProcessStatement stprst]:
    T_STOP T_PROCESS ident=T_IDENT { $stprst = new StopProcessStatement($ident.text); }
    ;

//Metodo drop  Trigger: Antonio 6-2-2014
dropTriggerStatement returns [DropTriggerStatement drtrst]:
    T_DROP 
    T_TRIGGER ident=T_IDENT 
    T_ON 
    ident2=T_IDENT  
    {$drtrst = new DropTriggerStatement($ident.text,$ident2.text);}
    ;
//Metodo create  Trigger: Antonio 7-2-2014
createTriggerStatement returns [CreateTriggerStatement crtrst]:
    T_CREATE 
    T_TRIGGER trigger_name=T_IDENT 
    T_ON 
    table_name=T_IDENT
    T_USING class_name=T_IDENT    
    {$crtrst = new CreateTriggerStatement($trigger_name.text,$table_name.text,$class_name.text);}
    ;
=======
selectStatement returns [SelectStatement slctst]:
    T_SELECT {$slctst = new SelectStatement();}
    ;

insertIntoStatement returns [InsertIntoStatement nsntst]
    @init{
        List<String> ids = new ArrayList<>();
        boolean ifNotExists = false;
        int typeValues = InsertIntoStatement.TYPE_VALUES_CLAUSE;
        List<ValueCell> cellValues = new ArrayList<>();
        boolean optsInc = false;
        List<Option> options = new ArrayList<>();
    }:
    T_INSERT 
    T_INTO 
    tableName=getTableID
    T_START_BRACKET 
    ident1=T_IDENT {ids.add($ident1.text);} 
    (T_COMMA identN=T_IDENT {ids.add($identN.text);})* 
    T_END_BRACKET
    ( 
        selectStmnt=selectStatement {typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;}
        | 
        T_VALUES
        T_START_BRACKET 
            term1=getTermOrLiteral {cellValues.add(term1);}
            (T_COMMA termN=getTermOrLiteral {cellValues.add(termN);})*
        T_END_BRACKET
    )
    (T_IF T_NOT T_EXISTS {ifNotExists=true;} )?
    (
        T_USING {optsInc=true;} 
        opt1=getOption {
            options.add(opt1);
        }
        (T_AND optN=getOption {options.add(optN);})*
    )?
    {
        if(typeValues==InsertIntoStatement.TYPE_SELECT_CLAUSE)
            if(optsInc)
                $nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists, options);
            else
                $nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists);
        else
            if(optsInc)
                $nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists, options);
            else
                $nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists);
                
    }
    ;

>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
explainPlanStatement returns [ExplainPlanStatement xpplst]:
    T_EXPLAIN T_PLAN T_FOR parsedStmnt=metaStatement
    {$xpplst = new ExplainPlanStatement(parsedStmnt);}
    ;

setOptionsStatement returns [SetOptionsStatement stptst]
    @init{
        ArrayList<Boolean> checks = new ArrayList<>();
        checks.add(false);
        checks.add(false);
        boolean analytics = false;
        Consistency cnstc=Consistency.ALL;
    }:
    T_SET T_OPTIONS (
        T_ANALYTICS T_EQUAL (T_TRUE{analytics=true;}|T_FALSE{analytics=false;}) {checks.set(0, true);}
        (T_AND T_CONSISTENCY T_EQUAL
            (T_ALL {cnstc=Consistency.ALL;} 
            | T_ANY {cnstc=Consistency.ANY;}
            | T_QUORUM {cnstc=Consistency.QUORUM;}
            | T_ONE {cnstc=Consistency.ONE;}
            | T_TWO {cnstc=Consistency.TWO;}
            | T_THREE {cnstc=Consistency.THREE;}
            | T_EACH_QUORUM {cnstc=Consistency.EACH_QUORUM;}
            | T_LOCAL_ONE {cnstc=Consistency.LOCAL_ONE;}
            | T_LOCAL_QUORUM {cnstc=Consistency.LOCAL_QUORUM;})
            {checks.set(1, true);}
        )?
        { $stptst = new SetOptionsStatement(analytics, cnstc, checks);}
        | T_CONSISTENCY T_EQUAL 
            (T_ALL {cnstc=Consistency.ALL;} 
            | T_ANY {cnstc=Consistency.ANY;}
            | T_QUORUM {cnstc=Consistency.QUORUM;}
            | T_ONE {cnstc=Consistency.ONE;}
            | T_TWO {cnstc=Consistency.TWO;}
            | T_THREE {cnstc=Consistency.THREE;}
            | T_EACH_QUORUM {cnstc=Consistency.EACH_QUORUM;}
            | T_LOCAL_ONE {cnstc=Consistency.LOCAL_ONE;}
            | T_LOCAL_QUORUM {cnstc=Consistency.LOCAL_QUORUM;})
            {checks.set(1, true);}
        (T_AND T_ANALYTICS T_EQUAL (T_TRUE{analytics=true;}|T_FALSE{analytics=false;}) 
            {checks.set(0, true);})?
        { $stptst = new SetOptionsStatement(analytics, cnstc, checks);}
    )
    ;

useStatement returns [UseStatement usst]:
    T_USE
    iden=T_IDENT {$usst = new UseStatement($iden.text);}
    ;

dropKeyspaceStatement returns [DropKeyspaceStatement drksst]
    @init{
        boolean ifExists = false;
    }:
    T_DROP
    T_KEYSPACE
    (T_IF T_EXISTS {ifExists = true;})?
    iden=T_IDENT
    { $drksst = new DropKeyspaceStatement($iden.text, ifExists);}
    ;

alterKeyspaceStatement returns [AlterKeyspaceStatement alksst]
    @init{
        HashMap<String, ValueProperty> properties = new HashMap<>();
    }:
    T_ALTER
    T_KEYSPACE
    ident=T_IDENT
    T_WITH
    identProp1=T_IDENT T_EQUAL valueProp1=getValueProperty {properties.put($identProp1.text, valueProp1);}
    (T_AND identPropN=T_IDENT T_EQUAL valuePropN=getValueProperty {properties.put($identPropN.text, valuePropN);} )*
    { $alksst = new AlterKeyspaceStatement($ident.text, properties); }
    ;

createKeyspaceStatement returns [CreateKeyspaceStatement crksst]
    @init{
        boolean ifNotExists = false;
        HashMap<String, ValueProperty> properties = new HashMap<>();
    }:
    T_CREATE
    T_KEYSPACE
    (T_IF T_NOT T_EXISTS {ifNotExists = true;})?
    identKS=T_IDENT
    T_WITH    
    identProp1=T_IDENT T_EQUAL valueProp1=getValueProperty {properties.put($identProp1.text, valueProp1);}
    (T_AND identPropN=T_IDENT T_EQUAL valuePropN=getValueProperty {properties.put($identPropN.text, valuePropN);} )*
    { $crksst = new CreateKeyspaceStatement($identKS.text, ifNotExists, properties); }
    ;

dropTableStatement returns [DropTableStatement drtbst]
    @init{
        boolean ifExists = false;
    }:
    T_DROP
    T_TABLE
    (T_IF T_EXISTS { ifExists = true; })?
    ident=getTableID {
        $drtbst = new DropTableStatement(ident, ifExists);
    }
    ;

truncateStatement returns [TruncateStatement trst]: 
	T_TRUNCATE 
        ident=getTableID {
            $trst = new TruncateStatement(ident);
	}
	;

metaStatement returns [Statement st]:
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
//cambiado por Antonio 7-2-2014
    st_crtr= createTriggerStatement { $st = st_crtr; }
    |st_drtr= dropTriggerStatement { $st = st_drtr; }
    |st_stpr = stopProcessStatement { $st = st_stpr; }
=======
    st_slct = selectStatement { $st = st_slct;}
    | st_nsnt = insertIntoStatement { $st = st_nsnt;}
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
    | st_xppl = explainPlanStatement { $st = st_xppl;}
    | st_stpt = setOptionsStatement { $st = st_stpt; }
    | st_usks = useStatement { $st = st_usks; }
    | st_drks = dropKeyspaceStatement { $st = st_drks ;}
    | st_crks = createKeyspaceStatement { $st = st_crks; }
    | st_alks = alterKeyspaceStatement { $st = st_alks; }
    | st_tbdr = dropTableStatement { $st = st_tbdr; }
    | st_trst = truncateStatement { $st = st_trst; };    

query returns [Statement st]: 
	mtst=metaStatement (T_SEMICOLON)+ EOF {
		$st = mtst;
	};


//FUNCTIONS

getOption returns [Option opt]:
    T_COMPACT T_STORAGE {$opt=new Option(Option.OPTION_COMPACT);}
    | T_CLUSTERING T_ORDER {$opt=new Option(Option.OPTION_CLUSTERING);}
    | identProp=T_IDENT T_EQUAL valueProp=getValueProperty {$opt=new Option($identProp.text, valueProp);}
;

getList returns [List list]
    @init{
        list = new ArrayList<String>();
    }:
    term1=getTerm {list.add($term1.text);}
    (T_COMMA termN=getTerm {list.add($termN.text);})*
    ;

getTermOrLiteral returns [ValueCell vc]
    @init{
        CollectionLiteral cl = new CollectionLiteral();
    }:
    term=getTerm {$vc=new Term($term.text);}
    |
    T_START_SBRACKET
    (
        term1=getTerm {cl.addLiteral(new Term($term1.text));}
        (T_COMMA termN=getTerm {cl.addLiteral(new Term($termN.text));})*
    )?
    T_END_SBRACKET {$vc=cl;}
;

getTableID returns [String tableID]: 
    (ks=T_IDENT '.')? 
    ident=T_IDENT {$tableID = new String($ks.text==null?$ident.text:$ks.text+'.'+$ident.text);} ;

getTerm returns [String term]:
    ident=T_IDENT {$term = $ident.text;}
    | noIdent=T_TERM {$term = $noIdent.text;}
    ;

getMapLiteral returns [Map<String, String> mapTerms]
    @init{
        $mapTerms = new HashMap<>();
    }:
    T_START_SBRACKET 
    (leftTerm1=getTerm T_COLON rightTerm1=getTerm {$mapTerms.put($leftTerm1.text, $rightTerm1.text);}
    (T_COMMA leftTermN=getTerm T_COLON rightTermN=getTerm {$mapTerms.put($leftTermN.text, $rightTermN.text);})*)?
    T_END_SBRACKET
    ;

getValueProperty returns [ValueProperty value]:
    ident=T_IDENT {$value = new IdentifierProperty($ident.text);}
    | constant=T_CONSTANT {$value = new ConstantProperty(Integer.parseInt($constant.text));}
    | mapliteral=getMapLiteral {$value = new MapLiteralProperty(mapliteral);};


WS: (' ' | '\t' | '\n' | '\r')+ { 
        $channel = HIDDEN; 
    };
