package org.subbu.project.messenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.subbu.project.messenger.model.Message;
import org.subbu.project.messenger.service.MessageService;

@Path("/messages")
public class MessageResource {
	

	public MessageResource() {
		// TODO Auto-generated constructor stub
	}
MessageService messageService = new MessageService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getMessages() {
		return messageService.getAllMessages();
	}
	
	@GET
	@Path("/{messageId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Message getMessage(@PathParam("messageId") long id) {	
		return messageService.getMessage(id);
	}
}
