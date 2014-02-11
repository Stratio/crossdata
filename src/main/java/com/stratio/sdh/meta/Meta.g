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
    import com.stratio.sdh.meta.statements.SetOptionsStatement;
    import com.stratio.sdh.meta.statements.StopProcessStatement;
    import com.stratio.sdh.meta.statements.DropTriggerStatement;
    import com.stratio.sdh.meta.statements.CreateTriggerStatement;
    import com.stratio.sdh.meta.statements.TruncateStatement;
    import com.stratio.sdh.meta.statements.UseStatement;
    import com.stratio.sdh.meta.statements.AlterTableStatement;
    import com.stratio.sdh.meta.statements.CreateTableStatement;
    import com.stratio.sdh.meta.structures.Consistency;
    import com.stratio.sdh.meta.structures.ConstantProperty;
    import com.stratio.sdh.meta.structures.IdentifierProperty;
    import com.stratio.sdh.meta.structures.MapLiteralProperty;
    import com.stratio.sdh.meta.structures.ValueProperty;
    import com.stratio.sdh.meta.structures.FloatProperty;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.LinkedHashMap;
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
fragment EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

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
T_STOP: S T O P;
T_PROCESS: P R O C E S S;
//cambiado por Antonio 6-2-2014
T_TRIGGER: T R I G G E R;
T_ON: O N;
//Cambiado por Antonio 7-2-2014
T_USING: U S I N G;
T_TYPE: T Y P E;
T_ADD: A D D;
//Cambiado por Antonio 10-02-2014
T_PRIMARY: P R I M A R Y;
T_KEY: K E Y;


T_SEMICOLON: ';';
T_EQUAL: '=';
T_POINT: '.';
T_START_SBRACKET: '{';
T_END_SBRACKET: '}';
T_COLON: ':';
T_COMMA: ',';

fragment LETTER: ('A'..'Z' | 'a'..'z');
fragment DIGIT: '0'..'9';

T_CONSTANT: (DIGIT)+;

T_IDENT: LETTER (LETTER | DIGIT | '_')*;

T_TERM: (LETTER | DIGIT | '_' | '.')+;
T_FLOAT:   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
     |   '.' ('0'..'9')+ EXPONENT?
     |   ('0'..'9')+ EXPONENT
     ;


//STATEMENTS

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

createTableStatement returns [CreateTableStatement crtast]
@init{
    LinkedHashMap<String, String> columns = new LinkedHashMap<>();
    List<String>   primaryKey = new ArrayList<String>();
    List<String> clusterKey = new ArrayList<String>();
    LinkedHashMap<String, ValueProperty> propierties = new LinkedHashMap<>();
    int Type_Primary_Key= 0;
    int columnNumberPK= 0;
    int columnNumberPK_inter= 0;
    boolean ifNotExists_2 = false;
    boolean withClusterKey = false;
    boolean withPropierties = false;

    }:
    

    T_CREATE
    T_TABLE
    (T_IF T_NOT T_EXISTS {ifNotExists_2 = true;})? 
    name_table=T_IDENT
    '(' (
            
                ident_column1=T_IDENT type1=T_IDENT (T_PRIMARY T_KEY)? {columns.put($ident_column1.text,$type1.text); Type_Primary_Key=1;}
                (   
                    ( ',' ident_columN=T_IDENT typeN=T_IDENT (T_PRIMARY T_KEY {Type_Primary_Key=2;columnNumberPK=columnNumberPK_inter +1;})? {columns.put($ident_columN.text,$typeN.text);columnNumberPK_inter+=1;})
                    |(  
                        ',' T_PRIMARY T_KEY '('
                        (
                            (   primaryK=T_IDENT {primaryKey.add($primaryK.text);Type_Primary_Key=3;}
                           
                                (','partitionKN=T_IDENT {primaryKey.add($partitionKN.text);})*
                            )
                            |(
                                '(' partitionK=T_IDENT {primaryKey.add($partitionK.text);Type_Primary_Key=4;}
                                    (','partitionKN=T_IDENT {primaryKey.add($partitionKN.text);})*
                                ')' 
                                (',' clusterKN=T_IDENT {clusterKey.add($clusterKN.text);withClusterKey=true;})*

                            )
                        )
                       ')' 
                   )
                )* 
         )     
        
    ')' T_WITH?
    ( identProp1=T_IDENT T_EQUAL valueProp1=getValueProperty {propierties.put($identProp1.text, valueProp1);withPropierties=true;}
            (T_AND identPropN=T_IDENT T_EQUAL valuePropN=getValueProperty {propierties.put($identPropN.text, valuePropN);withPropierties=true;} )*)?
            
     {$crtast = new CreateTableStatement($name_table.text,columns,primaryKey,clusterKey,propierties,Type_Primary_Key,ifNotExists_2,withClusterKey,columnNumberPK,withPropierties);  } ;        

        
alterTableStatement returns [AlterTableStatement altast]
@init{
        LinkedHashMap<String, ValueProperty> option = new LinkedHashMap<>();
        int prop= 0;
    }:

    T_ALTER
    T_TABLE
    name_table=T_IDENT
    (T_ALTER column=T_IDENT T_TYPE type=T_IDENT {prop=1;}
        |T_ADD column=T_IDENT type=T_IDENT {prop=2;}
        |T_DROP column=T_IDENT {prop=3;}
        |T_WITH 
            identProp1=T_IDENT T_EQUAL valueProp1=getValueProperty {option.put($identProp1.text, valueProp1);}
            (T_AND identPropN=T_IDENT T_EQUAL valuePropN=getValueProperty {option.put($identPropN.text, valuePropN);} )*
            {prop=4;}
    )
    {$altast = new AlterTableStatement($name_table.text,$column.text,$type.text,option,prop);  }
    ;

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
//cambiado por Antonio 7-2-2014
    st_crta= createTableStatement { $st = st_crta;}
    | st_alta= alterTableStatement { $st = st_alta;}
    | st_crtr= createTriggerStatement { $st = st_crtr; }
    | st_drtr= dropTriggerStatement { $st = st_drtr; }
    | st_stpr = stopProcessStatement { $st = st_stpr; }
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
    | mapliteral=getMapLiteral {$value = new MapLiteralProperty(mapliteral);}
    | number=getFloat {$value = new FloatProperty(Float.parseFloat(number));}
    ;


getFloat returns [String floating]:
    termToken=T_TERM {$floating=$termToken.text;}
    |
    floatToken = T_FLOAT {$floating=$floatToken.text;} 
    ;


WS: (' ' | '\t' | '\n' | '\r')+ { 
        $channel = HIDDEN; 
    };
