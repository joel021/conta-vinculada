package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.FuncionarioRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    public Funcionario saveIfNotExists(Funcionario funcionario) throws NotAcceptableException {

        if (funcionario == null) {
            throw new NotAcceptableException("O funcionário deve ser definido.");
        }

        Optional<Funcionario> funcionarioOptional;
        if (funcionario.getIdFuncionario() != null) {
            funcionarioOptional = funcionarioRepository.findById(funcionario.getIdFuncionario());
        } else {
            funcionarioOptional = funcionarioRepository.findByMatricula(funcionario.getMatricula());
        }

        if (funcionarioOptional.isPresent()) {
            return funcionarioOptional.get();
        }
        funcionario.setDeletadoPor(null);
        funcionario.setDeletadoEm(null);

        return save(funcionario);
    }

    public Funcionario save(Funcionario funcionario) {
        funcionario.setPessoaFisica(pessoaFisicaService.saveIfNotExists(funcionario.getPessoaFisica()));
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario findByMatriculaOrThrows(String matricula) throws NotFoundException {

        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findByMatricula(matricula);
        if (funcionarioOptional.isPresent()){
            return funcionarioOptional.get();
        }
        throw new NotFoundException("Funcionario com matricula "+matricula+" não encontrado.");
    }

    public Funcionario findById(int id) throws NotFoundException {

        Optional<Funcionario> funcionarioOptional = funcionarioRepository.findById(id);
        if (funcionarioOptional.isPresent()) {
            return funcionarioOptional.get();
        }
        throw new NotFoundException("O funcionário de id "+id+" não foi encontrado.");
    }

    public Funcionario update(Funcionario funcionario) throws NotFoundException {

        Funcionario funcionarioFound = findById(funcionario.getIdFuncionario());
        funcionarioFound.setNivel(funcionario.getNivel());
        funcionarioFound.setMatricula(funcionario.getMatricula());
        funcionarioFound.setRacaCor(funcionario.getRacaCor());

        PessoaFisica pessoaFisica = funcionarioFound.getPessoaFisica();
        pessoaFisica.setNome(funcionario.getPessoaFisica().getNome());
        pessoaFisica.setCpf(funcionario.getPessoaFisica().getCpf());

        funcionarioFound.setPessoaFisica(pessoaFisicaService.update(pessoaFisica));

        return funcionarioRepository.save(funcionarioFound);
    }

    public List<Funcionario> findByCpf(String cpf) {

        return funcionarioRepository.findNonDeleletedByCpfStartsWith(cpf);
    }
}
