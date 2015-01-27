// Generated from /home/linjie/batfish/projects/batfish/src/batfish/grammar/bpf/BPFParser.g4 by ANTLR 4.4

package batfish.grammar.bpf;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BPFParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BPFParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code PortNumRange}
	 * labeled alternative in {@link BPFParser#port_range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortNumRange(@NotNull BPFParser.PortNumRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DirDstOrSrc}
	 * labeled alternative in {@link BPFParser#dqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirDstOrSrc(@NotNull BPFParser.DirDstOrSrcContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Host}
	 * labeled alternative in {@link BPFParser#network_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHost(@NotNull BPFParser.HostContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProtoICMP}
	 * labeled alternative in {@link BPFParser#pqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtoICMP(@NotNull BPFParser.ProtoICMPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PTQualId}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPTQualId(@NotNull BPFParser.PTQualIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PDQualId}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPDQualId(@NotNull BPFParser.PDQualIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Singleton}
	 * labeled alternative in {@link BPFParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleton(@NotNull BPFParser.SingletonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypePortRange}
	 * labeled alternative in {@link BPFParser#tqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePortRange(@NotNull BPFParser.TypePortRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PortName}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortName(@NotNull BPFParser.PortNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DirDst}
	 * labeled alternative in {@link BPFParser#dqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirDst(@NotNull BPFParser.DirDstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProtoIP}
	 * labeled alternative in {@link BPFParser#pqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtoIP(@NotNull BPFParser.ProtoIPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeHost}
	 * labeled alternative in {@link BPFParser#tqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeHost(@NotNull BPFParser.TypeHostContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProtoIGMP}
	 * labeled alternative in {@link BPFParser#pqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtoIGMP(@NotNull BPFParser.ProtoIGMPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SmallPortNum}
	 * labeled alternative in {@link BPFParser#port_num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSmallPortNum(@NotNull BPFParser.SmallPortNumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Net1}
	 * labeled alternative in {@link BPFParser#network_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNet1(@NotNull BPFParser.Net1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code Net2}
	 * labeled alternative in {@link BPFParser#network_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNet2(@NotNull BPFParser.Net2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code Net3}
	 * labeled alternative in {@link BPFParser#network_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNet3(@NotNull BPFParser.Net3Context ctx);
	/**
	 * Visit a parse tree produced by the {@code ProtoSCTP}
	 * labeled alternative in {@link BPFParser#pqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtoSCTP(@NotNull BPFParser.ProtoSCTPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TQualId}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTQualId(@NotNull BPFParser.TQualIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(@NotNull BPFParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PortRange}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortRange(@NotNull BPFParser.PortRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotNetwork}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotNetwork(@NotNull BPFParser.NotNetworkContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PortNum}
	 * labeled alternative in {@link BPFParser#port_num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortNum(@NotNull BPFParser.PortNumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotPortName}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotPortName(@NotNull BPFParser.NotPortNameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Prefix}
	 * labeled alternative in {@link BPFParser#network_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefix(@NotNull BPFParser.PrefixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DirSrc}
	 * labeled alternative in {@link BPFParser#dqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirSrc(@NotNull BPFParser.DirSrcContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Disjunction}
	 * labeled alternative in {@link BPFParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(@NotNull BPFParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Port}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort(@NotNull BPFParser.PortContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProtoTCP}
	 * labeled alternative in {@link BPFParser#pqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtoTCP(@NotNull BPFParser.ProtoTCPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotPortRange}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotPortRange(@NotNull BPFParser.NotPortRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Network}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNetwork(@NotNull BPFParser.NetworkContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdOnly}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdOnly(@NotNull BPFParser.IdOnlyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(@NotNull BPFParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DTQualId}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDTQualId(@NotNull BPFParser.DTQualIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProtoOnly}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtoOnly(@NotNull BPFParser.ProtoOnlyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProtoUDP}
	 * labeled alternative in {@link BPFParser#pqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProtoUDP(@NotNull BPFParser.ProtoUDPContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PDTQualId}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPDTQualId(@NotNull BPFParser.PDTQualIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PQualId}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPQualId(@NotNull BPFParser.PQualIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DirSrcAndDst}
	 * labeled alternative in {@link BPFParser#dqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirSrcAndDst(@NotNull BPFParser.DirSrcAndDstContext ctx);
	/**
	 * Visit a parse tree produced by {@link BPFParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(@NotNull BPFParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenPortNum}
	 * labeled alternative in {@link BPFParser#port_num}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenPortNum(@NotNull BPFParser.ParenPortNumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenPortNumRange}
	 * labeled alternative in {@link BPFParser#port_range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenPortNumRange(@NotNull BPFParser.ParenPortNumRangeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotPort}
	 * labeled alternative in {@link BPFParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotPort(@NotNull BPFParser.NotPortContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeNet}
	 * labeled alternative in {@link BPFParser#tqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNet(@NotNull BPFParser.TypeNetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Conjunction}
	 * labeled alternative in {@link BPFParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(@NotNull BPFParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Masked}
	 * labeled alternative in {@link BPFParser#network_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMasked(@NotNull BPFParser.MaskedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DQualId}
	 * labeled alternative in {@link BPFParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDQualId(@NotNull BPFParser.DQualIdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DirSrcOrDst}
	 * labeled alternative in {@link BPFParser#dqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirSrcOrDst(@NotNull BPFParser.DirSrcOrDstContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DirDstAndSrc}
	 * labeled alternative in {@link BPFParser#dqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDirDstAndSrc(@NotNull BPFParser.DirDstAndSrcContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypePort}
	 * labeled alternative in {@link BPFParser#tqual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypePort(@NotNull BPFParser.TypePortContext ctx);
}