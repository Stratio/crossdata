// $ANTLR 3.5.1 MetaHelp.g 2014-03-26 16:24:16

	package com.stratio.meta.sh.help.generated;

import com.stratio.meta.sh.help.HelpStatement;
import com.stratio.meta.sh.help.HelpType;
import org.antlr.runtime.*;

@SuppressWarnings("all")
public class MetaHelpParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "E", "EXPONENT", 
		"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "POINT", "Q", "R", 
		"S", "T", "T_ADD", "T_ALTER", "T_ANALYTICS", "T_AS", "T_ASC", "T_BETWEEN", 
		"T_CLUSTERING", "T_COMPACT", "T_CONSISTENCY", "T_COUNT", "T_CREATE", "T_DATATYPES", 
		"T_DEFAULT", "T_DELETE", "T_DESC", "T_DESCRIBE", "T_DISABLE", "T_DISTINCT", 
		"T_DROP", "T_EXIT", "T_EXPLAIN", "T_FOR", "T_FROM", "T_HASH", "T_HELP", 
		"T_INDEX", "T_INSERT", "T_INTO", "T_KEY", "T_KEYSPACE", "T_LIKE", "T_LIST", 
		"T_LUCENE", "T_OPTIONS", "T_ORDER", "T_PLAN", "T_PRIMARY", "T_PROCESS", 
		"T_QUIT", "T_REMOVE", "T_SELECT", "T_SEMICOLON", "T_SET", "T_STOP", "T_STORAGE", 
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
	public static final int T_DESCRIBE=41;
	public static final int T_DISABLE=42;
	public static final int T_DISTINCT=43;
	public static final int T_DROP=44;
	public static final int T_EXIT=45;
	public static final int T_EXPLAIN=46;
	public static final int T_FOR=47;
	public static final int T_FROM=48;
	public static final int T_HASH=49;
	public static final int T_HELP=50;
	public static final int T_INDEX=51;
	public static final int T_INSERT=52;
	public static final int T_INTO=53;
	public static final int T_KEY=54;
	public static final int T_KEYSPACE=55;
	public static final int T_LIKE=56;
	public static final int T_LIST=57;
	public static final int T_LUCENE=58;
	public static final int T_OPTIONS=59;
	public static final int T_ORDER=60;
	public static final int T_PLAN=61;
	public static final int T_PRIMARY=62;
	public static final int T_PROCESS=63;
	public static final int T_QUIT=64;
	public static final int T_REMOVE=65;
	public static final int T_SELECT=66;
	public static final int T_SEMICOLON=67;
	public static final int T_SET=68;
	public static final int T_STOP=69;
	public static final int T_STORAGE=70;
	public static final int T_TABLE=71;
	public static final int T_TRIGGER=72;
	public static final int T_TRUNCATE=73;
	public static final int T_TYPE=74;
	public static final int T_UDF=75;
	public static final int T_UPDATE=76;
	public static final int T_USE=77;
	public static final int T_USING=78;
	public static final int T_VALUES=79;
	public static final int U=80;
	public static final int V=81;
	public static final int W=82;
	public static final int WS=83;
	public static final int X=84;
	public static final int Y=85;
	public static final int Z=86;

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



	// $ANTLR start "describeHelpStatement"
	// MetaHelp.g:137:1: describeHelpStatement returns [HelpType type] : T_DESCRIBE ( T_KEYSPACE | T_TABLE )? ;
	public final HelpType describeHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:137:46: ( T_DESCRIBE ( T_KEYSPACE | T_TABLE )? )
			// MetaHelp.g:138:2: T_DESCRIBE ( T_KEYSPACE | T_TABLE )?
			{
			match(input,T_DESCRIBE,FOLLOW_T_DESCRIBE_in_describeHelpStatement1219); 
			type = HelpType.DESCRIBE;
			// MetaHelp.g:139:2: ( T_KEYSPACE | T_TABLE )?
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
					// MetaHelp.g:140:3: T_KEYSPACE
					{
					match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_describeHelpStatement1228); 
					type = HelpType.DESCRIBE_KEYSPACE;
					}
					break;
				case 2 :
					// MetaHelp.g:141:5: T_TABLE
					{
					match(input,T_TABLE,FOLLOW_T_TABLE_in_describeHelpStatement1236); 
					type = HelpType.DESCRIBE_TABLE;
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
	// $ANTLR end "describeHelpStatement"



	// $ANTLR start "alterHelpStatement"
	// MetaHelp.g:145:1: alterHelpStatement returns [HelpType type] : T_ALTER ( T_KEYSPACE | T_TABLE )? ;
	public final HelpType alterHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:145:43: ( T_ALTER ( T_KEYSPACE | T_TABLE )? )
			// MetaHelp.g:146:2: T_ALTER ( T_KEYSPACE | T_TABLE )?
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterHelpStatement1256); 
			type = HelpType.ALTER;
			// MetaHelp.g:147:2: ( T_KEYSPACE | T_TABLE )?
			int alt2=3;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==T_KEYSPACE) ) {
				alt2=1;
			}
			else if ( (LA2_0==T_TABLE) ) {
				alt2=2;
			}
			switch (alt2) {
				case 1 :
					// MetaHelp.g:148:3: T_KEYSPACE
					{
					match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterHelpStatement1265); 
					type = HelpType.ALTER_KEYSPACE;
					}
					break;
				case 2 :
					// MetaHelp.g:149:5: T_TABLE
					{
					match(input,T_TABLE,FOLLOW_T_TABLE_in_alterHelpStatement1273); 
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
	// MetaHelp.g:153:1: listHelpStatement returns [HelpType type] : T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )? ;
	public final HelpType listHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:153:42: ( T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )? )
			// MetaHelp.g:154:2: T_LIST ( T_PROCESS | T_UDF | T_TRIGGER )?
			{
			match(input,T_LIST,FOLLOW_T_LIST_in_listHelpStatement1293); 
			type = HelpType.LIST;
			// MetaHelp.g:155:2: ( T_PROCESS | T_UDF | T_TRIGGER )?
			int alt3=4;
			switch ( input.LA(1) ) {
				case T_PROCESS:
					{
					alt3=1;
					}
					break;
				case T_UDF:
					{
					alt3=2;
					}
					break;
				case T_TRIGGER:
					{
					alt3=3;
					}
					break;
			}
			switch (alt3) {
				case 1 :
					// MetaHelp.g:156:3: T_PROCESS
					{
					match(input,T_PROCESS,FOLLOW_T_PROCESS_in_listHelpStatement1302); 
					type = HelpType.LIST_PROCESS;
					}
					break;
				case 2 :
					// MetaHelp.g:157:5: T_UDF
					{
					match(input,T_UDF,FOLLOW_T_UDF_in_listHelpStatement1310); 
					type = HelpType.LIST_UDF;
					}
					break;
				case 3 :
					// MetaHelp.g:158:5: T_TRIGGER
					{
					match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_listHelpStatement1318); 
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
	// MetaHelp.g:162:1: dropHelpStatement returns [HelpType type] : T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )? ;
	public final HelpType dropHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:162:42: ( T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )? )
			// MetaHelp.g:163:2: T_DROP ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )?
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropHelpStatement1338); 
			type = HelpType.DROP;
			// MetaHelp.g:164:2: ( T_KEYSPACE | T_TABLE | T_INDEX | T_TRIGGER )?
			int alt4=5;
			switch ( input.LA(1) ) {
				case T_KEYSPACE:
					{
					alt4=1;
					}
					break;
				case T_TABLE:
					{
					alt4=2;
					}
					break;
				case T_INDEX:
					{
					alt4=3;
					}
					break;
				case T_TRIGGER:
					{
					alt4=4;
					}
					break;
			}
			switch (alt4) {
				case 1 :
					// MetaHelp.g:165:3: T_KEYSPACE
					{
					match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropHelpStatement1347); 
					type = HelpType.DROP_KEYSPACE;
					}
					break;
				case 2 :
					// MetaHelp.g:166:5: T_TABLE
					{
					match(input,T_TABLE,FOLLOW_T_TABLE_in_dropHelpStatement1355); 
					type = HelpType.DROP_TABLE;
					}
					break;
				case 3 :
					// MetaHelp.g:167:5: T_INDEX
					{
					match(input,T_INDEX,FOLLOW_T_INDEX_in_dropHelpStatement1363); 
					type = HelpType.DROP_INDEX;
					}
					break;
				case 4 :
					// MetaHelp.g:168:5: T_TRIGGER
					{
					match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropHelpStatement1371); 
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
	// MetaHelp.g:172:1: insertHelpStatement returns [HelpType type] : T_INSERT ( T_INTO )? ;
	public final HelpType insertHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:172:44: ( T_INSERT ( T_INTO )? )
			// MetaHelp.g:173:2: T_INSERT ( T_INTO )?
			{
			match(input,T_INSERT,FOLLOW_T_INSERT_in_insertHelpStatement1391); 
			type = HelpType.INSERT_INTO;
			// MetaHelp.g:174:2: ( T_INTO )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==T_INTO) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// MetaHelp.g:175:3: T_INTO
					{
					match(input,T_INTO,FOLLOW_T_INTO_in_insertHelpStatement1400); 
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
	// MetaHelp.g:179:1: createHelpStatement returns [HelpType type] : T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )? ;
	public final HelpType createHelpStatement() throws RecognitionException {
		HelpType type = null;


		try {
			// MetaHelp.g:179:44: ( T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )? )
			// MetaHelp.g:180:2: T_CREATE ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createHelpStatement1420); 
			type = HelpType.CREATE;
			// MetaHelp.g:181:2: ( T_KEYSPACE | T_TABLE | T_INDEX | ( T_DEFAULT T_INDEX ) | ( T_LUCENE T_INDEX ) )?
			int alt6=6;
			switch ( input.LA(1) ) {
				case T_KEYSPACE:
					{
					alt6=1;
					}
					break;
				case T_TABLE:
					{
					alt6=2;
					}
					break;
				case T_INDEX:
					{
					alt6=3;
					}
					break;
				case T_DEFAULT:
					{
					alt6=4;
					}
					break;
				case T_LUCENE:
					{
					alt6=5;
					}
					break;
			}
			switch (alt6) {
				case 1 :
					// MetaHelp.g:182:3: T_KEYSPACE
					{
					match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createHelpStatement1429); 
					type = HelpType.CREATE_KEYSPACE;
					}
					break;
				case 2 :
					// MetaHelp.g:183:5: T_TABLE
					{
					match(input,T_TABLE,FOLLOW_T_TABLE_in_createHelpStatement1437); 
					type = HelpType.CREATE_TABLE;
					}
					break;
				case 3 :
					// MetaHelp.g:184:5: T_INDEX
					{
					match(input,T_INDEX,FOLLOW_T_INDEX_in_createHelpStatement1445); 
					type = HelpType.CREATE_INDEX;
					}
					break;
				case 4 :
					// MetaHelp.g:185:5: ( T_DEFAULT T_INDEX )
					{
					// MetaHelp.g:185:5: ( T_DEFAULT T_INDEX )
					// MetaHelp.g:185:6: T_DEFAULT T_INDEX
					{
					match(input,T_DEFAULT,FOLLOW_T_DEFAULT_in_createHelpStatement1454); 
					match(input,T_INDEX,FOLLOW_T_INDEX_in_createHelpStatement1456); 
					}

					type = HelpType.CREATE_INDEX;
					}
					break;
				case 5 :
					// MetaHelp.g:186:5: ( T_LUCENE T_INDEX )
					{
					// MetaHelp.g:186:5: ( T_LUCENE T_INDEX )
					// MetaHelp.g:186:6: T_LUCENE T_INDEX
					{
					match(input,T_LUCENE,FOLLOW_T_LUCENE_in_createHelpStatement1466); 
					match(input,T_INDEX,FOLLOW_T_INDEX_in_createHelpStatement1468); 
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
	// MetaHelp.g:190:1: helpStatement returns [HelpType type] : ( ( T_QUIT | T_EXIT ) | ( T_DATATYPES ) | (createType= createHelpStatement ) | ( T_UPDATE ) | (insertType= insertHelpStatement ) | ( T_TRUNCATE ) | (dropType= dropHelpStatement ) | ( T_SELECT ) | ( T_ADD ) | (listType= listHelpStatement ) | ( T_REMOVE T_UDF ) | ( T_DELETE ) | ( T_SET T_OPTIONS ) | ( T_EXPLAIN T_PLAN ) | (alterType= alterHelpStatement ) | ( T_STOP ) | (describeType= describeHelpStatement ) );
	public final HelpType helpStatement() throws RecognitionException {
		HelpType type = null;


		HelpType createType =null;
		HelpType insertType =null;
		HelpType dropType =null;
		HelpType listType =null;
		HelpType alterType =null;
		HelpType describeType =null;


				HelpType t = HelpType.CONSOLE_HELP;
			
		try {
			// MetaHelp.g:193:3: ( ( T_QUIT | T_EXIT ) | ( T_DATATYPES ) | (createType= createHelpStatement ) | ( T_UPDATE ) | (insertType= insertHelpStatement ) | ( T_TRUNCATE ) | (dropType= dropHelpStatement ) | ( T_SELECT ) | ( T_ADD ) | (listType= listHelpStatement ) | ( T_REMOVE T_UDF ) | ( T_DELETE ) | ( T_SET T_OPTIONS ) | ( T_EXPLAIN T_PLAN ) | (alterType= alterHelpStatement ) | ( T_STOP ) | (describeType= describeHelpStatement ) )
			int alt7=17;
			switch ( input.LA(1) ) {
			case T_EXIT:
			case T_QUIT:
				{
				alt7=1;
				}
				break;
			case T_DATATYPES:
				{
				alt7=2;
				}
				break;
			case T_CREATE:
				{
				alt7=3;
				}
				break;
			case T_UPDATE:
				{
				alt7=4;
				}
				break;
			case T_INSERT:
				{
				alt7=5;
				}
				break;
			case T_TRUNCATE:
				{
				alt7=6;
				}
				break;
			case T_DROP:
				{
				alt7=7;
				}
				break;
			case T_SELECT:
				{
				alt7=8;
				}
				break;
			case T_ADD:
				{
				alt7=9;
				}
				break;
			case T_LIST:
				{
				alt7=10;
				}
				break;
			case T_REMOVE:
				{
				alt7=11;
				}
				break;
			case T_DELETE:
				{
				alt7=12;
				}
				break;
			case T_SET:
				{
				alt7=13;
				}
				break;
			case T_EXPLAIN:
				{
				alt7=14;
				}
				break;
			case T_ALTER:
				{
				alt7=15;
				}
				break;
			case T_STOP:
				{
				alt7=16;
				}
				break;
			case T_DESCRIBE:
				{
				alt7=17;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}
			switch (alt7) {
				case 1 :
					// MetaHelp.g:194:2: ( T_QUIT | T_EXIT )
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
					// MetaHelp.g:195:4: ( T_DATATYPES )
					{
					// MetaHelp.g:195:4: ( T_DATATYPES )
					// MetaHelp.g:195:5: T_DATATYPES
					{
					match(input,T_DATATYPES,FOLLOW_T_DATATYPES_in_helpStatement1508); 
					}

					t = HelpType.DATATYPES;
					}
					break;
				case 3 :
					// MetaHelp.g:196:4: (createType= createHelpStatement )
					{
					// MetaHelp.g:196:4: (createType= createHelpStatement )
					// MetaHelp.g:196:5: createType= createHelpStatement
					{
					pushFollow(FOLLOW_createHelpStatement_in_helpStatement1519);
					createType=createHelpStatement();
					state._fsp--;

					}

					t = createType;
					}
					break;
				case 4 :
					// MetaHelp.g:197:4: ( T_UPDATE )
					{
					// MetaHelp.g:197:4: ( T_UPDATE )
					// MetaHelp.g:197:5: T_UPDATE
					{
					match(input,T_UPDATE,FOLLOW_T_UPDATE_in_helpStatement1528); 
					}

					t = HelpType.UPDATE;
					}
					break;
				case 5 :
					// MetaHelp.g:198:4: (insertType= insertHelpStatement )
					{
					// MetaHelp.g:198:4: (insertType= insertHelpStatement )
					// MetaHelp.g:198:5: insertType= insertHelpStatement
					{
					pushFollow(FOLLOW_insertHelpStatement_in_helpStatement1539);
					insertType=insertHelpStatement();
					state._fsp--;

					}

					t = insertType;
					}
					break;
				case 6 :
					// MetaHelp.g:199:4: ( T_TRUNCATE )
					{
					// MetaHelp.g:199:4: ( T_TRUNCATE )
					// MetaHelp.g:199:5: T_TRUNCATE
					{
					match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_helpStatement1548); 
					}

					t = HelpType.TRUNCATE;
					}
					break;
				case 7 :
					// MetaHelp.g:200:4: (dropType= dropHelpStatement )
					{
					// MetaHelp.g:200:4: (dropType= dropHelpStatement )
					// MetaHelp.g:200:5: dropType= dropHelpStatement
					{
					pushFollow(FOLLOW_dropHelpStatement_in_helpStatement1559);
					dropType=dropHelpStatement();
					state._fsp--;

					}

					t = dropType;
					}
					break;
				case 8 :
					// MetaHelp.g:201:4: ( T_SELECT )
					{
					// MetaHelp.g:201:4: ( T_SELECT )
					// MetaHelp.g:201:5: T_SELECT
					{
					match(input,T_SELECT,FOLLOW_T_SELECT_in_helpStatement1568); 
					}

					t = HelpType.SELECT;
					}
					break;
				case 9 :
					// MetaHelp.g:202:4: ( T_ADD )
					{
					// MetaHelp.g:202:4: ( T_ADD )
					// MetaHelp.g:202:5: T_ADD
					{
					match(input,T_ADD,FOLLOW_T_ADD_in_helpStatement1577); 
					}

					t = HelpType.ADD;
					}
					break;
				case 10 :
					// MetaHelp.g:203:4: (listType= listHelpStatement )
					{
					// MetaHelp.g:203:4: (listType= listHelpStatement )
					// MetaHelp.g:203:5: listType= listHelpStatement
					{
					pushFollow(FOLLOW_listHelpStatement_in_helpStatement1588);
					listType=listHelpStatement();
					state._fsp--;

					}

					t = listType;
					}
					break;
				case 11 :
					// MetaHelp.g:204:4: ( T_REMOVE T_UDF )
					{
					// MetaHelp.g:204:4: ( T_REMOVE T_UDF )
					// MetaHelp.g:204:5: T_REMOVE T_UDF
					{
					match(input,T_REMOVE,FOLLOW_T_REMOVE_in_helpStatement1597); 
					match(input,T_UDF,FOLLOW_T_UDF_in_helpStatement1599); 
					}

					t = HelpType.REMOVE_UDF;
					}
					break;
				case 12 :
					// MetaHelp.g:205:4: ( T_DELETE )
					{
					// MetaHelp.g:205:4: ( T_DELETE )
					// MetaHelp.g:205:5: T_DELETE
					{
					match(input,T_DELETE,FOLLOW_T_DELETE_in_helpStatement1608); 
					}

					t = HelpType.DELETE;
					}
					break;
				case 13 :
					// MetaHelp.g:206:4: ( T_SET T_OPTIONS )
					{
					// MetaHelp.g:206:4: ( T_SET T_OPTIONS )
					// MetaHelp.g:206:5: T_SET T_OPTIONS
					{
					match(input,T_SET,FOLLOW_T_SET_in_helpStatement1617); 
					match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_helpStatement1619); 
					}

					t = HelpType.SET_OPTIONS;
					}
					break;
				case 14 :
					// MetaHelp.g:207:4: ( T_EXPLAIN T_PLAN )
					{
					// MetaHelp.g:207:4: ( T_EXPLAIN T_PLAN )
					// MetaHelp.g:207:5: T_EXPLAIN T_PLAN
					{
					match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_helpStatement1628); 
					match(input,T_PLAN,FOLLOW_T_PLAN_in_helpStatement1630); 
					}

					t = HelpType.EXPLAIN_PLAN;
					}
					break;
				case 15 :
					// MetaHelp.g:208:4: (alterType= alterHelpStatement )
					{
					// MetaHelp.g:208:4: (alterType= alterHelpStatement )
					// MetaHelp.g:208:5: alterType= alterHelpStatement
					{
					pushFollow(FOLLOW_alterHelpStatement_in_helpStatement1641);
					alterType=alterHelpStatement();
					state._fsp--;

					}

					t = alterType;
					}
					break;
				case 16 :
					// MetaHelp.g:209:4: ( T_STOP )
					{
					// MetaHelp.g:209:4: ( T_STOP )
					// MetaHelp.g:209:5: T_STOP
					{
					match(input,T_STOP,FOLLOW_T_STOP_in_helpStatement1650); 
					}

					t = HelpType.STOP;
					}
					break;
				case 17 :
					// MetaHelp.g:210:11: (describeType= describeHelpStatement )
					{
					// MetaHelp.g:210:11: (describeType= describeHelpStatement )
					// MetaHelp.g:210:12: describeType= describeHelpStatement
					{
					pushFollow(FOLLOW_describeHelpStatement_in_helpStatement1668);
					describeType=describeHelpStatement();
					state._fsp--;

					}

					t = describeType;
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
	// MetaHelp.g:216:1: query returns [HelpStatement st] : T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF ;
	public final HelpStatement query() throws RecognitionException {
		HelpStatement st = null;


		HelpType type =null;


				HelpType t = HelpType.CONSOLE_HELP;
			
		try {
			// MetaHelp.g:219:3: ( T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF )
			// MetaHelp.g:220:2: T_HELP (type= helpStatement )? ( T_SEMICOLON )* EOF
			{
			match(input,T_HELP,FOLLOW_T_HELP_in_query1694); 
			// MetaHelp.g:220:9: (type= helpStatement )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( ((LA8_0 >= T_ADD && LA8_0 <= T_ALTER)||(LA8_0 >= T_CREATE && LA8_0 <= T_DATATYPES)||LA8_0==T_DELETE||LA8_0==T_DESCRIBE||(LA8_0 >= T_DROP && LA8_0 <= T_EXPLAIN)||LA8_0==T_INSERT||LA8_0==T_LIST||(LA8_0 >= T_QUIT && LA8_0 <= T_SELECT)||(LA8_0 >= T_SET && LA8_0 <= T_STOP)||LA8_0==T_TRUNCATE||LA8_0==T_UPDATE) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// MetaHelp.g:220:10: type= helpStatement
					{
					pushFollow(FOLLOW_helpStatement_in_query1699);
					type=helpStatement();
					state._fsp--;

					t=type;
					}
					break;

			}

			// MetaHelp.g:220:41: ( T_SEMICOLON )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==T_SEMICOLON) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// MetaHelp.g:220:42: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query1706); 
					}
					break;

				default :
					break loop9;
				}
			}

			match(input,EOF,FOLLOW_EOF_in_query1710); 
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



	public static final BitSet FOLLOW_T_DESCRIBE_in_describeHelpStatement1219 = new BitSet(new long[]{0x0080000000000002L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_describeHelpStatement1228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TABLE_in_describeHelpStatement1236 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterHelpStatement1256 = new BitSet(new long[]{0x0080000000000002L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterHelpStatement1265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TABLE_in_alterHelpStatement1273 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIST_in_listHelpStatement1293 = new BitSet(new long[]{0x8000000000000002L,0x0000000000000900L});
	public static final BitSet FOLLOW_T_PROCESS_in_listHelpStatement1302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_UDF_in_listHelpStatement1310 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRIGGER_in_listHelpStatement1318 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropHelpStatement1338 = new BitSet(new long[]{0x0088000000000002L,0x0000000000000180L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropHelpStatement1347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TABLE_in_dropHelpStatement1355 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INDEX_in_dropHelpStatement1363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropHelpStatement1371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INSERT_in_insertHelpStatement1391 = new BitSet(new long[]{0x0020000000000002L});
	public static final BitSet FOLLOW_T_INTO_in_insertHelpStatement1400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createHelpStatement1420 = new BitSet(new long[]{0x0488004000000002L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createHelpStatement1429 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TABLE_in_createHelpStatement1437 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DEFAULT_in_createHelpStatement1454 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1456 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LUCENE_in_createHelpStatement1466 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_INDEX_in_createHelpStatement1468 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_helpStatement1494 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DATATYPES_in_helpStatement1508 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createHelpStatement_in_helpStatement1519 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_UPDATE_in_helpStatement1528 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_insertHelpStatement_in_helpStatement1539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_helpStatement1548 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropHelpStatement_in_helpStatement1559 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SELECT_in_helpStatement1568 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ADD_in_helpStatement1577 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_listHelpStatement_in_helpStatement1588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_REMOVE_in_helpStatement1597 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_UDF_in_helpStatement1599 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DELETE_in_helpStatement1608 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_helpStatement1617 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_helpStatement1619 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_helpStatement1628 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_helpStatement1630 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterHelpStatement_in_helpStatement1641 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_STOP_in_helpStatement1650 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_describeHelpStatement_in_helpStatement1668 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_HELP_in_query1694 = new BitSet(new long[]{0x021072B00C000000L,0x000000000000123FL});
	public static final BitSet FOLLOW_helpStatement_in_query1699 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query1706 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_EOF_in_query1710 = new BitSet(new long[]{0x0000000000000002L});
}
