// $ANTLR 3.5.1 Meta.g 2014-02-10 09:11:49

    package com.stratio.sdh.meta.generated;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaLexer extends Lexer {
	public static final int EOF=-1;
	public static final int T__85=85;
	public static final int T__86=86;
	public static final int T__87=87;
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
	public static final int T_ADD=26;
	public static final int T_ALL=27;
	public static final int T_ALTER=28;
	public static final int T_ANALYTICS=29;
	public static final int T_AND=30;
	public static final int T_ANY=31;
	public static final int T_COLON=32;
	public static final int T_COMMA=33;
	public static final int T_CONSISTENCY=34;
	public static final int T_CONSTANT=35;
	public static final int T_CREATE=36;
	public static final int T_DROP=37;
	public static final int T_EACH_QUORUM=38;
	public static final int T_END_SBRACKET=39;
	public static final int T_EQUAL=40;
	public static final int T_EXISTS=41;
	public static final int T_EXPLAIN=42;
	public static final int T_FALSE=43;
	public static final int T_FOR=44;
	public static final int T_IDENT=45;
	public static final int T_IF=46;
	public static final int T_INDEX=47;
	public static final int T_INDEX_TYPE=48;
	public static final int T_KEYSPACE=49;
	public static final int T_LEFT_PARENTHESIS=50;
	public static final int T_LIST=51;
	public static final int T_LOCAL_ONE=52;
	public static final int T_LOCAL_QUORUM=53;
	public static final int T_NOT=54;
	public static final int T_ON=55;
	public static final int T_ONE=56;
	public static final int T_OPTIONS=57;
	public static final int T_PATH=58;
	public static final int T_PLAN=59;
	public static final int T_POINT=60;
	public static final int T_QUORUM=61;
	public static final int T_QUOTE=62;
	public static final int T_REMOVE=63;
	public static final int T_RIGHT_PARENTHESIS=64;
	public static final int T_SEMICOLON=65;
	public static final int T_SET=66;
	public static final int T_START_SBRACKET=67;
	public static final int T_TABLE=68;
	public static final int T_TERM=69;
	public static final int T_THREE=70;
	public static final int T_TRUE=71;
	public static final int T_TRUNCATE=72;
	public static final int T_TWO=73;
	public static final int T_UDF=74;
	public static final int T_USE=75;
	public static final int T_USING=76;
	public static final int T_WITH=77;
	public static final int U=78;
	public static final int V=79;
	public static final int W=80;
	public static final int WS=81;
	public static final int X=82;
	public static final int Y=83;
	public static final int Z=84;

	    


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

	// $ANTLR start "T__85"
	public final void mT__85() throws RecognitionException {
		try {
			int _type = T__85;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:14:7: ( 'PROCESS' )
			// Meta.g:14:9: 'PROCESS'
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
	// $ANTLR end "T__85"

	// $ANTLR start "T__86"
	public final void mT__86() throws RecognitionException {
		try {
			int _type = T__86;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:15:7: ( 'TRIGGER' )
			// Meta.g:15:9: 'TRIGGER'
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
	// $ANTLR end "T__86"

	// $ANTLR start "T__87"
	public final void mT__87() throws RecognitionException {
		try {
			int _type = T__87;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:16:7: ( 'UDF' )
			// Meta.g:16:9: 'UDF'
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
	// $ANTLR end "T__87"

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// Meta.g:70:11: ( ( 'a' | 'A' ) )
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
			// Meta.g:71:11: ( ( 'b' | 'B' ) )
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
			// Meta.g:72:11: ( ( 'c' | 'C' ) )
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
			// Meta.g:73:11: ( ( 'd' | 'D' ) )
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
			// Meta.g:74:11: ( ( 'e' | 'E' ) )
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
			// Meta.g:75:11: ( ( 'f' | 'F' ) )
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
			// Meta.g:76:11: ( ( 'g' | 'G' ) )
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
			// Meta.g:77:11: ( ( 'h' | 'H' ) )
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
			// Meta.g:78:11: ( ( 'i' | 'I' ) )
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
			// Meta.g:79:11: ( ( 'j' | 'J' ) )
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
			// Meta.g:80:11: ( ( 'k' | 'K' ) )
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
			// Meta.g:81:11: ( ( 'l' | 'L' ) )
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
			// Meta.g:82:11: ( ( 'm' | 'M' ) )
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
			// Meta.g:83:11: ( ( 'n' | 'N' ) )
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
			// Meta.g:84:11: ( ( 'o' | 'O' ) )
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
			// Meta.g:85:11: ( ( 'p' | 'P' ) )
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
			// Meta.g:86:11: ( ( 'q' | 'Q' ) )
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
			// Meta.g:87:11: ( ( 'r' | 'R' ) )
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
			// Meta.g:88:11: ( ( 's' | 'S' ) )
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
			// Meta.g:89:11: ( ( 't' | 'T' ) )
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
			// Meta.g:90:11: ( ( 'u' | 'U' ) )
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
			// Meta.g:91:11: ( ( 'v' | 'V' ) )
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
			// Meta.g:92:11: ( ( 'w' | 'W' ) )
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
			// Meta.g:93:11: ( ( 'x' | 'X' ) )
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
			// Meta.g:94:11: ( ( 'y' | 'Y' ) )
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
			// Meta.g:95:11: ( ( 'z' | 'Z' ) )
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

	// $ANTLR start "T_TRUNCATE"
	public final void mT_TRUNCATE() throws RecognitionException {
		try {
			int _type = T_TRUNCATE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:98:11: ( T R U N C A T E )
			// Meta.g:98:13: T R U N C A T E
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
			// Meta.g:99:9: ( C R E A T E )
			// Meta.g:99:11: C R E A T E
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
			// Meta.g:100:8: ( A L T E R )
			// Meta.g:100:10: A L T E R
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
			// Meta.g:101:11: ( K E Y S P A C E )
			// Meta.g:101:13: K E Y S P A C E
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
			// Meta.g:102:6: ( N O T )
			// Meta.g:102:8: N O T
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
			// Meta.g:103:7: ( W I T H )
			// Meta.g:103:9: W I T H
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
			// Meta.g:104:7: ( D R O P )
			// Meta.g:104:9: D R O P
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
			// Meta.g:105:8: ( T A B L E )
			// Meta.g:105:10: T A B L E
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
			// Meta.g:106:5: ( I F )
			// Meta.g:106:7: I F
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
			// Meta.g:107:9: ( E X I S T S )
			// Meta.g:107:11: E X I S T S
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
			// Meta.g:108:6: ( A N D )
			// Meta.g:108:8: A N D
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
			// Meta.g:109:6: ( U S E )
			// Meta.g:109:8: U S E
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
			// Meta.g:110:6: ( S E T )
			// Meta.g:110:8: S E T
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
			// Meta.g:111:10: ( O P T I O N S )
			// Meta.g:111:12: O P T I O N S
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
			// Meta.g:112:12: ( A N A L Y T I C S )
			// Meta.g:112:14: A N A L Y T I C S
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
			// Meta.g:113:7: ( T R U E )
			// Meta.g:113:9: T R U E
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
			// Meta.g:114:8: ( F A L S E )
			// Meta.g:114:10: F A L S E
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
			// Meta.g:115:14: ( C O N S I S T E N C Y )
			// Meta.g:115:16: C O N S I S T E N C Y
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
			// Meta.g:116:6: ( A L L )
			// Meta.g:116:8: A L L
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
			// Meta.g:117:6: ( A N Y )
			// Meta.g:117:8: A N Y
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
			// Meta.g:118:9: ( Q U O R U M )
			// Meta.g:118:11: Q U O R U M
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
			// Meta.g:119:6: ( O N E )
			// Meta.g:119:8: O N E
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
			// Meta.g:120:6: ( T W O )
			// Meta.g:120:8: T W O
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
			// Meta.g:121:8: ( T H R E E )
			// Meta.g:121:10: T H R E E
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
			// Meta.g:122:14: ( E A C H '_' Q U O R U M )
			// Meta.g:122:16: E A C H '_' Q U O R U M
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
			// Meta.g:123:12: ( L O C A L '_' O N E )
			// Meta.g:123:14: L O C A L '_' O N E
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
			// Meta.g:124:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:124:17: L O C A L '_' Q U O R U M
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
			// Meta.g:125:10: ( E X P L A I N )
			// Meta.g:125:12: E X P L A I N
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
			// Meta.g:126:7: ( P L A N )
			// Meta.g:126:9: P L A N
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
			// Meta.g:127:6: ( F O R )
			// Meta.g:127:8: F O R
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
			// Meta.g:128:8: ( I N D E X )
			// Meta.g:128:10: I N D E X
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

	// $ANTLR start "T_ADD"
	public final void mT_ADD() throws RecognitionException {
		try {
			int _type = T_ADD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:129:6: ( A D D )
			// Meta.g:129:8: A D D
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

	// $ANTLR start "T_LIST"
	public final void mT_LIST() throws RecognitionException {
		try {
			int _type = T_LIST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:130:7: ( L I S T )
			// Meta.g:130:9: L I S T
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
			// Meta.g:131:9: ( R E M O V E )
			// Meta.g:131:11: R E M O V E
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

	// $ANTLR start "T_ON"
	public final void mT_ON() throws RecognitionException {
		try {
			int _type = T_ON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:132:5: ( O N )
			// Meta.g:132:7: O N
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
			// Meta.g:133:8: ( U S I N G )
			// Meta.g:133:10: U S I N G
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

	// $ANTLR start "T_UDF"
	public final void mT_UDF() throws RecognitionException {
		try {
			int _type = T_UDF;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:134:6: ( U D F )
			// Meta.g:134:8: U D F
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

	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:136:12: ( ';' )
			// Meta.g:136:14: ';'
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
			// Meta.g:137:8: ( '=' )
			// Meta.g:137:10: '='
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

	// $ANTLR start "T_POINT"
	public final void mT_POINT() throws RecognitionException {
		try {
			int _type = T_POINT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:138:8: ( '.' )
			// Meta.g:138:10: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_POINT"

	// $ANTLR start "T_START_SBRACKET"
	public final void mT_START_SBRACKET() throws RecognitionException {
		try {
			int _type = T_START_SBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:139:17: ( '{' )
			// Meta.g:139:19: '{'
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
			// Meta.g:140:15: ( '}' )
			// Meta.g:140:17: '}'
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
			// Meta.g:141:8: ( ':' )
			// Meta.g:141:10: ':'
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
			// Meta.g:142:8: ( ',' )
			// Meta.g:142:10: ','
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

	// $ANTLR start "T_LEFT_PARENTHESIS"
	public final void mT_LEFT_PARENTHESIS() throws RecognitionException {
		try {
			int _type = T_LEFT_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:143:19: ( '(' )
			// Meta.g:143:21: '('
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
	// $ANTLR end "T_LEFT_PARENTHESIS"

	// $ANTLR start "T_RIGHT_PARENTHESIS"
	public final void mT_RIGHT_PARENTHESIS() throws RecognitionException {
		try {
			int _type = T_RIGHT_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:144:20: ( ')' )
			// Meta.g:144:22: ')'
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
	// $ANTLR end "T_RIGHT_PARENTHESIS"

	// $ANTLR start "T_QUOTE"
	public final void mT_QUOTE() throws RecognitionException {
		try {
			int _type = T_QUOTE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:145:8: ( '\"' | '\\'' )
			// Meta.g:
			{
			if ( input.LA(1)=='\"'||input.LA(1)=='\'' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_QUOTE"

	// $ANTLR start "T_INDEX_TYPE"
	public final void mT_INDEX_TYPE() throws RecognitionException {
		try {
			int _type = T_INDEX_TYPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:147:13: ( ( 'HASH' | 'FULLTEXT' | 'CUSTOM' ) )
			// Meta.g:147:15: ( 'HASH' | 'FULLTEXT' | 'CUSTOM' )
			{
			// Meta.g:147:15: ( 'HASH' | 'FULLTEXT' | 'CUSTOM' )
			int alt1=3;
			switch ( input.LA(1) ) {
			case 'H':
				{
				alt1=1;
				}
				break;
			case 'F':
				{
				alt1=2;
				}
				break;
			case 'C':
				{
				alt1=3;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}
			switch (alt1) {
				case 1 :
					// Meta.g:147:16: 'HASH'
					{
					match("HASH"); 

					}
					break;
				case 2 :
					// Meta.g:147:25: 'FULLTEXT'
					{
					match("FULLTEXT"); 

					}
					break;
				case 3 :
					// Meta.g:147:38: 'CUSTOM'
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

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// Meta.g:149:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
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
			// Meta.g:150:15: ( '0' .. '9' )
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

	// $ANTLR start "T_CONSTANT"
	public final void mT_CONSTANT() throws RecognitionException {
		try {
			int _type = T_CONSTANT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:152:11: ( ( DIGIT )+ )
			// Meta.g:152:13: ( DIGIT )+
			{
			// Meta.g:152:13: ( DIGIT )+
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
			// Meta.g:154:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:154:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:154:17: ( LETTER | DIGIT | '_' )*
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')||(LA3_0 >= 'A' && LA3_0 <= 'Z')||LA3_0=='_'||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
					alt3=1;
				}

				switch (alt3) {
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
					break loop3;
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

	// $ANTLR start "T_TERM"
	public final void mT_TERM() throws RecognitionException {
		try {
			int _type = T_TERM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:156:7: ( ( LETTER | DIGIT | '_' | '.' )+ )
			// Meta.g:156:9: ( LETTER | DIGIT | '_' | '.' )+
			{
			// Meta.g:156:9: ( LETTER | DIGIT | '_' | '.' )+
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0=='.'||(LA4_0 >= '0' && LA4_0 <= '9')||(LA4_0 >= 'A' && LA4_0 <= 'Z')||LA4_0=='_'||(LA4_0 >= 'a' && LA4_0 <= 'z')) ) {
					alt4=1;
				}

				switch (alt4) {
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
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
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

	// $ANTLR start "T_PATH"
	public final void mT_PATH() throws RecognitionException {
		try {
			int _type = T_PATH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:158:7: ( ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+ )
			// Meta.g:158:9: ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+
			{
			// Meta.g:158:9: ( LETTER | DIGIT | '_' | '.' | '-' | '/' )+
			int cnt5=0;
			loop5:
			while (true) {
				int alt5=2;
				int LA5_0 = input.LA(1);
				if ( ((LA5_0 >= '-' && LA5_0 <= '9')||(LA5_0 >= 'A' && LA5_0 <= 'Z')||LA5_0=='_'||(LA5_0 >= 'a' && LA5_0 <= 'z')) ) {
					alt5=1;
				}

				switch (alt5) {
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
	// $ANTLR end "T_PATH"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:373:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:373:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:373:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			int cnt6=0;
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '\t' && LA6_0 <= '\n')||LA6_0=='\r'||LA6_0==' ') ) {
					alt6=1;
				}

				switch (alt6) {
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
					if ( cnt6 >= 1 ) break loop6;
					EarlyExitException eee = new EarlyExitException(6, input);
					throw eee;
				}
				cnt6++;
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
		// Meta.g:1:8: ( T__85 | T__86 | T__87 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_ADD | T_LIST | T_REMOVE | T_ON | T_USING | T_UDF | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_LEFT_PARENTHESIS | T_RIGHT_PARENTHESIS | T_QUOTE | T_INDEX_TYPE | T_CONSTANT | T_IDENT | T_TERM | T_PATH | WS )
		int alt7=56;
		alt7 = dfa7.predict(input);
		switch (alt7) {
			case 1 :
				// Meta.g:1:10: T__85
				{
				mT__85(); 

				}
				break;
			case 2 :
				// Meta.g:1:16: T__86
				{
				mT__86(); 

				}
				break;
			case 3 :
				// Meta.g:1:22: T__87
				{
				mT__87(); 

				}
				break;
			case 4 :
				// Meta.g:1:28: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 5 :
				// Meta.g:1:39: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 6 :
				// Meta.g:1:48: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 7 :
				// Meta.g:1:56: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 8 :
				// Meta.g:1:67: T_NOT
				{
				mT_NOT(); 

				}
				break;
			case 9 :
				// Meta.g:1:73: T_WITH
				{
				mT_WITH(); 

				}
				break;
			case 10 :
				// Meta.g:1:80: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 11 :
				// Meta.g:1:87: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 12 :
				// Meta.g:1:95: T_IF
				{
				mT_IF(); 

				}
				break;
			case 13 :
				// Meta.g:1:100: T_EXISTS
				{
				mT_EXISTS(); 

				}
				break;
			case 14 :
				// Meta.g:1:109: T_AND
				{
				mT_AND(); 

				}
				break;
			case 15 :
				// Meta.g:1:115: T_USE
				{
				mT_USE(); 

				}
				break;
			case 16 :
				// Meta.g:1:121: T_SET
				{
				mT_SET(); 

				}
				break;
			case 17 :
				// Meta.g:1:127: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 18 :
				// Meta.g:1:137: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 19 :
				// Meta.g:1:149: T_TRUE
				{
				mT_TRUE(); 

				}
				break;
			case 20 :
				// Meta.g:1:156: T_FALSE
				{
				mT_FALSE(); 

				}
				break;
			case 21 :
				// Meta.g:1:164: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 22 :
				// Meta.g:1:178: T_ALL
				{
				mT_ALL(); 

				}
				break;
			case 23 :
				// Meta.g:1:184: T_ANY
				{
				mT_ANY(); 

				}
				break;
			case 24 :
				// Meta.g:1:190: T_QUORUM
				{
				mT_QUORUM(); 

				}
				break;
			case 25 :
				// Meta.g:1:199: T_ONE
				{
				mT_ONE(); 

				}
				break;
			case 26 :
				// Meta.g:1:205: T_TWO
				{
				mT_TWO(); 

				}
				break;
			case 27 :
				// Meta.g:1:211: T_THREE
				{
				mT_THREE(); 

				}
				break;
			case 28 :
				// Meta.g:1:219: T_EACH_QUORUM
				{
				mT_EACH_QUORUM(); 

				}
				break;
			case 29 :
				// Meta.g:1:233: T_LOCAL_ONE
				{
				mT_LOCAL_ONE(); 

				}
				break;
			case 30 :
				// Meta.g:1:245: T_LOCAL_QUORUM
				{
				mT_LOCAL_QUORUM(); 

				}
				break;
			case 31 :
				// Meta.g:1:260: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 32 :
				// Meta.g:1:270: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 33 :
				// Meta.g:1:277: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 34 :
				// Meta.g:1:283: T_INDEX
				{
				mT_INDEX(); 

				}
				break;
			case 35 :
				// Meta.g:1:291: T_ADD
				{
				mT_ADD(); 

				}
				break;
			case 36 :
				// Meta.g:1:297: T_LIST
				{
				mT_LIST(); 

				}
				break;
			case 37 :
				// Meta.g:1:304: T_REMOVE
				{
				mT_REMOVE(); 

				}
				break;
			case 38 :
				// Meta.g:1:313: T_ON
				{
				mT_ON(); 

				}
				break;
			case 39 :
				// Meta.g:1:318: T_USING
				{
				mT_USING(); 

				}
				break;
			case 40 :
				// Meta.g:1:326: T_UDF
				{
				mT_UDF(); 

				}
				break;
			case 41 :
				// Meta.g:1:332: T_SEMICOLON
				{
				mT_SEMICOLON(); 

				}
				break;
			case 42 :
				// Meta.g:1:344: T_EQUAL
				{
				mT_EQUAL(); 

				}
				break;
			case 43 :
				// Meta.g:1:352: T_POINT
				{
				mT_POINT(); 

				}
				break;
			case 44 :
				// Meta.g:1:360: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 

				}
				break;
			case 45 :
				// Meta.g:1:377: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 

				}
				break;
			case 46 :
				// Meta.g:1:392: T_COLON
				{
				mT_COLON(); 

				}
				break;
			case 47 :
				// Meta.g:1:400: T_COMMA
				{
				mT_COMMA(); 

				}
				break;
			case 48 :
				// Meta.g:1:408: T_LEFT_PARENTHESIS
				{
				mT_LEFT_PARENTHESIS(); 

				}
				break;
			case 49 :
				// Meta.g:1:427: T_RIGHT_PARENTHESIS
				{
				mT_RIGHT_PARENTHESIS(); 

				}
				break;
			case 50 :
				// Meta.g:1:447: T_QUOTE
				{
				mT_QUOTE(); 

				}
				break;
			case 51 :
				// Meta.g:1:455: T_INDEX_TYPE
				{
				mT_INDEX_TYPE(); 

				}
				break;
			case 52 :
				// Meta.g:1:468: T_CONSTANT
				{
				mT_CONSTANT(); 

				}
				break;
			case 53 :
				// Meta.g:1:479: T_IDENT
				{
				mT_IDENT(); 

				}
				break;
			case 54 :
				// Meta.g:1:487: T_TERM
				{
				mT_TERM(); 

				}
				break;
			case 55 :
				// Meta.g:1:494: T_PATH
				{
				mT_PATH(); 

				}
				break;
			case 56 :
				// Meta.g:1:501: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA7 dfa7 = new DFA7(this);
	static final String DFA7_eotS =
		"\1\uffff\24\50\2\uffff\1\113\7\uffff\3\50\1\115\1\50\1\116\2\uffff\1\50"+
		"\1\uffff\24\50\1\147\5\50\1\156\7\50\1\uffff\1\50\2\uffff\5\50\1\176\1"+
		"\50\1\u0080\1\u0081\1\u0082\5\50\1\u0088\1\u0089\1\50\1\u008b\1\u008c"+
		"\1\50\1\u008e\2\50\1\uffff\4\50\1\u0095\1\50\1\uffff\1\u0097\2\50\1\u009a"+
		"\6\50\1\u00a1\2\50\1\u00a4\1\50\1\uffff\1\50\3\uffff\5\50\2\uffff\1\50"+
		"\2\uffff\1\50\1\uffff\1\u00ae\1\u00af\4\50\1\uffff\1\50\1\uffff\2\50\1"+
		"\uffff\2\50\1\u00b9\1\50\1\u00bb\1\50\1\uffff\2\50\1\uffff\1\u00bf\1\u00c0"+
		"\1\u00c1\3\50\1\u00c5\2\50\2\uffff\1\u00c8\5\50\1\u00ce\2\50\1\uffff\1"+
		"\50\1\uffff\3\50\3\uffff\1\u00bb\1\u00d5\1\50\1\uffff\2\50\1\uffff\1\u00d9"+
		"\4\50\1\uffff\1\u00de\1\50\1\u00e1\1\u00e2\1\u00e3\1\50\1\uffff\3\50\1"+
		"\uffff\1\u00e8\1\50\1\u00ea\1\50\1\uffff\2\50\3\uffff\1\u00ee\2\50\1\u00f1"+
		"\1\uffff\1\50\1\uffff\1\u00bb\2\50\1\uffff\1\50\1\u00f6\1\uffff\1\50\1"+
		"\u00f8\2\50\1\uffff\1\50\1\uffff\1\50\1\u00fd\1\u00fe\1\50\2\uffff\1\u0100"+
		"\1\uffff";
	static final String DFA7_eofS =
		"\u0101\uffff";
	static final String DFA7_minS =
		"\1\11\24\55\2\uffff\1\55\7\uffff\6\55\2\uffff\1\55\1\uffff\42\55\1\uffff"+
		"\1\55\2\uffff\30\55\1\uffff\6\55\1\uffff\17\55\1\uffff\1\55\3\uffff\5"+
		"\55\2\uffff\1\55\2\uffff\1\55\1\uffff\6\55\1\uffff\1\55\1\uffff\2\55\1"+
		"\uffff\6\55\1\uffff\2\55\1\uffff\11\55\2\uffff\11\55\1\uffff\1\55\1\uffff"+
		"\3\55\3\uffff\3\55\1\uffff\2\55\1\uffff\5\55\1\uffff\6\55\1\uffff\3\55"+
		"\1\uffff\4\55\1\uffff\2\55\3\uffff\4\55\1\uffff\1\55\1\uffff\3\55\1\uffff"+
		"\2\55\1\uffff\4\55\1\uffff\1\55\1\uffff\4\55\2\uffff\1\55\1\uffff";
	static final String DFA7_maxS =
		"\1\175\24\172\2\uffff\1\172\7\uffff\6\172\2\uffff\1\172\1\uffff\42\172"+
		"\1\uffff\1\172\2\uffff\30\172\1\uffff\6\172\1\uffff\17\172\1\uffff\1\172"+
		"\3\uffff\5\172\2\uffff\1\172\2\uffff\1\172\1\uffff\6\172\1\uffff\1\172"+
		"\1\uffff\2\172\1\uffff\6\172\1\uffff\2\172\1\uffff\11\172\2\uffff\11\172"+
		"\1\uffff\1\172\1\uffff\3\172\3\uffff\3\172\1\uffff\2\172\1\uffff\5\172"+
		"\1\uffff\6\172\1\uffff\3\172\1\uffff\4\172\1\uffff\2\172\3\uffff\4\172"+
		"\1\uffff\1\172\1\uffff\3\172\1\uffff\2\172\1\uffff\4\172\1\uffff\1\172"+
		"\1\uffff\4\172\2\uffff\1\172\1\uffff";
	static final String DFA7_acceptS =
		"\25\uffff\1\51\1\52\1\uffff\1\54\1\55\1\56\1\57\1\60\1\61\1\62\6\uffff"+
		"\1\67\1\70\1\uffff\1\65\42\uffff\1\53\1\uffff\1\64\1\66\30\uffff\1\14"+
		"\6\uffff\1\46\17\uffff\1\32\1\uffff\1\3\1\50\1\17\5\uffff\1\26\1\16\1"+
		"\uffff\1\27\1\43\1\uffff\1\10\6\uffff\1\20\1\uffff\1\31\2\uffff\1\41\6"+
		"\uffff\1\40\2\uffff\1\23\11\uffff\1\11\1\12\11\uffff\1\44\1\uffff\1\63"+
		"\3\uffff\1\13\1\33\1\47\3\uffff\1\6\2\uffff\1\42\5\uffff\1\24\6\uffff"+
		"\1\5\3\uffff\1\15\4\uffff\1\30\2\uffff\1\45\1\1\1\2\4\uffff\1\37\1\uffff"+
		"\1\21\3\uffff\1\4\2\uffff\1\7\4\uffff\1\22\1\uffff\1\35\4\uffff\1\25\1"+
		"\34\1\uffff\1\36";
	static final String DFA7_specialS =
		"\u0101\uffff}>";
	static final String[] DFA7_transitionS = {
			"\2\46\2\uffff\1\46\22\uffff\1\46\1\uffff\1\36\4\uffff\1\36\1\34\1\35"+
			"\2\uffff\1\33\1\45\1\27\1\45\12\42\1\32\1\25\1\uffff\1\26\3\uffff\1\6"+
			"\1\43\1\5\1\12\1\14\1\20\1\43\1\37\1\13\1\43\1\7\1\22\1\43\1\10\1\17"+
			"\1\1\1\21\1\24\1\16\1\2\1\3\1\43\1\11\3\43\4\uffff\1\44\1\uffff\1\6\1"+
			"\43\1\41\1\12\1\14\1\40\2\43\1\13\1\43\1\7\1\22\1\43\1\10\1\17\1\23\1"+
			"\21\1\24\1\16\1\4\1\15\1\43\1\11\3\43\1\30\1\uffff\1\31",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\51\5\52\1\47\10\52\4\uffff\1\52"+
			"\1\uffff\13\52\1\51\16\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\55\6\52\1\57\11\52\1\53\4\52\1\56\3"+
			"\52\4\uffff\1\52\1\uffff\1\55\6\52\1\57\11\52\1\54\4\52\1\56\3\52",
			"\1\45\1\44\1\45\12\52\7\uffff\3\52\1\60\16\52\1\61\7\52\4\uffff\1\52"+
			"\1\uffff\3\52\1\62\16\52\1\61\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\55\6\52\1\57\11\52\1\54\4\52\1\56\3"+
			"\52\4\uffff\1\52\1\uffff\1\55\6\52\1\57\11\52\1\54\4\52\1\56\3\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\65\2\52\1\64\2\52\1\63\5\52\4"+
			"\uffff\1\52\1\uffff\16\52\1\65\2\52\1\64\10\52",
			"\1\45\1\44\1\45\12\52\7\uffff\3\52\1\70\7\52\1\66\1\52\1\67\14\52\4"+
			"\uffff\1\52\1\uffff\3\52\1\70\7\52\1\66\1\52\1\67\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\71\25\52\4\uffff\1\52\1\uffff\4"+
			"\52\1\71\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\72\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\72\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\73\21\52\4\uffff\1\52\1\uffff"+
			"\10\52\1\73\21\52",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\74\10\52\4\uffff\1\52\1\uffff"+
			"\21\52\1\74\10\52",
			"\1\45\1\44\1\45\12\52\7\uffff\5\52\1\75\7\52\1\76\14\52\4\uffff\1\52"+
			"\1\uffff\5\52\1\75\7\52\1\76\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\100\26\52\1\77\2\52\4\uffff\1\52\1\uffff"+
			"\1\100\26\52\1\77\2\52",
			"\1\45\1\44\1\45\12\52\7\uffff\3\52\1\62\16\52\1\61\7\52\4\uffff\1\52"+
			"\1\uffff\3\52\1\62\16\52\1\61\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\101\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\101\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\103\1\52\1\102\12\52\4\uffff\1"+
			"\52\1\uffff\15\52\1\103\1\52\1\102\12\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\105\15\52\1\106\5\52\1\104\5\52\4\uffff"+
			"\1\52\1\uffff\1\105\15\52\1\106\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\24\52\1\107\5\52\4\uffff\1\52\1\uffff"+
			"\24\52\1\107\5\52",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\111\5\52\1\110\13\52\4\uffff\1"+
			"\52\1\uffff\10\52\1\111\5\52\1\110\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\51\16\52\4\uffff\1\52\1\uffff"+
			"\13\52\1\51\16\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\112\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\112\25\52",
			"",
			"",
			"\1\45\1\44\1\45\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\1\114\31\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\105\15\52\1\106\13\52\4\uffff\1\52\1"+
			"\uffff\1\105\15\52\1\106\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\65\2\52\1\64\10\52\4\uffff\1\52"+
			"\1\uffff\16\52\1\65\2\52\1\64\10\52",
			"\1\45\1\44\1\45\12\42\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\117\13\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\1\120\31\52\4\uffff\1\52\1\uffff\1\120"+
			"\31\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\121\13\52\1\122\5\52\4\uffff\1"+
			"\52\1\uffff\24\52\1\122\5\52",
			"\1\45\1\44\1\45\12\52\7\uffff\24\52\1\122\5\52\4\uffff\1\52\1\uffff"+
			"\24\52\1\122\5\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\52\1\123\30\52\4\uffff\1\52\1\uffff"+
			"\1\52\1\123\30\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\124\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\124\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\125\10\52\4\uffff\1\52\1\uffff"+
			"\21\52\1\125\10\52",
			"\1\45\1\44\1\45\12\52\7\uffff\5\52\1\126\24\52\4\uffff\1\52\1\uffff"+
			"\5\52\1\127\24\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\130\3\52\1\131\21\52\4\uffff\1"+
			"\52\1\uffff\4\52\1\130\3\52\1\131\21\52",
			"\1\45\1\44\1\45\12\52\7\uffff\5\52\1\127\24\52\4\uffff\1\52\1\uffff"+
			"\5\52\1\127\24\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\132\7\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\133\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\133\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\134\14\52\4\uffff\1\52\1\uffff"+
			"\15\52\1\134\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\136\7\52\1\135\6\52\4\uffff\1"+
			"\52\1\uffff\13\52\1\136\7\52\1\135\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\140\2\52\1\137\24\52\1\141\1\52\4\uffff"+
			"\1\52\1\uffff\1\140\2\52\1\137\24\52\1\141\1\52",
			"\1\45\1\44\1\45\12\52\7\uffff\3\52\1\142\26\52\4\uffff\1\52\1\uffff"+
			"\3\52\1\142\26\52",
			"\1\45\1\44\1\45\12\52\7\uffff\30\52\1\143\1\52\4\uffff\1\52\1\uffff"+
			"\30\52\1\143\1\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\144\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\144\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\145\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\145\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\146\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\146\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\3\52\1\150\26\52\4\uffff\1\52\1\uffff"+
			"\3\52\1\150\26\52",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\151\6\52\1\152\12\52\4\uffff\1"+
			"\52\1\uffff\10\52\1\151\6\52\1\152\12\52",
			"\1\45\1\44\1\45\12\52\7\uffff\2\52\1\153\27\52\4\uffff\1\52\1\uffff"+
			"\2\52\1\153\27\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\154\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\154\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\155\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\155\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\157\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\157\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\160\16\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\161\16\52\4\uffff\1\52\1\uffff"+
			"\13\52\1\161\16\52",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\162\10\52\4\uffff\1\52\1\uffff"+
			"\21\52\1\162\10\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\163\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\163\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\2\52\1\164\27\52\4\uffff\1\52\1\uffff"+
			"\2\52\1\164\27\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\165\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\165\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\14\52\1\166\15\52\4\uffff\1\52\1\uffff"+
			"\14\52\1\166\15\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\167\7\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\2\52\1\170\27\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\171\14\52\4\uffff\1\52\1\uffff"+
			"\15\52\1\171\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\6\52\1\172\23\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\174\10\52\1\173\14\52\4\uffff\1"+
			"\52\1\uffff\4\52\1\174\10\52\1\173\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\175\16\52\4\uffff\1\52\1\uffff"+
			"\13\52\1\175\16\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\177\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\177\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\u0083\14\52\4\uffff\1\52\1\uffff"+
			"\15\52\1\u0083\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u0084\6\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\u0085\31\52\4\uffff\1\52\1\uffff\1\u0085"+
			"\31\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u0086\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u0086\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u0087\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u0087\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\u008a\16\52\4\uffff\1\52\1\uffff"+
			"\13\52\1\u008a\16\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u008d\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u008d\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\7\52\1\u008f\22\52\4\uffff\1\52\1\uffff"+
			"\7\52\1\u008f\22\52",
			"\1\45\1\44\1\45\12\52\7\uffff\17\52\1\u0090\12\52\4\uffff\1\52\1\uffff"+
			"\17\52\1\u0090\12\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u0091\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u0091\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u0092\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u0092\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\u0093\16\52\4\uffff\1\52\1\uffff"+
			"\13\52\1\u0093\16\52",
			"\1\45\1\44\1\45\12\52\7\uffff\7\52\1\u0094\22\52\4\uffff\1\52\1\uffff"+
			"\7\52\1\u0094\22\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\u0096\21\52\4\uffff\1\52\1\uffff"+
			"\10\52\1\u0096\21\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\u0098\16\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u0099\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u0099\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\u009b\10\52\4\uffff\1\52\1\uffff"+
			"\21\52\1\u009b\10\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\u009c\31\52\4\uffff\1\52\1\uffff\1\u009c"+
			"\31\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u009d\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\u009d\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\u009e\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\u009e\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\7\52\1\u009f\22\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00a0\25\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\6\52\1\u00a2\23\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\2\52\1\u00a3\27\52\4\uffff\1\52\1\uffff"+
			"\2\52\1\u00a3\27\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00a5\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00a5\25\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00a6\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00a6\25\52",
			"",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\6\52\1\u00a7\23\52\4\uffff\1\52\1\uffff"+
			"\6\52\1\u00a7\23\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\u00a8\13\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u00a9\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\u00a9\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\u00aa\21\52\4\uffff\1\52\1\uffff"+
			"\10\52\1\u00aa\21\52",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\u00ab\10\52\4\uffff\1\52\1\uffff"+
			"\21\52\1\u00ab\10\52",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\30\52\1\u00ac\1\52\4\uffff\1\52\1\uffff"+
			"\30\52\1\u00ac\1\52",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\17\52\1\u00ad\12\52\4\uffff\1\52\1\uffff"+
			"\17\52\1\u00ad\12\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\27\52\1\u00b0\2\52\4\uffff\1\52\1\uffff"+
			"\27\52\1\u00b0\2\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u00b1\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\u00b1\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\u00b2\31\52\4\uffff\1\52\1\uffff\1\u00b2"+
			"\31\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\u00b3\1\uffff\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\u00b4\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\u00b4\13\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u00b5\6\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00b6\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00b6\25\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\24\52\1\u00b7\5\52\4\uffff\1\52\1\uffff"+
			"\24\52\1\u00b7\5\52",
			"\1\45\1\44\1\45\12\52\7\uffff\13\52\1\u00b8\16\52\4\uffff\1\52\1\uffff"+
			"\13\52\1\u00b8\16\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\25\52\1\u00ba\4\52\4\uffff\1\52\1\uffff"+
			"\25\52\1\u00ba\4\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u00bc\7\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00bd\25\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\u00be\31\52\4\uffff\1\52\1\uffff\1\u00be"+
			"\31\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\14\52\1\u00c2\15\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00c3\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00c3\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u00c4\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u00c4\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u00c6\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\u00c6\6\52",
			"\1\45\1\44\1\45\12\52\7\uffff\1\u00c7\31\52\4\uffff\1\52\1\uffff\1\u00c7"+
			"\31\52",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u00c9\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u00c9\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\u00ca\21\52\4\uffff\1\52\1\uffff"+
			"\10\52\1\u00ca\21\52",
			"\1\45\1\44\1\45\12\52\7\uffff\20\52\1\u00cb\11\52\4\uffff\1\52\1\uffff"+
			"\20\52\1\u00cb\11\52",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\u00cc\14\52\4\uffff\1\52\1\uffff"+
			"\15\52\1\u00cc\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00cd\25\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\14\52\1\u00cf\15\52\4\uffff\1\52\1\uffff"+
			"\14\52\1\u00cf\15\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\u00d0\1\uffff\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00d1\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00d1\25\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u00d2\7\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\u00d3\10\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u00d4\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\u00d4\6\52",
			"",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u00d6\6\52\4\uffff\1\52\1\uffff"+
			"\23\52\1\u00d6\6\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\10\52\1\u00d7\21\52\4\uffff\1\52\1\uffff"+
			"\10\52\1\u00d7\21\52",
			"\1\45\1\44\1\45\12\52\7\uffff\2\52\1\u00d8\27\52\4\uffff\1\52\1\uffff"+
			"\2\52\1\u00d8\27\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\u00da\14\52\4\uffff\1\52\1\uffff"+
			"\15\52\1\u00da\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\24\52\1\u00db\5\52\4\uffff\1\52\1\uffff"+
			"\24\52\1\u00db\5\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u00dc\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u00dc\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\27\52\1\u00dd\2\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\u00df\1\52\1\u00e0\11\52\4\uffff"+
			"\1\52\1\uffff\16\52\1\u00df\1\52\1\u00e0\11\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00e4\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00e4\25\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00e5\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00e5\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\2\52\1\u00e6\27\52\4\uffff\1\52\1\uffff"+
			"\2\52\1\u00e6\27\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00e7\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00e7\25\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\u00e9\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\u00e9\13\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\23\52\1\u00eb\6\52\4\uffff\1\52\1\uffff"+
			"\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\u00ec\14\52\4\uffff\1\52\1\uffff"+
			"\15\52\1\u00ec\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\24\52\1\u00ed\5\52\4\uffff\1\52\1\uffff"+
			"\24\52\1\u00ed\5\52",
			"",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\15\52\1\u00ef\14\52\4\uffff\1\52\1\uffff"+
			"\15\52\1\u00ef\14\52",
			"\1\45\1\44\1\45\12\52\7\uffff\22\52\1\u00f0\7\52\4\uffff\1\52\1\uffff"+
			"\22\52\1\u00f0\7\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\u00f2\10\52\4\uffff\1\52\1\uffff"+
			"\21\52\1\u00f2\10\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\4\52\1\u00f3\25\52\4\uffff\1\52\1\uffff"+
			"\4\52\1\u00f3\25\52",
			"\1\45\1\44\1\45\12\52\7\uffff\16\52\1\u00f4\13\52\4\uffff\1\52\1\uffff"+
			"\16\52\1\u00f4\13\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\2\52\1\u00f5\27\52\4\uffff\1\52\1\uffff"+
			"\2\52\1\u00f5\27\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\24\52\1\u00f7\5\52\4\uffff\1\52\1\uffff"+
			"\24\52\1\u00f7\5\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\21\52\1\u00f9\10\52\4\uffff\1\52\1\uffff"+
			"\21\52\1\u00f9\10\52",
			"\1\45\1\44\1\45\12\52\7\uffff\30\52\1\u00fa\1\52\4\uffff\1\52\1\uffff"+
			"\30\52\1\u00fa\1\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\14\52\1\u00fb\15\52\4\uffff\1\52\1\uffff"+
			"\14\52\1\u00fb\15\52",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\24\52\1\u00fc\5\52\4\uffff\1\52\1\uffff"+
			"\24\52\1\u00fc\5\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			"\1\45\1\44\1\45\12\52\7\uffff\14\52\1\u00ff\15\52\4\uffff\1\52\1\uffff"+
			"\14\52\1\u00ff\15\52",
			"",
			"",
			"\1\45\1\44\1\45\12\52\7\uffff\32\52\4\uffff\1\52\1\uffff\32\52",
			""
	};

	static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
	static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
	static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
	static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
	static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
	static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
	static final short[][] DFA7_transition;

	static {
		int numStates = DFA7_transitionS.length;
		DFA7_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
		}
	}

	protected class DFA7 extends DFA {

		public DFA7(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 7;
			this.eot = DFA7_eot;
			this.eof = DFA7_eof;
			this.min = DFA7_min;
			this.max = DFA7_max;
			this.accept = DFA7_accept;
			this.special = DFA7_special;
			this.transition = DFA7_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__85 | T__86 | T__87 | T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_ADD | T_LIST | T_REMOVE | T_ON | T_USING | T_UDF | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_LEFT_PARENTHESIS | T_RIGHT_PARENTHESIS | T_QUOTE | T_INDEX_TYPE | T_CONSTANT | T_IDENT | T_TERM | T_PATH | WS );";
		}
	}

}
