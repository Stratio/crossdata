// $ANTLR 3.5.1 Meta.g 2014-03-18 10:26:33

    package com.stratio.meta.core.grammar.generated;    
import com.stratio.meta.core.structures.SelectorFunction;
import com.stratio.meta.core.structures.SelectorMeta;
import com.stratio.meta.core.structures.OrderDirection;
import com.stratio.meta.core.structures.ValueAssignment;
import com.stratio.meta.core.structures.Selection;
import com.stratio.meta.core.structures.SelectionSelector;
import com.stratio.meta.core.structures.SelectionSelectors;
import com.stratio.meta.core.structures.SelectorIdentifier;
import com.stratio.meta.core.structures.MapLiteralProperty;
import com.stratio.meta.core.structures.IdentMap;
import com.stratio.meta.core.structures.Consistency;
import com.stratio.meta.core.structures.WindowLast;
import com.stratio.meta.core.structures.MetaProperty;
import com.stratio.meta.core.structures.IntTerm;
import com.stratio.meta.core.structures.Term;
import com.stratio.meta.core.structures.MetaOrdering;
import com.stratio.meta.core.structures.QuotedLiteral;
import com.stratio.meta.core.structures.RelationCompare;
import com.stratio.meta.core.structures.Option;
import com.stratio.meta.core.structures.MetaRelation;
import com.stratio.meta.core.structures.TimeUnit;
import com.stratio.meta.core.structures.RelationToken;
import com.stratio.meta.core.structures.SelectionList;
import com.stratio.meta.core.structures.PropertyNameValue;
import com.stratio.meta.core.structures.IdentifierProperty;
import com.stratio.meta.core.structures.GroupBy;
import com.stratio.meta.core.structures.SelectorGroupBy;
import com.stratio.meta.core.structures.IdentifierAssignment;
import com.stratio.meta.core.structures.WindowRows;
import com.stratio.meta.core.structures.IdentIntOrLiteral;
import com.stratio.meta.core.structures.RelationIn;
import com.stratio.meta.core.structures.WindowTime;
import com.stratio.meta.core.structures.PropertyClusteringOrder;
import com.stratio.meta.core.structures.ValueCell;
import com.stratio.meta.core.structures.SelectionAsterisk;
import com.stratio.meta.core.structures.InnerJoin;
import com.stratio.meta.core.structures.ConstantProperty;
import com.stratio.meta.core.structures.SetLiteral;
import com.stratio.meta.core.structures.ValueProperty;
import com.stratio.meta.core.structures.FloatProperty;
import com.stratio.meta.core.structures.SelectionCount;
import com.stratio.meta.core.structures.GroupByFunction;
import com.stratio.meta.core.structures.PropertyCompactStorage;
import com.stratio.meta.core.structures.ListLiteral;
import com.stratio.meta.core.structures.WindowSelect;
import com.stratio.meta.core.structures.CollectionLiteral;
import com.stratio.meta.core.structures.Assignment;
import com.stratio.meta.core.structures.SelectionClause;
import com.stratio.meta.core.structures.RelationBetween;
import com.stratio.meta.core.structures.BooleanProperty;
import com.stratio.meta.common.utils.AntlrError;
import com.stratio.meta.common.utils.ErrorsHelper;
    import com.stratio.meta.common.statements.SelectStatement;
import com.stratio.meta.common.statements.InsertIntoStatement;
import com.stratio.meta.common.statements.RemoveUDFStatement;
import com.stratio.meta.common.statements.CreateIndexStatement;
import com.stratio.meta.common.statements.CreateTableStatement;
import com.stratio.meta.common.statements.UpdateTableStatement;
import com.stratio.meta.common.statements.CreateTriggerStatement;
import com.stratio.meta.common.statements.UseStatement;
import com.stratio.meta.common.statements.StopProcessStatement;
import com.stratio.meta.common.statements.ExplainPlanStatement;
import com.stratio.meta.common.statements.CreateKeyspaceStatement;
import com.stratio.meta.common.statements.AddStatement;
import com.stratio.meta.common.statements.AlterKeyspaceStatement;
import com.stratio.meta.common.statements.DropTableStatement;
import com.stratio.meta.common.statements.ListStatement;
import com.stratio.meta.common.statements.DropTriggerStatement;
import com.stratio.meta.common.statements.DeleteStatement;
import com.stratio.meta.common.statements.TruncateStatement;
import com.stratio.meta.common.statements.MetaStatement;
import com.stratio.meta.common.statements.SetOptionsStatement;
import com.stratio.meta.common.statements.DropKeyspaceStatement;
import com.stratio.meta.common.statements.DropIndexStatement;
import com.stratio.meta.common.statements.AlterTableStatement;
    import java.util.LinkedHashMap;
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
		"P", "POINT", "Q", "QUOTED_LITERAL", "R", "S", "T", "T_ADD", "T_AGGREGATION", 
		"T_ALL", "T_ALTER", "T_ANALYTICS", "T_AND", "T_ANY", "T_AS", "T_ASC", 
		"T_ASTERISK", "T_AT", "T_AVG", "T_BETWEEN", "T_BY", "T_CLUSTERING", "T_COLON", 
		"T_COMMA", "T_COMPACT", "T_CONSISTENCY", "T_CONSTANT", "T_COUNT", "T_CREATE", 
		"T_DELETE", "T_DESC", "T_DISABLE", "T_DISTINCT", "T_DROP", "T_EACH_QUORUM", 
		"T_END_BRACKET", "T_END_PARENTHESIS", "T_END_SBRACKET", "T_EPHEMERAL", 
		"T_EQUAL", "T_EXISTS", "T_EXPLAIN", "T_FALSE", "T_FLOAT", "T_FOR", "T_FROM", 
		"T_GROUP", "T_GT", "T_GTE", "T_IDENT", "T_IF", "T_IN", "T_INDEX", "T_INDEX_TYPE", 
		"T_INNER", "T_INSERT", "T_INTERROGATION", "T_INTO", "T_JOIN", "T_KEY", 
		"T_KEYSPACE", "T_KS_AND_TN", "T_LAST", "T_LIKE", "T_LIMIT", "T_LIST", 
		"T_LOCAL_ONE", "T_LOCAL_QUORUM", "T_LT", "T_LTE", "T_MAX", "T_MIN", "T_NOT", 
		"T_NOT_EQUAL", "T_ON", "T_ONE", "T_OPTIONS", "T_ORDER", "T_PATH", "T_PLAN", 
		"T_PLUS", "T_PRIMARY", "T_PROCESS", "T_QUORUM", "T_QUOTE", "T_REMOVE", 
		"T_ROWS", "T_SELECT", "T_SEMICOLON", "T_SET", "T_SINGLE_QUOTE", "T_START_BRACKET", 
		"T_START_PARENTHESIS", "T_START_SBRACKET", "T_STOP", "T_STORAGE", "T_SUBTRACT", 
		"T_TABLE", "T_TERM", "T_THREE", "T_TOKEN", "T_TRIGGER", "T_TRUE", "T_TRUNCATE", 
		"T_TWO", "T_TYPE", "T_UDF", "T_UPDATE", "T_USE", "T_USING", "T_VALUES", 
		"T_WHERE", "T_WINDOW", "T_WITH", "U", "V", "W", "WS", "X", "Y", "Z", "'1'", 
		"'D'", "'DAYS'", "'H'", "'HOURS'", "'M'", "'MINUTES'", "'S'", "'SECONDS'", 
		"'d'", "'days'", "'h'", "'hours'", "'m'", "'minutes'", "'s'", "'seconds'"
	};
	public static final int EOF=-1;
	public static final int T__143=143;
	public static final int T__144=144;
	public static final int T__145=145;
	public static final int T__146=146;
	public static final int T__147=147;
	public static final int T__148=148;
	public static final int T__149=149;
	public static final int T__150=150;
	public static final int T__151=151;
	public static final int T__152=152;
	public static final int T__153=153;
	public static final int T__154=154;
	public static final int T__155=155;
	public static final int T__156=156;
	public static final int T__157=157;
	public static final int T__158=158;
	public static final int T__159=159;
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
	public static final int QUOTED_LITERAL=25;
	public static final int R=26;
	public static final int S=27;
	public static final int T=28;
	public static final int T_ADD=29;
	public static final int T_AGGREGATION=30;
	public static final int T_ALL=31;
	public static final int T_ALTER=32;
	public static final int T_ANALYTICS=33;
	public static final int T_AND=34;
	public static final int T_ANY=35;
	public static final int T_AS=36;
	public static final int T_ASC=37;
	public static final int T_ASTERISK=38;
	public static final int T_AT=39;
	public static final int T_AVG=40;
	public static final int T_BETWEEN=41;
	public static final int T_BY=42;
	public static final int T_CLUSTERING=43;
	public static final int T_COLON=44;
	public static final int T_COMMA=45;
	public static final int T_COMPACT=46;
	public static final int T_CONSISTENCY=47;
	public static final int T_CONSTANT=48;
	public static final int T_COUNT=49;
	public static final int T_CREATE=50;
	public static final int T_DELETE=51;
	public static final int T_DESC=52;
	public static final int T_DISABLE=53;
	public static final int T_DISTINCT=54;
	public static final int T_DROP=55;
	public static final int T_EACH_QUORUM=56;
	public static final int T_END_BRACKET=57;
	public static final int T_END_PARENTHESIS=58;
	public static final int T_END_SBRACKET=59;
	public static final int T_EPHEMERAL=60;
	public static final int T_EQUAL=61;
	public static final int T_EXISTS=62;
	public static final int T_EXPLAIN=63;
	public static final int T_FALSE=64;
	public static final int T_FLOAT=65;
	public static final int T_FOR=66;
	public static final int T_FROM=67;
	public static final int T_GROUP=68;
	public static final int T_GT=69;
	public static final int T_GTE=70;
	public static final int T_IDENT=71;
	public static final int T_IF=72;
	public static final int T_IN=73;
	public static final int T_INDEX=74;
	public static final int T_INDEX_TYPE=75;
	public static final int T_INNER=76;
	public static final int T_INSERT=77;
	public static final int T_INTERROGATION=78;
	public static final int T_INTO=79;
	public static final int T_JOIN=80;
	public static final int T_KEY=81;
	public static final int T_KEYSPACE=82;
	public static final int T_KS_AND_TN=83;
	public static final int T_LAST=84;
	public static final int T_LIKE=85;
	public static final int T_LIMIT=86;
	public static final int T_LIST=87;
	public static final int T_LOCAL_ONE=88;
	public static final int T_LOCAL_QUORUM=89;
	public static final int T_LT=90;
	public static final int T_LTE=91;
	public static final int T_MAX=92;
	public static final int T_MIN=93;
	public static final int T_NOT=94;
	public static final int T_NOT_EQUAL=95;
	public static final int T_ON=96;
	public static final int T_ONE=97;
	public static final int T_OPTIONS=98;
	public static final int T_ORDER=99;
	public static final int T_PATH=100;
	public static final int T_PLAN=101;
	public static final int T_PLUS=102;
	public static final int T_PRIMARY=103;
	public static final int T_PROCESS=104;
	public static final int T_QUORUM=105;
	public static final int T_QUOTE=106;
	public static final int T_REMOVE=107;
	public static final int T_ROWS=108;
	public static final int T_SELECT=109;
	public static final int T_SEMICOLON=110;
	public static final int T_SET=111;
	public static final int T_SINGLE_QUOTE=112;
	public static final int T_START_BRACKET=113;
	public static final int T_START_PARENTHESIS=114;
	public static final int T_START_SBRACKET=115;
	public static final int T_STOP=116;
	public static final int T_STORAGE=117;
	public static final int T_SUBTRACT=118;
	public static final int T_TABLE=119;
	public static final int T_TERM=120;
	public static final int T_THREE=121;
	public static final int T_TOKEN=122;
	public static final int T_TRIGGER=123;
	public static final int T_TRUE=124;
	public static final int T_TRUNCATE=125;
	public static final int T_TWO=126;
	public static final int T_TYPE=127;
	public static final int T_UDF=128;
	public static final int T_UPDATE=129;
	public static final int T_USE=130;
	public static final int T_USING=131;
	public static final int T_VALUES=132;
	public static final int T_WHERE=133;
	public static final int T_WINDOW=134;
	public static final int T_WITH=135;
	public static final int U=136;
	public static final int V=137;
	public static final int W=138;
	public static final int WS=139;
	public static final int X=140;
	public static final int Y=141;
	public static final int Z=142;

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


	    private ErrorsHelper foundErrors = new ErrorsHelper();

	    public ErrorsHelper getFoundErrors(){
	        return foundErrors;
	    }

	    @Override
	    public void displayRecognitionError(String[] tokenNames, RecognitionException e){        
	        String hdr = getErrorHeader(e);
	        String msg = getErrorMessage(e, tokenNames);
	        /*System.err.println("Antlr exception: ");
	        System.err.print("\tError recognized: ");
	        System.err.print(hdr+": ");
	        System.err.println(msg);*/
	        AntlrError antlrError = new AntlrError(hdr, msg);
	        foundErrors.addError(antlrError);
	    }



	// $ANTLR start "deleteStatement"
	// Meta.g:227:1: deleteStatement returns [DeleteStatement ds] : T_DELETE (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )? T_FROM tablename= getTableID T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ;
	public final DeleteStatement deleteStatement() throws RecognitionException {
		DeleteStatement ds = null;


		Token firstField=null;
		Token field=null;
		String tablename =null;
		MetaRelation rel1 =null;
		MetaRelation relN =null;


				ds = new DeleteStatement();
			
		try {
			// Meta.g:230:3: ( T_DELETE (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )? T_FROM tablename= getTableID T_WHERE rel1= getRelation ( T_AND relN= getRelation )* )
			// Meta.g:231:2: T_DELETE (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )? T_FROM tablename= getTableID T_WHERE rel1= getRelation ( T_AND relN= getRelation )*
			{
			match(input,T_DELETE,FOLLOW_T_DELETE_in_deleteStatement1991); 
			// Meta.g:232:2: (firstField= T_IDENT ( T_COMMA field= T_IDENT )* )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==T_IDENT) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// Meta.g:232:3: firstField= T_IDENT ( T_COMMA field= T_IDENT )*
					{
					firstField=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_deleteStatement1998); 
					ds.addColumn((firstField!=null?firstField.getText():null));
					// Meta.g:233:3: ( T_COMMA field= T_IDENT )*
					loop1:
					while (true) {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( (LA1_0==T_COMMA) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// Meta.g:233:4: T_COMMA field= T_IDENT
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_deleteStatement2005); 
							field=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_deleteStatement2009); 
							ds.addColumn((field!=null?field.getText():null));
							}
							break;

						default :
							break loop1;
						}
					}

					}
					break;

			}

			match(input,T_FROM,FOLLOW_T_FROM_in_deleteStatement2028); 
			pushFollow(FOLLOW_getTableID_in_deleteStatement2033);
			tablename=getTableID();
			state._fsp--;

			ds.setTablename(tablename);
			match(input,T_WHERE,FOLLOW_T_WHERE_in_deleteStatement2038); 
			pushFollow(FOLLOW_getRelation_in_deleteStatement2043);
			rel1=getRelation();
			state._fsp--;

			ds.addRelation(rel1);
			// Meta.g:238:44: ( T_AND relN= getRelation )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0==T_AND) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// Meta.g:238:45: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_deleteStatement2048); 
					pushFollow(FOLLOW_getRelation_in_deleteStatement2052);
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
	// Meta.g:242:1: addStatement returns [AddStatement as] : T_ADD ( T_QUOTE | T_SINGLE_QUOTE ) name= T_PATH ( T_QUOTE | T_SINGLE_QUOTE ) ;
	public final AddStatement addStatement() throws RecognitionException {
		AddStatement as = null;


		Token name=null;

		try {
			// Meta.g:242:39: ( T_ADD ( T_QUOTE | T_SINGLE_QUOTE ) name= T_PATH ( T_QUOTE | T_SINGLE_QUOTE ) )
			// Meta.g:243:2: T_ADD ( T_QUOTE | T_SINGLE_QUOTE ) name= T_PATH ( T_QUOTE | T_SINGLE_QUOTE )
			{
			match(input,T_ADD,FOLLOW_T_ADD_in_addStatement2071); 
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			name=(Token)match(input,T_PATH,FOLLOW_T_PATH_in_addStatement2083); 
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
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
	// Meta.g:247:1: listStatement returns [ListStatement ls] : T_LIST (type= getListTypes ) ;
	public final ListStatement listStatement() throws RecognitionException {
		ListStatement ls = null;


		ParserRuleReturnScope type =null;

		try {
			// Meta.g:247:41: ( T_LIST (type= getListTypes ) )
			// Meta.g:248:2: T_LIST (type= getListTypes )
			{
			match(input,T_LIST,FOLLOW_T_LIST_in_listStatement2108); 
			// Meta.g:248:9: (type= getListTypes )
			// Meta.g:248:10: type= getListTypes
			{
			pushFollow(FOLLOW_getListTypes_in_listStatement2113);
			type=getListTypes();
			state._fsp--;

			}


					if((type!=null?input.toString(type.start,type.stop):null) != null){
						ls = new ListStatement((type!=null?input.toString(type.start,type.stop):null));
					}else{
						throw new RecognitionException();
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
		return ls;
	}
	// $ANTLR end "listStatement"



	// $ANTLR start "removeUDFStatement"
	// Meta.g:259:1: removeUDFStatement returns [RemoveUDFStatement rus] : T_REMOVE T_UDF ( T_QUOTE | T_SINGLE_QUOTE ) jar= getTerm ( T_QUOTE | T_SINGLE_QUOTE ) ;
	public final RemoveUDFStatement removeUDFStatement() throws RecognitionException {
		RemoveUDFStatement rus = null;


		Term jar =null;

		try {
			// Meta.g:259:52: ( T_REMOVE T_UDF ( T_QUOTE | T_SINGLE_QUOTE ) jar= getTerm ( T_QUOTE | T_SINGLE_QUOTE ) )
			// Meta.g:261:2: T_REMOVE T_UDF ( T_QUOTE | T_SINGLE_QUOTE ) jar= getTerm ( T_QUOTE | T_SINGLE_QUOTE )
			{
			match(input,T_REMOVE,FOLLOW_T_REMOVE_in_removeUDFStatement2134); 
			match(input,T_UDF,FOLLOW_T_UDF_in_removeUDFStatement2136); 
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_getTerm_in_removeUDFStatement2148);
			jar=getTerm();
			state._fsp--;

			rus = new RemoveUDFStatement(jar.getTerm());
			if ( input.LA(1)==T_QUOTE||input.LA(1)==T_SINGLE_QUOTE ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
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
		return rus;
	}
	// $ANTLR end "removeUDFStatement"



	// $ANTLR start "dropIndexStatement"
	// Meta.g:265:1: dropIndexStatement returns [DropIndexStatement dis] : T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT ;
	public final DropIndexStatement dropIndexStatement() throws RecognitionException {
		DropIndexStatement dis = null;


		Token name=null;


				dis = new DropIndexStatement();
			
		try {
			// Meta.g:268:3: ( T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT )
			// Meta.g:269:2: T_DROP T_INDEX ( T_IF T_EXISTS )? name= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropIndexStatement2178); 
			match(input,T_INDEX,FOLLOW_T_INDEX_in_dropIndexStatement2180); 
			// Meta.g:270:2: ( T_IF T_EXISTS )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==T_IF) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// Meta.g:270:3: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropIndexStatement2184); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropIndexStatement2186); 
					dis.setDropIfExists();
					}
					break;

			}

			name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropIndexStatement2195); 
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
	// Meta.g:278:1: createIndexStatement returns [CreateIndexStatement cis] : T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? (name= T_IDENT )? T_ON tablename= getTableID T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? ;
	public final CreateIndexStatement createIndexStatement() throws RecognitionException {
		CreateIndexStatement cis = null;


		Token indexType=null;
		Token name=null;
		Token firstField=null;
		Token field=null;
		Token key=null;
		String tablename =null;
		Term usingClass =null;
		ValueProperty value =null;


				cis = new CreateIndexStatement();
			
		try {
			// Meta.g:281:3: ( T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? (name= T_IDENT )? T_ON tablename= getTableID T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )? )
			// Meta.g:282:2: T_CREATE indexType= T_INDEX_TYPE T_INDEX ( T_IF T_NOT T_EXISTS )? (name= T_IDENT )? T_ON tablename= getTableID T_START_PARENTHESIS firstField= T_IDENT ( T_COMMA field= T_IDENT )* T_END_PARENTHESIS ( T_USING usingClass= getTerm )? ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createIndexStatement2220); 
			indexType=(Token)match(input,T_INDEX_TYPE,FOLLOW_T_INDEX_TYPE_in_createIndexStatement2224); 
			cis.setIndexType((indexType!=null?indexType.getText():null));
			match(input,T_INDEX,FOLLOW_T_INDEX_in_createIndexStatement2228); 
			// Meta.g:283:2: ( T_IF T_NOT T_EXISTS )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==T_IF) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// Meta.g:283:3: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createIndexStatement2232); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createIndexStatement2234); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createIndexStatement2236); 
					cis.setCreateIfNotExists();
					}
					break;

			}

			// Meta.g:284:2: (name= T_IDENT )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==T_IDENT) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Meta.g:284:3: name= T_IDENT
					{
					name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2246); 
					cis.setName((name!=null?name.getText():null));
					}
					break;

			}

			match(input,T_ON,FOLLOW_T_ON_in_createIndexStatement2254); 
			pushFollow(FOLLOW_getTableID_in_createIndexStatement2258);
			tablename=getTableID();
			state._fsp--;

			cis.setTablename(tablename);
			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createIndexStatement2263); 
			firstField=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2268); 
			cis.addColumn((firstField!=null?firstField.getText():null));
			// Meta.g:288:2: ( T_COMMA field= T_IDENT )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==T_COMMA) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// Meta.g:288:3: T_COMMA field= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createIndexStatement2274); 
					field=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2280); 
					cis.addColumn((field!=null?field.getText():null));
					}
					break;

				default :
					break loop7;
				}
			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createIndexStatement2289); 
			// Meta.g:292:2: ( T_USING usingClass= getTerm )?
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==T_USING) ) {
				alt8=1;
			}
			switch (alt8) {
				case 1 :
					// Meta.g:292:3: T_USING usingClass= getTerm
					{
					match(input,T_USING,FOLLOW_T_USING_in_createIndexStatement2293); 
					pushFollow(FOLLOW_getTerm_in_createIndexStatement2297);
					usingClass=getTerm();
					state._fsp--;

					cis.setUsingClass(usingClass.getTerm());
					}
					break;

			}

			// Meta.g:293:2: ( T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )* )?
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0==T_WITH) ) {
				alt10=1;
			}
			switch (alt10) {
				case 1 :
					// Meta.g:293:3: T_WITH T_OPTIONS key= T_IDENT T_EQUAL value= getValueProperty ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createIndexStatement2305); 
					match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_createIndexStatement2307); 
					key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2311); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement2313); 
					pushFollow(FOLLOW_getValueProperty_in_createIndexStatement2317);
					value=getValueProperty();
					state._fsp--;

					cis.addOption((key!=null?key.getText():null), value);
					// Meta.g:294:3: ( T_AND key= T_IDENT T_EQUAL value= getValueProperty )*
					loop9:
					while (true) {
						int alt9=2;
						int LA9_0 = input.LA(1);
						if ( (LA9_0==T_AND) ) {
							alt9=1;
						}

						switch (alt9) {
						case 1 :
							// Meta.g:294:4: T_AND key= T_IDENT T_EQUAL value= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_createIndexStatement2324); 
							key=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createIndexStatement2328); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createIndexStatement2330); 
							pushFollow(FOLLOW_getValueProperty_in_createIndexStatement2334);
							value=getValueProperty();
							state._fsp--;

							cis.addOption((key!=null?key.getText():null), value);
							}
							break;

						default :
							break loop9;
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
	// Meta.g:303:1: updateTableStatement returns [UpdateTableStatement pdtbst] : T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )? ;
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
		Term term1 =null;
		Term termN =null;


		        boolean optsInc = false;
		        boolean condsInc = false;
		        List<Option> options = new ArrayList<>();
		        List<Assignment> assignments = new ArrayList<>();
		        List<MetaRelation> whereclauses = new ArrayList<>();
		        Map<String, Term> conditions = new HashMap<>();
		    
		try {
			// Meta.g:311:6: ( T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )? )
			// Meta.g:312:5: T_UPDATE tablename= getTableID ( T_USING opt1= getOption (optN= getOption )* )? T_SET assig1= getAssignment ( T_COMMA assigN= getAssignment )* T_WHERE rel1= getRelation ( T_AND relN= getRelation )* ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )?
			{
			match(input,T_UPDATE,FOLLOW_T_UPDATE_in_updateTableStatement2375); 
			pushFollow(FOLLOW_getTableID_in_updateTableStatement2379);
			tablename=getTableID();
			state._fsp--;

			// Meta.g:313:5: ( T_USING opt1= getOption (optN= getOption )* )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==T_USING) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// Meta.g:313:6: T_USING opt1= getOption (optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_updateTableStatement2386); 
					pushFollow(FOLLOW_getOption_in_updateTableStatement2390);
					opt1=getOption();
					state._fsp--;

					optsInc = true; options.add(opt1);
					// Meta.g:313:66: (optN= getOption )*
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( (LA11_0==T_CLUSTERING||LA11_0==T_COMPACT||LA11_0==T_IDENT) ) {
							alt11=1;
						}

						switch (alt11) {
						case 1 :
							// Meta.g:313:67: optN= getOption
							{
							pushFollow(FOLLOW_getOption_in_updateTableStatement2397);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop11;
						}
					}

					}
					break;

			}

			match(input,T_SET,FOLLOW_T_SET_in_updateTableStatement2409); 
			pushFollow(FOLLOW_getAssignment_in_updateTableStatement2413);
			assig1=getAssignment();
			state._fsp--;

			assignments.add(assig1);
			// Meta.g:314:59: ( T_COMMA assigN= getAssignment )*
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( (LA13_0==T_COMMA) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// Meta.g:314:60: T_COMMA assigN= getAssignment
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_updateTableStatement2418); 
					pushFollow(FOLLOW_getAssignment_in_updateTableStatement2422);
					assigN=getAssignment();
					state._fsp--;

					assignments.add(assigN);
					}
					break;

				default :
					break loop13;
				}
			}

			match(input,T_WHERE,FOLLOW_T_WHERE_in_updateTableStatement2432); 
			pushFollow(FOLLOW_getRelation_in_updateTableStatement2436);
			rel1=getRelation();
			state._fsp--;

			whereclauses.add(rel1);
			// Meta.g:315:56: ( T_AND relN= getRelation )*
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==T_AND) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// Meta.g:315:57: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_updateTableStatement2441); 
					pushFollow(FOLLOW_getRelation_in_updateTableStatement2445);
					relN=getRelation();
					state._fsp--;

					whereclauses.add(relN);
					}
					break;

				default :
					break loop14;
				}
			}

			// Meta.g:316:5: ( T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )* )?
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==T_IF) ) {
				alt16=1;
			}
			switch (alt16) {
				case 1 :
					// Meta.g:316:6: T_IF id1= T_IDENT T_EQUAL term1= getTerm ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )*
					{
					match(input,T_IF,FOLLOW_T_IF_in_updateTableStatement2456); 
					id1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_updateTableStatement2460); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_updateTableStatement2462); 
					pushFollow(FOLLOW_getTerm_in_updateTableStatement2466);
					term1=getTerm();
					state._fsp--;

					condsInc = true; conditions.put((id1!=null?id1.getText():null), new Term(term1.getTerm()));
					// Meta.g:317:21: ( T_AND idN= T_IDENT T_EQUAL termN= getTerm )*
					loop15:
					while (true) {
						int alt15=2;
						int LA15_0 = input.LA(1);
						if ( (LA15_0==T_AND) ) {
							alt15=1;
						}

						switch (alt15) {
						case 1 :
							// Meta.g:317:22: T_AND idN= T_IDENT T_EQUAL termN= getTerm
							{
							match(input,T_AND,FOLLOW_T_AND_in_updateTableStatement2492); 
							idN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_updateTableStatement2496); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_updateTableStatement2498); 
							pushFollow(FOLLOW_getTerm_in_updateTableStatement2502);
							termN=getTerm();
							state._fsp--;

							conditions.put((idN!=null?idN.getText():null), new Term(termN.getTerm()));
							}
							break;

						default :
							break loop15;
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
	// Meta.g:332:1: stopProcessStatement returns [StopProcessStatement stprst] : T_STOP T_PROCESS ident= T_IDENT ;
	public final StopProcessStatement stopProcessStatement() throws RecognitionException {
		StopProcessStatement stprst = null;


		Token ident=null;

		try {
			// Meta.g:332:59: ( T_STOP T_PROCESS ident= T_IDENT )
			// Meta.g:333:5: T_STOP T_PROCESS ident= T_IDENT
			{
			match(input,T_STOP,FOLLOW_T_STOP_in_stopProcessStatement2534); 
			match(input,T_PROCESS,FOLLOW_T_PROCESS_in_stopProcessStatement2536); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_stopProcessStatement2540); 
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
	// Meta.g:336:1: dropTriggerStatement returns [DropTriggerStatement drtrst] : T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT ;
	public final DropTriggerStatement dropTriggerStatement() throws RecognitionException {
		DropTriggerStatement drtrst = null;


		Token ident=null;
		Token ident2=null;

		try {
			// Meta.g:336:59: ( T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT )
			// Meta.g:337:5: T_DROP T_TRIGGER ident= T_IDENT T_ON ident2= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTriggerStatement2562); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_dropTriggerStatement2569); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement2573); 
			match(input,T_ON,FOLLOW_T_ON_in_dropTriggerStatement2580); 
			ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropTriggerStatement2589); 
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
	// Meta.g:344:1: createTriggerStatement returns [CreateTriggerStatement crtrst] : T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT ;
	public final CreateTriggerStatement createTriggerStatement() throws RecognitionException {
		CreateTriggerStatement crtrst = null;


		Token trigger_name=null;
		Token table_name=null;
		Token class_name=null;

		try {
			// Meta.g:344:63: ( T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT )
			// Meta.g:345:5: T_CREATE T_TRIGGER trigger_name= T_IDENT T_ON table_name= T_IDENT T_USING class_name= T_IDENT
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTriggerStatement2617); 
			match(input,T_TRIGGER,FOLLOW_T_TRIGGER_in_createTriggerStatement2624); 
			trigger_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2628); 
			match(input,T_ON,FOLLOW_T_ON_in_createTriggerStatement2635); 
			table_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2644); 
			match(input,T_USING,FOLLOW_T_USING_in_createTriggerStatement2650); 
			class_name=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createTriggerStatement2654); 
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
	// Meta.g:354:1: createTableStatement returns [CreateTableStatement crtast] : T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= getTableID '(' (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' ) )* ) ')' ( T_WITH properties= getMetaProperties )? ;
	public final CreateTableStatement createTableStatement() throws RecognitionException {
		CreateTableStatement crtast = null;


		Token ident_column1=null;
		Token ident_columN=null;
		Token primaryK=null;
		Token partitionKN=null;
		Token partitionK=null;
		Token clusterKN=null;
		String name_table =null;
		String type1 =null;
		String typeN =null;
		List<MetaProperty> properties =null;


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
			// Meta.g:366:6: ( T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= getTableID '(' (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' ) )* ) ')' ( T_WITH properties= getMetaProperties )? )
			// Meta.g:367:5: T_CREATE T_TABLE ( T_IF T_NOT T_EXISTS )? name_table= getTableID '(' (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' ) )* ) ')' ( T_WITH properties= getMetaProperties )?
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createTableStatement2693); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_createTableStatement2699); 
			// Meta.g:369:5: ( T_IF T_NOT T_EXISTS )?
			int alt17=2;
			int LA17_0 = input.LA(1);
			if ( (LA17_0==T_IF) ) {
				alt17=1;
			}
			switch (alt17) {
				case 1 :
					// Meta.g:369:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createTableStatement2706); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createTableStatement2708); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createTableStatement2710); 
					ifNotExists_2 = true;
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_createTableStatement2723);
			name_table=getTableID();
			state._fsp--;

			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement2729); 
			// Meta.g:371:9: (ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' ) )* )
			// Meta.g:372:17: ident_column1= ( T_IDENT | T_KEY ) type1= getDataType ( T_PRIMARY T_KEY )? ( ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' ) )*
			{
			ident_column1=input.LT(1);
			if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			pushFollow(FOLLOW_getDataType_in_createTableStatement2773);
			type1=getDataType();
			state._fsp--;

			// Meta.g:372:67: ( T_PRIMARY T_KEY )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==T_PRIMARY) ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// Meta.g:372:68: T_PRIMARY T_KEY
					{
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2776); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2778); 
					}
					break;

			}

			columns.put((ident_column1!=null?ident_column1.getText():null),type1); Type_Primary_Key=1;
			// Meta.g:373:17: ( ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? ) | ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' ) )*
			loop24:
			while (true) {
				int alt24=3;
				int LA24_0 = input.LA(1);
				if ( (LA24_0==T_COMMA) ) {
					int LA24_2 = input.LA(2);
					if ( (LA24_2==T_IDENT||LA24_2==T_KEY) ) {
						alt24=1;
					}
					else if ( (LA24_2==T_PRIMARY) ) {
						alt24=2;
					}

				}

				switch (alt24) {
				case 1 :
					// Meta.g:374:21: ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? )
					{
					// Meta.g:374:21: ( ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )? )
					// Meta.g:374:23: ',' ident_columN= ( T_IDENT | T_KEY ) typeN= getDataType ( T_PRIMARY T_KEY )?
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement2827); 
					ident_columN=input.LT(1);
					if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
						input.consume();
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_getDataType_in_createTableStatement2841);
					typeN=getDataType();
					state._fsp--;

					// Meta.g:374:76: ( T_PRIMARY T_KEY )?
					int alt19=2;
					int LA19_0 = input.LA(1);
					if ( (LA19_0==T_PRIMARY) ) {
						alt19=1;
					}
					switch (alt19) {
						case 1 :
							// Meta.g:374:77: T_PRIMARY T_KEY
							{
							match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2844); 
							match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2846); 
							Type_Primary_Key=2;columnNumberPK=columnNumberPK_inter +1;
							}
							break;

					}

					columns.put((ident_columN!=null?ident_columN.getText():null),typeN);columnNumberPK_inter+=1;
					}

					}
					break;
				case 2 :
					// Meta.g:375:22: ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' )
					{
					// Meta.g:375:22: ( ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')' )
					// Meta.g:376:25: ',' T_PRIMARY T_KEY '(' ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) ) ')'
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement2904); 
					match(input,T_PRIMARY,FOLLOW_T_PRIMARY_in_createTableStatement2906); 
					match(input,T_KEY,FOLLOW_T_KEY_in_createTableStatement2908); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement2910); 
					// Meta.g:377:25: ( (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ) | ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* ) )
					int alt23=2;
					int LA23_0 = input.LA(1);
					if ( (LA23_0==T_IDENT||LA23_0==T_KEY) ) {
						alt23=1;
					}
					else if ( (LA23_0==T_START_PARENTHESIS) ) {
						alt23=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 23, 0, input);
						throw nvae;
					}

					switch (alt23) {
						case 1 :
							// Meta.g:378:29: (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* )
							{
							// Meta.g:378:29: (primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* )
							// Meta.g:378:33: primaryK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )*
							{
							primaryK=input.LT(1);
							if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
								input.consume();
								state.errorRecovery=false;
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							primaryKey.add((primaryK!=null?primaryK.getText():null));Type_Primary_Key=3;
							// Meta.g:380:33: ( ',' partitionKN= ( T_IDENT | T_KEY ) )*
							loop20:
							while (true) {
								int alt20=2;
								int LA20_0 = input.LA(1);
								if ( (LA20_0==T_COMMA) ) {
									alt20=1;
								}

								switch (alt20) {
								case 1 :
									// Meta.g:380:34: ',' partitionKN= ( T_IDENT | T_KEY )
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3043); 
									partitionKN=input.LT(1);
									if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
										input.consume();
										state.errorRecovery=false;
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										throw mse;
									}
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop20;
								}
							}

							}

							}
							break;
						case 2 :
							// Meta.g:382:30: ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* )
							{
							// Meta.g:382:30: ( '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )* )
							// Meta.g:383:33: '(' partitionK= ( T_IDENT | T_KEY ) ( ',' partitionKN= ( T_IDENT | T_KEY ) )* ')' ( ',' clusterKN= ( T_IDENT | T_KEY ) )*
							{
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_createTableStatement3151); 
							partitionK=input.LT(1);
							if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
								input.consume();
								state.errorRecovery=false;
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							primaryKey.add((partitionK!=null?partitionK.getText():null));Type_Primary_Key=4;
							// Meta.g:384:37: ( ',' partitionKN= ( T_IDENT | T_KEY ) )*
							loop21:
							while (true) {
								int alt21=2;
								int LA21_0 = input.LA(1);
								if ( (LA21_0==T_COMMA) ) {
									alt21=1;
								}

								switch (alt21) {
								case 1 :
									// Meta.g:384:38: ',' partitionKN= ( T_IDENT | T_KEY )
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3202); 
									partitionKN=input.LT(1);
									if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
										input.consume();
										state.errorRecovery=false;
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										throw mse;
									}
									primaryKey.add((partitionKN!=null?partitionKN.getText():null));
									}
									break;

								default :
									break loop21;
								}
							}

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3249); 
							// Meta.g:386:33: ( ',' clusterKN= ( T_IDENT | T_KEY ) )*
							loop22:
							while (true) {
								int alt22=2;
								int LA22_0 = input.LA(1);
								if ( (LA22_0==T_COMMA) ) {
									alt22=1;
								}

								switch (alt22) {
								case 1 :
									// Meta.g:386:34: ',' clusterKN= ( T_IDENT | T_KEY )
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_createTableStatement3285); 
									clusterKN=input.LT(1);
									if ( input.LA(1)==T_IDENT||input.LA(1)==T_KEY ) {
										input.consume();
										state.errorRecovery=false;
									}
									else {
										MismatchedSetException mse = new MismatchedSetException(null,input);
										throw mse;
									}
									clusterKey.add((clusterKN!=null?clusterKN.getText():null));withClusterKey=true;
									}
									break;

								default :
									break loop22;
								}
							}

							}

							}
							break;

					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3381); 
					}

					}
					break;

				default :
					break loop24;
				}
			}

			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_createTableStatement3453); 
			// Meta.g:394:9: ( T_WITH properties= getMetaProperties )?
			int alt25=2;
			int LA25_0 = input.LA(1);
			if ( (LA25_0==T_WITH) ) {
				alt25=1;
			}
			switch (alt25) {
				case 1 :
					// Meta.g:394:10: T_WITH properties= getMetaProperties
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_createTableStatement3456); 
					withPropierties=true;
					pushFollow(FOLLOW_getMetaProperties_in_createTableStatement3462);
					properties=getMetaProperties();
					state._fsp--;

					}
					break;

			}

			crtast = new CreateTableStatement(name_table,columns,primaryKey,clusterKey,properties,Type_Primary_Key,ifNotExists_2,withClusterKey,columnNumberPK,withPropierties);
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
	// Meta.g:400:1: alterTableStatement returns [AlterTableStatement altast] : T_ALTER T_TABLE name_table= getTableID ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) ;
	public final AlterTableStatement alterTableStatement() throws RecognitionException {
		AlterTableStatement altast = null;


		Token column=null;
		Token type=null;
		Token identProp1=null;
		Token identPropN=null;
		String name_table =null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        LinkedHashMap<String, ValueProperty> option = new LinkedHashMap<>();
		        int prop= 0;
		    
		try {
			// Meta.g:404:6: ( T_ALTER T_TABLE name_table= getTableID ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ) )
			// Meta.g:405:5: T_ALTER T_TABLE name_table= getTableID ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement3525); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_alterTableStatement3531); 
			pushFollow(FOLLOW_getTableID_in_alterTableStatement3539);
			name_table=getTableID();
			state._fsp--;

			// Meta.g:408:5: ( T_ALTER column= T_IDENT T_TYPE type= T_IDENT | T_ADD column= T_IDENT type= T_IDENT | T_DROP column= T_IDENT | T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			int alt27=4;
			switch ( input.LA(1) ) {
			case T_ALTER:
				{
				alt27=1;
				}
				break;
			case T_ADD:
				{
				alt27=2;
				}
				break;
			case T_DROP:
				{
				alt27=3;
				}
				break;
			case T_WITH:
				{
				alt27=4;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 27, 0, input);
				throw nvae;
			}
			switch (alt27) {
				case 1 :
					// Meta.g:408:6: T_ALTER column= T_IDENT T_TYPE type= T_IDENT
					{
					match(input,T_ALTER,FOLLOW_T_ALTER_in_alterTableStatement3546); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3550); 
					match(input,T_TYPE,FOLLOW_T_TYPE_in_alterTableStatement3552); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3556); 
					prop=1;
					}
					break;
				case 2 :
					// Meta.g:409:10: T_ADD column= T_IDENT type= T_IDENT
					{
					match(input,T_ADD,FOLLOW_T_ADD_in_alterTableStatement3569); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3573); 
					type=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3577); 
					prop=2;
					}
					break;
				case 3 :
					// Meta.g:410:10: T_DROP column= T_IDENT
					{
					match(input,T_DROP,FOLLOW_T_DROP_in_alterTableStatement3590); 
					column=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3594); 
					prop=3;
					}
					break;
				case 4 :
					// Meta.g:411:10: T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_alterTableStatement3607); 
					identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3624); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement3626); 
					pushFollow(FOLLOW_getValueProperty_in_alterTableStatement3630);
					valueProp1=getValueProperty();
					state._fsp--;

					option.put((identProp1!=null?identProp1.getText():null), valueProp1);
					// Meta.g:413:13: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
					loop26:
					while (true) {
						int alt26=2;
						int LA26_0 = input.LA(1);
						if ( (LA26_0==T_AND) ) {
							alt26=1;
						}

						switch (alt26) {
						case 1 :
							// Meta.g:413:14: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
							{
							match(input,T_AND,FOLLOW_T_AND_in_alterTableStatement3647); 
							identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterTableStatement3651); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterTableStatement3653); 
							pushFollow(FOLLOW_getValueProperty_in_alterTableStatement3657);
							valuePropN=getValueProperty();
							state._fsp--;

							option.put((identPropN!=null?identPropN.getText():null), valuePropN);
							}
							break;

						default :
							break loop26;
						}
					}

					prop=4;
					}
					break;

			}

			altast = new AlterTableStatement(name_table, (column!=null?column.getText():null), (type!=null?type.getText():null), option, prop);  
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
	// Meta.g:419:1: selectStatement returns [SelectStatement slctst] : T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )? ;
	public final SelectStatement selectStatement() throws RecognitionException {
		SelectStatement slctst = null;


		Token constant=null;
		SelectionClause selClause =null;
		String tablename =null;
		WindowSelect window =null;
		String identJoin =null;
		Map<String, String> fields =null;
		List<MetaRelation> whereClauses =null;
		List<MetaOrdering> ordering =null;
		List groupby =null;


		        boolean windowInc = false;
		        boolean joinInc = false;
		        boolean whereInc = false;
		        boolean orderInc = false;
		        boolean groupInc = false;
		        boolean limitInc = false;
		        boolean disable = false;
		    
		try {
			// Meta.g:428:6: ( T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )? )
			// Meta.g:429:5: T_SELECT selClause= getSelectClause T_FROM tablename= getTableID ( T_WITH T_WINDOW window= getWindow )? ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )? ( T_WHERE whereClauses= getWhereClauses )? ( T_ORDER T_BY ordering= getOrdering )? ( T_GROUP T_BY groupby= getList )? ( T_LIMIT constant= T_CONSTANT )? ( T_DISABLE T_ANALYTICS )?
			{
			match(input,T_SELECT,FOLLOW_T_SELECT_in_selectStatement3712); 
			pushFollow(FOLLOW_getSelectClause_in_selectStatement3716);
			selClause=getSelectClause();
			state._fsp--;

			match(input,T_FROM,FOLLOW_T_FROM_in_selectStatement3718); 
			pushFollow(FOLLOW_getTableID_in_selectStatement3722);
			tablename=getTableID();
			state._fsp--;

			// Meta.g:430:5: ( T_WITH T_WINDOW window= getWindow )?
			int alt28=2;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==T_WITH) ) {
				alt28=1;
			}
			switch (alt28) {
				case 1 :
					// Meta.g:430:6: T_WITH T_WINDOW window= getWindow
					{
					match(input,T_WITH,FOLLOW_T_WITH_in_selectStatement3730); 
					match(input,T_WINDOW,FOLLOW_T_WINDOW_in_selectStatement3732); 
					windowInc = true;
					pushFollow(FOLLOW_getWindow_in_selectStatement3738);
					window=getWindow();
					state._fsp--;

					}
					break;

			}

			// Meta.g:431:5: ( T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields )?
			int alt29=2;
			int LA29_0 = input.LA(1);
			if ( (LA29_0==T_INNER) ) {
				alt29=1;
			}
			switch (alt29) {
				case 1 :
					// Meta.g:431:6: T_INNER T_JOIN identJoin= getTableID T_ON fields= getFields
					{
					match(input,T_INNER,FOLLOW_T_INNER_in_selectStatement3751); 
					match(input,T_JOIN,FOLLOW_T_JOIN_in_selectStatement3753); 
					 joinInc = true;
					pushFollow(FOLLOW_getTableID_in_selectStatement3759);
					identJoin=getTableID();
					state._fsp--;

					match(input,T_ON,FOLLOW_T_ON_in_selectStatement3761); 
					pushFollow(FOLLOW_getFields_in_selectStatement3765);
					fields=getFields();
					state._fsp--;

					}
					break;

			}

			// Meta.g:432:5: ( T_WHERE whereClauses= getWhereClauses )?
			int alt30=2;
			int LA30_0 = input.LA(1);
			if ( (LA30_0==T_WHERE) ) {
				alt30=1;
			}
			switch (alt30) {
				case 1 :
					// Meta.g:432:6: T_WHERE whereClauses= getWhereClauses
					{
					match(input,T_WHERE,FOLLOW_T_WHERE_in_selectStatement3774); 
					whereInc = true;
					pushFollow(FOLLOW_getWhereClauses_in_selectStatement3780);
					whereClauses=getWhereClauses();
					state._fsp--;

					}
					break;

			}

			// Meta.g:433:5: ( T_ORDER T_BY ordering= getOrdering )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==T_ORDER) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// Meta.g:433:6: T_ORDER T_BY ordering= getOrdering
					{
					match(input,T_ORDER,FOLLOW_T_ORDER_in_selectStatement3789); 
					match(input,T_BY,FOLLOW_T_BY_in_selectStatement3791); 
					orderInc = true;
					pushFollow(FOLLOW_getOrdering_in_selectStatement3797);
					ordering=getOrdering();
					state._fsp--;

					}
					break;

			}

			// Meta.g:434:5: ( T_GROUP T_BY groupby= getList )?
			int alt32=2;
			int LA32_0 = input.LA(1);
			if ( (LA32_0==T_GROUP) ) {
				alt32=1;
			}
			switch (alt32) {
				case 1 :
					// Meta.g:434:6: T_GROUP T_BY groupby= getList
					{
					match(input,T_GROUP,FOLLOW_T_GROUP_in_selectStatement3806); 
					match(input,T_BY,FOLLOW_T_BY_in_selectStatement3808); 
					groupInc = true;
					pushFollow(FOLLOW_getList_in_selectStatement3814);
					groupby=getList();
					state._fsp--;

					}
					break;

			}

			// Meta.g:435:5: ( T_LIMIT constant= T_CONSTANT )?
			int alt33=2;
			int LA33_0 = input.LA(1);
			if ( (LA33_0==T_LIMIT) ) {
				alt33=1;
			}
			switch (alt33) {
				case 1 :
					// Meta.g:435:6: T_LIMIT constant= T_CONSTANT
					{
					match(input,T_LIMIT,FOLLOW_T_LIMIT_in_selectStatement3823); 
					limitInc = true;
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_selectStatement3829); 
					}
					break;

			}

			// Meta.g:436:5: ( T_DISABLE T_ANALYTICS )?
			int alt34=2;
			int LA34_0 = input.LA(1);
			if ( (LA34_0==T_DISABLE) ) {
				alt34=1;
			}
			switch (alt34) {
				case 1 :
					// Meta.g:436:6: T_DISABLE T_ANALYTICS
					{
					match(input,T_DISABLE,FOLLOW_T_DISABLE_in_selectStatement3838); 
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_selectStatement3840); 
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
	// Meta.g:455:1: insertIntoStatement returns [InsertIntoStatement nsntst] : T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? ;
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
			// Meta.g:463:6: ( T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )? )
			// Meta.g:464:5: T_INSERT T_INTO tableName= getTableID T_START_PARENTHESIS ident1= T_IDENT ( T_COMMA identN= T_IDENT )* T_END_PARENTHESIS (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS ) ( T_IF T_NOT T_EXISTS )? ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			{
			match(input,T_INSERT,FOLLOW_T_INSERT_in_insertIntoStatement3873); 
			match(input,T_INTO,FOLLOW_T_INTO_in_insertIntoStatement3880); 
			pushFollow(FOLLOW_getTableID_in_insertIntoStatement3889);
			tableName=getTableID();
			state._fsp--;

			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3895); 
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement3904); 
			ids.add((ident1!=null?ident1.getText():null));
			// Meta.g:469:5: ( T_COMMA identN= T_IDENT )*
			loop35:
			while (true) {
				int alt35=2;
				int LA35_0 = input.LA(1);
				if ( (LA35_0==T_COMMA) ) {
					alt35=1;
				}

				switch (alt35) {
				case 1 :
					// Meta.g:469:6: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement3914); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_insertIntoStatement3918); 
					ids.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop35;
				}
			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement3929); 
			// Meta.g:471:5: (selectStmnt= selectStatement | T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS )
			int alt37=2;
			int LA37_0 = input.LA(1);
			if ( (LA37_0==T_SELECT) ) {
				alt37=1;
			}
			else if ( (LA37_0==T_VALUES) ) {
				alt37=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 37, 0, input);
				throw nvae;
			}

			switch (alt37) {
				case 1 :
					// Meta.g:472:9: selectStmnt= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_insertIntoStatement3948);
					selectStmnt=selectStatement();
					state._fsp--;

					typeValues = InsertIntoStatement.TYPE_SELECT_CLAUSE;
					}
					break;
				case 2 :
					// Meta.g:474:9: T_VALUES T_START_PARENTHESIS term1= getTermOrLiteral ( T_COMMA termN= getTermOrLiteral )* T_END_PARENTHESIS
					{
					match(input,T_VALUES,FOLLOW_T_VALUES_in_insertIntoStatement3971); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3981); 
					pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement3998);
					term1=getTermOrLiteral();
					state._fsp--;

					cellValues.add(term1);
					// Meta.g:477:13: ( T_COMMA termN= getTermOrLiteral )*
					loop36:
					while (true) {
						int alt36=2;
						int LA36_0 = input.LA(1);
						if ( (LA36_0==T_COMMA) ) {
							alt36=1;
						}

						switch (alt36) {
						case 1 :
							// Meta.g:477:14: T_COMMA termN= getTermOrLiteral
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_insertIntoStatement4015); 
							pushFollow(FOLLOW_getTermOrLiteral_in_insertIntoStatement4019);
							termN=getTermOrLiteral();
							state._fsp--;

							cellValues.add(termN);
							}
							break;

						default :
							break loop36;
						}
					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement4033); 
					}
					break;

			}

			// Meta.g:480:5: ( T_IF T_NOT T_EXISTS )?
			int alt38=2;
			int LA38_0 = input.LA(1);
			if ( (LA38_0==T_IF) ) {
				alt38=1;
			}
			switch (alt38) {
				case 1 :
					// Meta.g:480:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_insertIntoStatement4046); 
					match(input,T_NOT,FOLLOW_T_NOT_in_insertIntoStatement4048); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_insertIntoStatement4050); 
					ifNotExists=true;
					}
					break;

			}

			// Meta.g:481:5: ( T_USING opt1= getOption ( T_AND optN= getOption )* )?
			int alt40=2;
			int LA40_0 = input.LA(1);
			if ( (LA40_0==T_USING) ) {
				alt40=1;
			}
			switch (alt40) {
				case 1 :
					// Meta.g:482:9: T_USING opt1= getOption ( T_AND optN= getOption )*
					{
					match(input,T_USING,FOLLOW_T_USING_in_insertIntoStatement4071); 
					optsInc=true;
					pushFollow(FOLLOW_getOption_in_insertIntoStatement4086);
					opt1=getOption();
					state._fsp--;


					            options.add(opt1);
					        
					// Meta.g:486:9: ( T_AND optN= getOption )*
					loop39:
					while (true) {
						int alt39=2;
						int LA39_0 = input.LA(1);
						if ( (LA39_0==T_AND) ) {
							alt39=1;
						}

						switch (alt39) {
						case 1 :
							// Meta.g:486:10: T_AND optN= getOption
							{
							match(input,T_AND,FOLLOW_T_AND_in_insertIntoStatement4099); 
							pushFollow(FOLLOW_getOption_in_insertIntoStatement4103);
							optN=getOption();
							state._fsp--;

							options.add(optN);
							}
							break;

						default :
							break loop39;
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
	// Meta.g:503:1: explainPlanStatement returns [ExplainPlanStatement xpplst] : T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement ;
	public final ExplainPlanStatement explainPlanStatement() throws RecognitionException {
		ExplainPlanStatement xpplst = null;


		MetaStatement parsedStmnt =null;

		try {
			// Meta.g:503:59: ( T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement )
			// Meta.g:504:5: T_EXPLAIN T_PLAN T_FOR parsedStmnt= metaStatement
			{
			match(input,T_EXPLAIN,FOLLOW_T_EXPLAIN_in_explainPlanStatement4140); 
			match(input,T_PLAN,FOLLOW_T_PLAN_in_explainPlanStatement4142); 
			match(input,T_FOR,FOLLOW_T_FOR_in_explainPlanStatement4144); 
			pushFollow(FOLLOW_metaStatement_in_explainPlanStatement4148);
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
	// Meta.g:508:1: setOptionsStatement returns [SetOptionsStatement stptst] : T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) ;
	public final SetOptionsStatement setOptionsStatement() throws RecognitionException {
		SetOptionsStatement stptst = null;



		        ArrayList<Boolean> checks = new ArrayList<>();
		        checks.add(false);
		        checks.add(false);
		        boolean analytics = false;
		        Consistency cnstc=Consistency.ALL;
		    
		try {
			// Meta.g:515:6: ( T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? ) )
			// Meta.g:516:5: T_SET T_OPTIONS ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			{
			match(input,T_SET,FOLLOW_T_SET_in_setOptionsStatement4182); 
			match(input,T_OPTIONS,FOLLOW_T_OPTIONS_in_setOptionsStatement4184); 
			// Meta.g:516:21: ( T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )? | T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )? )
			int alt47=2;
			int LA47_0 = input.LA(1);
			if ( (LA47_0==T_ANALYTICS) ) {
				alt47=1;
			}
			else if ( (LA47_0==T_CONSISTENCY) ) {
				alt47=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 47, 0, input);
				throw nvae;
			}

			switch (alt47) {
				case 1 :
					// Meta.g:517:9: T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					{
					match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement4196); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4198); 
					// Meta.g:517:29: ( T_TRUE | T_FALSE )
					int alt41=2;
					int LA41_0 = input.LA(1);
					if ( (LA41_0==T_TRUE) ) {
						alt41=1;
					}
					else if ( (LA41_0==T_FALSE) ) {
						alt41=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 41, 0, input);
						throw nvae;
					}

					switch (alt41) {
						case 1 :
							// Meta.g:517:30: T_TRUE
							{
							match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement4201); 
							analytics=true;
							}
							break;
						case 2 :
							// Meta.g:517:54: T_FALSE
							{
							match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement4204); 
							analytics=false;
							}
							break;

					}

					checks.set(0, true);
					// Meta.g:518:9: ( T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) )?
					int alt43=2;
					int LA43_0 = input.LA(1);
					if ( (LA43_0==T_AND) ) {
						alt43=1;
					}
					switch (alt43) {
						case 1 :
							// Meta.g:518:10: T_AND T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement4219); 
							match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement4221); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4223); 
							// Meta.g:519:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
							int alt42=9;
							switch ( input.LA(1) ) {
							case T_ALL:
								{
								alt42=1;
								}
								break;
							case T_ANY:
								{
								alt42=2;
								}
								break;
							case T_QUORUM:
								{
								alt42=3;
								}
								break;
							case T_ONE:
								{
								alt42=4;
								}
								break;
							case T_TWO:
								{
								alt42=5;
								}
								break;
							case T_THREE:
								{
								alt42=6;
								}
								break;
							case T_EACH_QUORUM:
								{
								alt42=7;
								}
								break;
							case T_LOCAL_ONE:
								{
								alt42=8;
								}
								break;
							case T_LOCAL_QUORUM:
								{
								alt42=9;
								}
								break;
							default:
								NoViableAltException nvae =
									new NoViableAltException("", 42, 0, input);
								throw nvae;
							}
							switch (alt42) {
								case 1 :
									// Meta.g:519:14: T_ALL
									{
									match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement4238); 
									cnstc=Consistency.ALL;
									}
									break;
								case 2 :
									// Meta.g:520:15: T_ANY
									{
									match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement4257); 
									cnstc=Consistency.ANY;
									}
									break;
								case 3 :
									// Meta.g:521:15: T_QUORUM
									{
									match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement4275); 
									cnstc=Consistency.QUORUM;
									}
									break;
								case 4 :
									// Meta.g:522:15: T_ONE
									{
									match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement4293); 
									cnstc=Consistency.ONE;
									}
									break;
								case 5 :
									// Meta.g:523:15: T_TWO
									{
									match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement4311); 
									cnstc=Consistency.TWO;
									}
									break;
								case 6 :
									// Meta.g:524:15: T_THREE
									{
									match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement4329); 
									cnstc=Consistency.THREE;
									}
									break;
								case 7 :
									// Meta.g:525:15: T_EACH_QUORUM
									{
									match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4347); 
									cnstc=Consistency.EACH_QUORUM;
									}
									break;
								case 8 :
									// Meta.g:526:15: T_LOCAL_ONE
									{
									match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4365); 
									cnstc=Consistency.LOCAL_ONE;
									}
									break;
								case 9 :
									// Meta.g:527:15: T_LOCAL_QUORUM
									{
									match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4383); 
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
					// Meta.g:531:11: T_CONSISTENCY T_EQUAL ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM ) ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					{
					match(input,T_CONSISTENCY,FOLLOW_T_CONSISTENCY_in_setOptionsStatement4433); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4435); 
					// Meta.g:532:13: ( T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM )
					int alt44=9;
					switch ( input.LA(1) ) {
					case T_ALL:
						{
						alt44=1;
						}
						break;
					case T_ANY:
						{
						alt44=2;
						}
						break;
					case T_QUORUM:
						{
						alt44=3;
						}
						break;
					case T_ONE:
						{
						alt44=4;
						}
						break;
					case T_TWO:
						{
						alt44=5;
						}
						break;
					case T_THREE:
						{
						alt44=6;
						}
						break;
					case T_EACH_QUORUM:
						{
						alt44=7;
						}
						break;
					case T_LOCAL_ONE:
						{
						alt44=8;
						}
						break;
					case T_LOCAL_QUORUM:
						{
						alt44=9;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 44, 0, input);
						throw nvae;
					}
					switch (alt44) {
						case 1 :
							// Meta.g:532:14: T_ALL
							{
							match(input,T_ALL,FOLLOW_T_ALL_in_setOptionsStatement4451); 
							cnstc=Consistency.ALL;
							}
							break;
						case 2 :
							// Meta.g:533:15: T_ANY
							{
							match(input,T_ANY,FOLLOW_T_ANY_in_setOptionsStatement4470); 
							cnstc=Consistency.ANY;
							}
							break;
						case 3 :
							// Meta.g:534:15: T_QUORUM
							{
							match(input,T_QUORUM,FOLLOW_T_QUORUM_in_setOptionsStatement4488); 
							cnstc=Consistency.QUORUM;
							}
							break;
						case 4 :
							// Meta.g:535:15: T_ONE
							{
							match(input,T_ONE,FOLLOW_T_ONE_in_setOptionsStatement4506); 
							cnstc=Consistency.ONE;
							}
							break;
						case 5 :
							// Meta.g:536:15: T_TWO
							{
							match(input,T_TWO,FOLLOW_T_TWO_in_setOptionsStatement4524); 
							cnstc=Consistency.TWO;
							}
							break;
						case 6 :
							// Meta.g:537:15: T_THREE
							{
							match(input,T_THREE,FOLLOW_T_THREE_in_setOptionsStatement4542); 
							cnstc=Consistency.THREE;
							}
							break;
						case 7 :
							// Meta.g:538:15: T_EACH_QUORUM
							{
							match(input,T_EACH_QUORUM,FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4560); 
							cnstc=Consistency.EACH_QUORUM;
							}
							break;
						case 8 :
							// Meta.g:539:15: T_LOCAL_ONE
							{
							match(input,T_LOCAL_ONE,FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4578); 
							cnstc=Consistency.LOCAL_ONE;
							}
							break;
						case 9 :
							// Meta.g:540:15: T_LOCAL_QUORUM
							{
							match(input,T_LOCAL_QUORUM,FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4596); 
							cnstc=Consistency.LOCAL_QUORUM;
							}
							break;

					}

					checks.set(1, true);
					// Meta.g:542:9: ( T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE ) )?
					int alt46=2;
					int LA46_0 = input.LA(1);
					if ( (LA46_0==T_AND) ) {
						alt46=1;
					}
					switch (alt46) {
						case 1 :
							// Meta.g:542:10: T_AND T_ANALYTICS T_EQUAL ( T_TRUE | T_FALSE )
							{
							match(input,T_AND,FOLLOW_T_AND_in_setOptionsStatement4624); 
							match(input,T_ANALYTICS,FOLLOW_T_ANALYTICS_in_setOptionsStatement4626); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_setOptionsStatement4628); 
							// Meta.g:542:36: ( T_TRUE | T_FALSE )
							int alt45=2;
							int LA45_0 = input.LA(1);
							if ( (LA45_0==T_TRUE) ) {
								alt45=1;
							}
							else if ( (LA45_0==T_FALSE) ) {
								alt45=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 45, 0, input);
								throw nvae;
							}

							switch (alt45) {
								case 1 :
									// Meta.g:542:37: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_setOptionsStatement4631); 
									analytics=true;
									}
									break;
								case 2 :
									// Meta.g:542:61: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_setOptionsStatement4634); 
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
	// Meta.g:547:1: useStatement returns [UseStatement usst] : T_USE iden= T_IDENT ;
	public final UseStatement useStatement() throws RecognitionException {
		UseStatement usst = null;


		Token iden=null;

		try {
			// Meta.g:547:41: ( T_USE iden= T_IDENT )
			// Meta.g:548:5: T_USE iden= T_IDENT
			{
			match(input,T_USE,FOLLOW_T_USE_in_useStatement4684); 
			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_useStatement4692); 
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
	// Meta.g:551:1: dropKeyspaceStatement returns [DropKeyspaceStatement drksst] : T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT ;
	public final DropKeyspaceStatement dropKeyspaceStatement() throws RecognitionException {
		DropKeyspaceStatement drksst = null;


		Token iden=null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:554:6: ( T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT )
			// Meta.g:555:5: T_DROP T_KEYSPACE ( T_IF T_EXISTS )? iden= T_IDENT
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropKeyspaceStatement4717); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement4723); 
			// Meta.g:557:5: ( T_IF T_EXISTS )?
			int alt48=2;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==T_IF) ) {
				alt48=1;
			}
			switch (alt48) {
				case 1 :
					// Meta.g:557:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropKeyspaceStatement4730); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropKeyspaceStatement4732); 
					ifExists = true;
					}
					break;

			}

			iden=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_dropKeyspaceStatement4744); 
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
	// Meta.g:561:1: alterKeyspaceStatement returns [AlterKeyspaceStatement alksst] : T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
	public final AlterKeyspaceStatement alterKeyspaceStatement() throws RecognitionException {
		AlterKeyspaceStatement alksst = null;


		Token ident=null;
		Token identProp1=null;
		Token identPropN=null;
		ValueProperty valueProp1 =null;
		ValueProperty valuePropN =null;


		        HashMap<String, ValueProperty> properties = new HashMap<>();
		    
		try {
			// Meta.g:564:6: ( T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:565:5: T_ALTER T_KEYSPACE ident= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_ALTER,FOLLOW_T_ALTER_in_alterKeyspaceStatement4773); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement4779); 
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4787); 
			match(input,T_WITH,FOLLOW_T_WITH_in_alterKeyspaceStatement4793); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4801); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement4803); 
			pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement4807);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:570:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop49:
			while (true) {
				int alt49=2;
				int LA49_0 = input.LA(1);
				if ( (LA49_0==T_AND) ) {
					alt49=1;
				}

				switch (alt49) {
				case 1 :
					// Meta.g:570:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_alterKeyspaceStatement4816); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_alterKeyspaceStatement4820); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_alterKeyspaceStatement4822); 
					pushFollow(FOLLOW_getValueProperty_in_alterKeyspaceStatement4826);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop49;
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
	// Meta.g:573:1: createKeyspaceStatement returns [CreateKeyspaceStatement crksst] : T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* ;
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
			// Meta.g:577:6: ( T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )* )
			// Meta.g:578:5: T_CREATE T_KEYSPACE ( T_IF T_NOT T_EXISTS )? identKS= T_IDENT T_WITH identProp1= T_IDENT T_EQUAL valueProp1= getValueProperty ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			{
			match(input,T_CREATE,FOLLOW_T_CREATE_in_createKeyspaceStatement4860); 
			match(input,T_KEYSPACE,FOLLOW_T_KEYSPACE_in_createKeyspaceStatement4862); 
			// Meta.g:579:5: ( T_IF T_NOT T_EXISTS )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==T_IF) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// Meta.g:579:6: T_IF T_NOT T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_createKeyspaceStatement4869); 
					match(input,T_NOT,FOLLOW_T_NOT_in_createKeyspaceStatement4871); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_createKeyspaceStatement4873); 
					ifNotExists = true;
					}
					break;

			}

			identKS=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement4885); 
			match(input,T_WITH,FOLLOW_T_WITH_in_createKeyspaceStatement4891); 
			identProp1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement4903); 
			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement4905); 
			pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement4909);
			valueProp1=getValueProperty();
			state._fsp--;

			properties.put((identProp1!=null?identProp1.getText():null), valueProp1);
			// Meta.g:583:5: ( T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty )*
			loop51:
			while (true) {
				int alt51=2;
				int LA51_0 = input.LA(1);
				if ( (LA51_0==T_AND) ) {
					alt51=1;
				}

				switch (alt51) {
				case 1 :
					// Meta.g:583:6: T_AND identPropN= T_IDENT T_EQUAL valuePropN= getValueProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_createKeyspaceStatement4918); 
					identPropN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_createKeyspaceStatement4922); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_createKeyspaceStatement4924); 
					pushFollow(FOLLOW_getValueProperty_in_createKeyspaceStatement4928);
					valuePropN=getValueProperty();
					state._fsp--;

					properties.put((identPropN!=null?identPropN.getText():null), valuePropN);
					}
					break;

				default :
					break loop51;
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
	// Meta.g:586:1: dropTableStatement returns [DropTableStatement drtbst] : T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID ;
	public final DropTableStatement dropTableStatement() throws RecognitionException {
		DropTableStatement drtbst = null;


		String identID =null;


		        boolean ifExists = false;
		    
		try {
			// Meta.g:589:6: ( T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID )
			// Meta.g:590:5: T_DROP T_TABLE ( T_IF T_EXISTS )? identID= getTableID
			{
			match(input,T_DROP,FOLLOW_T_DROP_in_dropTableStatement4961); 
			match(input,T_TABLE,FOLLOW_T_TABLE_in_dropTableStatement4967); 
			// Meta.g:592:5: ( T_IF T_EXISTS )?
			int alt52=2;
			int LA52_0 = input.LA(1);
			if ( (LA52_0==T_IF) ) {
				alt52=1;
			}
			switch (alt52) {
				case 1 :
					// Meta.g:592:6: T_IF T_EXISTS
					{
					match(input,T_IF,FOLLOW_T_IF_in_dropTableStatement4974); 
					match(input,T_EXISTS,FOLLOW_T_EXISTS_in_dropTableStatement4976); 
					 ifExists = true; 
					}
					break;

			}

			pushFollow(FOLLOW_getTableID_in_dropTableStatement4988);
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
	// Meta.g:597:1: truncateStatement returns [TruncateStatement trst] : T_TRUNCATE ident= getTableID ;
	public final TruncateStatement truncateStatement() throws RecognitionException {
		TruncateStatement trst = null;


		String ident =null;

		try {
			// Meta.g:597:51: ( T_TRUNCATE ident= getTableID )
			// Meta.g:598:2: T_TRUNCATE ident= getTableID
			{
			match(input,T_TRUNCATE,FOLLOW_T_TRUNCATE_in_truncateStatement5003); 
			pushFollow(FOLLOW_getTableID_in_truncateStatement5016);
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
	// Meta.g:603:1: metaStatement returns [MetaStatement st] : (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_pdtb= updateTableStatement |st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement |ds= deleteStatement );
	public final MetaStatement metaStatement() throws RecognitionException {
		MetaStatement st = null;


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
			// Meta.g:603:41: (st_crta= createTableStatement |st_alta= alterTableStatement |st_crtr= createTriggerStatement |st_drtr= dropTriggerStatement |st_stpr= stopProcessStatement |st_pdtb= updateTableStatement |st_slct= selectStatement |st_nsnt= insertIntoStatement |st_xppl= explainPlanStatement |st_stpt= setOptionsStatement |st_usks= useStatement |st_drks= dropKeyspaceStatement |st_crks= createKeyspaceStatement |st_alks= alterKeyspaceStatement |st_tbdr= dropTableStatement |st_trst= truncateStatement |cis= createIndexStatement |dis= dropIndexStatement |ls= listStatement |add= addStatement |rs= removeUDFStatement |ds= deleteStatement )
			int alt53=22;
			switch ( input.LA(1) ) {
			case T_CREATE:
				{
				switch ( input.LA(2) ) {
				case T_TABLE:
					{
					alt53=1;
					}
					break;
				case T_TRIGGER:
					{
					alt53=3;
					}
					break;
				case T_KEYSPACE:
					{
					alt53=13;
					}
					break;
				case T_INDEX_TYPE:
					{
					alt53=17;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_ALTER:
				{
				int LA53_2 = input.LA(2);
				if ( (LA53_2==T_TABLE) ) {
					alt53=2;
				}
				else if ( (LA53_2==T_KEYSPACE) ) {
					alt53=14;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 2, input);
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
					alt53=4;
					}
					break;
				case T_KEYSPACE:
					{
					alt53=12;
					}
					break;
				case T_TABLE:
					{
					alt53=15;
					}
					break;
				case T_INDEX:
					{
					alt53=18;
					}
					break;
				default:
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 53, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case T_STOP:
				{
				alt53=5;
				}
				break;
			case T_UPDATE:
				{
				alt53=6;
				}
				break;
			case T_SELECT:
				{
				alt53=7;
				}
				break;
			case T_INSERT:
				{
				alt53=8;
				}
				break;
			case T_EXPLAIN:
				{
				alt53=9;
				}
				break;
			case T_SET:
				{
				alt53=10;
				}
				break;
			case T_USE:
				{
				alt53=11;
				}
				break;
			case T_TRUNCATE:
				{
				alt53=16;
				}
				break;
			case T_LIST:
				{
				alt53=19;
				}
				break;
			case T_ADD:
				{
				alt53=20;
				}
				break;
			case T_REMOVE:
				{
				alt53=21;
				}
				break;
			case T_DELETE:
				{
				alt53=22;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 53, 0, input);
				throw nvae;
			}
			switch (alt53) {
				case 1 :
					// Meta.g:604:5: st_crta= createTableStatement
					{
					pushFollow(FOLLOW_createTableStatement_in_metaStatement5036);
					st_crta=createTableStatement();
					state._fsp--;

					 st = st_crta;
					}
					break;
				case 2 :
					// Meta.g:605:7: st_alta= alterTableStatement
					{
					pushFollow(FOLLOW_alterTableStatement_in_metaStatement5049);
					st_alta=alterTableStatement();
					state._fsp--;

					 st = st_alta;
					}
					break;
				case 3 :
					// Meta.g:606:7: st_crtr= createTriggerStatement
					{
					pushFollow(FOLLOW_createTriggerStatement_in_metaStatement5062);
					st_crtr=createTriggerStatement();
					state._fsp--;

					 st = st_crtr; 
					}
					break;
				case 4 :
					// Meta.g:607:7: st_drtr= dropTriggerStatement
					{
					pushFollow(FOLLOW_dropTriggerStatement_in_metaStatement5075);
					st_drtr=dropTriggerStatement();
					state._fsp--;

					 st = st_drtr; 
					}
					break;
				case 5 :
					// Meta.g:608:7: st_stpr= stopProcessStatement
					{
					pushFollow(FOLLOW_stopProcessStatement_in_metaStatement5089);
					st_stpr=stopProcessStatement();
					state._fsp--;

					 st = st_stpr; 
					}
					break;
				case 6 :
					// Meta.g:609:7: st_pdtb= updateTableStatement
					{
					pushFollow(FOLLOW_updateTableStatement_in_metaStatement5103);
					st_pdtb=updateTableStatement();
					state._fsp--;

					 st = st_pdtb; 
					}
					break;
				case 7 :
					// Meta.g:610:7: st_slct= selectStatement
					{
					pushFollow(FOLLOW_selectStatement_in_metaStatement5117);
					st_slct=selectStatement();
					state._fsp--;

					 st = st_slct;
					}
					break;
				case 8 :
					// Meta.g:611:7: st_nsnt= insertIntoStatement
					{
					pushFollow(FOLLOW_insertIntoStatement_in_metaStatement5131);
					st_nsnt=insertIntoStatement();
					state._fsp--;

					 st = st_nsnt;
					}
					break;
				case 9 :
					// Meta.g:612:7: st_xppl= explainPlanStatement
					{
					pushFollow(FOLLOW_explainPlanStatement_in_metaStatement5145);
					st_xppl=explainPlanStatement();
					state._fsp--;

					 st = st_xppl;
					}
					break;
				case 10 :
					// Meta.g:613:7: st_stpt= setOptionsStatement
					{
					pushFollow(FOLLOW_setOptionsStatement_in_metaStatement5159);
					st_stpt=setOptionsStatement();
					state._fsp--;

					 st = st_stpt; 
					}
					break;
				case 11 :
					// Meta.g:614:7: st_usks= useStatement
					{
					pushFollow(FOLLOW_useStatement_in_metaStatement5173);
					st_usks=useStatement();
					state._fsp--;

					 st = st_usks; 
					}
					break;
				case 12 :
					// Meta.g:615:7: st_drks= dropKeyspaceStatement
					{
					pushFollow(FOLLOW_dropKeyspaceStatement_in_metaStatement5187);
					st_drks=dropKeyspaceStatement();
					state._fsp--;

					 st = st_drks ;
					}
					break;
				case 13 :
					// Meta.g:616:7: st_crks= createKeyspaceStatement
					{
					pushFollow(FOLLOW_createKeyspaceStatement_in_metaStatement5201);
					st_crks=createKeyspaceStatement();
					state._fsp--;

					 st = st_crks; 
					}
					break;
				case 14 :
					// Meta.g:617:7: st_alks= alterKeyspaceStatement
					{
					pushFollow(FOLLOW_alterKeyspaceStatement_in_metaStatement5215);
					st_alks=alterKeyspaceStatement();
					state._fsp--;

					 st = st_alks; 
					}
					break;
				case 15 :
					// Meta.g:618:7: st_tbdr= dropTableStatement
					{
					pushFollow(FOLLOW_dropTableStatement_in_metaStatement5229);
					st_tbdr=dropTableStatement();
					state._fsp--;

					 st = st_tbdr; 
					}
					break;
				case 16 :
					// Meta.g:619:7: st_trst= truncateStatement
					{
					pushFollow(FOLLOW_truncateStatement_in_metaStatement5243);
					st_trst=truncateStatement();
					state._fsp--;

					 st = st_trst; 
					}
					break;
				case 17 :
					// Meta.g:620:7: cis= createIndexStatement
					{
					pushFollow(FOLLOW_createIndexStatement_in_metaStatement5257);
					cis=createIndexStatement();
					state._fsp--;

					 st = cis; 
					}
					break;
				case 18 :
					// Meta.g:621:7: dis= dropIndexStatement
					{
					pushFollow(FOLLOW_dropIndexStatement_in_metaStatement5272);
					dis=dropIndexStatement();
					state._fsp--;

					 st = dis; 
					}
					break;
				case 19 :
					// Meta.g:622:7: ls= listStatement
					{
					pushFollow(FOLLOW_listStatement_in_metaStatement5287);
					ls=listStatement();
					state._fsp--;

					 st = ls; 
					}
					break;
				case 20 :
					// Meta.g:623:7: add= addStatement
					{
					pushFollow(FOLLOW_addStatement_in_metaStatement5302);
					add=addStatement();
					state._fsp--;

					 st = add; 
					}
					break;
				case 21 :
					// Meta.g:624:7: rs= removeUDFStatement
					{
					pushFollow(FOLLOW_removeUDFStatement_in_metaStatement5317);
					rs=removeUDFStatement();
					state._fsp--;

					 st = rs; 
					}
					break;
				case 22 :
					// Meta.g:625:7: ds= deleteStatement
					{
					pushFollow(FOLLOW_deleteStatement_in_metaStatement5332);
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
	// Meta.g:628:1: query returns [MetaStatement st] : mtst= metaStatement ( T_SEMICOLON )+ EOF ;
	public final MetaStatement query() throws RecognitionException {
		MetaStatement st = null;


		MetaStatement mtst =null;

		try {
			// Meta.g:628:33: (mtst= metaStatement ( T_SEMICOLON )+ EOF )
			// Meta.g:629:2: mtst= metaStatement ( T_SEMICOLON )+ EOF
			{
			pushFollow(FOLLOW_metaStatement_in_query5355);
			mtst=metaStatement();
			state._fsp--;

			// Meta.g:629:21: ( T_SEMICOLON )+
			int cnt54=0;
			loop54:
			while (true) {
				int alt54=2;
				int LA54_0 = input.LA(1);
				if ( (LA54_0==T_SEMICOLON) ) {
					alt54=1;
				}

				switch (alt54) {
				case 1 :
					// Meta.g:629:22: T_SEMICOLON
					{
					match(input,T_SEMICOLON,FOLLOW_T_SEMICOLON_in_query5358); 
					}
					break;

				default :
					if ( cnt54 >= 1 ) break loop54;
					EarlyExitException eee = new EarlyExitException(54, input);
					throw eee;
				}
				cnt54++;
			}

			match(input,EOF,FOLLOW_EOF_in_query5362); 

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



	// $ANTLR start "getMetaProperties"
	// Meta.g:636:1: getMetaProperties returns [List<MetaProperty> props] : firstProp= getMetaProperty ( T_AND newProp= getMetaProperty )* ;
	public final List<MetaProperty> getMetaProperties() throws RecognitionException {
		List<MetaProperty> props = null;


		MetaProperty firstProp =null;
		MetaProperty newProp =null;


		        props = new ArrayList<>();
		    
		try {
			// Meta.g:639:6: (firstProp= getMetaProperty ( T_AND newProp= getMetaProperty )* )
			// Meta.g:640:5: firstProp= getMetaProperty ( T_AND newProp= getMetaProperty )*
			{
			pushFollow(FOLLOW_getMetaProperty_in_getMetaProperties5392);
			firstProp=getMetaProperty();
			state._fsp--;

			props.add(firstProp);
			// Meta.g:641:5: ( T_AND newProp= getMetaProperty )*
			loop55:
			while (true) {
				int alt55=2;
				int LA55_0 = input.LA(1);
				if ( (LA55_0==T_AND) ) {
					alt55=1;
				}

				switch (alt55) {
				case 1 :
					// Meta.g:641:6: T_AND newProp= getMetaProperty
					{
					match(input,T_AND,FOLLOW_T_AND_in_getMetaProperties5401); 
					pushFollow(FOLLOW_getMetaProperty_in_getMetaProperties5405);
					newProp=getMetaProperty();
					state._fsp--;

					props.add(newProp);
					}
					break;

				default :
					break loop55;
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
		return props;
	}
	// $ANTLR end "getMetaProperties"



	// $ANTLR start "getMetaProperty"
	// Meta.g:644:1: getMetaProperty returns [MetaProperty mp] : ( (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS ) | T_EPHEMERAL (| T_EQUAL ( T_FALSE | T_TRUE ) ) );
	public final MetaProperty getMetaProperty() throws RecognitionException {
		MetaProperty mp = null;


		Token identProp=null;
		ValueProperty valueProp =null;
		List<MetaOrdering> ordering =null;


		        BooleanProperty boolProp = new BooleanProperty(true);
		    
		try {
			// Meta.g:647:6: ( (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS ) | T_EPHEMERAL (| T_EQUAL ( T_FALSE | T_TRUE ) ) )
			int alt59=2;
			int LA59_0 = input.LA(1);
			if ( (LA59_0==T_CLUSTERING||LA59_0==T_COMPACT||LA59_0==T_IDENT) ) {
				alt59=1;
			}
			else if ( (LA59_0==T_EPHEMERAL) ) {
				alt59=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 59, 0, input);
				throw nvae;
			}

			switch (alt59) {
				case 1 :
					// Meta.g:648:5: (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS )
					{
					// Meta.g:648:5: (identProp= T_IDENT T_EQUAL valueProp= getValueProperty | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS )
					int alt56=3;
					switch ( input.LA(1) ) {
					case T_IDENT:
						{
						alt56=1;
						}
						break;
					case T_COMPACT:
						{
						alt56=2;
						}
						break;
					case T_CLUSTERING:
						{
						alt56=3;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 56, 0, input);
						throw nvae;
					}
					switch (alt56) {
						case 1 :
							// Meta.g:648:6: identProp= T_IDENT T_EQUAL valueProp= getValueProperty
							{
							identProp=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getMetaProperty5436); 
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getMetaProperty5438); 
							pushFollow(FOLLOW_getValueProperty_in_getMetaProperty5442);
							valueProp=getValueProperty();
							state._fsp--;

							mp = new PropertyNameValue((identProp!=null?identProp.getText():null), valueProp);
							}
							break;
						case 2 :
							// Meta.g:649:7: T_COMPACT T_STORAGE
							{
							match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getMetaProperty5453); 
							match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getMetaProperty5455); 
							mp = new PropertyCompactStorage();
							}
							break;
						case 3 :
							// Meta.g:650:7: T_CLUSTERING T_ORDER T_BY T_START_PARENTHESIS ordering= getOrdering T_END_PARENTHESIS
							{
							match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getMetaProperty5465); 
							match(input,T_ORDER,FOLLOW_T_ORDER_in_getMetaProperty5467); 
							match(input,T_BY,FOLLOW_T_BY_in_getMetaProperty5469); 
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getMetaProperty5471); 
							pushFollow(FOLLOW_getOrdering_in_getMetaProperty5475);
							ordering=getOrdering();
							state._fsp--;

							mp = new PropertyClusteringOrder(ordering);
							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getMetaProperty5479); 
							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:651:7: T_EPHEMERAL (| T_EQUAL ( T_FALSE | T_TRUE ) )
					{
					match(input,T_EPHEMERAL,FOLLOW_T_EPHEMERAL_in_getMetaProperty5488); 
					// Meta.g:651:19: (| T_EQUAL ( T_FALSE | T_TRUE ) )
					int alt58=2;
					int LA58_0 = input.LA(1);
					if ( (LA58_0==T_AND||LA58_0==T_SEMICOLON) ) {
						alt58=1;
					}
					else if ( (LA58_0==T_EQUAL) ) {
						alt58=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 58, 0, input);
						throw nvae;
					}

					switch (alt58) {
						case 1 :
							// Meta.g:651:21: 
							{
							}
							break;
						case 2 :
							// Meta.g:651:23: T_EQUAL ( T_FALSE | T_TRUE )
							{
							match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getMetaProperty5494); 
							// Meta.g:651:31: ( T_FALSE | T_TRUE )
							int alt57=2;
							int LA57_0 = input.LA(1);
							if ( (LA57_0==T_FALSE) ) {
								alt57=1;
							}
							else if ( (LA57_0==T_TRUE) ) {
								alt57=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 57, 0, input);
								throw nvae;
							}

							switch (alt57) {
								case 1 :
									// Meta.g:651:32: T_FALSE
									{
									match(input,T_FALSE,FOLLOW_T_FALSE_in_getMetaProperty5497); 
									new BooleanProperty(false);
									}
									break;
								case 2 :
									// Meta.g:651:72: T_TRUE
									{
									match(input,T_TRUE,FOLLOW_T_TRUE_in_getMetaProperty5503); 
									}
									break;

							}

							}
							break;

					}

					mp = new PropertyNameValue("ephemeral", boolProp);
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
		return mp;
	}
	// $ANTLR end "getMetaProperty"



	// $ANTLR start "getDataType"
	// Meta.g:654:1: getDataType returns [String dataType] : (ident1= T_IDENT ( '<' ident2= T_IDENT ( ',' ident3= T_IDENT )? '>' )? ) ;
	public final String getDataType() throws RecognitionException {
		String dataType = null;


		Token ident1=null;
		Token ident2=null;
		Token ident3=null;

		try {
			// Meta.g:654:38: ( (ident1= T_IDENT ( '<' ident2= T_IDENT ( ',' ident3= T_IDENT )? '>' )? ) )
			// Meta.g:655:5: (ident1= T_IDENT ( '<' ident2= T_IDENT ( ',' ident3= T_IDENT )? '>' )? )
			{
			// Meta.g:655:5: (ident1= T_IDENT ( '<' ident2= T_IDENT ( ',' ident3= T_IDENT )? '>' )? )
			// Meta.g:656:9: ident1= T_IDENT ( '<' ident2= T_IDENT ( ',' ident3= T_IDENT )? '>' )?
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getDataType5536); 
			// Meta.g:656:24: ( '<' ident2= T_IDENT ( ',' ident3= T_IDENT )? '>' )?
			int alt61=2;
			int LA61_0 = input.LA(1);
			if ( (LA61_0==T_LT) ) {
				alt61=1;
			}
			switch (alt61) {
				case 1 :
					// Meta.g:656:25: '<' ident2= T_IDENT ( ',' ident3= T_IDENT )? '>'
					{
					match(input,T_LT,FOLLOW_T_LT_in_getDataType5539); 
					ident2=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getDataType5543); 
					// Meta.g:656:44: ( ',' ident3= T_IDENT )?
					int alt60=2;
					int LA60_0 = input.LA(1);
					if ( (LA60_0==T_COMMA) ) {
						alt60=1;
					}
					switch (alt60) {
						case 1 :
							// Meta.g:656:45: ',' ident3= T_IDENT
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getDataType5546); 
							ident3=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getDataType5550); 
							}
							break;

					}

					match(input,T_GT,FOLLOW_T_GT_in_getDataType5554); 
					}
					break;

			}

			}

			dataType = (ident1!=null?ident1.getText():null).concat(ident2==null?"":"<"+(ident2!=null?ident2.getText():null)).concat(ident3==null?"":","+(ident3!=null?ident3.getText():null)).concat(ident2==null?"":">");
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return dataType;
	}
	// $ANTLR end "getDataType"



	// $ANTLR start "getOrdering"
	// Meta.g:661:1: getOrdering returns [List<MetaOrdering> order] : ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )* ;
	public final List<MetaOrdering> getOrdering() throws RecognitionException {
		List<MetaOrdering> order = null;


		Token ident1=null;
		Token identN=null;


		        order = new ArrayList<>();
		        MetaOrdering ordering;
		    
		try {
			// Meta.g:665:6: (ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )* )
			// Meta.g:666:5: ident1= T_IDENT ( T_ASC | T_DESC )? ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )*
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOrdering5594); 
			ordering = new MetaOrdering((ident1!=null?ident1.getText():null));
			// Meta.g:666:65: ( T_ASC | T_DESC )?
			int alt62=3;
			int LA62_0 = input.LA(1);
			if ( (LA62_0==T_ASC) ) {
				alt62=1;
			}
			else if ( (LA62_0==T_DESC) ) {
				alt62=2;
			}
			switch (alt62) {
				case 1 :
					// Meta.g:666:66: T_ASC
					{
					match(input,T_ASC,FOLLOW_T_ASC_in_getOrdering5599); 
					ordering.setOrderDir(OrderDirection.ASC);
					}
					break;
				case 2 :
					// Meta.g:666:118: T_DESC
					{
					match(input,T_DESC,FOLLOW_T_DESC_in_getOrdering5605); 
					ordering.setOrderDir(OrderDirection.DESC);
					}
					break;

			}

			order.add(ordering);
			// Meta.g:667:5: ( T_COMMA identN= T_IDENT ( T_ASC | T_DESC )? )*
			loop64:
			while (true) {
				int alt64=2;
				int LA64_0 = input.LA(1);
				if ( (LA64_0==T_COMMA) ) {
					alt64=1;
				}

				switch (alt64) {
				case 1 :
					// Meta.g:667:6: T_COMMA identN= T_IDENT ( T_ASC | T_DESC )?
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getOrdering5618); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOrdering5622); 
					ordering = new MetaOrdering((identN!=null?identN.getText():null));
					// Meta.g:667:74: ( T_ASC | T_DESC )?
					int alt63=3;
					int LA63_0 = input.LA(1);
					if ( (LA63_0==T_ASC) ) {
						alt63=1;
					}
					else if ( (LA63_0==T_DESC) ) {
						alt63=2;
					}
					switch (alt63) {
						case 1 :
							// Meta.g:667:75: T_ASC
							{
							match(input,T_ASC,FOLLOW_T_ASC_in_getOrdering5627); 
							ordering.setOrderDir(OrderDirection.ASC);
							}
							break;
						case 2 :
							// Meta.g:667:127: T_DESC
							{
							match(input,T_DESC,FOLLOW_T_DESC_in_getOrdering5633); 
							ordering.setOrderDir(OrderDirection.DESC);
							}
							break;

					}

					order.add(ordering);
					}
					break;

				default :
					break loop64;
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
	// Meta.g:670:1: getWhereClauses returns [List<MetaRelation> clauses] : rel1= getRelation ( T_AND relN= getRelation )* ;
	public final List<MetaRelation> getWhereClauses() throws RecognitionException {
		List<MetaRelation> clauses = null;


		MetaRelation rel1 =null;
		MetaRelation relN =null;


		        clauses = new ArrayList<>();
		    
		try {
			// Meta.g:673:6: (rel1= getRelation ( T_AND relN= getRelation )* )
			// Meta.g:674:5: rel1= getRelation ( T_AND relN= getRelation )*
			{
			pushFollow(FOLLOW_getRelation_in_getWhereClauses5667);
			rel1=getRelation();
			state._fsp--;

			clauses.add(rel1);
			// Meta.g:674:43: ( T_AND relN= getRelation )*
			loop65:
			while (true) {
				int alt65=2;
				int LA65_0 = input.LA(1);
				if ( (LA65_0==T_AND) ) {
					alt65=1;
				}

				switch (alt65) {
				case 1 :
					// Meta.g:674:44: T_AND relN= getRelation
					{
					match(input,T_AND,FOLLOW_T_AND_in_getWhereClauses5672); 
					pushFollow(FOLLOW_getRelation_in_getWhereClauses5676);
					relN=getRelation();
					state._fsp--;

					clauses.add(relN);
					}
					break;

				default :
					break loop65;
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
	// Meta.g:677:1: getFields returns [Map<String, String> fields] : ident1L= getTableID T_EQUAL ident1R= getTableID (identNL= getTableID T_EQUAL identNR= getTableID )* ;
	public final Map<String, String> getFields() throws RecognitionException {
		Map<String, String> fields = null;


		String ident1L =null;
		String ident1R =null;
		String identNL =null;
		String identNR =null;


		        fields = new HashMap<>();
		    
		try {
			// Meta.g:680:6: (ident1L= getTableID T_EQUAL ident1R= getTableID (identNL= getTableID T_EQUAL identNR= getTableID )* )
			// Meta.g:681:5: ident1L= getTableID T_EQUAL ident1R= getTableID (identNL= getTableID T_EQUAL identNR= getTableID )*
			{
			pushFollow(FOLLOW_getTableID_in_getFields5706);
			ident1L=getTableID();
			state._fsp--;

			match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getFields5708); 
			pushFollow(FOLLOW_getTableID_in_getFields5712);
			ident1R=getTableID();
			state._fsp--;

			 fields.put(ident1L, ident1R);
			// Meta.g:682:5: (identNL= getTableID T_EQUAL identNR= getTableID )*
			loop66:
			while (true) {
				int alt66=2;
				int LA66_0 = input.LA(1);
				if ( (LA66_0==T_IDENT||LA66_0==T_KS_AND_TN) ) {
					alt66=1;
				}

				switch (alt66) {
				case 1 :
					// Meta.g:682:6: identNL= getTableID T_EQUAL identNR= getTableID
					{
					pushFollow(FOLLOW_getTableID_in_getFields5723);
					identNL=getTableID();
					state._fsp--;

					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getFields5725); 
					pushFollow(FOLLOW_getTableID_in_getFields5729);
					identNR=getTableID();
					state._fsp--;

					 fields.put(identNL, identNR);
					}
					break;

				default :
					break loop66;
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
	// Meta.g:685:1: getWindow returns [WindowSelect ws] : ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) ) ;
	public final WindowSelect getWindow() throws RecognitionException {
		WindowSelect ws = null;


		Token cnstnt=null;
		TimeUnit unit =null;

		try {
			// Meta.g:685:36: ( ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) ) )
			// Meta.g:686:5: ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) )
			{
			// Meta.g:686:5: ( T_LAST |cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit ) )
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==T_LAST) ) {
				alt68=1;
			}
			else if ( (LA68_0==T_CONSTANT) ) {
				alt68=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 68, 0, input);
				throw nvae;
			}

			switch (alt68) {
				case 1 :
					// Meta.g:686:6: T_LAST
					{
					match(input,T_LAST,FOLLOW_T_LAST_in_getWindow5751); 
					ws = new WindowLast();
					}
					break;
				case 2 :
					// Meta.g:687:7: cnstnt= T_CONSTANT ( T_ROWS |unit= getTimeUnit )
					{
					cnstnt=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getWindow5764); 
					// Meta.g:687:25: ( T_ROWS |unit= getTimeUnit )
					int alt67=2;
					int LA67_0 = input.LA(1);
					if ( (LA67_0==T_ROWS) ) {
						alt67=1;
					}
					else if ( ((LA67_0 >= 144 && LA67_0 <= 159)) ) {
						alt67=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 67, 0, input);
						throw nvae;
					}

					switch (alt67) {
						case 1 :
							// Meta.g:687:26: T_ROWS
							{
							match(input,T_ROWS,FOLLOW_T_ROWS_in_getWindow5767); 
							ws = new WindowRows(Integer.parseInt((cnstnt!=null?cnstnt.getText():null)));
							}
							break;
						case 2 :
							// Meta.g:688:26: unit= getTimeUnit
							{
							pushFollow(FOLLOW_getTimeUnit_in_getWindow5799);
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
	// Meta.g:692:1: getTimeUnit returns [TimeUnit unit] : ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | 'seconds' | 'minutes' | 'hours' | 'days' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' ) ;
	public final TimeUnit getTimeUnit() throws RecognitionException {
		TimeUnit unit = null;


		try {
			// Meta.g:692:36: ( ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | 'seconds' | 'minutes' | 'hours' | 'days' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' ) )
			// Meta.g:693:5: ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | 'seconds' | 'minutes' | 'hours' | 'days' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )
			{
			// Meta.g:693:5: ( 'S' | 'M' | 'H' | 'D' | 's' | 'm' | 'h' | 'd' | 'seconds' | 'minutes' | 'hours' | 'days' | 'SECONDS' | 'MINUTES' | 'HOURS' | 'DAYS' )
			int alt69=16;
			switch ( input.LA(1) ) {
			case 150:
				{
				alt69=1;
				}
				break;
			case 148:
				{
				alt69=2;
				}
				break;
			case 146:
				{
				alt69=3;
				}
				break;
			case 144:
				{
				alt69=4;
				}
				break;
			case 158:
				{
				alt69=5;
				}
				break;
			case 156:
				{
				alt69=6;
				}
				break;
			case 154:
				{
				alt69=7;
				}
				break;
			case 152:
				{
				alt69=8;
				}
				break;
			case 159:
				{
				alt69=9;
				}
				break;
			case 157:
				{
				alt69=10;
				}
				break;
			case 155:
				{
				alt69=11;
				}
				break;
			case 153:
				{
				alt69=12;
				}
				break;
			case 151:
				{
				alt69=13;
				}
				break;
			case 149:
				{
				alt69=14;
				}
				break;
			case 147:
				{
				alt69=15;
				}
				break;
			case 145:
				{
				alt69=16;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 69, 0, input);
				throw nvae;
			}
			switch (alt69) {
				case 1 :
					// Meta.g:693:7: 'S'
					{
					match(input,150,FOLLOW_150_in_getTimeUnit5849); 
					unit =TimeUnit.SECONDS;
					}
					break;
				case 2 :
					// Meta.g:694:7: 'M'
					{
					match(input,148,FOLLOW_148_in_getTimeUnit5859); 
					unit =TimeUnit.MINUTES;
					}
					break;
				case 3 :
					// Meta.g:695:7: 'H'
					{
					match(input,146,FOLLOW_146_in_getTimeUnit5869); 
					unit =TimeUnit.HOURS;
					}
					break;
				case 4 :
					// Meta.g:696:7: 'D'
					{
					match(input,144,FOLLOW_144_in_getTimeUnit5879); 
					unit =TimeUnit.DAYS;
					}
					break;
				case 5 :
					// Meta.g:697:7: 's'
					{
					match(input,158,FOLLOW_158_in_getTimeUnit5889); 
					unit =TimeUnit.SECONDS;
					}
					break;
				case 6 :
					// Meta.g:698:7: 'm'
					{
					match(input,156,FOLLOW_156_in_getTimeUnit5899); 
					unit =TimeUnit.MINUTES;
					}
					break;
				case 7 :
					// Meta.g:699:7: 'h'
					{
					match(input,154,FOLLOW_154_in_getTimeUnit5909); 
					unit =TimeUnit.HOURS;
					}
					break;
				case 8 :
					// Meta.g:700:7: 'd'
					{
					match(input,152,FOLLOW_152_in_getTimeUnit5919); 
					unit =TimeUnit.DAYS;
					}
					break;
				case 9 :
					// Meta.g:701:7: 'seconds'
					{
					match(input,159,FOLLOW_159_in_getTimeUnit5929); 
					unit =TimeUnit.SECONDS;
					}
					break;
				case 10 :
					// Meta.g:702:7: 'minutes'
					{
					match(input,157,FOLLOW_157_in_getTimeUnit5939); 
					unit =TimeUnit.MINUTES;
					}
					break;
				case 11 :
					// Meta.g:703:7: 'hours'
					{
					match(input,155,FOLLOW_155_in_getTimeUnit5949); 
					unit =TimeUnit.HOURS;
					}
					break;
				case 12 :
					// Meta.g:704:7: 'days'
					{
					match(input,153,FOLLOW_153_in_getTimeUnit5959); 
					unit =TimeUnit.DAYS;
					}
					break;
				case 13 :
					// Meta.g:705:7: 'SECONDS'
					{
					match(input,151,FOLLOW_151_in_getTimeUnit5969); 
					unit =TimeUnit.SECONDS;
					}
					break;
				case 14 :
					// Meta.g:706:7: 'MINUTES'
					{
					match(input,149,FOLLOW_149_in_getTimeUnit5979); 
					unit =TimeUnit.MINUTES;
					}
					break;
				case 15 :
					// Meta.g:707:7: 'HOURS'
					{
					match(input,147,FOLLOW_147_in_getTimeUnit5989); 
					unit =TimeUnit.HOURS;
					}
					break;
				case 16 :
					// Meta.g:708:7: 'DAYS'
					{
					match(input,145,FOLLOW_145_in_getTimeUnit5999); 
					unit =TimeUnit.DAYS;
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
	// Meta.g:712:1: getSelectClause returns [SelectionClause sc] : (scc= getSelectionCount |scl= getSelectionList );
	public final SelectionClause getSelectClause() throws RecognitionException {
		SelectionClause sc = null;


		SelectionCount scc =null;
		SelectionList scl =null;

		try {
			// Meta.g:712:45: (scc= getSelectionCount |scl= getSelectionList )
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==T_COUNT) ) {
				int LA70_1 = input.LA(2);
				if ( (LA70_1==T_START_PARENTHESIS) ) {
					int LA70_3 = input.LA(3);
					if ( (LA70_3==T_ASTERISK||LA70_3==143) ) {
						alt70=1;
					}
					else if ( (LA70_3==T_AGGREGATION||LA70_3==T_AVG||LA70_3==T_COUNT||LA70_3==T_END_PARENTHESIS||LA70_3==T_IDENT||LA70_3==T_KS_AND_TN||(LA70_3 >= T_MAX && LA70_3 <= T_MIN)) ) {
						alt70=2;
					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 70, 3, input);
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
							new NoViableAltException("", 70, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA70_0==T_AGGREGATION||LA70_0==T_ASTERISK||LA70_0==T_AVG||LA70_0==T_DISTINCT||LA70_0==T_IDENT||LA70_0==T_KS_AND_TN||(LA70_0 >= T_MAX && LA70_0 <= T_MIN)) ) {
				alt70=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 70, 0, input);
				throw nvae;
			}

			switch (alt70) {
				case 1 :
					// Meta.g:713:5: scc= getSelectionCount
					{
					pushFollow(FOLLOW_getSelectionCount_in_getSelectClause6025);
					scc=getSelectionCount();
					state._fsp--;

					sc = scc;
					}
					break;
				case 2 :
					// Meta.g:714:7: scl= getSelectionList
					{
					pushFollow(FOLLOW_getSelectionList_in_getSelectClause6037);
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
	// Meta.g:717:1: getSelectionCount returns [SelectionCount scc] : T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )? ;
	public final SelectionCount getSelectionCount() throws RecognitionException {
		SelectionCount scc = null;


		Token ident=null;


		        boolean identInc = false;
		        char symbol = '*';
		    
		try {
			// Meta.g:721:6: ( T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )? )
			// Meta.g:722:5: T_COUNT T_START_PARENTHESIS ( T_ASTERISK | '1' ) T_END_PARENTHESIS ( T_AS ident= T_IDENT )?
			{
			match(input,T_COUNT,FOLLOW_T_COUNT_in_getSelectionCount6063); 
			match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelectionCount6065); 
			// Meta.g:722:33: ( T_ASTERISK | '1' )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==T_ASTERISK) ) {
				alt71=1;
			}
			else if ( (LA71_0==143) ) {
				alt71=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 71, 0, input);
				throw nvae;
			}

			switch (alt71) {
				case 1 :
					// Meta.g:722:35: T_ASTERISK
					{
					match(input,T_ASTERISK,FOLLOW_T_ASTERISK_in_getSelectionCount6069); 
					}
					break;
				case 2 :
					// Meta.g:722:48: '1'
					{
					match(input,143,FOLLOW_143_in_getSelectionCount6073); 
					symbol = '1';
					}
					break;

			}

			match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelectionCount6079); 
			// Meta.g:723:5: ( T_AS ident= T_IDENT )?
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==T_AS) ) {
				alt72=1;
			}
			switch (alt72) {
				case 1 :
					// Meta.g:723:6: T_AS ident= T_IDENT
					{
					match(input,T_AS,FOLLOW_T_AS_in_getSelectionCount6086); 
					identInc = true;
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelectionCount6092); 
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
	// Meta.g:732:1: getSelectionList returns [SelectionList scl] : ( T_DISTINCT )? selections= getSelection ;
	public final SelectionList getSelectionList() throws RecognitionException {
		SelectionList scl = null;


		Selection selections =null;


		        boolean distinct = false;
		    
		try {
			// Meta.g:735:6: ( ( T_DISTINCT )? selections= getSelection )
			// Meta.g:736:5: ( T_DISTINCT )? selections= getSelection
			{
			// Meta.g:736:5: ( T_DISTINCT )?
			int alt73=2;
			int LA73_0 = input.LA(1);
			if ( (LA73_0==T_DISTINCT) ) {
				alt73=1;
			}
			switch (alt73) {
				case 1 :
					// Meta.g:736:6: T_DISTINCT
					{
					match(input,T_DISTINCT,FOLLOW_T_DISTINCT_in_getSelectionList6127); 
					distinct = true;
					}
					break;

			}

			pushFollow(FOLLOW_getSelection_in_getSelectionList6135);
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
	// Meta.g:740:1: getSelection returns [Selection slct] : ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* ) ;
	public final Selection getSelection() throws RecognitionException {
		Selection slct = null;


		Token ident1=null;
		Token identN=null;
		SelectorMeta selector1 =null;
		SelectorMeta selectorN =null;


		        SelectionSelector slsl;
		        List<SelectionSelector> selections = new ArrayList<>();
		    
		try {
			// Meta.g:744:6: ( ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* ) )
			// Meta.g:745:5: ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* )
			{
			// Meta.g:745:5: ( T_ASTERISK |selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )* )
			int alt77=2;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==T_ASTERISK) ) {
				alt77=1;
			}
			else if ( (LA77_0==T_AGGREGATION||LA77_0==T_AVG||LA77_0==T_COUNT||LA77_0==T_IDENT||LA77_0==T_KS_AND_TN||(LA77_0 >= T_MAX && LA77_0 <= T_MIN)) ) {
				alt77=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 77, 0, input);
				throw nvae;
			}

			switch (alt77) {
				case 1 :
					// Meta.g:746:9: T_ASTERISK
					{
					match(input,T_ASTERISK,FOLLOW_T_ASTERISK_in_getSelection6175); 
					 slct = new SelectionAsterisk();
					}
					break;
				case 2 :
					// Meta.g:747:11: selector1= getSelector ( T_AS ident1= T_IDENT )? ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )*
					{
					pushFollow(FOLLOW_getSelector_in_getSelection6198);
					selector1=getSelector();
					state._fsp--;

					 slsl = new SelectionSelector(selector1);
					// Meta.g:747:77: ( T_AS ident1= T_IDENT )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==T_AS) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// Meta.g:747:78: T_AS ident1= T_IDENT
							{
							match(input,T_AS,FOLLOW_T_AS_in_getSelection6203); 
							ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelection6207); 
							slsl.setAlias((ident1!=null?ident1.getText():null));
							}
							break;

					}

					selections.add(slsl);
					// Meta.g:748:13: ( T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )? )*
					loop76:
					while (true) {
						int alt76=2;
						int LA76_0 = input.LA(1);
						if ( (LA76_0==T_COMMA) ) {
							alt76=1;
						}

						switch (alt76) {
						case 1 :
							// Meta.g:748:14: T_COMMA selectorN= getSelector ( T_AS identN= T_IDENT )?
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelection6228); 
							pushFollow(FOLLOW_getSelector_in_getSelection6232);
							selectorN=getSelector();
							state._fsp--;

							slsl = new SelectionSelector(selectorN);
							// Meta.g:748:87: ( T_AS identN= T_IDENT )?
							int alt75=2;
							int LA75_0 = input.LA(1);
							if ( (LA75_0==T_AS) ) {
								alt75=1;
							}
							switch (alt75) {
								case 1 :
									// Meta.g:748:88: T_AS identN= T_IDENT
									{
									match(input,T_AS,FOLLOW_T_AS_in_getSelection6237); 
									identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getSelection6241); 
									slsl.setAlias((identN!=null?identN.getText():null));
									}
									break;

							}

							selections.add(slsl);
							}
							break;

						default :
							break loop76;
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
	// Meta.g:753:1: getSelector returns [SelectorMeta slmt] : ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) ) ;
	public final SelectorMeta getSelector() throws RecognitionException {
		SelectorMeta slmt = null;


		SelectorMeta select1 =null;
		SelectorMeta selectN =null;
		String identID =null;


		        List<SelectorMeta> params = new ArrayList<>();
		        GroupByFunction gbFunc = null;
		    
		try {
			// Meta.g:757:6: ( ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) ) )
			// Meta.g:758:5: ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) )
			{
			// Meta.g:758:5: ( ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS |identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS ) )
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==T_AGGREGATION||LA84_0==T_AVG||LA84_0==T_COUNT||(LA84_0 >= T_MAX && LA84_0 <= T_MIN)) ) {
				alt84=1;
			}
			else if ( (LA84_0==T_IDENT||LA84_0==T_KS_AND_TN) ) {
				alt84=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 84, 0, input);
				throw nvae;
			}

			switch (alt84) {
				case 1 :
					// Meta.g:758:7: ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT ) T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS
					{
					// Meta.g:758:7: ( T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_COUNT )
					int alt78=5;
					switch ( input.LA(1) ) {
					case T_AGGREGATION:
						{
						alt78=1;
						}
						break;
					case T_MAX:
						{
						alt78=2;
						}
						break;
					case T_MIN:
						{
						alt78=3;
						}
						break;
					case T_AVG:
						{
						alt78=4;
						}
						break;
					case T_COUNT:
						{
						alt78=5;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 78, 0, input);
						throw nvae;
					}
					switch (alt78) {
						case 1 :
							// Meta.g:758:8: T_AGGREGATION
							{
							match(input,T_AGGREGATION,FOLLOW_T_AGGREGATION_in_getSelector6296); 
							gbFunc = GroupByFunction.aggregation;
							}
							break;
						case 2 :
							// Meta.g:759:10: T_MAX
							{
							match(input,T_MAX,FOLLOW_T_MAX_in_getSelector6310); 
							gbFunc = GroupByFunction.max;
							}
							break;
						case 3 :
							// Meta.g:760:10: T_MIN
							{
							match(input,T_MIN,FOLLOW_T_MIN_in_getSelector6324); 
							gbFunc = GroupByFunction.min;
							}
							break;
						case 4 :
							// Meta.g:761:10: T_AVG
							{
							match(input,T_AVG,FOLLOW_T_AVG_in_getSelector6338); 
							gbFunc = GroupByFunction.avg;
							}
							break;
						case 5 :
							// Meta.g:762:10: T_COUNT
							{
							match(input,T_COUNT,FOLLOW_T_COUNT_in_getSelector6352); 
							gbFunc = GroupByFunction.count;
							}
							break;

					}

					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelector6378); 
					// Meta.g:765:17: (select1= getSelector ( T_COMMA selectN= getSelector )* )?
					int alt80=2;
					int LA80_0 = input.LA(1);
					if ( (LA80_0==T_AGGREGATION||LA80_0==T_AVG||LA80_0==T_COUNT||LA80_0==T_IDENT||LA80_0==T_KS_AND_TN||(LA80_0 >= T_MAX && LA80_0 <= T_MIN)) ) {
						alt80=1;
					}
					switch (alt80) {
						case 1 :
							// Meta.g:765:18: select1= getSelector ( T_COMMA selectN= getSelector )*
							{
							pushFollow(FOLLOW_getSelector_in_getSelector6400);
							select1=getSelector();
							state._fsp--;

							params.add(select1);
							// Meta.g:765:61: ( T_COMMA selectN= getSelector )*
							loop79:
							while (true) {
								int alt79=2;
								int LA79_0 = input.LA(1);
								if ( (LA79_0==T_COMMA) ) {
									alt79=1;
								}

								switch (alt79) {
								case 1 :
									// Meta.g:765:62: T_COMMA selectN= getSelector
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelector6405); 
									pushFollow(FOLLOW_getSelector_in_getSelector6409);
									selectN=getSelector();
									state._fsp--;

									params.add(selectN);
									}
									break;

								default :
									break loop79;
								}
							}

							}
							break;

					}

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelector6430); 
					slmt = new SelectorGroupBy(gbFunc, params);
					}
					break;
				case 2 :
					// Meta.g:767:11: identID= getTableID (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS )
					{
					pushFollow(FOLLOW_getTableID_in_getSelector6446);
					identID=getTableID();
					state._fsp--;

					// Meta.g:767:30: (| T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS )
					int alt83=2;
					int LA83_0 = input.LA(1);
					if ( (LA83_0==T_AS||LA83_0==T_COMMA||LA83_0==T_END_PARENTHESIS||LA83_0==T_FROM) ) {
						alt83=1;
					}
					else if ( (LA83_0==T_START_PARENTHESIS) ) {
						alt83=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 83, 0, input);
						throw nvae;
					}

					switch (alt83) {
						case 1 :
							// Meta.g:768:13: 
							{
							slmt = new SelectorIdentifier(identID);
							}
							break;
						case 2 :
							// Meta.g:769:15: T_START_PARENTHESIS (select1= getSelector ( T_COMMA selectN= getSelector )* )? T_END_PARENTHESIS
							{
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getSelector6478); 
							// Meta.g:769:35: (select1= getSelector ( T_COMMA selectN= getSelector )* )?
							int alt82=2;
							int LA82_0 = input.LA(1);
							if ( (LA82_0==T_AGGREGATION||LA82_0==T_AVG||LA82_0==T_COUNT||LA82_0==T_IDENT||LA82_0==T_KS_AND_TN||(LA82_0 >= T_MAX && LA82_0 <= T_MIN)) ) {
								alt82=1;
							}
							switch (alt82) {
								case 1 :
									// Meta.g:769:36: select1= getSelector ( T_COMMA selectN= getSelector )*
									{
									pushFollow(FOLLOW_getSelector_in_getSelector6483);
									select1=getSelector();
									state._fsp--;

									params.add(select1);
									// Meta.g:769:79: ( T_COMMA selectN= getSelector )*
									loop81:
									while (true) {
										int alt81=2;
										int LA81_0 = input.LA(1);
										if ( (LA81_0==T_COMMA) ) {
											alt81=1;
										}

										switch (alt81) {
										case 1 :
											// Meta.g:769:80: T_COMMA selectN= getSelector
											{
											match(input,T_COMMA,FOLLOW_T_COMMA_in_getSelector6488); 
											pushFollow(FOLLOW_getSelector_in_getSelector6492);
											selectN=getSelector();
											state._fsp--;

											params.add(selectN);
											}
											break;

										default :
											break loop81;
										}
									}

									}
									break;

							}

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getSelector6517); 
							slmt = new SelectorFunction(identID, params);
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
	// Meta.g:775:1: getListTypes returns [String listType] : ident= ( T_PROCESS | T_UDF | T_TRIGGER ) ;
	public final MetaParser.getListTypes_return getListTypes() throws RecognitionException {
		MetaParser.getListTypes_return retval = new MetaParser.getListTypes_return();
		retval.start = input.LT(1);

		Token ident=null;

		try {
			// Meta.g:775:39: (ident= ( T_PROCESS | T_UDF | T_TRIGGER ) )
			// Meta.g:779:2: ident= ( T_PROCESS | T_UDF | T_TRIGGER )
			{
			ident=input.LT(1);
			if ( input.LA(1)==T_PROCESS||input.LA(1)==T_TRIGGER||input.LA(1)==T_UDF ) {
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
	// Meta.g:782:1: getAssignment returns [Assignment assign] : ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm ) ;
	public final Assignment getAssignment() throws RecognitionException {
		Assignment assign = null;


		Token ident=null;
		ValueAssignment value =null;
		Term termL =null;
		Term termR =null;

		try {
			// Meta.g:782:42: (ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm ) )
			// Meta.g:783:5: ident= T_IDENT ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm )
			{
			ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getAssignment6587); 
			// Meta.g:783:19: ( T_EQUAL value= getValueAssign | T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm )
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==T_EQUAL) ) {
				alt85=1;
			}
			else if ( (LA85_0==T_START_BRACKET) ) {
				alt85=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 85, 0, input);
				throw nvae;
			}

			switch (alt85) {
				case 1 :
					// Meta.g:784:9: T_EQUAL value= getValueAssign
					{
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getAssignment6599); 
					pushFollow(FOLLOW_getValueAssign_in_getAssignment6603);
					value=getValueAssign();
					state._fsp--;

					assign = new Assignment(new IdentifierAssignment((ident!=null?ident.getText():null)), value);
					}
					break;
				case 2 :
					// Meta.g:786:9: T_START_BRACKET termL= getTerm T_END_BRACKET T_EQUAL termR= getTerm
					{
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_getAssignment6622); 
					pushFollow(FOLLOW_getTerm_in_getAssignment6626);
					termL=getTerm();
					state._fsp--;

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_getAssignment6628); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getAssignment6630); 
					pushFollow(FOLLOW_getTerm_in_getAssignment6634);
					termR=getTerm();
					state._fsp--;

					 
					            assign = new Assignment (new IdentifierAssignment((ident!=null?ident.getText():null), termL), new ValueAssignment(termR));
					        
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
	// Meta.g:792:1: getValueAssign returns [ValueAssignment valueAssign] : (term1= getTerm |ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList ) );
	public final ValueAssignment getValueAssign() throws RecognitionException {
		ValueAssignment valueAssign = null;


		Token ident=null;
		Term term1 =null;
		Map<String, String> mapLiteral =null;
		IdentIntOrLiteral value1 =null;
		IdentIntOrLiteral value2 =null;

		try {
			// Meta.g:792:53: (term1= getTerm |ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList ) )
			int alt88=2;
			int LA88_0 = input.LA(1);
			if ( (LA88_0==T_IDENT) ) {
				int LA88_1 = input.LA(2);
				if ( (LA88_1==T_AT||LA88_1==T_COMMA||LA88_1==T_WHERE) ) {
					alt88=1;
				}
				else if ( (LA88_1==T_PLUS||LA88_1==T_SUBTRACT) ) {
					alt88=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 88, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA88_0==QUOTED_LITERAL||LA88_0==T_CONSTANT||LA88_0==T_FALSE||LA88_0==T_KS_AND_TN||LA88_0==T_PATH||LA88_0==T_TERM||LA88_0==T_TRUE||LA88_0==143) ) {
				alt88=1;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 88, 0, input);
				throw nvae;
			}

			switch (alt88) {
				case 1 :
					// Meta.g:793:5: term1= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getValueAssign6661);
					term1=getTerm();
					state._fsp--;

					 valueAssign = new ValueAssignment(term1);
					}
					break;
				case 2 :
					// Meta.g:794:7: ident= T_IDENT ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList )
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueAssign6673); 
					// Meta.g:794:21: ( T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList ) | T_SUBTRACT value2= getIntSetOrList )
					int alt87=2;
					int LA87_0 = input.LA(1);
					if ( (LA87_0==T_PLUS) ) {
						alt87=1;
					}
					else if ( (LA87_0==T_SUBTRACT) ) {
						alt87=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 87, 0, input);
						throw nvae;
					}

					switch (alt87) {
						case 1 :
							// Meta.g:794:22: T_PLUS ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList )
							{
							match(input,T_PLUS,FOLLOW_T_PLUS_in_getValueAssign6676); 
							// Meta.g:794:29: ( T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET |value1= getIntSetOrList )
							int alt86=2;
							int LA86_0 = input.LA(1);
							if ( (LA86_0==T_START_SBRACKET) ) {
								int LA86_1 = input.LA(2);
								if ( (LA86_1==T_START_SBRACKET) ) {
									alt86=1;
								}
								else if ( (LA86_1==QUOTED_LITERAL||LA86_1==T_CONSTANT||LA86_1==T_FALSE||LA86_1==T_IDENT||LA86_1==T_KS_AND_TN||LA86_1==T_PATH||LA86_1==T_TERM||LA86_1==T_TRUE||LA86_1==143) ) {
									alt86=2;
								}

								else {
									int nvaeMark = input.mark();
									try {
										input.consume();
										NoViableAltException nvae =
											new NoViableAltException("", 86, 1, input);
										throw nvae;
									} finally {
										input.rewind(nvaeMark);
									}
								}

							}
							else if ( (LA86_0==T_CONSTANT||LA86_0==T_START_BRACKET) ) {
								alt86=2;
							}

							else {
								NoViableAltException nvae =
									new NoViableAltException("", 86, 0, input);
								throw nvae;
							}

							switch (alt86) {
								case 1 :
									// Meta.g:794:30: T_START_SBRACKET mapLiteral= getMapLiteral T_END_SBRACKET
									{
									match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getValueAssign6679); 
									pushFollow(FOLLOW_getMapLiteral_in_getValueAssign6683);
									mapLiteral=getMapLiteral();
									state._fsp--;

									match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getValueAssign6685); 
									 valueAssign = new ValueAssignment(new IdentMap((ident!=null?ident.getText():null), new MapLiteralProperty(mapLiteral)));
									}
									break;
								case 2 :
									// Meta.g:795:35: value1= getIntSetOrList
									{
									pushFollow(FOLLOW_getIntSetOrList_in_getValueAssign6725);
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
							// Meta.g:804:11: T_SUBTRACT value2= getIntSetOrList
							{
							match(input,T_SUBTRACT,FOLLOW_T_SUBTRACT_in_getValueAssign6769); 
							pushFollow(FOLLOW_getIntSetOrList_in_getValueAssign6773);
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
	// Meta.g:815:1: getIntSetOrList returns [IdentIntOrLiteral iiol] : (constant= T_CONSTANT | T_START_BRACKET list= getList T_END_BRACKET | T_START_SBRACKET set= getSet T_END_SBRACKET );
	public final IdentIntOrLiteral getIntSetOrList() throws RecognitionException {
		IdentIntOrLiteral iiol = null;


		Token constant=null;
		List list =null;
		Set set =null;

		try {
			// Meta.g:815:49: (constant= T_CONSTANT | T_START_BRACKET list= getList T_END_BRACKET | T_START_SBRACKET set= getSet T_END_SBRACKET )
			int alt89=3;
			switch ( input.LA(1) ) {
			case T_CONSTANT:
				{
				alt89=1;
				}
				break;
			case T_START_BRACKET:
				{
				alt89=2;
				}
				break;
			case T_START_SBRACKET:
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
					// Meta.g:816:5: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getIntSetOrList6799); 
					 iiol = new IntTerm(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 2 :
					// Meta.g:817:7: T_START_BRACKET list= getList T_END_BRACKET
					{
					match(input,T_START_BRACKET,FOLLOW_T_START_BRACKET_in_getIntSetOrList6809); 
					pushFollow(FOLLOW_getList_in_getIntSetOrList6813);
					list=getList();
					state._fsp--;

					match(input,T_END_BRACKET,FOLLOW_T_END_BRACKET_in_getIntSetOrList6815); 
					 iiol = new ListLiteral(list);
					}
					break;
				case 3 :
					// Meta.g:818:7: T_START_SBRACKET set= getSet T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getIntSetOrList6825); 
					pushFollow(FOLLOW_getSet_in_getIntSetOrList6829);
					set=getSet();
					state._fsp--;

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getIntSetOrList6831); 
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
	// Meta.g:821:1: getRelation returns [MetaRelation mrel] : ( T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS ) |ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm ) );
	public final MetaRelation getRelation() throws RecognitionException {
		MetaRelation mrel = null;


		Token ident=null;
		List<String> listIds =null;
		String operator =null;
		Term term =null;
		List terms =null;
		String compSymbol =null;
		Term termR =null;
		Term term1 =null;
		Term term2 =null;

		try {
			// Meta.g:821:40: ( T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS ) |ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm ) )
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==T_TOKEN) ) {
				alt92=1;
			}
			else if ( (LA92_0==T_IDENT) ) {
				alt92=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 92, 0, input);
				throw nvae;
			}

			switch (alt92) {
				case 1 :
					// Meta.g:822:5: T_TOKEN T_START_PARENTHESIS listIds= getIds T_END_PARENTHESIS operator= getComparator (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS )
					{
					match(input,T_TOKEN,FOLLOW_T_TOKEN_in_getRelation6849); 
					match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation6851); 
					pushFollow(FOLLOW_getIds_in_getRelation6855);
					listIds=getIds();
					state._fsp--;

					match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation6857); 
					pushFollow(FOLLOW_getComparator_in_getRelation6861);
					operator=getComparator();
					state._fsp--;

					// Meta.g:822:89: (term= getTerm | T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS )
					int alt90=2;
					int LA90_0 = input.LA(1);
					if ( (LA90_0==QUOTED_LITERAL||LA90_0==T_CONSTANT||LA90_0==T_FALSE||LA90_0==T_IDENT||LA90_0==T_KS_AND_TN||LA90_0==T_PATH||LA90_0==T_TERM||LA90_0==T_TRUE||LA90_0==143) ) {
						alt90=1;
					}
					else if ( (LA90_0==T_TOKEN) ) {
						alt90=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 90, 0, input);
						throw nvae;
					}

					switch (alt90) {
						case 1 :
							// Meta.g:822:90: term= getTerm
							{
							pushFollow(FOLLOW_getTerm_in_getRelation6866);
							term=getTerm();
							state._fsp--;

							mrel = new RelationToken(listIds, operator, term);
							}
							break;
						case 2 :
							// Meta.g:823:31: T_TOKEN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS
							{
							match(input,T_TOKEN,FOLLOW_T_TOKEN_in_getRelation6900); 
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation6902); 
							pushFollow(FOLLOW_getTerms_in_getRelation6906);
							terms=getTerms();
							state._fsp--;

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation6908); 
							mrel = new RelationToken(listIds, operator, terms);
							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:824:7: ident= T_IDENT (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm )
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getRelation6921); 
					// Meta.g:824:21: (compSymbol= getComparator termR= getTerm | T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS | T_BETWEEN term1= getTerm T_AND term2= getTerm )
					int alt91=3;
					switch ( input.LA(1) ) {
					case T_EQUAL:
					case T_GT:
					case T_GTE:
					case T_LIKE:
					case T_LT:
					case T_LTE:
					case T_NOT_EQUAL:
						{
						alt91=1;
						}
						break;
					case T_IN:
						{
						alt91=2;
						}
						break;
					case T_BETWEEN:
						{
						alt91=3;
						}
						break;
					default:
						NoViableAltException nvae =
							new NoViableAltException("", 91, 0, input);
						throw nvae;
					}
					switch (alt91) {
						case 1 :
							// Meta.g:824:23: compSymbol= getComparator termR= getTerm
							{
							pushFollow(FOLLOW_getComparator_in_getRelation6927);
							compSymbol=getComparator();
							state._fsp--;

							pushFollow(FOLLOW_getTerm_in_getRelation6931);
							termR=getTerm();
							state._fsp--;

							mrel = new RelationCompare((ident!=null?ident.getText():null), compSymbol, termR);
							}
							break;
						case 2 :
							// Meta.g:825:23: T_IN T_START_PARENTHESIS terms= getTerms T_END_PARENTHESIS
							{
							match(input,T_IN,FOLLOW_T_IN_in_getRelation6957); 
							match(input,T_START_PARENTHESIS,FOLLOW_T_START_PARENTHESIS_in_getRelation6959); 
							pushFollow(FOLLOW_getTerms_in_getRelation6963);
							terms=getTerms();
							state._fsp--;

							match(input,T_END_PARENTHESIS,FOLLOW_T_END_PARENTHESIS_in_getRelation6965); 
							mrel = new RelationIn((ident!=null?ident.getText():null), terms);
							}
							break;
						case 3 :
							// Meta.g:826:23: T_BETWEEN term1= getTerm T_AND term2= getTerm
							{
							match(input,T_BETWEEN,FOLLOW_T_BETWEEN_in_getRelation6991); 
							pushFollow(FOLLOW_getTerm_in_getRelation6995);
							term1=getTerm();
							state._fsp--;

							match(input,T_AND,FOLLOW_T_AND_in_getRelation6997); 
							pushFollow(FOLLOW_getTerm_in_getRelation7001);
							term2=getTerm();
							state._fsp--;

							mrel = new RelationBetween((ident!=null?ident.getText():null), term1, term2);
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
	// Meta.g:830:1: getComparator returns [String comparator] : ( T_EQUAL | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_LIKE );
	public final String getComparator() throws RecognitionException {
		String comparator = null;


		try {
			// Meta.g:830:42: ( T_EQUAL | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_LIKE )
			int alt93=7;
			switch ( input.LA(1) ) {
			case T_EQUAL:
				{
				alt93=1;
				}
				break;
			case T_GT:
				{
				alt93=2;
				}
				break;
			case T_LT:
				{
				alt93=3;
				}
				break;
			case T_GTE:
				{
				alt93=4;
				}
				break;
			case T_LTE:
				{
				alt93=5;
				}
				break;
			case T_NOT_EQUAL:
				{
				alt93=6;
				}
				break;
			case T_LIKE:
				{
				alt93=7;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 93, 0, input);
				throw nvae;
			}
			switch (alt93) {
				case 1 :
					// Meta.g:831:5: T_EQUAL
					{
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getComparator7041); 
					comparator ="=";
					}
					break;
				case 2 :
					// Meta.g:832:7: T_GT
					{
					match(input,T_GT,FOLLOW_T_GT_in_getComparator7051); 
					comparator =">";
					}
					break;
				case 3 :
					// Meta.g:833:7: T_LT
					{
					match(input,T_LT,FOLLOW_T_LT_in_getComparator7061); 
					comparator ="<";
					}
					break;
				case 4 :
					// Meta.g:834:7: T_GTE
					{
					match(input,T_GTE,FOLLOW_T_GTE_in_getComparator7071); 
					comparator =">=";
					}
					break;
				case 5 :
					// Meta.g:835:7: T_LTE
					{
					match(input,T_LTE,FOLLOW_T_LTE_in_getComparator7082); 
					comparator ="<=";
					}
					break;
				case 6 :
					// Meta.g:836:7: T_NOT_EQUAL
					{
					match(input,T_NOT_EQUAL,FOLLOW_T_NOT_EQUAL_in_getComparator7092); 
					comparator ="<>";
					}
					break;
				case 7 :
					// Meta.g:837:7: T_LIKE
					{
					match(input,T_LIKE,FOLLOW_T_LIKE_in_getComparator7103); 
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
	// Meta.g:840:1: getIds returns [List<String> listStrs] : ident1= T_IDENT ( T_COMMA identN= T_IDENT )* ;
	public final List<String> getIds() throws RecognitionException {
		List<String> listStrs = null;


		Token ident1=null;
		Token identN=null;


		        listStrs = new ArrayList<>();
		    
		try {
			// Meta.g:843:6: (ident1= T_IDENT ( T_COMMA identN= T_IDENT )* )
			// Meta.g:844:5: ident1= T_IDENT ( T_COMMA identN= T_IDENT )*
			{
			ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getIds7131); 
			listStrs.add((ident1!=null?ident1.getText():null));
			// Meta.g:844:50: ( T_COMMA identN= T_IDENT )*
			loop94:
			while (true) {
				int alt94=2;
				int LA94_0 = input.LA(1);
				if ( (LA94_0==T_COMMA) ) {
					alt94=1;
				}

				switch (alt94) {
				case 1 :
					// Meta.g:844:51: T_COMMA identN= T_IDENT
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getIds7136); 
					identN=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getIds7140); 
					listStrs.add((identN!=null?identN.getText():null));
					}
					break;

				default :
					break loop94;
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
	// Meta.g:847:1: getOptions returns [List<Option> opts] : opt1= getOption (optN= getOption )* ;
	public final List<Option> getOptions() throws RecognitionException {
		List<Option> opts = null;


		Option opt1 =null;
		Option optN =null;


		        opts = new ArrayList<>();
		    
		try {
			// Meta.g:849:6: (opt1= getOption (optN= getOption )* )
			// Meta.g:850:5: opt1= getOption (optN= getOption )*
			{
			pushFollow(FOLLOW_getOption_in_getOptions7165);
			opt1=getOption();
			state._fsp--;

			opts.add(opt1);
			// Meta.g:850:38: (optN= getOption )*
			loop95:
			while (true) {
				int alt95=2;
				int LA95_0 = input.LA(1);
				if ( (LA95_0==T_CLUSTERING||LA95_0==T_COMPACT||LA95_0==T_IDENT) ) {
					alt95=1;
				}

				switch (alt95) {
				case 1 :
					// Meta.g:850:39: optN= getOption
					{
					pushFollow(FOLLOW_getOption_in_getOptions7172);
					optN=getOption();
					state._fsp--;

					opts.add(optN);
					}
					break;

				default :
					break loop95;
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
	// Meta.g:853:1: getOption returns [Option opt] : ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty );
	public final Option getOption() throws RecognitionException {
		Option opt = null;


		Token identProp=null;
		ValueProperty valueProp =null;

		try {
			// Meta.g:853:31: ( T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |identProp= T_IDENT T_EQUAL valueProp= getValueProperty )
			int alt96=3;
			switch ( input.LA(1) ) {
			case T_COMPACT:
				{
				alt96=1;
				}
				break;
			case T_CLUSTERING:
				{
				alt96=2;
				}
				break;
			case T_IDENT:
				{
				alt96=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 96, 0, input);
				throw nvae;
			}
			switch (alt96) {
				case 1 :
					// Meta.g:854:5: T_COMPACT T_STORAGE
					{
					match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getOption7192); 
					match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getOption7194); 
					opt =new Option(Option.OPTION_COMPACT);
					}
					break;
				case 2 :
					// Meta.g:855:7: T_CLUSTERING T_ORDER
					{
					match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getOption7204); 
					match(input,T_ORDER,FOLLOW_T_ORDER_in_getOption7206); 
					opt =new Option(Option.OPTION_CLUSTERING);
					}
					break;
				case 3 :
					// Meta.g:856:7: identProp= T_IDENT T_EQUAL valueProp= getValueProperty
					{
					identProp=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getOption7218); 
					match(input,T_EQUAL,FOLLOW_T_EQUAL_in_getOption7220); 
					pushFollow(FOLLOW_getValueProperty_in_getOption7224);
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
	// Meta.g:859:1: getList returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getList() throws RecognitionException {
		List list = null;


		Term term1 =null;
		Term termN =null;


		        list = new ArrayList<String>();
		    
		try {
			// Meta.g:862:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:863:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getList7252);
			term1=getTerm();
			state._fsp--;

			list.add(term1.getTerm());
			// Meta.g:864:5: ( T_COMMA termN= getTerm )*
			loop97:
			while (true) {
				int alt97=2;
				int LA97_0 = input.LA(1);
				if ( (LA97_0==T_COMMA) ) {
					alt97=1;
				}

				switch (alt97) {
				case 1 :
					// Meta.g:864:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getList7261); 
					pushFollow(FOLLOW_getTerm_in_getList7265);
					termN=getTerm();
					state._fsp--;

					list.add(termN.getTerm());
					}
					break;

				default :
					break loop97;
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
	// Meta.g:867:1: getTerms returns [List list] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final List getTerms() throws RecognitionException {
		List list = null;


		Term term1 =null;
		Term termN =null;


		        list = new ArrayList<Term>();
		    
		try {
			// Meta.g:870:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:871:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getTerms7299);
			term1=getTerm();
			state._fsp--;

			list.add(term1);
			// Meta.g:872:5: ( T_COMMA termN= getTerm )*
			loop98:
			while (true) {
				int alt98=2;
				int LA98_0 = input.LA(1);
				if ( (LA98_0==T_COMMA) ) {
					alt98=1;
				}

				switch (alt98) {
				case 1 :
					// Meta.g:872:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getTerms7308); 
					pushFollow(FOLLOW_getTerm_in_getTerms7312);
					termN=getTerm();
					state._fsp--;

					list.add(termN);
					}
					break;

				default :
					break loop98;
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
	// Meta.g:875:1: getSet returns [Set set] : term1= getTerm ( T_COMMA termN= getTerm )* ;
	public final Set getSet() throws RecognitionException {
		Set set = null;


		Term term1 =null;
		Term termN =null;


		        set = new HashSet<String>();
		    
		try {
			// Meta.g:878:6: (term1= getTerm ( T_COMMA termN= getTerm )* )
			// Meta.g:879:5: term1= getTerm ( T_COMMA termN= getTerm )*
			{
			pushFollow(FOLLOW_getTerm_in_getSet7346);
			term1=getTerm();
			state._fsp--;

			set.add(term1.getTerm());
			// Meta.g:880:5: ( T_COMMA termN= getTerm )*
			loop99:
			while (true) {
				int alt99=2;
				int LA99_0 = input.LA(1);
				if ( (LA99_0==T_COMMA) ) {
					alt99=1;
				}

				switch (alt99) {
				case 1 :
					// Meta.g:880:6: T_COMMA termN= getTerm
					{
					match(input,T_COMMA,FOLLOW_T_COMMA_in_getSet7355); 
					pushFollow(FOLLOW_getTerm_in_getSet7359);
					termN=getTerm();
					state._fsp--;

					set.add(termN.getTerm());
					}
					break;

				default :
					break loop99;
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
	// Meta.g:883:1: getTermOrLiteral returns [ValueCell vc] : (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET );
	public final ValueCell getTermOrLiteral() throws RecognitionException {
		ValueCell vc = null;


		Term term =null;
		Term term1 =null;
		Term termN =null;


		        CollectionLiteral cl = new CollectionLiteral();
		    
		try {
			// Meta.g:886:6: (term= getTerm | T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET )
			int alt102=2;
			int LA102_0 = input.LA(1);
			if ( (LA102_0==QUOTED_LITERAL||LA102_0==T_CONSTANT||LA102_0==T_FALSE||LA102_0==T_IDENT||LA102_0==T_KS_AND_TN||LA102_0==T_PATH||LA102_0==T_TERM||LA102_0==T_TRUE||LA102_0==143) ) {
				alt102=1;
			}
			else if ( (LA102_0==T_START_SBRACKET) ) {
				alt102=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 102, 0, input);
				throw nvae;
			}

			switch (alt102) {
				case 1 :
					// Meta.g:887:5: term= getTerm
					{
					pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7393);
					term=getTerm();
					state._fsp--;

					vc =term;
					}
					break;
				case 2 :
					// Meta.g:889:5: T_START_SBRACKET (term1= getTerm ( T_COMMA termN= getTerm )* )? T_END_SBRACKET
					{
					match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getTermOrLiteral7407); 
					// Meta.g:890:5: (term1= getTerm ( T_COMMA termN= getTerm )* )?
					int alt101=2;
					int LA101_0 = input.LA(1);
					if ( (LA101_0==QUOTED_LITERAL||LA101_0==T_CONSTANT||LA101_0==T_FALSE||LA101_0==T_IDENT||LA101_0==T_KS_AND_TN||LA101_0==T_PATH||LA101_0==T_TERM||LA101_0==T_TRUE||LA101_0==143) ) {
						alt101=1;
					}
					switch (alt101) {
						case 1 :
							// Meta.g:891:9: term1= getTerm ( T_COMMA termN= getTerm )*
							{
							pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7425);
							term1=getTerm();
							state._fsp--;

							cl.addLiteral(term1);
							// Meta.g:892:9: ( T_COMMA termN= getTerm )*
							loop100:
							while (true) {
								int alt100=2;
								int LA100_0 = input.LA(1);
								if ( (LA100_0==T_COMMA) ) {
									alt100=1;
								}

								switch (alt100) {
								case 1 :
									// Meta.g:892:10: T_COMMA termN= getTerm
									{
									match(input,T_COMMA,FOLLOW_T_COMMA_in_getTermOrLiteral7438); 
									pushFollow(FOLLOW_getTerm_in_getTermOrLiteral7442);
									termN=getTerm();
									state._fsp--;

									cl.addLiteral(termN);
									}
									break;

								default :
									break loop100;
								}
							}

							}
							break;

					}

					match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getTermOrLiteral7459); 
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
	// Meta.g:897:1: getTableID returns [String tableID] : (ident1= T_IDENT |ident2= T_KS_AND_TN ) ;
	public final String getTableID() throws RecognitionException {
		String tableID = null;


		Token ident1=null;
		Token ident2=null;


		        tableID ="";
		    
		try {
			// Meta.g:900:6: ( (ident1= T_IDENT |ident2= T_KS_AND_TN ) )
			// Meta.g:901:5: (ident1= T_IDENT |ident2= T_KS_AND_TN )
			{
			// Meta.g:901:5: (ident1= T_IDENT |ident2= T_KS_AND_TN )
			int alt103=2;
			int LA103_0 = input.LA(1);
			if ( (LA103_0==T_IDENT) ) {
				alt103=1;
			}
			else if ( (LA103_0==T_KS_AND_TN) ) {
				alt103=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 103, 0, input);
				throw nvae;
			}

			switch (alt103) {
				case 1 :
					// Meta.g:901:6: ident1= T_IDENT
					{
					ident1=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getTableID7489); 
					tableID = new String((ident1!=null?ident1.getText():null));
					}
					break;
				case 2 :
					// Meta.g:902:7: ident2= T_KS_AND_TN
					{
					ident2=(Token)match(input,T_KS_AND_TN,FOLLOW_T_KS_AND_TN_in_getTableID7505); 
					tableID = new String((ident2!=null?ident2.getText():null));
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
		return tableID;
	}
	// $ANTLR end "getTableID"



	// $ANTLR start "getTerm"
	// Meta.g:905:1: getTerm returns [Term term] : term1= getPartialTerm (| T_AT term2= getPartialTerm ) ;
	public final Term getTerm() throws RecognitionException {
		Term term = null;


		Term term1 =null;
		Term term2 =null;

		try {
			// Meta.g:905:28: (term1= getPartialTerm (| T_AT term2= getPartialTerm ) )
			// Meta.g:906:5: term1= getPartialTerm (| T_AT term2= getPartialTerm )
			{
			pushFollow(FOLLOW_getPartialTerm_in_getTerm7530);
			term1=getPartialTerm();
			state._fsp--;

			// Meta.g:906:26: (| T_AT term2= getPartialTerm )
			int alt104=2;
			int LA104_0 = input.LA(1);
			if ( (LA104_0==T_AND||(LA104_0 >= T_COLON && LA104_0 <= T_COMMA)||LA104_0==T_DISABLE||(LA104_0 >= T_END_BRACKET && LA104_0 <= T_END_SBRACKET)||LA104_0==T_GROUP||LA104_0==T_IF||LA104_0==T_LIMIT||LA104_0==T_ORDER||LA104_0==T_QUOTE||LA104_0==T_SEMICOLON||LA104_0==T_SINGLE_QUOTE||LA104_0==T_USING||LA104_0==T_WHERE||LA104_0==T_WITH) ) {
				alt104=1;
			}
			else if ( (LA104_0==T_AT) ) {
				alt104=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 104, 0, input);
				throw nvae;
			}

			switch (alt104) {
				case 1 :
					// Meta.g:906:28: 
					{
					term = term1;
					}
					break;
				case 2 :
					// Meta.g:907:5: T_AT term2= getPartialTerm
					{
					match(input,T_AT,FOLLOW_T_AT_in_getTerm7543); 
					pushFollow(FOLLOW_getPartialTerm_in_getTerm7547);
					term2=getPartialTerm();
					state._fsp--;

					term = new Term(term1.getTerm()+"@"+term2.getTerm());
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
		return term;
	}
	// $ANTLR end "getTerm"



	// $ANTLR start "getPartialTerm"
	// Meta.g:910:1: getPartialTerm returns [Term term] : (ident= T_IDENT |constant= T_CONSTANT | '1' | T_FALSE | T_TRUE |ksAndTn= T_KS_AND_TN |noIdent= T_TERM |path= T_PATH |qLiteral= QUOTED_LITERAL );
	public final Term getPartialTerm() throws RecognitionException {
		Term term = null;


		Token ident=null;
		Token constant=null;
		Token ksAndTn=null;
		Token noIdent=null;
		Token path=null;
		Token qLiteral=null;

		try {
			// Meta.g:910:35: (ident= T_IDENT |constant= T_CONSTANT | '1' | T_FALSE | T_TRUE |ksAndTn= T_KS_AND_TN |noIdent= T_TERM |path= T_PATH |qLiteral= QUOTED_LITERAL )
			int alt105=9;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt105=1;
				}
				break;
			case T_CONSTANT:
				{
				alt105=2;
				}
				break;
			case 143:
				{
				alt105=3;
				}
				break;
			case T_FALSE:
				{
				alt105=4;
				}
				break;
			case T_TRUE:
				{
				alt105=5;
				}
				break;
			case T_KS_AND_TN:
				{
				alt105=6;
				}
				break;
			case T_TERM:
				{
				alt105=7;
				}
				break;
			case T_PATH:
				{
				alt105=8;
				}
				break;
			case QUOTED_LITERAL:
				{
				alt105=9;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 105, 0, input);
				throw nvae;
			}
			switch (alt105) {
				case 1 :
					// Meta.g:911:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getPartialTerm7569); 
					term = new Term((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:912:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getPartialTerm7581); 
					term = new Term((constant!=null?constant.getText():null));
					}
					break;
				case 3 :
					// Meta.g:913:7: '1'
					{
					match(input,143,FOLLOW_143_in_getPartialTerm7591); 
					term = new Term("1");
					}
					break;
				case 4 :
					// Meta.g:914:7: T_FALSE
					{
					match(input,T_FALSE,FOLLOW_T_FALSE_in_getPartialTerm7601); 
					term = new Term("false");
					}
					break;
				case 5 :
					// Meta.g:915:7: T_TRUE
					{
					match(input,T_TRUE,FOLLOW_T_TRUE_in_getPartialTerm7611); 
					term = new Term("true");
					}
					break;
				case 6 :
					// Meta.g:916:7: ksAndTn= T_KS_AND_TN
					{
					ksAndTn=(Token)match(input,T_KS_AND_TN,FOLLOW_T_KS_AND_TN_in_getPartialTerm7623); 
					term = new Term((ksAndTn!=null?ksAndTn.getText():null));
					}
					break;
				case 7 :
					// Meta.g:917:7: noIdent= T_TERM
					{
					noIdent=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getPartialTerm7635); 
					term = new Term((noIdent!=null?noIdent.getText():null));
					}
					break;
				case 8 :
					// Meta.g:918:7: path= T_PATH
					{
					path=(Token)match(input,T_PATH,FOLLOW_T_PATH_in_getPartialTerm7648); 
					term = new Term((path!=null?path.getText():null));
					}
					break;
				case 9 :
					// Meta.g:919:7: qLiteral= QUOTED_LITERAL
					{
					qLiteral=(Token)match(input,QUOTED_LITERAL,FOLLOW_QUOTED_LITERAL_in_getPartialTerm7660); 
					term = new Term((qLiteral!=null?qLiteral.getText():null), true);
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
	// $ANTLR end "getPartialTerm"



	// $ANTLR start "getMapLiteral"
	// Meta.g:922:1: getMapLiteral returns [Map<String, String> mapTerms] : T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET ;
	public final Map<String, String> getMapLiteral() throws RecognitionException {
		Map<String, String> mapTerms = null;


		Term leftTerm1 =null;
		Term rightTerm1 =null;
		Term leftTermN =null;
		Term rightTermN =null;


		        mapTerms = new HashMap<>();
		    
		try {
			// Meta.g:925:6: ( T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET )
			// Meta.g:926:5: T_START_SBRACKET (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )? T_END_SBRACKET
			{
			match(input,T_START_SBRACKET,FOLLOW_T_START_SBRACKET_in_getMapLiteral7686); 
			// Meta.g:927:5: (leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )* )?
			int alt107=2;
			int LA107_0 = input.LA(1);
			if ( (LA107_0==QUOTED_LITERAL||LA107_0==T_CONSTANT||LA107_0==T_FALSE||LA107_0==T_IDENT||LA107_0==T_KS_AND_TN||LA107_0==T_PATH||LA107_0==T_TERM||LA107_0==T_TRUE||LA107_0==143) ) {
				alt107=1;
			}
			switch (alt107) {
				case 1 :
					// Meta.g:927:6: leftTerm1= getTerm T_COLON rightTerm1= getTerm ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					{
					pushFollow(FOLLOW_getTerm_in_getMapLiteral7696);
					leftTerm1=getTerm();
					state._fsp--;

					match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral7698); 
					pushFollow(FOLLOW_getTerm_in_getMapLiteral7702);
					rightTerm1=getTerm();
					state._fsp--;

					mapTerms.put(leftTerm1.getTerm(), rightTerm1.getTerm());
					// Meta.g:928:5: ( T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm )*
					loop106:
					while (true) {
						int alt106=2;
						int LA106_0 = input.LA(1);
						if ( (LA106_0==T_COMMA) ) {
							alt106=1;
						}

						switch (alt106) {
						case 1 :
							// Meta.g:928:6: T_COMMA leftTermN= getTerm T_COLON rightTermN= getTerm
							{
							match(input,T_COMMA,FOLLOW_T_COMMA_in_getMapLiteral7711); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral7715);
							leftTermN=getTerm();
							state._fsp--;

							match(input,T_COLON,FOLLOW_T_COLON_in_getMapLiteral7717); 
							pushFollow(FOLLOW_getTerm_in_getMapLiteral7721);
							rightTermN=getTerm();
							state._fsp--;

							mapTerms.put(leftTermN.getTerm(), rightTermN.getTerm());
							}
							break;

						default :
							break loop106;
						}
					}

					}
					break;

			}

			match(input,T_END_SBRACKET,FOLLOW_T_END_SBRACKET_in_getMapLiteral7733); 
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
	// Meta.g:932:1: getValueProperty returns [ValueProperty value] : (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat | T_FALSE | T_TRUE | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |quotedLiteral= QUOTED_LITERAL );
	public final ValueProperty getValueProperty() throws RecognitionException {
		ValueProperty value = null;


		Token ident=null;
		Token constant=null;
		Token quotedLiteral=null;
		Map<String, String> mapliteral =null;
		String number =null;


		        StringBuilder sb = new StringBuilder();
		    
		try {
			// Meta.g:935:6: (ident= T_IDENT |constant= T_CONSTANT |mapliteral= getMapLiteral |number= getFloat | T_FALSE | T_TRUE | T_COMPACT T_STORAGE | T_CLUSTERING T_ORDER |quotedLiteral= QUOTED_LITERAL )
			int alt108=9;
			switch ( input.LA(1) ) {
			case T_IDENT:
				{
				alt108=1;
				}
				break;
			case T_CONSTANT:
				{
				alt108=2;
				}
				break;
			case T_START_SBRACKET:
				{
				alt108=3;
				}
				break;
			case T_FLOAT:
			case T_TERM:
				{
				alt108=4;
				}
				break;
			case T_FALSE:
				{
				alt108=5;
				}
				break;
			case T_TRUE:
				{
				alt108=6;
				}
				break;
			case T_COMPACT:
				{
				alt108=7;
				}
				break;
			case T_CLUSTERING:
				{
				alt108=8;
				}
				break;
			case QUOTED_LITERAL:
				{
				alt108=9;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 108, 0, input);
				throw nvae;
			}
			switch (alt108) {
				case 1 :
					// Meta.g:936:5: ident= T_IDENT
					{
					ident=(Token)match(input,T_IDENT,FOLLOW_T_IDENT_in_getValueProperty7763); 
					value = new IdentifierProperty((ident!=null?ident.getText():null));
					}
					break;
				case 2 :
					// Meta.g:937:7: constant= T_CONSTANT
					{
					constant=(Token)match(input,T_CONSTANT,FOLLOW_T_CONSTANT_in_getValueProperty7775); 
					value = new ConstantProperty(Integer.parseInt((constant!=null?constant.getText():null)));
					}
					break;
				case 3 :
					// Meta.g:938:7: mapliteral= getMapLiteral
					{
					pushFollow(FOLLOW_getMapLiteral_in_getValueProperty7787);
					mapliteral=getMapLiteral();
					state._fsp--;

					value = new MapLiteralProperty(mapliteral);
					}
					break;
				case 4 :
					// Meta.g:939:7: number= getFloat
					{
					pushFollow(FOLLOW_getFloat_in_getValueProperty7799);
					number=getFloat();
					state._fsp--;

					value = new FloatProperty(Float.parseFloat(number));
					}
					break;
				case 5 :
					// Meta.g:940:7: T_FALSE
					{
					match(input,T_FALSE,FOLLOW_T_FALSE_in_getValueProperty7809); 
					value = new BooleanProperty(false);
					}
					break;
				case 6 :
					// Meta.g:941:7: T_TRUE
					{
					match(input,T_TRUE,FOLLOW_T_TRUE_in_getValueProperty7819); 
					value = new BooleanProperty(true);
					}
					break;
				case 7 :
					// Meta.g:942:7: T_COMPACT T_STORAGE
					{
					match(input,T_COMPACT,FOLLOW_T_COMPACT_in_getValueProperty7829); 
					match(input,T_STORAGE,FOLLOW_T_STORAGE_in_getValueProperty7831); 
					value = new IdentifierProperty("COMPACT STORAGE");
					}
					break;
				case 8 :
					// Meta.g:943:7: T_CLUSTERING T_ORDER
					{
					match(input,T_CLUSTERING,FOLLOW_T_CLUSTERING_in_getValueProperty7841); 
					match(input,T_ORDER,FOLLOW_T_ORDER_in_getValueProperty7843); 
					value = new IdentifierProperty("CLUSTERING ORDER");
					}
					break;
				case 9 :
					// Meta.g:944:7: quotedLiteral= QUOTED_LITERAL
					{
					quotedLiteral=(Token)match(input,QUOTED_LITERAL,FOLLOW_QUOTED_LITERAL_in_getValueProperty7855); 
					value = new QuotedLiteral((quotedLiteral!=null?quotedLiteral.getText():null));
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
	// Meta.g:948:1: getFloat returns [String floating] : (termToken= T_TERM |floatToken= T_FLOAT );
	public final String getFloat() throws RecognitionException {
		String floating = null;


		Token termToken=null;
		Token floatToken=null;

		try {
			// Meta.g:948:35: (termToken= T_TERM |floatToken= T_FLOAT )
			int alt109=2;
			int LA109_0 = input.LA(1);
			if ( (LA109_0==T_TERM) ) {
				alt109=1;
			}
			else if ( (LA109_0==T_FLOAT) ) {
				alt109=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 109, 0, input);
				throw nvae;
			}

			switch (alt109) {
				case 1 :
					// Meta.g:949:5: termToken= T_TERM
					{
					termToken=(Token)match(input,T_TERM,FOLLOW_T_TERM_in_getFloat7880); 
					floating =(termToken!=null?termToken.getText():null);
					}
					break;
				case 2 :
					// Meta.g:951:5: floatToken= T_FLOAT
					{
					floatToken=(Token)match(input,T_FLOAT,FOLLOW_T_FLOAT_in_getFloat7898); 
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



	public static final BitSet FOLLOW_T_DELETE_in_deleteStatement1991 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000088L});
	public static final BitSet FOLLOW_T_IDENT_in_deleteStatement1998 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_COMMA_in_deleteStatement2005 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_deleteStatement2009 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_FROM_in_deleteStatement2028 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_deleteStatement2033 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_WHERE_in_deleteStatement2038 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000080L});
	public static final BitSet FOLLOW_getRelation_in_deleteStatement2043 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_deleteStatement2048 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000080L});
	public static final BitSet FOLLOW_getRelation_in_deleteStatement2052 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_ADD_in_addStatement2071 = new BitSet(new long[]{0x0000000000000000L,0x0001040000000000L});
	public static final BitSet FOLLOW_set_in_addStatement2073 = new BitSet(new long[]{0x0000000000000000L,0x0000001000000000L});
	public static final BitSet FOLLOW_T_PATH_in_addStatement2083 = new BitSet(new long[]{0x0000000000000000L,0x0001040000000000L});
	public static final BitSet FOLLOW_set_in_addStatement2085 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIST_in_listStatement2108 = new BitSet(new long[]{0x0000000000000000L,0x0800010000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_getListTypes_in_listStatement2113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_REMOVE_in_removeUDFStatement2134 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_T_UDF_in_removeUDFStatement2136 = new BitSet(new long[]{0x0000000000000000L,0x0001040000000000L});
	public static final BitSet FOLLOW_set_in_removeUDFStatement2138 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_removeUDFStatement2148 = new BitSet(new long[]{0x0000000000000000L,0x0001040000000000L});
	public static final BitSet FOLLOW_set_in_removeUDFStatement2152 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropIndexStatement2178 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_INDEX_in_dropIndexStatement2180 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000180L});
	public static final BitSet FOLLOW_T_IF_in_dropIndexStatement2184 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropIndexStatement2186 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_dropIndexStatement2195 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createIndexStatement2220 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_T_INDEX_TYPE_in_createIndexStatement2224 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_T_INDEX_in_createIndexStatement2228 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000180L});
	public static final BitSet FOLLOW_T_IF_in_createIndexStatement2232 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_T_NOT_in_createIndexStatement2234 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createIndexStatement2236 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2246 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_T_ON_in_createIndexStatement2254 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_createIndexStatement2258 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createIndexStatement2263 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2268 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createIndexStatement2274 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2280 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createIndexStatement2289 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000088L});
	public static final BitSet FOLLOW_T_USING_in_createIndexStatement2293 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_createIndexStatement2297 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_WITH_in_createIndexStatement2305 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_createIndexStatement2307 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2311 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement2313 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement2317 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_createIndexStatement2324 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createIndexStatement2328 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createIndexStatement2330 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_createIndexStatement2334 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_UPDATE_in_updateTableStatement2375 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_updateTableStatement2379 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_USING_in_updateTableStatement2386 = new BitSet(new long[]{0x0000480000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getOption_in_updateTableStatement2390 = new BitSet(new long[]{0x0000480000000000L,0x0000800000000080L});
	public static final BitSet FOLLOW_getOption_in_updateTableStatement2397 = new BitSet(new long[]{0x0000480000000000L,0x0000800000000080L});
	public static final BitSet FOLLOW_T_SET_in_updateTableStatement2409 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getAssignment_in_updateTableStatement2413 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_COMMA_in_updateTableStatement2418 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getAssignment_in_updateTableStatement2422 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_WHERE_in_updateTableStatement2432 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000080L});
	public static final BitSet FOLLOW_getRelation_in_updateTableStatement2436 = new BitSet(new long[]{0x0000000400000002L,0x0000000000000100L});
	public static final BitSet FOLLOW_T_AND_in_updateTableStatement2441 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000080L});
	public static final BitSet FOLLOW_getRelation_in_updateTableStatement2445 = new BitSet(new long[]{0x0000000400000002L,0x0000000000000100L});
	public static final BitSet FOLLOW_T_IF_in_updateTableStatement2456 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_updateTableStatement2460 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_updateTableStatement2462 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_updateTableStatement2466 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_updateTableStatement2492 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_updateTableStatement2496 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_updateTableStatement2498 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_updateTableStatement2502 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_STOP_in_stopProcessStatement2534 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_T_PROCESS_in_stopProcessStatement2536 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_stopProcessStatement2540 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTriggerStatement2562 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_T_TRIGGER_in_dropTriggerStatement2569 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement2573 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_T_ON_in_dropTriggerStatement2580 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_dropTriggerStatement2589 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTriggerStatement2617 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_T_TRIGGER_in_createTriggerStatement2624 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2628 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_T_ON_in_createTriggerStatement2635 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2644 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_USING_in_createTriggerStatement2650 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createTriggerStatement2654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createTableStatement2693 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_createTableStatement2699 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080180L});
	public static final BitSet FOLLOW_T_IF_in_createTableStatement2706 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_T_NOT_in_createTableStatement2708 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createTableStatement2710 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_createTableStatement2723 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement2729 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_set_in_createTableStatement2763 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getDataType_in_createTableStatement2773 = new BitSet(new long[]{0x0400200000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2776 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2778 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement2827 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_set_in_createTableStatement2831 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getDataType_in_createTableStatement2841 = new BitSet(new long[]{0x0400200000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2844 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2846 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement2904 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
	public static final BitSet FOLLOW_T_PRIMARY_in_createTableStatement2906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
	public static final BitSet FOLLOW_T_KEY_in_createTableStatement2908 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement2910 = new BitSet(new long[]{0x0000000000000000L,0x0004000000020080L});
	public static final BitSet FOLLOW_set_in_createTableStatement2972 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3043 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_set_in_createTableStatement3046 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_createTableStatement3151 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_set_in_createTableStatement3155 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3202 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_set_in_createTableStatement3205 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3249 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_createTableStatement3285 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020080L});
	public static final BitSet FOLLOW_set_in_createTableStatement3289 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3381 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_createTableStatement3453 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_WITH_in_createTableStatement3456 = new BitSet(new long[]{0x1000480000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getMetaProperties_in_createTableStatement3462 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement3525 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_alterTableStatement3531 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_alterTableStatement3539 = new BitSet(new long[]{0x0080000120000000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_ALTER_in_alterTableStatement3546 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3550 = new BitSet(new long[]{0x0000000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_T_TYPE_in_alterTableStatement3552 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3556 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ADD_in_alterTableStatement3569 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3573 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3577 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_alterTableStatement3590 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3594 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_WITH_in_alterTableStatement3607 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3624 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement3626 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement3630 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_alterTableStatement3647 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterTableStatement3651 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterTableStatement3653 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_alterTableStatement3657 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_SELECT_in_selectStatement3712 = new BitSet(new long[]{0x0042014040000000L,0x0000000030080080L});
	public static final BitSet FOLLOW_getSelectClause_in_selectStatement3716 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_FROM_in_selectStatement3718 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_selectStatement3722 = new BitSet(new long[]{0x0020000000000002L,0x0000000800401010L,0x00000000000000A0L});
	public static final BitSet FOLLOW_T_WITH_in_selectStatement3730 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000040L});
	public static final BitSet FOLLOW_T_WINDOW_in_selectStatement3732 = new BitSet(new long[]{0x0001000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_getWindow_in_selectStatement3738 = new BitSet(new long[]{0x0020000000000002L,0x0000000800401010L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_INNER_in_selectStatement3751 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
	public static final BitSet FOLLOW_T_JOIN_in_selectStatement3753 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_selectStatement3759 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
	public static final BitSet FOLLOW_T_ON_in_selectStatement3761 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getFields_in_selectStatement3765 = new BitSet(new long[]{0x0020000000000002L,0x0000000800400010L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_WHERE_in_selectStatement3774 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000080L});
	public static final BitSet FOLLOW_getWhereClauses_in_selectStatement3780 = new BitSet(new long[]{0x0020000000000002L,0x0000000800400010L});
	public static final BitSet FOLLOW_T_ORDER_in_selectStatement3789 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_BY_in_selectStatement3791 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getOrdering_in_selectStatement3797 = new BitSet(new long[]{0x0020000000000002L,0x0000000000400010L});
	public static final BitSet FOLLOW_T_GROUP_in_selectStatement3806 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_BY_in_selectStatement3808 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getList_in_selectStatement3814 = new BitSet(new long[]{0x0020000000000002L,0x0000000000400000L});
	public static final BitSet FOLLOW_T_LIMIT_in_selectStatement3823 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_T_CONSTANT_in_selectStatement3829 = new BitSet(new long[]{0x0020000000000002L});
	public static final BitSet FOLLOW_T_DISABLE_in_selectStatement3838 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_selectStatement3840 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_INSERT_in_insertIntoStatement3873 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_T_INTO_in_insertIntoStatement3880 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_insertIntoStatement3889 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3895 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement3904 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement3914 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_insertIntoStatement3918 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement3929 = new BitSet(new long[]{0x0000000000000000L,0x0000200000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_selectStatement_in_insertIntoStatement3948 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_VALUES_in_insertIntoStatement3971 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_insertIntoStatement3981 = new BitSet(new long[]{0x0001000002000000L,0x1108001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement3998 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_insertIntoStatement4015 = new BitSet(new long[]{0x0001000002000000L,0x1108001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTermOrLiteral_in_insertIntoStatement4019 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_insertIntoStatement4033 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_IF_in_insertIntoStatement4046 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_T_NOT_in_insertIntoStatement4048 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_insertIntoStatement4050 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_T_USING_in_insertIntoStatement4071 = new BitSet(new long[]{0x0000480000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement4086 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_insertIntoStatement4099 = new BitSet(new long[]{0x0000480000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getOption_in_insertIntoStatement4103 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_EXPLAIN_in_explainPlanStatement4140 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_T_PLAN_in_explainPlanStatement4142 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_T_FOR_in_explainPlanStatement4144 = new BitSet(new long[]{0x808C000120000000L,0x2010A80000802000L,0x0000000000000006L});
	public static final BitSet FOLLOW_metaStatement_in_explainPlanStatement4148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SET_in_setOptionsStatement4182 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_T_OPTIONS_in_setOptionsStatement4184 = new BitSet(new long[]{0x0000800200000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement4196 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4198 = new BitSet(new long[]{0x0000000000000000L,0x1000000000000001L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement4201 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement4204 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement4219 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement4221 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4223 = new BitSet(new long[]{0x0100000880000000L,0x4200020203000000L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement4238 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement4257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement4275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement4293 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement4311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement4329 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4347 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4365 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4383 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSISTENCY_in_setOptionsStatement4433 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4435 = new BitSet(new long[]{0x0100000880000000L,0x4200020203000000L});
	public static final BitSet FOLLOW_T_ALL_in_setOptionsStatement4451 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_ANY_in_setOptionsStatement4470 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_QUORUM_in_setOptionsStatement4488 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_ONE_in_setOptionsStatement4506 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_TWO_in_setOptionsStatement4524 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_THREE_in_setOptionsStatement4542 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_EACH_QUORUM_in_setOptionsStatement4560 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_LOCAL_ONE_in_setOptionsStatement4578 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_LOCAL_QUORUM_in_setOptionsStatement4596 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_setOptionsStatement4624 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_T_ANALYTICS_in_setOptionsStatement4626 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_setOptionsStatement4628 = new BitSet(new long[]{0x0000000000000000L,0x1000000000000001L});
	public static final BitSet FOLLOW_T_TRUE_in_setOptionsStatement4631 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_setOptionsStatement4634 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_USE_in_useStatement4684 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_useStatement4692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropKeyspaceStatement4717 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_dropKeyspaceStatement4723 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000180L});
	public static final BitSet FOLLOW_T_IF_in_dropKeyspaceStatement4730 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropKeyspaceStatement4732 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_dropKeyspaceStatement4744 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ALTER_in_alterKeyspaceStatement4773 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_alterKeyspaceStatement4779 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4787 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_WITH_in_alterKeyspaceStatement4793 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4801 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement4803 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement4807 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_alterKeyspaceStatement4816 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_alterKeyspaceStatement4820 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_alterKeyspaceStatement4822 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_alterKeyspaceStatement4826 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_CREATE_in_createKeyspaceStatement4860 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
	public static final BitSet FOLLOW_T_KEYSPACE_in_createKeyspaceStatement4862 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000180L});
	public static final BitSet FOLLOW_T_IF_in_createKeyspaceStatement4869 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
	public static final BitSet FOLLOW_T_NOT_in_createKeyspaceStatement4871 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_createKeyspaceStatement4873 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement4885 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_WITH_in_createKeyspaceStatement4891 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement4903 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement4905 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement4909 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_createKeyspaceStatement4918 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_createKeyspaceStatement4922 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_createKeyspaceStatement4924 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_createKeyspaceStatement4928 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_DROP_in_dropTableStatement4961 = new BitSet(new long[]{0x0000000000000000L,0x0080000000000000L});
	public static final BitSet FOLLOW_T_TABLE_in_dropTableStatement4967 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080180L});
	public static final BitSet FOLLOW_T_IF_in_dropTableStatement4974 = new BitSet(new long[]{0x4000000000000000L});
	public static final BitSet FOLLOW_T_EXISTS_in_dropTableStatement4976 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_dropTableStatement4988 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUNCATE_in_truncateStatement5003 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_truncateStatement5016 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTableStatement_in_metaStatement5036 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterTableStatement_in_metaStatement5049 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createTriggerStatement_in_metaStatement5062 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTriggerStatement_in_metaStatement5075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stopProcessStatement_in_metaStatement5089 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_updateTableStatement_in_metaStatement5103 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_selectStatement_in_metaStatement5117 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_insertIntoStatement_in_metaStatement5131 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_explainPlanStatement_in_metaStatement5145 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_setOptionsStatement_in_metaStatement5159 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_useStatement_in_metaStatement5173 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropKeyspaceStatement_in_metaStatement5187 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createKeyspaceStatement_in_metaStatement5201 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_alterKeyspaceStatement_in_metaStatement5215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropTableStatement_in_metaStatement5229 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_truncateStatement_in_metaStatement5243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_createIndexStatement_in_metaStatement5257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_dropIndexStatement_in_metaStatement5272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_listStatement_in_metaStatement5287 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_addStatement_in_metaStatement5302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_removeUDFStatement_in_metaStatement5317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_deleteStatement_in_metaStatement5332 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_metaStatement_in_query5355 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_T_SEMICOLON_in_query5358 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_EOF_in_query5362 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMetaProperty_in_getMetaProperties5392 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_getMetaProperties5401 = new BitSet(new long[]{0x1000480000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getMetaProperty_in_getMetaProperties5405 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getMetaProperty5436 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getMetaProperty5438 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_getMetaProperty5442 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COMPACT_in_getMetaProperty5453 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_T_STORAGE_in_getMetaProperty5455 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getMetaProperty5465 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getMetaProperty5467 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_T_BY_in_getMetaProperty5469 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getMetaProperty5471 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getOrdering_in_getMetaProperty5475 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getMetaProperty5479 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EPHEMERAL_in_getMetaProperty5488 = new BitSet(new long[]{0x2000000000000002L});
	public static final BitSet FOLLOW_T_EQUAL_in_getMetaProperty5494 = new BitSet(new long[]{0x0000000000000000L,0x1000000000000001L});
	public static final BitSet FOLLOW_T_FALSE_in_getMetaProperty5497 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_getMetaProperty5503 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getDataType5536 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
	public static final BitSet FOLLOW_T_LT_in_getDataType5539 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_getDataType5543 = new BitSet(new long[]{0x0000200000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_COMMA_in_getDataType5546 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_getDataType5550 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_T_GT_in_getDataType5554 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOrdering5594 = new BitSet(new long[]{0x0010202000000002L});
	public static final BitSet FOLLOW_T_ASC_in_getOrdering5599 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_DESC_in_getOrdering5605 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getOrdering5618 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_getOrdering5622 = new BitSet(new long[]{0x0010202000000002L});
	public static final BitSet FOLLOW_T_ASC_in_getOrdering5627 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_DESC_in_getOrdering5633 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getRelation_in_getWhereClauses5667 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_T_AND_in_getWhereClauses5672 = new BitSet(new long[]{0x0000000000000000L,0x0400000000000080L});
	public static final BitSet FOLLOW_getRelation_in_getWhereClauses5676 = new BitSet(new long[]{0x0000000400000002L});
	public static final BitSet FOLLOW_getTableID_in_getFields5706 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getFields5708 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_getFields5712 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_getFields5723 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getFields5725 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080080L});
	public static final BitSet FOLLOW_getTableID_in_getFields5729 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080080L});
	public static final BitSet FOLLOW_T_LAST_in_getWindow5751 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getWindow5764 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L,0x00000000FFFF0000L});
	public static final BitSet FOLLOW_T_ROWS_in_getWindow5767 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTimeUnit_in_getWindow5799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_150_in_getTimeUnit5849 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_148_in_getTimeUnit5859 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_146_in_getTimeUnit5869 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_144_in_getTimeUnit5879 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_158_in_getTimeUnit5889 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_156_in_getTimeUnit5899 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_154_in_getTimeUnit5909 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_152_in_getTimeUnit5919 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_159_in_getTimeUnit5929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_157_in_getTimeUnit5939 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_155_in_getTimeUnit5949 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_153_in_getTimeUnit5959 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_151_in_getTimeUnit5969 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_149_in_getTimeUnit5979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_147_in_getTimeUnit5989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_145_in_getTimeUnit5999 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelectionCount_in_getSelectClause6025 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelectionList_in_getSelectClause6037 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COUNT_in_getSelectionCount6063 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelectionCount6065 = new BitSet(new long[]{0x0000004000000000L,0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_T_ASTERISK_in_getSelectionCount6069 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_143_in_getSelectionCount6073 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelectionCount6079 = new BitSet(new long[]{0x0000001000000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelectionCount6086 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelectionCount6092 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_DISTINCT_in_getSelectionList6127 = new BitSet(new long[]{0x0002014040000000L,0x0000000030080080L});
	public static final BitSet FOLLOW_getSelection_in_getSelectionList6135 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_ASTERISK_in_getSelection6175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getSelector_in_getSelection6198 = new BitSet(new long[]{0x0000201000000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelection6203 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelection6207 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelection6228 = new BitSet(new long[]{0x0002010040000000L,0x0000000030080080L});
	public static final BitSet FOLLOW_getSelector_in_getSelection6232 = new BitSet(new long[]{0x0000201000000002L});
	public static final BitSet FOLLOW_T_AS_in_getSelection6237 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_getSelection6241 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_AGGREGATION_in_getSelector6296 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_MAX_in_getSelector6310 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_MIN_in_getSelector6324 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_AVG_in_getSelector6338 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_COUNT_in_getSelector6352 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelector6378 = new BitSet(new long[]{0x0402010040000000L,0x0000000030080080L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6400 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelector6405 = new BitSet(new long[]{0x0002010040000000L,0x0000000030080080L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6409 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelector6430 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTableID_in_getSelector6446 = new BitSet(new long[]{0x0000000000000002L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getSelector6478 = new BitSet(new long[]{0x0402010040000000L,0x0000000030080080L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6483 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getSelector6488 = new BitSet(new long[]{0x0002010040000000L,0x0000000030080080L});
	public static final BitSet FOLLOW_getSelector_in_getSelector6492 = new BitSet(new long[]{0x0400200000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getSelector6517 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_getListTypes6556 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getAssignment6587 = new BitSet(new long[]{0x2000000000000000L,0x0002000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getAssignment6599 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getValueAssign_in_getAssignment6603 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_getAssignment6622 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getAssignment6626 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_getAssignment6628 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getAssignment6630 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getAssignment6634 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getValueAssign6661 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueAssign6673 = new BitSet(new long[]{0x0000000000000000L,0x0040004000000000L});
	public static final BitSet FOLLOW_T_PLUS_in_getValueAssign6676 = new BitSet(new long[]{0x0001000000000000L,0x000A000000000000L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getValueAssign6679 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueAssign6683 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getValueAssign6685 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getIntSetOrList_in_getValueAssign6725 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_SUBTRACT_in_getValueAssign6769 = new BitSet(new long[]{0x0001000000000000L,0x000A000000000000L});
	public static final BitSet FOLLOW_getIntSetOrList_in_getValueAssign6773 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getIntSetOrList6799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_BRACKET_in_getIntSetOrList6809 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getList_in_getIntSetOrList6813 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_T_END_BRACKET_in_getIntSetOrList6815 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getIntSetOrList6825 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getSet_in_getIntSetOrList6829 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getIntSetOrList6831 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TOKEN_in_getRelation6849 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation6851 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_getIds_in_getRelation6855 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation6857 = new BitSet(new long[]{0x2000000000000000L,0x000000008C200060L});
	public static final BitSet FOLLOW_getComparator_in_getRelation6861 = new BitSet(new long[]{0x0001000002000000L,0x1500001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6866 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TOKEN_in_getRelation6900 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation6902 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerms_in_getRelation6906 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation6908 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getRelation6921 = new BitSet(new long[]{0x2000020000000000L,0x000000008C200260L});
	public static final BitSet FOLLOW_getComparator_in_getRelation6927 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6931 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IN_in_getRelation6957 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_T_START_PARENTHESIS_in_getRelation6959 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerms_in_getRelation6963 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_T_END_PARENTHESIS_in_getRelation6965 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_BETWEEN_in_getRelation6991 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getRelation6995 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_T_AND_in_getRelation6997 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getRelation7001 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_EQUAL_in_getComparator7041 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_GT_in_getComparator7051 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LT_in_getComparator7061 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_GTE_in_getComparator7071 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LTE_in_getComparator7082 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_NOT_EQUAL_in_getComparator7092 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_LIKE_in_getComparator7103 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getIds7131 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getIds7136 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_IDENT_in_getIds7140 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getOption_in_getOptions7165 = new BitSet(new long[]{0x0000480000000002L,0x0000000000000080L});
	public static final BitSet FOLLOW_getOption_in_getOptions7172 = new BitSet(new long[]{0x0000480000000002L,0x0000000000000080L});
	public static final BitSet FOLLOW_T_COMPACT_in_getOption7192 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_T_STORAGE_in_getOption7194 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getOption7204 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getOption7206 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getOption7218 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_T_EQUAL_in_getOption7220 = new BitSet(new long[]{0x0001480002000000L,0x1108000000000083L});
	public static final BitSet FOLLOW_getValueProperty_in_getOption7224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getTerm_in_getList7252 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getList7261 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getList7265 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getTerm_in_getTerms7299 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getTerms7308 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getTerms7312 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getTerm_in_getSet7346 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_T_COMMA_in_getSet7355 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getSet7359 = new BitSet(new long[]{0x0000200000000002L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7393 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getTermOrLiteral7407 = new BitSet(new long[]{0x0801000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7425 = new BitSet(new long[]{0x0800200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getTermOrLiteral7438 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getTermOrLiteral7442 = new BitSet(new long[]{0x0800200000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getTermOrLiteral7459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getTableID7489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_KS_AND_TN_in_getTableID7505 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getPartialTerm_in_getTerm7530 = new BitSet(new long[]{0x0000008000000002L});
	public static final BitSet FOLLOW_T_AT_in_getTerm7543 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getPartialTerm_in_getTerm7547 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getPartialTerm7569 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getPartialTerm7581 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_143_in_getPartialTerm7591 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_getPartialTerm7601 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_getPartialTerm7611 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_KS_AND_TN_in_getPartialTerm7623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getPartialTerm7635 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_PATH_in_getPartialTerm7648 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUOTED_LITERAL_in_getPartialTerm7660 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_START_SBRACKET_in_getMapLiteral7686 = new BitSet(new long[]{0x0801000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7696 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral7698 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7702 = new BitSet(new long[]{0x0800200000000000L});
	public static final BitSet FOLLOW_T_COMMA_in_getMapLiteral7711 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7715 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_T_COLON_in_getMapLiteral7717 = new BitSet(new long[]{0x0001000002000000L,0x1100001000080081L,0x0000000000008000L});
	public static final BitSet FOLLOW_getTerm_in_getMapLiteral7721 = new BitSet(new long[]{0x0800200000000000L});
	public static final BitSet FOLLOW_T_END_SBRACKET_in_getMapLiteral7733 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_IDENT_in_getValueProperty7763 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CONSTANT_in_getValueProperty7775 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getMapLiteral_in_getValueProperty7787 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_getFloat_in_getValueProperty7799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FALSE_in_getValueProperty7809 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TRUE_in_getValueProperty7819 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_COMPACT_in_getValueProperty7829 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_T_STORAGE_in_getValueProperty7831 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_CLUSTERING_in_getValueProperty7841 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_T_ORDER_in_getValueProperty7843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_QUOTED_LITERAL_in_getValueProperty7855 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_TERM_in_getFloat7880 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_T_FLOAT_in_getFloat7898 = new BitSet(new long[]{0x0000000000000002L});
}
