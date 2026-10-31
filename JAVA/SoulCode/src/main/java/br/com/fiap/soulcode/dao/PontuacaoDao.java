package br.com.fiap.soulcode.dao;
import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.model.Pontuacao;
import br.com.fiap.soulcode.model.Usuario;
import java.sql.*;
import java.util.*;

public class PontuacaoDao {
    public void inserir(Pontuacao pontuacao) {
        try(Connection c=ConnectionFactory.abrirConexao()) {
            c.setAutoCommit(false);
            try {
                int usuarioId=pontuacao.getUsuario().getId();
                try(PreparedStatement lock=c.prepareStatement("SELECT ID_PONTUACAO FROM T_SU_PONTUACAO WHERE ID_USUARIO=? FOR UPDATE")) {
                    lock.setInt(1,usuarioId);
                    try(ResultSet rs=lock.executeQuery()) {
                        if(rs.next()) {
                            try(PreparedStatement ps=c.prepareStatement("UPDATE T_SU_PONTUACAO SET QT_PONTOS_ATUAIS=QT_PONTOS_ATUAIS+?,QT_PONTOS_TOTAIS_GANHOS=QT_PONTOS_TOTAIS_GANHOS+?,DT_ULTIMA_ATUALIZACAO=SYSDATE WHERE ID_USUARIO=?")) {
                                ps.setInt(1,pontuacao.getQuantidadePontos());ps.setInt(2,pontuacao.getQuantidadePontos());ps.setInt(3,usuarioId);ps.executeUpdate();
                            }
                        } else {
                            int id=OracleIds.proximo(c,"SEQ_SU_PONTUACAO");
                            try(PreparedStatement ps=c.prepareStatement("INSERT INTO T_SU_PONTUACAO (ID_PONTUACAO,ID_USUARIO,QT_PONTOS_ATUAIS,QT_PONTOS_TOTAIS_GANHOS,DT_ULTIMA_ATUALIZACAO) VALUES (?,?,?,?,SYSDATE)")) {
                                ps.setInt(1,id);ps.setInt(2,usuarioId);ps.setInt(3,pontuacao.getQuantidadePontos());ps.setInt(4,pontuacao.getQuantidadePontos());ps.executeUpdate();
                            }
                            pontuacao.setId(id);
                        }
                    }
                }
                c.commit();
            } catch(Exception e) {c.rollback();throw e;}
        } catch(Exception e) {throw new IllegalStateException("Erro ao registrar pontuação",e);}
    }
    public List<Pontuacao> listar() {
        List<Pontuacao> itens=new ArrayList<>();
        try(Connection c=ConnectionFactory.abrirConexao();PreparedStatement ps=c.prepareStatement(
                "SELECT p.ID_PONTUACAO,p.ID_USUARIO,p.QT_PONTOS_ATUAIS,p.DT_ULTIMA_ATUALIZACAO,u.NM_USUARIO FROM T_SU_PONTUACAO p JOIN T_SU_USUARIO u ON u.ID_USUARIO=p.ID_USUARIO");ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                Usuario u=new Usuario(rs.getInt("ID_USUARIO"),rs.getString("NM_USUARIO"),"","",0,"","",0);
                itens.add(new Pontuacao(rs.getInt("ID_PONTUACAO"),u,rs.getInt("QT_PONTOS_ATUAIS"),rs.getTimestamp("DT_ULTIMA_ATUALIZACAO").toLocalDateTime()));
            }
            return itens;
        } catch(SQLException e) {throw new IllegalStateException("Erro ao listar pontuações",e);}
    }
    public void remover(int id) {
        try(Connection c=ConnectionFactory.abrirConexao();PreparedStatement ps=c.prepareStatement("DELETE FROM T_SU_PONTUACAO WHERE ID_PONTUACAO=?")) {
            ps.setInt(1,id);ps.executeUpdate();
        } catch(SQLException e) {throw new IllegalStateException("Erro ao remover pontuação",e);}
    }
}
