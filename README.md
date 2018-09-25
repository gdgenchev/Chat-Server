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
Some programming aspects:

        1.Multithreading and Swing
        2.Displaying online users in a field
          -Updating when a user logs in/leaves without blocking the GUI
        3.Future: Chat rooms and better UI

![ScreenShot](/image/img.jpg)