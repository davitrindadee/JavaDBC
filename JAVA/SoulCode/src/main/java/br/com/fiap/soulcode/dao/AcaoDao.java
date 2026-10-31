package br.com.fiap.soulcode.dao;
import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.model.Acao;
import java.sql.*;
import java.util.*;

public class AcaoDao {
    public void inserir(Acao a) {
        try (Connection c=ConnectionFactory.abrirConexao(); PreparedStatement ps=c.prepareStatement(
                "INSERT INTO T_SU_TIPO_ACAO (ID_TIPO_ACAO,NM_TIPO_ACAO,DS_TIPO_ACAO,QT_PONTOS_PADRAO) VALUES (?,?,?,?)")) {
            int id=OracleIds.proximo(c,"SEQ_SU_TIPO_ACAO");
            ps.setInt(1,id); ps.setString(2,a.getNome()); ps.setString(3,a.getDescricao()); ps.setInt(4,a.getPontos());
            ps.executeUpdate(); a.setId(id);
        } catch(SQLException e) { throw new IllegalStateException("Erro ao inserir tipo de ação",e); }
    }
    public List<Acao> listar() {
        List<Acao> itens=new ArrayList<>();
        try(Connection c=ConnectionFactory.abrirConexao(); PreparedStatement ps=c.prepareStatement(
                "SELECT ID_TIPO_ACAO,NM_TIPO_ACAO,DS_TIPO_ACAO,QT_PONTOS_PADRAO FROM T_SU_TIPO_ACAO ORDER BY ID_TIPO_ACAO"); ResultSet rs=ps.executeQuery()) {
            while(rs.next()) itens.add(new Acao(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),true));
            return itens;
        } catch(SQLException e) { throw new IllegalStateException("Erro ao listar tipos de ação",e); }
    }
    public void atualizar(Acao a) {
        try(Connection c=ConnectionFactory.abrirConexao(); PreparedStatement ps=c.prepareStatement(
                "UPDATE T_SU_TIPO_ACAO SET NM_TIPO_ACAO=?,DS_TIPO_ACAO=?,QT_PONTOS_PADRAO=? WHERE ID_TIPO_ACAO=?")) {
            ps.setString(1,a.getNome());ps.setString(2,a.getDescricao());ps.setInt(3,a.getPontos());ps.setInt(4,a.getId());ps.executeUpdate();
        } catch(SQLException e) { throw new IllegalStateException("Erro ao atualizar tipo de ação",e); }
    }
    public void remover(int id) {
        try(Connection c=ConnectionFactory.abrirConexao(); PreparedStatement ps=c.prepareStatement("DELETE FROM T_SU_TIPO_ACAO WHERE ID_TIPO_ACAO=?")) {
            ps.setInt(1,id);ps.executeUpdate();
        } catch(SQLException e) { throw new IllegalStateException("Erro ao remover tipo de ação",e); }
    }
}
