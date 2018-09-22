Socket communication:

    LOGIN username c->s - adds username to list of logged in users
    
    LOGIN 1 s->c - All good
    
    LOGIN 2 s->c - Already logged in
    
    LIST-USERS 1 c->s - request for ALL active users
    
    LIST-USERS 2 chatRoomName c->s - request for active users in chatRoomName
    
    LIST-USERS 0 s->c - start of users list
    
    LIST-USERS 1 s->c - end of users list
    
    SEND 1 from to c->s - indicates sender and receiver
    
    SEND 2 c->c - sending message chunks
    
    SEND 3 c->c finished message
    
    SEND 0 s->c - user is not online
