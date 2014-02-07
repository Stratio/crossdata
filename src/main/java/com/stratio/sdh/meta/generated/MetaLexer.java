// $ANTLR 3.5.1 Meta.g 2014-02-08 00:44:02

    package com.stratio.sdh.meta.generated;


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
	public static final int T_ON=55;
	public static final int T_ONE=56;
	public static final int T_OPTIONS=57;
	public static final int T_ORDER=58;
	public static final int T_PLAN=59;
	public static final int T_POINT=60;
	public static final int T_PROCESS=61;
	public static final int T_QUORUM=62;
	public static final int T_SELECT=63;
	public static final int T_SEMICOLON=64;
	public static final int T_SET=65;
	public static final int T_START_BRACKET=66;
	public static final int T_START_SBRACKET=67;
	public static final int T_STOP=68;
	public static final int T_STORAGE=69;
	public static final int T_TABLE=70;
	public static final int T_TERM=71;
	public static final int T_THREE=72;
	public static final int T_TRIGGER=73;
	public static final int T_TRUE=74;
	public static final int T_TRUNCATE=75;
	public static final int T_TWO=76;
	public static final int T_USE=77;
	public static final int T_USING=78;
	public static final int T_VALUES=79;
	public static final int T_WITH=80;
	public static final int U=81;
	public static final int V=82;
	public static final int W=83;
	public static final int WS=84;
	public static final int X=85;
	public static final int Y=86;
	public static final int Z=87;

	    


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
			// Meta.g:74:11: ( ( 'a' | 'A' ) )
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
			// Meta.g:75:11: ( ( 'b' | 'B' ) )
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
			// Meta.g:76:11: ( ( 'c' | 'C' ) )
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
			// Meta.g:77:11: ( ( 'd' | 'D' ) )
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
			// Meta.g:78:11: ( ( 'e' | 'E' ) )
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
			// Meta.g:79:11: ( ( 'f' | 'F' ) )
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
			// Meta.g:80:11: ( ( 'g' | 'G' ) )
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
			// Meta.g:81:11: ( ( 'h' | 'H' ) )
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
			// Meta.g:82:11: ( ( 'i' | 'I' ) )
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
			// Meta.g:83:11: ( ( 'j' | 'J' ) )
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
			// Meta.g:84:11: ( ( 'k' | 'K' ) )
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
			// Meta.g:85:11: ( ( 'l' | 'L' ) )
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
			// Meta.g:86:11: ( ( 'm' | 'M' ) )
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
			// Meta.g:87:11: ( ( 'n' | 'N' ) )
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
			// Meta.g:88:11: ( ( 'o' | 'O' ) )
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
			// Meta.g:89:11: ( ( 'p' | 'P' ) )
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
			// Meta.g:90:11: ( ( 'q' | 'Q' ) )
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
			// Meta.g:91:11: ( ( 'r' | 'R' ) )
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
			// Meta.g:92:11: ( ( 's' | 'S' ) )
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
			// Meta.g:93:11: ( ( 't' | 'T' ) )
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
			// Meta.g:94:11: ( ( 'u' | 'U' ) )
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
			// Meta.g:95:11: ( ( 'v' | 'V' ) )
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
			// Meta.g:96:11: ( ( 'w' | 'W' ) )
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
			// Meta.g:97:11: ( ( 'x' | 'X' ) )
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
			// Meta.g:98:11: ( ( 'y' | 'Y' ) )
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
			// Meta.g:99:11: ( ( 'z' | 'Z' ) )
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
			// Meta.g:102:11: ( T R U N C A T E )
			// Meta.g:102:13: T R U N C A T E
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
			// Meta.g:103:9: ( C R E A T E )
			// Meta.g:103:11: C R E A T E
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
			// Meta.g:104:8: ( A L T E R )
			// Meta.g:104:10: A L T E R
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
			// Meta.g:105:11: ( K E Y S P A C E )
			// Meta.g:105:13: K E Y S P A C E
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
			// Meta.g:106:6: ( N O T )
			// Meta.g:106:8: N O T
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
			// Meta.g:107:7: ( W I T H )
			// Meta.g:107:9: W I T H
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
			// Meta.g:108:7: ( D R O P )
			// Meta.g:108:9: D R O P
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
			// Meta.g:109:8: ( T A B L E )
			// Meta.g:109:10: T A B L E
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
			// Meta.g:110:5: ( I F )
			// Meta.g:110:7: I F
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
			// Meta.g:111:9: ( E X I S T S )
			// Meta.g:111:11: E X I S T S
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
			// Meta.g:112:6: ( A N D )
			// Meta.g:112:8: A N D
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
			// Meta.g:113:6: ( U S E )
			// Meta.g:113:8: U S E
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
			// Meta.g:114:6: ( S E T )
			// Meta.g:114:8: S E T
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
			// Meta.g:115:10: ( O P T I O N S )
			// Meta.g:115:12: O P T I O N S
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
			// Meta.g:116:12: ( A N A L Y T I C S )
			// Meta.g:116:14: A N A L Y T I C S
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
			// Meta.g:117:7: ( T R U E )
			// Meta.g:117:9: T R U E
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
			// Meta.g:118:8: ( F A L S E )
			// Meta.g:118:10: F A L S E
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
			// Meta.g:119:14: ( C O N S I S T E N C Y )
			// Meta.g:119:16: C O N S I S T E N C Y
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
			// Meta.g:120:6: ( A L L )
			// Meta.g:120:8: A L L
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
			// Meta.g:121:6: ( A N Y )
			// Meta.g:121:8: A N Y
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
			// Meta.g:122:9: ( Q U O R U M )
			// Meta.g:122:11: Q U O R U M
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
			// Meta.g:123:6: ( O N E )
			// Meta.g:123:8: O N E
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
			// Meta.g:124:6: ( T W O )
			// Meta.g:124:8: T W O
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
			// Meta.g:125:8: ( T H R E E )
			// Meta.g:125:10: T H R E E
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
			// Meta.g:126:14: ( E A C H '_' Q U O R U M )
			// Meta.g:126:16: E A C H '_' Q U O R U M
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
			// Meta.g:127:12: ( L O C A L '_' O N E )
			// Meta.g:127:14: L O C A L '_' O N E
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
			// Meta.g:128:15: ( L O C A L '_' Q U O R U M )
			// Meta.g:128:17: L O C A L '_' Q U O R U M
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
			// Meta.g:129:10: ( E X P L A I N )
			// Meta.g:129:12: E X P L A I N
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
			// Meta.g:130:7: ( P L A N )
			// Meta.g:130:9: P L A N
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
			// Meta.g:131:6: ( F O R )
			// Meta.g:131:8: F O R
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

	// $ANTLR start "T_PROCESS"
	public final void mT_PROCESS() throws RecognitionException {
		try {
			int _type = T_PROCESS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:134:10: ( P R O C E S S )
			// Meta.g:134:12: P R O C E S S
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
			// Meta.g:136:10: ( T R I G G E R )
			// Meta.g:136:12: T R I G G E R
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

	// $ANTLR start "T_ON"
	public final void mT_ON() throws RecognitionException {
		try {
			int _type = T_ON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:137:5: ( O N )
			// Meta.g:137:7: O N
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
			// Meta.g:139:8: ( U S I N G )
			// Meta.g:139:10: U S I N G
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

	// $ANTLR start "T_INSERT"
	public final void mT_INSERT() throws RecognitionException {
		try {
			int _type = T_INSERT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:141:9: ( I N S E R T )
			// Meta.g:141:11: I N S E R T
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
			// Meta.g:142:7: ( I N T O )
			// Meta.g:142:9: I N T O
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
			// Meta.g:143:10: ( C O M P A C T )
			// Meta.g:143:12: C O M P A C T
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
			// Meta.g:144:10: ( S T O R A G E )
			// Meta.g:144:12: S T O R A G E
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
			// Meta.g:145:13: ( C L U S T E R I N G )
			// Meta.g:145:15: C L U S T E R I N G
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
			// Meta.g:146:8: ( O R D E R )
			// Meta.g:146:10: O R D E R
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
			// Meta.g:147:9: ( S E L E C T )
			// Meta.g:147:11: S E L E C T
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
			// Meta.g:148:9: ( V A L U E S )
			// Meta.g:148:11: V A L U E S
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

	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:151:12: ( ';' )
			// Meta.g:151:14: ';'
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
			// Meta.g:152:8: ( '=' )
			// Meta.g:152:10: '='
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
			// Meta.g:153:8: ( '.' )
			// Meta.g:153:10: '.'
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
			// Meta.g:154:17: ( '{' )
			// Meta.g:154:19: '{'
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
			// Meta.g:155:15: ( '}' )
			// Meta.g:155:17: '}'
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
			// Meta.g:156:8: ( ':' )
			// Meta.g:156:10: ':'
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
			// Meta.g:157:8: ( ',' )
			// Meta.g:157:10: ','
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

	// $ANTLR start "T_START_BRACKET"
	public final void mT_START_BRACKET() throws RecognitionException {
		try {
			int _type = T_START_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:158:16: ( '(' )
			// Meta.g:158:18: '('
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
	// $ANTLR end "T_START_BRACKET"

	// $ANTLR start "T_END_BRACKET"
	public final void mT_END_BRACKET() throws RecognitionException {
		try {
			int _type = T_END_BRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// Meta.g:159:14: ( ')' )
			// Meta.g:159:16: ')'
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
	// $ANTLR end "T_END_BRACKET"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// Meta.g:161:16: ( ( 'A' .. 'Z' | 'a' .. 'z' ) )
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
			// Meta.g:162:15: ( '0' .. '9' )
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
			// Meta.g:164:11: ( ( DIGIT )+ )
			// Meta.g:164:13: ( DIGIT )+
			{
			// Meta.g:164:13: ( DIGIT )+
			int cnt1=0;
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '0' && LA1_0 <= '9')) ) {
					alt1=1;
				}

				switch (alt1) {
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
					if ( cnt1 >= 1 ) break loop1;
					EarlyExitException eee = new EarlyExitException(1, input);
					throw eee;
				}
				cnt1++;
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
			// Meta.g:166:8: ( LETTER ( LETTER | DIGIT | '_' )* )
			// Meta.g:166:10: LETTER ( LETTER | DIGIT | '_' )*
			{
			mLETTER(); 

			// Meta.g:166:17: ( LETTER | DIGIT | '_' )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
					alt2=1;
				}

				switch (alt2) {
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
					break loop2;
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
			// Meta.g:168:7: ( ( LETTER | DIGIT | '_' | '.' )+ )
			// Meta.g:168:9: ( LETTER | DIGIT | '_' | '.' )+
			{
			// Meta.g:168:9: ( LETTER | DIGIT | '_' | '.' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( (LA3_0=='.'||(LA3_0 >= '0' && LA3_0 <= '9')||(LA3_0 >= 'A' && LA3_0 <= 'Z')||LA3_0=='_'||(LA3_0 >= 'a' && LA3_0 <= 'z')) ) {
					alt3=1;
				}

				switch (alt3) {
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
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
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
			// Meta.g:437:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// Meta.g:437:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// Meta.g:437:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			int cnt4=0;
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( ((LA4_0 >= '\t' && LA4_0 <= '\n')||LA4_0=='\r'||LA4_0==' ') ) {
					alt4=1;
				}

				switch (alt4) {
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
					if ( cnt4 >= 1 ) break loop4;
					EarlyExitException eee = new EarlyExitException(4, input);
					throw eee;
				}
				cnt4++;
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
		// Meta.g:1:8: ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_STOP | T_PROCESS | T_TRIGGER | T_ON | T_USING | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_BRACKET | T_END_BRACKET | T_CONSTANT | T_IDENT | T_TERM | WS )
		int alt5=56;
		alt5 = dfa5.predict(input);
		switch (alt5) {
			case 1 :
				// Meta.g:1:10: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 2 :
				// Meta.g:1:21: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 3 :
				// Meta.g:1:30: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 4 :
				// Meta.g:1:38: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 5 :
				// Meta.g:1:49: T_NOT
				{
				mT_NOT(); 

				}
				break;
			case 6 :
				// Meta.g:1:55: T_WITH
				{
				mT_WITH(); 

				}
				break;
			case 7 :
				// Meta.g:1:62: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 8 :
				// Meta.g:1:69: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 9 :
				// Meta.g:1:77: T_IF
				{
				mT_IF(); 

				}
				break;
			case 10 :
				// Meta.g:1:82: T_EXISTS
				{
				mT_EXISTS(); 

				}
				break;
			case 11 :
				// Meta.g:1:91: T_AND
				{
				mT_AND(); 

				}
				break;
			case 12 :
				// Meta.g:1:97: T_USE
				{
				mT_USE(); 

				}
				break;
			case 13 :
				// Meta.g:1:103: T_SET
				{
				mT_SET(); 

				}
				break;
			case 14 :
				// Meta.g:1:109: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 15 :
				// Meta.g:1:119: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 16 :
				// Meta.g:1:131: T_TRUE
				{
				mT_TRUE(); 

				}
				break;
			case 17 :
				// Meta.g:1:138: T_FALSE
				{
				mT_FALSE(); 

				}
				break;
			case 18 :
				// Meta.g:1:146: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 19 :
				// Meta.g:1:160: T_ALL
				{
				mT_ALL(); 

				}
				break;
			case 20 :
				// Meta.g:1:166: T_ANY
				{
				mT_ANY(); 

				}
				break;
			case 21 :
				// Meta.g:1:172: T_QUORUM
				{
				mT_QUORUM(); 

				}
				break;
			case 22 :
				// Meta.g:1:181: T_ONE
				{
				mT_ONE(); 

				}
				break;
			case 23 :
				// Meta.g:1:187: T_TWO
				{
				mT_TWO(); 

				}
				break;
			case 24 :
				// Meta.g:1:193: T_THREE
				{
				mT_THREE(); 

				}
				break;
			case 25 :
				// Meta.g:1:201: T_EACH_QUORUM
				{
				mT_EACH_QUORUM(); 

				}
				break;
			case 26 :
				// Meta.g:1:215: T_LOCAL_ONE
				{
				mT_LOCAL_ONE(); 

				}
				break;
			case 27 :
				// Meta.g:1:227: T_LOCAL_QUORUM
				{
				mT_LOCAL_QUORUM(); 

				}
				break;
			case 28 :
				// Meta.g:1:242: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 29 :
				// Meta.g:1:252: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 30 :
				// Meta.g:1:259: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 31 :
				// Meta.g:1:265: T_STOP
				{
				mT_STOP(); 

				}
				break;
			case 32 :
				// Meta.g:1:272: T_PROCESS
				{
				mT_PROCESS(); 

				}
				break;
			case 33 :
				// Meta.g:1:282: T_TRIGGER
				{
				mT_TRIGGER(); 

				}
				break;
			case 34 :
				// Meta.g:1:292: T_ON
				{
				mT_ON(); 

				}
				break;
			case 35 :
				// Meta.g:1:297: T_USING
				{
				mT_USING(); 

				}
				break;
			case 36 :
				// Meta.g:1:305: T_INSERT
				{
				mT_INSERT(); 

				}
				break;
			case 37 :
				// Meta.g:1:314: T_INTO
				{
				mT_INTO(); 

				}
				break;
			case 38 :
				// Meta.g:1:321: T_COMPACT
				{
				mT_COMPACT(); 

				}
				break;
			case 39 :
				// Meta.g:1:331: T_STORAGE
				{
				mT_STORAGE(); 

				}
				break;
			case 40 :
				// Meta.g:1:341: T_CLUSTERING
				{
				mT_CLUSTERING(); 

				}
				break;
			case 41 :
				// Meta.g:1:354: T_ORDER
				{
				mT_ORDER(); 

				}
				break;
			case 42 :
				// Meta.g:1:362: T_SELECT
				{
				mT_SELECT(); 

				}
				break;
			case 43 :
				// Meta.g:1:371: T_VALUES
				{
				mT_VALUES(); 

				}
				break;
			case 44 :
				// Meta.g:1:380: T_SEMICOLON
				{
				mT_SEMICOLON(); 

				}
				break;
			case 45 :
				// Meta.g:1:392: T_EQUAL
				{
				mT_EQUAL(); 

				}
				break;
			case 46 :
				// Meta.g:1:400: T_POINT
				{
				mT_POINT(); 

				}
				break;
			case 47 :
				// Meta.g:1:408: T_START_SBRACKET
				{
				mT_START_SBRACKET(); 

				}
				break;
			case 48 :
				// Meta.g:1:425: T_END_SBRACKET
				{
				mT_END_SBRACKET(); 

				}
				break;
			case 49 :
				// Meta.g:1:440: T_COLON
				{
				mT_COLON(); 

				}
				break;
			case 50 :
				// Meta.g:1:448: T_COMMA
				{
				mT_COMMA(); 

				}
				break;
			case 51 :
				// Meta.g:1:456: T_START_BRACKET
				{
				mT_START_BRACKET(); 

				}
				break;
			case 52 :
				// Meta.g:1:472: T_END_BRACKET
				{
				mT_END_BRACKET(); 

				}
				break;
			case 53 :
				// Meta.g:1:486: T_CONSTANT
				{
				mT_CONSTANT(); 

				}
				break;
			case 54 :
				// Meta.g:1:497: T_IDENT
				{
				mT_IDENT(); 

				}
				break;
			case 55 :
				// Meta.g:1:505: T_TERM
				{
				mT_TERM(); 

				}
				break;
			case 56 :
				// Meta.g:1:512: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA5 dfa5 = new DFA5(this);
	static final String DFA5_eotS =
		"\1\uffff\21\37\2\uffff\1\77\6\uffff\1\100\1\37\3\uffff\16\37\1\123\7\37"+
		"\1\137\10\37\2\uffff\3\37\1\155\6\37\1\164\1\165\1\37\1\167\1\37\1\171"+
		"\2\37\1\uffff\5\37\1\u0081\1\37\1\u0083\3\37\1\uffff\1\u0088\2\37\1\u008b"+
		"\6\37\1\u0092\2\37\1\uffff\6\37\2\uffff\1\37\1\uffff\1\37\1\uffff\1\u009d"+
		"\1\u009e\1\37\1\u00a0\3\37\1\uffff\1\37\1\uffff\1\37\1\u00a6\2\37\1\uffff"+
		"\2\37\1\uffff\2\37\1\u00ad\3\37\1\uffff\1\37\1\u00b2\1\u00b3\4\37\1\u00b8"+
		"\2\37\2\uffff\1\37\1\uffff\3\37\1\u00bf\1\37\1\uffff\2\37\1\u00c3\1\u00c4"+
		"\2\37\1\uffff\4\37\2\uffff\1\u00cb\3\37\1\uffff\2\37\1\u00d1\1\u00d2\2"+
		"\37\1\uffff\1\u00d5\2\37\2\uffff\1\u00d8\2\37\1\u00dc\1\37\1\u00de\1\uffff"+
		"\1\37\1\u00e0\3\37\2\uffff\1\u00e4\1\37\1\uffff\1\u00e6\1\u00e7\1\uffff"+
		"\2\37\1\u00ea\1\uffff\1\u00eb\1\uffff\1\37\1\uffff\2\37\1\u00ef\1\uffff"+
		"\1\37\2\uffff\2\37\2\uffff\2\37\1\u00f5\1\uffff\1\37\1\u00f7\2\37\1\u00fa"+
		"\1\uffff\1\37\1\uffff\1\37\1\u00fd\1\uffff\1\u00fe\1\37\2\uffff\1\u0100"+
		"\1\uffff";
	static final String DFA5_eofS =
		"\u0101\uffff";
	static final String DFA5_minS =
		"\1\11\21\56\2\uffff\1\56\6\uffff\2\56\3\uffff\37\56\2\uffff\22\56\1\uffff"+
		"\13\56\1\uffff\15\56\1\uffff\6\56\2\uffff\1\56\1\uffff\1\56\1\uffff\7"+
		"\56\1\uffff\1\56\1\uffff\4\56\1\uffff\2\56\1\uffff\6\56\1\uffff\12\56"+
		"\2\uffff\1\56\1\uffff\5\56\1\uffff\6\56\1\uffff\4\56\2\uffff\4\56\1\uffff"+
		"\6\56\1\uffff\3\56\2\uffff\6\56\1\uffff\5\56\2\uffff\2\56\1\uffff\2\56"+
		"\1\uffff\3\56\1\uffff\1\56\1\uffff\1\56\1\uffff\3\56\1\uffff\1\56\2\uffff"+
		"\2\56\2\uffff\3\56\1\uffff\5\56\1\uffff\1\56\1\uffff\2\56\1\uffff\2\56"+
		"\2\uffff\1\56\1\uffff";
	static final String DFA5_maxS =
		"\1\175\21\172\2\uffff\1\172\6\uffff\2\172\3\uffff\37\172\2\uffff\22\172"+
		"\1\uffff\13\172\1\uffff\15\172\1\uffff\6\172\2\uffff\1\172\1\uffff\1\172"+
		"\1\uffff\7\172\1\uffff\1\172\1\uffff\4\172\1\uffff\2\172\1\uffff\6\172"+
		"\1\uffff\12\172\2\uffff\1\172\1\uffff\5\172\1\uffff\6\172\1\uffff\4\172"+
		"\2\uffff\4\172\1\uffff\6\172\1\uffff\3\172\2\uffff\6\172\1\uffff\5\172"+
		"\2\uffff\2\172\1\uffff\2\172\1\uffff\3\172\1\uffff\1\172\1\uffff\1\172"+
		"\1\uffff\3\172\1\uffff\1\172\2\uffff\2\172\2\uffff\3\172\1\uffff\5\172"+
		"\1\uffff\1\172\1\uffff\2\172\1\uffff\2\172\2\uffff\1\172\1\uffff";
	static final String DFA5_acceptS =
		"\22\uffff\1\54\1\55\1\uffff\1\57\1\60\1\61\1\62\1\63\1\64\2\uffff\1\67"+
		"\1\70\1\66\37\uffff\1\56\1\65\22\uffff\1\11\13\uffff\1\42\15\uffff\1\27"+
		"\6\uffff\1\23\1\13\1\uffff\1\24\1\uffff\1\5\7\uffff\1\14\1\uffff\1\15"+
		"\4\uffff\1\26\2\uffff\1\36\6\uffff\1\20\12\uffff\1\6\1\7\1\uffff\1\45"+
		"\5\uffff\1\37\6\uffff\1\35\4\uffff\1\10\1\30\4\uffff\1\3\6\uffff\1\43"+
		"\3\uffff\1\51\1\21\6\uffff\1\2\5\uffff\1\44\1\12\2\uffff\1\52\2\uffff"+
		"\1\25\3\uffff\1\53\1\uffff\1\41\1\uffff\1\46\3\uffff\1\34\1\uffff\1\47"+
		"\1\16\2\uffff\1\40\1\1\3\uffff\1\4\5\uffff\1\17\1\uffff\1\32\2\uffff\1"+
		"\50\2\uffff\1\22\1\31\1\uffff\1\33";
	static final String DFA5_specialS =
		"\u0101\uffff}>";
	static final String[] DFA5_transitionS = {
			"\2\36\2\uffff\1\36\22\uffff\1\36\7\uffff\1\31\1\32\2\uffff\1\30\1\uffff"+
			"\1\24\1\uffff\12\33\1\27\1\22\1\uffff\1\23\3\uffff\1\3\1\34\1\2\1\7\1"+
			"\11\1\15\2\34\1\10\1\34\1\4\1\17\1\34\1\5\1\14\1\20\1\16\1\34\1\13\1"+
			"\1\1\12\1\21\1\6\3\34\4\uffff\1\35\1\uffff\1\3\1\34\1\2\1\7\1\11\1\15"+
			"\2\34\1\10\1\34\1\4\1\17\1\34\1\5\1\14\1\20\1\16\1\34\1\13\1\1\1\12\1"+
			"\21\1\6\3\34\1\25\1\uffff\1\26",
			"\1\35\1\uffff\12\44\7\uffff\1\41\6\44\1\43\11\44\1\40\4\44\1\42\3\44"+
			"\4\uffff\1\44\1\uffff\1\41\6\44\1\43\11\44\1\40\4\44\1\42\3\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\47\2\44\1\46\2\44\1\45\10\44\4\uffff"+
			"\1\44\1\uffff\13\44\1\47\2\44\1\46\2\44\1\45\10\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\50\1\44\1\51\14\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\50\1\44\1\51\14\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\52\25\44\4\uffff\1\44\1\uffff\4\44"+
			"\1\52\25\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\53\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\53\13\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\54\21\44\4\uffff\1\44\1\uffff\10"+
			"\44\1\54\21\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\55\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\55\10\44",
			"\1\35\1\uffff\12\44\7\uffff\5\44\1\56\7\44\1\57\14\44\4\uffff\1\44\1"+
			"\uffff\5\44\1\56\7\44\1\57\14\44",
			"\1\35\1\uffff\12\44\7\uffff\1\61\26\44\1\60\2\44\4\uffff\1\44\1\uffff"+
			"\1\61\26\44\1\60\2\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\62\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\62\7\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\63\16\44\1\64\6\44\4\uffff\1\44\1"+
			"\uffff\4\44\1\63\16\44\1\64\6\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\66\1\44\1\65\1\44\1\67\10\44\4\uffff"+
			"\1\44\1\uffff\15\44\1\66\1\44\1\65\1\44\1\67\10\44",
			"\1\35\1\uffff\12\44\7\uffff\1\70\15\44\1\71\13\44\4\uffff\1\44\1\uffff"+
			"\1\70\15\44\1\71\13\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\72\5\44\4\uffff\1\44\1\uffff\24"+
			"\44\1\72\5\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\73\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\73\13\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\74\5\44\1\75\10\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\74\5\44\1\75\10\44",
			"\1\35\1\uffff\12\44\7\uffff\1\76\31\44\4\uffff\1\44\1\uffff\1\76\31"+
			"\44",
			"",
			"",
			"\1\35\1\uffff\12\35\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\35\1\uffff\12\33\7\uffff\32\35\4\uffff\1\35\1\uffff\32\35",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\102\13\44\1\101\5\44\4\uffff\1\44"+
			"\1\uffff\10\44\1\102\13\44\1\101\5\44",
			"\1\35\1\uffff\12\44\7\uffff\1\44\1\103\30\44\4\uffff\1\44\1\uffff\1"+
			"\44\1\103\30\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\104\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\104\13\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\105\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\105\10\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\106\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\106\25\44",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\110\1\107\14\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\110\1\107\14\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\111\5\44\4\uffff\1\44\1\uffff\24"+
			"\44\1\111\5\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\113\7\44\1\112\6\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\113\7\44\1\112\6\44",
			"\1\35\1\uffff\12\44\7\uffff\1\115\2\44\1\114\24\44\1\116\1\44\4\uffff"+
			"\1\44\1\uffff\1\115\2\44\1\114\24\44\1\116\1\44",
			"\1\35\1\uffff\12\44\7\uffff\30\44\1\117\1\44\4\uffff\1\44\1\uffff\30"+
			"\44\1\117\1\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\120\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\120\6\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\121\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\121\6\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\122\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\122\13\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\124\1\125\6\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\124\1\125\6\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\126\6\44\1\127\12\44\4\uffff\1\44"+
			"\1\uffff\10\44\1\126\6\44\1\127\12\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\130\27\44\4\uffff\1\44\1\uffff\2"+
			"\44\1\130\27\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\131\3\44\1\132\21\44\4\uffff\1\44"+
			"\1\uffff\4\44\1\131\3\44\1\132\21\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\134\7\44\1\133\6\44\4\uffff\1\44"+
			"\1\uffff\13\44\1\134\7\44\1\133\6\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\135\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\135\13\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\136\6\44\4\uffff\1\44\1\uffff\23"+
			"\44\1\136\6\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\140\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\140\25\44",
			"\1\35\1\uffff\12\44\7\uffff\3\44\1\141\26\44\4\uffff\1\44\1\uffff\3"+
			"\44\1\141\26\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\142\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\142\16\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\143\10\44\4\uffff\1\44\1\uffff\21"+
			"\44\1\143\10\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\144\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\144\13\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\145\27\44\4\uffff\1\44\1\uffff\2"+
			"\44\1\145\27\44",
			"\1\35\1\uffff\12\44\7\uffff\1\146\31\44\4\uffff\1\44\1\uffff\1\146\31"+
			"\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\147\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\147\13\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\150\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\150\16\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\152\10\44\1\151\14\44\4\uffff\1\44"+
			"\1\uffff\4\44\1\152\10\44\1\151\14\44",
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\153\23\44\4\uffff\1\44\1\uffff\6"+
			"\44\1\153\23\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\154\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\154\16\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\156\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\156\25\44",
			"\1\35\1\uffff\12\44\7\uffff\1\157\31\44\4\uffff\1\44\1\uffff\1\157\31"+
			"\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\160\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\160\7\44",
			"\1\35\1\uffff\12\44\7\uffff\17\44\1\161\12\44\4\uffff\1\44\1\uffff\17"+
			"\44\1\161\12\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\162\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\162\7\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\163\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\163\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\166\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\166\16\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\170\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\170\7\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\7\44\1\172\22\44\4\uffff\1\44\1\uffff\7"+
			"\44\1\172\22\44",
			"\1\35\1\uffff\12\44\7\uffff\17\44\1\173\12\44\4\uffff\1\44\1\uffff\17"+
			"\44\1\173\12\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\174\25\44\4\uffff\1\44\1\uffff\4"+
			"\44\1\174\25\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\175\13\44\4\uffff\1\44\1\uffff\16"+
			"\44\1\175\13\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\176\7\44\4\uffff\1\44\1\uffff\22"+
			"\44\1\176\7\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\177\16\44\4\uffff\1\44\1\uffff\13"+
			"\44\1\177\16\44",
			"\1\35\1\uffff\12\44\7\uffff\7\44\1\u0080\22\44\4\uffff\1\44\1\uffff"+
			"\7\44\1\u0080\22\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u0082\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u0082\14\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u0084\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u0084\25\44",
			"\1\35\1\uffff\12\44\7\uffff\17\44\1\u0085\1\44\1\u0086\10\44\4\uffff"+
			"\1\44\1\uffff\17\44\1\u0085\1\44\1\u0086\10\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u0087\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u0087\21\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u0089\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u0089\25\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u008a\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u008a\7\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u008c\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u008c\10\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u008d\31\44\4\uffff\1\44\1\uffff\1\u008d"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u008e\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u008e\14\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u008f\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u008f\27\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u0090\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u0090\5\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u0091\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u0091\27\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\u0093\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u0093\23\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u0094\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u0094\25\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u0095\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u0095\25\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u0096\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u0096\6\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u0097\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u0097\21\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u0098\31\44\4\uffff\1\44\1\uffff\1\u0098"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u0099\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u0099\6\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u009a\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u009a\10\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\30\44\1\u009b\1\44\4\uffff\1\44\1\uffff"+
			"\30\44\1\u009b\1\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\17\44\1\u009c\12\44\4\uffff\1\44\1\uffff"+
			"\17\44\1\u009c\12\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u009f\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u009f\10\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00a1\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00a1\6\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u00a2\31\44\4\uffff\1\44\1\uffff\1\u00a2"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\u00a3\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\u00a4\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u00a4\23\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00a5\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00a5\27\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u00a7\31\44\4\uffff\1\44\1\uffff\1\u00a7"+
			"\31\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u00a8\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00a8\13\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00a9\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00a9\10\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00aa\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00aa\25\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00ab\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00ab\5\44",
			"\1\35\1\uffff\12\44\7\uffff\13\44\1\u00ac\16\44\4\uffff\1\44\1\uffff"+
			"\13\44\1\u00ac\16\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00ae\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00ae\25\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00af\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00af\25\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u00b0\31\44\4\uffff\1\44\1\uffff\1\u00b0"+
			"\31\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00b1\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00b1\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00b4\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00b4\25\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00b5\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00b5\7\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00b6\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00b6\27\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00b7\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00b7\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00b9\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00b9\6\44",
			"\1\35\1\uffff\12\44\7\uffff\1\u00ba\31\44\4\uffff\1\44\1\uffff\1\u00ba"+
			"\31\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00bb\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00bb\6\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00bc\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00bc\7\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u00bd\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00bd\21\44",
			"\1\35\1\uffff\12\44\7\uffff\20\44\1\u00be\11\44\4\uffff\1\44\1\uffff"+
			"\20\44\1\u00be\11\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00c0\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00c0\6\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\u00c1\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u00c1\23\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00c2\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00c2\14\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\u00c5\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00c5\15\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\u00c6\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00c7\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00c7\7\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00c8\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00c8\7\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00c9\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00c9\6\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00ca\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00ca\10\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00cc\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00cc\6\44",
			"\1\35\1\uffff\12\44\7\uffff\23\44\1\u00cd\6\44\4\uffff\1\44\1\uffff"+
			"\23\44\1\u00cd\6\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00ce\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00ce\10\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u00cf\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00cf\21\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00d0\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00d0\27\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00d3\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00d3\14\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00d4\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00d4\5\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00d6\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00d6\25\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00d7\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00d7\7\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u00d9\1\44\1\u00da\11\44\4\uffff"+
			"\1\44\1\uffff\16\44\1\u00d9\1\44\1\u00da\11\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00db\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00db\7\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00dd\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00dd\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00df\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00df\25\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\10\44\1\u00e1\21\44\4\uffff\1\44\1\uffff"+
			"\10\44\1\u00e1\21\44",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00e2\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00e2\27\44",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00e3\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00e3\25\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u00e5\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00e5\13\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00e8\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00e8\14\44",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00e9\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00e9\5\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00ec\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00ec\14\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\15\44\1\u00ed\14\44\4\uffff\1\44\1\uffff"+
			"\15\44\1\u00ed\14\44",
			"\1\35\1\uffff\12\44\7\uffff\22\44\1\u00ee\7\44\4\uffff\1\44\1\uffff"+
			"\22\44\1\u00ee\7\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00f0\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00f0\10\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\4\44\1\u00f1\25\44\4\uffff\1\44\1\uffff"+
			"\4\44\1\u00f1\25\44",
			"\1\35\1\uffff\12\44\7\uffff\16\44\1\u00f2\13\44\4\uffff\1\44\1\uffff"+
			"\16\44\1\u00f2\13\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\2\44\1\u00f3\27\44\4\uffff\1\44\1\uffff"+
			"\2\44\1\u00f3\27\44",
			"\1\35\1\uffff\12\44\7\uffff\6\44\1\u00f4\23\44\4\uffff\1\44\1\uffff"+
			"\6\44\1\u00f4\23\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00f6\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00f6\5\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\21\44\1\u00f8\10\44\4\uffff\1\44\1\uffff"+
			"\21\44\1\u00f8\10\44",
			"\1\35\1\uffff\12\44\7\uffff\30\44\1\u00f9\1\44\4\uffff\1\44\1\uffff"+
			"\30\44\1\u00f9\1\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\u00fb\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00fb\15\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\24\44\1\u00fc\5\44\4\uffff\1\44\1\uffff"+
			"\24\44\1\u00fc\5\44",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			"\1\35\1\uffff\12\44\7\uffff\14\44\1\u00ff\15\44\4\uffff\1\44\1\uffff"+
			"\14\44\1\u00ff\15\44",
			"",
			"",
			"\1\35\1\uffff\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
			""
	};

	static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
	static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
	static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
	static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
	static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
	static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
	static final short[][] DFA5_transition;

	static {
		int numStates = DFA5_transitionS.length;
		DFA5_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
		}
	}

	protected class DFA5 extends DFA {

		public DFA5(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 5;
			this.eot = DFA5_eot;
			this.eof = DFA5_eof;
			this.min = DFA5_min;
			this.max = DFA5_max;
			this.accept = DFA5_accept;
			this.special = DFA5_special;
			this.transition = DFA5_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_NOT | T_WITH | T_DROP | T_TABLE | T_IF | T_EXISTS | T_AND | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_TRUE | T_FALSE | T_CONSISTENCY | T_ALL | T_ANY | T_QUORUM | T_ONE | T_TWO | T_THREE | T_EACH_QUORUM | T_LOCAL_ONE | T_LOCAL_QUORUM | T_EXPLAIN | T_PLAN | T_FOR | T_STOP | T_PROCESS | T_TRIGGER | T_ON | T_USING | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_SEMICOLON | T_EQUAL | T_POINT | T_START_SBRACKET | T_END_SBRACKET | T_COLON | T_COMMA | T_START_BRACKET | T_END_BRACKET | T_CONSTANT | T_IDENT | T_TERM | WS );";
		}
	}

}
