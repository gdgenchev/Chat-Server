Programming:

    1. GUI with Swing
    2. Info for online users and chat room
       - Updating whenever a client connects/leaves/creates a room/deletes a room.
    3. Info for joined users in a chat room
    4. Support for receiving private messages and group messages at the same time.
       - Background thread(ReceiverBroadcaster) for receiving messages,
         whose task is to broadcast the message
         to all the receivers, which are responsible
         for updating the UI.
    5. Logging with log4j
    6. Chat History for chat rooms
    
  
![ScreenShot](/image/img.png)
