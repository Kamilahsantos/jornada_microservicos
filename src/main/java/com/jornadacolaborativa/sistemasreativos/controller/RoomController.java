package com.jornadacolaborativa.sistemasreativos.controller;

import com.jornadacolaborativa.sistemasreativos.model.Room;
import com.jornadacolaborativa.sistemasreativos.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class RoomController {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private RoomRepository roomRepository;

    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Room> createReserveRoom ( @RequestBody Room room){
        log.info("Criando uma nova reserva para a sala {}  de {}  ate {} ",
                room.getName(), room.getStartDate(), room.getEndDate());
        return  roomRepository.save(room);
    }

    @GetMapping("/rooms")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Room> getAllRooms (){
        log.info("Listando todas as reservas de sala");
        return  roomRepository.findAll();
    }

    @GetMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Room>> getRoomById (@PathVariable (value = "id") String roomId){
        log.info("Acessando as informacoes da sala {} ", roomId);
        return  roomRepository.findById(roomId)
                .map(savedRoom -> ResponseEntity.ok(savedRoom))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Room>> updateRoom (@PathVariable (value = "id") String roomId,
                                                  @RequestBody Room room){
        log.info("Atualizando a reserva da sala {}  para a data e hora de {}  e {} ", roomId,room.getEndDate(), room.getEndDate());
        return  roomRepository.findById(roomId)
                .flatMap(existingRoom -> {
                    existingRoom.setStartDate(room.getStartDate());
                    existingRoom.setEndDate(room.getEndDate());
                    return  roomRepository.save(existingRoom);

                }).map(updateRoom -> new ResponseEntity<>(updateRoom, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRoom(@PathVariable(value = "id") String roomId) {
log.info("Excluindo reserva da sala {} ", roomId);
        return roomRepository
                .findById(roomId)
                .flatMap(existingRoom ->
                        roomRepository.delete(existingRoom)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }







}