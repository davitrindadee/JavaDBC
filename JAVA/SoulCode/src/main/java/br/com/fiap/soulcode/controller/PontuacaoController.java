
package br.com.fiap.soulcode.controller;

import br.com.fiap.soulcode.dao.PontuacaoDao;
import br.com.fiap.soulcode.model.Pontuacao;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontuacoes")
public class PontuacaoController {

    private final PontuacaoDao pontuacaoDao = new PontuacaoDao();

    @GetMapping
    public List<Pontuacao> listar() {
        return pontuacaoDao.listar();
    }

    @PostMapping
    public String inserir(@RequestBody Pontuacao pontuacao) {
        pontuacaoDao.inserir(pontuacao);
        return "Pontuação registrada com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String remover(@PathVariable int id) {
        pontuacaoDao.remover(id);
        return "Pontuação removida com sucesso!";
    }
}
