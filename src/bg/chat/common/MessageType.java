package bg.chat.common;

public enum MessageType {
    LOGIN,
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LIST_USERS,
    LIST_CHAT_ROOMS,
    CREATE,
    CREATE_SUCCESS,
    CREATE_FAIL,
    JOIN,
    JOIN_SUCCESS,
    JOIN_FAIL,
    JOINED_USERS,
    SEND_PRIVATE,
    SEND_PRIVATE_SUCCESS,
    SEND_PRIVATE_FAIL,
    SEND_GROUP,
    LEAVE_ROOM,
    DELETE_ROOM,
    CHAT_ROOM_DELETED,
    QUIT,
}
