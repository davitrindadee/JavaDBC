package br.com.fiap.soulcode.dao;
import br.com.fiap.soulcode.connection.ConnectionFactory;
import br.com.fiap.soulcode.model.Acao;
import br.com.fiap.soulcode.model.Interacao;
import br.com.fiap.soulcode.model.Usuario;
import java.sql.*;
import java.util.*;

public class InteracaoDao {
    public void inserir(Interacao interacao) {
        String sql="INSERT INTO T_SU_ACAO_USUARIO (ID_ACAO,ID_USUARIO,ID_TIPO_ACAO,DS_ACAO,QT_PONTOS_GERADOS,DT_ACAO) VALUES (?,?,?,?,?,?)";
        try(Connection c=ConnectionFactory.abrirConexao();PreparedStatement ps=c.prepareStatement(sql)) {
            int id=OracleIds.proximo(c,"SEQ_SU_ACAO_USUARIO");
            ps.setInt(1,id);ps.setInt(2,interacao.getUsuario().getId());ps.setInt(3,interacao.getAcao().getId());
            ps.setString(4,interacao.getTipo());ps.setInt(5,interacao.getAcao().getPontos());ps.setTimestamp(6,Timestamp.valueOf(interacao.getData()));
            ps.executeUpdate();interacao.setId(id);
        } catch(SQLException e) {throw new IllegalStateException("Erro ao registrar ação do usuário",e);}
    }
    public List<Interacao> listar() {
        List<Interacao> itens=new ArrayList<>();
        String sql="SELECT i.ID_ACAO,i.DS_ACAO,i.DT_ACAO,u.ID_USUARIO,u.NM_USUARIO,t.ID_TIPO_ACAO,t.NM_TIPO_ACAO,t.DS_TIPO_ACAO,t.QT_PONTOS_PADRAO FROM T_SU_ACAO_USUARIO i JOIN T_SU_USUARIO u ON i.ID_USUARIO=u.ID_USUARIO JOIN T_SU_TIPO_ACAO t ON i.ID_TIPO_ACAO=t.ID_TIPO_ACAO";
        try(Connection c=ConnectionFactory.abrirConexao();PreparedStatement ps=c.prepareStatement(sql);ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                Usuario u=new Usuario(rs.getInt("ID_USUARIO"),rs.getString("NM_USUARIO"),"","",0,"","",0);
                Acao a=new Acao(rs.getInt("ID_TIPO_ACAO"),rs.getString("NM_TIPO_ACAO"),rs.getString("DS_TIPO_ACAO"),rs.getInt("QT_PONTOS_PADRAO"),true);
                itens.add(new Interacao(rs.getInt("ID_ACAO"),u,a,rs.getString("DS_ACAO"),rs.getTimestamp("DT_ACAO").toLocalDateTime()));
            }
            return itens;
        } catch(SQLException e) {throw new IllegalStateException("Erro ao listar ações dos usuários",e);}
    }
    public void remover(int id) {
        try(Connection c=ConnectionFactory.abrirConexao();PreparedStatement ps=c.prepareStatement("DELETE FROM T_SU_ACAO_USUARIO WHERE ID_ACAO=?")) {
            ps.setInt(1,id);ps.executeUpdate();
        } catch(SQLException e) {throw new IllegalStateException("Erro ao remover ação do usuário",e);}
    }
}
