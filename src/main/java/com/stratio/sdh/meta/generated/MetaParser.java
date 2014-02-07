// $ANTLR 3.5.1 Meta.g 2014-02-06 18:42:57

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
		"T_ON", "T_ONE", "T_OPTIONS", "T_PLAN", "T_POINT", "T_PROCESS", "T_QUORUM", 
		"T_SEMICOLON", "T_SET", "T_START_SBRACKET", "T_STOP", "T_TABLE", "T_TERM", 
		"T_THREE", "T_TRIGGER", "T_TRUE", "T_TRUNCATE", "T_TWO", "T_USE", "T_WITH", 
		"U", "V", "W", "WS", "X", "Y", "Z"
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
	public static final int T_ON=50;
	public static final int T_ONE=51;
	public static final int T_OPTIONS=52;
	public static final int T_PLAN=53;
	public static final int T_POINT=54;
	public static final int T_PROCESS=55;
	public static final int T_QUORUM=56;
	public static final int T_SEMICOLON=57;
	public static final int T_SET=58;
	public static final int T_START_SBRACKET=59;
	public static final int T_STOP=60;
	public static final int T_TABLE=61;
	public static final int T_TERM=62;
	public static final int T_THREE=63;
	public static final int T_TRIGGER=64;
	public static final int T_TRUE=65;
	public static final int T_TRUNCATE=66;
	public static final int T_TWO=67;
	public static final int T_USE=68;
	public static final int T_WITH=69;
	public static final int U=70;
	public static final int V=71;
	public static final int W=72;
	public static final int WS=73;
	public static final int X=74;
	public static final int Y=75;
	public static final int Z=76;

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



	// $ANTLR start "stopProcessStatement"
	// Meta.g:151:1: stopProcessStatement returns [StopProcessStatement stprst] : T_STOP T_PROCESS ident= T_IDENT ;
	public final StopProcessStatement stopProcessStatement() throws RecognitionException {
		StopProcessStatement stprst = null;


		Token ident=null;

		try {
			// Meta.g:151:59: ( T_STOP T_PROCESS ident= T_IDENT )
			// Meta.g:152:5: T_STOP T_PROCESS ident= T_IDENT
			{
			match(input,T_STOP,FOLLOW_T_STOP_in_stopProcessStatement1017); 
			match(input,T_PROCESS,FOLLOW_T_PROCESS_in_stopProcessStatement1019); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_stopProcessStatement1023); 
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
	// Meta.g:156:1: dropTriggerStatement returns [DropTriggerStatement drtrst] : T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT ;
	public final DropTriggerStatement dropTriggerStatement() throws RecognitionException {
		DropTriggerStatement drtrst = null;


		Token ident=null;
		Token ident2=null;

		try {
			// Meta.g:156:59: ( T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT )
			// Meta.g:157:5: T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTriggerStatement1046); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropTriggerStatement1053); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement1057); 
			match(input,T_ON,FOLLOW_T_ON_in_dropTriggerStatement1064); 
			ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement1073); 
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



	// $ANTLR start "explainPlanStatement"
	// Meta.g:163:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		Statement parsedStmnt =null;

		try {
			// Meta.g:163:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:164:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement1100); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement1102); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement1104); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement1108);
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
	// Meta.g:168:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:175:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:176:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement1142); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement1144); 
			// Meta.g:176:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
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
					// Meta.g:177:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1156); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1158); 
					// Meta.g:177:29: ( T_TRUE | T_FALSE )
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
							// Meta.g:177:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1161); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:177:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1164); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:178:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt3=2;
					int LA3_0 = input.LA(1);
					if ( (LA3_0==T_AND) ) {
						alt3=1;
					}
					switch (alt3) {
						case 1 :
							// Meta.g:178:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1179); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1181); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1183); 
							// Meta.g:179:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
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
									// Meta.g:179:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1198); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:180:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1217); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:181:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1235); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:182:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1253); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:183:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1271); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:184:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1289); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:185:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1307); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:186:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1325); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:187:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1343); 
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
					// Meta.g:191:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1393); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1395); 
					// Meta.g:192:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
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
							// Meta.g:192:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1411); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:193:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1430); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:194:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1448); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:195:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1466); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:196:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1484); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:197:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1502); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:198:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1520); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:199:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1538); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:200:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1556); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:202:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt6=2;
					int LA6_0 = input.LA(1);
					if ( (LA6_0==T_AND) ) {
						alt6=1;
					}
					switch (alt6) {
						case 1 :
							// Meta.g:202:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1584); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1586); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1588); 
							// Meta.g:202:36: ( T_TRUE | T_FALSE )
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
									// Meta.g:202:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1591); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:202:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1594); 
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
	// Meta.g:208:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:208:41: ( T_USE iden= T_IDENT )
			// Meta.g:209:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement1649); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement1657); 
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
	// Meta.g:213:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:216:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:217:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement1687); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1693); 
			// Meta.g:219:5: ( T_IF T_EXISTS )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==T_IF) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// Meta.g:219:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement1700); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement1702); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement1714); 
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
	// Meta.g:224:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:227:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:228:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement1748); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement1754); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1762); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement1768); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1776); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement1778); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement1782);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:233:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==T_AND) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// Meta.g:233:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement1791); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1795); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement1797); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement1801);
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
	// Meta.g:237:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
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
			// Meta.g:241:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:242:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement1840); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement1846); 
			// Meta.g:244:5: ( T_IF T_NOT T_EXISTS )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==T_IF) ) {
				alt10=1;
			}
			switch (alt10) {
				case 1 :
					// Meta.g:244:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement1853); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement1855); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement1857); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1869); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement1875); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1887); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement1889); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement1893);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:248:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==T_AND) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// Meta.g:248:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement1902); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1906); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement1908); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement1912);
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
	// Meta.g:252:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String ident =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:255:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID )
			// Meta.g:256:5: T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement1951); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement1957); 
			// Meta.g:258:5: ( T_IF T_EXISTS )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==T_IF) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// Meta.g:258:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement1964); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement1966); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement1978);
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
	// Meta.g:264:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:264:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:265:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement1998); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement2011);
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
	// Meta.g:271:1: metaStatement returns [Statement st] : (st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement );
	public final Statement metaStatement() throws RecognitionException {
		Statement st = null;


		DropTriggerStatement st_drtr =null;
		StopProcessStatement st_stpr =null;
		ExplainPlanStatement st_xppl =null;
		SetOptionsStatement st_stpt =null;
		UseStatement st_usks =null;
		DropKeyspaceStatement st_drks =null;
		CreateKeyspaceStatement st_crks =null;
		AlterKeyspaceStatement st_alks =null;
		DropTableStatement st_tbdr =null;
		TruncateStatement st_trst =null;

		try {
			// Meta.g:271:37: (st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement )
			int alt13=10;
			switch ( input.LA(1) ) {
			case T_DROP:
				{
				switch ( input.LA(2) ) {
				case T_TRIGGER:
					{
					alt13=1;
					}
					break;
				case T_KEYSPACE:
					{
					alt13=6;
					}
					break;
				case T_TABLE:
					{
					alt13=9;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 13, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_STOP:
				{
				alt13=2;
				}
				break;
			case T_EXPLAIN:
				{
				alt13=3;
				}
				break;
			case T_SET:
				{
				alt13=4;
				}
				break;
			case T_USE:
				{
				alt13=5;
				}
				break;
			case T_CREATE:
				{
				alt13=7;
				}
				break;
			case T_ALTER:
				{
				alt13=8;
				}
				break;
			case T_TRUNCATE:
				{
				alt13=10;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}
			switch (alt13) {
				case 1 :
					// Meta.g:273:5: st_drtr= dropTriggerStatement
					{
					pushFollow(FOLLOW_dropTriggerStatement_in_metaStatement2034);
					st_drtr=dropTriggerStatement();
					state._fsp--;

					 st = st_drtr; 
					}
					break;
				case 2 :
					// Meta.g:274:6: st_stpr= stopProcessStatement
					{
					pushFollow(FOLLOW_stopProcessStatement_in_metaStatement2047);
					st_stpr=stopProcessStatement();
					state._fsp--;

					 st = st_stpr; 
					}
					break;
				case 3 :
					// Meta.g:275:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement2061);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 4 :
					// Meta.g:276:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement2075);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 5 :
					// Meta.g:277:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement2089);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 6 :
					// Meta.g:278:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement2103);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 7 :
					// Meta.g:279:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement2117);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 8 :
					// Meta.g:280:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement2131);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 9 :
					// Meta.g:281:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement2145);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 10 :
					// Meta.g:282:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement2159);
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
	// Meta.g:284:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final Statement query() throws RecognitionException {
		Statement st = null;


		Statement mtst =null;

		try {
			// Meta.g:284:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:285:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query2180);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:285:21: ( T_SEMICOLON )+
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
					// Meta.g:285:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query2183); 
					}
					break;

				default :
					if ( cnt14 >= 1 ) break loop14;
					EarlyExitException eee = new EarlyExitException(14, input);
					throw eee;
				}
				cnt14++;
			}

			match(input,EOF,FOLLOW_EOF_in_query2187); 

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
	// Meta.g:292:1: getTableID returns [String tableID] : (ks= T_IDENT '.' )? ident= T_IDENT ;
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ks=null;
		Token ident=null;

		try {
			// Meta.g:292:36: ( (ks= T_IDENT '.' )? ident= T_IDENT )
			// Meta.g:293:5: (ks= T_IDENT '.' )? ident= T_IDENT
			{
			// Meta.g:293:5: (ks= T_IDENT '.' )?
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
					// Meta.g:293:6: ks= T_IDENT '.'
					{
					ks=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2211); 
					match(input,T_POINT,FOLLOW_T_POINT_in_getTableID2213); 
					}
					break;

			}

			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2224); 
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
	// Meta.g:296:1: getTerm returns [String term] : (ident= T_IDENT |noIdent= T_TERM );
	public final MetaParser.getTerm_return getTerm() throws RecognitionException {
		MetaParser.getTerm_return retval = new MetaParser.getTerm_return();
		retval.start = input.LT(1);

		Token ident=null;
		Token noIdent=null;

		try {
			// Meta.g:296:30: (ident= T_IDENT |noIdent= T_TERM )
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
					// Meta.g:297:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm2244); 
					retval.term = (ident!=null?ident.getText():null);
					}
					break;
				case 2 :
					// Meta.g:298:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm2256); 
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
	// Meta.g:301:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		ParserRuleReturnScope leftTerm1 =null;
		ParserRuleReturnScope rightTerm1 =null;
		ParserRuleReturnScope leftTermN =null;
		ParserRuleReturnScope rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:304:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:305:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral2286); 
			// Meta.g:306:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_IDENT||LA18_0==T_TERM) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// Meta.g:306:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2296);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2298); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2302);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put((leftTerm1!=null?input.toString(leftTerm1.start,leftTerm1.stop):null), (rightTerm1!=null?input.toString(rightTerm1.start,rightTerm1.stop):null));
					// Meta.g:307:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop17:
					while (true) {
						int alt17=2;
						int LA17_0 = input.LA(1);
						if ( (LA17_0==T_COMMA) ) {
							alt17=1;
						}

						switch (alt17) {
						case 1 :
							// Meta.g:307:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral2311); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2315);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2317); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2321);
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

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral2333); 
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
	// Meta.g:311:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Map<String, String> mapliteral =null;

		try {
			// Meta.g:311:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral )
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
					// Meta.g:312:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty2355); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:313:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty2367); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:314:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty2379);
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



	public static final BitSet FOLLOW_T_STOP_in_stopProcessStatement1017 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_PROCESS_in_stopProcessStatement1019 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_stopProcessStatement1023 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTriggerStatement1046 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropTriggerStatement1053 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1057 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_ON_in_dropTriggerStatement1064 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1073 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement1100 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement1102 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement1104 = new BitSet(new long[]{0x1400021808000000L,0x0000000000000014L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement1108 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement1142 = new BitSet(new long[]{0x0010000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement1144 = new BitSet(new long[]{0x0000000210000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1156 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1158 = new BitSet(new long[]{0x0000040000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1161 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1164 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1179 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1181 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1183 = new BitSet(new long[]{0x8109802044000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1198 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1217 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1235 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1271 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1289 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1307 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1393 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1395 = new BitSet(new long[]{0x8109802044000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1411 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1430 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1448 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1466 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1484 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1502 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1520 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1538 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1556 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1584 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1586 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1588 = new BitSet(new long[]{0x0000040000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1591 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1594 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement1649 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement1657 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement1687 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1693 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement1700 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement1702 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement1714 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement1748 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement1754 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1762 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement1768 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1776 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement1778 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement1782 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement1791 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1795 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement1797 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement1801 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement1840 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement1846 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement1853 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement1855 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement1857 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1869 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement1875 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1887 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement1889 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement1893 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement1902 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1906 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement1908 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement1912 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement1951 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement1957 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement1964 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement1966 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement1978 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement1998 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement2011 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTriggerStatement_in_metaStatement2034 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stopProcessStatement_in_metaStatement2047 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement2061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement2075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement2089 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement2103 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement2117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement2131 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement2145 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement2159 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query2180 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query2183 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_EOF_in_query2187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2211 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_POINT_in_getTableID2213 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm2244 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm2256 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral2286 = new BitSet(new long[]{0x4000104000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2296 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2298 = new BitSet(new long[]{0x4000100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2302 = new BitSet(new long[]{0x0000004100000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral2311 = new BitSet(new long[]{0x4000100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2315 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2317 = new BitSet(new long[]{0x4000100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2321 = new BitSet(new long[]{0x0000004100000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral2333 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty2355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty2367 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty2379 = new BitSet(new long[]{0x0000000000000002L});
}
