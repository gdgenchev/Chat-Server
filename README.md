Socket communication:

    LOGIN username c->s - adds username to list of logged in users
    LOGIN_SUCCESS s->c - All good
    LOGIN_FAIL s->c - Already logged in
    LIST userNames s->c - update online users
    SEND_PRIVATE from to message c->s - send message
    SEND_PRIVATE_SUCCESS s->c - All good
    SEND_PRIVATE_FAIL s->c - User isn't online
    QUIT username c->s - disconnect user
    QUIT s->c - quit client
    CREATE creator chatRoomName c->s - indicate creation of a chat room
    JOIN username chatRoomName c->s - join chat room
    JOIN_SUCCESS s->c - All good
    JOIN_FAIL s->c - Already joined
    LEAVE_ROOM username chatRoom c->s - Leave chatRoom
    DELETE_ROOM room c->s - Delete room (only available for creator)
    
    
    
Programming:
    
    1. Support receiving private messages and group messages at the same time.
      - Background thread(ReceiverBroadcaster) for receiving messages 
        whose task is to broadcast the message
        to all the receivers, which are responsible
        for updating the UI in the EDT.
  
![ScreenShot](/image/img.jpg)