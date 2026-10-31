package br.com.fiap.soulcode.dao;
import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.model.Missao;
import java.sql.*;
import java.util.*;

public class MissaoDao {
    public void inserir(Missao m) {
        String sql="INSERT INTO T_SU_MISSAO (ID_MISSAO,ID_TIPO_MISSAO,TL_MISSAO,DS_MISSAO,QT_PONTOS_RECOMPENSA) VALUES (?,?,?,?,?)";
        try(Connection c=ConnectionFactory.abrirConexao(); PreparedStatement ps=c.prepareStatement(sql)) {
            int id=OracleIds.proximo(c,"SEQ_SU_MISSAO");
            // O modelo Java não expõe o tipo de missão; escolhe um tipo existente no banco.
            int tipo;
            try (PreparedStatement tipoStmt=c.prepareStatement("SELECT MIN(ID_TIPO_MISSAO) FROM T_SU_TIPO_MISSAO");
                 ResultSet rs=tipoStmt.executeQuery()) {
                rs.next(); tipo=rs.getInt(1);
                if (rs.wasNull()) throw new IllegalStateException("Cadastre um tipo em T_SU_TIPO_MISSAO antes de inserir missões.");
            }
            ps.setInt(1,id);ps.setInt(2,tipo);ps.setString(3,m.getNome());ps.setString(4,m.getDescricao());ps.setInt(5,m.getPontos());
            ps.executeUpdate();m.setId(id);
        } catch(SQLException e) {throw new IllegalStateException("Erro ao inserir missão",e);}
    }
    public List<Missao> listar() {
        List<Missao> itens=new ArrayList<>();
        try(Connection c=ConnectionFactory.abrirConexao(); PreparedStatement ps=c.prepareStatement(
                "SELECT ID_MISSAO,TL_MISSAO,DS_MISSAO,QT_PONTOS_RECOMPENSA FROM T_SU_MISSAO ORDER BY ID_MISSAO");ResultSet rs=ps.executeQuery()) {
            while(rs.next()) itens.add(new Missao(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),true));
            return itens;
        } catch(SQLException e) {throw new IllegalStateException("Erro ao listar missões",e);}
    }
    public void atualizar(Missao m) {
        try(Connection c=ConnectionFactory.abrirConexao();PreparedStatement ps=c.prepareStatement(
                "UPDATE T_SU_MISSAO SET TL_MISSAO=?,DS_MISSAO=?,QT_PONTOS_RECOMPENSA=? WHERE ID_MISSAO=?")) {
            ps.setString(1,m.getNome());ps.setString(2,m.getDescricao());ps.setInt(3,m.getPontos());ps.setInt(4,m.getId());ps.executeUpdate();
        } catch(SQLException e) {throw new IllegalStateException("Erro ao atualizar missão",e);}
    }
    public void remover(int id) {
        try(Connection c=ConnectionFactory.abrirConexao();PreparedStatement ps=c.prepareStatement("DELETE FROM T_SU_MISSAO WHERE ID_MISSAO=?")) {
            ps.setInt(1,id);ps.executeUpdate();
        } catch(SQLException e) {throw new IllegalStateException("Erro ao remover missão",e);}
    }
}
