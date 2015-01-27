// Generated from /home/linjie/batfish/projects/batfish/src/batfish/grammar/bpf/BPFParser.g4 by ANTLR 4.4

package batfish.grammar.bpf;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BPFParser extends batfish.grammar.BatfishParser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		UDP=6, SCTP=7, SRC=16, HOST=10, ICMP=8, RANGE=25, PORT=13, NOT=19, ID=27, 
		AND=17, NUM=26, LPAREN=2, RPAREN=3, NET=11, WS=1, MASK=12, IP=4, PORTRANGE=14, 
		NID3=22, NID1=24, HID=21, NID2=23, OR=18, TCP=5, IGMP=9, CIDR=20, DST=15;
	public static final String[] tokenNames = {
		"<INVALID>", "WS", "'('", "')'", "'ip'", "'tcp'", "'udp'", "'sctp'", "'icmp'", 
		"'igmp'", "'host'", "'net'", "'mask'", "'port'", "'portrange'", "'dst'", 
		"'src'", "AND", "OR", "NOT", "CIDR", "HID", "NID3", "NID2", "NID1", "RANGE", 
		"NUM", "ID"
	};
	public static final int
		RULE_prog = 0, RULE_expr = 1, RULE_term = 2, RULE_id = 3, RULE_network_id = 4, 
		RULE_port_num = 5, RULE_port_range = 6, RULE_pqual = 7, RULE_dqual = 8, 
		RULE_tqual = 9;
	public static final String[] ruleNames = {
		"prog", "expr", "term", "id", "network_id", "port_num", "port_range", 
		"pqual", "dqual", "tqual"
	};

	@Override
	public String getGrammarFileName() { return "BPFParser.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public BPFParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(BPFParser.EOF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			setState(24);
			switch (_input.LA(1)) {
			case LPAREN:
			case IP:
			case TCP:
			case UDP:
			case SCTP:
			case ICMP:
			case IGMP:
			case HOST:
			case NET:
			case PORT:
			case PORTRANGE:
			case DST:
			case SRC:
			case NOT:
			case CIDR:
			case HID:
			case NID3:
			case NID2:
			case NID1:
			case RANGE:
			case NUM:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(20); expr(0);
				setState(21); match(EOF);
				}
				break;
			case EOF:
				enterOuterAlt(_localctx, 2);
				{
				setState(23); match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DisjunctionContext extends ExprContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode OR() { return getToken(BPFParser.OR, 0); }
		public DisjunctionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDisjunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConjunctionContext extends ExprContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode AND() { return getToken(BPFParser.AND, 0); }
		public ConjunctionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitConjunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SingletonContext extends ExprContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public SingletonContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitSingleton(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new SingletonContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(27); term();
			}
			_ctx.stop = _input.LT(-1);
			setState(37);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(35);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new ConjunctionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(29);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(30); match(AND);
						setState(31); term();
						}
						break;
					case 2:
						{
						_localctx = new DisjunctionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(32);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(33); match(OR);
						setState(34); term();
						}
						break;
					}
					} 
				}
				setState(39);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	 
		public TermContext() { }
		public void copyFrom(TermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PQualIdContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public PqualContext pqual() {
			return getRuleContext(PqualContext.class,0);
		}
		public PQualIdContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPQualId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TQualIdContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TqualContext tqual() {
			return getRuleContext(TqualContext.class,0);
		}
		public TQualIdContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitTQualId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenExprContext extends TermContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(BPFParser.RPAREN, 0); }
		public TerminalNode LPAREN() { return getToken(BPFParser.LPAREN, 0); }
		public ParenExprContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitParenExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdOnlyContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public IdOnlyContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitIdOnly(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PTQualIdContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public PqualContext pqual() {
			return getRuleContext(PqualContext.class,0);
		}
		public TqualContext tqual() {
			return getRuleContext(TqualContext.class,0);
		}
		public PTQualIdContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPTQualId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PDQualIdContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public PqualContext pqual() {
			return getRuleContext(PqualContext.class,0);
		}
		public DqualContext dqual() {
			return getRuleContext(DqualContext.class,0);
		}
		public PDQualIdContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPDQualId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegationContext extends TermContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode NOT() { return getToken(BPFParser.NOT, 0); }
		public NegationContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNegation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DTQualIdContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TqualContext tqual() {
			return getRuleContext(TqualContext.class,0);
		}
		public DqualContext dqual() {
			return getRuleContext(DqualContext.class,0);
		}
		public DTQualIdContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDTQualId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PDTQualIdContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public PqualContext pqual() {
			return getRuleContext(PqualContext.class,0);
		}
		public TqualContext tqual() {
			return getRuleContext(TqualContext.class,0);
		}
		public DqualContext dqual() {
			return getRuleContext(DqualContext.class,0);
		}
		public PDTQualIdContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPDTQualId(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProtoOnlyContext extends TermContext {
		public PqualContext pqual() {
			return getRuleContext(PqualContext.class,0);
		}
		public ProtoOnlyContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProtoOnly(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DQualIdContext extends TermContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public DqualContext dqual() {
			return getRuleContext(DqualContext.class,0);
		}
		public DQualIdContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDQualId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_term);
		try {
			setState(74);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				_localctx = new NegationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(40); match(NOT);
				setState(41); term();
				}
				break;
			case 2:
				_localctx = new PDTQualIdContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(42); pqual();
				setState(43); dqual();
				setState(44); tqual();
				setState(45); id();
				}
				break;
			case 3:
				_localctx = new PDQualIdContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(47); pqual();
				setState(48); dqual();
				setState(49); id();
				}
				break;
			case 4:
				_localctx = new PTQualIdContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(51); pqual();
				setState(52); tqual();
				setState(53); id();
				}
				break;
			case 5:
				_localctx = new DTQualIdContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(55); dqual();
				setState(56); tqual();
				setState(57); id();
				}
				break;
			case 6:
				_localctx = new PQualIdContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(59); pqual();
				setState(60); id();
				}
				break;
			case 7:
				_localctx = new DQualIdContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(62); dqual();
				setState(63); id();
				}
				break;
			case 8:
				_localctx = new TQualIdContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(65); tqual();
				setState(66); id();
				}
				break;
			case 9:
				_localctx = new IdOnlyContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(68); id();
				}
				break;
			case 10:
				_localctx = new ProtoOnlyContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(69); pqual();
				}
				break;
			case 11:
				_localctx = new ParenExprContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(70); match(LPAREN);
				setState(71); expr(0);
				setState(72); match(RPAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdContext extends ParserRuleContext {
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
	 
		public IdContext() { }
		public void copyFrom(IdContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PortContext extends IdContext {
		public Port_numContext port_num() {
			return getRuleContext(Port_numContext.class,0);
		}
		public PortContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPort(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotPortRangeContext extends IdContext {
		public Port_rangeContext port_range() {
			return getRuleContext(Port_rangeContext.class,0);
		}
		public TerminalNode NOT() { return getToken(BPFParser.NOT, 0); }
		public NotPortRangeContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNotPortRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NetworkContext extends IdContext {
		public Network_idContext network_id() {
			return getRuleContext(Network_idContext.class,0);
		}
		public NetworkContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNetwork(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotNetworkContext extends IdContext {
		public TerminalNode NOT() { return getToken(BPFParser.NOT, 0); }
		public Network_idContext network_id() {
			return getRuleContext(Network_idContext.class,0);
		}
		public NotNetworkContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNotNetwork(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PortRangeContext extends IdContext {
		public Port_rangeContext port_range() {
			return getRuleContext(Port_rangeContext.class,0);
		}
		public PortRangeContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPortRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotPortNameContext extends IdContext {
		public TerminalNode ID() { return getToken(BPFParser.ID, 0); }
		public TerminalNode NOT() { return getToken(BPFParser.NOT, 0); }
		public NotPortNameContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNotPortName(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotPortContext extends IdContext {
		public TerminalNode NOT() { return getToken(BPFParser.NOT, 0); }
		public Port_numContext port_num() {
			return getRuleContext(Port_numContext.class,0);
		}
		public NotPortContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNotPort(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PortNameContext extends IdContext {
		public TerminalNode ID() { return getToken(BPFParser.ID, 0); }
		public PortNameContext(IdContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPortName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_id);
		try {
			setState(88);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new NetworkContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(76); network_id();
				}
				break;
			case 2:
				_localctx = new PortContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(77); port_num();
				}
				break;
			case 3:
				_localctx = new PortRangeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(78); port_range();
				}
				break;
			case 4:
				_localctx = new PortNameContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(79); match(ID);
				}
				break;
			case 5:
				_localctx = new NotNetworkContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(80); match(NOT);
				setState(81); network_id();
				}
				break;
			case 6:
				_localctx = new NotPortContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(82); match(NOT);
				setState(83); port_num();
				}
				break;
			case 7:
				_localctx = new NotPortRangeContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(84); match(NOT);
				setState(85); port_range();
				}
				break;
			case 8:
				_localctx = new NotPortNameContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(86); match(NOT);
				setState(87); match(ID);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Network_idContext extends ParserRuleContext {
		public Network_idContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_network_id; }
	 
		public Network_idContext() { }
		public void copyFrom(Network_idContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Net1Context extends Network_idContext {
		public TerminalNode NID1() { return getToken(BPFParser.NID1, 0); }
		public Net1Context(Network_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNet1(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Net2Context extends Network_idContext {
		public TerminalNode NID2() { return getToken(BPFParser.NID2, 0); }
		public Net2Context(Network_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNet2(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Net3Context extends Network_idContext {
		public TerminalNode NID3() { return getToken(BPFParser.NID3, 0); }
		public Net3Context(Network_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitNet3(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class HostContext extends Network_idContext {
		public TerminalNode HID() { return getToken(BPFParser.HID, 0); }
		public HostContext(Network_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitHost(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrefixContext extends Network_idContext {
		public TerminalNode CIDR() { return getToken(BPFParser.CIDR, 0); }
		public PrefixContext(Network_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPrefix(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MaskedContext extends Network_idContext {
		public List<TerminalNode> HID() { return getTokens(BPFParser.HID); }
		public TerminalNode MASK() { return getToken(BPFParser.MASK, 0); }
		public TerminalNode HID(int i) {
			return getToken(BPFParser.HID, i);
		}
		public MaskedContext(Network_idContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitMasked(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Network_idContext network_id() throws RecognitionException {
		Network_idContext _localctx = new Network_idContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_network_id);
		try {
			setState(98);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				_localctx = new PrefixContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(90); match(CIDR);
				}
				break;
			case 2:
				_localctx = new MaskedContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(91); match(HID);
				setState(92); match(MASK);
				setState(93); match(HID);
				}
				break;
			case 3:
				_localctx = new HostContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(94); match(HID);
				}
				break;
			case 4:
				_localctx = new Net1Context(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(95); match(NID1);
				}
				break;
			case 5:
				_localctx = new Net2Context(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(96); match(NID2);
				}
				break;
			case 6:
				_localctx = new Net3Context(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(97); match(NID3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Port_numContext extends ParserRuleContext {
		public Port_numContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_port_num; }
	 
		public Port_numContext() { }
		public void copyFrom(Port_numContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ParenPortNumContext extends Port_numContext {
		public TerminalNode RPAREN() { return getToken(BPFParser.RPAREN, 0); }
		public Port_numContext port_num() {
			return getRuleContext(Port_numContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(BPFParser.LPAREN, 0); }
		public ParenPortNumContext(Port_numContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitParenPortNum(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PortNumContext extends Port_numContext {
		public TerminalNode NUM() { return getToken(BPFParser.NUM, 0); }
		public PortNumContext(Port_numContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPortNum(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SmallPortNumContext extends Port_numContext {
		public TerminalNode NID1() { return getToken(BPFParser.NID1, 0); }
		public SmallPortNumContext(Port_numContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitSmallPortNum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Port_numContext port_num() throws RecognitionException {
		Port_numContext _localctx = new Port_numContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_port_num);
		try {
			setState(106);
			switch (_input.LA(1)) {
			case NUM:
				_localctx = new PortNumContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(100); match(NUM);
				}
				break;
			case NID1:
				_localctx = new SmallPortNumContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(101); match(NID1);
				}
				break;
			case LPAREN:
				_localctx = new ParenPortNumContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(102); match(LPAREN);
				setState(103); port_num();
				setState(104); match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Port_rangeContext extends ParserRuleContext {
		public Port_rangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_port_range; }
	 
		public Port_rangeContext() { }
		public void copyFrom(Port_rangeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PortNumRangeContext extends Port_rangeContext {
		public TerminalNode RANGE() { return getToken(BPFParser.RANGE, 0); }
		public PortNumRangeContext(Port_rangeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitPortNumRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenPortNumRangeContext extends Port_rangeContext {
		public Port_rangeContext port_range() {
			return getRuleContext(Port_rangeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(BPFParser.RPAREN, 0); }
		public TerminalNode LPAREN() { return getToken(BPFParser.LPAREN, 0); }
		public ParenPortNumRangeContext(Port_rangeContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitParenPortNumRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Port_rangeContext port_range() throws RecognitionException {
		Port_rangeContext _localctx = new Port_rangeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_port_range);
		try {
			setState(113);
			switch (_input.LA(1)) {
			case RANGE:
				_localctx = new PortNumRangeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(108); match(RANGE);
				}
				break;
			case LPAREN:
				_localctx = new ParenPortNumRangeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(109); match(LPAREN);
				setState(110); port_range();
				setState(111); match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PqualContext extends ParserRuleContext {
		public PqualContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pqual; }
	 
		public PqualContext() { }
		public void copyFrom(PqualContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ProtoSCTPContext extends PqualContext {
		public TerminalNode SCTP() { return getToken(BPFParser.SCTP, 0); }
		public ProtoSCTPContext(PqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProtoSCTP(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProtoTCPContext extends PqualContext {
		public TerminalNode TCP() { return getToken(BPFParser.TCP, 0); }
		public ProtoTCPContext(PqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProtoTCP(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProtoICMPContext extends PqualContext {
		public TerminalNode ICMP() { return getToken(BPFParser.ICMP, 0); }
		public ProtoICMPContext(PqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProtoICMP(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProtoUDPContext extends PqualContext {
		public TerminalNode UDP() { return getToken(BPFParser.UDP, 0); }
		public ProtoUDPContext(PqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProtoUDP(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProtoIPContext extends PqualContext {
		public TerminalNode IP() { return getToken(BPFParser.IP, 0); }
		public ProtoIPContext(PqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProtoIP(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProtoIGMPContext extends PqualContext {
		public TerminalNode IGMP() { return getToken(BPFParser.IGMP, 0); }
		public ProtoIGMPContext(PqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitProtoIGMP(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PqualContext pqual() throws RecognitionException {
		PqualContext _localctx = new PqualContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_pqual);
		try {
			setState(121);
			switch (_input.LA(1)) {
			case IP:
				_localctx = new ProtoIPContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(115); match(IP);
				}
				break;
			case TCP:
				_localctx = new ProtoTCPContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(116); match(TCP);
				}
				break;
			case UDP:
				_localctx = new ProtoUDPContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(117); match(UDP);
				}
				break;
			case SCTP:
				_localctx = new ProtoSCTPContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(118); match(SCTP);
				}
				break;
			case ICMP:
				_localctx = new ProtoICMPContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(119); match(ICMP);
				}
				break;
			case IGMP:
				_localctx = new ProtoIGMPContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(120); match(IGMP);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DqualContext extends ParserRuleContext {
		public DqualContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dqual; }
	 
		public DqualContext() { }
		public void copyFrom(DqualContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DirSrcAndDstContext extends DqualContext {
		public TerminalNode AND() { return getToken(BPFParser.AND, 0); }
		public TerminalNode SRC() { return getToken(BPFParser.SRC, 0); }
		public TerminalNode DST() { return getToken(BPFParser.DST, 0); }
		public DirSrcAndDstContext(DqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDirSrcAndDst(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DirDstOrSrcContext extends DqualContext {
		public TerminalNode OR() { return getToken(BPFParser.OR, 0); }
		public TerminalNode SRC() { return getToken(BPFParser.SRC, 0); }
		public TerminalNode DST() { return getToken(BPFParser.DST, 0); }
		public DirDstOrSrcContext(DqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDirDstOrSrc(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DirSrcOrDstContext extends DqualContext {
		public TerminalNode OR() { return getToken(BPFParser.OR, 0); }
		public TerminalNode SRC() { return getToken(BPFParser.SRC, 0); }
		public TerminalNode DST() { return getToken(BPFParser.DST, 0); }
		public DirSrcOrDstContext(DqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDirSrcOrDst(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DirDstContext extends DqualContext {
		public TerminalNode DST() { return getToken(BPFParser.DST, 0); }
		public DirDstContext(DqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDirDst(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DirDstAndSrcContext extends DqualContext {
		public TerminalNode AND() { return getToken(BPFParser.AND, 0); }
		public TerminalNode SRC() { return getToken(BPFParser.SRC, 0); }
		public TerminalNode DST() { return getToken(BPFParser.DST, 0); }
		public DirDstAndSrcContext(DqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDirDstAndSrc(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DirSrcContext extends DqualContext {
		public TerminalNode SRC() { return getToken(BPFParser.SRC, 0); }
		public DirSrcContext(DqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitDirSrc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DqualContext dqual() throws RecognitionException {
		DqualContext _localctx = new DqualContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_dqual);
		try {
			setState(137);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				_localctx = new DirSrcOrDstContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(123); match(SRC);
				setState(124); match(OR);
				setState(125); match(DST);
				}
				break;
			case 2:
				_localctx = new DirDstOrSrcContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(126); match(DST);
				setState(127); match(OR);
				setState(128); match(SRC);
				}
				break;
			case 3:
				_localctx = new DirSrcAndDstContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(129); match(SRC);
				setState(130); match(AND);
				setState(131); match(DST);
				}
				break;
			case 4:
				_localctx = new DirDstAndSrcContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(132); match(DST);
				setState(133); match(AND);
				setState(134); match(SRC);
				}
				break;
			case 5:
				_localctx = new DirSrcContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(135); match(SRC);
				}
				break;
			case 6:
				_localctx = new DirDstContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(136); match(DST);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TqualContext extends ParserRuleContext {
		public TqualContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tqual; }
	 
		public TqualContext() { }
		public void copyFrom(TqualContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TypeNetContext extends TqualContext {
		public TerminalNode NET() { return getToken(BPFParser.NET, 0); }
		public TypeNetContext(TqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitTypeNet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypePortContext extends TqualContext {
		public TerminalNode PORT() { return getToken(BPFParser.PORT, 0); }
		public TypePortContext(TqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitTypePort(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypePortRangeContext extends TqualContext {
		public TerminalNode PORTRANGE() { return getToken(BPFParser.PORTRANGE, 0); }
		public TypePortRangeContext(TqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitTypePortRange(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TypeHostContext extends TqualContext {
		public TerminalNode HOST() { return getToken(BPFParser.HOST, 0); }
		public TypeHostContext(TqualContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BPFParserVisitor ) return ((BPFParserVisitor<? extends T>)visitor).visitTypeHost(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TqualContext tqual() throws RecognitionException {
		TqualContext _localctx = new TqualContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_tqual);
		try {
			setState(143);
			switch (_input.LA(1)) {
			case HOST:
				_localctx = new TypeHostContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(139); match(HOST);
				}
				break;
			case NET:
				_localctx = new TypeNetContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(140); match(NET);
				}
				break;
			case PORT:
				_localctx = new TypePortContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(141); match(PORT);
				}
				break;
			case PORTRANGE:
				_localctx = new TypePortRangeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(142); match(PORTRANGE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 2);
		case 1: return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\35\u0094\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\3\2\3\2\3\2\3\2\5\2\33\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7"+
		"\3&\n\3\f\3\16\3)\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\5\4M\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\5\5[\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6e\n\6\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\5\7m\n\7\3\b\3\b\3\b\3\b\3\b\5\bt\n\b\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\5\t|\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5"+
		"\n\u008c\n\n\3\13\3\13\3\13\3\13\5\13\u0092\n\13\3\13\2\3\4\f\2\4\6\b"+
		"\n\f\16\20\22\24\2\2\u00b2\2\32\3\2\2\2\4\34\3\2\2\2\6L\3\2\2\2\bZ\3\2"+
		"\2\2\nd\3\2\2\2\fl\3\2\2\2\16s\3\2\2\2\20{\3\2\2\2\22\u008b\3\2\2\2\24"+
		"\u0091\3\2\2\2\26\27\5\4\3\2\27\30\7\2\2\3\30\33\3\2\2\2\31\33\7\2\2\3"+
		"\32\26\3\2\2\2\32\31\3\2\2\2\33\3\3\2\2\2\34\35\b\3\1\2\35\36\5\6\4\2"+
		"\36\'\3\2\2\2\37 \f\4\2\2 !\7\23\2\2!&\5\6\4\2\"#\f\3\2\2#$\7\24\2\2$"+
		"&\5\6\4\2%\37\3\2\2\2%\"\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\5\3"+
		"\2\2\2)\'\3\2\2\2*+\7\25\2\2+M\5\6\4\2,-\5\20\t\2-.\5\22\n\2./\5\24\13"+
		"\2/\60\5\b\5\2\60M\3\2\2\2\61\62\5\20\t\2\62\63\5\22\n\2\63\64\5\b\5\2"+
		"\64M\3\2\2\2\65\66\5\20\t\2\66\67\5\24\13\2\678\5\b\5\28M\3\2\2\29:\5"+
		"\22\n\2:;\5\24\13\2;<\5\b\5\2<M\3\2\2\2=>\5\20\t\2>?\5\b\5\2?M\3\2\2\2"+
		"@A\5\22\n\2AB\5\b\5\2BM\3\2\2\2CD\5\24\13\2DE\5\b\5\2EM\3\2\2\2FM\5\b"+
		"\5\2GM\5\20\t\2HI\7\4\2\2IJ\5\4\3\2JK\7\5\2\2KM\3\2\2\2L*\3\2\2\2L,\3"+
		"\2\2\2L\61\3\2\2\2L\65\3\2\2\2L9\3\2\2\2L=\3\2\2\2L@\3\2\2\2LC\3\2\2\2"+
		"LF\3\2\2\2LG\3\2\2\2LH\3\2\2\2M\7\3\2\2\2N[\5\n\6\2O[\5\f\7\2P[\5\16\b"+
		"\2Q[\7\35\2\2RS\7\25\2\2S[\5\n\6\2TU\7\25\2\2U[\5\f\7\2VW\7\25\2\2W[\5"+
		"\16\b\2XY\7\25\2\2Y[\7\35\2\2ZN\3\2\2\2ZO\3\2\2\2ZP\3\2\2\2ZQ\3\2\2\2"+
		"ZR\3\2\2\2ZT\3\2\2\2ZV\3\2\2\2ZX\3\2\2\2[\t\3\2\2\2\\e\7\26\2\2]^\7\27"+
		"\2\2^_\7\16\2\2_e\7\27\2\2`e\7\27\2\2ae\7\32\2\2be\7\31\2\2ce\7\30\2\2"+
		"d\\\3\2\2\2d]\3\2\2\2d`\3\2\2\2da\3\2\2\2db\3\2\2\2dc\3\2\2\2e\13\3\2"+
		"\2\2fm\7\34\2\2gm\7\32\2\2hi\7\4\2\2ij\5\f\7\2jk\7\5\2\2km\3\2\2\2lf\3"+
		"\2\2\2lg\3\2\2\2lh\3\2\2\2m\r\3\2\2\2nt\7\33\2\2op\7\4\2\2pq\5\16\b\2"+
		"qr\7\5\2\2rt\3\2\2\2sn\3\2\2\2so\3\2\2\2t\17\3\2\2\2u|\7\6\2\2v|\7\7\2"+
		"\2w|\7\b\2\2x|\7\t\2\2y|\7\n\2\2z|\7\13\2\2{u\3\2\2\2{v\3\2\2\2{w\3\2"+
		"\2\2{x\3\2\2\2{y\3\2\2\2{z\3\2\2\2|\21\3\2\2\2}~\7\22\2\2~\177\7\24\2"+
		"\2\177\u008c\7\21\2\2\u0080\u0081\7\21\2\2\u0081\u0082\7\24\2\2\u0082"+
		"\u008c\7\22\2\2\u0083\u0084\7\22\2\2\u0084\u0085\7\23\2\2\u0085\u008c"+
		"\7\21\2\2\u0086\u0087\7\21\2\2\u0087\u0088\7\23\2\2\u0088\u008c\7\22\2"+
		"\2\u0089\u008c\7\22\2\2\u008a\u008c\7\21\2\2\u008b}\3\2\2\2\u008b\u0080"+
		"\3\2\2\2\u008b\u0083\3\2\2\2\u008b\u0086\3\2\2\2\u008b\u0089\3\2\2\2\u008b"+
		"\u008a\3\2\2\2\u008c\23\3\2\2\2\u008d\u0092\7\f\2\2\u008e\u0092\7\r\2"+
		"\2\u008f\u0092\7\17\2\2\u0090\u0092\7\20\2\2\u0091\u008d\3\2\2\2\u0091"+
		"\u008e\3\2\2\2\u0091\u008f\3\2\2\2\u0091\u0090\3\2\2\2\u0092\25\3\2\2"+
		"\2\r\32%\'LZdls{\u008b\u0091";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}