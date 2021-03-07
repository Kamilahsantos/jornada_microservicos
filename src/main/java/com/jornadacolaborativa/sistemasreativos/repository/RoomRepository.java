package com.jornadacolaborativa.sistemasreativos.repository;

import com.jornadacolaborativa.sistemasreativos.model.Room;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends ReactiveMongoRepository<Room, String> {
}
