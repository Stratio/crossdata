// $ANTLR 3.5.1 Meta.g 2014-02-06 11:52:16

    package com.stratio.sdh.meta.generated;    
    import com.stratio.sdh.meta.statements.Statement;
    import com.stratio.sdh.meta.statements.AlterKeyspaceStatement;
    import com.stratio.sdh.meta.statements.CreateKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropKeyspaceStatement;
    import com.stratio.sdh.meta.statements.DropTableStatement;
    import com.stratio.sdh.meta.statements.ExplainPlanStatement;
    import com.stratio.sdh.meta.statements.SetOptionsStatement;
    import com.stratio.sdh.meta.statements.TruncateStatement;
    import com.stratio.sdh.meta.statements.UseStatement;
    import com.stratio.sdh.meta.structures.Consistency;
    import com.stratio.sdh.meta.structures.ConstantProperty;
    import com.stratio.sdh.meta.structures.IdentifierProperty;
    import com.stratio.sdh.meta.structures.MapLiteralProperty;
    import com.stratio.sdh.meta.structures.ValueProperty;
    import java.util.HashMap;
    import java.util.Map;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "DIGIT", "E", 
		"F", "G", "H", "I", "J", "K", "L", "LETTER", "M", "N", "O", "P", "Q", 
		"R", "S", "T", "T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", "T_ANY", "T_COLON", 
		"T_COMMA", "T_CONSISTENCY", "T_CONSTANT", "T_CREATE", "T_DROP", "T_EACH_QUORUM", 
		"T_END_SBRACKET", "T_EQUAL", "T_EXISTS", "T_EXPLAIN", "T_FALSE", "T_FOR", 
		"T_IDENT", "T_IF", "T_KEYSPACE", "T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_NOT", 
		"T_ONE", "T_OPTIONS", "T_PLAN", "T_POINT", "T_QUORUM", "T_SEMICOLON", 
		"T_SET", "T_START_SBRACKET", "T_TABLE", "T_TERM", "T_THREE", "T_TRUE", 
		"T_TRUNCATE", "T_TWO", "T_USE", "T_WITH", "U", "V", "W", "WS", "X", "Y", 
		"Z"
	};
	public static final int EOF=-1;
	public static final int A=4;
	public static final int B=5;
	public static final int C=6;
	public static final int D=7;
	public static final int DIGIT=8;
	public static final int E=9;
	public static final int F=10;
	public static final int G=11;
	public static final int H=12;
	public static final int I=13;
	public static final int J=14;
	public static final int K=15;
	public static final int L=16;
	public static final int LETTER=17;
	public static final int M=18;
	public static final int N=19;
	public static final int O=20;
	public static final int P=21;
	public static final int Q=22;
	public static final int R=23;
	public static final int S=24;
	public static final int T=25;
	public static final int T_ALL=26;
	public static final int T_ALTER=27;
	public static final int T_ANALYTICS=28;
	public static final int T_AND=29;
	public static final int T_ANY=30;
	public static final int T_COLON=31;
	public static final int T_COMMA=32;
	public static final int T_CONSISTENCY=33;
	public static final int T_CONSTANT=34;
	public static final int T_CREATE=35;
	public static final int T_DROP=36;
	public static final int T_EACH_QUORUM=37;
	public static final int T_END_SBRACKET=38;
	public static final int T_EQUAL=39;
	public static final int T_EXISTS=40;
	public static final int T_EXPLAIN=41;
	public static final int T_FALSE=42;
	public static final int T_FOR=43;
	public static final int T_IDENT=44;
	public static final int T_IF=45;
	public static final int T_KEYSPACE=46;
	public static final int T_LOCAL_ONE=47;
	public static final int T_LOCAL_QUORUM=48;
	public static final int T_NOT=49;
	public static final int T_ONE=50;
	public static final int T_OPTIONS=51;
	public static final int T_PLAN=52;
	public static final int T_POINT=53;
	public static final int T_QUORUM=54;
	public static final int T_SEMICOLON=55;
	public static final int T_SET=56;
	public static final int T_START_SBRACKET=57;
	public static final int T_TABLE=58;
	public static final int T_TERM=59;
	public static final int T_THREE=60;
	public static final int T_TRUE=61;
	public static final int T_TRUNCATE=62;
	public static final int T_TWO=63;
	public static final int T_USE=64;
	public static final int T_WITH=65;
	public static final int U=66;
	public static final int V=67;
	public static final int W=68;
	public static final int WS=69;
	public static final int X=70;
	public static final int Y=71;
	public static final int Z=72;

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



	// $ANTLR start "explainPlanStatement"
	// Meta.g:144:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		try {
			// Meta.g:144:59: ( T_EXPLAIN T_PLAN T_FOR )
			// Meta.g:145:5: T_EXPLAIN T_PLAN T_FOR
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement960); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement962); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement964); 
			xpplst = new ExplainPlanStatement("temp");
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
	// Meta.g:149:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:156:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:157:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement999); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement1001); 
			// Meta.g:157:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==T_ANALYTICS) ) {
				alt7=1;
			}
			else if ( (LA7_0==T_CONSISTENCY) ) {
				alt7=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// Meta.g:158:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1013); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1015); 
					// Meta.g:158:29: ( T_TRUE | T_FALSE )
					int alt1=2;
					int LA1_0 = input.LA(1);
					if ( (LA1_0==T_TRUE) ) {
						alt1=1;
					}
					else if ( (LA1_0==T_FALSE) ) {
						alt1=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 1, 0, input);
						throw nvae;
					}

					switch (alt1) {
						case 1 :
							// Meta.g:158:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1018); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:158:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1021); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:159:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt3=2;
					int LA3_0 = input.LA(1);
					if ( (LA3_0==T_AND) ) {
						alt3=1;
					}
					switch (alt3) {
						case 1 :
							// Meta.g:159:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1036); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1038); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1040); 
							// Meta.g:160:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt2=9;
							switch ( input.LA(1) ) {
							case T_ALL:
								{
								alt2=1;
								}
								break;
							case T_ANY:
								{
								alt2=2;
								}
								break;
							case T_QUORUM:
								{
								alt2=3;
								}
								break;
							case T_ONE:
								{
								alt2=4;
								}
								break;
							case T_TWO:
								{
								alt2=5;
								}
								break;
							case T_THREE:
								{
								alt2=6;
								}
								break;
							case T_EACH_QUORUM:
								{
								alt2=7;
								}
								break;
							case T_LOCAL_ONE:
								{
								alt2=8;
								}
								break;
							case T_LOCAL_QUORUM:
								{
								alt2=9;
								}
								break;
							default:
								NoViableAltException nvae =
									new NoViableAltException("", 2, 0, input);
								throw nvae;
							}
							switch (alt2) {
								case 1 :
									// Meta.g:160:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1055); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:161:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1074); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:162:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1092); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:163:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1110); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:164:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1128); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:165:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1146); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:166:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1164); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:167:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1182); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:168:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1200); 
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
					// Meta.g:172:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1250); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1252); 
					// Meta.g:173:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt4=9;
					switch ( input.LA(1) ) {
					case T_ALL:
						{
						alt4=1;
						}
						break;
					case T_ANY:
						{
						alt4=2;
						}
						break;
					case T_QUORUM:
						{
						alt4=3;
						}
						break;
					case T_ONE:
						{
						alt4=4;
						}
						break;
					case T_TWO:
						{
						alt4=5;
						}
						break;
					case T_THREE:
						{
						alt4=6;
						}
						break;
					case T_EACH_QUORUM:
						{
						alt4=7;
						}
						break;
					case T_LOCAL_ONE:
						{
						alt4=8;
						}
						break;
					case T_LOCAL_QUORUM:
						{
						alt4=9;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 4, 0, input);
						throw nvae;
					}
					switch (alt4) {
						case 1 :
							// Meta.g:173:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1268); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:174:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1287); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:175:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1305); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:176:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1323); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:177:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1341); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:178:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1359); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:179:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1377); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:180:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1395); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:181:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1413); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:183:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt6=2;
					int LA6_0 = input.LA(1);
					if ( (LA6_0==T_AND) ) {
						alt6=1;
					}
					switch (alt6) {
						case 1 :
							// Meta.g:183:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1441); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1443); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1445); 
							// Meta.g:183:36: ( T_TRUE | T_FALSE )
							int alt5=2;
							int LA5_0 = input.LA(1);
							if ( (LA5_0==T_TRUE) ) {
								alt5=1;
							}
							else if ( (LA5_0==T_FALSE) ) {
								alt5=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 5, 0, input);
								throw nvae;
							}

							switch (alt5) {
								case 1 :
									// Meta.g:183:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1448); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:183:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1451); 
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
	// Meta.g:189:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:189:41: ( T_USE iden= T_IDENT )
			// Meta.g:190:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement1506); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement1514); 
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
	// Meta.g:194:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:197:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:198:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement1544); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1550); 
			// Meta.g:200:5: ( T_IF T_EXISTS )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==T_IF) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// Meta.g:200:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement1557); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement1559); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement1571); 
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
	// Meta.g:205:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:208:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:209:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement1605); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement1611); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1619); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement1625); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1633); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement1635); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement1639);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:214:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==T_AND) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// Meta.g:214:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement1648); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1652); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement1654); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement1658);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop9;
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
	// Meta.g:218:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
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
			// Meta.g:222:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:223:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement1697); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement1703); 
			// Meta.g:225:5: ( T_IF T_NOT T_EXISTS )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==T_IF) ) {
				alt10=1;
			}
			switch (alt10) {
				case 1 :
					// Meta.g:225:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement1710); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement1712); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement1714); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1726); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement1732); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1744); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement1746); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement1750);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:229:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==T_AND) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// Meta.g:229:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement1759); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1763); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement1765); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement1769);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop11;
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
	// Meta.g:233:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String ident =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:236:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID )
			// Meta.g:237:5: T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement1808); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement1814); 
			// Meta.g:239:5: ( T_IF T_EXISTS )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==T_IF) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// Meta.g:239:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement1821); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement1823); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement1835);
			ident=getTableID();
			state._fsp--;


			        drtbst = new DropTableStatement(ident, ifExists);
			    
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
	// Meta.g:245:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:245:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:246:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement1855); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement1868);
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
	// Meta.g:252:1: metaStatement returns [Statement st] : (st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement );
	public final Statement metaStatement() throws RecognitionException {
		Statement st = null;


		ExplainPlanStatement st_xppl =null;
		SetOptionsStatement st_stpt =null;
		UseStatement st_usks =null;
		DropKeyspaceStatement st_drks =null;
		CreateKeyspaceStatement st_crks =null;
		AlterKeyspaceStatement st_alks =null;
		DropTableStatement st_tbdr =null;
		TruncateStatement st_trst =null;

		try {
			// Meta.g:252:37: (st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement )
			int alt13=8;
			switch ( input.LA(1) ) {
			case T_EXPLAIN:
				{
				alt13=1;
				}
				break;
			case T_SET:
				{
				alt13=2;
				}
				break;
			case T_USE:
				{
				alt13=3;
				}
				break;
			case T_DROP:
				{
				int LA13_4 = input.LA(2);
				if ( (LA13_4==T_KEYSPACE) ) {
					alt13=4;
				}
				else if ( (LA13_4==T_TABLE) ) {
					alt13=7;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 13, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case T_CREATE:
				{
				alt13=5;
				}
				break;
			case T_ALTER:
				{
				alt13=6;
				}
				break;
			case T_TRUNCATE:
				{
				alt13=8;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}
			switch (alt13) {
				case 1 :
					// Meta.g:253:5: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement1891);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 2 :
					// Meta.g:254:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement1905);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 3 :
					// Meta.g:255:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement1919);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 4 :
					// Meta.g:256:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement1933);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 5 :
					// Meta.g:257:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement1947);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 6 :
					// Meta.g:258:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement1961);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 7 :
					// Meta.g:259:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement1975);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 8 :
					// Meta.g:260:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement1989);
					st_trst=truncateStatement();
					state._fsp--;

					 st = st_trst; 
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
	// Meta.g:262:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final Statement query() throws RecognitionException {
		Statement st = null;


		Statement mtst =null;

		try {
			// Meta.g:262:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:263:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query2010);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:263:21: ( T_SEMICOLON )+
			int cnt14=0;
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==T_SEMICOLON) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// Meta.g:263:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query2013); 
					}
					break;

				default :
					if ( cnt14 >= 1 ) break loop14;
					EarlyExitException eee = new EarlyExitException(14, input);
					throw eee;
				}
				cnt14++;
			}

			match(input,EOF,FOLLOW_EOF_in_query2017); 

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



	// $ANTLR start "getTableID"
	// Meta.g:270:1: getTableID returns [String tableID] : (ks= T_IDENT '.' )? ident= T_IDENT ;
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ks=null;
		Token ident=null;

		try {
			// Meta.g:270:36: ( (ks= T_IDENT '.' )? ident= T_IDENT )
			// Meta.g:271:5: (ks= T_IDENT '.' )? ident= T_IDENT
			{
			// Meta.g:271:5: (ks= T_IDENT '.' )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==T_IDENT) ) {
				int LA15_1 = input.LA(2);
				if ( (LA15_1==T_POINT) ) {
					alt15=1;
				}
			}
			switch (alt15) {
				case 1 :
					// Meta.g:271:6: ks= T_IDENT '.'
					{
					ks=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2041); 
					match(input,T_POINT,FOLLOW_T_POINT_in_getTableID2043); 
					}
					break;

			}

			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2054); 
			tableID = new String((ks!=null?ks.getText():null)==null?(ident!=null?ident.getText():null):(ks!=null?ks.getText():null)+'.'+(ident!=null?ident.getText():null));
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


	public static class getTerm_return extends ParserRuleReturnScope {
		public String term;
	};


	// $ANTLR start "getTerm"
	// Meta.g:274:1: getTerm returns [String term] : (ident= T_IDENT |noIdent= T_TERM );
	public final MetaParser.getTerm_return getTerm() throws RecognitionException {
		MetaParser.getTerm_return retval = new MetaParser.getTerm_return();
		retval.start = input.LT(1);

		Token ident=null;
		Token noIdent=null;

		try {
			// Meta.g:274:30: (ident= T_IDENT |noIdent= T_TERM )
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IDENT) ) {
				alt16=1;
			}
			else if ( (LA16_0==T_TERM) ) {
				alt16=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}

			switch (alt16) {
				case 1 :
					// Meta.g:275:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm2074); 
					retval.term = (ident!=null?ident.getText():null);
					}
					break;
				case 2 :
					// Meta.g:276:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm2086); 
					retval.term = (noIdent!=null?noIdent.getText():null);
					}
					break;

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
	// $ANTLR end "getTerm"



	// $ANTLR start "getMapLiteral"
	// Meta.g:279:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		ParserRuleReturnScope leftTerm1 =null;
		ParserRuleReturnScope rightTerm1 =null;
		ParserRuleReturnScope leftTermN =null;
		ParserRuleReturnScope rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:282:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:283:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral2116); 
			// Meta.g:284:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_IDENT||LA18_0==T_TERM) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// Meta.g:284:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2126);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2128); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2132);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put((leftTerm1!=null?input.toString(leftTerm1.start,leftTerm1.stop):null), (rightTerm1!=null?input.toString(rightTerm1.start,rightTerm1.stop):null));
					// Meta.g:285:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop17:
					while (true) {
						int alt17=2;
						int LA17_0 = input.LA(1);
						if ( (LA17_0==T_COMMA) ) {
							alt17=1;
						}

						switch (alt17) {
						case 1 :
							// Meta.g:285:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral2141); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2145);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2147); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2151);
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put((leftTermN!=null?input.toString(leftTermN.start,leftTermN.stop):null), (rightTermN!=null?input.toString(rightTermN.start,rightTermN.stop):null));
							}
							break;

						default :
							break loop17;
						}
					}

					}
					break;

			}

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral2163); 
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
	// Meta.g:289:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Map<String, String> mapliteral =null;

		try {
			// Meta.g:289:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral )
			int alt19=3;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt19=1;
				}
				break;
			case T_CONSTANT:
				{
				alt19=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt19=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 19, 0, input);
				throw nvae;
			}
			switch (alt19) {
				case 1 :
					// Meta.g:290:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty2185); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:291:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty2197); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:292:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty2209);
					mapliteral=getMapLiteral();
					state._fsp--;

					value = new MapLiteralProperty(mapliteral);
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

	// Delegated rules



	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement960 = new BitSet(new long[]{0x0010000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement962 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement964 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement999 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement1001 = new BitSet(new long[]{0x0000000210000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1013 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1015 = new BitSet(new long[]{0x2000040000000000L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1018 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1021 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1036 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1038 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1040 = new BitSet(new long[]{0x9045802044000000L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1055 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1074 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1092 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1110 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1164 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1182 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1200 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1250 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1252 = new BitSet(new long[]{0x9045802044000000L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1268 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1287 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1305 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1323 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1341 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1359 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1377 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1395 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1413 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1441 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1443 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1445 = new BitSet(new long[]{0x2000040000000000L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1451 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement1506 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement1514 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement1544 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1550 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement1557 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement1559 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement1571 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement1605 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement1611 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1619 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement1625 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1633 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement1635 = new BitSet(new long[]{0x0200100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement1639 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement1648 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1652 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement1654 = new BitSet(new long[]{0x0200100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement1658 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement1697 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement1703 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement1710 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement1712 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement1714 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1726 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement1732 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1744 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement1746 = new BitSet(new long[]{0x0200100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement1750 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement1759 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1763 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement1765 = new BitSet(new long[]{0x0200100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement1769 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement1808 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement1814 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement1821 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement1823 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement1835 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement1855 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement1868 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement1891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement1905 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement1919 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement1933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement1947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement1961 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement1975 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement1989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query2010 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query2013 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_EOF_in_query2017 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2041 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_T_POINT_in_getTableID2043 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2054 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm2074 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm2086 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral2116 = new BitSet(new long[]{0x0800104000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2126 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2128 = new BitSet(new long[]{0x0800100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2132 = new BitSet(new long[]{0x0000004100000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral2141 = new BitSet(new long[]{0x0800100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2145 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2147 = new BitSet(new long[]{0x0800100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2151 = new BitSet(new long[]{0x0000004100000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral2163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty2185 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty2197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty2209 = new BitSet(new long[]{0x0000000000000002L});
}
