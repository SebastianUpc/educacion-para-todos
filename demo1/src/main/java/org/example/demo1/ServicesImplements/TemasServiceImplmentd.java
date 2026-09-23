package org.example.demo1.ServicesImplements;

import org.example.demo1.Entities.Temas;
import org.example.demo1.Repositories.ITemasRepository;
import org.example.demo1.ServicesInterfaces.ITemasServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TemasServiceImplmentd implements ITemasServices {
    @Autowired
    private ITemasRepository tR;
    @Override
    public List<Temas> list() {
        return tR.findAll();
    }

    @Override
    public Temas insert(Temas temas) {
        return tR.save(temas);
    }

    @Override
    public void update(Temas temas) {
        tR.save(temas)

    }

    @Override
    public void delete(int id) {
        tR.deleteById(id);

    }

    @Override
    public Optional<Temas> listid(int id) {
        return tR.findById(id);
    }
}
