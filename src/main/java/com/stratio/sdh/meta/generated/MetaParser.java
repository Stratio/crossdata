// $ANTLR 3.5.1 Meta.g 2014-02-13 18:10:27

    package com.stratio.sdh.meta.generated;    
    import com.stratio.sdh.meta.statements.*;
    import com.stratio.sdh.meta.structures.*;
    import java.util.LinkedHashMap;
    import com.stratio.sdh.meta.structures.ValueAssignment;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Set;
    import java.util.HashSet;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "DIGIT", "E", 
		"EXPONENT", "F", "G", "H", "I", "J", "K", "L", "LETTER", "M", "N", "O", 
		"P", "POINT", "Q", "R", "S", "T", "T_ADD", "T_AGGREGATION", "T_ALL", "T_ALTER", 
		"T_ANALYTICS", "T_AND", "T_ANY", "T_AS", "T_ASC", "T_ASTERISK", "T_AVG", 
		"T_BETWEEN", "T_BY", "T_CLUSTERING", "T_COLON", "T_COMMA", "T_COMPACT", 
		"T_CONSISTENCY", "T_CONSTANT", "T_COUNT", "T_CREATE", "T_DELETE", "T_DESC", 
		"T_DISABLE", "T_DISTINCT", "T_DROP", "T_EACH_QUORUM", "T_END_BRACKET", 
		"T_END_PARENTHESIS", "T_END_SBRACKET", "T_EQUAL", "T_EXISTS", "T_EXPLAIN", 
		"T_FALSE", "T_FLOAT", "T_FOR", "T_FROM", "T_GET", "T_GROUP", "T_GT", "T_IDENT", 
		"T_IF", "T_IN", "T_INDEX", "T_INDEX_TYPE", "T_INNER", "T_INSERT", "T_INTERROGATION", 
		"T_INTO", "T_JOIN", "T_KEY", "T_KEYSPACE", "T_KS_AND_TN", "T_LAST", "T_LET", 
		"T_LIKE", "T_LIMIT", "T_LIST", "T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_LT", 
		"T_MAX", "T_MIN", "T_NOT", "T_NOT_EQUAL", "T_ON", "T_ONE", "T_OPTIONS", 
		"T_ORDER", "T_PATH", "T_PLAN", "T_PLUS", "T_PRIMARY", "T_PROCESS", "T_QUORUM", 
		"T_QUOTE", "T_REMOVE", "T_ROWS", "T_SELECT", "T_SEMICOLON", "T_SET", "T_START_BRACKET", 
		"T_START_PARENTHESIS", "T_START_SBRACKET", "T_STOP", "T_STORAGE", "T_SUBTRACT", 
		"T_TABLE", "T_TERM", "T_THREE", "T_TOKEN", "T_TRIGGER", "T_TRUE", "T_TRUNCATE", 
		"T_TWO", "T_TYPE", "T_UDF", "T_UPDATE", "T_USE", "T_USING", "T_VALUES", 
		"T_WHERE", "T_WINDOW", "T_WITH", "U", "V", "W", "WS", "X", "Y", "Z", "'1'", 
		"'D'", "'H'", "'M'", "'PROCESS'", "'S'", "'TRIGGER'", "'UDF'", "'d'", 
		"'h'", "'m'", "'s'"
	};
	public static final int EOF=-1;
	public static final int T__139=139;
	public static final int T__140=140;
	public static final int T__141=141;
	public static final int T__142=142;
	public static final int T__143=143;
	public static final int T__144=144;
	public static final int T__145=145;
	public static final int T__146=146;
	public static final int T__147=147;
	public static final int T__148=148;
	public static final int T__149=149;
	public static final int T__150=150;
	public static final int A=4;
	public static final int B=5;
	public static final int C=6;
	public static final int D=7;
	public static final int DIGIT=8;
	public static final int E=9;
	public static final int EXPONENT=10;
	public static final int F=11;
	public static final int G=12;
	public static final int H=13;
	public static final int I=14;
	public static final int J=15;
	public static final int K=16;
	public static final int L=17;
	public static final int LETTER=18;
	public static final int M=19;
	public static final int N=20;
	public static final int O=21;
	public static final int P=22;
	public static final int POINT=23;
	public static final int Q=24;
	public static final int R=25;
	public static final int S=26;
	public static final int T=27;
	public static final int T_ADD=28;
	public static final int T_AGGREGATION=29;
	public static final int T_ALL=30;
	public static final int T_ALTER=31;
	public static final int T_ANALYTICS=32;
	public static final int T_AND=33;
	public static final int T_ANY=34;
	public static final int T_AS=35;
	public static final int T_ASC=36;
	public static final int T_ASTERISK=37;
	public static final int T_AVG=38;
	public static final int T_BETWEEN=39;
	public static final int T_BY=40;
	public static final int T_CLUSTERING=41;
	public static final int T_COLON=42;
	public static final int T_COMMA=43;
	public static final int T_COMPACT=44;
	public static final int T_CONSISTENCY=45;
	public static final int T_CONSTANT=46;
	public static final int T_COUNT=47;
	public static final int T_CREATE=48;
	public static final int T_DELETE=49;
	public static final int T_DESC=50;
	public static final int T_DISABLE=51;
	public static final int T_DISTINCT=52;
	public static final int T_DROP=53;
	public static final int T_EACH_QUORUM=54;
	public static final int T_END_BRACKET=55;
	public static final int T_END_PARENTHESIS=56;
	public static final int T_END_SBRACKET=57;
	public static final int T_EQUAL=58;
	public static final int T_EXISTS=59;
	public static final int T_EXPLAIN=60;
	public static final int T_FALSE=61;
	public static final int T_FLOAT=62;
	public static final int T_FOR=63;
	public static final int T_FROM=64;
	public static final int T_GET=65;
	public static final int T_GROUP=66;
	public static final int T_GT=67;
	public static final int T_IDENT=68;
	public static final int T_IF=69;
	public static final int T_IN=70;
	public static final int T_INDEX=71;
	public static final int T_INDEX_TYPE=72;
	public static final int T_INNER=73;
	public static final int T_INSERT=74;
	public static final int T_INTERROGATION=75;
	public static final int T_INTO=76;
	public static final int T_JOIN=77;
	public static final int T_KEY=78;
	public static final int T_KEYSPACE=79;
	public static final int T_KS_AND_TN=80;
	public static final int T_LAST=81;
	public static final int T_LET=82;
	public static final int T_LIKE=83;
	public static final int T_LIMIT=84;
	public static final int T_LIST=85;
	public static final int T_LOCAL_ONE=86;
	public static final int T_LOCAL_QUORUM=87;
	public static final int T_LT=88;
	public static final int T_MAX=89;
	public static final int T_MIN=90;
	public static final int T_NOT=91;
	public static final int T_NOT_EQUAL=92;
	public static final int T_ON=93;
	public static final int T_ONE=94;
	public static final int T_OPTIONS=95;
	public static final int T_ORDER=96;
	public static final int T_PATH=97;
	public static final int T_PLAN=98;
	public static final int T_PLUS=99;
	public static final int T_PRIMARY=100;
	public static final int T_PROCESS=101;
	public static final int T_QUORUM=102;
	public static final int T_QUOTE=103;
	public static final int T_REMOVE=104;
	public static final int T_ROWS=105;
	public static final int T_SELECT=106;
	public static final int T_SEMICOLON=107;
	public static final int T_SET=108;
	public static final int T_START_BRACKET=109;
	public static final int T_START_PARENTHESIS=110;
	public static final int T_START_SBRACKET=111;
	public static final int T_STOP=112;
	public static final int T_STORAGE=113;
	public static final int T_SUBTRACT=114;
	public static final int T_TABLE=115;
	public static final int T_TERM=116;
	public static final int T_THREE=117;
	public static final int T_TOKEN=118;
	public static final int T_TRIGGER=119;
	public static final int T_TRUE=120;
	public static final int T_TRUNCATE=121;
	public static final int T_TWO=122;
	public static final int T_TYPE=123;
	public static final int T_UDF=124;
	public static final int T_UPDATE=125;
	public static final int T_USE=126;
	public static final int T_USING=127;
	public static final int T_VALUES=128;
	public static final int T_WHERE=129;
	public static final int T_WINDOW=130;
	public static final int T_WITH=131;
	public static final int U=132;
	public static final int V=133;
	public static final int W=134;
	public static final int WS=135;
	public static final int X=136;
	public static final int Y=137;
	public static final int Z=138;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public MetaParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public MetaParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return MetaParser.tokenNames; }
	@Override public String getGrammarFileName() { return "Meta.g"; }


	    public void displayRecognitionError(String[] tokenNames, RecognitionException e){
	        System.err.print("Error recognized: ");
	        String hdr = getErrorHeader(e);
	        String msg = getErrorMessage(e, tokenNames);
	        System.err.print(hdr+": ");
	        System.err.println(msg);
	    }



	// $ANTLR start "deleteStatement"
	// Meta.g:208:1: deleteStatement returns [DeleteStatement ds] : T_DELETE ( T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS )* T_FROM tablename= T_IDENT T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ;
	public final DeleteStatement deleteStatement() throws RecognitionException {
		DeleteStatement ds = null;


		Token firstField=null;
		Token field=null;
		Token tablename=null;
		MetaRelation rel1 =null;
		MetaRelation relN =null;


				ds = new DeleteStatement();
			
		try {
			// Meta.g:211:3: ( T_DELETE ( T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS )* T_FROM tablename= T_IDENT T_WHERE rel1= getRelation ( T_AND relN= getRelation )* )
			// Meta.g:212:2: T_DELETE ( T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS )* T_FROM tablename= T_IDENT T_WHERE rel1= getRelation ( T_AND relN= getRelation )*
			{
			match(input,T_DELETE,FOLLOW_T_DELETE_in_deleteStatement1902); 
			// Meta.g:213:2: ( T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==T_START_PARENTHESIS) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// Meta.g:213:3: T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS
					{
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_deleteStatement1906); 
					firstField=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_deleteStatement1911); 
					ds.addColumn((firstField!=null?firstField.getText():null));
					// Meta.g:215:3: ( T_COMMA field= T_IDENT )*
					loop1:
					while (true) {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( (LA1_0==T_COMMA) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// Meta.g:215:4: T_COMMA field= T_IDENT
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_deleteStatement1918); 
							field=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_deleteStatement1925); 
							ds.addColumn((field!=null?field.getText():null));
							}
							break;

						default :
							break loop1;
						}
					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_deleteStatement1937); 
					}
					break;

				default :
					break loop2;
				}
			}

			match(input,T_FROM,FOLLOW_T_FROM_in_deleteStatement1942); 
			tablename=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_deleteStatement1947); 
			ds.setTablename((tablename!=null?tablename.getText():null));
			match(input,T_WHERE,FOLLOW_T_WHERE_in_deleteStatement1952); 
			pushFollow(FOLLOW_getRelation_in_deleteStatement1957);
			rel1=getRelation();
			state._fsp--;

			ds.addRelation(rel1);
			// Meta.g:223:44: ( T_AND relN= getRelation )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==T_AND) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// Meta.g:223:45: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_deleteStatement1962); 
					pushFollow(FOLLOW_getRelation_in_deleteStatement1966);
					relN=getRelation();
					state._fsp--;

					ds.addRelation(relN);
					}
					break;

				default :
					break loop3;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return ds;
	}
	// $ANTLR end "deleteStatement"



	// $ANTLR start "addStatement"
	// Meta.g:227:1: addStatement returns [AddStatement as] : T_ADD T_QUOTE name= T_PATH T_QUOTE ;
	public final AddStatement addStatement() throws RecognitionException {
		AddStatement as = null;


		Token name=null;

		try {
			// Meta.g:227:39: ( T_ADD T_QUOTE name= T_PATH T_QUOTE )
			// Meta.g:228:2: T_ADD T_QUOTE name= T_PATH T_QUOTE
			{
			match(input,T_ADD,FOLLOW_T_ADD_in_addStatement1985); 
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_addStatement1987); 
			name=(Token)match(input,T_PATH,FOLLOW_T_PATH_in_addStatement1991); 
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_addStatement1993); 
			as = new AddStatement((name!=null?name.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return as;
	}
	// $ANTLR end "addStatement"



	// $ANTLR start "listStatement"
	// Meta.g:232:1: listStatement returns [ListStatement ls] : T_LIST (type= getListTypes ) ;
	public final ListStatement listStatement() throws RecognitionException {
		ListStatement ls = null;


		ParserRuleReturnScope type =null;

		try {
			// Meta.g:232:41: ( T_LIST (type= getListTypes ) )
			// Meta.g:233:2: T_LIST (type= getListTypes )
			{
			match(input,T_LIST,FOLLOW_T_LIST_in_listStatement2010); 
			// Meta.g:233:9: (type= getListTypes )
			// Meta.g:233:10: type= getListTypes
			{
			pushFollow(FOLLOW_getListTypes_in_listStatement2015);
			type=getListTypes();
			state._fsp--;

			}

			ls = new ListStatement((type!=null?input.toString(type.start,type.stop):null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return ls;
	}
	// $ANTLR end "listStatement"



	// $ANTLR start "removeUDFStatement"
	// Meta.g:237:1: removeUDFStatement returns [RemoveUDFStatement rus] : T_REMOVE 'UDF' T_QUOTE jar= getTerm T_QUOTE ;
	public final RemoveUDFStatement removeUDFStatement() throws RecognitionException {
		RemoveUDFStatement rus = null;


		String jar =null;

		try {
			// Meta.g:237:52: ( T_REMOVE 'UDF' T_QUOTE jar= getTerm T_QUOTE )
			// Meta.g:238:2: T_REMOVE 'UDF' T_QUOTE jar= getTerm T_QUOTE
			{
			match(input,T_REMOVE,FOLLOW_T_REMOVE_in_removeUDFStatement2033); 
			match(input,146,FOLLOW_146_in_removeUDFStatement2035); 
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_removeUDFStatement2037); 
			pushFollow(FOLLOW_getTerm_in_removeUDFStatement2041);
			jar=getTerm();
			state._fsp--;

			rus = new RemoveUDFStatement(jar);
			match(input,T_QUOTE,FOLLOW_T_QUOTE_in_removeUDFStatement2045); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return rus;
	}
	// $ANTLR end "removeUDFStatement"



	// $ANTLR start "dropIndexStatement"
	// Meta.g:242:1: dropIndexStatement returns [DropIndexStatement dis] : T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT ;
	public final DropIndexStatement dropIndexStatement() throws RecognitionException {
		DropIndexStatement dis = null;


		Token name=null;


				dis = new DropIndexStatement();
			
		try {
			// Meta.g:245:3: ( T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT )
			// Meta.g:246:2: T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropIndexStatement2065); 
			match(input,T_INDEX,FOLLOW_T_INDEX_in_dropIndexStatement2067); 
			// Meta.g:247:2: ( T_IF T_EXISTS )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==T_IF) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// Meta.g:247:3: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropIndexStatement2071); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropIndexStatement2073); 
					dis.setDropIfExists();
					}
					break;

			}

			name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropIndexStatement2082); 
			dis.setName((name!=null?name.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return dis;
	}
	// $ANTLR end "dropIndexStatement"



	// $ANTLR start "createIndexStatement"
	// Meta.g:255:1: createIndexStatement returns [CreateIndexStatement cis] : T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? name= T_IDENT T_ON tablename= T_IDENT T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? ;
	public final CreateIndexStatement createIndexStatement() throws RecognitionException {
		CreateIndexStatement cis = null;


		Token indexType=null;
		Token name=null;
		Token tablename=null;
		Token firstField=null;
		Token field=null;
		Token key=null;
		String usingClass =null;
		ValueProperty value =null;


				cis = new CreateIndexStatement();
			
		try {
			// Meta.g:258:3: ( T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? name= T_IDENT T_ON tablename= T_IDENT T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? )
			// Meta.g:259:2: T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? name= T_IDENT T_ON tablename= T_IDENT T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createIndexStatement2107); 
			indexType=(Token)match(input,T_INDEX_TYPE,FOLLOW_T_INDEX_TYPE_in_createIndexStatement2111); 
			cis.setIndexType((indexType!=null?indexType.getText():null));
			match(input,T_INDEX,FOLLOW_T_INDEX_in_createIndexStatement2115); 
			// Meta.g:260:2: ( T_IF T_NOT T_EXISTS )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==T_IF) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// Meta.g:260:3: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createIndexStatement2119); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createIndexStatement2121); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createIndexStatement2123); 
					cis.setCreateIfNotExists();
					}
					break;

			}

			name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2132); 
			cis.setName((name!=null?name.getText():null));
			match(input,T_ON,FOLLOW_T_ON_in_createIndexStatement2137); 
			tablename=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2141); 
			cis.setTablename((tablename!=null?tablename.getText():null));
			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createIndexStatement2146); 
			firstField=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2151); 
			cis.addColumn((firstField!=null?firstField.getText():null));
			// Meta.g:265:2: ( T_COMMA field= T_IDENT )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( (LA6_0==T_COMMA) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// Meta.g:265:3: T_COMMA field= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createIndexStatement2157); 
					field=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2163); 
					cis.addColumn((field!=null?field.getText():null));
					}
					break;

				default :
					break loop6;
				}
			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createIndexStatement2172); 
			// Meta.g:269:2: ( T_USING usingClass= getTerm )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==T_USING) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// Meta.g:269:3: T_USING usingClass= getTerm
					{
					match(input,T_USING,FOLLOW_T_USING_in_createIndexStatement2176); 
					pushFollow(FOLLOW_getTerm_in_createIndexStatement2180);
					usingClass=getTerm();
					state._fsp--;

					cis.setUsingClass(usingClass);
					}
					break;

			}

			// Meta.g:270:2: ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==T_WITH) ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// Meta.g:270:3: T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createIndexStatement2188); 
					match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_createIndexStatement2190); 
					key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2194); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement2196); 
					pushFollow(FOLLOW_getValueProperty_in_createIndexStatement2200);
					value=getValueProperty();
					state._fsp--;

					cis.addOption((key!=null?key.getText():null), value);
					// Meta.g:271:3: ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					loop8:
					while (true) {
						int alt8=2;
						int LA8_0 = input.LA(1);
						if ( (LA8_0==T_AND) ) {
							alt8=1;
						}

						switch (alt8) {
						case 1 :
							// Meta.g:271:4: T_AND key= T_IDENT T_EQUAL value= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_createIndexStatement2207); 
							key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2211); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement2213); 
							pushFollow(FOLLOW_getValueProperty_in_createIndexStatement2217);
							value=getValueProperty();
							state._fsp--;

							cis.addOption((key!=null?key.getText():null), value);
							}
							break;

						default :
							break loop8;
						}
					}

					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return cis;
	}
	// $ANTLR end "createIndexStatement"



	// $ANTLR start "updateTableStatement"
	// Meta.g:276:1: updateTableStatement returns [UpdateTableStatement pdtbst] : T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )? ;
	public final UpdateTableStatement updateTableStatement() throws RecognitionException {
		UpdateTableStatement pdtbst = null;


		Token id1=null;
		Token idN=null;
		String tablename =null;
		Option opt1 =null;
		Option optN =null;
		Assignment assig1 =null;
		Assignment assigN =null;
		MetaRelation rel1 =null;
		MetaRelation relN =null;
		String term1 =null;
		String termN =null;


		        boolean optsInc = false;
		        boolean condsInc = false;
		        List<Option> options = new ArrayList<>();
		        List<Assignment> assignments = new ArrayList<>();
		        List<MetaRelation> whereclauses = new ArrayList<>();
		        Map<String, Term> conditions = new HashMap<>();
		    
		try {
			// Meta.g:284:6: ( T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )? )
			// Meta.g:285:5: T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )?
			{
			match(input,T_UPDATE,FOLLOW_T_UPDATE_in_updateTableStatement2256); 
			pushFollow(FOLLOW_getTableID_in_updateTableStatement2260);
			tablename=getTableID();
			state._fsp--;

			// Meta.g:286:5: ( T_USING opt1= getOption (optN= getOption )* )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==T_USING) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// Meta.g:286:6: T_USING opt1= getOption (optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_updateTableStatement2267); 
					pushFollow(FOLLOW_getOption_in_updateTableStatement2271);
					opt1=getOption();
					state._fsp--;

					optsInc = true; options.add(opt1);
					// Meta.g:286:66: (optN= getOption )*
					loop10:
					while (true) {
						int alt10=2;
						int LA10_0 = input.LA(1);
						if ( (LA10_0==T_CLUSTERING||LA10_0==T_COMPACT||LA10_0==T_IDENT) ) {
							alt10=1;
						}

						switch (alt10) {
						case 1 :
							// Meta.g:286:67: optN= getOption
							{
							pushFollow(FOLLOW_getOption_in_updateTableStatement2278);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop10;
						}
					}

					}
					break;

			}

			match(input,T_SET,FOLLOW_T_SET_in_updateTableStatement2290); 
			pushFollow(FOLLOW_getAssignment_in_updateTableStatement2294);
			assig1=getAssignment();
			state._fsp--;

			assignments.add(assig1);
			// Meta.g:287:59: ( T_COMMA assigN= getAssignment )*
			loop12:
			while (true) {
				int alt12=2;
				int LA12_0 = input.LA(1);
				if ( (LA12_0==T_COMMA) ) {
					alt12=1;
				}

				switch (alt12) {
				case 1 :
					// Meta.g:287:60: T_COMMA assigN= getAssignment
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_updateTableStatement2299); 
					pushFollow(FOLLOW_getAssignment_in_updateTableStatement2303);
					assigN=getAssignment();
					state._fsp--;

					assignments.add(assigN);
					}
					break;

				default :
					break loop12;
				}
			}

			match(input,T_WHERE,FOLLOW_T_WHERE_in_updateTableStatement2313); 
			pushFollow(FOLLOW_getRelation_in_updateTableStatement2317);
			rel1=getRelation();
			state._fsp--;

			whereclauses.add(rel1);
			// Meta.g:288:56: ( T_AND relN= getRelation )*
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( (LA13_0==T_AND) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// Meta.g:288:57: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_updateTableStatement2322); 
					pushFollow(FOLLOW_getRelation_in_updateTableStatement2326);
					relN=getRelation();
					state._fsp--;

					whereclauses.add(relN);
					}
					break;

				default :
					break loop13;
				}
			}

			// Meta.g:289:5: ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==T_IF) ) {
				alt15=1;
			}
			switch (alt15) {
				case 1 :
					// Meta.g:289:6: T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )*
					{
					match(input,T_IF,FOLLOW_T_IF_in_updateTableStatement2337); 
					id1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_updateTableStatement2341); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_updateTableStatement2343); 
					pushFollow(FOLLOW_getTerm_in_updateTableStatement2347);
					term1=getTerm();
					state._fsp--;

					condsInc = true; conditions.put((id1!=null?id1.getText():null), new Term(term1));
					// Meta.g:290:21: ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )*
					loop14:
					while (true) {
						int alt14=2;
						int LA14_0 = input.LA(1);
						if ( (LA14_0==T_AND) ) {
							alt14=1;
						}

						switch (alt14) {
						case 1 :
							// Meta.g:290:22: T_AND idN= T_IDENT T_EQUAL termN= getTerm
							{
							match(input,T_AND,FOLLOW_T_AND_in_updateTableStatement2373); 
							idN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_updateTableStatement2377); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_updateTableStatement2379); 
							pushFollow(FOLLOW_getTerm_in_updateTableStatement2383);
							termN=getTerm();
							state._fsp--;

							conditions.put((idN!=null?idN.getText():null), new Term(termN));
							}
							break;

						default :
							break loop14;
						}
					}

					}
					break;

			}

			 
			        if(optsInc)
			            if(condsInc)
			                pdtbst = new UpdateTableStatement(tablename, options, assignments, whereclauses, conditions);
			            else
			                pdtbst = new UpdateTableStatement(tablename, options, assignments, whereclauses);
			        else
			            if(condsInc)
			                pdtbst = new UpdateTableStatement(tablename, assignments, whereclauses, conditions);
			            else
			                pdtbst = new UpdateTableStatement(tablename, assignments, whereclauses);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return pdtbst;
	}
	// $ANTLR end "updateTableStatement"



	// $ANTLR start "stopProcessStatement"
	// Meta.g:305:1: stopProcessStatement returns [StopProcessStatement stprst] : T_STOP T_PROCESS ident= T_IDENT ;
	public final StopProcessStatement stopProcessStatement() throws RecognitionException {
		StopProcessStatement stprst = null;


		Token ident=null;

		try {
			// Meta.g:305:59: ( T_STOP T_PROCESS ident= T_IDENT )
			// Meta.g:306:5: T_STOP T_PROCESS ident= T_IDENT
			{
			match(input,T_STOP,FOLLOW_T_STOP_in_stopProcessStatement2415); 
			match(input,T_PROCESS,FOLLOW_T_PROCESS_in_stopProcessStatement2417); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_stopProcessStatement2421); 
			 stprst = new StopProcessStatement((ident!=null?ident.getText():null)); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return stprst;
	}
	// $ANTLR end "stopProcessStatement"



	// $ANTLR start "dropTriggerStatement"
	// Meta.g:309:1: dropTriggerStatement returns [DropTriggerStatement drtrst] : T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT ;
	public final DropTriggerStatement dropTriggerStatement() throws RecognitionException {
		DropTriggerStatement drtrst = null;


		Token ident=null;
		Token ident2=null;

		try {
			// Meta.g:309:59: ( T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT )
			// Meta.g:310:5: T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTriggerStatement2443); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropTriggerStatement2450); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement2454); 
			match(input,T_ON,FOLLOW_T_ON_in_dropTriggerStatement2461); 
			ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement2470); 
			drtrst = new DropTriggerStatement((ident!=null?ident.getText():null),(ident2!=null?ident2.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return drtrst;
	}
	// $ANTLR end "dropTriggerStatement"



	// $ANTLR start "createTriggerStatement"
	// Meta.g:317:1: createTriggerStatement returns [CreateTriggerStatement crtrst] : T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT ;
	public final CreateTriggerStatement createTriggerStatement() throws RecognitionException {
		CreateTriggerStatement crtrst = null;


		Token trigger_name=null;
		Token table_name=null;
		Token class_name=null;

		try {
			// Meta.g:317:63: ( T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT )
			// Meta.g:318:5: T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTriggerStatement2498); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_createTriggerStatement2505); 
			trigger_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2509); 
			match(input,T_ON,FOLLOW_T_ON_in_createTriggerStatement2516); 
			table_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2525); 
			match(input,T_USING,FOLLOW_T_USING_in_createTriggerStatement2531); 
			class_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2535); 
			crtrst = new CreateTriggerStatement((trigger_name!=null?trigger_name.getText():null),(table_name!=null?table_name.getText():null),(class_name!=null?class_name.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return crtrst;
	}
	// $ANTLR end "createTriggerStatement"



	// $ANTLR start "createTableStatement"
	// Meta.g:327:1: createTableStatement returns [CreateTableStatement crtast] : T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= T_IDENT '(' (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* ) ')' ( T_WITH )? (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )? ;
	public final CreateTableStatement createTableStatement() throws RecognitionException {
		CreateTableStatement crtast = null;


		Token name_table=null;
		Token ident_column1=null;
		Token type1=null;
		Token ident_columN=null;
		Token typeN=null;
		Token primaryK=null;
		Token partitionKN=null;
		Token partitionK=null;
		Token clusterKN=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


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
		    
		try {
			// Meta.g:339:6: ( T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= T_IDENT '(' (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* ) ')' ( T_WITH )? (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )? )
			// Meta.g:340:5: T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= T_IDENT '(' (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* ) ')' ( T_WITH )? (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTableStatement2574); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_createTableStatement2580); 
			// Meta.g:342:5: ( T_IF T_NOT T_EXISTS )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IF) ) {
				alt16=1;
			}
			switch (alt16) {
				case 1 :
					// Meta.g:342:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createTableStatement2587); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createTableStatement2589); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createTableStatement2591); 
					ifNotExists_2 = true;
					}
					break;

			}

			name_table=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2604); 
			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement2610); 
			// Meta.g:344:9: (ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )* )
			// Meta.g:345:17: ident_column1= T_IDENT type1= T_IDENT ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )*
			{
			ident_column1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2644); 
			type1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2648); 
			// Meta.g:345:53: ( T_PRIMARY T_KEY )?
			int alt17=2;
			int LA17_0 = input.LA(1);
			if ( (LA17_0==T_PRIMARY) ) {
				alt17=1;
			}
			switch (alt17) {
				case 1 :
					// Meta.g:345:54: T_PRIMARY T_KEY
					{
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2651); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2653); 
					}
					break;

			}

			columns.put((ident_column1!=null?ident_column1.getText():null),(type1!=null?type1.getText():null)); Type_Primary_Key=1;
			// Meta.g:346:17: ( ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' ) )*
			loop23:
			while (true) {
				int alt23=3;
				int LA23_0 = input.LA(1);
				if ( (LA23_0==T_COMMA) ) {
					int LA23_2 = input.LA(2);
					if ( (LA23_2==T_IDENT) ) {
						alt23=1;
					}
					else if ( (LA23_2==T_PRIMARY) ) {
						alt23=2;
					}

				}

				switch (alt23) {
				case 1 :
					// Meta.g:347:21: ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? )
					{
					// Meta.g:347:21: ( ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )? )
					// Meta.g:347:23: ',' ident_columN= T_IDENT typeN= T_IDENT ( T_PRIMARY T_KEY )?
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement2702); 
					ident_columN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2706); 
					typeN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2710); 
					// Meta.g:347:62: ( T_PRIMARY T_KEY )?
					int alt18=2;
					int LA18_0 = input.LA(1);
					if ( (LA18_0==T_PRIMARY) ) {
						alt18=1;
					}
					switch (alt18) {
						case 1 :
							// Meta.g:347:63: T_PRIMARY T_KEY
							{
							match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2713); 
							match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2715); 
							Type_Primary_Key=2;columnNumberPK=columnNumberPK_inter +1;
							}
							break;

					}

					columns.put((ident_columN!=null?ident_columN.getText():null),(typeN!=null?typeN.getText():null));columnNumberPK_inter+=1;
					}

					}
					break;
				case 2 :
					// Meta.g:348:22: ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' )
					{
					// Meta.g:348:22: ( ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')' )
					// Meta.g:349:25: ',' T_PRIMARY T_KEY '(' ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) ) ')'
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement2773); 
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2775); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2777); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement2779); 
					// Meta.g:350:25: ( (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* ) | ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* ) )
					int alt22=2;
					int LA22_0 = input.LA(1);
					if ( (LA22_0==T_IDENT) ) {
						alt22=1;
					}
					else if ( (LA22_0==T_START_PARENTHESIS) ) {
						alt22=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 22, 0, input);
						throw nvae;
					}

					switch (alt22) {
						case 1 :
							// Meta.g:351:29: (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* )
							{
							// Meta.g:351:29: (primaryK= T_IDENT ( ',' partitionKN= T_IDENT )* )
							// Meta.g:351:33: primaryK= T_IDENT ( ',' partitionKN= T_IDENT )*
							{
							primaryK=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2841); 
							primaryKey.add((primaryK!=null?primaryK.getText():null));Type_Primary_Key=3;
							// Meta.g:353:33: ( ',' partitionKN= T_IDENT )*
							loop19:
							while (true) {
								int alt19=2;
								int LA19_0 = input.LA(1);
								if ( (LA19_0==T_COMMA) ) {
									alt19=1;
								}

								switch (alt19) {
								case 1 :
									// Meta.g:353:34: ',' partitionKN= T_IDENT
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement2906); 
									partitionKN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement2909); 
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop19;
								}
							}

							}

							}
							break;
						case 2 :
							// Meta.g:355:30: ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* )
							{
							// Meta.g:355:30: ( '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )* )
							// Meta.g:356:33: '(' partitionK= T_IDENT ( ',' partitionKN= T_IDENT )* ')' ( ',' clusterKN= T_IDENT )*
							{
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement3008); 
							partitionK=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement3012); 
							primaryKey.add((partitionK!=null?partitionK.getText():null));Type_Primary_Key=4;
							// Meta.g:357:37: ( ',' partitionKN= T_IDENT )*
							loop20:
							while (true) {
								int alt20=2;
								int LA20_0 = input.LA(1);
								if ( (LA20_0==T_COMMA) ) {
									alt20=1;
								}

								switch (alt20) {
								case 1 :
									// Meta.g:357:38: ',' partitionKN= T_IDENT
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3053); 
									partitionKN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement3056); 
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop20;
								}
							}

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3094); 
							// Meta.g:359:33: ( ',' clusterKN= T_IDENT )*
							loop21:
							while (true) {
								int alt21=2;
								int LA21_0 = input.LA(1);
								if ( (LA21_0==T_COMMA) ) {
									alt21=1;
								}

								switch (alt21) {
								case 1 :
									// Meta.g:359:34: ',' clusterKN= T_IDENT
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3130); 
									clusterKN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement3134); 
									clusterKey.add((clusterKN!=null?clusterKN.getText():null));withClusterKey=true;
									}
									break;

								default :
									break loop21;
								}
							}

							}

							}
							break;

					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3220); 
					}

					}
					break;

				default :
					break loop23;
				}
			}

			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3292); 
			// Meta.g:367:9: ( T_WITH )?
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==T_WITH) ) {
				alt24=1;
			}
			switch (alt24) {
				case 1 :
					// Meta.g:367:9: T_WITH
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createTableStatement3294); 
					}
					break;

			}

			// Meta.g:368:5: (identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )?
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0==T_IDENT) ) {
				alt26=1;
			}
			switch (alt26) {
				case 1 :
					// Meta.g:368:7: identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					{
					identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement3305); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createTableStatement3307); 
					pushFollow(FOLLOW_getValueProperty_in_createTableStatement3311);
					valueProp1=getValueProperty();
					state._fsp--;

					propierties.put((identProp1!=null?identProp1.getText():null), valueProp1);withPropierties=true;
					// Meta.g:369:13: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					loop25:
					while (true) {
						int alt25=2;
						int LA25_0 = input.LA(1);
						if ( (LA25_0==T_AND) ) {
							alt25=1;
						}

						switch (alt25) {
						case 1 :
							// Meta.g:369:14: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_createTableStatement3328); 
							identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTableStatement3332); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createTableStatement3334); 
							pushFollow(FOLLOW_getValueProperty_in_createTableStatement3338);
							valuePropN=getValueProperty();
							state._fsp--;

							propierties.put((identPropN!=null?identPropN.getText():null), valuePropN);withPropierties=true;
							}
							break;

						default :
							break loop25;
						}
					}

					}
					break;

			}

			crtast = new CreateTableStatement((name_table!=null?name_table.getText():null),columns,primaryKey,clusterKey,propierties,Type_Primary_Key,ifNotExists_2,withClusterKey,columnNumberPK,withPropierties);  
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return crtast;
	}
	// $ANTLR end "createTableStatement"



	// $ANTLR start "alterTableStatement"
	// Meta.g:374:1: alterTableStatement returns [AlterTableStatement altast] : T_ALTER T_TABLE name_table= T_IDENT ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) ;
	public final AlterTableStatement alterTableStatement() throws RecognitionException {
		AlterTableStatement altast = null;


		Token name_table=null;
		Token column=null;
		Token type=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        LinkedHashMap<String, ValueProperty> option = new LinkedHashMap<>();
		        int prop= 0;
		    
		try {
			// Meta.g:378:6: ( T_ALTER T_TABLE name_table= T_IDENT ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) )
			// Meta.g:379:5: T_ALTER T_TABLE name_table= T_IDENT ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement3402); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_alterTableStatement3408); 
			name_table=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3416); 
			// Meta.g:382:5: ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			int alt28=4;
			switch ( input.LA(1) ) {
			case T_ALTER:
				{
				alt28=1;
				}
				break;
			case T_ADD:
				{
				alt28=2;
				}
				break;
			case T_DROP:
				{
				alt28=3;
				}
				break;
			case T_WITH:
				{
				alt28=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 28, 0, input);
				throw nvae;
			}
			switch (alt28) {
				case 1 :
					// Meta.g:382:6: T_ALTER column= T_IDENT T_TYPE type= T_IDENT
					{
					match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement3423); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3427); 
					match(input,T_TYPE,FOLLOW_T_TYPE_in_alterTableStatement3429); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3433); 
					prop=1;
					}
					break;
				case 2 :
					// Meta.g:383:10: T_ADD column= T_IDENT type= T_IDENT
					{
					match(input,T_ADD,FOLLOW_T_ADD_in_alterTableStatement3446); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3450); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3454); 
					prop=2;
					}
					break;
				case 3 :
					// Meta.g:384:10: T_DROP column= T_IDENT
					{
					match(input,T_DROP,FOLLOW_T_DROP_in_alterTableStatement3467); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3471); 
					prop=3;
					}
					break;
				case 4 :
					// Meta.g:385:10: T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_alterTableStatement3484); 
					identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3501); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement3503); 
					pushFollow(FOLLOW_getValueProperty_in_alterTableStatement3507);
					valueProp1=getValueProperty();
					state._fsp--;

					option.put((identProp1!=null?identProp1.getText():null), valueProp1);
					// Meta.g:387:13: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					loop27:
					while (true) {
						int alt27=2;
						int LA27_0 = input.LA(1);
						if ( (LA27_0==T_AND) ) {
							alt27=1;
						}

						switch (alt27) {
						case 1 :
							// Meta.g:387:14: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_alterTableStatement3524); 
							identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3528); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement3530); 
							pushFollow(FOLLOW_getValueProperty_in_alterTableStatement3534);
							valuePropN=getValueProperty();
							state._fsp--;

							option.put((identPropN!=null?identPropN.getText():null), valuePropN);
							}
							break;

						default :
							break loop27;
						}
					}

					prop=4;
					}
					break;

			}

			altast = new AlterTableStatement((name_table!=null?name_table.getText():null),(column!=null?column.getText():null),(type!=null?type.getText():null),option,prop);  
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return altast;
	}
	// $ANTLR end "alterTableStatement"



	// $ANTLR start "selectStatement"
	// Meta.g:393:1: selectStatement returns [SelectStatement slctst] : T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )? ;
	public final SelectStatement selectStatement() throws RecognitionException {
		SelectStatement slctst = null;


		Token constant=null;
		SelectionClause selClause =null;
		String tablename =null;
		WindowSelect window =null;
		String identJoin =null;
		Map<String, String> fields =null;
		List<MetaRelation> whereClauses =null;
		List<Ordering> ordering =null;
		List groupby =null;


		        boolean windowInc = false;
		        boolean joinInc = false;
		        boolean whereInc = false;
		        boolean orderInc = false;
		        boolean groupInc = false;
		        boolean limitInc = false;
		        boolean disable = false;
		    
		try {
			// Meta.g:402:6: ( T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )? )
			// Meta.g:403:5: T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )?
			{
			match(input,T_SELECT,FOLLOW_T_SELECT_in_selectStatement3589); 
			pushFollow(FOLLOW_getSelectClause_in_selectStatement3593);
			selClause=getSelectClause();
			state._fsp--;

			match(input,T_FROM,FOLLOW_T_FROM_in_selectStatement3595); 
			pushFollow(FOLLOW_getTableID_in_selectStatement3599);
			tablename=getTableID();
			state._fsp--;

			// Meta.g:404:5: ( T_WITH T_WINDOW window= getWindow )?
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0==T_WITH) ) {
				alt29=1;
			}
			switch (alt29) {
				case 1 :
					// Meta.g:404:6: T_WITH T_WINDOW window= getWindow
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_selectStatement3607); 
					match(input,T_WINDOW,FOLLOW_T_WINDOW_in_selectStatement3609); 
					windowInc = true;
					pushFollow(FOLLOW_getWindow_in_selectStatement3615);
					window=getWindow();
					state._fsp--;

					}
					break;

			}

			// Meta.g:405:5: ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )?
			int alt30=2;
			int LA30_0 = input.LA(1);
			if ( (LA30_0==T_INNER) ) {
				alt30=1;
			}
			switch (alt30) {
				case 1 :
					// Meta.g:405:6: T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields
					{
					match(input,T_INNER,FOLLOW_T_INNER_in_selectStatement3628); 
					match(input,T_JOIN,FOLLOW_T_JOIN_in_selectStatement3630); 
					 joinInc = true;
					pushFollow(FOLLOW_getTableID_in_selectStatement3636);
					identJoin=getTableID();
					state._fsp--;

					match(input,T_ON,FOLLOW_T_ON_in_selectStatement3638); 
					pushFollow(FOLLOW_getFields_in_selectStatement3642);
					fields=getFields();
					state._fsp--;

					}
					break;

			}

			// Meta.g:406:5: ( T_WHERE whereClauses= getWhereClauses )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==T_WHERE) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// Meta.g:406:6: T_WHERE whereClauses= getWhereClauses
					{
					match(input,T_WHERE,FOLLOW_T_WHERE_in_selectStatement3651); 
					whereInc = true;
					pushFollow(FOLLOW_getWhereClauses_in_selectStatement3657);
					whereClauses=getWhereClauses();
					state._fsp--;

					}
					break;

			}

			// Meta.g:407:5: ( T_ORDER T_BY ordering= getOrdering )?
			int alt32=2;
			int LA32_0 = input.LA(1);
			if ( (LA32_0==T_ORDER) ) {
				alt32=1;
			}
			switch (alt32) {
				case 1 :
					// Meta.g:407:6: T_ORDER T_BY ordering= getOrdering
					{
					match(input,T_ORDER,FOLLOW_T_ORDER_in_selectStatement3666); 
					match(input,T_BY,FOLLOW_T_BY_in_selectStatement3668); 
					orderInc = true;
					pushFollow(FOLLOW_getOrdering_in_selectStatement3674);
					ordering=getOrdering();
					state._fsp--;

					}
					break;

			}

			// Meta.g:408:5: ( T_GROUP T_BY groupby= getList )?
			int alt33=2;
			int LA33_0 = input.LA(1);
			if ( (LA33_0==T_GROUP) ) {
				alt33=1;
			}
			switch (alt33) {
				case 1 :
					// Meta.g:408:6: T_GROUP T_BY groupby= getList
					{
					match(input,T_GROUP,FOLLOW_T_GROUP_in_selectStatement3683); 
					match(input,T_BY,FOLLOW_T_BY_in_selectStatement3685); 
					groupInc = true;
					pushFollow(FOLLOW_getList_in_selectStatement3691);
					groupby=getList();
					state._fsp--;

					}
					break;

			}

			// Meta.g:409:5: ( T_LIMIT constant= T_CONSTANT )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==T_LIMIT) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// Meta.g:409:6: T_LIMIT constant= T_CONSTANT
					{
					match(input,T_LIMIT,FOLLOW_T_LIMIT_in_selectStatement3700); 
					limitInc = true;
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_selectStatement3706); 
					}
					break;

			}

			// Meta.g:410:5: ( T_DISABLE T_ANALYTICS )?
			int alt35=2;
			int LA35_0 = input.LA(1);
			if ( (LA35_0==T_DISABLE) ) {
				alt35=1;
			}
			switch (alt35) {
				case 1 :
					// Meta.g:410:6: T_DISABLE T_ANALYTICS
					{
					match(input,T_DISABLE,FOLLOW_T_DISABLE_in_selectStatement3715); 
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_selectStatement3717); 
					disable = true;
					}
					break;

			}


			        slctst = new SelectStatement(selClause, tablename);        
			        if(windowInc)
			            slctst.setWindow(window);
			        if(joinInc)
			            slctst.setJoin(new InnerJoin(identJoin, fields)); 
			        if(whereInc)
			             slctst.setWhere(whereClauses); 
			        if(orderInc)
			             slctst.setOrder(ordering);
			        if(groupInc)
			            slctst.setGroup(new GroupBy(groupby)); 
			        if(limitInc)
			            slctst.setLimit(Integer.parseInt((constant!=null?constant.getText():null)));
			        if(disable)
			            slctst.setDisableAnalytics(true);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return slctst;
	}
	// $ANTLR end "selectStatement"



	// $ANTLR start "insertIntoStatement"
	// Meta.g:430:1: insertIntoStatement returns [InsertIntoStatement nsntst] : T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? ;
	public final InsertIntoStatement insertIntoStatement() throws RecognitionException {
		InsertIntoStatement nsntst = null;


		Token ident1=null;
		Token identN=null;
		String tableName =null;
		SelectStatement selectStmnt =null;
		ValueCell term1 =null;
		ValueCell termN =null;
		Option opt1 =null;
		Option optN =null;


		        List<String> ids = new ArrayList<>();
		        boolean ifNotExists = false;
		        int typeValues = InsertIntoStatement.TYPE_VALUES_CLAUSE;
		        List<ValueCell> cellValues = new ArrayList<>();
		        boolean optsInc = false;
		        List<Option> options = new ArrayList<>();
		    
		try {
			// Meta.g:438:6: ( T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? )
			// Meta.g:439:5: T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			{
			match(input,T_INSERT,FOLLOW_T_INSERT_in_insertIntoStatement3755); 
			match(input,T_INTO,FOLLOW_T_INTO_in_insertIntoStatement3762); 
			pushFollow(FOLLOW_getTableID_in_insertIntoStatement3771);
			tableName=getTableID();
			state._fsp--;

			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3777); 
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement3786); 
			ids.add((ident1!=null?ident1.getText():null));
			// Meta.g:444:5: ( T_COMMA identN= T_IDENT )*
			loop36:
			while (true) {
				int alt36=2;
				int LA36_0 = input.LA(1);
				if ( (LA36_0==T_COMMA) ) {
					alt36=1;
				}

				switch (alt36) {
				case 1 :
					// Meta.g:444:6: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement3796); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement3800); 
					ids.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop36;
				}
			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement3811); 
			// Meta.g:446:5: (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS )
			int alt38=2;
			int LA38_0 = input.LA(1);
			if ( (LA38_0==T_SELECT) ) {
				alt38=1;
			}
			else if ( (LA38_0==T_VALUES) ) {
				alt38=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 38, 0, input);
				throw nvae;
			}

			switch (alt38) {
				case 1 :
					// Meta.g:447:9: selectStmnt= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_insertIntoStatement3830);
					selectStmnt=selectStatement();
					state._fsp--;

					typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;
					}
					break;
				case 2 :
					// Meta.g:449:9: T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS
					{
					match(input,T_VALUES,FOLLOW_T_VALUES_in_insertIntoStatement3853); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3863); 
					pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement3880);
					term1=getTermOrLiteral();
					state._fsp--;

					cellValues.add(term1);
					// Meta.g:452:13: ( T_COMMA termN= getTermOrLiteral )*
					loop37:
					while (true) {
						int alt37=2;
						int LA37_0 = input.LA(1);
						if ( (LA37_0==T_COMMA) ) {
							alt37=1;
						}

						switch (alt37) {
						case 1 :
							// Meta.g:452:14: T_COMMA termN= getTermOrLiteral
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement3897); 
							pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement3901);
							termN=getTermOrLiteral();
							state._fsp--;

							cellValues.add(termN);
							}
							break;

						default :
							break loop37;
						}
					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement3915); 
					}
					break;

			}

			// Meta.g:455:5: ( T_IF T_NOT T_EXISTS )?
			int alt39=2;
			int LA39_0 = input.LA(1);
			if ( (LA39_0==T_IF) ) {
				alt39=1;
			}
			switch (alt39) {
				case 1 :
					// Meta.g:455:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_insertIntoStatement3928); 
					match(input,T_NOT,FOLLOW_T_NOT_in_insertIntoStatement3930); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_insertIntoStatement3932); 
					ifNotExists=true;
					}
					break;

			}

			// Meta.g:456:5: ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			int alt41=2;
			int LA41_0 = input.LA(1);
			if ( (LA41_0==T_USING) ) {
				alt41=1;
			}
			switch (alt41) {
				case 1 :
					// Meta.g:457:9: T_USING opt1= getOption ( T_AND optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_insertIntoStatement3953); 
					optsInc=true;
					pushFollow(FOLLOW_getOption_in_insertIntoStatement3968);
					opt1=getOption();
					state._fsp--;


					            options.add(opt1);
					        
					// Meta.g:461:9: ( T_AND optN= getOption )*
					loop40:
					while (true) {
						int alt40=2;
						int LA40_0 = input.LA(1);
						if ( (LA40_0==T_AND) ) {
							alt40=1;
						}

						switch (alt40) {
						case 1 :
							// Meta.g:461:10: T_AND optN= getOption
							{
							match(input,T_AND,FOLLOW_T_AND_in_insertIntoStatement3981); 
							pushFollow(FOLLOW_getOption_in_insertIntoStatement3985);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop40;
						}
					}

					}
					break;

			}


			        if(typeValues==InsertIntoStatement.TYPE_SELECT_CLAUSE)
			            if(optsInc)
			                nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists, options);
			            else
			                nsntst = new InsertIntoStatement(tableName, ids, selectStmnt, ifNotExists);
			        else
			            if(optsInc)
			                nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists, options);
			            else
			                nsntst = new InsertIntoStatement(tableName, ids, cellValues, ifNotExists);
			                
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return nsntst;
	}
	// $ANTLR end "insertIntoStatement"



	// $ANTLR start "explainPlanStatement"
	// Meta.g:478:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		Statement parsedStmnt =null;

		try {
			// Meta.g:478:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:479:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement4022); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement4024); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement4026); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement4030);
			parsedStmnt=metaStatement();
			state._fsp--;

			xpplst = new ExplainPlanStatement(parsedStmnt);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return xpplst;
	}
	// $ANTLR end "explainPlanStatement"



	// $ANTLR start "setOptionsStatement"
	// Meta.g:483:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:490:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:491:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement4064); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement4066); 
			// Meta.g:491:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt48=2;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==T_ANALYTICS) ) {
				alt48=1;
			}
			else if ( (LA48_0==T_CONSISTENCY) ) {
				alt48=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 48, 0, input);
				throw nvae;
			}

			switch (alt48) {
				case 1 :
					// Meta.g:492:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement4078); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4080); 
					// Meta.g:492:29: ( T_TRUE | T_FALSE )
					int alt42=2;
					int LA42_0 = input.LA(1);
					if ( (LA42_0==T_TRUE) ) {
						alt42=1;
					}
					else if ( (LA42_0==T_FALSE) ) {
						alt42=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 42, 0, input);
						throw nvae;
					}

					switch (alt42) {
						case 1 :
							// Meta.g:492:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement4083); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:492:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement4086); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:493:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt44=2;
					int LA44_0 = input.LA(1);
					if ( (LA44_0==T_AND) ) {
						alt44=1;
					}
					switch (alt44) {
						case 1 :
							// Meta.g:493:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement4101); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement4103); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4105); 
							// Meta.g:494:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt43=9;
							switch ( input.LA(1) ) {
							case T_ALL:
								{
								alt43=1;
								}
								break;
							case T_ANY:
								{
								alt43=2;
								}
								break;
							case T_QUORUM:
								{
								alt43=3;
								}
								break;
							case T_ONE:
								{
								alt43=4;
								}
								break;
							case T_TWO:
								{
								alt43=5;
								}
								break;
							case T_THREE:
								{
								alt43=6;
								}
								break;
							case T_EACH_QUORUM:
								{
								alt43=7;
								}
								break;
							case T_LOCAL_ONE:
								{
								alt43=8;
								}
								break;
							case T_LOCAL_QUORUM:
								{
								alt43=9;
								}
								break;
							default:
								NoViableAltException nvae =
									new NoViableAltException("", 43, 0, input);
								throw nvae;
							}
							switch (alt43) {
								case 1 :
									// Meta.g:494:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement4120); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:495:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement4139); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:496:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement4157); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:497:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement4175); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:498:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement4193); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:499:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement4211); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:500:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4229); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:501:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4247); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:502:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4265); 
									cnstc=Consistency.LOCAL_QUORUM;
									}
									break;

							}

							checks.set(1, true);
							}
							break;

					}

					 stptst = new SetOptionsStatement(analytics, cnstc, checks);
					}
					break;
				case 2 :
					// Meta.g:506:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement4315); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4317); 
					// Meta.g:507:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt45=9;
					switch ( input.LA(1) ) {
					case T_ALL:
						{
						alt45=1;
						}
						break;
					case T_ANY:
						{
						alt45=2;
						}
						break;
					case T_QUORUM:
						{
						alt45=3;
						}
						break;
					case T_ONE:
						{
						alt45=4;
						}
						break;
					case T_TWO:
						{
						alt45=5;
						}
						break;
					case T_THREE:
						{
						alt45=6;
						}
						break;
					case T_EACH_QUORUM:
						{
						alt45=7;
						}
						break;
					case T_LOCAL_ONE:
						{
						alt45=8;
						}
						break;
					case T_LOCAL_QUORUM:
						{
						alt45=9;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 45, 0, input);
						throw nvae;
					}
					switch (alt45) {
						case 1 :
							// Meta.g:507:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement4333); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:508:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement4352); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:509:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement4370); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:510:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement4388); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:511:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement4406); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:512:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement4424); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:513:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4442); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:514:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4460); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:515:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4478); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:517:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt47=2;
					int LA47_0 = input.LA(1);
					if ( (LA47_0==T_AND) ) {
						alt47=1;
					}
					switch (alt47) {
						case 1 :
							// Meta.g:517:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement4506); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement4508); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4510); 
							// Meta.g:517:36: ( T_TRUE | T_FALSE )
							int alt46=2;
							int LA46_0 = input.LA(1);
							if ( (LA46_0==T_TRUE) ) {
								alt46=1;
							}
							else if ( (LA46_0==T_FALSE) ) {
								alt46=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 46, 0, input);
								throw nvae;
							}

							switch (alt46) {
								case 1 :
									// Meta.g:517:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement4513); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:517:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement4516); 
									analytics=false;
									}
									break;

							}

							checks.set(0, true);
							}
							break;

					}

					 stptst = new SetOptionsStatement(analytics, cnstc, checks);
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return stptst;
	}
	// $ANTLR end "setOptionsStatement"



	// $ANTLR start "useStatement"
	// Meta.g:523:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:523:41: ( T_USE iden= T_IDENT )
			// Meta.g:524:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement4571); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement4579); 
			usst = new UseStatement((iden!=null?iden.getText():null));
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return usst;
	}
	// $ANTLR end "useStatement"



	// $ANTLR start "dropKeyspaceStatement"
	// Meta.g:528:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:531:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:532:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement4609); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement4615); 
			// Meta.g:534:5: ( T_IF T_EXISTS )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==T_IF) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// Meta.g:534:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement4622); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement4624); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement4636); 
			 drksst = new DropKeyspaceStatement((iden!=null?iden.getText():null), ifExists);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return drksst;
	}
	// $ANTLR end "dropKeyspaceStatement"



	// $ANTLR start "alterKeyspaceStatement"
	// Meta.g:539:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:542:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:543:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement4670); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement4676); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4684); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement4690); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4698); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement4700); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement4704);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:548:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop50:
			while (true) {
				int alt50=2;
				int LA50_0 = input.LA(1);
				if ( (LA50_0==T_AND) ) {
					alt50=1;
				}

				switch (alt50) {
				case 1 :
					// Meta.g:548:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement4713); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4717); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement4719); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement4723);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop50;
				}
			}

			 alksst = new AlterKeyspaceStatement((ident!=null?ident.getText():null), properties); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return alksst;
	}
	// $ANTLR end "alterKeyspaceStatement"



	// $ANTLR start "createKeyspaceStatement"
	// Meta.g:552:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final CreateKeyspaceStatement createKeyspaceStatement() throws RecognitionException {
		CreateKeyspaceStatement crksst = null;


		Token identKS=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        boolean ifNotExists = false;
		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:556:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:557:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement4762); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement4768); 
			// Meta.g:559:5: ( T_IF T_NOT T_EXISTS )?
			int alt51=2;
			int LA51_0 = input.LA(1);
			if ( (LA51_0==T_IF) ) {
				alt51=1;
			}
			switch (alt51) {
				case 1 :
					// Meta.g:559:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement4775); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement4777); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement4779); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement4791); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement4797); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement4809); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement4811); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement4815);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:563:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop52:
			while (true) {
				int alt52=2;
				int LA52_0 = input.LA(1);
				if ( (LA52_0==T_AND) ) {
					alt52=1;
				}

				switch (alt52) {
				case 1 :
					// Meta.g:563:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement4824); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement4828); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement4830); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement4834);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop52;
				}
			}

			 crksst = new CreateKeyspaceStatement((identKS!=null?identKS.getText():null), ifNotExists, properties); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return crksst;
	}
	// $ANTLR end "createKeyspaceStatement"



	// $ANTLR start "dropTableStatement"
	// Meta.g:567:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String identID =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:570:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID )
			// Meta.g:571:5: T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement4873); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement4879); 
			// Meta.g:573:5: ( T_IF T_EXISTS )?
			int alt53=2;
			int LA53_0 = input.LA(1);
			if ( (LA53_0==T_IF) ) {
				alt53=1;
			}
			switch (alt53) {
				case 1 :
					// Meta.g:573:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement4886); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement4888); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement4900);
			identID=getTableID();
			state._fsp--;


			        drtbst = new DropTableStatement(identID, ifExists);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return drtbst;
	}
	// $ANTLR end "dropTableStatement"



	// $ANTLR start "truncateStatement"
	// Meta.g:579:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:579:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:580:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement4920); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement4933);
			ident=getTableID();
			state._fsp--;


			            trst = new TruncateStatement(ident);
				
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return trst;
	}
	// $ANTLR end "truncateStatement"



	// $ANTLR start "metaStatement"
	// Meta.g:586:1: metaStatement returns [Statement st] : (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_pdtb= updateTableStatement |st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement |ds= deleteStatement );
	public final Statement metaStatement() throws RecognitionException {
		Statement st = null;


		CreateTableStatement st_crta =null;
		AlterTableStatement st_alta =null;
		CreateTriggerStatement st_crtr =null;
		DropTriggerStatement st_drtr =null;
		StopProcessStatement st_stpr =null;
		UpdateTableStatement st_pdtb =null;
		SelectStatement st_slct =null;
		InsertIntoStatement st_nsnt =null;
		ExplainPlanStatement st_xppl =null;
		SetOptionsStatement st_stpt =null;
		UseStatement st_usks =null;
		DropKeyspaceStatement st_drks =null;
		CreateKeyspaceStatement st_crks =null;
		AlterKeyspaceStatement st_alks =null;
		DropTableStatement st_tbdr =null;
		TruncateStatement st_trst =null;
		CreateIndexStatement cis =null;
		DropIndexStatement dis =null;
		ListStatement ls =null;
		AddStatement add =null;
		RemoveUDFStatement rs =null;
		DeleteStatement ds =null;

		try {
			// Meta.g:586:37: (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_pdtb= updateTableStatement |st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement |ds= deleteStatement )
			int alt54=22;
			switch ( input.LA(1) ) {
			case T_CREATE:
				{
				switch ( input.LA(2) ) {
				case T_TABLE:
					{
					alt54=1;
					}
					break;
				case T_TRIGGER:
					{
					alt54=3;
					}
					break;
				case T_KEYSPACE:
					{
					alt54=13;
					}
					break;
				case T_INDEX_TYPE:
					{
					alt54=17;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 54, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_ALTER:
				{
				int LA54_2 = input.LA(2);
				if ( (LA54_2==T_TABLE) ) {
					alt54=2;
				}
				else if ( (LA54_2==T_KEYSPACE) ) {
					alt54=14;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 54, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case T_DROP:
				{
				switch ( input.LA(2) ) {
				case T_TRIGGER:
					{
					alt54=4;
					}
					break;
				case T_KEYSPACE:
					{
					alt54=12;
					}
					break;
				case T_TABLE:
					{
					alt54=15;
					}
					break;
				case T_INDEX:
					{
					alt54=18;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 54, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_STOP:
				{
				alt54=5;
				}
				break;
			case T_UPDATE:
				{
				alt54=6;
				}
				break;
			case T_SELECT:
				{
				alt54=7;
				}
				break;
			case T_INSERT:
				{
				alt54=8;
				}
				break;
			case T_EXPLAIN:
				{
				alt54=9;
				}
				break;
			case T_SET:
				{
				alt54=10;
				}
				break;
			case T_USE:
				{
				alt54=11;
				}
				break;
			case T_TRUNCATE:
				{
				alt54=16;
				}
				break;
			case T_LIST:
				{
				alt54=19;
				}
				break;
			case T_ADD:
				{
				alt54=20;
				}
				break;
			case T_REMOVE:
				{
				alt54=21;
				}
				break;
			case T_DELETE:
				{
				alt54=22;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 54, 0, input);
				throw nvae;
			}
			switch (alt54) {
				case 1 :
					// Meta.g:589:5: st_crta= createTableStatement
					{
					pushFollow(FOLLOW_createTableStatement_in_metaStatement4957);
					st_crta=createTableStatement();
					state._fsp--;

					 st = st_crta;
					}
					break;
				case 2 :
					// Meta.g:590:7: st_alta= alterTableStatement
					{
					pushFollow(FOLLOW_alterTableStatement_in_metaStatement4970);
					st_alta=alterTableStatement();
					state._fsp--;

					 st = st_alta;
					}
					break;
				case 3 :
					// Meta.g:591:7: st_crtr= createTriggerStatement
					{
					pushFollow(FOLLOW_createTriggerStatement_in_metaStatement4983);
					st_crtr=createTriggerStatement();
					state._fsp--;

					 st = st_crtr; 
					}
					break;
				case 4 :
					// Meta.g:592:7: st_drtr= dropTriggerStatement
					{
					pushFollow(FOLLOW_dropTriggerStatement_in_metaStatement4996);
					st_drtr=dropTriggerStatement();
					state._fsp--;

					 st = st_drtr; 
					}
					break;
				case 5 :
					// Meta.g:593:7: st_stpr= stopProcessStatement
					{
					pushFollow(FOLLOW_stopProcessStatement_in_metaStatement5010);
					st_stpr=stopProcessStatement();
					state._fsp--;

					 st = st_stpr; 
					}
					break;
				case 6 :
					// Meta.g:594:7: st_pdtb= updateTableStatement
					{
					pushFollow(FOLLOW_updateTableStatement_in_metaStatement5024);
					st_pdtb=updateTableStatement();
					state._fsp--;

					 st = st_pdtb; 
					}
					break;
				case 7 :
					// Meta.g:595:7: st_slct= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_metaStatement5038);
					st_slct=selectStatement();
					state._fsp--;

					 st = st_slct;
					}
					break;
				case 8 :
					// Meta.g:596:7: st_nsnt= insertIntoStatement
					{
					pushFollow(FOLLOW_insertIntoStatement_in_metaStatement5052);
					st_nsnt=insertIntoStatement();
					state._fsp--;

					 st = st_nsnt;
					}
					break;
				case 9 :
					// Meta.g:597:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement5066);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 10 :
					// Meta.g:598:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement5080);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 11 :
					// Meta.g:599:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement5094);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 12 :
					// Meta.g:600:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement5108);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 13 :
					// Meta.g:601:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement5122);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 14 :
					// Meta.g:602:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement5136);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 15 :
					// Meta.g:603:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement5150);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 16 :
					// Meta.g:604:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement5164);
					st_trst=truncateStatement();
					state._fsp--;

					 st = st_trst; 
					}
					break;
				case 17 :
					// Meta.g:605:7: cis= createIndexStatement
					{
					pushFollow(FOLLOW_createIndexStatement_in_metaStatement5178);
					cis=createIndexStatement();
					state._fsp--;

					 st = cis; 
					}
					break;
				case 18 :
					// Meta.g:606:7: dis= dropIndexStatement
					{
					pushFollow(FOLLOW_dropIndexStatement_in_metaStatement5193);
					dis=dropIndexStatement();
					state._fsp--;

					 st = dis; 
					}
					break;
				case 19 :
					// Meta.g:607:7: ls= listStatement
					{
					pushFollow(FOLLOW_listStatement_in_metaStatement5208);
					ls=listStatement();
					state._fsp--;

					 st = ls; 
					}
					break;
				case 20 :
					// Meta.g:608:7: add= addStatement
					{
					pushFollow(FOLLOW_addStatement_in_metaStatement5223);
					add=addStatement();
					state._fsp--;

					 st = add; 
					}
					break;
				case 21 :
					// Meta.g:609:7: rs= removeUDFStatement
					{
					pushFollow(FOLLOW_removeUDFStatement_in_metaStatement5238);
					rs=removeUDFStatement();
					state._fsp--;

					 st = rs; 
					}
					break;
				case 22 :
					// Meta.g:610:7: ds= deleteStatement
					{
					pushFollow(FOLLOW_deleteStatement_in_metaStatement5253);
					ds=deleteStatement();
					state._fsp--;

					 st = ds; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return st;
	}
	// $ANTLR end "metaStatement"



	// $ANTLR start "query"
	// Meta.g:613:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final Statement query() throws RecognitionException {
		Statement st = null;


		Statement mtst =null;

		try {
			// Meta.g:613:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:614:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query5276);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:614:21: ( T_SEMICOLON )+
			int cnt55=0;
			loop55:
			while (true) {
				int alt55=2;
				int LA55_0 = input.LA(1);
				if ( (LA55_0==T_SEMICOLON) ) {
					alt55=1;
				}

				switch (alt55) {
				case 1 :
					// Meta.g:614:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query5279); 
					}
					break;

				default :
					if ( cnt55 >= 1 ) break loop55;
					EarlyExitException eee = new EarlyExitException(55, input);
					throw eee;
				}
				cnt55++;
			}

			match(input,EOF,FOLLOW_EOF_in_query5283); 

					st = mtst;
				
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return st;
	}
	// $ANTLR end "query"



	// $ANTLR start "getOrdering"
	// Meta.g:621:1: getOrdering returns [List<Ordering> order] : ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )* ;
	public final List<Ordering> getOrdering() throws RecognitionException {
		List<Ordering> order = null;


		Token ident1=null;
		Token identN=null;


		        order = new ArrayList<>();
		        Ordering ordering;
		    
		try {
			// Meta.g:625:6: (ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )* )
			// Meta.g:626:5: ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )*
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOrdering5313); 
			ordering = new Ordering((ident1!=null?ident1.getText():null));
			// Meta.g:626:61: ( T_ASC | T_DESC )?
			int alt56=3;
			int LA56_0 = input.LA(1);
			if ( (LA56_0==T_ASC) ) {
				alt56=1;
			}
			else if ( (LA56_0==T_DESC) ) {
				alt56=2;
			}
			switch (alt56) {
				case 1 :
					// Meta.g:626:62: T_ASC
					{
					match(input,T_ASC,FOLLOW_T_ASC_in_getOrdering5318); 
					ordering.setOrderDir(OrderDirection.ASC);
					}
					break;
				case 2 :
					// Meta.g:626:114: T_DESC
					{
					match(input,T_DESC,FOLLOW_T_DESC_in_getOrdering5324); 
					ordering.setOrderDir(OrderDirection.DESC);
					}
					break;

			}

			order.add(ordering);
			// Meta.g:627:5: ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )*
			loop58:
			while (true) {
				int alt58=2;
				int LA58_0 = input.LA(1);
				if ( (LA58_0==T_COMMA) ) {
					alt58=1;
				}

				switch (alt58) {
				case 1 :
					// Meta.g:627:6: T_COMMA identN= T_IDENT ( T_ASC | T_DESC )?
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getOrdering5337); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOrdering5341); 
					ordering = new Ordering((identN!=null?identN.getText():null));
					// Meta.g:627:70: ( T_ASC | T_DESC )?
					int alt57=3;
					int LA57_0 = input.LA(1);
					if ( (LA57_0==T_ASC) ) {
						alt57=1;
					}
					else if ( (LA57_0==T_DESC) ) {
						alt57=2;
					}
					switch (alt57) {
						case 1 :
							// Meta.g:627:71: T_ASC
							{
							match(input,T_ASC,FOLLOW_T_ASC_in_getOrdering5346); 
							ordering.setOrderDir(OrderDirection.ASC);
							}
							break;
						case 2 :
							// Meta.g:627:123: T_DESC
							{
							match(input,T_DESC,FOLLOW_T_DESC_in_getOrdering5352); 
							ordering.setOrderDir(OrderDirection.DESC);
							}
							break;

					}

					order.add(ordering);
					}
					break;

				default :
					break loop58;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return order;
	}
	// $ANTLR end "getOrdering"



	// $ANTLR start "getWhereClauses"
	// Meta.g:630:1: getWhereClauses returns [List<MetaRelation> clauses] : rel1= getRelation ( T_AND relN= getRelation )* ;
	public final List<MetaRelation> getWhereClauses() throws RecognitionException {
		List<MetaRelation> clauses = null;


		MetaRelation rel1 =null;
		MetaRelation relN =null;


		        clauses = new ArrayList<>();
		    
		try {
			// Meta.g:633:6: (rel1= getRelation ( T_AND relN= getRelation )* )
			// Meta.g:634:5: rel1= getRelation ( T_AND relN= getRelation )*
			{
			pushFollow(FOLLOW_getRelation_in_getWhereClauses5386);
			rel1=getRelation();
			state._fsp--;

			clauses.add(rel1);
			// Meta.g:634:43: ( T_AND relN= getRelation )*
			loop59:
			while (true) {
				int alt59=2;
				int LA59_0 = input.LA(1);
				if ( (LA59_0==T_AND) ) {
					alt59=1;
				}

				switch (alt59) {
				case 1 :
					// Meta.g:634:44: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_getWhereClauses5391); 
					pushFollow(FOLLOW_getRelation_in_getWhereClauses5395);
					relN=getRelation();
					state._fsp--;

					clauses.add(relN);
					}
					break;

				default :
					break loop59;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return clauses;
	}
	// $ANTLR end "getWhereClauses"



	// $ANTLR start "getFields"
	// Meta.g:637:1: getFields returns [Map<String, String> fields] : ident1L= T_IDENT T_EQUAL ident1R= T_IDENT (identNL= T_IDENT T_EQUAL identNR= T_IDENT )* ;
	public final Map<String, String> getFields() throws RecognitionException {
		Map<String, String> fields = null;


		Token ident1L=null;
		Token ident1R=null;
		Token identNL=null;
		Token identNR=null;


		        fields = new HashMap<>();
		    
		try {
			// Meta.g:640:6: (ident1L= T_IDENT T_EQUAL ident1R= T_IDENT (identNL= T_IDENT T_EQUAL identNR= T_IDENT )* )
			// Meta.g:641:5: ident1L= T_IDENT T_EQUAL ident1R= T_IDENT (identNL= T_IDENT T_EQUAL identNR= T_IDENT )*
			{
			ident1L=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getFields5425); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getFields5427); 
			ident1R=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getFields5431); 
			 fields.put((ident1L!=null?ident1L.getText():null), (ident1R!=null?ident1R.getText():null));
			// Meta.g:642:5: (identNL= T_IDENT T_EQUAL identNR= T_IDENT )*
			loop60:
			while (true) {
				int alt60=2;
				int LA60_0 = input.LA(1);
				if ( (LA60_0==T_IDENT) ) {
					alt60=1;
				}

				switch (alt60) {
				case 1 :
					// Meta.g:642:6: identNL= T_IDENT T_EQUAL identNR= T_IDENT
					{
					identNL=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getFields5442); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getFields5444); 
					identNR=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getFields5448); 
					 fields.put((identNL!=null?identNL.getText():null), (identNR!=null?identNR.getText():null));
					}
					break;

				default :
					break loop60;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return fields;
	}
	// $ANTLR end "getFields"



	// $ANTLR start "getWindow"
	// Meta.g:645:1: getWindow returns [WindowSelect ws] : ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) ) ;
	public final WindowSelect getWindow() throws RecognitionException {
		WindowSelect ws = null;


		Token cnstnt=null;
		TimeUnit unit =null;

		try {
			// Meta.g:645:36: ( ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) ) )
			// Meta.g:646:5: ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) )
			{
			// Meta.g:646:5: ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) )
			int alt62=2;
			int LA62_0 = input.LA(1);
			if ( (LA62_0==T_LAST) ) {
				alt62=1;
			}
			else if ( (LA62_0==T_CONSTANT) ) {
				alt62=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 62, 0, input);
				throw nvae;
			}

			switch (alt62) {
				case 1 :
					// Meta.g:646:6: T_LAST
					{
					match(input,T_LAST,FOLLOW_T_LAST_in_getWindow5470); 
					ws = new WindowLast();
					}
					break;
				case 2 :
					// Meta.g:647:7: cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit )
					{
					cnstnt=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getWindow5483); 
					// Meta.g:647:25: ( T_ROWS |unit= getTimeUnit )
					int alt61=2;
					int LA61_0 = input.LA(1);
					if ( (LA61_0==T_ROWS) ) {
						alt61=1;
					}
					else if ( ((LA61_0 >= 140 && LA61_0 <= 142)||LA61_0==144||(LA61_0 >= 147 && LA61_0 <= 150)) ) {
						alt61=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 61, 0, input);
						throw nvae;
					}

					switch (alt61) {
						case 1 :
							// Meta.g:647:26: T_ROWS
							{
							match(input,T_ROWS,FOLLOW_T_ROWS_in_getWindow5486); 
							ws = new WindowRows(Integer.parseInt((cnstnt!=null?cnstnt.getText():null)));
							}
							break;
						case 2 :
							// Meta.g:648:26: unit= getTimeUnit
							{
							pushFollow(FOLLOW_getTimeUnit_in_getWindow5518);
							unit=getTimeUnit();
							state._fsp--;

							ws = new WindowTime(Integer.parseInt((cnstnt!=null?cnstnt.getText():null)), unit);
							}
							break;

					}

					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return ws;
	}
	// $ANTLR end "getWindow"



	// $ANTLR start "getTimeUnit"
	// Meta.g:653:1: getTimeUnit returns [TimeUnit unit] : ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' ) ;
	public final TimeUnit getTimeUnit() throws RecognitionException {
		TimeUnit unit = null;


		try {
			// Meta.g:653:36: ( ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' ) )
			// Meta.g:654:5: ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' )
			{
			// Meta.g:654:5: ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' )
			int alt63=8;
			switch ( input.LA(1) ) {
			case 144:
				{
				alt63=1;
				}
				break;
			case 142:
				{
				alt63=2;
				}
				break;
			case 141:
				{
				alt63=3;
				}
				break;
			case 140:
				{
				alt63=4;
				}
				break;
			case 150:
				{
				alt63=5;
				}
				break;
			case 149:
				{
				alt63=6;
				}
				break;
			case 148:
				{
				alt63=7;
				}
				break;
			case 147:
				{
				alt63=8;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 63, 0, input);
				throw nvae;
			}
			switch (alt63) {
				case 1 :
					// Meta.g:654:7: 'S'
					{
					match(input,144,FOLLOW_144_in_getTimeUnit5569); 
					unit =TimeUnit.S;
					}
					break;
				case 2 :
					// Meta.g:655:7: 'M'
					{
					match(input,142,FOLLOW_142_in_getTimeUnit5579); 
					unit =TimeUnit.M;
					}
					break;
				case 3 :
					// Meta.g:656:7: 'H'
					{
					match(input,141,FOLLOW_141_in_getTimeUnit5589); 
					unit =TimeUnit.H;
					}
					break;
				case 4 :
					// Meta.g:657:7: 'D'
					{
					match(input,140,FOLLOW_140_in_getTimeUnit5599); 
					unit =TimeUnit.D;
					}
					break;
				case 5 :
					// Meta.g:658:7: 's'
					{
					match(input,150,FOLLOW_150_in_getTimeUnit5609); 
					unit =TimeUnit.s;
					}
					break;
				case 6 :
					// Meta.g:659:7: 'm'
					{
					match(input,149,FOLLOW_149_in_getTimeUnit5619); 
					unit =TimeUnit.m;
					}
					break;
				case 7 :
					// Meta.g:660:7: 'h'
					{
					match(input,148,FOLLOW_148_in_getTimeUnit5629); 
					unit =TimeUnit.h;
					}
					break;
				case 8 :
					// Meta.g:661:7: 'd'
					{
					match(input,147,FOLLOW_147_in_getTimeUnit5639); 
					unit =TimeUnit.d;
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return unit;
	}
	// $ANTLR end "getTimeUnit"



	// $ANTLR start "getSelectClause"
	// Meta.g:664:1: getSelectClause returns [SelectionClause sc] : (scc= getSelectionCount |scl= getSelectionList );
	public final SelectionClause getSelectClause() throws RecognitionException {
		SelectionClause sc = null;


		SelectionCount scc =null;
		SelectionList scl =null;

		try {
			// Meta.g:664:45: (scc= getSelectionCount |scl= getSelectionList )
			int alt64=2;
			int LA64_0 = input.LA(1);
			if ( (LA64_0==T_COUNT) ) {
				int LA64_1 = input.LA(2);
				if ( (LA64_1==T_START_PARENTHESIS) ) {
					int LA64_3 = input.LA(3);
					if ( (LA64_3==T_ASTERISK||LA64_3==139) ) {
						alt64=1;
					}
					else if ( (LA64_3==T_AGGREGATION||LA64_3==T_AVG||LA64_3==T_COUNT||LA64_3==T_END_PARENTHESIS||LA64_3==T_IDENT||(LA64_3 >= T_MAX && LA64_3 <= T_MIN)) ) {
						alt64=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 64, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 64, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA64_0==T_AGGREGATION||(LA64_0 >= T_ASTERISK && LA64_0 <= T_AVG)||LA64_0==T_DISTINCT||LA64_0==T_IDENT||(LA64_0 >= T_MAX && LA64_0 <= T_MIN)) ) {
				alt64=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 64, 0, input);
				throw nvae;
			}

			switch (alt64) {
				case 1 :
					// Meta.g:665:5: scc= getSelectionCount
					{
					pushFollow(FOLLOW_getSelectionCount_in_getSelectClause5661);
					scc=getSelectionCount();
					state._fsp--;

					sc = scc;
					}
					break;
				case 2 :
					// Meta.g:666:7: scl= getSelectionList
					{
					pushFollow(FOLLOW_getSelectionList_in_getSelectClause5673);
					scl=getSelectionList();
					state._fsp--;

					sc = scl;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return sc;
	}
	// $ANTLR end "getSelectClause"



	// $ANTLR start "getSelectionCount"
	// Meta.g:669:1: getSelectionCount returns [SelectionCount scc] : T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )? ;
	public final SelectionCount getSelectionCount() throws RecognitionException {
		SelectionCount scc = null;


		Token ident=null;


		        boolean identInc = false;
		        char symbol = '*';
		    
		try {
			// Meta.g:673:6: ( T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )? )
			// Meta.g:674:5: T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )?
			{
			match(input,T_COUNT,FOLLOW_T_COUNT_in_getSelectionCount5699); 
			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelectionCount5701); 
			// Meta.g:674:33: ( T_ASTERISK | '1' )
			int alt65=2;
			int LA65_0 = input.LA(1);
			if ( (LA65_0==T_ASTERISK) ) {
				alt65=1;
			}
			else if ( (LA65_0==139) ) {
				alt65=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 65, 0, input);
				throw nvae;
			}

			switch (alt65) {
				case 1 :
					// Meta.g:674:35: T_ASTERISK
					{
					match(input,T_ASTERISK,FOLLOW_T_ASTERISK_in_getSelectionCount5705); 
					}
					break;
				case 2 :
					// Meta.g:674:48: '1'
					{
					match(input,139,FOLLOW_139_in_getSelectionCount5709); 
					symbol = '1';
					}
					break;

			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelectionCount5715); 
			// Meta.g:675:5: ( T_AS ident= T_IDENT )?
			int alt66=2;
			int LA66_0 = input.LA(1);
			if ( (LA66_0==T_AS) ) {
				alt66=1;
			}
			switch (alt66) {
				case 1 :
					// Meta.g:675:6: T_AS ident= T_IDENT
					{
					match(input,T_AS,FOLLOW_T_AS_in_getSelectionCount5722); 
					identInc = true;
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelectionCount5728); 
					}
					break;

			}


			        if(identInc)
			            scc = new SelectionCount(symbol, identInc, (ident!=null?ident.getText():null));
			        else
			            scc = new SelectionCount(symbol);
			    
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return scc;
	}
	// $ANTLR end "getSelectionCount"



	// $ANTLR start "getSelectionList"
	// Meta.g:684:1: getSelectionList returns [SelectionList scl] : ( T_DISTINCT )? selections= getSelection ;
	public final SelectionList getSelectionList() throws RecognitionException {
		SelectionList scl = null;


		Selection selections =null;


		        boolean distinct = false;
		    
		try {
			// Meta.g:687:6: ( ( T_DISTINCT )? selections= getSelection )
			// Meta.g:688:5: ( T_DISTINCT )? selections= getSelection
			{
			// Meta.g:688:5: ( T_DISTINCT )?
			int alt67=2;
			int LA67_0 = input.LA(1);
			if ( (LA67_0==T_DISTINCT) ) {
				alt67=1;
			}
			switch (alt67) {
				case 1 :
					// Meta.g:688:6: T_DISTINCT
					{
					match(input,T_DISTINCT,FOLLOW_T_DISTINCT_in_getSelectionList5763); 
					distinct = true;
					}
					break;

			}

			pushFollow(FOLLOW_getSelection_in_getSelectionList5771);
			selections=getSelection();
			state._fsp--;

			 scl = new SelectionList(distinct, selections);
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return scl;
	}
	// $ANTLR end "getSelectionList"



	// $ANTLR start "getSelection"
	// Meta.g:692:1: getSelection returns [Selection slct] : ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* ) ;
	public final Selection getSelection() throws RecognitionException {
		Selection slct = null;


		Token ident1=null;
		Token identN=null;
		SelectorMeta selector1 =null;
		SelectorMeta selectorN =null;


		        SelectionSelector slsl;
		        List<SelectionSelector> selections = new ArrayList<>();
		    
		try {
			// Meta.g:696:6: ( ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* ) )
			// Meta.g:697:5: ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* )
			{
			// Meta.g:697:5: ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==T_ASTERISK) ) {
				alt71=1;
			}
			else if ( (LA71_0==T_AGGREGATION||LA71_0==T_AVG||LA71_0==T_COUNT||LA71_0==T_IDENT||(LA71_0 >= T_MAX && LA71_0 <= T_MIN)) ) {
				alt71=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 71, 0, input);
				throw nvae;
			}

			switch (alt71) {
				case 1 :
					// Meta.g:698:9: T_ASTERISK
					{
					match(input,T_ASTERISK,FOLLOW_T_ASTERISK_in_getSelection5811); 
					 slct = new SelectionAsterisk();
					}
					break;
				case 2 :
					// Meta.g:699:11: selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )*
					{
					pushFollow(FOLLOW_getSelector_in_getSelection5834);
					selector1=getSelector();
					state._fsp--;

					 slsl = new SelectionSelector(selector1);
					// Meta.g:699:77: ( T_AS ident1= T_IDENT )?
					int alt68=2;
					int LA68_0 = input.LA(1);
					if ( (LA68_0==T_AS) ) {
						alt68=1;
					}
					switch (alt68) {
						case 1 :
							// Meta.g:699:78: T_AS ident1= T_IDENT
							{
							match(input,T_AS,FOLLOW_T_AS_in_getSelection5839); 
							ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelection5843); 
							slsl.setIdentifier((ident1!=null?ident1.getText():null));
							}
							break;

					}

					selections.add(slsl);
					// Meta.g:700:13: ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )*
					loop70:
					while (true) {
						int alt70=2;
						int LA70_0 = input.LA(1);
						if ( (LA70_0==T_COMMA) ) {
							alt70=1;
						}

						switch (alt70) {
						case 1 :
							// Meta.g:700:14: T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )?
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelection5864); 
							pushFollow(FOLLOW_getSelector_in_getSelection5868);
							selectorN=getSelector();
							state._fsp--;

							slsl = new SelectionSelector(selectorN);
							// Meta.g:700:87: ( T_AS identN= T_IDENT )?
							int alt69=2;
							int LA69_0 = input.LA(1);
							if ( (LA69_0==T_AS) ) {
								alt69=1;
							}
							switch (alt69) {
								case 1 :
									// Meta.g:700:88: T_AS identN= T_IDENT
									{
									match(input,T_AS,FOLLOW_T_AS_in_getSelection5873); 
									identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelection5877); 
									slsl.setIdentifier((identN!=null?identN.getText():null));
									}
									break;

							}

							selections.add(slsl);
							}
							break;

						default :
							break loop70;
						}
					}

					 slct = new SelectionSelectors(selections);
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return slct;
	}
	// $ANTLR end "getSelection"



	// $ANTLR start "getSelector"
	// Meta.g:705:1: getSelector returns [SelectorMeta slmt] : ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |ident= T_IDENT (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) ) ;
	public final SelectorMeta getSelector() throws RecognitionException {
		SelectorMeta slmt = null;


		Token ident=null;
		SelectorMeta select1 =null;
		SelectorMeta selectN =null;


		        List<SelectorMeta> params = new ArrayList<>();
		        GroupByFunction gbFunc = null;
		    
		try {
			// Meta.g:709:6: ( ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |ident= T_IDENT (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) ) )
			// Meta.g:710:5: ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |ident= T_IDENT (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) )
			{
			// Meta.g:710:5: ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |ident= T_IDENT (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) )
			int alt78=2;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==T_AGGREGATION||LA78_0==T_AVG||LA78_0==T_COUNT||(LA78_0 >= T_MAX && LA78_0 <= T_MIN)) ) {
				alt78=1;
			}
			else if ( (LA78_0==T_IDENT) ) {
				alt78=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 78, 0, input);
				throw nvae;
			}

			switch (alt78) {
				case 1 :
					// Meta.g:710:7: ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS
					{
					// Meta.g:710:7: ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT )
					int alt72=5;
					switch ( input.LA(1) ) {
					case T_AGGREGATION:
						{
						alt72=1;
						}
						break;
					case T_MAX:
						{
						alt72=2;
						}
						break;
					case T_MIN:
						{
						alt72=3;
						}
						break;
					case T_AVG:
						{
						alt72=4;
						}
						break;
					case T_COUNT:
						{
						alt72=5;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 72, 0, input);
						throw nvae;
					}
					switch (alt72) {
						case 1 :
							// Meta.g:710:8: T_AGGREGATION
							{
							match(input,T_AGGREGATION,FOLLOW_T_AGGREGATION_in_getSelector5932); 
							gbFunc = GroupByFunction.aggregation;
							}
							break;
						case 2 :
							// Meta.g:711:10: T_MAX
							{
							match(input,T_MAX,FOLLOW_T_MAX_in_getSelector5946); 
							gbFunc = GroupByFunction.max;
							}
							break;
						case 3 :
							// Meta.g:712:10: T_MIN
							{
							match(input,T_MIN,FOLLOW_T_MIN_in_getSelector5960); 
							gbFunc = GroupByFunction.min;
							}
							break;
						case 4 :
							// Meta.g:713:10: T_AVG
							{
							match(input,T_AVG,FOLLOW_T_AVG_in_getSelector5974); 
							gbFunc = GroupByFunction.avg;
							}
							break;
						case 5 :
							// Meta.g:714:10: T_COUNT
							{
							match(input,T_COUNT,FOLLOW_T_COUNT_in_getSelector5988); 
							gbFunc = GroupByFunction.count;
							}
							break;

					}

					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelector6014); 
					// Meta.g:717:17: (select1= getSelector ( T_COMMA selectN= getSelector )* )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==T_AGGREGATION||LA74_0==T_AVG||LA74_0==T_COUNT||LA74_0==T_IDENT||(LA74_0 >= T_MAX && LA74_0 <= T_MIN)) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// Meta.g:717:18: select1= getSelector ( T_COMMA selectN= getSelector )*
							{
							pushFollow(FOLLOW_getSelector_in_getSelector6036);
							select1=getSelector();
							state._fsp--;

							params.add(select1);
							// Meta.g:717:61: ( T_COMMA selectN= getSelector )*
							loop73:
							while (true) {
								int alt73=2;
								int LA73_0 = input.LA(1);
								if ( (LA73_0==T_COMMA) ) {
									alt73=1;
								}

								switch (alt73) {
								case 1 :
									// Meta.g:717:62: T_COMMA selectN= getSelector
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelector6041); 
									pushFollow(FOLLOW_getSelector_in_getSelector6045);
									selectN=getSelector();
									state._fsp--;

									params.add(selectN);
									}
									break;

								default :
									break loop73;
								}
							}

							}
							break;

					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelector6066); 
					slmt = new SelectorGroupBy(gbFunc, params);
					}
					break;
				case 2 :
					// Meta.g:719:11: ident= T_IDENT (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS )
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelector6082); 
					// Meta.g:719:25: (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS )
					int alt77=2;
					int LA77_0 = input.LA(1);
					if ( (LA77_0==T_AS||LA77_0==T_COMMA||LA77_0==T_END_PARENTHESIS||LA77_0==T_FROM) ) {
						alt77=1;
					}
					else if ( (LA77_0==T_START_PARENTHESIS) ) {
						alt77=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 77, 0, input);
						throw nvae;
					}

					switch (alt77) {
						case 1 :
							// Meta.g:720:13: 
							{
							slmt = new SelectorIdentifier((ident!=null?ident.getText():null));
							}
							break;
						case 2 :
							// Meta.g:721:15: T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS
							{
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelector6114); 
							// Meta.g:721:35: (select1= getSelector ( T_COMMA selectN= getSelector )* )?
							int alt76=2;
							int LA76_0 = input.LA(1);
							if ( (LA76_0==T_AGGREGATION||LA76_0==T_AVG||LA76_0==T_COUNT||LA76_0==T_IDENT||(LA76_0 >= T_MAX && LA76_0 <= T_MIN)) ) {
								alt76=1;
							}
							switch (alt76) {
								case 1 :
									// Meta.g:721:36: select1= getSelector ( T_COMMA selectN= getSelector )*
									{
									pushFollow(FOLLOW_getSelector_in_getSelector6119);
									select1=getSelector();
									state._fsp--;

									params.add(select1);
									// Meta.g:721:79: ( T_COMMA selectN= getSelector )*
									loop75:
									while (true) {
										int alt75=2;
										int LA75_0 = input.LA(1);
										if ( (LA75_0==T_COMMA) ) {
											alt75=1;
										}

										switch (alt75) {
										case 1 :
											// Meta.g:721:80: T_COMMA selectN= getSelector
											{
											match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelector6124); 
											pushFollow(FOLLOW_getSelector_in_getSelector6128);
											selectN=getSelector();
											state._fsp--;

											params.add(selectN);
											}
											break;

										default :
											break loop75;
										}
									}

									}
									break;

							}

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelector6153); 
							slmt = new SelectorFunction((ident!=null?ident.getText():null), params);
							}
							break;

					}

					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return slmt;
	}
	// $ANTLR end "getSelector"


	public static class getListTypes_return extends ParserRuleReturnScope {
		public String listType;
	};


	// $ANTLR start "getListTypes"
	// Meta.g:727:1: getListTypes returns [String listType] : ident= ( 'PROCESS' | 'UDF' | 'TRIGGER' ) ;
	public final MetaParser.getListTypes_return getListTypes() throws RecognitionException {
		MetaParser.getListTypes_return retval = new MetaParser.getListTypes_return();
		retval.start = input.LT(1);

		Token ident=null;

		try {
			// Meta.g:727:39: (ident= ( 'PROCESS' | 'UDF' | 'TRIGGER' ) )
			// Meta.g:728:2: ident= ( 'PROCESS' | 'UDF' | 'TRIGGER' )
			{
			ident=input.LT(1);
			if ( input.LA(1)==143||(input.LA(1) >= 145 && input.LA(1) <= 146) ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			retval.listType = new String((ident!=null?ident.getText():null));
			}

			retval.stop = input.LT(-1);

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "getListTypes"



	// $ANTLR start "getAssignment"
	// Meta.g:731:1: getAssignment returns [Assignment assign] : ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm ) ;
	public final Assignment getAssignment() throws RecognitionException {
		Assignment assign = null;


		Token ident=null;
		ValueAssignment value =null;
		String termL =null;
		String termR =null;

		try {
			// Meta.g:731:42: (ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm ) )
			// Meta.g:732:5: ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm )
			{
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getAssignment6217); 
			// Meta.g:732:19: ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm )
			int alt79=2;
			int LA79_0 = input.LA(1);
			if ( (LA79_0==T_EQUAL) ) {
				alt79=1;
			}
			else if ( (LA79_0==T_START_BRACKET) ) {
				alt79=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 79, 0, input);
				throw nvae;
			}

			switch (alt79) {
				case 1 :
					// Meta.g:733:9: T_EQUAL value= getValueAssign
					{
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getAssignment6229); 
					pushFollow(FOLLOW_getValueAssign_in_getAssignment6233);
					value=getValueAssign();
					state._fsp--;

					assign = new Assignment(new IdentifierAssignment((ident!=null?ident.getText():null)), value);
					}
					break;
				case 2 :
					// Meta.g:735:9: T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm
					{
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_getAssignment6252); 
					pushFollow(FOLLOW_getTerm_in_getAssignment6256);
					termL=getTerm();
					state._fsp--;

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_getAssignment6258); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getAssignment6260); 
					pushFollow(FOLLOW_getTerm_in_getAssignment6264);
					termR=getTerm();
					state._fsp--;

					 
					            assign = new Assignment (new IdentifierAssignment((ident!=null?ident.getText():null), new Term(termL)), new ValueAssignment(new Term(termR)));
					        
					}
					break;

			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return assign;
	}
	// $ANTLR end "getAssignment"



	// $ANTLR start "getValueAssign"
	// Meta.g:741:1: getValueAssign returns [ValueAssignment valueAssign] : (term1= getTerm |ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList ) );
	public final ValueAssignment getValueAssign() throws RecognitionException {
		ValueAssignment valueAssign = null;


		Token ident=null;
		String term1 =null;
		Map<String, String> mapLiteral =null;
		IdentIntOrLiteral value1 =null;
		IdentIntOrLiteral value2 =null;

		try {
			// Meta.g:741:53: (term1= getTerm |ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList ) )
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==T_IDENT) ) {
				int LA82_1 = input.LA(2);
				if ( (LA82_1==T_COMMA||LA82_1==T_WHERE) ) {
					alt82=1;
				}
				else if ( (LA82_1==T_PLUS||LA82_1==T_SUBTRACT) ) {
					alt82=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 82, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA82_0==T_KS_AND_TN||LA82_0==T_TERM) ) {
				alt82=1;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 82, 0, input);
				throw nvae;
			}

			switch (alt82) {
				case 1 :
					// Meta.g:742:5: term1= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getValueAssign6291);
					term1=getTerm();
					state._fsp--;

					 valueAssign = new ValueAssignment(new Term(term1));
					}
					break;
				case 2 :
					// Meta.g:743:7: ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList )
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueAssign6303); 
					// Meta.g:743:21: ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList )
					int alt81=2;
					int LA81_0 = input.LA(1);
					if ( (LA81_0==T_PLUS) ) {
						alt81=1;
					}
					else if ( (LA81_0==T_SUBTRACT) ) {
						alt81=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 81, 0, input);
						throw nvae;
					}

					switch (alt81) {
						case 1 :
							// Meta.g:743:22: T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList )
							{
							match(input,T_PLUS,FOLLOW_T_PLUS_in_getValueAssign6306); 
							// Meta.g:743:29: ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList )
							int alt80=2;
							int LA80_0 = input.LA(1);
							if ( (LA80_0==T_START_SBRACKET) ) {
								int LA80_1 = input.LA(2);
								if ( (LA80_1==T_START_SBRACKET) ) {
									alt80=1;
								}
								else if ( (LA80_1==T_IDENT||LA80_1==T_KS_AND_TN||LA80_1==T_TERM) ) {
									alt80=2;
								}

								else {
									int nvaeMark = input.mark();
									try {
										input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 80, 1, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

							}
							else if ( (LA80_0==T_CONSTANT||LA80_0==T_START_BRACKET) ) {
								alt80=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 80, 0, input);
								throw nvae;
							}

							switch (alt80) {
								case 1 :
									// Meta.g:743:30: T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET
									{
									match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getValueAssign6309); 
									pushFollow(FOLLOW_getMapLiteral_in_getValueAssign6313);
									mapLiteral=getMapLiteral();
									state._fsp--;

									match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getValueAssign6315); 
									 valueAssign = new ValueAssignment(new IdentMap((ident!=null?ident.getText():null), new MapLiteralProperty(mapLiteral)));
									}
									break;
								case 2 :
									// Meta.g:744:35: value1= getIntSetOrList
									{
									pushFollow(FOLLOW_getIntSetOrList_in_getValueAssign6355);
									value1=getIntSetOrList();
									state._fsp--;

									 
									                                                            if(value1 instanceof IntTerm)
									                                                                valueAssign = new ValueAssignment(new IntTerm((ident!=null?ident.getText():null), '+', ((IntTerm) value1).getTerm()));
									                                                            else if(value1 instanceof ListLiteral)
									                                                                valueAssign = new ValueAssignment(new ListLiteral((ident!=null?ident.getText():null), '+', ((ListLiteral) value1).getLiterals()));
									                                                            else
									                                                                valueAssign = new ValueAssignment(new SetLiteral((ident!=null?ident.getText():null), '+', ((SetLiteral) value1).getLiterals()));
									                                                         
									}
									break;

							}

							}
							break;
						case 2 :
							// Meta.g:753:11: T_SUBTRACT value2= getIntSetOrList
							{
							match(input,T_SUBTRACT,FOLLOW_T_SUBTRACT_in_getValueAssign6399); 
							pushFollow(FOLLOW_getIntSetOrList_in_getValueAssign6403);
							value2=getIntSetOrList();
							state._fsp--;

							 
							                                                if(value2 instanceof IntTerm)
							                                                    valueAssign = new ValueAssignment(new IntTerm((ident!=null?ident.getText():null), '-', ((IntTerm) value2).getTerm()));
							                                                else if(value2 instanceof ListLiteral)
							                                                    valueAssign = new ValueAssignment(new ListLiteral((ident!=null?ident.getText():null), '-', ((ListLiteral) value2).getLiterals()));
							                                                else
							                                                    valueAssign = new ValueAssignment(new SetLiteral((ident!=null?ident.getText():null), '-', ((SetLiteral) value2).getLiterals()));
							                                            
							}
							break;

					}

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return valueAssign;
	}
	// $ANTLR end "getValueAssign"



	// $ANTLR start "getIntSetOrList"
	// Meta.g:764:1: getIntSetOrList returns [IdentIntOrLiteral iiol] : (constant= T_CONSTANT | T_START_BRACKET list= getList T_END_BRACKET | T_START_SBRACKET set= getSet T_END_SBRACKET );
	public final IdentIntOrLiteral getIntSetOrList() throws RecognitionException {
		IdentIntOrLiteral iiol = null;


		Token constant=null;
		List list =null;
		Set set =null;

		try {
			// Meta.g:764:49: (constant= T_CONSTANT | T_START_BRACKET list= getList T_END_BRACKET | T_START_SBRACKET set= getSet T_END_SBRACKET )
			int alt83=3;
			switch ( input.LA(1) ) {
			case T_CONSTANT:
				{
				alt83=1;
				}
				break;
			case T_START_BRACKET:
				{
				alt83=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt83=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 83, 0, input);
				throw nvae;
			}
			switch (alt83) {
				case 1 :
					// Meta.g:765:5: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getIntSetOrList6429); 
					 iiol = new IntTerm(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 2 :
					// Meta.g:766:7: T_START_BRACKET list= getList T_END_BRACKET
					{
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_getIntSetOrList6439); 
					pushFollow(FOLLOW_getList_in_getIntSetOrList6443);
					list=getList();
					state._fsp--;

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_getIntSetOrList6445); 
					 iiol = new ListLiteral(list);
					}
					break;
				case 3 :
					// Meta.g:767:7: T_START_SBRACKET set= getSet T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getIntSetOrList6455); 
					pushFollow(FOLLOW_getSet_in_getIntSetOrList6459);
					set=getSet();
					state._fsp--;

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getIntSetOrList6461); 
					 iiol = new SetLiteral(set);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return iiol;
	}
	// $ANTLR end "getIntSetOrList"



	// $ANTLR start "getRelation"
	// Meta.g:770:1: getRelation returns [MetaRelation mrel] : ( T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator term= getTerm |ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm ) );
	public final MetaRelation getRelation() throws RecognitionException {
		MetaRelation mrel = null;


		Token ident=null;
		List<String> listIds =null;
		String operator =null;
		String term =null;
		String compSymbol =null;
		String termR =null;
		List terms =null;
		String term1 =null;
		String term2 =null;

		try {
			// Meta.g:770:40: ( T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator term= getTerm |ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm ) )
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==T_TOKEN) ) {
				alt85=1;
			}
			else if ( (LA85_0==T_IDENT) ) {
				alt85=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 85, 0, input);
				throw nvae;
			}

			switch (alt85) {
				case 1 :
					// Meta.g:771:5: T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator term= getTerm
					{
					match(input,T_TOKEN,FOLLOW_T_TOKEN_in_getRelation6479); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation6481); 
					pushFollow(FOLLOW_getIds_in_getRelation6485);
					listIds=getIds();
					state._fsp--;

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation6487); 
					pushFollow(FOLLOW_getComparator_in_getRelation6491);
					operator=getComparator();
					state._fsp--;

					pushFollow(FOLLOW_getTerm_in_getRelation6495);
					term=getTerm();
					state._fsp--;

					mrel = new RelationToken(listIds, operator, new Term(term));
					}
					break;
				case 2 :
					// Meta.g:772:7: ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm )
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getRelation6507); 
					// Meta.g:772:21: (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm )
					int alt84=3;
					switch ( input.LA(1) ) {
					case T_EQUAL:
					case T_GET:
					case T_GT:
					case T_LET:
					case T_LIKE:
					case T_LT:
					case T_NOT_EQUAL:
						{
						alt84=1;
						}
						break;
					case T_IN:
						{
						alt84=2;
						}
						break;
					case T_BETWEEN:
						{
						alt84=3;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 84, 0, input);
						throw nvae;
					}
					switch (alt84) {
						case 1 :
							// Meta.g:772:23: compSymbol= getComparator termR= getTerm
							{
							pushFollow(FOLLOW_getComparator_in_getRelation6513);
							compSymbol=getComparator();
							state._fsp--;

							pushFollow(FOLLOW_getTerm_in_getRelation6517);
							termR=getTerm();
							state._fsp--;

							mrel = new RelationCompare((ident!=null?ident.getText():null), compSymbol, new Term(termR));
							}
							break;
						case 2 :
							// Meta.g:773:23: T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS
							{
							match(input,T_IN,FOLLOW_T_IN_in_getRelation6543); 
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation6545); 
							pushFollow(FOLLOW_getTerms_in_getRelation6549);
							terms=getTerms();
							state._fsp--;

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation6551); 
							mrel = new RelationIn((ident!=null?ident.getText():null), terms);
							}
							break;
						case 3 :
							// Meta.g:774:23: T_BETWEEN term1= getTerm T_AND term2= getTerm
							{
							match(input,T_BETWEEN,FOLLOW_T_BETWEEN_in_getRelation6577); 
							pushFollow(FOLLOW_getTerm_in_getRelation6581);
							term1=getTerm();
							state._fsp--;

							match(input,T_AND,FOLLOW_T_AND_in_getRelation6583); 
							pushFollow(FOLLOW_getTerm_in_getRelation6587);
							term2=getTerm();
							state._fsp--;

							mrel = new RelationBetween((ident!=null?ident.getText():null), new Term(term1), new Term(term2));
							}
							break;

					}

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return mrel;
	}
	// $ANTLR end "getRelation"



	// $ANTLR start "getComparator"
	// Meta.g:778:1: getComparator returns [String comparator] : ( T_EQUAL | T_GT | T_LT | T_GET | T_LET | T_NOT_EQUAL | T_LIKE );
	public final String getComparator() throws RecognitionException {
		String comparator = null;


		try {
			// Meta.g:778:42: ( T_EQUAL | T_GT | T_LT | T_GET | T_LET | T_NOT_EQUAL | T_LIKE )
			int alt86=7;
			switch ( input.LA(1) ) {
			case T_EQUAL:
				{
				alt86=1;
				}
				break;
			case T_GT:
				{
				alt86=2;
				}
				break;
			case T_LT:
				{
				alt86=3;
				}
				break;
			case T_GET:
				{
				alt86=4;
				}
				break;
			case T_LET:
				{
				alt86=5;
				}
				break;
			case T_NOT_EQUAL:
				{
				alt86=6;
				}
				break;
			case T_LIKE:
				{
				alt86=7;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 86, 0, input);
				throw nvae;
			}
			switch (alt86) {
				case 1 :
					// Meta.g:779:5: T_EQUAL
					{
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getComparator6627); 
					comparator ="=";
					}
					break;
				case 2 :
					// Meta.g:780:7: T_GT
					{
					match(input,T_GT,FOLLOW_T_GT_in_getComparator6637); 
					comparator =">";
					}
					break;
				case 3 :
					// Meta.g:781:7: T_LT
					{
					match(input,T_LT,FOLLOW_T_LT_in_getComparator6647); 
					comparator ="<";
					}
					break;
				case 4 :
					// Meta.g:782:7: T_GET
					{
					match(input,T_GET,FOLLOW_T_GET_in_getComparator6657); 
					comparator =">=";
					}
					break;
				case 5 :
					// Meta.g:783:7: T_LET
					{
					match(input,T_LET,FOLLOW_T_LET_in_getComparator6668); 
					comparator ="<=";
					}
					break;
				case 6 :
					// Meta.g:784:7: T_NOT_EQUAL
					{
					match(input,T_NOT_EQUAL,FOLLOW_T_NOT_EQUAL_in_getComparator6678); 
					comparator ="<>";
					}
					break;
				case 7 :
					// Meta.g:785:7: T_LIKE
					{
					match(input,T_LIKE,FOLLOW_T_LIKE_in_getComparator6689); 
					comparator ="LIKE";
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return comparator;
	}
	// $ANTLR end "getComparator"



	// $ANTLR start "getIds"
	// Meta.g:788:1: getIds returns [List<String> listStrs] : ident1= T_IDENT ( T_COMMA identN= T_IDENT )* ;
	public final List<String> getIds() throws RecognitionException {
		List<String> listStrs = null;


		Token ident1=null;
		Token identN=null;


		        listStrs = new ArrayList<>();
		    
		try {
			// Meta.g:791:6: (ident1= T_IDENT ( T_COMMA identN= T_IDENT )* )
			// Meta.g:792:5: ident1= T_IDENT ( T_COMMA identN= T_IDENT )*
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getIds6717); 
			listStrs.add((ident1!=null?ident1.getText():null));
			// Meta.g:792:50: ( T_COMMA identN= T_IDENT )*
			loop87:
			while (true) {
				int alt87=2;
				int LA87_0 = input.LA(1);
				if ( (LA87_0==T_COMMA) ) {
					alt87=1;
				}

				switch (alt87) {
				case 1 :
					// Meta.g:792:51: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getIds6722); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getIds6726); 
					listStrs.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop87;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return listStrs;
	}
	// $ANTLR end "getIds"



	// $ANTLR start "getOptions"
	// Meta.g:795:1: getOptions returns [List<Option> opts] : opt1= getOption (optN= getOption )* ;
	public final List<Option> getOptions() throws RecognitionException {
		List<Option> opts = null;


		Option opt1 =null;
		Option optN =null;


		        opts = new ArrayList<>();
		    
		try {
			// Meta.g:797:6: (opt1= getOption (optN= getOption )* )
			// Meta.g:798:5: opt1= getOption (optN= getOption )*
			{
			pushFollow(FOLLOW_getOption_in_getOptions6751);
			opt1=getOption();
			state._fsp--;

			opts.add(opt1);
			// Meta.g:798:38: (optN= getOption )*
			loop88:
			while (true) {
				int alt88=2;
				int LA88_0 = input.LA(1);
				if ( (LA88_0==T_CLUSTERING||LA88_0==T_COMPACT||LA88_0==T_IDENT) ) {
					alt88=1;
				}

				switch (alt88) {
				case 1 :
					// Meta.g:798:39: optN= getOption
					{
					pushFollow(FOLLOW_getOption_in_getOptions6758);
					optN=getOption();
					state._fsp--;

					opts.add(optN);
					}
					break;

				default :
					break loop88;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return opts;
	}
	// $ANTLR end "getOptions"



	// $ANTLR start "getOption"
	// Meta.g:801:1: getOption returns [Option opt] : ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty );
	public final Option getOption() throws RecognitionException {
		Option opt = null;


		Token identProp=null;
		ValueProperty valueProp =null;

		try {
			// Meta.g:801:31: ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty )
			int alt89=3;
			switch ( input.LA(1) ) {
			case T_COMPACT:
				{
				alt89=1;
				}
				break;
			case T_CLUSTERING:
				{
				alt89=2;
				}
				break;
			case T_IDENT:
				{
				alt89=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 89, 0, input);
				throw nvae;
			}
			switch (alt89) {
				case 1 :
					// Meta.g:802:5: T_COMPACT T_STORAGE
					{
					match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getOption6778); 
					match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getOption6780); 
					opt =new Option(Option.OPTION_COMPACT);
					}
					break;
				case 2 :
					// Meta.g:803:7: T_CLUSTERING T_ORDER
					{
					match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getOption6790); 
					match(input,T_ORDER,FOLLOW_T_ORDER_in_getOption6792); 
					opt =new Option(Option.OPTION_CLUSTERING);
					}
					break;
				case 3 :
					// Meta.g:804:7: identProp= T_IDENT T_EQUAL valueProp= getValueProperty
					{
					identProp=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOption6804); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getOption6806); 
					pushFollow(FOLLOW_getValueProperty_in_getOption6810);
					valueProp=getValueProperty();
					state._fsp--;

					opt =new Option((identProp!=null?identProp.getText():null), valueProp);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return opt;
	}
	// $ANTLR end "getOption"



	// $ANTLR start "getList"
	// Meta.g:807:1: getList returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getList() throws RecognitionException {
		List list = null;


		String term1 =null;
		String termN =null;


		        list = new ArrayList<String>();
		    
		try {
			// Meta.g:810:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:811:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getList6838);
			term1=getTerm();
			state._fsp--;

			list.add(term1);
			// Meta.g:812:5: ( T_COMMA termN= getTerm )*
			loop90:
			while (true) {
				int alt90=2;
				int LA90_0 = input.LA(1);
				if ( (LA90_0==T_COMMA) ) {
					alt90=1;
				}

				switch (alt90) {
				case 1 :
					// Meta.g:812:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getList6847); 
					pushFollow(FOLLOW_getTerm_in_getList6851);
					termN=getTerm();
					state._fsp--;

					list.add(termN);
					}
					break;

				default :
					break loop90;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return list;
	}
	// $ANTLR end "getList"



	// $ANTLR start "getTerms"
	// Meta.g:815:1: getTerms returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getTerms() throws RecognitionException {
		List list = null;


		String term1 =null;
		String termN =null;


		        list = new ArrayList<Term>();
		    
		try {
			// Meta.g:818:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:819:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getTerms6885);
			term1=getTerm();
			state._fsp--;

			list.add(new Term(term1));
			// Meta.g:820:5: ( T_COMMA termN= getTerm )*
			loop91:
			while (true) {
				int alt91=2;
				int LA91_0 = input.LA(1);
				if ( (LA91_0==T_COMMA) ) {
					alt91=1;
				}

				switch (alt91) {
				case 1 :
					// Meta.g:820:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getTerms6894); 
					pushFollow(FOLLOW_getTerm_in_getTerms6898);
					termN=getTerm();
					state._fsp--;

					list.add(new Term(termN));
					}
					break;

				default :
					break loop91;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return list;
	}
	// $ANTLR end "getTerms"



	// $ANTLR start "getSet"
	// Meta.g:823:1: getSet returns [Set set] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final Set getSet() throws RecognitionException {
		Set set = null;


		String term1 =null;
		String termN =null;


		        set = new HashSet<String>();
		    
		try {
			// Meta.g:826:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:827:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getSet6932);
			term1=getTerm();
			state._fsp--;

			set.add(term1);
			// Meta.g:828:5: ( T_COMMA termN= getTerm )*
			loop92:
			while (true) {
				int alt92=2;
				int LA92_0 = input.LA(1);
				if ( (LA92_0==T_COMMA) ) {
					alt92=1;
				}

				switch (alt92) {
				case 1 :
					// Meta.g:828:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getSet6941); 
					pushFollow(FOLLOW_getTerm_in_getSet6945);
					termN=getTerm();
					state._fsp--;

					set.add(termN);
					}
					break;

				default :
					break loop92;
				}
			}

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return set;
	}
	// $ANTLR end "getSet"



	// $ANTLR start "getTermOrLiteral"
	// Meta.g:831:1: getTermOrLiteral returns [ValueCell vc] : (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET );
	public final ValueCell getTermOrLiteral() throws RecognitionException {
		ValueCell vc = null;


		String term =null;
		String term1 =null;
		String termN =null;


		        CollectionLiteral cl = new CollectionLiteral();
		    
		try {
			// Meta.g:834:6: (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET )
			int alt95=2;
			int LA95_0 = input.LA(1);
			if ( (LA95_0==T_IDENT||LA95_0==T_KS_AND_TN||LA95_0==T_TERM) ) {
				alt95=1;
			}
			else if ( (LA95_0==T_START_SBRACKET) ) {
				alt95=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 95, 0, input);
				throw nvae;
			}

			switch (alt95) {
				case 1 :
					// Meta.g:835:5: term= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getTermOrLiteral6979);
					term=getTerm();
					state._fsp--;

					vc =new Term(term);
					}
					break;
				case 2 :
					// Meta.g:837:5: T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getTermOrLiteral6993); 
					// Meta.g:838:5: (term1= getTerm ( T_COMMA termN= getTerm )* )?
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==T_IDENT||LA94_0==T_KS_AND_TN||LA94_0==T_TERM) ) {
						alt94=1;
					}
					switch (alt94) {
						case 1 :
							// Meta.g:839:9: term1= getTerm ( T_COMMA termN= getTerm )*
							{
							pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7011);
							term1=getTerm();
							state._fsp--;

							cl.addLiteral(new Term(term1));
							// Meta.g:840:9: ( T_COMMA termN= getTerm )*
							loop93:
							while (true) {
								int alt93=2;
								int LA93_0 = input.LA(1);
								if ( (LA93_0==T_COMMA) ) {
									alt93=1;
								}

								switch (alt93) {
								case 1 :
									// Meta.g:840:10: T_COMMA termN= getTerm
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getTermOrLiteral7024); 
									pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7028);
									termN=getTerm();
									state._fsp--;

									cl.addLiteral(new Term(termN));
									}
									break;

								default :
									break loop93;
								}
							}

							}
							break;

					}

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getTermOrLiteral7045); 
					vc =cl;
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return vc;
	}
	// $ANTLR end "getTermOrLiteral"



	// $ANTLR start "getTableID"
	// Meta.g:845:1: getTableID returns [String tableID] : (ident1= T_IDENT |ident2= T_KS_AND_TN );
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ident1=null;
		Token ident2=null;

		try {
			// Meta.g:845:36: (ident1= T_IDENT |ident2= T_KS_AND_TN )
			int alt96=2;
			int LA96_0 = input.LA(1);
			if ( (LA96_0==T_IDENT) ) {
				alt96=1;
			}
			else if ( (LA96_0==T_KS_AND_TN) ) {
				alt96=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 96, 0, input);
				throw nvae;
			}

			switch (alt96) {
				case 1 :
					// Meta.g:846:5: ident1= T_IDENT
					{
					ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID7066); 
					tableID = new String((ident1!=null?ident1.getText():null));
					}
					break;
				case 2 :
					// Meta.g:847:7: ident2= T_KS_AND_TN
					{
					ident2=(Token)match(input,T_KS_AND_TN,FOLLOW_T_KS_AND_TN_in_getTableID7082); 
					tableID = new String((ident2!=null?ident2.getText():null));
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return tableID;
	}
	// $ANTLR end "getTableID"



	// $ANTLR start "getTerm"
	// Meta.g:850:1: getTerm returns [String term] : (ident= T_IDENT |ksAndTn= T_KS_AND_TN |noIdent= T_TERM );
	public final String getTerm() throws RecognitionException {
		String term = null;


		Token ident=null;
		Token ksAndTn=null;
		Token noIdent=null;

		try {
			// Meta.g:850:30: (ident= T_IDENT |ksAndTn= T_KS_AND_TN |noIdent= T_TERM )
			int alt97=3;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt97=1;
				}
				break;
			case T_KS_AND_TN:
				{
				alt97=2;
				}
				break;
			case T_TERM:
				{
				alt97=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 97, 0, input);
				throw nvae;
			}
			switch (alt97) {
				case 1 :
					// Meta.g:851:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm7106); 
					term = (ident!=null?ident.getText():null);
					}
					break;
				case 2 :
					// Meta.g:852:7: ksAndTn= T_KS_AND_TN
					{
					ksAndTn=(Token)match(input,T_KS_AND_TN,FOLLOW_T_KS_AND_TN_in_getTerm7118); 
					term = (ksAndTn!=null?ksAndTn.getText():null);
					}
					break;
				case 3 :
					// Meta.g:853:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm7130); 
					term = (noIdent!=null?noIdent.getText():null);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return term;
	}
	// $ANTLR end "getTerm"



	// $ANTLR start "getMapLiteral"
	// Meta.g:856:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		String leftTerm1 =null;
		String rightTerm1 =null;
		String leftTermN =null;
		String rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:859:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:860:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral7160); 
			// Meta.g:861:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt99=2;
			int LA99_0 = input.LA(1);
			if ( (LA99_0==T_IDENT||LA99_0==T_KS_AND_TN||LA99_0==T_TERM) ) {
				alt99=1;
			}
			switch (alt99) {
				case 1 :
					// Meta.g:861:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral7170);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral7172); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral7176);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put(leftTerm1, rightTerm1);
					// Meta.g:862:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop98:
					while (true) {
						int alt98=2;
						int LA98_0 = input.LA(1);
						if ( (LA98_0==T_COMMA) ) {
							alt98=1;
						}

						switch (alt98) {
						case 1 :
							// Meta.g:862:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral7185); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral7189);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral7191); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral7195);
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put(leftTermN, rightTermN);
							}
							break;

						default :
							break loop98;
						}
					}

					}
					break;

			}

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral7207); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return mapTerms;
	}
	// $ANTLR end "getMapLiteral"



	// $ANTLR start "getValueProperty"
	// Meta.g:866:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat | T_FALSE | T_TRUE );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Map<String, String> mapliteral =null;
		String number =null;

		try {
			// Meta.g:866:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat | T_FALSE | T_TRUE )
			int alt100=6;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt100=1;
				}
				break;
			case T_CONSTANT:
				{
				alt100=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt100=3;
				}
				break;
			case T_FLOAT:
			case T_TERM:
				{
				alt100=4;
				}
				break;
			case T_FALSE:
				{
				alt100=5;
				}
				break;
			case T_TRUE:
				{
				alt100=6;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 100, 0, input);
				throw nvae;
			}
			switch (alt100) {
				case 1 :
					// Meta.g:867:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty7229); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:868:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty7241); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:869:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty7253);
					mapliteral=getMapLiteral();
					state._fsp--;

					value = new MapLiteralProperty(mapliteral);
					}
					break;
				case 4 :
					// Meta.g:870:7: number= getFloat
					{
					pushFollow(FOLLOW_getFloat_in_getValueProperty7265);
					number=getFloat();
					state._fsp--;

					value = new FloatProperty(Float.parseFloat(number));
					}
					break;
				case 5 :
					// Meta.g:871:7: T_FALSE
					{
					match(input,T_FALSE,FOLLOW_T_FALSE_in_getValueProperty7275); 
					value = new BooleanProperty(false);
					}
					break;
				case 6 :
					// Meta.g:872:7: T_TRUE
					{
					match(input,T_TRUE,FOLLOW_T_TRUE_in_getValueProperty7285); 
					value = new BooleanProperty(true);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return value;
	}
	// $ANTLR end "getValueProperty"



	// $ANTLR start "getFloat"
	// Meta.g:876:1: getFloat returns [String floating] : (termToken= T_TERM |floatToken= T_FLOAT );
	public final String getFloat() throws RecognitionException {
		String floating = null;


		Token termToken=null;
		Token floatToken=null;

		try {
			// Meta.g:876:35: (termToken= T_TERM |floatToken= T_FLOAT )
			int alt101=2;
			int LA101_0 = input.LA(1);
			if ( (LA101_0==T_TERM) ) {
				alt101=1;
			}
			else if ( (LA101_0==T_FLOAT) ) {
				alt101=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 101, 0, input);
				throw nvae;
			}

			switch (alt101) {
				case 1 :
					// Meta.g:877:5: termToken= T_TERM
					{
					termToken=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getFloat7310); 
					floating =(termToken!=null?termToken.getText():null);
					}
					break;
				case 2 :
					// Meta.g:879:5: floatToken= T_FLOAT
					{
					floatToken=(Token)match(input,T_FLOAT,FOLLOW_T_FLOAT_in_getFloat7328); 
					floating =(floatToken!=null?floatToken.getText():null);
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return floating;
	}
	// $ANTLR end "getFloat"

	// Delegated rules



	public static final BitSet FOLLOW_T_DELETE_in_deleteStatement1902 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000001L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_deleteStatement1906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_deleteStatement1911 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_deleteStatement1918 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_deleteStatement1925 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_deleteStatement1937 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000001L});
	public static final BitSet FOLLOW_T_FROM_in_deleteStatement1942 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_deleteStatement1947 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_WHERE_in_deleteStatement1952 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000010L});
	public static final BitSet FOLLOW_getRelation_in_deleteStatement1957 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_deleteStatement1962 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000010L});
	public static final BitSet FOLLOW_getRelation_in_deleteStatement1966 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_ADD_in_addStatement1985 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_addStatement1987 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_T_PATH_in_addStatement1991 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_addStatement1993 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIST_in_listStatement2010 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000068000L});
	public static final BitSet FOLLOW_getListTypes_in_listStatement2015 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_REMOVE_in_removeUDFStatement2033 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_146_in_removeUDFStatement2035 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_removeUDFStatement2037 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_removeUDFStatement2041 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_QUOTE_in_removeUDFStatement2045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropIndexStatement2065 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_INDEX_in_dropIndexStatement2067 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_T_IF_in_dropIndexStatement2071 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropIndexStatement2073 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_dropIndexStatement2082 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createIndexStatement2107 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_T_INDEX_TYPE_in_createIndexStatement2111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_INDEX_in_createIndexStatement2115 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_T_IF_in_createIndexStatement2119 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_T_NOT_in_createIndexStatement2121 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createIndexStatement2123 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2132 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_T_ON_in_createIndexStatement2137 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2141 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createIndexStatement2146 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2151 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createIndexStatement2157 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2163 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createIndexStatement2172 = new BitSet(new long[]{0x0000000000000002L,0x8000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_USING_in_createIndexStatement2176 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_createIndexStatement2180 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_WITH_in_createIndexStatement2188 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_createIndexStatement2190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2194 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement2196 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement2200 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_createIndexStatement2207 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2211 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement2213 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement2217 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_UPDATE_in_updateTableStatement2256 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010010L});
	public static final BitSet FOLLOW_getTableID_in_updateTableStatement2260 = new BitSet(new long[]{0x0000000000000000L,0x8000100000000000L});
	public static final BitSet FOLLOW_T_USING_in_updateTableStatement2267 = new BitSet(new long[]{0x0000120000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getOption_in_updateTableStatement2271 = new BitSet(new long[]{0x0000120000000000L,0x0000100000000010L});
	public static final BitSet FOLLOW_getOption_in_updateTableStatement2278 = new BitSet(new long[]{0x0000120000000000L,0x0000100000000010L});
	public static final BitSet FOLLOW_T_SET_in_updateTableStatement2290 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getAssignment_in_updateTableStatement2294 = new BitSet(new long[]{0x0000080000000000L,0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_updateTableStatement2299 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getAssignment_in_updateTableStatement2303 = new BitSet(new long[]{0x0000080000000000L,0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_WHERE_in_updateTableStatement2313 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000010L});
	public static final BitSet FOLLOW_getRelation_in_updateTableStatement2317 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_AND_in_updateTableStatement2322 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000010L});
	public static final BitSet FOLLOW_getRelation_in_updateTableStatement2326 = new BitSet(new long[]{0x0000000200000002L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_IF_in_updateTableStatement2337 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_updateTableStatement2341 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_updateTableStatement2343 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_updateTableStatement2347 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_updateTableStatement2373 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_updateTableStatement2377 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_updateTableStatement2379 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_updateTableStatement2383 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_STOP_in_stopProcessStatement2415 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_T_PROCESS_in_stopProcessStatement2417 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_stopProcessStatement2421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTriggerStatement2443 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropTriggerStatement2450 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement2454 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_T_ON_in_dropTriggerStatement2461 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement2470 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTriggerStatement2498 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_T_TRIGGER_in_createTriggerStatement2505 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2509 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_T_ON_in_createTriggerStatement2516 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2525 = new BitSet(new long[]{0x0000000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_T_USING_in_createTriggerStatement2531 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2535 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTableStatement2574 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_createTableStatement2580 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_T_IF_in_createTableStatement2587 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_T_NOT_in_createTableStatement2589 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createTableStatement2591 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2604 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement2610 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2644 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2648 = new BitSet(new long[]{0x0100080000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2651 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2653 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement2702 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2706 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2710 = new BitSet(new long[]{0x0100080000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2713 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2715 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement2773 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2775 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2777 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement2779 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2841 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement2906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement2909 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement3008 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement3012 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3053 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement3056 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3094 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3130 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement3134 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3220 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3292 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_WITH_in_createTableStatement3294 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement3305 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createTableStatement3307 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_createTableStatement3311 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_createTableStatement3328 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createTableStatement3332 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createTableStatement3334 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_createTableStatement3338 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement3402 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_alterTableStatement3408 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3416 = new BitSet(new long[]{0x0020000090000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement3423 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3427 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_T_TYPE_in_alterTableStatement3429 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3433 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ADD_in_alterTableStatement3446 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3450 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3454 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_alterTableStatement3467 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3471 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_WITH_in_alterTableStatement3484 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3501 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement3503 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement3507 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_alterTableStatement3524 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3528 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement3530 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement3534 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_SELECT_in_selectStatement3589 = new BitSet(new long[]{0x0010806020000000L,0x0000000006000010L});
	public static final BitSet FOLLOW_getSelectClause_in_selectStatement3593 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_FROM_in_selectStatement3595 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010010L});
	public static final BitSet FOLLOW_getTableID_in_selectStatement3599 = new BitSet(new long[]{0x0008000000000002L,0x0000000100100204L,0x000000000000000AL});
	public static final BitSet FOLLOW_T_WITH_in_selectStatement3607 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_WINDOW_in_selectStatement3609 = new BitSet(new long[]{0x0000400000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_getWindow_in_selectStatement3615 = new BitSet(new long[]{0x0008000000000002L,0x0000000100100204L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_INNER_in_selectStatement3628 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
	public static final BitSet FOLLOW_T_JOIN_in_selectStatement3630 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010010L});
	public static final BitSet FOLLOW_getTableID_in_selectStatement3636 = new BitSet(new long[]{0x0000000000000000L,0x0000000020000000L});
	public static final BitSet FOLLOW_T_ON_in_selectStatement3638 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getFields_in_selectStatement3642 = new BitSet(new long[]{0x0008000000000002L,0x0000000100100004L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_WHERE_in_selectStatement3651 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000010L});
	public static final BitSet FOLLOW_getWhereClauses_in_selectStatement3657 = new BitSet(new long[]{0x0008000000000002L,0x0000000100100004L});
	public static final BitSet FOLLOW_T_ORDER_in_selectStatement3666 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_BY_in_selectStatement3668 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getOrdering_in_selectStatement3674 = new BitSet(new long[]{0x0008000000000002L,0x0000000000100004L});
	public static final BitSet FOLLOW_T_GROUP_in_selectStatement3683 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_BY_in_selectStatement3685 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getList_in_selectStatement3691 = new BitSet(new long[]{0x0008000000000002L,0x0000000000100000L});
	public static final BitSet FOLLOW_T_LIMIT_in_selectStatement3700 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_CONSTANT_in_selectStatement3706 = new BitSet(new long[]{0x0008000000000002L});
	public static final BitSet FOLLOW_T_DISABLE_in_selectStatement3715 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_selectStatement3717 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INSERT_in_insertIntoStatement3755 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_INTO_in_insertIntoStatement3762 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010010L});
	public static final BitSet FOLLOW_getTableID_in_insertIntoStatement3771 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3777 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement3786 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement3796 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement3800 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement3811 = new BitSet(new long[]{0x0000000000000000L,0x0000040000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_selectStatement_in_insertIntoStatement3830 = new BitSet(new long[]{0x0000000000000002L,0x8000000000000020L});
	public static final BitSet FOLLOW_T_VALUES_in_insertIntoStatement3853 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3863 = new BitSet(new long[]{0x0000000000000000L,0x0010800000010010L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement3880 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement3897 = new BitSet(new long[]{0x0000000000000000L,0x0010800000010010L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement3901 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement3915 = new BitSet(new long[]{0x0000000000000002L,0x8000000000000020L});
	public static final BitSet FOLLOW_T_IF_in_insertIntoStatement3928 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_T_NOT_in_insertIntoStatement3930 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_insertIntoStatement3932 = new BitSet(new long[]{0x0000000000000002L,0x8000000000000000L});
	public static final BitSet FOLLOW_T_USING_in_insertIntoStatement3953 = new BitSet(new long[]{0x0000120000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement3968 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_insertIntoStatement3981 = new BitSet(new long[]{0x0000120000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement3985 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement4022 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement4024 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement4026 = new BitSet(new long[]{0x1023000090000000L,0x6201150000200400L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement4030 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement4064 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement4066 = new BitSet(new long[]{0x0000200100000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement4078 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4080 = new BitSet(new long[]{0x2000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement4083 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement4086 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement4101 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement4103 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4105 = new BitSet(new long[]{0x0040000440000000L,0x0420004040C00000L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement4120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement4139 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement4157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement4175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement4193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement4211 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4229 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement4315 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4317 = new BitSet(new long[]{0x0040000440000000L,0x0420004040C00000L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement4333 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement4352 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement4370 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement4388 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement4406 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement4424 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4442 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4460 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4478 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement4506 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement4508 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4510 = new BitSet(new long[]{0x2000000000000000L,0x0100000000000000L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement4513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement4516 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement4571 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement4579 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement4609 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement4615 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement4622 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement4624 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement4636 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement4670 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement4676 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4684 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement4690 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4698 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement4700 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement4704 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement4713 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4717 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement4719 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement4723 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement4762 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement4768 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000030L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement4775 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement4777 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement4779 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement4791 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement4797 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement4809 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement4811 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement4815 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement4824 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement4828 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement4830 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement4834 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement4873 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement4879 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010030L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement4886 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement4888 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010010L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement4900 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement4920 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010010L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement4933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTableStatement_in_metaStatement4957 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterTableStatement_in_metaStatement4970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTriggerStatement_in_metaStatement4983 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTriggerStatement_in_metaStatement4996 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stopProcessStatement_in_metaStatement5010 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_updateTableStatement_in_metaStatement5024 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_selectStatement_in_metaStatement5038 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_insertIntoStatement_in_metaStatement5052 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement5066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement5080 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement5094 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement5108 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement5122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement5136 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement5150 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement5164 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createIndexStatement_in_metaStatement5178 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropIndexStatement_in_metaStatement5193 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_listStatement_in_metaStatement5208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_addStatement_in_metaStatement5223 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_removeUDFStatement_in_metaStatement5238 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_deleteStatement_in_metaStatement5253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query5276 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query5279 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_EOF_in_query5283 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOrdering5313 = new BitSet(new long[]{0x0004081000000002L});
	public static final BitSet FOLLOW_T_ASC_in_getOrdering5318 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_DESC_in_getOrdering5324 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getOrdering5337 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getOrdering5341 = new BitSet(new long[]{0x0004081000000002L});
	public static final BitSet FOLLOW_T_ASC_in_getOrdering5346 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_DESC_in_getOrdering5352 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_getRelation_in_getWhereClauses5386 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_AND_in_getWhereClauses5391 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000010L});
	public static final BitSet FOLLOW_getRelation_in_getWhereClauses5395 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getFields5425 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getFields5427 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getFields5431 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getFields5442 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getFields5444 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getFields5448 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_LAST_in_getWindow5470 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getWindow5483 = new BitSet(new long[]{0x0000000000000000L,0x0000020000000000L,0x0000000000797000L});
	public static final BitSet FOLLOW_T_ROWS_in_getWindow5486 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTimeUnit_in_getWindow5518 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_144_in_getTimeUnit5569 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_142_in_getTimeUnit5579 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_141_in_getTimeUnit5589 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_140_in_getTimeUnit5599 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_150_in_getTimeUnit5609 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_149_in_getTimeUnit5619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_148_in_getTimeUnit5629 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_147_in_getTimeUnit5639 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelectionCount_in_getSelectClause5661 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelectionList_in_getSelectClause5673 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COUNT_in_getSelectionCount5699 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelectionCount5701 = new BitSet(new long[]{0x0000002000000000L,0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_ASTERISK_in_getSelectionCount5705 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_139_in_getSelectionCount5709 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelectionCount5715 = new BitSet(new long[]{0x0000000800000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelectionCount5722 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelectionCount5728 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DISTINCT_in_getSelectionList5763 = new BitSet(new long[]{0x0000806020000000L,0x0000000006000010L});
	public static final BitSet FOLLOW_getSelection_in_getSelectionList5771 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ASTERISK_in_getSelection5811 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelector_in_getSelection5834 = new BitSet(new long[]{0x0000080800000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelection5839 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelection5843 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelection5864 = new BitSet(new long[]{0x0000804020000000L,0x0000000006000010L});
	public static final BitSet FOLLOW_getSelector_in_getSelection5868 = new BitSet(new long[]{0x0000080800000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelection5873 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelection5877 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_AGGREGATION_in_getSelector5932 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_MAX_in_getSelector5946 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_MIN_in_getSelector5960 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_AVG_in_getSelector5974 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_COUNT_in_getSelector5988 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelector6014 = new BitSet(new long[]{0x0100804020000000L,0x0000000006000010L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6036 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelector6041 = new BitSet(new long[]{0x0000804020000000L,0x0000000006000010L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6045 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelector6066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelector6082 = new BitSet(new long[]{0x0000000000000002L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelector6114 = new BitSet(new long[]{0x0100804020000000L,0x0000000006000010L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6119 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelector6124 = new BitSet(new long[]{0x0000804020000000L,0x0000000006000010L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6128 = new BitSet(new long[]{0x0100080000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelector6153 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_getListTypes6186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getAssignment6217 = new BitSet(new long[]{0x0400000000000000L,0x0000200000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getAssignment6229 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getValueAssign_in_getAssignment6233 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_getAssignment6252 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getAssignment6256 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_getAssignment6258 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getAssignment6260 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getAssignment6264 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getValueAssign6291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueAssign6303 = new BitSet(new long[]{0x0000000000000000L,0x0004000800000000L});
	public static final BitSet FOLLOW_T_PLUS_in_getValueAssign6306 = new BitSet(new long[]{0x0000400000000000L,0x0000A00000000000L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getValueAssign6309 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueAssign6313 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getValueAssign6315 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getIntSetOrList_in_getValueAssign6355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SUBTRACT_in_getValueAssign6399 = new BitSet(new long[]{0x0000400000000000L,0x0000A00000000000L});
	public static final BitSet FOLLOW_getIntSetOrList_in_getValueAssign6403 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getIntSetOrList6429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_getIntSetOrList6439 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getList_in_getIntSetOrList6443 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_getIntSetOrList6445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getIntSetOrList6455 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getSet_in_getIntSetOrList6459 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getIntSetOrList6461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TOKEN_in_getRelation6479 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation6481 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getIds_in_getRelation6485 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation6487 = new BitSet(new long[]{0x0400000000000000L,0x00000000110C000AL});
	public static final BitSet FOLLOW_getComparator_in_getRelation6491 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6495 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getRelation6507 = new BitSet(new long[]{0x0400008000000000L,0x00000000110C004AL});
	public static final BitSet FOLLOW_getComparator_in_getRelation6513 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6517 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IN_in_getRelation6543 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation6545 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerms_in_getRelation6549 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation6551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_BETWEEN_in_getRelation6577 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6581 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_AND_in_getRelation6583 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6587 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EQUAL_in_getComparator6627 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_GT_in_getComparator6637 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LT_in_getComparator6647 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_GET_in_getComparator6657 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LET_in_getComparator6668 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_NOT_EQUAL_in_getComparator6678 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIKE_in_getComparator6689 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getIds6717 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getIds6722 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_IDENT_in_getIds6726 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_getOption_in_getOptions6751 = new BitSet(new long[]{0x0000120000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_getOption_in_getOptions6758 = new BitSet(new long[]{0x0000120000000002L,0x0000000000000010L});
	public static final BitSet FOLLOW_T_COMPACT_in_getOption6778 = new BitSet(new long[]{0x0000000000000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_T_STORAGE_in_getOption6780 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getOption6790 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getOption6792 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOption6804 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getOption6806 = new BitSet(new long[]{0x6000400000000000L,0x0110800000000010L});
	public static final BitSet FOLLOW_getValueProperty_in_getOption6810 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getList6838 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getList6847 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getList6851 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_getTerm_in_getTerms6885 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getTerms6894 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getTerms6898 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_getTerm_in_getSet6932 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getSet6941 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getSet6945 = new BitSet(new long[]{0x0000080000000002L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral6979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getTermOrLiteral6993 = new BitSet(new long[]{0x0200000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7011 = new BitSet(new long[]{0x0200080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getTermOrLiteral7024 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7028 = new BitSet(new long[]{0x0200080000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getTermOrLiteral7045 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID7066 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_KS_AND_TN_in_getTableID7082 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm7106 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_KS_AND_TN_in_getTerm7118 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm7130 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral7160 = new BitSet(new long[]{0x0200000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7170 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral7172 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7176 = new BitSet(new long[]{0x0200080000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral7185 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7189 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral7191 = new BitSet(new long[]{0x0000000000000000L,0x0010000000010010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7195 = new BitSet(new long[]{0x0200080000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral7207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty7229 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty7241 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty7253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getFloat_in_getValueProperty7265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_getValueProperty7275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_getValueProperty7285 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getFloat7310 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FLOAT_in_getFloat7328 = new BitSet(new long[]{0x0000000000000002L});
}
