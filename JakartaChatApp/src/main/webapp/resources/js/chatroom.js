'use strict'

const hostAddress = "ws://localhost:8080/jakarta-chat-app/chatroom";
let hostWebSocket;
const userName = document.title.match(/\[(.*)\]/)[1];
const outputBox = document.querySelector("#output-box");
const messageTextArea = document.querySelector("#message-text-area");
const sendMessageButton = document.querySelector("#send-message-button");
const onlineUsersContainer = document.querySelector(".online-users-container");


function connect() {
	outputBox.append("Connecting to the server...\n");
	hostWebSocket =  new WebSocket(hostAddress);
	hostWebSocket.onmessage = processReceivedMessage;
	hostWebSocket.onerror = processErrorAndDisconnect;
	hostWebSocket.onopen = function(event) {
		outputBox.append("Successfully connected to the server. Sey hello to the chat!\n");
	};
}

function sendMessage() {
	const type = "chat";
	const text = messageTextArea.value;
	messageTextArea.value = "";
	if(text.trim() == "")
		return;
	const dateObject = new Date();
	//Present the date in the format [hh:mm:ss]
	const date = "[" + 
		dateObject.getHours() + ":" +
		dateObject.getMinutes() + ":"+ 
		dateObject.getSeconds() + "]";
	
	const message = { "type": type, "username": userName, "date": date, "text": text };
	hostWebSocket.send(JSON.stringify(message));
}

function processReceivedMessage(event) {
	const message = JSON.parse(event.data);
	if(message.type == "chat") {
		const userName = message.username;
		const date = message.date;
		const text = message.text;
		const msgToPrint = date + " " + userName + ": " + text; 
		outputBox.append(msgToPrint + "\n");
		outputBox.scrollTop = 999999;
	}else if(message.type == "users") {
		const userList = message.userlist;
		const unsortedList = document.createElement("ul");
		document.querySelector(".online-users-container > ul").remove();
		for(const user of userList) {
			const listElement = document.createElement("li");
			listElement.textContent = user;
			unsortedList.append(listElement);
		}
		
		onlineUsersContainer.append(unsortedList);
	}
}

function processErrorAndDisconnect(event) {
	console.log("An unknown error occurred during the connection to the server: ", event);
	outputBox.append("An unknown error occurred during the connection to the server.\n");
	outputBox.append("Try to refresh the page\n");
	messageTextArea.disabled = true;
	sendMessageButton.disabled = true;
}


sendMessageButton.onclick = sendMessage;

messageTextArea.onkeydown = function(event){
	if(event.keyCode != 13)
		return;
		
	event.preventDefault();
	sendMessage();
};


window.onload = connect;