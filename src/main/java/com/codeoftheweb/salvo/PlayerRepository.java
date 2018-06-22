package com.codeoftheweb.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
//An interface can’t be instantiated. It can be implemented by a class or extended by another interface.
//in this case, PlayerRepository extends from JpaRepository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    //I don't get this line; what is @Param(" ")?
    Player findByUserName(@Param("userName") String userName);
}
