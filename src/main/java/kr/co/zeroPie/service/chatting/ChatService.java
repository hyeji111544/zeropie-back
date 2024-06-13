package kr.co.zeroPie.service.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kr.co.zeroPie.dto.*;
import kr.co.zeroPie.entity.ChatRecords;
import kr.co.zeroPie.entity.ChatRoom;
import kr.co.zeroPie.entity.ChatUser;
import kr.co.zeroPie.repository.ChatRecordsRepository;
import kr.co.zeroPie.repository.ChatRoomRepository;
import kr.co.zeroPie.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatRecordsRepository chatRecordsRepository;
    private final Map<String, ChatRoomDTO> chatRooms = new LinkedHashMap<>();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        // 초기화 코드
    }

    public void removeSession(String userId) {
        sessions.remove(userId);
    }

    public List<ChatRoomDTO> findAllRoom() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        return chatRoomList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ChatRoomDTO convertToDto(ChatRoom chatRoom) {
        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .build();
    }

    public ChatRoomDTO findRoomById(String roomId) {
        log.info("여기로 들어옴?...1 - roomId"+roomId);

        if (chatRooms.containsKey(roomId)) {
            return chatRooms.get(roomId);
        }

        Optional<ChatRoom> optRoom = chatRoomRepository.findById(roomId);

        if (optRoom.isPresent()) {
            ChatRoom chatRoom = optRoom.get();

            log.info("여기로 들어옴?...2  - chatRoom "+chatRoom);

            ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                    .roomId(chatRoom.getRoomId())
                    .name(chatRoom.getName())
                    .build();

            chatRooms.put(roomId, chatRoomDTO);  // 캐시에 저장

            return chatRoomDTO;
        } else {
            return null;
        }
    }

    public ChatRoomDTO createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                .roomId(randomId)
                .name(name)
                .build();

        ChatRoom chatRoom = modelMapper.map(chatRoomDTO, ChatRoom.class);
        chatRoomRepository.save(chatRoom);

        chatRooms.put(randomId, chatRoomDTO);

        return chatRoomDTO;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {

        log.info("여기 들어옴?.....7"+session+"/"+message);

        try {//소켓이 끊기더라도 session이 닫히지 않아서
            log.info("try안쪽....");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            log.info("try안쪽 - session.sendMessage()이후....");
        } catch (IOException e) {
            log.error("Error sending message", e);
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException ex) {
                log.error("Error closing session", ex);
            }
        }
    }

    //room에 유저가 들어가 있는지 체크
    public ResponseEntity<?> findUser(ChatUserDTO chatUserDTO){

        log.info("chatService - findUser : "+chatUserDTO.toString());

        int count = chatUserRepository.countByRoomIdAndStfNo(chatUserDTO.getRoomId(),chatUserDTO.getStfNo());

        log.info("chatService - findUser - count : "+count);

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    //룸에 지금 어떤 아이디들이 들어가 있는지 저장
    public void saveUser(String id, String roomId){

        ChatUser chatUser = new ChatUser();

        chatUser.setRoomId(roomId);
        chatUser.setStfNo(id);

        chatUserRepository.save(chatUser);

    }

    //채팅 내용 저장
    public void saveMessage(ChatRecordsDTO chatRecordsDTO){

        log.info("chatService - saveMessage : "+chatRecordsDTO.toString());

        ChatRecords chatRecords = modelMapper.map(chatRecordsDTO, ChatRecords.class);

        chatRecordsRepository.save(chatRecords);

    }



    public void addSession(String sessionId, WebSocketSession session) {
        sessions.put(sessionId, session);
    }

    //세션 아이디 찾기
    public WebSocketSession getSessionById(String sessionId){
        return sessions.get(sessionId);
    }

    //클릭한 룸에서 채팅 내용들고오기
    public List<ChatRecordsDTO> findRoom(String roomId){

        log.info("서비스에서 roomId 확인 여기는 채팅 내용 들고오기 : "+roomId);

        List<ChatRecords> Records= chatRecordsRepository.findByRoomId(roomId);

        log.info("Records : "+Records);

        List<ChatRecordsDTO> chatList = Records.stream()
                .map(entity -> {
                    ChatRecordsDTO dto = modelMapper.map(entity, ChatRecordsDTO.class);
                    return dto;
                })
                .toList();

        return chatList;
    }
}
