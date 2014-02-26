// $ANTLR 3.5.1 Meta.g 2014-02-26 11:24:44

    package com.stratio.meta.grammar.generated;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaLexer extends Lexer {
	public static final int EOF=-1;
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
	public static final int T__151=151;
	public static final int T__152=152;
	public static final int T__153=153;
	public static final int T__154=154;
	public static final int T__155=155;
	public static final int T__156=156;
	public static final int T__157=157;
	public static final int T__158=158;
	public static final int T__159=159;
	public static final int T__160=160;
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
	public static final int T_AVG=39;
	public static final int T_BETWEEN=40;
	public static final int T_BY=41;
	public static final int T_CLUSTERING=42;
	public static final int T_COLON=43;
	public static final int T_COMMA=44;
	public static final int T_COMPACT=45;
	public static final int T_CONSISTENCY=46;
	public static final int T_CONSTANT=47;
	public static final int T_COUNT=48;
	public static final int T_CREATE=49;
	public static final int T_DELETE=50;
	public static final int T_DESC=51;
	public static final int T_DISABLE=52;
	public static final int T_DISTINCT=53;
	public static final int T_DROP=54;
	public static final int T_EACH_QUORUM=55;
	public static final int T_END_BRACKET=56;
	public static final int T_END_PARENTHESIS=57;
	public static final int T_END_SBRACKET=58;
	public static final int T_EQUAL=59;
	public static final int T_EXISTS=60;
	public static final int T_EXPLAIN=61;
	public static final int T_FALSE=62;
	public static final int T_FLOAT=63;
	public static final int T_FOR=64;
	public static final int T_FROM=65;
	public static final int T_GROUP=66;
	public static final int T_GT=67;
	public static final int T_GTE=68;
	public static final int T_IDENT=69;
	public static final int T_IF=70;
	public static final int T_IN=71;
	public static final int T_INDEX=72;
	public static final int T_INDEX_TYPE=73;
	public static final int T_INNER=74;
	public static final int T_INSERT=75;
	public static final int T_INTERROGATION=76;
	public static final int T_INTO=77;
	public static final int T_JOIN=78;
	public static final int T_KEY=79;
	public static final int T_KEYSPACE=80;
	public static final int T_KS_AND_TN=81;
	public static final int T_LAST=82;
	public static final int T_LIKE=83;
	public static final int T_LIMIT=84;
	public static final int T_LIST=85;
	public static final int T_LOCAL_ONE=86;
	public static final int T_LOCAL_QUORUM=87;
	public static final int T_LT=88;
	public static final int T_LTE=89;
	public static final int T_MAX=90;
	public static final int T_MIN=91;
	public static final int T_NOT=92;
	public static final int T_NOT_EQUAL=93;
	public static final int T_ON=94;
	public static final int T_ONE=95;
	public static final int T_OPTIONS=96;
	public static final int T_ORDER=97;
	public static final int T_PATH=98;
	public static final int T_PLAN=99;
	public static final int T_PLUS=100;
	public static final int T_PRIMARY=101;
	public static final int T_PROCESS=102;
	public static final int T_QUORUM=103;
	public static final int T_QUOTE=104;
	public static final int T_REMOVE=105;
	public static final int T_ROWS=106;
	public static final int T_SELECT=107;
	public static final int T_SEMICOLON=108;
	public static final int T_SET=109;
	public static final int T_SINGLE_QUOTE=110;
	public static final int T_START_BRACKET=111;
	public static final int T_START_PARENTHESIS=112;
	public static final int T_START_SBRACKET=113;
	public static final int T_STOP=114;
	public static final int T_STORAGE=115;
	public static final int T_SUBTRACT=116;
	public static final int T_TABLE=117;
	public static final int T_TERM=118;
	public static final int T_THREE=119;
	public static final int T_TOKEN=120;
	public static final int T_TRIGGER=121;
	public static final int T_TRUE=122;
	public static final int T_TRUNCATE=123;
	public static final int T_TWO=124;
	public static final int T_TYPE=125;
	public static final int T_UDF=126;
	public static final int T_UPDATE=127;
	public static final int T_USE=128;
	public static final int T_USING=129;
	public static final int T_VALUES=130;
	public static final int T_WHERE=131;
	public static final int T_WINDOW=132;
	public static final int T_WITH=133;
	public static final int U=134;
	public static final int V=135;
	public static final int W=136;
	public static final int WS=137;
	public static final int X=138;
	public static final int Y=139;
	public static final int Z=140;

	    


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

	// $ANTLR start "T__141"
	public final void mT__141() throws RecognitionException {
		try {
			int _type = T__141;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:14:8: ( '1' )
			// Meta.g:14:10: '1'
			{
			match('1'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__141"

	// $ANTLR start "T__142"
	public final void mT__142() throws RecognitionException {
		try {
			int _type = T__142;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:15:8: ( 'D' )
			// Meta.g:15:10: 'D'
			{
			match('D'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__142"

	// $ANTLR start "T__143"
	public final void mT__143() throws RecognitionException {
		try {
			int _type = T__143;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:16:8: ( 'DAYS' )
			// Meta.g:16:10: 'DAYS'
			{
			match("DAYS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__143"

	// $ANTLR start "T__144"
	public final void mT__144() throws RecognitionException {
		try {
			int _type = T__144;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:17:8: ( 'H' )
			// Meta.g:17:10: 'H'
			{
			match('H'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__144"

	// $ANTLR start "T__145"
	public final void mT__145() throws RecognitionException {
		try {
			int _type = T__145;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:18:8: ( 'HOURS' )
			// Meta.g:18:10: 'HOURS'
			{
			match("HOURS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__145"

	// $ANTLR start "T__146"
	public final void mT__146() throws RecognitionException {
		try {
			int _type = T__146;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:19:8: ( 'M' )
			// Meta.g:19:10: 'M'
			{
			match('M'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__146"

	// $ANTLR start "T__147"
	public final void mT__147() throws RecognitionException {
		try {
			int _type = T__147;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:20:8: ( 'MINUTES' )
			// Meta.g:20:10: 'MINUTES'
			{
			match("MINUTES"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__147"

	// $ANTLR start "T__148"
	public final void mT__148() throws RecognitionException {
		try {
			int _type = T__148;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:21:8: ( 'PROCESS' )
			// Meta.g:21:10: 'PROCESS'
			{
			match("PROCESS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__148"

	// $ANTLR start "T__149"
	public final void mT__149() throws RecognitionException {
		try {
			int _type = T__149;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:22:8: ( 'S' )
			// Meta.g:22:10: 'S'
			{
			match('S'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__149"

	// $ANTLR start "T__150"
	public final void mT__150() throws RecognitionException {
		try {
			int _type = T__150;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:23:8: ( 'SECONDS' )
			// Meta.g:23:10: 'SECONDS'
			{
			match("SECONDS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__150"

	// $ANTLR start "T__151"
	public final void mT__151() throws RecognitionException {
		try {
			int _type = T__151;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:24:8: ( 'TRIGGER' )
			// Meta.g:24:10: 'TRIGGER'
			{
			match("TRIGGER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__151"

	// $ANTLR start "T__152"
	public final void mT__152() throws RecognitionException {
		try {
			int _type = T__152;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:25:8: ( 'UDF' )
			// Meta.g:25:10: 'UDF'
			{
			match("UDF"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__152"

	// $ANTLR start "T__153"
	public final void mT__153() throws RecognitionException {
		try {
			int _type = T__153;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:26:8: ( 'd' )
			// Meta.g:26:10: 'd'
			{
			match('d'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__153"

	// $ANTLR start "T__154"
	public final void mT__154() throws RecognitionException {
		try {
			int _type = T__154;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:27:8: ( 'days' )
			// Meta.g:27:10: 'days'
			{
			match("days"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__154"

	// $ANTLR start "T__155"
	public final void mT__155() throws RecognitionException {
		try {
			int _type = T__155;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:28:8: ( 'h' )
			// Meta.g:28:10: 'h'
			{
			match('h'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__155"

	// $ANTLR start "T__156"
	public final void mT__156() throws RecognitionException {
		try {
			int _type = T__156;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:29:8: ( 'hours' )
			// Meta.g:29:10: 'hours'
			{
			match("hours"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__156"

	// $ANTLR start "T__157"
	public final void mT__157() throws RecognitionException {
		try {
			int _type = T__157;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:30:8: ( 'm' )
			// Meta.g:30:10: 'm'
			{
			match('m'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__157"

	// $ANTLR start "T__158"
	public final void mT__158() throws RecognitionException {
		try {
			int _type = T__158;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:31:8: ( 'minutes' )
			// Meta.g:31:10: 'minutes'
			{
			match("minutes"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__158"

	// $ANTLR start "T__159"
	public final void mT__159() throws RecognitionException {
		try {
			int _type = T__159;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:32:8: ( 's' )
			// Meta.g:32:10: 's'
			{
			match('s'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__159"

	// $ANTLR start "T__160"
	public final void mT__160() throws RecognitionException {
		try {
			int _type = T__160;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:33:8: ( 'seconds' )
			// Meta.g:33:10: 'seconds'
			{
			match("seconds"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__160"

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// Meta.g:67:11: ( ( 'a' | 'A' ) )
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
			// Meta.g:68:11: ( ( 'b' | 'B' ) )
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
			// Meta.g:69:11: ( ( 'c' | 'C' ) )
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
			// Meta.g:70:11: ( ( 'd' | 'D' ) )
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
			// Meta.g:71:11: ( ( 'e' | 'E' ) )
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
			// Meta.g:72:11: ( ( 'f' | 'F' ) )
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
			// Meta.g:73:11: ( ( 'g' | 'G' ) )
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
			// Meta.g:74:11: ( ( 'h' | 'H' ) )
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
			// Meta.g:75:11: ( ( 'i' | 'I' ) )
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
			// Meta.g:76:11: ( ( 'j' | 'J' ) )
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
			// Meta.g:77:11: ( ( 'k' | 'K' ) )
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
			// Meta.g:78:11: ( ( 'l' | 'L' ) )
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
			// Meta.g:79:11: ( ( 'm' | 'M' ) )
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
			// Meta.g:80:11: ( ( 'n' | 'N' ) )
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
			// Meta.g:81:11: ( ( 'o' | 'O' ) )
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
			// Meta.g:82:11: ( ( 'p' | 'P' ) )
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
			// Meta.g:83:11: ( ( 'q' | 'Q' ) )
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
			// Meta.g:84:11: ( ( 'r' | 'R' ) )
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
			// Meta.g:85:11: ( ( 's' | 'S' ) )
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
			// Meta.g:86:11: ( ( 't' | 'T' ) )
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
			// Meta.g:87:11: ( ( 'u' | 'U' ) )
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
			// Meta.g:88:11: ( ( 'v' | 'V' ) )
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
			// Meta.g:89:11: ( ( 'w' | 'W' ) )
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
			// Meta.g:90:11: ( ( 'x' | 'X' ) )
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
			// Meta.g:91:11: ( ( 'y' | 'Y' ) )
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
			// Meta.g:92:11: ( ( 'z' | 'Z' ) )
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
			// Meta.g:93:19: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
			// Meta.g:93:21: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// Meta.g:93:31: ( '+' | '-' )?
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

			// Meta.g:93:42: ( '0' .. '9' )+
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
			// Meta.g:94:15: ( '.' )
			// Meta.g:94:17: '.'
			{
			match('.'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "POINT"

	// $ANTLR start "T_TRUNCATE"
	public final void mT_TRUNCATE() throws RecognitionException {
		try {
			int _type = T_TRUNCATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:97:11: ( T R U N C A T E )
			// Meta.g:97:13: T R U N C A T E
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
			// Meta.g:98:9: ( C R E A T E )
			// Meta.g:98:11: C R E A T E
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
			// Meta.g:99:8: ( A L T E R )
			// Meta.g:99:10: A L T E R
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
			// Meta.g:100:11: ( K E Y S P A C E )
			// Meta.g:100:13: K E Y S P A C E
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

	// $ANTLR start "T_NOT"
	public final void mT_NOT() throws RecognitionException {
		try {
			int _type = T_NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:101:6: ( N O T )
			// Meta.g:101:8: N O T
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
			// Meta.g:102:7: ( W I T H )
			// Meta.g:102:9: W I T H
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
			// Meta.g:103:7: ( D R O P )
			// Meta.g:103:9: D R O P
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
			// Meta.g:104:8: ( T A B L E )
			// Meta.g:104:10: T A B L E
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

	// $ANTLR start "T_IF"
	public final void mT_IF() throws RecognitionException {
		try {
			int _type = T_IF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:105:5: ( I F )
			// Meta.g:105:7: I F
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
			// Meta.g:106:9: ( E X I S T S )
			// Meta.g:106:11: E X I S T S
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
			// Meta.g:107:6: ( A N D )
			// Meta.g:107:8: A N D
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
			// Meta.g:108:6: ( U S E )
			// Meta.g:108:8: U S E
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
			// Meta.g:109:6: ( S E T )
			// Meta.g:109:8: S E T
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
			// Meta.g:110:10: ( O P T I O N S )
			// Meta.g:110:12: O P T I O N S
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
			// Meta.g:111:12: ( A N A L Y T I C S )
			// Meta.g:111:14: A N A L Y T I C S
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
			// Meta.g:112:7: ( T R U E )
			// Meta.g:112:9: T R U E
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
			// Meta.g:113:8: ( F A L S E )
			// Meta.g:113:10: F A L S E
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
			// Meta.g:114:14: ( C O N S I S T E N C Y )
			// Meta.g:114:16: C O N S I S T E N C Y
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
			// Meta.g:115:6: ( A L L )
			// Meta.g:115:8: A L L
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
			// Meta.g:116:6: ( A N Y )
			// Meta.g:116:8: A N Y
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
			// Meta.g:117:9: ( Q U O R U M )
			// Meta.g:117:11: Q U O R U M
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
			// Meta.g:118:6: ( O N E )
			// Meta.g:118:8: O N E
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
			// Meta.g:119:6: ( T W O )
			// Meta.g:119:8: T W O
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
			// Meta.g:120:8: ( T H R E E )
			// Meta.g:120:10: T H R E E
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
			// Meta.g:121:14: ( E A C H '_' Q U O R U M )
			// Meta.g:121:16: E A C H '_' Q U O R U M
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
			// Meta.g:122:12: ( L O C A L '_' O N E )
			// Meta.g:122:14: L O C A L '_' O N E
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
			// Meta.g:123:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:123:17: L O C A L '_' Q U O R U M
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
			// Meta.g:124:10: ( E X P L A I N )
			// Meta.g:124:12: E X P L A I N
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
			// Meta.g:125:7: ( P L A N )
			// Meta.g:125:9: P L A N
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
			// Meta.g:126:6: ( F O R )
			// Meta.g:126:8: F O R
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
			// Meta.g:127:8: ( I N D E X )
			// Meta.g:127:10: I N D E X
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
			// Meta.g:128:7: ( L I S T )
			// Meta.g:128:9: L I S T
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
			// Meta.g:129:9: ( R E M O V E )
			// Meta.g:129:11: R E M O V E
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
			// Meta.g:130:6: ( U D F )
			// Meta.g:130:8: U D F
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
			// Meta.g:131:10: ( P R O C E S S )
			// Meta.g:131:12: P R O C E S S
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
			// Meta.g:132:10: ( T R I G G E R )
			// Meta.g:132:12: T R I G G E R
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
			// Meta.g:133:7: ( S T O P )
			// Meta.g:133:9: S T O P
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
			// Meta.g:134:5: ( O N )
			// Meta.g:134:7: O N
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
			// Meta.g:135:8: ( U S I N G )
			// Meta.g:135:10: U S I N G
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
			// Meta.g:136:7: ( T Y P E )
			// Meta.g:136:9: T Y P E
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
			// Meta.g:137:6: ( A D D )
			// Meta.g:137:8: A D D
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
			// Meta.g:138:10: ( P R I M A R Y )
			// Meta.g:138:12: P R I M A R Y
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
			// Meta.g:139:6: ( K E Y )
			// Meta.g:139:8: K E Y
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
			// Meta.g:140:9: ( I N S E R T )
			// Meta.g:140:11: I N S E R T
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
			// Meta.g:141:7: ( I N T O )
			// Meta.g:141:9: I N T O
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
			// Meta.g:142:10: ( C O M P A C T )
			// Meta.g:142:12: C O M P A C T
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
			// Meta.g:143:10: ( S T O R A G E )
			// Meta.g:143:12: S T O R A G E
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

	// $ANTLR start "T_CLUSTERING"
	public final void mT_CLUSTERING() throws RecognitionException {
		try {
			int _type = T_CLUSTERING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:144:13: ( C L U S T E R I N G )
			// Meta.g:144:15: C L U S T E R I N G
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
			// Meta.g:145:8: ( O R D E R )
			// Meta.g:145:10: O R D E R
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
			// Meta.g:146:9: ( S E L E C T )
			// Meta.g:146:11: S E L E C T
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
			// Meta.g:147:9: ( V A L U E S )
			// Meta.g:147:11: V A L U E S
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
			// Meta.g:148:9: ( U P D A T E )
			// Meta.g:148:11: U P D A T E
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
			// Meta.g:149:8: ( W H E R E )
			// Meta.g:149:10: W H E R E
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
			// Meta.g:150:5: ( I N )
			// Meta.g:150:7: I N
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
			// Meta.g:151:7: ( F R O M )
			// Meta.g:151:9: F R O M
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
			// Meta.g:152:9: ( D E L E T E )
			// Meta.g:152:11: D E L E T E
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
			// Meta.g:153:9: ( W I N D O W )
			// Meta.g:153:11: W I N D O W
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
			// Meta.g:154:7: ( L A S T )
			// Meta.g:154:9: L A S T
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
			// Meta.g:155:7: ( R O W S )
			// Meta.g:155:9: R O W S
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
			// Meta.g:156:8: ( I N N E R )
			// Meta.g:156:10: I N N E R
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
			// Meta.g:157:7: ( J O I N )
			// Meta.g:157:9: J O I N
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
			// Meta.g:158:5: ( B Y )
			// Meta.g:158:7: B Y
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
			// Meta.g:159:8: ( L I M I T )
			// Meta.g:159:10: L I M I T
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
			// Meta.g:160:10: ( D I S A B L E )
			// Meta.g:160:12: D I S A B L E
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
			// Meta.g:161:11: ( D I S T I N C T )
			// Meta.g:161:13: D I S T I N C T
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
			// Meta.g:162:8: ( C O U N T )
			// Meta.g:162:10: C O U N T
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
			// Meta.g:163:5: ( A S )
			// Meta.g:163:7: A S
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
			// Meta.g:164:10: ( B E T W E E N )
			// Meta.g:164:12: B E T W E E N
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
			// Meta.g:165:6: ( A S C )
			// Meta.g:165:8: A S C
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
			// Meta.g:166:7: ( D E S C )
			// Meta.g:166:9: D E S C
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
			// Meta.g:167:7: ( L I K E )
			// Meta.g:167:9: L I K E
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

	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:169:12: ( ';' )
			// Meta.g:169:14: ';'
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
			// Meta.g:170:8: ( '=' )
			// Meta.g:170:10: '='
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
			// Meta.g:171:17: ( '{' )
			// Meta.g:171:19: '{'
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
			// Meta.g:172:15: ( '}' )
			// Meta.g:172:17: '}'
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
			// Meta.g:173:8: ( ':' )
			// Meta.g:173:10: ':'
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
			// Meta.g:174:8: ( ',' )
			// Meta.g:174:10: ','
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
			// Meta.g:175:20: ( '(' )
			// Meta.g:175:22: '('
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
			// Meta.g:176:18: ( ')' )
			// Meta.g:176:20: ')'
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
			// Meta.g:177:8: ( '\"' )
			// Meta.g:177:10: '\"'
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
			// Meta.g:178:15: ( '\\'' )
			// Meta.g:178:17: '\\''
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

	// $ANTLR start "T_INDEX_TYPE"
	public final void mT_INDEX_TYPE() throws RecognitionException {
		try {
			int _type = T_INDEX_TYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:179:13: ( ( 'DEFAULT' | 'LUCENE' | 'CUSTOM' ) )
			// Meta.g:179:15: ( 'DEFAULT' | 'LUCENE' | 'CUSTOM' )
			{
			// Meta.g:179:15: ( 'DEFAULT' | 'LUCENE' | 'CUSTOM' )
			int alt3=3;
			switch ( input.LA(1) ) {
			case 'D':
				{
				alt3=1;
				}
				break;
			case 'L':
				{
				alt3=2;
				}
				break;
			case 'C':
				{
				alt3=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 3, 0, input);
				throw nvae;
			}
			switch (alt3) {
				case 1 :
					// Meta.g:179:16: 'DEFAULT'
					{
					match("DEFAULT"); 

					}
					break;
				case 2 :
					// Meta.g:179:28: 'LUCENE'
					{
					match("LUCENE"); 

					}
					break;
				case 3 :
					// Meta.g:179:39: 'CUSTOM'
					{
					match("CUSTOM"); 

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
	// $ANTLR end "T_INDEX_TYPE"

	// $ANTLR start "T_START_BRACKET"
	public final void mT_START_BRACKET() throws RecognitionException {
		try {
			int _type = T_START_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:180:16: ( '[' )
			// Meta.g:180:18: '['
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
			// Meta.g:181:14: ( ']' )
			// Meta.g:181:16: ']'
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
			// Meta.g:182:7: ( '+' )
			// Meta.g:182:9: '+'
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
			// Meta.g:183:11: ( '-' )
			// Meta.g:183:13: '-'
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

	// $ANTLR start "T_INTERROGATION"
	public final void mT_INTERROGATION() throws RecognitionException {
		try {
			int _type = T_INTERROGATION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:184:16: ( '?' )
			// Meta.g:184:18: '?'
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
			// Meta.g:185:11: ( '*' )
			// Meta.g:185:13: '*'
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
			// Meta.g:186:8: ( G R O U P )
			// Meta.g:186:10: G R O U P
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
			// Meta.g:187:14: ( A G G R E G A T I O N )
			// Meta.g:187:16: A G G R E G A T I O N
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

	// $ANTLR start "T_MAX"
	public final void mT_MAX() throws RecognitionException {
		try {
			int _type = T_MAX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:188:6: ( M A X )
			// Meta.g:188:8: M A X
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
			// Meta.g:189:6: ( M I N )
			// Meta.g:189:8: M I N
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
			// Meta.g:190:6: ( A V G )
			// Meta.g:190:8: A V G
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
			// Meta.g:191:5: ( '>' )
			// Meta.g:191:7: '>'
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
			// Meta.g:192:5: ( '<' )
			// Meta.g:192:7: '<'
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
			// Meta.g:193:6: ( '>' '=' )
			// Meta.g:193:8: '>' '='
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
			// Meta.g:194:6: ( '<' '=' )
			// Meta.g:194:8: '<' '='
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
			// Meta.g:195:12: ( '<' '>' )
			// Meta.g:195:14: '<' '>'
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
			// Meta.g:196:8: ( T O K E N )
			// Meta.g:196:10: T O K E N
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

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// Meta.g:198:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
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
			// Meta.g:199:15: ( '0' .. '9' )
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
			// Meta.g:203:38: ( '\\'' (c=~ ( '\\'' ) | '\\'' '\\'' )* '\\'' )
			// Meta.g:204:9: '\\'' (c=~ ( '\\'' ) | '\\'' '\\'' )* '\\''
			{
			match('\''); 
			// Meta.g:204:14: (c=~ ( '\\'' ) | '\\'' '\\'' )*
			loop4:
			while (true) {
				int alt4=3;
				int LA4_0 = input.LA(1);
				if ( (LA4_0=='\'') ) {
					int LA4_1 = input.LA(2);
					if ( (LA4_1=='\'') ) {
						alt4=2;
					}

				}
				else if ( ((LA4_0 >= '\u0000' && LA4_0 <= '&')||(LA4_0 >= '(' && LA4_0 <= '\uFFFF')) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// Meta.g:204:15: c=~ ( '\\'' )
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
				case 2 :
					// Meta.g:204:53: '\\'' '\\''
					{
					match('\''); 
					match('\''); 
					 sb.appendCodePoint('\''); 
					}
					break;

				default :
					break loop4;
				}
			}

			match('\''); 
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
			// Meta.g:207:11: ( ( DIGIT )+ )
			// Meta.g:207:13: ( DIGIT )+
			{
			// Meta.g:207:13: ( DIGIT )+
			int cnt5=0;
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( ((LA5_0 >= '0' && LA5_0 <= '9')) ) {
					alt5=1;
				}

				switch (alt5) {
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
					if ( cnt5 >= 1 ) break loop5;
					EarlyExitException eee = new EarlyExitException(5, input);
					throw eee;
				}
				cnt5++;
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
			// Meta.g:209:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:209:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:209:17: ( LETTER | DIGIT | '_' )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '0' && LA6_0 <= '9')||(LA6_0 >= 'A' && LA6_0 <= 'Z')||LA6_0=='_'||(LA6_0 >= 'a' && LA6_0 <= 'z')) ) {
					alt6=1;
				}

				switch (alt6) {
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
					break loop6;
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
			// Meta.g:211:12: ( LETTER ( LETTER | DIGIT | '_' )* ( POINT LETTER ( LETTER | DIGIT | '_' )* )? )
			// Meta.g:211:14: LETTER ( LETTER | DIGIT | '_' )* ( POINT LETTER ( LETTER | DIGIT | '_' )* )?
			{
			mLETTER(); 

			// Meta.g:211:21: ( LETTER | DIGIT | '_' )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( ((LA7_0 >= '0' && LA7_0 <= '9')||(LA7_0 >= 'A' && LA7_0 <= 'Z')||LA7_0=='_'||(LA7_0 >= 'a' && LA7_0 <= 'z')) ) {
					alt7=1;
				}

				switch (alt7) {
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
					break loop7;
				}
			}

			// Meta.g:211:45: ( POINT LETTER ( LETTER | DIGIT | '_' )* )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0=='.') ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// Meta.g:211:46: POINT LETTER ( LETTER | DIGIT | '_' )*
					{
					mPOINT(); 

					mLETTER(); 

					// Meta.g:211:59: ( LETTER | DIGIT | '_' )*
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

	// $ANTLR start "T_TERM"
	public final void mT_TERM() throws RecognitionException {
		try {
			int _type = T_TERM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:213:7: ( ( LETTER | DIGIT | '_' | '.' )+ )
			// Meta.g:213:9: ( LETTER | DIGIT | '_' | '.' )+
			{
			// Meta.g:213:9: ( LETTER | DIGIT | '_' | '.' )+
			int cnt10=0;
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0=='.'||(LA10_0 >= '0' && LA10_0 <= '9')||(LA10_0 >= 'A' && LA10_0 <= 'Z')||LA10_0=='_'||(LA10_0 >= 'a' && LA10_0 <= 'z')) ) {
					alt10=1;
				}

				switch (alt10) {
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
					if ( cnt10 >= 1 ) break loop10;
					EarlyExitException eee = new EarlyExitException(10, input);
					throw eee;
				}
				cnt10++;
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

	// $ANTLR start "T_FLOAT"
	public final void mT_FLOAT() throws RecognitionException {
		try {
			int _type = T_FLOAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:215:8: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
			int alt17=3;
			alt17 = dfa17.predict(input);
			switch (alt17) {
				case 1 :
					// Meta.g:215:12: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
					{
					// Meta.g:215:12: ( '0' .. '9' )+
					int cnt11=0;
					loop11:
					while (true) {
						int alt11=2;
						int LA11_0 = input.LA(1);
						if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
							alt11=1;
						}

						switch (alt11) {
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
							if ( cnt11 >= 1 ) break loop11;
							EarlyExitException eee = new EarlyExitException(11, input);
							throw eee;
						}
						cnt11++;
					}

					match('.'); 
					// Meta.g:215:28: ( '0' .. '9' )*
					loop12:
					while (true) {
						int alt12=2;
						int LA12_0 = input.LA(1);
						if ( ((LA12_0 >= '0' && LA12_0 <= '9')) ) {
							alt12=1;
						}

						switch (alt12) {
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
							break loop12;
						}
					}

					// Meta.g:215:40: ( EXPONENT )?
					int alt13=2;
					int LA13_0 = input.LA(1);
					if ( (LA13_0=='E'||LA13_0=='e') ) {
						alt13=1;
					}
					switch (alt13) {
						case 1 :
							// Meta.g:215:40: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 2 :
					// Meta.g:216:10: '.' ( '0' .. '9' )+ ( EXPONENT )?
					{
					match('.'); 
					// Meta.g:216:14: ( '0' .. '9' )+
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

					// Meta.g:216:26: ( EXPONENT )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0=='E'||LA15_0=='e') ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// Meta.g:216:26: EXPONENT
							{
							mEXPONENT(); 

							}
							break;

					}

					}
					break;
				case 3 :
					// Meta.g:217:10: ( '0' .. '9' )+ EXPONENT
					{
					// Meta.g:217:10: ( '0' .. '9' )+
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

					mEXPONENT(); 

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_FLOAT"

	// $ANTLR start "T_PATH"
	public final void mT_PATH() throws RecognitionException {
		try {
			int _type = T_PATH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:220:7: ( ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+ )
			// Meta.g:220:9: ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+
			{
			// Meta.g:220:9: ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+
			int cnt18=0;
			loop18:
			while (true) {
				int alt18=2;
				int LA18_0 = input.LA(1);
				if ( ((LA18_0 >= '-' && LA18_0 <= '9')||(LA18_0 >= 'A' && LA18_0 <= 'Z')||LA18_0=='_'||(LA18_0 >= 'a' && LA18_0 <= 'z')) ) {
					alt18=1;
				}

				switch (alt18) {
				case 1 :
					// Meta.g:
					{
					if ( (input.LA(1) >= '-' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_PATH"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:941:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:941:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:941:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			int cnt19=0;
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( ((LA19_0 >= '\t' && LA19_0 <= '\n')||LA19_0=='\r'||LA19_0==' ') ) {
					alt19=1;
				}

				switch (alt19) {
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
					if ( cnt19 >= 1 ) break loop19;
					EarlyExitException eee = new EarlyExitException(19, input);
					throw eee;
				}
				cnt19++;
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
		// Meta.g:1:8: ( T__141 | T__142 | T__143 | T__144 | T__145 | T__146 | T__147 | T__148 | T__149 | T__150 | T__151 | T__152 | T__153 | T__154 | T__155 | T__156 | T__157 | T__158 | T__159 | T__160 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_WHERE | T_IN | T_FROM | T_DELETE | T_WINDOW | T_LAST | T_ROWS | T_INNER | T_JOIN | T_BY | T_LIMIT | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_SEMICOLON | T_EQUAL | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_PARENTHESIS | T_END_PARENTHESIS | T_QUOTE | T_SINGLE_QUOTE | T_INDEX_TYPE | T_START_BRACKET | T_END_BRACKET | T_PLUS | T_SUBTRACT | T_INTERROGATION | T_ASTERISK | T_GROUP | T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_TOKEN | QUOTED_LITERAL | T_CONSTANT | T_IDENT | T_KS_AND_TN | T_TERM | T_FLOAT | T_PATH | WS )
		int alt20=127;
		alt20 = dfa20.predict(input);
		switch (alt20) {
			case 1 :
				// Meta.g:1:10: T__141
				{
				mT__141(); 

				}
				break;
			case 2 :
				// Meta.g:1:17: T__142
				{
				mT__142(); 

				}
				break;
			case 3 :
				// Meta.g:1:24: T__143
				{
				mT__143(); 

				}
				break;
			case 4 :
				// Meta.g:1:31: T__144
				{
				mT__144(); 

				}
				break;
			case 5 :
				// Meta.g:1:38: T__145
				{
				mT__145(); 

				}
				break;
			case 6 :
				// Meta.g:1:45: T__146
				{
				mT__146(); 

				}
				break;
			case 7 :
				// Meta.g:1:52: T__147
				{
				mT__147(); 

				}
				break;
			case 8 :
				// Meta.g:1:59: T__148
				{
				mT__148(); 

				}
				break;
			case 9 :
				// Meta.g:1:66: T__149
				{
				mT__149(); 

				}
				break;
			case 10 :
				// Meta.g:1:73: T__150
				{
				mT__150(); 

				}
				break;
			case 11 :
				// Meta.g:1:80: T__151
				{
				mT__151(); 

				}
				break;
			case 12 :
				// Meta.g:1:87: T__152
				{
				mT__152(); 

				}
				break;
			case 13 :
				// Meta.g:1:94: T__153
				{
				mT__153(); 

				}
				break;
			case 14 :
				// Meta.g:1:101: T__154
				{
				mT__154(); 

				}
				break;
			case 15 :
				// Meta.g:1:108: T__155
				{
				mT__155(); 

				}
				break;
			case 16 :
				// Meta.g:1:115: T__156
				{
				mT__156(); 

				}
				break;
			case 17 :
				// Meta.g:1:122: T__157
				{
				mT__157(); 

				}
				break;
			case 18 :
				// Meta.g:1:129: T__158
				{
				mT__158(); 

				}
				break;
			case 19 :
				// Meta.g:1:136: T__159
				{
				mT__159(); 

				}
				break;
			case 20 :
				// Meta.g:1:143: T__160
				{
				mT__160(); 

				}
				break;
			case 21 :
				// Meta.g:1:150: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 22 :
				// Meta.g:1:161: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 23 :
				// Meta.g:1:170: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 24 :
				// Meta.g:1:178: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 25 :
				// Meta.g:1:189: T_NOT
				{
				mT_NOT(); 

				}
				break;
			case 26 :
				// Meta.g:1:195: T_WITH
				{
				mT_WITH(); 

				}
				break;
			case 27 :
				// Meta.g:1:202: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 28 :
				// Meta.g:1:209: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 29 :
				// Meta.g:1:217: T_IF
				{
				mT_IF(); 

				}
				break;
			case 30 :
				// Meta.g:1:222: T_EXISTS
				{
				mT_EXISTS(); 

				}
				break;
			case 31 :
				// Meta.g:1:231: T_AND
				{
				mT_AND(); 

				}
				break;
			case 32 :
				// Meta.g:1:237: T_USE
				{
				mT_USE(); 

				}
				break;
			case 33 :
				// Meta.g:1:243: T_SET
				{
				mT_SET(); 

				}
				break;
			case 34 :
				// Meta.g:1:249: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 35 :
				// Meta.g:1:259: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 36 :
				// Meta.g:1:271: T_TRUE
				{
				mT_TRUE(); 

				}
				break;
			case 37 :
				// Meta.g:1:278: T_FALSE
				{
				mT_FALSE(); 

				}
				break;
			case 38 :
				// Meta.g:1:286: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 39 :
				// Meta.g:1:300: T_ALL
				{
				mT_ALL(); 

				}
				break;
			case 40 :
				// Meta.g:1:306: T_ANY
				{
				mT_ANY(); 

				}
				break;
			case 41 :
				// Meta.g:1:312: T_QUORUM
				{
				mT_QUORUM(); 

				}
				break;
			case 42 :
				// Meta.g:1:321: T_ONE
				{
				mT_ONE(); 

				}
				break;
			case 43 :
				// Meta.g:1:327: T_TWO
				{
				mT_TWO(); 

				}
				break;
			case 44 :
				// Meta.g:1:333: T_THREE
				{
				mT_THREE(); 

				}
				break;
			case 45 :
				// Meta.g:1:341: T_EACH_QUORUM
				{
				mT_EACH_QUORUM(); 

				}
				break;
			case 46 :
				// Meta.g:1:355: T_LOCAL_ONE
				{
				mT_LOCAL_ONE(); 

				}
				break;
			case 47 :
				// Meta.g:1:367: T_LOCAL_QUORUM
				{
				mT_LOCAL_QUORUM(); 

				}
				break;
			case 48 :
				// Meta.g:1:382: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 49 :
				// Meta.g:1:392: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 50 :
				// Meta.g:1:399: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 51 :
				// Meta.g:1:405: T_INDEX
				{
				mT_INDEX(); 

				}
				break;
			case 52 :
				// Meta.g:1:413: T_LIST
				{
				mT_LIST(); 

				}
				break;
			case 53 :
				// Meta.g:1:420: T_REMOVE
				{
				mT_REMOVE(); 

				}
				break;
			case 54 :
				// Meta.g:1:429: T_UDF
				{
				mT_UDF(); 

				}
				break;
			case 55 :
				// Meta.g:1:435: T_PROCESS
				{
				mT_PROCESS(); 

				}
				break;
			case 56 :
				// Meta.g:1:445: T_TRIGGER
				{
				mT_TRIGGER(); 

				}
				break;
			case 57 :
				// Meta.g:1:455: T_STOP
				{
				mT_STOP(); 

				}
				break;
			case 58 :
				// Meta.g:1:462: T_ON
				{
				mT_ON(); 

				}
				break;
			case 59 :
				// Meta.g:1:467: T_USING
				{
				mT_USING(); 

				}
				break;
			case 60 :
				// Meta.g:1:475: T_TYPE
				{
				mT_TYPE(); 

				}
				break;
			case 61 :
				// Meta.g:1:482: T_ADD
				{
				mT_ADD(); 

				}
				break;
			case 62 :
				// Meta.g:1:488: T_PRIMARY
				{
				mT_PRIMARY(); 

				}
				break;
			case 63 :
				// Meta.g:1:498: T_KEY
				{
				mT_KEY(); 

				}
				break;
			case 64 :
				// Meta.g:1:504: T_INSERT
				{
				mT_INSERT(); 

				}
				break;
			case 65 :
				// Meta.g:1:513: T_INTO
				{
				mT_INTO(); 

				}
				break;
			case 66 :
				// Meta.g:1:520: T_COMPACT
				{
				mT_COMPACT(); 

				}
				break;
			case 67 :
				// Meta.g:1:530: T_STORAGE
				{
				mT_STORAGE(); 

				}
				break;
			case 68 :
				// Meta.g:1:540: T_CLUSTERING
				{
				mT_CLUSTERING(); 

				}
				break;
			case 69 :
				// Meta.g:1:553: T_ORDER
				{
				mT_ORDER(); 

				}
				break;
			case 70 :
				// Meta.g:1:561: T_SELECT
				{
				mT_SELECT(); 

				}
				break;
			case 71 :
				// Meta.g:1:570: T_VALUES
				{
				mT_VALUES(); 

				}
				break;
			case 72 :
				// Meta.g:1:579: T_UPDATE
				{
				mT_UPDATE(); 

				}
				break;
			case 73 :
				// Meta.g:1:588: T_WHERE
				{
				mT_WHERE(); 

				}
				break;
			case 74 :
				// Meta.g:1:596: T_IN
				{
				mT_IN(); 

				}
				break;
			case 75 :
				// Meta.g:1:601: T_FROM
				{
				mT_FROM(); 

				}
				break;
			case 76 :
				// Meta.g:1:608: T_DELETE
				{
				mT_DELETE(); 

				}
				break;
			case 77 :
				// Meta.g:1:617: T_WINDOW
				{
				mT_WINDOW(); 

				}
				break;
			case 78 :
				// Meta.g:1:626: T_LAST
				{
				mT_LAST(); 

				}
				break;
			case 79 :
				// Meta.g:1:633: T_ROWS
				{
				mT_ROWS(); 

				}
				break;
			case 80 :
				// Meta.g:1:640: T_INNER
				{
				mT_INNER(); 

				}
				break;
			case 81 :
				// Meta.g:1:648: T_JOIN
				{
				mT_JOIN(); 

				}
				break;
			case 82 :
				// Meta.g:1:655: T_BY
				{
				mT_BY(); 

				}
				break;
			case 83 :
				// Meta.g:1:660: T_LIMIT
				{
				mT_LIMIT(); 

				}
				break;
			case 84 :
				// Meta.g:1:668: T_DISABLE
				{
				mT_DISABLE(); 

				}
				break;
			case 85 :
				// Meta.g:1:678: T_DISTINCT
				{
				mT_DISTINCT(); 

				}
				break;
			case 86 :
				// Meta.g:1:689: T_COUNT
				{
				mT_COUNT(); 

				}
				break;
			case 87 :
				// Meta.g:1:697: T_AS
				{
				mT_AS(); 

				}
				break;
			case 88 :
				// Meta.g:1:702: T_BETWEEN
				{
				mT_BETWEEN(); 

				}
				break;
			case 89 :
				// Meta.g:1:712: T_ASC
				{
				mT_ASC(); 

				}
				break;
			case 90 :
				// Meta.g:1:718: T_DESC
				{
				mT_DESC(); 

				}
				break;
			case 91 :
				// Meta.g:1:725: T_LIKE
				{
				mT_LIKE(); 

				}
				break;
			case 92 :
				// Meta.g:1:732: T_SEMICOLON
				{
				mT_SEMICOLON(); 

				}
				break;
			case 93 :
				// Meta.g:1:744: T_EQUAL
				{
				mT_EQUAL(); 

				}
				break;
			case 94 :
				// Meta.g:1:752: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 

				}
				break;
			case 95 :
				// Meta.g:1:769: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 

				}
				break;
			case 96 :
				// Meta.g:1:784: T_COLON
				{
				mT_COLON(); 

				}
				break;
			case 97 :
				// Meta.g:1:792: T_COMMA
				{
				mT_COMMA(); 

				}
				break;
			case 98 :
				// Meta.g:1:800: T_START_PARENTHESIS
				{
				mT_START_PARENTHESIS(); 

				}
				break;
			case 99 :
				// Meta.g:1:820: T_END_PARENTHESIS
				{
				mT_END_PARENTHESIS(); 

				}
				break;
			case 100 :
				// Meta.g:1:838: T_QUOTE
				{
				mT_QUOTE(); 

				}
				break;
			case 101 :
				// Meta.g:1:846: T_SINGLE_QUOTE
				{
				mT_SINGLE_QUOTE(); 

				}
				break;
			case 102 :
				// Meta.g:1:861: T_INDEX_TYPE
				{
				mT_INDEX_TYPE(); 

				}
				break;
			case 103 :
				// Meta.g:1:874: T_START_BRACKET
				{
				mT_START_BRACKET(); 

				}
				break;
			case 104 :
				// Meta.g:1:890: T_END_BRACKET
				{
				mT_END_BRACKET(); 

				}
				break;
			case 105 :
				// Meta.g:1:904: T_PLUS
				{
				mT_PLUS(); 

				}
				break;
			case 106 :
				// Meta.g:1:911: T_SUBTRACT
				{
				mT_SUBTRACT(); 

				}
				break;
			case 107 :
				// Meta.g:1:922: T_INTERROGATION
				{
				mT_INTERROGATION(); 

				}
				break;
			case 108 :
				// Meta.g:1:938: T_ASTERISK
				{
				mT_ASTERISK(); 

				}
				break;
			case 109 :
				// Meta.g:1:949: T_GROUP
				{
				mT_GROUP(); 

				}
				break;
			case 110 :
				// Meta.g:1:957: T_AGGREGATION
				{
				mT_AGGREGATION(); 

				}
				break;
			case 111 :
				// Meta.g:1:971: T_MAX
				{
				mT_MAX(); 

				}
				break;
			case 112 :
				// Meta.g:1:977: T_MIN
				{
				mT_MIN(); 

				}
				break;
			case 113 :
				// Meta.g:1:983: T_AVG
				{
				mT_AVG(); 

				}
				break;
			case 114 :
				// Meta.g:1:989: T_GT
				{
				mT_GT(); 

				}
				break;
			case 115 :
				// Meta.g:1:994: T_LT
				{
				mT_LT(); 

				}
				break;
			case 116 :
				// Meta.g:1:999: T_GTE
				{
				mT_GTE(); 

				}
				break;
			case 117 :
				// Meta.g:1:1005: T_LTE
				{
				mT_LTE(); 

				}
				break;
			case 118 :
				// Meta.g:1:1011: T_NOT_EQUAL
				{
				mT_NOT_EQUAL(); 

				}
				break;
			case 119 :
				// Meta.g:1:1023: T_TOKEN
				{
				mT_TOKEN(); 

				}
				break;
			case 120 :
				// Meta.g:1:1031: QUOTED_LITERAL
				{
				mQUOTED_LITERAL(); 

				}
				break;
			case 121 :
				// Meta.g:1:1046: T_CONSTANT
				{
				mT_CONSTANT(); 

				}
				break;
			case 122 :
				// Meta.g:1:1057: T_IDENT
				{
				mT_IDENT(); 

				}
				break;
			case 123 :
				// Meta.g:1:1065: T_KS_AND_TN
				{
				mT_KS_AND_TN(); 

				}
				break;
			case 124 :
				// Meta.g:1:1077: T_TERM
				{
				mT_TERM(); 

				}
				break;
			case 125 :
				// Meta.g:1:1084: T_FLOAT
				{
				mT_FLOAT(); 

				}
				break;
			case 126 :
				// Meta.g:1:1092: T_PATH
				{
				mT_PATH(); 

				}
				break;
			case 127 :
				// Meta.g:1:1099: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA17 dfa17 = new DFA17(this);
	protected DFA20 dfa20 = new DFA20(this);
	static final String DFA17_eotS =
		"\5\uffff";
	static final String DFA17_eofS =
		"\5\uffff";
	static final String DFA17_minS =
		"\2\56\3\uffff";
	static final String DFA17_maxS =
		"\1\71\1\145\3\uffff";
	static final String DFA17_acceptS =
		"\2\uffff\1\2\1\1\1\3";
	static final String DFA17_specialS =
		"\5\uffff}>";
	static final String[] DFA17_transitionS = {
			"\1\2\1\uffff\12\1",
			"\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4",
			"",
			"",
			""
	};

	static final short[] DFA17_eot = DFA.unpackEncodedString(DFA17_eotS);
	static final short[] DFA17_eof = DFA.unpackEncodedString(DFA17_eofS);
	static final char[] DFA17_min = DFA.unpackEncodedStringToUnsignedChars(DFA17_minS);
	static final char[] DFA17_max = DFA.unpackEncodedStringToUnsignedChars(DFA17_maxS);
	static final short[] DFA17_accept = DFA.unpackEncodedString(DFA17_acceptS);
	static final short[] DFA17_special = DFA.unpackEncodedString(DFA17_specialS);
	static final short[][] DFA17_transition;

	static {
		int numStates = DFA17_transitionS.length;
		DFA17_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA17_transition[i] = DFA.unpackEncodedString(DFA17_transitionS[i]);
		}
	}

	protected class DFA17 extends DFA {

		public DFA17(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 17;
			this.eot = DFA17_eot;
			this.eof = DFA17_eof;
			this.min = DFA17_min;
			this.max = DFA17_max;
			this.accept = DFA17_accept;
			this.special = DFA17_special;
			this.transition = DFA17_transition;
		}
		@Override
		public String getDescription() {
			return "215:1: T_FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
		}
	}

	static final String DFA20_eotS =
		"\1\uffff\1\72\1\77\1\106\1\110\1\114\1\120\2\114\1\137\1\141\1\143\1\145"+
		"\22\114\11\uffff\1\u0089\2\114\3\uffff\1\u008b\2\uffff\1\114\1\u008e\1"+
		"\u0091\1\u0092\1\114\2\u0093\3\uffff\2\u0093\2\114\1\uffff\4\114\1\u0093"+
		"\1\114\1\uffff\1\114\1\uffff\3\114\1\uffff\3\114\1\uffff\16\114\1\uffff"+
		"\1\114\1\uffff\1\114\1\uffff\1\114\1\uffff\7\114\1\u00ca\6\114\1\u00d3"+
		"\1\u00d4\3\114\1\u00dd\15\114\1\u00ee\1\114\3\uffff\1\114\7\uffff\4\u0093"+
		"\1\70\1\uffff\6\114\1\u00fc\1\114\2\u0100\1\u0101\5\114\1\u0107\6\114"+
		"\1\u0110\3\114\1\u0114\1\u0115\1\u0116\4\114\1\u0100\10\114\1\u0124\1"+
		"\u0125\1\114\1\u0127\1\u0128\1\uffff\1\u0129\1\114\1\u012b\1\u012c\1\u012e"+
		"\3\114\2\uffff\10\114\1\uffff\1\u013a\2\114\1\u013d\14\114\1\uffff\2\114"+
		"\2\u0093\1\70\1\u0099\1\u014f\2\114\1\u0152\1\u0153\2\114\1\uffff\1\u00fc"+
		"\2\114\2\uffff\3\114\1\u015b\1\114\1\uffff\1\114\1\u015e\4\114\1\u0163"+
		"\1\114\1\uffff\1\114\1\u0166\1\114\3\uffff\2\114\1\u016a\12\114\2\uffff"+
		"\1\114\3\uffff\1\114\2\uffff\1\114\1\uffff\1\u0178\4\114\1\u017d\5\114"+
		"\1\uffff\2\114\1\uffff\1\u0185\3\114\1\u0189\1\114\1\u018b\1\u018c\1\114"+
		"\1\u018e\1\114\1\u0190\2\114\1\u0093\1\70\1\u0099\1\uffff\2\114\2\uffff"+
		"\2\114\1\u0198\4\114\1\uffff\2\114\1\uffff\4\114\1\uffff\1\u01a3\1\u01a4"+
		"\1\uffff\1\u01a5\1\u01a6\1\114\1\uffff\1\u01a8\6\114\1\u01af\1\114\1\u01b1"+
		"\3\114\1\uffff\1\114\1\u01b6\1\u01b7\1\114\1\uffff\1\u01b9\4\114\1\u01be"+
		"\1\u01bf\1\uffff\3\114\1\uffff\1\u01c3\2\uffff\1\114\1\uffff\1\114\1\uffff"+
		"\1\114\1\u01c7\1\u0099\1\114\1\u01c9\2\114\1\uffff\5\114\1\u01d1\4\114"+
		"\4\uffff\1\u01d6\1\uffff\2\114\1\u01d9\1\u01da\2\114\1\uffff\1\114\1\uffff"+
		"\3\114\1\u01e1\2\uffff\1\u01e2\1\uffff\1\u01e3\3\114\2\uffff\1\u01e7\1"+
		"\u01d9\1\114\1\uffff\1\u01ea\1\u01eb\1\114\1\uffff\1\u01d9\1\uffff\1\u01ed"+
		"\1\114\1\u01ef\1\u01f0\1\u01f1\1\u01f2\1\u01f3\1\uffff\1\u01f4\1\u01f5"+
		"\1\u01f6\1\114\1\uffff\1\u01f8\1\u01f9\2\uffff\1\114\1\u01fb\4\114\3\uffff"+
		"\1\u0200\1\114\1\u0202\1\uffff\2\114\2\uffff\1\u0205\1\uffff\1\u0206\10"+
		"\uffff\1\u0207\2\uffff\1\114\1\uffff\3\114\1\u020c\1\uffff\1\114\1\uffff"+
		"\2\114\3\uffff\2\114\1\u0212\1\114\1\uffff\1\114\1\u0215\2\114\1\u0218"+
		"\1\uffff\2\114\1\uffff\1\114\1\u021c\1\uffff\1\u021d\1\u021e\1\114\3\uffff"+
		"\1\u0220\1\uffff";
	static final String DFA20_eofS =
		"\u0221\uffff";
	static final String DFA20_minS =
		"\1\11\36\55\11\uffff\1\0\2\55\3\uffff\1\55\2\uffff\1\55\2\75\4\55\3\uffff"+
		"\1\55\1\53\2\55\1\uffff\6\55\1\uffff\1\55\1\uffff\3\55\1\uffff\3\55\1"+
		"\uffff\16\55\1\uffff\1\55\1\uffff\1\55\1\uffff\1\55\1\uffff\43\55\3\uffff"+
		"\1\55\7\uffff\2\55\1\53\1\55\1\60\1\uffff\60\55\1\uffff\10\55\2\uffff"+
		"\10\55\1\uffff\20\55\1\uffff\2\55\1\53\1\55\1\60\10\55\1\uffff\3\55\2"+
		"\uffff\5\55\1\uffff\10\55\1\uffff\3\55\3\uffff\15\55\2\uffff\1\55\3\uffff"+
		"\1\55\2\uffff\1\55\1\uffff\13\55\1\uffff\2\55\1\uffff\17\55\1\60\1\55"+
		"\1\uffff\2\55\2\uffff\7\55\1\uffff\2\55\1\uffff\4\55\1\uffff\2\55\1\uffff"+
		"\3\55\1\uffff\15\55\1\uffff\4\55\1\uffff\7\55\1\uffff\3\55\1\uffff\1\55"+
		"\2\uffff\1\55\1\uffff\1\55\1\uffff\7\55\1\uffff\12\55\4\uffff\1\55\1\uffff"+
		"\6\55\1\uffff\1\55\1\uffff\4\55\2\uffff\1\55\1\uffff\4\55\2\uffff\3\55"+
		"\1\uffff\3\55\1\uffff\1\55\1\uffff\7\55\1\uffff\4\55\1\uffff\2\55\2\uffff"+
		"\6\55\3\uffff\3\55\1\uffff\2\55\2\uffff\1\55\1\uffff\1\55\10\uffff\1\55"+
		"\2\uffff\1\55\1\uffff\4\55\1\uffff\1\55\1\uffff\2\55\3\uffff\4\55\1\uffff"+
		"\5\55\1\uffff\2\55\1\uffff\2\55\1\uffff\3\55\3\uffff\1\55\1\uffff";
	static final String DFA20_maxS =
		"\1\175\36\172\11\uffff\1\uffff\2\172\3\uffff\1\172\2\uffff\1\172\1\75"+
		"\1\76\4\172\3\uffff\4\172\1\uffff\6\172\1\uffff\1\172\1\uffff\3\172\1"+
		"\uffff\3\172\1\uffff\16\172\1\uffff\1\172\1\uffff\1\172\1\uffff\1\172"+
		"\1\uffff\43\172\3\uffff\1\172\7\uffff\4\172\1\71\1\uffff\60\172\1\uffff"+
		"\10\172\2\uffff\10\172\1\uffff\20\172\1\uffff\4\172\1\71\10\172\1\uffff"+
		"\3\172\2\uffff\5\172\1\uffff\10\172\1\uffff\3\172\3\uffff\15\172\2\uffff"+
		"\1\172\3\uffff\1\172\2\uffff\1\172\1\uffff\13\172\1\uffff\2\172\1\uffff"+
		"\17\172\1\71\1\172\1\uffff\2\172\2\uffff\7\172\1\uffff\2\172\1\uffff\4"+
		"\172\1\uffff\2\172\1\uffff\3\172\1\uffff\15\172\1\uffff\4\172\1\uffff"+
		"\7\172\1\uffff\3\172\1\uffff\1\172\2\uffff\1\172\1\uffff\1\172\1\uffff"+
		"\7\172\1\uffff\12\172\4\uffff\1\172\1\uffff\6\172\1\uffff\1\172\1\uffff"+
		"\4\172\2\uffff\1\172\1\uffff\4\172\2\uffff\3\172\1\uffff\3\172\1\uffff"+
		"\1\172\1\uffff\7\172\1\uffff\4\172\1\uffff\2\172\2\uffff\6\172\3\uffff"+
		"\3\172\1\uffff\2\172\2\uffff\1\172\1\uffff\1\172\10\uffff\1\172\2\uffff"+
		"\1\172\1\uffff\4\172\1\uffff\1\172\1\uffff\2\172\3\uffff\4\172\1\uffff"+
		"\5\172\1\uffff\2\172\1\uffff\2\172\1\uffff\3\172\3\uffff\1\172\1\uffff";
	static final String DFA20_acceptS =
		"\37\uffff\1\134\1\135\1\136\1\137\1\140\1\141\1\142\1\143\1\144\3\uffff"+
		"\1\147\1\150\1\151\1\uffff\1\153\1\154\7\uffff\1\176\1\177\1\1\4\uffff"+
		"\1\2\6\uffff\1\4\1\uffff\1\6\3\uffff\1\172\3\uffff\1\11\16\uffff\1\15"+
		"\1\uffff\1\17\1\uffff\1\21\1\uffff\1\23\43\uffff\1\145\1\170\1\152\1\uffff"+
		"\1\164\1\162\1\165\1\166\1\163\1\171\1\174\5\uffff\1\175\60\uffff\1\127"+
		"\10\uffff\1\35\1\112\10\uffff\1\72\20\uffff\1\122\15\uffff\1\173\3\uffff"+
		"\1\160\1\157\5\uffff\1\41\10\uffff\1\53\3\uffff\1\14\1\66\1\40\15\uffff"+
		"\1\47\1\37\1\uffff\1\50\1\75\1\131\1\uffff\1\161\1\77\1\uffff\1\31\13"+
		"\uffff\1\52\2\uffff\1\62\21\uffff\1\3\2\uffff\1\132\1\33\7\uffff\1\61"+
		"\2\uffff\1\71\4\uffff\1\44\2\uffff\1\74\3\uffff\1\16\15\uffff\1\32\4\uffff"+
		"\1\101\7\uffff\1\113\3\uffff\1\64\1\uffff\1\133\1\116\1\uffff\1\117\1"+
		"\uffff\1\121\7\uffff\1\5\12\uffff\1\34\1\54\1\167\1\73\1\uffff\1\20\6"+
		"\uffff\1\126\1\uffff\1\27\4\uffff\1\111\1\63\1\uffff\1\120\4\uffff\1\105"+
		"\1\45\3\uffff\1\123\3\uffff\1\155\1\uffff\1\114\7\uffff\1\106\4\uffff"+
		"\1\110\2\uffff\1\146\1\26\6\uffff\1\115\1\100\1\36\3\uffff\1\51\2\uffff"+
		"\1\65\1\107\1\uffff\1\124\1\uffff\1\7\1\10\1\67\1\76\1\12\1\103\1\13\1"+
		"\70\1\uffff\1\22\1\24\1\uffff\1\102\4\uffff\1\60\1\uffff\1\42\2\uffff"+
		"\1\130\1\125\1\25\4\uffff\1\30\5\uffff\1\43\2\uffff\1\56\2\uffff\1\104"+
		"\3\uffff\1\46\1\156\1\55\1\uffff\1\57";
	static final String DFA20_specialS =
		"\50\uffff\1\0\u01f8\uffff}>";
	static final String[] DFA20_transitionS = {
			"\2\71\2\uffff\1\71\22\uffff\1\71\1\uffff\1\47\4\uffff\1\50\1\45\1\46"+
			"\1\60\1\55\1\44\1\56\1\66\1\70\1\64\1\1\10\64\1\43\1\37\1\63\1\40\1\62"+
			"\1\57\1\uffff\1\17\1\36\1\16\1\2\1\24\1\27\1\61\1\3\1\23\1\35\1\20\1"+
			"\31\1\4\1\21\1\26\1\5\1\30\1\33\1\6\1\7\1\10\1\34\1\22\3\65\1\53\1\uffff"+
			"\1\54\1\uffff\1\67\1\uffff\1\17\1\36\1\52\1\11\1\24\1\27\1\61\1\12\1"+
			"\23\1\35\1\20\1\51\1\13\1\21\1\26\1\32\1\30\1\33\1\14\1\15\1\25\1\34"+
			"\1\22\3\65\1\41\1\uffff\1\42",
			"\1\70\1\73\1\70\12\64\7\uffff\4\67\1\74\25\67\4\uffff\1\67\1\uffff\4"+
			"\67\1\74\25\67",
			"\1\70\1\104\1\70\12\103\7\uffff\1\75\3\103\1\76\3\103\1\102\10\103\1"+
			"\100\10\103\4\uffff\1\103\1\uffff\4\103\1\101\3\103\1\102\10\103\1\100"+
			"\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\105\13\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\111\7\103\1\107\21\103\4\uffff\1\103"+
			"\1\uffff\1\111\7\103\1\112\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\115\5\103\1\113\10\103\4\uffff"+
			"\1\103\1\uffff\13\103\1\115\5\103\1\116\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\117\16\103\1\122\6\103\4\uffff"+
			"\1\103\1\uffff\4\103\1\121\16\103\1\122\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\125\6\103\1\127\6\103\1\131\2\103"+
			"\1\123\4\103\1\126\1\103\1\130\1\103\4\uffff\1\103\1\uffff\1\125\6\103"+
			"\1\127\6\103\1\131\2\103\1\124\4\103\1\126\1\103\1\130\1\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\132\13\103\1\135\2\103\1\133"+
			"\7\103\4\uffff\1\103\1\uffff\3\103\1\134\13\103\1\135\2\103\1\133\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\101\3\103\1\102\10\103\1\100"+
			"\10\103\4\uffff\1\103\1\uffff\1\136\3\103\1\101\3\103\1\102\10\103\1"+
			"\100\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\16\103"+
			"\1\140\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\111\7\103\1\112\21\103\4\uffff\1\103"+
			"\1\uffff\1\111\7\103\1\142\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\121\16\103\1\122\6\103\4\uffff"+
			"\1\103\1\uffff\4\103\1\144\16\103\1\122\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\125\6\103\1\127\6\103\1\131\2\103"+
			"\1\124\4\103\1\126\1\103\1\130\1\103\4\uffff\1\103\1\uffff\1\125\6\103"+
			"\1\127\6\103\1\131\2\103\1\124\4\103\1\126\1\103\1\130\1\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\151\2\103\1\150\2\103\1\147"+
			"\2\103\1\146\5\103\4\uffff\1\103\1\uffff\13\103\1\151\2\103\1\150\2\103"+
			"\1\147\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\154\2\103\1\156\4\103\1\152"+
			"\1\103\1\153\4\103\1\155\2\103\1\157\4\103\4\uffff\1\103\1\uffff\3\103"+
			"\1\154\2\103\1\156\4\103\1\152\1\103\1\153\4\103\1\155\2\103\1\157\4"+
			"\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\160\25\103\4\uffff\1\103\1\uffff"+
			"\4\103\1\160\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\161\13\103\4\uffff\1\103\1"+
			"\uffff\16\103\1\161\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\7\103\1\163\1\162\21\103\4\uffff\1\103"+
			"\1\uffff\7\103\1\163\1\162\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\5\103\1\164\7\103\1\165\14\103\4\uffff"+
			"\1\103\1\uffff\5\103\1\164\7\103\1\165\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\167\26\103\1\166\2\103\4\uffff\1\103"+
			"\1\uffff\1\167\26\103\1\166\2\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\134\13\103\1\135\2\103\1\133"+
			"\7\103\4\uffff\1\103\1\uffff\3\103\1\134\13\103\1\135\2\103\1\133\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\171\1\103\1\170\1\103\1\172"+
			"\10\103\4\uffff\1\103\1\uffff\15\103\1\171\1\103\1\170\1\103\1\172\10"+
			"\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\173\15\103\1\174\2\103\1\175\10\103"+
			"\4\uffff\1\103\1\uffff\1\173\15\103\1\174\2\103\1\175\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\176\5\103\4\uffff\1\103\1\uffff"+
			"\24\103\1\176\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u0082\7\103\1\u0081\5\103\1\u0080"+
			"\5\103\1\177\5\103\4\uffff\1\103\1\uffff\1\u0082\7\103\1\u0081\5\103"+
			"\1\u0080\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\115\5\103\1\116\10\103\4\uffff"+
			"\1\103\1\uffff\13\103\1\115\5\103\1\116\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0083\11\103\1\u0084\13\103"+
			"\4\uffff\1\103\1\uffff\4\103\1\u0083\11\103\1\u0084\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u0085\31\103\4\uffff\1\103\1\uffff"+
			"\1\u0085\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0086\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u0086\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0088\23\103\1\u0087\1\103\4"+
			"\uffff\1\103\1\uffff\4\103\1\u0088\23\103\1\u0087\1\103",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\0\u008a",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u0082\7\103\1\u0081\5\103\1\u0080"+
			"\13\103\4\uffff\1\103\1\uffff\1\u0082\7\103\1\u0081\5\103\1\u0080\13"+
			"\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\151\2\103\1\150\2\103\1\147"+
			"\10\103\4\uffff\1\103\1\uffff\13\103\1\151\2\103\1\150\2\103\1\147\10"+
			"\103",
			"",
			"",
			"",
			"\15\70\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u008c\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u008c\10\103",
			"\1\u008d",
			"\1\u008f\1\u0090",
			"\1\70\1\73\1\70\12\64\7\uffff\4\67\1\74\25\67\4\uffff\1\67\1\uffff\4"+
			"\67\1\74\25\67",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\67\1\70\12\u0094\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\1\70\1\67\1\70\12\67\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"",
			"",
			"",
			"\1\70\1\67\1\70\12\u0095\7\uffff\4\67\1\u0096\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0096\25\67",
			"\1\u0099\1\uffff\1\u0098\1\67\1\70\12\u0097\7\uffff\32\67\4\uffff\1"+
			"\67\1\uffff\32\67",
			"\1\70\1\104\1\70\12\103\7\uffff\30\103\1\u009a\1\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\5\103\1\u009b\5\103\1\u009c\6\103\1"+
			"\u009d\7\103\4\uffff\1\103\1\uffff\13\103\1\u009c\6\103\1\u009d\7\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u009e\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u009e\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u009c\6\103\1\u009d\7\103\4"+
			"\uffff\1\103\1\uffff\13\103\1\u009c\6\103\1\u009d\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u009f\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u009f\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\67\1\70\12\67\7\uffff\32\u00a0\4\uffff\1\67\1\uffff\32\u00a0",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u00a1\5\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u00a2\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u00a3\14\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\27\103\1\u00a4\2\103\4\uffff\1\103\1"+
			"\uffff\27\103\1\u00a4\2\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u00a3\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u00a3\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u00a7\5\103\1\u00a5\13\103"+
			"\4\uffff\1\103\1\uffff\10\103\1\u00a7\5\103\1\u00a6\13\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u00a8\31\103\4\uffff\1\103\1\uffff"+
			"\1\u00a8\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u00a7\5\103\1\u00a6\13\103"+
			"\4\uffff\1\103\1\uffff\10\103\1\u00a7\5\103\1\u00a6\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u00a9\10\103\1\u00ab\7\103\1"+
			"\u00aa\6\103\4\uffff\1\103\1\uffff\13\103\1\u00ab\7\103\1\u00aa\6\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u00ab\7\103\1\u00aa\6\103\4"+
			"\uffff\1\103\1\uffff\13\103\1\u00ab\7\103\1\u00aa\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u00ac\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u00ac\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u00ad\13\103\1\u00ae\5\103"+
			"\4\uffff\1\103\1\uffff\10\103\1\u00af\13\103\1\u00ae\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u00af\13\103\1\u00ae\5\103"+
			"\4\uffff\1\103\1\uffff\10\103\1\u00af\13\103\1\u00ae\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\103\1\u00b0\30\103\4\uffff\1\103\1"+
			"\uffff\1\103\1\u00b0\30\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u00b1\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u00b1\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u00b2\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u00b2\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\17\103\1\u00b3\12\103\4\uffff\1\103"+
			"\1\uffff\17\103\1\u00b3\12\103",
			"\1\70\1\104\1\70\12\103\7\uffff\12\103\1\u00b4\17\103\4\uffff\1\103"+
			"\1\uffff\12\103\1\u00b4\17\103",
			"\1\70\1\104\1\70\12\103\7\uffff\5\103\1\u00b5\24\103\4\uffff\1\103\1"+
			"\uffff\5\103\1\u00b6\24\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u00b7\3\103\1\u00b8\21\103\4"+
			"\uffff\1\103\1\uffff\4\103\1\u00b7\3\103\1\u00b8\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\5\103\1\u00b6\24\103\4\uffff\1\103\1"+
			"\uffff\5\103\1\u00b6\24\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\u00b9\26\103\4\uffff\1\103\1"+
			"\uffff\3\103\1\u00b9\26\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\30\103"+
			"\1\u00ba\1\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\24\103"+
			"\1\u00bb\5\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u00a3\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u00bc\14\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u00ab\7\103\1\u00aa\6\103\4"+
			"\uffff\1\103\1\uffff\2\103\1\u00bd\10\103\1\u00ab\7\103\1\u00aa\6\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u00be\7\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u00bf\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u00bf\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u00c1\1\u00c0\6\103\1\u00c2"+
			"\5\103\4\uffff\1\103\1\uffff\14\103\1\u00c1\1\u00c0\6\103\1\u00c2\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u00c3\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u00c3\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u00c5\7\103\1\u00c4\6\103\4"+
			"\uffff\1\103\1\uffff\13\103\1\u00c5\7\103\1\u00c4\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u00c7\2\103\1\u00c6\24\103\1\u00c8"+
			"\1\103\4\uffff\1\103\1\uffff\1\u00c7\2\103\1\u00c6\24\103\1\u00c8\1\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\u00c9\26\103\4\uffff\1\103\1"+
			"\uffff\3\103\1\u00c9\26\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u00cb\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u00cb\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u00cc\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u00cc\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u00cd\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u00cd\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\30\103\1\u00ce\1\103\4\uffff\1\103\1"+
			"\uffff\30\103\1\u00ce\1\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u00cf\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u00cf\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u00d1\5\103\1\u00d0\6\103\4"+
			"\uffff\1\103\1\uffff\15\103\1\u00d1\5\103\1\u00d0\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u00d2\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u00d2\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\u00d5\11\103\1\u00d8\4\103\1"+
			"\u00d6\1\u00d7\6\103\4\uffff\1\103\1\uffff\3\103\1\u00d5\11\103\1\u00d8"+
			"\4\103\1\u00d6\1\u00d7\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u00d9\6\103\1\u00da\12\103"+
			"\4\uffff\1\103\1\uffff\10\103\1\u00d9\6\103\1\u00da\12\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u00db\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u00db\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u00dc\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u00dc\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u00de\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u00de\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\u00df\26\103\4\uffff\1\103\1"+
			"\uffff\3\103\1\u00df\26\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u00e0\16\103\4\uffff\1\103"+
			"\1\uffff\13\103\1\u00e0\16\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u00e1\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u00e1\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u00e2\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u00e2\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u00e3\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u00e3\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u00e4\27\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u00e5\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u00e5\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\12\103\1\u00e8\1\103\1\u00e7\5\103\1"+
			"\u00e6\7\103\4\uffff\1\103\1\uffff\12\103\1\u00e8\1\103\1\u00e7\5\103"+
			"\1\u00e6\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u00e9\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u00e9\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u00ea\15\103\4\uffff\1\103"+
			"\1\uffff\14\103\1\u00ea\15\103",
			"\1\70\1\104\1\70\12\103\7\uffff\26\103\1\u00eb\3\103\4\uffff\1\103\1"+
			"\uffff\26\103\1\u00eb\3\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u00ec\16\103\4\uffff\1\103"+
			"\1\uffff\13\103\1\u00ec\16\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u00ed\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u00ed\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u00ef\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u00ef\6\103",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u00f0\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u00f0\13\103",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\70\1\67\1\70\12\u0094\7\uffff\4\67\1\u00f1\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u00f1\25\67",
			"\1\70\1\67\1\70\12\u0095\7\uffff\4\67\1\u0096\25\67\4\uffff\1\67\1\uffff"+
			"\4\67\1\u0096\25\67",
			"\1\u0099\1\uffff\1\u00f3\1\67\1\70\12\u00f2\7\uffff\32\67\4\uffff\1"+
			"\67\1\uffff\32\67",
			"\1\70\1\67\1\70\12\u0097\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\12\u00f4",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u00f5\7\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u00f6\31\103\4\uffff\1\103\1\uffff"+
			"\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u00f7\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u00f7\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u00f8\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u00f8\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\17\103\1\u00f9\12\103\4\uffff\1\103"+
			"\1\uffff\17\103\1\u00f9\12\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u00fa\22\103\1\u00fb\6\103\4\uffff"+
			"\1\103\1\uffff\1\u00fa\22\103\1\u00fb\6\103",
			"\1\70\1\67\1\70\12\u00fd\7\uffff\32\u00fd\4\uffff\1\u00fd\1\uffff\32"+
			"\u00fd",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u00fe\10\103\4\uffff\1\103"+
			"\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u00ff\5\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u0102\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u0103\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u0103\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u0103\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u0104\15\103\4\uffff\1\103"+
			"\1\uffff\14\103\1\u0104\15\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0105\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0105\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0106\13\103\4\uffff\1\103"+
			"\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0108\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0108\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\17\103\1\u0109\1\103\1\u010a\10\103"+
			"\4\uffff\1\103\1\uffff\17\103\1\u0109\1\103\1\u010a\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u010b\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u010c\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u010e\10\103\1\u010d\14\103"+
			"\4\uffff\1\103\1\uffff\4\103\1\u010e\10\103\1\u010d\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u010c\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u010c\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u010f\16\103\4\uffff\1\103"+
			"\1\uffff\13\103\1\u010f\16\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0111\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0111\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0112\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0112\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0113\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0113\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0117\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0117\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u0118\31\103\4\uffff\1\103\1\uffff"+
			"\1\u0118\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\22\103"+
			"\1\u0119\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\21\103"+
			"\1\u011a\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\24\103"+
			"\1\u011b\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\16\103"+
			"\1\u011c\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u011d\6\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u011e\31\103\4\uffff\1\103\1\uffff"+
			"\1\u011e\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u011f\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u011f\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\17\103\1\u0120\12\103\4\uffff\1\103"+
			"\1\uffff\17\103\1\u0120\12\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0121\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0121\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u0122\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u0122\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0123\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0123\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u0126\16\103\4\uffff\1\103"+
			"\1\uffff\13\103\1\u0126\16\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u012a\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u012a\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u012d\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u012d\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\7\103\1\u012f\22\103\4\uffff\1\103\1"+
			"\uffff\7\103\1\u012f\22\103",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\u0130\26\103\4\uffff\1\103\1"+
			"\uffff\3\103\1\u0130\26\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u0131\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u0131\10\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0132\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0132\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0133\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0133\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0134\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u0134\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0135\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0135\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u0136\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u0136\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u0137\16\103\4\uffff\1\103"+
			"\1\uffff\13\103\1\u0137\16\103",
			"\1\70\1\104\1\70\12\103\7\uffff\7\103\1\u0138\22\103\4\uffff\1\103\1"+
			"\uffff\7\103\1\u0138\22\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u0139\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u0139\21\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u013b\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u013b\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u013c\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u013c\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u013e\15\103\4\uffff\1\103"+
			"\1\uffff\14\103\1\u013e\15\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u013f\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u013f\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0140\25\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u0141\31\103\4\uffff\1\103\1\uffff"+
			"\1\u0141\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u0142\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u0142\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u0143\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u0143\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0144\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0144\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u0145\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u0145\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0146\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u0146\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u0147\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u0147\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u0148\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u0148\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0149\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0149\14\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\26\103\1\u014a\3\103\4\uffff\1\103\1"+
			"\uffff\26\103\1\u014a\3\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u014b\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u014b\5\103",
			"\1\u0099\1\uffff\1\u014d\1\67\1\70\12\u014c\7\uffff\32\67\4\uffff\1"+
			"\67\1\uffff\32\67",
			"\1\70\1\67\1\70\12\u00f2\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\12\u014e",
			"\3\70\12\u00f4\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u0150\5\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u0151\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u0151\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\103\1\u0154\30\103\4\uffff\1\103\1"+
			"\uffff\1\103\1\u0154\30\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u0155\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u0155\21\103",
			"",
			"\1\70\1\67\1\70\12\u00fd\7\uffff\32\u00fd\4\uffff\1\u00fd\1\uffff\32"+
			"\u00fd",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u0156\7\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u0157\6\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0158\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0159\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0159\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0159\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u015a\31\103\4\uffff\1\103\1\uffff"+
			"\1\u015a\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u015c\14\103\4\uffff\1\103"+
			"\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u015d\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u015d\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u015f\31\103\4\uffff\1\103\1\uffff"+
			"\1\u015f\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u0160\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u0161\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u0161\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u0161\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u0162\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u0162\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0164\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0164\25\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0165\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0165\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0167\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0167\14\103",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u0168\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u0168\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u0169\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u0169\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\22\103"+
			"\1\u016b\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\23\103"+
			"\1\u016c\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\15\103"+
			"\1\u016d\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u016e\13\103\4\uffff\1\103"+
			"\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u016f\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u016f\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u0170\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u0170\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u0171\31\103\4\uffff\1\103\1\uffff"+
			"\1\u0171\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u0172\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u0172\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u0173\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u0173\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u0174\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u0174\10\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\30\103\1\u0175\1\103\4\uffff\1\103\1"+
			"\uffff\30\103\1\u0175\1\103",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0176\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0176\25\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\17\103\1\u0177\12\103\4\uffff\1\103"+
			"\1\uffff\17\103\1\u0177\12\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0179\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u0179\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u017a\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u017a\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\27\103\1\u017b\2\103\4\uffff\1\103\1"+
			"\uffff\27\103\1\u017b\2\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u017c\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u017c\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u017e\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u017e\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u017f\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u017f\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u0180\31\103\4\uffff\1\103\1\uffff"+
			"\1\u0180\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\u0181\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0182\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u0182\13\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u0183\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u0183\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0184\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0184\25\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u0186\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u0186\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0187\14\103\4\uffff\1\103"+
			"\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u0188\16\103\4\uffff\1\103"+
			"\1\uffff\13\103\1\u0188\16\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u018a\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u018a\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\25\103\1\u018d\4\103\4\uffff\1\103\1"+
			"\uffff\25\103\1\u018d\4\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u018f\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u018f\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0191\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0191\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\17\103\1\u0192\12\103\4\uffff\1\103"+
			"\1\uffff\17\103\1\u0192\12\103",
			"\1\70\1\67\1\70\12\u014c\7\uffff\32\67\4\uffff\1\67\1\uffff\32\67",
			"\12\u0193",
			"\3\70\12\u014e\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u0194\16\103\4\uffff\1\103"+
			"\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0195\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u0195\25\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\13\103\1\u0196\16\103\4\uffff\1\103"+
			"\1\uffff\13\103\1\u0196\16\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0197\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0197\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u0199\25\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u019a\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u019b\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u019b\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u019b\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u019c\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u019c\10\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\3\103\1\u019d\26\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u019e\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u019e\6\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u019f\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u019f\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01a0\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01a1\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01a1\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01a1\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u01a2\31\103\4\uffff\1\103\1\uffff"+
			"\1\u01a2\31\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01a7\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01a7\25\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\4\103\1"+
			"\u01a9\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\3\103\1"+
			"\u01aa\26\103",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u01ab\15\103\4\uffff\1\103"+
			"\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01ac\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01ac\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01ad\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u01ad\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u01ae\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u01ae\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01b0\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01b0\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01b2\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u01b2\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u01b3\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u01b3\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u01b4\31\103\4\uffff\1\103\1\uffff"+
			"\1\u01b4\31\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\26\103\1\u01b5\3\103\4\uffff\1\103\1"+
			"\uffff\26\103\1\u01b5\3\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01b8\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u01b8\6\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01ba\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u01ba\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u01bb\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u01bb\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\20\103\1\u01bc\11\103\4\uffff\1\103"+
			"\1\uffff\20\103\1\u01bc\11\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u01bd\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u01bd\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u01c0\15\103\4\uffff\1\103"+
			"\1\uffff\14\103\1\u01c0\15\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01c1\25\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\u01c2\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01c4\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01c4\25\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01c5\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u01c5\7\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01c6\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01c6\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\3\70\12\u0193\7\uffff\32\70\4\uffff\1\70\1\uffff\32\70",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01c8\6\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01ca\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01ca\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u01cb\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u01cb\27\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01cc\7\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01cd\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u01ce\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01ce\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u01ce\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\30\103\1\u01cf\1\103\4\uffff\1\103\1"+
			"\uffff\30\103\1\u01cf\1\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01d0\7\103\4\uffff\1\103\1"+
			"\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01d2\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01d2\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u01d3\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u01d4\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u01d4\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u01d4\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01d5\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u01d5\6\103",
			"",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\22\103"+
			"\1\u01d7\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\22\103"+
			"\1\u01d8\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01db\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u01db\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01dc\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u01dc\6\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u01dd\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u01dd\10\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u01de\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u01de\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\1\u01df\31\103\4\uffff\1\103\1\uffff"+
			"\1\u01df\31\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u01e0\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u01e0\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u01e4\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u01e4\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u01e5\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u01e5\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u01e6\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u01e6\7\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u01e8\1\103\1\u01e9\11\103"+
			"\4\uffff\1\103\1\uffff\16\103\1\u01e8\1\103\1\u01e9\11\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u01ec\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u01ec\14\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01ee\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u01ee\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01f7\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01f7\25\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01fa\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01fa\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u01fc\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u01fc\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u01fd\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u01fd\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\23\103\1\u01fe\6\103\4\uffff\1\103\1"+
			"\uffff\23\103\1\u01fe\6\103",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u01ff\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u01ff\25\103",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0201\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u0201\13\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0203\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0203\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u0204\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u0204\5\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0208\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0208\14\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0209\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0209\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\22\103\1\u020a\7\103\4\uffff\1\103\1"+
			"\uffff\22\103\1\u020a\7\103",
			"\1\70\1\104\1\70\12\103\7\uffff\10\103\1\u020b\21\103\4\uffff\1\103"+
			"\1\uffff\10\103\1\u020b\21\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u020d\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u020d\10\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\4\103\1\u020e\25\103\4\uffff\1\103\1"+
			"\uffff\4\103\1\u020e\25\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u020f\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u020f\13\103",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\2\103\1\u0210\27\103\4\uffff\1\103\1"+
			"\uffff\2\103\1\u0210\27\103",
			"\1\70\1\104\1\70\12\103\7\uffff\6\103\1\u0211\23\103\4\uffff\1\103\1"+
			"\uffff\6\103\1\u0211\23\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\16\103\1\u0213\13\103\4\uffff\1\103"+
			"\1\uffff\16\103\1\u0213\13\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u0214\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u0214\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\21\103\1\u0216\10\103\4\uffff\1\103"+
			"\1\uffff\21\103\1\u0216\10\103",
			"\1\70\1\104\1\70\12\103\7\uffff\30\103\1\u0217\1\103\4\uffff\1\103\1"+
			"\uffff\30\103\1\u0217\1\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\15\103\1\u0219\14\103\4\uffff\1\103"+
			"\1\uffff\15\103\1\u0219\14\103",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u021a\15\103\4\uffff\1\103"+
			"\1\uffff\14\103\1\u021a\15\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\24\103\1\u021b\5\103\4\uffff\1\103\1"+
			"\uffff\24\103\1\u021b\5\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			"\1\70\1\104\1\70\12\103\7\uffff\14\103\1\u021f\15\103\4\uffff\1\103"+
			"\1\uffff\14\103\1\u021f\15\103",
			"",
			"",
			"",
			"\1\70\1\104\1\70\12\103\7\uffff\32\103\4\uffff\1\103\1\uffff\32\103",
			""
	};

	static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
	static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
	static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
	static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
	static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
	static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
	static final short[][] DFA20_transition;

	static {
		int numStates = DFA20_transitionS.length;
		DFA20_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
		}
	}

	protected class DFA20 extends DFA {

		public DFA20(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 20;
			this.eot = DFA20_eot;
			this.eof = DFA20_eof;
			this.min = DFA20_min;
			this.max = DFA20_max;
			this.accept = DFA20_accept;
			this.special = DFA20_special;
			this.transition = DFA20_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__141 | T__142 | T__143 | T__144 | T__145 | T__146 | T__147 | T__148 | T__149 | T__150 | T__151 | T__152 | T__153 | T__154 | T__155 | T__156 | T__157 | T__158 | T__159 | T__160 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_ON | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_WHERE | T_IN | T_FROM | T_DELETE | T_WINDOW | T_LAST | T_ROWS | T_INNER | T_JOIN | T_BY | T_LIMIT | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_SEMICOLON | T_EQUAL | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_PARENTHESIS | T_END_PARENTHESIS | T_QUOTE | T_SINGLE_QUOTE | T_INDEX_TYPE | T_START_BRACKET | T_END_BRACKET | T_PLUS | T_SUBTRACT | T_INTERROGATION | T_ASTERISK | T_GROUP | T_AGGREGATION | T_MAX | T_MIN | T_AVG | T_GT | T_LT | T_GTE | T_LTE | T_NOT_EQUAL | T_TOKEN | QUOTED_LITERAL | T_CONSTANT | T_IDENT | T_KS_AND_TN | T_TERM | T_FLOAT | T_PATH | WS );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA20_40 = input.LA(1);
						s = -1;
						if ( ((LA20_40 >= '\u0000' && LA20_40 <= '\uFFFF')) ) {s = 138;}
						else s = 137;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 20, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
