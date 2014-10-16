Networked Notifier
======

What is it?
------
A simple networked java application to be able to put up a message on my screen at home for me to read later, since my memory is horrible.

The 'Protocol'
------

The protocol is a very simple text based protocol.

It works like this.

Server -> Client
	
	* "SRV_MSG :" + type

	Where type can be:
		* CONNECTED	(Notify the client that it has successfully connected)
		* SUCCESS 	(Notify the client that its message has been added to the display)
		* ERROR		(Notify the client that its message has not been added to the queue)
		* EXIT		(Notify the client that the server is going down)

Client -> Server
	
	* "CLNT_MSG :" + type
	* msg

	Where type can be:
		* add 		(Tell the server that the client is going to add a message. This is followed up by the client sending the 'msg')
		* echo 		(A simple echo, no real use for this at the moment.)
		* exit 		(Tells the server to exit.)

