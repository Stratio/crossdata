// $ANTLR 3.5.1 MetaHelp.g 2014-03-18 10:49:16

	package com.stratio.meta.sh.help.generated;
	import com.stratio.meta.sh.help.HelpType;
	import com.stratio.meta.sh.help.HelpStatement;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaHelpParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "E", "EXPONENT", 
		"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "POINT", "Q", "R", 
		"S", "T", "T_ADD", "T_ALTER", "T_ANALYTICS", "T_AS", "T_ASC", "T_BETWEEN", 
		"T_CLUSTERING", "T_COMPACT", "T_CONSISTENCY", "T_COUNT", "T_CREATE", "T_DATATYPES", 
		"T_DEFAULT", "T_DELETE", "T_DESC", "T_DISABLE", "T_DISTINCT", "T_DROP", 
		"T_EXIT", "T_EXPLAIN", "T_FOR", "T_FROM", "T_HASH", "T_HELP", "T_INDEX", 
		"T_INSERT", "T_INTO", "T_KEY", "T_KEYSPACE", "T_LIKE", "T_LIST", "T_LUCENE", 
		"T_OPTIONS", "T_ORDER", "T_PLAN", "T_PRIMARY", "T_PROCESS", "T_QUIT", 
		"T_REMOVE", "T_SELECT", "T_SEMICOLON", "T_SET", "T_STOP", "T_STORAGE", 
		"T_TABLE", "T_TRIGGER", "T_TRUNCATE", "T_TYPE", "T_UDF", "T_UPDATE", "T_USE", 
		"T_USING", "T_VALUES", "U", "V", "W", "WS", "X", "Y", "Z"
	};
	public static final int EOF=-1;
	public static final int A=4;
	public static final int B=5;
	public static final int C=6;
	public static final int D=7;
	public static final int E=8;
	public static final int EXPONENT=9;
	public static final int F=10;
	public static final int G=11;
	public static final int H=12;
	public static final int I=13;
	public static final int J=14;
	public static final int K=15;
	public static final int L=16;
	public static final int M=17;
	public static final int N=18;
	public static final int O=19;
	public static final int P=20;
	public static final int POINT=21;
	public static final int Q=22;
	public static final int R=23;
	public static final int S=24;
	public static final int T=25;
	public static final int T_ADD=26;
	public static final int T_ALTER=27;
	public static final int T_ANALYTICS=28;
	public static final int T_AS=29;
	public static final int T_ASC=30;
	public static final int T_BETWEEN=31;
	public static final int T_CLUSTERING=32;
	public static final int T_COMPACT=33;
	public static final int T_CONSISTENCY=34;
	public static final int T_COUNT=35;
	public static final int T_CREATE=36;
	public static final int T_DATATYPES=37;
	public static final int T_DEFAULT=38;
	public static final int T_DELETE=39;
	public static final int T_DESC=40;
	public static final int T_DISABLE=41;
	public static final int T_DISTINCT=42;
	public static final int T_DROP=43;
	public static final int T_EXIT=44;
	public static final int T_EXPLAIN=45;
	public static final int T_FOR=46;
	public static final int T_FROM=47;
	public static final int T_HASH=48;
	public static final int T_HELP=49;
	public static final int T_INDEX=50;
	public static final int T_INSERT=51;
	public static final int T_INTO=52;
	public static final int T_KEY=53;
	public static final int T_KEYSPACE=54;
	public static final int T_LIKE=55;
	public static final int T_LIST=56;
	public static final int T_LUCENE=57;
	public static final int T_OPTIONS=58;
	public static final int T_ORDER=59;
	public static final int T_PLAN=60;
	public static final int T_PRIMARY=61;
	public static final int T_PROCESS=62;
	public static final int T_QUIT=63;
	public static final int T_REMOVE=64;
	public static final int T_SELECT=65;
	public static final int T_SEMICOLON=66;
	public static final int T_SET=67;
	public static final int T_STOP=68;
	public static final int T_STORAGE=69;
	public static final int T_TABLE=70;
	public static final int T_TRIGGER=71;
	public static final int T_TRUNCATE=72;
	public static final int T_TYPE=73;
	public static final int T_UDF=74;
	public static final int T_UPDATE=75;
	public static final int T_USE=76;
	public static final int T_USING=77;
	public static final int T_VALUES=78;
	public static final int U=79;
	public static final int V=80;
	public static final int W=81;
	public static final int WS=82;
	public static final int X=83;
	public static final int Y=84;
	public static final int Z=85;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public MetaHelpParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public MetaHelpParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return MetaHelpParser.tokenNames; }
	@Override public String getGrammarFileName() { return "MetaHelp.g"; }


		public void displayRecognitionError(String[] tokenNames, RecognitionException e){
			System.err.print("Error recognized: ");
			String hdr = getErrorHeader(e);
			String msg = getErrorMessage(e, tokenNames);
			System.err.print(hdr+": ");
			System.err.println(msg);
		}



	// $ANTLR start "alterHelpStatement"
	// MetaHelp.g:138:1: alterHelpStatement returns [HelpType type] : T_ALTER ( T_KEYSPACE | T_TABLE )? ;
	public final HelpType alterHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:138:43: ( T_ALTER ( T_KEYSPACE | T_TABLE )? )
			// MetaHelp.g:139:2: T_ALTER ( T_KEYSPACE | T_TABLE )?
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterHelpStatement1201); 
			type = HelpType.ALTER;
			// MetaHelp.g:140:2: ( T_KEYSPACE | T_TABLE )?
			int alt1=3;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==T_KEYSPACE) ) {
				alt1=1;
			}
			else if ( (LA1_0==T_TABLE) ) {
				alt1=2;
			}
			switch (alt1) {
				case 1 :
					// MetaHelp.g:141:3: T_KEYSPACE
					{
					match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterHelpStatement1210); 
					type = HelpType.ALTER_KEYSPACE;
					}
					break;
				case 2 :
					// MetaHelp.g:142:5: T_TABLE
					{
					match(input,T_TABLE,FOLLOW_T_TABLE_in_alterHelpStatement1218); 
					type = HelpType.ALTER_TABLE;
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
		return type;
	}
	// $ANTLR end "alterHelpStatement"



	// $ANTLR start "listHelpStatement"
	// MetaHelp.g:146:1: listHelpStatement returns [HelpType type] : T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )? ;
	public final HelpType listHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:146:42: ( T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )? )
			// MetaHelp.g:147:2: T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )?
			{
			match(input,T_LIST,FOLLOW_T_LIST_in_listHelpStatement1238); 
			type = HelpType.LIST;
			// MetaHelp.g:148:2: ( T_PROCESS | T_UDF | T_TRIGGER )?
			int alt2=4;
			switch ( input.LA(1) ) {
				case T_PROCESS:
					{
					alt2=1;
					}
					break;
				case T_UDF:
					{
					alt2=2;
					}
					break;
				case T_TRIGGER:
					{
					alt2=3;
					}
					break;
			}
			switch (alt2) {
				case 1 :
					// MetaHelp.g:149:3: T_PROCESS
					{
					match(input,T_PROCESS,FOLLOW_T_PROCESS_in_listHelpStatement1247); 
					type = HelpType.LIST_PROCESS;
					}
					break;
				case 2 :
					// MetaHelp.g:150:5: T_UDF
					{
					match(input,T_UDF,FOLLOW_T_UDF_in_listHelpStatement1255); 
					type = HelpType.LIST_UDF;
					}
					break;
				case 3 :
					// MetaHelp.g:151:5: T_TRIGGER
					{
					match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_listHelpStatement1263); 
					type = HelpType.LIST_TRIGGER;
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
		return type;
	}
	// $ANTLR end "listHelpStatement"



	// $ANTLR start "dropHelpStatement"
	// MetaHelp.g:155:1: dropHelpStatement returns [HelpType type] : T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )? ;
	public final HelpType dropHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:155:42: ( T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )? )
			// MetaHelp.g:156:2: T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )?
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropHelpStatement1283); 
			type = HelpType.DROP;
			// MetaHelp.g:157:2: ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )?
			int alt3=5;
			switch ( input.LA(1) ) {
				case T_KEYSPACE:
					{
					alt3=1;
					}
					break;
				case T_TABLE:
					{
					alt3=2;
					}
					break;
				case T_INDEX:
					{
					alt3=3;
					}
					break;
				case T_TRIGGER:
					{
					alt3=4;
					}
					break;
			}
			switch (alt3) {
				case 1 :
					// MetaHelp.g:158:3: T_KEYSPACE
					{
					match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropHelpStatement1292); 
					type = HelpType.DROP_KEYSPACE;
					}
					break;
				case 2 :
					// MetaHelp.g:159:5: T_TABLE
					{
					match(input,T_TABLE,FOLLOW_T_TABLE_in_dropHelpStatement1300); 
					type = HelpType.DROP_TABLE;
					}
					break;
				case 3 :
					// MetaHelp.g:160:5: T_INDEX
					{
					match(input,T_INDEX,FOLLOW_T_INDEX_in_dropHelpStatement1308); 
					type = HelpType.DROP_INDEX;
					}
					break;
				case 4 :
					// MetaHelp.g:161:5: T_TRIGGER
					{
					match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropHelpStatement1316); 
					type = HelpType.DROP_TRIGGER;
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
		return type;
	}
	// $ANTLR end "dropHelpStatement"



	// $ANTLR start "insertHelpStatement"
	// MetaHelp.g:165:1: insertHelpStatement returns [HelpType type] : T_INSERT ( T_INTO )? ;
	public final HelpType insertHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:165:44: ( T_INSERT ( T_INTO )? )
			// MetaHelp.g:166:2: T_INSERT ( T_INTO )?
			{
			match(input,T_INSERT,FOLLOW_T_INSERT_in_insertHelpStatement1336); 
			type = HelpType.INSERT_INTO;
			// MetaHelp.g:167:2: ( T_INTO )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==T_INTO) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// MetaHelp.g:168:3: T_INTO
					{
					match(input,T_INTO,FOLLOW_T_INTO_in_insertHelpStatement1345); 
					type = HelpType.INSERT_INTO;
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
		return type;
	}
	// $ANTLR end "insertHelpStatement"



	// $ANTLR start "createHelpStatement"
	// MetaHelp.g:172:1: createHelpStatement returns [HelpType type] : T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )? ;
	public final HelpType createHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:172:44: ( T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )? )
			// MetaHelp.g:173:2: T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createHelpStatement1365); 
			type = HelpType.CREATE;
			// MetaHelp.g:174:2: ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )?
			int alt5=6;
			switch ( input.LA(1) ) {
				case T_KEYSPACE:
					{
					alt5=1;
					}
					break;
				case T_TABLE:
					{
					alt5=2;
					}
					break;
				case T_INDEX:
					{
					alt5=3;
					}
					break;
				case T_DEFAULT:
					{
					alt5=4;
					}
					break;
				case T_LUCENE:
					{
					alt5=5;
					}
					break;
			}
			switch (alt5) {
				case 1 :
					// MetaHelp.g:175:3: T_KEYSPACE
					{
					match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createHelpStatement1374); 
					type = HelpType.CREATE_KEYSPACE;
					}
					break;
				case 2 :
					// MetaHelp.g:176:5: T_TABLE
					{
					match(input,T_TABLE,FOLLOW_T_TABLE_in_createHelpStatement1382); 
					type = HelpType.CREATE_TABLE;
					}
					break;
				case 3 :
					// MetaHelp.g:177:5: T_INDEX
					{
					match(input,T_INDEX,FOLLOW_T_INDEX_in_createHelpStatement1390); 
					type = HelpType.CREATE_INDEX;
					}
					break;
				case 4 :
					// MetaHelp.g:178:5: ( T_DEFAULT T_INDEX )
					{
					// MetaHelp.g:178:5: ( T_DEFAULT T_INDEX )
					// MetaHelp.g:178:6: T_DEFAULT T_INDEX
					{
					match(input,T_DEFAULT,FOLLOW_T_DEFAULT_in_createHelpStatement1399); 
					match(input,T_INDEX,FOLLOW_T_INDEX_in_createHelpStatement1401); 
					}

					type = HelpType.CREATE_INDEX;
					}
					break;
				case 5 :
					// MetaHelp.g:179:5: ( T_LUCENE T_INDEX )
					{
					// MetaHelp.g:179:5: ( T_LUCENE T_INDEX )
					// MetaHelp.g:179:6: T_LUCENE T_INDEX
					{
					match(input,T_LUCENE,FOLLOW_T_LUCENE_in_createHelpStatement1411); 
					match(input,T_INDEX,FOLLOW_T_INDEX_in_createHelpStatement1413); 
					}

					type = HelpType.CREATE_LUCENE_INDEX;
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
		return type;
	}
	// $ANTLR end "createHelpStatement"



	// $ANTLR start "helpStatement"
	// MetaHelp.g:183:1: helpStatement returns [HelpType type] : ( ( T_QUIT | T_EXIT ) | ( T_DATATYPES ) | (createType= createHelpStatement ) | ( T_UPDATE ) | (insertType= insertHelpStatement ) | ( T_TRUNCATE ) | (dropType= dropHelpStatement ) | ( T_SELECT ) | ( T_ADD ) | (listType= listHelpStatement ) | ( T_REMOVE T_UDF ) | ( T_DELETE ) | ( T_SET T_OPTIONS ) | ( T_EXPLAIN T_PLAN ) | (alterType= alterHelpStatement ) | ( T_STOP ) );
	public final HelpType helpStatement() throws RecognitionException {
		HelpType type = null;


		HelpType createType =null;
		HelpType insertType =null;
		HelpType dropType =null;
		HelpType listType =null;
		HelpType alterType =null;


				HelpType t = HelpType.CONSOLE_HELP;
			
		try {
			// MetaHelp.g:186:3: ( ( T_QUIT | T_EXIT ) | ( T_DATATYPES ) | (createType= createHelpStatement ) | ( T_UPDATE ) | (insertType= insertHelpStatement ) | ( T_TRUNCATE ) | (dropType= dropHelpStatement ) | ( T_SELECT ) | ( T_ADD ) | (listType= listHelpStatement ) | ( T_REMOVE T_UDF ) | ( T_DELETE ) | ( T_SET T_OPTIONS ) | ( T_EXPLAIN T_PLAN ) | (alterType= alterHelpStatement ) | ( T_STOP ) )
			int alt6=16;
			switch ( input.LA(1) ) {
			case T_EXIT:
			case T_QUIT:
				{
				alt6=1;
				}
				break;
			case T_DATATYPES:
				{
				alt6=2;
				}
				break;
			case T_CREATE:
				{
				alt6=3;
				}
				break;
			case T_UPDATE:
				{
				alt6=4;
				}
				break;
			case T_INSERT:
				{
				alt6=5;
				}
				break;
			case T_TRUNCATE:
				{
				alt6=6;
				}
				break;
			case T_DROP:
				{
				alt6=7;
				}
				break;
			case T_SELECT:
				{
				alt6=8;
				}
				break;
			case T_ADD:
				{
				alt6=9;
				}
				break;
			case T_LIST:
				{
				alt6=10;
				}
				break;
			case T_REMOVE:
				{
				alt6=11;
				}
				break;
			case T_DELETE:
				{
				alt6=12;
				}
				break;
			case T_SET:
				{
				alt6=13;
				}
				break;
			case T_EXPLAIN:
				{
				alt6=14;
				}
				break;
			case T_ALTER:
				{
				alt6=15;
				}
				break;
			case T_STOP:
				{
				alt6=16;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}
			switch (alt6) {
				case 1 :
					// MetaHelp.g:187:2: ( T_QUIT | T_EXIT )
					{
					if ( input.LA(1)==T_EXIT||input.LA(1)==T_QUIT ) {
						input.consume();
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					t = HelpType.EXIT;
					}
					break;
				case 2 :
					// MetaHelp.g:188:4: ( T_DATATYPES )
					{
					// MetaHelp.g:188:4: ( T_DATATYPES )
					// MetaHelp.g:188:5: T_DATATYPES
					{
					match(input,T_DATATYPES,FOLLOW_T_DATATYPES_in_helpStatement1453); 
					}

					t = HelpType.DATATYPES;
					}
					break;
				case 3 :
					// MetaHelp.g:189:4: (createType= createHelpStatement )
					{
					// MetaHelp.g:189:4: (createType= createHelpStatement )
					// MetaHelp.g:189:5: createType= createHelpStatement
					{
					pushFollow(FOLLOW_createHelpStatement_in_helpStatement1464);
					createType=createHelpStatement();
					state._fsp--;

					}

					t = createType;
					}
					break;
				case 4 :
					// MetaHelp.g:190:4: ( T_UPDATE )
					{
					// MetaHelp.g:190:4: ( T_UPDATE )
					// MetaHelp.g:190:5: T_UPDATE
					{
					match(input,T_UPDATE,FOLLOW_T_UPDATE_in_helpStatement1473); 
					}

					t = HelpType.UPDATE;
					}
					break;
				case 5 :
					// MetaHelp.g:191:4: (insertType= insertHelpStatement )
					{
					// MetaHelp.g:191:4: (insertType= insertHelpStatement )
					// MetaHelp.g:191:5: insertType= insertHelpStatement
					{
					pushFollow(FOLLOW_insertHelpStatement_in_helpStatement1484);
					insertType=insertHelpStatement();
					state._fsp--;

					}

					t = insertType;
					}
					break;
				case 6 :
					// MetaHelp.g:192:4: ( T_TRUNCATE )
					{
					// MetaHelp.g:192:4: ( T_TRUNCATE )
					// MetaHelp.g:192:5: T_TRUNCATE
					{
					match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_helpStatement1493); 
					}

					t = HelpType.TRUNCATE;
					}
					break;
				case 7 :
					// MetaHelp.g:193:4: (dropType= dropHelpStatement )
					{
					// MetaHelp.g:193:4: (dropType= dropHelpStatement )
					// MetaHelp.g:193:5: dropType= dropHelpStatement
					{
					pushFollow(FOLLOW_dropHelpStatement_in_helpStatement1504);
					dropType=dropHelpStatement();
					state._fsp--;

					}

					t = dropType;
					}
					break;
				case 8 :
					// MetaHelp.g:194:4: ( T_SELECT )
					{
					// MetaHelp.g:194:4: ( T_SELECT )
					// MetaHelp.g:194:5: T_SELECT
					{
					match(input,T_SELECT,FOLLOW_T_SELECT_in_helpStatement1513); 
					}

					t = HelpType.SELECT;
					}
					break;
				case 9 :
					// MetaHelp.g:195:4: ( T_ADD )
					{
					// MetaHelp.g:195:4: ( T_ADD )
					// MetaHelp.g:195:5: T_ADD
					{
					match(input,T_ADD,FOLLOW_T_ADD_in_helpStatement1522); 
					}

					t = HelpType.ADD;
					}
					break;
				case 10 :
					// MetaHelp.g:196:4: (listType= listHelpStatement )
					{
					// MetaHelp.g:196:4: (listType= listHelpStatement )
					// MetaHelp.g:196:5: listType= listHelpStatement
					{
					pushFollow(FOLLOW_listHelpStatement_in_helpStatement1533);
					listType=listHelpStatement();
					state._fsp--;

					}

					t = listType;
					}
					break;
				case 11 :
					// MetaHelp.g:197:4: ( T_REMOVE T_UDF )
					{
					// MetaHelp.g:197:4: ( T_REMOVE T_UDF )
					// MetaHelp.g:197:5: T_REMOVE T_UDF
					{
					match(input,T_REMOVE,FOLLOW_T_REMOVE_in_helpStatement1542); 
					match(input,T_UDF,FOLLOW_T_UDF_in_helpStatement1544); 
					}

					t = HelpType.REMOVE_UDF;
					}
					break;
				case 12 :
					// MetaHelp.g:198:4: ( T_DELETE )
					{
					// MetaHelp.g:198:4: ( T_DELETE )
					// MetaHelp.g:198:5: T_DELETE
					{
					match(input,T_DELETE,FOLLOW_T_DELETE_in_helpStatement1553); 
					}

					t = HelpType.DELETE;
					}
					break;
				case 13 :
					// MetaHelp.g:199:4: ( T_SET T_OPTIONS )
					{
					// MetaHelp.g:199:4: ( T_SET T_OPTIONS )
					// MetaHelp.g:199:5: T_SET T_OPTIONS
					{
					match(input,T_SET,FOLLOW_T_SET_in_helpStatement1562); 
					match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_helpStatement1564); 
					}

					t = HelpType.SET_OPTIONS;
					}
					break;
				case 14 :
					// MetaHelp.g:200:4: ( T_EXPLAIN T_PLAN )
					{
					// MetaHelp.g:200:4: ( T_EXPLAIN T_PLAN )
					// MetaHelp.g:200:5: T_EXPLAIN T_PLAN
					{
					match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_helpStatement1573); 
					match(input,T_PLAN,FOLLOW_T_PLAN_in_helpStatement1575); 
					}

					t = HelpType.EXPLAIN_PLAN;
					}
					break;
				case 15 :
					// MetaHelp.g:201:4: (alterType= alterHelpStatement )
					{
					// MetaHelp.g:201:4: (alterType= alterHelpStatement )
					// MetaHelp.g:201:5: alterType= alterHelpStatement
					{
					pushFollow(FOLLOW_alterHelpStatement_in_helpStatement1586);
					alterType=alterHelpStatement();
					state._fsp--;

					}

					t = alterType;
					}
					break;
				case 16 :
					// MetaHelp.g:202:4: ( T_STOP )
					{
					// MetaHelp.g:202:4: ( T_STOP )
					// MetaHelp.g:202:5: T_STOP
					{
					match(input,T_STOP,FOLLOW_T_STOP_in_helpStatement1595); 
					}

					t = HelpType.STOP;
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

					type = t;
				
		}
		return type;
	}
	// $ANTLR end "helpStatement"



	// $ANTLR start "query"
	// MetaHelp.g:208:1: query returns [HelpStatement st] : T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF ;
	public final HelpStatement query() throws RecognitionException {
		HelpStatement st = null;


		HelpType type =null;


				HelpType t = HelpType.CONSOLE_HELP;
			
		try {
			// MetaHelp.g:211:3: ( T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF )
			// MetaHelp.g:212:2: T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF
			{
			match(input,T_HELP,FOLLOW_T_HELP_in_query1621); 
			// MetaHelp.g:212:9: (type= helpStatement )?
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( ((LA7_0 >= T_ADD && LA7_0 <= T_ALTER)||(LA7_0 >= T_CREATE && LA7_0 <= T_DATATYPES)||LA7_0==T_DELETE||(LA7_0 >= T_DROP && LA7_0 <= T_EXPLAIN)||LA7_0==T_INSERT||LA7_0==T_LIST||(LA7_0 >= T_QUIT && LA7_0 <= T_SELECT)||(LA7_0 >= T_SET && LA7_0 <= T_STOP)||LA7_0==T_TRUNCATE||LA7_0==T_UPDATE) ) {
				alt7=1;
			}
			switch (alt7) {
				case 1 :
					// MetaHelp.g:212:10: type= helpStatement
					{
					pushFollow(FOLLOW_helpStatement_in_query1626);
					type=helpStatement();
					state._fsp--;

					t=type;
					}
					break;

			}

			// MetaHelp.g:212:41: ( T_SEMICOLON )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==T_SEMICOLON) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// MetaHelp.g:212:42: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query1633); 
					}
					break;

				default :
					break loop8;
				}
			}

			match(input,EOF,FOLLOW_EOF_in_query1637); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving

					st = new HelpStatement(t);
				
		}
		return st;
	}
	// $ANTLR end "query"

	// Delegated rules



	public static final BitSet FOLLOW_T_ALTER_in_alterHelpStatement1201 = new BitSet(new long[]{0x0040000000000002L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterHelpStatement1210 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TABLE_in_alterHelpStatement1218 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIST_in_listHelpStatement1238 = new BitSet(new long[]{0x4000000000000002L,0x0000000000000480L});
	public static final BitSet FOLLOW_T_PROCESS_in_listHelpStatement1247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_UDF_in_listHelpStatement1255 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRIGGER_in_listHelpStatement1263 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropHelpStatement1283 = new BitSet(new long[]{0x0044000000000002L,0x00000000000000C0L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropHelpStatement1292 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TABLE_in_dropHelpStatement1300 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INDEX_in_dropHelpStatement1308 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropHelpStatement1316 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INSERT_in_insertHelpStatement1336 = new BitSet(new long[]{0x0010000000000002L});
	public static final BitSet FOLLOW_T_INTO_in_insertHelpStatement1345 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createHelpStatement1365 = new BitSet(new long[]{0x0244004000000002L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createHelpStatement1374 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TABLE_in_createHelpStatement1382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1390 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DEFAULT_in_createHelpStatement1399 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1401 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LUCENE_in_createHelpStatement1411 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1413 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_helpStatement1439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DATATYPES_in_helpStatement1453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createHelpStatement_in_helpStatement1464 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_UPDATE_in_helpStatement1473 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_insertHelpStatement_in_helpStatement1484 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_helpStatement1493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropHelpStatement_in_helpStatement1504 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SELECT_in_helpStatement1513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ADD_in_helpStatement1522 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_listHelpStatement_in_helpStatement1533 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_REMOVE_in_helpStatement1542 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_UDF_in_helpStatement1544 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DELETE_in_helpStatement1553 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_helpStatement1562 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_helpStatement1564 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_helpStatement1573 = new BitSet(new long[]{0x1000000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_helpStatement1575 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterHelpStatement_in_helpStatement1586 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_STOP_in_helpStatement1595 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_HELP_in_query1621 = new BitSet(new long[]{0x810838B00C000000L,0x000000000000091FL});
	public static final BitSet FOLLOW_helpStatement_in_query1626 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query1633 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_EOF_in_query1637 = new BitSet(new long[]{0x0000000000000002L});
}
