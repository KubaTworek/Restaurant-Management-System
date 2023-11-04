package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

interface SqlMenuRepository extends JpaRepository<MenuSnapshot, Long> {
    <S extends MenuSnapshot> S save(S entity);

    void deleteById(Long id);
}

interface SqlMenuQueryRepository extends MenuQueryRepository, JpaRepository<MenuSnapshot, Long> {
}

@Repository
class MenuRepositoryImpl implements MenuRepository {

    private final SqlMenuRepository repository;

    MenuRepositoryImpl(final SqlMenuRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return repository.findById(id).map(Menu::restore);
    }

    @Override
    public Menu save(final Menu entity) {
        return Menu.restore(repository.save(entity.getSnapshot()));
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }
}
