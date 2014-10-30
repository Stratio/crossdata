// $ANTLR 3.5.2 Meta.g 2014-10-30 18:05:10

    package com.stratio.crossdata.core.grammar.generated;
    import com.stratio.crossdata.common.exceptions.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaLexer extends Lexer {
	public static final int EOF=-1;
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
	public static final int T_ATTACH=40;
	public static final int T_AVG=41;
	public static final int T_BETWEEN=42;
	public static final int T_BIGINT=43;
	public static final int T_BOOLEAN=44;
	public static final int T_BY=45;
	public static final int T_CATALOG=46;
	public static final int T_CATALOGS=47;
	public static final int T_CLUSTER=48;
	public static final int T_CLUSTERING=49;
	public static final int T_CLUSTERS=50;
	public static final int T_COLON=51;
	public static final int T_COMMA=52;
	public static final int T_COMPACT=53;
	public static final int T_CONNECTOR=54;
	public static final int T_CONNECTORS=55;
	public static final int T_CONSISTENCY=56;
	public static final int T_CONSTANT=57;
	public static final int T_COUNT=58;
	public static final int T_CREATE=59;
	public static final int T_CTLG_TBL_COL=60;
	public static final int T_CUSTOM=61;
	public static final int T_DATASTORE=62;
	public static final int T_DATASTORES=63;
	public static final int T_DAY=64;
	public static final int T_DAYS=65;
	public static final int T_DEFAULT=66;
	public static final int T_DELETE=67;
	public static final int T_DESC=68;
	public static final int T_DESCRIBE=69;
	public static final int T_DETACH=70;
	public static final int T_DISABLE=71;
	public static final int T_DISTINCT=72;
	public static final int T_DOUBLE=73;
	public static final int T_DROP=74;
	public static final int T_EACH_QUORUM=75;
	public static final int T_END_BRACKET=76;
	public static final int T_END_PARENTHESIS=77;
	public static final int T_END_SBRACKET=78;
	public static final int T_EPHEMERAL=79;
	public static final int T_EQUAL=80;
	public static final int T_EXISTS=81;
	public static final int T_EXPLAIN=82;
	public static final int T_FALSE=83;
	public static final int T_FLOAT=84;
	public static final int T_FOR=85;
	public static final int T_FROM=86;
	public static final int T_FULL_TEXT=87;
	public static final int T_GROUP=88;
	public static final int T_GT=89;
	public static final int T_GTE=90;
	public static final int T_HOUR=91;
	public static final int T_HOURS=92;
	public static final int T_IDENT=93;
	public static final int T_IF=94;
	public static final int T_IN=95;
	public static final int T_INDEX=96;
	public static final int T_INNER=97;
	public static final int T_INSERT=98;
	public static final int T_INT=99;
	public static final int T_INTEGER=100;
	public static final int T_INTERROGATION=101;
	public static final int T_INTO=102;
	public static final int T_JOIN=103;
	public static final int T_KEY=104;
	public static final int T_KEYSPACE=105;
	public static final int T_KEYSPACES=106;
	public static final int T_KS_AND_TN=107;
	public static final int T_LAST=108;
	public static final int T_LIKE=109;
	public static final int T_LIMIT=110;
	public static final int T_LIST=111;
	public static final int T_LOCAL_ONE=112;
	public static final int T_LOCAL_QUORUM=113;
	public static final int T_LT=114;
	public static final int T_LTE=115;
	public static final int T_LUCENE=116;
	public static final int T_MAP=117;
	public static final int T_MATCH=118;
	public static final int T_MAX=119;
	public static final int T_MIN=120;
	public static final int T_MINS=121;
	public static final int T_MINUTE=122;
	public static final int T_MINUTES=123;
	public static final int T_NOT=124;
	public static final int T_NOT_EQUAL=125;
	public static final int T_NULL=126;
	public static final int T_ON=127;
	public static final int T_ONE=128;
	public static final int T_OPTIONS=129;
	public static final int T_ORDER=130;
	public static final int T_PLAN=131;
	public static final int T_PLUS=132;
	public static final int T_PRIMARY=133;
	public static final int T_PROCESS=134;
	public static final int T_QUORUM=135;
	public static final int T_QUOTE=136;
	public static final int T_REMOVE=137;
	public static final int T_ROWS=138;
	public static final int T_SEC=139;
	public static final int T_SECOND=140;
	public static final int T_SECONDS=141;
	public static final int T_SECS=142;
	public static final int T_SELECT=143;
	public static final int T_SEMICOLON=144;
	public static final int T_SET=145;
	public static final int T_SINGLE_QUOTE=146;
	public static final int T_SLASH=147;
	public static final int T_START_BRACKET=148;
	public static final int T_START_PARENTHESIS=149;
	public static final int T_START_SBRACKET=150;
	public static final int T_STOP=151;
	public static final int T_STORAGE=152;
	public static final int T_SUBTRACT=153;
	public static final int T_SUM=154;
	public static final int T_TABLE=155;
	public static final int T_TABLES=156;
	public static final int T_TERM=157;
	public static final int T_TEXT=158;
	public static final int T_THREE=159;
	public static final int T_TO=160;
	public static final int T_TOKEN=161;
	public static final int T_TRIGGER=162;
	public static final int T_TRUE=163;
	public static final int T_TRUNCATE=164;
	public static final int T_TWO=165;
	public static final int T_TYPE=166;
	public static final int T_UDF=167;
	public static final int T_UPDATE=168;
	public static final int T_USE=169;
	public static final int T_USING=170;
	public static final int T_VALUES=171;
	public static final int T_VARCHAR=172;
	public static final int T_WHERE=173;
	public static final int T_WINDOW=174;
	public static final int T_WITH=175;
	public static final int U=176;
	public static final int V=177;
	public static final int W=178;
	public static final int WS=179;
	public static final int X=180;
	public static final int Y=181;
	public static final int Z=182;

	    @Override
	    public void recover(RecognitionException re) {
	        throw new ParsingException("line " + input.getLine() + ":" + input.getCharPositionInLine()
	                    + ": no viable alternative");
	    }


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public MetaLexer() {} 
	public MetaLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public MetaLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "Meta.g"; }

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// Meta.g:145:11: ( ( 'a' | 'A' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "A"

	// $ANTLR start "B"
	public final void mB() throws RecognitionException {
		try {
			// Meta.g:146:11: ( ( 'b' | 'B' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "B"

	// $ANTLR start "C"
	public final void mC() throws RecognitionException {
		try {
			// Meta.g:147:11: ( ( 'c' | 'C' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "C"

	// $ANTLR start "D"
	public final void mD() throws RecognitionException {
		try {
			// Meta.g:148:11: ( ( 'd' | 'D' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "D"

	// $ANTLR start "E"
	public final void mE() throws RecognitionException {
		try {
			// Meta.g:149:11: ( ( 'e' | 'E' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "E"

	// $ANTLR start "F"
	public final void mF() throws RecognitionException {
		try {
			// Meta.g:150:11: ( ( 'f' | 'F' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "F"

	// $ANTLR start "G"
	public final void mG() throws RecognitionException {
		try {
			// Meta.g:151:11: ( ( 'g' | 'G' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "G"

	// $ANTLR start "H"
	public final void mH() throws RecognitionException {
		try {
			// Meta.g:152:11: ( ( 'h' | 'H' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "H"

	// $ANTLR start "I"
	public final void mI() throws RecognitionException {
		try {
			// Meta.g:153:11: ( ( 'i' | 'I' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "I"

	// $ANTLR start "J"
	public final void mJ() throws RecognitionException {
		try {
			// Meta.g:154:11: ( ( 'j' | 'J' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "J"

	// $ANTLR start "K"
	public final void mK() throws RecognitionException {
		try {
			// Meta.g:155:11: ( ( 'k' | 'K' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K"

	// $ANTLR start "L"
	public final void mL() throws RecognitionException {
		try {
			// Meta.g:156:11: ( ( 'l' | 'L' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "L"

	// $ANTLR start "M"
	public final void mM() throws RecognitionException {
		try {
			// Meta.g:157:11: ( ( 'm' | 'M' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "M"

	// $ANTLR start "N"
	public final void mN() throws RecognitionException {
		try {
			// Meta.g:158:11: ( ( 'n' | 'N' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "N"

	// $ANTLR start "O"
	public final void mO() throws RecognitionException {
		try {
			// Meta.g:159:11: ( ( 'o' | 'O' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "O"

	// $ANTLR start "P"
	public final void mP() throws RecognitionException {
		try {
			// Meta.g:160:11: ( ( 'p' | 'P' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "P"

	// $ANTLR start "Q"
	public final void mQ() throws RecognitionException {
		try {
			// Meta.g:161:11: ( ( 'q' | 'Q' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Q"

	// $ANTLR start "R"
	public final void mR() throws RecognitionException {
		try {
			// Meta.g:162:11: ( ( 'r' | 'R' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "R"

	// $ANTLR start "S"
	public final void mS() throws RecognitionException {
		try {
			// Meta.g:163:11: ( ( 's' | 'S' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "S"

	// $ANTLR start "T"
	public final void mT() throws RecognitionException {
		try {
			// Meta.g:164:11: ( ( 't' | 'T' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T"

	// $ANTLR start "U"
	public final void mU() throws RecognitionException {
		try {
			// Meta.g:165:11: ( ( 'u' | 'U' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "U"

	// $ANTLR start "V"
	public final void mV() throws RecognitionException {
		try {
			// Meta.g:166:11: ( ( 'v' | 'V' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "V"

	// $ANTLR start "W"
	public final void mW() throws RecognitionException {
		try {
			// Meta.g:167:11: ( ( 'w' | 'W' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "W"

	// $ANTLR start "X"
	public final void mX() throws RecognitionException {
		try {
			// Meta.g:168:11: ( ( 'x' | 'X' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "X"

	// $ANTLR start "Y"
	public final void mY() throws RecognitionException {
		try {
			// Meta.g:169:11: ( ( 'y' | 'Y' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Y"

	// $ANTLR start "Z"
	public final void mZ() throws RecognitionException {
		try {
			// Meta.g:170:11: ( ( 'z' | 'Z' ) )
			// Meta.g:
			{
			if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Z"

	// $ANTLR start "EXPONENT"
	public final void mEXPONENT() throws RecognitionException {
		try {
			// Meta.g:171:19: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
			// Meta.g:171:21: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Meta.g:171:31: ( '+' | '-' )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0=='+'||LA1_0=='-') ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// Meta.g:
					{
					if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			// Meta.g:171:42: ( '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EXPONENT"

	// $ANTLR start "POINT"
	public final void mPOINT() throws RecognitionException {
		try {
			// Meta.g:172:15: ( '.' )
			// Meta.g:172:17: '.'
			{
			match('.'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "POINT"

	// $ANTLR start "T_DESCRIBE"
	public final void mT_DESCRIBE() throws RecognitionException {
		try {
			int _type = T_DESCRIBE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:175:11: ( D E S C R I B E )
			// Meta.g:175:13: D E S C R I B E
			{
			mD(); 

			mE(); 

			mS(); 

			mC(); 

			mR(); 

			mI(); 

			mB(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DESCRIBE"

	// $ANTLR start "T_TRUNCATE"
	public final void mT_TRUNCATE() throws RecognitionException {
		try {
			int _type = T_TRUNCATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:176:11: ( T R U N C A T E )
			// Meta.g:176:13: T R U N C A T E
			{
			mT(); 

			mR(); 

			mU(); 

			mN(); 

			mC(); 

			mA(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TRUNCATE"

	// $ANTLR start "T_CREATE"
	public final void mT_CREATE() throws RecognitionException {
		try {
			int _type = T_CREATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:177:9: ( C R E A T E )
			// Meta.g:177:11: C R E A T E
			{
			mC(); 

			mR(); 

			mE(); 

			mA(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CREATE"

	// $ANTLR start "T_ALTER"
	public final void mT_ALTER() throws RecognitionException {
		try {
			int _type = T_ALTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:178:8: ( A L T E R )
			// Meta.g:178:10: A L T E R
			{
			mA(); 

			mL(); 

			mT(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ALTER"

	// $ANTLR start "T_KEYSPACE"
	public final void mT_KEYSPACE() throws RecognitionException {
		try {
			int _type = T_KEYSPACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:179:11: ( K E Y S P A C E )
			// Meta.g:179:13: K E Y S P A C E
			{
			mK(); 

			mE(); 

			mY(); 

			mS(); 

			mP(); 

			mA(); 

			mC(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_KEYSPACE"

	// $ANTLR start "T_KEYSPACES"
	public final void mT_KEYSPACES() throws RecognitionException {
		try {
			int _type = T_KEYSPACES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:180:12: ( K E Y S P A C E S )
			// Meta.g:180:14: K E Y S P A C E S
			{
			mK(); 

			mE(); 

			mY(); 

			mS(); 

			mP(); 

			mA(); 

			mC(); 

			mE(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_KEYSPACES"

	// $ANTLR start "T_NOT"
	public final void mT_NOT() throws RecognitionException {
		try {
			int _type = T_NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:181:6: ( N O T )
			// Meta.g:181:8: N O T
			{
			mN(); 

			mO(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_NOT"

	// $ANTLR start "T_WITH"
	public final void mT_WITH() throws RecognitionException {
		try {
			int _type = T_WITH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:182:7: ( W I T H )
			// Meta.g:182:9: W I T H
			{
			mW(); 

			mI(); 

			mT(); 

			mH(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_WITH"

	// $ANTLR start "T_DROP"
	public final void mT_DROP() throws RecognitionException {
		try {
			int _type = T_DROP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:183:7: ( D R O P )
			// Meta.g:183:9: D R O P
			{
			mD(); 

			mR(); 

			mO(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DROP"

	// $ANTLR start "T_TABLE"
	public final void mT_TABLE() throws RecognitionException {
		try {
			int _type = T_TABLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:184:8: ( T A B L E )
			// Meta.g:184:10: T A B L E
			{
			mT(); 

			mA(); 

			mB(); 

			mL(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TABLE"

	// $ANTLR start "T_TABLES"
	public final void mT_TABLES() throws RecognitionException {
		try {
			int _type = T_TABLES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:185:9: ( T A B L E S )
			// Meta.g:185:11: T A B L E S
			{
			mT(); 

			mA(); 

			mB(); 

			mL(); 

			mE(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TABLES"

	// $ANTLR start "T_IF"
	public final void mT_IF() throws RecognitionException {
		try {
			int _type = T_IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:186:5: ( I F )
			// Meta.g:186:7: I F
			{
			mI(); 

			mF(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_IF"

	// $ANTLR start "T_EXISTS"
	public final void mT_EXISTS() throws RecognitionException {
		try {
			int _type = T_EXISTS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:187:9: ( E X I S T S )
			// Meta.g:187:11: E X I S T S
			{
			mE(); 

			mX(); 

			mI(); 

			mS(); 

			mT(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EXISTS"

	// $ANTLR start "T_AND"
	public final void mT_AND() throws RecognitionException {
		try {
			int _type = T_AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:188:6: ( A N D )
			// Meta.g:188:8: A N D
			{
			mA(); 

			mN(); 

			mD(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AND"

	// $ANTLR start "T_USE"
	public final void mT_USE() throws RecognitionException {
		try {
			int _type = T_USE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:189:6: ( U S E )
			// Meta.g:189:8: U S E
			{
			mU(); 

			mS(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_USE"

	// $ANTLR start "T_SET"
	public final void mT_SET() throws RecognitionException {
		try {
			int _type = T_SET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:190:6: ( S E T )
			// Meta.g:190:8: S E T
			{
			mS(); 

			mE(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SET"

	// $ANTLR start "T_OPTIONS"
	public final void mT_OPTIONS() throws RecognitionException {
		try {
			int _type = T_OPTIONS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:191:10: ( O P T I O N S )
			// Meta.g:191:12: O P T I O N S
			{
			mO(); 

			mP(); 

			mT(); 

			mI(); 

			mO(); 

			mN(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_OPTIONS"

	// $ANTLR start "T_ANALYTICS"
	public final void mT_ANALYTICS() throws RecognitionException {
		try {
			int _type = T_ANALYTICS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:192:12: ( A N A L Y T I C S )
			// Meta.g:192:14: A N A L Y T I C S
			{
			mA(); 

			mN(); 

			mA(); 

			mL(); 

			mY(); 

			mT(); 

			mI(); 

			mC(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ANALYTICS"

	// $ANTLR start "T_TRUE"
	public final void mT_TRUE() throws RecognitionException {
		try {
			int _type = T_TRUE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:193:7: ( T R U E )
			// Meta.g:193:9: T R U E
			{
			mT(); 

			mR(); 

			mU(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TRUE"

	// $ANTLR start "T_FALSE"
	public final void mT_FALSE() throws RecognitionException {
		try {
			int _type = T_FALSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:194:8: ( F A L S E )
			// Meta.g:194:10: F A L S E
			{
			mF(); 

			mA(); 

			mL(); 

			mS(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FALSE"

	// $ANTLR start "T_CONSISTENCY"
	public final void mT_CONSISTENCY() throws RecognitionException {
		try {
			int _type = T_CONSISTENCY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:195:14: ( C O N S I S T E N C Y )
			// Meta.g:195:16: C O N S I S T E N C Y
			{
			mC(); 

			mO(); 

			mN(); 

			mS(); 

			mI(); 

			mS(); 

			mT(); 

			mE(); 

			mN(); 

			mC(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CONSISTENCY"

	// $ANTLR start "T_ALL"
	public final void mT_ALL() throws RecognitionException {
		try {
			int _type = T_ALL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:196:6: ( A L L )
			// Meta.g:196:8: A L L
			{
			mA(); 

			mL(); 

			mL(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ALL"

	// $ANTLR start "T_ANY"
	public final void mT_ANY() throws RecognitionException {
		try {
			int _type = T_ANY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:197:6: ( A N Y )
			// Meta.g:197:8: A N Y
			{
			mA(); 

			mN(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ANY"

	// $ANTLR start "T_QUORUM"
	public final void mT_QUORUM() throws RecognitionException {
		try {
			int _type = T_QUORUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:198:9: ( Q U O R U M )
			// Meta.g:198:11: Q U O R U M
			{
			mQ(); 

			mU(); 

			mO(); 

			mR(); 

			mU(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_QUORUM"

	// $ANTLR start "T_ONE"
	public final void mT_ONE() throws RecognitionException {
		try {
			int _type = T_ONE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:199:6: ( O N E )
			// Meta.g:199:8: O N E
			{
			mO(); 

			mN(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ONE"

	// $ANTLR start "T_TWO"
	public final void mT_TWO() throws RecognitionException {
		try {
			int _type = T_TWO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:200:6: ( T W O )
			// Meta.g:200:8: T W O
			{
			mT(); 

			mW(); 

			mO(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TWO"

	// $ANTLR start "T_THREE"
	public final void mT_THREE() throws RecognitionException {
		try {
			int _type = T_THREE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:201:8: ( T H R E E )
			// Meta.g:201:10: T H R E E
			{
			mT(); 

			mH(); 

			mR(); 

			mE(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_THREE"

	// $ANTLR start "T_EACH_QUORUM"
	public final void mT_EACH_QUORUM() throws RecognitionException {
		try {
			int _type = T_EACH_QUORUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:202:14: ( E A C H '_' Q U O R U M )
			// Meta.g:202:16: E A C H '_' Q U O R U M
			{
			mE(); 

			mA(); 

			mC(); 

			mH(); 

			match('_'); 
			mQ(); 

			mU(); 

			mO(); 

			mR(); 

			mU(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EACH_QUORUM"

	// $ANTLR start "T_LOCAL_ONE"
	public final void mT_LOCAL_ONE() throws RecognitionException {
		try {
			int _type = T_LOCAL_ONE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:203:12: ( L O C A L '_' O N E )
			// Meta.g:203:14: L O C A L '_' O N E
			{
			mL(); 

			mO(); 

			mC(); 

			mA(); 

			mL(); 

			match('_'); 
			mO(); 

			mN(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LOCAL_ONE"

	// $ANTLR start "T_LOCAL_QUORUM"
	public final void mT_LOCAL_QUORUM() throws RecognitionException {
		try {
			int _type = T_LOCAL_QUORUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:204:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:204:17: L O C A L '_' Q U O R U M
			{
			mL(); 

			mO(); 

			mC(); 

			mA(); 

			mL(); 

			match('_'); 
			mQ(); 

			mU(); 

			mO(); 

			mR(); 

			mU(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LOCAL_QUORUM"

	// $ANTLR start "T_EXPLAIN"
	public final void mT_EXPLAIN() throws RecognitionException {
		try {
			int _type = T_EXPLAIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:205:10: ( E X P L A I N )
			// Meta.g:205:12: E X P L A I N
			{
			mE(); 

			mX(); 

			mP(); 

			mL(); 

			mA(); 

			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EXPLAIN"

	// $ANTLR start "T_PLAN"
	public final void mT_PLAN() throws RecognitionException {
		try {
			int _type = T_PLAN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:206:7: ( P L A N )
			// Meta.g:206:9: P L A N
			{
			mP(); 

			mL(); 

			mA(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PLAN"

	// $ANTLR start "T_FOR"
	public final void mT_FOR() throws RecognitionException {
		try {
			int _type = T_FOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:207:6: ( F O R )
			// Meta.g:207:8: F O R
			{
			mF(); 

			mO(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FOR"

	// $ANTLR start "T_INDEX"
	public final void mT_INDEX() throws RecognitionException {
		try {
			int _type = T_INDEX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:208:8: ( I N D E X )
			// Meta.g:208:10: I N D E X
			{
			mI(); 

			mN(); 

			mD(); 

			mE(); 

			mX(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INDEX"

	// $ANTLR start "T_LIST"
	public final void mT_LIST() throws RecognitionException {
		try {
			int _type = T_LIST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:209:7: ( L I S T )
			// Meta.g:209:9: L I S T
			{
			mL(); 

			mI(); 

			mS(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LIST"

	// $ANTLR start "T_REMOVE"
	public final void mT_REMOVE() throws RecognitionException {
		try {
			int _type = T_REMOVE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:210:9: ( R E M O V E )
			// Meta.g:210:11: R E M O V E
			{
			mR(); 

			mE(); 

			mM(); 

			mO(); 

			mV(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_REMOVE"

	// $ANTLR start "T_UDF"
	public final void mT_UDF() throws RecognitionException {
		try {
			int _type = T_UDF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:211:6: ( U D F )
			// Meta.g:211:8: U D F
			{
			mU(); 

			mD(); 

			mF(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_UDF"

	// $ANTLR start "T_PROCESS"
	public final void mT_PROCESS() throws RecognitionException {
		try {
			int _type = T_PROCESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:212:10: ( P R O C E S S )
			// Meta.g:212:12: P R O C E S S
			{
			mP(); 

			mR(); 

			mO(); 

			mC(); 

			mE(); 

			mS(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PROCESS"

	// $ANTLR start "T_TRIGGER"
	public final void mT_TRIGGER() throws RecognitionException {
		try {
			int _type = T_TRIGGER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:213:10: ( T R I G G E R )
			// Meta.g:213:12: T R I G G E R
			{
			mT(); 

			mR(); 

			mI(); 

			mG(); 

			mG(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TRIGGER"

	// $ANTLR start "T_STOP"
	public final void mT_STOP() throws RecognitionException {
		try {
			int _type = T_STOP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:214:7: ( S T O P )
			// Meta.g:214:9: S T O P
			{
			mS(); 

			mT(); 

			mO(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_STOP"

	// $ANTLR start "T_ON"
	public final void mT_ON() throws RecognitionException {
		try {
			int _type = T_ON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:215:5: ( O N )
			// Meta.g:215:7: O N
			{
			mO(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ON"

	// $ANTLR start "T_USING"
	public final void mT_USING() throws RecognitionException {
		try {
			int _type = T_USING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:216:8: ( U S I N G )
			// Meta.g:216:10: U S I N G
			{
			mU(); 

			mS(); 

			mI(); 

			mN(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_USING"

	// $ANTLR start "T_TYPE"
	public final void mT_TYPE() throws RecognitionException {
		try {
			int _type = T_TYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:217:7: ( T Y P E )
			// Meta.g:217:9: T Y P E
			{
			mT(); 

			mY(); 

			mP(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TYPE"

	// $ANTLR start "T_ADD"
	public final void mT_ADD() throws RecognitionException {
		try {
			int _type = T_ADD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:218:6: ( A D D )
			// Meta.g:218:8: A D D
			{
			mA(); 

			mD(); 

			mD(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ADD"

	// $ANTLR start "T_PRIMARY"
	public final void mT_PRIMARY() throws RecognitionException {
		try {
			int _type = T_PRIMARY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:219:10: ( P R I M A R Y )
			// Meta.g:219:12: P R I M A R Y
			{
			mP(); 

			mR(); 

			mI(); 

			mM(); 

			mA(); 

			mR(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PRIMARY"

	// $ANTLR start "T_KEY"
	public final void mT_KEY() throws RecognitionException {
		try {
			int _type = T_KEY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:220:6: ( K E Y )
			// Meta.g:220:8: K E Y
			{
			mK(); 

			mE(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_KEY"

	// $ANTLR start "T_INSERT"
	public final void mT_INSERT() throws RecognitionException {
		try {
			int _type = T_INSERT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:221:9: ( I N S E R T )
			// Meta.g:221:11: I N S E R T
			{
			mI(); 

			mN(); 

			mS(); 

			mE(); 

			mR(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INSERT"

	// $ANTLR start "T_INTO"
	public final void mT_INTO() throws RecognitionException {
		try {
			int _type = T_INTO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:222:7: ( I N T O )
			// Meta.g:222:9: I N T O
			{
			mI(); 

			mN(); 

			mT(); 

			mO(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INTO"

	// $ANTLR start "T_COMPACT"
	public final void mT_COMPACT() throws RecognitionException {
		try {
			int _type = T_COMPACT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:223:10: ( C O M P A C T )
			// Meta.g:223:12: C O M P A C T
			{
			mC(); 

			mO(); 

			mM(); 

			mP(); 

			mA(); 

			mC(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COMPACT"

	// $ANTLR start "T_STORAGE"
	public final void mT_STORAGE() throws RecognitionException {
		try {
			int _type = T_STORAGE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:224:10: ( S T O R A G E )
			// Meta.g:224:12: S T O R A G E
			{
			mS(); 

			mT(); 

			mO(); 

			mR(); 

			mA(); 

			mG(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_STORAGE"

	// $ANTLR start "T_CLUSTER"
	public final void mT_CLUSTER() throws RecognitionException {
		try {
			int _type = T_CLUSTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:225:10: ( C L U S T E R )
			// Meta.g:225:12: C L U S T E R
			{
			mC(); 

			mL(); 

			mU(); 

			mS(); 

			mT(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CLUSTER"

	// $ANTLR start "T_CLUSTERS"
	public final void mT_CLUSTERS() throws RecognitionException {
		try {
			int _type = T_CLUSTERS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:226:11: ( C L U S T E R S )
			// Meta.g:226:13: C L U S T E R S
			{
			mC(); 

			mL(); 

			mU(); 

			mS(); 

			mT(); 

			mE(); 

			mR(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CLUSTERS"

	// $ANTLR start "T_CLUSTERING"
	public final void mT_CLUSTERING() throws RecognitionException {
		try {
			int _type = T_CLUSTERING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:227:13: ( C L U S T E R I N G )
			// Meta.g:227:15: C L U S T E R I N G
			{
			mC(); 

			mL(); 

			mU(); 

			mS(); 

			mT(); 

			mE(); 

			mR(); 

			mI(); 

			mN(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CLUSTERING"

	// $ANTLR start "T_ORDER"
	public final void mT_ORDER() throws RecognitionException {
		try {
			int _type = T_ORDER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:228:8: ( O R D E R )
			// Meta.g:228:10: O R D E R
			{
			mO(); 

			mR(); 

			mD(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ORDER"

	// $ANTLR start "T_SELECT"
	public final void mT_SELECT() throws RecognitionException {
		try {
			int _type = T_SELECT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:229:9: ( S E L E C T )
			// Meta.g:229:11: S E L E C T
			{
			mS(); 

			mE(); 

			mL(); 

			mE(); 

			mC(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SELECT"

	// $ANTLR start "T_VALUES"
	public final void mT_VALUES() throws RecognitionException {
		try {
			int _type = T_VALUES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:230:9: ( V A L U E S )
			// Meta.g:230:11: V A L U E S
			{
			mV(); 

			mA(); 

			mL(); 

			mU(); 

			mE(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_VALUES"

	// $ANTLR start "T_UPDATE"
	public final void mT_UPDATE() throws RecognitionException {
		try {
			int _type = T_UPDATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:231:9: ( U P D A T E )
			// Meta.g:231:11: U P D A T E
			{
			mU(); 

			mP(); 

			mD(); 

			mA(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_UPDATE"

	// $ANTLR start "T_WHERE"
	public final void mT_WHERE() throws RecognitionException {
		try {
			int _type = T_WHERE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:232:8: ( W H E R E )
			// Meta.g:232:10: W H E R E
			{
			mW(); 

			mH(); 

			mE(); 

			mR(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_WHERE"

	// $ANTLR start "T_IN"
	public final void mT_IN() throws RecognitionException {
		try {
			int _type = T_IN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:233:5: ( I N )
			// Meta.g:233:7: I N
			{
			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_IN"

	// $ANTLR start "T_FROM"
	public final void mT_FROM() throws RecognitionException {
		try {
			int _type = T_FROM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:234:7: ( F R O M )
			// Meta.g:234:9: F R O M
			{
			mF(); 

			mR(); 

			mO(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FROM"

	// $ANTLR start "T_DELETE"
	public final void mT_DELETE() throws RecognitionException {
		try {
			int _type = T_DELETE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:235:9: ( D E L E T E )
			// Meta.g:235:11: D E L E T E
			{
			mD(); 

			mE(); 

			mL(); 

			mE(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DELETE"

	// $ANTLR start "T_WINDOW"
	public final void mT_WINDOW() throws RecognitionException {
		try {
			int _type = T_WINDOW;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:236:9: ( W I N D O W )
			// Meta.g:236:11: W I N D O W
			{
			mW(); 

			mI(); 

			mN(); 

			mD(); 

			mO(); 

			mW(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_WINDOW"

	// $ANTLR start "T_LAST"
	public final void mT_LAST() throws RecognitionException {
		try {
			int _type = T_LAST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:237:7: ( L A S T )
			// Meta.g:237:9: L A S T
			{
			mL(); 

			mA(); 

			mS(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LAST"

	// $ANTLR start "T_ROWS"
	public final void mT_ROWS() throws RecognitionException {
		try {
			int _type = T_ROWS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:238:7: ( R O W S )
			// Meta.g:238:9: R O W S
			{
			mR(); 

			mO(); 

			mW(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ROWS"

	// $ANTLR start "T_INNER"
	public final void mT_INNER() throws RecognitionException {
		try {
			int _type = T_INNER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:239:8: ( I N N E R )
			// Meta.g:239:10: I N N E R
			{
			mI(); 

			mN(); 

			mN(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INNER"

	// $ANTLR start "T_JOIN"
	public final void mT_JOIN() throws RecognitionException {
		try {
			int _type = T_JOIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:240:7: ( J O I N )
			// Meta.g:240:9: J O I N
			{
			mJ(); 

			mO(); 

			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_JOIN"

	// $ANTLR start "T_BY"
	public final void mT_BY() throws RecognitionException {
		try {
			int _type = T_BY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:241:5: ( B Y )
			// Meta.g:241:7: B Y
			{
			mB(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_BY"

	// $ANTLR start "T_LIMIT"
	public final void mT_LIMIT() throws RecognitionException {
		try {
			int _type = T_LIMIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:242:8: ( L I M I T )
			// Meta.g:242:10: L I M I T
			{
			mL(); 

			mI(); 

			mM(); 

			mI(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LIMIT"

	// $ANTLR start "T_DISABLE"
	public final void mT_DISABLE() throws RecognitionException {
		try {
			int _type = T_DISABLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:243:10: ( D I S A B L E )
			// Meta.g:243:12: D I S A B L E
			{
			mD(); 

			mI(); 

			mS(); 

			mA(); 

			mB(); 

			mL(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DISABLE"

	// $ANTLR start "T_DISTINCT"
	public final void mT_DISTINCT() throws RecognitionException {
		try {
			int _type = T_DISTINCT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:244:11: ( D I S T I N C T )
			// Meta.g:244:13: D I S T I N C T
			{
			mD(); 

			mI(); 

			mS(); 

			mT(); 

			mI(); 

			mN(); 

			mC(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DISTINCT"

	// $ANTLR start "T_COUNT"
	public final void mT_COUNT() throws RecognitionException {
		try {
			int _type = T_COUNT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:245:8: ( C O U N T )
			// Meta.g:245:10: C O U N T
			{
			mC(); 

			mO(); 

			mU(); 

			mN(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COUNT"

	// $ANTLR start "T_AS"
	public final void mT_AS() throws RecognitionException {
		try {
			int _type = T_AS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:246:5: ( A S )
			// Meta.g:246:7: A S
			{
			mA(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AS"

	// $ANTLR start "T_BETWEEN"
	public final void mT_BETWEEN() throws RecognitionException {
		try {
			int _type = T_BETWEEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:247:10: ( B E T W E E N )
			// Meta.g:247:12: B E T W E E N
			{
			mB(); 

			mE(); 

			mT(); 

			mW(); 

			mE(); 

			mE(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_BETWEEN"

	// $ANTLR start "T_ASC"
	public final void mT_ASC() throws RecognitionException {
		try {
			int _type = T_ASC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:248:6: ( A S C )
			// Meta.g:248:8: A S C
			{
			mA(); 

			mS(); 

			mC(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ASC"

	// $ANTLR start "T_DESC"
	public final void mT_DESC() throws RecognitionException {
		try {
			int _type = T_DESC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:249:7: ( D E S C )
			// Meta.g:249:9: D E S C
			{
			mD(); 

			mE(); 

			mS(); 

			mC(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DESC"

	// $ANTLR start "T_LIKE"
	public final void mT_LIKE() throws RecognitionException {
		try {
			int _type = T_LIKE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:250:7: ( L I K E )
			// Meta.g:250:9: L I K E
			{
			mL(); 

			mI(); 

			mK(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LIKE"

	// $ANTLR start "T_EPHEMERAL"
	public final void mT_EPHEMERAL() throws RecognitionException {
		try {
			int _type = T_EPHEMERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:251:12: ( E P H E M E R A L )
			// Meta.g:251:14: E P H E M E R A L
			{
			mE(); 

			mP(); 

			mH(); 

			mE(); 

			mM(); 

			mE(); 

			mR(); 

			mA(); 

			mL(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EPHEMERAL"

	// $ANTLR start "T_AT"
	public final void mT_AT() throws RecognitionException {
		try {
			int _type = T_AT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:252:5: ( '@' )
			// Meta.g:252:7: '@'
			{
			match('@'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AT"

	// $ANTLR start "T_CATALOG"
	public final void mT_CATALOG() throws RecognitionException {
		try {
			int _type = T_CATALOG;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:253:10: ( C A T A L O G )
			// Meta.g:253:12: C A T A L O G
			{
			mC(); 

			mA(); 

			mT(); 

			mA(); 

			mL(); 

			mO(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CATALOG"

	// $ANTLR start "T_CATALOGS"
	public final void mT_CATALOGS() throws RecognitionException {
		try {
			int _type = T_CATALOGS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:254:11: ( C A T A L O G S )
			// Meta.g:254:13: C A T A L O G S
			{
			mC(); 

			mA(); 

			mT(); 

			mA(); 

			mL(); 

			mO(); 

			mG(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CATALOGS"

	// $ANTLR start "T_DATASTORE"
	public final void mT_DATASTORE() throws RecognitionException {
		try {
			int _type = T_DATASTORE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:255:12: ( D A T A S T O R E )
			// Meta.g:255:14: D A T A S T O R E
			{
			mD(); 

			mA(); 

			mT(); 

			mA(); 

			mS(); 

			mT(); 

			mO(); 

			mR(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DATASTORE"

	// $ANTLR start "T_DATASTORES"
	public final void mT_DATASTORES() throws RecognitionException {
		try {
			int _type = T_DATASTORES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:256:13: ( D A T A S T O R E S )
			// Meta.g:256:15: D A T A S T O R E S
			{
			mD(); 

			mA(); 

			mT(); 

			mA(); 

			mS(); 

			mT(); 

			mO(); 

			mR(); 

			mE(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DATASTORES"

	// $ANTLR start "T_CONNECTOR"
	public final void mT_CONNECTOR() throws RecognitionException {
		try {
			int _type = T_CONNECTOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:257:12: ( C O N N E C T O R )
			// Meta.g:257:14: C O N N E C T O R
			{
			mC(); 

			mO(); 

			mN(); 

			mN(); 

			mE(); 

			mC(); 

			mT(); 

			mO(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CONNECTOR"

	// $ANTLR start "T_CONNECTORS"
	public final void mT_CONNECTORS() throws RecognitionException {
		try {
			int _type = T_CONNECTORS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:258:13: ( C O N N E C T O R S )
			// Meta.g:258:15: C O N N E C T O R S
			{
			mC(); 

			mO(); 

			mN(); 

			mN(); 

			mE(); 

			mC(); 

			mT(); 

			mO(); 

			mR(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CONNECTORS"

	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:260:12: ( ';' )
			// Meta.g:260:14: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SEMICOLON"

	// $ANTLR start "T_EQUAL"
	public final void mT_EQUAL() throws RecognitionException {
		try {
			int _type = T_EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:261:8: ( '=' )
			// Meta.g:261:10: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_EQUAL"

	// $ANTLR start "T_START_SBRACKET"
	public final void mT_START_SBRACKET() throws RecognitionException {
		try {
			int _type = T_START_SBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:262:17: ( '{' )
			// Meta.g:262:19: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_START_SBRACKET"

	// $ANTLR start "T_END_SBRACKET"
	public final void mT_END_SBRACKET() throws RecognitionException {
		try {
			int _type = T_END_SBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:263:15: ( '}' )
			// Meta.g:263:17: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_END_SBRACKET"

	// $ANTLR start "T_COLON"
	public final void mT_COLON() throws RecognitionException {
		try {
			int _type = T_COLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:264:8: ( ':' )
			// Meta.g:264:10: ':'
			{
			match(':'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COLON"

	// $ANTLR start "T_COMMA"
	public final void mT_COMMA() throws RecognitionException {
		try {
			int _type = T_COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:265:8: ( ',' )
			// Meta.g:265:10: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_COMMA"

	// $ANTLR start "T_START_PARENTHESIS"
	public final void mT_START_PARENTHESIS() throws RecognitionException {
		try {
			int _type = T_START_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:266:20: ( '(' )
			// Meta.g:266:22: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_START_PARENTHESIS"

	// $ANTLR start "T_END_PARENTHESIS"
	public final void mT_END_PARENTHESIS() throws RecognitionException {
		try {
			int _type = T_END_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:267:18: ( ')' )
			// Meta.g:267:20: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_END_PARENTHESIS"

	// $ANTLR start "T_QUOTE"
	public final void mT_QUOTE() throws RecognitionException {
		try {
			int _type = T_QUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:268:8: ( '\"' )
			// Meta.g:268:10: '\"'
			{
			match('\"'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_QUOTE"

	// $ANTLR start "T_SINGLE_QUOTE"
	public final void mT_SINGLE_QUOTE() throws RecognitionException {
		try {
			int _type = T_SINGLE_QUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:269:15: ( '\\'' )
			// Meta.g:269:17: '\\''
			{
			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SINGLE_QUOTE"

	// $ANTLR start "T_DEFAULT"
	public final void mT_DEFAULT() throws RecognitionException {
		try {
			int _type = T_DEFAULT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:270:10: ( D E F A U L T )
			// Meta.g:270:12: D E F A U L T
			{
			mD(); 

			mE(); 

			mF(); 

			mA(); 

			mU(); 

			mL(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DEFAULT"

	// $ANTLR start "T_LUCENE"
	public final void mT_LUCENE() throws RecognitionException {
		try {
			int _type = T_LUCENE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:271:9: ( L U C E N E )
			// Meta.g:271:11: L U C E N E
			{
			mL(); 

			mU(); 

			mC(); 

			mE(); 

			mN(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LUCENE"

	// $ANTLR start "T_CUSTOM"
	public final void mT_CUSTOM() throws RecognitionException {
		try {
			int _type = T_CUSTOM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:272:9: ( C U S T O M )
			// Meta.g:272:11: C U S T O M
			{
			mC(); 

			mU(); 

			mS(); 

			mT(); 

			mO(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CUSTOM"

	// $ANTLR start "T_FULL_TEXT"
	public final void mT_FULL_TEXT() throws RecognitionException {
		try {
			int _type = T_FULL_TEXT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:273:12: ( F U L L '_' T E X T )
			// Meta.g:273:14: F U L L '_' T E X T
			{
			mF(); 

			mU(); 

			mL(); 

			mL(); 

			match('_'); 
			mT(); 

			mE(); 

			mX(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FULL_TEXT"

	// $ANTLR start "T_START_BRACKET"
	public final void mT_START_BRACKET() throws RecognitionException {
		try {
			int _type = T_START_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:274:16: ( '[' )
			// Meta.g:274:18: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_START_BRACKET"

	// $ANTLR start "T_END_BRACKET"
	public final void mT_END_BRACKET() throws RecognitionException {
		try {
			int _type = T_END_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:275:14: ( ']' )
			// Meta.g:275:16: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_END_BRACKET"

	// $ANTLR start "T_PLUS"
	public final void mT_PLUS() throws RecognitionException {
		try {
			int _type = T_PLUS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:276:7: ( '+' )
			// Meta.g:276:9: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PLUS"

	// $ANTLR start "T_SUBTRACT"
	public final void mT_SUBTRACT() throws RecognitionException {
		try {
			int _type = T_SUBTRACT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:277:11: ( '-' )
			// Meta.g:277:13: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SUBTRACT"

	// $ANTLR start "T_SLASH"
	public final void mT_SLASH() throws RecognitionException {
		try {
			int _type = T_SLASH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:278:8: ( '/' )
			// Meta.g:278:10: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SLASH"

	// $ANTLR start "T_INTERROGATION"
	public final void mT_INTERROGATION() throws RecognitionException {
		try {
			int _type = T_INTERROGATION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:279:16: ( '?' )
			// Meta.g:279:18: '?'
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INTERROGATION"

	// $ANTLR start "T_ASTERISK"
	public final void mT_ASTERISK() throws RecognitionException {
		try {
			int _type = T_ASTERISK;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:280:11: ( '*' )
			// Meta.g:280:13: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ASTERISK"

	// $ANTLR start "T_GROUP"
	public final void mT_GROUP() throws RecognitionException {
		try {
			int _type = T_GROUP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:281:8: ( G R O U P )
			// Meta.g:281:10: G R O U P
			{
			mG(); 

			mR(); 

			mO(); 

			mU(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_GROUP"

	// $ANTLR start "T_AGGREGATION"
	public final void mT_AGGREGATION() throws RecognitionException {
		try {
			int _type = T_AGGREGATION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:282:14: ( A G G R E G A T I O N )
			// Meta.g:282:16: A G G R E G A T I O N
			{
			mA(); 

			mG(); 

			mG(); 

			mR(); 

			mE(); 

			mG(); 

			mA(); 

			mT(); 

			mI(); 

			mO(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AGGREGATION"

	// $ANTLR start "T_SUM"
	public final void mT_SUM() throws RecognitionException {
		try {
			int _type = T_SUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:283:6: ( S U M )
			// Meta.g:283:8: S U M
			{
			mS(); 

			mU(); 

			mM(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SUM"

	// $ANTLR start "T_MAX"
	public final void mT_MAX() throws RecognitionException {
		try {
			int _type = T_MAX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:284:6: ( M A X )
			// Meta.g:284:8: M A X
			{
			mM(); 

			mA(); 

			mX(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MAX"

	// $ANTLR start "T_MIN"
	public final void mT_MIN() throws RecognitionException {
		try {
			int _type = T_MIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:285:6: ( M I N )
			// Meta.g:285:8: M I N
			{
			mM(); 

			mI(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MIN"

	// $ANTLR start "T_AVG"
	public final void mT_AVG() throws RecognitionException {
		try {
			int _type = T_AVG;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:286:6: ( A V G )
			// Meta.g:286:8: A V G
			{
			mA(); 

			mV(); 

			mG(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_AVG"

	// $ANTLR start "T_GT"
	public final void mT_GT() throws RecognitionException {
		try {
			int _type = T_GT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:287:5: ( '>' )
			// Meta.g:287:7: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_GT"

	// $ANTLR start "T_LT"
	public final void mT_LT() throws RecognitionException {
		try {
			int _type = T_LT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:288:5: ( '<' )
			// Meta.g:288:7: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LT"

	// $ANTLR start "T_GTE"
	public final void mT_GTE() throws RecognitionException {
		try {
			int _type = T_GTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:289:6: ( '>' '=' )
			// Meta.g:289:8: '>' '='
			{
			match('>'); 
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_GTE"

	// $ANTLR start "T_LTE"
	public final void mT_LTE() throws RecognitionException {
		try {
			int _type = T_LTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:290:6: ( '<' '=' )
			// Meta.g:290:8: '<' '='
			{
			match('<'); 
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_LTE"

	// $ANTLR start "T_NOT_EQUAL"
	public final void mT_NOT_EQUAL() throws RecognitionException {
		try {
			int _type = T_NOT_EQUAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:291:12: ( '<' '>' )
			// Meta.g:291:14: '<' '>'
			{
			match('<'); 
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_NOT_EQUAL"

	// $ANTLR start "T_TOKEN"
	public final void mT_TOKEN() throws RecognitionException {
		try {
			int _type = T_TOKEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:292:8: ( T O K E N )
			// Meta.g:292:10: T O K E N
			{
			mT(); 

			mO(); 

			mK(); 

			mE(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TOKEN"

	// $ANTLR start "T_MATCH"
	public final void mT_MATCH() throws RecognitionException {
		try {
			int _type = T_MATCH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:293:8: ( M A T C H )
			// Meta.g:293:10: M A T C H
			{
			mM(); 

			mA(); 

			mT(); 

			mC(); 

			mH(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MATCH"

	// $ANTLR start "T_SEC"
	public final void mT_SEC() throws RecognitionException {
		try {
			int _type = T_SEC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:294:6: ( S E C )
			// Meta.g:294:8: S E C
			{
			mS(); 

			mE(); 

			mC(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SEC"

	// $ANTLR start "T_SECS"
	public final void mT_SECS() throws RecognitionException {
		try {
			int _type = T_SECS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:295:7: ( S E C S )
			// Meta.g:295:9: S E C S
			{
			mS(); 

			mE(); 

			mC(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SECS"

	// $ANTLR start "T_SECOND"
	public final void mT_SECOND() throws RecognitionException {
		try {
			int _type = T_SECOND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:296:9: ( S E C O N D )
			// Meta.g:296:11: S E C O N D
			{
			mS(); 

			mE(); 

			mC(); 

			mO(); 

			mN(); 

			mD(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SECOND"

	// $ANTLR start "T_SECONDS"
	public final void mT_SECONDS() throws RecognitionException {
		try {
			int _type = T_SECONDS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:297:10: ( S E C O N D S )
			// Meta.g:297:12: S E C O N D S
			{
			mS(); 

			mE(); 

			mC(); 

			mO(); 

			mN(); 

			mD(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_SECONDS"

	// $ANTLR start "T_MINS"
	public final void mT_MINS() throws RecognitionException {
		try {
			int _type = T_MINS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:298:7: ( M I N S )
			// Meta.g:298:9: M I N S
			{
			mM(); 

			mI(); 

			mN(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MINS"

	// $ANTLR start "T_MINUTE"
	public final void mT_MINUTE() throws RecognitionException {
		try {
			int _type = T_MINUTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:299:9: ( M I N U T E )
			// Meta.g:299:11: M I N U T E
			{
			mM(); 

			mI(); 

			mN(); 

			mU(); 

			mT(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MINUTE"

	// $ANTLR start "T_MINUTES"
	public final void mT_MINUTES() throws RecognitionException {
		try {
			int _type = T_MINUTES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:300:10: ( M I N U T E S )
			// Meta.g:300:12: M I N U T E S
			{
			mM(); 

			mI(); 

			mN(); 

			mU(); 

			mT(); 

			mE(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MINUTES"

	// $ANTLR start "T_HOUR"
	public final void mT_HOUR() throws RecognitionException {
		try {
			int _type = T_HOUR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:301:7: ( H O U R )
			// Meta.g:301:9: H O U R
			{
			mH(); 

			mO(); 

			mU(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_HOUR"

	// $ANTLR start "T_HOURS"
	public final void mT_HOURS() throws RecognitionException {
		try {
			int _type = T_HOURS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:302:8: ( H O U R S )
			// Meta.g:302:10: H O U R S
			{
			mH(); 

			mO(); 

			mU(); 

			mR(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_HOURS"

	// $ANTLR start "T_DAY"
	public final void mT_DAY() throws RecognitionException {
		try {
			int _type = T_DAY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:303:6: ( D A Y )
			// Meta.g:303:8: D A Y
			{
			mD(); 

			mA(); 

			mY(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DAY"

	// $ANTLR start "T_DAYS"
	public final void mT_DAYS() throws RecognitionException {
		try {
			int _type = T_DAYS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:304:7: ( D A Y S )
			// Meta.g:304:9: D A Y S
			{
			mD(); 

			mA(); 

			mY(); 

			mS(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DAYS"

	// $ANTLR start "T_NULL"
	public final void mT_NULL() throws RecognitionException {
		try {
			int _type = T_NULL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:305:7: ( N U L L )
			// Meta.g:305:9: N U L L
			{
			mN(); 

			mU(); 

			mL(); 

			mL(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_NULL"

	// $ANTLR start "T_ATTACH"
	public final void mT_ATTACH() throws RecognitionException {
		try {
			int _type = T_ATTACH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:306:9: ( A T T A C H )
			// Meta.g:306:11: A T T A C H
			{
			mA(); 

			mT(); 

			mT(); 

			mA(); 

			mC(); 

			mH(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_ATTACH"

	// $ANTLR start "T_DETACH"
	public final void mT_DETACH() throws RecognitionException {
		try {
			int _type = T_DETACH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:307:9: ( D E T A C H )
			// Meta.g:307:11: D E T A C H
			{
			mD(); 

			mE(); 

			mT(); 

			mA(); 

			mC(); 

			mH(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DETACH"

	// $ANTLR start "T_TO"
	public final void mT_TO() throws RecognitionException {
		try {
			int _type = T_TO;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:308:5: ( T O )
			// Meta.g:308:7: T O
			{
			mT(); 

			mO(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TO"

	// $ANTLR start "T_DOUBLE"
	public final void mT_DOUBLE() throws RecognitionException {
		try {
			int _type = T_DOUBLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:309:9: ( D O U B L E )
			// Meta.g:309:11: D O U B L E
			{
			mD(); 

			mO(); 

			mU(); 

			mB(); 

			mL(); 

			mE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_DOUBLE"

	// $ANTLR start "T_MAP"
	public final void mT_MAP() throws RecognitionException {
		try {
			int _type = T_MAP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:310:6: ( M A P )
			// Meta.g:310:8: M A P
			{
			mM(); 

			mA(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_MAP"

	// $ANTLR start "T_INT"
	public final void mT_INT() throws RecognitionException {
		try {
			int _type = T_INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:311:6: ( I N T )
			// Meta.g:311:8: I N T
			{
			mI(); 

			mN(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INT"

	// $ANTLR start "T_INTEGER"
	public final void mT_INTEGER() throws RecognitionException {
		try {
			int _type = T_INTEGER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:312:10: ( I N T E G E R )
			// Meta.g:312:12: I N T E G E R
			{
			mI(); 

			mN(); 

			mT(); 

			mE(); 

			mG(); 

			mE(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_INTEGER"

	// $ANTLR start "T_BOOLEAN"
	public final void mT_BOOLEAN() throws RecognitionException {
		try {
			int _type = T_BOOLEAN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:313:10: ( B O O L E A N )
			// Meta.g:313:12: B O O L E A N
			{
			mB(); 

			mO(); 

			mO(); 

			mL(); 

			mE(); 

			mA(); 

			mN(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_BOOLEAN"

	// $ANTLR start "T_VARCHAR"
	public final void mT_VARCHAR() throws RecognitionException {
		try {
			int _type = T_VARCHAR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:314:10: ( V A R C H A R )
			// Meta.g:314:12: V A R C H A R
			{
			mV(); 

			mA(); 

			mR(); 

			mC(); 

			mH(); 

			mA(); 

			mR(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_VARCHAR"

	// $ANTLR start "T_TEXT"
	public final void mT_TEXT() throws RecognitionException {
		try {
			int _type = T_TEXT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:315:7: ( T E X T )
			// Meta.g:315:9: T E X T
			{
			mT(); 

			mE(); 

			mX(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TEXT"

	// $ANTLR start "T_BIGINT"
	public final void mT_BIGINT() throws RecognitionException {
		try {
			int _type = T_BIGINT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:316:9: ( B I G I N T )
			// Meta.g:316:11: B I G I N T
			{
			mB(); 

			mI(); 

			mG(); 

			mI(); 

			mN(); 

			mT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_BIGINT"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// Meta.g:318:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
			// Meta.g:
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LETTER"

	// $ANTLR start "DIGIT"
	public final void mDIGIT() throws RecognitionException {
		try {
			// Meta.g:319:15: ( '0' .. '9' )
			// Meta.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIGIT"

	// $ANTLR start "QUOTED_LITERAL"
	public final void mQUOTED_LITERAL() throws RecognitionException {
		try {
			int _type = QUOTED_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			int c;


			        StringBuilder sb = new StringBuilder();
			    
			// Meta.g:327:6: ( ( '\"' ) (c=~ ( '\"' ) )* ( '\"' ) | ( '\\'' ) (c=~ ( '\\'' ) )* ( '\\'' ) )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0=='\"') ) {
				alt5=1;
			}
			else if ( (LA5_0=='\'') ) {
				alt5=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// Meta.g:328:7: ( '\"' ) (c=~ ( '\"' ) )* ( '\"' )
					{
					// Meta.g:328:7: ( '\"' )
					// Meta.g:328:8: '\"'
					{
					match('\"'); 
					}

					// Meta.g:328:15: (c=~ ( '\"' ) )*
					loop3:
					while (true) {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( ((LA3_0 >= '\u0000' && LA3_0 <= '!')||(LA3_0 >= '#' && LA3_0 <= '\uFFFF')) ) {
							alt3=1;
						}

						switch (alt3) {
						case 1 :
							// Meta.g:328:16: c=~ ( '\"' )
							{
							c= input.LA(1);
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							 sb.appendCodePoint(c);
							}
							break;

						default :
							break loop3;
						}
					}

					// Meta.g:328:54: ( '\"' )
					// Meta.g:328:55: '\"'
					{
					match('\"'); 
					}

					}
					break;
				case 2 :
					// Meta.g:329:7: ( '\\'' ) (c=~ ( '\\'' ) )* ( '\\'' )
					{
					// Meta.g:329:7: ( '\\'' )
					// Meta.g:329:8: '\\''
					{
					match('\''); 
					}

					// Meta.g:329:14: (c=~ ( '\\'' ) )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( ((LA4_0 >= '\u0000' && LA4_0 <= '&')||(LA4_0 >= '(' && LA4_0 <= '\uFFFF')) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// Meta.g:329:15: c=~ ( '\\'' )
							{
							c= input.LA(1);
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							 sb.appendCodePoint(c);
							}
							break;

						default :
							break loop4;
						}
					}

					// Meta.g:329:53: ( '\\'' )
					// Meta.g:329:54: '\\''
					{
					match('\''); 
					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;

			        setText(sb.toString());
			    
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "QUOTED_LITERAL"

	// $ANTLR start "T_CONSTANT"
	public final void mT_CONSTANT() throws RecognitionException {
		try {
			int _type = T_CONSTANT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:332:11: ( ( '-' )? ( DIGIT )+ )
			// Meta.g:332:13: ( '-' )? ( DIGIT )+
			{
			// Meta.g:332:13: ( '-' )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0=='-') ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// Meta.g:332:13: '-'
					{
					match('-'); 
					}
					break;

			}

			// Meta.g:332:18: ( DIGIT )+
			int cnt7=0;
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( ((LA7_0 >= '0' && LA7_0 <= '9')) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt7 >= 1 ) break loop7;
					EarlyExitException eee = new EarlyExitException(7, input);
					throw eee;
				}
				cnt7++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CONSTANT"

	// $ANTLR start "T_IDENT"
	public final void mT_IDENT() throws RecognitionException {
		try {
			int _type = T_IDENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:334:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:334:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:334:17: ( LETTER | DIGIT | '_' )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( ((LA8_0 >= '0' && LA8_0 <= '9')||(LA8_0 >= 'A' && LA8_0 <= 'Z')||LA8_0=='_'||(LA8_0 >= 'a' && LA8_0 <= 'z')) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop8;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_IDENT"

	// $ANTLR start "T_KS_AND_TN"
	public final void mT_KS_AND_TN() throws RecognitionException {
		try {
			int _type = T_KS_AND_TN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:336:12: ( T_IDENT ( POINT T_IDENT )? )
			// Meta.g:336:14: T_IDENT ( POINT T_IDENT )?
			{
			mT_IDENT(); 

			// Meta.g:336:22: ( POINT T_IDENT )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0=='.') ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// Meta.g:336:23: POINT T_IDENT
					{
					mPOINT(); 

					mT_IDENT(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_KS_AND_TN"

	// $ANTLR start "T_CTLG_TBL_COL"
	public final void mT_CTLG_TBL_COL() throws RecognitionException {
		try {
			int _type = T_CTLG_TBL_COL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:338:15: ( T_IDENT ( POINT T_IDENT ( POINT T_IDENT )? )? )
			// Meta.g:338:17: T_IDENT ( POINT T_IDENT ( POINT T_IDENT )? )?
			{
			mT_IDENT(); 

			// Meta.g:338:25: ( POINT T_IDENT ( POINT T_IDENT )? )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0=='.') ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// Meta.g:338:26: POINT T_IDENT ( POINT T_IDENT )?
					{
					mPOINT(); 

					mT_IDENT(); 

					// Meta.g:338:40: ( POINT T_IDENT )?
					int alt10=2;
					int LA10_0 = input.LA(1);
					if ( (LA10_0=='.') ) {
						alt10=1;
					}
					switch (alt10) {
						case 1 :
							// Meta.g:338:41: POINT T_IDENT
							{
							mPOINT(); 

							mT_IDENT(); 

							}
							break;

					}

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_CTLG_TBL_COL"

	// $ANTLR start "T_FLOAT"
	public final void mT_FLOAT() throws RecognitionException {
		try {
			int _type = T_FLOAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:340:8: ( ( '-' )? ( ( '0' .. '9' )+ POINT ( '0' .. '9' )+ ( EXPONENT )? | POINT ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT ) )
			// Meta.g:340:10: ( '-' )? ( ( '0' .. '9' )+ POINT ( '0' .. '9' )+ ( EXPONENT )? | POINT ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
			{
			// Meta.g:340:10: ( '-' )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0=='-') ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// Meta.g:340:10: '-'
					{
					match('-'); 
					}
					break;

			}

			// Meta.g:340:15: ( ( '0' .. '9' )+ POINT ( '0' .. '9' )+ ( EXPONENT )? | POINT ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
			int alt19=3;
			alt19 = dfa19.predict(input);
			switch (alt19) {
				case 1 :
					// Meta.g:340:16: ( '0' .. '9' )+ POINT ( '0' .. '9' )+ ( EXPONENT )?
					{
					// Meta.g:340:16: ( '0' .. '9' )+
					int cnt13=0;
					loop13:
					while (true) {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( ((LA13_0 >= '0' && LA13_0 <= '9')) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt13 >= 1 ) break loop13;
							EarlyExitException eee = new EarlyExitException(13, input);
							throw eee;
						}
						cnt13++;
					}

					mPOINT(); 

					// Meta.g:340:34: ( '0' .. '9' )+
					int cnt14=0;
					loop14:
					while (true) {
						int alt14=2;
						int LA14_0 = input.LA(1);
						if ( ((LA14_0 >= '0' && LA14_0 <= '9')) ) {
							alt14=1;
						}

						switch (alt14) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt14 >= 1 ) break loop14;
							EarlyExitException eee = new EarlyExitException(14, input);
							throw eee;
						}
						cnt14++;
					}

					// Meta.g:340:46: ( EXPONENT )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0=='E'||LA15_0=='e') ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// Meta.g:340:46: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:340:58: POINT ( '0' .. '9' )+ ( EXPONENT )?
					{
					mPOINT(); 

					// Meta.g:340:64: ( '0' .. '9' )+
					int cnt16=0;
					loop16:
					while (true) {
						int alt16=2;
						int LA16_0 = input.LA(1);
						if ( ((LA16_0 >= '0' && LA16_0 <= '9')) ) {
							alt16=1;
						}

						switch (alt16) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt16 >= 1 ) break loop16;
							EarlyExitException eee = new EarlyExitException(16, input);
							throw eee;
						}
						cnt16++;
					}

					// Meta.g:340:76: ( EXPONENT )?
					int alt17=2;
					int LA17_0 = input.LA(1);
					if ( (LA17_0=='E'||LA17_0=='e') ) {
						alt17=1;
					}
					switch (alt17) {
						case 1 :
							// Meta.g:340:76: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 3 :
					// Meta.g:340:88: ( '0' .. '9' )+ EXPONENT
					{
					// Meta.g:340:88: ( '0' .. '9' )+
					int cnt18=0;
					loop18:
					while (true) {
						int alt18=2;
						int LA18_0 = input.LA(1);
						if ( ((LA18_0 >= '0' && LA18_0 <= '9')) ) {
							alt18=1;
						}

						switch (alt18) {
						case 1 :
							// Meta.g:
							{
							if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt18 >= 1 ) break loop18;
							EarlyExitException eee = new EarlyExitException(18, input);
							throw eee;
						}
						cnt18++;
					}

					mEXPONENT(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FLOAT"

	// $ANTLR start "T_TERM"
	public final void mT_TERM() throws RecognitionException {
		try {
			int _type = T_TERM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:342:7: ( ( LETTER | DIGIT | '_' | POINT )+ )
			// Meta.g:342:9: ( LETTER | DIGIT | '_' | POINT )+
			{
			// Meta.g:342:9: ( LETTER | DIGIT | '_' | POINT )+
			int cnt20=0;
			loop20:
			while (true) {
				int alt20=2;
				int LA20_0 = input.LA(1);
				if ( (LA20_0=='.'||(LA20_0 >= '0' && LA20_0 <= '9')||(LA20_0 >= 'A' && LA20_0 <= 'Z')||LA20_0=='_'||(LA20_0 >= 'a' && LA20_0 <= 'z')) ) {
					alt20=1;
				}

				switch (alt20) {
				case 1 :
					// Meta.g:
					{
					if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt20 >= 1 ) break loop20;
					EarlyExitException eee = new EarlyExitException(20, input);
					throw eee;
				}
				cnt20++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_TERM"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:1211:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:1211:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:1211:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			int cnt21=0;
			loop21:
			while (true) {
				int alt21=2;
				int LA21_0 = input.LA(1);
				if ( ((LA21_0 >= '\t' && LA21_0 <= '\n')||LA21_0=='\r'||LA21_0==' ') ) {
					alt21=1;
				}

				switch (alt21) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt21 >= 1 ) break loop21;
					EarlyExitException eee = new EarlyExitException(21, input);
					throw eee;
				}
				cnt21++;
			}

			 
			        _channel = HIDDEN; 
			    
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// Meta.g:1:8: ( T_DESCRIBE | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_KEYSPACES | T_NOT | T_WITH | T_DROP | T_TABLE | T_TABLES | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTER | T_CLUSTERS | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_WHERE | T_IN | T_FROM | T_DELETE | T_WINDOW | T_LAST | T_ROWS | T_INNER | T_JOIN | T_BY | T_LIMIT | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_EPHEMERAL | T_AT | T_CATALOG | T_CATALOGS | T_DATASTORE | T_DATASTORES | T_CONNECTOR | T_CONNECTORS | T_SEMICOLON | T_EQUAL | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_PARENTHESIS | T_END_PARENTHESIS | T_QUOTE | T_SINGLE_QUOTE | T_DEFAULT | T_LUCENE | T_CUSTOM | T_FULL_TEXT | T_START_BRACKET | T_END_BRACKET | T_PLUS | T_SUBTRACT | T_SLASH | T_INTERROGATION | T_ASTERISK | T_GROUP | T_AGGREGATION | T_SUM | T_MAX | T_MIN | T_AVG | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_TOKEN | T_MATCH | T_SEC | T_SECS | T_SECOND | T_SECONDS | T_MINS | T_MINUTE | T_MINUTES | T_HOUR | T_HOURS | T_DAY | T_DAYS | T_NULL | T_ATTACH | T_DETACH | T_TO | T_DOUBLE | T_MAP | T_INT | T_INTEGER | T_BOOLEAN | T_VARCHAR | T_TEXT | T_BIGINT | QUOTED_LITERAL | T_CONSTANT | T_IDENT | T_KS_AND_TN | T_CTLG_TBL_COL | T_FLOAT | T_TERM | WS )
		int alt22=149;
		alt22 = dfa22.predict(input);
		switch (alt22) {
			case 1 :
				// Meta.g:1:10: T_DESCRIBE
				{
				mT_DESCRIBE(); 

				}
				break;
			case 2 :
				// Meta.g:1:21: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 3 :
				// Meta.g:1:32: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 4 :
				// Meta.g:1:41: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 5 :
				// Meta.g:1:49: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 6 :
				// Meta.g:1:60: T_KEYSPACES
				{
				mT_KEYSPACES(); 

				}
				break;
			case 7 :
				// Meta.g:1:72: T_NOT
				{
				mT_NOT(); 

				}
				break;
			case 8 :
				// Meta.g:1:78: T_WITH
				{
				mT_WITH(); 

				}
				break;
			case 9 :
				// Meta.g:1:85: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 10 :
				// Meta.g:1:92: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 11 :
				// Meta.g:1:100: T_TABLES
				{
				mT_TABLES(); 

				}
				break;
			case 12 :
				// Meta.g:1:109: T_IF
				{
				mT_IF(); 

				}
				break;
			case 13 :
				// Meta.g:1:114: T_EXISTS
				{
				mT_EXISTS(); 

				}
				break;
			case 14 :
				// Meta.g:1:123: T_AND
				{
				mT_AND(); 

				}
				break;
			case 15 :
				// Meta.g:1:129: T_USE
				{
				mT_USE(); 

				}
				break;
			case 16 :
				// Meta.g:1:135: T_SET
				{
				mT_SET(); 

				}
				break;
			case 17 :
				// Meta.g:1:141: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 18 :
				// Meta.g:1:151: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 19 :
				// Meta.g:1:163: T_TRUE
				{
				mT_TRUE(); 

				}
				break;
			case 20 :
				// Meta.g:1:170: T_FALSE
				{
				mT_FALSE(); 

				}
				break;
			case 21 :
				// Meta.g:1:178: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 22 :
				// Meta.g:1:192: T_ALL
				{
				mT_ALL(); 

				}
				break;
			case 23 :
				// Meta.g:1:198: T_ANY
				{
				mT_ANY(); 

				}
				break;
			case 24 :
				// Meta.g:1:204: T_QUORUM
				{
				mT_QUORUM(); 

				}
				break;
			case 25 :
				// Meta.g:1:213: T_ONE
				{
				mT_ONE(); 

				}
				break;
			case 26 :
				// Meta.g:1:219: T_TWO
				{
				mT_TWO(); 

				}
				break;
			case 27 :
				// Meta.g:1:225: T_THREE
				{
				mT_THREE(); 

				}
				break;
			case 28 :
				// Meta.g:1:233: T_EACH_QUORUM
				{
				mT_EACH_QUORUM(); 

				}
				break;
			case 29 :
				// Meta.g:1:247: T_LOCAL_ONE
				{
				mT_LOCAL_ONE(); 

				}
				break;
			case 30 :
				// Meta.g:1:259: T_LOCAL_QUORUM
				{
				mT_LOCAL_QUORUM(); 

				}
				break;
			case 31 :
				// Meta.g:1:274: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 32 :
				// Meta.g:1:284: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 33 :
				// Meta.g:1:291: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 34 :
				// Meta.g:1:297: T_INDEX
				{
				mT_INDEX(); 

				}
				break;
			case 35 :
				// Meta.g:1:305: T_LIST
				{
				mT_LIST(); 

				}
				break;
			case 36 :
				// Meta.g:1:312: T_REMOVE
				{
				mT_REMOVE(); 

				}
				break;
			case 37 :
				// Meta.g:1:321: T_UDF
				{
				mT_UDF(); 

				}
				break;
			case 38 :
				// Meta.g:1:327: T_PROCESS
				{
				mT_PROCESS(); 

				}
				break;
			case 39 :
				// Meta.g:1:337: T_TRIGGER
				{
				mT_TRIGGER(); 

				}
				break;
			case 40 :
				// Meta.g:1:347: T_STOP
				{
				mT_STOP(); 

				}
				break;
			case 41 :
				// Meta.g:1:354: T_ON
				{
				mT_ON(); 

				}
				break;
			case 42 :
				// Meta.g:1:359: T_USING
				{
				mT_USING(); 

				}
				break;
			case 43 :
				// Meta.g:1:367: T_TYPE
				{
				mT_TYPE(); 

				}
				break;
			case 44 :
				// Meta.g:1:374: T_ADD
				{
				mT_ADD(); 

				}
				break;
			case 45 :
				// Meta.g:1:380: T_PRIMARY
				{
				mT_PRIMARY(); 

				}
				break;
			case 46 :
				// Meta.g:1:390: T_KEY
				{
				mT_KEY(); 

				}
				break;
			case 47 :
				// Meta.g:1:396: T_INSERT
				{
				mT_INSERT(); 

				}
				break;
			case 48 :
				// Meta.g:1:405: T_INTO
				{
				mT_INTO(); 

				}
				break;
			case 49 :
				// Meta.g:1:412: T_COMPACT
				{
				mT_COMPACT(); 

				}
				break;
			case 50 :
				// Meta.g:1:422: T_STORAGE
				{
				mT_STORAGE(); 

				}
				break;
			case 51 :
				// Meta.g:1:432: T_CLUSTER
				{
				mT_CLUSTER(); 

				}
				break;
			case 52 :
				// Meta.g:1:442: T_CLUSTERS
				{
				mT_CLUSTERS(); 

				}
				break;
			case 53 :
				// Meta.g:1:453: T_CLUSTERING
				{
				mT_CLUSTERING(); 

				}
				break;
			case 54 :
				// Meta.g:1:466: T_ORDER
				{
				mT_ORDER(); 

				}
				break;
			case 55 :
				// Meta.g:1:474: T_SELECT
				{
				mT_SELECT(); 

				}
				break;
			case 56 :
				// Meta.g:1:483: T_VALUES
				{
				mT_VALUES(); 

				}
				break;
			case 57 :
				// Meta.g:1:492: T_UPDATE
				{
				mT_UPDATE(); 

				}
				break;
			case 58 :
				// Meta.g:1:501: T_WHERE
				{
				mT_WHERE(); 

				}
				break;
			case 59 :
				// Meta.g:1:509: T_IN
				{
				mT_IN(); 

				}
				break;
			case 60 :
				// Meta.g:1:514: T_FROM
				{
				mT_FROM(); 

				}
				break;
			case 61 :
				// Meta.g:1:521: T_DELETE
				{
				mT_DELETE(); 

				}
				break;
			case 62 :
				// Meta.g:1:530: T_WINDOW
				{
				mT_WINDOW(); 

				}
				break;
			case 63 :
				// Meta.g:1:539: T_LAST
				{
				mT_LAST(); 

				}
				break;
			case 64 :
				// Meta.g:1:546: T_ROWS
				{
				mT_ROWS(); 

				}
				break;
			case 65 :
				// Meta.g:1:553: T_INNER
				{
				mT_INNER(); 

				}
				break;
			case 66 :
				// Meta.g:1:561: T_JOIN
				{
				mT_JOIN(); 

				}
				break;
			case 67 :
				// Meta.g:1:568: T_BY
				{
				mT_BY(); 

				}
				break;
			case 68 :
				// Meta.g:1:573: T_LIMIT
				{
				mT_LIMIT(); 

				}
				break;
			case 69 :
				// Meta.g:1:581: T_DISABLE
				{
				mT_DISABLE(); 

				}
				break;
			case 70 :
				// Meta.g:1:591: T_DISTINCT
				{
				mT_DISTINCT(); 

				}
				break;
			case 71 :
				// Meta.g:1:602: T_COUNT
				{
				mT_COUNT(); 

				}
				break;
			case 72 :
				// Meta.g:1:610: T_AS
				{
				mT_AS(); 

				}
				break;
			case 73 :
				// Meta.g:1:615: T_BETWEEN
				{
				mT_BETWEEN(); 

				}
				break;
			case 74 :
				// Meta.g:1:625: T_ASC
				{
				mT_ASC(); 

				}
				break;
			case 75 :
				// Meta.g:1:631: T_DESC
				{
				mT_DESC(); 

				}
				break;
			case 76 :
				// Meta.g:1:638: T_LIKE
				{
				mT_LIKE(); 

				}
				break;
			case 77 :
				// Meta.g:1:645: T_EPHEMERAL
				{
				mT_EPHEMERAL(); 

				}
				break;
			case 78 :
				// Meta.g:1:657: T_AT
				{
				mT_AT(); 

				}
				break;
			case 79 :
				// Meta.g:1:662: T_CATALOG
				{
				mT_CATALOG(); 

				}
				break;
			case 80 :
				// Meta.g:1:672: T_CATALOGS
				{
				mT_CATALOGS(); 

				}
				break;
			case 81 :
				// Meta.g:1:683: T_DATASTORE
				{
				mT_DATASTORE(); 

				}
				break;
			case 82 :
				// Meta.g:1:695: T_DATASTORES
				{
				mT_DATASTORES(); 

				}
				break;
			case 83 :
				// Meta.g:1:708: T_CONNECTOR
				{
				mT_CONNECTOR(); 

				}
				break;
			case 84 :
				// Meta.g:1:720: T_CONNECTORS
				{
				mT_CONNECTORS(); 

				}
				break;
			case 85 :
				// Meta.g:1:733: T_SEMICOLON
				{
				mT_SEMICOLON(); 

				}
				break;
			case 86 :
				// Meta.g:1:745: T_EQUAL
				{
				mT_EQUAL(); 

				}
				break;
			case 87 :
				// Meta.g:1:753: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 

				}
				break;
			case 88 :
				// Meta.g:1:770: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 

				}
				break;
			case 89 :
				// Meta.g:1:785: T_COLON
				{
				mT_COLON(); 

				}
				break;
			case 90 :
				// Meta.g:1:793: T_COMMA
				{
				mT_COMMA(); 

				}
				break;
			case 91 :
				// Meta.g:1:801: T_START_PARENTHESIS
				{
				mT_START_PARENTHESIS(); 

				}
				break;
			case 92 :
				// Meta.g:1:821: T_END_PARENTHESIS
				{
				mT_END_PARENTHESIS(); 

				}
				break;
			case 93 :
				// Meta.g:1:839: T_QUOTE
				{
				mT_QUOTE(); 

				}
				break;
			case 94 :
				// Meta.g:1:847: T_SINGLE_QUOTE
				{
				mT_SINGLE_QUOTE(); 

				}
				break;
			case 95 :
				// Meta.g:1:862: T_DEFAULT
				{
				mT_DEFAULT(); 

				}
				break;
			case 96 :
				// Meta.g:1:872: T_LUCENE
				{
				mT_LUCENE(); 

				}
				break;
			case 97 :
				// Meta.g:1:881: T_CUSTOM
				{
				mT_CUSTOM(); 

				}
				break;
			case 98 :
				// Meta.g:1:890: T_FULL_TEXT
				{
				mT_FULL_TEXT(); 

				}
				break;
			case 99 :
				// Meta.g:1:902: T_START_BRACKET
				{
				mT_START_BRACKET(); 

				}
				break;
			case 100 :
				// Meta.g:1:918: T_END_BRACKET
				{
				mT_END_BRACKET(); 

				}
				break;
			case 101 :
				// Meta.g:1:932: T_PLUS
				{
				mT_PLUS(); 

				}
				break;
			case 102 :
				// Meta.g:1:939: T_SUBTRACT
				{
				mT_SUBTRACT(); 

				}
				break;
			case 103 :
				// Meta.g:1:950: T_SLASH
				{
				mT_SLASH(); 

				}
				break;
			case 104 :
				// Meta.g:1:958: T_INTERROGATION
				{
				mT_INTERROGATION(); 

				}
				break;
			case 105 :
				// Meta.g:1:974: T_ASTERISK
				{
				mT_ASTERISK(); 

				}
				break;
			case 106 :
				// Meta.g:1:985: T_GROUP
				{
				mT_GROUP(); 

				}
				break;
			case 107 :
				// Meta.g:1:993: T_AGGREGATION
				{
				mT_AGGREGATION(); 

				}
				break;
			case 108 :
				// Meta.g:1:1007: T_SUM
				{
				mT_SUM(); 

				}
				break;
			case 109 :
				// Meta.g:1:1013: T_MAX
				{
				mT_MAX(); 

				}
				break;
			case 110 :
				// Meta.g:1:1019: T_MIN
				{
				mT_MIN(); 

				}
				break;
			case 111 :
				// Meta.g:1:1025: T_AVG
				{
				mT_AVG(); 

				}
				break;
			case 112 :
				// Meta.g:1:1031: T_GT
				{
				mT_GT(); 

				}
				break;
			case 113 :
				// Meta.g:1:1036: T_LT
				{
				mT_LT(); 

				}
				break;
			case 114 :
				// Meta.g:1:1041: T_GTE
				{
				mT_GTE(); 

				}
				break;
			case 115 :
				// Meta.g:1:1047: T_LTE
				{
				mT_LTE(); 

				}
				break;
			case 116 :
				// Meta.g:1:1053: T_NOT_EQUAL
				{
				mT_NOT_EQUAL(); 

				}
				break;
			case 117 :
				// Meta.g:1:1065: T_TOKEN
				{
				mT_TOKEN(); 

				}
				break;
			case 118 :
				// Meta.g:1:1073: T_MATCH
				{
				mT_MATCH(); 

				}
				break;
			case 119 :
				// Meta.g:1:1081: T_SEC
				{
				mT_SEC(); 

				}
				break;
			case 120 :
				// Meta.g:1:1087: T_SECS
				{
				mT_SECS(); 

				}
				break;
			case 121 :
				// Meta.g:1:1094: T_SECOND
				{
				mT_SECOND(); 

				}
				break;
			case 122 :
				// Meta.g:1:1103: T_SECONDS
				{
				mT_SECONDS(); 

				}
				break;
			case 123 :
				// Meta.g:1:1113: T_MINS
				{
				mT_MINS(); 

				}
				break;
			case 124 :
				// Meta.g:1:1120: T_MINUTE
				{
				mT_MINUTE(); 

				}
				break;
			case 125 :
				// Meta.g:1:1129: T_MINUTES
				{
				mT_MINUTES(); 

				}
				break;
			case 126 :
				// Meta.g:1:1139: T_HOUR
				{
				mT_HOUR(); 

				}
				break;
			case 127 :
				// Meta.g:1:1146: T_HOURS
				{
				mT_HOURS(); 

				}
				break;
			case 128 :
				// Meta.g:1:1154: T_DAY
				{
				mT_DAY(); 

				}
				break;
			case 129 :
				// Meta.g:1:1160: T_DAYS
				{
				mT_DAYS(); 

				}
				break;
			case 130 :
				// Meta.g:1:1167: T_NULL
				{
				mT_NULL(); 

				}
				break;
			case 131 :
				// Meta.g:1:1174: T_ATTACH
				{
				mT_ATTACH(); 

				}
				break;
			case 132 :
				// Meta.g:1:1183: T_DETACH
				{
				mT_DETACH(); 

				}
				break;
			case 133 :
				// Meta.g:1:1192: T_TO
				{
				mT_TO(); 

				}
				break;
			case 134 :
				// Meta.g:1:1197: T_DOUBLE
				{
				mT_DOUBLE(); 

				}
				break;
			case 135 :
				// Meta.g:1:1206: T_MAP
				{
				mT_MAP(); 

				}
				break;
			case 136 :
				// Meta.g:1:1212: T_INT
				{
				mT_INT(); 

				}
				break;
			case 137 :
				// Meta.g:1:1218: T_INTEGER
				{
				mT_INTEGER(); 

				}
				break;
			case 138 :
				// Meta.g:1:1228: T_BOOLEAN
				{
				mT_BOOLEAN(); 

				}
				break;
			case 139 :
				// Meta.g:1:1238: T_VARCHAR
				{
				mT_VARCHAR(); 

				}
				break;
			case 140 :
				// Meta.g:1:1248: T_TEXT
				{
				mT_TEXT(); 

				}
				break;
			case 141 :
				// Meta.g:1:1255: T_BIGINT
				{
				mT_BIGINT(); 

				}
				break;
			case 142 :
				// Meta.g:1:1264: QUOTED_LITERAL
				{
				mQUOTED_LITERAL(); 

				}
				break;
			case 143 :
				// Meta.g:1:1279: T_CONSTANT
				{
				mT_CONSTANT(); 

				}
				break;
			case 144 :
				// Meta.g:1:1290: T_IDENT
				{
				mT_IDENT(); 

				}
				break;
			case 145 :
				// Meta.g:1:1298: T_KS_AND_TN
				{
				mT_KS_AND_TN(); 

				}
				break;
			case 146 :
				// Meta.g:1:1310: T_CTLG_TBL_COL
				{
				mT_CTLG_TBL_COL(); 

				}
				break;
			case 147 :
				// Meta.g:1:1325: T_FLOAT
				{
				mT_FLOAT(); 

				}
				break;
			case 148 :
				// Meta.g:1:1333: T_TERM
				{
				mT_TERM(); 

				}
				break;
			case 149 :
				// Meta.g:1:1340: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA19 dfa19 = new DFA19(this);
	protected DFA22 dfa22 = new DFA22(this);
	static final String DFA19_eotS =
		"\5\uffff";
	static final String DFA19_eofS =
		"\5\uffff";
	static final String DFA19_minS =
		"\2\56\3\uffff";
	static final String DFA19_maxS =
		"\1\71\1\145\3\uffff";
	static final String DFA19_acceptS =
		"\2\uffff\1\2\1\1\1\3";
	static final String DFA19_specialS =
		"\5\uffff}>";
	static final String[] DFA19_transitionS = {
			"\1\2\1\uffff\12\1",
			"\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
			"",
			"",
			""
	};

	static final short[] DFA19_eot = DFA.unpackEncodedString(DFA19_eotS);
	static final short[] DFA19_eof = DFA.unpackEncodedString(DFA19_eofS);
	static final char[] DFA19_min = DFA.unpackEncodedStringToUnsignedChars(DFA19_minS);
	static final char[] DFA19_max = DFA.unpackEncodedStringToUnsignedChars(DFA19_maxS);
	static final short[] DFA19_accept = DFA.unpackEncodedString(DFA19_acceptS);
	static final short[] DFA19_special = DFA.unpackEncodedString(DFA19_specialS);
	static final short[][] DFA19_transition;

	static {
		int numStates = DFA19_transitionS.length;
		DFA19_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA19_transition[i] = DFA.unpackEncodedString(DFA19_transitionS[i]);
		}
	}

	protected class DFA19 extends DFA {

		public DFA19(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 19;
			this.eot = DFA19_eot;
			this.eof = DFA19_eof;
			this.min = DFA19_min;
			this.max = DFA19_max;
			this.accept = DFA19_accept;
			this.special = DFA19_special;
			this.transition = DFA19_transition;
		}
		@Override
		public String getDescription() {
			return "340:15: ( ( '0' .. '9' )+ POINT ( '0' .. '9' )+ ( EXPONENT )? | POINT ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )";
		}
	}

	static final String DFA22_eotS =
		"\1\uffff\24\61\11\uffff\1\162\1\164\3\uffff\1\165\3\uffff\2\61\1\174\1"+
		"\177\1\61\1\u0081\1\61\1\57\3\uffff\6\61\1\57\5\61\1\u0095\11\61\1\u00a5"+
		"\10\61\1\u00b0\1\u00b1\12\61\1\u00c4\20\61\1\u00da\3\61\4\uffff\1\u0081"+
		"\1\uffff\3\61\5\uffff\1\61\1\uffff\2\57\1\167\7\61\1\u00ef\1\61\1\u00f2"+
		"\3\61\1\u00f9\2\61\1\uffff\12\61\1\u0107\1\u0108\1\61\1\u010a\1\u010b"+
		"\1\uffff\1\u010c\1\61\1\u010e\1\61\1\u0110\1\u0112\4\61\2\uffff\2\61\1"+
		"\u0119\5\61\1\u0121\1\61\1\u0123\1\61\1\u0125\1\61\1\u0127\1\61\1\u012c"+
		"\1\61\1\uffff\1\u012e\2\61\1\u0131\21\61\1\uffff\4\61\1\u0147\1\61\1\u0149"+
		"\1\u014a\1\61\2\167\1\57\1\u0150\3\61\1\u0155\3\61\1\uffff\1\u0159\1\61"+
		"\1\uffff\1\u00f2\1\57\1\61\1\u015d\2\61\1\uffff\1\61\1\u0161\1\61\1\u0163"+
		"\11\61\2\uffff\1\61\3\uffff\1\61\1\uffff\1\61\1\uffff\1\61\1\uffff\1\u0171"+
		"\1\u0172\4\61\1\uffff\1\u0177\6\61\1\uffff\1\61\1\uffff\1\61\1\uffff\1"+
		"\61\1\uffff\1\u0181\1\61\1\u0183\1\61\1\uffff\1\61\1\uffff\2\61\1\uffff"+
		"\1\u0188\3\61\1\u018c\1\61\1\u018e\1\u018f\1\61\1\u0191\3\61\1\u0195\2"+
		"\61\1\u0198\4\61\1\uffff\1\61\2\uffff\1\u019e\1\61\1\u01a0\1\57\1\167"+
		"\1\uffff\4\61\1\uffff\3\61\1\uffff\1\61\1\u01ab\1\61\1\uffff\1\61\1\u01af"+
		"\1\u01b1\1\uffff\1\u01b2\1\uffff\4\61\1\u01b7\3\61\1\u01bb\4\61\2\uffff"+
		"\1\61\1\u01c1\1\u01c2\1\61\1\uffff\1\61\1\u01c5\4\61\1\u01ca\2\61\1\uffff"+
		"\1\61\1\uffff\2\61\1\u01d0\1\u01d1\1\uffff\3\61\1\uffff\1\u01d5\2\uffff"+
		"\1\61\1\uffff\3\61\1\uffff\2\61\1\uffff\3\61\1\u01df\1\u01e0\1\uffff\1"+
		"\61\1\uffff\1\u01e2\1\167\1\61\1\u01e4\1\61\1\u01e6\3\61\1\u01ea\1\uffff"+
		"\1\u01ab\2\61\1\uffff\1\u01ed\2\uffff\1\u01ee\3\61\1\uffff\2\61\1\u01f4"+
		"\1\uffff\2\61\1\u01f7\1\61\1\u01f9\2\uffff\1\u01fa\1\61\1\uffff\1\u01fc"+
		"\3\61\1\uffff\1\u0200\1\u0201\1\u0202\2\61\2\uffff\1\61\1\u0207\1\61\1"+
		"\uffff\1\u020a\2\61\1\u020d\1\u020e\3\61\1\u0212\2\uffff\1\u0213\1\uffff"+
		"\1\61\1\uffff\1\u0216\1\uffff\1\u0217\2\61\1\uffff\1\61\1\u021b\2\uffff"+
		"\2\61\1\u021e\1\u021f\1\u0222\1\uffff\2\61\1\uffff\1\61\2\uffff\1\u0227"+
		"\1\uffff\1\u0228\2\61\3\uffff\1\u022b\1\u022c\1\u022d\1\61\1\uffff\2\61"+
		"\1\uffff\1\u0231\1\u0232\2\uffff\1\u0233\1\u0234\1\u0235\2\uffff\1\u0236"+
		"\1\u0237\2\uffff\1\u0238\1\61\1\u023a\1\uffff\2\61\2\uffff\1\u023d\1\61"+
		"\1\uffff\1\u023f\2\61\1\u0242\2\uffff\2\61\3\uffff\3\61\10\uffff\1\u0249"+
		"\1\uffff\1\61\1\u024c\1\uffff\1\61\1\uffff\1\u024f\1\61\1\uffff\1\u0251"+
		"\1\61\1\u0253\1\u0254\1\u0255\1\61\1\uffff\1\u0257\1\61\1\uffff\1\u0259"+
		"\1\u025a\1\uffff\1\61\1\uffff\1\61\3\uffff\1\61\1\uffff\1\u025e\2\uffff"+
		"\1\u025f\1\u0260\1\61\3\uffff\1\u0262\1\uffff";
	static final String DFA22_eofS =
		"\u0263\uffff";
	static final String DFA22_minS =
		"\1\11\24\56\11\uffff\2\0\3\uffff\1\56\3\uffff\2\56\2\75\3\56\1\60\3\uffff"+
		"\6\56\1\101\71\56\4\uffff\1\56\1\uffff\3\56\5\uffff\1\56\1\uffff\1\60"+
		"\1\53\21\56\1\uffff\17\56\1\uffff\12\56\2\uffff\22\56\1\uffff\25\56\1"+
		"\uffff\13\56\1\53\10\56\1\uffff\2\56\1\uffff\1\56\1\101\4\56\1\uffff\15"+
		"\56\2\uffff\1\56\3\uffff\1\56\1\uffff\1\56\1\uffff\1\56\1\uffff\6\56\1"+
		"\uffff\7\56\1\uffff\1\56\1\uffff\1\56\1\uffff\1\56\1\uffff\4\56\1\uffff"+
		"\1\56\1\uffff\2\56\1\uffff\25\56\1\uffff\1\56\2\uffff\3\56\1\53\1\56\1"+
		"\uffff\4\56\1\uffff\3\56\1\uffff\3\56\1\uffff\3\56\1\uffff\1\56\1\uffff"+
		"\15\56\2\uffff\4\56\1\uffff\11\56\1\uffff\1\56\1\uffff\4\56\1\uffff\3"+
		"\56\1\uffff\1\56\2\uffff\1\56\1\uffff\3\56\1\uffff\2\56\1\uffff\5\56\1"+
		"\uffff\1\56\1\uffff\12\56\1\uffff\3\56\1\uffff\1\56\2\uffff\4\56\1\uffff"+
		"\3\56\1\uffff\5\56\2\uffff\2\56\1\uffff\4\56\1\uffff\5\56\2\uffff\3\56"+
		"\1\uffff\11\56\2\uffff\1\56\1\uffff\1\56\1\uffff\1\56\1\uffff\3\56\1\uffff"+
		"\2\56\2\uffff\5\56\1\uffff\2\56\1\uffff\1\56\2\uffff\1\56\1\uffff\3\56"+
		"\3\uffff\4\56\1\uffff\2\56\1\uffff\2\56\2\uffff\3\56\2\uffff\2\56\2\uffff"+
		"\3\56\1\uffff\2\56\2\uffff\2\56\1\uffff\4\56\2\uffff\2\56\3\uffff\3\56"+
		"\10\uffff\1\56\1\uffff\2\56\1\uffff\1\56\1\uffff\2\56\1\uffff\6\56\1\uffff"+
		"\2\56\1\uffff\2\56\1\uffff\1\56\1\uffff\1\56\3\uffff\1\56\1\uffff\1\56"+
		"\2\uffff\3\56\3\uffff\1\56\1\uffff";
	static final String DFA22_maxS =
		"\1\175\24\172\11\uffff\2\uffff\3\uffff\1\71\3\uffff\2\172\1\75\1\76\3"+
		"\172\1\71\3\uffff\100\172\4\uffff\1\145\1\uffff\3\172\5\uffff\1\172\1"+
		"\uffff\2\71\21\172\1\uffff\17\172\1\uffff\12\172\2\uffff\22\172\1\uffff"+
		"\25\172\1\uffff\13\172\1\71\10\172\1\uffff\2\172\1\uffff\6\172\1\uffff"+
		"\15\172\2\uffff\1\172\3\uffff\1\172\1\uffff\1\172\1\uffff\1\172\1\uffff"+
		"\6\172\1\uffff\7\172\1\uffff\1\172\1\uffff\1\172\1\uffff\1\172\1\uffff"+
		"\4\172\1\uffff\1\172\1\uffff\2\172\1\uffff\25\172\1\uffff\1\172\2\uffff"+
		"\3\172\1\71\1\172\1\uffff\4\172\1\uffff\3\172\1\uffff\3\172\1\uffff\3"+
		"\172\1\uffff\1\172\1\uffff\15\172\2\uffff\4\172\1\uffff\11\172\1\uffff"+
		"\1\172\1\uffff\4\172\1\uffff\3\172\1\uffff\1\172\2\uffff\1\172\1\uffff"+
		"\3\172\1\uffff\2\172\1\uffff\5\172\1\uffff\1\172\1\uffff\12\172\1\uffff"+
		"\3\172\1\uffff\1\172\2\uffff\4\172\1\uffff\3\172\1\uffff\5\172\2\uffff"+
		"\2\172\1\uffff\4\172\1\uffff\5\172\2\uffff\3\172\1\uffff\11\172\2\uffff"+
		"\1\172\1\uffff\1\172\1\uffff\1\172\1\uffff\3\172\1\uffff\2\172\2\uffff"+
		"\5\172\1\uffff\2\172\1\uffff\1\172\2\uffff\1\172\1\uffff\3\172\3\uffff"+
		"\4\172\1\uffff\2\172\1\uffff\2\172\2\uffff\3\172\2\uffff\2\172\2\uffff"+
		"\3\172\1\uffff\2\172\2\uffff\2\172\1\uffff\4\172\2\uffff\2\172\3\uffff"+
		"\3\172\10\uffff\1\172\1\uffff\2\172\1\uffff\1\172\1\uffff\2\172\1\uffff"+
		"\6\172\1\uffff\2\172\1\uffff\2\172\1\uffff\1\172\1\uffff\1\172\3\uffff"+
		"\1\172\1\uffff\1\172\2\uffff\3\172\3\uffff\1\172\1\uffff";
	static final String DFA22_acceptS =
		"\25\uffff\1\116\1\125\1\126\1\127\1\130\1\131\1\132\1\133\1\134\2\uffff"+
		"\1\143\1\144\1\145\1\uffff\1\147\1\150\1\151\10\uffff\1\u0094\1\u0095"+
		"\1\u0090\100\uffff\1\135\1\u008e\1\136\1\146\1\uffff\1\u0093\3\uffff\1"+
		"\162\1\160\1\163\1\164\1\161\1\uffff\1\u008f\23\uffff\1\u0085\17\uffff"+
		"\1\110\12\uffff\1\14\1\73\22\uffff\1\51\25\uffff\1\103\24\uffff\1\u0080"+
		"\2\uffff\1\u0091\6\uffff\1\32\15\uffff\1\26\1\16\1\uffff\1\27\1\54\1\112"+
		"\1\uffff\1\157\1\uffff\1\56\1\uffff\1\7\6\uffff\1\u0088\7\uffff\1\17\1"+
		"\uffff\1\45\1\uffff\1\20\1\uffff\1\167\4\uffff\1\154\1\uffff\1\31\2\uffff"+
		"\1\41\25\uffff\1\155\1\uffff\1\u0087\1\156\5\uffff\1\113\4\uffff\1\11"+
		"\3\uffff\1\u0081\3\uffff\1\23\3\uffff\1\53\1\uffff\1\u008c\15\uffff\1"+
		"\u0082\1\10\4\uffff\1\60\11\uffff\1\170\1\uffff\1\50\4\uffff\1\74\3\uffff"+
		"\1\43\1\uffff\1\114\1\77\1\uffff\1\40\3\uffff\1\100\2\uffff\1\102\5\uffff"+
		"\1\173\1\uffff\1\176\12\uffff\1\u0092\3\uffff\1\12\1\uffff\1\33\1\165"+
		"\4\uffff\1\107\3\uffff\1\4\5\uffff\1\72\1\42\2\uffff\1\101\4\uffff\1\52"+
		"\5\uffff\1\66\1\24\3\uffff\1\104\11\uffff\1\152\1\166\1\uffff\1\177\1"+
		"\uffff\1\75\1\uffff\1\u0084\3\uffff\1\u0086\2\uffff\1\13\1\3\5\uffff\1"+
		"\141\2\uffff\1\u0083\1\uffff\1\76\1\57\1\uffff\1\15\3\uffff\1\71\1\67"+
		"\1\171\4\uffff\1\30\2\uffff\1\140\2\uffff\1\44\1\70\3\uffff\1\u008d\1"+
		"\174\2\uffff\1\137\1\105\3\uffff\1\47\2\uffff\1\61\1\63\2\uffff\1\117"+
		"\4\uffff\1\u0089\1\37\2\uffff\1\172\1\62\1\21\3\uffff\1\46\1\55\1\u008b"+
		"\1\111\1\u008a\1\175\1\1\1\106\1\uffff\1\2\2\uffff\1\64\1\uffff\1\120"+
		"\2\uffff\1\5\6\uffff\1\121\2\uffff\1\123\2\uffff\1\22\1\uffff\1\6\1\uffff"+
		"\1\115\1\142\1\35\1\uffff\1\122\1\uffff\1\124\1\65\3\uffff\1\25\1\153"+
		"\1\34\1\uffff\1\36";
	static final String DFA22_specialS =
		"\36\uffff\1\1\1\0\u0243\uffff}>";
	static final String[] DFA22_transitionS = {
			"\2\60\2\uffff\1\60\22\uffff\1\60\1\uffff\1\36\4\uffff\1\37\1\34\1\35"+
			"\1\46\1\42\1\33\1\43\1\56\1\44\12\54\1\32\1\26\1\52\1\27\1\51\1\45\1"+
			"\25\1\4\1\24\1\3\1\1\1\11\1\15\1\47\1\53\1\10\1\23\1\5\1\17\1\50\1\6"+
			"\1\14\1\20\1\16\1\21\1\13\1\2\1\12\1\22\1\7\3\55\1\40\1\uffff\1\41\1"+
			"\uffff\1\57\1\uffff\1\4\1\24\1\3\1\1\1\11\1\15\1\47\1\53\1\10\1\23\1"+
			"\5\1\17\1\50\1\6\1\14\1\20\1\16\1\21\1\13\1\2\1\12\1\22\1\7\3\55\1\30"+
			"\1\uffff\1\31",
			"\1\70\1\uffff\12\67\7\uffff\1\65\3\67\1\62\3\67\1\64\5\67\1\66\2\67"+
			"\1\63\10\67\4\uffff\1\67\1\uffff\1\65\3\67\1\62\3\67\1\64\5\67\1\66\2"+
			"\67\1\63\10\67",
			"\1\70\1\uffff\12\67\7\uffff\1\72\3\67\1\77\2\67\1\74\6\67\1\76\2\67"+
			"\1\71\4\67\1\73\1\67\1\75\1\67\4\uffff\1\67\1\uffff\1\72\3\67\1\77\2"+
			"\67\1\74\6\67\1\76\2\67\1\71\4\67\1\73\1\67\1\75\1\67",
			"\1\70\1\uffff\12\67\7\uffff\1\103\12\67\1\102\2\67\1\101\2\67\1\100"+
			"\2\67\1\104\5\67\4\uffff\1\67\1\uffff\1\103\12\67\1\102\2\67\1\101\2"+
			"\67\1\100\2\67\1\104\5\67",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\107\2\67\1\111\4\67\1\105\1\67\1"+
			"\106\4\67\1\110\1\113\1\67\1\112\4\67\4\uffff\1\67\1\uffff\3\67\1\107"+
			"\2\67\1\111\4\67\1\105\1\67\1\106\4\67\1\110\1\113\1\67\1\112\4\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\114\25\67\4\uffff\1\67\1\uffff\4"+
			"\67\1\114\25\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\115\5\67\1\116\5\67\4\uffff\1\67"+
			"\1\uffff\16\67\1\115\5\67\1\116\5\67",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\120\1\117\21\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\120\1\117\21\67",
			"\1\70\1\uffff\12\67\7\uffff\5\67\1\121\7\67\1\122\14\67\4\uffff\1\67"+
			"\1\uffff\5\67\1\121\7\67\1\122\14\67",
			"\1\70\1\uffff\12\67\7\uffff\1\124\16\67\1\125\7\67\1\123\2\67\4\uffff"+
			"\1\67\1\uffff\1\124\16\67\1\125\7\67\1\123\2\67",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\127\13\67\1\130\2\67\1\126\7\67\4"+
			"\uffff\1\67\1\uffff\3\67\1\127\13\67\1\130\2\67\1\126\7\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\131\16\67\1\132\1\133\5\67\4\uffff"+
			"\1\67\1\uffff\4\67\1\131\16\67\1\132\1\133\5\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\135\1\67\1\134\1\67\1\136\10\67"+
			"\4\uffff\1\67\1\uffff\15\67\1\135\1\67\1\134\1\67\1\136\10\67",
			"\1\70\1\uffff\12\67\7\uffff\1\137\15\67\1\140\2\67\1\141\2\67\1\142"+
			"\5\67\4\uffff\1\67\1\uffff\1\137\15\67\1\140\2\67\1\141\2\67\1\142\5"+
			"\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\143\5\67\4\uffff\1\67\1\uffff\24"+
			"\67\1\143\5\67",
			"\1\70\1\uffff\12\67\7\uffff\1\146\7\67\1\145\5\67\1\144\5\67\1\147\5"+
			"\67\4\uffff\1\67\1\uffff\1\146\7\67\1\145\5\67\1\144\5\67\1\147\5\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\150\5\67\1\151\10\67\4\uffff\1\67"+
			"\1\uffff\13\67\1\150\5\67\1\151\10\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\152\11\67\1\153\13\67\4\uffff\1\67"+
			"\1\uffff\4\67\1\152\11\67\1\153\13\67",
			"\1\70\1\uffff\12\67\7\uffff\1\154\31\67\4\uffff\1\67\1\uffff\1\154\31"+
			"\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\155\13\67\4\uffff\1\67\1\uffff\16"+
			"\67\1\155\13\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\157\3\67\1\161\5\67\1\160\11\67\1"+
			"\156\1\67\4\uffff\1\67\1\uffff\4\67\1\157\3\67\1\161\5\67\1\160\11\67"+
			"\1\156\1\67",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\0\163",
			"\0\163",
			"",
			"",
			"",
			"\1\167\1\uffff\12\166",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\170\10\67\4\uffff\1\67\1\uffff\21"+
			"\67\1\170\10\67",
			"\1\70\1\uffff\12\67\7\uffff\1\171\7\67\1\172\21\67\4\uffff\1\67\1\uffff"+
			"\1\171\7\67\1\172\21\67",
			"\1\173",
			"\1\175\1\176",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0080\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0080\13\67",
			"\1\u0082\1\uffff\12\54\7\uffff\4\57\1\u0083\25\57\4\uffff\1\57\1\uffff"+
			"\4\57\1\u0083\25\57",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\12\u0084",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\5\67\1\u0087\5\67\1\u0086\6\67\1\u0085\1"+
			"\u0088\6\67\4\uffff\1\67\1\uffff\5\67\1\u0087\5\67\1\u0086\6\67\1\u0085"+
			"\1\u0088\6\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0089\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0089\13\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u008a\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u008a\7\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u008b\4\67\1\u008c\1\67\4\uffff"+
			"\1\67\1\uffff\23\67\1\u008b\4\67\1\u008c\1\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u008d\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u008d\5\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\32\u008e\6\uffff\32\u008e",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u0090\13\67\1\u008f\5\67\4\uffff"+
			"\1\67\1\uffff\10\67\1\u0090\13\67\1\u008f\5\67",
			"\1\70\1\uffff\12\67\7\uffff\1\67\1\u0091\30\67\4\uffff\1\67\1\uffff"+
			"\1\67\1\u0091\30\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0092\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0092\13\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0093\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0093\10\67",
			"\1\70\1\uffff\12\67\7\uffff\17\67\1\u0094\12\67\4\uffff\1\67\1\uffff"+
			"\17\67\1\u0094\12\67",
			"\1\70\1\uffff\12\67\7\uffff\12\67\1\u0096\17\67\4\uffff\1\67\1\uffff"+
			"\12\67\1\u0096\17\67",
			"\1\70\1\uffff\12\67\7\uffff\27\67\1\u0097\2\67\4\uffff\1\67\1\uffff"+
			"\27\67\1\u0097\2\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0098\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0098\25\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u009a\1\u0099\6\67\1\u009b\5\67"+
			"\4\uffff\1\67\1\uffff\14\67\1\u009a\1\u0099\6\67\1\u009b\5\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u009c\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u009c\5\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u009d\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u009d\6\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u009e\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u009e\7\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u00a0\7\67\1\u009f\6\67\4\uffff"+
			"\1\67\1\uffff\13\67\1\u00a0\7\67\1\u009f\6\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u00a2\2\67\1\u00a1\24\67\1\u00a3\1\67"+
			"\4\uffff\1\67\1\uffff\1\u00a2\2\67\1\u00a1\24\67\1\u00a3\1\67",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\u00a4\26\67\4\uffff\1\67\1\uffff"+
			"\3\67\1\u00a4\26\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u00a6\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u00a6\27\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u00a7\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u00a7\23\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u00a8\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u00a8\23\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u00a9\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u00a9\6\67",
			"\1\70\1\uffff\12\67\7\uffff\30\67\1\u00aa\1\67\4\uffff\1\67\1\uffff"+
			"\30\67\1\u00aa\1\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u00ab\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u00ab\6\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u00ac\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u00ac\16\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u00ae\5\67\1\u00ad\6\67\4\uffff"+
			"\1\67\1\uffff\15\67\1\u00ae\5\67\1\u00ad\6\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00af\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u00af\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\u00b2\11\67\1\u00b5\4\67\1\u00b3"+
			"\1\u00b4\6\67\4\uffff\1\67\1\uffff\3\67\1\u00b2\11\67\1\u00b5\4\67\1"+
			"\u00b3\1\u00b4\6\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u00b6\6\67\1\u00b7\12\67\4\uffff"+
			"\1\67\1\uffff\10\67\1\u00b6\6\67\1\u00b7\12\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u00b8\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u00b8\27\67",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\u00b9\22\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\u00b9\22\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00ba\3\67\1\u00bb\21\67\4\uffff"+
			"\1\67\1\uffff\4\67\1\u00ba\3\67\1\u00bb\21\67",
			"\1\70\1\uffff\12\67\7\uffff\5\67\1\u00bc\24\67\4\uffff\1\67\1\uffff"+
			"\5\67\1\u00bc\24\67",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\u00bd\26\67\4\uffff\1\67\1\uffff"+
			"\3\67\1\u00bd\26\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u00c0\10\67\1\u00bf\7\67\1\u00be"+
			"\6\67\4\uffff\1\67\1\uffff\2\67\1\u00c0\10\67\1\u00bf\7\67\1\u00be\6"+
			"\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u00c1\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u00c1\13\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u00c2\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u00c2\15\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u00c3\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u00c3\6\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00c5\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u00c5\25\67",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\u00c6\26\67\4\uffff\1\67\1\uffff"+
			"\3\67\1\u00c6\26\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u00c7\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u00c7\16\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u00c8\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u00c8\10\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u00c9\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u00c9\13\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u00ca\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u00ca\16\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u00cb\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u00cb\13\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u00cc\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u00cc\27\67",
			"\1\70\1\uffff\12\67\7\uffff\12\67\1\u00cf\1\67\1\u00ce\5\67\1\u00cd"+
			"\7\67\4\uffff\1\67\1\uffff\12\67\1\u00cf\1\67\1\u00ce\5\67\1\u00cd\7"+
			"\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u00d0\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u00d0\7\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u00d1\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u00d1\27\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u00d2\31\67\4\uffff\1\67\1\uffff\1\u00d2"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u00d4\5\67\1\u00d3\13\67\4\uffff"+
			"\1\67\1\uffff\10\67\1\u00d4\5\67\1\u00d3\13\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u00d5\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u00d5\15\67",
			"\1\70\1\uffff\12\67\7\uffff\26\67\1\u00d6\3\67\4\uffff\1\67\1\uffff"+
			"\26\67\1\u00d6\3\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u00d7\5\67\1\u00d8\10\67\4\uffff"+
			"\1\67\1\uffff\13\67\1\u00d7\5\67\1\u00d8\10\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u00d9\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u00d9\21\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u00db\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u00db\6\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u00dc\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u00dc\13\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u00dd\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u00dd\23\67",
			"",
			"",
			"",
			"",
			"\1\167\1\uffff\12\166\13\uffff\1\167\37\uffff\1\167",
			"",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u00de\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u00de\13\67",
			"\1\70\1\uffff\12\67\7\uffff\17\67\1\u00e1\3\67\1\u00e0\3\67\1\u00df"+
			"\2\67\4\uffff\1\67\1\uffff\17\67\1\u00e1\3\67\1\u00e0\3\67\1\u00df\2"+
			"\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u00e2\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u00e2\14\67",
			"",
			"",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u00e3\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u00e3\5\67",
			"",
			"\12\u00e4",
			"\1\167\1\uffff\1\167\2\uffff\12\u00e5",
			"\1\57\1\uffff\12\u0084\7\uffff\4\57\1\u00e6\25\57\4\uffff\1\57\1\uffff"+
			"\4\57\1\u00e6\25\57",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u00e7\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u00e7\27\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00e8\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u00e8\25\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u00e9\31\67\4\uffff\1\67\1\uffff\1\u00e9"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u00ea\31\67\4\uffff\1\67\1\uffff\1\u00ea"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\17\67\1\u00eb\12\67\4\uffff\1\67\1\uffff"+
			"\17\67\1\u00eb\12\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u00ec\22\67\1\u00ed\6\67\4\uffff\1\67"+
			"\1\uffff\1\u00ec\22\67\1\u00ed\6\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u00ee\31\67\4\uffff\1\67\1\uffff\1\u00ee"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u00f0\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u00f0\7\67",
			"\1\70\1\uffff\12\67\7\uffff\1\67\1\u00f1\30\67\4\uffff\1\67\1\uffff"+
			"\1\67\1\u00f1\30\67",
			"\1\u00f4\1\uffff\12\u00f3\7\uffff\32\u00f3\4\uffff\1\u00f3\1\uffff\32"+
			"\u00f3",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00f6\10\67\1\u00f5\14\67\4\uffff"+
			"\1\67\1\uffff\4\67\1\u00f6\10\67\1\u00f5\14\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u00f7\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u00f7\23\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u00f8\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u00f8\16\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00fa\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u00fa\25\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00fb\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u00fb\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u00fc\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u00fc\25\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u00fd\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u00fd\6\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u00fe\31\67\4\uffff\1\67\1\uffff\1\u00fe"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0100\4\67\1\u00ff\7\67\4\uffff"+
			"\1\67\1\uffff\15\67\1\u0100\4\67\1\u00ff\7\67",
			"\1\70\1\uffff\12\67\7\uffff\17\67\1\u0101\12\67\4\uffff\1\67\1\uffff"+
			"\17\67\1\u0101\12\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0102\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0102\14\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0103\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0103\7\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u0104\31\67\4\uffff\1\67\1\uffff\1\u0104"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0105\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0105\6\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0106\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0106\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u0109\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u0109\16\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u010d\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u010d\10\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u010f\31\67\4\uffff\1\67\1\uffff\1\u010f"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0111\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0111\7\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u0113\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u0113\16\67",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\u0114\22\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\u0114\22\67",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\u0115\26\67\4\uffff\1\67\1\uffff"+
			"\3\67\1\u0115\26\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0116\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0116\10\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0117\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0117\25\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0118\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0118\25\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u011b\11\67\1\u011a\13\67\4\uffff"+
			"\1\67\1\uffff\4\67\1\u011b\11\67\1\u011a\13\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u011c\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u011c\25\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u011d\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u011d\7\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u011e\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u011e\16\67",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\u011f\22\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\u011f\22\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0120\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0120\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0122\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0122\14\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u0124\31\67\4\uffff\1\67\1\uffff\1\u0124"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0126\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0126\25\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0129\3\67\1\u0128\7\67\4\uffff"+
			"\1\67\1\uffff\16\67\1\u0129\3\67\1\u0128\7\67",
			"\1\70\1\uffff\12\67\7\uffff\17\67\1\u012a\1\67\1\u012b\10\67\4\uffff"+
			"\1\67\1\uffff\17\67\1\u012a\1\67\1\u012b\10\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u012d\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u012d\21\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u012f\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u012f\25\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0130\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0130\7\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u0132\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u0132\15\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u0133\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u0133\16\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0134\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0134\10\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u0135\31\67\4\uffff\1\67\1\uffff\1\u0135"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0136\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0136\6\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u0137\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u0137\21\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0138\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0138\25\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0139\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0139\6\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u013a\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u013a\25\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u013b\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u013b\14\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u013c\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u013c\27\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u013d\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u013d\15\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u013e\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u013e\13\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u013f\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u013f\7\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u0140\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u0140\5\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u0141\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u0141\27\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0142\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0142\14\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\26\67\1\u0143\3\67\4\uffff\1\67\1\uffff"+
			"\26\67\1\u0143\3\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u0144\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u0144\16\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u0145\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u0145\21\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u0146\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u0146\5\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u0148\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u0148\27\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u014b\1\67\1\u014c\5\67\4\uffff"+
			"\1\67\1\uffff\22\67\1\u014b\1\67\1\u014c\5\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u014d\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u014d\10\67",
			"\1\57\1\uffff\12\u00e4\7\uffff\4\57\1\u014e\25\57\4\uffff\1\57\1\uffff"+
			"\4\57\1\u014e\25\57",
			"\1\57\1\uffff\12\u00e5\7\uffff\32\57\4\uffff\1\57\1\uffff\32\57",
			"\1\167\1\uffff\1\167\2\uffff\12\u014f",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0151\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0151\10\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0152\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0152\6\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u0153\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u0153\5\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u0154\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u0154\27\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\1\67\1\u0156\30\67\4\uffff\1\67\1\uffff"+
			"\1\67\1\u0156\30\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u0157\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u0157\21\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0158\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0158\7\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u015a\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u015a\16\67",
			"",
			"\1\u00f4\1\uffff\12\u00f3\7\uffff\32\u00f3\4\uffff\1\u00f3\1\uffff\32"+
			"\u00f3",
			"\32\u015b\6\uffff\32\u015b",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u015c\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u015c\27\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u015e\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u015e\23\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u015f\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u015f\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0160\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0160\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0162\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0162\14\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0164\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0164\6\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u0165\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u0165\21\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0166\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0166\25\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u0167\31\67\4\uffff\1\67\1\uffff\1\u0167"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0168\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0168\6\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0169\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0169\6\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u016a\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u016a\16\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u016b\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u016b\13\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u016c\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u016c\10\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\30\67\1\u016d\1\67\4\uffff\1\67\1\uffff"+
			"\30\67\1\u016d\1\67",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u016e\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u016e\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u016f\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u016f\27\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\17\67\1\u0170\12\67\4\uffff\1\67\1\uffff"+
			"\17\67\1\u0170\12\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0173\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0173\13\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0174\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0174\25\67",
			"\1\70\1\uffff\12\67\7\uffff\27\67\1\u0175\2\67\4\uffff\1\67\1\uffff"+
			"\27\67\1\u0175\2\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0176\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0176\10\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u0178\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u0178\23\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0179\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0179\10\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u017a\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u017a\6\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u017b\31\67\4\uffff\1\67\1\uffff\1\u017b"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\u017c\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u017d\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u017d\15\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u017e\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u017e\23\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u017f\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u017f\6\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u0180\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u0180\27\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0182\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0182\14\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u0184\31\67\4\uffff\1\67\1\uffff\1\u0184"+
			"\31\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0185\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0185\13\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0186\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0186\10\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0187\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0187\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\u0189\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u018a\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u018a\5\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u018b\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u018b\16\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u018d\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u018d\6\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0190\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0190\14\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0192\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0192\25\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u0193\31\67\4\uffff\1\67\1\uffff\1\u0193"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\25\67\1\u0194\4\67\4\uffff\1\67\1\uffff"+
			"\25\67\1\u0194\4\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0196\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0196\25\67",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\u0197\22\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\u0197\22\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0199\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0199\25\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u019a\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u019a\25\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u019b\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u019b\14\67",
			"\1\70\1\uffff\12\67\7\uffff\17\67\1\u019c\12\67\4\uffff\1\67\1\uffff"+
			"\17\67\1\u019c\12\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\u019d\22\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\u019d\22\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u019f\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u019f\6\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u01a1\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u01a1\7\67",
			"\1\167\1\uffff\1\167\2\uffff\12\u01a2",
			"\1\57\1\uffff\12\u014f\7\uffff\32\57\4\uffff\1\57\1\uffff\32\57",
			"",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u01a3\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u01a3\21\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01a4\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01a4\25\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u01a5\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u01a5\16\67",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\u01a6\22\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\u01a6\22\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u01a7\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u01a7\16\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u01a8\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u01a8\14\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01a9\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01a9\6\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01aa\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01aa\25\67",
			"\1\57\1\uffff\12\u01ac\7\uffff\32\u01ac\4\uffff\1\u01ac\1\uffff\32\u01ac",
			"\1\70\1\uffff\12\67\7\uffff\1\u01ad\31\67\4\uffff\1\67\1\uffff\1\u01ad"+
			"\31\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01ae\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01ae\25\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u01b0\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u01b0\7\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01b3\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01b3\25\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u01b4\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u01b4\7\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u01b5\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u01b5\27\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u01b6\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u01b6\27\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01b8\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01b8\25\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u01b9\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u01b9\13\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u01ba\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u01ba\15\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01bc\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01bc\6\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u01bd\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u01bd\23\67",
			"\1\70\1\uffff\12\67\7\uffff\7\67\1\u01be\22\67\4\uffff\1\67\1\uffff"+
			"\7\67\1\u01be\22\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u01bf\31\67\4\uffff\1\67\1\uffff\1\u01bf"+
			"\31\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\26\67\1\u01c0\3\67\4\uffff\1\67\1\uffff"+
			"\26\67\1\u01c0\3\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01c3\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01c3\6\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01c4\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01c4\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u01c6\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u01c6\7\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u01c7\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u01c7\21\67",
			"\1\70\1\uffff\12\67\7\uffff\20\67\1\u01c8\11\67\4\uffff\1\67\1\uffff"+
			"\20\67\1\u01c8\11\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01c9\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01c9\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01cb\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01cb\25\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01cc\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01cc\6\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\3\67\1\u01cd\26\67\4\uffff\1\67\1\uffff"+
			"\3\67\1\u01cd\26\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u01ce\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u01ce\23\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u01cf\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u01cf\14\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01d2\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01d2\6\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u01d3\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u01d3\15\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\u01d4\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01d6\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01d6\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u01d7\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u01d7\7\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u01d8\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u01d8\10\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01d9\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01d9\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u01da\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u01da\7\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u01db\31\67\4\uffff\1\67\1\uffff\1\u01db"+
			"\31\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01dc\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01dc\25\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u01dd\31\67\4\uffff\1\67\1\uffff\1\u01dd"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01de\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01de\6\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01e1\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01e1\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\57\1\uffff\12\u01a2\7\uffff\32\57\4\uffff\1\57\1\uffff\32\57",
			"\1\70\1\uffff\12\67\7\uffff\1\67\1\u01e3\30\67\4\uffff\1\67\1\uffff"+
			"\1\67\1\u01e3\30\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01e5\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01e5\6\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u01e7\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u01e7\25\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u01e8\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u01e8\27\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u01e9\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u01e9\13\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\57\1\uffff\12\u01ac\7\uffff\32\u01ac\4\uffff\1\u01ac\1\uffff\32\u01ac",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01eb\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01eb\6\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u01ec\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u01ec\10\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01ef\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01ef\6\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01f0\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01f0\6\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u01f1\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u01f1\6\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u01f2\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u01f2\10\67",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u01f3\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u01f3\23\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u01f5\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u01f5\21\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u01f6\31\67\4\uffff\1\67\1\uffff\1\u01f6"+
			"\31\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u01f8\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u01f8\27\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u01fb\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u01fb\10\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u01fd\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u01fd\14\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u01fe\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u01fe\5\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u01ff\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u01ff\10\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0203\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0203\7\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0204\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0204\25\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0205\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0205\7\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0206\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0206\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0208\1\67\1\u0209\11\67\4\uffff"+
			"\1\67\1\uffff\16\67\1\u0208\1\67\1\u0209\11\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u020b\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u020b\7\67",
			"\1\70\1\uffff\12\67\7\uffff\30\67\1\u020c\1\67\4\uffff\1\67\1\uffff"+
			"\30\67\1\u020c\1\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u020f\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u020f\10\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0210\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0210\14\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u0211\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u0211\14\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0214\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0214\7\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0215\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0215\25\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0218\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0218\6\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0219\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0219\10\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u021a\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u021a\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u021c\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u021c\25\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u021d\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u021d\13\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u0221\11\67\1\u0220\7\67\4\uffff"+
			"\1\67\1\uffff\10\67\1\u0221\11\67\1\u0220\7\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0223\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0223\7\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u0224\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u0224\27\67",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0225\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0225\6\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0226\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0226\25\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0229\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0229\13\67",
			"\1\70\1\uffff\12\67\7\uffff\1\u022a\31\67\4\uffff\1\67\1\uffff\1\u022a"+
			"\31\67",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\27\67\1\u022e\2\67\4\uffff\1\67\1\uffff"+
			"\27\67\1\u022e\2\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u022f\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u022f\14\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u0230\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u0230\5\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0239\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0239\25\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u023b\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u023b\14\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u023c\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u023c\10\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u023e\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u023e\14\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0240\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0240\7\67",
			"\1\70\1\uffff\12\67\7\uffff\10\67\1\u0241\21\67\4\uffff\1\67\1\uffff"+
			"\10\67\1\u0241\21\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u0243\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u0243\7\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0244\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0244\10\67",
			"\1\70\1\uffff\12\67\7\uffff\13\67\1\u0245\16\67\4\uffff\1\67\1\uffff"+
			"\13\67\1\u0245\16\67",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\23\67\1\u0246\6\67\4\uffff\1\67\1\uffff"+
			"\23\67\1\u0246\6\67",
			"\1\70\1\uffff\12\67\7\uffff\4\67\1\u0247\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0247\25\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0248\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0248\13\67",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u024a\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u024a\7\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\2\67\1\u024b\27\67\4\uffff\1\67\1\uffff"+
			"\2\67\1\u024b\27\67",
			"\1\70\1\uffff\12\67\7\uffff\22\67\1\u024d\7\67\4\uffff\1\67\1\uffff"+
			"\22\67\1\u024d\7\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\6\67\1\u024e\23\67\4\uffff\1\67\1\uffff"+
			"\6\67\1\u024e\23\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\16\67\1\u0250\13\67\4\uffff\1\67\1\uffff"+
			"\16\67\1\u0250\13\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u0252\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u0252\5\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\21\67\1\u0256\10\67\4\uffff\1\67\1\uffff"+
			"\21\67\1\u0256\10\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\30\67\1\u0258\1\67\4\uffff\1\67\1\uffff"+
			"\30\67\1\u0258\1\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\15\67\1\u025b\14\67\4\uffff\1\67\1\uffff"+
			"\15\67\1\u025b\14\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u025c\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u025c\15\67",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\24\67\1\u025d\5\67\4\uffff\1\67\1\uffff"+
			"\24\67\1\u025d\5\67",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\uffff\12\67\7\uffff\14\67\1\u0261\15\67\4\uffff\1\67\1\uffff"+
			"\14\67\1\u0261\15\67",
			"",
			"",
			"",
			"\1\70\1\uffff\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			""
	};

	static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
	static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
	static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
	static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
	static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
	static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
	static final short[][] DFA22_transition;

	static {
		int numStates = DFA22_transitionS.length;
		DFA22_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
		}
	}

	protected class DFA22 extends DFA {

		public DFA22(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 22;
			this.eot = DFA22_eot;
			this.eof = DFA22_eof;
			this.min = DFA22_min;
			this.max = DFA22_max;
			this.accept = DFA22_accept;
			this.special = DFA22_special;
			this.transition = DFA22_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T_DESCRIBE | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_KEYSPACES | T_NOT | T_WITH | T_DROP | T_TABLE | T_TABLES | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTER | T_CLUSTERS | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_WHERE | T_IN | T_FROM | T_DELETE | T_WINDOW | T_LAST | T_ROWS | T_INNER | T_JOIN | T_BY | T_LIMIT | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_EPHEMERAL | T_AT | T_CATALOG | T_CATALOGS | T_DATASTORE | T_DATASTORES | T_CONNECTOR | T_CONNECTORS | T_SEMICOLON | T_EQUAL | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_PARENTHESIS | T_END_PARENTHESIS | T_QUOTE | T_SINGLE_QUOTE | T_DEFAULT | T_LUCENE | T_CUSTOM | T_FULL_TEXT | T_START_BRACKET | T_END_BRACKET | T_PLUS | T_SUBTRACT | T_SLASH | T_INTERROGATION | T_ASTERISK | T_GROUP | T_AGGREGATION | T_SUM | T_MAX | T_MIN | T_AVG | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_TOKEN | T_MATCH | T_SEC | T_SECS | T_SECOND | T_SECONDS | T_MINS | T_MINUTE | T_MINUTES | T_HOUR | T_HOURS | T_DAY | T_DAYS | T_NULL | T_ATTACH | T_DETACH | T_TO | T_DOUBLE | T_MAP | T_INT | T_INTEGER | T_BOOLEAN | T_VARCHAR | T_TEXT | T_BIGINT | QUOTED_LITERAL | T_CONSTANT | T_IDENT | T_KS_AND_TN | T_CTLG_TBL_COL | T_FLOAT | T_TERM | WS );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA22_31 = input.LA(1);
						s = -1;
						if ( ((LA22_31 >= '\u0000' && LA22_31 <= '\uFFFF')) ) {s = 115;}
						else s = 116;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA22_30 = input.LA(1);
						s = -1;
						if ( ((LA22_30 >= '\u0000' && LA22_30 <= '\uFFFF')) ) {s = 115;}
						else s = 114;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 22, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
