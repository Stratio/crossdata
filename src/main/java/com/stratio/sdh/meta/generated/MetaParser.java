<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
// $ANTLR 3.5.1 Meta.g 2014-02-07 10:17:30
=======
// $ANTLR 3.5.1 Meta.g 2014-02-07 23:33:24
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

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


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "A", "B", "C", "D", "DIGIT", "E", 
		"F", "G", "H", "I", "J", "K", "L", "LETTER", "M", "N", "O", "P", "Q", 
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
		"R", "S", "T", "T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", "T_ANY", "T_COLON", 
		"T_COMMA", "T_CONSISTENCY", "T_CONSTANT", "T_CREATE", "T_DROP", "T_EACH_QUORUM", 
		"T_END_SBRACKET", "T_EQUAL", "T_EXISTS", "T_EXPLAIN", "T_FALSE", "T_FOR", 
		"T_IDENT", "T_IF", "T_KEYSPACE", "T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_NOT", 
		"T_ON", "T_ONE", "T_OPTIONS", "T_PLAN", "T_POINT", "T_PROCESS", "T_QUORUM", 
		"T_SEMICOLON", "T_SET", "T_START_SBRACKET", "T_STOP", "T_TABLE", "T_TERM", 
		"T_THREE", "T_TRIGGER", "T_TRUE", "T_TRUNCATE", "T_TWO", "T_USE", "T_USING", 
		"T_WITH", "U", "V", "W", "WS", "X", "Y", "Z"
=======
		"R", "S", "T", "T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", "T_ANY", "T_CLUSTERING", 
		"T_COLON", "T_COMMA", "T_COMPACT", "T_CONSISTENCY", "T_CONSTANT", "T_CREATE", 
		"T_DROP", "T_EACH_QUORUM", "T_END_BRACKET", "T_END_SBRACKET", "T_EQUAL", 
		"T_EXISTS", "T_EXPLAIN", "T_FALSE", "T_FOR", "T_IDENT", "T_IF", "T_INSERT", 
		"T_INTO", "T_KEYSPACE", "T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_NOT", "T_ONE", 
		"T_OPTIONS", "T_ORDER", "T_PLAN", "T_POINT", "T_QUORUM", "T_SELECT", "T_SEMICOLON", 
		"T_SET", "T_START_BRACKET", "T_START_SBRACKET", "T_STORAGE", "T_TABLE", 
		"T_TERM", "T_THREE", "T_TRUE", "T_TRUNCATE", "T_TWO", "T_USE", "T_USING", 
		"T_VALUES", "T_WITH", "U", "V", "W", "WS", "X", "Y", "Z"
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
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
	public static final int T_USING=69;
	public static final int T_WITH=70;
	public static final int U=71;
	public static final int V=72;
	public static final int W=73;
	public static final int WS=74;
	public static final int X=75;
	public static final int Y=76;
	public static final int Z=77;
=======
	public static final int T_CLUSTERING=31;
	public static final int T_COLON=32;
	public static final int T_COMMA=33;
	public static final int T_COMPACT=34;
	public static final int T_CONSISTENCY=35;
	public static final int T_CONSTANT=36;
	public static final int T_CREATE=37;
	public static final int T_DROP=38;
	public static final int T_EACH_QUORUM=39;
	public static final int T_END_BRACKET=40;
	public static final int T_END_SBRACKET=41;
	public static final int T_EQUAL=42;
	public static final int T_EXISTS=43;
	public static final int T_EXPLAIN=44;
	public static final int T_FALSE=45;
	public static final int T_FOR=46;
	public static final int T_IDENT=47;
	public static final int T_IF=48;
	public static final int T_INSERT=49;
	public static final int T_INTO=50;
	public static final int T_KEYSPACE=51;
	public static final int T_LOCAL_ONE=52;
	public static final int T_LOCAL_QUORUM=53;
	public static final int T_NOT=54;
	public static final int T_ONE=55;
	public static final int T_OPTIONS=56;
	public static final int T_ORDER=57;
	public static final int T_PLAN=58;
	public static final int T_POINT=59;
	public static final int T_QUORUM=60;
	public static final int T_SELECT=61;
	public static final int T_SEMICOLON=62;
	public static final int T_SET=63;
	public static final int T_START_BRACKET=64;
	public static final int T_START_SBRACKET=65;
	public static final int T_STORAGE=66;
	public static final int T_TABLE=67;
	public static final int T_TERM=68;
	public static final int T_THREE=69;
	public static final int T_TRUE=70;
	public static final int T_TRUNCATE=71;
	public static final int T_TWO=72;
	public static final int T_USE=73;
	public static final int T_USING=74;
	public static final int T_VALUES=75;
	public static final int T_WITH=76;
	public static final int U=77;
	public static final int V=78;
	public static final int W=79;
	public static final int WS=80;
	public static final int X=81;
	public static final int Y=82;
	public static final int Z=83;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

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



<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// $ANTLR start "stopProcessStatement"
	// Meta.g:154:1: stopProcessStatement returns [StopProcessStatement stprst] : T_STOP T_PROCESS ident= T_IDENT ;
	public final StopProcessStatement stopProcessStatement() throws RecognitionException {
		StopProcessStatement stprst = null;


		Token ident=null;

		try {
			// Meta.g:154:59: ( T_STOP T_PROCESS ident= T_IDENT )
			// Meta.g:155:5: T_STOP T_PROCESS ident= T_IDENT
			{
			match(input,T_STOP,FOLLOW_T_STOP_in_stopProcessStatement1032); 
			match(input,T_PROCESS,FOLLOW_T_PROCESS_in_stopProcessStatement1034); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_stopProcessStatement1038); 
			 stprst = new StopProcessStatement((ident!=null?ident.getText():null)); 
=======
	// $ANTLR start "selectStatement"
	// Meta.g:161:1: selectStatement returns [SelectStatement slctst] : T_SELECT ;
	public final SelectStatement selectStatement() throws RecognitionException {
		SelectStatement slctst = null;


		try {
			// Meta.g:161:49: ( T_SELECT )
			// Meta.g:162:5: T_SELECT
			{
			match(input,T_SELECT,FOLLOW_T_SELECT_in_selectStatement1120); 
			slctst = new SelectStatement();
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
		return stprst;
	}
	// $ANTLR end "stopProcessStatement"



	// $ANTLR start "dropTriggerStatement"
	// Meta.g:159:1: dropTriggerStatement returns [DropTriggerStatement drtrst] : T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT ;
	public final DropTriggerStatement dropTriggerStatement() throws RecognitionException {
		DropTriggerStatement drtrst = null;


		Token ident=null;
		Token ident2=null;

		try {
			// Meta.g:159:59: ( T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT )
			// Meta.g:160:5: T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTriggerStatement1061); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropTriggerStatement1068); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement1072); 
			match(input,T_ON,FOLLOW_T_ON_in_dropTriggerStatement1079); 
			ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement1088); 
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
	// Meta.g:167:1: createTriggerStatement returns [CreateTriggerStatement crtrst] : T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT ;
	public final CreateTriggerStatement createTriggerStatement() throws RecognitionException {
		CreateTriggerStatement crtrst = null;


		Token trigger_name=null;
		Token table_name=null;
		Token class_name=null;

		try {
			// Meta.g:167:63: ( T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT )
			// Meta.g:168:5: T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTriggerStatement1116); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_createTriggerStatement1123); 
			trigger_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement1127); 
			match(input,T_ON,FOLLOW_T_ON_in_createTriggerStatement1134); 
			table_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement1143); 
			match(input,T_USING,FOLLOW_T_USING_in_createTriggerStatement1149); 
			class_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement1153); 
			crtrst = new CreateTriggerStatement((trigger_name!=null?trigger_name.getText():null),(table_name!=null?table_name.getText():null),(class_name!=null?class_name.getText():null));
=======
		return slctst;
	}
	// $ANTLR end "selectStatement"



	// $ANTLR start "insertIntoStatement"
	// Meta.g:165:1: insertIntoStatement returns [InsertIntoStatement nsntst] : T_INSERT T_INTO tableName= getTableID T_START_BRACKET ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_BRACKET (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? ;
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
			// Meta.g:173:6: ( T_INSERT T_INTO tableName= getTableID T_START_BRACKET ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_BRACKET (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? )
			// Meta.g:174:5: T_INSERT T_INTO tableName= getTableID T_START_BRACKET ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_BRACKET (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			{
			match(input,T_INSERT,FOLLOW_T_INSERT_in_insertIntoStatement1150); 
			match(input,T_INTO,FOLLOW_T_INTO_in_insertIntoStatement1157); 
			pushFollow(FOLLOW_getTableID_in_insertIntoStatement1166);
			tableName=getTableID();
			state._fsp--;

			match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_insertIntoStatement1172); 
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement1181); 
			ids.add((ident1!=null?ident1.getText():null));
			// Meta.g:179:5: ( T_COMMA identN= T_IDENT )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==T_COMMA) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// Meta.g:179:6: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement1191); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement1195); 
					ids.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop1;
				}
			}

			match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_insertIntoStatement1206); 
			// Meta.g:181:5: (selectStmnt= selectStatement | T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET )
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==T_SELECT) ) {
				alt3=1;
			}
			else if ( (LA3_0==T_VALUES) ) {
				alt3=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}

			switch (alt3) {
				case 1 :
					// Meta.g:182:9: selectStmnt= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_insertIntoStatement1225);
					selectStmnt=selectStatement();
					state._fsp--;

					typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;
					}
					break;
				case 2 :
					// Meta.g:184:9: T_VALUES T_START_BRACKET term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_BRACKET
					{
					match(input,T_VALUES,FOLLOW_T_VALUES_in_insertIntoStatement1248); 
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_insertIntoStatement1258); 
					pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement1275);
					term1=getTermOrLiteral();
					state._fsp--;

					cellValues.add(term1);
					// Meta.g:187:13: ( T_COMMA termN= getTermOrLiteral )*
					loop2:
					while (true) {
						int alt2=2;
						int LA2_0 = input.LA(1);
						if ( (LA2_0==T_COMMA) ) {
							alt2=1;
						}

						switch (alt2) {
						case 1 :
							// Meta.g:187:14: T_COMMA termN= getTermOrLiteral
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement1292); 
							pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement1296);
							termN=getTermOrLiteral();
							state._fsp--;

							cellValues.add(termN);
							}
							break;

						default :
							break loop2;
						}
					}

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_insertIntoStatement1310); 
					}
					break;

			}

			// Meta.g:190:5: ( T_IF T_NOT T_EXISTS )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==T_IF) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// Meta.g:190:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_insertIntoStatement1323); 
					match(input,T_NOT,FOLLOW_T_NOT_in_insertIntoStatement1325); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_insertIntoStatement1327); 
					ifNotExists=true;
					}
					break;

			}

			// Meta.g:191:5: ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==T_USING) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Meta.g:192:9: T_USING opt1= getOption ( T_AND optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_insertIntoStatement1348); 
					optsInc=true;
					pushFollow(FOLLOW_getOption_in_insertIntoStatement1363);
					opt1=getOption();
					state._fsp--;


					            options.add(opt1);
					        
					// Meta.g:196:9: ( T_AND optN= getOption )*
					loop5:
					while (true) {
						int alt5=2;
						int LA5_0 = input.LA(1);
						if ( (LA5_0==T_AND) ) {
							alt5=1;
						}

						switch (alt5) {
						case 1 :
							// Meta.g:196:10: T_AND optN= getOption
							{
							match(input,T_AND,FOLLOW_T_AND_in_insertIntoStatement1376); 
							pushFollow(FOLLOW_getOption_in_insertIntoStatement1380);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop5;
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
			                
			    
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
		return crtrst;
	}
	// $ANTLR end "createTriggerStatement"
=======
		return nsntst;
	}
	// $ANTLR end "insertIntoStatement"
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c



	// $ANTLR start "explainPlanStatement"
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:175:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
=======
	// Meta.g:213:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		Statement parsedStmnt =null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:175:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:176:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement1182); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement1184); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement1186); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement1190);
=======
			// Meta.g:213:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:214:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement1417); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement1419); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement1421); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement1425);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:180:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
=======
	// Meta.g:218:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:187:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:188:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement1224); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement1226); 
			// Meta.g:188:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==T_ANALYTICS) ) {
				alt7=1;
=======
			// Meta.g:225:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:226:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement1459); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement1461); 
			// Meta.g:226:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==T_ANALYTICS) ) {
				alt13=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}
			else if ( (LA13_0==T_CONSISTENCY) ) {
				alt13=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:189:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1238); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1240); 
					// Meta.g:189:29: ( T_TRUE | T_FALSE )
					int alt1=2;
					int LA1_0 = input.LA(1);
					if ( (LA1_0==T_TRUE) ) {
						alt1=1;
=======
					// Meta.g:227:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1473); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1475); 
					// Meta.g:227:29: ( T_TRUE | T_FALSE )
					int alt7=2;
					int LA7_0 = input.LA(1);
					if ( (LA7_0==T_TRUE) ) {
						alt7=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					}
					else if ( (LA7_0==T_FALSE) ) {
						alt7=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 7, 0, input);
						throw nvae;
					}

					switch (alt7) {
						case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:189:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1243); 
=======
							// Meta.g:227:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1478); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							analytics=true;
							}
							break;
						case 2 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:189:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1246); 
=======
							// Meta.g:227:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1481); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:190:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt3=2;
					int LA3_0 = input.LA(1);
					if ( (LA3_0==T_AND) ) {
						alt3=1;
=======
					// Meta.g:228:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( (LA9_0==T_AND) ) {
						alt9=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					}
					switch (alt9) {
						case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:190:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1261); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1263); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1265); 
							// Meta.g:191:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt2=9;
=======
							// Meta.g:228:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1496); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1498); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1500); 
							// Meta.g:229:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt8=9;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							switch ( input.LA(1) ) {
							case T_ALL:
								{
								alt8=1;
								}
								break;
							case T_ANY:
								{
								alt8=2;
								}
								break;
							case T_QUORUM:
								{
								alt8=3;
								}
								break;
							case T_ONE:
								{
								alt8=4;
								}
								break;
							case T_TWO:
								{
								alt8=5;
								}
								break;
							case T_THREE:
								{
								alt8=6;
								}
								break;
							case T_EACH_QUORUM:
								{
								alt8=7;
								}
								break;
							case T_LOCAL_ONE:
								{
								alt8=8;
								}
								break;
							case T_LOCAL_QUORUM:
								{
								alt8=9;
								}
								break;
							default:
								NoViableAltException nvae =
									new NoViableAltException("", 8, 0, input);
								throw nvae;
							}
							switch (alt8) {
								case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:191:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1280); 
=======
									// Meta.g:229:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1515); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:192:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1299); 
=======
									// Meta.g:230:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1534); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:193:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1317); 
=======
									// Meta.g:231:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1552); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:194:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1335); 
=======
									// Meta.g:232:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1570); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:195:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1353); 
=======
									// Meta.g:233:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1588); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:196:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1371); 
=======
									// Meta.g:234:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1606); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:197:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1389); 
=======
									// Meta.g:235:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1624); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:198:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1407); 
=======
									// Meta.g:236:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1642); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:199:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1425); 
=======
									// Meta.g:237:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1660); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:203:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1475); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1477); 
					// Meta.g:204:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt4=9;
=======
					// Meta.g:241:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement1710); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1712); 
					// Meta.g:242:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt10=9;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					switch ( input.LA(1) ) {
					case T_ALL:
						{
						alt10=1;
						}
						break;
					case T_ANY:
						{
						alt10=2;
						}
						break;
					case T_QUORUM:
						{
						alt10=3;
						}
						break;
					case T_ONE:
						{
						alt10=4;
						}
						break;
					case T_TWO:
						{
						alt10=5;
						}
						break;
					case T_THREE:
						{
						alt10=6;
						}
						break;
					case T_EACH_QUORUM:
						{
						alt10=7;
						}
						break;
					case T_LOCAL_ONE:
						{
						alt10=8;
						}
						break;
					case T_LOCAL_QUORUM:
						{
						alt10=9;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 10, 0, input);
						throw nvae;
					}
					switch (alt10) {
						case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:204:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1493); 
=======
							// Meta.g:242:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement1728); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:205:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1512); 
=======
							// Meta.g:243:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement1747); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:206:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1530); 
=======
							// Meta.g:244:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement1765); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:207:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1548); 
=======
							// Meta.g:245:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement1783); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:208:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1566); 
=======
							// Meta.g:246:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement1801); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:209:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1584); 
=======
							// Meta.g:247:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement1819); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:210:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1602); 
=======
							// Meta.g:248:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1837); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:211:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1620); 
=======
							// Meta.g:249:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1855); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:212:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1638); 
=======
							// Meta.g:250:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1873); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:214:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt6=2;
					int LA6_0 = input.LA(1);
					if ( (LA6_0==T_AND) ) {
						alt6=1;
=======
					// Meta.g:252:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==T_AND) ) {
						alt12=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					}
					switch (alt12) {
						case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:214:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1666); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1668); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1670); 
							// Meta.g:214:36: ( T_TRUE | T_FALSE )
							int alt5=2;
							int LA5_0 = input.LA(1);
							if ( (LA5_0==T_TRUE) ) {
								alt5=1;
=======
							// Meta.g:252:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement1901); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement1903); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement1905); 
							// Meta.g:252:36: ( T_TRUE | T_FALSE )
							int alt11=2;
							int LA11_0 = input.LA(1);
							if ( (LA11_0==T_TRUE) ) {
								alt11=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							}
							else if ( (LA11_0==T_FALSE) ) {
								alt11=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 11, 0, input);
								throw nvae;
							}

							switch (alt11) {
								case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:214:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1673); 
=======
									// Meta.g:252:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement1908); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
									analytics=true;
									}
									break;
								case 2 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
									// Meta.g:214:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1676); 
=======
									// Meta.g:252:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement1911); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:220:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
=======
	// Meta.g:258:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:220:41: ( T_USE iden= T_IDENT )
			// Meta.g:221:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement1731); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement1739); 
=======
			// Meta.g:258:41: ( T_USE iden= T_IDENT )
			// Meta.g:259:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement1966); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement1974); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:225:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
=======
	// Meta.g:263:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:228:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:229:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement1769); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1775); 
			// Meta.g:231:5: ( T_IF T_EXISTS )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==T_IF) ) {
				alt8=1;
=======
			// Meta.g:266:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:267:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement2004); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement2010); 
			// Meta.g:269:5: ( T_IF T_EXISTS )?
			int alt14=2;
			int LA14_0 = input.LA(1);
			if ( (LA14_0==T_IF) ) {
				alt14=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}
			switch (alt14) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:231:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement1782); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement1784); 
=======
					// Meta.g:269:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement2017); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement2019); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					ifExists = true;
					}
					break;

			}

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement1796); 
=======
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement2031); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:236:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
=======
	// Meta.g:274:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:239:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:240:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement1830); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement1836); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1844); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement1850); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1858); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement1860); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement1864);
=======
			// Meta.g:277:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:278:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement2065); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement2071); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2079); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement2085); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2093); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement2095); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement2099);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:245:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop9:
=======
			// Meta.g:283:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop15:
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( (LA15_0==T_AND) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:245:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement1873); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement1877); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement1879); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement1883);
=======
					// Meta.g:283:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement2108); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement2112); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement2114); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement2118);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop15;
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:249:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
=======
	// Meta.g:287:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:253:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:254:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement1922); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement1928); 
			// Meta.g:256:5: ( T_IF T_NOT T_EXISTS )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==T_IF) ) {
				alt10=1;
=======
			// Meta.g:291:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:292:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement2157); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement2163); 
			// Meta.g:294:5: ( T_IF T_NOT T_EXISTS )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IF) ) {
				alt16=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}
			switch (alt16) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:256:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement1935); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement1937); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement1939); 
=======
					// Meta.g:294:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement2170); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement2172); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement2174); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					ifNotExists = true;
					}
					break;

			}

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1951); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement1957); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1969); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement1971); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement1975);
=======
			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2186); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement2192); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2204); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement2206); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement2210);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:260:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop11:
=======
			// Meta.g:298:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop17:
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( (LA17_0==T_AND) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:260:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement1984); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement1988); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement1990); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement1994);
=======
					// Meta.g:298:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement2219); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement2223); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement2225); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement2229);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop17;
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:264:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID ;
=======
	// Meta.g:302:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String ident =null;


		        boolean ifExists = false;
		    
		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:267:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID )
			// Meta.g:268:5: T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement2033); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement2039); 
			// Meta.g:270:5: ( T_IF T_EXISTS )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==T_IF) ) {
				alt12=1;
=======
			// Meta.g:305:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID )
			// Meta.g:306:5: T_DROP T_TABLE ( T_IF T_EXISTS )? ident= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement2268); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement2274); 
			// Meta.g:308:5: ( T_IF T_EXISTS )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_IF) ) {
				alt18=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}
			switch (alt18) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:270:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement2046); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement2048); 
=======
					// Meta.g:308:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement2281); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement2283); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					 ifExists = true; 
					}
					break;

			}

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			pushFollow(FOLLOW_getTableID_in_dropTableStatement2060);
=======
			pushFollow(FOLLOW_getTableID_in_dropTableStatement2295);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:276:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
=======
	// Meta.g:314:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:276:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:277:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement2080); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement2093);
=======
			// Meta.g:314:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:315:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement2315); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement2328);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:283:1: metaStatement returns [Statement st] : (st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement );
=======
	// Meta.g:321:1: metaStatement returns [Statement st] : (st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement );
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final Statement metaStatement() throws RecognitionException {
		Statement st = null;


<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
		CreateTriggerStatement st_crtr =null;
		DropTriggerStatement st_drtr =null;
		StopProcessStatement st_stpr =null;
=======
		SelectStatement st_slct =null;
		InsertIntoStatement st_nsnt =null;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
		ExplainPlanStatement st_xppl =null;
		SetOptionsStatement st_stpt =null;
		UseStatement st_usks =null;
		DropKeyspaceStatement st_drks =null;
		CreateKeyspaceStatement st_crks =null;
		AlterKeyspaceStatement st_alks =null;
		DropTableStatement st_tbdr =null;
		TruncateStatement st_trst =null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:283:37: (st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement )
			int alt13=11;
			switch ( input.LA(1) ) {
			case T_CREATE:
				{
				int LA13_1 = input.LA(2);
				if ( (LA13_1==T_TRIGGER) ) {
					alt13=1;
				}
				else if ( (LA13_1==T_KEYSPACE) ) {
					alt13=8;
				}

				else {
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

=======
			// Meta.g:321:37: (st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement )
			int alt19=10;
			switch ( input.LA(1) ) {
			case T_SELECT:
				{
				alt19=1;
				}
				break;
			case T_INSERT:
				{
				alt19=2;
				}
				break;
			case T_EXPLAIN:
				{
				alt19=3;
				}
				break;
			case T_SET:
				{
				alt19=4;
				}
				break;
			case T_USE:
				{
				alt19=5;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
				}
				break;
			case T_DROP:
				{
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				switch ( input.LA(2) ) {
				case T_TRIGGER:
					{
					alt13=2;
					}
					break;
				case T_KEYSPACE:
					{
					alt13=7;
					}
					break;
				case T_TABLE:
					{
					alt13=10;
					}
					break;
				default:
=======
				int LA19_6 = input.LA(2);
				if ( (LA19_6==T_KEYSPACE) ) {
					alt19=6;
				}
				else if ( (LA19_6==T_TABLE) ) {
					alt19=9;
				}

				else {
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							new NoViableAltException("", 13, 2, input);
=======
							new NoViableAltException("", 19, 6, input);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_STOP:
				{
				alt13=3;
				}
				break;
			case T_EXPLAIN:
				{
				alt13=4;
				}
				break;
			case T_SET:
				{
				alt19=7;
				}
				break;
			case T_USE:
				{
				alt19=8;
				}
				break;
			case T_ALTER:
				{
				alt13=9;
				}
				break;
			case T_TRUNCATE:
				{
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				alt13=11;
=======
				alt19=10;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 19, 0, input);
				throw nvae;
			}
			switch (alt19) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:285:5: st_crtr= createTriggerStatement
					{
					pushFollow(FOLLOW_createTriggerStatement_in_metaStatement2116);
					st_crtr=createTriggerStatement();
					state._fsp--;

					 st = st_crtr; 
					}
					break;
				case 2 :
					// Meta.g:286:6: st_drtr= dropTriggerStatement
					{
					pushFollow(FOLLOW_dropTriggerStatement_in_metaStatement2128);
					st_drtr=dropTriggerStatement();
					state._fsp--;

					 st = st_drtr; 
					}
					break;
				case 3 :
					// Meta.g:287:6: st_stpr= stopProcessStatement
					{
					pushFollow(FOLLOW_stopProcessStatement_in_metaStatement2141);
					st_stpr=stopProcessStatement();
					state._fsp--;

					 st = st_stpr; 
					}
					break;
				case 4 :
					// Meta.g:288:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement2155);
=======
					// Meta.g:322:5: st_slct= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_metaStatement2351);
					st_slct=selectStatement();
					state._fsp--;

					 st = st_slct;
					}
					break;
				case 2 :
					// Meta.g:323:7: st_nsnt= insertIntoStatement
					{
					pushFollow(FOLLOW_insertIntoStatement_in_metaStatement2365);
					st_nsnt=insertIntoStatement();
					state._fsp--;

					 st = st_nsnt;
					}
					break;
				case 3 :
					// Meta.g:324:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement2379);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				case 5 :
					// Meta.g:289:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement2169);
=======
				case 4 :
					// Meta.g:325:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement2393);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				case 6 :
					// Meta.g:290:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement2183);
=======
				case 5 :
					// Meta.g:326:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement2407);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				case 7 :
					// Meta.g:291:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement2197);
=======
				case 6 :
					// Meta.g:327:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement2421);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				case 8 :
					// Meta.g:292:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement2211);
=======
				case 7 :
					// Meta.g:328:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement2435);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				case 9 :
					// Meta.g:293:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement2225);
=======
				case 8 :
					// Meta.g:329:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement2449);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				case 10 :
					// Meta.g:294:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement2239);
=======
				case 9 :
					// Meta.g:330:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement2463);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
				case 11 :
					// Meta.g:295:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement2253);
=======
				case 10 :
					// Meta.g:331:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement2477);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:297:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
=======
	// Meta.g:333:1: query returns [Statement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final Statement query() throws RecognitionException {
		Statement st = null;


		Statement mtst =null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:297:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:298:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query2274);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:298:21: ( T_SEMICOLON )+
			int cnt14=0;
			loop14:
=======
			// Meta.g:333:29: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:334:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query2498);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:334:21: ( T_SEMICOLON )+
			int cnt20=0;
			loop20:
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			while (true) {
				int alt20=2;
				int LA20_0 = input.LA(1);
				if ( (LA20_0==T_SEMICOLON) ) {
					alt20=1;
				}

				switch (alt20) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:298:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query2277); 
=======
					// Meta.g:334:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query2501); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					}
					break;

				default :
					if ( cnt20 >= 1 ) break loop20;
					EarlyExitException eee = new EarlyExitException(20, input);
					throw eee;
				}
				cnt20++;
			}

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			match(input,EOF,FOLLOW_EOF_in_query2281); 
=======
			match(input,EOF,FOLLOW_EOF_in_query2505); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c

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



	// $ANTLR start "getOption"
	// Meta.g:341:1: getOption returns [Option opt] : ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty );
	public final Option getOption() throws RecognitionException {
		Option opt = null;


		Token identProp=null;
		ValueProperty valueProp =null;

		try {
			// Meta.g:341:31: ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty )
			int alt21=3;
			switch ( input.LA(1) ) {
			case T_COMPACT:
				{
				alt21=1;
				}
				break;
			case T_CLUSTERING:
				{
				alt21=2;
				}
				break;
			case T_IDENT:
				{
				alt21=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 21, 0, input);
				throw nvae;
			}
			switch (alt21) {
				case 1 :
					// Meta.g:342:5: T_COMPACT T_STORAGE
					{
					match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getOption2525); 
					match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getOption2527); 
					opt =new Option(Option.OPTION_COMPACT);
					}
					break;
				case 2 :
					// Meta.g:343:7: T_CLUSTERING T_ORDER
					{
					match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getOption2537); 
					match(input,T_ORDER,FOLLOW_T_ORDER_in_getOption2539); 
					opt =new Option(Option.OPTION_CLUSTERING);
					}
					break;
				case 3 :
					// Meta.g:344:7: identProp= T_IDENT T_EQUAL valueProp= getValueProperty
					{
					identProp=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOption2551); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getOption2553); 
					pushFollow(FOLLOW_getValueProperty_in_getOption2557);
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
	// Meta.g:347:1: getList returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getList() throws RecognitionException {
		List list = null;


		ParserRuleReturnScope term1 =null;
		ParserRuleReturnScope termN =null;


		        list = new ArrayList<String>();
		    
		try {
			// Meta.g:350:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:351:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getList2585);
			term1=getTerm();
			state._fsp--;

			list.add((term1!=null?input.toString(term1.start,term1.stop):null));
			// Meta.g:352:5: ( T_COMMA termN= getTerm )*
			loop22:
			while (true) {
				int alt22=2;
				int LA22_0 = input.LA(1);
				if ( (LA22_0==T_COMMA) ) {
					alt22=1;
				}

				switch (alt22) {
				case 1 :
					// Meta.g:352:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getList2594); 
					pushFollow(FOLLOW_getTerm_in_getList2598);
					termN=getTerm();
					state._fsp--;

					list.add((termN!=null?input.toString(termN.start,termN.stop):null));
					}
					break;

				default :
					break loop22;
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



	// $ANTLR start "getTermOrLiteral"
	// Meta.g:355:1: getTermOrLiteral returns [ValueCell vc] : (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET );
	public final ValueCell getTermOrLiteral() throws RecognitionException {
		ValueCell vc = null;


		ParserRuleReturnScope term =null;
		ParserRuleReturnScope term1 =null;
		ParserRuleReturnScope termN =null;


		        CollectionLiteral cl = new CollectionLiteral();
		    
		try {
			// Meta.g:358:6: (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET )
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==T_IDENT||LA25_0==T_TERM) ) {
				alt25=1;
			}
			else if ( (LA25_0==T_START_SBRACKET) ) {
				alt25=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 25, 0, input);
				throw nvae;
			}

			switch (alt25) {
				case 1 :
					// Meta.g:359:5: term= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getTermOrLiteral2632);
					term=getTerm();
					state._fsp--;

					vc =new Term((term!=null?input.toString(term.start,term.stop):null));
					}
					break;
				case 2 :
					// Meta.g:361:5: T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getTermOrLiteral2646); 
					// Meta.g:362:5: (term1= getTerm ( T_COMMA termN= getTerm )* )?
					int alt24=2;
					int LA24_0 = input.LA(1);
					if ( (LA24_0==T_IDENT||LA24_0==T_TERM) ) {
						alt24=1;
					}
					switch (alt24) {
						case 1 :
							// Meta.g:363:9: term1= getTerm ( T_COMMA termN= getTerm )*
							{
							pushFollow(FOLLOW_getTerm_in_getTermOrLiteral2664);
							term1=getTerm();
							state._fsp--;

							cl.addLiteral(new Term((term1!=null?input.toString(term1.start,term1.stop):null)));
							// Meta.g:364:9: ( T_COMMA termN= getTerm )*
							loop23:
							while (true) {
								int alt23=2;
								int LA23_0 = input.LA(1);
								if ( (LA23_0==T_COMMA) ) {
									alt23=1;
								}

								switch (alt23) {
								case 1 :
									// Meta.g:364:10: T_COMMA termN= getTerm
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getTermOrLiteral2677); 
									pushFollow(FOLLOW_getTerm_in_getTermOrLiteral2681);
									termN=getTerm();
									state._fsp--;

									cl.addLiteral(new Term((termN!=null?input.toString(termN.start,termN.stop):null)));
									}
									break;

								default :
									break loop23;
								}
							}

							}
							break;

					}

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getTermOrLiteral2698); 
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:305:1: getTableID returns [String tableID] : (ks= T_IDENT '.' )? ident= T_IDENT ;
=======
	// Meta.g:369:1: getTableID returns [String tableID] : (ks= T_IDENT '.' )? ident= T_IDENT ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ks=null;
		Token ident=null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:305:36: ( (ks= T_IDENT '.' )? ident= T_IDENT )
			// Meta.g:306:5: (ks= T_IDENT '.' )? ident= T_IDENT
			{
			// Meta.g:306:5: (ks= T_IDENT '.' )?
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( (LA15_0==T_IDENT) ) {
				int LA15_1 = input.LA(2);
				if ( (LA15_1==T_POINT) ) {
					alt15=1;
=======
			// Meta.g:369:36: ( (ks= T_IDENT '.' )? ident= T_IDENT )
			// Meta.g:370:5: (ks= T_IDENT '.' )? ident= T_IDENT
			{
			// Meta.g:370:5: (ks= T_IDENT '.' )?
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0==T_IDENT) ) {
				int LA26_1 = input.LA(2);
				if ( (LA26_1==T_POINT) ) {
					alt26=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
				}
			}
			switch (alt26) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:306:6: ks= T_IDENT '.'
					{
					ks=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2305); 
					match(input,T_POINT,FOLLOW_T_POINT_in_getTableID2307); 
=======
					// Meta.g:370:6: ks= T_IDENT '.'
					{
					ks=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2720); 
					match(input,T_POINT,FOLLOW_T_POINT_in_getTableID2722); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					}
					break;

			}

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2318); 
=======
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID2733); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:309:1: getTerm returns [String term] : (ident= T_IDENT |noIdent= T_TERM );
=======
	// Meta.g:373:1: getTerm returns [String term] : (ident= T_IDENT |noIdent= T_TERM );
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final MetaParser.getTerm_return getTerm() throws RecognitionException {
		MetaParser.getTerm_return retval = new MetaParser.getTerm_return();
		retval.start = input.LT(1);

		Token ident=null;
		Token noIdent=null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:309:30: (ident= T_IDENT |noIdent= T_TERM )
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IDENT) ) {
				alt16=1;
=======
			// Meta.g:373:30: (ident= T_IDENT |noIdent= T_TERM )
			int alt27=2;
			int LA27_0 = input.LA(1);
			if ( (LA27_0==T_IDENT) ) {
				alt27=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}
			else if ( (LA27_0==T_TERM) ) {
				alt27=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 27, 0, input);
				throw nvae;
			}

			switch (alt27) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:310:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm2338); 
=======
					// Meta.g:374:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTerm2753); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					retval.term = (ident!=null?ident.getText():null);
					}
					break;
				case 2 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:311:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm2350); 
=======
					// Meta.g:375:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getTerm2765); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:314:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
=======
	// Meta.g:378:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		ParserRuleReturnScope leftTerm1 =null;
		ParserRuleReturnScope rightTerm1 =null;
		ParserRuleReturnScope leftTermN =null;
		ParserRuleReturnScope rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:317:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:318:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral2380); 
			// Meta.g:319:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_IDENT||LA18_0==T_TERM) ) {
				alt18=1;
=======
			// Meta.g:381:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:382:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral2795); 
			// Meta.g:383:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0==T_IDENT||LA29_0==T_TERM) ) {
				alt29=1;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			}
			switch (alt29) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:319:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2390);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2392); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2396);
=======
					// Meta.g:383:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2805);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2807); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral2811);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put((leftTerm1!=null?input.toString(leftTerm1.start,leftTerm1.stop):null), (rightTerm1!=null?input.toString(rightTerm1.start,rightTerm1.stop):null));
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:320:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop17:
=======
					// Meta.g:384:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop28:
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					while (true) {
						int alt28=2;
						int LA28_0 = input.LA(1);
						if ( (LA28_0==T_COMMA) ) {
							alt28=1;
						}

						switch (alt28) {
						case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
							// Meta.g:320:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral2405); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2409);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2411); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2415);
=======
							// Meta.g:384:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral2820); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2824);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral2826); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral2830);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put((leftTermN!=null?input.toString(leftTermN.start,leftTermN.stop):null), (rightTermN!=null?input.toString(rightTermN.start,rightTermN.stop):null));
							}
							break;

						default :
							break loop28;
						}
					}

					}
					break;

			}

<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral2427); 
=======
			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral2842); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	// Meta.g:324:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral );
=======
	// Meta.g:388:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral );
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Map<String, String> mapliteral =null;

		try {
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
			// Meta.g:324:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral )
			int alt19=3;
=======
			// Meta.g:388:47: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral )
			int alt30=3;
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt30=1;
				}
				break;
			case T_CONSTANT:
				{
				alt30=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt30=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 30, 0, input);
				throw nvae;
			}
			switch (alt30) {
				case 1 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:325:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty2449); 
=======
					// Meta.g:389:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty2864); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:326:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty2461); 
=======
					// Meta.g:390:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty2876); 
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
					// Meta.g:327:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty2473);
=======
					// Meta.g:391:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty2888);
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
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



<<<<<<< d1ae33aec023b251155183ae7c0643a1b8f0fc99
	public static final BitSet FOLLOW_T_STOP_in_stopProcessStatement1032 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_T_PROCESS_in_stopProcessStatement1034 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_stopProcessStatement1038 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTriggerStatement1061 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropTriggerStatement1068 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1072 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_ON_in_dropTriggerStatement1079 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement1088 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTriggerStatement1116 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_TRIGGER_in_createTriggerStatement1123 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1127 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_ON_in_createTriggerStatement1134 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1143 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_USING_in_createTriggerStatement1149 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement1153 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement1182 = new BitSet(new long[]{0x0020000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement1184 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement1186 = new BitSet(new long[]{0x1400021808000000L,0x0000000000000014L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement1190 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement1224 = new BitSet(new long[]{0x0010000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement1226 = new BitSet(new long[]{0x0000000210000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1238 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1240 = new BitSet(new long[]{0x0000040000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1243 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1246 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1261 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1263 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1265 = new BitSet(new long[]{0x8109802044000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1280 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1299 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1353 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1389 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1425 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1475 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1477 = new BitSet(new long[]{0x8109802044000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1493 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1512 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1530 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1548 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1566 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1584 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1602 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1620 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1638 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1666 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1668 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1670 = new BitSet(new long[]{0x0000040000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1673 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1676 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement1731 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement1739 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement1769 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement1775 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement1782 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement1784 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement1796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement1830 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement1836 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1844 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement1850 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1858 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement1860 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement1864 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement1873 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement1877 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement1879 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement1883 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement1922 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement1928 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement1935 = new BitSet(new long[]{0x0002000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement1937 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement1939 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1951 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement1957 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1969 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement1971 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement1975 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement1984 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement1988 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement1990 = new BitSet(new long[]{0x0800100400000000L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement1994 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement2033 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement2039 = new BitSet(new long[]{0x0000300000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement2046 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement2048 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement2060 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement2080 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement2093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTriggerStatement_in_metaStatement2116 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTriggerStatement_in_metaStatement2128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stopProcessStatement_in_metaStatement2141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement2155 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement2169 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement2183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement2197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement2211 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement2225 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement2239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement2253 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query2274 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query2277 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_EOF_in_query2281 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2305 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_POINT_in_getTableID2307 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2318 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm2338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm2350 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral2380 = new BitSet(new long[]{0x4000104000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2390 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2392 = new BitSet(new long[]{0x4000100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2396 = new BitSet(new long[]{0x0000004100000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral2405 = new BitSet(new long[]{0x4000100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2409 = new BitSet(new long[]{0x0000000080000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2411 = new BitSet(new long[]{0x4000100000000000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2415 = new BitSet(new long[]{0x0000004100000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral2427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty2449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty2461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty2473 = new BitSet(new long[]{0x0000000000000002L});
=======
	public static final BitSet FOLLOW_T_SELECT_in_selectStatement1120 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INSERT_in_insertIntoStatement1150 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_T_INTO_in_insertIntoStatement1157 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_insertIntoStatement1166 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_insertIntoStatement1172 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement1181 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement1191 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement1195 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_insertIntoStatement1206 = new BitSet(new long[]{0x2000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_selectStatement_in_insertIntoStatement1225 = new BitSet(new long[]{0x0001000000000002L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_VALUES_in_insertIntoStatement1248 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_insertIntoStatement1258 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000012L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement1275 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement1292 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000012L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement1296 = new BitSet(new long[]{0x0000010200000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_insertIntoStatement1310 = new BitSet(new long[]{0x0001000000000002L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_IF_in_insertIntoStatement1323 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_insertIntoStatement1325 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_insertIntoStatement1327 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_USING_in_insertIntoStatement1348 = new BitSet(new long[]{0x0000800480000000L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement1363 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_insertIntoStatement1376 = new BitSet(new long[]{0x0000800480000000L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement1380 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement1417 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement1419 = new BitSet(new long[]{0x0000400000000000L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement1421 = new BitSet(new long[]{0xA002106008000000L,0x0000000000000280L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement1425 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement1459 = new BitSet(new long[]{0x0100000000000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement1461 = new BitSet(new long[]{0x0000000810000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1473 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1475 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1478 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1481 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1496 = new BitSet(new long[]{0x0000000800000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1498 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1500 = new BitSet(new long[]{0x10B0008044000000L,0x0000000000000120L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1515 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1552 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1606 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1624 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1642 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1660 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement1710 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1712 = new BitSet(new long[]{0x10B0008044000000L,0x0000000000000120L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement1728 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement1747 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement1765 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement1783 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement1801 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement1819 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement1837 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement1855 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement1873 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement1901 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement1903 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement1905 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement1908 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement1911 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement1966 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement1974 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement2004 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement2010 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement2017 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement2019 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement2031 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement2065 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement2071 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2079 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement2085 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2093 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement2095 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement2099 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement2108 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement2112 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement2114 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement2118 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement2157 = new BitSet(new long[]{0x0008000000000000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement2163 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement2170 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement2172 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement2174 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2186 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement2192 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2204 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement2206 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement2210 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement2219 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement2223 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement2225 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement2229 = new BitSet(new long[]{0x0000000020000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement2268 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement2274 = new BitSet(new long[]{0x0001800000000000L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement2281 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement2283 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement2295 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement2315 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement2328 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_selectStatement_in_metaStatement2351 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_insertIntoStatement_in_metaStatement2365 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement2379 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement2393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement2407 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement2421 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement2435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement2449 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement2463 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement2477 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query2498 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query2501 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_EOF_in_query2505 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COMPACT_in_getOption2525 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_STORAGE_in_getOption2527 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getOption2537 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getOption2539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOption2551 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getOption2553 = new BitSet(new long[]{0x0000801000000000L,0x0000000000000002L});
	public static final BitSet FOLLOW_getValueProperty_in_getOption2557 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getList2585 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getList2594 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getTerm_in_getList2598 = new BitSet(new long[]{0x0000000200000002L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral2632 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getTermOrLiteral2646 = new BitSet(new long[]{0x0000820000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral2664 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getTermOrLiteral2677 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral2681 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getTermOrLiteral2698 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2720 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_POINT_in_getTableID2722 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID2733 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTerm2753 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getTerm2765 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral2795 = new BitSet(new long[]{0x0000820000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2805 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2807 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2811 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral2820 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2824 = new BitSet(new long[]{0x0000000100000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral2826 = new BitSet(new long[]{0x0000800000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral2830 = new BitSet(new long[]{0x0000020200000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral2842 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty2864 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty2876 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty2888 = new BitSet(new long[]{0x0000000000000002L});
>>>>>>> 4142c6035937b06dbf662df8391fcd7146d52d4c
}
