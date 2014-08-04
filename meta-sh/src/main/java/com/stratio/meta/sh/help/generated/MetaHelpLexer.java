/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// $ANTLR 3.5.1 MetaHelp.g 2014-06-09 11:45:59

	package com.stratio.meta.sh.help.generated;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MetaHelpLexer extends Lexer {
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
	public static final int T_KEYSPACES=56;
	public static final int T_LIKE=57;
	public static final int T_LIST=58;
	public static final int T_LUCENE=59;
	public static final int T_OPTIONS=60;
	public static final int T_ORDER=61;
	public static final int T_PLAN=62;
	public static final int T_PRIMARY=63;
	public static final int T_PROCESS=64;
	public static final int T_QUIT=65;
	public static final int T_REMOVE=66;
	public static final int T_SELECT=67;
	public static final int T_SEMICOLON=68;
	public static final int T_SET=69;
	public static final int T_STOP=70;
	public static final int T_STORAGE=71;
	public static final int T_TABLE=72;
	public static final int T_TABLES=73;
	public static final int T_TRIGGER=74;
	public static final int T_TRUNCATE=75;
	public static final int T_TYPE=76;
	public static final int T_UDF=77;
	public static final int T_UPDATE=78;
	public static final int T_USE=79;
	public static final int T_USING=80;
	public static final int T_VALUES=81;
	public static final int U=82;
	public static final int V=83;
	public static final int W=84;
	public static final int WS=85;
	public static final int X=86;
	public static final int Y=87;
	public static final int Z=88;




	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public MetaHelpLexer() {} 
	public MetaHelpLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public MetaHelpLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "MetaHelp.g"; }

	// $ANTLR start "A"
	public final void mA() throws RecognitionException {
		try {
			// MetaHelp.g:51:11: ( ( 'a' | 'A' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:52:11: ( ( 'b' | 'B' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:53:11: ( ( 'c' | 'C' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:54:11: ( ( 'd' | 'D' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:55:11: ( ( 'e' | 'E' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:56:11: ( ( 'f' | 'F' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:57:11: ( ( 'g' | 'G' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:58:11: ( ( 'h' | 'H' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:59:11: ( ( 'i' | 'I' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:60:11: ( ( 'j' | 'J' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:61:11: ( ( 'k' | 'K' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:62:11: ( ( 'l' | 'L' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:63:11: ( ( 'm' | 'M' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:64:11: ( ( 'n' | 'N' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:65:11: ( ( 'o' | 'O' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:66:11: ( ( 'p' | 'P' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:67:11: ( ( 'q' | 'Q' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:68:11: ( ( 'r' | 'R' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:69:11: ( ( 's' | 'S' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:70:11: ( ( 't' | 'T' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:71:11: ( ( 'u' | 'U' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:72:11: ( ( 'v' | 'V' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:73:11: ( ( 'w' | 'W' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:74:11: ( ( 'x' | 'X' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:75:11: ( ( 'y' | 'Y' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:76:11: ( ( 'z' | 'Z' ) )
			// MetaHelp.g:
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
			// MetaHelp.g:77:19: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
			// MetaHelp.g:77:21: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// MetaHelp.g:77:31: ( '+' | '-' )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0=='+'||LA1_0=='-') ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// MetaHelp.g:
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

			// MetaHelp.g:77:42: ( '0' .. '9' )+
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
					// MetaHelp.g:
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
			// MetaHelp.g:78:15: ( '.' )
			// MetaHelp.g:78:17: '.'
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
			// MetaHelp.g:81:11: ( T R U N C A T E )
			// MetaHelp.g:81:13: T R U N C A T E
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
			// MetaHelp.g:82:9: ( C R E A T E )
			// MetaHelp.g:82:11: C R E A T E
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
			// MetaHelp.g:83:8: ( A L T E R )
			// MetaHelp.g:83:10: A L T E R
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
			// MetaHelp.g:84:11: ( K E Y S P A C E )
			// MetaHelp.g:84:13: K E Y S P A C E
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
			// MetaHelp.g:85:12: ( K E Y S P A C E S )
			// MetaHelp.g:85:14: K E Y S P A C E S
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

	// $ANTLR start "T_DROP"
	public final void mT_DROP() throws RecognitionException {
		try {
			int _type = T_DROP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:86:7: ( D R O P )
			// MetaHelp.g:86:9: D R O P
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
			// MetaHelp.g:87:8: ( T A B L E )
			// MetaHelp.g:87:10: T A B L E
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
			// MetaHelp.g:88:9: ( T A B L E S )
			// MetaHelp.g:88:11: T A B L E S
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

	// $ANTLR start "T_USE"
	public final void mT_USE() throws RecognitionException {
		try {
			int _type = T_USE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:89:6: ( U S E )
			// MetaHelp.g:89:8: U S E
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
			// MetaHelp.g:90:6: ( S E T )
			// MetaHelp.g:90:8: S E T
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
			// MetaHelp.g:91:10: ( O P T I O N S )
			// MetaHelp.g:91:12: O P T I O N S
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
			// MetaHelp.g:92:12: ( A N A L Y T I C S )
			// MetaHelp.g:92:14: A N A L Y T I C S
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

	// $ANTLR start "T_CONSISTENCY"
	public final void mT_CONSISTENCY() throws RecognitionException {
		try {
			int _type = T_CONSISTENCY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:93:14: ( C O N S I S T E N C Y )
			// MetaHelp.g:93:16: C O N S I S T E N C Y
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

	// $ANTLR start "T_EXPLAIN"
	public final void mT_EXPLAIN() throws RecognitionException {
		try {
			int _type = T_EXPLAIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:94:10: ( E X P L A I N )
			// MetaHelp.g:94:12: E X P L A I N
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
			// MetaHelp.g:95:7: ( P L A N )
			// MetaHelp.g:95:9: P L A N
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
			// MetaHelp.g:96:6: ( F O R )
			// MetaHelp.g:96:8: F O R
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
			// MetaHelp.g:97:8: ( I N D E X )
			// MetaHelp.g:97:10: I N D E X
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

	// $ANTLR start "T_HASH"
	public final void mT_HASH() throws RecognitionException {
		try {
			int _type = T_HASH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:98:7: ( H A S H )
			// MetaHelp.g:98:9: H A S H
			{
			mH(); 

			mA(); 

			mS(); 

			mH(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_HASH"

	// $ANTLR start "T_LIST"
	public final void mT_LIST() throws RecognitionException {
		try {
			int _type = T_LIST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:99:7: ( L I S T )
			// MetaHelp.g:99:9: L I S T
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
			// MetaHelp.g:100:9: ( R E M O V E )
			// MetaHelp.g:100:11: R E M O V E
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
			// MetaHelp.g:101:6: ( U D F )
			// MetaHelp.g:101:8: U D F
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
			// MetaHelp.g:102:10: ( P R O C E S S )
			// MetaHelp.g:102:12: P R O C E S S
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
			// MetaHelp.g:103:10: ( T R I G G E R )
			// MetaHelp.g:103:12: T R I G G E R
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
			// MetaHelp.g:104:7: ( S T O P )
			// MetaHelp.g:104:9: S T O P
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

	// $ANTLR start "T_USING"
	public final void mT_USING() throws RecognitionException {
		try {
			int _type = T_USING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:105:8: ( U S I N G )
			// MetaHelp.g:105:10: U S I N G
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
			// MetaHelp.g:106:7: ( T Y P E )
			// MetaHelp.g:106:9: T Y P E
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
			// MetaHelp.g:107:6: ( A D D )
			// MetaHelp.g:107:8: A D D
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
			// MetaHelp.g:108:10: ( P R I M A R Y )
			// MetaHelp.g:108:12: P R I M A R Y
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
			// MetaHelp.g:109:6: ( K E Y )
			// MetaHelp.g:109:8: K E Y
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
			// MetaHelp.g:110:9: ( I N S E R T )
			// MetaHelp.g:110:11: I N S E R T
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
			// MetaHelp.g:111:7: ( I N T O )
			// MetaHelp.g:111:9: I N T O
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
			// MetaHelp.g:112:10: ( C O M P A C T )
			// MetaHelp.g:112:12: C O M P A C T
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
			// MetaHelp.g:113:10: ( S T O R A G E )
			// MetaHelp.g:113:12: S T O R A G E
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
			// MetaHelp.g:114:13: ( C L U S T E R I N G )
			// MetaHelp.g:114:15: C L U S T E R I N G
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
			// MetaHelp.g:115:8: ( O R D E R )
			// MetaHelp.g:115:10: O R D E R
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
			// MetaHelp.g:116:9: ( S E L E C T )
			// MetaHelp.g:116:11: S E L E C T
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
			// MetaHelp.g:117:9: ( V A L U E S )
			// MetaHelp.g:117:11: V A L U E S
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
			// MetaHelp.g:118:9: ( U P D A T E )
			// MetaHelp.g:118:11: U P D A T E
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

	// $ANTLR start "T_FROM"
	public final void mT_FROM() throws RecognitionException {
		try {
			int _type = T_FROM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:119:7: ( F R O M )
			// MetaHelp.g:119:9: F R O M
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
			// MetaHelp.g:120:9: ( D E L E T E )
			// MetaHelp.g:120:11: D E L E T E
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

	// $ANTLR start "T_DISABLE"
	public final void mT_DISABLE() throws RecognitionException {
		try {
			int _type = T_DISABLE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:121:10: ( D I S A B L E )
			// MetaHelp.g:121:12: D I S A B L E
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
			// MetaHelp.g:122:11: ( D I S T I N C T )
			// MetaHelp.g:122:13: D I S T I N C T
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
			// MetaHelp.g:123:8: ( C O U N T )
			// MetaHelp.g:123:10: C O U N T
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
			// MetaHelp.g:124:5: ( A S )
			// MetaHelp.g:124:7: A S
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
			// MetaHelp.g:125:10: ( B E T W E E N )
			// MetaHelp.g:125:12: B E T W E E N
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
			// MetaHelp.g:126:6: ( A S C )
			// MetaHelp.g:126:8: A S C
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
			// MetaHelp.g:127:7: ( D E S C )
			// MetaHelp.g:127:9: D E S C
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
			// MetaHelp.g:128:7: ( L I K E )
			// MetaHelp.g:128:9: L I K E
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

	// $ANTLR start "T_HELP"
	public final void mT_HELP() throws RecognitionException {
		try {
			int _type = T_HELP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:129:7: ( H E L P )
			// MetaHelp.g:129:9: H E L P
			{
			mH(); 

			mE(); 

			mL(); 

			mP(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_HELP"

	// $ANTLR start "T_EXIT"
	public final void mT_EXIT() throws RecognitionException {
		try {
			int _type = T_EXIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:130:7: ( E X I T )
			// MetaHelp.g:130:9: E X I T
			{
			mE(); 

			mX(); 

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
	// $ANTLR end "T_EXIT"

	// $ANTLR start "T_QUIT"
	public final void mT_QUIT() throws RecognitionException {
		try {
			int _type = T_QUIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:131:7: ( Q U I T )
			// MetaHelp.g:131:9: Q U I T
			{
			mQ(); 

			mU(); 

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
	// $ANTLR end "T_QUIT"

	// $ANTLR start "T_DATATYPES"
	public final void mT_DATATYPES() throws RecognitionException {
		try {
			int _type = T_DATATYPES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:132:12: ( D A T A T Y P E S )
			// MetaHelp.g:132:14: D A T A T Y P E S
			{
			mD(); 

			mA(); 

			mT(); 

			mA(); 

			mT(); 

			mY(); 

			mP(); 

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
	// $ANTLR end "T_DATATYPES"

	// $ANTLR start "T_LUCENE"
	public final void mT_LUCENE() throws RecognitionException {
		try {
			int _type = T_LUCENE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:133:9: ( L U C E N E )
			// MetaHelp.g:133:11: L U C E N E
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

	// $ANTLR start "T_DEFAULT"
	public final void mT_DEFAULT() throws RecognitionException {
		try {
			int _type = T_DEFAULT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:134:10: ( D E F A U L T )
			// MetaHelp.g:134:12: D E F A U L T
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

	// $ANTLR start "T_DESCRIBE"
	public final void mT_DESCRIBE() throws RecognitionException {
		try {
			int _type = T_DESCRIBE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:135:11: ( D E S C R I B E )
			// MetaHelp.g:135:13: D E S C R I B E
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

	// $ANTLR start "T_SEMICOLON"
	public final void mT_SEMICOLON() throws RecognitionException {
		try {
			int _type = T_SEMICOLON;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:137:12: ( ';' )
			// MetaHelp.g:137:14: ';'
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

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MetaHelp.g:232:3: ( ( ' ' | '\\t' | '\\n' | '\\r' )+ )
			// MetaHelp.g:232:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			{
			// MetaHelp.g:232:5: ( ' ' | '\\t' | '\\n' | '\\r' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '\t' && LA3_0 <= '\n')||LA3_0=='\r'||LA3_0==' ') ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// MetaHelp.g:
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
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
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
		// MetaHelp.g:1:8: ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_KEYSPACES | T_DROP | T_TABLE | T_TABLES | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_CONSISTENCY | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_HASH | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_FROM | T_DELETE | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_HELP | T_EXIT | T_QUIT | T_DATATYPES | T_LUCENE | T_DEFAULT | T_DESCRIBE | T_SEMICOLON | WS )
		int alt4=57;
		alt4 = dfa4.predict(input);
		switch (alt4) {
			case 1 :
				// MetaHelp.g:1:10: T_TRUNCATE
				{
				mT_TRUNCATE(); 

				}
				break;
			case 2 :
				// MetaHelp.g:1:21: T_CREATE
				{
				mT_CREATE(); 

				}
				break;
			case 3 :
				// MetaHelp.g:1:30: T_ALTER
				{
				mT_ALTER(); 

				}
				break;
			case 4 :
				// MetaHelp.g:1:38: T_KEYSPACE
				{
				mT_KEYSPACE(); 

				}
				break;
			case 5 :
				// MetaHelp.g:1:49: T_KEYSPACES
				{
				mT_KEYSPACES(); 

				}
				break;
			case 6 :
				// MetaHelp.g:1:61: T_DROP
				{
				mT_DROP(); 

				}
				break;
			case 7 :
				// MetaHelp.g:1:68: T_TABLE
				{
				mT_TABLE(); 

				}
				break;
			case 8 :
				// MetaHelp.g:1:76: T_TABLES
				{
				mT_TABLES(); 

				}
				break;
			case 9 :
				// MetaHelp.g:1:85: T_USE
				{
				mT_USE(); 

				}
				break;
			case 10 :
				// MetaHelp.g:1:91: T_SET
				{
				mT_SET(); 

				}
				break;
			case 11 :
				// MetaHelp.g:1:97: T_OPTIONS
				{
				mT_OPTIONS(); 

				}
				break;
			case 12 :
				// MetaHelp.g:1:107: T_ANALYTICS
				{
				mT_ANALYTICS(); 

				}
				break;
			case 13 :
				// MetaHelp.g:1:119: T_CONSISTENCY
				{
				mT_CONSISTENCY(); 

				}
				break;
			case 14 :
				// MetaHelp.g:1:133: T_EXPLAIN
				{
				mT_EXPLAIN(); 

				}
				break;
			case 15 :
				// MetaHelp.g:1:143: T_PLAN
				{
				mT_PLAN(); 

				}
				break;
			case 16 :
				// MetaHelp.g:1:150: T_FOR
				{
				mT_FOR(); 

				}
				break;
			case 17 :
				// MetaHelp.g:1:156: T_INDEX
				{
				mT_INDEX(); 

				}
				break;
			case 18 :
				// MetaHelp.g:1:164: T_HASH
				{
				mT_HASH(); 

				}
				break;
			case 19 :
				// MetaHelp.g:1:171: T_LIST
				{
				mT_LIST(); 

				}
				break;
			case 20 :
				// MetaHelp.g:1:178: T_REMOVE
				{
				mT_REMOVE(); 

				}
				break;
			case 21 :
				// MetaHelp.g:1:187: T_UDF
				{
				mT_UDF(); 

				}
				break;
			case 22 :
				// MetaHelp.g:1:193: T_PROCESS
				{
				mT_PROCESS(); 

				}
				break;
			case 23 :
				// MetaHelp.g:1:203: T_TRIGGER
				{
				mT_TRIGGER(); 

				}
				break;
			case 24 :
				// MetaHelp.g:1:213: T_STOP
				{
				mT_STOP(); 

				}
				break;
			case 25 :
				// MetaHelp.g:1:220: T_USING
				{
				mT_USING(); 

				}
				break;
			case 26 :
				// MetaHelp.g:1:228: T_TYPE
				{
				mT_TYPE(); 

				}
				break;
			case 27 :
				// MetaHelp.g:1:235: T_ADD
				{
				mT_ADD(); 

				}
				break;
			case 28 :
				// MetaHelp.g:1:241: T_PRIMARY
				{
				mT_PRIMARY(); 

				}
				break;
			case 29 :
				// MetaHelp.g:1:251: T_KEY
				{
				mT_KEY(); 

				}
				break;
			case 30 :
				// MetaHelp.g:1:257: T_INSERT
				{
				mT_INSERT(); 

				}
				break;
			case 31 :
				// MetaHelp.g:1:266: T_INTO
				{
				mT_INTO(); 

				}
				break;
			case 32 :
				// MetaHelp.g:1:273: T_COMPACT
				{
				mT_COMPACT(); 

				}
				break;
			case 33 :
				// MetaHelp.g:1:283: T_STORAGE
				{
				mT_STORAGE(); 

				}
				break;
			case 34 :
				// MetaHelp.g:1:293: T_CLUSTERING
				{
				mT_CLUSTERING(); 

				}
				break;
			case 35 :
				// MetaHelp.g:1:306: T_ORDER
				{
				mT_ORDER(); 

				}
				break;
			case 36 :
				// MetaHelp.g:1:314: T_SELECT
				{
				mT_SELECT(); 

				}
				break;
			case 37 :
				// MetaHelp.g:1:323: T_VALUES
				{
				mT_VALUES(); 

				}
				break;
			case 38 :
				// MetaHelp.g:1:332: T_UPDATE
				{
				mT_UPDATE(); 

				}
				break;
			case 39 :
				// MetaHelp.g:1:341: T_FROM
				{
				mT_FROM(); 

				}
				break;
			case 40 :
				// MetaHelp.g:1:348: T_DELETE
				{
				mT_DELETE(); 

				}
				break;
			case 41 :
				// MetaHelp.g:1:357: T_DISABLE
				{
				mT_DISABLE(); 

				}
				break;
			case 42 :
				// MetaHelp.g:1:367: T_DISTINCT
				{
				mT_DISTINCT(); 

				}
				break;
			case 43 :
				// MetaHelp.g:1:378: T_COUNT
				{
				mT_COUNT(); 

				}
				break;
			case 44 :
				// MetaHelp.g:1:386: T_AS
				{
				mT_AS(); 

				}
				break;
			case 45 :
				// MetaHelp.g:1:391: T_BETWEEN
				{
				mT_BETWEEN(); 

				}
				break;
			case 46 :
				// MetaHelp.g:1:401: T_ASC
				{
				mT_ASC(); 

				}
				break;
			case 47 :
				// MetaHelp.g:1:407: T_DESC
				{
				mT_DESC(); 

				}
				break;
			case 48 :
				// MetaHelp.g:1:414: T_LIKE
				{
				mT_LIKE(); 

				}
				break;
			case 49 :
				// MetaHelp.g:1:421: T_HELP
				{
				mT_HELP(); 

				}
				break;
			case 50 :
				// MetaHelp.g:1:428: T_EXIT
				{
				mT_EXIT(); 

				}
				break;
			case 51 :
				// MetaHelp.g:1:435: T_QUIT
				{
				mT_QUIT(); 

				}
				break;
			case 52 :
				// MetaHelp.g:1:442: T_DATATYPES
				{
				mT_DATATYPES(); 

				}
				break;
			case 53 :
				// MetaHelp.g:1:454: T_LUCENE
				{
				mT_LUCENE(); 

				}
				break;
			case 54 :
				// MetaHelp.g:1:463: T_DEFAULT
				{
				mT_DEFAULT(); 

				}
				break;
			case 55 :
				// MetaHelp.g:1:473: T_DESCRIBE
				{
				mT_DESCRIBE(); 

				}
				break;
			case 56 :
				// MetaHelp.g:1:484: T_SEMICOLON
				{
				mT_SEMICOLON(); 

				}
				break;
			case 57 :
				// MetaHelp.g:1:496: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA4 dfa4 = new DFA4(this);
	static final String DFA4_eotS =
		"\36\uffff\1\73\36\uffff\1\121\25\uffff\1\132\4\uffff\1\134\7\uffff\1\141"+
		"\2\uffff";
	static final String DFA4_eofS =
		"\143\uffff";
	static final String DFA4_minS =
		"\1\11\1\101\1\114\1\104\1\105\1\101\1\104\1\105\1\120\1\130\1\114\1\117"+
		"\1\116\1\101\1\111\6\uffff\1\111\1\102\2\uffff\1\115\4\uffff\1\103\1\131"+
		"\1\uffff\1\106\1\123\1\uffff\1\105\2\uffff\1\114\1\117\2\uffff\1\111\1"+
		"\uffff\1\111\2\uffff\1\104\2\uffff\1\113\3\uffff\1\114\5\uffff\1\123\1"+
		"\uffff\1\103\1\uffff\1\101\4\uffff\1\120\11\uffff\1\105\1\uffff\1\120"+
		"\1\122\4\uffff\1\123\1\101\4\uffff\1\103\1\105\1\123\2\uffff";
	static final String DFA4_maxS =
		"\1\166\1\171\1\162\1\163\1\145\1\162\1\163\1\164\1\162\1\170\2\162\1\156"+
		"\1\145\1\165\6\uffff\1\165\1\142\2\uffff\1\165\4\uffff\1\143\1\171\1\uffff"+
		"\2\163\1\uffff\1\151\2\uffff\1\164\1\157\2\uffff\1\160\1\uffff\1\157\2"+
		"\uffff\1\164\2\uffff\1\163\3\uffff\1\154\5\uffff\1\163\1\uffff\1\143\1"+
		"\uffff\1\164\4\uffff\1\162\11\uffff\1\145\1\uffff\1\160\1\162\4\uffff"+
		"\1\163\1\141\4\uffff\1\143\1\145\1\163\2\uffff";
	static final String DFA4_acceptS =
		"\17\uffff\1\24\1\45\1\55\1\63\1\70\1\71\2\uffff\1\32\1\2\1\uffff\1\42"+
		"\1\3\1\14\1\33\2\uffff\1\6\2\uffff\1\64\1\uffff\1\25\1\46\2\uffff\1\13"+
		"\1\43\1\uffff\1\17\1\uffff\1\20\1\47\1\uffff\1\22\1\61\1\uffff\1\65\1"+
		"\1\1\27\1\uffff\1\15\1\40\1\53\1\54\1\56\1\uffff\1\50\1\uffff\1\66\1\uffff"+
		"\1\11\1\31\1\12\1\44\1\uffff\1\16\1\62\1\26\1\34\1\21\1\36\1\37\1\23\1"+
		"\60\1\uffff\1\35\2\uffff\1\51\1\52\1\30\1\41\2\uffff\1\57\1\67\1\7\1\10"+
		"\3\uffff\1\4\1\5";
	static final String DFA4_specialS =
		"\143\uffff}>";
	static final String[] DFA4_transitionS = {
			"\2\24\2\uffff\1\24\22\uffff\1\24\32\uffff\1\23\5\uffff\1\3\1\21\1\2\1"+
			"\5\1\11\1\13\1\uffff\1\15\1\14\1\uffff\1\4\1\16\2\uffff\1\10\1\12\1\22"+
			"\1\17\1\7\1\1\1\6\1\20\12\uffff\1\3\1\21\1\2\1\5\1\11\1\13\1\uffff\1"+
			"\15\1\14\1\uffff\1\4\1\16\2\uffff\1\10\1\12\1\22\1\17\1\7\1\1\1\6\1\20",
			"\1\26\20\uffff\1\25\6\uffff\1\27\7\uffff\1\26\20\uffff\1\25\6\uffff"+
			"\1\27",
			"\1\32\2\uffff\1\31\2\uffff\1\30\31\uffff\1\32\2\uffff\1\31\2\uffff\1"+
			"\30",
			"\1\35\7\uffff\1\33\1\uffff\1\34\4\uffff\1\36\20\uffff\1\35\7\uffff\1"+
			"\33\1\uffff\1\34\4\uffff\1\36",
			"\1\37\37\uffff\1\37",
			"\1\43\3\uffff\1\41\3\uffff\1\42\10\uffff\1\40\16\uffff\1\43\3\uffff"+
			"\1\41\3\uffff\1\42\10\uffff\1\40",
			"\1\45\13\uffff\1\46\2\uffff\1\44\20\uffff\1\45\13\uffff\1\46\2\uffff"+
			"\1\44",
			"\1\47\16\uffff\1\50\20\uffff\1\47\16\uffff\1\50",
			"\1\51\1\uffff\1\52\35\uffff\1\51\1\uffff\1\52",
			"\1\53\37\uffff\1\53",
			"\1\54\5\uffff\1\55\31\uffff\1\54\5\uffff\1\55",
			"\1\56\2\uffff\1\57\34\uffff\1\56\2\uffff\1\57",
			"\1\60\37\uffff\1\60",
			"\1\61\3\uffff\1\62\33\uffff\1\61\3\uffff\1\62",
			"\1\63\13\uffff\1\64\23\uffff\1\63\13\uffff\1\64",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\66\13\uffff\1\65\23\uffff\1\66\13\uffff\1\65",
			"\1\67\37\uffff\1\67",
			"",
			"",
			"\1\71\1\70\6\uffff\1\72\27\uffff\1\71\1\70\6\uffff\1\72",
			"",
			"",
			"",
			"",
			"\1\74\37\uffff\1\74",
			"\1\75\37\uffff\1\75",
			"",
			"\1\100\5\uffff\1\76\6\uffff\1\77\22\uffff\1\100\5\uffff\1\76\6\uffff"+
			"\1\77",
			"\1\101\37\uffff\1\101",
			"",
			"\1\102\3\uffff\1\103\33\uffff\1\102\3\uffff\1\103",
			"",
			"",
			"\1\105\7\uffff\1\104\27\uffff\1\105\7\uffff\1\104",
			"\1\106\37\uffff\1\106",
			"",
			"",
			"\1\110\6\uffff\1\107\30\uffff\1\110\6\uffff\1\107",
			"",
			"\1\112\5\uffff\1\111\31\uffff\1\112\5\uffff\1\111",
			"",
			"",
			"\1\113\16\uffff\1\114\1\115\17\uffff\1\113\16\uffff\1\114\1\115",
			"",
			"",
			"\1\117\7\uffff\1\116\27\uffff\1\117\7\uffff\1\116",
			"",
			"",
			"",
			"\1\120\37\uffff\1\120",
			"",
			"",
			"",
			"",
			"",
			"\1\122\37\uffff\1\122",
			"",
			"\1\123\37\uffff\1\123",
			"",
			"\1\124\22\uffff\1\125\14\uffff\1\124\22\uffff\1\125",
			"",
			"",
			"",
			"",
			"\1\126\1\uffff\1\127\35\uffff\1\126\1\uffff\1\127",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\130\37\uffff\1\130",
			"",
			"\1\131\37\uffff\1\131",
			"\1\133\37\uffff\1\133",
			"",
			"",
			"",
			"",
			"\1\135\37\uffff\1\135",
			"\1\136\37\uffff\1\136",
			"",
			"",
			"",
			"",
			"\1\137\37\uffff\1\137",
			"\1\140\37\uffff\1\140",
			"\1\142\37\uffff\1\142",
			"",
			""
	};

	static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
	static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
	static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
	static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
	static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
	static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
	static final short[][] DFA4_transition;

	static {
		int numStates = DFA4_transitionS.length;
		DFA4_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
		}
	}

	protected class DFA4 extends DFA {

		public DFA4(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 4;
			this.eot = DFA4_eot;
			this.eof = DFA4_eof;
			this.min = DFA4_min;
			this.max = DFA4_max;
			this.accept = DFA4_accept;
			this.special = DFA4_special;
			this.transition = DFA4_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T_TRUNCATE | T_CREATE | T_ALTER | T_KEYSPACE | T_KEYSPACES | T_DROP | T_TABLE | T_TABLES | T_USE | T_SET | T_OPTIONS | T_ANALYTICS | T_CONSISTENCY | T_EXPLAIN | T_PLAN | T_FOR | T_INDEX | T_HASH | T_LIST | T_REMOVE | T_UDF | T_PROCESS | T_TRIGGER | T_STOP | T_USING | T_TYPE | T_ADD | T_PRIMARY | T_KEY | T_INSERT | T_INTO | T_COMPACT | T_STORAGE | T_CLUSTERING | T_ORDER | T_SELECT | T_VALUES | T_UPDATE | T_FROM | T_DELETE | T_DISABLE | T_DISTINCT | T_COUNT | T_AS | T_BETWEEN | T_ASC | T_DESC | T_LIKE | T_HELP | T_EXIT | T_QUIT | T_DATATYPES | T_LUCENE | T_DEFAULT | T_DESCRIBE | T_SEMICOLON | WS );";
		}
	}

}
