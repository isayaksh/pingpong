package org.prography.pingpong.domain.room.service;

import lombok.RequiredArgsConstructor;
import org.prography.pingpong.domain.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public void deleteAll() {
        roomRepository.deleteAll();
    }

}
