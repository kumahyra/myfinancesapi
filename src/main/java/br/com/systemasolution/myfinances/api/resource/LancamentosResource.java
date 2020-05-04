package br.com.systemasolution.myfinances.api.resource;

import br.com.systemasolution.myfinances.api.dto.LancamenosDTO;
import br.com.systemasolution.myfinances.exception.RegraNegocioException;
import br.com.systemasolution.myfinances.model.entity.Lancamentos;
import br.com.systemasolution.myfinances.model.entity.Usuario;
import br.com.systemasolution.myfinances.service.LancamentoService;
import br.com.systemasolution.myfinances.service.UsuarioService;
import br.com.systemasolution.myfinances.shared.enums.StatusLancamento;
import br.com.systemasolution.myfinances.shared.enums.TipoLancamento;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentosResource {

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "mes", required = false) Integer mes,
            @RequestParam(value = "ano", required = false) Integer ano,
            @RequestParam("usuario") Long idUsuario
    ){
        Lancamentos lancamentosFiltros = new Lancamentos();
        lancamentosFiltros.setDescricao(descricao);
        lancamentosFiltros.setMes(mes);
        lancamentosFiltros.setAno(ano);

        Optional<Usuario> usuarioOptional = usuarioService.obterPorId(idUsuario);

        if(!usuarioOptional.isPresent()){
            return ResponseEntity.badRequest().body("Consulta inválida. Usuário não encontrado!");
        }else{
            lancamentosFiltros.setUsuario(usuarioOptional.get());
        }

        List<Lancamentos> lancamentosList = lancamentoService.buscar(lancamentosFiltros);

        return ResponseEntity.ok(lancamentosList);
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamenosDTO lancamenosDTO) {
        try {
            Lancamentos lancamentosSalvo = lancamentoService.salvar(converter(lancamenosDTO));
            return new ResponseEntity(lancamentosSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamenosDTO lancamenosDTO) {
        return lancamentoService.obterPorId(id).map(entity -> {
            try {
                Lancamentos lancamento = converter(lancamenosDTO);
                lancamento.setId(entity.getId());

                lancamentoService.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            } catch (RegraNegocioException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity("Lançamento não econtrado!", HttpStatus.BAD_REQUEST));

    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return lancamentoService.obterPorId(id).map(entity -> {
           lancamentoService.deletar(entity);
           return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity("Lançamento não encontrado!", HttpStatus.BAD_REQUEST));
    }

    private Lancamentos converter(LancamenosDTO lancamenosDTO) {

        Usuario usuario = usuarioService
                .obterPorId(lancamenosDTO.getUsuario())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado!"));

        Lancamentos lancamentos = modelMapper.map(lancamenosDTO, Lancamentos.class);
        lancamentos.setUsuario(usuario);
        lancamentos.setTipo(TipoLancamento.valueOf(lancamenosDTO.getTipo()));
        lancamentos.setStatus(StatusLancamento.valueOf(lancamenosDTO.getStatus()));

        return lancamentos;
    }

}