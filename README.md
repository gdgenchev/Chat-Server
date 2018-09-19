LOGIN username c->s - adds username to list of logged in users

LOGIN 1 s->c - All good

LOGIN 2 s->c - Already logged in

LIST-USERS c->s - request for ALL active users

LIST-USERS chatRoomName c->s - request for active users in chatRoomName

LIST-USERS 0 s->c - start of users list

LIST-USERS 1 s->c - end of users list

SEND 1 message c->c - sending message

SEND 2 s->c - user is not online
