package kr.co.zeroPie.service.kanban;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.zeroPie.dto.kanban.BoardDTO;
import kr.co.zeroPie.dto.kanban.KanbanDTO;
import kr.co.zeroPie.dto.kanban.KanbanStfDTO;
import kr.co.zeroPie.entity.kanban.Board;
import kr.co.zeroPie.entity.kanban.Kanban;
import kr.co.zeroPie.entity.kanban.KanbanStf;
import kr.co.zeroPie.repository.BoardRepository;
import kr.co.zeroPie.repository.KanbanRepository;
import kr.co.zeroPie.repository.KanbanStfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class KanbanService {

    private final KanbanRepository kanbanRepository;
    private final KanbanStfRepository kanbanStfRepository;
    private final BoardRepository boardRepository;

    public List<KanbanDTO> getKanbanList (String kanbanStf){

        List<KanbanStf> stfList = kanbanStfRepository.findByStfNo(kanbanStf);

        List<KanbanDTO> kanbanDTOList = new ArrayList<>();
        for (KanbanStf stf : stfList) {
            int kanbanId = stf.getKanbanId();
            Optional<Kanban> findKanban = kanbanRepository.findById(kanbanId);
            if (findKanban.isPresent()) {
                Kanban foundKanban = findKanban.get();
                KanbanDTO kanbanDTO = new KanbanDTO();
                kanbanDTO.setKanbanId(foundKanban.getKanbanId());
                kanbanDTO.setKanbanName(foundKanban.getKanbanName());
                kanbanDTO.setKanbanInfo(foundKanban.getKanbanInfo());
                kanbanDTO.setKanbanStf(foundKanban.getKanbanStf());
                kanbanDTOList.add(kanbanDTO);
            }
        }

        log.info("getKanbanList: " + kanbanDTOList);

        return kanbanDTOList;
    }

    public int createKanban(KanbanDTO kanbanDTO){
        Kanban kanban = new Kanban();
        kanban.setKanbanName(kanbanDTO.getKanbanName());
        kanban.setKanbanInfo(kanbanDTO.getKanbanInfo());
        kanban.setKanbanStf(kanbanDTO.getKanbanStf());

        Kanban savedKanban = kanbanRepository.save(kanban);

        return savedKanban.getKanbanId();
    }

    public void createMember(KanbanStfDTO kanbanStfDTO){
        KanbanStf kanbanStf = new KanbanStf();
        kanbanStf.setKanbanId(kanbanStfDTO.getKanbanId());
        kanbanStf.setStfNo(kanbanStfDTO.getStfNo());

        kanbanStfRepository.save(kanbanStf);
    }

    public void createBoard(String boardJson){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BoardDTO boardDTO = objectMapper.readValue(boardJson, BoardDTO.class);
            log.info("Add Board : " + boardDTO.toString());
            // 여기서 boardDTO를 처리

            Board board = new Board();
            board.setId(boardDTO.getId());
            board.setBoardName(boardDTO.getBoardName());
            board.setKanbanId(boardDTO.getKanbanId());

            boardRepository.save(board);

        } catch (JsonProcessingException e) {
            log.error("Error parsing board JSON:", e);
        }
    }
}